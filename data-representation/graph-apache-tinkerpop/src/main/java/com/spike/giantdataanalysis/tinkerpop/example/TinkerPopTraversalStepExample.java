package com.spike.giantdataanalysis.tinkerpop.example;

import static com.spike.giantdataanalysis.tinkerpop.support.TinkerPops.explain;
import static com.spike.giantdataanalysis.tinkerpop.support.TinkerPops.introspect;
import static com.spike.giantdataanalysis.tinkerpop.support.TinkerPops.result;
import static com.spike.giantdataanalysis.tinkerpop.support.TinkerPops.sampleMordenGraph;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.Scope;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.TraversalOptionParent.Pick;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import com.google.common.collect.Lists;

/**
 * REF: docs/reference/index.html#traversal
 * @author zhoujiagen
 */
public class TinkerPopTraversalStepExample {

  public static void main(String[] args) {
    try (TinkerGraph graph = sampleMordenGraph();) {
      // try (TinkerGraph graph = sampleCrewGraph();) {

      GraphTraversalSource g = graph.traversal();

      // 通用的步骤
      // general_map(g);
      // general_flatMap(g);
      // general_filter(g);
      // general_sideEffect(g);
      // general_branch(g);

      // 具体的步骤
      // AddEdgeStep(g);
      // AddVertexStep(g);
      // AddPropertyStep(g);
      // AggregateStep(g);
      // AndStep(g);
      // AsStep(g);
      // BarrierStep(g);
      // ByStep(g);
      // CapStep(g);
      // CoalesceStep(g);
      // CountStep(g);
      // ChooseStep(g);
      // CoinStep(g);
      // ConstantStep(g);
      // CyclicPathStep(g);
      // DedupStep(g);
      // DropStep(g);
      // ExplainStep(g);
      // FoldStep(g);
      // GraphStep(g);
      // GroupStep(g);
      // GroupCountStep(g);
      // HasStep(g);
      // InjectStep(g);
      // IsStep(g);
      // LimitStep(g);
      // LocalStep(g);
      // MatchStep(g);
      // MaxStep(g);
      // MeanStep(g);
      // MinStep(g);
      // OptionStep(g);
      // OptionalStep(g);
      // OrStep(g);
      // OrderStep(g);
      // PageRankStep(g);
      // PathStep(g);
      // PeerPressureStep(g);
      // ProfileStep(g);
      // ProjectStep(g);
      // ProgramStep(g);
      // RangeStep(g);
      // RepeatStep(g);
      // SackStep(g);
      // SampleStep(g);
      // SelectStep(g);
      // SimplePathStep(g);
      // StoreStep(g);
      // SubgraphStep(g);
      // SumStep(g);
      // TailStep(g);
      // TimeLimitStep(g);
      // TreeStep(g);
      // UnfoldStep(g);
      // UnionStep(g);
      // ValueMapStep(g);
      // VertexSteps(g);
      WhereStep(g);
    }
  }

  static void general_map(GraphTraversalSource g) {
    // 快捷方法
    result(g.V(1).out().values("name"));

    // map(Function)
    result(g.V(1).out().map(new Function<Traverser<Vertex>, String>() {
      @Override
      public String apply(Traverser<Vertex> t) {
        return t.get().value("name");
      }
    }));

    // map(Traversal)
    result(g.V(1).out().map(__.values("name")));
  }

  @SuppressWarnings("unchecked")
  static void general_flatMap(GraphTraversalSource g) {
    result(g.V());

    // flatMap(Function)
    Long vCnt = g.V().count().next();
    result(g.inject(g.V().next(vCnt.intValue()))//
        .flatMap(new Function<Traverser<List<Vertex>>, Iterator<String>>() {
          @Override
          public Iterator<String> apply(Traverser<List<Vertex>> t) {
            List<String> names = Lists.newArrayList();
            for (Vertex v : t.get()) {
              names.add(v.<String> property("name").value());
            }
            return names.iterator();
          }
        }));

    // flatMap(Traversal)
    result(g.inject(g.V().next(vCnt.intValue()))//
        // 处理List<Vertex>
        .flatMap(__.unfold().values("name")));
  }

  static void general_filter(GraphTraversalSource g) {
    result(g.V().hasLabel("person"));

    // filter(Predicate)
    result(g.V().filter(new Predicate<Traverser<Vertex>>() {
      @Override
      public boolean test(Traverser<Vertex> t) {
        return t.get().label().equals("person");
      }
    }));

    // filter(Traversal)
    result(g.V().filter(__.label().is("person")));
  }

  static void general_sideEffect(GraphTraversalSource g) {
    // sideEffect(Consumer)
    result(g.V().hasLabel("person").sideEffect(new Consumer<Traverser<Vertex>>() {
      @Override
      public void accept(Traverser<Vertex> t) {
        System.out.println("SIDEEFFECT: " + t.get());
      }
    }));

    // sideEffect(Traversal)
    result(g.V()//
        .sideEffect(__.outE().count().store("o"))//
        .sideEffect(__.inE().count().store("i"))//
        .cap("o", "i"));
  }

  static void general_branch(GraphTraversalSource g) {
    // CHOOSE AN UN-EXISTED NAME: marko2

    // branch(Function)
    result(g.V().branch(//
      (Traverser<Vertex> t) -> {
        return t.get().value("name");
      })//
        .option("marko2", __.values("age"))//
        .option(Pick.none, __.values("name")));// alias for none in gremlin shell

    // branch(Traversal)
    result(g.V().branch(__.values("name"))//
        .option("marko2", __.values("age"))//
        .option(Pick.none, __.values("name")));

    // USE choose()
    result(g.V()//
        .choose(__.has("name", "marko2"), __.values("age"), __.values("name")));
  }

  static void AddEdgeStep(GraphTraversalSource g) {
    // 添加边co-developer
    result(g.V(1).as("marko").out("created").in("created").where(P.neq("marko"))//
        .addE("co-developer").from("marko").property("year", 2009));

    // 添加边createdBy
    result(g.V(3, 4, 5).aggregate("startV").has("name", "josh").as("josh")//
        .select("startV").unfold().hasLabel("software")//
        .addE("createdBy").to("josh"));

    result(g.V().as("startV").out("created").addE("createdBy").to("startV")
        .property("acl", "public"));
    // 对照组
    result(g.E().hasLabel("created"));
  }

  static void AddVertexStep(GraphTraversalSource g) {
    result(g.addV("person").property("name", "stephen"));
    result(g.V().values("name"));

    // 创建的节点没有边关联
    result(g.V().outE("knows").addV().property("name", "nothing"));
    result(g.V().has("name", "nothing").bothE());
  }

  static void AddPropertyStep(GraphTraversalSource g) {
    result(g.V(1).property("country", "usa"));
    result(g.V(1).valueMap());

    // 设置节点属性的基数
    result(g.V(1).property(Cardinality.list, "age", 35));
    result(g.V(1).valueMap());

    // 数值与遍历中获取
    // __.outE()是在当前遍历中而不是全局中
    result(g.V(1).as("marko").property("createdWeight", __.outE("created").values("weight").sum(),//
      "acl", "private"));
    result(g.V(1).valueMap());
    // acl是属性createdWeight上的元属性
    result(g.V(1).properties("createdWeight").valueMap());
  }

  static void AggregateStep(GraphTraversalSource g) {
    // marko的协作者创建的marko没有参与的作品
    result(g.V(1).out("created").aggregate("markoProducts").in("created").as("collaborators")
        .out("created").where(P.without("markoProducts")).as("otherProducts")//
        .select("collaborators", "otherProducts").by("name").by("name"));
  }

  static void AndStep(GraphTraversalSource g) {
    // 年龄小于30, 且认识一些人的人
    result(g.V().and(//
      __.outE("knows"), __.values("age").is(P.lt(30))).valueMap());
  }

  static void AsStep(GraphTraversalSource g) {
    // 步骤调节器
    // 谁创建了什么产品
    result(g.V().as("person").out("created").as("product")//
        .select("person", "product").by("name"));

    // 关联多个标签
    // 什么产品由谁创建
    result(g.V().hasLabel("software").as("a", "b", "c")//
        .select("a", "b", "c")//
        .by("name").by("lang").by(__.in("created").values("name").fold()));
  }

  static void BarrierStep(GraphTraversalSource g) {
    // barrier之前的动作必须立即执行
    Consumer<Traverser<Vertex>> consumer = new Consumer<Traverser<Vertex>>() {
      @Override
      public void accept(Traverser<Vertex> t) {
        System.out.print(t.get().value("name") + " ");
      }
    };
    g.V().sideEffect(consumer).sideEffect(consumer).iterate();
    System.out.println();
    g.V().sideEffect(consumer).barrier().sideEffect(consumer).iterate();
    System.out.println();

  }

  static void ByStep(GraphTraversalSource g) {
    // 按出边数量汇总
    result(g.V().group().by(__.outE().count()));
    // 设置单个分组中的取值
    result(g.V().group().by(__.outE().count()).by("name"));
  }

  static void CapStep(GraphTraversalSource g) {
    result(g.V().groupCount("a").by(T.label));
    // 收集副作用
    result(g.V().groupCount("a").by(T.label).cap("a"));

    result(g.V().groupCount("a").by(T.label).groupCount("b").by(__.outE().count()).cap("a", "b"));
  }

  @SuppressWarnings("unchecked")
  static void CoalesceStep(GraphTraversalSource g) {
    // 联合, 返回至少提交一个元素的第一遍历
    result(g.V(1).coalesce(__.outE("knows"), __.outE("created")).inV().path()//
        // 依次按名称/标签获取路径中元素的相应属性值
        .by("name").by(T.label));

    result(g.V(1).coalesce(__.outE("created"), __.outE("knows")).inV().path()//
        .by("name").by(T.label));
  }

  static void CountStep(GraphTraversalSource g) {
    // 计数
    result(g.V().count());
    result(g.V().hasLabel("person").count());
  }

  static void ChooseStep(GraphTraversalSource g) {
    // 实现if/else逻辑
    // 注意: 所有遍历均会输出
    result(g.V().hasLabel("person")
        .choose(__.values("age").is(P.lt(30)), __.values("name"), __.values("age")));

    // 使用choose/option
    result(g.V().hasLabel("person")//
        .choose(__.values("name"))//
        .option("marko", __.label())//
        .option("josh", __.values("name"))//
        .option(Pick.none, __.valueMap()));
  }

  static void CoinStep(GraphTraversalSource g) {
    // 随机过滤遍历者
    result(g.V().coin(0.0d));
    result(g.V().coin(0.5d));// 不一定是一半
    result(g.V().coin(1.0d));
  }

  @SuppressWarnings("unchecked")
  static void ConstantStep(GraphTraversalSource g) {
    // 给遍历者指定常量
    // choose
    result(g.V().choose(__.hasLabel("person"), __.values("name"), __.constant("inhuman")));
    // coalesce
    result(g.V().coalesce(__.hasLabel("person").values("name"), __.constant("inhuman")));
  }

  static void CyclicPathStep(GraphTraversalSource g) {
    // 筛选出循环路径
    result(g.V(1).both().both());
    result(g.V(1).both().both().cyclicPath());
    result(g.V(1).both().both().cyclicPath().path());
  }

  static void DedupStep(GraphTraversalSource g) {
    // 移除遍历流中多次见到的对象
    result(g.V().values("lang"));
    result(g.V().values("lang").dedup());

    // with by
    result(g.V().dedup().by(T.label).valueMap());

    // with labels
    result(g.V().as("person").out("created").as("product").in("created").as("person2")//
        .select("person", "product", "person2"));
    result(g.V().as("person").out("created").as("product").in("created").as("person2")//
        .dedup("person", "product").select("person", "product", "person2"));
  }

  static void DropStep(GraphTraversalSource g) {
    // 删除边/节点/属性
    g.V().outE().drop().iterate();// 调用iterate实施删除动作
    result(g.E());

    g.V().properties("name").drop().iterate();
    result(g.V().valueMap());

    g.V().drop().iterate();
    result(g.V());
  }

  static void ExplainStep(GraphTraversalSource g) {
    // 解释, 可以看到优化策略Strategy
    explain(g.V().hasLabel("person").outE().identity().inV().count().is(P.gt(5)));
  }

  static void FoldStep(GraphTraversalSource g) {
    // 使用barrier聚合遍历流中所有对象
    result(g.V(1).out("knows").values("name").fold());

    // 计算年龄之和
    // t, u, r
    BiFunction<Integer, Integer, Integer> foldFunction =
        new BiFunction<Integer, Integer, Integer>() {
          @Override
          public Integer apply(Integer t, Integer u) {
            return t + u;
          }
        };
    result(g.V().<Integer> values("age").<Integer> fold(new Integer(0), foldFunction));
    result(g.V().values("age").sum());
  }

  static void GraphStep(GraphTraversalSource g) {
    // 3
    result(g.V().has("name", P.within("marko", "vadas", "josh")));
    // 2
    result(g.V().has("name", P.within("lop", "ripple")));
    // 添加边: 3 * 2
    result(g.V().has("name", P.within("marko", "vadas", "josh")).as("person")//
        // 在遍历中使用V()
        .V().has("name", P.within("lop", "ripple")).addE("uses").from("person"));
  }

  static void GroupStep(GraphTraversalSource g) {
    result(g.V().group().by(T.label));
    // key-projection, value-projection
    result(g.V().group().by(T.label).by("name"));
    result(g.V().group().by(T.label).by(__.count()));
  }

  static void GroupCountStep(GraphTraversalSource g) {
    result(g.V().hasLabel("person").values("age").groupCount());
    // or
    result(g.V().hasLabel("person").groupCount().by("age"));
  }

  static void HasStep(GraphTraversalSource g) {
    result(g.V().hasLabel("person").valueMap());
    result(g.V().has("age", P.outside(20, 30)).valueMap());
  }

  static void InjectStep(GraphTraversalSource g) {
    result(g.V(1).out("knows").values("name").inject("self"));
    result(__.inject(1, 2, 3));
  }

  static void IsStep(GraphTraversalSource g) {
    result(g.V().values("age").is(P.outside(20, 30)));
  }

  static void LimitStep(GraphTraversalSource g) {
    result(g.V().values("name"));
    result(g.V().values("name").limit(2));
    result(g.V().values("name").range(0, 2));

    // local: work with incoming collection
    result(g.V().valueMap());
    result(g.V().valueMap().limit(1));
    result(g.V().valueMap().limit(Scope.local, 1));
  }

  static void LocalStep(GraphTraversalSource g) {
    // USE sampleCrewGraph
    result(g.V().as("person")//
        .properties("location").order().by("startTime", Order.incr).limit(2).as("location")//
        .select("person", "location").by("name").by());

    result(g.V().as("person")
        //
        .local(__.properties("location").order().by("startTime", Order.incr).limit(2))
        .as("location")//
        .select("person", "location").by("name").by());
  }

  /**
   * <pre>
   * match()支持模式匹配和嵌套使用
   * 
   * 相较于SPARQL
   * (1) 支持任意复杂的模式而不仅限于triple模式;
   * (2) 递归支持, 支持模式中包含基于分支的步骤, 包括repeat()
   * (3) 命令式和声明式混合, 在match()之前和之后可以使用传统的Gremlin遍历
   * 
   * 3种类型的match()遍历模式, 其中a,b为声明的变量
   * (1) as(a)...as(b) 
   * (2) as(a)...
   * (3) ...
   * 约束:
   * 如果变量在遍历模式的头部, 则它必须在遍历者的路径历史中作为label存在, 否则遍历者无法通过这一路径.(第一条遍历模式除外!)
   * 如果变量在遍历模式的尾部, 则
   *  (a) 如果该变量在遍历者的路径历史中存在, 则遍历者的当前路径必须匹配(相等于)在同一label上的历史位置;
   *  (b) 如果该变量在遍历者的路径历史中不存在, 则当前位置以该变量为label, 从而该变量成为后续遍历模式的绑定变量.
   * 
   * 如果遍历模式尾部没有label, 则遍历者简单的通过该模式, 进入下一条模式.
   * 
   * 如果遍历模式头部没有label, 则遍历者可以在任意点进入该路径, 但仅在遍历者历史中执行且只执行一次遍历模式时通过该模式一次.
   * 一般遍历模式头部和尾部均没有label, 则遍历模式与and(),or(), where()一起使用.
   * 
   * 一旦遍历者通过所有模式, match()步骤分析遍历者的路径历史, 生成变量绑定值Map[String, Object]传递给遍历中的下一步骤.
   * </pre>
   * @param g
   */
  static void MatchStep(GraphTraversalSource g) {
    // 谁创建了lop, 谁年龄为29也创建了lop, 返回这两个人名
    result(g
        .V()
        .match(
          //
          __.as("person").out("created").has("name", "lop").as("lop"),
          __.as("lop").in("created").has("age", 29).as("person2")//
        ).select("person", "person2").by("name").path());
  }

  static void MaxStep(GraphTraversalSource g) {
    result(g.V().values("age").max());
  }

  static void MeanStep(GraphTraversalSource g) {
    result(g.V().values("age").mean());
  }

  static void MinStep(GraphTraversalSource g) {
    result(g.V().values("age").min());
  }

  /**
   * @param g
   * @see #general_branch(GraphTraversalSource)
   * @see #ChooseStep(GraphTraversalSource)
   */
  static void OptionStep(GraphTraversalSource g) {
  }

  static void OptionalStep(GraphTraversalSource g) {
    // 如果指定的遍历返回结果则返回该结果, 否则返回调用的对象
    result(g.V(2).optional(__.out("knows")));// 返回调用的对象v2
    result(g.V(2).optional(__.in("knows")));// 返回指定遍历的结果
  }

  static void OrStep(GraphTraversalSource g) {
    result(g.V().or(//
      __.outE("created"),//
      __.inE("created").count().is(P.gt(1))).values("name"));
  }

  static void OrderStep(GraphTraversalSource g) {
    result(g.V().values("name").order());// 默认升序
    result(g.V().values("name").order().by(Order.decr));
    result(g.V().order().by("name").values("name"));

    // local: 本地对象排序, 而不是整个遍历流排序
    result(g.V().values("age").fold().order(Scope.local).by(Order.decr));
  }

  static void PageRankStep(GraphTraversalSource g) {
    throw new UnsupportedOperationException();
  }

  /**
   * <pre>
   * 遍历者的历史通过path()步骤查看
   * 
   * Path数据结构是对象的顺序列表, 每个对象关联一个laebl的集合Set[String]
   * </pre>
   * @param g
   */
  static void PathStep(GraphTraversalSource g) {
    result(g.V().out().out().values("name"));
    result(g.V().out().out().values("name").path());

    // 显示路径中元素的属性: name->age->name
    result(g.V().out().out().path().by("name").by("age"));

    // 查看路径数据结构
    Path path = g.V(1).as("a").has("name").as("b")//
        .out("knows").out("created").as("c")//
        .has("name", "ripple").values("name").as("d")//
        .identity().as("e").path().next();
    introspect(path);
  }

  static void PeerPressureStep(GraphTraversalSource g) {
    throw new UnsupportedOperationException();
  }

  static void ProfileStep(GraphTraversalSource g) {
    result(//
    g.V().out("created").repeat(__.both()).times(3).hasLabel("person").values("age").sum()
        .profile());
  }

  static void ProjectStep(GraphTraversalSource g) {
    result(g.V().out("created"));
    // 将当前对象转换为Map[String, Object]
    result(g.V().out("created")//
        .project("a", "b").by("name").by(__.in("created").count()));
    result(g.V().out("created")//
        .project("a", "b").by("name").by(__.in("created").count())//
        .order().by(__.select("b"), Order.incr)//
        .select("a", "b"));
  }

  static void ProgramStep(GraphTraversalSource g) {
    throw new UnsupportedOperationException();
  }

  static void RangeStep(GraphTraversalSource g) {
    result(g.V().range(0, 3));

    // local: with incoming collection
    result(g.V().as("a").out().as("b").in().as("c").select("a", "b", "c").by("name"));
    result(g.V().as("a").out().as("b").in().as("c").select("a", "b", "c").by("name")//
        .range(Scope.local, 1, 2));
  }

  static void RepeatStep(GraphTraversalSource g) {
    // do while
    result(g.V(1).repeat(__.out()).times(2).path().by("name"));

    // while do
    result(g.V(1).until(__.has("name", "ripple")).repeat(__.out()).path().by("name"));
    result(g.V(1).repeat(__.out()).until(__.has("name", "ripple")).path().by("name"));

    // emit谓词
    // repeat -> emit
    result(g.V(1).repeat(__.out()).times(2).emit().path().by("name"));
    // emit -> repeat: see [marko]
    result(g.V(1).emit().repeat(__.out()).times(2).path().by("name"));
    // with predicate
    result(g.V(1).repeat(__.out()).times(2).emit(__.has("lang")).path().by("name"));
  }

  /**
   * sack: 遍历者的局部数据结构
   * @param g
   * @see GraphTraversalSource#withSack(Object)
   * @see GraphTraversalSource#withSack(Object, java.util.function.UnaryOperator,
   *      java.util.function.BinaryOperator)
   * @see org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal#sack()
   * @see org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal#sack(BiFunction)
   */
  static void SackStep(GraphTraversalSource g) {
    result(g.withSack(1.0f).V().sack());

    // work with java.util.function.Supplier
    final Random random = new Random(System.currentTimeMillis());
    result(g.withSack(new Supplier<Float>() {
      @Override
      public Float get() {
        return random.nextFloat();
      }
    }).V().sack());
  }

  static void SampleStep(GraphTraversalSource g) {
    // 在遍历中采样遍历者
    result(g.V().outE().sample(1).values("weight"));
    result(g.V().outE().sample(1).by("weight").values("weight"));
  }

  static void SelectStep(GraphTraversalSource g) {
    result(g.V().as("a").out().as("b").out().as("c"));
    result(g.V().as("a").out().as("b").out().as("c").select("a", "b", "c"));
    result(g.V().as("a").out().as("b").out().as("c").select("a", "b"));
    result(g.V().as("a").out().as("b").out().as("c").select("a", "b", "c").by("name"));

    // work with where()
    result(g.V().as("a").out("created").in("created").as("b")//
        .select("a", "b").by("name"));
    result(g.V().as("a").out("created").in("created").as("b")//
        .select("a", "b").by("name").where("a", P.neq("b")));
  }

  static void SimplePathStep(GraphTraversalSource g) {
    // 保证遍历者不在图中重复其遍历路径
    result(g.V(1).both().both().path());
    result(g.V(1).both().both().simplePath().path());
  }

  static void StoreStep(GraphTraversalSource g) {
    // lazy aggregation
    result(g.V().aggregate("v").limit(1).cap("v"));
    result(g.V().store("v").limit(1).cap("v")); // 2 result

    result(g.E().store("e").by("weight").cap("e"));
  }

  static void SubgraphStep(GraphTraversalSource g) {
    // 从任意遍历中生成边包含的子图
    TinkerGraph subGraph =
        g.E().hasLabel("knows").subgraph("subGraph").<TinkerGraph> cap("subGraph").next();

    result(subGraph.traversal().E().label());
    result(subGraph.traversal().V().values("name"));
  }

  static void SumStep(GraphTraversalSource g) {
    result(g.V().values("age").sum());
  }

  static void TailStep(GraphTraversalSource g) {
    // like limit(), but emits last n objects
    result(g.V().limit(2));
    result(g.V().tail(2));
  }

  static void TimeLimitStep(GraphTraversalSource g) {
    // {v[1]=2744208, v[2]=1136688, v[3]=2744208, v[4]=2744208, v[5]=1136688, v[6]=1136688}
    result(g.V().repeat(__.both().groupCount("m")).times(16).cap("m"));

    // {v[1]=2611264, v[2]=1081276, v[3]=2610249, v[4]=2611264, v[5]=1081276, v[6]=1081892}
    result(g.V().repeat(__.timeLimit(1).both().groupCount("m")).times(16).cap("m"));
  }

  static void TreeStep(GraphTraversalSource g) {
    // HashMap<T, Tree<T>>
    Tree<?> tree = g.V().out().out().tree().by("name").next();

    result(tree);
  }

  @SuppressWarnings("unchecked")
  static void UnfoldStep(GraphTraversalSource g) {
    result(g.V().out().values("name").fold()
        .inject(Lists.newArrayList("Gremlin", Lists.newArrayList("1", "2")))//
        .unfold());
  }

  @SuppressWarnings("unchecked")
  static void UnionStep(GraphTraversalSource g) {
    // a branch
    result(g.V(4).union(//
      __.in().values("age"),//
      __.out().values("name")));

    result(g.V(4).union(//
      __.in().values("age"),//
      __.out().values("name")).path());
  }

  static void ValueMapStep(GraphTraversalSource g) {
    // 元素属性的Map表示
    result(g.V().valueMap());
    result(g.V().has("name", "marko").properties());
    result(g.V().has("name", "marko").properties().valueMap()); // 元属性
  }

  static void VertexSteps(GraphTraversalSource g) {
    // out(), in(), both(), *E(), otherV()
    result(g.V());
  }

  static void WhereStep(GraphTraversalSource g) {
    result(g.V().as("a").out("created").in("created").as("b")//
        .select("a", "b").by("name").where("a", P.neq("b")));
  }
}

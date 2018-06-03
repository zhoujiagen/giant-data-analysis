package com.spike.giantdataanalysis.akka


package object commons {

  /**
    * Akka工具类.
    */
  object Akkas {

    import java.util.concurrent.{Executors, TimeUnit}
    import akka.actor.{Actor, ActorContext, ActorPath, ActorRef, ActorSelection, ActorSystem, Deploy, Props, Terminated}

    import scala.collection.immutable
    import scala.concurrent.{ExecutionContextExecutor, Future}
    import scala.reflect.ClassTag

    //---------------------------------------------------------------------------
    // Implicits and Constants
    //---------------------------------------------------------------------------
    /** 默认的ExecutionContextExecutor */
    implicit val defaultECE = scala.concurrent.ExecutionContext.Implicits.global

    /** 默认的ExecutorService, 大小与CPU数相同. */
    implicit val defaultES = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())

    //---------------------------------------------------------------------------
    // Actor System
    //---------------------------------------------------------------------------
    /**
      * 创建[[ActorSystem]].
      *
      * @param name   ActorSystem名称
      * @param config ActorSystem的配置
      * @return [[ActorSystem]]
      */
    def actorSystem(name: String,
                    config: com.typesafe.config.Config = com.typesafe.config.ConfigFactory.empty()): ActorSystem =
      ActorSystem(name, com.typesafe.config.ConfigFactory.empty())

    /**
      * 创建应用默认的[[ActorSystem]], 使用application.conf中的配置.
      *
      * @param name ActorSystem名称
      * @return [[ActorSystem]]
      * @see com.typesafe.config.ConfigFactory#parseResourcesAnySyntax(java.lang.String, com.typesafe.config.ConfigParseOptions)
      */
    def applicationActorSystem(name: String): ActorSystem =
      ActorSystem(name)

    def terminate(actorSystem: ActorSystem): Future[Terminated] = actorSystem.terminate()

    case class ActorSystemContent(
                                   log: akka.event.LoggingAdapter,
                                   deadLetters: ActorRef,
                                   eventStream: akka.event.EventStream,
                                   sheduler: akka.actor.Scheduler,
                                   dispatcher: ExecutionContextExecutor
                                 )

    def contentOf(actorSystem: ActorSystem): ActorSystemContent =
      new ActorSystemContent(
        actorSystem.log,
        actorSystem.deadLetters,
        actorSystem.eventStream,
        actorSystem.scheduler,
        actorSystem.dispatcher)

    //---------------------------------------------------------------------------
    // Props
    //---------------------------------------------------------------------------
    /**
      * @return 空的[[Props]]
      */
    def props(): Props = Props.empty

    /**
      * 创建带默认构造器的自定义Actor.
      *
      * @tparam T 自定义Actor类
      * @return
      */
    def props[T <: Actor : ClassTag]() = Props[T]

    /**
      * 创建带默认构造器的自定义即时Actor. 例: `Props(new Actor with Stash { ... })`.
      *
      * @param creator
      * @tparam T
      * @return
      */
    def props[T <: Actor : ClassTag](creator: ⇒ T) = Props[T] {
      creator
    }

    /**
      * 创建带构造器参数的Actor.
      *
      * @param clazz 自定义Actor类
      * @param args  构造器参数
      * @return
      */
    def props(clazz: Class[_], args: Any*): Props = Props(clazz, args)

    /**
      * 附加Dispatcher配置.
      *
      * @param props
      * @param d
      * @return
      */
    def propsWithDispatcher(props: Props, d: String): Props = props.withDispatcher(d)

    /**
      * 附加Router配置.
      *
      * @param props
      * @param r
      * @return
      */
    def propsWithRouter(props: Props, r: akka.routing.RouterConfig): Props = props.withRouter(r)

    /**
      * 附加Mailbox配置.
      *
      * @param props
      * @param m
      * @return
      */
    def propsWithMailbox(props: Props, m: String): Props = props.withMailbox(m)

    /**
      * 附加Deploy配置.
      *
      * @param props
      * @param d
      * @return
      */
    def withDeploy(props: Props, d: Deploy): Props = props.withDeploy(d)

    //---------------------------------------------------------------------------
    // Actor
    //---------------------------------------------------------------------------
    /**
      * 在`/user`下创建Actor.
      *
      * @param actorSystem
      * @param props Actor的属性
      * @param name  Actor的名称
      * @return [[ActorRef]]
      * @see [[akka.actor.Props]]
      *
      */
    def actorOf(actorSystem: ActorSystem, props: Props, name: String): ActorRef =
      actorSystem.actorOf(props, name)

    def actorSelection(actorSystem: ActorSystem, path: String): ActorSelection =
      actorSystem.actorSelection(path)

    def actorSelection(actorSystem: ActorSystem, path: ActorPath): ActorSelection =
      actorSystem.actorSelection(path)

    def stop(actorSystem: ActorSystem, actorRef: ActorRef): Unit =
      actorSystem.stop(actorRef)


    //---------------------------------------------------------------------------
    // ActorContext
    //---------------------------------------------------------------------------
    def context(actor: Actor): ActorContext = actor.context

    def actor(actorContext: ActorContext, props: Props): ActorRef =
      actorContext.actorOf(props)

    def actorOf(actorContext: ActorContext, props: Props, name: String): ActorRef =
      actorContext.actorOf(props, name)

    def actorSelection(actorContext: ActorContext, path: String): Any =
      actorContext.actorSelection(path)

    def actorSelection(actorContext: ActorContext, path: ActorPath): Any =
      actorContext.actorSelection(path)

    def stop(actorContext: ActorContext, actorRef: ActorRef): Unit =
      actorContext.stop(actorRef)

    def become(actorContext: ActorContext, behavior: Actor.Receive, discardOld: Boolean = true): Unit =
      actorContext.become(behavior, discardOld)

    /** ref [[ActorContext.become()]] when discardOld is false */
    def unbecome(actorContext: ActorContext): Unit =
      actorContext.unbecome()

    def parent(actorContext: ActorContext): ActorRef =
      actorContext.parent

    def children(actorContext: ActorContext): immutable.Iterable[ActorRef] =
      actorContext.children

    def child(actorContext: ActorContext, name: String): Any =
      actorContext.child(name)

    def system(actorContext: ActorContext): ActorSystem =
      actorContext.system

    def props(actorContext: ActorContext): Props =
      actorContext.props

    //---------------------------------------------------------------------------
    // Timeout and Duration
    //---------------------------------------------------------------------------

    /**
      * 超时时间.
      *
      * @param length 量值
      * @param unit   单位
      * @return [[akka.util.Timeout]]
      */
    def timeout(length: Long, unit: scala.concurrent.duration.TimeUnit): akka.util.Timeout = {
      import scala.concurrent.duration._

      (length, unit) match {
        case (n, TimeUnit.NANOSECONDS) => n nanos
        case (n, TimeUnit.MICROSECONDS) => n microseconds
        case (n, TimeUnit.MILLISECONDS) => n milliseconds
        case (n, TimeUnit.SECONDS) => n seconds
        case (n, TimeUnit.MINUTES) => n minutes
        case (n, TimeUnit.HOURS) => n hours
        case (n, TimeUnit.DAYS) => n days
      }
    }

    /**
      * 时间区间.
      *
      * @param length 量值
      * @param unit   单位
      * @return [[scala.concurrent.duration.Duration]]
      */
    def duration(length: Long, unit: scala.concurrent.duration.TimeUnit): scala.concurrent.duration.Duration = {
      import scala.concurrent.duration._

      (length, unit) match {
        case (n, TimeUnit.NANOSECONDS) => n nanos
        case (n, TimeUnit.MICROSECONDS) => n microseconds
        case (n, TimeUnit.MILLISECONDS) => n milliseconds
        case (n, TimeUnit.SECONDS) => n seconds
        case (n, TimeUnit.MINUTES) => n minutes
        case (n, TimeUnit.HOURS) => n hours
        case (n, TimeUnit.DAYS) => n days
      }
    }


  }


}

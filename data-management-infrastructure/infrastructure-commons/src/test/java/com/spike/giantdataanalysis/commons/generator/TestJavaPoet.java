package com.spike.giantdataanalysis.commons.generator;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Modifier;

import org.junit.Test;

import com.spike.giantdataanalysis.commons.annotation.ReferenceWebUrl;
import com.spike.giantdataanalysis.commons.lang.StringUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * <pre>
 * JavaPoet的Spike单元测试，直接写至控制台。
 * 
 * 是生成.java源文件的Java API。
 * </pre>
 * 
 * @see TypeSpec#annotationBuilder(String)
 * @see TypeSpec#classBuilder(String)
 * @see TypeSpec#anonymousClassBuilder(String, Object...)
 * @see MethodSpec#methodBuilder(String)
 * @see ParameterSpec
 * @see FieldSpec
 * @see AnnotationSpec
 * @see CodeBlock
 * @see ClassName
 * @see TypeName
 * @see ParameterizedTypeName
 * @see JavaFile
 * @author zhoujiagen
 */
@ReferenceWebUrl(title = "javapoet", url = "https://github.com/square/javapoet")
public class TestJavaPoet {

  /** 默认的包名 */
  private static String DEFAULT_PACKAGE = "com.example";

  /** 未知类的包名 */
  private static String SPIKE_PACKAGE = "com.spike";

  /**
   * <pre>
   * 生成
   * <code>
   * package com.example.helloworld;
   * 
   * public final class HelloWorld {
   *   public static void main(String[] args) {
   *     System.out.println("Hello, JavaPoet!");
   *   }
   * }
   * </code>
   * </pre>
   * 
   * @throws IOException
   */
  @Test
  public void _00_example() throws IOException {
    // 方法描述
    MethodSpec mainMethodSpec = MethodSpec.methodBuilder("main")//
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)//
        .returns(void.class)//
        .addParameter(String[].class, "args")//
        .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet")//
        .build();

    // 类型描述
    TypeSpec helloWorldTypeSpec = TypeSpec.classBuilder("HelloWorld")//
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)//
        .addMethod(mainMethodSpec)//
        .build();

    // 生成文件
    JavaFile javaFile = JavaFile//
        .builder(DEFAULT_PACKAGE + ".helloworld", helloWorldTypeSpec)//
        .build();

    javaFile.writeTo(System.out);
  }

  @Test
  public void _01_code_and_control_flow() {

    MethodSpec mainMethodSpec = null;

    // 方法1
    StringBuilder sb = new StringBuilder();
    sb.append("int total = 0;").append(StringUtils.NEWLINE);
    sb.append("for(int i =0; i < 10; i++) {").append(StringUtils.NEWLINE);
    sb.append("	total += i;").append(StringUtils.NEWLINE);
    sb.append("}").append(StringUtils.NEWLINE);

    mainMethodSpec = MethodSpec.methodBuilder("main")//
        .addCode(sb.toString()) //
        .build();

    // 直接打印
    System.out.println(mainMethodSpec.toString());

    // 方法2
    CodeBlock codeBlock = CodeBlock.builder()//
        .addStatement("int total = 0")//
        .beginControlFlow("for (int i = 0; i < 10; i++)")//
        .addStatement("total += i")//
        .endControlFlow()//
        .build();

    mainMethodSpec = MethodSpec.methodBuilder("main").addCode(codeBlock).build();
    System.out.println(mainMethodSpec.toString());

    // 加些动态参数
    System.out.println(this.computeRange("multiply10to20", 10, 20, "*"));
  }

  private MethodSpec computeRange(String methodName, int from, int to, String op) {
    return MethodSpec.methodBuilder(methodName)//
        .returns(int.class)//
        .addStatement("int result = 0")//
        .beginControlFlow("for (int i = " + from + "; i < " + to + "; i++)")//
        .addStatement("result = result " + op + " i")//
        .endControlFlow()//
        .addStatement("return result")//
        .build();
  }

  /** 字面量$L */
  @Test
  public void _02_literal() {

    String name = "main";
    int from = 0;
    int to = 10;
    String op = "+";

    MethodSpec methodSpec = MethodSpec.methodBuilder(name)//
        .returns(int.class)//
        .addStatement("int result = 0")//
        .beginControlFlow("for (int i = $L; i < $L; i++)", from, to)//
        .addStatement("result = result $L i", op)//
        .endControlFlow()//
        .addStatement("return result")//
        .build();

    System.out.println(methodSpec.toString());
  }

  /**
   * <pre>
   * 字符串$S 
   * 
   * 生成
   * <code>
   * public final class HelloWorld {
   *   String slimShady() {
   *     return "slimShady";
   *   }
   * 
   *   String eminem() {
   *     return "eminem";
   *   }
   * 
   *   String marshallMathers() {
   *     return "marshallMathers";
   *   }
   * }
   * </code>
   * </pre>
   */
  @Test
  public void _03_strings() {
    TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")//
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)//
        .addMethod(this.whatsMyName("slimShady"))//
        .addMethod(this.whatsMyName("eminem"))//
        .addMethod(this.whatsMyName("marshalMathers"))//
        .build();

    System.out.println(helloWorld.toString());
  }

  private MethodSpec whatsMyName(String methodName) {
    return MethodSpec.methodBuilder(methodName)//
        .returns(String.class)//
        .addStatement("return $S", methodName)//
        .build();
  }

  /**
   * 类型$T
   * @throws IOException
   */
  @Test
  public void _04_types() throws IOException {
    // 1 使用已经存在的类
    MethodSpec today = MethodSpec.methodBuilder("today")//
        .returns(Date.class)//
        .addStatement("return new $T()", Date.class)//
        .build();

    TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")//
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)//
        .addMethod(today)//
        .build();

    JavaFile javaFile = JavaFile.builder(DEFAULT_PACKAGE + ".helloworld", helloWorld).build();

    javaFile.writeTo(System.out);

    // 2 使用不存在的类
    ClassName hoverboard = ClassName.get(SPIKE_PACKAGE, "Hoverboard");
    MethodSpec tomorrow = MethodSpec.methodBuilder("tomorrow")//
        .returns(hoverboard)//
        .addStatement("return new $T()", hoverboard)//
        .build();
    System.out.println(tomorrow.toString());

    // 3 关于ClassName的更多示例
    this.classNameUsage();

    // 4 静态导入
    this.importStatics();
  }

  /**
   * <pre>
   * {@link ClassName}的使用
   * 
   * 生成代码
   * <code>
   * package com.example.helloworld;
   * 
   * import com.mattel.Hoverboard;
   * import java.util.ArrayList;
   * import java.util.List;
   * 
   * public final class HelloWorld {
   *   List<Hoverboard> beyong() {
   *     List<Hoverboard> result = new ArrayList<>;
   *     result.add(new Hoverboard());
   *     result.add(new Hoverboard());
   *     result.add(new Hoverboard());
   *     return result;
   *   }
   * }
   * </code>
   * &#64;see ClassName
   * &#64;see TypeName
   * </pre>
   *
   * @throws IOException
   */
  private void classNameUsage() throws IOException {
    ClassName hoverboard = ClassName.get(SPIKE_PACKAGE, "Hoverboard");
    ClassName list = ClassName.get("java.util", "List");
    ClassName arrayList = ClassName.get("java.util", "ArrayList");
    // 泛型
    TypeName listOfHoverboards = ParameterizedTypeName.get(list, hoverboard);

    MethodSpec beyond = MethodSpec.methodBuilder("beyong")//
        .returns(listOfHoverboards)//
        .addStatement("$T result = new $T<>", listOfHoverboards, arrayList)//
        .addStatement("result.add(new $T())", hoverboard)//
        .addStatement("result.add(new $T())", hoverboard)//
        .addStatement("result.add(new $T())", hoverboard)//
        .addStatement("return result")//
        .build();

    TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")//
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)//
        .addMethod(beyond)//
        .build();

    JavaFile javaFile = JavaFile.builder(DEFAULT_PACKAGE + ".helloworld", helloWorld).build();

    javaFile.writeTo(System.out);
  }

  /**
   * <pre>
   * 生成代码
   * <code>
   * package com.example.helloworld;
   * 
   * import static com.mattel.Hoverboard.Boards.*;
   * import static com.mattel.Hoverboard.createNimbus;
   * import static java.util.Collections.*;
   * 
   * import com.mattel.Hoverboard;
   * import java.util.ArrayList;
   * import java.util.List;
   * 
   * class HelloWorld {
   *   List<Hoverboard> beyond() {
   *     List<Hoverboard> result = new ArrayList<>();
   *     result.add(createNimbus(2000));
   *     result.add(createNimbus("2001"));
   *     result.add(createNimbus(THUNDERBOLT));
   *     sort(result);
   *     return result.isEmpty() ? emptyList() : result;
   *   }
   * }
   * </code>
   * </pre>
   * 
   * @throws IOException
   */
  private void importStatics() throws IOException {
    // 支持嵌套类定义
    ClassName namedBoards = ClassName.get(SPIKE_PACKAGE, "Hoverboard", "Boards");

    ClassName hoverboard = ClassName.get(SPIKE_PACKAGE, "Hoverboard");
    ClassName list = ClassName.get("java.util", "List");
    ClassName arrayList = ClassName.get("java.util", "ArrayList");
    // 泛型
    TypeName listOfHoverboards = ParameterizedTypeName.get(list, hoverboard);

    MethodSpec beyond = MethodSpec.methodBuilder("beyond")//
        .returns(listOfHoverboards)//
        .addStatement("$T result = new $T<>()", listOfHoverboards, arrayList)//
        .addStatement("result.add($T.createNimbus(2000))", hoverboard)//
        .addStatement("result.add($T.createNimbus(\"2001\"))", hoverboard)//
        .addStatement("$T.createNimbus($T.THUNDERBOLT))", hoverboard, namedBoards)//
        .addStatement("$T.sort(result)", Collections.class)//
        .addStatement("return result.isEmpty() ? $T.emptyList() : result", Collections.class)//
        .build();

    TypeSpec hello = TypeSpec.classBuilder("HelloWorld")//
        .addMethod(beyond).build();

    JavaFile javaFile = JavaFile.builder(DEFAULT_PACKAGE + ".helloworld", hello)//
        .addStaticImport(hoverboard, "createNimbus")//
        .addStaticImport(namedBoards, "*")//
        .addStaticImport(Collections.class, "*")//
        .build();
    javaFile.writeTo(System.out);
  }

  /** 引用名称$N */

  /**
   * <pre>
   * 引用名称$N
   * 
   * 生成代码
   * <code>
   * public String byteToHex(int b) {
   *   char[] result = new char[2];
   *   result[0] = hexDigit((b >>> 4) & 0xf);
   *   result[1] = hexDigit(b & 0xf);
   *   return new String(result);
   * }
   * 
   * public char hexDigit(int i) {
   *   return (char) (i < 10 ? i + '0' : i - 10 + 'a');
   * }
   * </code>
   * </pre>
   */
  @Test
  public void _05_names() {
    MethodSpec hexDigit = MethodSpec.methodBuilder("hexDigit")//
        .addParameter(int.class, "i")// 参数
        .returns(char.class)//
        .addStatement("return (char) (i < 10 ? i + '0' : i - 10 + 'a')")//
        .build();

    MethodSpec byteToHex = MethodSpec.methodBuilder("byteToHex")//
        .addParameter(int.class, "b")//
        .returns(String.class)//
        .addStatement("char[] result = new char[2]")//
        .addStatement("result[0] = $N((b >>> 4) & 0xf)", hexDigit)// 方法名称引用
        .addStatement("result[1] = $N(b & 0xf)", hexDigit)//
        .addStatement("return new String(result)")//
        .build();

    System.out.println(hexDigit.toString());
    System.out.println(byteToHex.toString());
  }

  /**
   * <pre>
   * 使用Modifiers.ABSTRACT生成没有方法体的方法
   * 
   * 生成代码
   * <code>
   * public abstract class HelloWorld {
   *   protected abstract void flux();
   * }
   * </code>
   * </pre>
   */
  @Test
  public void _06_methods() {
    MethodSpec flux = MethodSpec.methodBuilder("flux")//
        .addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED)//
        .build();

    TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")//
        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)//
        .addMethod(flux)//
        .build();

    System.out.println(helloWorld);
  }

  /**
   * <pre>
   * 
   * 生成代码
   * <code>
   * public class HelloWorld {
   *   private final String greeting;
   * 
   *   public HelloWorld(String greeting) {
   *     this.greeting = greeting;
   *   }
   * }
   * </code>
   * </pre>
   */
  @Test
  public void _07_constructors() {

    MethodSpec constructor = MethodSpec.constructorBuilder()//
        .addModifiers(Modifier.PUBLIC)//
        .addParameter(String.class, "greeting")//
        .addStatement("this.$N = $N", "greeting", "greeting")//
        .build();

    TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")//
        .addModifiers(Modifier.PUBLIC)//
        .addField(String.class, "greeting", Modifier.PRIVATE, Modifier.FINAL)// 添加字段
        .addMethod(constructor).build();

    System.out.println(helloWorld.toString());
  }

  /**
   * <pre>
   * 生成代码
   * <code>
   * void welcomeOverlords(final String android, final String robot) {
   * }
   * </code>
   * &#64;see ParameterSpec
   * </pre>
   */
  @Test
  public void _08_parameters() {
    // 参数描述
    ParameterSpec android = ParameterSpec.builder(String.class, "android")//
        .addModifiers(Modifier.FINAL)//
        .build();

    MethodSpec welcomeOverlords = MethodSpec.methodBuilder("welcomeOverlords")//
        .addParameter(android)//
        .addParameter(String.class, "robot", Modifier.FINAL)//
        .build();

    System.out.println(welcomeOverlords);

  }

  /**
   * <pre>
   *  生成代码
   *  <code>
   *  public class HelloWorld {
   *   private final String android;
   *   private final String android2 = "Lollipop V." + 5.0;
   *   private final String robot;
   * }
   *   </code>
   * </pre>
   */
  @Test
  public void _09_fields() {
    // 字段描述
    FieldSpec android = FieldSpec.builder(String.class, "android")//
        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)//
        .build();

    // 带初始化的字段
    FieldSpec android2 =
        FieldSpec.builder(String.class, "android2", Modifier.PRIVATE, Modifier.FINAL)//
            .initializer("$S + $L", "Lollipop V.", 5.0d)// 初始化
            .build();

    TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")//
        .addModifiers(Modifier.PUBLIC)//
        .addField(android)// 添加字段
        .addField(android2)//
        .addField(String.class, "robot", Modifier.PRIVATE, Modifier.FINAL)//
        .build();

    System.out.println(helloWorld);
  }

  /**
   * <pre>
   * 生成代码
   * <code>
   * public interface HelloWorld {
   *   String ONLY_THING_THAT_IS_CONSTANT = "change";
   * 
   *   void beep();
   * }
   * </code>
   * </pre>
   */
  @Test
  public void _10_interfaces() {
    TypeSpec helloWorld = TypeSpec.interfaceBuilder("HelloWorld")
        //
        .addModifiers(Modifier.PUBLIC)
        //
        .addField(// 字段
          FieldSpec.builder(String.class, "ONLY_THING_THAT_IS_CONSTANT")//
              .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)//
              .initializer("$S", "change")//
              .build()//
        )//
        .addMethod(// 方法
          MethodSpec.methodBuilder("beep")//
              .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)//
              .build()//
        )//
        .build();

    System.out.println(helloWorld.toString());
  }

  @Test
  public void _11_enums() {

    this.simpleEnum();

    this.complexEnum();
  }

  /**
   * <pre>
   * <code>
   * public enum Roshambo {
   *   ROCK,
   *   SCISSORS,
   *   PAPER
   * }
   * </code>
   * </pre>
   */
  private void simpleEnum() {
    TypeSpec roshambo = TypeSpec.enumBuilder("Roshambo")//
        .addModifiers(Modifier.PUBLIC)//
        .addEnumConstant("ROCK")// 添加枚举常量
        .addEnumConstant("SCISSORS")//
        .addEnumConstant("PAPER")//
        .build();

    System.out.println(roshambo.toString());
  }

  /**
   * <pre>
   * <code>
   * public enum Roshambo {
   *   ROCK("fist") {
   *     &#64;Override
   *     public void toString() {
   *       return "avalanche!";
   *     }
   *   },
   * 
   *   SCISSORS("peace"),
   * 
   *   PAPER("flat");
   * 
   *   private final String handsign;
   * 
   *   Roshambo(String handsign) {
   *     this.handsign = handsign;
   *   }
   * }
   * </code>
   * </pre>
   */
  private void complexEnum() {
    TypeSpec roshambo = TypeSpec.enumBuilder("Roshambo")//
        .addModifiers(Modifier.PUBLIC)//

        // 1 匿名类，带覆盖方法
        .addEnumConstant(//
          "ROCK", //
          TypeSpec.anonymousClassBuilder("$S", "first")//
              .addMethod(MethodSpec.methodBuilder("toString")//
                  .returns(String.class)//
                  .addAnnotation(Override.class)//
                  .addModifiers(Modifier.PUBLIC)//
                  .addStatement("return $S", "avalanche!")//
                  .build())//
              .build()//
        )//

        // 2 匿名类
        .addEnumConstant("SCISSORS", TypeSpec.anonymousClassBuilder("$S", "peace").build())//

        // 3 匿名类
        .addEnumConstant("PAPER", TypeSpec.anonymousClassBuilder("$S", "flat").build())//

        // 4 字段
        .addField(String.class, "handsign", Modifier.PRIVATE, Modifier.FINAL)//

        // 5 构造器
        .addMethod(MethodSpec.constructorBuilder()//
            .addParameter(String.class, "handsign")//
            .addStatement("this.$N = $N", "handsign", "handsign")//
            .build())

        .build();

    System.out.println(roshambo.toString());
  }

  /**
   * <pre>
   * 生成代码
   * <code>
   * void sortByLength(List<String> strings) {
   *   Collections.sort(strings, new Comparator<String>() {
   *     &#64;Override
   *     public int compare(String a, String b) {
   *       return a.length() - b.length();
   *     }
   *   });
   * }
   * </code>
   * </pre>
   */
  @Test
  public void _12_anonymous_inner_classes() {
    // 匿名内部类描述
    TypeSpec comparator = TypeSpec.anonymousClassBuilder("")//
        .addSuperinterface(ParameterizedTypeName.get(Comparator.class, String.class))//
        .addMethod(MethodSpec.methodBuilder("compare")//
            .addAnnotation(Override.class)//
            .addModifiers(Modifier.PUBLIC)//
            .addParameter(String.class, "a")//
            .addParameter(String.class, "b")//
            .returns(int.class)//
            .addStatement("return $N.length() - $N.length()", "a", "b")//
            .build())//
        .build();

    TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")//
        .addMethod(MethodSpec.methodBuilder("sortByLength")//
            .addParameter(ParameterizedTypeName.get(List.class, String.class), "strings")//
            // 这里使用字面量$L
            .addStatement("$T.sort($N, $L)", Collections.class, "strings", comparator)//
            .build())//
        .build();

    System.out.println(helloWorld.toString());
  }

  @Test
  public void _13_annotations() {
    this.simpleAnnotation();
    this.complexAnnotattion();
    this.moreComplexAnnotation();
  }

  /**
   * <pre>
   * 生成代码
   * <code>
   *   &#64;Override
   *   public String toString() {
   *     return "Hoverboard";
   *   }
   * <code>
   * </pre>
   */
  private void simpleAnnotation() {
    MethodSpec toString = MethodSpec.methodBuilder("toString")//
        .addAnnotation(Override.class)// 添加方法上注解
        .returns(String.class)//
        .addModifiers(Modifier.PUBLIC)//
        .addStatement("return $S", "Hoverboard")//
        .build();

    System.out.println(toString.toString());
  }

  /**
   * <pre>
   * 生成代码
   * <code>
   *   &#64;Headers(
   *     accept = "application/json; charset=utf-8",
   *     userAgent = "Square Cash"
   * )
   * LogReceipt recordEvent(LogRecord logRecord);
   * <code>
   * </pre>
   */
  private void complexAnnotattion() {
    AnnotationSpec annotationSpec = AnnotationSpec.builder(ClassName.get(SPIKE_PACKAGE, "Headers"))//
        .addMember("accept", "$S", "application/json; charset=utf-8")//
        .addMember("userAgent", "$S", "Square Cash")//
        .build();

    MethodSpec recordEvent = MethodSpec.methodBuilder("recordEvent")//
        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)//
        .addAnnotation(annotationSpec)//
        .addParameter(ClassName.get(SPIKE_PACKAGE, "LogRecord"), "logRecord")//
        .returns(ClassName.get(SPIKE_PACKAGE, "LogReceipt"))//
        .build();

    System.out.println(recordEvent.toString());
  }

  /**
   * <pre>
   * 生成代码
   * <code>
   *   &#64;HeaderList({
   *     &#64;Header(name = "Accept", value = "application/json; charset=utf-8"),
   *     &#64;Header(name = "User-Agent", value = "Square Cash")
   * })
   * LogReceipt recordEvent(LogRecord logRecord);
   * <code>
   * </pre>
   */
  private void moreComplexAnnotation() {
    AnnotationSpec acceptHeaderAnnotationSpec =
        AnnotationSpec.builder(ClassName.get(SPIKE_PACKAGE, "Header"))//
            .addMember("name", "$S", "Accept")//
            .addMember("value", "$S", "application/json; charset=utf-8")//
            .build();
    AnnotationSpec userAgentHeaderAnnotationSpec =
        AnnotationSpec.builder(ClassName.get(SPIKE_PACKAGE, "Header"))//
            .addMember("name", "$S", "User")//
            .addMember("value", "$S", "Square")//
            .build();

    AnnotationSpec headerListAnnotationSpec =
        AnnotationSpec.builder(ClassName.get(SPIKE_PACKAGE, "HeaderList"))//
            .addMember("value", "$L", acceptHeaderAnnotationSpec)//
            .addMember("value", "$L", userAgentHeaderAnnotationSpec)//
            .build();

    MethodSpec recordEvent = MethodSpec.methodBuilder("recordEvent")//
        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)//
        .addAnnotation(headerListAnnotationSpec)//
        .addParameter(ClassName.get(SPIKE_PACKAGE, "LogRecord"), "logRecord")//
        .returns(ClassName.get(SPIKE_PACKAGE, "LogReceipt"))//
        .build();

    System.out.println(recordEvent.toString());
  }

  /**
   * 示例
   * 
   * <pre>
   * Hides {@code message} from the caller's history. Other
   * participants in the conversation will continue to see the
   * message in their own history unless they also delete it.
   * 
   * <p>
   * Use {@link #delete(Conversation)} to delete the entire
   * conversation for all participants.
   * </pre>
   */
  // void dismiss(Message message);
  @Test
  public void _14_javadoc() {
    MethodSpec dismissMethodSpec = MethodSpec.methodBuilder("dismiss")
        .addJavadoc("Hides {@code message} from the caller's history. Other\n"
            + "participants in the conversation will continue to see the\n"
            + "message in their own history unless they also delete it.\n")//
        .addJavadoc("\n")//
        .addJavadoc(
          "<p>Use {@link #delete($T)} to delete the entire\n"
              + " conversation for all participants.\n",
          ClassName.get(SPIKE_PACKAGE, "Conversation"))//
        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)//
        .addParameter(ClassName.get(SPIKE_PACKAGE, "Message"), "message")//
        .build();

    System.out.println(dismissMethodSpec.toString());
  }
}

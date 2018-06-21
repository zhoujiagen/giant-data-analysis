package com.spike.giantdataanalysis.commons.cli;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

// REF: http://commons.apache.org/proper/commons-cli/
public class TestCommonsCLI {
  public static void main(String[] args) {
    DateApp.run(args);
    System.out.println();

    AntApp.run(args);
    System.out.println();

    LsApp.run(args);
  }

  // Usage: ls [OPTION]... [FILE]...
  // List information about the FILEs (the current directory by default).
  // Sort entries alphabetically if none of -cftuSUX nor --sort.
  //
  // -a, --all ...do not hide entries starting with .
  // -A, --almost-all ...do not list implied . and ..
  // -b, --escape ...print octal escapes for nongraphic characters
  // ....--block-size=SIZE ...use SIZE-byte blocks
  // -B, --ignore-backups ...do not list implied entries ending with ~
  //
  // -c ...with -lt: sort by, and show, ctime (time of last modification of file status information)
  // ......with -l: show ctime and sort by name otherwise: sort by ctime
  // -C ...list entries by columns
  static class LsApp {
    public static void run(String[] args) {

      args = new String[] { "--block-size=10" };

      Options options = new Options();
      // opt: -a, longOpt: --all
      options.addOption("a", "all", false, "do not hide entries starting with .");

      options.addOption("A", "almost-all", false, "do not list implied . and ..");
      options.addOption("b", "escape", false, "print octal escapes for nongraphic characters");
      // 第二个longOpt直接加入, 不指定opt
      options.addOption(Option.builder().longOpt("block-size").hasArg().argName("SIZE")
          .desc("use SIZE-byte blocks").build());
      options.addOption("B", "ignore-backups", false, "do not list implied entried ending with ~");
      options.addOption("c", false,
        "with -lt: sort by, and show, ctime (time of last "
            + "modification of file status information) "
            + "with -l: show ctime and sort by name otherwise: sort by ctime");
      options.addOption("C", false, "list entries by columns");

      HelpFormatter hf = new HelpFormatter();
      hf.printHelp("ls [OPTION]... [FILE]...", options);

      try {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        if (line.hasOption("block-size")) {
          System.out.println("block-size: " + line.getOptionValue("block-size"));
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }

  // ant [options] [target [target2 [target3] ...]]
  // Options:
  // -help print this message
  // -projecthelp print project help information
  // -version print the version information and exit
  // -quiet be extra quiet
  // -verbose be extra verbose
  // -debug print debugging information
  // -emacs produce logging information without adornments
  // -logfile <file> use given file for log
  // -logger <classname> the class which is to perform logging
  // -listener <classname> add an instance of class as a project listener
  // -buildfile <file> use given buildfile
  // -D<property>=<value> use value for given property
  // -find <file> search for buildfile towards the root of the filesystem and use it
  static class AntApp {
    public static void run(String[] args) {

      args = new String[] { "-buildfile", "test.xml", "-Da=1", "-Db=2" };

      // 创建选项
      Option help = new Option("help", "print this message");
      Option projecthelp = new Option("projecthelp", "print project help information");
      Option version = new Option("version", "print the version information and exit");
      Option quiet = new Option("quiet", "be extra quiet");
      Option verbose = new Option("verbose", "be extra verbose");
      Option debug = new Option("debug", "print debugging information");
      Option emacs = new Option("emacs", "produce logging information without adornments");

      // 创建带参数的选项
      Option logfile =
          Option.builder("logfile").hasArg().argName("file").desc("use given file for log").build();

      Option logger = Option.builder("logger").hasArg().argName("classname")
          .desc("the class which is to perform logging").build();

      Option listener = Option.builder("listener").hasArg().argName("classname")
          .desc("add an instance of class as a project listener").build();

      Option buildfile =
          Option.builder("buildfile").hasArg().argName("file").desc("use given buildfile").build();

      Option find = Option.builder("find").hasArg().argName("file")
          .desc("search for buildfile towards the root of the filesystem and use it").build();

      // 创建Java属性选项
      Option property = Option.builder("D").argName("property=value").numberOfArgs(2)
          .valueSeparator('=').desc("use value for given property").build();

      Options options = new Options();
      options.addOption(help);
      options.addOption(projecthelp);
      options.addOption(version);
      options.addOption(quiet);
      options.addOption(verbose);
      options.addOption(debug);
      options.addOption(emacs);
      options.addOption(logfile);
      options.addOption(logger);
      options.addOption(listener);
      options.addOption(buildfile);
      options.addOption(find);
      options.addOption(property);

      // 输出Usage Help
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp("ant", options);

      // 创建解析器
      CommandLineParser parser = new DefaultParser();
      try {
        CommandLine cmd = parser.parse(options, args);

        // 获取选项和值
        if (cmd.hasOption("buildfile")) {
          String buildfileOptionValue = cmd.getOptionValue("buildfile");
          System.out.println("buildfile: " + buildfileOptionValue);
        }
        if (cmd.hasOption("D")) {
          Properties props = cmd.getOptionProperties("D");
          System.out.println("D: " + props);
        }

      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }

  //
  // -t
  // -c <country code>
  static class DateApp {
    public static void run(String[] args) {

      // args = new String[] { "-t" };
      // args = new String[] { "" };
      args = new String[] { "-t", "-c", "CN" };

      // 创建选项
      Options options = new Options();
      options.addOption("t", false, "display current time");
      options.addOption("c", true, "country code");// 有参数

      // 创建解析器
      CommandLineParser parser = new DefaultParser();
      try {
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("t")) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
          System.out.println(sdf.format(new Date()));
        } else {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          System.out.println(sdf.format(new Date()));
        }

        String countryCode = cmd.getOptionValue("c"); // 获取选项的参数
        if (countryCode != null) {
          System.out.println("CountryCode: " + countryCode);
        }

      } catch (ParseException e) {
        e.printStackTrace();
      }

    }
  }

}

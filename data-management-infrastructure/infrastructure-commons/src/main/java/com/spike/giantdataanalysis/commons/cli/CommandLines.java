package com.spike.giantdataanalysis.commons.cli;

import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * 命令行参数解析工具
 * @author zhoujiagen
 */
public final class CommandLines {

  // ======================================== Option

  /**
   * 例: <code>
   * -help print this message
   * </code>
   * @param opt
   * @param description
   * @return
   */
  public static Option option(String opt, String description) {
    return Option.builder(opt).desc(description).build();
  }

  /**
   * 例: <code>
   * -buildfile <_file_> use given buildfile
   * </code>
   * @param opt
   * @param description
   * @return
   */
  public static Option option(String opt, String argName, String description) {
    return Option.builder(opt).hasArg().argName(argName).desc(description).build();
  }

  /**
   * 例: <code>
   * --block-size <_SIZE_>   use SIZE-byte blocks
   * </code>
   * @param longOpt
   * @param argName
   * @param description
   * @return
   */
  public static Option longOptionWithArg(String longOpt, String argName, String description) {
    return Option.builder().longOpt(longOpt).hasArg().argName(argName).desc(description).build();
  }

  /**
   * 例: <code>
   * -a, --all do not hide entries starting with
   * </code>
   * @param opt
   * @param longOpt
   * @param description
   * @return
   */
  public static Option optionWithLongOption(String opt, String longOpt, String description) {
    return Option.builder(opt).longOpt(longOpt).desc(description).build();
  }

  // public static Option option(String opt, String longOpt, String argName, String description) {
  // return
  // Option.builder(opt).longOpt(longOpt).hasArg().argName(argName).desc(description).build();
  // }

  /**
   * 例: <code>
   * -D<_property_>=<_value_> use value for given property
   * </code>
   * @param opt
   * @param argName
   * @param numberOfArgs
   * @param valueSeparator
   * @param description
   * @return
   */
  public static Option option(String opt, String argName, int numberOfArgs, char valueSeparator,
      String description) {
    return Option.builder(opt).argName(argName).numberOfArgs(numberOfArgs)
        .valueSeparator(valueSeparator).desc(description).build();
  }

  // ======================================== Parser

  public static CommandLineParser defaultParser() {
    return new DefaultParser();
  }

  // ======================================== CommandLine

  public static CommandLine parse(CommandLineParser parser, Options options, String[] args)
      throws ParseException {
    return parser.parse(options, args);
  }

  public static boolean hasOption(CommandLine cl, String opt) {
    return cl.hasOption(opt);
  }

  public static String getOptionValue(CommandLine cl, String opt) {
    return cl.getOptionValue(opt);
  }

  public static String getOptionValue(CommandLine cl, String opt, String defaultOptValue) {
    return cl.getOptionValue(opt, defaultOptValue);
  }

  public static Properties getOptionProperties(CommandLine cl, String opt) {
    return cl.getOptionProperties(opt);
  }

  // ======================================== HelpFormatter

  public static void printHelp(String cmdLineSyntax, Options options) {
    HelpFormatter hf = new HelpFormatter();
    hf.printHelp(cmdLineSyntax, options);
  }

  public static void printUsage(PrintWriter pw, int width, String app, Options options) {
    HelpFormatter hf = new HelpFormatter();
    hf.printUsage(pw, width, app, options);
  }
}

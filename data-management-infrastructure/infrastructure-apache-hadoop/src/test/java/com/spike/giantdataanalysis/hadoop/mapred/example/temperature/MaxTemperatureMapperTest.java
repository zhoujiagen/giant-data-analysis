package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;

/**
 * {@link MaxTemperatureMapper}的MRUnit单元测试
 * @author zhoujiagen
 */
public class MaxTemperatureMapperTest {

  @Test
  public void processValidRecord() throws IOException {
    String line =
        "0029029070999991901"
            + "010106004+64333+023450FM-12+000599999V0202701N015919999999N0000001N9"
            + "-00781+99999102001ADDGF108991999999999999999999";

    // String year = line.substring(15, 19);
    // int airTemperature = Integer.parseInt(line.substring(87, 92));

    List<Pair<LongWritable, Text>> inputRecords = new ArrayList<Pair<LongWritable, Text>>();
    Pair<LongWritable, Text> pair =
        new Pair<LongWritable, Text>(new LongWritable(0), new Text(line));
    inputRecords.add(pair);
    new MapDriver<LongWritable, Text, Text, IntWritable>()//
        .withMapper(new MaxTemperatureMapper())//
        .withAll(inputRecords)//
        .withOutput(new Text("1901"), new IntWritable(-78))//
        .runTest();
  }

  @Test
  public void ignoreMissingTemperatureRecord() throws IOException {

    String line =
        "0029029070999991901"
            + "010106004+64333+023450FM-12+000599999V0202701N015919999999N0000001N9"
            + "+99991+99999102001ADDGF108991999999999999999999";

    // String year = line.substring(15, 19);

    System.out.println(line.substring(87, 92));
    int airTemperature = Integer.parseInt(line.substring(87, 92));
    System.out.println(airTemperature);

    List<Pair<LongWritable, Text>> inputRecords = new ArrayList<Pair<LongWritable, Text>>();
    Pair<LongWritable, Text> pair =
        new Pair<LongWritable, Text>(new LongWritable(0), new Text(line));
    inputRecords.add(pair);
    new MapDriver<LongWritable, Text, Text, IntWritable>()//
        .withMapper(new MaxTemperatureMapper())//
        .withAll(inputRecords)//
        .runTest();
  }

}

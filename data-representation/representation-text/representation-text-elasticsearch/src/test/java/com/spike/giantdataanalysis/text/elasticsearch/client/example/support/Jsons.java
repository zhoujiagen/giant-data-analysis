package com.spike.giantdataanalysis.text.elasticsearch.client.example.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.spike.giantdataanalysis.commons.lang.DateUtils;
import com.spike.giantdataanalysis.commons.lang.StringUtils;

public final class Jsons {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  // 相对于CLASSPATH的路径
  private static final String ROOT_JSON_DIR = "/mappings/";

  static {
    // 配置
    OBJECT_MAPPER.setDateFormat(DateUtils.DEFAULT_DATE_FORMAT);
    OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    // OBJECT_MAPPER.configure(SerializationFeature.WRAP_EXCEPTIONS, false);// dangerous
  }

  public static void setDateFormat(String dateFormatPattern) {
    OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(dateFormatPattern));
  }

  public static void setDateFormat(SimpleDateFormat sdf) {
    OBJECT_MAPPER.setDateFormat(sdf);
  }

  public static String asJson(Object object) {
    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "";
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> fileToMap(String fileName) throws FileNotFoundException {
    if (StringUtils.isBlank(fileName)) throw new IllegalArgumentException();

    InputStream inputStream = Jsons.class.getResourceAsStream(ROOT_JSON_DIR + fileName);
    if (inputStream == null) throw new FileNotFoundException();

    Map<String, Object> result = null;
    try {
      result = OBJECT_MAPPER.readValue(inputStream, Map.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  public static List<Map<String, Object>> fileToMapList(String fileName)
      throws FileNotFoundException {
    if (StringUtils.isBlank(fileName)) throw new IllegalArgumentException();

    InputStream inputStream = Jsons.class.getResourceAsStream(ROOT_JSON_DIR + fileName);
    if (inputStream == null) throw new FileNotFoundException();

    List<Map<String, Object>> result = null;
    try {
      // result = OBJECT_MAPPER.readValue(inputStream, //
      // TypeFactory.defaultInstance().constructCollectionLikeType(List.class, Map.class));
      result = OBJECT_MAPPER.readValue(inputStream, List.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }

  // test
  public static void main(String[] args) throws FileNotFoundException {
    System.out.println(fileToMap("twitter_tweet_mapping.json"));
  }

}

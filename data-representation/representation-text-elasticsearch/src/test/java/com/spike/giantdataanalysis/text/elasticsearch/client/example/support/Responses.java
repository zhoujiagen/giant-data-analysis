package com.spike.giantdataanalysis.text.elasticsearch.client.example.support;

import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.broadcast.BroadcastResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.get.GetResult;

import com.spike.giantdataanalysis.commons.lang.StringUtils;

/**
 * <pre>
 * 查看响应内容
 * 
 * 使用JSON展示结果: @see {@link #asString(BulkResponse)}
 * </pre>
 * @author zhoujiagen
 * @see #asString(Object)
 */
public final class Responses {

  private static final String REPEAT_SYMBOL = "=";
  private static final String TITLE_REPEAT_SYMBOL = "-";
  // private static final String BODY_REPEAT_SYMBOL = ".";
  private static final int REPEAT_CNT = 50;

  /**
   * representation of response with no class signature
   * @param response
   * @return
   */
  public static String asString(Object response) {
    if (response == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();
    sb.append(header(response.getClass().getSimpleName()));
    sb.append(Jsons.asJson(response)).append(StringUtils.NEWLINE);
    sb.append(footer());

    return sb.toString();
  }

  public static String asString(BulkResponse response) {

    if (response == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();
    sb.append(header(response.getClass().getSimpleName()));
    // sb.append("tookInMillis=" + response.getTookInMillis()).append(StringUtils.NEWLINE);
    // sb.append("hasFailures=" + response.hasFailures()).append(StringUtils.NEWLINE);
    //
    // for (BulkItemResponse itemResponse : response.getItems()) {
    // // 使用JSON展示结果
    // sb.append(Jsons.asJson(itemResponse))//
    // .append(StringUtils.NEWLINE)//
    // .append(StringUtils.REPEAT(BODY_REPEAT_SYMBOL, REPEAT_CNT))//
    // .append(StringUtils.NEWLINE);
    // }
    sb.append(Jsons.asJson(response)).append(StringUtils.NEWLINE);

    sb.append(footer());

    return sb.toString();

  }

  public static String asString(MultiGetResponse response) {
    if (response == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();
    sb.append(header(response.getClass().getSimpleName()));

    // for (MultiGetItemResponse itemResponse : response.getResponses()) {
    // GetResponse getResponse = itemResponse.getResponse();
    // sb.append(asString(extract(getResponse)))//
    // .append(StringUtils.REPEAT(BODY_REPEAT_SYMBOL, REPEAT_CNT)).append(StringUtils.NEWLINE);
    // }
    sb.append(Jsons.asJson(response)).append(StringUtils.NEWLINE);

    sb.append(footer());

    return sb.toString();
  }

  public static String asString(UpdateResponse response) {
    if (response == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();

    sb.append(header(response.getClass().getSimpleName()));
    // sb.append("index=" + response.getIndex()).append(StringUtils.NEWLINE);
    // sb.append("type=" + response.getType()).append(StringUtils.NEWLINE);
    // sb.append("id=" + response.getId()).append(StringUtils.NEWLINE);
    // sb.append("version=" + response.getVersion()).append(StringUtils.NEWLINE);
    // sb.append("created=" + response.isCreated()).append(StringUtils.NEWLINE);
    // sb.append("getResult=" + asString(response.getGetResult())).append(StringUtils.NEWLINE);
    sb.append(Jsons.asJson(response)).append(StringUtils.NEWLINE);
    sb.append(footer());

    return sb.toString();
  }

  public static String asString(DeleteResponse response) {
    if (response == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();

    sb.append(header(response.getClass().getSimpleName()));
    // sb.append("index=" + response.getIndex()).append(StringUtils.NEWLINE);
    // sb.append("type=" + response.getType()).append(StringUtils.NEWLINE);
    // sb.append("id=" + response.getId()).append(StringUtils.NEWLINE);
    // sb.append("version=" + response.getVersion()).append(StringUtils.NEWLINE);
    // sb.append("found=" + response.isFound()).append(StringUtils.NEWLINE);
    sb.append(Jsons.asJson(response)).append(StringUtils.NEWLINE);
    sb.append(footer());

    return sb.toString();
  }

  private static String asString(GetResult getResult) {
    if (getResult == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();
    // sb.append("index=" + getResult.getIndex()).append(StringUtils.NEWLINE);
    // sb.append("type=" + getResult.getType()).append(StringUtils.NEWLINE);
    // sb.append("id=" + getResult.getId()).append(StringUtils.NEWLINE);
    // sb.append("version=" + getResult.getVersion()).append(StringUtils.NEWLINE);
    // sb.append("exists=" + getResult.isExists()).append(StringUtils.NEWLINE);
    // sb.append("fields=" + getResult.getFields()).append(StringUtils.NEWLINE);
    // sb.append("source=" + getResult.getSource()).append(StringUtils.NEWLINE);
    sb.append(Jsons.asJson(getResult)).append(StringUtils.NEWLINE);
    return sb.toString();
  }

  private static GetResult extract(GetResponse response) {
    if (response == null) return null;

    GetResult result = new GetResult(//
        response.getIndex(), //
        response.getType(), //
        response.getId(), //
        response.getVersion(),//
        response.isExists(), //
        response.getSourceInternal(), //
        response.getFields());

    return result;
  }

  public static String asString(GetResponse response) {
    if (response == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();

    sb.append(header(response.getClass().getSimpleName()));
    sb.append(asString(extract(response))).append(StringUtils.NEWLINE);
    // sb.append(Jsons.asJson(response)).append(StringUtils.NEWLINE);
    sb.append(footer());

    return sb.toString();
  }

  public static String asString(GetSettingsResponse response) {
    if (response == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();

    sb.append(header(response.getClass().getSimpleName()));
    // sb.append("indexToSettings=" + response.getIndexToSettings()).append(StringUtils.NEWLINE);
    sb.append(Jsons.asJson(response)).append(StringUtils.NEWLINE);
    sb.append(footer());

    return sb.toString();
  }

  public static String asString(BroadcastResponse response) {
    if (response == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();

    sb.append(header(response.getClass().getSimpleName()));
    // sb.append("totalShards=" + response.getTotalShards()).append(StringUtils.NEWLINE);
    // sb.append("successfulShards=" + response.getSuccessfulShards()).append(StringUtils.NEWLINE);
    // sb.append("failedShards=" + response.getFailedShards()).append(StringUtils.NEWLINE);
    // sb.append("shardFailures=" + Lists.newArrayList(response.getShardFailures())).append(
    // StringUtils.NEWLINE);
    sb.append(Jsons.asJson(response)).append(StringUtils.NEWLINE);
    sb.append(footer());

    return sb.toString();
  }

  public static String asString(AcknowledgedResponse response) {
    if (response == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();

    sb.append(header(response.getClass().getSimpleName()));

    // sb.append("acknowledged=" + response.isAcknowledged()).append(StringUtils.NEWLINE);
    // boolean isContextEmpty = response.isContextEmpty();
    // sb.append("isContextEmpty=" + isContextEmpty).append(StringUtils.NEWLINE);
    // if (!isContextEmpty) sb.append("context=" +
    // response.getContext()).append(StringUtils.NEWLINE);
    // Set<String> headers = response.getHeaders();
    // if (CollectionUtils.isNotEmpty(headers)) {
    // for (String header : headers) {
    // sb.append(header + "=" + response.getHeader(header)).append(StringUtils.NEWLINE);
    // }
    // }
    sb.append(Jsons.asJson(response)).append(StringUtils.NEWLINE);

    sb.append(footer());

    return sb.toString();
  }

  public static String asString(IndexResponse response) {

    if (response == null) return StringUtils.EMPTY;

    StringBuilder sb = new StringBuilder();

    sb.append(header(response.getClass().getSimpleName()));

    // sb.append("index=" + response.getIndex()).append(StringUtils.NEWLINE);
    // sb.append("type=" + response.getType()).append(StringUtils.NEWLINE);
    // sb.append("id=" + response.getId()).append(StringUtils.NEWLINE);
    // sb.append("version=" + response.getVersion()).append(StringUtils.NEWLINE);
    // sb.append("created=" + response.isCreated()).append(StringUtils.NEWLINE);
    sb.append(Jsons.asJson(response)).append(StringUtils.NEWLINE);

    sb.append(footer());

    return sb.toString();
  }

  private static String header(String title) {
    if (StringUtils.isBlank(title)) title = "";

    return StringUtils.NEWLINE //
        + StringUtils.REPEAT(REPEAT_SYMBOL, REPEAT_CNT)//
        + StringUtils.NEWLINE //
        + title //
        + StringUtils.NEWLINE //
        + StringUtils.REPEAT(TITLE_REPEAT_SYMBOL, REPEAT_CNT)//
        + StringUtils.NEWLINE;
  }

  private static String footer() {
    return StringUtils.REPEAT(REPEAT_SYMBOL, REPEAT_CNT) + StringUtils.NEWLINE;
  }

}

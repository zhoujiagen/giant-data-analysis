package com.spike.giantdataanalysis.etl.progress;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.etl.exception.ETLException;
import com.spike.giantdataanalysis.etl.process.DataFileOps;
import com.spike.giantdataanalysis.etl.supports.ETLConstants;

/**
 * 进展持有器
 */
public final class ProgressHolder {
  private static final Logger LOG = LoggerFactory.getLogger(ProgressHolder.class);

  public static final String FIELD_SEP = ",";
  private static ProgressHolder INSTANCE = new ProgressHolder();

  private ProgressHolder() {
  }

  public static ProgressHolder I() {
    return INSTANCE;
  }

  /** 进展记录 */
  private Map<String, WorkStatus> progress = Maps.newConcurrentMap();
  /** 已经处理的数量 */
  private Map<String, Long> handledCountMap = Maps.newConcurrentMap();

  public void updateHandled(String workId, long deltaCount) {
    LOG.debug("更新已处理数量, workId={}, deltaCount={}", workId, deltaCount);

    if (workId == null || "".equals(workId)) return;

    workId = DataFileOps.I().stripProgressSuffix(workId);// ~

    Long count = handledCountMap.get(workId);
    if (count == null) count = 0l;
    count += deltaCount;
    handledCountMap.put(workId, count);
  }

  public long handledCount(String workId) {
    if (workId == null || "".equals(workId)) return 0l;

    workId = DataFileOps.I().stripProgressSuffix(workId);// ~

    Long result = handledCountMap.get(workId);
    if (result == null) return 0l;
    return result;
  }

  /** 设置进展 */
  public void set(List<String> workIds, ProgressEnum progressEnum) {
    LOG.debug("SET PROGRESS, workIds={}, progressEnum={}", workIds, progressEnum);

    if (workIds == null || workIds.size() == 0) return;

    WorkStatus workStatus = null;
    for (String workId : workIds) {
      workId = DataFileOps.I().stripProgressSuffix(workId);// ~

      workStatus = progress.get(workId);
      if (workStatus == null) {
        workStatus = new WorkStatus();
        workStatus.setProgressEnum(ProgressEnum.NONE);
        workStatus.setHandledCount(0l);
      } else {
        workStatus.setProgressEnum(progressEnum);
      }
      progress.put(workId, workStatus);
    }
  }

  /** 更新进展 */
  public void update(String workId, ProgressEnum progressEnum, long deltaCount) {
    LOG.debug("UPDATE PROGRESS, workIds={}, progressEnum={}, deltaCount={}", workId, progressEnum,
      deltaCount);

    workId = DataFileOps.I().stripProgressSuffix(workId);// ~

    WorkStatus workStatus = progress.get(workId);
    if (workStatus == null) {
      workStatus = new WorkStatus();
      workStatus.setProgressEnum(ProgressEnum.NONE);
      workStatus.setHandledCount(deltaCount);
    } else {
      workStatus.setProgressEnum(progressEnum);
      workStatus.setHandledCount(workStatus.getHandledCount() + deltaCount);
    }
    progress.put(workId, workStatus);
  }

  /** 移除 */
  public void remove(String workId) {
    LOG.debug("REMOVE PROGRESS, workId={}", workId);

    workId = DataFileOps.I().stripProgressSuffix(workId);// ~

    progress.remove(workId);
  }

  public WorkStatus loadFormProgressFile(String filepath, String workId) throws ETLException {
    if (workId == null || "".equals(workId)) return null;

    Map<String, WorkStatus> map = loadFromProgressFile(filepath);
    if (map != null) {
      workId = DataFileOps.I().stripProgressSuffix(workId);// ~
      return map.get(workId);
    }
    return null;
  }

  public Map<String, WorkStatus> loadFromProgressFile(String filepath) throws ETLException {
    LOG.debug("load from file: {}", filepath);

    if (filepath == null || "".equals(filepath.trim())) return null;

    Map<String, WorkStatus> result = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filepath));) {
      String line = null;
      List<String> fields = null;
      String workId = null;
      WorkStatus workStatus = null;
      int fieldsSize = 0;
      while ((line = reader.readLine()) != null) {
        if (ETLConstants.BLANK.equals(line.trim())) continue;
        fields = Splitter.on(FIELD_SEP).splitToList(line);
        if (fields == null) continue;
        fieldsSize = fields.size();
        if (fieldsSize != 3 && fieldsSize != 4) continue;

        workId = fields.get(0);
        workStatus = new WorkStatus();
        workStatus.setProgressEnum(ProgressEnum.of(fields.get(1)));
        workStatus.setHandledCount(Long.valueOf(fields.get(2)));
        if (fieldsSize == 4) {
          workStatus.setTotalCount(Long.valueOf(fields.get(3)));
        }
        result.put(workId, workStatus);
      }

    } catch (Exception e) {
      throw ETLException.newException(e);
    }

    return result;
  }

  /** 转储到文件 */
  void dumpToFile(String filepath) throws ETLException {
    LOG.debug("dump to file: {}", filepath);

    if (filepath == null || "".equals(filepath.trim())) return;

    try (FileWriter writer = new FileWriter(filepath);) {
      writer.write(snapshot());
      writer.flush();
    } catch (IOException e) {
      throw ETLException.newException(e);
    }
  }

  public void dump(PrintWriter printWriter) {
    String snapshot = snapshot();
    LOG.debug(snapshot);

    printWriter.write(snapshot);
  }

  private String snapshot() {
    StringBuilder sb = new StringBuilder();

    TreeMap<String, WorkStatus> treeMap = new TreeMap<>(progress);
    WorkStatus workStatus = null;

    for (String workId : treeMap.keySet()) {
      sb.append(workId + FIELD_SEP);
      workStatus = treeMap.get(workId);
      sb.append(workStatus.getProgressEnum().name() + FIELD_SEP);
      sb.append(workStatus.getHandledCount());
      if (workStatus.getTotalCount() != null) {
        sb.append(FIELD_SEP + workStatus.getTotalCount());
      }
      sb.append(ETLConstants.NEWLINE);
    }

    return sb.toString();
  }
}

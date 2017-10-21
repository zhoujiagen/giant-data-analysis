package com.spike.giantdataanalysis.etl.process;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.etl.progress.ProgressEnum;
import com.spike.giantdataanalysis.etl.supports.ETLConstants;

/**
 * 数据文件操作
 */
public class DataFileOps {

  private static final Logger LOG = LoggerFactory.getLogger(DataFileOps.class);

  private static DataFileOps INSTANCE = new DataFileOps();

  private DataFileOps() {
  }

  public static DataFileOps I() {
    return INSTANCE;
  }

  /** @return 标记后的路径 */
  public String mark(String datafilepath, ProgressEnum progressEnum) {
    if (datafilepath == null || "".equals(datafilepath.trim())) {
      LOG.error("参数datafilepath为空");
      return datafilepath;
    }
    if (progressEnum == null) {
      progressEnum = ProgressEnum.NONE;
    }

    File file = new File(datafilepath);
    if (!file.exists()) {
      LOG.error("文件{}不存在!", datafilepath);
      return datafilepath;
    }

    String toFileName = datafilepath;
    if (ProgressEnum.NONE.equals(progressEnum)) {
      toFileName = stripProgressSuffix(datafilepath);
    } else if (ProgressEnum.DOING.equals(progressEnum)) {
      toFileName = stripProgressSuffix(datafilepath);
      toFileName += ETLConstants.POINT + ProgressEnum.DOING.name();
    } else if (ProgressEnum.FINISHED.equals(progressEnum)) {
      toFileName = stripProgressSuffix(datafilepath);
      toFileName += ETLConstants.POINT + ProgressEnum.FINISHED.name();
    }

    file.renameTo(new File(toFileName));
    return toFileName;
  }

  /** 按文件名称判断进展 */
  public ProgressEnum progress(String datafilepath) {
    if (datafilepath == null) return null;

    if (datafilepath.endsWith(ETLConstants.POINT + ProgressEnum.DOING.name())) {
      return ProgressEnum.DOING;
    } else if (datafilepath.endsWith(ETLConstants.POINT + ProgressEnum.FINISHED.name())) {
      return ProgressEnum.FINISHED;
    } else {
      return ProgressEnum.NONE;
    }
  }

  /** @return 移除文件名中进展标志后缀 */
  public String stripProgressSuffix(String datafilepath) {
    if (datafilepath == null) return datafilepath;

    if (datafilepath.endsWith(ETLConstants.POINT + ProgressEnum.DOING.name())) {
      return datafilepath.substring(0, datafilepath.length()
          - (ETLConstants.POINT + ProgressEnum.DOING.name()).length());
    } else if (datafilepath.endsWith(ETLConstants.POINT + ProgressEnum.FINISHED.name())) {
      return datafilepath.substring(0, datafilepath.length()
          - (ETLConstants.POINT + ProgressEnum.FINISHED.name()).length());
    }
    return datafilepath;
  }

  /** @return 满足要求的文件路径列表 */
  public List<String> locate(List<String> parentDirs, ProgressEnum progressEnum) {
    List<String> result = new ArrayList<>();

    if (progressEnum == null) {
      progressEnum = ProgressEnum.NONE;
    }

    for (String dataFileDir : parentDirs) {
      File dataFileDirFile = new File(dataFileDir);
      if (!dataFileDirFile.exists()) {
        continue;
      }

      File[] childFiles = dataFileDirFile.listFiles(new FileFilter() {
        @Override
        public boolean accept(File pathname) {
          if (pathname.isFile() && pathname.canRead()) {
            return true;
          } else {
            return false;
          }
        }
      });
      if (childFiles != null && childFiles.length > 0) {
        String childFileName = null;
        for (File childFile : childFiles) {
          childFileName = childFile.getAbsolutePath();

          if (ProgressEnum.NONE.equals(progressEnum)) {
            if (!childFileName.endsWith(ProgressEnum.DOING.name()) //
                && !childFileName.endsWith(ProgressEnum.FINISHED.name())) {
              result.add(childFileName);
            }

          } else if (ProgressEnum.DOING.equals(progressEnum)) {
            if (childFileName.endsWith(ProgressEnum.DOING.name())) {
              result.add(childFileName);
            }
          } else if (ProgressEnum.FINISHED.equals(progressEnum)) {
            if (childFileName.endsWith(ProgressEnum.FINISHED.name())) {
              result.add(childFileName);
            }
          }
        }
      }

    }

    return result;
  }
}

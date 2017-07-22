package com.spike.giantdataanalysis.hbase.support;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class HBaseNamespaces {
  private static final Logger LOG = LoggerFactory.getLogger(HBaseNamespaces.class);

  /**
   * 创建命名空间描述符
   * @param name
   * @return
   */
  public static NamespaceDescriptor createNamespaceDescriptor(String name) {
    Preconditions.checkArgument(name != null, "Argument name must not be null");
    return NamespaceDescriptor.create(name).build();
  }

  /**
   * 获取命名空间描述符
   * @param admin
   * @param name
   * @return
   * @throws IOException
   */
  public static NamespaceDescriptor namespaceDescriptor(Admin admin, String name)
      throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(name),
      "Argument name must not be null or empty!");

    return admin.getNamespaceDescriptor(name);
  }

  /**
   * 修改命名空间
   * @param admin
   * @param nsDescriptor
   * @throws IOException
   */
  public static void modifyNamespace(Admin admin, NamespaceDescriptor nsDescriptor)
      throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(nsDescriptor != null, "Argument nsDescriptor must not be null!");

    LOG.info("修改命名空间, 参数: " + nsDescriptor);
    admin.modifyNamespace(nsDescriptor);
  }
}

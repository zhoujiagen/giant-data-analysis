package com.spike.giantdataanalysis.hadoop.support;

/**
 * Hadoop中常量
 * @author zhoujiagen
 */
@Deprecated
public interface HadoopConstants {

  interface Common {
    /** 配置文件名称 */
    String config_filename = "core-site.xml";

    /** 文件系统URI */
    String fs_defaultFS_KEY = "fs.default.name";
    String fs_defaultFS_VALUE_Standalone = "file:///";
    String fs_defaultFS_VALUE_PseudoDistributed = "hdfs://localhost/";
    String fs_defaultFS_VALUE_FullyDistributed_FORMAT = "hdfs://<namenode>/";

  }

  interface HDFS {
    /** 配置文件名称 */
    String config_filename = "hdfs-site.xml";

    /** 文件副本的数量 */
    String dfs_replication_KEY = "dfs.replication";
    String dfs_replication_VALUE_PseudoDistributed = "1";
    String dfs_replication_VALUE_FullyDistributed = "3";

    /** 写入文件时，最小副本写入成功的数量 */
    String dfs_namenode_replication_min_KEY = "dfs .namenode.replication.min";
    String dfs_namenode_replication_min_VALUE = "1";

    /** 文件校验的区块大小 */
    String file_bytes_per_chechsum_KEY = "file.bytes-per-chechsum";
    /** 文件校验的默认区块大小，单位byte */
    String file_bytes_per_chechsum_VALUE_DEFAULT = "512";
    /** 文件校验结果存放文件后缀 */
    String cRC_file_extention = ".crc";

    /** 文件系统的实现 */
    String fs_file_impl_KEY = "fs.file.impl";
    String fs_file_impl_VALUE_RawLocalFileSystem = "org.apache.hadoop.fs.RawLocalFileSystem";

    /** 压缩格式 */
    enum CompressionFormat {
      DEFLATE("org.apache.hadoop.io.compress.DefaultCodec"), //
      gzip("org.apache.hadoop.io.compress.GzipCodec"), //
      bzip2("org.apache.hadoop.io.compress.BZip2Codec"), //
      LZO("com.hadoop.cpmpression.lzo.LzopCodec"), //
      LZ4("org.apache.hadoop.io.compress.Lz4Codec"), //
      Snappy("org.apache.hadoop.io.compress.SnappyCodec");

      private String codec;

      CompressionFormat(String codec) {
        this.codec = codec;
      }

      public String getCodec() {
        return this.codec;
      }
    }

    /** 指定Serialization实现的属性，不清楚用在conf还是hdfs-site中，多个按逗号分隔 */
    String io_serializations_KEY = "io.serializations";
    /** 默认值 */
    String io_serializations_KEY_DEFAULT = "org.apache.hadoop.io.serializer.WritableSerialization";
  }

  /**
   * 一些默认值官方文档见
   * http://hadoop.apache.org/docs/stable/hadoop-mapreduce-client/hadoop-mapreduce-client-
   * core/mapred-default.xml
   * @author zhoujiagen
   */
  interface MapReduce {
    /** 配置文件名称 */
    String config_filename = "mapred-site.xml";

    /** 执行框架 */
    String mapreduce_framework_name_KEY = "mapreduce.framework.name";
    String mapreduce_framework_name_VALUE_Standalone = "local";
    String mapreduce_framework_name_VALUE_PseudoDistributed = "yarn";
    String mapreduce_framework_name_VALUE_FullyDistributed = "yarn";

    /** 指定期望作业运行队列的名称，未指定时队列名称为default */
    String mapreduce_job_queuename = "mapreduce.job.queuename";

    /** 作业输出压缩属性，默认为false */
    String mapreduce_output_fileoutputformat_compress =
        "mapreduce.output.fileoutputformat.compress";
    /** 作业输出压缩CompressionCodec：默认为org.apache.hadoop.io.compress.DefaultCodec */
    String mapreduce_output_fileoutputformat_compress_codec =
        "mapreduce.output.fileoutputformat.compress.codec";
    /** 作业输出压缩格式，可取值RECORD, BLOCK, NONE，默认为RECORD */
    String mapreduce_output_fileoutputformat_compress_type =
        "mapreduce.output.fileoutputformat.compress.type";

    /** 作业Map输出压缩属性，默认为false，见Job.MAP_OUTPUT_COMPRESS */
    String mapreduce_map_output_compress = "mapreduce.map.output.compress";
    /**
     * 作业Map输出压缩CompressionCodec，默认为org.apache.hadoop.io.compress.DefaultCodec，见Job.
     * MAP_OUTPUT_COMPRESS_CODEC
     */
    String mapreduce_map_output_compress_codec = "mapreduce.map.output.compress.codec";
  }

  interface YARN {
    /** 配置文件名称 */
    String config_filename = "yarn-site.xml";

    /** RM主机名 */
    String yarn_resourcemanager_hostname_KEY = "yarn.resourcemanager.address";
    String yarn_resourcemanager_hostname_VALUE_PseudoDistributed = "localhost";
    String yarn_resourcemanager_hostname_VALUE_FullyDistributed_FORMAT = "<resourcemanager>";

    /** NM辅助服务 */
    String yarn_nodemanager_aux_services_KEY = "yarn.nodemanager.aux-services";
    String yarn_nodemanager_aux_services_VALUE_PseudoDistributed = "mapreduce_shuffle";
    String yarn_nodemanager_aux_services_VALUE_FullyDistributed = "mapreduce_shuffle";

    /** YARN中的调度器 */
    interface Scheduler {
      /** 调度器开启配置 */
      String yarn_resourcemanager_scheduler_class_KEY = "yarn.resourcemanager.scheduler.class";
      String yarn_resourcemanager_scheduler_class_VALUE_Default =
          "org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity.CapacityScheduler";
      String yarn_resourcemanager_scheduler_class_VALUE_Fair =
          "org.apache.hadoop.yarn.server.resourcemanager.scheduler.fair.FairScheduler";

      interface FIFO {

      }

      interface Capacity {
        /** 配置文件名称 */
        String config_filename = "capacity-scheduler.xml";

        /** 配置属性名称的前缀 */
        String config_property_name_prefix = "yarn.scheduler.capacity.root";

        /** 属性: 指定父子队列关系 */
        String config_property_queues = "queues";
        /** 属性: 指定队列容量大小 */
        String config_property_capacity = "capacity";
        /** 属性: 指定队列的最大容量，体现队列弹性(queue elasticity) */
        String config_property_maximum_capacity = "maximum-capacity";
      }

      interface Fair {
        /** 配置文件名称 */
        String config_filename = "fair-scheduler.xml";
        /** 配置文件名称修改对应的属性 */
        String yarn_scheduler_fair_allocation_file_KEY = "yarn.scheduler.fair.allocation.file";

        /** 提交的应用放置在默认队列还是每个用户的队列中，值为false时对应于前者 */
        String yarn_scheduler_fiar_user_as_default_queue_KEY =
            "yarn.scheduler.fiar.user-as-default-queue";

        /** 是否允许用户动态创建队列，值为false时不允许 */
        String yarn_scheduler_fair_allow_undeclared_pools_KEY =
            "yarn.scheduler.fair.allow-undeclared-pools";

        /** 抢占(preemption)激活配置属性，为true时激活 */
        String yarn_scheduler_fair_preemption_KEY = "yarn.scheduler.fair.preemption";
      }
    }

  }

  /** replication number of job JAR */
  String mapred_submit_replication_KEY = "mapred.submit.replication";
  String mapred_submit_replication_VALUE_DEFAULT = "10";

  /** numbers of Reduce tasks in a job */
  String mapred_reduce_tasks_KEY = "mapred.reduce.tasks";

  /** job finish event HTTP notification url, i.e. callback in client */
  String job_end_notification_url_KEY = "job.end.notification.url";

  /** with YARN, generate input splits in cluster */
  String yarn_app_mapreduce_am_compute_splits_in_cluster_KEY =
      "yarn.app.mapreduce.am.compute-splits-in-cluster";

  /**
   * with YARN, uber tasks(small job): <br>
   * contain less than 10 mappers, 1 reducer, small HDFS block, and usually run in a JVM
   */
  String mapreduce_job_ubertask_enable_KEY = "mapreduce.job.ubertask.enable";
  String mapreduce_job_ubertask_maxmaps_KEY = "mapreduce.job.ubertask.maxmaps";
  String mapreduce_job_ubertask_maxreduces_KEY = "mapreduce.job.ubertask.maxreduces";
  String mapreduce_job_ubertask_maxbytes_KEY = "mapreduce.job.ubertask.maxbytes";

  /** with YARN, map and recude task memory allocated */
  String mapreduce_map_memory_mb_KEY = "mapreduce.map.memory.mb";
  String mapreduce_map_memory_mb_VALUE_DEFAULT = "1024";
  String mapreduce_reduce_memory_mb_KEY = "mapreduce.reduce.memory.mb";
  String mapreduce_reduce_memory_mb_VALUE_DEFAULT = "1024";

  /** YARN capacity scheduler memory allocation */
  String yarn_scheduler_capacity_minimum_allocation_mb_KEY =
      "yarn.scheduler.capacity.minimum-allocation-mb";
  String yarn_scheduler_capacity_minimum_allocation_mb_VALUE_DEFAULT = "1024";
  String yarn_scheduler_capacity_maximum_allocation_mb_KEY =
      "yarn.scheduler.capacity.maximum-allocation-mb";
  String yarn_scheduler_capacity_maximum_allocation_mb_VALUE_DEFUALT = "10240";

  /** with YARN, client poll job progress interval(default 1s) */
  String mapreduce_client_progressmonitor_pollinterval_KEY =
      "mapreduce.client.progressmonitor.pollinterval";
  /** with YARN, client poll job completion interval(default 5s) */
  String mapreduce_client_completion_pollinterval_KEY = "mapreduce.client.completion.pollinterval";
}

# 1 资源

+ [Solr wiki](https://wiki.apache.org/solr)

## 安装和配置

+ [通用安装步骤](https://wiki.apache.org/solr/SolrInstall)
+ [Solr日志记录](https://wiki.apache.org/solr/SolrLogging)
+ [配置Solr](https://wiki.apache.org/solr/ConfiguringSolr)
+ [Solr管理客户端](https://wiki.apache.org/solr/SolrAdminGUI)
+ [Solr Cloud](https://wiki.apache.org/solr/SolrCloud)

### 配置文件

+ [solr.xml](https://wiki.apache.org/solr/Solr.xml%204.4%20and%20beyond)
+ [solrconfig.xml](https://wiki.apache.org/solr/SolrConfigXml)
+ [schema.xml](https://wiki.apache.org/solr/SchemaXml)

Solr的中心目录是`solr.solr.home`或Servlet容器中的`solr/home`, 如果前面的属性未定义, 则是当前工作目录下的`./solr`.

Solr尝试在`solr.solr.home`目录下寻找`solr.xml`, 该文件中是全局配置信息以及core的位置信息.
如果`solr.xml`文件不存在, Solr将尝试以单core模式启动, 该特性计划在5.0版本后移除.

在每个core中, Solr尝试查找`conf/solrconfig.xml`, `solrconfig.xml`中可以定义其他配置文件, 例如用于数据导入处理器的`conf/dih-config.xml`.
除非在`solrconfig.xml`中修改, `conf/schema.xml`将用于加载schema.





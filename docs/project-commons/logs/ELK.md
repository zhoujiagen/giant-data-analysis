# ELK versions

+ Elasticsearch 2.4.0(elasticsearch-2.4.0)
+ Logstash 2.4.1(logstash-2.4.1)
+ Kibana 4.6.3(kibana-4.6.3-darwin-x86_64)

# ELK Configurations

REF [A session of Log Collect, Retrieval and Analysis using ELK Stack](http://www.cnblogs.com/zhoujiagen/p/5317686.html) for details.

Elasticsearch, Kibana使用默认配置.

## Logstash的配置

logstash-2.4.1/start_log4j.sh

    bin/logstash -f config/log4j.conf

logstash-2.4.1/config/log4j.conf

    #########################################################
    ### 输入
    #########################################################
    input{
      stdin{}
      file{
        path => ["/Users/jiedong/logs/commons-example*.log"]
      }
    }

    #########################################################
    ### 过滤
    ###
    ### grok 可以使用测试链接：http://grokdebug.herokuapp.com/
    #########################################################
    filter{
      multiline {
          pattern => "^(%{TIMESTAMP_ISO8601})"
          negate => true
          what => "previous"
       }
       grok {
          # Do multiline matching with (?m) as the above mutliline filter may add newlines to the log messages.
          match => [ "message", "(?m)^%{TIMESTAMP_ISO8601:logtime} \[%{LOGLEVEL:loglevel}\] \[%{PROG:threadname}\] %{JAVACLASS:classname}\.<?%{WORD:method}>?\(%{JAVAFILE:filename}\:%{NUMBER:lineno}\) => %{GREEDYDATA:logmessage}" ]
       }
    }

    #########################################################
    ### 输出
    #########################################################
    output{
      elasticsearch { hosts => "localhost:9200" }
      stdout{ codec=>rubydebug }
    }

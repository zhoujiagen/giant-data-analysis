# Apache Parquet

## 0 实践版本

	1.10.1

## 1 资源

- [Documentation](https://parquet.apache.org/documentation/latest/)

## 2 运行实例

```
com.spike.giantdataanalysis.parquet.example.ExampleParquetFile
com.spike.giantdataanalysis.parquet.example.ExampleParquetFileWithAvro
```

## 3 安装和配置


```
<properties>
  <avro.version>1.10.1</avro.version>
</properties>
```

```
<dependencies>
  <dependency>
    <groupId>org.apache.parquet</groupId>
    <artifactId>parquet-common</artifactId>
    <version>${parquet.version}</version>
  </dependency>
  <dependency>
    <groupId>org.apache.parquet</groupId>
    <artifactId>parquet-encoding</artifactId>
    <version>${parquet.version}</version>
  </dependency>
  <dependency>
    <groupId>org.apache.parquet</groupId>
    <artifactId>parquet-column</artifactId>
    <version>${parquet.version}</version>
  </dependency>
  <dependency>
    <groupId>org.apache.parquet</groupId>
    <artifactId>parquet-hadoop</artifactId>
    <version>${parquet.version}</version>
  </dependency>
  <dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-client</artifactId>
    <version>${hadoop2.version}</version>
    <scope>provided</scope>
    <exclusions>
      <exclusion>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
      </exclusion>
    </exclusions>
  </dependency>

  <dependency>
    <groupId>org.apache.parquet</groupId>
    <artifactId>parquet-avro</artifactId>
    <version>${parquet.version}</version>
  </dependency>
  <dependency>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro</artifactId>
    <version>${avro.version}</version>
  </dependency>
  <dependency>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro-ipc</artifactId>
    <version>${avro.version}</version>
  </dependency>

</dependencies>
```

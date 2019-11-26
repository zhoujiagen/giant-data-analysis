# Apache Avro

## 0 实践版本

	1.8.2

## 1 资源

- [Apache Avro™ 1.8.2 Specification](http://avro.apache.org/docs/1.8.2/spec.html)

## 2 运行实例

```
com.spike.giantdataanalysis.avro.example.ExampleAvroRPC
com.spike.giantdataanalysis.avro.example.ExampleAvroDataSerialization
```

## 3 安装和配置

Maven依赖和构建插件

```
<properties>
  <avro.version>1.8.2</avro.version>
</properties>
```

```
<dependencies>
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

```
<plugins>
			<plugin>
				<groupId>org.apache.avro</groupId>
				<artifactId>avro-maven-plugin</artifactId>
				<version>1.8.1</version>
				<executions>
					<execution>
						<id>schemas</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>schema</goal>
							<goal>protocol</goal>
							<goal>idl-protocol</goal>
						</goals>
						<configuration>
							<sourceDirectory>${basedir}/src/main/avro/</sourceDirectory>
							<outputDirectory>${basedir}/target/generated-resources/avro/</outputDirectory>
						</configuration>
					</execution>
				</executions>
			</plugin>

			<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>build-helper-maven-plugin</artifactId>
				<version>1.4</version>
				<executions>
					<execution>
						<id>add-source</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>add-source</goal>
						</goals>
						<configuration>
							<sources>
								<source>${basedir}/src/main/avro</source>
								<source>${basedir}/target/generated-resources/avro</source>
							</sources>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>
```

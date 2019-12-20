# Container for GDA project

## Java

构建镜像:

```
$ docker build --force-rm --squash --tag java8:ubuntu16.04 .
```


## Hadoop

构建镜像:

```
$ docker build -f Dockerfile --force-rm --squash --tag hadoop:2.10.0 .
$ docker build -f nn.Dockerfile --force-rm --tag hadoop-nn:2.10.0 .
$ docker build -f dn.Dockerfile --force-rm --tag hadoop-dn:2.10.0 .
```

创建网络:

```
docker network create hadoop-network
```

启动NameNode:

```
$ docker run -d --hostname namenode \
  --name hadoop-nn \
  --net=hadoop-network --net-alias=namenode \
  -p 9000:9000 -p 50070:50070 \
  --mount type=volume,source=volnn,destination=/gda \
  hadoop-nn:2.10.0
```

启动DataNode:

```
$ docker run -d --hostname datanode1 \
  --name hadoop-dn1 \
  --net=hadoop-network --net-alias=datanode1 \
  -p 50010:50010 -p 50020:50020 -p 50075:50075 \
  --mount type=volume,source=voldn1,destination=/gda \
  hadoop-dn:2.10.0
$ docker exec -it hadoop-dn1 bash
# service ssh restart
# ssh localhost
## exit
# sbin/hadoop-daemons.sh --config /etc/hadoop --script hdfs start datanode
```

```
$ docker run -d --hostname datanode2 \
  --name hadoop-dn2 \
  --net=hadoop-network --net-alias=datanode2 \
  -p 50011:50010 -p 50021:50020 -p 50076:50075 \
  --mount type=volume,source=voldn2,destination=/gda \
  hadoop-dn:2.10.0
$ docker exec -it hadoop-dn2 bash
# service ssh restart
# ssh localhost
## exit
# sbin/hadoop-daemons.sh --config /etc/hadoop --script hdfs start datanode
```

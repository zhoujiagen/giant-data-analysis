
# 0 实践版本

# 1 资源

# 2 运行实例

# 3 安装和配置

## org.ethereum.Start 

+ src/main/resources/ethereumj.conf - from ethereumj-core.jar

	ip.list
	bind.ip
	trusted

+ src/main/resources/genesis/frontier.json - from ethereumj-core.jar

## ethereum-harmony

	./gradlew runCustom

+ src/main/resources/harmony.conf

	web.port = 9999
	
+ config/ethereumj.conf - from ethereumj-core.jar

	ip.list
	bind.ip
	peer.listen.port = 30304
	trusted

+ src/main/resources/genesis/frontier.json - from ethereumj-core.jar

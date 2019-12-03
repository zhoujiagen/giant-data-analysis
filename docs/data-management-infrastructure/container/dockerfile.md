# Dockerfile笔记

## 资源

- [Dockerfile reference](https://docs.docker.com/engine/reference/builder/)
- [Docker should include a formal grammar for Dockerfile #12221](https://github.com/moby/moby/issues/12221): NO!!!
- [fabric8io/docker-maven-plugin](https://github.com/fabric8io/docker-maven-plugin/): Maven plugin for running and creating Docker images
- [fabric8io/fabric8-maven-plugin](https://github.com/fabric8io/fabric8-maven-plugin): Maven plugin for getting your Java apps on to Kubernetes and OpenShift

## 镜像构建过程

### `docker build`

``` shell
$ docker build --help

Usage:	docker build [OPTIONS] PATH | URL | -

Build an image from a Dockerfile
```

其中, `PATH`或`URL`所指向的文件称为context(上下文), context包含构建Docker镜像中需要的Dockerfile以及其他的资源文件.

例子:

``` shell
# 从标准输入中读入Dockerfile
docker build - < Dockerfile

# git repository URL: 克隆仓库到本地临时目录, 将该目录作为context
docker build github.com/creak/docker-firefox

# context为本地文件或目录
docker build -t vieus/apache:2.0 .
# 指定Dockerfile和context
docker build -f /home/me/myapp/mydockerfile /home/me/myapp
```

### `.dockerignore`文件

context的根目录下的`.dockerignore`, 用于剔除匹配模式的文件和目录.

例:

```
# comment
*/temp*
*/*/temp*
temp?

# 以!开始的行用于标记不剔除的项目
*.md
!README*.md
README-secret.md
```


### 镜像构建过程

Dockerfile描述了组装镜像的步骤, 其中每条指令都是单独执行的.

除了`FROM`指令, 其它每条指令都会在上一条指令所生成镜像的基础上执行, 执行完后会生成一个新的镜像层, 新的镜像层覆盖在原来的镜像之上形成新的镜像.

Docker daemon会缓存构建过程中的中间镜像. 在寻找缓存的过程中, `COPY`和`ADD`指令除了对比指令字符串之外, 还会对比容器中的文件内容与所添加的文件内容是否相同; 其它指令只会对比生成镜像的指令字符串是否相同.

在镜像的构建过程中, 一旦缓存失效, 后续的指令将不再使用缓存.


## 语法和语义

### BuildKit

从18.09版本开始, 通过[moby/buildkit](https://github.com/moby/buildkit)项目提供了执行构建的新后端.

### 解析器命令(Parser Directives)

解析器命令的形式:

``` dockerfile
# directive=value
```

必须出现在Dockerfile的顶部. 支持两个解析器命令:

- `syntax`: 只在使用BuildKit后端时可用, 定义了用于构建当前Dockerfile的构建器的位置.
- `escape`: 设置Dockerfile中的转义字符, 默认为`\`.

### 环境替换

用`ENV`语句声明的环境变量, 也可以作为变量用在一些指令中.

记法: `$variable_name`, `${variable_name}`; 其中,

- `${variable:-word}`: 如果设置了`variable`, 结果为该值; 否则结果为`word`;
- `${variable:+word}`: 如果设置了`variable`, 结果为`word`; 否则结果为空字符串.

支持环境变量的指令:

- `ADD`
- `COPY`
- `ENV`
- `EXPOST`
- `FROM`
- `LABEL`
- `STOPSIGNAL`
- `USER`
- `VOLUME`
- `WORKDIR`
- `ONBUILD`

### 指令(Instruction)

格式:

```
# Comment
INSTRUCTION arguments
```

#### FROM

``` dockerfile
FROM <image> [AS <name>]
FROM <image>[:<tag>] [AS <name>]
FROM <image>[@<digest>] [AS <name>]
```

#### RUN

``` dockerfile
# shell形式
RUN <command>
# exec形式
RUN ["executable", "param1", "param2"]
```

#### CMD

``` dockerfile
# exec形式
CMD ["executable","param1","param2"]
# 作为ENTRYPOINT的默认参数
CMD ["param1","param2"]
# shell形式
CMD command param1 param2
```

#### LABEL

给镜像添加元数据.

``` dockerfile
LABEL <key>=<value> <key>=<value> <key>=<value> ...
```

#### EXPOSE

告知Docker容器在运行时监听的网络端口.

``` dockerfile
EXPOSE <port> [<port>/<protocol>...]
```

#### ENV

设置环境变量

``` dockerfile
ENV <key> <value>
ENV <key>=<value> ...
```

#### ADD

从`<src>`处拷贝新文件或目录, 添加到镜像中文件系统的`<dest>`路径中.

``` dockerfile
ADD [--chown=<user>:<group>] <src>... <dest>
# 路径中有空格时
ADD [--chown=<user>:<group>] ["<src>",... "<dest>"]
```

#### COPY

``` dockerfile
COPY [--chown=<user>:<group>] <src>... <dest>
# 路径中有空格时
COPY [--chown=<user>:<group>] ["<src>",... "<dest>"]
```

#### ENTRYPOINT

``` dockerfile
# exec形式
ENTRYPOINT ["executable", "param1", "param2"]
# shell形式
ENTRYPOINT command param1 param2
```

#### VOLUME

创建挂载点, 并将其标记为持有外部挂在的卷.

``` dockerfile
VOLUME ["/data"]
```

#### USER

设置后续的`RUN`、`CMD`、`ENTRYPOINT`和运行镜像时的用户ID和用户组ID.

``` dockerfile
USER <user>[:<group>] or
USER <UID>[:<GID>]
```

#### WORKDIR

设置后续的`RUN`、`CMD`、`ENTRYPOINT`、`COPY`、`ADD`指令的工作目录.

``` dockerfile
WORKDIR /path/to/workdir
```

#### ARG

定义使用`docker build`命令时可以用`--build-arg <varname>=<value>`标记传入的变量.

``` dockerfile
ARG <name>[=<default value>]
```

预定义的`ARG`:

- HTTP_PROXY
- http_proxy
- HTTPS_PROXY
- https_proxy
- FTP_PROXY
- ftp_proxy
- NO_PROXY
- no_proxy

平台的`ARG`:

- TARGETPLATFORM
- TARGETOS
- TARGETARCH
- TARGETVARIANT
- BUILDPLATFORM
- BUILDOS
- BUILDARCH
- BUILDVARIANT

#### ONBUILD

在镜像中添加trigger指令, 该指令在当前镜像作为另一个构建的基础镜像时使用.

``` dockerfile
ONBUILD [INSTRUCTION]
```

#### STOPSIGNAL

设置用于发送给容器以退出时使用的系统调用信号.

``` dockerfile
STOPSIGNAL signal
```

#### HEALTHCHECK

告知Docker如何检查容器是否仍在工作.

``` dockerfile
# 通过在容器中执行命令检查容器的健康
HEALTHCHECK [OPTIONS] CMD command
# 关闭任何从基础镜像集成的健康检查
HEALTHCHECK NONE
```

`OPTIONS`有:

- `--interval=DURATION` (默认30s)
- `--timeout=DURATION` (默认30s)
- `--start-period=DURATION` (默认0s)
- `--retries=N` (默认3)

#### SHELL

允许覆盖命令的shell形式使用的默认shell. Linux上的默认shell是`["/bin/sh", "-c"]`.

``` dockerfile
SHELL ["executable", "parameters"]
```

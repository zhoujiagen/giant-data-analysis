# Netty in Action的笔记

> Netty in Action, Norman Maurer and Marvin Allen Wolfthal, 2015.

## 存在问题
ChannelHandler在每个服务器实例的每个连接处理中用的是同一个实例? YES


## 图例

- Netty高层次的组件图

![Netty高层次的组件图](./images/high-level-components.png)

- Channel/EventLoop/EventLoopGroup的关系

![Channel/EventLoop/EventLoopGroup的关系](./images/channel-eventloop-eventloopgroup.png)

- ChannelPipeline和ChannelHandler的关系

![ChannelPipeline和ChannelHandler的关系](./images/channel-pipeline-with-channel-handler.png)

- ByteBuf区分了读写索引

![ByteBuf区分了读写索引](./images/ByteBuf-read-write-index.png)

- ByteBuf的容量

![ByteBuf的容量](./images/ByteBuf-capacity.png)

- Channel/ChannelHandler/ChannelPipeline/ChannelHandlerContext的关系

![Channel/ChannelHandler/ChannelPipeline/ChannelHandlerContext的关系](./images/channel-with-handler-pipepline-context.png)

- EventLoop的类层次

![EventLoop的类层次](./images/eventloop-class-hierarchy.png)


### 既有实现/协议

- SslHandler(加密)数据流

![SslHandler(加密)数据流](./images/SslHandler-data-flow.png)

- HTTP请求组件

![HTTP请求组件](./images/HTTP-request-components.png)

- HTTP响应组件

![HTTP响应组件](./images/HTTP-response-components.png)

- WebSocket协议

![WebSocket协议](./images/WebSocket-protocol.png)


## PART 1 Netty概念和体系结构

- Netty 异步和事件驱动: 1
- 首个Netty应用: 2 Your first Netty application
- Netty组件和设计: 3 Netty components and design<br>
  核心网络协议: 4 Transports<br>
  数据处理层: 5,6  ByteBuf, ChannelHandler, ChannelPipeline<br>
  并发模型: 7 EventLoop, threading model
- 配置Netty组件: 8 Bootstrapping
- 单元测试: 9 Unit testing

### 1 asynchronous and event-driven

Netty核心组件: Channel, Callback, Future, Events and handler<br>
Channel是Java NIO的基本构造: 打开的实体连接, 实体可以是硬件设备/文件/网络socket/能执行IO操作的软件组件

Netty内部处理事件时使用Callback, callback被触发时, 事件可以被ChannelHandler实现处理.

Future提供了另一种操作完成时通知应用的方法. Netty提供了实现ChannelFuture.<br>
ChannelFuture上注册ChannelFutureListener, 异步操作完成时listener#operationComplete()被调用; 避免了轮询检测或阻塞等待结果.<br>
Netty的外向(outbound)每个IO操作返回ChannelFuture, 不会阻塞.

Netty使用不同的事件通知操作状态的改变.<br>

Netty事件按inbound/outbound数据流分类:

+ inbound: 连接active/inactive, 读到数据, 用户事件, 错误时间
+ outbound(操作的结果将会触发动作): 开启/关闭远端链接, 写/刷新数据到socket

每个事件可被分发到ChannelHandler中用户实现的方法.
事件可以在ChannelHandler链中传播.

Netty的异步编程模型建立在Future和Callback概念上, 同时将事件分发给相应的Handler方法.

Netty抽象隔离了Selector, 避免硬编码的分发代码. 给每个Channel分配一个EventLoop, 用于处理所有事件, 包括:

+ 注册感兴趣事件
+ 将事件分发给ChannelHandler
+ 调度远期动作

EventLoop自身只由一个线程驱动, 该线程处理一个channel的所有IO事件, 在EventLoop生命周期内不变.

### 2 Your first Netty application

Echo server and Client:

+ 客户端建立连接后, 向服务器发送一个或多个消息
+ 服务端将消息echo给客户端

Server:

+ 至少一个ChannelHandler, 实现收到客户端数据后的业务处理逻辑
+ 配置服务器的启动代码, 最简情形下绑定端口后监听连接请求

ChannelHandler: 处理数据处理逻辑, 将应用逻辑与网络代码解耦.<br>
处理inbound事件, 需要实现ChannelInboundHandler, 子类ChannelInboundHandlerAdapter

Client<br>
SimpleChannelInboundHandler

### 3 Netty components and design

Netty处理了两类问题: 技术性, 体系结构性

+ 技术性
在Java  NIO基础上搭建的异步和事件驱动实现, 保证大负载下应用性能和可扩展性
+ 体系结构性
将应用逻辑与网络层解耦, 简化开发, 最大化可测试性/模块化/代码可重用性

#### channel, eventloop, channel future
##### Channel - sockets
Netty的Channel接口简化了Java中Socket的操作.<br>
已有部分实现: EmbeddedChannel, LocalServerChannel, NioDatagramChannel, NioSctpChannel, NioSocketChannel

##### EventLoop - control flow, multithreading, concurrency

- EventLoopGroup包含1个或多个EventLoop;
- 每个EventLoop在其生命周期内绑定到单个Thread;
- 每个EventLoop处理的所有IO事件, 在其专用的Thread中处理;
- 每个Channel在其生命周期内只可被注册到单个EventLoop;
- 单个EventLoop可以被指派给1个或多个Channel.

设计中一个Channel中的IO操作由同一线程处理, 本质上避免了同步开销.

##### ChannelFuture - asynchronous notification

- Netty中所有IO操作都是异步的, 需要在将来某一时刻检查异步操作的结果.
- ChannelFuture中addListener(ChannelFutureListener)方法, 在操作完成时(不管成功还是失败)通知listener.
- ChannelFuture表示的操作最终会被执行, 同一个Channel上的操作保证按被调用循序执行.

#### channel handler, channel pipeline

管理数据流和执行应用逻辑.

##### ChannelHandler

ChannelHandler是用于处理inbound/outbound数据的应用逻辑的容器.<br>
ChannelHandler中方法由网络事件触发.<br>
已有实现: ChannelInboundHandler(接收由应用逻辑处理的inbound事件和数据, 将数据发送给连接的客户端).

##### ChannelPipeline

ChannelPipeline是ChannelHandler链的容器, 同时提供了将inbound/outbound事件在链中传播的API.<br>
ChannelHandler的安装: ChannelInitializer.initChannel()<br>
ChannelInitializer, 一个特殊的ChannelInboundHandler, 在Channel注册了EventLoop后, 初始化channel的handler链.

ChannelHander的两个扩展: ChannelInboudnHandler, ChannelOutboundHander.<br>
Netty区分inbound/outbound的实现, 传递数据/事件时仅会传递给同一方向类型的handler.


ChannelHandlerContext: 表示ChannelHandler与ChannelPipeline之间的结构关系.

Netty中发送消息的两种方式:<br>
(1) 直接使用Channel#write(..), 消息从ChannelPipeline的tail开始.<br>
(2) 使用ChannelHandlerContext#write(..), 消息从ChannelPipeline中下一个ChannelHandler开始.

ChannelHandler的子类型:

- encoder, decoder, ChannelInboundHandler, ChannelOutboundHandler等
- 一些adapter: ChannelHandlerAdapter, ChannelInboundHandlerAdapter, ChannelOutboundHandlerAdapter, ChannelDuplexHandlerAdapter

##### encoder, decoder

decoded: 将inbound消息由byte转换为另一种格式, 例如Java对象;<br>
encoded: 将outbound消息由特定格式转换为byte.<br>
命名约定: ByteToMessageDecoder/MessageToByteEncoder, ProtobufEncoder/ProtobufDecoder<br>
Netty中所有encoder/decoder的adapter类均实现了ChannelInboundHandler/ChannelOutboundHandler

SimpleChannelInboundHander<T>#channelRead0(ChannelHandlerContext, T): ChannelInboundHandlerAdapter的子类<br>
直接处理decoded消息, 方法中需要保证当前IO线程不被阻塞.

#### bootstrapping

应用网络层配置的容器

ServerBootstrap: 绑定端口<br>
需要两个EventLoopGroup<br>
两类不同的Channel: ServerChannel(服务端监听的socket), 处理客户端链接的Channel

与ServerChannel相关的EventLoopGroup指派的EventLoop, 负责为连接请求创建Channel;<br>
一旦连接请求被接受, 第二个EventLoopGroup为新创建的Channel指派EventLoop.

Bootstrap: 连接远端<br>
只需要一个EventLoopGroup

### 4 Transport

案例: 接受连接后, 向连接发送'Hi', 关闭连接.<br>
Java原生的实现 v.s. Netty的实现<br>
OIO: blocking transport<br>
NIO: asynchronous transport<br>
Netty为不同的transport提供了一致的API.

local transport: async communication within a JVM<br>
embedded transport: testing ChannelHandlers<br>

#### Transport API

Channel, AttributeMap, ChannelConfig, ChannelPipeline

ChannelHandler的典型应用:

+ 转换数据格式
+ 发生异常时发布通知
+ Channel活跃状态改变时发布通知
+ Channel注册或注销EventLoop时发布通知
+ 对用户自定义事件发布通知

可以动态修改ChannelPipeline: 添加/移除ChannelHandler.

Netty的Channel实现是线程安全的.

#### Netti提供的transport

NIO, Epoll, OIO, Local, Embedded


### 5 ByteBuf

Java ByteBuffer, Netty ByteBuf

ByteBuf, ByteBufHolder

#### ByteBuf

支持用户自定义buffer;<br>
透明的zero-copy: 由内建的聚合buffer类型支持;<br>
读写使用不同的index;<br>
支持引用计数/pooling.

readerIndex, writerIndex<br>
readerIndex > writerIndex会抛IndexOutOfBoundsException

ByteBuf#read...()/write...()方法会修改读写索引, 而get...()/set...()不会.

ByteBuf的最大容量默认是Integer.MAX_VALUE

ByteBuf的使用模式:

+ 使用堆中buffer
+ 借助本地调用使用OS buffer
+ 聚合Buffer: 多个ByteBuf的聚合视图


#### 字节层次的操作

- Random access indexing
- Sequential access indexing
- Discardable bytes
- Readable bytes
- Writable bytes
- Index management
- Search operations
- Derived buffers
- Read/write operations

#### ByteBufHolder

包装ByteBuf作为消息的payload使用.

#### ByteBuf分配(allocation)

可以从Channel, ChannelHandlerContext中获取.
ByteBufAllocator: PooledByteBufAllocator, UnpooledByteBufAllocator.

#### 引用计数

ReferenceCounted


### 6 ChannelHandler, ChannelPipeline
#### ChannelHandler

Channel的生命周期<br>

```
ChannelRegistered -> ChannelActive -> ChannelInactive -> ChannelUnregistered
注册了EventLoop       连接到远端
```

ChannelHandler的生命周期: 在ChannelPipeline中添加/移除<br>
两个重要的子接口: ChannelInboundHandler, ChannelOutboundHandler<br>
handlerAdded, handlerRemoved, exceptionCaught(异常捕获移动到了ChannelInboundHandler中)

ChannelInboundHandlerAdapter, SimpleChannelInboundHandler<br>
依据是否要将消息传递给下一个ChannelHandler, 调用ReferenceCountUtil#release/retain(msg).

ChannelOutboundHandler可以按需延迟处理操作或事件.<br>
ChannelPromise, ChannelFuture的子接口, 提供设置Future结果方法.


资源泄露检测: ResourceLeakDetector<br>
使用java -Dio.netty.leakDetection.level=SIMPLE<br>
值见io.netty.util.ResourceLeakDetector.Level

#### ChannelPipeline

每个Channel创建时被指派一个ChannelPipeline, 这种关系是持久性的.

ChannelHandlerContext辅助ChannelHandler与ChannelPipeline以及其他ChannelHandler交互;<br>
甚至帮助ChannelHandler修改其隶属于的ChannelPipeline.

ChannelHandler在ChannelPipeline中所处的位置由添加时指定.

ChannelPipeline中的ChannelHandler负责处理其关联Channel的EventLoop传递的事件.<br>
不要在ChannelHandler中执行阻塞的操作.

??? - 需要在ChannelHandler中执行阻塞的操作.<br>
ChannelPipeline.addLast(EventExecutorGroup group, ChannelHandler... handlers)

If an event is passed to a custom EventExecutorGroup, it will be handled by one of the EventExecutors contained in this EventExecutorGroup and thus be removed from the EventLoop of the Channel itself.
For this use case Netty provides an implementation called DefaultEventExecutorGroup.<br>
ChannelHandler由谁调用, 怎么执行???


总结:<br>
(1) ChannelPipeline是Channel相关ChannelHander的容器;<br>
(2) ChannelPipeline可以动态修改;<br>
(3) ChannelPipeline提供了调用inbound和outbound实现的方法.

#### ChannelHandlerContext

ChannelHandlerContext在ChannelHandler添加到ChannelPipeline时创建,<br>
一旦关联便不再改变, 可以在ChannelHandler中持有其引用.

维护ChannelHandler与其他ChannelHandler之间的交互.

ChannelHandlerContext中有与Channel或ChannelPipeline相同的方法, 如果被调用仅影响下一个ChannelHandler,<br>
而不是像后者影响整个ChannelPipeline.

ChannelHandlerContext#channel()获取关联Channel实例;<br>
ChannelHandlerContext#pipeline()获取关联ChannelPipeline实例.

共享ChannelHandler的典型应用是统计多个Channel的指标.


#### 处理异常

一旦inbound ChannelHandler中出现异常, 该异常会流经整个ChannelPipeline;<br>
如果没有在ChannelPipeline中处理异常, Netty会记录日志.

处理outbound异常:<br>
(1) 每个outbound操作会返回ChannelFuture, 注册的ChannelFutureListener可用于处理成功或失败情况;<br>
(2) ChannelOutboundHaneler的方法一般有ChannelPromise参数, 可用于处理成功或失败情况.


#### 7 EventLoop and threading model

#### 线程模型threading model

描述在OS/编程语言/框架/应用上下文中, 线程管理的关键特性.

线程池模式: Java 5 Executor API, 但无法避免上下文切换的消耗.

#### EventLoop

io.netty.channel.EventLoop: 执行任务处理连接过程中发生的事件.

EventLoop设计融合了并发和网络设计: io.netty.util.concurrent(java.util.concurrent), io.netty.channel.

#### task scheduling

#### implementation

thread management
eventloop-thread-channel allocation


### 8 Bootstrapping

> TODO

### 9 unit testing

>> TODO

## PART 2 codec

### 10 codec framework
encoder/decoder
应用数据与网络传输格式之间的转换
抽象: 将消息视为由字节构成的结构化数据
encoder: 将消息转换为网络字节流, 即处理outbound数据
decoder: 将网络字节流转换为消息, 即处理inbound数据

codec: 同时具备encoder/decoder功能


#### decoder: 处理inbound数据, Netty的decoder实现了ChannelInboundHandler
两类decoder:
(1) 字节流=>消息   ByteToMessageDecoder, ReplayingDecoder
(2) 消息=>消息     MessageToMessageDecoder

ByteToMessageDecoder
因无法预知远端是否一次性发送完整的消息, 该类会缓存inbound数据

ReplayingDecoder
继承了ByteToMessageDecoder, decode(...)中按已接收到需要的数据处理(避免调用ByteBuf#readableBytes()), 提供了特殊的ByteBuf实现: ReplayingDecoderBuf
ReplayingDecoderBuf并没有提供完整的ByteBuf功能.

更多的decoder:
io.netty.handler.codec.LineBasedFrameDecoder
io.netty.handler.codec.http.HttpObjectDecoder

MessageToMessageDecoder
复杂的示例: io.netty.handler.codec.http.HttpObjectAggregator

TooLongFrameException
Netty是异步框架, 在获取足够的字节前会缓存数据, 但缓存太多的字节会引起一些问题.
该异常会被CHannelHandler.exceptionCaught()捕获.


#### encoder: 处理outbound数据, Netty的encoder实现了ChannelOutboundHandler
两类encoder:
(1) 消息=>字节流   MessageToByteEncoder
(2) 消息=>消息     MessageToMessageEncoder

MessageToByteEncoder
既有示例: WebSocket08FrameEncoder

MessageToMessageEncoder
既有示例: ProtobufEncoder


#### codec: Netty的codec实现了ChannelInboundHandler和ChannelOutboundHandler

ByteToMessageCodec
示例POJO: SmtpRequest, SmtpResponse

MessageToMessageCodec<INBOUND_IN, OUTBOUND_IN>

CombinedChannelDuplexHandler: 将encoder和decoder作为一个单元部署
CombinedChannelDuplexHandler<I extends ChannelInboundHandler, O extends ChannelOutboundHandler>

### 11 既有ChannelHandler和code实现


#### Netty application with SSL/TLS
加密协议SSL/TLS
Java提供了javax.net.ssl, SSLContext, SSLEngine
Netty提供了SslHandler的ChannelHandler实现, 以及基于OpenSSL toolkit的SSLEngine实现: OpenSslEngine

#### Netty HTTP/HTTPS application

HTTP decoder/encoder/codec
基础接口: HttpObject
请求: FullHttpRequest(HttpRequest, HttpContent*, LastHttpContent)
HttpRequestEncoder/Decoder
响应: FullHttpResponse(HttpResonse, HttpContent*, LastHttpContent)
HttpResponseEncoder/Decoder

HTTP message aggregation
HttpObjectAggregator, 不需要考虑消息片段(fragment)

HTTP compression
既有实现支持gzip和deflat压缩
HttpContentDecompressor, HttpContentCompressor

HTTPS
使用SslHandler

WebSocket
WebSocketServerProtocolHandler
帧(Frame):
BinaryWebSocketFrame, TextWebSocketFrame, ContinuationWebSocketFrame,
CloseWebSocketFrame, PingWebSocketFrame, PongWebSocketFrame

#### idle connection and timeout
#### delimited and length-based protocol
#### writing big data
#### data serializing



## PART 3 网络协议

> TODO

## PART 4 案例

> TODO

 # netty-springboot
 springboot + nettyRPC 实现游戏服务器 支出 TCP网络通讯和PRC远程调用，采用protobuf协议通讯。支持PRC调用超时重连，采用logback日志。

#RPC调用方式
HelloService helloService = RemoteService.newRemoteProxyObject(HelloService.class);

#invoke类具体请求实现。支持通讯阻塞和超时断线处理,获取RPC返回结果

#采用MessageAbstract结构进行协议通讯-- 加密和解密，压缩和解压，粘包问题操作暂时未做处理









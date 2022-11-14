 # netty-springboot
 springboot + nettyRPC 实现游戏服务器 支出 TCP网络通讯和PRC远程调用，采用protobuf协议通讯。支持PRC调用超时重连，采用logback日志。

#RPC调用方式
HelloService helloService = RemoteService.newRemoteProxyObject(HelloService.class);

#invoke类具体请求实现。支持通讯阻塞和超时断线处理,获取RPC返回结果

#采用MessageAbstract结构进行协议通讯-- 加密和解密，压缩和解压，粘包问题操作暂时未做处理

public abstract class MessageAbstract<T> implements Message{
    public int id;

    public int opcode;

    public int uid;

    public T data;

    public MessageAbstract(){

    }

    public MessageAbstract(int id, int opcode, int uid, T data){
        this.id = id;
        this.opcode = opcode;
        this.uid = uid;
        this.data = data;
    }
}

//服务器需要根据RPC请求的响应的opcode获取到对应执行器处理
@Override
public void handleReceivedMessage(ChannelHandlerContext handlerContext, Message message){
    ReceivedMessageType type = getReceivedMessageType(message.getOpcode());
    switch (type){
        case REQUEST:
            handleRequest(handlerContext, message);
            break;
        case RESPONSE:
            handleResponse(handlerContext, message);
            break;
        case UNKNOWN:
            handleUnknownMessage(handlerContext, message);
            break;
    }
}
  
  
  
  








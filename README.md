## **项目背景** ##
    RPC（Remote Procedure Call Protocol）远程过程调用协议，它是一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。简言之，RPC使得程序能够像访问本地系统资源一样，去访问远端系统资源。比较关键的一些方面包括：通讯协议、序列化、资源（接口）描述、服务框架、性能、语言支持等。简单的说，RPC就是从一台机器(客户端)上通过参数传递的方式调用另一台机器(服务器)上的一个函数或方法(可以统称为服务)并得到返回的结果。
   

## **功能说明** ##
核心代码：

    HelloService helloService = RemoteService.newRemoteProxyObject(HelloService.class);
    JSONArray result = helloService.sayHello("hello world");
    
    
根据动态代码实现RPC调用，核心是重写invoke方法:

    /**
     * 动态代理的真实对象的实现
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T newRemoteProxyObject(final Class<?> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new              ProxyHandler(service));
    }

采用netty进行长链接进行通讯。自定义coder和idel心跳检测处理，断线重连机制。

    MyNettyClient.INSTANCE.newRequest(Opcode.RPC_REQUEST, rpcRequest, new SubRequestSuccess() {
            @Override
            public void success(Result data) {
                //返回的数据不是error
                if(data.getCode() == 0){
                    JSONArray resultArray = (JSONArray) data.getResult();
                    result[0] = resultArray;
                }

            }
        });
    
    
    
    
    

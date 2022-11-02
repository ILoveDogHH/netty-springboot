package com.haoxy.common.handler;

import com.haoxy.common.message.*;
import com.haoxy.common.opcode.Opcode;
import com.haoxy.common.request.RequestFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public abstract class MessageHandler<T> extends ChannelInboundHandlerAdapter implements Handler {

    public HandlerExecutor handlerExecutor;

    public RequestFactory factory;

    public MessageHandler(HandlerExecutor handlerExecutor,  RequestFactory factory){
        this.handlerExecutor = handlerExecutor;
        this.factory = factory;
    }


    @Override
    public ReceivedMessageType getReceivedMessageType(int opcode) {
        switch (opcode){
            case Opcode.RPC_REQUEST:
                return ReceivedMessageType.REQUEST;
            case Opcode.PRC_RESPONSE:
                return ReceivedMessageType.RESPONSE;
            default:
                return ReceivedMessageType.UNKNOWN;
        }
    }


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

    @Override
    public void handleRequest(ChannelHandlerContext handlerContext, Message message) {
        Result<?> result;
        try {
            result = handlerExecutor.exe(message);
            handleResult(handlerContext, message, result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void handleResponse(ChannelHandlerContext handlerContext, Message message) {
        try {
            factory.doCallback(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void handleUnknownMessage(ChannelHandlerContext handlerContext, Message message) {
        try {
            System.out.println("非正常opcode");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void handleResult(ChannelHandlerContext handlerContext, Message message, Result result) {
        handlerExecutor.handResult(handlerContext, message, result);
    }
}

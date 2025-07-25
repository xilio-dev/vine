package cn.xilio.vine.core;

import cn.xilio.vine.core.protocol.TunnelMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 隧道消息处理器
 */
public interface MessageHandler {
    /**
     * 处理业务逻辑
     *
     * @param ctx channel上下文
     * @param msg 消息内容
     * @throws Exception 异常
     */
    void handle(ChannelHandlerContext ctx, TunnelMessage.Message msg) throws Exception;
}

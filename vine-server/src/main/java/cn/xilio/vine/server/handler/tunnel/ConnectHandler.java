package cn.xilio.vine.server.handler.tunnel;

import cn.xilio.vine.core.VineConstants;
import cn.xilio.vine.core.protocol.TunnelMessage;
import cn.xilio.vine.server.ChannelManager;
import cn.xilio.vine.core.AbstractMessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;

/**
 * 连接消息处理器
 */
public class ConnectHandler extends AbstractMessageHandler {
    @Override
    protected void doHandle(ChannelHandlerContext ctx, TunnelMessage.Message msg) {
        long sessionId = msg.getSessionId();
        String secretKey = msg.getExt();
        Channel controllTunnelChannel = ChannelManager.getControlTunnelChannel(secretKey);
        Channel visitorChannel = ChannelManager.getVisitorChannel(controllTunnelChannel, sessionId);
        ctx.channel().attr(VineConstants.NEXT_CHANNEL).set(visitorChannel);
        ctx.channel().attr(VineConstants.SECRET_KEY).set(secretKey);
        ctx.channel().attr(VineConstants.SESSION_ID).set(sessionId);
        //将数据隧道-通道绑定到访问者通道上，用于访问通道代理将消息通过数据隧道转发到内网
        visitorChannel.attr(VineConstants.NEXT_CHANNEL).set(ctx.channel());
        visitorChannel.config().setOption(ChannelOption.AUTO_READ, true);
    }
}

package cn.xilio.vine.server;


import cn.xilio.vine.core.EventLoopUtils;
import cn.xilio.vine.core.Lifecycle;
import cn.xilio.vine.core.heart.IdleCheckHandler;
import cn.xilio.vine.core.protocol.TunnelMessageDecoder;
import cn.xilio.vine.core.protocol.TunnelMessageEncoder;
import cn.xilio.vine.server.handler.TunnelChannelHandler;
import cn.xilio.vine.server.handler.VisitorChannelHandler;
import cn.xilio.vine.server.store.ProxyManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TunnelServer implements Lifecycle {
    private final Logger logger = LoggerFactory.getLogger(TunnelServer.class);
    private boolean ssl;
    private String host;
    private int port;
    private EventLoopGroup tunnelBossGroup;
    private EventLoopGroup tunnelWorkerGroup;

    @Override
    public void start() {
        try {
            EventLoopUtils.ServerConfig config = EventLoopUtils.createServerEventLoopConfig();
            tunnelBossGroup = config.bossGroup;
            tunnelWorkerGroup = config.workerGroup;
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(tunnelBossGroup, tunnelWorkerGroup)
                    .channel(config.serverChannelClass)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline()
                                    .addLast(new TunnelMessageDecoder(1024 * 1024, 0, 4, 0, 0))
                                    .addLast(new TunnelMessageEncoder())
                                    .addLast(new IdleCheckHandler(60, 40, 0, TimeUnit.SECONDS))
                                    .addLast(new TunnelChannelHandler());
                        }
                    });
            if (StringUtils.hasText(host)) {
                serverBootstrap.bind(host, port).sync();
            } else {
                serverBootstrap.bind(port).sync();
            }
            startTcpProxy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        logger.info("关闭TunnelServer|释放资源");
        tunnelBossGroup.shutdownGracefully();
        tunnelWorkerGroup.shutdownGracefully();
    }

    private static void startTcpProxy() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new VisitorChannelHandler());
                    }
                });
        try {
            List<Integer> ports = ProxyManager.getInstance().getAllPublicNetworkPort();
            for (Integer port : ports) {
                serverBootstrap.bind(port).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

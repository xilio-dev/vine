package cn.xilio.vine.core.command.model;

import cn.xilio.vine.core.protocol.ProtocolType;

import java.io.Serializable;

public class ProxyModel implements Serializable {
    /**
     * 客户端密钥
     */
    private String secretKey;
    /**
     * 代理名称 自定义
     */
    private String name;
    /**
     * 协议 {@link ProtocolType}
     */
    private ProtocolType type;
    /**
     * 内网IP地址
     */
    private String localIP;
    /**
     * 内网被代理服务的IP地址
     */
    private Integer localPort;
    /**
     * 远程服务端口
     */
    private Integer remotePort;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProtocolType getType() {
        return type;
    }

    public void setType(ProtocolType type) {
        this.type = type;
    }

    public String getLocalIP() {
        return localIP;
    }

    public void setLocalIP(String localIP) {
        this.localIP = localIP;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }
}

package cn.xilio.vine.core.command.model;

public class ClientModel {
    /**
     * 客户端名称
     */
    private String name;
    /**
     * 客户端密钥，用于与代理服务器通信认证
     */
    private String secretKey;
    /**
     * 客户端的状态。1:在线，0:离线
     */
    private int status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

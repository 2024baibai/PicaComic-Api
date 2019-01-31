package com.flannep.picacomic.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * 代表pica的http标准请求头
 */
public class PicaHeader {

    /**
     * 客户端某个硬编码的值
     */
    private String secret_key = "~n}$S9$lGts=U)8zfL/R.PM9;4[3|@/CEsl~Kk!7?BYZ:BAa5zkkRBL7r|1/*Cr";

    /**
     * 噪声，防重放攻击
     * 测试无效果，随机即可
     */
    private String nonce = UUID.randomUUID().toString().replace("-", "");

    /**
     * 客户端请求头（似乎也是固定？）
     */
    private String api_key = "C69BAF41DA5ABD1FFEDC6D2FEA56B";

    /**
     * 客户端版本号
     */
    private String version = "2.1.0.7";

    /**
     * 构建的版本？安卓sdk版本？
     */
    private String build_version = "40";
    /**
     * 分流服务器？
     */
    private int channel = 1;
    /**
     * 时间戳
     * 注意，和服务器的时间差要控制在300秒以内
     */
    private long timestamp = System.currentTimeMillis() / 1000;
    /**
     * 登陆后服务器返回的token
     * 大部分请求需要此字段，否则会返回401错误
     */
    private String authorization = null;

    /**
     * 要访问的url
     */
    private String url = null;

    /**
     * 设置请求类型
     * GET或POST
     */
    private Method method = Method.GET;

    /**
     * 提交类型
     * 部分请求需要此字段
     * 例：
     * 登录：application/json; charset=UTF-8 无此字段将会报too many requests
     */
    private String contentType = null;


    private String user_agent = null;

    private String host = null;
    private String connection = null;

    private String accept_encoding = null;

    /**
     * 代表请求的模式
     */
    public enum Method {
        /**
         * GET请求
         */
        GET,
        /**
         * POST请求
         */
        POST;



    }
    public PicaHeader() {

    }

    /**
     * 获取http请求头
     * 注意，调用后请尽快使用，避免请求过期
     *
     * @return
     */
    public Map<String, String> getHttpHeader() {
        Map<String, String> header = new TreeMap<>();
        try {

            if (authorization != null) {
                header.put("authorization", authorization);
            }

            if (contentType != null) {
                header.put("Content-Type", contentType);
            }

            header.put("signature", getSignature());
            header.put("api-key", getApi_key());
            header.put("app-channel", String.valueOf(getChannel()));
            header.put("time", String.valueOf(getTimestamp()));
            header.put("nonce", getNonce());
            header.put("app-version", getVersion());
            header.put("app-build-version", getBuild_version());
            header.put("accept", "application/vnd.picacomic.com.v1+json");
            header.put("app-nonce", "1b509109-476d-3790-a5f0-0acfeace9a5b");
            header.put("app-platform", "android");
            header.put("app-uuid", "983f6ed7-e528-3129-a125-19a5427d603f");
            header.put("User-Agent", "okhttp/3.8.1");
            header.put("Host", "picaapi.picacomic.com");
            if (user_agent != null) {
                header.put("User-Agent", getUser_agent());
            }
            if (host != null) {
                header.put("Host", getHost());
            }
            if (connection!=null){
                header.put("Connection",getConnection());
            }
            if (accept_encoding!=null){
                header.put("Accept-Encoding",accept_encoding);
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return header;
    }


    /**
     * 获取签名
     * 通过 URL 、时间戳、噪声、提交方式和APIKEY计算出请求头的签名
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public String getSignature() throws NoSuchAlgorithmException, InvalidKeyException {
        url = url.replace("https://picaapi.picacomic.com/", "");
        url = url + getTimestamp() + getNonce() + getMethod() + getApi_key();
        url = url.toLowerCase();
        return HMACSHA256(url.getBytes(), getSecret_key().getBytes());
    }


    private static String HMACSHA256(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        return byte2hex(mac.doFinal(data));
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString();
    }


    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuild_version() {
        return build_version;
    }

    public void setBuild_version(String build_version) {
        this.build_version = build_version;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 设置请求的时间戳，单位秒
     * 注意：如果时间和服务器相差300秒以上将会报错，请在设置后尽快发起请求！
     *
     * @param timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthorization() {
        return authorization;
    }

    /**
     * 设置身份验证字段
     * 大部分请求需要带有此字段鉴权
     * 使用登录api获取
     *
     * @param authorization
     */
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }



    public String getAccept_encoding() {
        return accept_encoding;
    }

    public void setAccept_encoding(String accept_encoding) {
        this.accept_encoding = accept_encoding;
    }
}

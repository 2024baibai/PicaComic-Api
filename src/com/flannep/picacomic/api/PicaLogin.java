package com.flannep.picacomic.api;

import com.flannep.picacomic.api.util.NetUtil;
import net.sf.json.JSONObject;

/**
 * 代表哔咔漫画登录类
 */
public class PicaLogin {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;


    public PicaLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 执行登录
     *
     * @return 登录成功返回header的authorization字段，用于大部分请求的鉴权，不需要重复登录！
     * @throws
     */
    public String login() throws Exception {

        PicaHeader header = new PicaHeader();
        header.setUrl("https://picaapi.picacomic.com/auth/sign-in");
        header.setMethod(PicaHeader.Method.POST);
        //登录不加会报错
        header.setContentType("application/json; charset=UTF-8");

        JSONObject param = new JSONObject();
        param.put("email", username);
        param.put("password", password);

        PicaResult result =
                new PicaResult(
                        JSONObject.fromObject(
                                NetUtil.sendPost(header, param.toString(), true)));
        //有错误就抛出异常
        if (result.hasError()) {
            String errMsg = result.getMessage();
            if (errMsg.contains("invalid email or password")) {
                errMsg = "账号密码错误";
            }
            throw new IllegalArgumentException(errMsg + "\n" + result.toString());
        }
        return result.getData().getString("token");
    }
}

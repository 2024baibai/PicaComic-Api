package com.flannep.picacomic.api;

import com.flannep.picacomic.api.util.NetUtil;
import net.sf.json.JSONObject;

/**
 * 代表pica服务器返回的请求结果对象
 */
public class PicaResult {

    private JSONObject result;

    /**
     * 使用哔咔服务器返回的json构造一个PicaResult对象
     *
     * @param webJson
     */
    public PicaResult(JSONObject webJson) {
        result = webJson;
    }

    /**
     * 静态方法，获取pica请求结果
     *
     * @param header pica标准请求头
     * @return
     * @throws Exception 仅当无法解析网页内容时抛出异常
     */
    public static PicaResult getPicaResult(PicaHeader header) throws Exception {
        String webContent = NetUtil.sendGet(header, true);
        return new PicaResult(JSONObject.fromObject(webContent));
    }

    /**
     * 获取http状态码
     *
     * @return
     */
    public int getCode() {
        return result.getInt("code");
    }

    /**
     * 判断请求是否出现错误
     *
     * @return
     */
    public boolean hasError() {
        return result.containsKey("error");
    }

    /**
     * 获取错误码
     * 不存在错误时返回-1
     *
     * @return
     */
    public int getErrorCode() {
        if (hasError()) {
            return result.getInt("error");
        }
        return -1;
    }

    /**
     * 获取消息
     * 一般来说成功返回success，失败返回对应的错误信息
     *
     * @return
     */
    public String getMessage() {
        if (result.containsKey("message")) {
            return result.getString("message");
        }
        return null;
    }

    /**
     * 获取数据
     * 如果不存在此字段，将会返回null
     *
     * @return 不存在时返回null
     */
    public JSONObject getData() {
        if (result.containsKey("data")) {
            return result.getJSONObject("data");
        }
        return null;
    }

    @Override
    public String toString() {
        return this.result.toString();
    }
}

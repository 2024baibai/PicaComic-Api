package com.flannep.picacomic.api.book;

import net.sf.json.JSONObject;

/**
 * 代表每个文件对象
 * 一般来说，访问这些文件是不需要带请求头的
 *
 * @author FlanN
 */
public class Media {
    private JSONObject media;
    private boolean usingBackupServer = false;

    Media(JSONObject mediaJson) {
        this.media = mediaJson;
    }

    /**
     * 获取文件原始名称
     *
     * @return
     */
    public String getOriginalName() {
        if (media.containsKey("originalName")) {
            return media.getString("originalName");
        }
        return null;
    }

    /**
     * 获取文件名称
     * 例：1a95563a-9c31-476e-945b-c435322aa506.jpg
     *
     * @return
     */
    public String getPath() {
        if (media.containsKey("path")) {
            return media.getString("path");
        }
        return null;
    }

    /**
     * 选择是否使用备用服务器
     * 哔咔请求的地址和资源提供的地址不一致
     * 资源地址似乎有一定的访问限制
     *
     * @param usingBackupServer
     */
    public void setUsingBackupServer(boolean usingBackupServer) {
        this.usingBackupServer = usingBackupServer;
    }

    /**
     * 获取文件的原始服务器
     * 例：https://storage1.picacomic.com
     *
     * @return
     */
    public String getFileServer() {
        if (media.containsKey("fileServer")) {
            if (usingBackupServer) {
                return "https://s3.picacomic.com";
            }
            return media.getString("fileServer");
        }
        return null;
    }

    /**
     * 获取文件的URL
     * 注意：这个URL非json中的数据，只是根据规则构造的，不保证未来能用。
     *
     * @return
     */
    public String getURL() {
        return getFileServer() + "/static/" + getPath();
    }

    @Override
    public String toString() {
        return this.media.toString();
    }
}
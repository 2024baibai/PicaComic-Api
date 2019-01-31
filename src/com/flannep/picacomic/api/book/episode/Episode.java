package com.flannep.picacomic.api.book.episode;

import net.sf.json.JSONObject;

/**
 * 代表一个章节
 */
public class Episode {

    private JSONObject docs;

    Episode(JSONObject docs) {
        this.docs = docs;
    }

    /**
     * 此章节的ID
     * 目前没什么用
     *
     * @return
     */
    public String get_id() {
        if (docs.containsKey("_id")) {
            return docs.getString("_id");
        }
        return null;
    }

    /**
     * 此章节的名称
     * 例：第一章
     *
     * @return
     */
    public String getTitle() {
        if (docs.containsKey("title")) {
            return docs.getString("title");
        }
        return null;
    }

    /**
     * 此章节的编号，用于获取本子地址
     * 例: 1
     *
     * @return
     */
    public int getOrder() {
        if (docs.containsKey("order")) {
            return docs.getInt("order");
        }
        return -1;
    }

    /**
     * 此章节的更新时间
     * 例：2018-11-24T12:49:53.201Z
     *
     * @return
     */
    public String getUpdated_at() {
        if (docs.containsKey("updated_at")) {
            return docs.getString("updated_at");
        }
        return null;
    }


}

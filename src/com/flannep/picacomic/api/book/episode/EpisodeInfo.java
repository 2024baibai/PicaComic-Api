package com.flannep.picacomic.api.book.episode;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 代表章节信息
 */
public class EpisodeInfo {

    private JSONObject eps;

    public EpisodeInfo(JSONObject eps) {
        this.eps = eps;
    }

    /**
     * 获取所有章节
     *
     * @return 获取失败返回null，不存在章节返回一个空数组
     */
    public Episode[] getEpisodes() {
        if (eps.containsKey("docs")) {
            JSONArray arr = eps.getJSONArray("docs");
            Episode[] epi = new Episode[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                epi[i] = new Episode(arr.getJSONObject(i));
            }
            return epi;
        }
        return null;
    }

    /**
     * 获取章节总数
     *
     * @return
     */
    public int getTotal() {
        if (eps.containsKey("total")) {
            return eps.getInt("total");
        }
        return -1;
    }

    /**
     * 获取单页限制的章节数量
     * 到目前还没看到有翻页需求的
     *
     * @return
     */
    public int getLimit() {
        if (eps.containsKey("limit")) {
            return eps.getInt("limit");
        }
        return -1;
    }

    /**
     * 获取当前页的页码
     *
     * @return
     */
    public int getPage() {
        if (eps.containsKey("page")) {
            return eps.getInt("page");
        }
        return -1;
    }

    /**
     * 获取总页码
     *
     * @return
     */
    public int getPages() {
        if (eps.containsKey("pages")) {
            return eps.getInt("pages");
        }
        return -1;
    }


}


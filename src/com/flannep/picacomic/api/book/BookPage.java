package com.flannep.picacomic.api.book;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 代表本子的图片信息
 * 截止19/01/30 一个对象包含20张图片的信息
 */
public class BookPage {

    private JSONObject pages;
    private JSONObject ep;

    BookPage(JSONObject data) {
        if (data.containsKey("pages") && data.containsKey("ep")) {
            this.pages = data.getJSONObject("pages");
            this.ep = data.getJSONObject("ep");
        } else {
            throw new IllegalArgumentException("非法的构造函数:\n" + data.toString());
        }
    }

    /**
     * 获取图片总数
     *
     * @return
     */
    public int getTotal() {
        if (pages.containsKey("total")) {
            return pages.getInt("total");
        }
        return -1;
    }

    /**
     * 获取单页图片限制
     *
     * @return
     */
    public int getLimit() {
        if (pages.containsKey("limit")) {
            return pages.getInt("limit");
        }
        return -1;
    }

    /**
     * 获取当前页码
     * 20/40张图片为1页？
     *
     * @return
     */
    public int getPage() {
        if (pages.containsKey("page")) {
            return pages.getInt("page");
        }
        return -1;
    }

    /**
     * 获取总页码
     *
     * @return
     */
    public int getPages() {
        if (pages.containsKey("pages")) {
            return pages.getInt("pages");
        }
        return -1;
    }

    /**
     * 获取本子的图片资源
     *
     * @return 不存在此字段返回null，如果本页没有内容返回空白数组
     */
    public Media[] getPics() {
        if (pages.containsKey("docs")) {
            JSONArray arr = pages.getJSONArray("docs");
            Media[] mdr = new Media[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                mdr[i] = new Media(arr.getJSONObject(i).getJSONObject("media"));
            }
            return mdr;
        }
        return null;
    }

    /**
     * 获取此章节的ID
     * 目前没什么用处
     *
     * @return
     */
    public String get_EpId() {
        if (ep.containsKey("_id")) {
            return ep.getString("_id");
        }
        return null;
    }

    /**
     * 获取此章节的标题
     *
     * @return
     */
    public String getEpTitle() {
        if (ep.containsKey("title")) {
            return ep.getString("title");
        }
        return null;
    }


}

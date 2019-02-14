package com.flannep.picacomic.api.book;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 代表请求页的返回列表
 * 包括：我喜欢的本子列表，搜索的本子列表
 */
public class PicaBookResult {

    private JSONObject comic;

    public PicaBookResult(JSONObject data) {
        if (!data.containsKey("comics")) {
            throw new IllegalArgumentException("非法的构造函数:\n" + data.toString());
        }
        this.comic = data.getJSONObject("comics");
    }

    /**
     * 获取当前的页码
     *
     * @return 无法获取返回-1
     */
    public int getCurrentPage() {
        if (comic.containsKey("page")) {
            return comic.getInt("page");
        }
        return -1;
    }

    /**
     * 获取总页码
     *
     * @return 无法获取返回-1
     */
    public int getPages() {
        if (comic.containsKey("pages")) {
            return comic.getInt("pages");
        }
        return -1;
    }

    /**
     * 获取每页的数量限制
     *
     * @return 无法获取返回-1
     */
    public int getLimit() {
        if (comic.containsKey("limit")) {
            return comic.getInt("limit");
        }
        return -1;
    }

    /**
     * 获取收藏的本子总数
     *
     * @return 无法获取返回-1
     */
    public int getTotal() {
        if (comic.containsKey("total")) {
            return comic.getInt("total");
        }
        return -1;
    }

    /**
     * 获取本页所有喜欢的本子
     *
     * @return
     */
    public BookSimpleInfo[] getBooks() {
        if (comic.containsKey("docs")) {

            JSONArray docs = comic.getJSONArray("docs");
            BookSimpleInfo[] books = new BookSimpleInfo[docs.size()];
            for (int i = 0; i < docs.size(); i++) {
                books[i] = new BookSimpleInfo(docs.getJSONObject(i));
            }
            return books;
        }
        return null;
    }

    @Override
    public String toString() {
        return comic.toString();
    }
}

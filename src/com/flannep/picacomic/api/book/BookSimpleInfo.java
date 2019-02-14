package com.flannep.picacomic.api.book;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 代表每本本子的简单信息
 */
public class BookSimpleInfo {

    private JSONObject comic;

    /**
     * 构造函数
     * 应当是comic键的value
     *
     * @param comic
     */
    public BookSimpleInfo(JSONObject comic) {
        this.comic = comic;
    }

    /**
     * 获取此本子的唯一ID
     * 注意，本子字段包含了两个id（_id和id），这里取的是_id字段
     * 如果需要取id字段，使用getID方法
     * 截止19/01/28，这两个方法返回的ID都是相同的。
     *
     * @return
     */
    public String get_ID() {
        if (comic.containsKey("_id")) {
            return comic.getString("_id");
        }
        return null;
    }


    /**
     * 获取本子的标题
     * 例：つぐちゃんかわいそう日記
     *
     * @return
     */
    public String getTitle() {
        if (comic.containsKey("title")) {
            return comic.getString("title");
        }
        return null;
    }

    /**
     * 获取本子作者
     *
     * @return
     */
    public String getAuthor() {
        if (comic.containsKey("author")) {
            return comic.getString("author");
        }
        return null;
    }

    /**
     * 获取本子的页码总数
     *
     * @return
     */
    public int getPagesCount() {
        if (comic.containsKey("pagesCount")) {
            return comic.getInt("pagesCount");
        }
        return -1;
    }

    /**
     * 获取本子的章节总数
     *
     * @return
     */
    public int getEpsCount() {
        if (comic.containsKey("epsCount")) {
            return comic.getInt("epsCount");
        }
        return -1;
    }

    /**
     * 本子是否完结
     *
     * @return 未完结为false，完结为true，如果无法获取默认为false
     */
    public boolean isFinished() {
        if (comic.containsKey("finished")) {
            return comic.getBoolean("finished");
        }
        return false;
    }

    /**
     * 获取本子的分类
     * 例："短篇" "姐姐系"
     *
     * @return 如果不存在返回一个空的String数组
     */
    public String[] getCategories() {
        if (comic.containsKey("categories")) {
            JSONArray arr = comic.getJSONArray("categories");
            String[] str = new String[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                str[i] = arr.getString(i);
            }
            return str;
        }
        return new String[0];
    }

    /**
     * 获取本子的ID
     * 注意，本子字段包含了两个id（_id和id），这里取的是id字段
     * 如果需要取_id字段，使用get_ID方法
     * 截止19/01/28，这两个方法返回的ID都是相同的。
     *
     * @return
     */
    public String getID() {
        if (comic.containsKey("id")) {
            return comic.getString("id");
        }
        return null;
    }

    /**
     * 获取喜欢此本子的人数
     *
     * @return
     */
    public int getLikesCount() {
        if (comic.containsKey("likesCount")) {
            return comic.getInt("likesCount");
        }
        return -1;
    }

    /**
     * 获取文件封面信息
     *
     * @return
     */
    public Media getThumb() {
        if (comic.containsKey("thumb")) {
            return new Media(comic.getJSONObject("thumb"));
        }
        return null;
    }


}



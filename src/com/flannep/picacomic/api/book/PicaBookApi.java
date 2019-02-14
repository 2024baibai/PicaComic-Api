package com.flannep.picacomic.api.book;

import com.flannep.picacomic.api.PicaHeader;
import com.flannep.picacomic.api.PicaResult;
import com.flannep.picacomic.api.book.episode.EpisodeInfo;

import java.net.URLEncoder;

/**
 * 本子的API
 * 获取本子信息，搜索本子等
 */
public class PicaBookApi {

    private String authorization;

    public PicaBookApi(String authorization) {
        this.authorization = authorization;
    }

    /**
     * 搜索本子
     * 默认搜索第一页
     *
     * @param keyword 关键词
     * @return
     * @throws Exception
     */
    public PicaBookResult search(String keyword) throws Exception {
        return search(1, keyword);
    }

    /**
     * 指定页码的搜索本子
     *
     * @param page    页码
     * @param keyword 关键词
     * @return
     * @throws Exception
     */
    public PicaBookResult search(int page, String keyword) throws Exception {
        //URLEncoder.encode()
        String url = String.format("https://picaapi.picacomic.com/comics/search?page=%d&q=%s", page, URLEncoder.encode(keyword, "utf8"));
        PicaHeader header = new PicaHeader();
        header.setAuthorization(this.authorization);
        header.setUrl(url);
        PicaResult result = PicaResult.getPicaResult(header);
        if (result.hasError()) {
            System.err.println(result.toString());
            throw new Exception(result.getMessage());
        }
        return new PicaBookResult(result.getData());
    }


    /**
     * 获取本子的详细信息
     *
     * @param bookID 本子的唯一ID
     * @return
     * @throws Exception
     */
    public BookDetailInfo getBookDetail(String bookID) throws Exception {
        PicaHeader header = new PicaHeader();

        header.setAuthorization(authorization);
        header.setUrl("https://picaapi.picacomic.com/comics/" + bookID);

        PicaResult pr = PicaResult.getPicaResult(header);
        return new BookDetailInfo(pr.getData().getJSONObject("comic"));
    }

    /**
     * 获取本子的章节信息
     *
     * @param bookID 本子的唯一ID
     * @param page   页码，可从本子信息里获取，一般来说40章节为1页，目前哔咔尚未看到有超过40章的本子
     * @return
     * @throws Exception 请求失败抛出
     */
    public EpisodeInfo getEpisodeInfo(String bookID, int page) throws Exception {
        PicaHeader header = new PicaHeader();
        header.setUrl(String.format("https://picaapi.picacomic.com/comics/%s/eps?page=%d", bookID, page));
        header.setAuthorization(authorization);
        PicaResult pr = PicaResult.getPicaResult(header);
        return new EpisodeInfo(pr.getData().getJSONObject("eps"));
    }

    /**
     * 重载 获取本子的章节信息
     *
     * @param info 本子信息
     * @param page
     * @return
     * @throws Exception
     */
    public EpisodeInfo getEpisodeInfo(BookSimpleInfo info, int page) throws Exception {
        return getEpisodeInfo(info.get_ID(), page);
    }

    /**
     * 获取本子的页面
     * 即点击播放后显示的图片等信息
     *
     * @param bookID       本子的唯一ID
     * @param episodeOrder 章节编号
     * @param page         页码：20/40张图片为1页，可从返回的对象中getPages()获取所有页码和当前页码
     * @return
     * @throws Exception 出现访问失败则抛出任何异常
     */
    public BookPage getBookPage(String bookID, int episodeOrder, int page) throws Exception {
        PicaHeader header = new PicaHeader();
        header.setAuthorization(authorization);
        header.setUrl(String.format("https://picaapi.picacomic.com/comics/%s/order/%d/pages?page=%d", bookID, episodeOrder, page));
        PicaResult pr = PicaResult.getPicaResult(header);
        return new BookPage(pr.getData());
    }


}

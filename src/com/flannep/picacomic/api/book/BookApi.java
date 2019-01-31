package com.flannep.picacomic.api.book;

import com.flannep.picacomic.api.PicaHeader;
import com.flannep.picacomic.api.PicaResult;
import com.flannep.picacomic.api.book.episode.EpisodeInfo;

/**
 * 代表本子的API
 */
public class BookApi {

    private String authrization;

    public BookApi(String authorization) {
        this.authrization = authorization;
    }

    /**
     * 获取本子的详细信息
     *
     * @param bookID 本子的唯一ID
     * @return
     * @throws Exception
     */
    public BookDetail getBookDetail(String bookID) throws Exception {
        PicaHeader header = new PicaHeader();

        header.setAuthorization(authrization);
        header.setUrl("https://picaapi.picacomic.com/comics/" + bookID);

        PicaResult pr = PicaResult.getPicaResult(header);
        return new BookDetail(pr.getData().getJSONObject("comic"));
    }

    /**
     * 获取本子的章节信息
     *
     * @param bookID 本子的唯一ID
     * @param page   页码，可从本子信息里获取，一般来说40章节为1页，目前哔咔尚未看到有超过40章的本子
     * @return
     * @throws Exception 无法获取到任何信息时抛出
     */
    public EpisodeInfo getEpisodeInfo(String bookID, int page) throws Exception {
        PicaHeader header = new PicaHeader();
        header.setUrl(String.format("https://picaapi.picacomic.com/comics/%s/eps?page=%d", bookID, page));
        header.setAuthorization(authrization);
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
     * @param bookID  本子的唯一ID
     * @param episode 章节
     * @param page    页码：20/40张图片为1页，可从返回的对象中getPages()获取所有页码和当前页码
     * @return
     * @throws Exception 出现访问失败则抛出任何异常
     */
    public BookPage getBookPage(String bookID, int episode, int page) throws Exception {
        PicaHeader header = new PicaHeader();
        header.setAuthorization(authrization);
        header.setUrl(String.format("https://picaapi.picacomic.com/comics/%s/order/%d/pages?page=%d", bookID, episode, page));
        PicaResult pr = PicaResult.getPicaResult(header);
        return new BookPage(pr.getData());
    }


}

package com.flannep.test;


import com.flannep.picacomic.api.PicaHeader;
import com.flannep.picacomic.api.PicaLogin;
import com.flannep.picacomic.api.book.*;
import com.flannep.picacomic.api.book.episode.EpisodeInfo;
import com.flannep.picacomic.api.users.PicaUserApi;
import com.flannep.picacomic.api.util.FileUtil;
import com.flannep.picacomic.api.util.NetUtil;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {

        String username = "123456789@qq.com";
        String password = "123456789";

        //登陆，authorization可以保存一段时间（大约1周？退出登录不影响）
        String authorization = new PicaLogin(username, password).login();
        //用户Api
        PicaUserApi puApi = new PicaUserApi(authorization);

        PicaBookResult result = puApi.getMyFavorite(1);

        int page = result.getPages();
        for (int i = 1; i <= page; i++) {
            result = puApi.getMyFavorite(i);
            for (BookSimpleInfo info : result.getBooks()) {
                System.out.println(info.getTitle());
            }
        }

        //目录必须存在
        //下载所有喜欢的本子样例
        //downloadAllFavorite(puApi, authorization, "C:/Users/用户/Desktop/喜欢的本子");

    }


    public static void printTitle() {
        System.out.println("======================PicaComic CommandLine-Interface 命令行版===========================");
        System.out.println("██████╗ ██╗ ██████╗ █████╗  ██████╗ ██████╗ ███╗   ███╗██╗ ██████╗     ██████╗██╗     ██╗");
        System.out.println("██╔══██╗██║██╔════╝██╔══██╗██╔════╝██╔═══██╗████╗ ████║██║██╔════╝    ██╔════╝██║     ██║");
        System.out.println("██████╔╝██║██║     ███████║██║     ██║   ██║██╔████╔██║██║██║         ██║     ██║     ██║");
        System.out.println("██╔═══╝ ██║██║     ██╔══██║██║     ██║   ██║██║╚██╔╝██║██║██║         ██║     ██║     ██║");
        System.out.println("██║     ██║╚██████╗██║  ██║╚██████╗╚██████╔╝██║ ╚═╝ ██║██║╚██████╗    ╚██████╗███████╗██║");
        System.out.println("╚═╝     ╚═╝ ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝ ╚═════╝     ╚═════╝╚══════╝╚═╝");
        System.out.println("===========================在命令行轻松阅读本子，你懂的!==================================");
    }

    /**
     * 打印进度条
     *
     * @param pct    当前进度
     * @param all    总进度
     * @param indent 缩进tab
     */
    public static void printProgressBar(int pct, int all, int indent) {

        String title = String.format("[%d/%d %d%%]", pct, all, (int) ((double) pct * 100 / all));
        StringBuilder sb = new StringBuilder();


        int lenth = (int) ((double) pct * 100 / all) / 4;

        //打印的长度
        for (int i = 1; i <= lenth; i++) {
            sb.append("=");
        }
        sb.append(">");

        for (int i = 0; i < title.length() + sb.toString().length(); i++) {
            System.out.print(" ");
        }
        System.out.print("\r");
        for (int i = 0; i < indent; i++) {
            System.out.print("\t");
        }
        System.out.print(title + sb.toString());
    }

    /**
     * 样例：下载所有喜欢的本子
     * 懒得封装了
     *
     * @param puApi
     * @throws Exception
     */
    public static void downloadAllFavorite(PicaUserApi puApi, String authorization, String path) throws Exception {
        //获取我喜欢的本子，由于不知道总页码，先获取第一页
        PicaBookResult favo = puApi.getMyFavorite(1);
        //获取到页码后遍历每一页
        for (int i = 1; i <= favo.getPages(); i++) {
            System.out.println("\n总页码下载进度:");
            printProgressBar(i, favo.getPages(), 0);
            favo = puApi.getMyFavorite(i);
            //获取本页上本子的数量
            int booksLenth = favo.getBooks().length;
            for (int j = 0; j < booksLenth; j++) {
                System.out.println("\n\t本页本子下载进度:");
                printProgressBar(j, booksLenth, 1);
                //获取本子的详细信息
                PicaBookApi bapi = new PicaBookApi(authorization);
                BookDetailInfo binfo = bapi.getBookDetail(favo.getBooks()[j].get_ID());
                System.out.println("\n\t当前下载: " + binfo.getTitle());
                System.out.println("\t分类:" + Arrays.toString(binfo.getCategories()));
                System.out.println("\t标签:" + Arrays.toString(binfo.getTags()));
                //获取此本子章节的页码，一般为1（要超过40章才有翻页）
                for (int k = 1; k <= binfo.getEpsCount(); k++) {
                    EpisodeInfo eps = bapi.getEpisodeInfo(binfo.get_ID(), k);
                    //本子章节的数量
                    int epsLenth = eps.getEpisodes().length;
                    //遍历每个章节
                    for (int l = 0; l < epsLenth; l++) {
                        System.out.println("\t\t当前本子章节进度:");
                        printProgressBar(l + 1, epsLenth, 2);
                        int order = eps.getEpisodes()[l].getOrder();
                        //todo 可能time out
                        //获取此章节本子的总页码数
                        BookPage page = bapi.getBookPage(binfo.get_ID(), order, 1);
                        System.out.println("\n\t\t当前下载章节: " + page.getEpTitle());
                        //此本子的页码
                        int pageLenth = page.getPages();
                        //遍历每一页
                        for (int m = 1; m <= pageLenth; m++) {
                            System.out.println("\t\t\t" + page.getEpTitle() + " 下载进度:");
                            printProgressBar(m, pageLenth, 3);
                            page = bapi.getBookPage(binfo.get_ID(), order, m);
                            //获取本页面上所有的图片资源
                            //此页图片的数量
                            int picLenth = page.getPics().length;
                            long start = System.currentTimeMillis();
                            System.out.println("\n\t\t\t\t当前页面下载进度:");
                            //遍历所有图片资源
                            for (int n = 0; n < picLenth; n++) {
                                printProgressBar(n + 1, picLenth, 4);
                                Media mda = page.getPics()[n];
                                //主服务器似乎有请求数量的限制，建议设置此选项
                                mda.setUsingBackupServer(true);
                                //跳过重复本子
                                if (FileUtil.isFileExist(path,
                                        binfo.getTitle(),
                                        page.getEpTitle(),
                                        mda.getOriginalName())) {
                                    continue;
                                }
                                //创建一个哔咔请求头
                                PicaHeader header = new PicaHeader();
                                header.setUrl(mda.getURL());
                                //必须要添加host，否则访问一定次数后会403
                                header.setHost("s3.picacomic.com");
                                //其他请求头不一定需要
                                //header.setUser_agent("Dalvik/1.6.0 (Linux; U; Android 4.4.2;Build/V10R22A)");
                                //header.setConnection("Keep-Alive");
                                //header.setAccept_encoding("gzip");

                                byte[] data = null;
                                int cnt = 0;
                                while (data == null) {
                                    try {
                                        //获取图片数据
                                        data = NetUtil.getFile(header);
                                    } catch (FileNotFoundException e) {
                                        System.err.println(e.getMessage());
                                        break;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        if (cnt > 10) {
                                            System.err.println("连续10次出现异常，跳过处理");
                                            break;
                                        }
                                        System.err.println("出现异常，等待30s后重试");
                                        Thread.sleep(1000 * 30L);

                                        cnt++;
                                    }
                                }

                                if (data == null) {
                                    System.err.println("跳过:" + mda.getOriginalName());
                                    continue;
                                }
                                //保存到文件
                                FileUtil.saveFile(path,
                                        binfo.getTitle(),
                                        page.getEpTitle(),
                                        mda.getOriginalName(),
                                        data, true);

                            }
                            long end = System.currentTimeMillis();
                            System.out.println(String.format("\n\t\t\t\t\t用时: %ds", (int) ((end - start) / 1000)));
                        }
                    }
                }
            }
        }
    }


}


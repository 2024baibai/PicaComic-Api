package com.flannep.picacomic.api;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtil {


    public static String sendGet(PicaHeader header, boolean convertUnicode) throws Exception {
        return sendGet(header.getUrl(), header.getHttpHeader(), convertUnicode);
    }

    /**
     * 发送get请求
     *
     * @return
     */
    public static String sendGet(String url, Map<String, String> header, boolean convertUnicode) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        URL urls = new URL(url);

        // 打开和URL之间的连接
        HttpURLConnection conn = (HttpURLConnection) urls.openConnection();

        if (header != null) {
            //添加请求头
            for (String key : header.keySet()) {
                conn.setRequestProperty(key, header.get(key));
            }

            conn.setConnectTimeout(1000 * 30);
            conn.setReadTimeout(1000 * 30);
        }

        // 建立实际的连接
        try {
            conn.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 定义 BufferedReader输入流来读取URL的响应
        InputStream inputStream = null;
        int code = conn.getResponseCode();
        if (code >= 200 && code < 400) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }

        in = new BufferedReader(
                new InputStreamReader(inputStream));


        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }

        in.close();

        if (convertUnicode) {
            sb = new StringBuilder(unicodeToString(sb.toString()));
        }
        return sb.toString();
    }


    public static String sendPost(PicaHeader header, String param, boolean convertUnicode) throws Exception {
        return sendPost(header.getUrl(), header.getHttpHeader(), param, convertUnicode);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, String> header, String param, boolean convertUnicode) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();

        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

        if (header != null) {
            // 设置通用的请求属性
            for (String key : header.keySet()) {
                //System.out.println("添加请求头" + key + " | " + header.get(key));
                conn.setRequestProperty(key, header.get(key));
            }
        }

        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);

        //conn.connect();

        // 获取URLConnection对象对应的输出流
        out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        out.print(param);
        // flush输出流的缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应

        InputStream inputStream = null;
        int code = conn.getResponseCode();
        if (code >= 200 && code < 400) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }

        in = new BufferedReader(
                new InputStreamReader(inputStream));

        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }

        //使用finally块来关闭输出流、输入流


        out.close();

        in.close();

        if (convertUnicode) {
            sb = new StringBuilder(unicodeToString(sb.toString()));
        }
        return sb.toString();
    }


    public static byte[] getFile(PicaHeader header) throws Exception {
        return getFile(header.getUrl(), header.getHttpHeader());
    }

    public static byte[] getFile(String url, Map<String, String> header) throws Exception {
        /*return IOUtils.toByteArray(new URL(url));*/

        URL urls = new URL(url);

        // 打开和URL之间的连接
        HttpURLConnection conn = (HttpURLConnection) urls.openConnection();

        if (header != null) {
            //添加请求头
            for (String key : header.keySet()) {
                conn.setRequestProperty(key, header.get(key));
            }
            conn.setConnectTimeout(1000 * 30);
            conn.setReadTimeout(1000 * 30);
        }

        return IOUtils.toByteArray(conn);

    }


    /**
     * unicode转中文
     *
     * @param str
     * @return
     * @author yutao
     * @date 2017年1月24日上午10:33:25
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }
}

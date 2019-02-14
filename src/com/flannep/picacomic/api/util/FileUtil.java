package com.flannep.picacomic.api.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 文件操作的工具
 *
 * @author FlanN
 */
public class FileUtil {

    private static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

    /**
     * 文件名过滤
     *
     * @param str 需要过滤的名称
     * @return
     */
    private static String filenameFilter(String str) {
        return str == null ? null : FilePattern.matcher(str).replaceAll("");
    }

    /**
     * 写二进制文件到磁盘
     *
     * @param path        路径（必须存在）
     * @param bookName    本子名称，不存在则自动创建
     * @param episodeName 章节名称，不存在则自动创建
     * @param filename    文件名称，不存在则自动创建
     * @param data        要写入的二进制数据
     * @param skipExist   如果文件存在则跳过（不会比较内容是否相同）
     * @throws Exception
     */
    public static void saveFile(String path, String bookName, String episodeName, String filename, byte[] data, boolean skipExist) throws Exception {

        path = path.trim();
        bookName = filenameFilter(bookName).trim();
        episodeName = filenameFilter(episodeName).trim();
        filename = filenameFilter(filename).trim();

        File dir = new File(path + bookName);
        if (!dir.isDirectory() || !dir.exists()) {
            dir.mkdir();
        }
        dir = new File(path + bookName + "/" + episodeName);
        if (!dir.isDirectory() || !dir.exists()) {
            dir.mkdir();
        }

        File target = new File(path + bookName + "/" + episodeName + "/" + filename);
        if (target.exists() && skipExist) {
            return;
        }
        FileUtils.writeByteArrayToFile(target, data);
    }

    /**
     * 判断文件是否存在
     *
     * @param path        路径
     * @param bookName    本子名称
     * @param episodeName 章节名称
     * @param filename    目标文件名
     * @return
     */
    public static boolean isFileExist(String path, String bookName, String episodeName, String filename) {
        path = path.trim();
        bookName = filenameFilter(bookName).trim();
        episodeName = filenameFilter(episodeName).trim();
        filename = filenameFilter(filename).trim();
        File target = new File(path + bookName + "/" + episodeName + "/" + filename);
        return target.exists();
    }

}

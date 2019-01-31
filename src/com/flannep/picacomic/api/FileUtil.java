package com.flannep.picacomic.api;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.regex.Pattern;

public class FileUtil {

    private static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

    public static String filenameFilter(String str) {
        return str == null ? null : FilePattern.matcher(str).replaceAll("");
    }

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

    public static boolean isFileExist(String path, String bookName, String episodeName, String filename) {
        path = path.trim();
        bookName = filenameFilter(bookName).trim();
        episodeName = filenameFilter(episodeName).trim();
        filename = filenameFilter(filename).trim();
        File target = new File(path + bookName + "/" + episodeName + "/" + filename);
        return target.exists();
    }

}

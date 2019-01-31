package com.flannep.test;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class HtmlBuilder {


    private List<String> pic = new ArrayList<>();
    private String title = "test";

    public void addPic(String url) {
        pic.add(String.format("<img src=\"%s\">", url) + "<br>");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {


        StringBuilder sb = new StringBuilder();
        sb.append("\n" +
                "        <!DOCTYPE html>\n" +
                "<html>\n" +
                "<head> \n" +
                "<meta charset=\"utf-8\"> \n" +
                "<title>" + title + "</title> \n");

        for (String s : pic) {
            sb.append(s);
            sb.append("\n");
        }
        sb.append("</head>\n" +
                "<body>");

        return sb.toString();
    }

    public void toFile(String path, String fileName) throws Exception {
        PrintWriter pw = new PrintWriter(path + fileName, "UTF-8");
        pw.write(toString());
        pw.flush();
        pw.close();
    }

}

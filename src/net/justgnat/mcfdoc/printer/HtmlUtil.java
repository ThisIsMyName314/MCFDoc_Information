package net.justgnat.mcfdoc.printer;

public class HtmlUtil {

    public static String h5(String content) {
        return "<h5>" + content + "</h5>";
    }

    public static String h4(String content) {
        return "<h4>" + content + "</h4>";
    }

    public static String h2(String content) {
        return "<h2>" + content + "</h2>";
    }

    public static String h1(String content) {
        return "<h1>" + content + "</h1>";
    }

    public static String h3(String content) {
        return "<h3>" + content + "</h3>";
    }

    public static String div(String clazz, String content) {
        return "<div class=\"" + clazz + "\">" +
                content + "</div>";
    }

    public static String colour(String colour, String content) {
        return "<font color=\"#" + colour + "\">" + content + "</font>";
    }

    public static String p(String content) {
        return "<p>" + content + "</p>";
    }

}

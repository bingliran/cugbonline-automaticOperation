package sample.util;

public class SessionUtil {
    private static String cookie;

    public static String getCookie() {
        return cookie;
    }

    public static void setCookie(String cookie) {
        SessionUtil.cookie = cookie;
    }
}

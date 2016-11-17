/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network;

public class Http {

    public static class Headers {
        public static final String ACCEPT = "Accept";
        public static final String ACCEPT_CHARSET = "Accept-Charset";
        public static final String ACCEPT_ENCODING = "Accept-Encoding";
        public static final String ACCEPT_VERSION = "X-Accept-Version";
        public static final String AUTHORIZATION = "Authorization";
        public static String makeBearerAuthorization(String token) {
            return "Bearer " + token;
        }
        public static final String CONTENT_ENCODING = "Content-Encoding";
        public static final String CONTENT_LENGTH = "Content-Length";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String REFERER = "Referer";
        public static final String USER_AGENT = "User-Agent";
    }

    public static class ContentTypes {
        public static final String FORM = "application/x-www-form-urlencoded";
        public static final String FORM_UTF8 = Http.ContentTypes.withCharset(Http.ContentTypes.FORM, Http.Charsets.UTF8);
        public static final String JSON = "application/json";
        public static final String JSON_UTF8 = Http.ContentTypes.withCharset(ContentTypes.JSON, Http.Charsets.UTF8);
        public static String withCharset(String contentType, String charset) {
            return contentType + "; charset=" + charset;
        }
    }

    public interface Charsets {
        String UTF8 = "UTF-8";
    }

    public interface Encodings {
        String GZIP = "gzip";
    }
}

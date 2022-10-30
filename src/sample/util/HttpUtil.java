package sample.util;


import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * httpUtil 用于进行http操作
 * <p>
 * blr 2020.1.13
 */
public class HttpUtil {


    /**
     * 将得到的post请求内容进一步加工为byte[]
     */
    public static byte[] doPostReBytes(String url, Map<String, String> header, Map<String, String> requestData) throws IOException {
        return EntityUtils.toByteArray(doPost(url, header, requestData));
    }

    /**
     * 将得到的get请求内容进一步加工为byte[]
     */
    public static byte[] doGetReBytes(String url, Map<String, String> header, Map<String, String> requestData) throws IOException {
        return EntityUtils.toByteArray(doGet(url, header, requestData));

    }

    /**
     * 发送get请求,
     *
     * @param url         请求资源路径如果requestData中包含内容则url中不能写有xx=xx
     * @param header      请求头
     * @param requestData 请求参数 xx=xx
     */
    public static HttpEntity doGet(String url, Map<String, String> header, Map<String, String> requestData) throws IOException {
        if (MapUtils.isNotEmpty(requestData)) {
            StringBuilder stringBuilder = new StringBuilder(url);
            stringBuilder.append('?');
            requestData.forEach((o1, o2) -> stringBuilder.append(o1).append('=').append(o2));
            url = stringBuilder.toString();
        }
        HttpGet get = new HttpGet(url);
        setHeader(get, header);
        return execute(get).getEntity();
    }

    /**
     * 仅适用于application/json请求
     * Object requestData 为请求体 会转为json结构数据
     */
    public static HttpEntity doPost(String url, Map<String, String> header, Map<String, ?> requestData) throws IOException {
        return doPost(url, header, requestData, "UTF-8");
    }

    /**
     * 仅适用于application/json请求
     * Object requestData 为请求体 会转为json结构数据
     */
    public static HttpEntity doPost(String url, Map<String, String> header, Map<String, ?> requestData, String charset) throws IOException {
        HttpPost post = new HttpPost(url);
        setHeader(post, header);
        List<NameValuePair> params = new ArrayList<>();
        requestData.forEach((o1, o2) -> {
            if (o2 instanceof List) {
                ((List<?>) o2).forEach(s -> params.add(new BasicNameValuePair(o1, String.valueOf(s))));
            } else {
                params.add(new BasicNameValuePair(o1, String.valueOf(o2)));
            }
        });
        HttpEntity entity = new UrlEncodedFormEntity(params, charset);
        post.setEntity(entity);
        return execute(post).getEntity();
    }

    /**
     * 设置请求头
     */
    private static void setHeader(HttpRequestBase requestBase, Map<String, String> header) {
        requestBase.setHeader("Cookie", SessionUtil.getCookie());
        requestBase.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        requestBase.setHeader("Referer", "http://jiaoxue.cugbonline.cn/meol/jpk/course/layout/newpage/index.jsp?courseId=11367");
        requestBase.setHeader("Host", "jiaoxue.cugbonline.cn");
        if (header != null) {
            Set<Map.Entry<String, String>> set = header.entrySet();
            for (Map.Entry<String, String> entry : set) {
                requestBase.setHeader(entry.getKey().trim(), entry.getValue().trim());
            }
        }
        requestBase.setConfig(RequestConfig.custom().build());
    }

    private static CloseableHttpResponse execute(HttpRequestBase requestBase) throws IOException {
        return HttpClients.createDefault().execute(requestBase);
    }
}


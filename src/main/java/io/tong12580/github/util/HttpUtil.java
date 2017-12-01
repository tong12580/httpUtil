package io.tong12580.github.util;

import io.tong12580.github.config.HttpsClientPoolThread;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuton
 * @version 1.0
 * @description io.tong12580.github.util
 * @since 上午11:38 2017/11/28
 */
public class HttpUtil {

    private static volatile CloseableHttpClient httpClient;
    private static volatile BasicCookieStore cookieStore;

    /**
     * @param url     String
     * @param json    String json字符串
     * @param headers List<Header> 请求头
     * @return HttpResponse
     */
    public static HttpResponse post(String url, String json, List<Header> headers, Map<String, String> formParams)
            throws IOException, URISyntaxException {
        httpClient = HttpsClientPoolThread.getInstance().createHttpOrHttpsClientDefault();
        HttpPost httpPost = new HttpPost(new URIBuilder(url).build());
        if (null != headers && headers.size() > 0) {
            headers.forEach(httpPost::addHeader);
        }
        if (null != json) {
            httpPost.setEntity(new StringEntity(json, Consts.UTF_8));
        }
        if (null != formParams && formParams.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(getBasicNameValuePair(formParams), Consts.UTF_8));
        }
        return httpClient.execute(httpPost);
    }

    /**
     * @param url  String
     * @param json String json字符串
     * @return HttpResponse
     */
    public static HttpResponse post(String url, String json) throws IOException, URISyntaxException {
        return post(url, json, null, null);
    }

    /**
     * post 请求
     *
     * @param url String
     * @return HttpResponse
     */
    public static HttpResponse post(String url) throws IOException, URISyntaxException {
        return post(url, null, null, null);
    }


    /**
     * get 请求
     *
     * @param url     String
     * @param headers List<Header>
     * @return HttpResponse
     */
    public static HttpResponse get(String url, List<Header> headers, Map<String, String> params)
            throws IOException, URISyntaxException {
        httpClient = HttpsClientPoolThread.getInstance().createHttpOrHttpsClientDefault();
        if (null != params && params.size() > 0) {
            url = buildUrlParams(url, params);
        }
        HttpGet httpGet = new HttpGet(new URIBuilder(url).build());
        if (null != headers && headers.size() > 0) {
            headers.forEach(httpGet::addHeader);
        }
        return httpClient.execute(httpGet);
    }

    /**
     * get 请求
     *
     * @param url    String
     * @param params Map<String, String>
     * @return HttpResponse
     */
    public static HttpResponse get(String url, Map<String, String> params) throws IOException, URISyntaxException {
        return get(url, null, params);
    }

    /**
     * get 请求
     *
     * @param url     String
     * @param headers List<Header>
     * @return HttpResponse
     */
    public static HttpResponse get(String url, List<Header> headers) throws IOException, URISyntaxException {
        return get(url, headers, null);
    }

    /**
     * get 请求
     *
     * @param url String
     * @return HttpResponse
     */
    public static HttpResponse get(String url) throws IOException, URISyntaxException {
        return get(url, null, null);
    }

    /**
     * put请求
     *
     * @param url        String
     * @param json       String
     * @param headers    List<Header>
     * @param formParams Map<String, String>
     * @return HttpResponse
     */
    public static HttpResponse put(String url, String json, List<Header> headers, Map<String, String> formParams)
            throws IOException {
        CloseableHttpClient httpClient = HttpsClientPoolThread.getInstance().createHttpOrHttpsClientDefault();
        HttpPut httpPut = new HttpPut(url);
        if (null != headers && headers.size() > 0) {
            headers.forEach(httpPut::addHeader);
        }
        if (null != json) {
            httpPut.setEntity(new StringEntity(json, Consts.UTF_8));
        }
        if (null != formParams && formParams.size() > 0) {
            httpPut.setEntity(new UrlEncodedFormEntity(getBasicNameValuePair(formParams), Consts.UTF_8));
        }
        return httpClient.execute(httpPut);
    }

    /**
     * put 请求
     *
     * @param url String
     * @return HttpResponse
     */
    public static HttpResponse put(String url) throws IOException {
        return put(url, null, null, null);
    }

    /**
     * put 请求
     *
     * @param url  String
     * @param json String
     * @return HttpResponse
     */
    public static HttpResponse put(String url, String json) throws IOException {
        return put(url, json, null, null);
    }

    /**
     * put 请求
     *
     * @param url     String
     * @param json    String
     * @param headers List<Header>
     * @return HttpResponse
     */
    public static HttpResponse put(String url, String json, List<Header> headers) throws IOException {
        return put(url, json, headers, null);
    }

    /**
     * put 请求
     *
     * @param url        String
     * @param headers    List<Header>
     * @param formParams Map<String, String>
     * @return HttpResponse
     */
    public static HttpResponse put(String url, List<Header> headers, Map<String, String> formParams) throws IOException {
        return put(url, null, headers, formParams);
    }

    /**
     * put 请求
     *
     * @param url        String
     * @param formParams Map<String, String>
     * @return HttpResponse
     */
    public static HttpResponse put(String url, Map<String, String> formParams) throws IOException {
        return put(url, null, null, formParams);
    }

    /**
     * patch 请求
     *
     * @param url        String
     * @param json       String
     * @param headers    List<Header>
     * @param formParams Map<String, String>
     * @return HttpResponse
     */
    public static HttpResponse patch(String url, String json, List<Header> headers, Map<String, String> formParams) throws IOException {
        CloseableHttpClient httpClient = HttpsClientPoolThread.getInstance().createHttpOrHttpsClientDefault();
        HttpPatch httpPatch = new HttpPatch(url);
        if (null != headers && headers.size() > 0) {
            headers.forEach(httpPatch::addHeader);
        }
        if (null != json) {
            httpPatch.setEntity(new StringEntity(json, Consts.UTF_8));
        }
        if (null != formParams && formParams.size() > 0) {
            httpPatch.setEntity(new UrlEncodedFormEntity(getBasicNameValuePair(formParams), Consts.UTF_8));
        }
        return httpClient.execute(httpPatch);

    }

    /**
     * patch 请求
     *
     * @param url String
     * @return HttpResponse
     */
    public static HttpResponse patch(String url) throws IOException {
        return patch(url, null, null, null);
    }

    /**
     * patch 请求
     *
     * @param url  String
     * @param json String
     * @return HttpResponse
     */
    public static HttpResponse patch(String url, String json) throws IOException {
        return patch(url, json, null, null);
    }

    /**
     * patch 请求
     *
     * @param url     String
     * @param json    String
     * @param headers List<Header>
     * @return HttpResponse
     */
    public static HttpResponse patch(String url, String json, List<Header> headers) throws IOException {
        return patch(url, json, headers, null);
    }

    /**
     * patch 请求
     *
     * @param url        String
     * @param headers    List<Header>
     * @param formParams Map<String, String>
     * @return HttpResponse
     */
    public static HttpResponse patch(String url, List<Header> headers, Map<String, String> formParams) throws IOException {
        return patch(url, null, headers, formParams);
    }

    /**
     * patch 请求
     *
     * @param url        String
     * @param formParams Map<String, String>
     * @return HttpResponse
     */
    public static HttpResponse patch(String url, Map<String, String> formParams) throws IOException {
        return patch(url, null, null, formParams);
    }

    public static HttpResponse delete(String url, List<Header> headers, Map<String, String> params) throws IOException {
        CloseableHttpClient httpClient = HttpsClientPoolThread.getInstance().createHttpOrHttpsClientDefault();
        if (null != params && params.size() > 0) {
            url = buildUrlParams(url, params);
        }
        HttpDelete httpDelete = new HttpDelete(url);
        if (null != headers && headers.size() > 0) {
            headers.forEach(httpDelete::addHeader);
        }
        return httpClient.execute(httpDelete);
    }


    /**
     * 构建表单参数
     *
     * @param formParams Map<String, String>
     * @return List<BasicNameValuePair>
     */
    private static List<BasicNameValuePair> getBasicNameValuePair(Map<String, String> formParams) {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : formParams.entrySet()) {
            BasicNameValuePair valuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
            pairs.add(valuePair);
        }
        return pairs;
    }

    private static String buildUrlParams(String url, Map<String, String> params) {
        StringBuilder urlMap = new StringBuilder(url);
        urlMap.append("?");
        String keyValue;
        for (String key : params.keySet()) {
            keyValue = params.get(key);
            if (null != keyValue && !"".equals(keyValue)) {
                urlMap.append(key).append("=").append(keyValue).append("&");
            }
        }
        return urlMap.toString();
    }

    /**
     * 获取当前Http客户端状态中的Cookie
     *
     * @param domain    作用域
     * @param port      端口 传null 默认80
     * @param path      Cookie路径 传null 默认"/"
     * @param useSecure Cookie是否采用安全机制 传null 默认false
     * @return String
     */
    public static Map<String, Cookie> getCookie(String domain, Integer port, String path, Boolean useSecure) {
        if (domain == null) {
            return null;
        }
        if (port == null) {
            port = 80;
        }
        if (path == null) {
            path = "/";
        }
        if (useSecure == null) {
            useSecure = false;
        }
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies == null || cookies.isEmpty()) {
            return null;
        }
        CookieOrigin origin = new CookieOrigin(domain, port, path, useSecure);
        DefaultCookieSpec cookieSpec = new DefaultCookieSpec(null, false);
        Map<String, Cookie> retVal = new HashMap<>();
        for (Cookie cookie : cookies) {
            if (cookieSpec.match(cookie, origin)) {
                retVal.put(cookie.getName(), cookie);
            }
        }
        return retVal;
    }
}
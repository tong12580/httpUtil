package io.tong12580.github.util;

import io.tong12580.github.config.HttpsClientPoolThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yuton
 * @version 1.0
 * @description io.tong12580.github.util
 * @since 上午11:38 2017/11/28
 */
public class HttpUtil {

    private static Log log = LogFactory.getLog(HttpUtil.class);

    /**
     * @param url     String
     * @param json    String json字符串
     * @param headers List<Header> 请求头
     * @return HttpResponse
     */
    public static HttpResponse post(String url, String json, List<Header> headers, Map<String, String> formParams) {
        CloseableHttpClient httpClient;
        httpClient = HttpsClientPoolThread.getInstance().createHttpOrHttpsClientDefault();
        HttpPost httpPost = new HttpPost(url);
        if (null != headers && headers.size() > 0) {
            headers.forEach(httpPost::addHeader);
        }
        if (null != json) {
            httpPost.setEntity(new StringEntity(json, Consts.UTF_8));
        }
        if (null != formParams && formParams.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(getBasicNameValuePair(formParams), Consts.UTF_8));
        }
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return response;
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }

    /**
     * @param url  String
     * @param json String json字符串
     * @return HttpResponse
     */
    public static HttpResponse post(String url, String json) {
        return post(url, json, null, null);
    }

    /**
     * post 请求
     *
     * @param url String
     * @return HttpResponse
     */
    public static HttpResponse post(String url) {
        return post(url, null, null, null);
    }


    /**
     * get 请求
     *
     * @param url     String
     * @param headers List<Header>
     * @return HttpResponse
     */
    public static HttpResponse get(String url, List<Header> headers, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpsClientPoolThread.getInstance().createHttpOrHttpsClientDefault();
        if (null != params && params.size() > 0) {
            StringBuilder urlMap = new StringBuilder(url);
            urlMap.append("?");
            Object keyValue;
            for (String key : params.keySet()) {
                keyValue = params.get(key);
                if (null != keyValue && !Objects.equals(keyValue, "")) {
                    urlMap.append(key).append("=").append(keyValue).append("&");
                }
            }
            url = urlMap.toString();
        }
        HttpGet httpGet = new HttpGet(url);
        if (null != headers && headers.size() > 0) {
            headers.forEach(httpGet::addHeader);
        }
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return response;
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }

    /**
     * get 请求
     *
     * @param url    String
     * @param params Map<String, String>
     * @return HttpResponse
     */
    public static HttpResponse get(String url, Map<String, String> params) {
        return get(url, null, params);
    }

    /**
     * get 请求
     *
     * @param url     String
     * @param headers List<Header>
     * @return HttpResponse
     */
    public static HttpResponse get(String url, List<Header> headers) {
        return get(url, headers, null);
    }

    /**
     * get 请求
     *
     * @param url String
     * @return HttpResponse
     */
    public static HttpResponse get(String url) {
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
    public static HttpResponse put(String url, String json, List<Header> headers, Map<String, String> formParams) {
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
        try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
            return response;
        } catch (IOException e) {
            log.error(e);
            return null;
        }
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
}
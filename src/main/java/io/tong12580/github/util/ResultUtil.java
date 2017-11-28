package io.tong12580.github.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author yuton
 * @version 1.0
 * @description io.tong12580.github.util
 * @since 下午5:36 2017/11/28
 */
public class ResultUtil {


    /**
     * 响应体返回
     *
     * @param httpResponse HttpResponse
     * @return String
     */
    public static String getHttpResponseEntiry(HttpResponse httpResponse) throws IOException {
        String result = null;
        HttpEntity httpEntity = httpResponse.getEntity();
        if (null != httpEntity) {
            result = EntityUtils.toString(httpEntity, Consts.UTF_8);
        }
        return result;
    }

    /**
     * 返回状态码
     *
     * @param httpResponse HttpResponse
     * @return int
     */
    public static int getStatus(HttpResponse httpResponse) {
        return httpResponse.getStatusLine().getStatusCode();
    }


}

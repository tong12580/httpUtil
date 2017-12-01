package io.tong12580.github.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yuton
 * @version 1.0
 * @description io.tong12580.github.util
 * @since 上午11:29 2017/12/1
 */
class HttpUtilTest {
    @Test
    void get() {
        HttpResponse httpResponse;
        try {
            httpResponse = HttpUtil.get("https://www.baidu.com/");
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                System.out.println(EntityUtils.toString(entity, Consts.UTF_8));
                System.out.println(HttpUtil.getCookie(null, null, null, null));
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
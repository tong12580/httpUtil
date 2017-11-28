package io.tong12580.github.common;

/**
 * @author yuton
 * @version 1.0
 * @description io.tong12580.github.common
 * @since 上午11:17 2017/11/28
 */
public interface HttpUniversalHeaderParams {
    String HTTP_CONNECTION = "http";
    String HTTPS_CONNECTION = "https";
    int MAX_TOTAL = 200;
    int MAX_CON_PER_ROUTE = 20;
    int TIME_OUT = 10000;
    String USER_AGENT_MSG = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";
    String ACCEPT_LANGUAGE_MSG = "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3";
    String ACCEPT_CHARSET_MSG = "GB2312,utf-8;q=0.7,*;q=0.7";
    String ACCEPT_CHARSET = "Accept-Charset";
    String CONTENT_TYPE_JSON = "application/json;charset=utf-8";
    String ACCEPT_LANGUAGE = "Accept-Language";
    String ACCEPT_ENCODING = "Accept-Encoding";
    String ACCEPT_ENCODING_MSG = "gzip,deflate";
    String SELECT_PUBLIC_IP_ADDRESS = "http://city.ip138.com/ip2city.asp";
    String CONTENT_TYPE_APPLICATION_OCTET_STREAM = "application/octet-stream";
    String LOCATION = "Location";
    String CONTENT_DISPOSITION = "Content-Disposition";
    String APPLICATION_JSON = "application/json";
    String CONTENT_TYPE_TEXT_JSON = "text/json";
    String CONTENT_TYPE_APPLICATION = "application/x-www-form-urlencoded;charset=utf-8";
    String ACCEPT = "Accept";
    String AUTHORIZATION = "Authorization";


}

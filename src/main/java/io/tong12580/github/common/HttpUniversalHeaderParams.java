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
    String USER_AGENT_MSG = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)";
    String ACCEPT_LANGUAGE_MSG = "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3";
    String ACCEPT_CHARSET_MSG = "GB2312,utf-8;q=0.7,*;q=0.7";
    String ACCEPT_CHARSET = "Accept-Charset";
    String CONTENT_TYPE_JSON = "application/json;charset=utf-8";
    String ACCEPT_LANGUAGE = "Accept-Language";
    String ACCEPT_ENCODING = "Accept-Encoding";
    String ACCEPT_ENCODING_MSG = "gzip,deflate";
    String SELECT_PUBLIC_IP_ADDRESS = "http://www.ip138.com/ip2city.asp";
    String CONTENT_TYPE_APPLICATION_OCTET_STREAM = "application/octet-stream";
    String LOCATION = "Location";
    String CONTENT_DISPOSITION = "Content-Disposition";
    String APPLICATION_JSON = "application/json";
    String CONTENT_TYPE_TEXT_JSON = "text/json";
    String CONTENT_TYPE_APPLICATION = "application/x-www-form-urlencoded;charset=utf-8";
    String ACCEPT = "Accept";
    String AUTHORIZATION = "Authorization";


}

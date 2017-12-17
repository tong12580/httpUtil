package io.tong12580.github.csdn;

import io.tong12580.github.common.HttpUniversalHeaderParams;
import io.tong12580.github.config.HttpsClientPoolThread;
import io.tong12580.github.util.HttpUtil;
import io.tong12580.github.util.ResultUtil;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yutong
 * @version 1.0
 * @description
 * @since 2017/12/13 14:01
 */
public class CSNDUtil {

    private static String CSDN_BLOG = "http://blog.csdn.net/";
    private static String CSDN_BLOG_ = "http://blog.csdn.net";
    private static String PROXY_URL = "http://www.xicidaili.com/wn/";
    private static String BAI_DU = "http://www.baidu.com";

    public static List<String> getBlogLinkList(String blogName, Integer pageSize) throws IOException, URISyntaxException {
        List<String> blogHref = new ArrayList<>();
        if (null == pageSize) {
            pageSize = 10;
        }
        int i = 1;
        while (i < pageSize + 1) {
            HttpResponse httpResponse;
            httpResponse = HttpUtil.get(CSDN_BLOG + blogName + "/article/list/" + i);
            if (null == httpResponse || 200 != ResultUtil.getStatus(httpResponse)) {
                break;
            }
            String html = ResultUtil.getHttpResponseEntiry(httpResponse);
            Document document = Jsoup.parse(html);
            Element element = document.getElementsByClass("colu_author_c").get(0);
            if (null != element) {
                Elements elements = element.getElementsByTag("a");
                if (null != elements && 0 != elements.size()) {
                    for (Element e : elements) {
                        String href = e.attr("href");
                        if (null != href) {
                            blogHref.add(href);
                        }
                    }
                }
            }
            i++;
        }
        return blogHref;
    }

    public static List<IpAndPort> getIpList(int i) throws IOException, URISyntaxException {
        List<IpAndPort> imgSrcList = new ArrayList<>();
        HttpResponse httpResponse;
        httpResponse = HttpUtil.get(PROXY_URL + i);
        if (null == httpResponse || 200 != ResultUtil.getStatus(httpResponse)) {
            return null;
        }
        String html = ResultUtil.getHttpResponseEntiry(httpResponse);
        Document document = Jsoup.parse(html);
        Elements tr = document.getElementsByClass("odd");
        for (Element element : tr) {
            IpAndPort ipAndPort = new IpAndPort();
            ipAndPort.setIp(element.child(1).text());
            ipAndPort.setPort(element.child(2).text());
            imgSrcList.add(ipAndPort);
        }
        return imgSrcList;
    }

    /**
     * 代理有效性检测
     *
     * @param ip   String
     * @param port String
     * @return Boolean
     */
    public static Boolean visitBlogLink(String ip, String port) {
        CloseableHttpClient httpClient = HttpsClientPoolThread
                .getInstance()
                .createHttpOrHttpsClientDefault(ip, port);
        HttpGet httpGet = new HttpGet(BAI_DU);
        try {
            return 200 == ResultUtil.getStatus(httpClient.execute(httpGet));
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
//            CSNDUtil.getBlogLinkList("j3oker", 2);
        List<IpAndPort> ipAndPorts = getIpList(1);
        List<String> blogHref = getBlogLinkList("j3oker", 2);
        if (null == ipAndPorts || ipAndPorts.size() < 1) {
            return;
        }
        if (null == blogHref || blogHref.size() < 1) {
            return;
        }
        blogHref.parallelStream().forEach(str -> {
            for (IpAndPort ipAndPort : ipAndPorts) {
                try {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    RequestConfig.Builder builder = RequestConfig
                            .custom()
                            .setConnectionRequestTimeout(HttpUniversalHeaderParams.TIME_OUT)
                            .setConnectTimeout(HttpUniversalHeaderParams.TIME_OUT)
                            .setSocketTimeout(HttpUniversalHeaderParams.TIME_OUT)
                            .setCookieSpec(CookieSpecs.STANDARD);
                    HttpHost proxy = new HttpHost(ipAndPort.getIp(), Integer.parseInt(ipAndPort.getPort()));
                    builder.setProxy(proxy);
                    HttpGet httpGet = new HttpGet();
                    httpGet.setURI(new URI(BAI_DU));
                    httpGet.setConfig(builder.build());
                    if (200 != ResultUtil.getStatus(httpClient.execute(httpGet))) {
                        continue;
                    }
                    httpGet.setURI(new URI(CSDN_BLOG_ + str));
                    System.out.println(ipAndPort.getIp() + " " + ipAndPort.getPort() + " " + httpGet.getURI().toString());
                    httpClient.execute(httpGet);
                } catch (IOException | URISyntaxException e) {
                    continue;
                }
            }
        });
    }
}
package io.tong12580.github.csdn;

import io.tong12580.github.util.HttpUtil;
import io.tong12580.github.util.ResultUtil;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yutong
 * @version 1.0
 * @description
 * @since 2017/12/13 14:01
 */
public class CSNDUtil {

    private static String CSDN_BLOG = "http://blog.csdn.net/";

    public static Map<String, String> getBlogLinkList(String blogName, Integer pageSize) throws IOException, URISyntaxException {
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
        return null;
    }

    public static List<IpAndPort> getIpList() {
        return null;
    }

    public static void main(String[] args) {
        try {
            CSNDUtil.getBlogLinkList("j3oker", 2);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
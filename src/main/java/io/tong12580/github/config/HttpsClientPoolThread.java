package io.tong12580.github.config;

import io.tong12580.github.common.HttpUniversalHeaderParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * <h>https configs</h>
 *
 * @author yuton
 * @version 1.0
 * @description io.tong12580.github.config
 * @since 上午10:59 2017/11/28
 */
public class HttpsClientPoolThread {
    private static volatile HttpsClientPoolThread httpsClientPoolThread;
    private static int RETRY_COUNT = 5;
    private static final Log log = LogFactory.getLog(HttpsClientPoolThread.class);

    private HttpsClientPoolThread() {
    }

    public static HttpsClientPoolThread getInstance() {
        if (null == httpsClientPoolThread) {
            synchronized (HttpsClientPoolThread.class) {
                if (null == httpsClientPoolThread) {
                    httpsClientPoolThread = new HttpsClientPoolThread();
                }
            }
        }
        return httpsClientPoolThread;
    }

    public CloseableHttpClient createHttpOrHttpsClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (chain, authType) -> true)
                    .build();
            SSLConnectionSocketFactory sslCSF = new SSLConnectionSocketFactory(sslContext);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register(HttpUniversalHeaderParams.HTTP_CONNECTION, PlainConnectionSocketFactory.getSocketFactory())
                    .register(HttpUniversalHeaderParams.HTTPS_CONNECTION, sslCSF)
                    .build();
            SocketConfig socketConfig = SocketConfig
                    .custom()
                    .setSoTimeout(HttpUniversalHeaderParams.TIME_OUT)
                    .build();
            PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            poolConnManager.setMaxTotal(HttpUniversalHeaderParams.MAX_TOTAL);
            poolConnManager.setDefaultMaxPerRoute(HttpUniversalHeaderParams.MAX_CON_PER_ROUTE);
            poolConnManager.setDefaultSocketConfig(socketConfig);
            HttpRequestRetryHandler httpRequestRetryHandler = (iOException, count, httpContext) -> {
                if (count >= RETRY_COUNT) {
                    return false;
                }
                if (iOException instanceof NoHttpResponseException) {
                    return false;
                }
                if (iOException instanceof SSLHandshakeException) {
                    return false;
                }
                if (iOException instanceof InterruptedIOException) {
                    return false;
                }
                if (iOException instanceof UnknownHostException) {
                    return false;
                }
                if (iOException instanceof SSLException) {
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
                HttpRequest request = clientContext.getRequest();
                return !(request instanceof HttpEntityEnclosingRequest);
            };
            RequestConfig requestConfig = RequestConfig
                    .custom()
                    .setConnectionRequestTimeout(HttpUniversalHeaderParams.TIME_OUT)
                    .setConnectTimeout(HttpUniversalHeaderParams.TIME_OUT)
                    .setSocketTimeout(HttpUniversalHeaderParams.TIME_OUT)
                    .build();
            List<Header> defaultHeaders = new ArrayList<Header>() {{
                add(new BasicHeader(HTTP.CONTENT_TYPE, HttpUniversalHeaderParams.CONTENT_TYPE_JSON));
                add(new BasicHeader(HTTP.USER_AGENT, HttpUniversalHeaderParams.USER_AGENT_MSG));
                add(new BasicHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE));
                add(new BasicHeader(HttpUniversalHeaderParams.ACCEPT_LANGUAGE, HttpUniversalHeaderParams.ACCEPT_LANGUAGE_MSG));
                add(new BasicHeader(HttpUniversalHeaderParams.ACCEPT_CHARSET, HttpUniversalHeaderParams.ACCEPT_CHARSET_MSG));
                add(new BasicHeader(HttpUniversalHeaderParams.ACCEPT_ENCODING, HttpUniversalHeaderParams.ACCEPT_ENCODING_MSG));
            }};
            return HttpClients
                    .custom()
                    .setConnectionManager(poolConnManager)
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(httpRequestRetryHandler)
                    .setDefaultHeaders(defaultHeaders)
                    .build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            log.error(e);
        }
        return HttpClients.createDefault();
    }
}


package com.test.jm.util;

import com.test.jm.domain.HttpClientResult;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestUtils {

    private static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    private static final String ENCODING = "UTF-8";
    private static final int CONNECT_TIMEOUT = 3000;// 设置连接建立的超时时间为10s
    private static final int SOCKET_TIMEOUT = 3000;
    private static final int MAX_CONN = 1000; // 最大连接数
    private static final int Max_PRE_ROUTE = 400;
//    private static final int MAX_ROUTE = 80;
    private static CloseableHttpClient httpClient; // 发送请求的客户端单例
    private static CookieStore cookieStore;       // 创建CookieStore实例
    private static PoolingHttpClientConnectionManager manager; //连接池管理类
    private static ScheduledExecutorService monitorExecutor;
    private final static Object syncLock = new Object(); // 相当于线程锁,用于线程安全

    /**
     * 对http请求进行基本设置
     * @param httpRequestBase http请求
     */
    private static void setRequestConfig(HttpRequestBase httpRequestBase){
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        httpRequestBase.setConfig(requestConfig);
    }

    /**
     * 获取httpclient实例
     * @return
     */
    public static CloseableHttpClient getHttpClient(){
        if (httpClient == null){
            //多线程下多个线程同时调用getHttpClient容易导致重复创建httpClient对象的问题,所以加上了同步锁
            synchronized (syncLock){
                if (httpClient == null){
                    httpClient = createHttpClient();
                    //开启监控线程,对异常和空闲线程进行关闭
                    monitorExecutor = Executors.newScheduledThreadPool(1);
                    monitorExecutor.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            //关闭异常连接
                            manager.closeExpiredConnections();
                            //关闭5s空闲的连接
                            manager.closeIdleConnections(10, TimeUnit.SECONDS);
                            logger.info("close expired and idle for over 5s connection");
                        }
                    }, 10, 10, TimeUnit.SECONDS);
                }
            }
        }
        return httpClient;
    }

    /**
     * 构建httpclient实例
     * @return
     */
    public static CloseableHttpClient createHttpClient(){
        cookieStore =  new BasicCookieStore();
        ConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", plainSocketFactory)
                .register("https", sslSocketFactory)
                .build();

        manager = new PoolingHttpClientConnectionManager(registry);
        //设置连接参数
        manager.setMaxTotal(MAX_CONN); // 最大连接数
        manager.setDefaultMaxPerRoute(Max_PRE_ROUTE); // 路由最大连接数

//        HttpHost httpHost = new HttpHost(host, port);
//        manager.setMaxPerRoute(new HttpRoute(httpHost), MAX_ROUTE);

        //请求失败时,进行请求重试
        HttpRequestRetryHandler handler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                if (i > 3){
                    //重试超过3次,放弃请求
                    logger.error("retry has more than 3 time, give up request");
                    return false;
                }
                if (e instanceof NoHttpResponseException){
                    //服务器没有响应,可能是服务器断开了连接,应该重试
                    logger.error("receive no response from server, retry");
                    return true;
                }
                if (e instanceof SSLHandshakeException){
                    // SSL握手异常
                    logger.error("SSL hand shake exception");
                    return false;
                }
                if (e instanceof InterruptedIOException){
                    //超时
                    logger.error("InterruptedIOException");
                    return false;
                }
                if (e instanceof UnknownHostException){
                    // 服务器不可达
                    logger.error("server host unknown");
                    return false;
                }
                if (e instanceof ConnectTimeoutException){
                    // 连接超时
                    logger.error("Connection Time out");
                    return false;
                }
                if (e instanceof SSLException){
                    logger.error("SSLException");
                    return false;
                }

                HttpClientContext context = HttpClientContext.adapt(httpContext);
                HttpRequest request = context.getRequest();
                if (!(request instanceof HttpEntityEnclosingRequest)){
                    //如果请求不是关闭连接的请求
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).setRetryHandler(handler).build();
        return client;
    }

    /**
     * 发送get请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static HttpClientResult doGet(String url) throws Exception {
        return doGet(url, null, null, null);
    }

    /**
     * 发送get请求；带请求参数
     *
     * @param url 请求地址
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doGet(String url, String params) throws Exception {
        return doGet(url, null,null, params);
    }

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url 请求地址
     * @param headers 请求头集合
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doGet(String url, String headers, String params) throws Exception {
        return doGet(url, headers,null, params);
    }

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url 请求地址
     * @param headers 请求头集合
     * @param cookies 请求cookie集合
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doGet(String url, String headers,String cookies, String params) throws Exception {
        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        //组装参数
        if (params != null) {
            Set<Map.Entry<String, Object>> entrySet = CommonUtils.strToMap(params).entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        setRequestConfig(httpGet);
        packageHeader(headers, httpGet);
        setCookies(cookies);
        return getHttpClientResult(httpGet);
    }

    /**
     * 发送post请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPost(String url) throws Exception {
        return doPost(url, null, null);
    }

    /**
     * 发送post请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPost(String url, String params) throws Exception {
        return doPost(url, null, params);
    }

    /**
     * 发送post请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPost(String url, String headers,String params) throws Exception {
        return doPost(url, headers, null, params);
    }

    /**
     * 发送post请求；带请求头和请求参数
     *
     * @param url 请求地址
     * @param headers 请求头集合
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPost(String url, String headers,String cookies, String params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        setRequestConfig(httpPost);
        packageHeader(headers, httpPost);
        setCookies(cookies);
        packageParam(params, httpPost);
        return getHttpClientResult(httpPost);
    }

    /**
     * 发送put请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPut(String url) throws Exception {
        return doPut(url);
    }

    /**
     * 发送put请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPut(String url, String params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPut.setConfig(requestConfig);
        packageParam(params, httpPut);
        return getHttpClientResult(httpPut);
    }

    /**
     * 发送delete请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static HttpClientResult doDelete(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpDelete.setConfig(requestConfig);
        return getHttpClientResult(httpDelete);
    }

    /**
     * 发送delete请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doDelete(String url, String params) throws Exception {
        Map<String, Object> map = CommonUtils.strToMap(params);
        map.put("_method", "delete");
        return doPost(url, params);
    }

    /**
     * Description: 封装请求头
     * @param params
     * @param httpMethod
     */
    public static void packageHeader(String params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Map.Entry<String, Object>> entrySet = CommonUtils.strToMap(params).entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
    }

    public static List<Cookie> getCookies(){
        return cookieStore.getCookies();
    }

    public static void setCookies(String cookies){
        if (cookies != null) {
            Set<Map.Entry<String, Object>> entrySet = CommonUtils.strToMap(cookies).entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                BasicClientCookie cookie = new BasicClientCookie(entry.getKey(), String.valueOf(entry.getValue()));
                cookie.setDomain("domain");
                cookie.setExpiryDate(new Date());
                cookieStore.addCookie(cookie);
            }
        }
    }

    public static void clearCookies(){
        cookieStore.clear();
    }

    /**
     * Description: 封装请求参数
     *
     * @param params
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    public static void packageParam(String params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            /*List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, Object>> entrySet = CommonUtils.strToMap(params).entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
            // 设置到请求的http对象中
            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));*/
            StringEntity entity = new StringEntity(params, ENCODING);
            httpMethod.setEntity(entity);
        }
    }

    /**
     * Description: 获得响应结果
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static HttpClientResult getHttpClientResult(HttpRequestBase httpMethod){
        CloseableHttpResponse httpResponse = null;
        // 执行请求
        try {
            httpResponse = getHttpClient().execute(httpMethod, HttpClientContext.create());
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
                Header[] headers= httpResponse.getAllHeaders();
                List<Header> hh = Arrays.asList(headers);
                String header = hh.toString();
                String cookies = getCookies().toString();
                return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), header, content, cookies);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 关闭连接池
     */
    public static void closeConnectionPool(){
        try {
            httpClient.close();
            manager.close();
            monitorExecutor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

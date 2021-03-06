package com.test.jm.util;

import com.test.jm.domain.HttpClientResult;
import com.test.jm.dto.ApiDTO;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
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
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestUtils {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(RequestUtils.class);
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
                            manager.closeIdleConnections(5, TimeUnit.SECONDS);
//                            logger.info("close expired and idle for over 5s connection");
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
                if (i > 1){
                    //重试超过1次,放弃请求
                    logger.error("retry has more than 1 time, give up request");
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

        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(manager)
                .setRetryHandler(handler)
                .setDefaultCookieStore(cookieStore)
                .build();
        return client;
    }

    /**
     * 发送get请求；带请求头和请求参数
     * @return
     * @throws Exception
     */
    public static HttpClientResult doGet(Logger log, HttpClientResult result, ApiDTO apiDTO) throws Exception {
        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(apiDTO.getUrl());
        //组装参数
        if ( apiDTO.getBody()!= null && apiDTO.getParamstype().equals("form") ) {
            Map<String, Object> param = CommonUtils.strToMap(apiDTO.getBody());
            for (String key : param.keySet()) {
                uriBuilder.addParameter(key, (String) param.get(key));
            }
        }
        URI uri = uriBuilder.build();
        HttpGet httpGet = new HttpGet(uri);
        result.setReq_url(httpGet.getURI().toString());
        log.info("Request URL: {}",httpGet.getURI().toString());
        log.info("Request Method: {} {}",httpGet.getRequestLine().getProtocolVersion(),httpGet.getMethod());
        setRequestConfig(httpGet);
        packageHeader(log, result, apiDTO.getHeaders(), httpGet);
        setCookies(log, result, apiDTO.getCookies());

        return getHttpClientResult(log, result, httpGet);
    }

    /**
     * 发送post请求；带请求头和请求参数
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPost(Logger log, HttpClientResult result, ApiDTO apiDTO) throws Exception {
        URL url1 = new URL(apiDTO.getUrl());
        URI uri = new URI(url1.getProtocol(), null,url1.getHost(),url1.getPort(), url1.getPath(), url1.getQuery(), null);
        HttpPost httpPost = new HttpPost(uri);
//        HttpPost httpPost = new HttpPost(apiDTO.getUrl());
        result.setReq_url(httpPost.getURI().toString());
        log.info("Request URL: {}",httpPost.getURI().toString());
        log.info("Request Method: {} {}",httpPost.getRequestLine().getProtocolVersion(),httpPost.getMethod());
        setRequestConfig(httpPost);
        packageHeader(log, result, apiDTO.getHeaders(), httpPost);
        setCookies(log, result, apiDTO.getCookies());
        packageParam(log, result, apiDTO.getBody(), apiDTO.getParamstype(), httpPost);

        return getHttpClientResult(log, result, httpPost);
    }

    /**
     * 发送put请求；带请求参数
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPut(Logger log, HttpClientResult result, ApiDTO apiDTO) throws Exception {
        HttpPut httpPut = new HttpPut(apiDTO.getUrl());
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPut.setConfig(requestConfig);
        packageParam(log, result, apiDTO.getBody(),apiDTO.getParamstype(), httpPut);
        return getHttpClientResult(log, result, httpPut);
    }

    /**
     * 发送delete请求；带请求参数
     * @return
     * @throws Exception
     */
    public static HttpClientResult doDelete(Logger log, HttpClientResult result, ApiDTO apiDTO) throws Exception {
        Map<String, Object> map = CommonUtils.strToMap(apiDTO.getBody());
        map.put("_method", "delete");
        return doPost(log, result, apiDTO);
    }

    /**
     * Description: 封装请求头
     * @param params
     * @param httpMethod
     */
    public static void packageHeader(Logger log, HttpClientResult result, String params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null && params != "{}") {
            log.info("Request Headers:\n{}",params);
            result.setReq_headers(params);
            Map<String, Object> param = CommonUtils.strToMap(params);
            for (String key : param.keySet()) {
                httpMethod.setHeader(key, (String) param.get(key));
            }
        }
    }

    public static List<Cookie> getCookies(){
        return cookieStore.getCookies();
    }

    public static void setCookies(Logger log, HttpClientResult result, String cookies){
        if (cookies != null && cookies != "{}") {
            log.info("Request Cookies:\n{}",cookies);
            result.setReq_cookies(cookies);
            Map<String, Object> param = CommonUtils.strToMap(cookies);
            for (String key : param.keySet()) {
                BasicClientCookie cookie = new BasicClientCookie(key, String.valueOf(param.get(key)));
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
    public static void packageParam(Logger log, HttpClientResult result, String params, String paramstype, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null && params != "{}") {
            log.info("Request Parameters:\n{}",params);
            result.setReq_body(params);
            switch (paramstype){
                case "raw":
                    httpMethod.setEntity(new StringEntity(params, ENCODING));
                    break;
                case "form":
                    List<BasicNameValuePair> pair =new ArrayList<>();
                    Map<String, Object> param = CommonUtils.strToMap(params);
                    for (String key : param.keySet()) {
                        pair.add(new BasicNameValuePair(key, String.valueOf(param.get(key))));
                    }
                    httpMethod.setEntity(new UrlEncodedFormEntity(pair, ENCODING));
                    break;
                default:
                    httpMethod.setEntity(new StringEntity(params, ENCODING));
                    break;
            }
        }
    }

    /**
     * Description: 获得响应结果
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static HttpClientResult getHttpClientResult(Logger log, HttpClientResult result, HttpRequestBase httpMethod){
        CloseableHttpResponse httpResponse = null;
        // 执行请求
        try {
            httpResponse = getHttpClient().execute(httpMethod, HttpClientContext.create());
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
                log.info("Response StatusCode: {}",httpResponse.getStatusLine().getStatusCode());
                log.info("Response data:\n{}",content);
                Header[] headers= httpResponse.getAllHeaders();
                List<Header> hh = Arrays.asList(headers);
                String header = CommonUtils.HeaderListToMap(hh);
                String cookies = CommonUtils.CookieListToMap(getCookies());
                log.info("Response Cookies:\n{}",cookies);
                result.setRes_code(httpResponse.getStatusLine().getStatusCode());
                result.setRes_headers(header);
                result.setRes_cookies(cookies);
                result.setRes_body(content);
            }
        }catch (Exception e){
//            e.printStackTrace();
            log.info("Exception:\n{}{}",e.getMessage(),e.getCause());
            result.setRes_code(1000);
            result.setRes_body( e.getMessage());
        }finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
//                e.printStackTrace();
                result.setRes_code(1000);
                result.setRes_body( e.getMessage());
            }
            return result;
        }
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

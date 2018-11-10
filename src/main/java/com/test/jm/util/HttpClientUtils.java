package com.test.jm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能说明
 *    HttpClient 4.5.X版本 
 *    http连接是基于tcp的，而tcp创建连接需要三次握手，断开连接四次挥手，
 *    为了提升性能，使用连接池进行Http连接管理：
 *    	1、降低延迟
 *      2、支持更大的并发
 *    工具类支持http与https协议，同 时支持采用双向认证
 *
 *
 * @author Administrator
 *
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    //报文压缩协议类型
    private static final String GZIP = "gzip";

    //请求协议类型
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String CHAR_SET = "UTF-8";

    /**
     * 连接池的最大连接数400
     */
    private static int MAX_CONNECTION_NUM = 1000;

    /**
     * 单路由最大连接数80 
     */
    /// private static int MAX_PER_ROUTE = 100;

    /**
     * 默认的每个路由的最大连接数80
     */
    private static int DEFALUT_MAX_PER_ROUTE = 40;

    /**
     * 向服务端请求超时时间设置(单位:毫秒)
     * 读超时时间（等待数据超时时间）setSocketTimeout
     */
    private static int SOCKET_TIME_OUT = 60000;

    /**
     * 向服务端请求超时时间设置(单位:毫秒)
     * 接收数据的等待超时时间，单位ms setSoTimeout
     */
    private static int SO_TIME_OUT = 60000;

    /**
     * 向服务端请求超时时间设置(单位:毫秒)
     * 关闭Socket时，要么发送完所有数据，要么等待60s后，就关闭连接，此时socket.close()是阻塞的
     */
    private static int SO_LINGER = 60;


    /**
     * 从池中获取连接超时时间(单位:毫秒)
     */
    private static int CONNECT_REQUEST_TIME_OUT = 60000;


    /**
     * 服务端响应超时时间设置(单位:毫秒) 
     */
    // private static int SERVER_RESPONSE_TIME_OUT = 2000;

    /**
     * 连接超时时间
     */
    private static int CONNECT_TIME_OUT = 60000;

    /**
     * 重试处理
     * 重试次数，重试多少次放弃重试
     */
    private static int EXECUTION_COUNT = 5;

    private static RequestConfig requestConfig;
    //HttpClient连接
    private static CloseableHttpClient httpClient;

    //SSL连接工厂类
    private static SSLConnectionSocketFactory sslsf;

    //连接池管理器
    private static PoolingHttpClientConnectionManager cm;

    //SSL上下文创建器
    private static SSLContextBuilder builder;

    private final static Object syncLock = new Object();

    // 请求重试处理
    private static HttpRequestRetryHandler httpRequestRetryHandler;

    //会话存储
    private static CookieStore cookieStore;

    //认证证书相关信息
    /**
     * 私钥证书 文件存放路径
     */
    private static String keyStoreFilePath;

    /**
     * 私钥证书 密钥
     */
    private static String keyStorePass;

    /**
     * 信任证书库 文件存放路径
     */
    private static String trustStoreFilePath;

    /**
     * 信任证书库  密钥
     */
    private static String trustStorePass;

    /**
     * 创建httpclient连接池并初始化
     */
    static {
        //请求配置
        requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .build();

        //设置Cookie存储
        cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("sessionID", "######");
        cookie.setDomain("#####");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);

        /**
         * 重试处理
         * 默认是重试5次
         */
        //禁用重试(参数：retryCount、requestSentRetryEnabled)
        //HttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(0, false);
        httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
                if (executionCount >= EXECUTION_COUNT) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                //Retry if the request is considered idempotent
                //如果请求类型不是HttpEntityEnclosingRequest，被认为是幂等的，那么就重试
                //HttpEntityEnclosingRequest指的是有请求体的request，比HttpRequest多一个Entity属性
                //而常用的GET请求是没有请求体的，POST、PUT都是有请求体的
                //Rest一般用GET请求获取数据，故幂等，POST用于新增数据，故不幂等
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        System.out.println("init conection pool...");

        try {
            builder = new SSLContextBuilder();
            //使用证书方式 
            //initKeyStore(builder);
            // 如果没有服务器证书，可以采用自定义 信任机制 (即全部信任 不做身份鉴定 )
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });

            sslsf = new SSLConnectionSocketFactory(
                    builder.build(),
                    new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"},
                    null, NoopHostnameVerifier.INSTANCE);
            // 注册Socket连接的协议
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, PlainConnectionSocketFactory.INSTANCE)//new PlainConnectionSocketFactory()
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);

            //设置连接池的最大连接数
            cm.setMaxTotal(MAX_CONNECTION_NUM);//max connection
            /**
             * 路由配置（默认配置 和 某个host的配置）
             */
            //默认的每个路由的最大连接数
            cm.setDefaultMaxPerRoute(DEFALUT_MAX_PER_ROUTE);
            //设置到某个路由的最大连接数，会覆盖defaultMaxPerRoute
            //cm.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), MAX_PER_ROUTE); 

            /**
             * socket配置（默认配置 和 某个host的配置）
             */
            SocketConfig socketConfig = SocketConfig.custom()
                    .setTcpNoDelay(true)     //是否立即发送数据，设置为true会关闭Socket缓冲，默认为false
                    .setSoReuseAddress(true) //是否可以在一个进程关闭Socket后，即使它还没有释放端口，其它进程还可以立即重用端口
                    .setSoTimeout(SO_TIME_OUT)       //接收数据的等待超时时间，单位ms
                    .setSoLinger(SO_LINGER)         //关闭Socket时，要么发送完所有数据，要么等待60s后，就关闭连接，此时socket.close()是阻塞的
                    .setSoKeepAlive(true)    //开启监视TCP连接是否有效
                    .build();
            cm.setDefaultSocketConfig(socketConfig);
            //cm.setSocketConfig(new HttpHost("somehost", 80), socketConfig);
            getHttpClient();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化证书
     *
     * @param builder
     * @throws IOException
     */
    private static void initKeyStore(SSLContextBuilder builder) throws IOException {
        InputStream ksis = null;
        InputStream tsis = null;
        try {
            ksis = new FileInputStream(new File(keyStoreFilePath));// 私钥证书
            tsis = new FileInputStream(new File(trustStoreFilePath));// 信任证书库

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(ksis, keyStorePass.toCharArray());

            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(tsis, trustStorePass.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ksis != null) {
                ksis.close();
            }
            if (tsis != null) {
                tsis.close();
            }
        }


        //builder.loadKeyMaterial(ks, keyStorePass.toCharArray());  
        // 如果有 服务器证书  
        //builder.loadTrustMaterial(ts, new TrustSelfSignedStrategy()); 


    }

    /**
     * 线程安全并支持懒加载功能
     *
     * @return
     */
    public static CloseableHttpClient getConnectionInsatance() {
        return SingletonHttpClientHolder.closeableHttpClient;
    }

    /**
     * 创建HttpClient
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                    httpClient = HttpClients.custom()
                            .setConnectionManager(cm) //连接池管理器
                            //.setProxy(new HttpHost("myproxy", 8080))       //设置代理
                            .setDefaultCookieStore(cookieStore) // Cookie存储
                            .setDefaultRequestConfig(requestConfig) //默认请求配置
                            .setSSLSocketFactory(sslsf)
                            .setRetryHandler(httpRequestRetryHandler) // 请求重试处理(重试策略)
                            .setConnectionManagerShared(true) //支持连接共享
                            .build();
                }
            }
        }
        return httpClient;
    }

    /**
     * 设置请求配置
     * 创建一个Get请求，并重新设置请求参数，覆盖默认
     *
     * @param httpget
     */
    private static void requestConfig(HttpRequestBase httpget) {
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIME_OUT)
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT)    //从池中获取连接超时时间
                //.setProxy(new HttpHost("myotherproxy", 8080))
                //.setStaleConnectionCheckEnabled(true)//在提交请求之前 测试连接是否可用
                .build();
        httpget.setConfig(requestConfig);
    }


    /**
     * 获取 Cookie
     *
     * @param url
     * @return
     */
    public static Map<String, String> getCookie(String url) {
        //getHttpClient();
        HttpRequest httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute((HttpGet) httpGet);
            Header[] headers = response.getAllHeaders();
            Map<String, String> cookies = new HashMap<String, String>();
            for (Header header : headers) {
                cookies.put(header.getName(), header.getValue());
            }
            return cookies;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            closeResponse(response);
            //closeHttpClient(httpClient);
        }
        return null;
    }


    /**
     * httpClient post请求
     *
     * @param url 请求url
     * @return 可能为空 需要处理
     * @throws Exception
     */
    public static HttpResponse post(String url, String headers, String params) throws Exception {
        HttpPost httpPost = new HttpPost(url);

        //设置请求头部
        if (StringUtils.isNotEmpty(headers)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(headers);
            Iterator<Map.Entry<String, JsonNode>> elements = node.fields();
            while (elements.hasNext()) {
                Map.Entry<String, JsonNode> element = elements.next();
                httpPost.addHeader(element.getKey(), element.getValue().toString());
            }
        }
        // 设置请求参数
        if (StringUtils.isNotEmpty(params)) {
            StringEntity entity = new StringEntity(params, Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
//                entity.setContentType("application/json");
            httpPost.setEntity(entity);
        }

        //设置请求配置
        requestConfig(httpPost);

        HttpResponse response = httpClient.execute(httpPost);
        return response;
    }

    /**
     * 读请求响应body
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String getResponseBody(HttpResponse response)
            throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = response.getEntity();
        // 判断响应实体是否为空
        if (entity != null) {
            return EntityUtils.toString(entity, "UTF-8");
        }
        return null;
    }

    /**
     * 读请求相应headers
     * @param response
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String getResponseHeaders(HttpResponse response)
            throws ParseException, IOException {
        StringBuilder builder = new StringBuilder();
        HeaderIterator iterator = response.headerIterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next() + "\t");
        }
        return builder.toString();
    }





    /**
     * post请求,使用json格式传参
     * @param url
     * @param headers
     * @param data
     * @return
     */
    public static HttpEntity postJson(String url,Map<String,Object> headers,String data){
        HttpRequest request = new HttpPost(url);
        if(headers!=null&&!headers.isEmpty()){
            request = setHeaders(headers,request);
        }
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = (HttpPost) request;
            httpPost.setEntity(new StringEntity(data, ContentType.create("application/json", CHAR_SET)));//解决中文乱码问题 
            response=httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return entity;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResponse(response);
        }
        return null;
    }

    /**
     * post请求,使用json格式传参
     * @param url
     * @param headers
     * @param data
     * @return
     */
    public static String postJsonToString(String url,Map<String,Object> headers,String data){
        HttpRequest request = new HttpPost(url);
        if(headers!=null&&!headers.isEmpty()){
            request = setHeaders(headers,request);
        }
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = (HttpPost) request;
            httpPost.setEntity(new StringEntity(data, ContentType.create("application/json", CHAR_SET)));//解决中文乱码问题 
            response=httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResponse(response);
        }
        return null;
    }

    /**
     * 设置请求头信息
     *
     * @param headers
     * @param request
     * @return
     */
    private static HttpRequest setHeaders(Map<String,Object> headers, HttpRequest request) {
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            //如果设置的协议头不是Cookies
            if (!entry.getKey().equals("Cookie")) {
                request.addHeader((String) entry.getKey(), (String) entry.getValue());
            } else {
                Map<String, Object> Cookies = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> entry1 : Cookies.entrySet()) {
                    request.addHeader(new BasicHeader("Cookie", (String) entry1.getValue()));
                }
            }
        }
        return request;
    }

    /**
     * 使用表单键值对传参
     * @param url
     * @param headers
     * @param data
     * @return
     */
    public static HttpEntity postForm(String url,Map<String,Object> headers,List<NameValuePair> data){
        CloseableHttpClient httpClient = getHttpClient();
        HttpRequest request = new HttpPost(url);


        //设置请求配置
        requestConfig((HttpPost)request);

        if(headers!=null&&!headers.isEmpty()){
            request = setHeaders(headers,request);
        }
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = (HttpPost) request;
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(data,CHAR_SET);
            httpPost.setEntity(uefEntity);
            // httpPost.setEntity(new StringEntity(data, ContentType.create("application/json", "UTF-8")));
            response=httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return entity;
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            closeResponse(response);
        }
        return null;
    }

    /**
     * 拼装HTTP的Get请求URL与参数
     * @param url
     * @param params
     * @return
     */
    private static String assemblyGetRequestUrlAndParams(String url, Map<String, String> params) {
        if (StringUtils.isBlank(url)) {
            logger.error("Request URL Is Not Null");
            return null;
        }

        StringBuffer buffer = new StringBuffer(url);
        if (params != null && !params.isEmpty()) {
            if (url.indexOf("?") < 0) {
                buffer.append("?");
            }
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (url.indexOf("?") > 0) {
                    buffer.append("&" + entry.getKey() + "=" + entry.getValue());
                } else {
                    buffer.append(entry.getKey() + "=" + entry.getValue() + "&");
                }
            }
        }

        return buffer.toString();
    }

    /**
     * 获取HTTP的响应
     * @param url
     * @param headers
     * @return
     */
    public static CloseableHttpResponse getResponse(String url, Map<String, String> headers) {
        //1.创建客户端访问服务器的httpclient对象   打开浏览器
        getHttpClient();
        //2.以请求的连接地址创建get请求对象     浏览器中输入网址
        HttpGet httpget = new HttpGet(url);

        //设置请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpget.setHeader(entry.getKey(), entry.getValue());
            }
        }

        //设置请求配置
        requestConfig(httpget);

        //3.向服务器端发送请求 并且获取响应对象  浏览器中输入网址点击回车
        CloseableHttpResponse response = null;


        try {
            response = httpClient.execute(httpget);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                //释放资源直接中止这次连接 否则服务将会出现大量的CLOSE_WAIT
                httpget.abort();
                return response;
            }
        } catch (ClientProtocolException e) {
            httpget.abort();
            //logger.error(e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            httpget.abort();
            //logger.error(e.getMessage());
            e.printStackTrace();
            return response;
        } finally {
        }
        return response;
    }

    /**
     * GET 请求
     *
     * @param url - 请求路径 
     * @param params - 请求参数
     * @param headers - 请求头
     * @return
     * @throws Exception
     */
    public static HttpEntity get(String url, Map<String, String> params, Map<String, String> headers) {
        //1.拼装URL
        String tempUrl = assemblyGetRequestUrlAndParams(url, params);

        //2.向服务器端发送请求 并且获取响应对象  浏览器中输入网址点击回车
        CloseableHttpResponse response = getResponse(tempUrl, headers);

        if (response == null) {
            logger.error("CloseableHttpResponse  Object Is Not Null");
            return null;
        }

        //3.获取响应实体
        HttpEntity entity = response.getEntity();
        try{
            //4.获取响应对象中的响应码
            StatusLine statusLine = response.getStatusLine();//获取请求对象中的响应行对象
            int responseCode = statusLine.getStatusCode();//从状态行中获取状态码
            if (responseCode == 200) {
                System.out.println("响应成功!");
                System.out.println("信息----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println("----------------------------------------");
            } else {
                System.out.println("响应失败!");
                System.out.println("错误信息----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println("----------------------------------------");
            }

            EntityUtils.consume(entity);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            closeResponse(response);
            //closeHttpClient(httpClient);
        }

        return entity;
    }

    /**
     * 返回字符串，适合于返回结果为JSON XML格式
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws Exception
     */
    public static String getString(String url, Map<String, String> params, Map<String, String> headers){
        //1.拼装URL
        String tempUrl = assemblyGetRequestUrlAndParams(url, params);

        //2.向服务器端发送请求 并且获取响应对象  浏览器中输入网址点击回车
        CloseableHttpResponse response = getResponse(tempUrl, headers);

        //3.获取响应实体
        if (response == null) {
            logger.error("CloseableHttpResponse  Object Is Not Null");
            return null;
        }

        HttpEntity entity = response.getEntity();  ;
        if (entity != null) {
            try {

                // 判断返回报文是否压缩
                if (entity.getContentEncoding() != null && GZIP.equalsIgnoreCase(entity.getContentEncoding().getName())) {
                    entity = new GzipDecompressingEntity(entity);
                }

                String temp = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
                return temp;
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeResponse(response);
                //closeHttpClient(httpClient);
            }
        }
        return null;
    }


    /**
     * 关闭请求资源，
     * @param response
     */
    public static void closeResponse(CloseableHttpResponse response) {
        if(response!=null){
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 关闭连接管理器，并会关闭其管理的连接
     * @param httpClient
     */
    public static void closeHttpClient(CloseableHttpClient httpClient) {
        if (httpClient != null){
            try {
                //关闭连接管理器，并会关闭其管理的连接
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 单例模式
     *  利用静态内部类特性实现线程安全与懒加载
     *
     * @author
     *
     */
    private static class SingletonHttpClientHolder {
        private static final CloseableHttpClient closeableHttpClient = HttpClients.custom() //
                .setConnectionManager(cm) //连接池管理器
                //.setProxy(new HttpHost("myproxy", 8080))       //设置代理
                .setDefaultCookieStore(cookieStore) // Cookie存储
                .setDefaultRequestConfig(requestConfig) //默认请求配置
                .setSSLSocketFactory(sslsf)
                .setRetryHandler(httpRequestRetryHandler) // 请求重试处理(重试策略)
                .setConnectionManagerShared(true) //支持连接共享
                .build();
    }

}
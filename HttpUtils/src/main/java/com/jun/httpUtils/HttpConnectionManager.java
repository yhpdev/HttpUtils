package com.jun.httpUtils;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * Created by jun on 16/7/2.
 */
public class HttpConnectionManager {
    private static PoolingHttpClientConnectionManager cm;



    static {
        try{
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            Registry<ConnectionSocketFactory> registry =
                    RegistryBuilder.<ConnectionSocketFactory> create()
                            .register("https", sslsf)
                            .register("http", new PlainConnectionSocketFactory()).build();

            cm= new PoolingHttpClientConnectionManager(registry);
        }catch (Exception e){

        }

         cm.setMaxTotal(HttpBuilder.MAX_TOTAL_CONNECTIONS);
         cm.setDefaultMaxPerRoute(HttpBuilder.MAX_ROUTE_CONNECTIONS);


   }


    public  HttpClient getHtpClient(){

        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(cm)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();
        return httpClient;
    }

    public  HttpClient getHtpClient(HttpHost proxy){

        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(cm)
                .setProxy(proxy)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();
        return httpClient;
    }
    public  HttpClient getHtpClient(AuthenticationStrategy proxy){

        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(cm)
                .setProxyAuthenticationStrategy(proxy)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();
        return httpClient;
    }


}

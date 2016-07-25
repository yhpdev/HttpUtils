package com.jun.httpUtils;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by jun on 16/7/2.
 */
public class Session {
    private HttpClient httpClient;
    private HttpClientContext context;
    private HttpRequest request;
    private HttpResponse httpResponse;
    private EntityBuilder entityBuilder;
    private int httpCode;
    private ResponseUtils repUtils;
    private BasicCookieStore cookies;

    public Session() {
        this.context = HttpClientContext.create();
        this.httpClient = new HttpConnectionManager().getHtpClient();
        this.cookies = new BasicCookieStore();
        this.context.setCookieStore(cookies);
    }

    public Session post(String url) {
        this.request = new HttpPost(url);
        return this;
    }

    public Session addHeader(String name, String value) {
        Objects.requireNonNull(this.request);
        this.request.addHeader(name, value);
        return this;
    }

    public Session addHeader(Header header) {
        Objects.requireNonNull(this.request);
        this.request.addHeader(header);
        return this;
    }

    public Session get(String url) {
        this.request = new HttpGet(url);
        return this;
    }

    /**
     * post请求添加,添加参数
     *
     * @param paramsMap
     * @return
     * @throws UnsupportedEncodingException
     */
    public Session addParams(Map paramsMap) throws UnsupportedEncodingException {
        Objects.requireNonNull(paramsMap);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        initEntityBuilder().getEntityBuilder().setParameters(nameValuePairs);
        return this;
    }

    public Session initEntityBuilder(EntityBuilder... builder) {
        if (builder != null && builder.length>0) {
            this.entityBuilder = builder[0];
        } else if (this.entityBuilder == null) {
            this.entityBuilder = EntityBuilder.create();
        }
        return this;
    }

    public Session uploadFile(File file) {
        entityBuilder.setFile(file);
        return this;
    }
    //public Session


    public Session process() throws IOException {
        if (this.request instanceof HttpGet) {
            this.context.setCookieStore(cookies);
            HttpGet get = (HttpGet) this.request;
            this.httpResponse = this.httpClient.execute(get, this.context);
            this.httpCode = httpResponse.getStatusLine().getStatusCode();
            this.repUtils = new ResponseUtils(this.httpResponse);
        } else if (this.request instanceof HttpPost) {
            this.context.setCookieStore(cookies);
            HttpPost post = (HttpPost) this.request;
            if (this.getEntityBuilder() != null) {
                post.setEntity(this.getEntityBuilder().build());
            }
            this.httpResponse = this.httpClient.execute(post, this.context);
            this.httpCode = httpResponse.getStatusLine().getStatusCode();
            this.repUtils = new ResponseUtils(this.httpResponse);
        }
        return this;
    }

    public EntityBuilder getEntityBuilder() {
        return entityBuilder;
    }

    public HttpResponse getHttpResponse() {
        return this.httpResponse;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public BasicCookieStore getCookies() {
        return cookies;
    }


    public ResponseUtils getRepUtils() {
        return repUtils;
    }


}

package com.anycc.pmp.util;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 2016/11/2.
 */
public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static String doGet(String url) {
        HttpClient client = new HttpClient();
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
                "UTF-8");
        HttpMethod method = new GetMethod(url);
        try {
            client.executeMethod(method);
            return method.getResponseBodyAsString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行HTTP Get请求" + url + "时,发生异常！", e);
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    public static String doPost(String url, Map<String, String> paramMap,
                                String json) {
        HttpClient client = new HttpClient();
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
                "UTF-8");

        PostMethod method = new PostMethod(url);
        if (null != paramMap && !paramMap.isEmpty()) {
            NameValuePair[] pairs = new NameValuePair[paramMap.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                NameValuePair pair = new NameValuePair(entry.getKey(),
                        entry.getValue());
                pairs[i] = pair;
                i++;
            }
            method.setRequestBody(pairs);
        }
        if (!StringUtils.isEmpty(json)) {
            method.setRequestBody(json);
        }
        try {
            client.executeMethod(method);
            return method.getResponseBodyAsString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行HTTP post请求" + url + "时,发生异常！", e);
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    public static Map<String, Object> getJson(String url, String type) {

        HttpClient client = new HttpClient();
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
                "UTF-8");
        HttpMethodBase method = null;
        if (type.equals("post")) {
            method = new PostMethod(url);
        } else if (type.equals("get")) {
            method = new GetMethod(url);
        }
        String jsonText = null;
        // 接受结果的HashMap
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int status = client.executeMethod(method);
            if (status == HttpStatus.SC_OK) {// HTTP 200 OK
                jsonText = method.getResponseBodyAsString();// 获取字符串形式的结果，建议使用流式结果
                log.debug("请求的jsonTex：" + jsonText);
            }
            if (null == jsonText) {
                return map;
            }
            map = JsonUtil.jsonToMap(jsonText);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getJson error", e);
        } finally {
            method.releaseConnection();
        }

        return map;
    }


}
package com.ch.core;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Devid on 2016/10/25.
 */
public class RequestParams {
    private static final Logger logger = Logger.getLogger(RequestParams.class);

    private static RequestParams ourInstance = new RequestParams();

    private Map<String, String> cookies = new HashMap<>();

    public static RequestParams getInstance() {
        return ourInstance;
    }

    private RequestParams() {
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        logger.info("cookies: " + new Gson().toJson(cookies));
        this.cookies = cookies;
    }
}

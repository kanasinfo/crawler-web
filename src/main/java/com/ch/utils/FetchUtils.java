package com.ch.utils;

import com.ch.core.RequestParams;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Devid on 2016/10/25.
 */
public final class FetchUtils {
    private static final Logger logger = Logger.getLogger(FetchUtils.class);

    public static Document getByUrl(String url) throws IOException {
        logger.info("request url: " + url);
        Document doc = Jsoup.connect(url)
                .cookies(RequestParams.getInstance().getCookies())
                .get();
        logger.info("response doc: " + new Gson().toJson(doc));
        return doc;
    }
}

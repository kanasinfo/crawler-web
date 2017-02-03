package com.ch.service;

import com.ch.utils.FetchUtils;
import com.ch.dao.HashTagRepository;
import com.ch.dao.LikeRepository;
import com.ch.dao.TweetRepository;
import com.ch.dao.UserRepository;
import com.ch.model.Like;
import com.ch.model.Tweet;
import com.ch.model.User;
import com.ch.utils.GsonUtils;
import com.ch.utils.StringKit;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Devid on 2016/11/28.
 */
@Service
@Transactional(readOnly = true)
public class FetchTwitterService {
    private static final Logger logger = Logger.getLogger(FetchTwitterService.class);
    private static final Logger cmtLogger = Logger.getLogger("comment");
    private static final String MORE_CMT_URL = "http://twitter.com/i/%s/conversation/%s?include_available_features=1&include_entities=1&max_position=%s&reset_error_state=false";

    private static final String domain = "http://twitter.com";

    @Resource
    private TwitterService twitterService;
    @Resource
    private UserService userService;


    @Transactional
    public Tweet fetchTwitter(String account, String tweetId) throws IOException {
        String url = String.format("http://twitter.com/%s/status/%s", account, tweetId);
        User user = new User();
        user.setAccount(account);

        Tweet tweet = new Tweet();
        tweet.setId(tweetId);
        tweet.setUser(user);
        tweet.setType(Tweet.Type.MAIN);

        user = twitterService.saveUser(user);
        Element element = FetchUtils.getByUrl(url).body();
        tweet = twitterService.fetchMain(element, tweet);
        tweet.setUser(user);
        return tweet;
    }

    @Transactional
    public String fetchComment(String account, String tweetId, Tweet tweet) throws IOException {
        String url = String.format("http://twitter.com/%s/status/%s", account, tweetId);
        Element element = FetchUtils.getByUrl(url).body();
        cmtLogger.info("Comment Fetch Over!");

        logger.info("抓取完成！");
        return fetchComment(element, tweet);
    }

    /**
     * 获取列表评论
     */
    @Transactional
    private String fetchComment(Element element, Tweet tweet) {
        String minPosition = null;
        if (element.select("div.stream-container").size() > 0) {
            Element cmtEle = element.select("div.stream-container").first();
            minPosition = cmtEle.attr("data-min-position");
            logger.info("min-position: " + minPosition);
            cmtLogger.info("min-position: " + minPosition);

        }

        twitterService.getMainCommentsFromElement(element, tweet);
        return minPosition;
    }

    /**
     * 异步请求解析json内容
     */
    @Transactional
    public String fetchFromJson(String minPosition, Tweet tweet) {
        try {
            User user = userService.findById(tweet.getUserId());
            String moreUrl = String.format(MORE_CMT_URL, user.getAccount(), tweet.getId(), minPosition);
            cmtLogger.info("more url: " + moreUrl);

            String moreJson = FetchUtils.httpGet(moreUrl);
            Map<String, String> map = GsonUtils.getGson().fromJson(moreJson, Map.class);
            String content = map.get("items_html");
            Element ctEle = Jsoup.parse(content);
            twitterService.getMainCommentsFromElement(ctEle, tweet);

            String nextMinPosition = map.get("min_position");
            return nextMinPosition;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

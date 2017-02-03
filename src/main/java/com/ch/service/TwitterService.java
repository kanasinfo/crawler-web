package com.ch.service;

import com.ch.model.Like;
import com.ch.model.Tweet;
import com.ch.model.User;
import com.ch.utils.FetchUtils;
import com.ch.utils.GsonUtils;
import com.ch.utils.StringKit;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Created by gefangshuai on 2017/1/30.
 */
@Service
@Transactional
public class TwitterService {
    private static final Logger logger = Logger.getLogger(FetchTwitterService.class);
    private static final Logger cmtLogger = Logger.getLogger("comment");

    private static final String domain = "http://twitter.com";
    private static final String MORE_CMT_URL = "http://twitter.com/i/%s/conversation/%s?include_available_features=1&include_entities=1&max_position=%s&reset_error_state=false";

    @Resource
    private LikeService likeService;
    @Resource
    private TweetService tweetService;
    @Resource
    private UserService userService;
    @Resource
    private ApplicationContext ctx;

    /**
     * 获取内容正文
     */
    @Transactional
    public Tweet fetchMain(Element element, Tweet tweet) {
        String userId = element.select("div.permalink-header>a.js-user-profile-link").attr("data-user-id");
        String twitterContent = element.select("div.permalink-tweet-container").select("div.js-tweet-text-container").text();
        String pushTime = element.select("div.permalink-tweet-container").select("div.permalink-header").select("span._timestamp.js-short-timestamp").attr("data-time-ms");
        String username = element.select("div.permalink-header>a.js-user-profile-link>strong.fullname").text();

        tweet.setContent(twitterContent);
        tweet.setPushTime(DateFormatUtils.format(new Date(StringKit.toLong(pushTime)), "yyyy-MM-dd HH:mm:ss"));
        tweet.getUser().setUserId(userId);
        tweet.getUser().setUsername(username);
        tweet.setUserId(userId);
        logger.info("获取Main Tweet: " + GsonUtils.getGson().toJson(tweet));

        fetchReTweetUsers(tweet.getId());
        fetchLikeUsers(tweet.getId());

        return tweetService.save(tweet);
    }


    /**
     * 获取转推人的列表
     */
    @Transactional
    private List<Tweet> fetchReTweetUsers(String tweetId) {
        List<Tweet> tweetUsers = new ArrayList<>();
        String tweetUrl = "http://twitter.com/i/activity/retweeted_popup?id=%s";
        try {
            String contentJson = FetchUtils.httpGet(String.format(tweetUrl, tweetId));
            Map<String, String> map = GsonUtils.getGson().fromJson(contentJson, Map.class);
            String html = StringKit.toString(map.get("htmlUsers"));
            ListIterator<Element> iterator = Jsoup.parse(html).body().select("ol.activity-popup-users").select("li.js-stream-item").listIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String username = element.select("strong.fullname ").text();
                String userId = element.select("div.account").attr("data-user-id");
                String account = element.select("a.js-user-profile-link").attr("href").replace("/", "");
                String content = element.select("p.bio").text();
                String reTweetId = element.attr("data-item-id");

                User user = new User();
                user.setUserId(userId);
                user.setAccount(account);
                user.setUsername(username);
                saveUser(user);

                Tweet tweet = new Tweet();
                tweet.setUserId(userId);
                tweet.setContent(content);
                tweet.setId(reTweetId);
                tweet.setParentId(tweetId);
                tweet.setType(Tweet.Type.RETWEET);
                tweet.setUser(user);
                logger.info("获取转推: " + GsonUtils.getGson().toJson(tweet));
                tweetService.save(tweet);
            }
        } catch (IOException e) {
            logger.info("获取转推人列表失败", e);
        }
        return tweetUsers;
    }

    @Transactional
    public void getMainCommentsFromElement(Element element, Tweet tweet) {
        ListIterator<Element> elements = element.select("li.ThreadedConversation,div.ThreadedConversation--loneTweet").listIterator();
        while (elements.hasNext()) {
            logger.info("cmt index: " + elements.nextIndex());
            Element e = elements.next();
            getMainComment(e, tweet);
        }
    }

    @Transactional
    public void getMainComment(Element e, Tweet tweet) {
        if (e.hasClass("ThreadedConversation")) {    // 带显示更多
            ListIterator<Element> userEles = e.select("div.ThreadedConversation-tweet ").listIterator();
            while (userEles.hasNext()) {
                Element userEle = userEles.next();
                saveComment(userEle, tweet.getId());
            }
            Elements showMoreEles = e.select("li.ThreadedConversation-showMore");
            if (showMoreEles != null && showMoreEles.size() > 0) {
                String moreLink = showMoreEles.first().attr("data-expansion-url");
                String moreUrl = domain + moreLink;
                logger.info("moreUrl: " + moreUrl);
                try {
                    String cmtJson = FetchUtils.httpGet(moreUrl);
                    Map cmtMap = new Gson().fromJson(cmtJson, Map.class);
                    Object conversation = cmtMap.get("conversation_html");
                    if (conversation != null) {
                        String conversationHtml = conversation.toString();
                        Document document = Jsoup.parse(conversationHtml);
                        Elements elements = document.select("li.ThreadedConversation");
                        if (elements != null && elements.size() > 0) {
                            ListIterator<Element> iterator = elements.listIterator();
                            while (iterator.hasNext())
                                getMainComment(iterator.next(), tweet);
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
        if (e.hasClass("ThreadedConversation--loneTweet")) {  // 只有单条评论
            saveComment(e, tweet.getId());
        }
    }

    /**
     * 保存评论
     */
    @Transactional
    public void saveComment(Element e, String tweetId) {
        String id = e.select("a.account-group").attr("data-user-id");
        String account = e.select("a.account-group").attr("href").replace("/", "");
        String username = e.select("strong.fullname").text();
        String cmt = e.select("div.js-tweet-text-container").select("p.js-tweet-text").text();
        String cmtId = e.select("div.tweet.js-stream-tweet").attr("data-tweet-id");
        String cmtTimeStr = e.select("span._timestamp").attr("data-time-ms");
        Date date = new Date(StringKit.toLong(cmtTimeStr));

        User user = new User();
        user.setUserId(id);
        user.setUsername(username);
        user.setAccount(account);
        saveUser(user);

        Tweet tweet = new Tweet();
        tweet.setUser(user);
        tweet.setUserId(id);
        tweet.setId(cmtId);
        tweet.setContent(cmt);
        tweet.setParentId(tweetId);
        tweet.setType(Tweet.Type.COMMENT);
        tweet.setPushTime(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
        // 获取评论喜欢
        tweetService.save(tweet);
        cmtLogger.info("comment: " + cmt);

        fetchLikeUsers(cmtId);
    }

    @Transactional
    public User saveUser(User user) {
        User dbUser = userService.findByAccount(user.getAccount());
        if (dbUser != null) {
            if (StringUtils.isNotBlank(dbUser.getUsername()))
                user.setUsername(dbUser.getUsername());
            if (StringUtils.isNotBlank(dbUser.getUserId()))
                user.setUserId(dbUser.getUserId());
        }
        return userService.save(user);
    }

    /**
     * 获取喜欢人列表
     */
    @Transactional
    public List<Tweet> fetchLikeUsers(String tweetId) {
        List<Tweet> likeUsers = new ArrayList<>();
        String tweetUrl = "http://twitter.com/i/activity/favorited_popup?id=%s";
        try {
            String contentJson = FetchUtils.httpGet(String.format(tweetUrl, tweetId));
            logger.info("like json: " + contentJson);
            Map<String, String> map = GsonUtils.getGson().fromJson(contentJson, Map.class);
            String html = StringKit.toString(map.get("htmlUsers"));
            ListIterator<Element> iterator = Jsoup.parse(html).body().select("ol.activity-popup-users").select("li.js-stream-item").listIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String username = element.select("strong.fullname ").text();
                String account = element.select("a.js-user-profile-link").attr("href").replace("/", "");
                String userId = element.select("a.js-user-profile-link").select("img.js-action-profile-avatar").attr("data-user-id");
                String likeId = element.attr("data-item-id");
                User user = new User();
                user.setUserId(userId);
                user.setAccount(account);
                user.setUsername(username);
                userService.save(user);

                Like like = new Like();
                like.setId(likeId);

                like.setTweetId(tweetId);
                like.setUserId(userId);
//                logger.info("获取喜欢的人: " + GsonUtils.getGson().toJson(like));
                saveLike(like);
            }
        } catch (Exception e) {
            logger.info("获取喜欢人列表失败", e);
        }
        return likeUsers;
    }

    @Transactional
    public void saveLike(Like like) {
        likeService.save(like);
    }
}

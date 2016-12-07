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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final String domain = "http://twitter.com";
    private static final String MORE_CMT_URL = "http://twitter.com/i/%s/conversation/%s?include_available_features=1&include_entities=1&max_position=%s&reset_error_state=false";

    @Resource
    private HashTagRepository hashTagRepository;
    @Resource
    private LikeRepository likeRepository;
    @Resource
    private TweetRepository tweetRepository;
    @Resource
    private UserRepository userRepository;

    @Transactional
    public void saveUser(User user) {
        User dbUser = userRepository.findByAccount(user.getAccount());
        if (dbUser != null) {
            if (StringUtils.isNotBlank(dbUser.getUsername()))
                user.setUsername(dbUser.getUsername());
            if (StringUtils.isNotBlank(dbUser.getUserId()))
                user.setUserId(dbUser.getUserId());
        }
        userRepository.save(user);
    }

    @Transactional
    public void fetchTwitter(String account, String tweetId) throws IOException {
        String url = String.format("http://twitter.com/%s/status/%s", account, tweetId);
        User user = new User();
        user.setAccount(account);

        Tweet tweet = new Tweet();
        tweet.setId(tweetId);
        tweet.setUser(user);
        tweet.setType(Tweet.Type.MAIN);


        Element element = FetchUtils.getByUrl(url).body();
        fetchMain(element, tweet);

        fetchComment(element, tweet);

        tweetRepository.save(tweet);
        saveUser(user);
        logger.info("抓取完成！");
    }

    /**
     * 获取内容正文
     */
    @Transactional
    public void fetchMain(Element element, Tweet tweet) {
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
    }

    /**
     * 获取喜欢人列表
     */
    @Transactional
    private List<Tweet> fetchLikeUsers(String tweetId) {
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
                userRepository.save(user);

                Like like = new Like();
                like.setId(likeId);

                like.setTweetId(tweetId);
                like.setUserId(userId);
                logger.info("获取喜欢的人: " + GsonUtils.getGson().toJson(like));
                saveLike(like);
            }
        } catch (Exception e) {
            logger.info("获取喜欢人列表失败", e);
        }
        return likeUsers;
    }

    @Transactional
    private void saveLike(Like like) {
        likeRepository.save(like);
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
                tweet.setType(Tweet.Type.RETWEET);
                tweet.setUser(user);
                logger.info("获取转推: " + GsonUtils.getGson().toJson(tweet));
                tweetRepository.save(tweet);
            }
        } catch (IOException e) {
            logger.info("获取转推人列表失败", e);
        }
        return tweetUsers;
    }


    /**
     * 获取列表评论
     */
    @Transactional
    public void fetchComment(Element element, Tweet tweet) {
        String minPosition = null;
        if (element.select("div.stream-container").size() > 0) {
            Element cmtEle = element.select("div.stream-container").first();
            minPosition = cmtEle.attr("data-min-position");
            logger.info("min-position: " + minPosition);
        }

        ListIterator<Element> elements = element.select("li.ThreadedConversation,div.ThreadedConversation--loneTweet").listIterator();

        while (elements.hasNext()) {
            Element e = elements.next();
            getMainComment(e, tweet);
        }

        if (StringUtils.isNotBlank(minPosition)) {
            try {
                String moreJson = FetchUtils.httpGet(String.format(MORE_CMT_URL, tweet.getUser().getAccount(), tweet.getId(), minPosition));
                Map<String, String> map = GsonUtils.getGson().fromJson(moreJson, Map.class);
                String content = map.get("items_html");
                Element ctEle = Jsoup.parse(content);
                fetchComment(ctEle, tweet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Transactional
    private void getMainComment(Element e, Tweet tweet) {
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

    @Transactional
    private void saveComment(Element e, String tweetId) {
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
        tweetRepository.save(tweet);

        fetchLikeUsers(cmtId);
    }
}

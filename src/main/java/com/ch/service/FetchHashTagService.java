package com.ch.service;

import com.ch.utils.FetchUtils;
import com.ch.dao.HashTagRepository;
import com.ch.dao.LikeRepository;
import com.ch.dao.TweetRepository;
import com.ch.dao.UserRepository;
import com.ch.model.HashTag;
import com.ch.model.Tweet;
import com.ch.model.User;
import com.ch.utils.GsonUtils;
import com.ch.utils.StringKit;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Devid on 2016/12/4.
 */
@Service
@Transactional(readOnly = true)
public class FetchHashTagService {
    private static final Logger logger = Logger.getLogger(FetchHashTagService.class);
    private static int index = 1;
    @Resource
    private HashTagRepository hashTagRepository;
    @Resource
    private LikeRepository likeRepository;
    @Resource
    private TweetRepository tweetRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private FetchTwitterService fetchTwitterService;
    
    @Transactional
    public HashTag fetchHashTag(String tag) throws IOException {
        HashTag hashTag = new HashTag();
        tag = URLEncoder.encode("#" + tag, "utf-8");

        String url = String.format("http://twitter.com/hashtag/%s", URLEncoder.encode(tag, "utf-8"));
        hashTag.setTag(tag);
        hashTag.setUrl(url);

        hashTagRepository.save(hashTag);
        
        Element element = FetchUtils.getByUrl(url);
        fetchHashTag(tag, element);
        return hashTag;
    }
    
    @Transactional
    public void fetchHashTag(String tag, Element element) {
        Element rootEle = element.select("div#timeline").first();
        String minPosition = rootEle.select("div.stream-container").attr("data-min-position");
        String maxPosition = rootEle.select("div.stream-container").attr("data-max-position");
        logger.info("max position: " + maxPosition);
        Element listEle = rootEle.select("ol.stream-items.js-navigable-stream").first();

        fetchList(listEle);
        try {
            fetchAsync(tag, maxPosition);
        } catch (IOException e) {
            logger.info("异步数据获取错误", e);
        }
    }

    @Transactional
    private void fetchList(Element rootEle) {
        ListIterator<Element> list = rootEle.select("li.js-stream-item.stream-item.stream-item").listIterator();
        while (list.hasNext()) {
            Tweet tweet = new Tweet();
            tweet.setType(Tweet.Type.HASH_TAG);

            User user = new User();
            Element twitterEle = list.next();
            String twitterId = twitterEle.attr("data-item-id");
            String username = twitterEle.select("div.stream-item-header>a.account-group>strong.fullname").text();
            String userId = twitterEle.select("div.account").attr("data-user-id");
            String account = twitterEle.select("a.js-user-profile-link").attr("href").replace("/", "");
            String content = twitterEle.select("div.js-tweet-text-container>p.TweetTextSize.js-tweet-text.tweet-text").text();
            String cmtTimeStr = twitterEle.select("span._timestamp").attr("data-time-ms");
            Date date = new Date(StringKit.toLong(cmtTimeStr));

            user.setUserId(userId);
            user.setUsername(username);
            user.setAccount(account);
            fetchTwitterService.saveUser(user);
            
            tweet.setId(twitterId);
            tweet.setContent(content);
            tweet.setPushTime(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
            tweetRepository.save(tweet);
        }
    }

    @Transactional
    private void fetchAsync(String tag, String maxPosition) throws IOException {
        String url = "http://twitter.com/i/search/timeline?vertical=default&q=" + tag + "&include_available_features=1&include_entities=1&max_position=" + maxPosition + "&reset_error_state=false";
        String jsonContent = FetchUtils.httpGet(url);
        Map<String, Object> map = GsonUtils.getGson().fromJson(jsonContent, Map.class);
        Element element = Jsoup.parse(StringKit.toString(map.get("items_html")));
        fetchList(element);
        logger.info("index: " + (index++));
        logger.info("Json Content: " + jsonContent);
        if (map.get("items_html") != null && StringKit.toInt(map.get("new_latent_count")) > 0 && StringKit.isNotBlank(StringKit.toString("items_html").trim())) {
            fetchAsync(tag, StringKit.toString(map.get("min_position")));
        }

    }
}

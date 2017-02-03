package com.ch.service;

import com.ch.dao.TweetRepository;
import com.ch.model.Tweet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by gefangshuai on 2017/1/31.
 */
@Service
@Transactional(readOnly = true)
public class TweetService {
    @Resource
    private TweetRepository tweetRepository;

    @Transactional
    public Tweet save(Tweet tweet){
        return tweetRepository.save(tweet);
    }

    public Tweet findById(String twitterId) {
        return tweetRepository.findOne(twitterId);
    }

    public List<Tweet> findByParentId(String parentId){
        return tweetRepository.findByParentIdAndTypeOrderByPushTimeDesc(parentId, Tweet.Type.COMMENT);
    }

    public Long countRetweetByParentId(String parentId) {
        return tweetRepository.countByParentIdAndType(parentId, Tweet.Type.RETWEET);
    }
}

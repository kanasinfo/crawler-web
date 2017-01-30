package com.ch.dao;

import com.ch.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Devid on 2016/12/5.
 */
public interface TweetRepository extends JpaRepository<Tweet, String> {
    List<Tweet> findByParentIdOrderByPushTimeDesc(String parentId);

    List<Tweet> findByParentIdAndTypeOrderByPushTimeDesc(String parentId, Tweet.Type comment);

    Long countByParentIdAndType(String parentId, Tweet.Type retweet);
}

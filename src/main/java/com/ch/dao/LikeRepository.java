package com.ch.dao;

import com.ch.model.Like;
import com.ch.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Devid on 2016/12/5.
 */
public interface LikeRepository extends JpaRepository<Like, String> {
    Like findByUserIdAndTweetId(String userId, String tweetId);
}

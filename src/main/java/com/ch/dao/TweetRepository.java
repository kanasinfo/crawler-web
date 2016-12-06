package com.ch.dao;

import com.ch.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Devid on 2016/12/5.
 */
public interface TweetRepository extends JpaRepository<Tweet, String> {
}

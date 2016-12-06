package com.ch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 评论喜欢信息
 * Created by Devid on 2016/12/2.
 */
@Entity
@Table(name = "tweet_like")
public class Like implements Serializable{
    @Id
    private String id;
    
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "tweet_id")
    private String tweetId;

    public Like() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

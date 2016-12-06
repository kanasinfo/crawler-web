package com.ch.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 推文主内容
 * Created by Devid on 2016/12/2.
 */
@Entity
@Table(name = "tweet")
public class Tweet {
    @Id
    @Column(name = "id")
    private String id;   // 推文ID
    @Column(name = "content")
    private String content;     // 推文内容
    @Column(name = "push_time")
    private String pushTime;    // 发布时间
    
    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private Type type;        // 类型
    
    @Column(name = "parent_id")
    private String parentId;
    @Column(name = "hash_tag")
    private String hashTag;
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "fetch_time")
    private Date fetchTime = new Date();
    
    @Transient
    private User user;
    
    public enum Type{
        MAIN,   // 主要内容
        RETWEET,   // 转推
        HASH_TAG, COMMENT
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(Date fetchTime) {
        this.fetchTime = fetchTime;
    }
}

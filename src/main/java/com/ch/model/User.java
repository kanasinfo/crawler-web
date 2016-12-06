package com.ch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Devid on 2016/12/5.
 */
@Entity
@Table(name = "tweet_user")
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;      // 用户ID
    @Column(name = "username")
    private String username;    // 用户名
    @Column(name = "account")
    private String account;     // 用户账号

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}

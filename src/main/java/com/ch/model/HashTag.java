package com.ch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Devid on 2016/12/4.
 */
@Entity
@Table(name = "hash_tag")
public class HashTag {
    @Id
    @Column(name = "tag")
    private String tag;
    @Column(name = "url")
    private String url;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

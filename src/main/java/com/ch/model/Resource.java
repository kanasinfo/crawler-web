package com.ch.model;

import com.ch.utils.StringKit;
import com.google.gson.Gson;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by Devid on 2016/10/29.
 */
@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "search_scope")
    private String searchScope;
    @Column(name = "product_suites")
    private String productSuites;
    @Column(name = "title")
    private String title;
    @Column(name = "url")
    private String url;
    @Column(name = "content")
    private String content;
    @Column(name = "is_fetch_success")
    private boolean isFetchSuccess;

    public Resource() {
        this.id = StringKit.generateUUID();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchScope() {
        return searchScope;
    }

    public void setSearchScope(String searchScope) {
        this.searchScope = searchScope;
    }

    public String getProductSuites() {
        return productSuites;
    }

    public void setProductSuites(String productSuites) {
        this.productSuites = productSuites;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFetchSuccess() {
        return isFetchSuccess;
    }

    public void setFetchSuccess(boolean fetchSuccess) {
        isFetchSuccess = fetchSuccess;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

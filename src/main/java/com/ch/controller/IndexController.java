package com.ch.controller;

import com.ch.service.FetchHashTagService;
import com.ch.service.FetchTwitterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by Devid on 2016/12/5.
 */
@Controller
@RequestMapping("/")
public class IndexController {
    @Resource
    private FetchTwitterService fetchTwitterService;
    @Resource
    private FetchHashTagService fetchHashTagService;
    
    @RequestMapping
    public String index(){
        return "index";
    }
    
    @RequestMapping(value = "/twitter")
    public String fetchTwitter(String account, String twitterId) throws IOException {
        fetchTwitterService.fetchTwitter(account, twitterId);
        return "redirect:/";
    }

    @RequestMapping(value = "/hashtag")
    public String fetchHashTag(String hashTag) throws IOException {
        fetchHashTagService.fetchHashTag(hashTag);
        return "redirect:/";
    }
}

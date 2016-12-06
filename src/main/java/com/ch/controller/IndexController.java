package com.ch.controller;

import com.ch.service.FetchHashTagService;
import com.ch.service.FetchTwitterService;
import com.ch.utils.StringKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    
    @RequestMapping(value = "/twitter", method = RequestMethod.POST)
    public String fetchTwitter(String account, String twitterId) throws IOException {
        fetchTwitterService.fetchTwitter(account, twitterId);
        return "redirect:/";
    }

    @RequestMapping(value = "/hashtag", method = RequestMethod.POST)
    public String fetchHashTag(String hashTag, Integer page) throws IOException {
        page = StringKit.toInt(page, "-1");
        fetchHashTagService.fetchHashTag(hashTag, page);
        return "redirect:/";
    }
}

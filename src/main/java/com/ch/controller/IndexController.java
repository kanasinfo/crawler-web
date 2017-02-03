package com.ch.controller;

import com.ch.model.Tweet;
import com.ch.service.FetchHashTagService;
import com.ch.service.FetchTwitterService;
import com.ch.service.TweetService;
import com.ch.service.TwitterService;
import com.ch.utils.FetchUtils;
import com.ch.utils.GsonUtils;
import com.ch.utils.StringKit;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

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
    @Resource
    private TweetService tweetService;
    @Resource
    private TwitterService twitterService;
    
    @RequestMapping
    public String index(Model model){
        return "index";
    }
    
    @RequestMapping(value = "/twitter", method = RequestMethod.POST)
    @ResponseBody
    public String fetchTwitter(String account, String twitterId, RedirectAttributes redirectAttributes) throws IOException {
        Tweet tweet = fetchTwitterService.fetchTwitter(account, twitterId);
        String minPosition = fetchTwitterService.fetchComment(account, twitterId, tweet);

        return minPosition;
    }

    @RequestMapping(value = "/twitter/comments", method = RequestMethod.POST)
    @ResponseBody
    public String fetchComment(String minPosition, String twitterId){
        Tweet tweet = tweetService.findById(twitterId);
        return fetchTwitterService.fetchFromJson(minPosition, tweet);
    }

    @RequestMapping(value = "/hashtag", method = RequestMethod.POST)
    public String fetchHashTag(String hashTag, Integer page) throws IOException {
        page = StringKit.toInt(page, "-1");
        fetchHashTagService.fetchHashTag(hashTag, page);
        return "redirect:/";
    }

    @RequestMapping(value = "/view/{account}/{twitterId}", method = RequestMethod.GET)
    public String viewTwitter(@PathVariable String account, @PathVariable String twitterId, Model model){
        Tweet tweet = tweetService.findById(twitterId);
        model.addAttribute("tweet", tweet);
        model.addAttribute("tweets", tweetService.findByParentId(twitterId));
        return "viewTwitter";
    }
}

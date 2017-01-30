package com.ch.controller;

import com.ch.model.Tweet;
import com.ch.service.FetchHashTagService;
import com.ch.service.FetchTwitterService;
import com.ch.service.TwitterService;
import com.ch.utils.StringKit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Resource
    private TwitterService twitterService;
    
    @RequestMapping
    public String index(Model model){
        return "index";
    }
    
    @RequestMapping(value = "/twitter", method = RequestMethod.POST)
    public String fetchTwitter(String account, String twitterId, RedirectAttributes redirectAttributes) throws IOException {
        fetchTwitterService.fetchTwitter(account, twitterId);
        redirectAttributes.addFlashAttribute("account", account);
        redirectAttributes.addFlashAttribute("twitterId", twitterId);
        return "redirect:/";
    }

    @RequestMapping(value = "/hashtag", method = RequestMethod.POST)
    public String fetchHashTag(String hashTag, Integer page) throws IOException {
        page = StringKit.toInt(page, "-1");
        fetchHashTagService.fetchHashTag(hashTag, page);
        return "redirect:/";
    }

    @RequestMapping(value = "/view/{account}/{twitterId}", method = RequestMethod.GET)
    public String viewTwitter(@PathVariable String account, @PathVariable String twitterId, Model model){
        Tweet tweet = twitterService.findById(twitterId);
        model.addAttribute("tweet", tweet);
        model.addAttribute("tweets", twitterService.findByParentId(twitterId));
        return "viewTwitter";
    }
}

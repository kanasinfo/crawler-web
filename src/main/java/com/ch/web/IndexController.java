package com.ch.web;

import com.ch.service.FetchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by Devid on 2016/10/29.
 */
@Controller
@RequestMapping("/")
public class IndexController {
    private static final Logger logger = Logger.getLogger(IndexController.class);

    @Resource
    private FetchService fetchService;

    @RequestMapping
    public String index() {
        return "index";
    }

    @RequestMapping(value = "fetch", method = RequestMethod.GET)
    public String fetchGet(){
        return "index";
    }
    @RequestMapping(value = "fetch", method = RequestMethod.POST)
    @ResponseBody
    public String fetchPost(@RequestParam(defaultValue = "1") int start, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "1") int count, String params) {
        logger.info(String.format("开始根据配置，开始抓取数据[start: %s, pageSize: %s, count: %s, params: %s]", start, pageSize, count, params));
        fetchService.fetchList(start, pageSize, count, params);
        return "success";
    }

}

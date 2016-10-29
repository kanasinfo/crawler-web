package com.ch.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Devid on 2016/10/29.
 */
@RestController
@RequestMapping("/")
public class IndexController {
    @RequestMapping
    public String index() {
        return "server is running...";
    }
}

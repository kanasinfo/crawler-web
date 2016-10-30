package com.ch.init;

import com.ch.service.LoginService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by Devid on 2016/10/29.
 */
@Component
public class InitComponent {
    private static final Logger logger = Logger.getLogger(InitComponent.class);
    @Resource
    private LoginService loginService;

    @PostConstruct
    public void login() {
        try {
            logger.info("开始登录...");
            loginService.login();
            logger.info("登录成功!");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}

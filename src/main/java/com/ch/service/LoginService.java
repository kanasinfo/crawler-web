package com.ch.service;

import com.ch.core.RequestParams;
import com.ch.utils.PropUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Devid on 2016/10/25.
 */
public class LoginService {
    private static final Logger logger = Logger.getLogger(LoginService.class);

    private final String username = PropUtils.getProp("form.login.value.username");
    private final String password = PropUtils.getProp("form.login.value.password");

    public void login() throws IOException {
        String loginUrl = PropUtils.getProp("url.login");
        logger.info("处理登录请求");
        logger.info("登录url：" + loginUrl);
        logger.info("用户名：" + username);
        logger.info("密码：" + password);
        Connection.Response res = Jsoup.connect(loginUrl)
                .data(
                        PropUtils.getProp("form.login.key.username"), username,
                        PropUtils.getProp("form.login.key.password"), password
                )
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .execute();
        RequestParams.getInstance().setCookies(res.cookies());
    }
}

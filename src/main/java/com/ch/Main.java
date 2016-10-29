package com.ch;

import com.ch.service.LoginService;
import com.ch.utils.FetchUtils;
import com.ch.utils.PropUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ListIterator;

/**
 * Created by Devid on 2016/10/25.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("开始抓取页面列表");
        LoginService loginService = new LoginService();
        try {
            loginService.login();
            Document document = FetchUtils.getByUrl(PropUtils.getProp("url.list"));
            logger.info("document: " + document);
            ListIterator listIterator = document.select("table ol li a").listIterator();
            while (listIterator.hasNext()) {
                logger.info(listIterator.next());
            }
        } catch (IOException e) {
            logger.info(e);
        }
    }


}

package com.ch.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Devid on 2016/10/25.
 */
public class PropUtils {
    private static final String CONFIG_NAME = "config.properties";
    private static Properties properties = null;

    public synchronized static String getProp(String key){
        if(properties == null){
            InputStream inputStream = PropUtils.class.getResourceAsStream("/"+CONFIG_NAME);
            properties = new Properties();
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return properties.getProperty(key);
    }
}

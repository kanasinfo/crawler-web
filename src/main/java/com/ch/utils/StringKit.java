package com.ch.utils;

import java.util.UUID;

/**
 * Created by Devid on 2016/10/29.
 */
public class StringKit {
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(generateUUID());
    }
}

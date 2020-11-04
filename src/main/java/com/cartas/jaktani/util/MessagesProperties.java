package com.cartas.jaktani.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "messages")
public class MessagesProperties {
    public static final String LANG_ID = "id";
    public static final String LANG_EN = "en";

    private static final String errorGlobalID = "Terjadi keslaahan pada aplikasi, silahkan coba lagi";
    private static final String errorGlobalEN = "There's an error in the application, please restart again";

    public static String getErrorGlobal(String lang) {
        // if lang is empty then return lang id
        if (lang == null || lang.trim().equalsIgnoreCase("") || lang.trim().equalsIgnoreCase(LANG_ID)) {
            return errorGlobalID;
        } else if (lang.trim().equalsIgnoreCase(LANG_EN)) {
            return errorGlobalEN;
        }
        // default is lang id
        return errorGlobalID;
    }
}

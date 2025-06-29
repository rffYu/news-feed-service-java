package com.newsfeed.listener.utils;

import org.jsoup.Jsoup;

public class HtmlCleaner {
    public static String cleanHtml(String content) {
        return Jsoup.parse(content).text();
    }
}


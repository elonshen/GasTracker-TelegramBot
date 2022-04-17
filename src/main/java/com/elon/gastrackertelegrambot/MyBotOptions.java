package com.elon.gastrackertelegrambot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Component
public class MyBotOptions extends DefaultBotOptions {

    @Override
    public ProxyType getProxyType() {
        return ProxyType.HTTP;
    }

    @Override
    public String getProxyHost() {
        return "127.0.0.1";
    }

    @Override
    public int getProxyPort() {
        return 7890;
    }

}

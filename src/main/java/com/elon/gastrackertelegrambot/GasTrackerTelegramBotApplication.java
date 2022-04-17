package com.elon.gastrackertelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableScheduling
public class GasTrackerTelegramBotApplication {

    private static MyAmazingBot myAmazingBot;

    public GasTrackerTelegramBotApplication(MyAmazingBot myAmazingBot) {
        GasTrackerTelegramBotApplication.myAmazingBot = myAmazingBot;
    }

    public static void main(String[] args) {
        SpringApplication.run(GasTrackerTelegramBotApplication.class, args);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(myAmazingBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

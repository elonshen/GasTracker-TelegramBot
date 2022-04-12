package com.elon.gastrackertelegrambot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class MyAmazingBot extends TelegramLongPollingBot {
    public final static String botUsername = "";
    public final static String botToken = "";
    public final static String apikey = "";
    public static double threshold = 0;
    public static boolean taskHasBeenCompleted = true;
    public static SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
    WebClient client = WebClient.create("https://api.etherscan.io");
    WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
    WebClient.RequestBodySpec bodySpec = uriSpec.uri("/api?module=gastracker&action=gasoracle&apikey=" + apikey);
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("OK");
            String text = update.getMessage().getText();
            threshold = Double.parseDouble(text);
            taskHasBeenCompleted = false;
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    private void task() throws JsonProcessingException {
        if (!taskHasBeenCompleted) {
            Map<String, Object> res = gasTracker();
            if ("1".equals(res.get("status"))) {
                Map<String, Object> result = (Map<String, Object>) res.get("result");
                double suggestBaseFee = Double.parseDouble((String) result.get("suggestBaseFee"));
                if (threshold > suggestBaseFee) {
                    message.setText("suggestBaseFee:" + suggestBaseFee);
                    try {
                        execute(message); // Call method to send the message
                        taskHasBeenCompleted = true;
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Map<String, Object> gasTracker() throws JsonProcessingException {
        Mono<String> response = bodySpec.retrieve().bodyToMono(String.class);
        String jsonResponse = response.block(Duration.ofMinutes(1));
        return objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {
        });
    }
}

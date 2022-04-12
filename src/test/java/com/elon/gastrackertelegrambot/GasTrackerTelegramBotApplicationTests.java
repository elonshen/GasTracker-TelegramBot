package com.elon.gastrackertelegrambot;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class GasTrackerTelegramBotApplicationTests {

    @Test
    void contextLoads() throws JsonProcessingException {
        MyAmazingBot myAmazingBot = new MyAmazingBot();
        Map<String, Object> map = myAmazingBot.gasTracker();
        System.out.println(map.size());
    }

}

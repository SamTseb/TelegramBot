package org.joyapi;

import org.joyapi.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JoyBotApplication implements CommandLineRunner {
    private final BotService botService;

    @Autowired
    public JoyBotApplication(BotService botService){
        this.botService = botService;
    }

    public static void main(String[] args) {
        SpringApplication.run(JoyBotApplication.class, args);
    }

    @Override
    public void run(String... args){
        botService.botRun();
    }
}

package com.example.setkabot.service;

import com.example.setkabot.Commands.Commands;
import com.example.setkabot.Commands.Parser;
import com.example.setkabot.config.BotConfig;
import com.example.setkabot.config.BotInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class  MessageExecuter {
    private Parser parser;
    private final BotConfig botConfig;

    @Autowired
    public MessageExecuter(BotConfig botConfig) {
        this.parser = new Parser(botConfig.getBotName());
        this.botConfig = botConfig;
    }

    public void checkCommand(String msg){
        Execute(parser.getParsedCommand(msg).getCommand().toString());
    }

    public void Execute(String command){
        switch (command) {
            case "start" -> System.out.println(1);
            case "help" -> System.out.println(2);
            default -> {
            }
        }

    }
}

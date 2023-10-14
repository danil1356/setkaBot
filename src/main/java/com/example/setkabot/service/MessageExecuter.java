package com.example.setkabot.service;

import com.example.setkabot.Commands.Parser;
import com.example.setkabot.config.BotConfig;
import com.example.setkabot.config.BotInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


public class MessageExecuter {
    Update update;
    String messageText;
    long chatId;

    private Parser parser;

    @Autowired
    private BotConfig botConfig;

    public MessageExecuter(Update update) {
        this.update = update;
        this.messageText = update.getMessage().getText();
        this.chatId = update.getMessage().getChatId();

        this.parser = new Parser(botConfig.getBotName());
    }

    public void checkCommand(){

    }

    public void Execute(){

    }
}

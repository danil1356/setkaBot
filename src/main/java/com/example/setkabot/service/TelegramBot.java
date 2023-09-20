package com.example.setkabot.service;

import com.example.setkabot.config.BotConfig;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    BotConfig botConfig;

    @SneakyThrows
    @Autowired
    public TelegramBot (BotConfig botConfig){
        this.botConfig = botConfig;

        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start","начать"));
        this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId),
                            "Привет! ✴ Я бот молодежного сообщества СЕТКА, могу рассказать много чего полезного о градостроительстве и урбанистике, о необходимых навыках для студентов и супер-возможностях, где можно себя проявить Выберите, о чем хотели бы узнать ⬇" );
                    execute(sendMessage);

                    break;

                default:
                    SendMessage sendMessage2 = new SendMessage(String.valueOf(chatId), "no");
                    execute(sendMessage2);
            }

        }
    }
}

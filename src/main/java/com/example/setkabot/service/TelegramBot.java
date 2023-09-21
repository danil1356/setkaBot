package com.example.setkabot.service;

import com.example.setkabot.config.BotConfig;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
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
        //команды
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
                    List<InlineKeyboardButton> rowLine = new ArrayList<>();
                    List<InlineKeyboardButton> rowLine1 = new ArrayList<>();

                    rowLine.add(createButton("sites","Лучшие сайты для град анализа"));
                    rowLine1.add(createButton("themes","Предложить тему"));

                    rowsLine.add(rowLine);
                    rowsLine.add(rowLine1);
                    markup.setKeyboard(rowsLine);

                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId),
                            "Привет! ✴ \nЯ бот молодежного сообщества СЕТКА, могу рассказать много чего полезного о градостроительстве и урбанистике, о необходимых навыках для студентов и супер-возможностях, где можно себя проявить" );
                    SendMessage sendMessage1 = new SendMessage(String.valueOf(chatId), "Выберите, о чем хотели бы узнать ⬇");

                    sendMessage1.setReplyMarkup(markup);
                    execute(sendMessage);
                    execute(sendMessage1);

                    break;

                default:
                    SendMessage sendMessage2 = new SendMessage(String.valueOf(chatId), "no");
                    execute(sendMessage2);
            }

        }

        //кнопки
        else if (update.hasCallbackQuery()){
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            // TODO: 21.09.2023 s
            if (callbackData.equals("sites")){
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(chatId);
                sendDocument.setCaption("Лови! Никогда ещё предпроектная часть не делалась так легко и приятно ✴");
                sendDocument.setDocument(new InputFile(new File("src/main/resources/image0-9-1.gif.gif")));

                execute(sendDocument);
            }

            // TODO: 21.09.2023 t
            if (callbackData.equals("themes")){
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(chatId);
                sendDocument.setDocument(new InputFile(new File("src/main/resources/image0-9-1.gif.gif")));

                execute(sendDocument);
            }
        }
    }

    private InlineKeyboardButton createButton(String callBack, String text) {
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setCallbackData(callBack);
        keyboardButton.setText(text);
        return keyboardButton;
    }
}

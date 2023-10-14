package com.example.setkabot.service;

import com.example.setkabot.config.BotConfig;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
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

import java.awt.*;
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
//        //сообщения
//        if (update.hasMessage() && update.getMessage().hasText()){
//            MessageExecuter messageExecuter = new MessageExecuter(update);
//            messageExecuter.Execute();
//        }
//        //команды
//        else if (update.hasCallbackQuery()){
//            CallbackExecuter callbackExecuter = new CallbackExecuter(update);
//        }
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

                    rowLine1.add(createButton("bookClub","Вступить в книжный клуб"));

                    rowsLine.add(rowLine);
                    rowsLine.add(rowLine1);
                    markup.setKeyboard(rowsLine);

                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), "Привет! \uD83E\uDDE1\n" +
                            "\n" +
                            "Я бот молодежного сообщества СЕТКА, могу рассказать много чего полезного о градостроительстве и урбанистике, о необходимых навыках для студентов и супер-возможностях, где можно себя проявить \uD83D\uDE0E");
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

            if (callbackData.equals("bookClub")){
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
                List<InlineKeyboardButton> rowLine = new ArrayList<>();
                var button = createButton("linkClub","Ссылка на чат клуба");
                button.setUrl("https://t.me/+JhRoN8V6g6hmMmVi");
                rowLine.add(button);
                rowsLine.add(rowLine);
                markup.setKeyboard(rowsLine);

                execute(SendMessage.builder()
                        .chatId(chatId)
                        .text("\uD83D\uDCE2 <b>Правила книжного клуба СЕТКИ</b> \uD83C\uDFD9️\uD83D\uDCDA\n" +
                                "\n" +
                                "\uD83D\uDCD6 <b>О чем наш клуб?</b>\n" +
                                "Книжный клуб СЕТКИ - это инициатива, которая собирает вместе студентов и молодых специалистов  для обсуждения книг, связанных с городским развитием: от истории архитектуры до современных подходов к планированию городов.\n" +
                                "\n" +
                                "\uD83D\uDD0D <b>Что вы получите, присоединившись к нашему клубу?</b>\n" +
                                "- Обмен идеями и мнениями с единомышленниками, мотивацию читать и развиваться\n" +
                                "- Возможность расширить свои знания о городском планировании и не только, что определенно поможет вам в вашей учебе или работе\n" +
                                "- Попадете в дружелюбное и поддерживающее сообщество, в котором мы обсуждаем архитектурные и градостроительные штучки\n" +
                                "\n" +
                                "\uD83D\uDCDA <b>Как это работает?</b>\n" +
                                "Каждый месяц мы будем выбирать книгу для обсуждения. Вместе мы прочитаем выбранную книгу и проведем онлайн-дискуссию, где каждый сможет высказать свое мнение, поделиться впечатлениями и задать вопросы. Мы также будем предоставлять дополнительные материалы и ресурсы для более глубокого погружения в тему \uD83E\uDD13\n" +
                                "\n" +
                                "\uD83D\uDCA1 <b>Как присоединиться?</b>\n" +
                                "Мы будем рады видеть всех студентов, которые разделяют нашу страсть к урбанистике! Если вы заинтересованы в участии в градостроительном книжном клубе, кликайте на кнопку ниже")
                        .replyMarkup(markup)
                        .parseMode(ParseMode.HTML)
                        .build()
                );
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

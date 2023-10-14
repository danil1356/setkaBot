package com.example.setkabot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public class CallbackExecuter {
    Update update;

    public CallbackExecuter(Update update) {
        this.update = update;
    }
}

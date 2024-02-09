package com.example.setkabot.service;

import com.example.setkabot.Commands.Parser;
import com.example.setkabot.config.BotConfig;
import com.example.setkabot.data.Entity.Payments;
import com.example.setkabot.data.Entity.Users;
import com.example.setkabot.dataService.Impl.PaymentsServiceImpl;
import com.example.setkabot.dataService.Impl.UsersServiceImpl;
import com.example.setkabot.dataService.PaymentsService;
import com.example.setkabot.dataService.UsersService;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.invoices.CreateInvoiceLink;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.jar.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    BotConfig botConfig;
    String memePath = "/root/memes";
    File folder = new File(memePath);
    File[] listOfFiles = folder.listFiles();
    static final String sendType1 = "/всем";
    static final String sendType2 = "/подписка";
    static HashMap<String, String> SELECTED = new HashMap();
    static HashMap<String, Update> UPDATES = new HashMap();
    private final PaymentsService paymentsService;
    private final UsersService usersService;
    private final ArrayList<Long> admins = new ArrayList<>(Arrays.asList(1605771529L, 604552427L));

    @SneakyThrows
    @Autowired
    public TelegramBot (BotConfig botConfig, PaymentsServiceImpl paymentsService, UsersServiceImpl usersService){
        this.botConfig = botConfig;
        this.paymentsService = paymentsService;
        this.usersService = usersService;

        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start","Начать"));

        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
        }

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
        // TODO: 04.12.2023  рабочий прототип парсера
//        execute(SendMessage.builder()
//                .text(new Parser(botConfig.getBotName()).getParsedCommand(update.getMessage().getText()).getCommand().toString())
//                .chatId(update.getMessage().getChatId())
//                .build());

        //рассылка
        if (update.hasMessage() && admins.contains(update.getMessage().getFrom().getId())
                && SELECTED.containsKey(String.valueOf(update.getMessage().getFrom().getId()))
                && SELECTED.get(String.valueOf(update.getMessage().getFrom().getId())) != null
        ){
            String UID = String.valueOf(update.getMessage().getFrom().getId());

            if (SELECTED.get(UID) == sendType1){
                UPDATES.put(UID, update);
                var button1 = createButton("allSM_"+UID, "Отправить");
                execute(CopyMessage.builder()
                        .messageId(UPDATES.get(UID).getMessage().getMessageId())
                        .chatId(UPDATES.get(UID).getMessage().getChatId())
                        .fromChatId(UPDATES.get(UID).getMessage().getChatId())
                        .captionEntities(UPDATES.get(UID).getMessage().getCaptionEntities())
                        .caption(UPDATES.get(UID).getMessage().getCaption())
                        .messageThreadId(UPDATES.get(UID).getMessage().getMessageThreadId())
                        .replyMarkup(new InlineKeyboardMarkup(Arrays.asList(Arrays.asList(button1))))
                        .build());
            }
            else if(SELECTED.get(UID) == sendType2){
                UPDATES.put(UID, update);
                var button1 = createButton("premSM_"+UID, "Отправить");
                execute(CopyMessage.builder()
                        .messageId(UPDATES.get(UID).getMessage().getMessageId())
                        .chatId(UPDATES.get(UID).getMessage().getChatId())
                        .fromChatId(UPDATES.get(UID).getMessage().getChatId())
                        .captionEntities(UPDATES.get(UID).getMessage().getCaptionEntities())
                        .caption(UPDATES.get(UID).getMessage().getCaption())
                        .messageThreadId(UPDATES.get(UID).getMessage().getMessageThreadId())
                        .replyMarkup(new InlineKeyboardMarkup(Arrays.asList(Arrays.asList(button1))))
                        .build());
            }
        }

        //System.out.println(update);
        /**Когда пользователь подтвердит платёж, Telegram пришлёт вам webhook
         * с Update, который содержит объект PreCheckoutQuery.
         * На этот запрос нужно ответить в течение 10 секунд, вызвав метод answerPreCheckoutQuery.
         */
        if(update.hasPreCheckoutQuery()){
            PreCheckoutQuery preCheckoutQuery = update.getPreCheckoutQuery();
            AnswerPreCheckoutQuery answer = new AnswerPreCheckoutQuery();
            answer.setPreCheckoutQueryId(preCheckoutQuery.getId());
            answer.setErrorMessage("oshibka");
            answer.setOk(true);
            execute(answer);
        }

        /**Если платёж пройдёт успешно, вы получите два уведомления: от Telegram (webhook с объектом SuccessfulPayment)
         * и от ЮKassa (email). Бота можно запрограммировать так, чтобы после успешного платежа он совершал определённое действие:
         * например, подключал покупателю услугу или отправлял контент.
         */
        if(update.getMessage()!= null && update.getMessage().hasSuccessfulPayment()){
            Message msg = update.getMessage();
            User usr = update.getMessage().getFrom();
            SuccessfulPayment scfl = update.getMessage().getSuccessfulPayment();

            Users userFind = usersService.findByUserId(String.valueOf(usr.getId()));
            Payments payments = new Payments(null,scfl.getCurrency(), String.valueOf(scfl.getTotalAmount()), scfl.getInvoicePayload(), scfl.getShippingOptionId(), scfl.getOrderInfo().getName(), scfl.getOrderInfo().getPhoneNumber(), scfl.getOrderInfo().getEmail(), String.valueOf(scfl.getOrderInfo().getShippingAddress()), scfl.getTelegramPaymentChargeId(), scfl.getProviderPaymentChargeId(), (System.currentTimeMillis()/1000), null);

            //если уже есть пользователь
            if (userFind != null){
                Users user = new Users(userFind.getId(),String.valueOf(msg.getChatId()), usr.getFirstName(), usr.getLastName(), usr.getUserName(), String.valueOf(usr.getId()), (System.currentTimeMillis()/1000),
                        userFind.getEndpaymenttime() == null ? ((System.currentTimeMillis()/1000)+2629743) : (userFind.getEndpaymenttime() >(System.currentTimeMillis()/1000)? (userFind.getEndpaymenttime()+2629743):(System.currentTimeMillis()/1000)+2629743));
                payments.setUser(user);

                usersService.save(user);
                paymentsService.save(payments);
                execute(SendMessage.builder()
                        .text("Ваша подписк активна до:" +(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date (user.getEndpaymenttime()*1000))) )
                        .chatId(msg.getChatId())
                        .build());
            //если еще нет
            }else {
                Users user = new Users(null,String.valueOf(msg.getChatId()), usr.getFirstName(), usr.getLastName(), usr.getUserName(), String.valueOf(usr.getId()), (System.currentTimeMillis()/1000), ((System.currentTimeMillis()/1000)+2629743));
                payments.setUser(user);

                usersService.save(user);
                paymentsService.save(payments);
                execute(SendMessage.builder()
                        .text("Ваша подписк активна до:" +(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date (user.getEndpaymenttime()*1000))) )
                        .chatId(msg.getChatId())
                        .build());
            }
        }


        //команды
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText){
                case "/start":
                    // TODO: 05.11.2023
                    SELECTED.put(String.valueOf(update.getMessage().getFrom().getId()),null);

                    User usr = update.getMessage().getFrom();
                    Users userFind = usersService.findByUserId(String.valueOf(usr.getId()));
                    if (userFind == null){
                        usersService.save(
                                new Users(null,String.valueOf(update.getMessage().getChatId()), usr.getFirstName(), usr.getLastName(), usr.getUserName(), String.valueOf(usr.getId()), null, null)
                        );
                    }

                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
                    List<InlineKeyboardButton> rowLine = new ArrayList<>();
                    List<InlineKeyboardButton> rowLine1 = new ArrayList<>();
                    List<InlineKeyboardButton> rowLine2 = new ArrayList<>();
                    List<InlineKeyboardButton> rowLine3 = new ArrayList<>();

                    // TODO: 23.10.2023 сделайть патдэржат каланал
                    rowLine.add(createButton("payments", "Поддержать канал!"));
                    rowLine1.add(createButton("bookClub","Вступить в книжный клуб"));
                    rowLine2.add(createButton("staffaj", "Отменный стаффаж для подач"));
                    rowLine3.add(createButton("rndMEMEStart", "le meme"));

                    rowsLine.add(rowLine);
                    rowsLine.add(rowLine1);
                    rowsLine.add(rowLine2);
                    rowsLine.add(rowLine3);
                    markup.setKeyboard(rowsLine);

                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), "Привет! \uD83E\uDDE1\n" +
                            "\n" +
                            "Я бот молодежного сообщества СЕТКА, могу рассказать много чего полезного о градостроительстве и урбанистике, о необходимых навыках для студентов и супер-возможностях, где можно себя проявить \uD83D\uDE0E");
                    SendMessage sendMessage1 = new SendMessage(String.valueOf(chatId), "Выберите, о чем хотели бы узнать ⬇");

                    sendMessage1.setReplyMarkup(markup);
                    execute(sendMessage);
                    execute(sendMessage1);

                    break;

                case sendType1:
                    if (admins.contains(update.getMessage().getFrom().getId())){
                        SELECTED.put(String.valueOf(update.getMessage().getFrom().getId()), sendType1);
                        execute(SendMessage.builder()
                                .chatId(chatId)
                                .text("Я отправлю ВСЕМ:")
                                .build());
                    }
                    break;
                case sendType2:
                    if (admins.contains(update.getMessage().getFrom().getId())){
                        SELECTED.put(String.valueOf(update.getMessage().getFrom().getId()), sendType2);
                        execute(SendMessage.builder()
                                .chatId(chatId)
                                .text("Я отправлю ПОДПИСЧИКАМ:")
                                .build());
                    }
                    break;

                default:
                    if (admins.contains(update.getMessage().getFrom().getId()) == false){
                    SendMessage sendMessage2 = new SendMessage(String.valueOf(chatId), "введите команду");
                    execute(sendMessage2);}
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

            // TODO: 23.10.2023
            if (callbackData.equals("payments")){
                List a = new ArrayList();
                a.add(new LabeledPrice("Цена", 10000));

                // TODO: 05.11.2023
                //
                CreateInvoiceLink link = new CreateInvoiceLink("СЕТКА подписка на месяц", "Это доступ к закрытому авторскому контенту от СЕТКИ", "1",
                        "----", "RUB", a );
                link.setNeedEmail(true);

                String s = execute(link);

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
                List<InlineKeyboardButton> rowLine = new ArrayList<>();
                InlineKeyboardButton ass = createButton("paymentLink","Ссылка на оплату");
                ass.setUrl(s);
                rowLine.add(ass);
                rowsLine.add(rowLine);
                markup.setKeyboard(rowsLine);


                execute(SendMessage.builder()
                        .chatId(chatId)
                        .text("Привет! Спасибо за интерес к нашей платной подписке \uD83D\uDD25\n" +
                                "\n" +
                                "Подключаясь к рассылке, вы получите доступ к нашему закрытому авторскому контенту на месяц. Там мы рассказываем обо всех молодежных возможностях, напоминаем о дедлайнах, проводим закрытые практикумы по портфолио, самопрезентации, мастер-классы по архитектурной подаче и многое другое \uD83E\uDD13\n" +
                                "\n" +
                                "А еще подписка - лучший способ поддержать наши старания! Ждем тебя \uD83E\uDDE1"+
                                "\n"+
                                "По всем вопросам пишите @realurban_help"
                        )
                        .replyMarkup(markup)
                        .build()
                );
            }

            if (callbackData.equals("staffaj")){
                var button = createButton("","срочно скачиваю!");
                button.setUrl("vk.cc/crRKlN");
                execute(SendMessage.builder()
                        .replyMarkup(new InlineKeyboardMarkup(Arrays.asList(Arrays.asList(button))))
                        .text("<b>Привет, рады видеть тебя в нашем боте</b>\uD83E\uDDE1\n" +
                                "\n" +
                                "\uD83D\uDD38<b>Для чего тебе наш пак?</b>\n" +
                                "Иногда бывает так, что хочется внести изюминку в коллаж или на видовой кадр, но не получается найти подходящего пнг-человечка.. Ну или просто на просторах интернета тяжело отыскать картинку достойного качества того самого мужчинки с кофе в руках.. Знакомо? \n" +
                                "Если да, то тебе по адресу! \n" +
                                "\n" +
                                "Здесь мы собрали папку отменного cut-out, жми на кнопку ниже и скачивай себе на кампуктер! <i>Надеемся, что ты сможешь подобрать того самого персонажа, который украсит твой коллаж на планшете</i> \uD83D\uDE0B")
                        .chatId(chatId)
                        .parseMode(ParseMode.HTML)
                        .build()
                );
            }

            if (callbackData.contains("allSM_")){
                List<Users> users = usersService.getAll();
                String UID = callbackData.replace("allSM_", "");
                CopyMessage copyMessage = new CopyMessage();
                copyMessage.setMessageId(UPDATES.get(UID).getMessage().getMessageId());
                copyMessage.setFromChatId(UPDATES.get(UID).getMessage().getChatId());
                copyMessage.setCaptionEntities(UPDATES.get(UID).getMessage().getCaptionEntities());
                copyMessage.setCaption(UPDATES.get(UID).getMessage().getCaption());
                copyMessage.setMessageThreadId(UPDATES.get(UID).getMessage().getMessageThreadId());

                Thread thread = new Thread(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        for(Users user:users){
                            copyMessage.setChatId(user.getChatId());
                            execute(copyMessage);
                            Thread.sleep(90);
                        }
                    }
                });
                thread.start();
                SELECTED.put(UID,null);
            }

            if (callbackData.contains("premSM_")){
                List<Users> users = usersService.getAll();

                String UID = callbackData.replace("premSM_", "");
                CopyMessage copyMessage = new CopyMessage();
                copyMessage.setMessageId(UPDATES.get(UID).getMessage().getMessageId());
                copyMessage.setFromChatId(UPDATES.get(UID).getMessage().getChatId());
                copyMessage.setCaptionEntities(UPDATES.get(UID).getMessage().getCaptionEntities());
                copyMessage.setCaption(UPDATES.get(UID).getMessage().getCaption());
                copyMessage.setMessageThreadId(UPDATES.get(UID).getMessage().getMessageThreadId());

                Thread thread = new Thread(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        for(Users user:users){
                            copyMessage.setChatId(user.getChatId());
                            if(user.getEndpaymenttime()!=null && user.getEndpaymenttime()>=(System.currentTimeMillis()/1000)){
                                execute(copyMessage);
                                Thread.sleep(90);
                            }
                        }
                    }
                });
                thread.start();
                SELECTED.put(UID,null);
            }

            /**
             * Блок для мемев
             */
            if (callbackData.contains("rndMEMEStart")){
                var button1 = createButton("sendContact","Предложить мем");
                var button2 = createButton("rndMEME","Еще!");

                execute(SendPhoto.builder()
                        .chatId(chatId)
                        .photo(new InputFile(new File(memePath+"/"+listOfFiles[(int)(Math.random() * (listOfFiles.length))].getName())))
                        .replyMarkup(new InlineKeyboardMarkup(Arrays.asList(Arrays.asList(button1,button2))))
                        .build());
            }
            if (callbackData.equals("sendContact")){
                execute(SendMessage.builder()
                        .text("@realurban_help")
                        .chatId(chatId)
                        .build()
                );
            }
            if (callbackData.equals("rndMEME")){
                var button = createButton("rndMEME","Еще!");

                execute(SendPhoto.builder()
                        .chatId(chatId)
                        .photo(new InputFile(new File(memePath+"/"+listOfFiles[(int)(Math.random() * (listOfFiles.length))].getName())))
                        .replyMarkup(new InlineKeyboardMarkup(Arrays.asList(Arrays.asList(button))))
                        .build());
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

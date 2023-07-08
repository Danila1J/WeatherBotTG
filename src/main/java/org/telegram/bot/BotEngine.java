package org.telegram.bot;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.telegram.bot.handlers.*;
import org.telegram.bot.service.files.Singleton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.telegram.bot.service.files.Dictionaries.arrivalStationMap;
import static org.telegram.bot.service.files.Dictionaries.translatedCitiesMap;

public class BotEngine extends TelegramLongPollingBot {
    private String chatId;
    private String userName = "";
    private Integer firstMessageId;

    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;
    private final ChromeDriver chromeDriver = new ChromeDriver(new ChromeOptions().addArguments("headless"));

    public BotEngine() {
        super(System.getProperty("TOKEN_TG"));

        this.messageHandler = new MessageHandler(this);
        WeatherHandler weatherHandler = new WeatherHandler(this, messageHandler);
        ScheduleHandler scheduleHandler = new ScheduleHandler(this, messageHandler);
        RouteHandler routeHandler = new RouteHandler(this, messageHandler);
        this.callbackHandler = new CallbackHandler(messageHandler, weatherHandler, routeHandler, scheduleHandler);
    }

    @Override
    public String getBotUsername() {
        return "Погода для котиков";
    }
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            messageHandler.sendFirstMessage(update.getMessage());
        }
        if (update.hasCallbackQuery()) {
            processCallbackQuery(update.getCallbackQuery());
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
    }
    private void processCallbackQuery(CallbackQuery callbackQuery) {
        //Отправка кнопок в сообщениях
        if (callbackHandler.buttons.containsKey(callbackQuery.getData())) {
            callbackHandler.buttons.get(callbackQuery.getData()).run();
        }
        //Погода в городах
        if (callbackHandler.cityWeather.containsKey(callbackQuery.getData())) {
            callbackHandler.cityWeather.get(callbackQuery.getData()).accept(translatedCitiesMap.get(callbackQuery.getData()));
        }
        //Расписание
        if (callbackHandler.toStation.containsKey(callbackQuery.getData())) {
            callbackHandler.toStation.get(callbackQuery.getData()).run();
            Singleton.getInstance().getProperties().setName_from(arrivalStationMap.get(callbackQuery.getData()));
        }
        //Маршруты
        if (callbackHandler.routes.containsKey(callbackQuery.getData())) {
            callbackHandler.routes.get(callbackQuery.getData()).accept(callbackQuery.getData());
        }
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setFirstMessageId(Integer firstMessageId) {
        this.firstMessageId = firstMessageId;
    }
    public String getChatId() {
        return chatId;
    }
    public String getUserName() {
        return userName;
    }
    public Integer getFirstMessageId() {
        return firstMessageId;
    }
    public ChromeDriver getChromeDriver() {
        return chromeDriver;
    }
}
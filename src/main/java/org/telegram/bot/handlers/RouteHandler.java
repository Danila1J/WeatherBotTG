package org.telegram.bot.handlers;

import org.openqa.selenium.WebDriverException;
import org.telegram.bot.BotEngine;
import org.telegram.bot.ElementNotFoundException;
import org.telegram.bot.routes.Routes;
import org.telegram.bot.service.files.Dictionaries;
import org.telegram.bot.service.files.KeyboardButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RouteHandler {
    private final BotEngine botEngine;
    private final MessageHandler messageHandler;
    private final Routes routes = new Routes();

    public RouteHandler(BotEngine botEngine, MessageHandler messageHandler) {
        this.botEngine = botEngine;
        this.messageHandler = messageHandler;
    }

    private void processRouteDetails(String route, StringBuilder stringBuilder) {
        stringBuilder.append("Маршрут ").append(Dictionaries.routes.get(route)).append("\n\n");
        try {
            for (StringBuilder s : routes.getRouteDetails(route, botEngine.getChromeDriver())) {
                stringBuilder.append(s);
            }
        } catch (WebDriverException e) {
            throw new RuntimeException("Error accessing the browser driver");
        } catch (ElementNotFoundException e) {
            stringBuilder.setLength(0);// Удаление названия маршрута, т.к. на направлении нет транспорта
            int idMessage;
            try {
                idMessage = botEngine.execute(new SendMessage(botEngine.getChatId(), "На выбранном маршруте нет транспорта")).getMessageId();
            } catch (TelegramApiException ex) {
                throw new RuntimeException("Failed to send exception message(На выбранном маршруте нет транспорта)", ex);
            }
            if (idMessage != 0) messageHandler.deleteMessage(60, idMessage);
        }
    }

    // Метод изменения/обновление сообщения в отдельном потоке
    private void runCodeInSeparateThread(String route, StringBuilder stringBuilder, int idScheduleRouteMessage) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            stringBuilder.setLength(0);
            processRouteDetails(route, stringBuilder);

            EditMessageText editMessageText = new EditMessageText(stringBuilder.toString());
            editMessageText.setChatId(botEngine.getChatId());
            editMessageText.setMessageId(idScheduleRouteMessage);
            try {
                botEngine.execute(editMessageText);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Failed to edit message", e);
            }
        };
        executor.scheduleAtFixedRate(task, 30, 30, TimeUnit.SECONDS);
    }

    public void sendRoute(String route) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // Получение деталей маршрута и отправка сообщения с ними
            processRouteDetails(route, stringBuilder);
            int idScheduleRouteMessage = botEngine.execute(new SendMessage(botEngine.getChatId(), stringBuilder.toString())).getMessageId();

            //Обновление сообщения каждые 30 секунд на протяжении 5 минут
            runCodeInSeparateThread(route, stringBuilder, idScheduleRouteMessage);

            // Удаление сообщения с маршрутом спустя 590 секунд
            messageHandler.deleteMessage(590, idScheduleRouteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send route message", e);
        }
        // Редактирование сообщения с клавиатурой
        messageHandler.editInlineKeyBoardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory);
    }
}

package org.telegram.bot.handlers;

import org.telegram.bot.BotEngine;
import org.telegram.bot.routes.Routes;
import org.telegram.bot.service.files.KeyboardButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.*;

public class RouteHandler {
    private final BotEngine botEngine;
    private final MessageHandler messageHandler;
    private final Routes routes = new Routes();
    public RouteHandler(BotEngine botEngine, MessageHandler messageHandler) {
        this.botEngine = botEngine;
        this.messageHandler = messageHandler;
    }

    private void processRouteDetails(String route, StringBuilder stringBuilder) throws TelegramApiException {
        try {
            for (StringBuilder s : routes.getRouteDetails(route, botEngine.getChromeDriver())) {
                stringBuilder.append(s);
            }
        } catch (Exception e) {
            // Если на выбранном маршруте нет транспорта, отправляем сообщение об этом
            int idMessage = botEngine.execute(new SendMessage(botEngine.getChatId(), "На выбранном маршруте нет транспорта")).getMessageId();
            messageHandler.deleteMessage(60, idMessage);
        }
    }
    // Метод изменения/обновление сообщения в отдельном потоке
    private void runCodeInSeparateThread(String route, StringBuilder stringBuilder, int idScheduleRouteMessage) {
        CompletableFuture.runAsync(() -> {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

            Runnable task = () -> {
                try {
                    stringBuilder.setLength(0);
                    processRouteDetails(route, stringBuilder);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                EditMessageText editMessageText = new EditMessageText(stringBuilder.toString());
                editMessageText.setChatId(botEngine.getChatId());
                editMessageText.setMessageId(idScheduleRouteMessage);
                try {
                    botEngine.execute(editMessageText);
                } catch (TelegramApiException exception) {
                    throw new RuntimeException(exception);
                }
            };

            ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, 30, 30, TimeUnit.SECONDS);

            // Ожидаем 5 минуты
            try {
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException e) {
                // Обработка исключения
            }

            // Отменяем выполнение задачи
            future.cancel(true);

            // Останавливаем пул потоков
            executor.shutdown();
        });
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

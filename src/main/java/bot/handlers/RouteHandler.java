package bot.handlers;

import org.openqa.selenium.WebDriverException;
import bot.BotEngine;
import bot.ChatContext;
import exceptions.ElementNotFoundException;
import routes.Routes;
import bot.service.Dictionaries;
import bot.service.KeyboardButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Обрабатывает действия, связанные с маршрутом, в чате.
 */
public class RouteHandler {
    private final BotEngine botEngine;
    private final MessageHandler messageHandler;
    private final Routes routes;

    /**
     * Конструктор класса RouteHandler.
     *
     * @param botEngine Экземпляр BotEngine для отправки запросов Telegram API.
     * @param messageHandler Экземпляр MessageHandler для обработки сообщений чата.
     */
    public RouteHandler(BotEngine botEngine, MessageHandler messageHandler) {
        this.botEngine = botEngine;
        this.messageHandler = messageHandler;
        this.routes = new Routes();
    }

    /**
     * Обрабатывает и добавляет сведения о маршруте в переданный объект StringBuilder.
     *
     * @param route Маршрут для обработки.
     * @param routeDetails Объект StringBuilder для хранения сведений о маршруте.
     * @param context Контекст чата.
     */
    private void processRouteDetails(String route, StringBuilder routeDetails, ChatContext context) {
        routeDetails.append("Маршрут ").append(Dictionaries.routes.get(route)).append("\n\n");

        try {
            for (StringBuilder detail : routes.getRouteDetails(route, botEngine.getChromeDriver())) {
                routeDetails.append(detail);
            }
        } catch (WebDriverException e) {
            throw new RuntimeException("Error accessing the browser driver");
        } catch (ElementNotFoundException e) {
            routeDetails.setLength(0); // Удаление названия маршрута, так как по направлению нет транспорта
            sendThenDeleteMessage("На выбранном маршруте нет транспорта", context, 60);
        }
    }

    /**
     * Отправляет сообщение пользователю с последующим удалением, спустя указанное время.
     *
     * @param messageText Текст сообщения.
     * @param context Контекст чата.
     * @param delayInSeconds Задержка в секундах.
     */
    private void sendThenDeleteMessage(String messageText, ChatContext context, int delayInSeconds) {
        try {
            int messageId = botEngine.execute(new SendMessage(context.getChatId(), messageText)).getMessageId();
            if (messageId != 0) messageHandler.deleteMessageAfterDelay(delayInSeconds, messageId, context);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send exception message(" + messageText + ")", e);
        }
    }

    /**
     * Регулярно обновляет сообщение, в соответствии с актуальным временем прибытия транспорта на маршруте.
     *
     * @param route Маршрут для обновления.
     * @param routeDetails Объект StringBuilder, содержащий сведения о маршруте.
     * @param messageId Идентификатор исходного сообщения.
     * @param context Контекст чата.
     */
    private void regularlyUpdateMessageWithRouteDetails(String route, StringBuilder routeDetails, int messageId, ChatContext context) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            routeDetails.setLength(0);
            processRouteDetails(route, routeDetails, context);

            EditMessageText updateMessage = new EditMessageText(routeDetails.toString());
            updateMessage.setChatId(context.getChatId());
            updateMessage.setMessageId(messageId);

            try {
                botEngine.execute(updateMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Failed to edit message", e);
            }
        };

        executor.scheduleAtFixedRate(task, 30, 30, TimeUnit.SECONDS);
    }

    /**
     * Отправляет информацию о времени прибытия транспорта для данного маршрута в чат.
     *
     * @param route Маршрут для отправки.
     * @param context Контекст чата.
     */
    public void sendTransportArrivalInfo(String route, ChatContext context) {
        StringBuilder routeDetailsBuilder = new StringBuilder();
        try {
            // Получение сведений о маршруте и отправка сообщения с ними
            processRouteDetails(route, routeDetailsBuilder, context);
            int messageForScheduleId = botEngine.execute(new SendMessage(context.getChatId(), routeDetailsBuilder.toString())).getMessageId();

            // Обновление сообщения каждые 30 секунд в течение 5 минут
            regularlyUpdateMessageWithRouteDetails(route, routeDetailsBuilder, messageForScheduleId, context);

            // Удаление сообщения с маршрутом через 590 секунд
            messageHandler.deleteMessageAfterDelay(590, messageForScheduleId, context);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send route message", e);
        }
        // Редактирование сообщения
        messageHandler.updateInlineKeyboardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory, context);
    }
}


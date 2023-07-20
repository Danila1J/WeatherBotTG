package bot.handlers;

import bot.BotEngine;
import bot.ChatContext;
import exceptions.TrainsNotFoundException;
import schedule.Schedule;
import bot.service.KeyboardButton;
import bot.service.Singleton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обрабатывает действия, связанные с расписанием электричек, в чате.
 */
public class ScheduleHandler {

    private final BotEngine botEngine;
    private final MessageHandler messageHandler;

    /**
     * Конструктор класса ScheduleHandler.
     *
     * @param botEngine Экземпляр BotEngine для отправки запросов Telegram API.
     * @param messageHandler Экземпляр MessageHandler для обработки сообщений чата.
     */
    public ScheduleHandler(BotEngine botEngine, MessageHandler messageHandler) {
        this.botEngine = botEngine;
        this.messageHandler = messageHandler;
    }

    /**
     * Отправляет в чат расписание движения поездов между двумя станциями и обрабатывает исключения.
     * Если поезда не найдены, отправляет сообщение о том, что на выбранном маршруте нет свободных поездов.
     * @param departureStation Станция отправления.
     * @param destinationStation Станция назначения.
     * @param context Контекст чата.
     */
    public void sendSchedule(String departureStation, String destinationStation, ChatContext context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            try {
                new Schedule().train(departureStation, destinationStation);
                stringBuilder.append("Расписание ").append(departureStation).append("🚂").append(" - ").append(destinationStation).append("🚂").append("\n\n").append(Singleton.getInstance().getProperties().getStr());
            } catch (TrainsNotFoundException e) {
                botEngine.execute(new SendMessage(context.getChatId(), "На выбранном направлении нет электричек"));
            }
            int scheduleMessageId = botEngine.execute(new SendMessage(context.getChatId(), stringBuilder.toString())).getMessageId();

            messageHandler.deleteMessage(60, scheduleMessageId, context);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send rasp message", e);
        }
        messageHandler.editInlineKeyboardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory, context);
    }
}

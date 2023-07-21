package bot.handlers;

import bot.BotEngine;
import bot.ChatContext;
import bot.service.KeyboardButton;
import bot.service.Singleton;
import exceptions.TrainsNotFoundException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import schedule.Schedule;

/**
 * Обрабатывает действия, связанные с расписанием электричек, в чате.
 */
public class ScheduleHandler {

    private final BotEngine botEngine;
    private final MessageHandler messageHandler;
    private static final int DELETE_MESSAGE_DELAY = 60;

    /**
     * Конструктор класса ScheduleHandler.
     *
     * @param botEngine      Экземпляр BotEngine для отправки запросов Telegram API.
     * @param messageHandler Экземпляр MessageHandler для обработки сообщений чата.
     */
    public ScheduleHandler(BotEngine botEngine, MessageHandler messageHandler) {
        this.botEngine = botEngine;
        this.messageHandler = messageHandler;
    }

    /**
     * Отправляет в чат расписание движения поездов между двумя станциями и обрабатывает исключения.
     * Если поезда не найдены, отправляет сообщение о том, что на выбранном маршруте нет свободных поездов.
     *
     * @param departureStation   Станция отправления.
     * @param destinationStation Станция назначения.
     * @param context            Контекст чата.
     */
    public void sendTrainSchedule(String departureStation, String destinationStation, ChatContext context) {
        String scheduleMessage;
        try {
            scheduleMessage = createScheduleMessage(departureStation, destinationStation);
        } catch (TrainsNotFoundException e) {
            scheduleMessage = "На выбранном направлении нет электричек";
        }
        sendScheduleMessageToChat(context, scheduleMessage);
        messageHandler.updateInlineKeyboardMessage("Выберите  категорию", KeyboardButton.InlineKeyboardChooseCategory, context);
    }

    /**
     * Создает сообщение расписания для заданных станций отправления и назначения.
     *
     * @param departureStation Станция отправления.
     * @param destinationStation Станция назначения.
     * @return Строка, представляющая сообщение расписания.
     * @throws TrainsNotFoundException Если поезда по заданному маршруту не найдены.
     */
    private String createScheduleMessage(String departureStation, String destinationStation) throws TrainsNotFoundException {
        new Schedule().fetchTrainSchedule(departureStation, destinationStation);
        return "Расписание " + departureStation + "🚂 - " + destinationStation + "🚂\n\n" + Singleton.getInstance().getProperties().getStr();
    }

    /**
     * Отправляет сообщение о расписании в чат.
     *
     * @param context Контекст чата.
     * @param message Сообщение расписания для отправки.
     * @throws RuntimeException Если сообщение не может быть отправлено.
     */
    private void sendScheduleMessageToChat(ChatContext context, String message) {
        try {
            int scheduleMessageId = botEngine.execute(new SendMessage(context.getChatId(), message)).getMessageId();
            messageHandler.deleteMessageAfterDelay(DELETE_MESSAGE_DELAY, scheduleMessageId, context);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send schedule train message", e);
        }
    }
}

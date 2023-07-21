package bot.handlers;

import bot.BotEngine;
import bot.ChatContext;
import bot.service.KeyboardButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Обрабатывает действия, связанные с сообщениями в чате.
 */
public class MessageHandler {

    private final BotEngine botEngine;

    /**
     * Конструктор класса MessageHandler.
     *
     * @param botEngine Экземпляр BotEngine для отправки запросов Telegram API.
     */
    public MessageHandler(BotEngine botEngine) {
        this.botEngine = botEngine;
    }

    /**
     * Отправление первого сообщения пользователю, предлагая ему выбрать категорию.
     *
     * @param message Входящее сообщение от пользователя.
     * @param context Контекст чата.
     */
    public void sendInitialMessage(Message message, ChatContext context) {
        context.setChatId(message.getChatId().toString());
        SendMessage firstMessage = new SendMessage(message.getChatId().toString(), "Выберите  категорию");
        firstMessage.setReplyMarkup(KeyboardButton.InlineKeyboardChooseCategory);
        try {
            context.setFirstMessageId(botEngine.execute(firstMessage).getMessageId());
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send first message",e);
        }
    }

    /**
     * Удаление сообщения после определенной задержки.
     *
     * @param delaySeconds Задержка в секундах перед удалением сообщения.
     * @param messageId Идентификатор удаляемого сообщения.
     * @param context Контекст чата.
     */
    public void deleteMessageAfterDelay(int delaySeconds, int messageId, ChatContext context) {
        // Создает задачу для удаления сообщений после задержки
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            DeleteMessage deleteMessage = new DeleteMessage(context.getChatId(), messageId);
            try {
                botEngine.execute(deleteMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Failed to delete message",e);
            }
        }, delaySeconds, TimeUnit.SECONDS);

        executorService.shutdown();
    }

    /**
     * Редактирование InlineKeyboard в сообщении.
     *
     * @param text Текст сообщения для редактирования.
     * @param inlineKeyboardMarkup InlineKeyboardMarkup для установки в сообщении.
     * @param context Контекст чата.
     */
    public void updateInlineKeyboardMessage(String text, InlineKeyboardMarkup inlineKeyboardMarkup, ChatContext context) {
        EditMessageText editMessageText = new EditMessageText(text);
        editMessageText.setChatId(context.getChatId());
        editMessageText.setMessageId(context.getFirstMessageId());
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        try {
            botEngine.execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to edit InlineKeyBoard in message",e);
        }
    }
}

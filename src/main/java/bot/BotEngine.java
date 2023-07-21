package bot;

import bot.handlers.*;
import bot.service.Singleton;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.concurrent.*;

import static bot.service.Dictionaries.arrivalStationMap;
import static bot.service.Dictionaries.translatedCitiesMap;

/**
 * Представляет основной класс Telegram бота.
 */
public class BotEngine extends TelegramLongPollingBot {

    private final Map<String, ChatContext> chatContextMap = new ConcurrentHashMap<>();
    private final BlockingQueue<Update> botRequests = new ArrayBlockingQueue<>(100);
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;
    private final ChromeDriver chromeDriver = new ChromeDriver(new ChromeOptions().addArguments("headless"));

    /**
     * Создает новый экземпляр BotEngine. Инициализируя все handlers
     */
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
        return System.getProperty("botUsername");
    }

    /**
     * Выполняется всякий раз, когда получено обновление. Обрабатывает обновление асинхронно.
     *
     * @param update Обновление, полученное с сервера Telegram
     */
    public void onUpdateReceived(Update update) {
        executorService.submit(() -> processUpdate(update));
    }

    /**
     * Обрабатывает обновление
     *
     * @param update Полученное обновление
     */
    private void processUpdate(Update update) {
        String chatId = retrieveChatId(update);
        if (chatId == null) return;
        ChatContext context = chatContextMap.computeIfAbsent(chatId, k -> new ChatContext(k, 0));
        if (update.hasMessage()) {
            messageHandler.sendInitialMessage(update.getMessage(), context);
        }
        if (update.hasCallbackQuery()) {
            processCallbackQuery(update.getCallbackQuery(), context);
        }
    }

    /**
     * Извлекает идентификатор чата из объекта Update.
     *
     * @param update Полученное обновление
     * @return String ChatId или null, если не найден
     */
    private String retrieveChatId(Update update){
        if (update.hasMessage()) {
            return update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId().toString();
        }
        return null;
    }

    /**
     * Обрабатывает CallbackQuery из обновления, если оно существует.
     *
     * @param callbackQuery Запрос обратного вызова из обновления
     * @param context Контекст чата, связанный с идентификатором чата.
     */
    private void processCallbackQuery(CallbackQuery callbackQuery, ChatContext context) {
        //Отправка кнопок в сообщениях
        if (callbackHandler.buttonHandlers.containsKey(callbackQuery.getData())) {
            callbackHandler.buttonHandlers.get(callbackQuery.getData()).accept(context);
        }
        //Погода в городах
        if (callbackHandler.cityWeatherHandlers.containsKey(callbackQuery.getData())) {
            callbackHandler.cityWeatherHandlers.get(callbackQuery.getData()).accept(translatedCitiesMap.get(callbackQuery.getData()), context);
        }
        //Расписание
        if (callbackHandler.arrivalStationsHandlers.containsKey(callbackQuery.getData())) {
            callbackHandler.arrivalStationsHandlers.get(callbackQuery.getData()).accept(context);
            Singleton.getInstance().getProperties().setName_from(arrivalStationMap.get(callbackQuery.getData()));
        }
        //Маршруты
        if (callbackHandler.routeHandlers.containsKey(callbackQuery.getData())) {
            callbackHandler.routeHandlers.get(callbackQuery.getData()).accept(callbackQuery.getData(), context);
        }
    }

    /**
     * Возвращает  экземпляр ChromeDriver.
     *
     * @return ChromeDriver
     */
    public ChromeDriver getChromeDriver() {
        return chromeDriver;
    }
}
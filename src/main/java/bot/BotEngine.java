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
    public void onUpdateReceived(Update update) {
        if (!botRequests.offer(update)) {
            System.err.println("Очередь запросов бота в настоящее время заполнена. Пожалуйста, повторите попытку позже.");
        } else {
            executorService.submit(() -> {
                try {
                    Update receivedUpdate = botRequests.take();
                    processUpdate(receivedUpdate);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    private void processUpdate(Update update) {
        String chatId;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        } else {
            return;
        }

        ChatContext context = chatContextMap.computeIfAbsent(chatId, k -> new ChatContext(k, 0));

        if (update.hasMessage()) {
            messageHandler.sendFirstMessage(update.getMessage(), context);
        }
        if (update.hasCallbackQuery()) {
            processCallbackQuery(update.getCallbackQuery(), context);
        }
    }


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
    public ChromeDriver getChromeDriver() {
        return chromeDriver;
    }
}
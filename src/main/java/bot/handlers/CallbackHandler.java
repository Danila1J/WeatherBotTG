package bot.handlers;

import bot.ChatContext;
import bot.service.KeyboardButton;
import bot.service.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Обрабатывает действия, связанные с обратным вызовом(callback), в чате.
 */
public class CallbackHandler {
    // Map для хранения обработчиков обратного вызова
    public final Map<String, BiConsumer<String, ChatContext>> cityWeatherHandlers = new HashMap<>();
    public final Map<String, BiConsumer<String, ChatContext>> routeHandlers = new HashMap<>();
    public final Map<String, Consumer<ChatContext>> arrivalStationsHandlers = new HashMap<>();
    public final Map<String, Consumer<ChatContext>> buttonHandlers = new HashMap<>();

    /**
     * Конструктор CallbackHandler.
     *
     * @param messageHandler Экземпляр MessageHandler для обработки сообщений.
     * @param weatherHandler Экземпляр WeatherHandler для обработки погоды.
     * @param routeHandler Экземпляр RouteHandler для обработки маршрута.
     * @param scheduleHandler Экземпляр ScheduleHandler для обработки расписания.
     */
    public CallbackHandler(MessageHandler messageHandler, WeatherHandler weatherHandler, RouteHandler routeHandler, ScheduleHandler scheduleHandler) {
        initCityWeatherHandlers(weatherHandler);
        initRouteHandlers(routeHandler);
        initArrivalStationsHandlers(messageHandler);
        initButtonHandlers(messageHandler, scheduleHandler);
    }

    /**
     * Инициализирует WeatherHandlers по городам.
     *
     * @param weatherHandler Экземпляр WeatherHandler для обработки отдельных городов.
     */
    private void initCityWeatherHandlers(WeatherHandler weatherHandler) {
        cityWeatherHandlers.putAll(Map.of(
                "WeatherSamara", weatherHandler::sendWeatherInfo,
                "WeatherNovokybishevsk", weatherHandler::sendWeatherInfo
        ));
    }

    /**
     * Инициализирует обработчики маршрутов.
     *
     * @param routeHandler Экземпляр RouteHandler для обработки маршрутов.
     */
    private void initRouteHandlers(RouteHandler routeHandler) {
        routeHandlers.putAll(Map.of(
                "Telecentre->Railway", routeHandler::sendTransportArrivalInfo,
                "Railway->Telecentre", routeHandler::sendTransportArrivalInfo
        ));
    }

    /**
     * Инициализирует обработчики прибытия.
     *
     * @param messageHandler Экземпляр MessageHandler для обработки сообщений.
     */
    private void initArrivalStationsHandlers(MessageHandler messageHandler) {
        arrivalStationsHandlers.putAll(Map.of(
                "FromSamara", context -> messageHandler.updateInlineKeyboardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalSamara, context),
                "FromMirnaya", context -> messageHandler.updateInlineKeyboardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalMirnaya, context),
                "FromLipyagi", context -> messageHandler.updateInlineKeyboardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalLipyagi, context),
                "FromSrednevolzhskaya", context -> messageHandler.updateInlineKeyboardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalSrednevolzhskaya, context)
        ));
    }

    /**
     * Инициализирует обработчики кнопок.
     *
     * @param messageHandler Экземпляр MessageHandler для обработки сообщений.
     * @param scheduleHandler Экземпляр ScheduleHandler для обработки расписания.
     */
    // TODO подумать над более корректным разделением кнопок по соответствующим Map
    private void initButtonHandlers(MessageHandler messageHandler, ScheduleHandler scheduleHandler) {
        buttonHandlers.putAll(Map.of(
                "WeatherApp", context -> messageHandler.updateInlineKeyboardMessage("Выберите город", KeyboardButton.InlineKeyboardChooseCity, context),
                "Routes", context -> messageHandler.updateInlineKeyboardMessage("Выберите маршрут", KeyboardButton.InlineKeyboardChooseRoutes, context),
                "TabletimeFrom", context -> messageHandler.updateInlineKeyboardMessage("Cтанция отправления", KeyboardButton.InlineKeyboardChooseDeparture, context),
                "ToMirnaya", context -> scheduleHandler.sendTrainSchedule(Singleton.getInstance().getProperties().getName_from(), "Мирная", context),
                "ToSamara", context -> scheduleHandler.sendTrainSchedule(Singleton.getInstance().getProperties().getName_from(), "Самара", context),
                "ToLipyagi", context -> scheduleHandler.sendTrainSchedule(Singleton.getInstance().getProperties().getName_from(), "Липяги", context),
                "ToSrednevolzhskaya", context -> scheduleHandler.sendTrainSchedule(Singleton.getInstance().getProperties().getName_from(), "Средневолжская", context)
        ));
    }
}

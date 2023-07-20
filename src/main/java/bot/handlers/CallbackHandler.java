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

    // Инициализация обработчиков погоды, по городам
    private void initCityWeatherHandlers(WeatherHandler weatherHandler) {
        cityWeatherHandlers.putAll(Map.of(
                "WeatherSamara", weatherHandler::sendWeatherMessage,
                "WeatherNovokybishevsk", weatherHandler::sendWeatherMessage
        ));
    }

    // Инициализация обработчиков маршрутов
    private void initRouteHandlers(RouteHandler routeHandler) {
        routeHandlers.putAll(Map.of(
                "Telecentre->Railway", routeHandler::sendTransportArrivalInfo,
                "Railway->Telecentre", routeHandler::sendTransportArrivalInfo
        ));
    }

    // Инициализация обработчиков станций
    private void initArrivalStationsHandlers(MessageHandler messageHandler) {
        arrivalStationsHandlers.putAll(Map.of(
                "FromSamara", context -> messageHandler.editInlineKeyboardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalSamara, context),
                "FromMirnaya", context -> messageHandler.editInlineKeyboardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalMirnaya, context),
                "FromLipyagi", context -> messageHandler.editInlineKeyboardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalLipyagi, context),
                "FromSrednevolzhskaya", context -> messageHandler.editInlineKeyboardMessage("Cтанция прибытия", KeyboardButton.InlineKeyboardChooseArrivalSrednevolzhskaya, context)
        ));
    }

    // Инициализация обработчиков кнопок
    private void initButtonHandlers(MessageHandler messageHandler, ScheduleHandler scheduleHandler) {
        buttonHandlers.putAll(Map.of(
                "WeatherApp", context -> messageHandler.editInlineKeyboardMessage("Выберите город", KeyboardButton.InlineKeyboardChooseCity, context),
                "Routes", context -> messageHandler.editInlineKeyboardMessage("Выберите маршрут", KeyboardButton.InlineKeyboardChooseRoutes, context),
                "TabletimeFrom", context -> messageHandler.editInlineKeyboardMessage("Cтанция отправления", KeyboardButton.InlineKeyboardChooseDeparture, context),
                "ToMirnaya", context -> scheduleHandler.sendSchedule(Singleton.getInstance().getProperties().getName_from(), "Мирная", context),
                "ToSamara", context -> scheduleHandler.sendSchedule(Singleton.getInstance().getProperties().getName_from(), "Самара", context),
                "ToLipyagi", context -> scheduleHandler.sendSchedule(Singleton.getInstance().getProperties().getName_from(), "Липяги", context),
                "ToSrednevolzhskaya", context -> scheduleHandler.sendSchedule(Singleton.getInstance().getProperties().getName_from(), "Средневолжская", context)
        ));
    }
}

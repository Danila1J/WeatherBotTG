package pages;

import pages.to.samara.ArrivingTransportPage;
import pages.yandex.weather.YandexWeatherPage;

// Элементы общие для системы
public class HelpdeskBasePage extends AbstractPage {

    // Доступ к элементам главного меню
    public YandexWeatherPage mainMenuWeather() {
        return new YandexWeatherPage(driver);
    }

    public ArrivingTransportPage mainMenuTransport() {
        return new ArrivingTransportPage(driver);
    }
}

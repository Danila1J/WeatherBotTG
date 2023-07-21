package weather;

import org.openqa.selenium.chrome.ChromeDriver;
import pages.AbstractPage;
import pages.MainPage;
import pages.yandex.weather.YandexWeatherPage;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс обработки веб-автоматизации для получения прогнозов погоды.
 */
public class WeatherPredictionManager {
    private static final Map<Character, String> CITY_URLS = new HashMap<>(); // Карта для хранения URL-адресов погодных веб-сайтов для каждого города

    static {
        initializeCityUrls();
    }

    /**
     * Устанавливает URL-адреса для веб-сайтов погоды разных городов.
     */
    private static void initializeCityUrls() {
        CITY_URLS.putAll(Map.of(
                'С', System.getProperty("siteWeatherSamara.url"),
                'Н', System.getProperty("siteWeatherNovokybishevsk.url")
        ));
    }

    /**
     * Настраивает ChromeDriver с необходимыми свойствами и возвращает его.
     *
     * @return настроенный экземпляр ChromeDriver
     */
    private ChromeDriver initializeDriver() {
        ChromeDriver driver = new ChromeDriver(); // Создание экземпляра драйвера
        driver.manage().window().maximize(); // Устанавливаем размер окна браузера, как максимально возможный
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Установим время ожидания для поиска элементов
        AbstractPage.setDriver(driver); // Установить созданный драйвер для поиска в веб-страницах
        return driver;
    }

    /**
     * Получает URL-адрес, связанный с названием города.
     *
     * @param cityName название города
     * @return Строка URL, связанная с названием города
     */
    private String getCityWeatherSiteUrl(String cityName) {
        char firstCharacter = cityName.charAt(0);
        return CITY_URLS.getOrDefault(firstCharacter, "");
    }

    /**
     * Устанавливает и управляет драйвером, извлекает строку прогноза погоды, а затем закрывает драйвер.
     *
     * @param url URL-адрес для получения прогноза погоды.
     * @return полученная строка прогноза погоды
     */
    private String manageDriverAndFetchPrediction(String url) {
        ChromeDriver driver = initializeDriver();
        driver.get(url);

        MainPage mainPage = new MainPage();
        YandexWeatherPage yandexWeatherPage = mainPage.mainMenuWeather();

        String weatherPrediction = parseWeatherPage(yandexWeatherPage);

        driver.quit();
        return weatherPrediction;
    }

    /**
     * Извлекает строку прогноза погоды с веб-сайта погоды, связанного с названием данного города.
     *
     * @param cityName название города
     * @return полученная строка прогноза погоды; возвращает пустую строку, если ничего не получено
     */
    public String fetchWeatherPrediction(String cityName) {
        String url = getCityWeatherSiteUrl(cityName);
        return url.isEmpty() ? "" : manageDriverAndFetchPrediction(url);
    }

    /**
     * Анализирует страницу погоды Яндекса и возвращает предсказание погоды по виджету на сайте.
     *
     * @param yandexWeatherPage экземпляр YandexWeatherPage
     * @return конкретная строка прогноза погоды; если нет, возвращает пустую строку
     */
    private String parseWeatherPage(YandexWeatherPage yandexWeatherPage) {
        String weatherPrediction = yandexWeatherPage.getPredictionText();
        if ("В ближайшие 2 часа осадков не ожидается".equals(weatherPrediction)
                || "Открыть карту осадков".equals(weatherPrediction)) {
            return "";
        }
        return weatherPrediction;
    }
}
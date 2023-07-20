package weather;

import org.openqa.selenium.chrome.ChromeDriver;
import pages.AbstractPage;
import pages.MainPage;
import pages.yandex.weather.YandexWeatherPage;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DescriptionPrediction {
    private static final Map<Character, String> cityUrls = new HashMap<>();

    static {
        setupCityUrls();
    }

    // Создаем map для хранения URL веб-сайтов для каждого города
    private static void setupCityUrls() {
        cityUrls.putAll(Map.of(
                'С', System.getProperty("siteWeatherSamara.url"),
                'Н', System.getProperty("siteWeatherNovokybishevsk.url")
        ));
    }

    private ChromeDriver setup() {
        ChromeDriver driver = new ChromeDriver(); // Создание экземпляра драйвера
        driver.manage().window().maximize(); // Устанавливаем размер окна браузера, как максимально возможный
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Установим время ожидания для поиска элементов
        AbstractPage.setDriver(driver); // Установить созданный драйвер для поиска в веб-страницах
        return driver;
    }

    // Метод получения URL на основе первого символа в названии города
    private String getUrlByCityName(String cityName) {
        char firstCharacter = cityName.charAt(0);
        return cityUrls.getOrDefault(firstCharacter, "");
    }

    // Метод для настройки и использования драйвера
    private String useDriver(String url) {
        ChromeDriver driver = setup();
        driver.get(url);

        MainPage mainPage = new MainPage();
        YandexWeatherPage yandexWeatherPage = mainPage.mainMenuWeather();

        String answer = parseWeatherPage(yandexWeatherPage);
        // Закрыть драйвер после использования
        driver.quit();
        return answer;
    }

    public String getStringPrediction(String cityName) {
        String url = getUrlByCityName(cityName);
        return url.isEmpty() ? "" : useDriver(url);
    }

    private String parseWeatherPage(YandexWeatherPage yandexWeatherPage) {
        String weatherPrediction = yandexWeatherPage.getPredictionText();
        if ("В ближайшие 2 часа осадков не ожидается".equals(weatherPrediction)
                || "Открыть карту осадков".equals(weatherPrediction)) {
            return "";
        }
        return weatherPrediction;
    }
}
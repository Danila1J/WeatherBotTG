package pages.yandex.weather;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class YandexWeatherPage {
    final WebDriver driver;

    public YandexWeatherPage(WebDriver driver) {
        this.driver=driver;
        PageFactory.initElements(driver, this);
    }

    public String getPredictionText(){
        return driver.findElement(By.xpath("//p[@class='maps-widget-fact__title']")).getText();
    }
}

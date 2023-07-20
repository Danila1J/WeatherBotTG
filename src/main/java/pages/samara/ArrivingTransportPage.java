package pages.samara;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.PageFactory;
import exceptions.ElementNotFoundException;

/**
 * Главное меню (блок элементов)
 */
public class ArrivingTransportPage {
    final WebDriver driver;

    public ArrivingTransportPage(WebDriver driver) {
        /* Необходимо инициализировать элементы класса, аннотированные @FindBy.
           Лучше всего это делать в конструкторе. */
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getTimeText(int i) throws ElementNotFoundException {
        String minutesBeforeArrival;
        try {
            minutesBeforeArrival = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[1]")).getText();
        } catch (WebDriverException e) {
            throw new ElementNotFoundException();
        }
        return minutesBeforeArrival;
    }

    public String getTypeText(int i) throws ElementNotFoundException {
        String typeTransport;
        try {
            typeTransport = driver.findElement(By.xpath("//tbody/tr[" + i + "]")).getAttribute("class");
        } catch (WebDriverException e) {
            throw new ElementNotFoundException();
        }
        return typeTransport;
    }

    public String getNameText(int i) throws ElementNotFoundException {
        String nameRoute;
        try {
            nameRoute = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[2]/a")).getText();
        } catch (WebDriverException e) {
            throw new ElementNotFoundException();
        }
        return nameRoute.substring(0, nameRoute.indexOf(":"));
    }
}

package pages.to.samara;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/** Главное меню (блок элементов) */
public class ArrivingTransportPage {
    final WebDriver driver;

    public ArrivingTransportPage(WebDriver driver) {
        /* Необходимо инициализировать элементы класса, аннотированные @FindBy.
           Лучше всего это делать в конструкторе. */
        this.driver=driver;
        PageFactory.initElements(driver, this);
    }

    public String getTimeText(int i){
        return driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[1]")).getText();
    }
    public String getTypeText(int i){
        return driver.findElement(By.xpath("//tbody/tr[" + i + "]")).getAttribute("class");
    }
    public String getNameText(int i){
        String nameRoute = driver.findElement(By.xpath("//tbody/tr[" + i + "]/td[2]/a")).getText();
        return nameRoute.substring(0,nameRoute.indexOf(":"));
    }
}

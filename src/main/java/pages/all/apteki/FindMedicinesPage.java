package pages.all.apteki;

import exceptions.ElementNotFoundException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FindMedicinesPage {
    final WebDriver driver;

    public FindMedicinesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //TODO инициализировать переменные сразу, а не каждый раз искать при запуске метода

    public void closeAds() {
        driver.findElement(By.xpath("//*[@id='vita_modal']/div/div/div[2]/button")).click();
    }

    // Поле поиска
    public void enterNameMedicine(String name) {
        driver.findElement(By.xpath("//*[@id='input_form1_1']")).sendKeys(name);
    }

    // Нажать кнопку поиска
    public void pressButtonFind() {
        driver.findElement(By.xpath("//*[@id='b_search1']")).click();
    }

    // checkBox - Рядом со мной
    public void pressCheckBoxNextToMe() {
        driver.findElement(By.xpath("//*[@id='a_tab_where2']/div")).click();
    }

    // selectBox - форма выпуска
    public void choiceReleaseForm(String releaseForm) {
        new Select(driver.findElement(By.xpath("//*[@id='chose_type_form']"))).selectByVisibleText(releaseForm);
    }

    public String getSelectMedicineName(){
        return driver.findElement(By.xpath("//*[@id='p_you_search']")).getText();
    }

    /* Сортировка по кол-ву предложений
     2 клика - т.к. сначала по убыванию
     */
    public void sortByNumberOfOffers(){
        driver.findElement(By.xpath("//*[@id='main_table']/thead/tr/th[4]")).click();
        driver.findElement(By.xpath("//*[@id='main_table']/thead/tr/th[4]")).click();
    }

    // Клик на выбранное название лекарства
    public void clickOnSelectMedicines(int i){
        driver.findElement(By.xpath("//*[@id='main_table']/tbody/tr[" + i + "]/td[2]")).click();
    }

    // Cкриншот таблицы
    public File getScreenshot() throws IOException {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement element=driver.findElement(By.xpath("//*[@id='main_table']/tbody"));

        //скролл

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);

        File screenshot=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        BufferedImage bufferedImage=ImageIO.read(screenshot);
        int el=driver.findElement(By.xpath("//*[@id='main_table']/thead/tr/th[4]")).getLocation().getX();
        BufferedImage croppedImage =bufferedImage.getSubimage(125,0,1100,1500);
        ImageIO.write(croppedImage,"png",screenshot);

        return screenshot;


    }

    // Получение формы выпуска из названия препарата
    public String getRealeaseFormFromNameMedicine(int i) {
        String nameMedicine = driver.findElement(By.xpath("//*[@id='main_table']/tbody/tr[" + i + "]/td[2]")).getText();
        return nameMedicine.substring(0, nameMedicine.indexOf(' '));
    }

    // Получение формы выпуска и названия препарата
    public String getFullRealeaseForm(int i) {
        String nameMedicine = driver.findElement(By.xpath("//*[@id='main_table']/tbody/tr[" + i + "]/td[2]")).getText();
        return nameMedicine;
    }

    public void setToDisplay50Entries() throws ElementNotFoundException {
        // Открыть список выбора 25,50,100,1000
        driver.findElement(By.xpath("//*[@id='answer_block']/div[2]/div[4]/div[1]/div[3]/div[1]/span[2]/span/button")).click();
        // Нажать на 50
        driver.findElement(By.xpath("//*[@id='answer_block']/div[2]/div[4]/div[1]/div[3]/div[1]/span[2]/span/div/a[2]")).click();
    }

    public int getAmountRecord() throws ElementNotFoundException {
        int amount= driver.findElements(By.xpath("//*[@id='main_table']/tbody/tr")).size();
        if(amount<=1) throw new ElementNotFoundException();
        return amount;
    }
}

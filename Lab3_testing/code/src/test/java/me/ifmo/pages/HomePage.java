package me.ifmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage{
    private final By buttonLogin = By.xpath("//*[contains(text(), 'Войти')]");
    private final By buttonRubrics = By.xpath("//*[contains(text(), 'Рубрики')]");
    private final By inputSearch = By.xpath("//input[not(@type='hidden') and not(@disabled)]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://2gis.ru/spb");
        System.out.println("Opened URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());
    }

    public void search(String query) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputSearch));
        input.clear();
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(query);
        input.sendKeys(Keys.ENTER);
    }

    public boolean isInputVisible() {
        boolean isVisible;
        try{
            isVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(inputSearch)).isDisplayed();
        } catch (Exception exception){
            System.out.println("Search input не найден!");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            isVisible = false;
        }
        return isVisible;
    }

    public void openRubrics() {
        wait.until(ExpectedConditions.elementToBeClickable(buttonRubrics)).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("rubricator"),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Популярное')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Все рубрики')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Еда')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Авто')]"))
        ));
    }

    public boolean isButtonRubricsVisible() {
        boolean isVisible;
        try{
            isVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(buttonRubrics)).isDisplayed();
        }catch (Exception exception){
            isVisible = false;
        }
        return isVisible;
    }

    public void openLoginForm() {
        wait.until(ExpectedConditions.elementToBeClickable(buttonLogin)).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("id.2gis.com"),
                ExpectedConditions.titleContains("Вход"),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Телефон')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Продолжить')]"))
        ));
    }

    public boolean isButtonLoginVisible() {
        boolean isVisible;
        try{
            isVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(buttonLogin)).isDisplayed();
        }catch (Exception exception){
            isVisible = false;
        }
        return isVisible;
    }

    public boolean isRubricsOpened() {
        boolean isOpened;
        By rubricsContent = By.xpath(
                "//*[contains(text(), 'Поесть') " +
                        "or contains(text(), 'Продукты') " +
                        "or contains(text(), 'Автосервис') " +
                        "or contains(text(), 'Стоматолог') " +
                        "or contains(text(), 'Рубрики')]"
        );

        try {
            isOpened = wait.until(ExpectedConditions.visibilityOfElementLocated(rubricsContent)).isDisplayed();
        } catch (Exception exception) {
            System.out.println("Раздел 'Рубрики' не открылся!");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            isOpened = false;
        }

        return isOpened;
    }

    public boolean isLoginFormOpened() {
        boolean isOpened;

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("id.2gis.com"),
                    ExpectedConditions.titleContains("Вход"),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Телефон')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Продолжить')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Войдите')]"))
            ));

            isOpened = driver.getCurrentUrl().contains("id.2gis.com")
                    || driver.getTitle().contains("Вход")
                    || driver.getPageSource().contains("Телефон")
                    || driver.getPageSource().contains("Продолжить")
                    || driver.getPageSource().contains("Войдите");

        } catch (Exception exception) {
            System.out.println("Форма входа не открылась!");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            isOpened = false;
        }

        return isOpened;
    }

    @Override
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("2gis"));
            return driver.getCurrentUrl().contains("2gis")
                    && !driver.getTitle().isBlank();
        } catch (Exception exception) {
            System.out.println("Главная страница не загрузилась!");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            return false;
        }
    }
}

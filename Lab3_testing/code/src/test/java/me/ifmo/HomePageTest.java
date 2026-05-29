package me.ifmo;

import me.ifmo.pages.HomePage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomePageTest extends BaseTest {

    @Test
    public void testHomePageIsOpened() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        assertTrue(homePage.isPageLoaded(), "Главная страница 2ГИС не загрузилась!");
    }

    @Test
    public void testSearchInputIsVisible() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        assertTrue(homePage.isInputVisible(), "Поле поиска на главной странице 2ГИС не найдено!");
    }

    @Test
    public void testRubricsButtonIsVisible() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        assertTrue(homePage.isButtonRubricsVisible(), "Кнопка 'Рубрики' не отображается на главной странице!");
    }

    @Test
    public void testLoginButtonIsVisible() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        assertTrue(homePage.isButtonLoginVisible(), "Кнопка 'Войти' не отображается на главной странице!");
    }
}
package me.ifmo;

import me.ifmo.pages.HomePage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NavigationTest extends BaseTest {

    @Test
    public void testUserCanOpenRubrics() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        assertTrue(homePage.isButtonRubricsVisible(), "Кнопка 'Рубрики' не отображается на главной странице!");

        homePage.openRubrics();
        assertTrue(homePage.isRubricsOpened(), "Раздел 'Рубрики' не открылся!");
    }

    @Test
    public void testUserCanOpenLoginForm() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        assertTrue(homePage.isButtonLoginVisible(), "Кнопка 'Войти' не отображается на главной странице!");

        homePage.openLoginForm();
        assertTrue(homePage.isLoginFormOpened(), "Форма входа не открылась!");
    }
}
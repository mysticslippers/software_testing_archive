package me.ifmo;

import me.ifmo.pages.HomePage;
import me.ifmo.pages.OrganizationCardPage;
import me.ifmo.pages.RoutePage;
import me.ifmo.pages.SearchResultsPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteTest extends BaseTest {

    @Test
    public void testUserCanOpenRoutePanelFromOrganizationCard() {
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.search("кафе");

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        assertTrue(searchResultsPage.isSearchResultsOpened(), "Результаты поиска по запросу 'кафе' не открылись!");
        searchResultsPage.openFirstResult();

        OrganizationCardPage organizationCardPage = new OrganizationCardPage(driver);
        assertTrue(organizationCardPage.isCardOpened(), "Карточка организации не открылась!");
        organizationCardPage.clickRouteButton();

        RoutePage routePage = new RoutePage(driver);
        assertTrue(routePage.isRoutePanelOpened(), "Панель построения маршрута не открылась!");
    }
}
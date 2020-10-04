import devToPages.DevToMainPage;
import devToPages.DevToSinglePostPage;
import devToPages.DevToWeekPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseTest {
    WebDriver driver;
    WebDriverWait wait;

    //    WebDriverWait explicitWait;
    public void highlightElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: green; border: 3px solid blue;');", element);
    }

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\mahnamahna\\ZDAUTpol2Selenium\\src\\main\\resources\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 20);
//        explicitWait = new WebDriverWait(driver, 20);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //poczekaj zanim wywalisz error że elementu nie ma, sprawdzaj co sekundę
    }

    @Test
    public void selectFirstPostFromWeek() {
        DevToMainPage devToMainPage = new DevToMainPage(driver, wait);
        DevToWeekPage devToWeekPage = devToMainPage.goToWeekPage();

        String firstPostLink = devToWeekPage.firstPostOnWeek.getAttribute("href");
        String firstPostText = devToWeekPage.firstPostOnWeek.getText();

        DevToSinglePostPage devToSinglePostPage = devToWeekPage.selectFirstPost();
        wait.until(ExpectedConditions.urlToBe(firstPostLink));
        String postTitleText = devToSinglePostPage.postTitle.getText();
        String currentUrl = driver.getCurrentUrl();

        assertEquals("Urls aren't the same", currentUrl, firstPostLink);
        assertEquals("Titles aren't the same", postTitleText, firstPostText);
    }

    @Test
    public void searchBarTesting() {
        WebElement searchBox = driver.findElement(By.id("nav-search"));
        String searchText = "testing";
        String searchUrl = "https://dev.to/search?q=";
        String cssSelector = "h2.crayons-story__title a";
        searchBox.sendKeys(searchText + Keys.ENTER);
        wait.until(ExpectedConditions.urlToBe(searchUrl + searchText));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(cssSelector)));
        List<WebElement> postTilesList = driver.findElements(By.cssSelector(cssSelector));
        for (int i = 0; i < 3; i++) {
            String message = String.format("Header %d doesn't contain text %s", i, searchText);
            Assert.assertTrue(message, postTilesList.get(i).getText().toLowerCase().contains(searchText));
        }
    }

    @Test
    public void findJavaInNavBar() {
        WebElement java = driver.findElement(By.cssSelector("div#default-sidebar-element-java > a"));
        highlightElement(driver, java);
        java.click();
    }

    @After
    public void cleanUp() {
//        driver.quit();
    }

    @Test
    public void playFourthPodcast() {
        WebElement podcast = driver.findElement(By.xpath("//a[@href='/pod']"));
        podcast.click();
        wait.until(ExpectedConditions.urlToBe("https://dev.to/pod"));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("h3")));
        List<WebElement> podcasts = driver.findElements(By.tagName("h3"));
        podcasts.get(3).click();
        wait.until(ExpectedConditions.urlContains("stackpodcast"));
//        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("record-wrapper")));
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //poczekaj zanim wywalisz error że elementu nie ma, sprawdzaj co sekundę
        WebElement playArea = driver.findElement(By.className("record-wrapper"));
        playArea.click();
        WebElement initializing = driver.findElement(By.className("status-message"));
        wait.until(ExpectedConditions.invisibilityOf(initializing));
        String playAreaClassAttribute = playArea.getAttribute("class");
        boolean isPlaying = playAreaClassAttribute.contains("playing");
        assertTrue("Podcast isn't playing", isPlaying);


    }

}


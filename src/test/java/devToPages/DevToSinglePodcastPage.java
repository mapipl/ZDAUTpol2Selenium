package devToPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DevToSinglePodcastPage {
    WebDriverWait wait;
    WebDriver driver;

    @FindBy(className = "record-wrapper")
    public WebElement playArea;

    public DevToSinglePodcastPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void  playPodcast(){
        playArea.click();


    }


}

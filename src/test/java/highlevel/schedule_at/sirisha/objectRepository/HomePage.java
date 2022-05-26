package highlevel.schedule_at.sirisha.objectRepository;

import static highlevel.schedule_at.sirisha.commonUtilities.CommonBrowserSteps.loadHomePage;

import highlevel.schedule_at.sirisha.commonUtilities.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

  private final WebDriver driver;

  @FindBy(id = "sb_calendars")
  public WebElement calendersPageLink;

  @FindBy(id = "sb_settings")
  public WebElement settingsPageLink;

  public HomePage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public void ensureOnHomePage() {
    loadHomePage(driver);
  }

  public void openCalendarsPage() {
    ensureOnHomePage();
    calendersPageLink.click();
    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(ExpectedConditions.visibilityOfElementLocated(By.id("tb_calendars-tab")));
  }

  public void openSettingsPage() {
    ensureOnHomePage();
    settingsPageLink.click();
    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(ExpectedConditions.visibilityOfElementLocated(By.className("hl_settings--body")));
  }
}

package highlevel.schedule_at.sirisha.objectRepository;

import highlevel.schedule_at.sirisha.commonUtilities.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SettingsPage {
  private static final String TimezoneButtonXPath =
      "//select[@name='timezone']/following-sibling::button";
  private final WebDriver driver;

  @FindBy(id = "sb_business_info")
  WebElement businessInfoLink;

  @FindBy(xpath = TimezoneButtonXPath)
  WebElement timezoneButton;

  public SettingsPage(WebDriver driver) {

    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public String getTimezone() {
    businessInfoLink.click();
    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TimezoneButtonXPath)));
    String timezone = timezoneButton.getText();
    return timezone;
  }
}

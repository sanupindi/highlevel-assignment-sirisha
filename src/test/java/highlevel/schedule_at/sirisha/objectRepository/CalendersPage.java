package highlevel.schedule_at.sirisha.objectRepository;

import highlevel.schedule_at.sirisha.commonUtilities.Utils;
import highlevel.schedule_at.sirisha.models.BookingPerson;
import java.time.Duration;
import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CalendersPage {
  private final WebDriver driver;

  @FindBy(xpath = "//a[@id='tb_calendar-settings-top']")
  public WebElement calenderSettingsTab;

  @FindBy(id = "tb_apppontment-tab")
  public WebElement appointmentsTab;

  public CalendersPage(WebDriver driver) {

    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public void openCalendarSettings() {
    calenderSettingsTab.click();
    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(ExpectedConditions.visibilityOfElementLocated(By.id("card_view_container")));
  }

  public void openAppointments() {
    appointmentsTab.click();
    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(ExpectedConditions.visibilityOfElementLocated(By.className("appointment-actions")));
  }

  public String getAppointmentTime(BookingPerson bookingPerson) {
    String appointmentTimeXPath =
        String.format(
            "//table//h4[contains(text(), '%s')]/ancestor::tr/td[3]/div",
            bookingPerson.getFirstName());
    String appointmentTimeRawFromTable =
        driver.findElement(By.xpath(appointmentTimeXPath)).getText().trim();

    // The am/pm part of the time in the table is in lower case and this was causing problem with
    // the date time parse,
    // hence converting the am/pm part to uppercase.
    String[] parts = appointmentTimeRawFromTable.split(" ");
    parts[parts.length - 1] = parts[parts.length - 1].toUpperCase();
    String appointmentTimeFromTable = String.join(" ", parts);
    return appointmentTimeFromTable;
  }

  public void openAppointmentSelectionView(String calenderLink) {
    // Note: This is more generic, but there is no id/class available in DOM. It's better to have an
    // id or css class added to this button.
    String xpath = "//button[text()='" + calenderLink + "']";
    WebElement calenderLinkElement = driver.findElement(By.xpath(xpath));
    calenderLinkElement.click();
    String newTabHandle = new ArrayList<>(driver.getWindowHandles()).get(1);
    driver.switchTo().window(newTabHandle);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(
            ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[@class='datepicker-info']/div[@class='loader']")));
  }
}

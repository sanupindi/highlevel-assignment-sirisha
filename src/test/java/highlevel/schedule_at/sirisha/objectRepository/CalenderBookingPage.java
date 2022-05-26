package highlevel.schedule_at.sirisha.objectRepository;

import com.github.javafaker.Faker;
import highlevel.schedule_at.sirisha.commonUtilities.Utils;
import highlevel.schedule_at.sirisha.models.BookingDetails;
import highlevel.schedule_at.sirisha.models.BookingPerson;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CalenderBookingPage {

  // Code Attrib:
  // https://stackoverflow.com/questions/39741076/how-to-use-selenium-get-text-from-an-element-not-including-its-sub-elements#answer-42470664
  // Learnt something new today on how to get the immediate/direct content of a web element.
  private static final String ALL_DIRECT_TEXT_CONTENT =
      "var element = arguments[0], text = '';\n"
          + "for (var i = 0; i < element.childNodes.length; ++i) {\n"
          + "  var node = element.childNodes[i];\n"
          + "  if (node.nodeType == Node.TEXT_NODE"
          + " && node.textContent.trim() != '')\n"
          + "    text += node.textContent.trim();\n"
          + "}\n"
          + "return text;";
  private final WebDriver driver;

  @FindBy(xpath = "//button[text()='Continue']")
  public WebElement continueButton;

  @FindBy(xpath = "//button[text()='Schedule Meeting']")
  public WebElement scheduleMeetingButton;
  // getting list of all times on the page
  @FindBy(xpath = "//ul[@class='widgets-time-slots']/li")
  List<WebElement> allTimes;

  @FindBy(className = "multiselect__select")
  WebElement timeZoneDropdown;

  @FindBy(xpath = "//ul[@class='multiselect__content']/li")
  List<WebElement> timeZoneValuesDropdown;

  @FindBy(id = "first_name")
  WebElement firstNameTextbox;

  @FindBy(id = "last_name")
  WebElement lastNameTextbox;

  @FindBy(id = "phone")
  WebElement phoneTextbox;

  @FindBy(name = "email")
  WebElement emailTextbox;

  @FindBy(
      xpath =
          "//div[contains(@class, 'booking-info--datetime')]/div[contains(@class, 'booking-info-value')]")
  WebElement dateText;

  @FindBy(
      xpath =
          "//div[contains(@class, 'booking-info--datetime')]/div[contains(@class, 'booking-info-value')]/span")
  WebElement timeText;

  @FindBy(xpath = "//div[contains(@class, 'booking-info--timezone')]/div")
  WebElement timezoneText;

  @FindBy(className = "arrowNext")
  WebElement nextMonthButton;

  public CalenderBookingPage(WebDriver driver) {

    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public void selectDate(String date) {
    String xpath = "//td[@data-id ='" + date + "']";
    driver.findElement(By.xpath(xpath)).click();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(
            ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[@class='datepicker-info']/div[@class='loader']")));
  }

  /**
   * When the appointment booking page is loaded or next/prev month are clicked the calendar has a
   * loader popup which was resulting in sporadic date click failures, hence adding a wait untill
   * the loader is no longer in view.
   */
  private void waitForCalendarLoader() {
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(
            ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[@class='datepicker-info']/div[@class='loader']")));
  }

  /**
   * There is a possibility that there are no dates available in the current view of the calendar,
   * hence we need to keep moving to the next month untill there are dates available for selection.
   * Not being sure of how the UI is rendered when there are no available slots for a given date
   * (i.e i the date rendered as disabled or still shown as enabled), I have added extra logic to
   * check if there are open slots rendered for a selected date and if none found keep exploring
   * thenext day untill we move to nect month and so on. Took some time to wrap my head around it,
   * but finally was able to capture the logic
   */
  public void selectFirstAvailableDateAndSlotInternal(int iteration) {
    if (iteration > 10) {
      throw new Error("Could not find any available slot after " + (iteration + 1) + " tries.");
    }
    List<WebElement> selectableDateElements =
        driver.findElements(By.cssSelector(".vdpCell.selectable"));

    while (selectableDateElements.isEmpty()) {
      nextMonthButton.click();
      waitForCalendarLoader();
      selectableDateElements = driver.findElements(By.cssSelector(".vdpCell.selectable"));
    }

    for (WebElement dateElement : selectableDateElements) {
      if (dateElement != null) {
        dateElement.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        List<WebElement> timeSlots =
            driver.findElements(
                By.xpath("//ul[@class='widgets-time-slots']/li[@class='widgets-time-slot']"));
        if (timeSlots.isEmpty()) {
          continue;
        }
        timeSlots.get(0).click();
        return;
      }
    }
    selectFirstAvailableDateAndSlotInternal(iteration + 1);
  }

  public void selectFirstEnabledTimeSlot() {
    selectFirstAvailableDateAndSlotInternal(0);
  }

  public void clickContinue() {
    continueButton.click();

    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(ExpectedConditions.visibilityOf(firstNameTextbox));
  }

  /**
   * Get the list of all timezones from the dropdown. Randomly chose any timezone by generating a
   * random int between 0 and length-1 of the timezones list.
   */
  public void selectRandomTimeZone() {
    // click on the dropdown
    timeZoneDropdown.click();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

    // Get size of dropdown and select a random value
    int size = timeZoneValuesDropdown.size();

    int randomIndex = Faker.instance().random().nextInt(0, size);

    WebElement elementToClick = timeZoneValuesDropdown.get(randomIndex);
    elementToClick.click();
  }

  public BookingDetails enterDetailsAndSubmit(BookingPerson person) {

    firstNameTextbox.sendKeys(person.getFirstName());
    lastNameTextbox.sendKeys(person.getLastName());
    phoneTextbox.sendKeys(person.getPhoneNumber());
    emailTextbox.sendKeys(person.getEmail());

    scheduleMeetingButton.click();
    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(ExpectedConditions.visibilityOfElementLocated(By.className("confirmation-message")));

    // get the time,date and timezone for the schedule
    BookingDetails details = new BookingDetails();
    String dateString = getText(driver, dateText).trim();

    String time = timeText.getText();
    time = time.substring(1, time.lastIndexOf('-')).trim();

    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("EEE, MMM d, yyyy hh:mm a", Locale.ENGLISH);
    LocalDateTime date =
        LocalDateTime.parse(String.format("%s %s", dateString, time.toUpperCase()), formatter);

    details.setDate(date);
    details.setTimezone(timezoneText.getText());

    return details;
  }

  public String getText(WebDriver driver, WebElement element) {
    return (String) ((JavascriptExecutor) driver).executeScript(ALL_DIRECT_TEXT_CONTENT, element);
  }
}

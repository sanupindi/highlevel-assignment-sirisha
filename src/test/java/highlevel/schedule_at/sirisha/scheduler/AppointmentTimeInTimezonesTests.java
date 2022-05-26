package highlevel.schedule_at.sirisha.scheduler;

import static org.testng.AssertJUnit.*;

import highlevel.schedule_at.sirisha.commonUtilities.CommonBrowserSteps;
import highlevel.schedule_at.sirisha.commonUtilities.DriverManager;
import highlevel.schedule_at.sirisha.commonUtilities.Utils;
import highlevel.schedule_at.sirisha.models.BookingDetails;
import highlevel.schedule_at.sirisha.models.BookingPerson;
import highlevel.schedule_at.sirisha.objectRepository.CalenderBookingPage;
import highlevel.schedule_at.sirisha.objectRepository.CalendersPage;
import highlevel.schedule_at.sirisha.objectRepository.HomePage;
import highlevel.schedule_at.sirisha.objectRepository.SettingsPage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AppointmentTimeInTimezonesTests {
  WebDriver driver;
  CalendersPage calPage;
  CalenderBookingPage calBookPage;
  HomePage homePage;

  @Test(
      groups = "Scheduling",
      description =
          "Verifies that the the appointment created by a guest is displayed in right timezone. Preconditions: A team is set up with a valid calendar.")
  // Note: The test below can be enhanced further to consider the user/browser's locale.
  public void testAppointmentTimeIsDisplayedWithCorrectTimezoneConversion() {
    String currentHandle = driver.getWindowHandle();
    // Open Calendars Page
    homePage = new HomePage(driver);
    homePage.openCalendarsPage();

    // Open Calendar Settings page
    calPage = new CalendersPage(driver);
    calPage.openCalendarSettings();

    /*
     * Note: Currently, in interest of assignment completion time, I am using an already defined calendar
     * to run the automation against (defined via ENV Var: CALENDAR_SUT_NAME"). Ideally even the
     * calendar name should be either automatically picked up from calendaers page or better yet a
     * calendar is created as BeforeMethod of this test and deleted at the EndMethod.
     */
    // Open specific calendar to create a schedule.
    String calendarName = System.getProperty("CALENDAR_SUT_NAME");
    Assert.assertNotNull(
        calendarName, "Environment variable: CALENDAR_SUT_NAME is not set or is blank.");

    calPage.openAppointmentSelectionView(" " + calendarName + " ");

    // Open the booking page and schedule the date and time.
    calBookPage = new CalenderBookingPage(driver);

    // Select random timezone
    calBookPage.selectRandomTimeZone();
    calBookPage.selectFirstEnabledTimeSlot();
    calBookPage.clickContinue();

    BookingPerson bookingPerson = Utils.ForData.getBookingPerson();
    BookingDetails bookingDetails = calBookPage.enterDetailsAndSubmit(bookingPerson);

    /*
    Learnt basics about the timezone handling with java.time api as part of this exercise.
    */
    ZoneId bookingTimeZoneId =
        ZoneId.of(Utils.ForString.getTimezoneFromAppTimezoneString(bookingDetails.getTimezone()));
    ZonedDateTime bookingInZonedDateTime =
        ZonedDateTime.of(bookingDetails.getDate(), bookingTimeZoneId);
    driver.switchTo().window(currentHandle);

    // Get User's timezone preference.
    homePage.openSettingsPage();

    SettingsPage settingsPage = new SettingsPage(driver);

    String userTimeZone = settingsPage.getTimezone();
    assertNotNull("Timezone is empty", userTimeZone);
    assertTrue("Timezone is empty", userTimeZone.trim().length() > 0);
    userTimeZone = userTimeZone.trim();

    homePage.openCalendarsPage();

    // Get the latest appointment under the bookingPerson

    calPage.openAppointments();

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

    assertFalse(
        "Could not extract appointment time for latest booking.",
        appointmentTimeFromTable.isEmpty());

    DateTimeFormatter userDateTimeformatter =
        DateTimeFormatter.ofPattern("MMM d yyyy, hh:mm a", Locale.ENGLISH);

    String timezonePartForUser = Utils.ForString.getTimezoneFromAppTimezoneString(userTimeZone);

    LocalDateTime selectedDateTimeForUser =
        LocalDateTime.parse(appointmentTimeFromTable, userDateTimeformatter);

    ZonedDateTime userSelectedZonedDateTime =
        ZonedDateTime.of(selectedDateTimeForUser, ZoneId.of(timezonePartForUser));

    assertTrue(
        String.format(
            "Booking time: %s is not equal to User Time: %s",
            bookingInZonedDateTime, userSelectedZonedDateTime),
        userSelectedZonedDateTime.isEqual(bookingInZonedDateTime));
  }

  @Test(
      groups = "Scheduling",
      description =
          "Verifies that when multiple guests are trying to book an appointment for the same slot, the race condition is appropriately addressed. Preconditions: A team is set up with a valid calendar.")
  public void testConcurrentAppointmentsForSameSlotOfCalendarShowErrorOnExceedingTheLimit() {
    /* In order to economize on the time avaiable for this assignment, I could not implement this test case, however I am detailing the steps invovled
      The intent of this test case is to ensure race conditions/concurrent session trying to book the same slot is addressed appropriately without making double bookings for the same slot.
    * 1) Pre Conditions: Ensure a valid Team/Calendar exists, it has 1 team member (to minimize the need to create concurrent tabs.)
    * 2) As guest-1 open the appointment page in tab: Tab-1 to make an appointment, select date: Date-1, slot: Slot-1, Click continue, Enter guest details (name, email etc.)
    * 3) As guest-2 open the appointment page in tab: Tab-2 to make an appointment, select date: Date-1, slot: Slot-1, Click continue, Enter guest details (name, email etc.)
    * 4) On Tab-1 (used by guest-1) Click on "Schedule Meeting" - Expected Result: The guest should get a success message.
    * 5) On Tab-2 (used by guest-2) Click on "Schedule Meeting" - Expected Result: The guest shold get a message mentioning the slot is no longer available.
    */
  }

  @Test(
      groups = "Scheduling",
      description =
          "Verifies that if a user is using a different locale (like fr-FR), the dates/months etc. are displayed appropriately (e.g: Mai instead of May for month.).")
  public void testAppointmentsPageAdheresToBrowserLocale() {
    /* In order to economize on the time avaiable for this assignment, I could not implement this test case, however I am detailing the steps invovled
      The intent of this test case is to ensure the user/browser's locale is respected by the app.
    * 1) Pre Conditions: Ensure a valid Team/Calendar exists.
    * 2) As guest-1, with the driver's locale set as something other than en-US (e.g: fr-FR) open the appointment page in tab: Tab-1 to make an appointment, select date: Date-1, slot: Slot-1, Click continue.
    * 4) Expected Result: The resulting page should display the selected date/time in the set locale.
    */
  }

  @BeforeMethod
  public void beforeMethod() {
    /*
    Note: Ideally here we would create a fresh team and calendar to set up the pre-condition and then delete both the team and calendar in the afterMethod.
    This way we can isolate the test case and minimize impact because of any external factors (changes in the team by another test case etc.).
    */
    driver = DriverManager.getWebDriver("chrome");
    CommonBrowserSteps.loginToApp(driver);
  }

  @AfterMethod
  public void afterMethod() {
    driver.quit();
  }
}

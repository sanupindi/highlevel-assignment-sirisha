package highlevel.schedule_at.sirisha.commonUtilities;

import static highlevel.schedule_at.sirisha.commonUtilities.Utils.AppHomeUrl;

import highlevel.schedule_at.sirisha.objectRepository.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonBrowserSteps {

  public static void loginToApp(WebDriver driver) {
    driver.get(AppHomeUrl);
    driver.manage().window().maximize();
    LoginPage loginPage = new LoginPage(driver);
    loginPage.login(Utils.ForApp.getUserName(), Utils.ForApp.getPassword());
    new WebDriverWait(driver, Utils.DefaultWaitDuration)
        .until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));
  }

  public static boolean isLoggedIn(WebDriver driver) {
    return !driver.findElements(By.id("recent_activities-toggle")).isEmpty();
  }

  public static void loadHomePage(WebDriver driver) {
    if (!isLoggedIn(driver)) {
      loginToApp(driver);
    } else if (driver.findElements(By.id("dashboard")).isEmpty()) {
      // The user is logged in, but not on dashboard/home page, hence load the home URL.
      driver.get(AppHomeUrl);
      new WebDriverWait(driver, Utils.DefaultWaitDuration)
          .until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));
    }
  }
}

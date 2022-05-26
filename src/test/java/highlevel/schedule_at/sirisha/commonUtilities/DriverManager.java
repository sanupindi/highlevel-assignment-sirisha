package highlevel.schedule_at.sirisha.commonUtilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {

  private static final Map<String, WebDriver> DriversMap = new HashMap<>();

  /**
   * Though the testing was done using chrome, this method provides a central way of getting the Web
   * Driver as a singleton. This method call is thread safe since it's wrapped as a synchronized
   * method..
   */
  public static synchronized WebDriver getWebDriver(String browser) {
    switch (browser) {
      case "chrome":
        if (!DriversMap.containsKey("chrome")) {
          WebDriverManager.chromedriver().setup();
          ChromeOptions options = new ChromeOptions();
          options.addArguments("--disable-notifications");
          WebDriver driver = new ChromeDriver(options);
          DriversMap.put("chrome", driver);
        }
        return DriversMap.get("chrome");
      case "firefox":
        if (!DriversMap.containsKey("firefox")) {
          WebDriverManager.firefoxdriver().setup();
          WebDriver driver = new FirefoxDriver();
          DriversMap.put("firefox", driver);
        }
        return DriversMap.get("firefox");
      default:
        throw new RuntimeException("Browser: " + browser + " is not recognized.");
    }
  }
}

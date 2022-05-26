package highlevel.schedule_at.sirisha.objectRepository;

import highlevel.schedule_at.sirisha.commonUtilities.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
  private final WebDriver driver;

  @FindBy(id = "email")
  WebElement emailTextbox;

  @FindBy(id = "password")
  WebElement passwordTextbox;

  // Note: The DOM/HTML generated can have an id/css class assigned to the sign-in button for better
  // selection.
  @FindBy(xpath = "//button[text()='Sign in']")
  WebElement submitButton;

  public LoginPage(WebDriver driver) {

    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public void login(String userName, String password) {
    emailTextbox.clear();
    passwordTextbox.clear();
    emailTextbox.sendKeys(userName);
    passwordTextbox.sendKeys(password);
    submitButton.click();
    // id="dashboard"

    WebDriverWait wait = new WebDriverWait(driver, Utils.DefaultWaitDuration);
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));
  }
}

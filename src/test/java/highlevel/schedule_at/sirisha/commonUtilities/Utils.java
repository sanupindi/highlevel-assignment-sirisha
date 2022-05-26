package highlevel.schedule_at.sirisha.commonUtilities;

import com.github.javafaker.Faker;
import highlevel.schedule_at.sirisha.models.BookingPerson;
import io.github.cdimascio.dotenv.Dotenv;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** A dumpyard for any utility functions categorized by the context. */
public class Utils {

  public static final Duration DefaultWaitDuration = Duration.ofSeconds(30);
  public static final String AppHomeUrl = "https://app.gohighlevel.com/";
  private static Dotenv dotEnv = null;

  private static synchronized Dotenv getDotenv() {
    if (dotEnv == null) {
      dotEnv = Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().systemProperties().load();
    }
    return dotEnv;
  }

  public static class ForLocalDate {
    public static String DefaultDatePattern = "yyyy-M-d";
    public static DateTimeFormatter DefaultDateFormatter =
        DateTimeFormatter.ofPattern(DefaultDatePattern);

    public static String asString(LocalDate date, DateTimeFormatter dateFormat) {
      return dateFormat.format(date);
    }

    public static String asString(LocalDate date) {
      return asString(date, DefaultDateFormatter);
    }
  }

  public static class ForApp {

    public static String getUserName() {
      return getDotenv().get("HIGHLEVEL_APP_USERNAME");
    }

    public static String getPassword() {
      return getDotenv().get("HIGHLEVEL_APP_PASSWORD");
    }
  }

  public static class ForData {
    public static String concatAndAddRandomNumberAtEnd(String first, String second) {
      return String.format(
          "%s%s%s", first, second, Faker.instance().random().nextInt(10000, 99999));
    }

    public static BookingPerson getBookingPerson() {
      String firstName = concatAndAddRandomNumberAtEnd("AT-", Faker.instance().name().firstName());
      String lastName = concatAndAddRandomNumberAtEnd("AT-", Faker.instance().name().lastName());
      String email =
          String.format(
              "AT-%s%s",
              Faker.instance().random().nextInt(100, 9999),
              Faker.instance().internet().emailAddress());
      String phoneNumber = "9999999999";

      return new BookingPerson(firstName, lastName, email, phoneNumber);
    }
  }

  public static class ForString {
    public static String getTimezoneFromAppTimezoneString(String input) {
      return input.split(" ")[1];
    }
  }
}

package highlevel.schedule_at.sirisha.models;

import java.time.LocalDateTime;

public class BookingDetails {
  private String timezone;
  private LocalDateTime date;

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }
}

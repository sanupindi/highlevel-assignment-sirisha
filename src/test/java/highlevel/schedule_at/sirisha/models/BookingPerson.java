package highlevel.schedule_at.sirisha.models;

public class BookingPerson {
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;

  public BookingPerson(String firstName, String lastName, String email, String phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}

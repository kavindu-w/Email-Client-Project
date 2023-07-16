// An interface to separate birthday greetable recipients so they could have different birthday wishes.
package email_client;

public interface IBirthdayGreetable {
    public String birthdayGreet();

    public String getName();

    public String getBirthday();

    public String getEmail();

    public String toString();

}

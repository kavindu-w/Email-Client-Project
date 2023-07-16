/**
 * This class is a subclass of Recipient and implements the IBirthdayGreetable interface.
 */
package email_client;

public class Personal extends Recipient implements IBirthdayGreetable {
    private String birthday;
    private String nickName;

    protected Personal(String name, String nickName, String email, String birthday) {
        super(name, email);
        this.nickName = nickName;
        this.birthday = birthday;
    }

    /**
     * unique birthday wish for personal friend
     * 
     * @return String
     */
    public String birthdayGreet() {
        return "hugs and love on your birthday. " + this.getName();
    }

    // getter
    /**
     * @return String
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * @return String
     */
    public String getNickName() {
        return nickName;
    }

    // setters
    /**
     * @param birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * @param nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * overrided toString method to print out recipient details
     * @return String
     */
    @Override
    public String toString() {
        return "Name: " + this.getName()
                + "\n   Type     : Personal friend"
                + "\n   Nick name: " + nickName
                + "\n   Email    : " + this.getEmail();
    }
}

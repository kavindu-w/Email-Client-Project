/**
 * OfficialFriend is a subclass of Official and implements IBirthdayGreetable
 */
package email_client;

public class OfficialFriend extends Official implements IBirthdayGreetable {
    private String birthday;

    protected OfficialFriend(String name, String email, String designation, String birthday) {
        super(name, email, designation);
        this.birthday = birthday;
    }

    /**
     * unique birthday wish for official friend
     * 
     * @return String
     */
    public String birthdayGreet() {
        return "Wish you a Happy Birthday. " + this.getName();
    }

    // getter
    /**
     * @return String
     */
    public String getBirthday() {
        return birthday;
    }

    // setter
    /**
     * @param birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * overrided toString method to print out recipient details
     * @return String
     */
    @Override
    public String toString() {
        return "Name: " + this.getName()
                + "\n   Type       : Office/Official friend"
                + "\n   Email      : " + this.getEmail()
                + "\n   Designation: " + this.getDesignation();
    }
}

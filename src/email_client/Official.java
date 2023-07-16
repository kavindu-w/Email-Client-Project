/**
 * Official is a subclass of Recipient
 */
package email_client;

public class Official extends Recipient {
    private String designation;

    protected Official(String name, String email, String designation) {
        super(name, email);
        this.designation = designation;
    }

    // getter
    /**
     * @return String
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * overrided toString method to print out recipient details
     * @return String
     */
    @Override
    public String toString() {
        return "Name: " + this.getName()
                + "\n   Type       : Official"
                + "\n   Email      : " + this.getEmail()
                + "\n   Designation: " + getDesignation();
    }
}

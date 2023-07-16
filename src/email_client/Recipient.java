/**
 * The Recipient class is an abstract class that represents a recipient of an email
 */
package email_client;

public abstract class Recipient {
    private String name;
    private String email;
    private static int noOfRecipients = 0; // to keep track of recipient count in the current application

    public Recipient(String name, String email) {
        this.name = name;
        this.email = email;
        noOfRecipients += 1;
    }

    // getters
    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * @return String
     */
    public String getEmail() {
        return email;
    }

    // setters
    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter for recipient count in current application
     * 
     * @return int
     */
    public int getNoOfRecipients() {
        return noOfRecipients;
    }
}

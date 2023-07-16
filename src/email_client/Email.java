/**
 * This class is a representation of an email. It has a from, to, date, subject, and body.
 */
package email_client;

import java.io.Serializable;

public class Email implements Serializable {
    private String from;
    private String to;
    private String date;
    private String subject;
    private String body;
    // User-defined SerialVersionUID initialization
    // Whenever you make incompatible changes to the class, increment this version.
    private static final long serialVersionUID = 10l;

    public Email(String from, String to, String date, String subject, String body) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.subject = subject;
        this.body = body;
    }

    
    /** 
     * overrided toString method to print out email object
     * @return String
     */
    @Override
    public String toString() {
        return "___________________________________________________________"
                + "\nFrom   : " + from
                + "\nTo     : " + to
                + "\nDate   : " + date
                + "\nSubject: " + subject
                + "\nBody   : " + body;
    }

    /** 
     * method to write email objects to csv file as comma separated lines (for analysis)
     * @return String
     */
    public String toCsvString() {
        return from + "," + to + "," + date + "," + subject + "," + body;
    }

    // getters
    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDate() {
        return date;
    }

}

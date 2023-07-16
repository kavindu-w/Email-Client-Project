/**
 * Emailer class is a singleton class that connects to gmail smtp and sends emails
 */
package email_client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Emailer {
    private static Emailer emailer = null;

    private String username;
    private String password;

    // files for email obj serialization, one .txt file and one .ser file for backup
    private File emailLogFile;
    private File emailLogFileBackup;

    // email connection fields
    private Properties prop;
    private Session session;

    private Emailer(String username, String password) {
        this.username = username;
        this.password = password;

        // creating email log files (to serialize email objects)
        emailLogFile = new File("emailLog.ser");
        emailLogFileBackup = new File("emailLogBackup.txt");

        // connect to gmail smtp
        connect(username, password);
        System.out.println("Email connection successful.\n");
    }

    /**
     * public method ensuring one instance of emailer is maintained
     */
    public static Emailer getInstance(String username, String password) {
        if (emailer == null)
            emailer = new Emailer(username, password);
        return emailer;
    }

    /**
     * connect to gmail smtp
     * 
     * @param username
     * @param password
     */
    private void connect(String username, String password) {
        prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS
        // connect
        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        
        // validating username and password
        // try connecting, if fails throw invalid username, password error 
        // then get correct username and password and reconnect
        System.out.println("Connecting to email...\n");
        try (Transport transport = session.getTransport("smtp")) {
            transport.connect("smtp.gmail.com", username, password);
        } catch (AuthenticationFailedException e) {
            System.out.println("Email authentication failed, invalid username or password given.\n"
                            + "Please enter a correct username, without '@gmail.com' part: ");

            @SuppressWarnings("resource") // if you open another scanner object and close it, will throw
                                          // NoSuchElementException in main program
            Scanner scanner2 = new Scanner(System.in);
            String userInput = scanner2.nextLine().trim();
            setUsername(userInput);

            System.out.println("Please enter a correct password: ");
            userInput = scanner2.nextLine().trim();
            setPassword(userInput);
            // try connecting again
            connect(this.username, this.password);
        } catch (MessagingException e) {
            System.out.println("Error sending mail.");
        }
    }

    /**
     * method to send emails
     * 
     * @param email object
     */
    public void send(Email email) {
        try {
            // check if reciever email is valid
            if (InputUtils.isValidEmail(email.getTo())) {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(this.username));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(email.getTo()));
                message.setSubject(email.getSubject());
                message.setContent(email.getBody(),  "text/html");
                // message.setText(email.getBody());

                Transport.send(message);

                System.out.println("Email sent successfully.");

                // log the email as obj (serialize)
                log(emailLogFile, email);
                log(emailLogFileBackup, email);
            }
        } catch (MessagingException e) {
            System.out.println("Error sending mail.");
            e.printStackTrace();
        }
    }

    // can be extended to recieve emails

    /**
     * private method to serialize email object to a file
     * 
     * @param file
     * @param email
     */
    private void log(File file, Email email) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            // check if the log file already exists to stop rewriting the header again
            // else will lead to stream corruption
            if (file.exists() && file.length() != 0) {
                fileOutputStream = new FileOutputStream(file, true);
                objectOutputStream = new ObjectOutputStream(fileOutputStream) {
                    @Override
                    protected void writeStreamHeader() throws IOException {
                        reset();
                    }
                };
            } else {
                fileOutputStream = new FileOutputStream(file, true);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
            }
            objectOutputStream.reset(); // reset the stream to stop writing the header again
            objectOutputStream.writeObject(email);
            objectOutputStream.flush();

        } catch (FileNotFoundException e) {
            System.out.println("Error logging, File not found");
        } catch (IOException e) {
            System.out.println("Error logging,  error initializing stream");
        } finally {
            // close the streams
            if (objectOutputStream != null)
                try {objectOutputStream.close();} catch (Exception ignore) {}
            if (fileOutputStream != null)
                try { fileOutputStream.close();} catch (Exception ignore) {}
        }
    }

    /**
     * It reads an object from the emailLog file, casts it to an Email object, 
     * and adds it to an ArrayList if the date matches the date passed in
     * 
     * @param date the date of the email
     * @return An ArrayList of Email objects.
     */
    public ArrayList<Email> extractFromLog(String date) {
        ArrayList<Email> emails = new ArrayList<Email>();
        // try with resources is used here
        // this will execute implicit finally blocks for each streams, 
        // if either one throws exceptions both will be implicitly closed by jvm
        try (FileInputStream fileInputStream = new FileInputStream(emailLogFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {

            while (fileInputStream.available() != 0) {
                Email email = null;
                // Read objects and cast to an email object
                email = (Email) objectInputStream.readObject();
                if (email != null) {
                    // add to array list if email been sent on given date
                    if (date.equals(email.getDate())) {
                        emails.add(email);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error initializing stream.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error extracting mail objects.");
            e.printStackTrace();
        }
        return emails;
    }

    /**
     * getter for username
     * 
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * setter for username
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * getter for password
     * 
     * @param password
     */
    public String getPassword() {
        return password;
    }

    /**
     * setter for password
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}

// Registration number: 200694G0
// Name: WARNAKULASURIYA AK (ARACHCHIGE KAVINDU WARNAKULASURIYA)

// create a package named email_client, else comment out the following line 
package email_client;

// javax.mail and javax.activation packages needed for email sending
// javax.mail jar file download link: https://bit.ly/2v4Enoa
// javax.acitvation jar file download link: https://bit.ly/3JuN7JS

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Email_Client {
    public static void main(String[] args) throws Exception {
        // file to store recipient details
        File clientListFile = new File("clientList.txt");
        if (!clientListFile.exists()) clientListFile.createNewFile(); // new file is created if not already exists

        // getting today's date at start
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String today = dtf.format(LocalDateTime.now());

        Scanner scanner = new Scanner(System.in);

        // for adding recpients(recipient factory)
        RecipientCreator recipientCreator = new RecipientCreator();
        Recipient recipient = null;

        // getting the recipient info from file to program at start
        ArrayList<String> records = FileUtils.readFromFile(clientListFile);
        for (String record : records) {
            if (InputUtils.isRecipientRecordValid(record))
                recipient = recipientCreator.createRecipient(record);
        }
        
        // helper class for sending emails
        Emailer emailer = Emailer.getInstance("SENDER EMAIL", "SENDER PASSWORD(Careful about 2 factor authorization)");

        // sending mails to recipients who are having birthdays today at start
        System.out.println(
                "Checking for recipients who are having birthday today and sending them birthday greetings...\n");
        ArrayList<IBirthdayGreetable> birthdayRecepients = recipientCreator.getBirthdayRecipients();
        boolean isBirthdayToday = false;
        for (IBirthdayGreetable birthdayRecipient : birthdayRecepients) {
            if (birthdayRecipient.getBirthday().equals(today)) {
                isBirthdayToday = true;
                System.out.println(
                        "It's " + birthdayRecipient.getName() + "'s birthday today." + "\nSending a birthday mail...");
                // email object, so that it can be serialized to file
                // Email email = new Email("SENDER EMAIL",
                // birthdayRecipient.getEmail(), today, "Happy Birthday",
                // birthdayRecipient.birthdayGreet());

                // tester code
                Email email = new Email("SENDER EMAIL", "RECIEVER EMAIL", today,
                        "Happy Birthday", birthdayRecipient.birthdayGreet());
                emailer.send(email);
            }
        }
        if (!isBirthdayToday)
            System.out.println("No recipients are having birthday today");

        //###################################################################
        // main program starts here
        // repeating program until user exits
        // "scan" label to jump from switch cases
        scan: while (true) {
            System.out.println("====================================================================\n"
                    + "Enter option type: \n"
                    + " 1 - Adding a new recipient\n"
                    + " 2 - Sending an email\n"
                    + " 3 - Printing out all the recipients who have birthdays\n"
                    + " 4 - Printing out details of all the emails sent\n"
                    + " 5 - Printing out the number of recipient objects in the application\n"
                    + " 0 - Exit the program\n"
                    + "====================================================================\n");
            
            String userInput = scanner.nextLine().trim();
            int option = InputUtils.tryParseToInteger(userInput); // validate if a valid option is entered

            switch (option) {
                case 1: // Adding a new recipient
                    // input format - Official: nimal,nimal@gmail.com,ceo
                    userInput = scanner.nextLine().trim();
                    // check if a valid recipient record entered
                    if (InputUtils.isRecipientRecordValid(userInput)) {
                        // check if recipient already exist by reading from clientList.txt file
                        if (FileUtils.validateRecord(clientListFile, userInput)) {
                            System.out.println("The recipient already exists.");
                            continue scan;
                        }
                        // create recipient if unique
                        recipient = recipientCreator.createRecipient(userInput);
                        System.out.println("User succesfully added.");
                        
                        // sending birthday mails if the new recipient added have birthday today
                        if (recipient instanceof OfficialFriend || recipient instanceof Personal) {
                            IBirthdayGreetable birthdayRecipient = (IBirthdayGreetable) recipient;
                            if (birthdayRecipient.getBirthday().equals(today)) {
                                // email object, so that it can be serialized to file
                                // Email email = new Email("SENDER EMAIL",
                                // birthdayRecipient.getEmail(), today,
                                // "Happy Birthday",
                                // birthdayRecipient.birthdayGreet());

                                // tester code
                                Email email = new Email("SENDER EMAIL", "RECIEVER EMAIL",
                                    today, "Happy Birthday", birthdayRecipient.birthdayGreet());
                                System.out.println("It's " + birthdayRecipient.getName() + "'s birthday today."
                                    + "\nSending a birthday mail...");
                                emailer.send(email);
                            }
                        }
                        // store new recipient's details in clientList.txt file
                        FileUtils.writeToFile(clientListFile, userInput);
                    }
                    continue scan;
                //###################################################################
                case 2: // Sending an email
                    // This is to send email to anyone, 
                    // don't need to validate if our clientList.txt have it
                    // input format - email, subject, content
                    userInput = scanner.nextLine().trim();
                    if (InputUtils.isValidEmailSendRequest(userInput)) {
                        String[] data = userInput.trim().split("[,]");
                        // store the date of sending for the email log (case 4)
                        System.out.println("Sending an email...");
                        Email email = new Email("SENDER EMAIL", data[0], today, data[1], data[2]);
                        emailer.send(email);
                    }
                    continue scan;
                //###################################################################
                case 3: // Printing out all the recipients who have birthdays on a given date
                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    userInput = scanner.nextLine().trim();
                    if (InputUtils.isValidDate(userInput)) {
                        String date = userInput;
                        System.out.println("Recipients who have birthday on " + date + ":");
                        boolean isBirthdayOnDate = false;
                        for (IBirthdayGreetable birthdayRecipient : recipientCreator.getBirthdayRecipients()) {
                            if (birthdayRecipient.getBirthday().equals(date)) {
                                isBirthdayOnDate = true;
                                System.out.println(birthdayRecipient.toString());
                            }
                        }
                        if (!isBirthdayOnDate)
                            System.out.println("No recipients are having birthday on " + date);
                    }
                    continue scan;
                //###################################################################
                case 4: // Printing out details of all the emails sent on a given date
                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    userInput = scanner.nextLine().trim();
                    if (InputUtils.isValidDate(userInput)) {
                        String date = userInput;
                        // extracting (deserializing) email objects that have been sent on that date to a array list
                        ArrayList<Email> emails = emailer.extractFromLog(date);
                        if (emails.size() == 0) {
                            System.out.println("No emails have been sent out on " + date );
                        } else {
                            System.out.println("All the emails sent out on " + date + ":");
                            // csv file to store email log report on a given date, 
                            // so user can directly import to excel and analyze
                            File reportEmailsSent = new File("report_emails_sent_" + date.replace("/", "_") + ".csv");
                            if (!reportEmailsSent.exists()) reportEmailsSent.createNewFile(); // new file is created if not already exists
                            // write the csv file header
                            FileUtils.writeToFile(reportEmailsSent, "From,To,Date,Subject,Body");
                            for (Email e : emails) {
                                System.out.println(e); // display the emails, will invoke overrided `toString()` method
                                FileUtils.writeToFile(reportEmailsSent, e.toCsvString()); // write in csv format(seperated by commas)
                            } 
                        }
                    }
                    continue scan;
                //###################################################################
                case 5: // Printing out the number of recipient objects in the application
                    System.out.println("Number of recipients: " + recipient.getNoOfRecipients());
                    continue scan;
                case 0: // exit the program
                    System.out.println("Exiting the program...");
                    scanner.close();
                    break scan;
            }
        }
    }
}

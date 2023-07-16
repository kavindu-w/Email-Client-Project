/**
 * It's a static helper class for input handling
 */
package email_client;

import java.time.LocalDate;
import java.time.format.ResolverStyle;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InputUtils {
    private InputUtils() {
    }

    /**
     * method for checking if a valid option being enetered and returning the
     * converted integer value
     * 
     * @param str
     * @return Integer
     */
    public static Integer tryParseToInteger(String str) {
        if (str.matches("[0-5]"))
            return Integer.parseInt(str);
        System.out.println("Invalid option " + str);
        return -1;
    }

    /**
     * check if the entered recipient record is valid before adding a recipient
     * 
     * @param str
     * @return boolean
     */
    public static boolean isRecipientRecordValid(String str) {
        // input eg: Official: nimal,nimal@gmail.com,ceo
        // first removed the spaces because there is a space in every input after ":"
        // secondly splitted into to an array using ":" and ","
        String[] data = str.replace(" ", "").split("[:,]");
        boolean isValid = true;
        switch (data[0].toLowerCase()) {
            case "official":
                // check for size splitted (has: type, name, email, designation)
                if (data.length != 4) {
                    System.out.println("Invalid number of arguments provided for recipient type:\n"
                    + "Number of arguments provided: " + data.length + "\nNumber of arguments expected: 4");
                    isValid = false;
                    break;
                }
                if (!isValidEmail(data[2]))
                    isValid = false;
                break;
            case "office_friend":
                // check for size splitted (has: type, name, email, designation, birthday)
                if (data.length != 5) {
                    System.out.println("Invalid number of arguments provided for recipient type:\n"
                            + "Number of arguments provided: " + data.length + "\nNumber of arguments expected: 5");
                    isValid = false;
                    break;
                }
                if (!isValidEmail(data[2]))
                    isValid = false;
                if (!isValidDate(data[4]))
                    isValid = false;
                break;

            case "personal":
                // check for size splitted (has: type, name, nickName, email, birthday)
                if (data.length != 5) {
                    System.out.println("Invalid number of arguments provided for recipient type:\n"
                            + "Number of arguments provided: " + data.length + "\nNumber of arguments expected: 5");
                    isValid = false;
                    break;
                }
                if (!isValidEmail(data[3]))
                    isValid = false;
                if (!isValidDate(data[4]))
                    isValid = false;
                break;
            default:
                System.out.println("Invalid number of arguments provided for recipient type:\n");
                isValid = false;
        }
        return isValid;
    }

    /**
     * check if a valid email send request have been given
     * 
     * @param str
     * @return boolean
     */
    public static boolean isValidEmailSendRequest(String str) {
        // input format - email, subject, content
        String[] data = str.trim().split("[,]");
        boolean isValid = true;
        if (data.length != 3) {
            System.out.println("Invalid number of arguments provided for email request:\n"
                    + "Number of arguments provided: " + data.length + "\nNumber of arguments expected: 3 (email, subject, content)");
            isValid = false;
        }
        if (!isValidEmail(data[0]))
            isValid = false;
        return isValid;
    }

    /**
     * check if the given date is valid and according to the format yyyy/MM/dd
     * 
     * @return boolean
     */
    public static boolean isValidDate(String dateStr) {
        try {
            // strict checking including 30,31 day check for month and leap years
            LocalDate.parse(dateStr,
                    DateTimeFormatter.ofPattern("uuuu/MM/dd")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date given");
            return false;
        }
        return true;
    }
    
    /**
     * verify if a given email address is valid
     * 
     * @param email
     * @return boolean
     */
    public static boolean isValidEmail(String email) {
        // charcters that are allowed in a standard email address
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        // initialize a Pattern  and matcher object
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        // return if the given email address is valid or not
        if (matcher.matches())
            return true;
        System.out.println("Invalid email address.");
        return false;
    }

}

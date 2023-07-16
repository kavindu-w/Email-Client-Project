/**
 * This class is a factory class that creates recipients and keeps track of them.
 */
package email_client;

import java.util.ArrayList;

public class RecipientCreator {
    // array lists to keep track of recipients and birthday recipients objects
    ArrayList<Recipient> recepients;
    ArrayList<IBirthdayGreetable> birthdayRecepients;

    public RecipientCreator() {
        recepients = new ArrayList<Recipient>();
        birthdayRecepients = new ArrayList<IBirthdayGreetable>();
    }

    /**
     * factory method to create recipients
     * 
     * @param str
     * @return Recipient
     */
    public Recipient createRecipient(String str) {
        Recipient recipient = null;
        // first removed the spaces because there is a space in every input after ":"
        // eg: Official: nimal,nimal@gmail.com,ceo
        // secondly splitted into to an array using ":" and ","
        String[] data = str.replace(" ", "").split("[:,]");
        switch (data[0].toLowerCase()) {
            case "official":
                // name, email, designation
                Official official = new Official(data[1], data[2], data[3]);
                recepients.add(official);
                recipient = official;
                break;
            case "office_friend":
                // name, email, designation, birthday
                OfficialFriend officialFriend = new OfficialFriend(data[1], data[2], data[3], data[4]);
                recepients.add(officialFriend);
                birthdayRecepients.add(officialFriend);
                recipient = officialFriend;
                break;
            case "personal":
                // name, nickName, email, birthday
                Personal personal = new Personal(data[1], data[2], data[3], data[4]);
                recepients.add(personal);
                birthdayRecepients.add(personal);
                recipient = personal;
                break;
        }
        return recipient;
    }

    /**
     * getter for birthday recipient objects
     * 
     * @return ArrayList<IBirthdayGreetable>
     */
    public ArrayList<IBirthdayGreetable> getBirthdayRecipients() {
        return birthdayRecepients;
    }

    /**
     * getter for recipient objects
     * 
     * @return ArrayList<Recipient>
     */
    public ArrayList<Recipient> getRecipients() {
        return recepients;
    }

    /**
     * getter for total recipient objects count
     * 
     * @return int
     */
    public int getTotalRecipients() {
        return recepients.size();
    }
}

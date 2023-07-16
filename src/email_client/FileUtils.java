/**
 * It's a static helper class for file handling
 */
package email_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class FileUtils {
    private FileUtils() {
    }

    /**
     * read line by line from a file
     * 
     * @param file
     * @return ArrayList<String>
     */
    public static ArrayList<String> readFromFile(File file) {
        ArrayList<String> records = new ArrayList<String>();
        // try with resources is used here
        // this will execute implicit finally blocks for each streams, 
        // if either one throws exceptions both will be implicitly closed by jvm
        try (FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);) {
            String line = null;
            while ((line = reader.readLine()) != null)
                records.add(line);
        } catch (IOException e) {
            System.out.println("Error reading from file");
        }
        return records;
    }

    /**
     * validate a record from a file
     * 
     * @param file
     * @param str
     * @return boolean
     */
    public static boolean validateRecord(File file, String str) {
        try (FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                // if entered record match with the data in text file then user already exists
                if (line.equals(str))
                    return true;
            }
        } catch (IOException e) {
            System.out.println("Error reading from file, File not found");
        } 
        return false;
    }

    /**
     * write a record to a file
     * 
     * @param file
     * @param str
     */
    public static void writeToFile(File file, String str) {
        try (FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);) {
            writer.write(str + "\n");
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file, File not found");
        } catch (IOException e) {
            System.out.println("Error writing to file");
        }
    }
}

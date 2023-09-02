package chatt;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class MessageManager {

    // Handle Each message Object being sent
    // Handle different types
    // Data Location
    // Handle Upload File
    // Handle Download File/Save
    // Call Scene Manager to draw what how
    public static Message message = null;
    public static ArrayList<Message> messages = new ArrayList();

    public static void MessageRecieved(Message msg) {
        message = msg;
        // Decide What kind is it
        if (message.type == Message.types.TX) {
            // Text message
            // Pass to scene manager Name, Colour, text, Date
            DateTime dateTime = message.date;
            DateTimeZone timeZone = DateTimeZone.forID("Africa/Cairo");
            DateTime newDateTime = dateTime.withZone(timeZone);

            int hour = newDateTime.getHourOfDay();
            int minute = newDateTime.getMinuteOfHour();
            String time = Integer.toString(hour) + ":" + Integer.toString(minute);

            SceneManager.addTextMessage(message.getDataAsString(), message.name, time);
        } else {
            String extension = message.getExtension();
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")
                    || extension.equalsIgnoreCase("jfif") || extension.equalsIgnoreCase("png")
                    || extension.equalsIgnoreCase("svg")) {
                DrawAsIconImage(message, messages.indexOf(message));
            } else {
                DrawAsFile(message);
            }
        }
        messages.add(message);

    }

    public static void DrawAsIconImage(Message msg, int msgNumber) {
        // Get Name, Colour, Data, Date, Filename
        // Image Message
        ImageIcon image = new ImageIcon(msg.getData());
        int messageIndex = messages.indexOf(msg);
        // Call SceneManager to draw this

        DateTime dateTime = message.date;
        DateTimeZone timeZone = DateTimeZone.forID("Africa/Cairo");
        DateTime newDateTime = dateTime.withZone(timeZone);

        int hour = newDateTime.getHourOfDay();
        int minute = newDateTime.getMinuteOfHour();
        String time = Integer.toString(hour) + ":" + Integer.toString(minute);

        SceneManager.addImageMessage(msg.name, image, msg.getFileName(), time);
    }

    public static void DrawAsFile(Message msg) {
        // Get Name, Colour, Data, Date, Filename
        // File Messa
        String filename = msg.getFileName();
        int messageIndex = messages.indexOf(msg);
        // Call SceneManager to draw this
        DateTime dateTime = message.date;
        DateTimeZone timeZone = DateTimeZone.forID("Africa/Cairo");
        DateTime newDateTime = dateTime.withZone(timeZone);

        int hour = newDateTime.getHourOfDay();
        int minute = newDateTime.getMinuteOfHour();
        String time = Integer.toString(hour) + ":" + Integer.toString(minute);
        SceneManager.addFileMessage(msg.name, filename, msg.getSize(), time);
    }

    public static Message getFileMessage(String filename) {
        for (Message message : messages) {
            if (message.getFileName().equals(filename)) {
                return message;
            }
        }
        return null;
    }

    public static void DownloadFile(Message msg) {
        FileOutputStream fileOutputStream = null;
        try {

            String path = "src\\Data\\" + msg.getFileName();
            fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(msg.getData());

        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Couldn't write");
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException ex) {
                System.out.println("Couldn't close file");
            }
        }
    }
}

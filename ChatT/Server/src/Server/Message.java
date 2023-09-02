package Server;

import java.nio.charset.StandardCharsets;
import org.joda.time.DateTime;

public class Message {

    // Sent Using Pocket Crocodile Protocol
    public static enum types {

        FL,
        TX
    }
    private int size; // Size in bytes

    public String name;

    public DateTime date;    //2023-05-15T14:30:18.644+03:00

    public types type;

    private String fileName;

    private byte[] data;

    public Message(String name, DateTime date, types type) {
        this.size = 0;
        this.name = name;
        this.date = date;
        this.type = type.TX;
        this.fileName = "";

    }

    public Message(String name, DateTime date, types tp, String fileName) {
        this.size = 0;
        this.name = name;
        this.date = date;
        this.type = tp;
        this.fileName = fileName;

    }

    public Message(String Line) {
        String[] msgInfo = Line.split(",");
        parseMessageInfo(msgInfo);
    }

    public Message(String[] lines) {
        parseMessageInfo(lines);
    }

    public void setData(byte[] cache) {
        this.data = cache;
        this.size = this.data.length;
    }

    public void setDataAsString(String s) {
        this.data = s.getBytes(StandardCharsets.UTF_8);
        this.type = types.TX;
        this.size = this.data.length;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getDataAsString() {
        if (!(this.type == types.TX)) {
            System.out.println("The Data in Message is not Textual");
            return "";
        }
        if (this.data.length < 1) {
            System.out.println("There is no message");
            return "";
        }
        String text = new String(this.data, StandardCharsets.UTF_8);
        return text;
    }

    public boolean changeMessageInfo(int size, String name, DateTime date, types type, String filename) {
        this.size = size;
        this.name = name;
        this.date = date;
        this.type = type;
        this.fileName = filename;
        return true;
    }

    public String getExtension() {
        if (this.type == types.TX) {
            System.out.println("This is a text not a file");
            return "";
        }
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    public int getSize() {
        return this.size;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String name) {
        this.fileName = name + this.getExtension();
    }

    public void setFileNameAndExtension(String fn) {
        this.fileName = fn;
    }

    public String getInfo() {
        String s = (this.size + "," + this.name
                + "," + this.date.toString() + "," + this.type + "," + this.fileName);
        return s;
    }

    public void printInfo() {
        System.out.println("Message Info:");
        System.out.println(this.size + ",   " + this.name
                + ",   " + this.date.toString() + ",   " + this.type + ",   " + this.fileName);

    }

    private void parseMessageInfo(String[] line) {
        this.size = Integer.parseInt(line[0]);
        this.name = line[1];

        DateTime timestamp = DateTime.parse(line[2]);

        this.date = timestamp;
        this.type = types.valueOf(line[3]);
        if (line.length < 5) {
            this.fileName = "";
            return;
        }
        this.fileName = line[4];
    }

}

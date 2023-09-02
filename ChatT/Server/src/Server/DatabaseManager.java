package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

public class DatabaseManager {

    public static String port = "1433";
    public static String dbn = "ChatT"; // DataBase Name
    public static String user = "name";
    public static String pass = "pass";
    public static Connection con;
    public static Statement stmt;

    
    public static void init() {
        DatabaseManager.connectToDB();
    }
    public static void init(String prt, String DBName, String username, String password) {
        DatabaseManager.port = prt;
        DatabaseManager.dbn = DBName;
        DatabaseManager.user = username;
        DatabaseManager.pass = password;
        DatabaseManager.connectToDB();
    }

    public static void configureConnection(String prt, String DBName, String username, String password) {
        DatabaseManager.port = prt;
        DatabaseManager.dbn = DBName;
        DatabaseManager.user = username;
        DatabaseManager.pass = password;
    }
    
    public static boolean connectToDB() {
        String url = "jdbc:sqlserver://localhost:" + DatabaseManager.port + ";databaseName=" + DatabaseManager.dbn;

        try {
            con = DriverManager.getConnection(url, DatabaseManager.user, DatabaseManager.pass);
            System.out.println("Connected to database");
            return true;
        } catch (SQLException e) {
            System.out.println("Connection to database error: " + e.getMessage());
            return false;
        }
    }

    public static void insertToDB(String username, String date, String type, String filename, String text) {
        String statement = "INSERT INTO Messages (username, date, type, filename, text) VALUES (" + "'" + username + "'" + ", "
                + "'" + date + "'" + "," + "'" + type + "'" + ", " + "'" + filename + "'" + ", " + "'" + text + "');";
        DatabaseManager.executeNonquery(statement);
    }

    public static void getFromDB(String name) {

        try {
            ResultSet rs = executequery("select * from Messages");// add indpendent date in the db

            while (rs.next()) {
                String username_value = rs.getString("username");
                String Time_value = rs.getString("date");
                String type_value = rs.getString("type");
                String file_name_value = rs.getString("filename");
                String text_value = rs.getString("text");

                if ("TX".equals(type_value) || "tx".equals(type_value)) {
                    Message msg = new Message(username_value, DateTime.parse(Time_value), Message.types.valueOf(type_value), file_name_value);
                    msg.setDataAsString(text_value);
                    ClientHandler.SendClient(name, msg);
                } else {
                    try {
                        Message msg = new Message(username_value, DateTime.parse(Time_value), Message.types.valueOf(type_value), file_name_value);
                        File file = new File("src\\Data\\" + msg.getFileName());
                        byte[] fileContentBytes = new byte[(int) file.length()];
                        FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                        fileInputStream.read(fileContentBytes);
                        msg.setData(fileContentBytes);
                        ClientHandler.SendClient(name, msg);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        } catch (SQLException ex) {
            System.out.println("SQLException");
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public static boolean executeNonquery(String sqlStatement) { //to update, delete, insert
        try {
            stmt = con.createStatement();
            stmt.execute(sqlStatement);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    public static ResultSet executequery(String sqlStatement) {

        try {
            stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlStatement);
            System.out.println("QUERY EXCUTED SUCCEFULLY");
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Failed to excute query");
        return null;

    }

    public static void Close() { // to close the connection of SQL
        try {
            con.close();
        } catch (SQLException ex) {
            System.out.println("ERROR Connection");
        }
    }
}

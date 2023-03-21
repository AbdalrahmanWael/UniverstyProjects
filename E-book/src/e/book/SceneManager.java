package e.book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

abstract public class SceneManager {

    // Keep usefull data that is usefull between Scenes
    // Pass Data between Scenes
    // Change Scenes
    // Make one connection to the Database across all the scenes

    public static String port = "1433";
    public static String dbn = "E_Book"; // DataBase Name
    public static String user = "Golden";
    public static String pass = "123";
    public static String[] tables = {"Author", "Publisher", "Phone_Publisher", "Book", "Book_Author"};
    public static Connection con;
    public static Statement stmt;
    private static String[] cache;
    protected static HashMap<String, String> loginInfo = new HashMap<String, String>();

    public static HomeScreen HS = new HomeScreen();
    public static PublisherScreen PS = new PublisherScreen();
    public static InquiryScreen MS = new InquiryScreen();

    public static enum Screens {

        HOME,
        INQUIRY,
        PUBLISHER
    }

    private static Screens curScreen;

    public static void init() {
        curScreen = Screens.HOME;
        HS.setVisible(true);
        loginInfo.put("12200301", "01060657895");// A.Wael
        loginInfo.put("123456789", "01067131329");// Z.Awdallah
        loginInfo.put("123", "123"); // For testing
    }

    public static HashMap<String, String> getLoginInfo() {
        return loginInfo;
    }

    public static boolean verifyLogin(String userId, String pass) {
        if (loginInfo.containsKey(userId)) {
            if (loginInfo.get(userId).equals(pass)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static void drawColumns(String[] columns, DefaultTableModel model, JTable table)// ToDo: Fix
    {
        model = new DefaultTableModel();
        for (String column : columns) {
            model.addColumn(column);
        }
        table.setModel(model);
    }
    
    public static void changeScreen(Screens newScreen) {
        // Dispose current scene
        if (curScreen.equals(Screens.HOME))//ToDo: Not dissapearing
        {
            // Set home Screen invisible and/or dispose screen
            HS.setVisible(false);
            HS.dispose();
        }
        if (curScreen.equals(Screens.INQUIRY)) {
            // Set INQUIRY Screen invisible and/or dispose screen
            MS.setVisible(false);
        }
        if (curScreen.equals(Screens.PUBLISHER)) {
            // Set PUBLISHER Screen invisible and/or dispose screen
            PS.setVisible(false);
        }
        // Activate new scene
        if (newScreen.equals(Screens.HOME)) {
            // Set home Screen visible and activate 
            HS.setVisible(true);
            // maybe need to pass one data
        }
        if (newScreen.equals(Screens.INQUIRY)) {
            // Set home Screen visible and activate 
            MS.setVisible(true);
            // maybe need to pass one data
        }
        if (newScreen.equals(Screens.PUBLISHER)) {
            // Set home Screen visible and activate 
            PS.setVisible(true);
            // maybe need to pass one data
        }
        curScreen = newScreen;
    }

    public static void configureConnection(String prt, String DBName, String username, String password) {
        SceneManager.port = prt;
        SceneManager.dbn = DBName;
        SceneManager.user = username;
        SceneManager.pass = password;
    }

    public static boolean connectToDB() {
        String url = "jdbc:sqlserver://localhost:" + SceneManager.port + ";databaseName=" + SceneManager.dbn;

        try {
            con = DriverManager.getConnection(url, SceneManager.user, SceneManager.pass);
            System.out.println("Connected");
            return true;
        } catch (SQLException e) {
            System.out.println("Connection error");
            return false;
        }
    }

    public static boolean executeNonquary(String sqlStatement) { //to update, delete, insert
        try {
            //ConnectToSQL();
            stmt = con.createStatement();
            stmt.execute(sqlStatement);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    public static void Close() { // to close the connection of SQL
        try {
            con.close();
        } catch (SQLException ex) {
            System.out.println("ERROR Connection");
        }
    }

    public static void passData(String[] data) {
        cache = data;
    }

    public static String[] getCache() {
        return cache;
    }

    public static Screens getScreen() {
        return curScreen;
    }

    public static void main(String[] args) {
        SceneManager.connectToDB();
        SceneManager.init();
        //SceneManager.changeScreen(Screens.INQUIRY);
        //Expermintal exp = new Expermintal();
    }
}

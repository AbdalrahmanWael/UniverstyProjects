package chatt;

import form.ChatScreen;
import form.LoginScreen;
import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.Icon;
import operation.Operation;
import org.joda.time.DateTime;

abstract public class SceneManager {

    // Keep usefull data that is usefull between Scenes
    // Pass Data between Scenes
    // Change Scenes
    // Handle events between gui classes
    // Change and manipulate the GUI 
    // Handle Connections
    ArrayList<int[]> HashedUserName = new ArrayList<int[]>(3);
    private static Color[] colors = {new Color(255, 0, 0), new Color(0, 0, 255)};

    public static LoginScreen LoginS = new LoginScreen();
    public static ChatScreen ChatS;

    public static Socket socket;
    public static String host = "localhost";
    public static int port = 1234;
    public static String name = "Default";
    public static Client client;
    public static Operation p;

    public static enum Screens {

        LOGIN,
        CHAT,
    }

    private static Screens curScreen;

    public static void init(String host, int port, String rmiAdress, String rmiPort) {
        SceneManager.host = host;
        SceneManager.port = port;
        // RMI Connection
        try {
            SceneManager.p = (Operation) Naming.lookup("rmi://" + rmiAdress + ":" + rmiPort + "/db");
        } catch (NotBoundException ex) {
            System.out.println("Couldn't bind");
        } catch (MalformedURLException ex) {
            System.out.println("malformed url exception");
        } catch (RemoteException ex) {
            System.out.println("Remote exception");
        }

        curScreen = Screens.LOGIN;
        LoginS.setVisible(true);
    }

    public static void OpenConnection(String h, int p) {
        try {
            //localhost, 1234
            socket = new Socket(h, p);
            client = new Client(socket, name);

            client.readMessages();
        } catch (IOException ex) {
            System.out.println("Couldn't Open Socket");
        }
    }

    public static void sendToDb(Message message) {
        String nm = message.name;
        String tm = message.date.toString();
        String tp = message.type.toString();
        String fn = message.getFileName();

        try {
            if (tp == Message.types.TX.toString()) {
                p.insertToDB(nm, tm, tp, fn, message.getDataAsString());
            } else {
                p.insertToDB(nm, tm, tp, fn, "");
            }

        } catch (RemoteException ex) {
            System.out.println("Failed to insert to DB");
        }
    }

    public static void getFromDB() {
        try {
            p.getFromDB(SceneManager.name);
        } catch (RemoteException ex) {
            System.out.println("Failed here");
            ex.printStackTrace();
        }
    }

    public static void SendMessage(Message msg) {
        client.sendMessage(msg);
        SceneManager.sendToDb(msg);
    }

    public static void addTextMessage(String text, String user, String time) {
        int[] colors = generateColor(user);
        Color color = new Color(colors[0], colors[1], colors[2]);
        ChatS.chat.chatBody.addItemLeft(text, user, "", time, color);
    }

    public static void addImageMessage(String user, Icon image, String filename, String time) {
        int[] colors = generateColor(user);
        Color color = new Color(colors[0], colors[1], colors[2]);
        ChatS.chat.chatBody.addItemLeft("", user, filename, time, color, image);
    }

    public static void addFileMessage(String user, String filename, int size, String time) {
        int[] colors = generateColor(user);
        Color color = new Color(colors[0], colors[1], colors[2]);
        ChatS.chat.chatBody.addItemFile("", user, filename, size, time, color);
    }

    public static int[] generateColor(String text) {
        int hash = text.hashCode();
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = (hash >> (i * 8)) & 0xFF;
        }

        float[] hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], null);
        if (hsb[2] < 0.5) {
            hsb[2] = 0.5f;
            rgb = new int[] { (int)(hsb[0] * 255), (int)(hsb[1] * 255), (int)(hsb[2] * 255) };
        }

        return rgb;
    }

    public static int[] generateColor2(String text) {
        int hash = text.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        return new int[]{r, g, b};
    }

    public static void changeScreen(Screens newScreen) {
        // Dispose current scene
        if (curScreen.equals(Screens.LOGIN)) {
            LoginS.setVisible(false);
            LoginS.dispose();
        }
        if (curScreen.equals(Screens.CHAT)) {
            ChatS.setVisible(false);
            ChatS.dispose();

            LoginS = new LoginScreen();
        }
        // Activate new scene
        if (newScreen.equals(Screens.LOGIN)) {
            LoginS.setVisible(true);
            SceneManager.CloseConnection();
        }
        if (newScreen.equals(Screens.CHAT)) {
            SceneManager.OpenConnection(host, port);
            ChatS = new ChatScreen();
            ChatS.setVisible(true);
            ConnectedMessage();
        }
        curScreen = newScreen;
    }

    public static void CloseConnection() {
        try {
            // Close Connection and delete all data in Chat Screen
            socket.close();
        } catch (IOException ex) {
            System.out.println("Couldn't close");
        }
    }

    private static void ConnectedMessage() {
        Message msg = new Message("0,HOLDER,0,TX");
        msg.name = name;
        msg.date = new DateTime();
        msg.setDataAsString("JUST CONNECTED");
        SceneManager.SendMessage(msg);
    }

    public static Screens getScreen() {
        return curScreen;
    }

    public static void main(String[] args) {
        SceneManager.init("localhost", 1234, "localhost", "4321");
    }
}

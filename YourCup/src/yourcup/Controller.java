package yourcup;

import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import static yourcup.SearchScreen.playlistModel;
import java.io.IOException;
import java.io.PrintWriter;

abstract public class Controller {
    // ToDo: checks if input is valid by searching if url is real

    public static final String[] columns = {"Title", "Type", "Player", "Team"};

    private static int CurrentIndex = 0;
    public static DoublyVideoLinkedList dvll;

    public static HomeScreen HS;
    public static SearchScreen SS;
    public static ModifyScreen MS;

    public enum Type {

        Goal, Attempt, AmazingBall,
        RedCard, YellowCard, Foul,
        VarCheck, Penality
    }

    public static enum Screens {

        HOME,
        SEARCH,
        Modify
    }

    private static Screens curScreen;

    public static boolean Init() {
        dvll = new DoublyVideoLinkedList();
        Serialization.S_INIT();
        dvll = Serialization.GetData();

        HS = new HomeScreen();
        SS = new SearchScreen();
        MS = new ModifyScreen();

        curScreen = Screens.HOME;
        HS.setVisible(true);

        return false;
    }

    public static void AddToEnd(String loc, String name, Controller.Type tp, String playerName, String teamName) {
        dvll.insertLast(loc, name, tp, playerName, teamName);
    }

    public void RemoveFromStart() {
        dvll.removeFirst();
    }

    public VideoNode getVideo(int i) {
        return dvll.searchByIndex(i);
    }

    public static void changeScreen(Screens newScreen) {
        // Dispose current scene
        if (curScreen.equals(Screens.HOME)) {
            HS.setVisible(false);
            HS.dispose();
        }
        if (curScreen.equals(Screens.SEARCH)) {
            SS.setVisible(false);
        }
        if (curScreen.equals(Screens.Modify)) {
            MS.setVisible(false);
        }
        // Activate new scene
        if (newScreen.equals(Screens.HOME)) {
            HS.setVisible(true);
            HomeScreen.draw();
        }
        if (newScreen.equals(Screens.SEARCH)) {
            SS.setVisible(true);
        }
        if (newScreen.equals(Screens.Modify)) {
            MS.setVisible(true);
        }
        curScreen = newScreen;
    }

    public static void draw(String[] columns, DefaultTableModel model, JTable table, ArrayList<String> al) {
        model = new DefaultTableModel();

        int counter = 0;
        String url = "";
        String title = "";
        Controller.Type type = Controller.Type.YellowCard;
        String player = "";
        String team = "";

        for (String column : columns) {
            model.addColumn(column);
        }
        table.setModel(model);
        for (String data : al) {
            Scanner scanner = new Scanner(data);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (counter == 0) {
                    url = line;
                }
                if (counter == 1) {
                    title = line;
                }
                if (counter == 2) {
                    type = Controller.Type.valueOf(line);
                }
                if (counter == 3) {
                    player = line;
                }
                if (counter == 4) {
                    team = line;
                }
                counter++;
                if (counter == 5) {
                    model.addRow(new Object[]{title, type, player, team});
                    counter = 0;
                }
            }
            scanner.close();
        }
    }

    public static void drawVN(String[] columns, DefaultTableModel model, JTable table, ArrayList<VideoNode> al) {
        model = new DefaultTableModel();

        for (String column : columns) {
            model.addColumn(column);
        }
        table.setModel(model);
        for (VideoNode vn : al) {

            model.addRow(new Object[]{vn.title, vn.type, vn.player, vn.team});
        }

    }

    public static void drawColumns(String[] columns, DefaultTableModel model, JTable table) {
        model = new DefaultTableModel();

        int counter = 0;
        String url = "";
        String title = "";
        Controller.Type type = Controller.Type.YellowCard;
        String player = "";
        String team = "";

        for (String column : columns) {
            model.addColumn(column);
        }
        table.setModel(model);
    }

    public static void drawSingle(String[] columns, DefaultTableModel model, JTable table, VideoNode vn) {
        model = new DefaultTableModel();

        String title = "";
        Controller.Type type = Controller.Type.YellowCard;
        String player = "";
        String team = "";

        for (String column : columns) {
            model.addColumn(column);
        }
        table.setModel(model);

        title = vn.title;
        type = vn.type;
        player = vn.player;
        team = vn.team;
        model.addRow(new Object[]{title, type, player, team});
    }

    public static void EXCUTECMDCOMMAND(String Video) {
        String[] command = {
            "python",
            "play.py",
            "assets/" + Video,};
        Process p;
        try {

            p = Runtime.getRuntime().exec(command);
            new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
            PrintWriter stdin = new PrintWriter(p.getOutputStream());
            stdin.println("hostname");
            stdin.close();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DoublyVideoLinkedList dvlll = Serialization.GetData();
        dvlll.displayAll();
        System.out.println(dvlll.getLength() - 1);
        Controller.Init();

    }
}

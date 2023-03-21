package yourcup;
public class VideoNode implements Cloneable{

    public VideoNode next;
    public VideoNode prev;
    public String url;
    public String title;
    public Controller.Type type;
    public String player;
    public String team;

    public VideoNode(VideoNode n, VideoNode p, String loc, String name, Controller.Type tp, String playerName, String teamName) {
        next = n;
        prev = p;
        url = loc;
        title = name;
        type = tp;
        player = playerName;
        team = teamName;
    }
    public VideoNode(String loc, String name, Controller.Type tp, String playerName, String teamName) {
        next = null;
        prev = null;
        url = loc;
        title = name;
        type = tp;
        player = playerName;
        team = teamName;
    }

    public boolean update(String name, Controller.Type tp, String playerName, String teamName) {
        title = name;
        type = tp;
        player = playerName;
        team = teamName;
        return true;
    }

    public String toString() {
        return ("Name: " + title + " Type: " + type + " Player:" + player + " Team:" + team);
    }
    @Override
    public VideoNode clone() {
        try {
            VideoNode clone = (VideoNode) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

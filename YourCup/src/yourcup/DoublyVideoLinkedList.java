package yourcup;

import java.util.ArrayList;

public class DoublyVideoLinkedList {
    /*
     Assumptions
     Urls are unique and do not repeat
     Each video's url is not gonna change
     */

    public VideoNode head = null;
    public VideoNode tail = null;
    public int length = 0;

    DoublyVideoLinkedList() {
    }

    DoublyVideoLinkedList(String loc, String name, Controller.Type tp, String playerName, String teamName) {
        insertFirst(loc, name, tp, playerName, teamName);
    }

    public VideoNode insertFirst(String loc, String name, Controller.Type tp, String playerName, String teamName) {
        VideoNode t = new VideoNode(null, null, loc, name, tp, playerName, teamName);
        length++;
        if (isEmpty()) {
            head = t;
            tail = t;
            return t;
        } else {
            head.prev = t;
            t.next = head;
            head = t;
            return t;
        }

    }

    public VideoNode insertFirst(VideoNode vn) {
        length++;
        if (isEmpty()) {
            head = vn;
            tail = vn;
            return vn;
        } else {
            head.prev = vn;
            vn.next = head;
            head = vn;
            return vn;
        }
    }

    public VideoNode insertLast(String loc, String name, Controller.Type tp, String playerName, String teamName) {
        VideoNode t = new VideoNode(null, null, loc, name, tp, playerName, teamName);        
        if (isEmpty()) {
            insertFirst(loc, name, tp, playerName, teamName);
            return t;
        }
        else {
            length++;
            tail.next = t;
            t.prev = tail;
            tail = t;
            return t;
        }

    }

    public VideoNode insertAtUrl(String url, VideoNode vn) {
        VideoNode current = head;
        if (current.url.equals(url)) {
            insertFirst(vn);
            return current;
        }
        while (current != null) {
            if (current.url.equals(url)) {
                current.prev.next = vn;
                vn.next = current;
                current.prev = vn;
                length++;
                return current;
            }
            current = current.next;
        }
        return current;
    }
    // 1 x 3 4 5 6 

    public VideoNode replaceAtUrl(String url, VideoNode vn) {
        VideoNode current = head;
        if (head.url.equals(url)) {
            head.next.prev = vn;
            vn.prev = null;
            vn.next = head.next;
            head = vn;
            return head;
        }
        if (tail.url.equals(url)) {
            tail.prev.next = vn;
            vn.prev = tail.prev;
            vn.next = null;
            tail = vn;
            return current;
        }
        while (current != null) {
            if (current.url.equals(url)) {
                current.prev.next = vn;
                current.next.prev = vn;
                vn.prev = current.prev;
                vn.next = current.next;

                return current;
            }
            current = current.next;
        }

        return null;
    }

    public VideoNode removeFirst() {
        VideoNode t;
        if (isEmpty()) {
            return null;
        } else {
            length--;
            t = head;
            head.next.prev = null;
            head = head.next;
            return t;
        }
    }

    public VideoNode removeLast() {
        VideoNode t;
        if (isEmpty()) {
            return null;
        } else {
            length--;
            t = tail;
            tail.prev.next = null;
            tail = tail.prev;
            return t;
        }
    }

    public VideoNode removeAtURL(String url) {
        VideoNode current = head;
        if (current.url.equals(url)) {
            removeFirst();
            return current;
        }
        if (tail.url.equals(url)) {
            removeLast();
            return tail;
        }
        while (current != null) {
            if (current.url.equals(url)) {
                current.prev.next = current.next;
                current.next.prev = current.prev;
                length--;
                return current;
            }
            current = current.next;
        }
        return current;
    }

    public VideoNode searchByURL(String url) {
        VideoNode current = head;
        while (current != null) {
            if (current.url.equals(url)) {
                return current;
            }
            current = current.next;
        }
        return current;
    }

    public VideoNode searchByTitle(String title) {
        VideoNode current = head;
        while (current != null) {
            if (current.title.equals(title)) {
                return current;
            }
            current = current.next;
        }
        return current;
    }

    public ArrayList<VideoNode> searchByTeam(String team) {
        ArrayList<VideoNode> arr = new ArrayList<VideoNode>();
        VideoNode current = head;
        while (current != null) {
            if (current.team.equals(team)) {
                arr.add(current);
            }
            current = current.next;
        }
        return arr;
    }

    public ArrayList<VideoNode> searchByPlayer(String player) {
        ArrayList<VideoNode> arr = new ArrayList<VideoNode>();
        VideoNode current = head;
        while (current != null) {
            if (current.player.equals(player)) {
                arr.add(current);
            }
            current = current.next;
        }
        return arr;
    }

    public ArrayList<VideoNode> searchByAction(Controller.Type type) {
        ArrayList<VideoNode> arr = new ArrayList<VideoNode>();
        VideoNode current = head;
        while (current != null) {
            if (current.type.equals(type)) {
                arr.add(current);
            }
            current = current.next;
        }
        return arr;
    }

    public VideoNode searchByIndex(int x) {
        VideoNode current = head;
        int i = 0;
        while (current != null && i < x) {
            current = current.next;
            i++;
        }
        return current;
    }

    public void swap(String urlA, String urlB) {
        if (head == null || head.next == null || urlA == urlB) {
            return;
        }

        VideoNode A = searchByURL(urlA);
        VideoNode B = searchByURL(urlB);

        if(A == head)
            head = B;
        else if(B == head)
            head = A;
        if (A == tail)
            tail = B;
        else if (B == tail)
            tail = A;

        VideoNode temp;
        temp = A.next;
        A.next = B.next;
        B.next = temp;

        if (A.next != null){
            A.next.prev = A;
        }
        if (B.next != null){
            B.next.prev = B;
        }

        temp = A.prev;
        A.prev = B.prev;
        B.prev = temp;

        if (A.prev != null){
            A.prev.next = A;
        }
        if (B.prev != null){
            B.prev.next = B;
        }
    }

    public int getLength() {
        return length;
    }

    public boolean isEmpty() {
        return (head == null);
    }

    public String getFirst() {
        return head.toString();
    }

    public String getLast() {
        return tail.toString();
    }

    public void displayAll() {
        System.out.print("[ \n");
        VideoNode current = head;
        while (current != null) {
            System.out.print(current + "\n ");
            current = current.next;
        }
        System.out.println("]");
    }
    
    public ArrayList<String> ReturnAsStringAL() {
        ArrayList<String> arr = new ArrayList<String>();
        String temp = "";
        VideoNode current = head;
        while (current != null) {
            temp = current.url + "\n" + current.title + "\n"  + current.type + "\n"
                    + current.player + "\n" + current.team + "\n";
            arr.add(temp);
            current = current.next;
        }
        return arr;
    }
}

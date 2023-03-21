public class DoublyLinkedList2 {

    public Node head = null;
    public Node tail = null;
    public int length = 0;

    DoublyLinkedList2() {
    }

    DoublyLinkedList2(Object x) {
        insertFirst(x);
    }

    public Node insertFirst(Object x) {
        Node t = new Node(null, null, x);
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

    public Node insertFirst(Node vn) {
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

    public Node insertLast(String loc, String name, Object x) {
        Node t = new Node(null, null, x);
        if (isEmpty()) {
            insertFirst(x);
            return t;
        } else {
            length++;
            tail.next = t;
            t.prev = tail;
            tail = t;
            return t;
        }

    }


    public Node removeFirst() {
        Node t;
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

    public Node removeLast() {
        Node t;
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

//    public Node delete (Comparable key) {
//        Node current;
//        Node previous;
//        current = head;
//        while (current.value.compareTo (key) != 0) {
//            if (current.next == null)
//                return null;
//            else {
//                previous = current;
//                current = current.next;
//            } }
//        if (current == head)
//            head = head.next;
//        else
//            previous.next = current.next;
//        return current;
//    }

//    public VideoNode removeAtURL(String url) {
//        VideoNode current = head;
//        if (current.url.equals(url)) {
//            removeFirst();
//            return current;
//        }
//        if (tail.url.equals(url)) {
//            removeLast();
//            return tail;
//        }
//        while (current != null) {
//            if (current.url.equals(url)) {
//                current.prev.next = current.next;
//                current.next.prev = current.prev;
//                length--;
//                return current;
//            }
//            current = current.next;
//        }
//        return current;
//    }


    public void swap(String urlA, String urlB) {
//            if (head == null || head.next == null || urlA == urlB) {
//                return;
//            }
//
//            Node A = searchByURL(urlA);
//            Node B = searchByURL(urlB);
//
//            if(A == head)
//                head = B;
//            else if(B == head)
//                head = A;
//            if (A == tail)
//                tail = B;
//            else if (B == tail)
//                tail = A;
//
//            Node temp;
//            temp = A.next;
//            A.next = B.next;
//            B.next = temp;
//
//            if (A.next != null){
//                A.next.prev = A;
//            }
//            if (B.next != null){
//                B.next.prev = B;
//            }
//
//            temp = A.prev;
//            A.prev = B.prev;
//            B.prev = temp;
//
//            if (A.prev != null){
//                A.prev.next = A;
//            }
//            if (B.prev != null){
//                B.prev.next = B;
//            }
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

    public Object getLast2() {
        Node current = head;
        while (current.next != null)
            current = current.next;
        return current.value;
    }

    public void displayAll() {
        System.out.print("[ \n");
        Node current = head;
        while (current != null) {
            System.out.print(current + "\n ");
            current = current.next;
        }
        System.out.println("]");
    }


}



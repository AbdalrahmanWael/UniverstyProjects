public class Node {
    public Node next;
    public Node prev;
    public Object value;

    public Node(Node n, Node p, Object x) {
        next = n;
        prev = p;
        value = x;
    }
}

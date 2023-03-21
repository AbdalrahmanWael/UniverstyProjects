public class ArrayQueue {
    private int maxSize;
    private int[] queArray;
    private int front = 0;
    private int rear = -1;
    private int nItems = 0;

    ArrayQueue(int s){
        maxSize = s;
        queArray = new int[s];

    }

    public void enqueue(int i){
        if (isFull()) {
            System.out.println("Queue is full");
        } else {
            queArray[++rear % maxSize] = i;
            nItems++;
        }
    }
    public int dequeue() {
        int result;
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return (-1);
        }
        else { /* Q has only one element, so we reset the queue after deleting it. */
            result = queArray[front];
            if (front == rear) {
                front = 0;
                rear = -1;
            }
            else {
                front = (front + 1) % maxSize;
            }
            nItems--;
            return (result);
        }
    }

    public int peek(){
        return queArray[front];
    }
    public boolean isEmpty(){
        return (nItems == 0);
    }

    public boolean isFull() {
        return (nItems == maxSize);
    }

    public int size() {
        return nItems;
    }

    void display() {
        /* Function to display status of Circular Queue */
        int i;
        if (isEmpty()) {
            System.out.println("Empty Queue");
        } else {
            System.out.println("Front -> " + front);
            System.out.println("Items -> ");
            for (i = front; i != rear; i = (i + 1) % maxSize)
                System.out.print(queArray[i] + " ");
            System.out.println(queArray[i]);
            System.out.println("Rear -> " + rear);
        }
    }
}

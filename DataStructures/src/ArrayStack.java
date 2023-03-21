import java.util.ArrayList;

public class ArrayStack {

    private Object[] stackArray;
    private int top;
    private int maxSize;

    public ArrayStack(int s){
        maxSize = s;
        stackArray = new Object[maxSize];
        top = -1;
    }
    public void push(Object x)
    {
        if(!isFull())
        {
            top++;
            stackArray[top] = x;
        }

    }
    public Object pop(){
        if(!isEmpty())
        {
            Object result = stackArray[top];
            top--;
            return result;
        }
        return -1;
    }
    public Object top(){
        return stackArray[top];
    }
    public boolean isEmpty()
    {
        return (top == -1);
    }
    public boolean isFull()
    {
        return (top == maxSize - 1);
    }
}

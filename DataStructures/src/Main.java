import java.util.Scanner;

public class Main
{
    void away()
    {
        /*
        System.out.println("Why are we here");//sout tab

        Scanner scanner = new Scanner(System.in);

        String name = scanner.nextLine();
        System.out.println("Yes " + name);
        int age;
        System.out.println("How old are you? ");
        age = scanner.nextInt();
        scanner.nextLine();
        System.out.println("You like pizza?");
        String hm = scanner.nextLine();
        System.out.println("So you suffered for " + age + " years, cool!");
        System.out.println(hm);

        double fr = 10;
        fr = (double)fr/3;
        */
        /*
        String name = JOptionPane.showInputDialog("Enter your name:");
        JOptionPane.showMessageDialog(null, "Hello " + name);

        int age = Integer.parseInt(JOptionPane.showInputDialog("Enter your age:"));
        JOptionPane.showMessageDialog(null, "You are " + age + " years old!");

        double height = Double.parseDouble(JOptionPane.showInputDialog("Enter your height:"));
        JOptionPane.showMessageDialog(null, "You are " + height + " cms");
        */
        /*
        double x = 3.14;
        double y = -10;
        double z = Math.max(x, y);
        z = Math.abs(y);
        z = Math.sqrt(y);
        z = Math.round(y);
        z = Math.ceil(y);
        System.out.println(z);

        double a,b,c;

        Scanner scanner = new Scanner(System.in);

        System.out.println("enter side a: ");
        a = scanner.nextDouble();

        System.out.println("enter side b: ");
        b = scanner.nextDouble();

        c = Math.sqrt(a*a+b*b);
        System.out.println("the hypotense is " + c);
        */
        /*
        Random random = new Random();

        int x = random.nextInt(1,7);

        System.out.println(x);

        String day = "Friday";

        switch (day)
        {
            case "Friday":
                System.out.println("it's friday");
                break;
            case "wednesday":
                System.out.println("it's wednesday my dudes");
                break;
            default:
                System.out.println("aha");

        }
        Scanner scanner = new Scanner(System.in);
        String response = scanner.next();

        if(response.equals("Q") || response.equals("q"))
            System.out.println("ahah");


        String[] cars = {"Tesla", "Toyota", "hundai"};
        String[] carss = new String[3];
        String[][] carsss = new String[3][3];

        System.out.println(cars[0]);

        String sentence = "man wtf";
        sentence.equals("man wtf");
        sentence.equalsIgnoreCase("man wtf");
        sentence.length();
        char res = sentence.charAt(0);
        sentence.indexOf("w");
        sentence.isEmpty();
        sentence.toUpperCase();
        sentence.toLowerCase();
        sentence.trim(); // remove spaces
        sentence.replace('f', 'h');
         */
        /*

        // Wrapper class: provides a way to use primative  data types as reference data types
        // boolean Boolearn char Character int Integer double Double
        // autoboxing: the automatic conversion that the java compiler makes between the primiative types and thier object wrapper classes
        // unboxing: the reverse, auto convertion from wrapper to primitive

        Boolean a = true;
        Character b = '@';
        Integer c = 123;
        Double d = 3.14;

        if(a==true)
            System.out.println('t');

        ArrayList<String> food = new ArrayList<>();
        food.add("pizza");
        food.set(0,"sushi");
        food.size();
        food.remove(2);
        food.clear();

        ArrayList<ArrayList<String>> list2d = new ArrayList<>();
        list2d.get(0).get(0);

        String[] animals = {"c","b","A"};

        for (String i : animals) {
            System.out.println(i);
        }
        */
        /*
        hello();

        static void hello()
        {
            System.out.println("hello");
        }

        // overload methods

        // printf  % [flags] [precision] [width] [conversion-character]

        System.out.printf("this a format string %d %b %c %s ", 123,true,'c',"sdada");

        // width min num of char

        System.out.printf("hello %-10s", "ASDADA");

        //prec
        System.out.printf("hello %,.2f", 515112.545484848);


        final double PI = 3.1415943244;

        */




//        Car myCar = new Car();
//        System.out.println(myCar.model);
//
//        Human human = new Human("Bob", 50, 190);
//        human.eat();
//
//        System.out.println(myCar.toString());

        // static is one copy owned by the class itself
        System.out.println("cool");
    }
    public boolean checkparen(String word)
    {
        int n = word.length();
        ArrayStack stack = new ArrayStack(n);

        for (int i = 0; i < n;i++)
        {
            char ch = word.charAt(i);

            switch (ch){
                case '(' : case '[':
                    stack.push(ch);
                    break;
                case ')' : case ']':
                    if(stack.isEmpty()) return false;

                    char ch0 = (char) stack.pop();
                    if(ch == ')' && ch0 != '(') return false;
                    if(ch == ']' && ch0 != '[') return false;
                    break;
                default:
                    break;
            }
        }
        return stack.isEmpty();
    }
    public void test1()
    {
        //        int[] SortedArray = {-999999,-1000,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,50,200,1000,50000};
//        System.out.println(binarySearch1(1000, SortedArray));
//        System.out.println(binarySearch2(SortedArray, -1000, 0, SortedArray.length - 1));
//
//        int[] UnSortedArray = {75, 24, 36, -83, 62, 69, -96, 9, 40, 4, -30, 87, -94, 20, -80, 3, 44, -64, 79, -24};
//        SortingAlgorithms.bubbleSortWithStop(UnSortedArray);
//        SortingAlgorithms.printlst(UnSortedArray);

//        ArrayQueue aq = new ArrayQueue(5);
//        aq.enqueue(1);
//        aq.enqueue(2);
//        aq.enqueue(3);
//        System.out.println(aq.dequeue());
//        System.out.println(aq.dequeue());
//        System.out.println(aq.dequeue());
//
//        aq.enqueue(4);
//        aq.enqueue(5);
//        System.out.println(aq.dequeue());
//        System.out.println(aq.dequeue());
//        aq.display();
//
//        aq.enqueue(6);
//        aq.enqueue(7);
//        System.out.println(aq.dequeue());
//        System.out.println(aq.dequeue());
        //        int[] a = {1,8,9,8,44,488,4,8484,12,5,7,7,1,21,5151,51,85418,99,25,12,8,90000,0,-15};
//        SortingAlgorithms sort = new SortingAlgorithms();
//
//        sort.bubbleSortWithStop(a);
//        sort.printlst(a);
//
//        System.out.println("hmmm");
//
//        int x ;
//        String z;
//        Scanner scanner = new Scanner(System.in);
//        x = scanner.nextInt();
//        z = scanner.nextLine();

        ArrayStack theStack = new ArrayStack(10);
        theStack.push(2);

//        while (! theStack.isEmpty())
//        {
//            int value = theStack.pop();
//
//            System.out.print(value);
//            System.out.print(" ");
//        }
//
//        if (! theStack.isFull()) {
//            theStack.push(1);
//        }
//        else
//            System.out.println("Can’t insert, stack is full");
//
//
//    }



//        System.out.println((name.compareTo(name2)));
    }
    public boolean binarysearchnonrec(int[] arr, int val)
    {
        int left,right,mid;
        left = 0;
        right = arr.length-1;

        while(left <= right)
        {
            mid = (right - left) /2 + left;
            if(arr[mid] == val)
            {
                return true;
            }
            if (arr[mid] > val)
            {
                right = mid - 1;
            }
            else {
                left = mid + 1;
            }
        }
        return false;
    }
    public static int binarySearch1(long searchKey, int[] a) throws Exception {
        int len = a.length;
        int lowerbound = 0;
        int upperbound = len - 1;
        int curr;

        while(true){
            curr = (upperbound + lowerbound) / 2;
            if (a[curr] == searchKey){
                return a[curr];
            }
            if (lowerbound > upperbound){
                throw new Exception("Doesn't exist");
            }
            if (a[curr] < searchKey){
                lowerbound = curr + 1;
            }
            else{
                upperbound = curr - 1;
            }
        }
    }
    public static int binarySearch2(int[] arr, int val, int start, int end)
    {
        if(start > end){
            return - 1;
        }
        else{
            int cur = start + (end - start)/2;
            if (arr[cur] > val){
                return binarySearch2(arr, val, start, cur - 1);
            }
            else{
                if (arr[cur] < val){
                    return binarySearch2(arr, val, cur + 1, end);
                }
                else{
                    return arr[cur];
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

    }
}
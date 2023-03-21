import static java.util.Collections.swap;

public class SortingAlgorithms {

    public static void selectionSort(int[] a)
    {
        for(int i = 0; i < a.length; i++){
            int minIndex = i;
            int min = a[i];

            for(int j = i + 1; j < a.length; j++){
                if(a[j] < min){
                    minIndex = j;
                    min = a[j];
                }
            }
            int T = a[minIndex];
            a[minIndex] = a[i];
            a[i] = T;
        }
    }


    public static void insertionSort(int[] a)
    {
        for(int i = 1; i < a.length; i++){
            int value = a[i];
            int j;
            for(j = i-1; j >= 0 && a[j] > value; j--){
                a[j+1] = a[j];
            }
            a[j+1] = value;
        }
    }

    public void insertionSort2(int[] a)
    {
        for (int i = 1; i < a.length;i++)
        {
            int value = a[i];
            int j = (i-1);

            while (j >= 0 && a[j] > value)
            {
                a[j+1] = a[j];
                j--;
            }

            a[j+ 1] = value;
        }
    }

    public void bubbleSortBruteForce(int[] a)
    {
        for (int i = 0; i < a.length-1 ; i++)
        {
            for (int j = 1; j < a.length; j++)
            {
                if(a[j-1] > a[j])
                {
                    int temp = a[j];
                    a[j] = a[j-1];
                    a[j-1] = temp;

                }
            }
        }
    }



    public static void bubbleSort(int[] a)
    {
        for(int i = 0; i < a.length; i++ ){

            for (int j = 1; j < a.length - i; j++){
                // the largest value is in the correct place after the first, the 2nd largest after the 3rd pass and so on, hence the a.length - i
                if(a[j - 1] > a[j]){
                    int t = a[j];
                    a[j] = a[j - 1];
                    a[j - 1] = t;
                }
            }
        }
    }
    public static void bubbleSortWithStop(int[] a)
    {
        boolean cont = true;
        while (cont){
            cont = false;
            for(int j = 0; j < a.length - 1; j++){
                if(a[j + 1] < a[j]){
                    int t = a[j];
                    a[j] = a[j+1];
                    a[j+1] = t;
                    cont = true;
                }
            }
        }
    }

    public static void printlst(int[] a)
    {
        for (int j : a) {
            System.out.print(j + " ");
        }
        System.out.println();
    }
}

package yourcup;

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

import java.io.FileWriter;   // Import the FileWriter class
import java.util.ArrayList;

abstract public class Serialization {

    private static String Cache;
    private static String DataLocation = "Data\\data.txt";

    public static void S_INIT() {
        createFile();
    }

    public static DoublyVideoLinkedList GetData() {
        // Will read and make Video Node one by one and add the all to a Doubly Linked List
        // Calls Read and puts it in cache
        //Cache = READ();
        DoublyVideoLinkedList dvll = new DoublyVideoLinkedList();
        int counter = 0;
        String url = "";
        String title = "";
        Controller.Type type = Controller.Type.YellowCard;
        String player = "";
        String team = "";
        
        try {
            File myObj = new File(DataLocation);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {  
                
                String data = myReader.nextLine();   
                if (counter == 0){
                    url = data;
                }
                if (counter == 1){
                    title = data;
                }
                if (counter == 2){
                    type = Controller.Type.valueOf(data);
                }
                if (counter == 3){
                    player = data;
                }
                if (counter == 4){
                    team = data;
                }
                counter++;
                if(counter == 5){
                    dvll.insertLast(url, title, type, player, team);
                    counter = 0;
                }
                
            }            
            myReader.close();
            return dvll;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public static boolean PutData(ArrayList<String> al) {
        // Will read from the linked list and take each node and puts it in Cache
        // then puts it using Write 
        // return true;
        try {
            FileWriter myWriter = new FileWriter(DataLocation);
            
            for (String data: al){
                myWriter.write(data);
            }
            
            myWriter.close();
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return false;
    }

    public static void writeToAFile(String cache) {// overrides everything
        try {
            FileWriter myWriter = new FileWriter(DataLocation);
            myWriter.write(cache + "\n");
            myWriter.write("next line");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public static void createFile() {
        try {
            File myFile = new File(DataLocation);
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void createFile(String filename) {
        try {
            File myFile = new File("Data\\" + filename + ".txt");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void readFile() {
        try {
            File myObj = new File(DataLocation);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

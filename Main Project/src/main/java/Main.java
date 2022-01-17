import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static final String CAT_SEL = "cat";
    public static final String STU_SEL = "book";
    public static void main(String[] args) {

        System.out.println(
                """
                        Welcome to Gabe's mystical funny book catalog thingy
                        if you have any concerns with this program, then uh. ask me in English ig
                        important thing to note, there are multiple parts in this program that check for files
                        if the files aren't found, it's going to throw a hissy fit and create them
                        THIS WILL ERASE YOUR DATABASE, SO DON'T DELETE/MOVE THE FILES


                        """
        );
        System.out.printf("Type '%s' for access to the catalog or '%s' to loan/return books\nSelection: ",CAT_SEL,STU_SEL);
        Scanner selection = new Scanner(System.in);
        String select = selection.nextLine();


        switch (select) {
            case "cat" -> database();
            case "book" -> checkoutin();
        }
        System.out.println("Follow the directions next time, exiting");
        System.exit(1);


    }


    public static void database() {
        //Checks for the file and creates it if it can't find it
        try {
            FileReader database = new FileReader("database.txt");
        }
        catch (IOException e) {
            try {
                File databasefile = new File("database.txt");
                if(databasefile.createNewFile()) {
                    System.out.println("Created Database");
                }
            }
            catch (IOException f) {
                System.out.println("You royally fucked up idk what happened");
            }
        }
    }



    public static void checkoutin() {
        //Checks for the file and creates it if it can't find it
        try {
            FileReader bookcheckout = new FileReader("checkout.txt");
        }
        catch (IOException e) {
            try {
                File checkoutfile = new File("checkout.txt");
                if(checkoutfile.createNewFile()) {
                    System.out.println("Created Database");
                }
            }
            catch (IOException f) {
                System.out.println("You royally fucked up idk what happened");
            }
        }
    }
}

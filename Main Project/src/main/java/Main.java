import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.sql.*;

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
        System.out.printf("Type '%s' for access to the catalog or '%s' to loan/return books or 'exit' to exit\nSelection: ",CAT_SEL,STU_SEL);
        Scanner selection = new Scanner(System.in);
        String select = selection.nextLine();


        if (select.equals(CAT_SEL))
            database();
        else if(select.equals(STU_SEL))
            checkoutin();
        else if(select.equals("exit"))
            System.exit(0);
        System.out.println("invalid choice, restarting, remember it's case sensitive");
        try{
            Thread.sleep(1500);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String[] egg = new String[0];
        main(egg);


    }


    public static void database() {
        //Checks for the file and creates it if it can't find it
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:books.db");


            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS BOOKS " +
                    "(NAME           TEXT, " +
                    "ISBN            TEXT)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": a" + e.getMessage() );
            System.exit(0);
        }


        System.out.println("Welcome to the book catalog, type 'gen' to generate a list of books\n" +
                "type 'add' to add a book, 'rem' to remove one or 'check' to check the database 'exit' to exit");
        System.out.print("Selection: ");
        Scanner select = new Scanner(System.in);
        String selection = select.nextLine();

        switch (selection) {
            case "gen":
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
                LocalDateTime now = LocalDateTime.now();

                File report = new File("book_"+dtf.format(now)+".txt");
                try {
                    if (report.createNewFile()) {
                        System.out.println("Report Created "+report.getName());
                    }
                    FileWriter myWriter = new FileWriter(report.getName());
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:books.db");
                    c.setAutoCommit(false);
                    stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery( "SELECT * FROM BOOKS;" );
                    myWriter.write("Books in the Catalog\n");
                    while ( rs.next() ) {
                        myWriter.write("Book Name: "+rs.getString("NAME")+"\nISBN: "+rs.getString("ISBN")+"\n\n\n");
                    }
                    myWriter.close();
                    rs.close();
                    stmt.close();
                    c.close();
                }
                catch (Exception e) {
                    System.out.println("An Error Occurred");
                }
                break;
            case "add":
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:books.db");
                    c.setAutoCommit(false);
                    System.out.print("Whats the name of the book: ");
                    Scanner name = new Scanner(System.in);
                    String bname = name.nextLine();
                    System.out.print("Whats the ISBN of the book: ");
                    Scanner isbn = new Scanner(System.in);
                    String bisbn = name.nextLine();
                    stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT 1 FROM BOOKS WHERE NAME = '["+bname+"]' AND ISBN = '["+bisbn+"]'");
                    if (!rs.next()) {
                        String sql = "INSERT INTO BOOKS (NAME,ISBN) VALUES ('["+bname+"]', '["+bisbn+"]' )";
                        stmt.executeUpdate(sql);
                        System.out.printf("Successfully added %s of ISBN %s to database\nPress enter to continue\n",bname,bisbn);
                        String bla = select.nextLine();
                    }
                    rs.close();
                    stmt.close();
                    c.commit();
                    c.close();

                    /* Britt
                        ResultSet rs = c.executeQuery(SELECT 1 FROM BOOKS WHERE NAME = '["+bname+"]')
                        if (!rs.next()) {
                        // insert
                        }
                     */
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }

                break;
            case "rem":
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:books.db");
                    c.setAutoCommit(false);
                    System.out.print("Whats the name of the book you wish to remove: ");
                    Scanner name = new Scanner(System.in);
                    String bname = name.nextLine();
                    stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT 1 FROM BOOKS WHERE NAME = '["+bname+"]'");
                    if (rs.next()){
                        String sql = "DELETE from BOOKS where NAME='["+bname+"]'";
                        stmt.executeUpdate(sql);
                        System.out.printf("Successfully deleted book %s from the database\nPress enter to continue\n",bname);
                        String bla = select.nextLine();
                    }
                    stmt.close();
                    rs.close();
                    c.commit();
                    c.close();

                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }
                break;
            case "check":
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:books.db");
                    c.setAutoCommit(false);
                    System.out.print("Whats the name of the book you wish to find: ");
                    Scanner name = new Scanner(System.in);
                    String bname = name.nextLine();
                    stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT NAME,ISBN FROM BOOKS WHERE NAME = '["+bname+"]'");
                    while (rs.next()) {
                        String rname = rs.getString("NAME");
                        String risbn = rs.getString("ISBN");

                        System.out.printf("Book Exists in the database:\nName:%s\nISBN:%s\nPress enter to continue",rname,risbn);
                        String bla = select.nextLine();
                    }
                    stmt.close();
                    rs.close();
                    c.commit();
                    c.close();
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }
                break;
            case "exit":
                System.exit(0);
                break;
        }
        database();
    }




















    public static void checkoutin() {
        //Checks for the file and creates it if it can't find it

        checkoutin();

    }
}

package academy.metis.javabasics.lesson15.activity3.exercise1.part1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TitlesPage {
    private final Scanner scanner;
    private final InputValidator inputValidator;
    public static final List<Book> books = new ArrayList<>();
    public static final List<DVD> dvds = new ArrayList<>();
    public static String BOOKS_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/titlesBook.txt";
    public static String DVDS_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/titlesDVD.txt";

    public TitlesPage(Scanner scanner) {
        this.scanner = scanner;
        this.inputValidator = new InputValidator(scanner);
        loadTitles();
    }

    public static void loadTitlesFromFile(String filePath, String type) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    System.out.println("Invalid line (less than 6 parts): " + line + " | Parts length: " + parts.length);
                    continue;
                }
                try {
                    if (type.equals("Book")) {
                        Book book = new Book(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Long.parseLong(parts[5]), type);
                        books.add(book);
                    } else if (type.equals("DVD")) {
                        DVD dvd = new DVD(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Long.parseLong(parts[5]), type);
                        dvds.add(dvd);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Number format exception for line: " + line + " | Parts: " + Arrays.toString(parts));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("Error loading titles from file: " + filePath);
            throw e;
        }
    }

    public void loadTitles() {
        try {
            loadTitlesFromFile(BOOKS_FILE_PATH, "Book");
            loadTitlesFromFile(DVDS_FILE_PATH, "DVD");
        } catch (IOException e) {
            System.out.println("Error loading titles: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void showTitlesMenu() {
        while (true) {
            System.out.println("\nTitles\n1 - Show All Titles\n2 - Add Title\n3 - Remove Title\n4 - Back\nChoose an option:");
            int choice = inputValidator.getValidatedChoice(4);
            switch (choice) {
                case 1:
                    showAllTitles();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Please enter a number in the range from 1 to 4");
            }
        }
    }

    public void showAllTitles() {
        listingAllTitles();
        System.out.println("Press enter to return to the Titles menu...");
        scanner.nextLine();
    }

    public void listingAllTitles() {
        System.out.println("All Titles:");

        System.out.println("Books:");
        if (books.isEmpty()) {
            System.out.println("No books available");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }

        System.out.println("\nDVDs:");
        if (dvds.isEmpty()) {
            System.out.println("No DVDs available");
        } else {
            for (DVD dvd : dvds) {
                System.out.println(dvd);
            }
        }

        System.out.println("\nTotal number of all titles: " + (books.size() + dvds.size()));
    }
}

package academy.metis.javabasics.lesson23.activity3.exercise1.part1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TitlesPage {
    private final Scanner scanner;
    private final InputValidator inputValidator;
    private final IdGenerator idTitlesGenerator;

    public static final List<Book> books = new ArrayList<>();
    public static final List<DVD> dvds = new ArrayList<>();
    public static String BOOKS_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/titlesBook.txt";
    public static String DVDS_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/titlesDVD.txt";
    public static String TITLES_ID_COUNTER_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/IdTitlesCounter.txt";

    private static boolean titlesLoaded = false;

    public TitlesPage(Scanner scanner) {
        this.scanner = scanner;
        this.inputValidator = new InputValidator(scanner);
        this.idTitlesGenerator = new IdGenerator(TITLES_ID_COUNTER_FILE_PATH);
        if (!titlesLoaded) {
            loadTitles();
            titlesLoaded = true;
        }
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
                    System.out.println("Number format exception for line: " + line + " | Parts: " + parts.toString());
                }
            }
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
                    addTitleMenu();
                    break;
                case 3:
                    handleTitleDeletionProcess();
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

    public void addTitleMenu() {
        while (true) {
            System.out.println("\nAdd Title\n1 - Add Book\n2 - Add DVD\n3 - Back\nChoose an option:");
            int choice = inputValidator.getValidatedChoice(3);
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    addDVD();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Please enter a number in the range from 1 to 3");
            }
        }
    }

    public void addBook() {
        System.out.println("Enter Author's name:");
        String authorName = inputValidator.getValidatedString();
        System.out.println("Enter title name:");
        String titleName = inputValidator.getValidatedString();
        System.out.println("Enter available copies:");
        int availableCopies = inputValidator.getValidatedInt();
        System.out.println("Enter ISBN:");
        String isbn = inputValidator.getValidatedISBN();
        System.out.println("Enter number of pages:");
        int pageCount = inputValidator.getValidatedInt();
        long titleId = idTitlesGenerator.generateID();

        Book book = new Book(titleName, authorName, isbn, pageCount, availableCopies, titleId, "Book");
        boolean success = saveTitle(book);
        if (success) {
            System.out.println("Book added successfully...");
        } else {
            System.out.println("Failed to add the book.");
        }
        System.out.println("Press enter to continue...");
        scanner.nextLine();
    }

    public void addDVD() {
        System.out.println("Enter Author's name:");
        String authorName = inputValidator.getValidatedString();
        System.out.println("Enter title name:");
        String titleName = inputValidator.getValidatedString();
        System.out.println("Enter available copies:");
        int availableCopies = inputValidator.getValidatedInt();
        System.out.println("Enter Length (minutes):");
        int durationInMinutes = inputValidator.getValidatedInt();
        System.out.println("Enter number of chapters:");
        int numberOfChapters = inputValidator.getValidatedInt();
        long titleId = idTitlesGenerator.generateID();

        DVD dvd = new DVD(titleName, authorName, numberOfChapters, durationInMinutes, availableCopies, titleId, "DVD");
        boolean success = saveTitle(dvd);
        if (success) {
            System.out.println("DVD added successfully...");
        } else {
            System.out.println("Failed to add the DVD.");
        }
        System.out.println("Press enter to continue...");
        scanner.nextLine();
    }

    public boolean saveTitle(Object title) {
        boolean success = false;
        if (title instanceof Book) {
            Book book = (Book) title;
            books.add(book);
            String formattedRecord = book.getTitle() + "," +
                    book.getAuthorName() + "," +
                    book.getIsbn() + "," +
                    book.getPageCount() + "," +
                    book.getAvailableCopies() + "," +
                    book.getTitleId();
            success = saveToFile(BOOKS_FILE_PATH, formattedRecord);
        } else if (title instanceof DVD) {
            DVD dvd = (DVD) title;
            dvds.add(dvd);
            String formattedRecord = dvd.getTitle() + "," +
                    dvd.getAuthorName() + "," +
                    dvd.getNumberOfTracks() + "," +
                    dvd.getDurationInMinutes() + "," +
                    dvd.getAvailableCopies() + "," +
                    dvd.getTitleId();
            success = saveToFile(DVDS_FILE_PATH, formattedRecord);
        }
        return success;
    }

    private boolean saveToFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(System.lineSeparator() + content);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filePath);
            return false;
        }
    }

    public void handleTitleDeletionProcess() {
        if (books.isEmpty() && dvds.isEmpty()) {
            System.out.println("No titles to remove. Press enter to return to titles...");
            scanner.nextLine();
            return;
        }

        System.out.println("Remove Title Page");
        listingAllTitles();

        System.out.println("Select a title to delete: ");
        Object title = inputValidator.validationChecksTitlesId(books, dvds);

        boolean success;
        if (title instanceof Book) {
            success = deleteBookTitle((Book) title);
        } else {
            success = deleteDvdTitle((DVD) title);
        }

        if (success) {
            System.out.println("Title removed successfully!");
        } else {
            System.out.println("Title not removed.");
        }
        System.out.println("Press enter to continue...");
        scanner.nextLine();
    }

    public boolean deleteBookTitle(Book title) {
        boolean removed = books.remove(title);
        if (removed) {
            updateFile(BOOKS_FILE_PATH, books);
        }
        return removed;
    }

    public boolean deleteDvdTitle(DVD title) {
        boolean removed = dvds.remove(title);
        if (removed) {
            updateFile(DVDS_FILE_PATH, dvds);
        }
        return removed;
    }

    private void updateFile(String filePath, List<?> titles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            boolean firstLine = true;
            for (Object title : titles) {
                if (title == null) {
                    continue;
                }
                String formattedRecord = "";
                if (title instanceof Book) {
                    Book book = (Book) title;
                    formattedRecord = book.getTitle() + "," +
                            book.getAuthorName() + "," +
                            book.getIsbn() + "," +
                            book.getPageCount() + "," +
                            book.getAvailableCopies() + "," +
                            book.getTitleId();
                } else if (title instanceof DVD) {
                    DVD dvd = (DVD) title;
                    formattedRecord = dvd.getTitle() + "," +
                            dvd.getAuthorName() + "," +
                            dvd.getNumberOfTracks() + "," +
                            dvd.getDurationInMinutes() + "," +
                            dvd.getAvailableCopies() + "," +
                            dvd.getTitleId();
                }
                if (!formattedRecord.trim().isEmpty()) {
                    if (firstLine) {
                        writer.write(formattedRecord);
                        firstLine = false;
                    } else {
                        writer.newLine();
                        writer.write(formattedRecord);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating file: " + filePath);
        }
    }
    public List<Book> getBooks() {
        return books;
    }

    public List<DVD> getDvds() {
        return dvds;
    }
}

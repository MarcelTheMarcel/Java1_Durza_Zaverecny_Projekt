package academy.metis.javabasics.lesson22.activity3.exercise1.part1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;

public class RentalsPage {
    private final Scanner scanner;
    private final InputValidator inputValidator;
    private static final Map<Long, RentalRecord> rentalRecords = new HashMap<>();
    public static final String RENTALS_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/rentalRecords.txt";

    public RentalsPage(Scanner scanner) {
        this.scanner = scanner;
        this.inputValidator = new InputValidator(scanner);
        loadRentalRecordsFromFile(RENTALS_FILE_PATH);
    }

    public void showRentalsMenu() {
        while (true) {
            System.out.println("\nRentals page\n1 - Rent a title\n2 - Return a title\n3 - Prolong the rental\n4 - Show all rentals\n5 - Show rentals past due\n6 - Show Queue\n7 - Back\nChoose an option:");
            int choice = inputValidator.getValidatedChoice(7);
            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    showAllRentals();
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Please enter a number in the range from 1 to 7");
            }
        }
    }

    public static void loadRentalRecordsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) {
                    System.out.println("Invalid line (less than 7 parts): " + line);
                    continue;
                }
                try {
                    RentalRecord record = new RentalRecord(
                            Long.parseLong(parts[6]),
                            Long.parseLong(parts[0]),
                            LocalDate.parse(parts[1]),
                            LocalDate.parse(parts[2]),
                            Long.parseLong(parts[3]),
                            Integer.parseInt(parts[4]),
                            Boolean.parseBoolean(parts[5])
                    );
                    rentalRecords.put(record.getRentalRecordId(), record);
                } catch (NumberFormatException e) {
                    System.out.println("Number format exception for line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading rental records from file: " + filePath);
        }
    }

    public void showAllRentals() {
        listingAllRentals();
        System.out.println("Press enter to return to Rentals page...");
        scanner.nextLine();
    }

    public void listingAllRentals() {
        System.out.println("All rentals:");
        if (rentalRecords.isEmpty()) {
            System.out.println("No rentals available");
        } else {
            for (RentalRecord record : rentalRecords.values()) {
                System.out.println(record);
            }
            System.out.println("Total number of all rentals: " + rentalRecords.size());
        }
    }

    public Map<Long, RentalRecord> getRentalRecords() {
        return rentalRecords;
    }
}

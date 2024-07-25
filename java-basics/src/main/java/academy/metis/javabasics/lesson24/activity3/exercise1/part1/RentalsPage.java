package academy.metis.javabasics.lesson24.activity3.exercise1.part1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RentalsPage {
    private final Scanner scanner;
    private final InputValidator inputValidator;
    private final RentalValidator rentalValidator;
    private final IdGenerator idGenerator;
    private final TitlesPage titlesPage;
    private final MembersPage membersPage;
    private static final Map<Long, RentalRecord> rentalRecords = new HashMap<>();
    public static final String RENTALS_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/rentalRecords.txt";
    public static String RENTALS_ID_COUNTER_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/IdRentalsCounter.txt";

    public RentalsPage(Scanner scanner, TitlesPage titlesPage, MembersPage membersPage) {
        this.scanner = scanner;
        this.inputValidator = new InputValidator(scanner);
        this.rentalValidator = new RentalValidator();
        this.idGenerator = new IdGenerator(RENTALS_ID_COUNTER_FILE_PATH);
        this.titlesPage = titlesPage;
        this.membersPage = membersPage;
        loadRentalRecordsFromFile(RENTALS_FILE_PATH);
    }

    public void showRentalsMenu() {
        while (true) {
            System.out.println("\nRentals page\n1 - Rent a title\n2 - Return a title\n3 - Prolong the rental\n4 - Show all rentals\n5 - Show rentals past due\n6 - Show Queue\n7 - Back\nChoose an option:");
            int choice = inputValidator.getValidatedChoice(7);
            switch (choice) {
                case 1:
                    rentTitle();
                    break;
                case 2:
                    returnTitle();
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
                    System.out.println("Prosím zadajte číslo v rozsahu od 1 do 7");
            }
        }
    }

    public void rentTitle() {
        System.out.println("Rent title");
        System.out.println("Choose a member to rent a title:");
        membersPage.printAllMembers();
        System.out.println("Choose an option:");
        Member chosenMember = inputValidator.validationChecksUniqueId(membersPage.getMembers());
        System.out.println("Selected member: " + chosenMember.getMemberId());

        titlesPage.listingAllTitles();
        System.out.println("Choose a title to rent to:");
        Object chosenTitle = inputValidator.validationChecksTitlesId(titlesPage.getBooks(), titlesPage.getDvds());

        if (rentalValidator.isPossibleToRentTitle(chosenMember.getMemberId(), chosenTitle, rentalRecords)) {
            processRental(chosenMember, chosenTitle);
        }

        System.out.println("Press enter to continue...");
        scanner.nextLine();
    }

    public void processRental(Member chosenMember, Object chosenTitle) {
        LocalDate rentedDate = LocalDate.now();
        LocalDate returnDate = rentedDate.plusDays(RentalRecord.MAXIMUM_RENT_DURATION_IN_DAYS);
        long rentalRecordId = idGenerator.generateID();
        long titleId = 0;

        if (chosenTitle instanceof Book) {
            titleId = ((Book) chosenTitle).getTitleId();
            ((Book) chosenTitle).setAvailableCopies(((Book) chosenTitle).getAvailableCopies() - 1);
        } else if (chosenTitle instanceof DVD) {
            titleId = ((DVD) chosenTitle).getTitleId();
            ((DVD) chosenTitle).setAvailableCopies(((DVD) chosenTitle).getAvailableCopies() - 1);
        }

        RentalRecord rentalRecord = new RentalRecord(rentalRecordId, chosenMember.getMemberId(), rentedDate, returnDate, titleId, 0, false);

        if (saveRentalToTheFile(rentalRecord)) {
            rentalRecords.put(rentalRecordId, rentalRecord);
            System.out.println("Title rented successfully");
        } else {
            System.out.println("Writing to the file failed...Title was not rented");
        }
    }


    public boolean saveRentalToTheFile(RentalRecord rentalRecord) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RENTALS_FILE_PATH, true))) {
            String recordString = rentalRecord.getMemberId() + "," +
                    rentalRecord.getRentedDate() + "," +
                    rentalRecord.getReturnDate() + "," +
                    rentalRecord.getTitleId() + "," +
                    rentalRecord.getTimesProlonged() + "," +
                    rentalRecord.isReturned() + "," +
                    rentalRecord.getRentalRecordId();
            writer.write(System.lineSeparator() + recordString);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return false;
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

    public void updateAvailableCopiesInRentedTitle(Object title, boolean incrementTrueDecrementFalse) {
        if (title instanceof Book) {
            Book book = (Book) title;
            if (incrementTrueDecrementFalse) {
                book.setAvailableCopies(book.getAvailableCopies() + 1);
            } else {
                book.setAvailableCopies(book.getAvailableCopies() - 1);
            }
            titlesPage.updateTitles(book);
        } else if (title instanceof DVD) {
            DVD dvd = (DVD) title;
            if (incrementTrueDecrementFalse) {
                dvd.setAvailableCopies(dvd.getAvailableCopies() + 1);
            } else {
                dvd.setAvailableCopies(dvd.getAvailableCopies() - 1);
            }
            titlesPage.updateTitles(dvd);
        }
    }

    public void returnTitle() {
        System.out.println("Return Title Page");
        System.out.println("Choose a member, who wants to return a title:");
        membersPage.printAllMembers();
        System.out.println("Choose an option:");

        Member chosenMember = inputValidator.validationChecksUniqueId(membersPage.getMembers());
        System.out.println("Selected member: " + chosenMember.getMemberId());

        Map<Long, RentalRecord> filteredMap = returnTitlesRentedBySpecificMember(chosenMember);
        if (filteredMap.isEmpty()) {
            System.out.println("This member has no titles to return.");
            System.out.println("Press enter to return to Members page...");
            scanner.nextLine();
            return;
        }

        showAllTitlesRentedBySpecificMember(filteredMap, chosenMember);
    }

    public Object getSpecificTitleToReturn(RentalRecord rentalRecord) {
        long titleId = rentalRecord.getTitleId();
        for (Book book : titlesPage.getBooks()) {
            if (book.getTitleId() == titleId) {
                return book;
            }
        }
        for (DVD dvd : titlesPage.getDvds()) {
            if (dvd.getTitleId() == titleId) {
                return dvd;
            }
        }
        return null;
    }

    public boolean updateRentalRecord(RentalRecord rentalRecordToUpdate) {
        rentalRecordToUpdate.setReturned(true);
        return saveRentalToTheFile(rentalRecordToUpdate);
    }

    public Map<Long, RentalRecord> returnTitlesRentedBySpecificMember(Member member) {
        Map<Long, RentalRecord> filteredMap = new HashMap<>();
        for (Map.Entry<Long, RentalRecord> entry : rentalRecords.entrySet()) {
            RentalRecord record = entry.getValue();
            if (record.getMemberId() == member.getMemberId() && !record.isReturned()) {
                filteredMap.put(entry.getKey(), record);
            }
        }
        return filteredMap;
    }

    public void showAllTitlesRentedBySpecificMember(Map<Long, RentalRecord> filteredMap, Member member) {
        System.out.println("Select a title to return:");
        int index = 1;
        Map<Integer, Long> indexToRentalRecordIdMap = new HashMap<>();
        for (Map.Entry<Long, RentalRecord> entry : filteredMap.entrySet()) {
            RentalRecord record = entry.getValue();
            Object title = getSpecificTitleToReturn(record);
            String titleName = (title instanceof Book) ? ((Book) title).getTitle() : ((DVD) title).getTitle();
            String authorName = (title instanceof Book) ? ((Book) title).getAuthorName() : ((DVD) title).getAuthorName();
            System.out.println(index + ". | " + titleName + " | " + authorName + " | Rented on: " +
                    record.getRentedDate() + " | Rented by: " + member.getFirstName() + " " + member.getSurname() + " | Returned: " +
                    (record.isReturned() ? "RETURNED" : "NOT RETURNED") + " | Times Prolonged: " + record.getTimesProlonged());
            indexToRentalRecordIdMap.put(index, entry.getKey());
            index++;
        }

        System.out.println("Choose an option:");
        int choice = inputValidator.getValidatedChoice(indexToRentalRecordIdMap.size());

        long rentalRecordId = indexToRentalRecordIdMap.get(choice);
        RentalRecord rentalRecord = filteredMap.get(rentalRecordId);
        if (rentalRecord == null) {
            System.out.println("Invalid option selected.");
            return;
        }

        Object titleToReturn = getSpecificTitleToReturn(rentalRecord);
        if (updateRentalRecord(rentalRecord)) {
            updateAvailableCopiesInRentedTitle(titleToReturn, true);
            System.out.println("Title returned successfully");
        } else {
            System.out.println("Failed to update rental record.");
            System.out.println("Title was not returned");
        }

        System.out.println("Press enter to return to Rental page...");
        scanner.nextLine();
    }
}

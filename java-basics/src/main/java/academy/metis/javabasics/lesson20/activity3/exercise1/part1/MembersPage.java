package academy.metis.javabasics.lesson20.activity3.exercise1.part1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MembersPage {
    private final Scanner scanner;
    private final InputValidator inputValidator;
    private final IdGenerator idMembersGenerator;
    private static final Map<Long, Member> members = new HashMap<>();
    public static String MEMBERS_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/members.txt";
    public static String MEMBERS_ID_COUNTER_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/IdMembersCounter.txt";

    public MembersPage(Scanner scanner) {
        this.scanner = scanner;
        this.inputValidator = new InputValidator(scanner);
        this.idMembersGenerator = new IdGenerator(MEMBERS_ID_COUNTER_FILE_PATH);
        loadMembersFromFile(MEMBERS_FILE_PATH);
    }

    public void addMember() {
        System.out.println("Enter member's first name:");
        String firstName = inputValidator.getValidatedString();
        System.out.println("Enter member's last name:");
        String surname = inputValidator.getValidatedString();
        System.out.println("Enter member's date of birth (dd.MM.yyyy):");
        String dateOfBirth = inputValidator.validateDateOfBirthInput();

        long memberId = idMembersGenerator.generateID();

        Member member = new Member(firstName, surname, dateOfBirth, memberId);
        boolean success = saveMember(member);
        if (success) {
            System.out.println("Member created successfully.");
        } else {
            System.out.println("Failed to add the member.");
        }
        System.out.println("Press enter to return to Members page...");
        scanner.nextLine();
    }

    public boolean saveMember(Member member) {
        members.put(member.getMemberId(), member);
        String formattedRecord = member.getFirstName() + "," + member.getSurname() + "," + member.getDateOfBirth() + "," + member.getMemberId();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBERS_FILE_PATH, true))) {
            writer.write(System.lineSeparator() + formattedRecord);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to file: " + MEMBERS_FILE_PATH);
            return false;
        }
    }

    public static void loadMembersFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    System.out.println("Invalid line (less than 4 parts): " + line);
                    continue;
                }
                try {
                    Member member = new Member(parts[0], parts[1], parts[2], Long.parseLong(parts[3]));
                    members.put(member.getMemberId(), member);
                } catch (NumberFormatException e) {
                    System.out.println("Number format exception for line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading members from file: " + filePath);
        }
    }

    public Map<Long, Member> getMembers() {
        return members;
    }

    public void showMembersMenu() {
        while (true) {
            System.out.println("\nMembers\n1 - Show All Members\n2 - Add Member\n3 - Remove Member\n4 - Back\nChoose an option:");
            int choice = inputValidator.getValidatedChoice(4);
            switch (choice) {
                case 1:
                    showAllMembers();
                    break;
                case 2:
                    addMember();
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

    public void showAllMembers() {
        printAllMembers();
        System.out.println("Press enter to return to Members page...");
        scanner.nextLine();
    }

    public void printAllMembers() {
        System.out.println("All members:");
        Map<Long, Member> membersMap = getMembers();
        if (membersMap.isEmpty()) {
            System.out.println("No members available");
        } else {
            for (Member member : membersMap.values()) {
                System.out.println(member);
            }
            System.out.println("Total number of all members: " + membersMap.size());
        }
    }
}

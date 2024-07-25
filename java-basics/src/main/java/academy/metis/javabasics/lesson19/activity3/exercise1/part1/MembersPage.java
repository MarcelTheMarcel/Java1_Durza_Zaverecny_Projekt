package academy.metis.javabasics.lesson19.activity3.exercise1.part1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MembersPage {
    private final Scanner scanner;
    private final InputValidator inputValidator;
    private static final Map<Long, Member> members = new HashMap<>();
    public static String MEMBERS_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/members.txt";

    public MembersPage(Scanner scanner) {
        this.scanner = scanner;
        this.inputValidator = new InputValidator(scanner);
        loadMembersFromFile(MEMBERS_FILE_PATH);
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
        if (members.isEmpty()) {
            System.out.println("No members available");
        } else {
            for (Member member : members.values()) {
                System.out.println(member);
            }
            System.out.println("Total number of all members: " + members.size());
        }
    }
}

package academy.metis.javabasics.lesson17.activity3.exercise1.part1;

import java.util.Scanner;

public class InputValidator {
    private final Scanner scanner;

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getValidatedChoice(int maxOption) {
        int choice = -1;
        while (choice < 1 || choice > maxOption) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > maxOption) {
                    System.out.println("Please enter a number in the range from 1 to " + maxOption);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid value.");
            }
        }
        return choice;
    }

    public String getValidatedString() {
        while (true) {
            String input = scanner.nextLine();
            if (!input.trim().isEmpty()) {
                return input;
            } else {
                System.out.println("Please enter a valid value.");
            }
        }
    }

    public int getValidatedInt() {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= 0) {
                    return input;
                } else {
                    System.out.println("The number cannot be negative. Please enter a valid number:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number:");
            }
        }
    }

    public String getValidatedISBN() {
        while (true) {
            String input = scanner.nextLine();
            try {
                Long.parseLong(input);
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid value.");
            }
        }
    }

    public long getValidatedLong() {
        while (true) {
            try {
                long input = Long.parseLong(scanner.nextLine());
                if (input >= 0) {
                    return input;
                } else {
                    System.out.println("The number cannot be negative. Please enter a valid number:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number:");
            }
        }
    }
}
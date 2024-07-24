package academy.metis.javabasics.lesson15.activity3.exercise1.part1;

import java.util.Scanner;
import java.util.NoSuchElementException;
public class InputValidator {
    private final Scanner scanner;

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getValidatedChoice(int maxOption) {
        while (true) {
            if (!scanner.hasNextLine()) {
                throw new NoSuchElementException("No line found");
            }
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= maxOption) {
                    return choice;
                } else {
                    System.out.println("Please enter a number in the range from 1 to " + maxOption);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid value.");
            }
        }
    }
}

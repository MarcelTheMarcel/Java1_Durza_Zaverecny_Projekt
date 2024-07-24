package academy.metis.javabasics.lesson14.activity3.exercise1.part1;

import java.util.Scanner;

public class InputValidator {
    private final Scanner scanner;

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getValidatedChoice(int numberOfCase) {

        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice >= 1 && choice <= numberOfCase) {
                    return choice;
                } else {
                    System.out.println("Please enter a number in the range from 1 to " + numberOfCase);
                }
            } else {
                System.out.println("Please enter a valid value.");
                scanner.next();
            }
        }
    }
}

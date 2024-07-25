package academy.metis.javabasics.lesson17.activity3.exercise1.part1;

import java.util.Scanner;

public class LibraryApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InputValidator validator = new InputValidator(scanner);

    public static void main(String[] args) {
        System.out.println(">>>> Welcome to our Library <<<<");
        showMainMenu();
    }

    public static void showMainMenu() {
        System.out.println("\n1 - Titles\n2 - Members\n3 - Rentals\n4 - Exit\nChoose an option:");
        int choice = validator.getValidatedChoice(4);
        switch (choice) {
            case 1:
                TitlesPage titlesPage = new TitlesPage(scanner);
                titlesPage.showTitlesMenu();
                showMainMenu();
                break;
            case 2:
            case 3:
                showMainMenu();
                break;
            case 4:
                System.out.println("Exiting the program...");
                break;
        }
    }
}

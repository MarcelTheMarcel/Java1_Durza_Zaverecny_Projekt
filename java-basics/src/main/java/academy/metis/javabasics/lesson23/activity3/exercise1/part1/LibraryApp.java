package academy.metis.javabasics.lesson23.activity3.exercise1.part1;

import java.util.Scanner;

public class LibraryApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InputValidator validator = new InputValidator(scanner);

    public static void main(String[] args) {
        TitlesPage titlesPage = new TitlesPage(scanner);
        MembersPage membersPage = new MembersPage(scanner);
        RentalsPage rentalsPage = new RentalsPage(scanner, titlesPage, membersPage);

        System.out.println(">>>> Welcome to our Library <<<<");
        showMainMenu(titlesPage, membersPage, rentalsPage);
    }

    public static void showMainMenu(TitlesPage titlesPage, MembersPage membersPage, RentalsPage rentalsPage) {
        while (true) {
            System.out.println("\n1 - Titles\n2 - Members\n3 - Rentals\n4 - Exit\nChoose an option:");
            int choice = validator.getValidatedChoice(4);
            switch (choice) {
                case 1:
                    titlesPage.showTitlesMenu();
                    break;
                case 2:
                    membersPage.showMembersMenu();
                    break;
                case 3:
                    rentalsPage.showRentalsMenu();
                    break;
                case 4:
                    System.out.println("Exiting the program...");
                    return;
            }
        }
    }
}

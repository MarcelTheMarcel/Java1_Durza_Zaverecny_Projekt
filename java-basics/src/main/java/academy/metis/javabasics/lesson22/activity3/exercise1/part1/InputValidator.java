package academy.metis.javabasics.lesson22.activity3.exercise1.part1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
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

    public Object validationChecksTitlesId(List<Book> books, List<DVD> dvds) {
        while (true) {
            try {
                long id = Long.parseLong(scanner.nextLine());
                for (Book book : books) {
                    if (book.getTitleId() == id) {
                        return book;
                    }
                }
                for (DVD dvd : dvds) {
                    if (dvd.getTitleId() == id) {
                        return dvd;
                    }
                }
                System.out.println("Please enter a valid id:");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid id:");
            }
        }
    }

    public String validateDateOfBirthInput() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        while (true) {
            String input = scanner.nextLine();
            try {
                dateFormat.parse(input);
                return dateFormat.format(dateFormat.parse(input));
            } catch (ParseException e) {
                System.out.println("Please enter a valid value.");
            }
        }
    }

    public <T> T validationChecksUniqueId(Map<Long, T> objectMap) {
        while (true) {
            try {
                long id = Long.parseLong(scanner.nextLine());
                T obj = objectMap.get(id);
                if (obj != null) {
                    return obj;
                }
                System.out.println("Please enter a valid id:");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid id:");
            }
        }
    }
}

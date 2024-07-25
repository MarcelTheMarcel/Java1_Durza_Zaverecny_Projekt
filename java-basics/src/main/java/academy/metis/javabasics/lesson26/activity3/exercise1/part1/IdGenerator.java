package academy.metis.javabasics.lesson26.activity3.exercise1.part1;

import java.io.*;

public class IdGenerator {
    private final String idCounterFileName;
    private Long counter;

    public IdGenerator(String idCounterFileName) {
        this.idCounterFileName = idCounterFileName;
        this.counter = 0L;
        loadCounterFromFile();
    }

    public Long generateID() {
        counter++;
        saveCounterToFile();
        return counter;
    }

    private void loadCounterFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(idCounterFileName))) {
            String line = reader.readLine();
            if (line != null) {
                counter = Long.parseLong(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading counter from file: " + e.getMessage());
        }
    }

    private void saveCounterToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(idCounterFileName))) {
            writer.write(counter.toString());
        } catch (IOException e) {
            System.out.println("Error saving counter to file: " + e.getMessage());
        }
    }
}

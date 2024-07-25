package academy.metis.javabasics.lesson27.activity3.exercise1.part1;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Queue {
    private final IdGenerator idGenerator;
    private final Scanner scanner;
    private final InputValidator inputValidator;
    private static final Map<Long, QueueRecord> queueRecords = new HashMap<>();
    public static String QUEUE_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/queueRecords.txt";
    public static String QUEUE_RECORDS_ID_COUNTER_FILE_PATH = "C:/Users/MarCell/Desktop/Marcel Project/java-basics/src/main/resources/queueRecordsIdCounter.txt";

    public Queue(Scanner scanner) {
        this.scanner = scanner;
        this.inputValidator = new InputValidator(scanner);
        this.idGenerator = new IdGenerator(QUEUE_RECORDS_ID_COUNTER_FILE_PATH);
        loadQueueRecordsFromFile(QUEUE_FILE_PATH);
    }

    public void showQueueMenu(long memberId, long titleId) {
        System.out.println("Do you want to add your inquiry to the queue?\n1 - Yes\n2 - No\nChoose an option:");
        int choice = inputValidator.getValidatedChoice(2);
        if (choice == 1) {
            addToQueue(memberId, titleId);
        } else {
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        }
    }

    public void addToQueue(long memberId, long titleId) {
        long queueItemId = idGenerator.generateID();
        LocalDate addedToQueueDate = LocalDate.now();
        QueueRecord queueRecord = new QueueRecord(queueItemId, memberId, titleId, addedToQueueDate, false);
        if (saveTheQueueToTheFile(queueRecord)) {
            queueRecords.put(queueItemId, queueRecord);
            System.out.println("Successfully added to Queue");
        } else {
            System.out.println("Failed to add to Queue");
        }
    }

    public boolean saveTheQueueToTheFile(QueueRecord queueRecord) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(QUEUE_FILE_PATH, true))) {
            String recordString = queueRecord.getMemberId() + "," +
                    queueRecord.getTitleId() + "," +
                    queueRecord.getAddedToQueueDate() + "," +
                    queueRecord.isResolved() + "," +
                    queueRecord.getQueueItemId();
            writer.write(System.lineSeparator() + recordString);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }

    public void loadQueueRecordsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    System.out.println("Invalid line (less than 5 parts): " + line);
                    continue;
                }
                try {
                    QueueRecord record = new QueueRecord(
                            Long.parseLong(parts[4]),
                            Long.parseLong(parts[0]),
                            Long.parseLong(parts[1]),
                            LocalDate.parse(parts[2]),
                            Boolean.parseBoolean(parts[3])
                    );
                    queueRecords.put(record.getQueueItemId(), record);
                } catch (NumberFormatException e) {
                    System.out.println("Number format exception for line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading queue records from file: " + filePath);
        }
    }

    public void showAllQueueRecords() {
        System.out.println("All queue records:");
        listingAllQueueRecords();
        if (queueRecords.isEmpty()) {
            System.out.println("No queue records available");
        }
        System.out.println("Total number of all queue records: " + queueRecords.size());
        System.out.println("Press enter to return to Rentals page...");
        scanner.nextLine();
    }

    public void listingAllQueueRecords() {
        for (QueueRecord record : queueRecords.values()) {
            System.out.println(record);
        }
    }

    public Map<Long, QueueRecord> getQueueRecords() {
        return queueRecords;
    }
}

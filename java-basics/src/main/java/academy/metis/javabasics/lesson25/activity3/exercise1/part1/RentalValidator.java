package academy.metis.javabasics.lesson25.activity3.exercise1.part1;

import java.util.Map;

public class RentalValidator {
    private static final int MAX_NUMBER_OF_RENTED_TITLES = 2;

    public boolean isPossibleToRentTitle(long memberId, Object chosenTitle, Map<Long, RentalRecord> rentalRecords, Queue queue) {
        int availableCopies;
        long titleId;

        if (chosenTitle instanceof Book) {
            availableCopies = ((Book) chosenTitle).getAvailableCopies();
            titleId = ((Book) chosenTitle).getTitleId();
        } else {
            availableCopies = ((DVD) chosenTitle).getAvailableCopies();
            titleId = ((DVD) chosenTitle).getTitleId();
        }
        if (!isTheTitleAvailable(availableCopies)) {
            System.out.println("Title is not currently available");
            queue.showQueueMenu(memberId, titleId);
            return false;
        }
        if (rentedTwoTitlesOrRentedChosenTitleAlready(memberId, titleId, rentalRecords)) {
            return false;
        }
        return true;
    }

    public boolean memberAlreadyRentedThisTitle(RentalRecord rentalRecord, long titleId) {
        return !rentalRecord.isReturned() && rentalRecord.getTitleId() == titleId;
    }

    public boolean rentedTwoTitlesOrRentedChosenTitleAlready(long memberId, long titleId, Map<Long, RentalRecord> rentalRecords) {
        int rentedTitles = 0;
        for (RentalRecord record : rentalRecords.values()) {
            if (record.getMemberId() == memberId && !record.isReturned()) {
                rentedTitles++;
                if (memberAlreadyRentedThisTitle(record, titleId)) {
                    System.out.println("You can not rent this title because you have already rented this title");
                    return true;
                }
            }
        }
        if (rentedTitles >= MAX_NUMBER_OF_RENTED_TITLES) {
            System.out.println("You can not rent this title because you have already rented two titles");
            return true;
        }
        return false;
    }

    public boolean isTheTitleAvailable(int availableCopies) {
        return availableCopies > 0;
    }
}

package mertguler.CRS;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateManager {
    public static String datePattern = "dd-MM-yyyy";
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
    public static int rendezvousDayLimit = 15;


    public DateManager(){
    }

    public DateManager(String newDatePattern){
        datePattern = newDatePattern;
    }
    public DateManager(String newDatePattern, int newRendezvousDayLimit){
        datePattern = newDatePattern;
        rendezvousDayLimit = newRendezvousDayLimit;
    }

    public static LocalDate getCurrentDate(){
        LocalDate currentDate = LocalDate.now();
        return currentDate;
    }

    public static String getFormatedDate(LocalDate date){
        return date.format(dateFormatter);
    }

    public static LocalDate getLastDate(){
        LocalDate lastRendezvousDate = getCurrentDate().plusDays(rendezvousDayLimit);
        return lastRendezvousDate;
    }

    public static LocalDate isValidDate(String date) throws DateTimeParseException {
        LocalDate checkedDate = null;

        checkedDate = LocalDate.parse(date, dateFormatter);
        return checkedDate;
    }

    public static void checkDateRange(LocalDate desiredDate) throws DateTimeException {
        if (desiredDate == null){
            throw new DateTimeException("Enter valid date with specified date format");
        }

        if (desiredDate.isAfter(getLastDate())){
            throw new DateTimeException("Date is after last available rendezvous date");
        }

        if (desiredDate.isBefore(getCurrentDate())){
            throw new DateTimeException("Date is before current date");
        }
    }


}

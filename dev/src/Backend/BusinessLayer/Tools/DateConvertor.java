package Backend.BusinessLayer.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateConvertor {
    private List<SimpleDateFormat> knownPatterns;

    public DateConvertor() {
        knownPatterns = new ArrayList<>();
        setPatterns();
    }

    private void setPatterns() {
        SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm");
        SimpleDateFormat sd2 = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
        knownPatterns.add(sd);
        knownPatterns.add(sd2);
        sd.setLenient(false);
        sd2.setLenient(false);
    }

    private Date convertDate(String date) {
        for (SimpleDateFormat pattern : knownPatterns) {
            try {
                Date d = new Date(pattern.parse(date).getTime());
                return d;
            } catch (Exception pe) {

            }
        }
        return null;
    }



    public String dateToString(Date date) {
        for (SimpleDateFormat pattern : knownPatterns) {
            try {
                String d = pattern.format(date);
                return d;
            } catch (Exception pe) {

            }
        }
        return null;
    }

    public String dateToDailyString(Date date) {
        String s = dateToString(date);
        if(s == null)
            return s;
        return s.substring(0, s.length()-6);
    }
    public int dateToDayOfWeek(Date date) {
        LocalDateTime ldt = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return ldt.getDayOfWeek().getValue()%7;
    }


    public Date validateDate(String date) throws Exception {
        if (date == null || date.isEmpty()) {
            throw new Exception("Illegal date entered");
        }

        Date deliveryDateTime = convertDate(date);
        if (deliveryDateTime == null)
            throw new Exception("Invalid Date entered");
        Date currentTime = new Date();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(currentTime.toInstant(), ZoneId.systemDefault());
        Date dateFromLocalDT = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        if (dateFromLocalDT.compareTo(deliveryDateTime) > 0) {
            throw new Exception("The date your entered has been passed");
        }

        return deliveryDateTime;
    }
    public int compareDate(String date) throws Exception {
        if (date == null || date.isEmpty()) {
            throw new Exception("Illegal date entered");
        }

        Date deliveryDateTime = convertDate(date);
        if (deliveryDateTime == null)
            throw new Exception("Invalid Date entered");
        Date currentTime = new Date();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(currentTime.toInstant(), ZoneId.systemDefault());
        Date dateFromLocalDT = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        if (dateFromLocalDT.compareTo(deliveryDateTime) > 0) {
            return 1;
        }

        return -1;
    }
    public LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public Date convertToDate(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    public Date stringToDate(String s) {
        for (SimpleDateFormat pattern : knownPatterns) {
            try {
                Date d = new Date(pattern.parse(s).getTime());
                return d;
            } catch (Exception pe) {

            }
        }
        return null;
    }
}

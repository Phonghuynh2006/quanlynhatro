package App.Utils;

import java.time.LocalDate;
import java.sql.Date; // Import java.sql.Date
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TimeRange {

    private Date begin = new Date(System.currentTimeMillis());
    private Date end = new Date(System.currentTimeMillis());

    // Constructor that accepts LocalDate and converts to java.sql.Date
    private TimeRange(LocalDate begin, LocalDate end) {
        this.begin = java.sql.Date.valueOf(begin);  // Convert LocalDate to java.sql.Date
        this.end = java.sql.Date.valueOf(end);      // Convert LocalDate to java.sql.Date
    }

    public static TimeRange today() {
        LocalDate begin = LocalDate.now();
        return new TimeRange(begin, begin.plusDays(1));  // Creates TimeRange with LocalDate
    }

    public static TimeRange thisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate begin = now.minusDays(now.getDayOfWeek().getValue() - 1);
        return new TimeRange(begin, begin.plusDays(7));  // Creates TimeRange with LocalDate
    }

    public static TimeRange thisMonth() {
        LocalDate now = LocalDate.now();
        LocalDate begin = now.withDayOfMonth(1);
        return new TimeRange(begin, begin.plusDays(now.lengthOfMonth()));  // Creates TimeRange with LocalDate
    }

    public static TimeRange thisQuarter() {
        LocalDate now = LocalDate.now();
        int firstMonth = now.getMonth().firstMonthOfQuarter().getValue();
        LocalDate begin = now.withMonth(firstMonth).withDayOfMonth(1);
        return new TimeRange(begin, begin.plusMonths(3));  // Creates TimeRange with LocalDate
    }

    public static TimeRange thisYear() {
        LocalDate now = LocalDate.now();
        LocalDate begin = now.withMonth(1).withDayOfMonth(1);
        return new TimeRange(begin, begin.plusMonths(12));  // Creates TimeRange with LocalDate
    }
}

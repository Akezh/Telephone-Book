import java.util.Date;
import java.util.IllegalFormatException;

/**
 * Created by Фора on 15.07.2016.
 */
public class BirthDate {
    private int day;
    private int month;
    private int year;

    public BirthDate() {
        day = 0;
        month = 0;
        year = 0;
    }

    public BirthDate(int newday, int newmonth, int newyear) {
        if (!isValidDate(newday, newmonth, newyear)) {
            throw new IllegalArgumentException("Impossible date");
        }
        this.day = newday;
        this.month = newmonth;
        this.year = newyear;
    }

    public BirthDate(String BirthDateasString) {
        String[] args = BirthDateasString.split("\\.");
        if (args.length != 3) {
            throw new IllegalArgumentException("Invalid format");
        }
        int[] bd = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            bd[i] = Integer.parseInt(args[i]);
        }
        if (!isValidDate(bd[0], bd[1], bd[2])) {
            throw new IllegalArgumentException("Impossible BirthDate");
        }
        this.day = bd[0];
        this.month = bd[1];
        this.year = bd[2];
    }


    public void setDay(int newday) {
        if (!isValidDate(this.year, this.month, newday)) {
            throw new IllegalArgumentException("Impossible day");
        }
        day = newday;
    }

    public int getDay() {
        return day;
    }

    public void setMonth(int newmonth) {
        if (!isValidDate(this.year, newmonth, this.day)) {
            throw new IllegalArgumentException("Impossible month");
        }
        month = newmonth;
    }

    public int getMonth() {
        return month;
    }

    public void setYear(int newyear) {
        if (!isValidDate(newyear, this.month, this.day))
            throw new IllegalArgumentException("Impossible year");
        else year = newyear;
    }

    public int getYear() {
        return year;
    }


    public boolean isValidDate(int day, int month, int year) {
        boolean g = false;
        if ((year < 2100 && year > 0) && (month <= 12 && month > 0)) {
            if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && (day <= 31 && day > 0))
                g = true;
            else if ((month == 4 || month == 6 || month == 9 || month == 11) && (day <= 30 && day > 0)) g = true;
            else {
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    if (day > 0 && day <= 29) g = true;
                }   else if (day > 0 && day <= 28) g = true;
            }
        }
        return g;
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d.", day, month, year);
    }
}


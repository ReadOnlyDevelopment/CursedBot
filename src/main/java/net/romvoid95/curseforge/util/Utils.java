package net.romvoid95.curseforge.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class Utils {
	
	public static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

    public static int last(LocalDate date) {
        return Period.between(date, getLocalDate()).getDays();
    }

    public static Date getDateWithoutTimeUsingCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getDateWithoutTimeUsingFormat() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(new Date()));
    }

    public static LocalDate getLocalDate() {
        return LocalDate.now();
    }
}

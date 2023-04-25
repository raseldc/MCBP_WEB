/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperToFIleCreate;

/**
 *
 * @author PCUser
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final String[] timeFormats
            = {
                "HH:mm:ss", "HH:mm"
            };
    private static final String[] dateSeparators
            = {
                "/", "-", " "
            };
    private static final String DMY_FORMAT = "dd{sep}MM{sep}yyyy";
    private static final String DMY_FORMAT_1 = "dd{sep}M{sep}yyyy";
    private static final String DMY_FORMAT_2 = "d{sep}MM{sep}yyyy";
    private static final String DMY_FORMAT_3 = "d{sep}M{sep}yyyy";
    private static final String YMD_FORMAT = "yyyy{sep}MM{sep}dd";
    private static final String YMD_FORMAT_YYYY_M_DD = "yyyy{sep}M{sep}dd";
    private static final String YMD_FORMAT_YYYY_MM_D = "yyyy{sep}MM{sep}d";
    private static final String YMD_FORMAT_YYYY_M_D = "yyyy{sep}M{sep}d";
    private static final String DMMYY_FORMAT = "dd{sep}MMM{sep}yyyy";
    private static final String DDMMMYY_FORMAT = "dd{sep}MMM{sep}yy";
    private static final String ymd_template = "\\d{4}{sep}\\d{2}{sep}\\d{2}.*";
    private static final String dmy_template = "\\d{2}{sep}\\d{2}{sep}\\d{4}.*";
    private static final String dmy_template_1 = "\\d{1}{sep}\\d{2}{sep}\\d{4}.*";
    private static final String dmy_template_2 = "\\d{2}{sep}\\d{1}{sep}\\d{4}.*";
    private static final String dmy_template_3 = "\\d{1}{sep}\\d{1}{sep}\\d{4}.*";
    private static final String dmmmyyy_template = "\\d{2}{sep}\\w{3}{sep}\\d{4}.*";
    private static final String dmmmyy_template = "\\d{2}{sep}\\w{3}{sep}\\d{2}.*";

    public static Date stringToDate(String input) {
        Date date = null;
        String dateFormat = getDateFormat(input);
        if (dateFormat == null) {
            throw new IllegalArgumentException("Date is not in an accepted format " + input);
        }

        for (String sep : dateSeparators) {
            String actualDateFormat = patternForSeparator(dateFormat, sep);
            //try first with the time
            for (String time : timeFormats) {
                date = tryParse(input, actualDateFormat + " " + time);
                if (date != null) {
                    return date;
                }
            }
            //didn't work, try without the time formats
            date = tryParse(input, actualDateFormat);
            if (date != null) {
                return date;
            }
        }

        return date;
    }

    public static Calendar stringToCalendar(String input) {
        Date date = stringToDate(input);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private static String getDateFormat(String date) {
        for (String sep : dateSeparators) {
            String ymdPattern = patternForSeparator(ymd_template, sep);
            String dmyPattern = patternForSeparator(dmy_template, sep);
            String dmmmyPattern = patternForSeparator(dmmmyyy_template, sep);
            String dmmmyyPattern = patternForSeparator(dmmmyy_template, sep);
            if (date.matches(ymdPattern)) {
                return YMD_FORMAT;
            }
            if (date.matches(ymdPattern)) {
                return YMD_FORMAT;
            }
            if (date.matches(ymdPattern)) {
                return YMD_FORMAT;
            }

            if (date.matches(dmyPattern)) {
                return DMY_FORMAT;
            }
            if (date.matches(dmmmyPattern)) {
                return DMMYY_FORMAT;
            }

            if (date.matches(dmmmyyPattern)) {
                return DDMMMYY_FORMAT;
            }
        }
        return null;
    }

    private static String patternForSeparator(String template, String sep) {
        return template.replace("{sep}", sep);
    }

    private static Date tryParse(String input, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(input);
        } catch (ParseException e) {
        }
        return null;
    }

    public static String getStringWithFormat(Date date, String pattern) {
        try {
            if (date == null) {
                return "";
            }
// Create an instance of SimpleDateFormat used for formatting 
// the string representation of date according to the chosen pattern
            DateFormat df = new SimpleDateFormat(pattern);

// Using DateFormat format method we can create a string 
// representation of a date with the defined format.
            String day_st = df.format(date);

            return day_st;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}

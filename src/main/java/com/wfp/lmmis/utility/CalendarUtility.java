/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
//import org.joda.time.DateMidnight;
//import org.joda.time.DateTime;
//import org.joda.time.Days;
//import org.joda.time.Interval;

/**
 *
 * @author ratul
 */
public class CalendarUtility
{

    public CalendarUtility()
    {
    }

    public static Calendar getCurrentDateAsCalendar()
    {
//        String strDate = DateFormat.getInstance().format(new Date());
//        Date date = new Date(strDate);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        return calendar;
        // Above code does not return second in calendar object
        return Calendar.getInstance();
    }

    public static Calendar getCalenderByDate(Date date)
    {
        if (date == null)
        {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String getCurrentDateAsString()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     *
     * @param from
     * @param to
     * @return
     */
    public static Calendar getDifferenceOfHourMin(Calendar from, Calendar to)
    {
        int dHour = from.getTime().getHours() - to.getTime().getHours();
        int dMin = from.getTime().getMinutes() - to.getTime().getMinutes();
        int dSec = from.getTime().getSeconds() - to.getTime().getSeconds();
        Date duration = from.getTime();
        duration.setHours(dHour);
        duration.setMinutes(dMin);
        duration.setSeconds(dSec);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(duration);
        return calendar;
    }

    //count started from zero
    public static String getMonthStringFromInt(int monthNo)
    {
        switch (monthNo)
        {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "Invalid month.";
        }

    }

    public static int getTotalDayForMonth(int month, int year)
    {

        int numDays = 0;

        switch (month + 1)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                numDays = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                numDays = 30;
                break;
            case 2:
                if (((year % 4 == 0) && !(year % 100 == 0))
                        || (year % 400 == 0))
                {
                    numDays = 29;
                }
                else
                {
                    numDays = 28;
                }
                break;
            default:
                System.out.println("Invalid month.");
                break;
        }

        return numDays;

    }

    public int getLastDayOfMonth(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Calendar getLastDateOfMonth(Calendar calendar)
    {
        calendar.set(Calendar.DATE, 1); //set the date to start of month
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        return calendar;
    }

    public static Calendar getFirstDateOfMonth(Calendar calendar)
    {
        calendar.set(Calendar.DATE, 1); //set the date to start of month

        return calendar;
    }

//    public static int getNumberOfDaysFromDateRange(Date fromDate, Date toDate)
//    {
//        long t = 0L;
//        int days = Days.daysBetween(new DateTime(fromDate), new DateTime(toDate)).getDays();
//        System.out.println("days = " + days);
//        long diff = toDate.getTime() - fromDate.getTime();
//        int d = (int) (diff / (1000 * 60 * 60 * 24));
//        System.out.println("d = " + d);
//        return days;
//    }
    /**
     * This method return List of calendar(month start, month end) array.
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static List<Calendar[]> getCalendarArrayFromDateRange(Date fromDate, Date toDate)
    {
        try
        {
            Calendar st = getCalenderByDate(fromDate);
            Calendar end = getCalenderByDate(toDate);

            List<Calendar[]> calendarArrayList = new ArrayList<Calendar[]>();
            Calendar[] ob = new Calendar[2];
            Calendar temp1, temp2;
            ob[0] = st;
            while (st.compareTo(end) <= 0)
            {
                temp1 = getLastDateOfMonthN(st);

                if (temp1.compareTo(end) == 1)
                {
                    break;
                }
                ob[1] = temp1;
                calendarArrayList.add(ob);

                temp2 = getFirstDateOfNextMonthN(temp1);
                if (temp2.compareTo(end) == 1)
                {
                    ob = new Calendar[2];
                    ob[0] = temp2;
                    System.out.println("l");
                    break;
                }
                ob = new Calendar[2];
                ob[0] = temp2;
                st = temp2;
            }
            ob[1] = end;
            calendarArrayList.add(ob);

//            for (int i = 0; i < calendarArrayList.size(); i++)
//            {
//                Calendar[] a = calendarArrayList.get(i);
//                System.out.println(" = " + a[0].getTime() + "..." + a[1].getTime());
//            }
            return calendarArrayList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<Calendar[]>();

        }

    }

    /**
     * This method unchanged parameter(calendar), so it will be useful if
     * further needed after this method call
     *
     * @param calendar
     * @return
     */
    public static Calendar getLastDateOfMonthN(Calendar calendar)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        return cal;
    }

    /**
     * This method unchanged parameter(calendar), so it will be useful if
     * further needed after this method call
     *
     * @param calendar
     * @return
     */
    public static Calendar getFirstDateOfMonthN(Calendar calendar)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }

    public static Calendar getFirstDateOfNextMonthN(Calendar calendar)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        return cal;
    }

//    public static Interval getFirstWeekOfCurrentMonth()
//    {
//        DateMidnight firstDate = new DateMidnight().withDayOfMonth(1);
//
//        DateMidnight lastWeekDate = new DateMidnight().withDayOfMonth(8);
//
//        Interval interval = new Interval(firstDate, lastWeekDate);
//
//        System.out.println("interval>>>>>" + interval);
//        return interval;
//    }
//
//    public static Interval getLastWeekOfCurrentMonth()
//    {
//        DateMidnight firstDateOfNextMonth = new DateMidnight().withDayOfMonth(1).plusMonths(1);
//
//        DateMidnight firstDateOfLastWeekOfCurrentMonth = firstDateOfNextMonth.minusDays(7);
//
//        Interval interval = new Interval(firstDateOfLastWeekOfCurrentMonth, firstDateOfNextMonth);
//
//        System.out.println("interval>>>>>" + interval);
//        return interval;
//    }
    public static String getDateString(Date date)
    {
        if (date != null)
        {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.format(date);
        }
        else
        {
            return null;
        }
    }

    public static String getDateString(Calendar calendar)
    {
        if (calendar != null)
        {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.format(calendar.getTime());
        }
        else
        {
            return null;
        }
    }

    /**
     *
     * @param calendar
     * @param format
     * @return
     */
    public static String getDateString(Calendar calendar, String format)
    {
        if (calendar != null)
        {
            DateFormat df = new SimpleDateFormat(format);
            return df.format(calendar.getTime());
        }
        else
        {
            return null;
        }
    }

    public static String getDateString(Date date, String format)
    {
        if (date != null)
        {
            DateFormat df = new SimpleDateFormat(format);
            return df.format(date);
        }
        else
        {
            return null;
        }
    }

    /**
     * Return list of dates between startdate and enddate
     *
     * @param startdate
     * @param enddate
     * @return
     */
    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate)
    {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate))
        {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        dates.add(enddate);
        return dates;
    }
}

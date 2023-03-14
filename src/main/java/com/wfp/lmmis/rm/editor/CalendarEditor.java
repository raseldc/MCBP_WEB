/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.rm.editor;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import org.springframework.stereotype.Component;

/**
 *
 * @author sarwar
 */
@Component
public class CalendarEditor extends PropertyEditorSupport
{

    public CalendarEditor()
    {
    }

    // Converts a String to a Calendar (when submitting form)
    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        System.out.println("hi");
        if (text.contains(":"))
        {
            try
            {
                setValue(parseDate(text));
            }
            catch (ParseException ex)
            {
                //logger.getlogger(CalendarEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            setValue(tryParse(text));
        }
    }

    // Converts a Calendar to a String (when displaying form)
    @Override
    public String getAsText() throws IllegalArgumentException
    {
        Calendar text = (Calendar) this.getValue();
        String format;
        if (text != null && text.get(Calendar.HOUR_OF_DAY) == 0 && text.get(Calendar.MINUTE) == 0 && text.get(Calendar.SECOND) == 0)
        {
            format = "yy-mm-dd";
        }
        else
        {
            format = "yy-mm-dd hh:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (text == null)
        {
            return "";
        }
        String timeInString = sdf.format(text.getTime());
        return timeInString;
    }

    private Calendar tryParse(String dateString)
    {
        String formatString = "yy-mm-dd";
        try
        {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
            Date date = sdf.parse(dateString);
            cal.setTime(date);
            return cal;
        }
        catch (ParseException e)
        {
            System.out.println("tryParse(): in parse exception");
        }
        return null;
    }

    private Calendar parseDate(String text) throws ParseException
    {
        Calendar cal = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yy-mm-dd", Locale.ENGLISH);
        Date d = format.parse(text);
        cal.setTime(d);
        return cal;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.editor;

import com.wfp.lmmis.utility.DateUtilities;
import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author sarwar
 */
public class CalendarEditor extends PropertyEditorSupport
{

    public CalendarEditor()
    {
    }

    // Converts a String to a Calendar (when submitting form)
    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        setValue(DateUtilities.stringToCalendar(text));
//        if (text.contains(":"))
//        {
//            setValue(parseDate(text));
//        }
//        else
//        {
//            setValue(tryParse(text));
//        }
    }

    // Converts a Calendar to a String (when displaying form)

    /**
     *
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public String getAsText() throws IllegalArgumentException
    {
        Calendar text = (Calendar) this.getValue();
//        //System.out.println("text=" + text);
        String format;
        if (text != null && text.get(Calendar.HOUR_OF_DAY) == 0 && text.get(Calendar.MINUTE) == 0 && text.get(Calendar.SECOND) == 0)
        {
            format = "yyyy-MM-dd";
        }
        else
        {
            format = "yy-mm-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (text == null)
        {
            return "";
        }
        String timeInString = sdf.format(text.getTime());
//        //System.out.println("timeInString = " + timeInString);
        return timeInString;
    }

    
}

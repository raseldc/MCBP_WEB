/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

/**
 *
 * @author PCUser
 */
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;

public class Localizer
{

    //    private final static String RESOURCE_BUNDLE = "com.wfp.lmmis.properties.messages";
    private final static String RESOURCE_BUNDLE = "messages";
    private Locale locale = null;

    public Localizer()
    {
        this(Locale.getDefault());
    }

    public Localizer(Locale locale)
    {
        this.locale = locale;
    }

    /**
     *
     * @param key
     * @param locale
     * @return
     */
    public String getLocalizedText(String key, Locale locale)
    {
        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE,
                    locale, this.getClass().getClassLoader());

            if (bundle.keySet().contains(key))
            {
                return bundle.getString(key);
            }
            else
            {
                return key + "(No localization entry found)";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "LOCALIZATION FAILED: " + e.toString();
        }
    }

    public Locale getLocale()
    {
        return this.locale;
    }

    public static Localizer getBrowserLocalizer()
    {
        return new Localizer(LocaleContextHolder.getLocale());
    }
}

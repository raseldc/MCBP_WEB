/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;

/**
 *
 * @author Philip
 */
public enum LoggingServiceType
{
    CREATE("Save", "সেভ"),
    EDIT("Edit", "সম্পাদনা"),
    DELETE("Delete", "মুছে ফেলা");

    private final String displayName;

    private final String displayNameBn;

    private LoggingServiceType(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

    /**
     *
     * @return
     */
    public String toString()
    {
        return displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getDisplayNameBn()
    {
        return displayNameBn;
    }
}

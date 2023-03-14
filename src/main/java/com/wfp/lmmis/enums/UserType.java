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
public enum UserType
{
    MINISTRY("Ministry", "মন্ত্রণালয়"),

    /**
     *
     */
    DIRECTORATE("Directorate", "পরিচালকের দপ্তর"),

    /**
     *
     */
    FIELD("Others", "অন্যান্য"),
    BGMEA("BGMEA", "বিজিএমইএ"),
    BKMEA("BKMEA", "বিকেএমইএ");

    private final String displayName;
    private final String displayNameBn;

    private UserType(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

    @Override
    public String toString()
    {
        return displayName;
    }

    /**
     *
     * @return
     */
    public String getDisplayName()
    {
        return displayName;
    }

    public String getDisplayNameBn()
    {
        return displayNameBn;
    }
}

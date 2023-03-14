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
public enum MaritalInfoEnum
{

    /**
     *
     */
    MARRIED("Married", "বিবাহিতা"),
    WIDOW("Widow", "বিধবা"),
    DIVORCED("Divorced", "তালাকপ্রাপ্ত"),

    /**
     *
     */
    HUSBAND_DESERTED("Husband Deserted", "স্বামী পরিত্যক্তা"),
    OTHERS("Others", "অন্যান্য");

    private final String displayName;
    private final String displayNameBn;

    private MaritalInfoEnum(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

    /**
     *
     * @return
     */
    @Override
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

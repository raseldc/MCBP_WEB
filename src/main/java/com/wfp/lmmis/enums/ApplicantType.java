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
public enum ApplicantType
{
    UNION("Union", "ইউনিয়ন"), 
    MUNICIPAL("Municipal", "পৌরসভা"), 
    CITYCORPORATION("City Corporation", "সিটি কর্পোরেশন"),    
    BGMEA("BGMEA", "বিজিএমইএ"),
    BKMEA("BKMEA", "বিকেএমইএ"),

    /**
     *
     */
    REGULAR("Regular", "সাধারণ"); 
    
    private final String displayName;
    private final String displayNameBn;

    private ApplicantType(String displayName, String displayNameBn)
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

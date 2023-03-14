/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.types;

/**
 *
 * @author user
 */
public enum CoverageAreaClass
{
    UNION("Union","ইউনিয়ন"),
    CITY_CORPORATION("City Corporation","সিটি কর্পোরেশন"),

    /**
     *
     */
    A_CATEGORY("A Category","A শ্রেণী"),
    B_CATEGORY("B Category","B শ্রেণী"),
    C_CATEGORY("C Category","C শ্রেণী");
    
    private final String displayName;
    private final String displayNameBn;
    private CoverageAreaClass(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

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

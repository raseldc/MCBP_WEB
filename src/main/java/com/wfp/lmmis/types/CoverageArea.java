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
public enum CoverageArea
{
    UNION("Union","ইউনিয়ন"),
    MUNICIPAL("Municipal","পৌরসভা"),
    CITY_CORPORATION("City Corporation","সিটি কর্পোরেশন");
    
    private final String displayName;
    private final String displayNameBn;
    private CoverageArea(String displayName, String displayNameBn)
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

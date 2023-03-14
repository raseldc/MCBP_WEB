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
public enum Occupation
{
    
    /**
     *
     */
    HOUSEWIFE("Housewife", "গৃহিনী"),      
    WORKER("Worker", "কর্মজীবী");
    
    private final String displayName;
    private final String displayNameBn;

    private Occupation(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

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


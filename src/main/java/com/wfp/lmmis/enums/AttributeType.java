/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;

/**
 *
 * @author sarwar
 */
public enum AttributeType
{
    COMBO("Combo", "কম্বো" ),

    /**
     *
     */
    TEXT("Text", "টেক্সট"), 
    RADIO("Radio", "রেডিও");

    private final String displayName;
    private final String displayNameBn;

    private AttributeType(String displayName, String displayNameBn)
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

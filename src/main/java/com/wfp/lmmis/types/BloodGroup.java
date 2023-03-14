/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.types;

/**
 *
 * @author PCUser
 */
public enum BloodGroup
{

    /**
     *
     */
    APositive("A+", "এ+"),
    ANegative("A-", "এ-"),
    BPositive("B+", "বি+"),
    BNegative("B-", "বি-"),
    ABPositive("AB+", "এবি+"),
    ABNegative("AB-", "এবি-"),
    OPositive("O+", "ও+"),
    ONegative("O-", "ও-");

    private final String displayName;
    private final String displayNameBn;

    private BloodGroup(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

    public String toString()
    {
        return displayName;
    }

    public String toBanglaString()
    {
        return displayNameBn;
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

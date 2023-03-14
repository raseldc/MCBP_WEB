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
public enum Gender
{
//    MALE("Male"),
    FEMALE("Female");

    
//    private static final ResourceBundle res = ResourceBundle.getBundle("messages", Locale.getDefault());

    private final String displayName;

    private Gender(String displayName)
    {
        this.displayName = displayName;
    }

    public String toString()
    {        
        return displayName;
    }

//    public static Gender getMALE()
//    {
//        return MALE;
//    }

    public static Gender getFEMALE()
    {
        return FEMALE;
    }

    public String getDisplayName()
    {
        return displayName;
    }

}

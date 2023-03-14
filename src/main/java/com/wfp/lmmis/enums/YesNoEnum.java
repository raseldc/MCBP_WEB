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
public enum YesNoEnum
{
    YES("Yes", "হ্যাঁ", 0),
    NO("No", "না", 1);

    private final String displayName;
    private final String displayNameBn;
    private final int score;

    private YesNoEnum(String displayName, String displayNameBn, int score)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
        this.score = score;
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
    
    public int getScore()
    {
        return score;
    }
}

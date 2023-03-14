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
public enum MonthlyIncome
{

    LESSTHAN6000("Less than or equal to 8000", "৮০০০ টাকার নীচে অথবা সমান", 1), // modified 6000 to 8000
    GREATERTHAN6000("Above 8000", "৮০০০ টাকার ঊর্ধ্বে", 0),
    LESSTHAN8000("Less than or equal to 10000", "১০০০০ টাকার নীচে অথবা সমান", 1), 

    /**
     *
     */
    GREATERTHAN8000("Above 10000", "১০০০০ টাকার ঊর্ধ্বে", 0),
    LESSTHAN12000("Less than or equal to 12000", "১২০০০ টাকার নীচে অথবা সমান", 1),
    GREATERTHAN12000("Above 12000", "১২০০০ টাকার ঊর্ধ্বে", 0);

    private final String displayName;
    private final String displayNameBn;
    private final int score;

    private MonthlyIncome(String displayName, String displayNameBn, int score)
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

    /**
     *
     * @return
     */
    public String getDisplayNameBn()
    {
        return displayNameBn;
    }
    public int getScore()
    {
        return score;
    }
}

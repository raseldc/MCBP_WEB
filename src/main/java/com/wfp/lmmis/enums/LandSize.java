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
public enum LandSize
{

    LESSTHANPOINTONEFIVE("Below 15 Decimal", "১৫ শতকের নীচে", 1),

    /**
     *
     */
    LESSTHANPOINTZEROFIVE("Below 5 Decimal", "৫ শতকের নীচে", 1),
    GREATERTHANPOINTONEFIVE("Above 15 Decimal", "১৫ শতকের ঊর্ধ্বে", 0);

    private final String displayName;
    private final String displayNameBn;
    private final int score;

    private LandSize(String displayName, String displayNameBn, int score)
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

    public int getScore()
    {
        return score;
    }
}

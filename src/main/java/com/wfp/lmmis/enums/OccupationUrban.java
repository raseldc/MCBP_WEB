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
public enum OccupationUrban
{

    RMGWORKER("Ready made garments worker", "গার্মেন্টস কর্মী", 1),
    DOMESTICWORKER("Domestic worker", "গৃহ কর্মী", 1),
    DAYLABOUR("Day Labour", "দিনমজুর", 1),
//    CRAFTSMAN("Craftsman", "কারিগর", 1), // removed by wfp on 10/02/20
    UNEMPLOYED("Unemployed", "বেকার", 2),
    OTHERS("Others", "অন্যান্য", 0);

    private final String displayName;
    private final String displayNameBn;
    private final int score;

    private OccupationUrban(String displayName, String displayNameBn, int score)
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

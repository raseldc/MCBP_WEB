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
public enum DisabilityInFamily
{

    SELF("Self", "নিজে", 1),
    FAMILYMEMBER("Family Member", "পরিবারের সদস্য", 1),
    NONE("None", "কেউ না", 0);

    private final String displayName;
    private final String displayNameBn;
    private final int score;

    private DisabilityInFamily(String displayName, String displayNameBn, int score)
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

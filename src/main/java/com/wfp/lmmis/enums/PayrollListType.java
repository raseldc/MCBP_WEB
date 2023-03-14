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
public enum PayrollListType
{

    /**
     *
     */
    UNION("Union", "উপজেলা"),
    MUNICIPAL("Municipal", "পৌরসভা"),
    DISTRICT("District", "জেলা"),

    /**
     *
     */
    BGMEA("BGMEA", "বিজিএমইএ"),
    BKMEA("BKMEA", "বিকেএমইএ");

    private final String displayName;
    private final String displayNameBn;

    private PayrollListType(String displayName, String displayNameBn)
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

    /**
     *
     * @return
     */
    public String getDisplayNameBn()
    {
        return displayNameBn;
    }
}

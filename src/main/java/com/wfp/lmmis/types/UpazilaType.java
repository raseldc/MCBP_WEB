/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.types;

/**
 *
 * @author USER
 */
public enum UpazilaType
{
    UPAZILA("Upazila","উপজেলা"),
    DISTRICT("District","জেলা");
    
    private final String displayName;
    private final String displayNameBn;
    private UpazilaType(String displayName, String displayNameBn)
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

    /**
     *
     * @return
     */
    public String getDisplayNameBn()
    {
        return displayNameBn;
    }
}

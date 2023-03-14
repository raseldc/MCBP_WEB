/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.types;

/**
 *
 * @author user
 */
public enum FactoryType
{
    GARMENTS("Garments","গার্মেন্টস"),

    /**
     *
     */
    KNITWEAR("Knitwear","নিটওয়্যার");
    
    private final String displayName;
    private final String displayNameBn;

    private FactoryType(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
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

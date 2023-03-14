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
public enum ProcurementItemType
{
    MAIN_ITEM("Main Item","প্রধান আইটেম"),
    SUB_ITEM("Sub Item","সাব আইটেম");
    
    private final String displayName;
    private final String displayNameBn;

    private ProcurementItemType(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
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
   

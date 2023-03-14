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
public enum PayrollStatus
{
    SAVED("Not Submitted for Approval", "অনুমোদনের জন্য জমা দেয়া হয়নি"),
    SUBMITTED("Approval Pending", "অনুমোদন পেন্ডিং"),
    APPROVED("Approved", "অনুমোদিত"),
    RECHECK("Recheck", "পুনঃপরীক্ষা");
    

    private final String displayName;
    private final String displayNameBn;

    private PayrollStatus(String displayName, String displayNameBn)
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

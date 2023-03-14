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
public enum BeneficiaryStatus
{
    ACTIVE("Active", "সচল"),//101//0

    /**
     *
     */
    TEMPORARILY_ACTIVE("Temporarily Inactive", "সাময়িকভাবে নিস্ক্রিয়" ),//102//1
    INACTIVE("Inactive", "নিষ্ক্রিয় " ),//103//2
    PAYMENT_HOLD("Payment Hold", "পেমেন্ট হোল্ড" ),//104//3
    PAYMENT_COMPLETE("Payment Complete", "সকম পেমেন্ট সম্পন্ন" );//104//4

    private final String displayName;
    private final String displayNameBn;

    private BeneficiaryStatus(String displayName, String displayNameBn)
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

    public String getDisplayNameBn()
    {
        return displayNameBn;
    }

    
}

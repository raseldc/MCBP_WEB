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
public enum BeneficiaryDeactivationReasons
{

    /**
     *
     */
    MISCARRIAGE("Miscarriage", "গর্ভপাত"),
    STILLBIRTH("Stillbirth", "মৃত শিশুর জন্ম"),
    CHILD_DIED_WITHIN_TWO_YEARS("Child Died Within Two Years","দুই বছরের মধ্যে শিশু মারা গেছে"),
    MOTHER_DIED_WITHIN_TWO_YEARS("Mother Died Within Two Years","দুই বছরের মধ্যে মা মারা গেছে"),
    WRONG_INFORMATION_PROVIDED("Wrong Information Provided","ভুল তথ্য প্রদান"),
    OTHER("Other", "অন্যান্য");
//    PAYMENT_HOLD("Payment Hold", "পেমেন্ট হোল্ড" );//104//3

    private final String displayName;
    private final String displayNameBn;

    private BeneficiaryDeactivationReasons(String displayName, String displayNameBn)
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

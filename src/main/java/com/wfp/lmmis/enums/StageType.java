/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;
public enum StageType
{

    /**
     *
     */
    SPBMU_VERIFICATION,
    FIELD_VERIFICATION,    

    /**
     *
     */
    FACTORY_VERIFICATION,
    FINAL_APPROVAL;
    
   @Override
    public String toString()
    {
        switch (this)
        {
            case SPBMU_VERIFICATION:
                return "Double Dipping Verification";
            case FIELD_VERIFICATION:
                return "Field Verification";
            case FINAL_APPROVAL:
                return "Final Approval";
            default:
                throw new AssertionError();
        }
    }
    public String toStringBangla()
    {
        switch (this)
        {
            case SPBMU_VERIFICATION:
                return "ডাবল ডিপিং যাচাই";
            case FIELD_VERIFICATION:
                return "মাঠ পর্যায়ে যাচাই";
            case FINAL_APPROVAL:
                return "চূড়ান্ত অনুমোদন";
            default:
                throw new AssertionError();
        }
    }
}

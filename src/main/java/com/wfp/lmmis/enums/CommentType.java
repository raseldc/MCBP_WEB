/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;

public enum CommentType
{
    APPROVED,
    REJECTED;

    @Override
    public String toString()
    {
        switch (this)
        {
            case APPROVED:
                return "Approved";
            case REJECTED:
                return "Rejected";
            default:
                throw new AssertionError();
        }
    }

    /**
     *
     * @return
     */
    public String toStringBangla()
    {
        switch (this)
        {
            case APPROVED:
                return "অনুমদিত";
            case REJECTED:
                return "বাতিল";
            default:
                throw new AssertionError();
        }
    }

//    
//    private final int value;
//
//    private LoggingServiceType(int value)
//    {
//        this.value = value;
//    }
//    public int getValue() { return value; }
}

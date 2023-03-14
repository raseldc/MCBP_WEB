/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.types;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public enum PaymentStatus {
    SENTTOIBASS("Sent to iBAS++", "iBAS++ এ পাঠানো হয়েছে"),
    SUCCESSFULPAYMENT("Successful Payments", "পেমেন্ট সফল হয়েছে"),
    RETURNEDEFTS("Returned EFTs ", "EFT রিটার্ন হয়েছে"),
    BLOCKEDPAYMENTS("Blocked Payments by SPBMU MIS", "SPBMU MIS কর্তৃক পেমেন্ট ব্লকড"),
    APPROVEDPAYROLLS("Approved Payrolls Waiting for Submission to iBAS++", "iBAS++ এ পাঠানো হয়নি এমন অনুমোদিত পে-রোল"),
    Deafult("Deafult", "খুঁজে পাওয়া যায়নি ");
    private final String displayName;
    private final String displayNameBn;

    private PaymentStatus(String displayName, String displayNameBn) {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

    public String toString() {
        return displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayNameBn() {
        return displayNameBn;
    }

//    public static List<EnumMappingClass> fromValuesToMap() {
//        List<EnumMappingClass> enumMappingClassList = new ArrayList<EnumMappingClass>();
//        for (PaymentStatus enumClass : PaymentStatus.values()) {
//            if (enumClass == PaymentStatus.Deafult || enumClass == PaymentStatus.APPROVEDPAYROLLS) {
//                continue;
//            }
//            enumMappingClassList.add(new EnumMappingClass(enumClass.name(), 0, enumClass.displayName, enumClass.displayNameBn));
//
//        }
//
//        return enumMappingClassList;
//    }

    public static List<PaymentStatus> getSelectedList() {
        List<PaymentStatus> enumMappingClassList = new ArrayList<PaymentStatus>();
        for (PaymentStatus enumClass : PaymentStatus.values()) {
            if (enumClass == PaymentStatus.Deafult || enumClass == PaymentStatus.APPROVEDPAYROLLS) {
                continue;
            }
            enumMappingClassList.add(enumClass);

        }

        return enumMappingClassList;
    }
}

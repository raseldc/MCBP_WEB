/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author PCUser
 */
public enum ApplicationStatus {
    PRIORITIZATION_PENDING("Prioritization Pending", "অগ্রাধিকার পেন্ডিং"),//0

    /**
     *
     */
    RECOMMENDATION_PENDING("Recommendation Pending", "সুপারিশ পেন্ডিং"),//1
    RECOMMENDATION_APPROVED("Recommended", "সুপারিশকৃত"),//2
    RECOMMENDATION_REJECTED("Recommendation Rejected", "সুপারিশ বাতিল"),//3

    /**
     *
     */
    VERIFICATION_PENDING("Verification Pending", "যাচাই পেন্ডিং"),//4
    VERIFICATION_APPROVED("Verification Approved", "যাচাই অনুমোদিত"),//5
    VERIFICATION_REJECTED("Verification Rejected", "যাচাই বাতিল"),//6 
    DWA_APPROVAL_PENDING("DWA Approval Pending", "ডিডব্লিউএ অফিসার কর্তৃক যাচাই পেন্ডিং "),//7
    DWA_APPROVAL_APPROVED("DWA Approval Approved", "ডিডব্লিউএ অফিসার কর্তৃক অনুমোদিত"),//8
    DWA_APPROVAL_REJECTED("DWA Rejected", "ডিডব্লিউএ অফিসার কর্তৃক বাতিল"),//9  

    REJECTED_AT_FINAL_APPROVAL("Rejected at Fianl Approval", "চূড়ান্ত নির্বাচনে বাতিল"),//10

    /**
     *
     */
    SELECTED_AT_FINAL_APPROVAL("Selected at Fianl Approval", "চূড়ান্ত নির্বাচনে নির্বাচিত"),//11

    BGMEA_REPRESENTITIVE_LEVEL_VERIFICATION("BGMEA Representative Level Verification", "বিজিএমইএ প্রতিনিধি পর্যায়ে যাচাই"),//31
    BKMEA_REPRESENTITIVE_LEVEL_VERIFICATION("BKMEA Representative Level Verification", "বিকেএমইএ প্রতিনিধি পর্যায়ে যাচাই"),//32
    PROCESSING_PENDING("Processing Pending", "Onsite/অফিসে প্রসেসিং পেন্ডিং"),//101//0
    NID_VERIFICATION_PENDING("NID Verification Pending", "NID যাচাই পেন্ডিং"),//102//1

    /**
     *
     */
    FIELD_VERIFICATION_PENDING_WITHOUT_NID("Field Verification Pending Without NID", "মাঠ পর্যায়ে যাচাই পেন্ডিং"),//103//2
    REJECTED_INITIALLY("Rejected Initially", "শুরুতে বাতিল"),//104//3
    NID_VERIFICATION_PROCESSING("NID Verification Processing", "NID যাচাই চলমান"),//201//4
    FIELD_VERIFICATION_PENDING_WITH_NID("Field Verification Pending With NID", "মাঠ পর্যায়ে যাচাই পেন্ডিং"),//202//5
    REJECTED_NID_VERIFICATION("Rejected @NID Verification", "NID যাচাইতে বাতিল "),//203//6

    /**
     *
     */
    NID_VERIFICATION_PROCESSING_WITH_NID_UPDATE("NID Verification Processing with NID update", ""),//204//7

    /**
     *
     */
    REJECTED_NID_UPDATE_VERIFICATION("Rejected @NID Update Verification", ""),//205//8
    CARD_PRINTING_PENDING_WITHOUT_NID("Card Printing Pending without NID", "কার্ড প্রিন্টিং পেন্ডিং"),//206//9
    PRIORITIZATION_PENDING_WITH_NID("Prioritization Pending with NID", "অগ্রাধিকার পেন্ডিং"),//301//10
    PRIORITIZATION_PENDING_WITHOUT_NID("Prioritization Pending without NID", "অগ্রাধিকার পেন্ডিং"),//302//11
    REJECTED_FIELD_VERIFICATION("Rejected @Field Verification", "মাঠ পর্যায়ে যাচাইতে বাতিল"),//303//12
    AUDITING_PENDING_WITH_NID("Auditing Pending with NID", ""),//401//13

    /**
     *
     */
    AUDITING_PENDING_WITHOUT_NID("Auditing Pending without NID", ""),//402//14
    REJECTED_PRIORITIZATION("Rejected @Prioritization", ""),//403//15
    NID_NEEDED("NID Needed", ""),//501//16
    CARD_PRINTING_PENDING_WITH_NID("Card Printing Pending with NID", "কার্ড প্রিন্টিং পেন্ডিং"),//502//17
    REJECTED_AUDITING("Rejected @Auditing", ""),//503//18
    NEW_NID_VERIFICATION_PENDING("New NID Verification Pending", ""),//601//19
    NID_NOT_UPDATED("NID not updated", ""),//602//20
    WAITING_FOR_CARD_DELIVERY("Waiting for Card Delivery", ""),//701//21
    FIELD_VERFICATION_DONE_WITH_NID("Field Verification Pending", "মাঠ পর্যায়ে যাচাই সম্পন্ন"),//22    
    FIELD_VERFICATION_DONE_WITHOUT_NID("Field Verification Done Without NID", "মাঠ পর্যায়ে যাচাই সম্পন্ন"),//23
    UNION_LEVEL_VERIFICATION("Union Level Verification", "ইউনিয়ন পরিষদে যাচাই পেন্ডিং"),//24
    UPAZILA_MEMBER_LEVEL_VERIFICATION("Upazila Member Level Verification", "উপজেলা সচিব পর্যায়ে যাচাই পেন্ডিং"),//25
    UPAZILA_PRESIDENT_LEVEL_VERIFICATION("Upazila President Level Verification", "উপজেলা সভাপতি পর্যায়ে যাচাই পেন্ডিং"),//26
    DOUBLE_DIPPING_VERIFICATION_PENDING("Double Dipping Verification Pending", "ডাবল ডিপিং যাচাই পেন্ডিং"),//27
    REJECTED_SPBMU("Rejected @SPBMU", "SPBMU তে বাতিল"),//28

    /**
     *
     */
    DISTRICT_MEMBER_LEVEL_VERIFICATION("District Member Level Verification", "জেলা সদস্য সচিব পর্যায়ে যাচাই"),//29
    DISTRICT_PRESIDENT_LEVEL_VERIFICATION("District President Level Verification", "জেলা সভাপতি পর্যায়ে যাচাই"),//30
    DWA_OFFICER_LEVEL_VERIFICATION("DWA Officer Level Verification", "ডিডব্লিউএ অফিসার পর্যায়ে যাচাই");//33
//    BGMEA_REPRESENTITIVE_LEVEL_VERIFICATION("DWA Officer Level Verification", "ডিডব্লিউএ অফিসার পর্যায়ে যাচাই"),//33
//    BKMEA_REPRESENTITIVE_LEVEL_VERIFICATION("DWA Officer Level Verification", "ডিডব্লিউএ অফিসার পর্যায়ে যাচাই");//33

//    private static final ResourceBundle res = ResourceBundle.getBundle("messages", Locale.getDefault());
    private final String displayName;
    private final String displayNameBn;

    private ApplicationStatus(String displayName, String displayNameBn) {
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

    /**
     *
     * @return
     */
    public static List<EnumMappingClass> fromValuesToMapForApplicationUpdate(String type) {
        List<EnumMappingClass> enumMappingClassList = new ArrayList<EnumMappingClass>();
        for (ApplicationStatus enumClass : ApplicationStatus.values()) {
            if (type.equals("union") || type.equals("municipal")) {
                List<Integer> statusList = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 10, 11));
                if (statusList.contains(enumClass.ordinal())) {
                    enumMappingClassList.add(new EnumMappingClass(enumClass.name(), enumClass.ordinal(), enumClass.displayName, enumClass.displayNameBn));
                }
            } else if (type.equals("bgmea")) {
                List<Integer> statusList = new ArrayList<>(Arrays.asList(31));
                if (statusList.contains(enumClass.ordinal())) {
                    enumMappingClassList.add(new EnumMappingClass(enumClass.name(), enumClass.ordinal(), enumClass.displayName, enumClass.displayNameBn));
                }
            } else if (type.equals("bkmea")) {
                List<Integer> statusList = new ArrayList<>(Arrays.asList(32));
                if (statusList.contains(enumClass.ordinal())) {
                    enumMappingClassList.add(new EnumMappingClass(enumClass.name(), enumClass.ordinal(), enumClass.displayName, enumClass.displayNameBn));
                }
            }

        }

        return enumMappingClassList;
    }

    public static ApplicationStatus getEnumFromValue(int value) {
        List<EnumMappingClass> enumMappingClassList = new ArrayList<EnumMappingClass>();
        for (ApplicationStatus enumClass : ApplicationStatus.values()) {
            if (enumClass.ordinal() == value) {
                return enumClass;
            }

        }
        return null;
    }
}

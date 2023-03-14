/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;

/**
 *
 * @author Philip
 */
public enum LoggingTableType
{
    Role("Role", "রোল"),
    Scheme("Scheme", "কর্মসূচি"),

    /**
     *
     */
    Scheme_Attribute("Selection Criteria", "নির্বাচন যোগ্যতা"),

    /**
     *
     */
    User("User", "ব্যবহারকারী"),
    GrievanceType("Grievance Type", "অভিযোগের ধরণ"),
    GrievanceStatus("Grievance Status", "অভিযোগের স্ট্যাটাস"),

    /**
     *
     */
    Grievance("Grievance", "অভিযোগ"),
    Training_Type("Training Type", "প্রশিক্ষণের বিষয়"),
    Purpose("Monitoring Purpose", "তদারকিকরণের উদ্দেশ্য"),
    Division("Division", "বিভাগ"),
    District("District", "জেলা"),

    /**
     *
     */
    Upazilla("Upazilla", "উপজেলা"),
    Union("Union", "ইউনিয়ন"),
    Bank("Bank", "ব্যাংক"),
    Branch("Bank-Branch", "ব্যাংক-শাখা"),
    AccountType("Account Type", "হিসাবের ধরণ"),
    FiscalYear("Fiscal Year", "অর্থবছর"),
    PaymentCycle("Payment Cycle", "পেমেন্ট চক্র"),
    PaymentType("Payment Type", "পেমেন্টের ধরন"),
    MobileBankingProvider("Mobile Banking Provider", "মোবাইল ব্যাংকিং সেবা প্রদানকারী"),
    BenificiaryQuota("Benificiary Quota", "ভাতাভোগীর কোটা"),

    /**
     *
     */
    Factory("Factory", "ফ্যাক্টরি"),
    PostOfficeBranch("Post Office Branch", "পোস্ট অফিস শাখা "),
    Batch("Batch", "ব্যাচ"),
    Village("Village", "Village");

    private final String displayName;

    private final String displayNameBn;

    private LoggingTableType(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

    /**
     *
     * @return
     */
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

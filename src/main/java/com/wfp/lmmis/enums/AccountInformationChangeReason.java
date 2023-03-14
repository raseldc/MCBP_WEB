/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public enum AccountInformationChangeReason {

    /**
     *
     */
    WrongAccount(1, "Wrong Account ", "ভুল একাউন্ট সংশোধন"),

    /**
     *
     */
    PaymentType(2, "Payment Type", "পেমেন্ট মাধ্যম সংশোধন"),
    BounceBack(3, "Bounce Back Correct", "বাউন্স ব্যাক সংশোধন"),
    Personal(4, "Personal", "ব্যক্তিগত");

    private int value;
    private String nameInEnglish;
    private String nameInBangla;

    private AccountInformationChangeReason(Integer value, String nameInEnglish, String nameInBangla) {
        this.value = value;
        this.nameInEnglish = nameInEnglish;
        this.nameInBangla = nameInBangla;
    }

    public int getValue() {
        return value;
    }

    public static List<EnumMappingClass> fromValuesToMap() {
        List<EnumMappingClass> enumMappingClassList = new ArrayList<EnumMappingClass>();
        for (AccountInformationChangeReason enumClass : AccountInformationChangeReason.values()) {

            enumMappingClassList.add(new EnumMappingClass(enumClass.name(), enumClass.getValue(), enumClass.nameInEnglish, enumClass.nameInBangla));

        }

        return enumMappingClassList;
    }

    /**
     *
     * @param value
     * @return
     */
    public static String nameFromValues(int value) {
        for (AccountInformationChangeReason enumClass : AccountInformationChangeReason.values()) {
            if (enumClass.getValue() == value) {
                return enumClass.name();
            }
        }

        return "";
    }

    public static EnumMappingClass getEnumMappingClassFromValue(int value) {
        EnumMappingClass enumMappingClass = new EnumMappingClass("class Name Not Found", 0, "Not Found", "পাওয়া যায়নি");
        for (AccountInformationChangeReason enumClass : AccountInformationChangeReason.values()) {
            if (enumClass.getValue() == value) {
                enumMappingClass = new EnumMappingClass(enumClass.name(), enumClass.getValue(), enumClass.nameInEnglish, enumClass.nameInBangla);

            }

        }

        return enumMappingClass;
    }

    public String getNameInEnglish() {
        return nameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish) {
        this.nameInEnglish = nameInEnglish;
    }

    public String getNameInBangla() {
        return nameInBangla;
    }

    public void setNameInBangla(String nameInBangla) {
        this.nameInBangla = nameInBangla;
    }

}

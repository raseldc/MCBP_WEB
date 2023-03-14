/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;

import com.wfp.lmmis.types.PaymentStatus;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philip
 */
public enum PaymentTypeEnum {
    BANKING("Banking", "ব্যাংকিং"),
    MOBILEBANKING("Mobile Banking", "মোবাইল ব্যাংকিং"),
    POSTOFFICE("Post Office", "পোস্ট অফিস");

    private final String displayName;
    private final String displayNameBn;

    private PaymentTypeEnum(String displayName, String displayNameBn) {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayNameBn() {
        return displayNameBn;
    }

    public static String nameFromValues(int value) {
        int i = 0;
        for (PaymentTypeEnum enumClass : PaymentTypeEnum.values()) {
            if (value == i) {
                return enumClass.getDisplayNameBn();
            }
            i++;
        }

        return "";
    }

    /**
     *
     * @param name
     * @return
     */
    public static int valueFromName(String name) {
        int i = 0;
        for (PaymentTypeEnum enumClass : PaymentTypeEnum.values()) {
            if (enumClass.name().equals(name)) {
                return i;
            }
            i++;

        }

        return 0;
    }

    public static List<PaymentTypeEnum> fromValuesToMap() {
        List<PaymentTypeEnum> enumMappingClassList = new ArrayList<PaymentTypeEnum>();
        for (PaymentTypeEnum enumClass : PaymentTypeEnum.values()) {
            if (enumClass == PaymentTypeEnum.POSTOFFICE) {
                continue;
            }
            enumMappingClassList.add(enumClass);
            // enumMappingClassList.add(new EnumMappingClass(enumClass.name(), 0, enumClass.displayName, enumClass.displayNameBn));

        }

        return enumMappingClassList;
    }
}

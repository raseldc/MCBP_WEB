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
public enum EducationLevelEnum
{
    FIVE("Class V", "৫ম শ্রেণী"),
    SIX("Class VI", "৬ষ্ঠ শ্রেণী"),

    /**
     *
     */
    SEVEN("Class VII", "৭ম শ্রেণী"),
    EIGHT("Class VIII", "৮ম শ্রেণী"),
    NINE("Class IX", "৯ম শ্রেণী"),

    /**
     *
     */
    TEN("Class X", "১০ম শ্রেণী"),
    ELEVEN("SSC/Similar", "এস এস সি/অনুরূপ"),
    TWELVE("HSC/Similar", "এইচ এস সি/অনুরূপ");

    private final String displayName;
    private final String displayNameBn;

    private EducationLevelEnum(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

    @Override
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

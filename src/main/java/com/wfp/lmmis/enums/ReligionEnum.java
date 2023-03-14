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
public enum ReligionEnum
{
    ISLAM("Islam", "ইসলাম"),
    HINDU("Hinduism", "হিন্দুধর্ম"),
    BUDDIST("Buddhism", "বৌদ্ধধর্ম"),
    CHRISTIAN("Christianity", "খ্রিস্টানধর্ম"),

    /**
     *
     */
    OTHERS("Others", "অন্যান্য");

    private final String displayName;
    private final String displayNameBn;

    private ReligionEnum(String displayName, String displayNameBn)
    {
        this.displayName = displayName;
        this.displayNameBn = displayNameBn;
    }

    @Override
    public String toString()
    {
        return displayName;
    }

    /**
     *
     * @return
     */
    public String getDisplayName()
    {
        return displayName;
    }

    public String getDisplayNameBn()
    {
        return displayNameBn;
    }
}

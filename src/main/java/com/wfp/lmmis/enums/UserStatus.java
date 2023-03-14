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
public enum UserStatus {

    /**
     *
     */
    ACTIVE("Active", "সক্রিয়"),
    INACTIVE("InActive", "নিষ্ক্রিয়"),
    PASSWORDCHANGE("PasswordChange", "পাসওয়ার্ড চেঞ্জ");
    private final String displayName;
    private final String displayNameBn;

    private UserStatus(String displayName, String displayNameBn) {
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

    /**
     *
     * @return
     */
    public String getDisplayNameBn() {
        return displayNameBn;
    }
}

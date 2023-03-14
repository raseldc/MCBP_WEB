/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;

public enum ComparisonType
{
    EQUALS("=", "="),
    NOT_EQUALS("!=", "!="),
    LESS_THAN("<", "<"),
    GREATER_THAN(">", ">"),
    LESS_THAN_EQUAL("<=", "<="),
    GREATER_THAN_EQUAL(">=", ">="),

    /**
     *
     */
    NONE("None", "নাই");
    private final String displayName;
    private final String displayNameBn;

    private ComparisonType(String displayName, String displayNameBn)
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

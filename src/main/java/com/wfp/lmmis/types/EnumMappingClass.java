/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.types;

import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.utility.StringModification;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class EnumMappingClass extends BaseModel {

    String name;
    Integer value;
    String displayNameBn;
    String displayName;
    Double score;

    public EnumMappingClass() {
    }

    public EnumMappingClass(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public EnumMappingClass(String name, Integer value, String nameInEnglish, String nameInBangla) {
        this.name = name;
        this.value = value;
        this.displayNameBn = nameInBangla;
        this.displayName = nameInEnglish;
    }

    /**
     *
     * @param name
     * @param value
     * @param nameInEnglish
     * @param nameInBangla
     * @param score
     */
    public EnumMappingClass(String name, Integer value, String nameInEnglish, String nameInBangla, double score) {
        this.name = name;
        this.value = value;
        this.displayNameBn = nameInBangla;
        this.displayName = nameInEnglish;
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    /**
     *
     * @return
     */
    public String getName() {

        return StringModification.AddSpacesToSentence(name, true);
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public String getDisplayNameBn() {
        return displayNameBn;
    }

    public void setDisplayNameBn(String displayNameBn) {
        this.displayNameBn = displayNameBn;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}

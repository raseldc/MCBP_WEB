/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;

import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.utility.StringModification;


/**
 *
 * @author icvgd
 */
public class EnumMappingClass extends BaseModel {

    String name;
    Integer value;
    String nameInBangla;
    String nameInEnglish;
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
        this.nameInBangla = nameInBangla;
        this.nameInEnglish = nameInEnglish;
    }

    public EnumMappingClass(String name, Integer value, String nameInEnglish, String nameInBangla, double score) {
        this.name = name;
        this.value = value;
        this.nameInBangla = nameInBangla;
        this.nameInEnglish = nameInEnglish;
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    /**
     *
     * @param score
     */
    public void setScore(Double score) {
        this.score = score;
    }

    public String getName() {

        return StringModification.AddSpacesToSentence(name, true);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getNameInBangla() {
        return nameInBangla;
    }

    public void setNameInBangla(String nameInBangla) {
        this.nameInBangla = nameInBangla;
    }

    public String getNameInEnglish() {
        return nameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish) {
        this.nameInEnglish = nameInEnglish;
    }

}

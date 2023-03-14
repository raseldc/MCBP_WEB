/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class ItemObject implements Serializable {

    private Integer id;
    private String nameInBangla;
    private String nameInEnglish;

    private Integer accountNoLength;//only for bank

    public ItemObject(Integer id, String nameInBangla, String nameInEnglish) {
        this.id = id;
        this.nameInBangla = nameInBangla;
        this.nameInEnglish = nameInEnglish;
    }

    /**
     *
     * @param id
     * @param nameInBangla
     * @param nameInEnglish
     * @param accountNoLength
     */
    public ItemObject(Integer id, String nameInBangla, String nameInEnglish, Integer accountNoLength) {
        this.id = id;
        this.nameInBangla = nameInBangla;
        this.nameInEnglish = nameInEnglish;
        this.accountNoLength = accountNoLength;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getAccountNoLength() {
        return accountNoLength;
    }

    public void setAccountNoLength(Integer accountNoLength) {
        this.accountNoLength = accountNoLength;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.model;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.model.BaseModel;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author user
 */
@Table(name = "training_attendee")
@Entity
public class TrainingAttendee extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private Beneficiary beneficiary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @Transient
    private String nidEn;
    @Transient
    private String nidBn;

    @Transient
    private String mobileNoEn;
    @Transient
    private String mobileNoBn;

    /**
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Training getTraining() {
        return training;
    }

    /**
     *
     * @param training
     */
    public void setTraining(Training training) {
        this.training = training;
    }

    public String getNidEn() {
        return nidEn;
    }

    public void setNidEn(String nidEn) {
        this.nidEn = nidEn;
    }

    public String getNidBn() {
        return nidBn;
    }

    public void setNidBn(String nidBn) {
        this.nidBn = nidBn;
    }

    public String getMobileNoEn() {
        return mobileNoEn;
    }

    public void setMobileNoEn(String mobileNoEn) {
        this.mobileNoEn = mobileNoEn;
    }

    public String getMobileNoBn() {
        return mobileNoBn;
    }

    /**
     *
     * @param mobileNoBn
     */
    public void setMobileNoBn(String mobileNoBn) {
        this.mobileNoBn = mobileNoBn;
    }
    

}

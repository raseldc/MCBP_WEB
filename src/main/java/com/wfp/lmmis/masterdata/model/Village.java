/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.model;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "village")
public class Village extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "mediumint unsigned")
    private Integer id;

    @Size(min = 1, max = 255)
    @Column(name = "name_in_bangla", length = 255, unique = true, nullable = false)
    private String nameInBangla;

    @Size(min = 1, max = 255)
    @Column(name = "name_in_english", length = 255, unique = true, nullable = false)
    private String nameInEnglish;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "ward_no", nullable = false)
    private Integer wardNo;

    @Column(name = "active", columnDefinition = "bit(1)", nullable = false)
    private boolean active;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "union_id", nullable = false)
    private Union union;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upazila_id", nullable = false)
    private Upazilla upazila;

    @Transient
    private District district;

    @Transient
    private Division division;

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
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

    /**
     *
     * @param nameInBangla
     */
    public void setNameInBangla(String nameInBangla) {
        this.nameInBangla = nameInBangla;
    }

    /**
     *
     * @return
     */
    public String getNameInEnglish() {
        return nameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish) {
        this.nameInEnglish = nameInEnglish;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public boolean isActive() {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getWardNo() {
        return wardNo;
    }

    public void setWardNo(Integer wardNo) {
        this.wardNo = wardNo;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public Upazilla getUpazila() {
        return upazila;
    }

    public void setUpazila(Upazilla upazila) {
        this.upazila = upazila;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.model;

import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.types.UpazilaType;
import java.beans.Transient;
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
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "upazila")
public class Upazilla extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint(4) unsigned")
    private Integer id;

    @Size(min = 1, max = 255)
    @Column(name = "name_in_bangla", length = 255, nullable = false)
    private String nameInBangla;

    @Size(min = 1, max = 255)
    @Column(name = "name_in_english", length = 255, nullable = false)
    private String nameInEnglish;

    @Size(min = 2, max = 2)
    @Column(name = "code", columnDefinition = "tinyint(2) unsigned zerofill", nullable = false)
    private String code;

    @Column(name = "active", columnDefinition = "bit(1)", nullable = false)
    private boolean active;

    @Column(name = "is_deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

//    @ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @Column(name = "type", columnDefinition = "bit(1)", nullable = false)
    private UpazilaType upazilaType;

    @javax.persistence.Transient
    private Division division;

    /**
     *
     * @return
     */
    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

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

    /**
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @return
     */
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public District getDistrict() {
        return district;
    }

    /**
     *
     * @param district
     */
    public void setDistrict(District district) {
        this.district = district;
    }

    public UpazilaType getUpazilaType() {
        return upazilaType;
    }

    public void setUpazilaType(UpazilaType upazilaType) {
        this.upazilaType = upazilaType;
    }

}

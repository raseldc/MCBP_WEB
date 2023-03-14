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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "post_office_branch")
public class PostOfficeBranch extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint unsigned")
    private Integer id;

    @Size(min = 1, max = 255)
    @Column(name = "name_in_bangla", length = 255, unique = true, nullable = false)
    private String nameInBangla;

    @Size(min = 1, max = 255)
    @Column(name = "name_in_english", length = 255, unique = true, nullable = false)
    private String nameInEnglish;

    @Column(name = "code", columnDefinition = "char(16)", unique = true, nullable = false)
    private String code;

    @Size(min = 1, max = 511)
    @Column(name = "address", columnDefinition = "varchar(511)")
    private String address;

    @Column(name = "active", columnDefinition = "bit(1)", nullable = false)
    private boolean active;

    @Column(name = "is_deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

    @Column(name = "routing_number")
    private Integer routingNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;

    @Column(name = "account_no_length", nullable = false)
    private int accountNoLength;

    /**
     *
     * @return
     */
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

    /**
     *
     * @param nameInEnglish
     */
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

    /**
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    /**
     *
     * @param deleted
     */
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

    public Integer getRoutingNumber() {
        return routingNumber;
    }

    /**
     *
     * @param routingNumber
     */
    public void setRoutingNumber(Integer routingNumber) {
        this.routingNumber = routingNumber;
    }

    @Override
    public String toString() {
        return "PostOfficeBranch{" + "id=" + id + '}';
    }

    /**
     *
     * @return
     */
    public int getAccountNoLength() {
        return accountNoLength;
    }

    public void setAccountNoLength(int accountNoLength) {
        this.accountNoLength = accountNoLength;
    }
}

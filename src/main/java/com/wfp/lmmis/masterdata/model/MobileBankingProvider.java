/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.model;

import com.wfp.lmmis.model.BaseModel;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "mobile_banking_provider")
public class MobileBankingProvider extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "tinyint unsigned")
    private Integer id;

    @Size(min = 1, max = 255)
    @Column(name = "name_in_bangla", length = 255, unique = true, nullable = false)
    private String nameInBangla;

    @Size(min = 1, max = 255)
    @Column(name = "name_in_english", length = 255, unique = true, nullable = false)
    private String nameInEnglish;

    @Size(min = 1, max = 16)
    @Column(name = "code", columnDefinition = "char(4)", unique = true, nullable = false)
    private String code;

    @Column(name = "routing_number", columnDefinition = "bit(1) default false")
    private String routingNumber;

    @Column(name = "active", columnDefinition = "bit(1)", nullable = false)
    private boolean active;

    @Column(name = "is_deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

    @Column(name = "account_no_length", nullable = false)
    private int accountNoLength;

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
    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MobileBankingProvider other = (MobileBankingProvider) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public int getAccountNoLength() {
        return accountNoLength;
    }

    public void setAccountNoLength(int accountNoLength) {
        this.accountNoLength = accountNoLength;
    }
}

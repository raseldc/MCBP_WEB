/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author user
 */
@Entity
@Table(name = "notice")
public class Notice extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "notice_en")
    private String noticeEn;

    @Column(name = "notice_bn")
    private String noticeBn;

    @Column(name = "description")
    private String description;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "notice_date")
    private Calendar noticeDate;

    @Column(name = "active")
    private boolean active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoticeEn() {
        return noticeEn;
    }

    public void setNoticeEn(String noticeEn) {
        this.noticeEn = noticeEn;
    }

    public String getNoticeBn() {
        return noticeBn;
    }

    public void setNoticeBn(String noticeBn) {
        this.noticeBn = noticeBn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(Calendar noticeDate) {
        this.noticeDate = noticeDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

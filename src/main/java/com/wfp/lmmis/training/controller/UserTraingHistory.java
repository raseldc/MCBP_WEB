/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.controller;

import java.util.Date;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class UserTraingHistory {

    private Double completeness;
    private String start;
    private int status;
    private String remarks;
    private Integer atleastOneTimeLogin;
    private Date lastActiveDate;
    private Double marks;
    private Double exam_mark;
    private Double quiz_mark;
    private String certificateLink;
    private String isMailSent;
    private String certificate_download_link;
    private String tracking_code;
    private Integer enrollComplete;

    public Integer getEnrollComplete() {
        return enrollComplete;
    }

    /**
     *
     * @param enrollComplete
     */
    public void setEnrollComplete(Integer enrollComplete) {
        this.enrollComplete = enrollComplete;
    }

    public String getCertificate_download_link() {
        return certificate_download_link;
    }

    /**
     *
     * @param certificate_download_link
     */
    public void setCertificate_download_link(String certificate_download_link) {
        this.certificate_download_link = certificate_download_link;
    }

    /**
     *
     * @return
     */
    public String getTracking_code() {
        return tracking_code;
    }

    public void setTracking_code(String tracking_code) {
        this.tracking_code = tracking_code;
    }

    public Double getExam_mark() {
        return exam_mark;
    }

    public void setExam_mark(Double exam_mark) {
        this.exam_mark = exam_mark;
    }

    public Double getQuiz_mark() {
        return quiz_mark;
    }

    public void setQuiz_mark(Double quiz_mark) {
        this.quiz_mark = quiz_mark;
    }

    public Double getCompleteness() {
        return completeness;
    }

    public void setCompleteness(Double completeness) {
        this.completeness = completeness;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     *
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getAtleastOneTimeLogin() {
        return atleastOneTimeLogin;
    }

    public void setAtleastOneTimeLogin(Integer atleastOneTimeLogin) {
        this.atleastOneTimeLogin = atleastOneTimeLogin;
    }

    public Date getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(Date lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    public String getCertificateLink() {
        return certificateLink;
    }

    public void setCertificateLink(String certificateLink) {
        this.certificateLink = certificateLink;
    }

    public String getIsMailSent() {
        return isMailSent;
    }

    public void setIsMailSent(String isMailSent) {
        this.isMailSent = isMailSent;
    }

}

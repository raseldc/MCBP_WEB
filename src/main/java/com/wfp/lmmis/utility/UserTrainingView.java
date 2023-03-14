/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import java.util.Date;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class UserTrainingView {

    private int id;
    private String courseName;
    private String batchName;
    private String trainingName;
    private Date creationDate;
    private Integer createdBy;
    private Integer courseId;
    private Integer batchId;
    private int applicantCount;
    private int getCertificateCount;

    private String fiscalYearName;
    private int fiscalYearId;

    /**
     *
     * @return
     */
    public int getGetCertificateCount() {
        return getCertificateCount;
    }

    public void setGetCertificateCount(int getCertificateCount) {
        this.getCertificateCount = getCertificateCount;
    }

    /**
     *
     * @return
     */
    public String getFiscalYearName() {
        return fiscalYearName;
    }

    public void setFiscalYearName(String fiscalYearName) {
        this.fiscalYearName = fiscalYearName;
    }

    public int getFiscalYearId() {
        return fiscalYearId;
    }

    public void setFiscalYearId(int fiscalYearId) {
        this.fiscalYearId = fiscalYearId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     *
     * @return
     */
    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    /**
     *
     * @return
     */
    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public int getApplicantCount() {
        return applicantCount;
    }

    public void setApplicantCount(int applicantCount) {
        this.applicantCount = applicantCount;
    }

}

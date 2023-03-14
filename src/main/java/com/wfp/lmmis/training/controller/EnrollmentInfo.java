/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.controller;

import java.util.List;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class EnrollmentInfo {

    private List<Integer> userIds;
    private int courseId;
    private int batchId;
    private String courseName;
    private String batchName;
    private int fiscalYearId;
    private String restictedUrl;

    /**
     *
     * @return
     */
    public String getRestictedUrl() {
        return restictedUrl;
    }

    public void setRestictedUrl(String restictedUrl) {
        this.restictedUrl = restictedUrl;
    }

    public int getFiscalYearId() {
        return fiscalYearId;
    }

    public void setFiscalYearId(int fiscalYearId) {
        this.fiscalYearId = fiscalYearId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

}

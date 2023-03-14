/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserViewForTraining {

    private Integer id;
    private String userID;
    private String email;
    private String fullNameBn;
    private String fullNameEn;
    private String designation;
    private String mobileNo;
    private String nid;

    private Integer userTrainingId;
    private String courseName;
    private String batchName;
    private Integer courseId;
    private Integer batchId;

    private String name;
    private String phone;

    private int complete;
    private int status;
    private String remarks;

    private Integer userTrainingAttendanceId;

    private Integer atleastOneTimeLogin;
    private Date lastActiveDate;
    private double marks;
    private Integer getCertificate;
    private String certificateLink;

    private Integer isZoomMeeting;

    private int enrollComplete;

    private String restictedUrl;

    public String getRestictedUrl() {
        return restictedUrl;
    }

    public void setRestictedUrl(String restictedUrl) {
        this.restictedUrl = restictedUrl;
    }

    public Integer getIsZoomMeeting() {
        return isZoomMeeting;
    }

    public void setIsZoomMeeting(Integer isZoomMeeting) {
        this.isZoomMeeting = isZoomMeeting;
    }

    public int getEnrollComplete() {
        return enrollComplete;
    }

    public void setEnrollComplete(int enrollComplete) {
        this.enrollComplete = enrollComplete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getUserID() {
        return userID;
    }

    /**
     *
     * @param userID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getFullNameBn() {
        return fullNameBn;
    }

    public void setFullNameBn(String fullNameBn) {
        this.fullNameBn = fullNameBn;
    }

    public String getFullNameEn() {
        return fullNameEn;
    }

    public void setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public Integer getUserTrainingId() {
        return userTrainingId;
    }

    public void setUserTrainingId(Integer userTrainingId) {
        this.userTrainingId = userTrainingId;
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

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     */
    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getUserTrainingAttendanceId() {
        return userTrainingAttendanceId;
    }

    public void setUserTrainingAttendanceId(Integer userTrainingAttendanceId) {
        this.userTrainingAttendanceId = userTrainingAttendanceId;
    }

    /**
     *
     * @return
     */
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

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    /**
     *
     * @return
     */
    public Integer getGetCertificate() {
        return getCertificate;
    }

    public void setGetCertificate(Integer getCertificate) {
        this.getCertificate = getCertificate;
    }

    public String getCertificateLink() {
        return certificateLink;
    }

    /**
     *
     * @param certificateLink
     */
    public void setCertificateLink(String certificateLink) {
        this.certificateLink = certificateLink;
    }

}

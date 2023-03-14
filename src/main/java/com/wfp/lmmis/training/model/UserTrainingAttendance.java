package com.wfp.lmmis.training.model;
// Generated Jan 5, 2021 1:20:21 AM by Hibernate Tools 4.3.1

import com.wfp.lmmis.rm.model.User;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * UserTrainingAttendance generated by hbm2java
 */
@Entity
@Table(name = "user_training_attendance",
        catalog = "imlma"
)
public class UserTrainingAttendance implements java.io.Serializable {

    private Integer id;
    private User user;
    private UserTraining userTraining;
    private String certificateLink;
    private Integer getCertificate;
    private Double complepte;
    private Double marks;
    private String remarks;
    private String journyStatus;
    private Integer atleastOneTimeLogin;
    private Integer enrollComplete;
    private Date lastActivieDate;
    private Integer isActive;

    private Integer isZoomMeeting;

    public UserTrainingAttendance() {
    }

    public UserTrainingAttendance(User user, UserTraining userTraining) {
        this.user = user;
        this.userTraining = userTraining;
    }

    public UserTrainingAttendance(User user, UserTraining userTraining, String certificateLink, Integer getCertificate, Double complepte, Double marks, String remarks, String journyStatus, Integer isLogin, Date lastActivieDate) {
        this.user = user;
        this.userTraining = userTraining;
        this.certificateLink = certificateLink;
        this.getCertificate = getCertificate;
        this.complepte = complepte;
        this.marks = marks;
        this.remarks = remarks;
        this.journyStatus = journyStatus;
        this.atleastOneTimeLogin = isLogin;
        this.lastActivieDate = lastActivieDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_training_id", nullable = false)
    public UserTraining getUserTraining() {
        return this.userTraining;
    }

    public void setUserTraining(UserTraining userTraining) {
        this.userTraining = userTraining;
    }

    @Column(name = "certificate_link", length = 1000)
    public String getCertificateLink() {
        return this.certificateLink;
    }

    public void setCertificateLink(String certificateLink) {
        this.certificateLink = certificateLink;
    }

    /**
     *
     * @return
     */
    @Column(name = "getCertificate")
    public Integer getGetCertificate() {
        return this.getCertificate;
    }

    public void setGetCertificate(Integer getCertificate) {
        this.getCertificate = getCertificate;
    }

    @Column(name = "isZoomMeeting")
    public Integer getIsZoomMeeting() {
        return isZoomMeeting;
    }

    public void setIsZoomMeeting(Integer isZoomMeeting) {
        this.isZoomMeeting = isZoomMeeting;
    }

    @Column(name = "complepte")
    public Double getComplepte() {
        return this.complepte;
    }

    public void setComplepte(Double complepte) {
        this.complepte = complepte;
    }

    /**
     *
     * @return
     */
    @Column(name = "marks")
    public Double getMarks() {
        return this.marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    @Column(name = "remarks", length = 10000)
    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Column(name = "journyStatus", length = 65535)
    public String getJournyStatus() {
        return this.journyStatus;
    }

    public void setJournyStatus(String journyStatus) {
        this.journyStatus = journyStatus;
    }

    @Column(name = "atleastOneTimeLogin")
    public Integer getAtleastOneTimeLogin() {
        return this.atleastOneTimeLogin;
    }

    public void setAtleastOneTimeLogin(Integer isLogin) {
        this.atleastOneTimeLogin = isLogin;
    }

    /**
     *
     * @return
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "lastActivieDate", length = 10)
    public Date getLastActivieDate() {
        return this.lastActivieDate;
    }

    public void setLastActivieDate(Date lastActivieDate) {
        this.lastActivieDate = lastActivieDate;
    }

    @Column(name = "enrollComplete")
    public Integer getEnrollComplete() {
        return enrollComplete;
    }

    public void setEnrollComplete(Integer enrollComplete) {
        this.enrollComplete = enrollComplete;
    }

    @Column(name = "isActive")
    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

}

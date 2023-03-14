package com.wfp.lmmis.beneficiary.model;
// Generated May 16, 2021 7:47:19 PM by Hibernate Tools 4.3.1

import com.wfp.lmmis.rm.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.persistence.Transient;
import org.springframework.web.multipart.MultipartFile;

/**
 * BeneficiaryChildInformation generated by hbm2java
 */
@Entity
@Table(name = "beneficiary_child_information",
        catalog = "imlma"
)
public class BeneficiaryChildInformation implements java.io.Serializable {

    private Integer id;
    private Beneficiary beneficiary;
    private User userByModifiedBy;
    private User userByCreatedBy;
    private String childName;
    private String childBirthCertificate;
    private Date childDob;
    private Integer childNo;
    private Integer isAlive;
    private Date deathDate;
    private String deathReason;
    private Date creationDate;
    private Date modificationDate;
    private String attachedFileLocation;

    private MultipartFile file;

    private int benId;
    private String dob_st;

    private String base64;

    private String childNoSt;

    public BeneficiaryChildInformation() {
    }

    public BeneficiaryChildInformation(Beneficiary beneficiary, User userByCreatedBy, Date creationDate) {
        this.beneficiary = beneficiary;
        this.userByCreatedBy = userByCreatedBy;
        this.creationDate = creationDate;
    }

    public BeneficiaryChildInformation(Beneficiary beneficiary, User userByModifiedBy, User userByCreatedBy, String childName, String childBirthCertificate, Date childDob, Integer childNo, Integer isAlive, Date deathDate, String deathReason, Date creationDate, Date modificationDate, String attachedFileLocation) {
        this.beneficiary = beneficiary;
        this.userByModifiedBy = userByModifiedBy;
        this.userByCreatedBy = userByCreatedBy;
        this.childName = childName;
        this.childBirthCertificate = childBirthCertificate;
        this.childDob = childDob;
        this.childNo = childNo;
        this.isAlive = isAlive;
        this.deathDate = deathDate;
        this.deathReason = deathReason;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.attachedFileLocation = attachedFileLocation;
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
    @JoinColumn(name = "ben_id", nullable = false)
    public Beneficiary getBeneficiary() {
        return this.beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    public User getUserByModifiedBy() {
        return this.userByModifiedBy;
    }

    public void setUserByModifiedBy(User userByModifiedBy) {
        this.userByModifiedBy = userByModifiedBy;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    public User getUserByCreatedBy() {
        return this.userByCreatedBy;
    }

    public void setUserByCreatedBy(User userByCreatedBy) {
        this.userByCreatedBy = userByCreatedBy;
    }

    @Column(name = "child_name", length = 50)
    public String getChildName() {
        return this.childName;
    }

    /**
     *
     * @param childName
     */
    public void setChildName(String childName) {
        this.childName = childName;
    }

    /**
     *
     * @return
     */
    @Column(name = "child_birth_certificate", length = 50)
    public String getChildBirthCertificate() {
        return this.childBirthCertificate;
    }

    public void setChildBirthCertificate(String childBirthCertificate) {
        this.childBirthCertificate = childBirthCertificate;
    }

    /**
     *
     * @return
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "child_dob", length = 10)
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "dd-MM-yyyy")
    public Date getChildDob() {
        return this.childDob;
    }

    public void setChildDob(Date childDob) {
        this.childDob = childDob;
    }

    @Column(name = "child_no")
    public Integer getChildNo() {
        return this.childNo;
    }

    public void setChildNo(Integer childNo) {
        this.childNo = childNo;
    }

    /**
     *
     * @return
     */
    @Column(name = "is_alive")
    public Integer getIsAlive() {
        return this.isAlive;
    }

    public void setIsAlive(Integer isAlive) {
        this.isAlive = isAlive;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "death_date", length = 10)
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "dd-MM-yyyy")
    public Date getDeathDate() {
        return this.deathDate;
    }

    /**
     *
     * @param deathDate
     */
    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    @Column(name = "death_reason", length = 50)
    public String getDeathReason() {
        return this.deathReason;
    }

    /**
     *
     * @param deathReason
     */
    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, length = 19)
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "dd-MM-yyyy")
    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modification_date", length = 19)
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "dd-MM-yyyy")
    public Date getModificationDate() {
        return this.modificationDate;
    }

    /**
     *
     * @param modificationDate
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Column(name = "attached_file_location", length = 1000)
    public String getAttachedFileLocation() {
        return this.attachedFileLocation;
    }

    public void setAttachedFileLocation(String attachedFileLocation) {
        this.attachedFileLocation = attachedFileLocation;
    }

    @Transient
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    /**
     *
     * @return
     */
    @Transient
    public int getBenId() {
        return benId;
    }

    /**
     *
     * @param benId
     */
    public void setBenId(int benId) {
        this.benId = benId;
    }

    @Transient
    public String getDob_st() {
        return dob_st;
    }

    public void setDob_st(String dob_st) {
        this.dob_st = dob_st;
    }

    @Transient
    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @Transient
    public String getChildNoSt() {
        return childNoSt;
    }

    public void setChildNoSt(String childNoSt) {
        this.childNoSt = childNoSt;
    }

}

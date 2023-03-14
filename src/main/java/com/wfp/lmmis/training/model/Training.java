/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.model;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.payroll.model.FiscalYear;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Philip
 */
@Table(name = "training")
@Entity
public class Training extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int unsigned")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fiscal_year_id", nullable = false)
    private FiscalYear fiscalYear;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType trainingType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(name = "number_of_perticipants", nullable = false)
    private Integer numberOfPerticipants;

    @Column(name = "head_office", nullable = true)
    private Boolean headOffice;

    @ManyToOne()
    @JoinColumn(name = "division_id", nullable = true)
    private Division division;

    @ManyToOne()
    @JoinColumn(name = "district_id", nullable = true)
    private District district;

    @ManyToOne()
    @JoinColumn(name = "upazilla_id", nullable = true)
    private Upazilla upazilla;

    @ManyToOne()
    @JoinColumn(name = "union_id", nullable = true)
    private Union union;

    @Column(name = "training_venue")
    private String trainingVenue;

    @Column(name = "training_cost")
    private Float trainingCost;

    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar startDate;

    @Column(name = "end_date", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Calendar endDate;

    @Column(name = "duration_day", nullable = false)
    private Integer durationDay;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "applicant_type", columnDefinition = "bit(2)", nullable = false)
    private ApplicantType applicantType;

    @Column(name = "deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;
    @Column(name = "file_base64", nullable = true)
    private String fileBase64;

    @Column(name = "file_extension", nullable = false)
    private String fileExtension;

    @Transient
    private boolean divisionAvailable;

    @Transient
    private boolean districtAvailable;

    @Transient
    private boolean upazilaAvailable;

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
    public FiscalYear getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    /**
     *
     * @param trainer
     */
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Integer getNumberOfPerticipants() {
        return numberOfPerticipants;
    }

    public void setNumberOfPerticipants(Integer numberOfPerticipants) {
        this.numberOfPerticipants = numberOfPerticipants;
    }

    /**
     *
     * @return
     */
    public Integer getDurationDay() {
        return durationDay;
    }

    public void setDurationDay(Integer durationDay) {
        this.durationDay = durationDay;
    }

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
    public Boolean getHeadOffice() {
        return headOffice;
    }

    public void setHeadOffice(Boolean headOffice) {
        this.headOffice = headOffice;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Upazilla getUpazilla() {
        return upazilla;
    }

    public void setUpazilla(Upazilla upazilla) {
        this.upazilla = upazilla;
    }

    /**
     *
     * @return
     */
    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public String getTrainingVenue() {
        return trainingVenue;
    }

    /**
     *
     * @param trainingVenue
     */
    public void setTrainingVenue(String trainingVenue) {
        this.trainingVenue = trainingVenue;
    }

    /**
     *
     * @return
     */
    public Float getTrainingCost() {
        return trainingCost;
    }

    public void setTrainingCost(Float trainingCost) {
        this.trainingCost = trainingCost;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDivisionAvailable() {
        return divisionAvailable;
    }

    /**
     *
     * @param divisionAvailable
     */
    public void setDivisionAvailable(boolean divisionAvailable) {
        this.divisionAvailable = divisionAvailable;
    }

    public boolean isDistrictAvailable() {
        return districtAvailable;
    }

    /**
     *
     * @param districtAvailable
     */
    public void setDistrictAvailable(boolean districtAvailable) {
        this.districtAvailable = districtAvailable;
    }

    public boolean isUpazilaAvailable() {
        return upazilaAvailable;
    }

    public void setUpazilaAvailable(boolean upazilaAvailable) {
        this.upazilaAvailable = upazilaAvailable;
    }

    /**
     *
     * @return
     */
    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        final Training other = (Training) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.wfp.lmmis.training.model.Training[ id=" + id + " ]";
    }

}

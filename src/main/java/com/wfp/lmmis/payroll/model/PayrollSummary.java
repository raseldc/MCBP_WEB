package com.wfp.lmmis.payroll.model;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.PayrollListType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.types.PayrollStatus;
import java.io.Serializable;
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

@Entity
@Table(name = "payroll_summary")
public class PayrollSummary extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_cycle_id", nullable = false)
    private PaymentCycle paymentCycle;

    @Column(name = "allowance_per_beneficiary", nullable = false)
    private float allowancePerBeneficiary;

    @Column(name = "total_beneficiary", nullable = false)
    private int totalBeneficiary;

    @Column(name = "total_allowance", nullable = false)
    private double totalAllowance;

    @Column(name = "payroll_status", nullable = true)
    private PayrollStatus payrollStatus;

    // division, district, upazila has been made nullable to incorporate LMA
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "division_id", nullable = true)
    private Division division;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id", nullable = true)
    private District district;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "upazila_id", nullable = true)
    private Upazilla upazilla;

    @Column(name = "approval_comments", nullable = true)
    private String approvalComments;

    @Column(name = "applicant_type", columnDefinition = "bit(3)", nullable = false)
    private ApplicantType applicantType;

    @Column(name = "payroll_list_type", columnDefinition = "bit(3)", nullable = false)
    private PayrollListType payrollListType;

    @Column(name = "is_exported_to_spbmu")
    private boolean isExportedToSpbmu;

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
    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public PaymentCycle getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(PaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public float getAllowancePerBeneficiary() {
        return allowancePerBeneficiary;
    }

    /**
     *
     * @param allowancePerBeneficiary
     */
    public void setAllowancePerBeneficiary(float allowancePerBeneficiary) {
        this.allowancePerBeneficiary = allowancePerBeneficiary;
    }

    public int getTotalBeneficiary() {
        return totalBeneficiary;
    }

    public void setTotalBeneficiary(int totalBeneficiary) {
        this.totalBeneficiary = totalBeneficiary;
    }

    public double getTotalAllowance() {
        return totalAllowance;
    }

    public void setTotalAllowance(double totalAllowance) {
        this.totalAllowance = totalAllowance;
    }

    /**
     *
     * @return
     */
    public PayrollStatus getPayrollStatus() {
        return payrollStatus;
    }

    public void setPayrollStatus(PayrollStatus payrollStatus) {
        this.payrollStatus = payrollStatus;
    }

    public Division getDivision() {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(Division division) {
        this.division = division;
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

    public String getApprovalComments() {
        return approvalComments;
    }

    /**
     *
     * @param approvalComments
     */
    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
    }

    public ApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public PayrollListType getPayrollListType() {
        return payrollListType;
    }

    public void setPayrollListType(PayrollListType payrollListType) {
        this.payrollListType = payrollListType;
    }

    /**
     *
     * @return
     */
    public boolean isIsExportedToSpbmu() {
        return isExportedToSpbmu;
    }

    public void setIsExportedToSpbmu(boolean isExportedToSpbmu) {
        this.isExportedToSpbmu = isExportedToSpbmu;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 5;
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
        final PayrollSummary other = (PayrollSummary) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}

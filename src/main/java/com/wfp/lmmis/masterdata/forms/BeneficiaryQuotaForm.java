/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.forms;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.masterdata.model.BenQuota;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.payroll.model.FiscalYear;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Philip
 */
public class BeneficiaryQuotaForm
{

    private ApplicantType applicantType;
    private List<BenQuota> benQuotaList;

    @NotNull
    private Division division;
    @NotNull
    private District district;
    @NotNull
    private Upazilla upazila;
    @NotBlank
    private Scheme scheme;
    @NotNull
    private FiscalYear fiscalYear;

    public Division getDivision()
    {
        return division;
    }

    public void setDivision(Division division)
    {
        this.division = division;
    }

    public District getDistrict()
    {
        return district;
    }

    public void setDistrict(District district)
    {
        this.district = district;
    }

    public Upazilla getUpazila()
    {
        return upazila;
    }

    public void setUpazila(Upazilla upazila)
    {
        this.upazila = upazila;
    }

    public Scheme getScheme()
    {
        return scheme;
    }

    public void setScheme(Scheme scheme)
    {
        this.scheme = scheme;
    }

    public List<BenQuota> getBenQuotaList()
    {
        return benQuotaList;
    }

    public void setBenQuotaList(List<BenQuota> benQuotaList)
    {
        this.benQuotaList = benQuotaList;
    }

    /**
     *
     * @return
     */
    public FiscalYear getFiscalYear()
    {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear)
    {
        this.fiscalYear = fiscalYear;
    }

    public ApplicantType getApplicantType()
    {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType)
    {
        this.applicantType = applicantType;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.forms;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.model.ApplicantBasicInfo;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author PCUser
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AddressForm extends ApplicantBasicInfo implements Serializable
{

//    private Integer id;
//    private FiscalYear fiscalYear;
    private ApplicantType applicantType;
    private Factory bgmeaFactory;
    private Factory bkmeaFactory;

    private String permanentAddressLine1;
    private String permanentAddressLine2;
    @NotNull
    private Division permanentDivision;
    @NotNull
    private District permanentDistrict;
    @NotNull
    private Upazilla permanentUpazila;
    @NotNull
    private Union permanentUnion;
    private String permanentWardNo;
    @NotEmpty
    @Size(min = 4, max = 4)
    private String permanentPostCode;

    private String presentAddressLine1;
    private String presentAddressLine2;
    @NotNull
    private Division presentDivision;
    @NotNull
    private District presentDistrict;
    @NotNull
    private Upazilla presentUpazila;
    @NotNull
    private Union presentUnion;
    private String presentWardNo;
    @Size(min = 4, max = 4)
    private String presentPostCode;

//    public Integer getId()
//    {
//        return id;
//    }
//
//    public void setId(Integer id)
//    {
//        this.id = id;
//    }
//
//    public FiscalYear getFiscalYear()
//    {
//        return fiscalYear;
//    }
//
//    public void setFiscalYear(FiscalYear fiscalYear)
//    {
//        this.fiscalYear = fiscalYear;
//    }
    public String getPermanentAddressLine1()
    {
        return permanentAddressLine1;
    }

    public void setPermanentAddressLine1(String permanentAddressLine1)
    {
        this.permanentAddressLine1 = permanentAddressLine1;
    }

    public String getPermanentAddressLine2()
    {
        return permanentAddressLine2;
    }

    public void setPermanentAddressLine2(String permanentAddressLine2)
    {
        this.permanentAddressLine2 = permanentAddressLine2;
    }

    public Division getPermanentDivision()
    {
        return permanentDivision;
    }

    public void setPermanentDivision(Division permanentDivision)
    {
        this.permanentDivision = permanentDivision;
    }

    /**
     *
     * @return
     */
    public District getPermanentDistrict()
    {
        return permanentDistrict;
    }

    public void setPermanentDistrict(District permanentDistrict)
    {
        this.permanentDistrict = permanentDistrict;
    }

    /**
     *
     * @return
     */
    public Upazilla getPermanentUpazila()
    {
        return permanentUpazila;
    }

    public void setPermanentUpazila(Upazilla permanentUpazila)
    {
        this.permanentUpazila = permanentUpazila;
    }

    /**
     *
     * @return
     */
    public Union getPermanentUnion()
    {
        return permanentUnion;
    }

    public void setPermanentUnion(Union permanentUnion)
    {
        this.permanentUnion = permanentUnion;
    }

    public String getPermanentWardNo()
    {
        return permanentWardNo;
    }

    public void setPermanentWardNo(String permanentWardNo)
    {
        this.permanentWardNo = permanentWardNo;
    }

    public String getPermanentPostCode()
    {
        return permanentPostCode;
    }

    public void setPermanentPostCode(String permanentPostCode)
    {
        this.permanentPostCode = permanentPostCode;
    }

    public String getPresentAddressLine1()
    {
        return presentAddressLine1;
    }

    public void setPresentAddressLine1(String presentAddressLine1)
    {
        this.presentAddressLine1 = presentAddressLine1;
    }

    public String getPresentAddressLine2()
    {
        return presentAddressLine2;
    }

    public void setPresentAddressLine2(String presentAddressLine2)
    {
        this.presentAddressLine2 = presentAddressLine2;
    }

    public Division getPresentDivision()
    {
        return presentDivision;
    }

    public void setPresentDivision(Division presentDivision)
    {
        this.presentDivision = presentDivision;
    }

    /**
     *
     * @return
     */
    public District getPresentDistrict()
    {
        return presentDistrict;
    }

    public void setPresentDistrict(District presentDistrict)
    {
        this.presentDistrict = presentDistrict;
    }

    public Upazilla getPresentUpazila()
    {
        return presentUpazila;
    }

    public void setPresentUpazila(Upazilla presentUpazila)
    {
        this.presentUpazila = presentUpazila;
    }

    public Union getPresentUnion()
    {
        return presentUnion;
    }

    /**
     *
     * @param presentUnion
     */
    public void setPresentUnion(Union presentUnion)
    {
        this.presentUnion = presentUnion;
    }

    /**
     *
     * @return
     */
    public String getPresentWardNo()
    {
        return presentWardNo;
    }

    public void setPresentWardNo(String presentWardNo)
    {
        this.presentWardNo = presentWardNo;
    }

    public String getPresentPostCode()
    {
        return presentPostCode;
    }

    public void setPresentPostCode(String presentPostCode)
    {
        this.presentPostCode = presentPostCode;
    }

    public ApplicantType getApplicantType()
    {
        return applicantType;
    }

    /**
     *
     * @param applicantType
     */
    public void setApplicantType(ApplicantType applicantType)
    {
        this.applicantType = applicantType;
    }

    public Factory getBgmeaFactory()
    {
        return bgmeaFactory;
    }

    public void setBgmeaFactory(Factory bgmeaFactory)
    {
        this.bgmeaFactory = bgmeaFactory;
    }

    public Factory getBkmeaFactory()
    {
        return bkmeaFactory;
    }

    public void setBkmeaFactory(Factory bkmeaFactory)
    {
        this.bkmeaFactory = bkmeaFactory;
    }

    

}

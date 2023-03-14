/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.grievance.controller;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.grievance.model.GrievanceStatus;
import com.wfp.lmmis.grievance.model.GrievanceType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;

/**
 *
 * @author Philip
 */
public class GrievanceSearchParameterForm
{

    private String nid;
    private ApplicantType applicantType;
    private GrievanceType grievanceType;
    private GrievanceStatus grievanceStatus;
    private Factory bgmeaFactory;
    private Factory bkmeaFactory;
    private boolean isDivisionAvailable;
    private Division division;
    private boolean isDistrictAvailable;
    private District district;
    private boolean isUpazilaAvailable;
    private Upazilla upazila;
    private boolean isUnionAvailable;
    private Union union;
    private boolean isBgmeaFactoryAvailable;
    private boolean isBkmeaFactoryAvailable;

    public String getNid()
    {
        return nid;
    }

    public void setNid(String nid)
    {
        this.nid = nid;
    }

    public ApplicantType getApplicantType()
    {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType)
    {
        this.applicantType = applicantType;
    }

    public GrievanceType getGrievanceType()
    {
        return grievanceType;
    }

    /**
     *
     * @param grievanceType
     */
    public void setGrievanceType(GrievanceType grievanceType)
    {
        this.grievanceType = grievanceType;
    }

    public GrievanceStatus getGrievanceStatus()
    {
        return grievanceStatus;
    }

    /**
     *
     * @param grievanceStatus
     */
    public void setGrievanceStatus(GrievanceStatus grievanceStatus)
    {
        this.grievanceStatus = grievanceStatus;
    }

    public boolean isIsDivisionAvailable()
    {
        return isDivisionAvailable;
    }

    public void setIsDivisionAvailable(boolean isDivisionAvailable)
    {
        this.isDivisionAvailable = isDivisionAvailable;
    }

    public Division getDivision()
    {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(Division division)
    {
        this.division = division;
    }

    public boolean isIsDistrictAvailable()
    {
        return isDistrictAvailable;
    }

    public void setIsDistrictAvailable(boolean isDistrictAvailable)
    {
        this.isDistrictAvailable = isDistrictAvailable;
    }

    public District getDistrict()
    {
        return district;
    }

    public void setDistrict(District district)
    {
        this.district = district;
    }

    public boolean isIsUpazilaAvailable()
    {
        return isUpazilaAvailable;
    }

    public void setIsUpazilaAvailable(boolean isUpazilaAvailable)
    {
        this.isUpazilaAvailable = isUpazilaAvailable;
    }

    public Upazilla getUpazila()
    {
        return upazila;
    }

    public void setUpazila(Upazilla upazila)
    {
        this.upazila = upazila;
    }

    public boolean isIsUnionAvailable()
    {
        return isUnionAvailable;
    }

    /**
     *
     * @param isUnionAvailable
     */
    public void setIsUnionAvailable(boolean isUnionAvailable)
    {
        this.isUnionAvailable = isUnionAvailable;
    }

    public Union getUnion()
    {
        return union;
    }

    public void setUnion(Union union)
    {
        this.union = union;
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @param bkmeaFactory
     */
    public void setBkmeaFactory(Factory bkmeaFactory)
    {
        this.bkmeaFactory = bkmeaFactory;
    }

    /**
     *
     * @return
     */
    public boolean isIsBgmeaFactoryAvailable()
    {
        return isBgmeaFactoryAvailable;
    }

    public void setIsBgmeaFactoryAvailable(boolean isBgmeaFactoryAvailable)
    {
        this.isBgmeaFactoryAvailable = isBgmeaFactoryAvailable;
    }

    public boolean isIsBkmeaFactoryAvailable()
    {
        return isBkmeaFactoryAvailable;
    }

    public void setIsBkmeaFactoryAvailable(boolean isBkmeaFactoryAvailable)
    {
        this.isBkmeaFactoryAvailable = isBkmeaFactoryAvailable;
    }

}

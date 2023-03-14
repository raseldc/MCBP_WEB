/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.rm.controller;

import com.wfp.lmmis.enums.UserStatus;
import com.wfp.lmmis.enums.UserType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;

/**
 *
 * @author Philip
 */
public class UserSearchParameterForm
{

    private String userID;
    private UserType userType;
    private UserStatus userStatus;
    private Division division;
    private District district;
    private Upazilla upazilla;
    private Union union;
    private Scheme scheme;

    public String getUserID()
    {
        return userID;
    }

    /**
     *
     * @param userID
     */
    public void setUserID(String userID)
    {
        this.userID = userID;
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

    public District getDistrict()
    {
        return district;
    }

    /**
     *
     * @param district
     */
    public void setDistrict(District district)
    {
        this.district = district;
    }

    public Upazilla getUpazilla()
    {
        return upazilla;
    }

    /**
     *
     * @param upazilla
     */
    public void setUpazilla(Upazilla upazilla)
    {
        this.upazilla = upazilla;
    }

    /**
     *
     * @return
     */
    public Union getUnion()
    {
        return union;
    }

    /**
     *
     * @param union
     */
    public void setUnion(Union union)
    {
        this.union = union;
    }

    public UserType getUserType()
    {
        return userType;
    }

    public void setUserType(UserType userType)
    {
        this.userType = userType;
    }

    public UserStatus getUserStatus()
    {
        return userStatus;
    }

    /**
     *
     * @param userStatus
     */
    public void setUserStatus(UserStatus userStatus)
    {
        this.userStatus = userStatus;
    }

    /**
     *
     * @return
     */
    public Scheme getScheme()
    {
        return scheme;
    }

    public void setScheme(Scheme scheme)
    {
        this.scheme = scheme;
    }
    

}

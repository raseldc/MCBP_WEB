/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.model;

import com.wfp.lmmis.enums.UserType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.payroll.model.FiscalYear;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Shamiul Islam
 */
public class UserDetail {

    private int id;
    private String userID;
    private String userNameBn;
    private String userNameEn;
    private String designation;
    private Integer userId;
    private String schemeShortName;
    private String schemeNameBn;
    private String schemeNameEn;
    private Integer schemeId;
    private Scheme scheme;
    private FiscalYear activeFiscalYear;
    private UserType userType;
    private Division division;
    private District district;
    private Upazilla upazila;
    private Integer roleId;
    private Union union;
    private Factory bgmeaFactory;
    private Factory bkmeaFactory;
    private String email;
    private String mobile;
    private String profilePicPath;
    private boolean active;
    private Integer status;
    private List<UserSchemeDetail> userSchemeDetailList;

    private String passeword;
    private String salt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPasseword() {
        return passeword;
    }

    public void setPasseword(String passeword) {
        this.passeword = passeword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getRoleId() {
        return roleId;
    }

    /**
     *
     * @param roleId
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserNameBn() {
        return userNameBn;
    }

    public void setUserNameBn(String userNameBn) {
        this.userNameBn = userNameBn;
    }

    public String getUserNameEn() {
        return userNameEn;
    }

    public void setUserNameEn(String userNameEn) {
        this.userNameEn = userNameEn;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSchemeShortName() {
        return schemeShortName;
    }

    public void setSchemeShortName(String schemeShortName) {
        this.schemeShortName = schemeShortName;
    }

    /**
     *
     * @return
     */
    public String getSchemeNameBn() {
        return schemeNameBn;
    }

    public void setSchemeNameBn(String schemeNameBn) {
        this.schemeNameBn = schemeNameBn;
    }

    public String getSchemeNameEn() {
        return schemeNameEn;
    }

    public void setSchemeNameEn(String schemeNameEn) {
        this.schemeNameEn = schemeNameEn;
    }

    public Integer getSchemeId() {
        return schemeId;
    }

    /**
     *
     * @param schemeId
     */
    public void setSchemeId(Integer schemeId) {
        this.schemeId = schemeId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Upazilla getUpazila() {
        return upazila;
    }

    public void setUpazila(Upazilla upazila) {
        this.upazila = upazila;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public Scheme getScheme() {
        return scheme;
    }

    /**
     *
     * @param scheme
     */
    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public FiscalYear getActiveFiscalYear() {
        return activeFiscalYear;
    }

    /**
     *
     * @param activeFiscalYear
     */
    public void setActiveFiscalYear(FiscalYear activeFiscalYear) {
        this.activeFiscalYear = activeFiscalYear;
    }

    public List<UserSchemeDetail> getUserSchemeDetailList() {
        return userSchemeDetailList;
    }

    public void setUserSchemeDetailList(List<UserSchemeDetail> userSchemeDetailList) {
        this.userSchemeDetailList = userSchemeDetailList;
    }

    public Factory getBgmeaFactory() {
        return bgmeaFactory;
    }

    public void setBgmeaFactory(Factory bgmeaFactory) {
        this.bgmeaFactory = bgmeaFactory;
    }

    public Factory getBkmeaFactory() {
        return bkmeaFactory;
    }

    /**
     *
     * @param bkmeaFactory
     */
    public void setBkmeaFactory(Factory bkmeaFactory) {
        this.bkmeaFactory = bkmeaFactory;
    }

    /**
     *
     * @return
     */
    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public boolean isActive() {
        return active;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.userId);
        return hash;
    }

    /**
     *
     * @param obj
     * @return
     */
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
        final UserDetail other = (UserDetail) obj;
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserDetail{" + "userID=" + userID + '}';
    }

}

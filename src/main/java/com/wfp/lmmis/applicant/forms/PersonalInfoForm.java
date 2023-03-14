/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.forms;

import com.wfp.lmmis.enums.EducationLevelEnum;
import com.wfp.lmmis.enums.MaritalInfoEnum;
import com.wfp.lmmis.enums.ReligionEnum;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.model.ApplicantBasicInfo;
import com.wfp.lmmis.types.BloodGroup;
import com.wfp.lmmis.types.Gender;
import java.io.Serializable;
import java.util.Calendar;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PCUser
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PersonalInfoForm extends ApplicantBasicInfo implements Serializable
{

    private String fullNameInBangla;
    private String fullNameInEnglish;
    private String nickName;
    @NotBlank
    @Size(min = 10, max = 17)
    private String nid;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Calendar dateOfBirth;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private Gender gender;
//    private EducationLevel educationLevel;
    private EducationLevelEnum educationLevelEnum;
    //private Religion religion;
    private ReligionEnum religionEnum;
    // private MaritalInfo maritalInfo;
    private MaritalInfoEnum maritalInfoEnum;
    private BloodGroup bloodGroup;
    @Size(min = 11, max = 11)
    private String mobileNo;
    @Email
    private String email;
    private boolean nrb;
    private Boolean beneficiaryInOtherScheme;

//    private String applicationID;
    private String profilePhotoPath;
    private MultipartFile profilePhoto;
    private String signaturePath;

    @NotNull
    private District birthPlace;

    /**
     *
     * @return
     */
    public String getFullNameInBangla()
    {
        return fullNameInBangla;
    }

    public void setFullNameInBangla(String fullNameInBangla)
    {
        this.fullNameInBangla = fullNameInBangla;
    }

    /**
     *
     * @return
     */
    public String getFullNameInEnglish()
    {
        return fullNameInEnglish;
    }

    public void setFullNameInEnglish(String fullNameInEnglish)
    {
        this.fullNameInEnglish = fullNameInEnglish;
    }

    /**
     *
     * @return
     */
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    /**
     *
     * @return
     */
    public String getNid()
    {
        return nid;
    }

    /**
     *
     * @param nid
     */
    public void setNid(String nid)
    {
        this.nid = nid;
    }

    public Calendar getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFatherName()
    {
        return fatherName;
    }

    public void setFatherName(String fatherName)
    {
        this.fatherName = fatherName;
    }

    public String getMotherName()
    {
        return motherName;
    }

    public void setMotherName(String motherName)
    {
        this.motherName = motherName;
    }

    public String getSpouseName()
    {
        return spouseName;
    }

    public void setSpouseName(String spouseName)
    {
        this.spouseName = spouseName;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

//    public EducationLevel getEducationLevel()
//    {
//        return educationLevel;
//    }
//
//    public void setEducationLevel(EducationLevel educationLevel)
//    {
//        this.educationLevel = educationLevel;
//    }

    /**
     *
     * @return
     */
    public EducationLevelEnum getEducationLevelEnum()
    {
        return educationLevelEnum;
    }

    public void setEducationLevelEnum(EducationLevelEnum educationLevelEnum)
    {
        this.educationLevelEnum = educationLevelEnum;
    }

//    public Religion getReligion()
//    {
//        return religion;
//    }
//
//    public void setReligion(Religion religion)
//    {
//        this.religion = religion;
//    }
    public ReligionEnum getReligionEnum()
    {
        return religionEnum;
    }

    public void setReligionEnum(ReligionEnum religionEnum)
    {
        this.religionEnum = religionEnum;
    }

    public MaritalInfoEnum getMaritalInfoEnum()
    {
        return maritalInfoEnum;
    }

    public void setMaritalInfoEnum(MaritalInfoEnum maritalInfoEnum)
    {
        this.maritalInfoEnum = maritalInfoEnum;
    }

//    public MaritalInfo getMaritalInfo()
//    {
//        return maritalInfo;
//    }
//
//    public void setMaritalInfo(MaritalInfo maritalInfo)
//    {
//        this.maritalInfo = maritalInfo;
//    }
    public BloodGroup getBloodGroup()
    {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup)
    {
        this.bloodGroup = bloodGroup;
    }

    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        this.mobileNo = mobileNo;
    }

    public String getEmail()
    {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean getNrb()
    {
        return nrb;
    }

    public void setNrb(boolean nrb)
    {
        this.nrb = nrb;
    }

    /**
     *
     * @return
     */
    public Boolean getBeneficiaryInOtherScheme()
    {
        return beneficiaryInOtherScheme;
    }

    /**
     *
     * @param beneficiaryInOtherScheme
     */
    public void setBeneficiaryInOtherScheme(Boolean beneficiaryInOtherScheme)
    {
        this.beneficiaryInOtherScheme = beneficiaryInOtherScheme;
    }

//    public String getApplicationID()
//    {
//        return applicationID;
//    }
//
//    public void setApplicationID(String applicationID)
//    {
//        this.applicationID = applicationID;
//    }
//
    public String getProfilePhotoPath()
    {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath)
    {
        this.profilePhotoPath = profilePhotoPath;
    }

    /**
     *
     * @return
     */
    public MultipartFile getProfilePhoto()
    {
        return profilePhoto;
    }

    public void setProfilePhoto(MultipartFile profilePhoto)
    {
        this.profilePhoto = profilePhoto;
    }

    public String getSignaturePath()
    {
        return signaturePath;
    }

    /**
     *
     * @param signaturePath
     */
    public void setSignaturePath(String signaturePath)
    {
        this.signaturePath = signaturePath;
    }

    public District getBirthPlace()
    {
        return birthPlace;
    }

    public void setBirthPlace(District birthPlace)
    {
        this.birthPlace = birthPlace;
    }

}

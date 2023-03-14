/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.forms;

import com.wfp.lmmis.model.ApplicantBasicInfo;
import com.wfp.lmmis.payroll.model.FiscalYear;
import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PCUser
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BiometricInfoForm extends ApplicantBasicInfo implements Serializable
{
    private String profilePhotoPath;
    private MultipartFile profilePhoto;
    private String signaturePath;
    private MultipartFile signature;    
    private String base64PhotoFile;
    private String base64SignatureFile;

    public String getProfilePhotoPath()
    {
        return profilePhotoPath;
    }

    /**
     *
     * @param profilePhotoPath
     */
    public void setProfilePhotoPath(String profilePhotoPath)
    {
        this.profilePhotoPath = profilePhotoPath;
    }

    public MultipartFile getProfilePhoto()
    {
        return profilePhoto;
    }

    /**
     *
     * @param profilePhoto
     */
    public void setProfilePhoto(MultipartFile profilePhoto)
    {
        this.profilePhoto = profilePhoto;
    }

    public String getSignaturePath()
    {
        return signaturePath;
    }

    public void setSignaturePath(String signaturePath)
    {
        this.signaturePath = signaturePath;
    }

    public MultipartFile getSignature()
    {
        return signature;
    }

    public void setSignature(MultipartFile signature)
    {
        this.signature = signature;
    }

    public String getBase64PhotoFile()
    {
        return base64PhotoFile;
    }

    public void setBase64PhotoFile(String base64PhotoFile)
    {
        this.base64PhotoFile = base64PhotoFile;
    }

    /**
     *
     * @return
     */
    public String getBase64SignatureFile()
    {
        return base64SignatureFile;
    }

    public void setBase64SignatureFile(String base64SignatureFile)
    {
        this.base64SignatureFile = base64SignatureFile;
    }
    
    

}

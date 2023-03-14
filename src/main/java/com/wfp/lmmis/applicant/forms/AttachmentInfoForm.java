/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.forms;

import com.wfp.lmmis.applicant.model.ApplicantAttachment;
import com.wfp.lmmis.beneficiary.model.BeneficiaryAttachment;
import com.wfp.lmmis.model.ApplicantBasicInfo;
import java.io.Serializable;
import java.util.List;

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
public class AttachmentInfoForm extends ApplicantBasicInfo implements Serializable
{
    List<MultipartFile> multipartFileList;
    List<ApplicantAttachment> attachmentList;
    List<BeneficiaryAttachment> beneficiaryAttachmentList;
    String removeList;

    public List<MultipartFile> getMultipartFileList()
    {
        return multipartFileList;
    }

    public void setMultipartFileList(List<MultipartFile> multipartFileList)
    {
        this.multipartFileList = multipartFileList;
    }

    public List<ApplicantAttachment> getAttachmentList()
    {
        return attachmentList;
    }

    public void setAttachmentList(List<ApplicantAttachment> attachmentList)
    {
        this.attachmentList = attachmentList;
    }

    public List<BeneficiaryAttachment> getBeneficiaryAttachmentList()
    {
        return beneficiaryAttachmentList;
    }

    public void setBeneficiaryAttachmentList(List<BeneficiaryAttachment> beneficiaryAttachmentList)
    {
        this.beneficiaryAttachmentList = beneficiaryAttachmentList;
    }

    /**
     *
     * @return
     */
    public String getRemoveList()
    {
        return removeList;
    }

    public void setRemoveList(String removeList)
    {
        this.removeList = removeList;
    }

    

    
}

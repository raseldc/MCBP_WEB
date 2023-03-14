/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.beneficiary.model;

import com.wfp.lmmis.enums.AttachmentType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author PCUser
 */
@Entity
@Table(name = "beneficiary_attachment")
public class BeneficiaryAttachment implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "attachment_name", nullable = false)
    private String attachmentName;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "file_path", nullable = false)
    private String filePath;
    @Column(name = "attachment_type", nullable = false)
    private AttachmentType attachmentType;
    @ManyToOne
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;

    /**
     *
     * @return
     */
    public Integer getId()
    {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAttachmentName()
    {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName)
    {
        this.attachmentName = attachmentName;
    }

    /**
     *
     * @return
     */
    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     *
     * @return
     */
    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public AttachmentType getAttachmentType()
    {
        return attachmentType;
    }

    public void setAttachmentType(AttachmentType attachmentType)
    {
        this.attachmentType = attachmentType;
    }

    public Beneficiary getBeneficiary()
    {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary)
    {
        this.beneficiary = beneficiary;
    }

}

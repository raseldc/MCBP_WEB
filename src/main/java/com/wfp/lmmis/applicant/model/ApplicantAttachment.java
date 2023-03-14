/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.model;

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
@Table(name = "applicant_attachment")
public class ApplicantAttachment implements Serializable
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
    @JoinColumn(name = "applicant")
    private Applicant applicant;

    public Integer getId()
    {
        return id;
    }

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

    public AttachmentType getAttachmentType()
    {
        return attachmentType;
    }

    /**
     *
     * @param attachmentType
     */
    public void setAttachmentType(AttachmentType attachmentType)
    {
        this.attachmentType = attachmentType;
    }
    
    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public Applicant getApplicant()
    {
        return applicant;
    }

    /**
     *
     * @param applicant
     */
    public void setApplicant(Applicant applicant)
    {
        this.applicant = applicant;
    }

}

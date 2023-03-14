/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.beneficiary.model;

import com.wfp.lmmis.model.BaseModel;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PCUser
 */
@Entity
@Table(name = "beneficiary_biometric_info")
public class BeneficiaryBiometricInfo implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Lob
    @Column(name = "photo", nullable = false)
    private byte[] photoData;

    @Lob
    @Column(name = "signature", nullable = false)
    private byte[] signatureData;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private Beneficiary beneficiary;

    @Transient
    private String base64PhotoData;

    @Transient
    private String base64SignatureData;

    @Column(name = "photo_path", nullable = false)
    private String photoPath;

    @Column(name = "signature_path", nullable = false)
    private String signaturePath;

    public Integer getId()
    {
        return Id;
    }

    public void setId(Integer Id)
    {
        this.Id = Id;
    }

    /**
     *
     * @return
     */
    public byte[] getPhotoData()
    {
        return photoData;
    }

    public void setPhotoData(byte[] photoData)
    {
        this.photoData = photoData;
    }

    public byte[] getSignatureData()
    {
        return signatureData;
    }

    public void setSignatureData(byte[] signatureData)
    {
        this.signatureData = signatureData;
    }

    /**
     *
     * @return
     */
    public Beneficiary getBeneficiary()
    {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary)
    {
        this.beneficiary = beneficiary;
    }

    public String getBase64PhotoData()
    {
        return base64PhotoData;
    }

    /**
     *
     * @param base64PhotoData
     */
    public void setBase64PhotoData(String base64PhotoData)
    {
        this.base64PhotoData = base64PhotoData;
    }

    public String getBase64SignatureData()
    {
        return base64SignatureData;
    }

    public void setBase64SignatureData(String base64SignatureData)
    {
        this.base64SignatureData = base64SignatureData;
    }

    public String getPhotoPath()
    {
        return photoPath;
    }

    public void setPhotoPath(String photoPath)
    {
        this.photoPath = photoPath;
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


    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.Id);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final BeneficiaryBiometricInfo other = (BeneficiaryBiometricInfo) obj;
        if (!Objects.equals(this.Id, other.Id))
        {
            return false;
        }
        return true;
    }

}

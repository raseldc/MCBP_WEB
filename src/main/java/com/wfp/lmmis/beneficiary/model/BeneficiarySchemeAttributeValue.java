package com.wfp.lmmis.beneficiary.model;

import com.wfp.lmmis.applicant.model.*;
import com.wfp.lmmis.masterdata.model.*;
import com.wfp.lmmis.model.BaseModel;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;


@Entity
@Table(name = "beneficiary_scheme_attribute_value")
public class BeneficiarySchemeAttributeValue extends BaseModel implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int unsigned")
    private Integer id;

    
    @Size(min = 1, max = 255)
    @Column(name = "scheme_attribute_value_in_english", length=255, nullable = true)
    private String schemeAttributeValueInEnglish;    
        
    @Size(min = 1, max = 255)
    @Column(name = "scheme_attribute_value_in_bangla", length=255, nullable = true)
    private String schemeAttributeValueInBangla;    
        
    @ManyToOne
    @JoinColumn(name = "scheme_attribute_id")
    private SchemeAttribute schemeAttribute;

    @ManyToOne
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;
    

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getSchemeAttributeValueInEnglish()
    {
        return schemeAttributeValueInEnglish;
    }

    /**
     *
     * @param schemeAttributeValueInEnglish
     */
    public void setSchemeAttributeValueInEnglish(String schemeAttributeValueInEnglish)
    {
        this.schemeAttributeValueInEnglish = schemeAttributeValueInEnglish;
    }

    public String getSchemeAttributeValueInBangla()
    {
        return schemeAttributeValueInBangla;
    }

    public void setSchemeAttributeValueInBangla(String schemeAttributeValueInBangla)
    {
        this.schemeAttributeValueInBangla = schemeAttributeValueInBangla;
    }

    /**
     *
     * @return
     */
    public SchemeAttribute getSchemeAttribute()
    {
        return schemeAttribute;
    }

    public void setSchemeAttribute(SchemeAttribute schemeAttribute)
    {
        this.schemeAttribute = schemeAttribute;
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

    @Override
    public String toString()
    {
        return this.schemeAttributeValueInEnglish;
    }

    
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final BeneficiarySchemeAttributeValue other = (BeneficiarySchemeAttributeValue) obj;
        if (!Objects.equals(this.schemeAttributeValueInEnglish, other.schemeAttributeValueInEnglish))
        {
            return false;
        }
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        if (!Objects.equals(this.schemeAttribute, other.schemeAttribute))
        {
            return false;
        }
        if (!Objects.equals(this.beneficiary, other.beneficiary))
        {
            return false;
        }
        return true;
    }


}

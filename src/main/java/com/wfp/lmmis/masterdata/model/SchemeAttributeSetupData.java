package com.wfp.lmmis.masterdata.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "scheme_attribute_setup_data")
public class SchemeAttributeSetupData extends BaseModel implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "smallint unsigned")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_attribute_id", nullable = false, unique=false)
    private SchemeAttribute schemeAttribute;
    
    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "attribute_value_in_english", length=255, nullable = false, unique=false)
    private String attributeValueInEnglish;
    
    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "attribute_value_in_bangla", length=255, nullable = false, unique=false)
    private String attributeValueInBangla;
    
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

    public SchemeAttribute getSchemeAttribute()
    {
        return schemeAttribute;
    }

    /**
     *
     * @param schemeAttribute
     */
    public void setSchemeAttribute(SchemeAttribute schemeAttribute)
    {
        this.schemeAttribute = schemeAttribute;
    }
    

    public String getAttributeValueInEnglish()
    {
        return attributeValueInEnglish;
    }

    public void setAttributeValueInEnglish(String attributeValueInEnglish)
    {
        this.attributeValueInEnglish = attributeValueInEnglish;
    }

    public String getAttributeValueInBangla()
    {
        return attributeValueInBangla;
    }

    public void setAttributeValueInBangla(String attributeValueInBangla)
    {
        this.attributeValueInBangla = attributeValueInBangla;
    }

    @Override
    public String toString()
    {
        return this.getAttributeValueInEnglish();
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    /**
     *
     * @param obj
     * @return
     */
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
        final SchemeAttributeSetupData other = (SchemeAttributeSetupData) obj;
        if (!Objects.equals(this.attributeValueInEnglish, other.attributeValueInEnglish))
        {
            return false;
        }
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}

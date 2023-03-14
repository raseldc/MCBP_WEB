package com.wfp.lmmis.masterdata.model;

import com.wfp.lmmis.enums.AttributeType;
import com.wfp.lmmis.enums.ComparisonType;
import com.wfp.lmmis.model.BaseModel;
import com.wfp.lmmis.types.OrderingType;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "scheme_attribute")
public class SchemeAttribute extends BaseModel implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "smallint unsigned")
    private Integer id;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "name_in_bangla", length = 255, nullable = false, unique = true)
    private String nameInBangla;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "name_in_english", length = 255, nullable = false, unique = true)
    private String nameInEnglish;

    @NotNull
    @Column(name = "attribute_type", nullable = false, unique = false, columnDefinition = "tinyint unsigned")
    private AttributeType attributeType;

    @Column(name = "is_mandatory_for_selection_criteria", nullable = false, unique = false)
    private boolean isMandatoryForSelectionCriteria = false;

    @NotNull
    @Column(name = "view_order", nullable = false, unique = false, columnDefinition = "TINYINT unsigned")
    private Integer viewOrder;

    @NotNull
    @Column(name = "ordering_type", nullable = false, unique = false, columnDefinition = "TINYINT unsigned")
    private OrderingType orderingType;

    @NotNull
    @Column(name = "selection_criteria_priority", nullable = false, unique = false, columnDefinition = "TINYINT unsigned")
    private Integer selectionCriteriaPriority;

    @Column(name = "comparison_type", nullable = true, unique = false, columnDefinition = "tinyint unsigned")
    private ComparisonType comparisonType;

    @Size(min = 0, max = 127)
    @Column(name = "compared_value", length = 127, nullable = true, unique = true)
    private String comparedValue;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_id", nullable = false, unique = false)
    private Scheme scheme;

    @OneToMany(mappedBy = "schemeAttribute", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL) // changed by mahbub
//    @OneToMany(mappedBy = "schemeAttribute", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<SchemeAttributeSetupData> schemeAttributeDataList;

    @Column(name = "deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

    /**
     *
     * @return
     */
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNameInBangla()
    {
        return nameInBangla;
    }

    /**
     *
     * @param nameInBangla
     */
    public void setNameInBangla(String nameInBangla)
    {
        this.nameInBangla = nameInBangla;
    }

    /**
     *
     * @return
     */
    public String getNameInEnglish()
    {
        return nameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish)
    {
        this.nameInEnglish = nameInEnglish;
    }

    public AttributeType getAttributeType()
    {
        return attributeType;
    }

    public void setAttributeType(AttributeType attributeType)
    {
        this.attributeType = attributeType;
    }

    public boolean isIsMandatoryForSelectionCriteria()
    {
        return isMandatoryForSelectionCriteria;
    }

    public void setIsMandatoryForSelectionCriteria(boolean isMandatoryForSelectionCriteria)
    {
        this.isMandatoryForSelectionCriteria = isMandatoryForSelectionCriteria;
    }

    public Integer getViewOrder()
    {
        return viewOrder;
    }

    public void setViewOrder(Integer viewOrder)
    {
        this.viewOrder = viewOrder;
    }

    public OrderingType getOrderingType()
    {
        return orderingType;
    }

    public void setOrderingType(OrderingType orderingType)
    {
        this.orderingType = orderingType;
    }

    public ComparisonType getComparisonType()
    {
        return comparisonType;
    }

    public void setComparisonType(ComparisonType comparisonType)
    {
        this.comparisonType = comparisonType;
    }

    /**
     *
     * @return
     */
    public String getComparedValue()
    {
        return comparedValue;
    }

    public void setComparedValue(String comparedValue)
    {
        this.comparedValue = comparedValue;
    }

    public Integer getSelectionCriteriaPriority()
    {
        return selectionCriteriaPriority;
    }

    public void setSelectionCriteriaPriority(Integer selectionCriteriaPriority)
    {
        this.selectionCriteriaPriority = selectionCriteriaPriority;
    }

    public Scheme getScheme()
    {
        return scheme;
    }

    public void setScheme(Scheme scheme)
    {
        this.scheme = scheme;
    }

    public List<SchemeAttributeSetupData> getSchemeAttributeDataList()
    {
        return schemeAttributeDataList;
    }

    public void setSchemeAttributeDataList(List<SchemeAttributeSetupData> schemeAttributeDataList)
    {
        this.schemeAttributeDataList = schemeAttributeDataList;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    /**
     *
     * @param deleted
     */
    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return this.getNameInEnglish();
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
        final SchemeAttribute other = (SchemeAttribute) obj;
        if (!Objects.equals(this.nameInEnglish, other.nameInEnglish))
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

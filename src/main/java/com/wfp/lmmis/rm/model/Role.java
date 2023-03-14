package com.wfp.lmmis.rm.model;

import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.model.BaseModel;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "role")
public class Role extends BaseModel implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, max = 255)
    @Column(name = "name_in_bangla", nullable = false)
    private String nameInBangla;

    @Size(min = 3, max = 255)
    @Column(name = "name_in_english", nullable = false)
    private String nameInEnglish;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "is_deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

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

//	public List<Page> getPages()
//	{
//		return pages;
//	}
//
//	public void setPages(List<Page> pages)
//	{
//		this.pages = pages;
//	}
    public String getNameInBangla()
    {
        return nameInBangla;
    }

    public void setNameInBangla(String nameInBangla)
    {
        this.nameInBangla = nameInBangla;
    }

    public String getNameInEnglish()
    {
        return nameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish)
    {
        this.nameInEnglish = nameInEnglish;
    }

    /**
     *
     * @return
     */
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

}

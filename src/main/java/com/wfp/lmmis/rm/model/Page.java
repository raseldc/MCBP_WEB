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
@Table(name = "page")
public class Page extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "tinyint unsigned")
    private Integer Id;

    @Size(min = 3, max = 255)
    @Column(name = "name_in_bangla", nullable = false, length = 255, unique = true)
    private String nameInBangla;

    @Size(min = 3, max = 255)
    @Column(name = "name_in_english", nullable = false, length = 255, unique = true)
    private String nameInEnglish;

    @Size(min = 1, max = 255)
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "page_order", nullable = false, columnDefinition = "tinyint unsigned")
    private int pageOrder;

    @ManyToOne
    @JoinColumn(name = "parent_page_id", nullable = true)
    private Page parentPage;

    @Column(name = "page_icon")
    private String pageIcon;

    @Column(name = "active")
    private boolean active;

    public Integer getId() {
        return Id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        Id = id;
    }

    public String getNameInBangla() {
        return nameInBangla;
    }

    public void setNameInBangla(String nameInBangla) {
        this.nameInBangla = nameInBangla;
    }

    public String getNameInEnglish() {
        return nameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish) {
        this.nameInEnglish = nameInEnglish;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public Page getParentPage() {
        return parentPage;
    }

    public int getPageOrder() {
        return pageOrder;
    }

    public void setPageOrder(int pageOrder) {
        this.pageOrder = pageOrder;
    }

    public void setParentPage(Page parentPage) {
        this.parentPage = parentPage;
    }

    public String getPageIcon() {
        return pageIcon;
    }

    public void setPageIcon(String pageIcon) {
        this.pageIcon = pageIcon;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((Id == null) ? 0 : Id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Page other = (Page) obj;
        if (Id == null) {
            if (other.Id != null) {
                return false;
            }
        } else if (!Id.equals(other.Id)) {
            return false;
        }
        return true;
    }

}

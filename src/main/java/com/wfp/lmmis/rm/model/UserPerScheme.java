package com.wfp.lmmis.rm.model;

import com.wfp.lmmis.enums.UserType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user_per_scheme")
public class UserPerScheme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint unsigned")
    private Integer id;

    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "head_office_user", nullable = true)
    private boolean headOfficeUser;

    @Column(name = "active", nullable = false, columnDefinition = "bit(1)")
    private boolean active;

    @Column(name = "is_default", columnDefinition = "bit(1) default false")
    private boolean isDefault;

    @Column(name = "is_deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @NotNull
    @ManyToOne()
    @JoinColumn(name = "scheme_id", nullable = true)
    private Scheme scheme;

    @ManyToOne()
    @JoinColumn(name = "division_id", nullable = true)
    private Division division;

    @ManyToOne()
    @JoinColumn(name = "district_id", nullable = true)
    private District district;

    @ManyToOne()
    @JoinColumn(name = "upazilla_id", nullable = true)
    private Upazilla upazilla;

    @ManyToOne()
    @JoinColumn(name = "union_id", nullable = true)
    private Union union;

    @ManyToOne()
    @JoinColumn(name = "bgmea_factory_id", nullable = true)
    private Factory bgmeaFactory;

    @ManyToOne()
    @JoinColumn(name = "bkmea_factory_id", nullable = true)
    private Factory bkmeaFactory;

    @ManyToOne()
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Transient
    private Integer remove;

    @Transient
    private String locationName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isHeadOfficeUser() {
        return headOfficeUser;
    }

    public void setHeadOfficeUser(boolean headOfficeUser) {
        this.headOfficeUser = headOfficeUser;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    /**
     *
     * @param isDefault
     */
    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     */
    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public Division getDivision() {
        return division;
    }

    /**
     *
     * @param division
     */
    public void setDivision(Division division) {
        this.division = division;
    }

    public District getDistrict() {
        return district;
    }

    /**
     *
     * @param district
     */
    public void setDistrict(District district) {
        this.district = district;
    }

    public Upazilla getUpazilla() {
        return upazilla;
    }

    public void setUpazilla(Upazilla upazilla) {
        this.upazilla = upazilla;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    /**
     *
     * @return
     */
    public Factory getBgmeaFactory() {
        return bgmeaFactory;
    }

    public void setBgmeaFactory(Factory bgmeaFactory) {
        this.bgmeaFactory = bgmeaFactory;
    }

    public Factory getBkmeaFactory() {
        return bkmeaFactory;
    }

    public void setBkmeaFactory(Factory bkmeaFactory) {
        this.bkmeaFactory = bkmeaFactory;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     *
     * @return
     */
    public Integer getRemove() {
        return remove;
    }

    /**
     *
     * @param remove
     */
    public void setRemove(Integer remove) {
        this.remove = remove;
    }

    public String getLocationName() {
        return locationName;
    }

    /**
     *
     * @param locationName
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
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
        return true;
    }

}

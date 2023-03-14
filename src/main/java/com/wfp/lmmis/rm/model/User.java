package com.wfp.lmmis.rm.model;

import com.wfp.lmmis.enums.UserType;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.model.BaseModel;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.Email;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "user")
public class User extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "smallint unsigned")
    private Integer id;

    @Column(name = "status")
    private Integer status;

    @NotEmpty
    @Column(name = "user_id", nullable = false)
    private String userID;

    @Email
    @Column(name = "email", nullable = true)
    private String email;

    @NotEmpty
    @Column(name = "full_name_bn", nullable = false)
    private String fullNameBn;

    @NotEmpty
    @Column(name = "full_name_en", nullable = false)
    private String fullNameEn;

    @NotEmpty
    @Column(name = "designation", nullable = false)
    private String designation;

    // @Size(min = 8)
    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "salt", nullable = false)
    private String salt;

    @NotEmpty
    @Column(name = "mobile_no", nullable = false, unique = true, columnDefinition = "char(11)")
    private String mobileNo;

    @Column(name = "profile_pic")
    private String profilePicPath;

    @Column(name = "active", nullable = false, columnDefinition = "bit(1)")
    private boolean active;

    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "head_office_user", nullable = true)
    private boolean headOfficeUser;

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

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Transient
    private MultipartFile profilePhoto;

    @Column(name = "is_deleted", columnDefinition = "bit(1) default false")
    private boolean deleted;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private UserPerScheme userPerScheme;

    public MultipartFile getProfilePhoto() {
        return profilePhoto;
    }

    /**
     *
     * @param profilePhoto
     */
    public void setProfilePhoto(MultipartFile profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    /**
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFullNameBn() {
        return fullNameBn;
    }

    public void setFullNameBn(String fullNameBn) {
        this.fullNameBn = fullNameBn;
    }

    public String getFullNameEn() {
        return fullNameEn;
    }

    public void setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     *
     * @return
     */
    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public boolean isHeadOfficeUser() {
        return headOfficeUser;
    }

    /**
     *
     * @param headOfficeUser
     */
    public void setHeadOfficeUser(boolean headOfficeUser) {
        this.headOfficeUser = headOfficeUser;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Upazilla getUpazilla() {
        return upazilla;
    }

    public void setUpazilla(Upazilla upazilla) {
        this.upazilla = upazilla;
    }

    /**
     *
     * @return
     */
    public Union getUnion() {
        return union;
    }

    /**
     *
     * @param union
     */
    public void setUnion(Union union) {
        this.union = union;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     *
     * @return
     */
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getFullNameEn();
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserPerScheme getUserPerScheme() {
        return userPerScheme;
    }

    /**
     *
     * @param userPerScheme
     */
    public void setUserPerScheme(UserPerScheme userPerScheme) {
        this.userPerScheme = userPerScheme;
    }

    /**
     *
     * @return
     */
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        final User other = (User) obj;
        if (!Objects.equals(this.fullNameEn, other.fullNameEn)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}

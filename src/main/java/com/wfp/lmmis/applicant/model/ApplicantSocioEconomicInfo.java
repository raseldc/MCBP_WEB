/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.model;

import com.wfp.lmmis.enums.ConceptionTerm;
import com.wfp.lmmis.enums.DisabilityInFamily;
import com.wfp.lmmis.enums.HHWallMadeOf;
import com.wfp.lmmis.enums.LandSize;
import com.wfp.lmmis.enums.MonthlyIncome;
import com.wfp.lmmis.enums.OccupationRural;
import com.wfp.lmmis.enums.OccupationUrban;
import com.wfp.lmmis.enums.YesNoEnum;
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

/**
 *
 * @author PCUser
 */
@Entity
@Table(name = "applicant_socio_economic_info")
public class ApplicantSocioEconomicInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "monthly_income", columnDefinition = "bit(3)", nullable = false)
    private MonthlyIncome monthlyIncome;

    @Column(name = "disability", columnDefinition = "bit(2)", nullable = false)
    private DisabilityInFamily disability;

    @Column(name = "hh_wall_made_of", columnDefinition = "bit(2)", nullable = false)
    private HHWallMadeOf hHWallMadeOf;

    @Column(name = "has_electricity", columnDefinition = "bit(1)", nullable = true)
    private YesNoEnum hASElectricity;

    @Column(name = "has_electric_fan", columnDefinition = "bit(1)", nullable = false)
    private YesNoEnum hASElectricFan;

//    ------------------ for rural------------
    @Column(name = "land_size_rural", columnDefinition = "bit(3)", nullable = true)
    private LandSize landSizeRural;

    @Column(name = "occupation_rural", columnDefinition = "bit(3)", nullable = true)
    private OccupationRural occupationRural;

    @Column(name = "has_latrine_rural", columnDefinition = "bit(1)", nullable = true)
    private YesNoEnum hASLatrineRural;

    @Column(name = "has_tubewell_rural", columnDefinition = "bit(1)", nullable = true)
    private YesNoEnum hASTubewellRural;

//     ------------------- for urban ------------------------
    @Column(name = "has_residence_urban", columnDefinition = "bit(1)", nullable = true)
    private YesNoEnum hasResidenceUrban;

    @Column(name = "occupation_urban", columnDefinition = "bit(3)", nullable = true)
    private OccupationUrban occupationUrban;

    @Column(name = "has_kitchen_urban", columnDefinition = "bit(1)", nullable = true)
    private YesNoEnum hASKitchenUrban;

    @Column(name = "has_telivision_urban", columnDefinition = "bit(1)", nullable = true)
    private YesNoEnum hASTelivisionUrban;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public MonthlyIncome getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(MonthlyIncome monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public DisabilityInFamily getDisability() {
        return disability;
    }

    /**
     *
     * @param disability
     */
    public void setDisability(DisabilityInFamily disability) {
        this.disability = disability;
    }

    public OccupationRural getOccupationRural() {
        return occupationRural;
    }

    public void setOccupationRural(OccupationRural occupationRural) {
        this.occupationRural = occupationRural;
    }

    public YesNoEnum gethASLatrineRural() {
        return hASLatrineRural;
    }

    public void sethASLatrineRural(YesNoEnum hASLatrineRural) {
        this.hASLatrineRural = hASLatrineRural;
    }

    /**
     *
     * @return
     */
    public YesNoEnum gethASElectricity() {
        return hASElectricity;
    }

    public void sethASElectricity(YesNoEnum hASElectricity) {
        this.hASElectricity = hASElectricity;
    }

    public YesNoEnum gethASElectricFan() {
        return hASElectricFan;
    }

    /**
     *
     * @param hASElectricFan
     */
    public void sethASElectricFan(YesNoEnum hASElectricFan) {
        this.hASElectricFan = hASElectricFan;
    }

    public LandSize getLandSizeRural() {
        return landSizeRural;
    }

    public void setLandSizeRural(LandSize landSizeRural) {
        this.landSizeRural = landSizeRural;
    }

    public YesNoEnum gethASTubewellRural() {
        return hASTubewellRural;
    }

    public void sethASTubewellRural(YesNoEnum hASTubewellRural) {
        this.hASTubewellRural = hASTubewellRural;
    }

    public YesNoEnum getHasResidenceUrban() {
        return hasResidenceUrban;
    }

    public void setHasResidenceUrban(YesNoEnum hasResidenceUrban) {
        this.hasResidenceUrban = hasResidenceUrban;
    }

    public OccupationUrban getOccupationUrban() {
        return occupationUrban;
    }

    public void setOccupationUrban(OccupationUrban occupationUrban) {
        this.occupationUrban = occupationUrban;
    }

    public YesNoEnum gethASKitchenUrban() {
        return hASKitchenUrban;
    }

    public void sethASKitchenUrban(YesNoEnum hASKitchenUrban) {
        this.hASKitchenUrban = hASKitchenUrban;
    }

    public YesNoEnum gethASTelivisionUrban() {
        return hASTelivisionUrban;
    }

    public void sethASTelivisionUrban(YesNoEnum hASTelivisionUrban) {
        this.hASTelivisionUrban = hASTelivisionUrban;
    }

    public HHWallMadeOf gethHWallMadeOf() {
        return hHWallMadeOf;
    }

    /**
     *
     * @param hHWallMadeOf
     */
    public void sethHWallMadeOf(HHWallMadeOf hHWallMadeOf) {
        this.hHWallMadeOf = hHWallMadeOf;
    }

    /**
     *
     * @return
     */
    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.Id);
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
        final ApplicantSocioEconomicInfo other = (ApplicantSocioEconomicInfo) obj;
        if (!Objects.equals(this.Id, other.Id)) {
            return false;
        }
        return true;
    }
}

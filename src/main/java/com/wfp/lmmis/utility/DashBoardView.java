/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.wfp.lmmis.dashboard.controller.ApplicantCountByStatus;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Getter
@Setter
public class DashBoardView {

    private Integer id;
    private Integer unionId;
    private Integer totalApplicant;
    private Integer activeApplicant;
    private Integer imlmaReferencApplicant;
    private Integer imlmaNonReferencApplicant;
    private Integer totalBeneficiary;
    private Integer activeBeneficiary;
    private Integer inactiveBeneficiary;
    private Integer temporarilyActiveBeneficiary;
    private Integer paymentHoldBeneficiary;
    private Integer paymentCompleteBeneficiary;
    private Double totalPayemtAmount;
    private Double totalPayemtAmountCurrentYear;
    private Integer totalUnionApplicant;
    private Integer totalBgmeApplicant;
    private Integer totalBkmeApplicant;
    private Integer totalCityCorporationApplicant;
    private Integer totalMunicipalApplicant;
    private Integer totalUnionBeneficiary;
    private Integer totalBgmeBeneficiary;
    private Integer totalBkmeBeneficiary;
    private Integer totalCityCorporationBeneficiary;
    private Integer totalMunicipalBeneficiary;
    private Integer totalUnionBeneficiaryActive;
    private Integer totalUnionBeneficiaryTemporarilyActive;
    private Integer totalUnionBeneficiaryInactive;
    private Integer totalUnionBeneficiarPaymentHold;
    private Integer totalUnionPaymentComplete;
    private Integer totalBgmeBeneficiaryActive;
    private Integer totalBgmeBeneficiaryTemporarilyActive;
    private Integer totalBgmeBeneficiaryInactive;
    private Integer totalBgmeBeneficiaryPaymentHold;
    private Integer totalBgmeBeneficiaryPaymentComplete;
    private Integer totalBkmeBeneficiaryActive;
    private Integer totalBkmeBeneficiaryTemporarilyActive;
    private Integer totalBkmeBeneficiaryInactive;
    private Integer totalBkmeBeneficiaryPaymentHold;
    private Integer totalBkmeBeneficiaryPaymentComplete;
    private Integer totalCityCorporationBeneficiaryActive;
    private Integer totalCityCorporationBeneficiaryTemporarilyActive;
    private Integer totalCityCorporationBeneficiaryInactive;
    private Integer totalCityCorporationBeneficiaryPaymentComplete;
    private Integer totalCityCorporationBeneficiaryPaymentHold;
    private Integer totalMunicipalBeneficiaryActive;
    private Integer totalMunicipalBeneficiaryTemporarilyActive;
    private Integer totalMunicipalBeneficiaryInactive;
    private Integer totalMunicipalBeneficiaryPaymentHold;
    private Integer totalMunicipalBeneficiaryPaymentComplete;
    private Integer applicantJan;
    private Integer applicantFeb;
    private Integer applicantMar;
    private Integer applicantApr;
    private Integer applicantMay;
    private Integer applicantJun;
    private Integer applicantJul;
    private Integer applicantAug;
    private Integer applicantSep;
    private Integer applicantOct;
    private Integer applicantNov;
    private Integer applicantDec;

    private Double paymentFiscalYear1;
    private Double paymentFiscalYear2;
    private Double paymentFiscalYear3;
    private Double paymentFiscalYear4;

}

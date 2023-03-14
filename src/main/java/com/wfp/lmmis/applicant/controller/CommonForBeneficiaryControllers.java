/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.controller;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.beneficiary.model.BeneficiaryForm;
import com.wfp.lmmis.beneficiary.model.BeneficiarySocioEconomicInfo;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.types.Gender;
import com.wfp.lmmis.utility.CommonUtility;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Component
public class CommonForBeneficiaryControllers {

    //  public static List<Integer> upazilaId = Arrays.asList(114, 662, 8134, 8527, 9141, 395, 7263);
    public static List<Integer> upazilaId = Arrays.asList(114, 395, 662, 1376, 2294, 2743, 4680, 4730, 4859, 5063, 5566, 5835, 6944, 7263, 7895, 7976, 8134, 8247, 8429, 8527, 8867, 9141, 9159, 9376, 9494);

    public void loadBasicInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        beneficiaryForm.setId(beneficiary.getId());
        beneficiaryForm.setScheme(beneficiary.getScheme());
        beneficiaryForm.setFiscalYear(beneficiary.getFiscalYear());
//        beneficiaryForm.setBatch(beneficiary.getBatch());

        beneficiaryForm.setFullNameInBangla(beneficiary.getFullNameInBangla());
        beneficiaryForm.setFullNameInEnglish(beneficiary.getFullNameInEnglish());
        beneficiaryForm.setFatherName(beneficiary.getFatherName());
        beneficiaryForm.setMotherName(beneficiary.getMotherName());
        beneficiaryForm.setSpouseName(beneficiary.getSpouseName());
        beneficiaryForm.setNickName(beneficiary.getNickName());
        beneficiaryForm.setDateOfBirth(beneficiary.getDateOfBirth());
        beneficiaryForm.setGender(Gender.FEMALE);
        beneficiaryForm.setBirthPlace(beneficiary.getBirthPlace());
        beneficiaryForm.setEducationLevelEnum(beneficiary.getEducationLevelEnum());
        beneficiaryForm.setReligionEnum(beneficiary.getReligionEnum());
        beneficiaryForm.setMaritalInfoEnum(beneficiary.getMaritalInfoEnum());

        beneficiaryForm.setIsLMMISExit(beneficiary.getIsLMMISExist() == 0 ? false : true);
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            beneficiaryForm.setNid(beneficiary.getNid().toString());
            beneficiaryForm.setMobileNo(beneficiary.getMobileNo() != null ? ("0" + beneficiary.getMobileNo()) : "");
        } else {

            beneficiaryForm.setMobileNo(beneficiary.getMobileNo() != null ? (CommonUtility.getNumberInBangla("0" + beneficiary.getMobileNo().toString())) : "");
            beneficiaryForm.setNid(CommonUtility.getNumberInBangla(beneficiary.getNid().toString()));
        }
        beneficiaryForm.setOccupation(beneficiary.getOccupation());
//        beneficiaryForm.setEmail(beneficiary.getEmail());
        beneficiaryForm.setBloodGroup(beneficiary.getBloodGroup());
    }

    public void loadAddressInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        beneficiaryForm.setApplicantType(beneficiary.getApplicantType());
        if (null != beneficiaryForm.getApplicantType()) {
            switch (beneficiaryForm.getApplicantType()) {
                case REGULAR:
                    //beneficiaryForm.setFactory(null);
                    break;
                case BGMEA:
                    beneficiaryForm.setBgmeaFactory(beneficiary.getFactory());
                    break;
                case BKMEA:
                    beneficiaryForm.setBkmeaFactory(beneficiary.getFactory());
                    break;
            }
        }
        beneficiaryForm.setPresentAddressLine1(beneficiary.getPresentAddressLine1());
        beneficiaryForm.setPresentAddressLine2(beneficiary.getPresentAddressLine2());
        beneficiaryForm.setPresentDivision(beneficiary.getPresentDivision());
        beneficiaryForm.setPresentDistrict(beneficiary.getPresentDistrict());
        beneficiaryForm.setPresentUpazila(beneficiary.getPresentUpazila());
        beneficiaryForm.setPresentUnion(beneficiary.getPresentUnion());
        beneficiaryForm.setPresentVillage(beneficiary.getPresentVillage());

        beneficiaryForm.setPermanentAddressLine1(beneficiary.getPermanentAddressLine1());
        beneficiaryForm.setPermanentAddressLine2(beneficiary.getPermanentAddressLine2());
        beneficiaryForm.setPermanentDivision(beneficiary.getPermanentDivision());
        beneficiaryForm.setPermanentDistrict(beneficiary.getPermanentDistrict());
        beneficiaryForm.setPermanentUpazila(beneficiary.getPermanentUpazila());
        beneficiaryForm.setPermanentUnion(beneficiary.getPermanentUnion());
        beneficiaryForm.setPermanentVillage(beneficiary.getPermanentVillage());
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            beneficiaryForm.setPresentWardNo(beneficiary.getPresentWardNo());
            beneficiaryForm.setPresentPostCode(beneficiary.getPresentPostCode());
            beneficiaryForm.setPermanentWardNo(beneficiary.getPermanentWardNo());
            beneficiaryForm.setPermanentPostCode(beneficiary.getPermanentPostCode());
        } else {
            beneficiaryForm.setPresentWardNo(CommonUtility.getNumberInBangla(beneficiary.getPresentWardNo()));
            beneficiaryForm.setPresentPostCode(CommonUtility.getNumberInBangla(beneficiary.getPresentPostCode()));
            beneficiaryForm.setPermanentWardNo(CommonUtility.getNumberInBangla(beneficiary.getPermanentWardNo()));
            beneficiaryForm.setPermanentPostCode(CommonUtility.getNumberInBangla(beneficiary.getPermanentPostCode()));
        }
    }

    /**
     *
     * @param beneficiaryForm
     * @param beneficiary
     */
    public void loadSocioEconomicInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        BeneficiarySocioEconomicInfo beneficiarySocioEconomicInfo = beneficiary.getBeneficiarySocioEconomicInfo();
        if (beneficiarySocioEconomicInfo != null) {
            beneficiaryForm.setMonthlyIncome(beneficiarySocioEconomicInfo.getMonthlyIncome());
            beneficiaryForm.setDisability(beneficiarySocioEconomicInfo.getDisability());
            beneficiaryForm.sethHWallMadeOf(beneficiarySocioEconomicInfo.gethHWallMadeOf());
            beneficiaryForm.sethASElectricity(beneficiarySocioEconomicInfo.gethASElectricity());
            beneficiaryForm.sethASElectricFan(beneficiarySocioEconomicInfo.gethASElectricFan());
            if (beneficiary.getApplicantType() == ApplicantType.UNION) {
                beneficiaryForm.setLandSizeRural(beneficiarySocioEconomicInfo.getLandSizeRural());
                beneficiaryForm.setOccupationRural(beneficiarySocioEconomicInfo.getOccupationRural());
                beneficiaryForm.sethASLatrineRural(beneficiarySocioEconomicInfo.gethASLatrineRural());
                beneficiaryForm.sethASTubewellRural(beneficiarySocioEconomicInfo.gethASTubewellRural());
            } else {
                beneficiaryForm.setHasResidenceUrban(beneficiarySocioEconomicInfo.getHasResidenceUrban());
                beneficiaryForm.setOccupationUrban(beneficiarySocioEconomicInfo.getOccupationUrban());
                beneficiaryForm.sethASKitchenUrban(beneficiarySocioEconomicInfo.gethASKitchenUrban());
                beneficiaryForm.sethASTelivisionUrban(beneficiarySocioEconomicInfo.gethASTelivisionUrban());
            }
        }
    }

    public void loadHealthStatusInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        beneficiaryForm.setConceptionTerm(beneficiary.getConceptionTerm());
        beneficiaryForm.setConceptionDuration(beneficiary.getConceptionDuration());
    }

    public void loadPaymentInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        beneficiaryForm.setPaymentType(beneficiary.getPaymentType());
        if (beneficiaryForm.getPaymentType() != null) {
            switch (beneficiaryForm.getPaymentType()) {
                case BANKING:
                    beneficiaryForm.setBank(beneficiary.getBank());
                    beneficiaryForm.setBranch(beneficiary.getBranch());
                    beneficiaryForm.setAccountType(beneficiary.getAccountType());
                    beneficiaryForm.setMobileBankingProvider(null);
                    beneficiaryForm.setPostOfficeBranch(null);
                    break;
                case MOBILEBANKING:
                    beneficiaryForm.setMobileBankingProvider(beneficiary.getMobileBankingProvider());
                    beneficiaryForm.setAccountType(beneficiary.getAccountType());
                    beneficiaryForm.setBank(null);
                    beneficiaryForm.setBranch(null);
                    beneficiaryForm.setPostOfficeBranch(null);
                    beneficiaryForm.setAccountType(null);
                    break;
                case POSTOFFICE:
                    beneficiaryForm.setPostOfficeBranch(beneficiary.getPostOfficeBranch());
                    beneficiaryForm.setAccountTypePO(beneficiary.getAccountType());
                    beneficiaryForm.setBank(null);
                    beneficiaryForm.setBranch(null);
                    beneficiaryForm.setMobileBankingProvider(null);
                    break;
            }
            beneficiaryForm.setAccountName(beneficiary.getAccountName());
            if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
                beneficiaryForm.setAccountNo(beneficiary.getAccountNo());
            } else {
                beneficiaryForm.setAccountNo(CommonUtility.getNumberInBangla(beneficiary.getAccountNo()));
            }
        } else {
            beneficiaryForm.setBank(null);
            beneficiaryForm.setBranch(null);
            beneficiaryForm.setPostOfficeBranch(null);
            beneficiaryForm.setMobileBankingProvider(null);
            beneficiaryForm.setAccountType(null);
            beneficiaryForm.setAccountName(null);
            beneficiaryForm.setAccountNo(null);
        }
    }

    public void loadBiometricInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        try {
            beneficiaryForm.setPhoto(null);
            beneficiaryForm.setPhotoPath(beneficiary.getBeneficiaryBiometricInfo().getPhotoPath());
            beneficiaryForm.setPhotoData(beneficiary.getBeneficiaryBiometricInfo().getBase64PhotoData());
            beneficiaryForm.setSignature(null);
            beneficiaryForm.setSignaturePath(beneficiary.getBeneficiaryBiometricInfo().getSignaturePath());
            beneficiaryForm.setSignatureData(beneficiary.getBeneficiaryBiometricInfo().getBase64SignatureData());
        } catch (Exception ex) {
            //logger.getlogger(ApplicantController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAttachmentInfo(BeneficiaryForm beneficiaryForm, Beneficiary beneficiary) {
        beneficiaryForm.setAttachmentList(beneficiary.getBeneficiaryAttachmentList());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.wfp.lmmis.applicant.forms.BiometricInfoForm;
import com.wfp.lmmis.beneficiary.model.BeneficiaryBiometricInfo;
import com.wfp.lmmis.beneficiary.model.BeneficiaryChildInformation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
public class BeneficiaryProfileView {

    private List<BeneficiaryProfilePaymentInformation> beneficiaryProfilePaymentInformations;
    private BeneficiaryProfile beneficiaryProfile;
    private List<BeneficiaryChildInformation> beneficiaryChildInformations = new ArrayList<>();

    private String base64PhotoData;

    private String base64SignatureData;

    public String getBase64PhotoData() {
        return base64PhotoData;
    }

    /**
     *
     * @param base64PhotoData
     */
    public void setBase64PhotoData(String base64PhotoData) {
        this.base64PhotoData = base64PhotoData;
    }

    public String getBase64SignatureData() {
        return base64SignatureData;
    }

    public void setBase64SignatureData(String base64SignatureData) {
        this.base64SignatureData = base64SignatureData;
    }

    /**
     *
     * @return
     */
    public List<BeneficiaryProfilePaymentInformation> getBeneficiaryProfilePaymentInformations() {
        return beneficiaryProfilePaymentInformations;
    }

    public void setBeneficiaryProfilePaymentInformations(List<BeneficiaryProfilePaymentInformation> beneficiaryProfilePaymentInformations) {
        this.beneficiaryProfilePaymentInformations = beneficiaryProfilePaymentInformations;
    }

    /**
     *
     * @return
     */
    public BeneficiaryProfile getBeneficiaryProfile() {
        return beneficiaryProfile;
    }

    public void setBeneficiaryProfile(BeneficiaryProfile beneficiaryProfile) {
        this.beneficiaryProfile = beneficiaryProfile;
    }

    public List<BeneficiaryChildInformation> getBeneficiaryChildInformations() {
        return beneficiaryChildInformations;
    }

    public void setBeneficiaryChildInformations(List<BeneficiaryChildInformation> beneficiaryChildInformations) {
        this.beneficiaryChildInformations = beneficiaryChildInformations;
    }

}

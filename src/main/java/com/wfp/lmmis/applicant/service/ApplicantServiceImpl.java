/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.dao.ApplicantDao;
import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.ApplicantAncInformation;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.report.data.ApplicantReportData;
import com.wfp.lmmis.report.data.ApplicantReportDataByLocation;
import com.wfp.lmmis.report.data.DoubleDippingReportData;
import com.wfp.lmmis.selection.controller.AncVerificationRespose;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.utility.JsonResult;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Service
@Transactional
public class ApplicantServiceImpl implements ApplicantService {

    @Autowired
    ApplicantDao applicantDao;

    @Override
    public Applicant getApplicant(Integer id) {
        return this.applicantDao.getApplicant(id);
    }

    @Override
    public Integer getApplicantIdByNid(BigInteger nid) {
        return this.applicantDao.getApplicantIdByNid(nid);
    }

    /**
     *
     * @param applicant
     * @throws ExceptionWrapper
     */
    @Override
    public void save(Applicant applicant) throws ExceptionWrapper {
        this.applicantDao.save(applicant);
    }

    @Override
    public void edit(Applicant applicant) {
        this.applicantDao.edit(applicant);
    }

//    @Override
//    public void delete (Integer districtId){
//        this.districtDao.delete(districtId);
//    }
//    
    /**
     *
     * @return
     */
    @Override
    public List<Applicant> getApplicantList() {
        return this.applicantDao.getApplicantList();
    }

    @Override
    public List<Object> getApplicantEligibilityList(List<ApplicationStatus> statusList, boolean isSearchByStatusList) {
        return this.applicantDao.getApplicantEligibilityList(statusList, isSearchByStatusList);
    }

    @Override
    public List<Object> getApplicantListForEditingBySearchParameter(Map parameter, int offset, int numofRecords) {
        return this.applicantDao.getApplicantListForEditingBySearchParameter(parameter, offset, numofRecords);
    }

    @Override
    public List<Object> getApplicantListBySearchParameter(Map parameter, int offset, int numofRecords) {
        return this.applicantDao.getApplicantListBySearchParameter(parameter, offset, numofRecords);
    }

    @Override
    public boolean checkUniqueNid(BigInteger nid, Integer appId) {
        return this.applicantDao.checkUniqueNid(nid, appId);
    }

    @Override
    public List<DoubleDippingReportData> getDoubleDippingFoundApplicants(Map parameter) {
        return this.applicantDao.getDoubleDippingFoundApplicants(parameter);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @Override
    public List<ApplicantReportData> getApplicantReportData(Map parameter) {
        return this.applicantDao.getApplicantReportData(parameter);
    }

    @Override
    public List<ApplicantReportDataByLocation> getApplicantSummaryReportData(Map parameter) {
        return this.applicantDao.getApplicantSummaryReportData(parameter);
    }

    @Override
    public List<ApplicantReportDataByLocation> getApplicantGroupReportData(Map parameter) {
        return this.applicantDao.getApplicantGroupReportData(parameter);
    }

    @Override
    public List<ApplicantReportData> getApplicantReportDataByIds(Map parameter) {
        return this.applicantDao.getApplicantReportDataByIds(parameter);
    }

    @Override
    public List<ApplicantReportData> getApplicantReportDataForPrioritizationPrint(Map parameter) {
        return this.applicantDao.getApplicantReportDataForPrioritizationPrint(parameter);
    }

    @Override
    public List<ApplicantReportData> getApplicantReportDataForPrint(Map parameter) {
        return this.applicantDao.getApplicantReportDataForPrint(parameter);
    }

    @Override
    public List<Object> getPrioritizedApplicantList(Map parameter, int offset, int numofRecords) {
        return this.applicantDao.getPrioritizedApplicantList(parameter, offset, numofRecords);
    }

    @Override
    public String updateApplicantStatus(Map parameter) {
        return this.applicantDao.updateApplicantStatus(parameter);
    }

    /**
     *
     * @param parameter
     * @throws ExceptionWrapper
     */
    @Override
    public void sendPrioritizedListForVerification(Map parameter) throws ExceptionWrapper {
        this.applicantDao.sendPrioritizedListForVerification(parameter);
    }

    @Override
    public boolean eligibilityToForwardUpazilaStage(int unionId, ApplicantType applicantType) throws ExceptionWrapper {
        return this.applicantDao.eligibilityToForwardUpazilaStage(unionId, applicantType);
    }

    /**
     *
     * @param unionId
     * @param applicantType
     * @return
     * @throws ExceptionWrapper
     */
    @Override
    public boolean eligibilityToForwardFromUpazilaStage(int unionId, ApplicantType applicantType) throws ExceptionWrapper {
        return this.applicantDao.eligibilityToForwardFromUpazilaStage(unionId, applicantType);
    }

    @Override
    public void allowRejectedApplicants(String nid) throws ExceptionWrapper {
        this.applicantDao.allowRejectedApplicants(nid);
    }

    @Override
    public JsonResult isExistOtherMis(String nid) {
        return applicantDao.isExistOtherMis(nid);
    }

    @Override
    public int getOtherMISchemeId(String nid) {
        return applicantDao.getOtherMISchemeId(nid);
    }

    /**
     *
     * @param accountNumber
     * @return
     */
    @Override
    public boolean checkUniqueAccountNumber(String accountNumber) {
        return applicantDao.checkUniqueAccountNumber(accountNumber);
    }

    @Override
    public boolean checkUniqueAccountNumberForBeneficiary(String accountNumber, int beneficiaryId) {
        return applicantDao.checkUniqueAccountNumberForBeneficiary(accountNumber, beneficiaryId);
    }

    @Override
    public List<ApplicantReportData> getBeneficiaryReportDataWithLMMISExist(Map parameter) {
        return applicantDao.getBeneficiaryReportDataWithLMMISExist(parameter);
    }

    public boolean checkUniqueAccountNumberAtApplicationSave(String accountNumber, Integer appId) {
        return applicantDao.checkUniqueAccountNumberAtApplicationSave(accountNumber, appId);
    }

    @Override
    public int updateApplicantAncStatus(AncVerificationRespose ancVerificationRespose) {
        return applicantDao.updateApplicantAncStatus(ancVerificationRespose);
    }

    public ApplicantAncInformation getAncInformationByApplicantId(int applicantId) {
        return applicantDao.getAncInformationByApplicantId(applicantId);
    }
}

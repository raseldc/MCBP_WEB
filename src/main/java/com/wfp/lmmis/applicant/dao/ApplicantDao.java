/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.report.data.ApplicantReportData;
import com.wfp.lmmis.report.data.ApplicantReportDataByLocation;
import com.wfp.lmmis.report.data.BeneficiaryReportData;
import com.wfp.lmmis.report.data.DoubleDippingReportData;
import com.wfp.lmmis.selection.controller.AncVerificationRespose;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.utility.JsonResult;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public interface ApplicantDao {

    public Applicant getApplicant(Integer id);

    public Integer getApplicantIdByNid(BigInteger nid);

    public void save(Applicant applicant) throws ExceptionWrapper;

    public void edit(Applicant applicant);
//    
//    public void delete (Integer districtId);
//    

    public List<Applicant> getApplicantList();

    public List<Object> getApplicantEligibilityList(List<ApplicationStatus> statusList, boolean isSearchByStatusList);

    /**
     *
     * @param parameter
     * @param offset
     * @param numofRecords
     * @return
     */
    public List<Object> getApplicantListForEditingBySearchParameter(Map parameter, int offset, int numofRecords);

    /**
     *
     * @param parameter
     * @param offset
     * @param numofRecords
     * @return
     */
    public List<Object> getApplicantListBySearchParameter(Map parameter, int offset, int numofRecords);

    /**
     *
     * @param parameter
     * @param offset
     * @param numofRecords
     * @return
     */
    public List<Object> getPrioritizedApplicantList(Map parameter, int offset, int numofRecords);

    public boolean checkUniqueNid(BigInteger nid, Integer appId);

    public List<DoubleDippingReportData> getDoubleDippingFoundApplicants(Map parameter);

    /**
     *
     * @param parameter
     * @return
     */
    public List<ApplicantReportData> getApplicantReportData(Map parameter);

    public List<ApplicantReportDataByLocation> getApplicantSummaryReportData(Map parameter);

    public List<ApplicantReportDataByLocation> getApplicantGroupReportData(Map parameter);

    /**
     *
     * @param parameter
     * @return
     */
    public List<ApplicantReportData> getApplicantReportDataByIds(Map parameter);

    public List<ApplicantReportData> getApplicantReportDataForPrioritizationPrint(Map parameter);

    public List<ApplicantReportData> getApplicantReportDataForPrint(Map parameter);

    public String updateApplicantStatus(Map parameter);

    public void sendPrioritizedListForVerification(Map parameter) throws ExceptionWrapper;

    public boolean eligibilityToForwardUpazilaStage(int unionId, ApplicantType applicantType) throws ExceptionWrapper;

    public boolean eligibilityToForwardFromUpazilaStage(int unionId, ApplicantType applicantType) throws ExceptionWrapper;

    /**
     *
     * @param nid
     * @throws ExceptionWrapper
     */
    public void allowRejectedApplicants(String nid) throws ExceptionWrapper;

    public JsonResult isExistOtherMis(String nid);

    public boolean checkUniqueAccountNumberForBeneficiary(String accountNumber, int beneficiaryId);

    public boolean checkUniqueAccountNumber(String accountNumber);

    public int getOtherMISchemeId(String nid);

    public List<ApplicantReportData> getBeneficiaryReportDataWithLMMISExist(Map parameter);

    public boolean checkUniqueAccountNumberAtApplicationSave(String accountNumber, Integer appId);

    public int updateApplicantAncStatus(AncVerificationRespose ancVerificationRespose);

}

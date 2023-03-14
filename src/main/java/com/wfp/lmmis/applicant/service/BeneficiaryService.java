/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.beneficiary.model.BeneficiaryChildInformation;
import com.wfp.lmmis.payroll.model.PaymentInformationHistory;
import com.wfp.lmmis.report.data.BeneficiaryReportData;
import com.wfp.lmmis.report.data.BeneficiaryReportDataByLocation;
import com.wfp.lmmis.report.data.DataEntryReportData;
import com.wfp.lmmis.report.data.DataEntryReportDataByUser;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.BeneficiaryInfo;
import com.wfp.lmmis.utility.BeneficiaryProfileView;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public interface BeneficiaryService {

    public Beneficiary getBeneficiary(Integer id);

    public Beneficiary getBeneficiaryByID(String benID);

    public void save(Beneficiary beneficiary);

    public void edit(Beneficiary beneficiary);

    public void edit(Beneficiary beneficiary, boolean fromPaymentDataUpdate);
//    
//    public void delete (Integer districtId);
//    

    public List<Beneficiary> getBeneficiaryList();

    /**
     *
     * @param parameter
     * @return
     */
    public List<BeneficiaryInfo> getbeneficiaryByGeolocation(Map parameter);

    public List<Object> getBeneficiaryListBySearchParameter(Map parameter, int offset, int numofRecords);

    public List<BeneficiaryReportData> getBeneficiaryReportData(Map parameter);

    public List<BeneficiaryReportData> getBeneficiaryReportDataWithPaymentInfo(Map parameter);

    public List<BeneficiaryReportDataByLocation> getBeneficiarySummaryReportData(Map parameter);

    /**
     *
     * @param parameter
     * @return
     */
    public List<BeneficiaryReportDataByLocation> getBeneficiaryGroupReportData(Map parameter);

    public List<BeneficiaryReportDataByLocation> getBeneficiaryUpazilaBasedReportData(Map parameter);

    public List<BeneficiaryReportDataByLocation> getBeneficiaryReportDataByPaymentType(Map parameter);

    public List<BeneficiaryReportData> getBeneficiaryReportDataWithoutMobileNo(Map parameter);

    public List<BeneficiaryReportData> getBeneficiaryReportDataWithoutAccNo(Map parameter);

    /**
     *
     * @param parameter
     * @return
     */
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithInvalidAccNo(Map parameter);

    public List<BeneficiaryReportData> getBeneficiaryReportDataWithDuplicateAccNo(Map parameter);

    public List<Beneficiary> getBeneficiaryListByUnionForCardPrinting(Map parameter);

    public boolean checkUniqueBenNid(BigInteger nid, Integer benId);

    public List<DataEntryReportData> getDataEntryDetailsReportData(Map parameter);

    public List<DataEntryReportDataByUser> getDataEntrySummaryReportData(Map paramerMap);

    /**
     *
     * @param parameter
     * @return
     */
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithLMMISExist(Map parameter);

    public int getOtherMISchemeId(String nid);

   
 
}

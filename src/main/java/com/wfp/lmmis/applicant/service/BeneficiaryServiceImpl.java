/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.service;

import com.wfp.lmmis.applicant.dao.BeneficiaryDao;
import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.beneficiary.model.BeneficiaryChildInformation;
import com.wfp.lmmis.report.data.BeneficiaryReportData;
import com.wfp.lmmis.report.data.BeneficiaryReportDataByLocation;
import com.wfp.lmmis.report.data.DataEntryReportData;
import com.wfp.lmmis.report.data.DataEntryReportDataByUser;
import com.wfp.lmmis.utility.BeneficiaryInfo;
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
public class BeneficiaryServiceImpl implements BeneficiaryService {

    @Autowired
    BeneficiaryDao beneficiaryDao;

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Beneficiary getBeneficiary(Integer id) {
        return this.beneficiaryDao.getBeneficiary(id);
    }

    @Override
    public Beneficiary getBeneficiaryByID(String benID) {
        return this.beneficiaryDao.getBeneficiaryByID(benID);
    }

    @Override
    public void save(Beneficiary beneficiary) {
        this.beneficiaryDao.save(beneficiary);
    }

    /**
     *
     * @param beneficiary
     */
    @Override
    public void edit(Beneficiary beneficiary) {
        this.beneficiaryDao.edit(beneficiary);
    }

    /**
     *
     * @param beneficiary
     * @param fromPaymentDataUpdate
     */
    @Override
    public void edit(Beneficiary beneficiary, boolean fromPaymentDataUpdate) {
        this.beneficiaryDao.edit(beneficiary, fromPaymentDataUpdate);
    }
//    @Override
//    public void delete (Integer districtId){
//        this.districtDao.delete(districtId);
//    }
//    

    @Override
    public List<Beneficiary> getBeneficiaryList() {
        return this.beneficiaryDao.getBeneficiaryList();
    }

    @Override
    public List<BeneficiaryInfo> getbeneficiaryByGeolocation(Map parameter) {
        return this.beneficiaryDao.getbeneficiaryByGeolocation(parameter);
    }

    @Override
    public List<Object> getBeneficiaryListBySearchParameter(Map parameter, int offset, int numofRecords) {
        return this.beneficiaryDao.getBeneficiaryListBySearchParameter(parameter, offset, numofRecords);
    }

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportData(Map parameter) {
        return this.beneficiaryDao.getBeneficiaryReportData(parameter);
    }

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithPaymentInfo(Map parameter) {
        return this.beneficiaryDao.getBeneficiaryReportDataWithPaymentInfo(parameter);
    }

    @Override
    public List<BeneficiaryReportDataByLocation> getBeneficiarySummaryReportData(Map parameter) {
        return this.beneficiaryDao.getBeneficiarySummaryReportData(parameter);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @Override
    public List<BeneficiaryReportDataByLocation> getBeneficiaryGroupReportData(Map parameter) {
        return this.beneficiaryDao.getBeneficiaryGroupReportData(parameter);
    }

    @Override
    public List<BeneficiaryReportDataByLocation> getBeneficiaryUpazilaBasedReportData(Map parameter) {
        return this.beneficiaryDao.getBeneficiaryUpazilaBasedReportData(parameter);
    }

    @Override
    public List<BeneficiaryReportDataByLocation> getBeneficiaryReportDataByPaymentType(Map parameter) {
        return this.beneficiaryDao.getBeneficiaryReportDataByPaymentType(parameter);
    }

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithoutMobileNo(Map parameter) {
        return this.beneficiaryDao.getBeneficiaryReportDataWithoutMobileNo(parameter);
    }

    /**
     *
     * @param parameter
     * @return
     */
    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithoutAccNo(Map parameter) {
        return this.beneficiaryDao.getBeneficiaryReportDataWithoutAccNo(parameter);
    }

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithInvalidAccNo(Map parameter) {
        return this.beneficiaryDao.getBeneficiaryReportDataWithInvalidAccNo(parameter);
    }

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithDuplicateAccNo(Map parameter) {
        return this.beneficiaryDao.getBeneficiaryReportDataWithDupplicateAccNo(parameter);
    }

    @Override
    public List<Beneficiary> getBeneficiaryListByUnionForCardPrinting(Map parameter) {
        return this.beneficiaryDao.getBeneficiaryListByUnionForCardPrinting(parameter);
    }

    @Override
    public boolean checkUniqueBenNid(BigInteger nid, Integer benId) {
        return this.beneficiaryDao.checkUniqueBenNid(nid, benId);
    }

    @Override
    public List<DataEntryReportData> getDataEntryDetailsReportData(Map parameter) {
        return this.beneficiaryDao.getDataEntryDetailsReportData(parameter);
    }

    @Override
    public List<DataEntryReportDataByUser> getDataEntrySummaryReportData(Map parameter) {
        return this.beneficiaryDao.getDataEntrySummaryReportData(parameter);
    }


    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithLMMISExist(Map parameter) {
        return beneficiaryDao.getBeneficiaryReportDataWithLMMISExist(parameter);
    }

    @Override
    public int getOtherMISchemeId(String nid) {
        return beneficiaryDao.getOtherMISchemeId(nid);

    }


   
}

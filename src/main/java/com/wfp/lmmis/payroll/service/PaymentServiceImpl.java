/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.service;

import com.wfp.lmmis.beneficiary.model.BeneficiaryView;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.payroll.dao.PaymentDao;
import com.wfp.lmmis.payroll.forms.PaymentGenerationForm;
import com.wfp.lmmis.payroll.forms.PaymentInfo;
import com.wfp.lmmis.payroll.model.Payment;
import com.wfp.lmmis.payroll.model.PaymentInfoForSpbmu;
import com.wfp.lmmis.payroll.model.PayrollSummary;
import com.wfp.lmmis.payroll.model.SupplementaryPayrollInfo;
import com.wfp.lmmis.report.data.PaymentReportData;
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
public class PaymentServiceImpl implements PaymentService
{

    @Autowired
    PaymentDao paymentDao;

    @Override
    public PayrollSummary getPayrollSummary(Integer id)
    {
        return this.paymentDao.getPayrollSummary(id);
    }

    @Override
    public void edit(PayrollSummary payrollSummary)
    {
        this.paymentDao.edit(payrollSummary);
    }

    @Override
    public Integer generatePaymentData(PaymentGenerationForm paymentGenerationForm) throws Exception
    {
        return this.paymentDao.generatePaymentData(paymentGenerationForm);
    }

    @Override
    public List<BeneficiaryView> checkPaymentDataOfBeneficiaries(SearchParameterForm searchParameterForm) throws ExceptionWrapper
    {
        return this.paymentDao.checkPaymentDataOfBeneficiaries(searchParameterForm);
    }

//    @Override
//    public void edit(PaymentCycle paymentCycle)
//    {
//        this.paymentDao.edit(paymentCycle);
//    }
//
//    @Override
//    public void delete(Integer id)
//    {
//        this.paymentDao.delete(id);
//    }
//    @Override
//    public List<PaymentCycle> getPaymentCycleList()
//    {
//        return this.paymentDao.getPaymentCycleList();
//    }
//    @Override
//    public List<ItemObject> getPaymentCycleIoList(boolean isSearchByFiscalYear, Integer fiscalYearId,
//            boolean isSearchByActive, boolean isActive)
//    {
//        return this.paymentDao.getPaymentCycleIoList(isSearchByFiscalYear, fiscalYearId,
//                isSearchByActive, isActive);
//    }
//
//    @Override
//    public List<ItemObject> getPaymentCycleIoList()
//    {
//        return this.paymentDao.getPaymentCycleIoList();
//    }
    @Override
    public List<Payment> getPaymentList()
    {
        return this.paymentDao.getPaymentList();
    }

    /**
     *
     * @param parameter
     * @return
     */
    @Override
    public List<PaymentReportData> getPaymentReportDataList(Map parameter)
    {
        return this.paymentDao.getPaymentReportDataList(parameter);
    }

    @Override
    public List<PaymentReportData> getPaymentReportDataCountList(Map parameter)
    {
        return this.paymentDao.getPaymentReportDataCountList(parameter);
    }

    @Override
    public List<PaymentReportData> getPaymentReportDataGroupList(Map parameter)
    {
        return this.paymentDao.getPaymentReportDataGroupList(parameter);
    }

    @Override
    public List<PaymentInfo> getPendingPaymentList(boolean isSearchByScheme, Integer schemeId,
            boolean isSearchByPaymentCycle, Integer paymentCycleId)
    {
        return this.paymentDao.getPendingPaymentList(isSearchByScheme, schemeId, isSearchByPaymentCycle, paymentCycleId);
    }

    @Override
    public List<Object> getPaymentListBySearchParameter(Map parameter, int offset, int numofRecords)
    {
        return this.paymentDao.getPaymentListBySearchParameter(parameter, offset, numofRecords);
    }

    @Override
    public List<Object> getPayrollSummaryListBySearchParameter(Map parameter, int beginIndex, int pageSize)
    {
        return this.paymentDao.getPayrollSummaryListBySearchParameter(parameter, beginIndex, pageSize);
    }

    /**
     *
     * @param paymentInfo
     */
    @Override
    public void editPaymentInfo(PaymentInfo paymentInfo)
    {
        this.paymentDao.editPaymentInfo(paymentInfo);
    }

    @Override
    public void setPaymentStatusToOne(Map parameter)
    {
        this.paymentDao.setPaymentStatusToOne(parameter);
    }
    
    @Override
    public void editPaymentStatus(Map parameter)
    {
        this.paymentDao.editPaymentStatus(parameter);
    }

    @Override
    public void updatePaymentSuccess(Integer schemeId, Integer paymentCycleId)
    {
        this.paymentDao.updatePaymentSuccess(schemeId, paymentCycleId);
    }

    @Override
    public List<Object[]> getUnpaidListByPaymentCycle(Integer paymentCycleId, Integer upazilaId) throws ExceptionWrapper
    {
        return this.paymentDao.getUnpaidListByPaymentCycle(paymentCycleId, upazilaId);
    }
    
    @Override
    public List<Object[]> getEFTBouncedPaymentList(Integer paymentCycleId, Integer upazilaId) throws ExceptionWrapper
    {
        return this.paymentDao.getEFTBouncedPaymentList(paymentCycleId, upazilaId);
    }

    /**
     *
     * @param parameter
     * @param offset
     * @param numofRecords
     * @return
     */
    @Override
    public List<Object[]> getbeforePaymentGenerationList(Map parameter, int offset, int numofRecords)
    {
        return this.paymentDao.getbeforePaymentGenerationList(parameter, offset, numofRecords);
    }

    @Override
    public BigInteger getbeforePaymentGenerationCount(Map parameter)
    {
        return this.paymentDao.getbeforePaymentGenerationCount(parameter);
    }

    @Override
    public List<Object[]> getbeforePaymentGenerationUnionList(Map parameter)
    {
        return this.paymentDao.getbeforePaymentGenerationUnionList(parameter);
    }

    /**
     *
     * @param payrollSummaryId
     * @return
     * @throws ExceptionWrapper
     */
    @Override
    public List<PaymentInfoForSpbmu> getPaymentInfoForSpbmu(Integer payrollSummaryId) throws ExceptionWrapper
    {
        return this.paymentDao.getPaymentInfoForSpbmu(payrollSummaryId);
    }

    @Override
    public boolean updateExportStatusOfPayrollSummaryList(String payrollSummaryIdList) throws ExceptionWrapper
    {
        return this.paymentDao.updateExportStatusOfPayrollSummaryList(payrollSummaryIdList);
    }
    
    /**
     *
     * @param parameter
     * @param beginIndex
     * @param pageSize
     * @return
     * @throws ExceptionWrapper
     */
    @Override
    public List<Object> getSupplementaryPayrollInfo(Map parameter, int beginIndex, int pageSize) throws ExceptionWrapper
    {
        return this.paymentDao.getSupplementaryPayrollInfo(parameter, beginIndex, pageSize);
    }
    
    @Override
    public void updatePayrollInfoForSupplementary(Integer paymentCycleId, Integer upazilaId) throws ExceptionWrapper
    {
        this.paymentDao.updatePayrollInfoForSupplementary(paymentCycleId, upazilaId);
    }
}

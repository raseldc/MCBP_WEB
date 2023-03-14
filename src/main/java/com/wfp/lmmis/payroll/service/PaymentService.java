/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.service;

import com.wfp.lmmis.beneficiary.model.BeneficiaryView;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.payroll.forms.PaymentGenerationForm;
import com.wfp.lmmis.payroll.forms.PaymentInfo;
import com.wfp.lmmis.payroll.model.Payment;
import com.wfp.lmmis.payroll.model.PaymentInfoForSpbmu;
import com.wfp.lmmis.payroll.model.PayrollSummary;
import com.wfp.lmmis.report.data.PaymentReportData;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Philip
 */
public interface PaymentService
{

    public PayrollSummary getPayrollSummary(Integer id);
//

    public void edit(PayrollSummary payrollSummary);
//

    public Integer generatePaymentData(PaymentGenerationForm paymentGenerationForm) throws Exception;

    public List<BeneficiaryView> checkPaymentDataOfBeneficiaries(SearchParameterForm searchParameterForm) throws ExceptionWrapper;

//    public void edit(PaymentCycle paymentCycle);
//
//    public void delete(Integer id);
//
    public List<Payment> getPaymentList();

    /**
     *
     * @param parameter
     * @return
     */
    public List<PaymentReportData> getPaymentReportDataList(Map parameter);

    public List<PaymentReportData> getPaymentReportDataCountList(Map parameter);

    public List<PaymentReportData> getPaymentReportDataGroupList(Map parameter);

//    public List<ItemObject> getPaymentCycleIoList(boolean isSearchByFiscalYear, Integer fiscalYearId,
//            boolean isSearchByActive, boolean isActive);
//
    public List<PaymentInfo> getPendingPaymentList(boolean isSearchByScheme, Integer schemeId,
            boolean isSearchByPaymentCycle, Integer paymentCycleId);

    public List<Object> getPaymentListBySearchParameter(Map parameter, int offset, int numofRecords);

    public List<Object> getPayrollSummaryListBySearchParameter(Map parameter, int beginIndex, int pageSize);

    public void editPaymentInfo(PaymentInfo paymentInfo);

    public void setPaymentStatusToOne(Map parameter);

    public void editPaymentStatus(Map parameter);

    public void updatePaymentSuccess(Integer schemeId, Integer paymentCycleId);
    
    /**
     *
     * @param paymentCycleId
     * @param upazilaId
     * @return
     * @throws ExceptionWrapper
     */
    public List<Object[]> getUnpaidListByPaymentCycle(Integer paymentCycleId, Integer upazilaId) throws ExceptionWrapper;

    public List<Object[]> getEFTBouncedPaymentList(Integer paymentCycleId, Integer upazilaId) throws ExceptionWrapper;

    public List<Object[]> getbeforePaymentGenerationList(Map parameter, int offset, int numofRecords);

    public BigInteger getbeforePaymentGenerationCount(Map parameter);

    public List<Object[]> getbeforePaymentGenerationUnionList(Map parameter);

    public List<PaymentInfoForSpbmu> getPaymentInfoForSpbmu(Integer payrollSummaryId) throws ExceptionWrapper;

    public boolean updateExportStatusOfPayrollSummaryList(String payrollSummaryIdList) throws ExceptionWrapper;

    public List<Object> getSupplementaryPayrollInfo(Map parameter, int beginIndex, int pageSize) throws ExceptionWrapper;
    
    public void updatePayrollInfoForSupplementary(Integer paymentCycleId, Integer upazilaId) throws ExceptionWrapper;
}

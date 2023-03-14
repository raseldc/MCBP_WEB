/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.dao;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.beneficiary.model.BeneficiaryView;
import com.wfp.lmmis.enums.ApplicantType;
import static com.wfp.lmmis.enums.PaymentTypeEnum.BANKING;
import static com.wfp.lmmis.enums.PaymentTypeEnum.MOBILEBANKING;
import static com.wfp.lmmis.enums.PaymentTypeEnum.POSTOFFICE;
import com.wfp.lmmis.enums.PayrollListType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.payroll.forms.PaymentGenerationForm;
import com.wfp.lmmis.payroll.forms.PaymentInfo;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.model.Payment;
import com.wfp.lmmis.payroll.model.PaymentCycle;
import com.wfp.lmmis.payroll.model.PaymentInfoForSpbmu;
import com.wfp.lmmis.payroll.model.PaymentView;
import com.wfp.lmmis.payroll.model.PayrollSummary;
import com.wfp.lmmis.payroll.model.SupplementaryPayrollInfo;
import com.wfp.lmmis.report.data.PaymentReportData;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.types.BeneficiaryStatus;
import com.wfp.lmmis.types.PaymentStatus;
import com.wfp.lmmis.types.PayrollStatus;
import com.wfp.lmmis.utility.CommonUtility;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Collections.list;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDaoImpl implements PaymentDao {

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public PayrollSummary getPayrollSummary(Integer id) {
        return (PayrollSummary) this.sessionFactory.getCurrentSession().get(PayrollSummary.class, id);
    }

    @Override
    public void edit(PayrollSummary payrollSummary) {
        try {
            System.out.println("payrollSummary id = " + payrollSummary.getId());
            payrollSummary.setModifiedBy(payrollSummary.getModifiedBy());
            payrollSummary.setModificationDate(payrollSummary.getModificationDate());
            this.sessionFactory.getCurrentSession().update(payrollSummary);
            if (payrollSummary.getPayrollStatus() == PayrollStatus.APPROVED) {
                String querySt = "update payment o "
                        + "set o.approval_status = 1, o.approval_comments = '" + payrollSummary.getApprovalComments() + "' "
                        + " where o.payroll_summary_id =" + payrollSummary.getId();
                SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(querySt);
                query.executeUpdate();
                System.out.println("PAYMENT TABLE APPROVED = ");
            }
        } catch (Exception e) {
            System.out.println("exc in save: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<BeneficiaryView> checkPaymentDataOfBeneficiaries(SearchParameterForm searchParameterForm) throws ExceptionWrapper {
        System.out.println("time 1 " + Calendar.getInstance().getTime());
        ApplicantType applicantType = searchParameterForm.getApplicantType();
        String querySt = "from BeneficiaryView o where o.schemeId = " + searchParameterForm.getScheme().getId()
                + " and o.beneficiaryStatus = " + BeneficiaryStatus.ACTIVE.ordinal();
        if (applicantType == ApplicantType.REGULAR) {
            querySt += " and o.upazilaId = " + searchParameterForm.getUpazila().getId();
            querySt += " and o.applicantType = " + ApplicantType.REGULAR.ordinal();
        }
        if (applicantType == ApplicantType.BGMEA) {
            querySt += " and o.applicantType = " + ApplicantType.BGMEA.ordinal();
        }
        if (applicantType == ApplicantType.BKMEA) {
            querySt += " and o.applicantType = " + ApplicantType.BKMEA.ordinal();
        }
        System.out.println("query check " + querySt);
        List<BeneficiaryView> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        System.out.println("time 2 " + Calendar.getInstance().getTime());
        System.out.println("benList = " + list.size());
        if (list.isEmpty()) {
            return list;
        }
        List<BeneficiaryView> benList = new ArrayList<>();

        for (BeneficiaryView beneficiary : list) {
            if (beneficiary.getPaymentType() == null) {
                benList.add(beneficiary);
                System.out.println("nid = " + beneficiary.getNid());
                System.out.println("mobile = " + beneficiary.getMobileNo());
            }
        }

        if (benList.isEmpty()) {
            return null;
        } else {
            return benList;
        }

    }

    @Override
    public Integer generatePaymentData(PaymentGenerationForm paymentGenerationForm) throws Exception {
        Integer divisionId = paymentGenerationForm.getSearchParameterForm().getDivision() != null ? paymentGenerationForm.getSearchParameterForm().getDivision().getId() : null;
        Integer districtId = paymentGenerationForm.getSearchParameterForm().getDistrict() != null ? paymentGenerationForm.getSearchParameterForm().getDistrict().getId() : null;
        Integer upazilaId = paymentGenerationForm.getSearchParameterForm().getUpazila() != null ? paymentGenerationForm.getSearchParameterForm().getUpazila().getId() : null;
        String querySt = "call paymentGeneration(" + paymentGenerationForm.getSearchParameterForm().getScheme().getId()
                + "," + paymentGenerationForm.getSearchParameterForm().getPaymentCycle().getId()
                + "," + divisionId
                + "," + districtId
                + "," + upazilaId
                + "," + paymentGenerationForm.getSearchParameterForm().getPayrollListType().ordinal()
                + "," + paymentGenerationForm.getCreatedBy().getId()
                + ")";
        System.out.println("query " + querySt);
        Integer returnValue = (Integer) sessionFactory.getCurrentSession().createSQLQuery(querySt).uniqueResult();
        System.out.println("id is " + returnValue);
        return returnValue;
    }

//    @Override
//    public PayrollSummary generatePaymentData(PaymentGenerationForm paymentGenerationForm) throws Exception
//    {
//        // if already has payroll summary delete it and all its child
//        ApplicantType applicantType = paymentGenerationForm.getSearchParameterForm().getApplicantType();
//        if (paymentGenerationForm.getPayrollSummary().getId() != null)
//        {
//            System.out.println("remove started");
//            String removeQuerySt = "delete from payment where payroll_summary_id=" + paymentGenerationForm.getPayrollSummary().getId();
//            SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(removeQuerySt);
//            query.executeUpdate();
//            System.out.println("Remove completed");
//        }
//
//        String querySt = "from Beneficiary o where o.scheme.id = " + paymentGenerationForm.getSearchParameterForm().getScheme().getId()
//                + " and o.fiscalYear.id = " + paymentGenerationForm.getSearchParameterForm().getFiscalYear().getId()
//                //                + " and o.permanentUpazila.id = " + paymentGenerationForm.getSearchParameterForm().getUpazila().getId()
//                + " and o.beneficiaryStatus = " + BeneficiaryStatus.ACTIVE.ordinal();
//        if (applicantType == ApplicantType.REGULAR)
//        {
//            querySt += " and o.presentUpazila.id = " + paymentGenerationForm.getSearchParameterForm().getUpazila().getId();
//            querySt += " and o.applicantType = " + ApplicantType.REGULAR.ordinal();
//        }
//        if (applicantType == ApplicantType.BGMEA)
//        {
//            querySt += " and o.applicantType = " + ApplicantType.BGMEA.ordinal();
//        }
//        if (applicantType == ApplicantType.BKMEA)
//        {
//            querySt += " and o.applicantType = " + ApplicantType.BKMEA.ordinal();
//        }
//        System.out.println("querySt = " + querySt);
//        List<Beneficiary> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
//
//        System.out.println("ben size = " + list.size());
//
//        PaymentCycle paymentCycle = paymentGenerationForm.getSearchParameterForm().getPaymentCycle();
//        System.out.println("paymentCycle = " + paymentCycle.getAllowanceAmount());
//        System.out.println("month count = " + paymentCycle.getMonthCount());
//        Integer totalBeneficiary = 0;
//        float totalAllowance = 0.0f; // paymentCycle.getAllowanceAmount() * list.size();
//
//        PayrollSummary payrollSummary = new PayrollSummary();
//        if (paymentGenerationForm.getPayrollSummary().getId() == null)
//        {
//            payrollSummary.setScheme(paymentGenerationForm.getSearchParameterForm().getScheme());
//            payrollSummary.setPaymentCycle(paymentGenerationForm.getSearchParameterForm().getPaymentCycle());
//            if (applicantType == ApplicantType.REGULAR)
//            {
//                payrollSummary.setDivision(paymentGenerationForm.getSearchParameterForm().getDivision());
//                payrollSummary.setDistrict(paymentGenerationForm.getSearchParameterForm().getDistrict());
//                payrollSummary.setUpazilla(paymentGenerationForm.getSearchParameterForm().getUpazila());
//                payrollSummary.setApplicantType(applicantType);
//            }
//            else
//            {
//                payrollSummary.setApplicantType(applicantType);
//            }
//            payrollSummary.setAllowancePerBeneficiary(paymentCycle.getAllowanceAmount() * paymentCycle.getMonthCount());
//            payrollSummary.setPayrollStatus(PayrollStatus.SAVED);
//            payrollSummary.setCreatedBy(paymentGenerationForm.getCreatedBy());
//            payrollSummary.setCreationDate(Calendar.getInstance());
//            try
//            {
//                this.sessionFactory.getCurrentSession().save(payrollSummary);
//                System.out.println("payroll summary_id = " + payrollSummary.getId());
//            }
//            catch (Exception e)
//            {
//                System.out.println("TEST22");
//                e.printStackTrace();
//                throw e;
//            }
//
//        }
//        else
//        {
//            payrollSummary = getPayrollSummary(paymentGenerationForm.getPayrollSummary().getId());
//        }
//
//        for (Beneficiary beneficiary : list)
//        {
//            System.out.println("payment...");
//            Payment payment = new Payment();
//            payment.setScheme(beneficiary.getScheme());
//            payment.setPaymentCycle(paymentCycle);
//            payment.setMinistryCode(ApplicationConstants.MINISTRY_CODE);
//            System.out.println("allowance = " + paymentCycle.getAllowanceAmount() * paymentCycle.getMonthCount());
//            payment.setAllowanceAmount(paymentCycle.getAllowanceAmount() * paymentCycle.getMonthCount());
//            totalBeneficiary++;
//            totalAllowance += (paymentCycle.getAllowanceAmount() * paymentCycle.getMonthCount());
//            System.out.println("totalAllowance = " + totalAllowance);
//
//            payment.setPaymentStatus(PaymentStatus.PENDING);
//            payment.setPaymentType(beneficiary.getPaymentType());
//            payment.setBenefiaryName(beneficiary.getFullNameInBangla());
//            payment.setMobileNumber(beneficiary.getMobileNo());
//            if (beneficiary.getPaymentType() == null)
//            {
//                continue;
//            }
//            if (PaymentTypeEnum.BANKING == payment.getPaymentType())
//            {
//                payment.setBank(beneficiary.getBank().getNameInBangla());
//                payment.setBranch(beneficiary.getBranch().getNameInBangla());
//            }
//            else
//            {
//                payment.setBank(beneficiary.getMobileBankingProvider().getNameInBangla());
//            }
//            payment.setAccountType(beneficiary.getAccountType());
//            payment.setAccountNumber(beneficiary.getAccountNo());
//
//            payment.setDivision(beneficiary.getPermanentDivision());
//            payment.setDistrict(beneficiary.getPermanentDistrict());
//            payment.setUpazilla(beneficiary.getPermanentUpazila());
//            payment.setUnion(beneficiary.getPermanentUnion());
//
//            payment.setDivisionCode(beneficiary.getPermanentDivision().getCode());
//            payment.setDistrictCode(beneficiary.getPermanentDistrict().getCode());
//            payment.setUpazilaCode(beneficiary.getPermanentUpazila().getCode());
//            payment.setUnionCode(beneficiary.getPermanentUnion().getCode());
//
//            payment.setComments(paymentGenerationForm.getComments());
//            payment.setBeneficiary(beneficiary);
//            payment.setApprovalStatus(null);
//            payment.setPayrollSummary(payrollSummary);
//            payment.setCreatedBy(paymentGenerationForm.getCreatedBy());
//            payment.setCreationDate(Calendar.getInstance());
//
//            try
//            {
//                this.sessionFactory.getCurrentSession().save(payment);
//            }
//            catch (Exception e)
//            {
//                System.out.println("TEST");
//                e.printStackTrace();
//                throw e;
//            }
//        }
//
//        payrollSummary.setTotalBeneficiary(totalBeneficiary);
//        payrollSummary.setTotalAllowance(totalAllowance);
//
//        this.sessionFactory.getCurrentSession().update(payrollSummary);
//
//        return payrollSummary;
//    }
//    @Override
//    public void edit(PaymentCycle editedPaymentCycle)
//    {
//        PaymentCycle paymentcycle = (PaymentCycle) this.sessionFactory.getCurrentSession().get(PaymentCycle.class, editedPaymentCycle.getId());
//        if (paymentcycle != null)
//        {
//            paymentcycle.setNameInBangla(editedPaymentCycle.getNameInBangla());
//            paymentcycle.setNameInEnglish(editedPaymentCycle.getNameInEnglish());
//            paymentcycle.setFiscalYear(editedPaymentCycle.getFiscalYear());
//            paymentcycle.setStartDate(editedPaymentCycle.getStartDate());
//            paymentcycle.setEndDate(editedPaymentCycle.getEndDate());
//            paymentcycle.setMinistry(editedPaymentCycle.getMinistry());
//            paymentcycle.setScheme(editedPaymentCycle.getScheme());
//            paymentcycle.setAllowanceAmount(editedPaymentCycle.getAllowanceAmount());
//            paymentcycle.setApprovalStatus(editedPaymentCycle.isApprovalStatus());
//            paymentcycle.setApprovalComment(editedPaymentCycle.getApprovalComment());
//            paymentcycle.setActive(editedPaymentCycle.isActive());
//
//            paymentcycle.setModifiedBy(editedPaymentCycle.getModifiedBy());
//            paymentcycle.setModificationDate(editedPaymentCycle.getModificationDate());
//        }
//        this.sessionFactory.getCurrentSession().update(paymentcycle);
//    }
//
//    @Override
//    public void delete(Integer id)
//    {
//        PaymentCycle paymentcycle = (PaymentCycle) this.sessionFactory.getCurrentSession().get(PaymentCycle.class, id);
//        if (paymentcycle != null)
//        {
//            this.sessionFactory.getCurrentSession().delete(paymentcycle);
//        }
//    }
//
    @Override
    public List<Payment> getPaymentList() {
        try {
            @SuppressWarnings("unchecked")
            List<Payment> list = sessionFactory.getCurrentSession().createQuery("from Payment").list();
            return list;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PaymentReportData> getPaymentReportDataList(Map parameter) {
        try {
            Integer paymentCycleId = !parameter.get("paymentCycleId").equals("") ? (Integer) parameter.get("paymentCycleId") : null;
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            Integer unionId = parameter.get("unionId") != null ? (Integer) parameter.get("unionId") : null;
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            PayrollListType payrollListType = parameter.get("payrollListType") != null ? (PayrollListType) parameter.get("payrollListType") : null;
            PaymentStatus paymentStatus = parameter.get("paymentStatus") != null ? (PaymentStatus) parameter.get("paymentStatus") : null;
            String orderBy = (String) parameter.get("orderBy");
            @SuppressWarnings("unchecked")
            String querySt;
            if ("bn".equals(locale)) {
                querySt = "select new com.wfp.lmmis.report.data.PaymentReportData(o.fullNameInBangla, o.nid, o.fatherName, o.motherName, o.spouseName,"
                        + "o.mobileNo, o.accountNo, o.bankBn, o.branchBn, o.allowanceAmount, o.divisionNameBn, o.districtNameBn, o.upazilaNameBn, o.unionNameBn) "
                        + "from PaymentView o where 0=0 ";
            } else {
                querySt = "select new com.wfp.lmmis.report.data.PaymentReportData(o.fullNameInEnglish, o.nid, o.fatherName, o.motherName, o.spouseName,"
                        + "o.mobileNo, o.accountNo, o.bankEn, o.branchEn, o.allowanceAmount, o.divisionNameEn, o.districtNameEn, o.upazilaNameEn, o.unionNameEn) "
                        + "from PaymentView o where 0=0 ";
            }
//
//            if (schemeId != null && schemeId != 0)
//            {
//                querySt += " AND o.schemeId = " + schemeId;
//            }
            if (paymentCycleId != null && paymentCycleId != 0) {
                querySt += " AND o.paymentCycleId = " + paymentCycleId;
            }
            querySt += " and o.payrollListType = " + payrollListType.ordinal();
            if (paymentStatus != null) {
                switch (paymentStatus) {
//                    case SENTTOIBASS:
//                        querySt += " AND o.paymentStatus = 1";
//                        break;
//                    case SUCCESSFULPAYMENT:
//                        querySt += " AND o.paymentStatus = 1 and o.eftReferenceNumber is null";
//                        break;
//                    case RETURNEDEFTS:
//                        querySt += " AND o.paymentStatus = 1 and o.eftReferenceNumber is not null";
//                        break;
//                    case BLOCKEDPAYMENTS:
//                        querySt += " AND o.paymentStatus = 0 and (o.returnedCode = 3 or o.returnedCode = 4 or o.returnedCode = 5 or o.returnedCode = 6)";
//                        break;
//                    case APPROVEDPAYROLLS:
//                        querySt += " AND o.approvalStatus = 1 and o.paymentStatus = 0 and o.returnedCode is null";
//                        break;
//                    default:
//                        break;
                    case SENTTOIBASS:
                        querySt += " AND o.paymentStatus = 1 AND o.payrollSummary.isExportedToSpbmu = 1";
                        break;
                    case SUCCESSFULPAYMENT:
                        querySt += " AND o.paymentStatus = 1 and o.eftReferenceNumber is null AND o.payrollSummary.isExportedToSpbmu = 1";
                        break;
                    case RETURNEDEFTS:
                        querySt += " AND o.paymentStatus = 1 and o.eftReferenceNumber is not null AND o.payrollSummary.isExportedToSpbmu = 1";
                        break;
                    case BLOCKEDPAYMENTS:
                        querySt += " AND o.payrollSummary.isExportedToSpbmu = 1 AND o.paymentStatus = 0 and (o.returnedCode = 3 or o.returnedCode = 4 or o.returnedCode = 5 or o.returnedCode = 6)";
                        break;
                    case APPROVEDPAYROLLS:
                        querySt += " AND o.approvalStatus = 1 and o.paymentStatus = 0 and o.returnedCode is null";
                        break;
                    default:
                        break;
                }
            }
            if (divisionId != null && divisionId != 0) {
                querySt += " AND o.divisionId = " + divisionId;
            }
            if (districtId != null && districtId != 0) {
                querySt += " AND o.districtId = " + districtId;
            }
            if (upazilaId != null && upazilaId != 0) {
                querySt += " AND o.upazilaId = " + upazilaId;
            }
            if (unionId != null && unionId != 0) {
                querySt += " AND o.unionId = " + unionId;
            }
            querySt += " order by o.unionNameEn";
            if (orderBy.equals("1")) {
                querySt += " ,o.nid asc";
            } else {
                querySt += " ,o.fullNameInEnglish";
            }
            List<PaymentReportData> list;
            Query query = sessionFactory.getCurrentSession().createQuery(querySt);
            list = query.list();
            for (PaymentReportData paymentReportData : list) {

                if ("bn".equals(locale)) {
                    paymentReportData.setNid(CommonUtility.getNumberInBangla(paymentReportData.getNid()));
                    paymentReportData.setMobileNo(CommonUtility.getNumberInBangla(paymentReportData.getMobileNo()));
                    paymentReportData.setAccountNo(CommonUtility.getNumberInBangla(paymentReportData.getAccountNo()));
                    paymentReportData.setAllowanceAmount(CommonUtility.getNumberInBangla(paymentReportData.getAllowanceAmount()));

                    if (divisionId == null) {
                        paymentReportData.setUnion("বিভাগ : " + paymentReportData.getDivision() + ",   জেলা : " + paymentReportData.getDistrict() + ",   উপজেলা : " + paymentReportData.getUpazila() + ",   ইউনিয়ন : " + paymentReportData.getUnion());
                    } else if (districtId == null) {
                        paymentReportData.setUnion("জেলা : " + paymentReportData.getDistrict() + ",   উপজেলা : " + paymentReportData.getUpazila() + ",   ইউনিয়ন : " + paymentReportData.getUnion());
                    } else if (upazilaId == null) {
                        paymentReportData.setUnion("উপজেলা : " + paymentReportData.getUpazila() + ",   ইউনিয়ন : " + paymentReportData.getUnion());
                    } else if (unionId == null) {
                        paymentReportData.setUnion("ইউনিয়ন : " + paymentReportData.getUnion()); // uinon bangla text appended
                    }
                } else {
                    if (divisionId == null) {
                        paymentReportData.setUnion("Division : " + paymentReportData.getDivision() + ",   District : " + paymentReportData.getDistrict() + ",   Upazila : " + paymentReportData.getUpazila() + ",   Union : " + paymentReportData.getUnion());
                    } else if (districtId == null) {
                        paymentReportData.setUnion("District : " + paymentReportData.getDistrict() + ",   Upazila : " + paymentReportData.getUpazila() + ",   Union : " + paymentReportData.getUnion());
                    } else if (upazilaId == null) {
                        paymentReportData.setUnion("Upazila : " + paymentReportData.getUpazila() + ",   Union : " + paymentReportData.getUnion());
                    } else if (unionId == null) {
                        paymentReportData.setUnion("Union : " + paymentReportData.getUnion()); // uinon bangla text appended
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PaymentReportData> getPaymentReportDataCountList(Map parameter) {
        try {
            Integer paymentCycleId = !parameter.get("paymentCycleId").equals("") ? (Integer) parameter.get("paymentCycleId") : null;
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            Integer unionId = parameter.get("unionId") != null ? (Integer) parameter.get("unionId") : null;
            PayrollListType payrollListType = parameter.get("payrollListType") != null ? (PayrollListType) parameter.get("payrollListType") : null;
            PaymentStatus paymentStatus = parameter.get("paymentStatus") != null ? (PaymentStatus) parameter.get("paymentStatus") : null;

            @SuppressWarnings("unchecked")
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            String querySt;
            if ("bn".equals(locale)) {
                querySt = "select new com.wfp.lmmis.report.data.PaymentReportData(o.paymentCycle.nameInBangla, "
                        + "o.division.nameInBangla, o.district.nameInBangla, o.upazilla.nameInBangla, o.union.nameInBangla, "
                        + "COUNT(*), SUM(o.allowanceAmount)) "
                        + "from Payment o where 0=0";
            } else {
                querySt = "select new com.wfp.lmmis.report.data.PaymentReportData(o.paymentCycle.nameInEnglish, "
                        + "o.division.nameInEnglish, o.district.nameInEnglish, o.upazilla.nameInEnglish, o.union.nameInEnglish, "
                        + "COUNT(*), SUM(o.allowanceAmount)) "
                        + "from Payment o where 0=0";
            }

            if (paymentCycleId != null && paymentCycleId != 0) {
                querySt += " AND o.paymentCycle.id = " + paymentCycleId;
            }
            if (payrollListType != null) {
                querySt += " and o.payrollSummary.payrollListType = " + payrollListType.ordinal();
            }

            if (divisionId != null && divisionId != 0) {
                querySt += " AND o.division.id = " + divisionId;
            }
            if (districtId != null && districtId != 0) {
                querySt += " AND o.district.id = " + districtId;
            }
            if (upazilaId != null && upazilaId != 0) {
                querySt += " AND o.upazilla.id = " + upazilaId;
            }
            if (unionId != null && unionId != 0) {
                querySt += " AND o.union.id = " + unionId;
            }
            if (paymentStatus != null) {
                switch (paymentStatus) {
                    case SENTTOIBASS:
                        querySt += " AND o.paymentStatus = 1 AND o.payrollSummary.isExportedToSpbmu = 1";
                        break;
                    case SUCCESSFULPAYMENT:
                        querySt += " AND o.paymentStatus = 1 and o.eftReferenceNumber is null AND o.payrollSummary.isExportedToSpbmu = 1";
                        break;
                    case RETURNEDEFTS:
                        querySt += " AND o.paymentStatus = 1 and o.eftReferenceNumber is not null AND o.payrollSummary.isExportedToSpbmu = 1";
                        break;
                    case BLOCKEDPAYMENTS:
                        querySt += " AND o.payrollSummary.isExportedToSpbmu = 1 AND o.paymentStatus = 0 and (o.returnedCode = 3 or o.returnedCode = 4 or o.returnedCode = 5 or o.returnedCode = 6)";
                        break;
                    case APPROVEDPAYROLLS:
                        querySt += " AND o.approvalStatus = 1 and o.paymentStatus = 0 and o.returnedCode is null";
                        break;
                    default:
                        break;
                }
            }
            querySt += " GROUP BY  o.union.nameInBangla,";

            if (divisionId != null && divisionId != 0) {
                querySt += " o.district.id";
                if (districtId != null && districtId != 0) {
                    querySt += ", o.upazilla.id";
                    if (upazilaId != null && upazilaId != 0) {
                        querySt += ", o.union.id";
                    } else {
                        querySt += ", o.upazilla.id";
                    }
                } else {
                    querySt += ", o.district.id";
                }
            } else {
                querySt += " o.division.id";
            }

            if (unionId != null && unionId != 0) {
                querySt += ", o.union.id";
            }

            System.out.println("querySt = " + querySt);
            List<PaymentReportData> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            double total = 0;
            Integer totalBen = 0;
            DecimalFormat formatter = new DecimalFormat("#,##0.00");
            for (PaymentReportData paymentReportData : list) {
                totalBen += Integer.parseInt(paymentReportData.getBeneficiaryCount());
                total += Double.parseDouble(paymentReportData.getAllowanceAmount());
                if ("bn".equals(locale)) {
                    paymentReportData.setBeneficiaryCount(CommonUtility.getNumberInBangla(paymentReportData.getBeneficiaryCount() + ""));
                    paymentReportData.setAllowanceAmount(CommonUtility.getNumberInBangla(formatter.format(new Double(paymentReportData.getAllowanceAmount())) + ""));
                    paymentReportData.setTotalAllowanceAmount(CommonUtility.getNumberInBangla(formatter.format(total)));
                    paymentReportData.setBenTotal(CommonUtility.getNumberInBangla(totalBen.toString()));
                } else {
                    paymentReportData.setAllowanceAmount(formatter.format(new Double(paymentReportData.getAllowanceAmount())));
                    paymentReportData.setTotalAllowanceAmount(formatter.format(total));
                    paymentReportData.setBenTotal(totalBen.toString());
                }
            }

            return list;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PaymentReportData> getPaymentReportDataGroupList(Map parameter) {
        System.out.println("Group Report DAO");
        System.out.println("schemeId = " + parameter.get("schemeId"));
        System.out.println("paymentCycleId = " + parameter.get("paymentCycleId"));
        System.out.println("divisionId = " + parameter.get("divisionId"));

        try {
            Integer schemeId = !parameter.get("schemeId").equals("") ? (Integer) parameter.get("schemeId") : null;
            Integer paymentCycleId = !parameter.get("paymentCycleId").equals("") ? (Integer) parameter.get("paymentCycleId") : null;
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            Integer unionId = parameter.get("unionId") != null ? (Integer) parameter.get("unionId") : null;
            ApplicantType applicantType = parameter.get("applicantType") != null ? (ApplicantType) parameter.get("applicantType") : null;
            PaymentStatus paymentStatus = parameter.get("paymentStatus") != null ? (PaymentStatus) parameter.get("paymentStatus") : null;

            @SuppressWarnings("unchecked")
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            String querySt;
            if ("bn".equals(locale)) {
                querySt = "select new com.wfp.lmmis.report.data.PaymentReportData(o.paymentCycle.nameInBangla, o.division.nameInBangla,"
                        + " o.district.nameInBangla, o.upazilla.nameInBangla, COUNT(distinct o.union.id), COUNT(o.id), SUM(o.allowanceAmount))"
                        + " from Payment o where  o.beneficiary.beneficiaryStatus=0 ";
            } else {
                querySt = "select new com.wfp.lmmis.report.data.PaymentReportData(o.paymentCycle.nameInEnglish, o.division.nameInEnglish,"
                        + " o.district.nameInEnglish, o.upazilla.nameInEnglish, COUNT(distinct o.union.id), COUNT(o.id), SUM(o.allowanceAmount))"
                        + " from Payment o where  o.beneficiary.beneficiaryStatus=0 ";
            }

            // condition (where clause)
            if (schemeId != null && schemeId != 0) {
                querySt += " AND o.scheme.id = " + schemeId;
            }
            if (paymentCycleId != null && paymentCycleId != 0) {
                querySt += " AND o.paymentCycle.id = " + paymentCycleId;
            }
            if (applicantType == null) {
                querySt += " and o.payrollSummary.applicantType = " + ApplicantType.REGULAR.ordinal();
            } else {
                querySt += " and o.payrollSummary.applicantType = " + applicantType.ordinal();
            }
            if (divisionId != null && divisionId != 0) {
                querySt += " AND o.division.id = " + divisionId;
            }
            if (districtId != null && districtId != 0) {
                querySt += " AND o.district.id = " + districtId;
            }
            if (upazilaId != null && upazilaId != 0) {
                querySt += " AND o.upazilla.id = " + upazilaId;
            }
            if (unionId != null && unionId != 0) {
                querySt += " AND o.union.id = " + unionId;
            }
            if (paymentStatus != null) {
                querySt += " AND o.paymentStatus = " + paymentStatus.ordinal();
            }
            querySt += " GROUP BY o.upazilla.id order by o.district.id";
//
//            querySt += " GROUP BY ";
//            // group by part
//            if (divisionId != null && divisionId != 0)
//            {
//                querySt += " o.district.id";
//            }
//            else
//            {
//                querySt += " o.division.id";
//            }
//
//            if (districtId != null && districtId != 0)
//            {
//                querySt += ", o.upazilla.id";
//            }
//            else
//            {
//                querySt += ", o.district.id";
//            }
//
//            if (upazilaId != null && upazilaId != 0)
//            {
//                querySt += ", o.union.id";
//            }
//            else
//            {
//                querySt += ", o.upazilla.id";
//            }
//
//            if (unionId != null && unionId != 0)
//            {
//                querySt += ", o.union.id";
//            }

            List<PaymentReportData> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            System.out.println("After query2");
            System.out.println("size = " + list.size());
            return list;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

//    @Override
//    public List<ItemObject> getPaymentCycleIoList(boolean isSearchByFiscalYear, Integer fiscalYearId,
//            boolean isSearchByActive, boolean isActive)
//    {
//        try
//        {
//            @SuppressWarnings("unchecked")
//            String querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish) from PaymentCycle o where 0 = 0 ";
//            if (isSearchByFiscalYear)
//            {
//                querySt += " and o.fiscalYear.id = " + fiscalYearId;
//            }
//            if (isSearchByActive)
//            {
//                querySt += " and o.active = " + isActive;
//            }
//
//            List<ItemObject> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
//            return list;
//        }
//        catch (Exception e)
//        {
//            System.out.println("exc=" + e.getMessage());
//            return null;
//        }
//    }
//
    @Override
    public List<PaymentInfo> getPendingPaymentList(boolean isSearchByScheme, Integer schemeId,
            boolean isSearchByPaymentCycle, Integer paymentCycleId) {
        System.out.println("in dao pending payment list = ");
        try {
            String querySt = "select o.scheme, o.paymentCycle.fiscalYear, o.paymentCycle, count(*), sum(o.allowanceAmount) from Payment o where 0=0 ";
            if (isSearchByScheme) {
                querySt += " and o.scheme.id = " + schemeId;
            }
            if (isSearchByPaymentCycle) {
                querySt += " and o.paymentCycle.id = " + paymentCycleId;
            }

            querySt += " and o.approvalStatus IS NULL ";
            querySt += " group by o.scheme, o.paymentCycle";

            List<Object[]> itemObjects = sessionFactory.getCurrentSession().createQuery(querySt).list();

            List<PaymentInfo> pendingPaymentList = new ArrayList<>();
            for (Object[] itemObject : itemObjects) {
                PaymentInfo paymentInfo = new PaymentInfo();

                paymentInfo.setScheme((Scheme) itemObject[0]);
                paymentInfo.setFiscalYear((FiscalYear) itemObject[1]);
                paymentInfo.setPaymentCycle((PaymentCycle) itemObject[2]);
                paymentInfo.setTotalBeneficiary(((Long) itemObject[3]).intValue());
                paymentInfo.setTotalAllowanceAmount(((Double) itemObject[4]).floatValue());

                pendingPaymentList.add(paymentInfo);
            }
            return pendingPaymentList;
        } catch (Exception e) {
            e.printStackTrace();
            //logger.infoer(e.getMessage());
            throw e;
        }

    }

    @Override
    public List<Object> getPaymentListBySearchParameter(Map parameter, int offset, int numofRecords) {
        try {
            String nid = parameter.get("nid") != null ? (String) parameter.get("nid") : null;
            String accountNo = parameter.get("accountNo") != null ? (String) parameter.get("accountNo") : null;
            Integer paymentCycleId = parameter.get("paymentCycleId") != null ? (Integer) parameter.get("paymentCycleId") : null;
            Integer payrollSummaryId = parameter.get("payrollSummaryId") != null ? (Integer) parameter.get("payrollSummaryId") : null;
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            Integer unionId = parameter.get("unionId") != null ? (Integer) parameter.get("unionId") : null;
            Integer bgmeaFactoryId = parameter.get("bgmeaFactoryId") != null ? (Integer) parameter.get("bgmeaFactoryId") : null;
            Integer bkmeaFactoryId = parameter.get("bkmeaFactoryId") != null ? (Integer) parameter.get("bkmeaFactoryId") : null;
            ApplicantType applicantType = parameter.get("applicantType") != null ? (ApplicantType) parameter.get("applicantType") : null;
            System.out.println("applicantType = " + applicantType);
            @SuppressWarnings("unchecked")
            String mainQuerySt = "select o from PaymentView o where 0=0 ";
            String countQuerySt = "select count(distinct o.id) from PaymentView o where 0=0 ";

            String querySt = "";

            if (paymentCycleId != null && paymentCycleId != 0) {
                querySt += " AND o.paymentCycleId = " + paymentCycleId;
            }
            if (payrollSummaryId != null && payrollSummaryId != 0) {
                querySt += " AND o.payrollSummaryId = " + payrollSummaryId;
            }
//            querySt += " AND o.applicantType = " + applicantType.ordinal();

            if (bgmeaFactoryId != null && bgmeaFactoryId != 0) {
                querySt += " and o.factoryId = " + bgmeaFactoryId;
            }
            if (bkmeaFactoryId != null && bkmeaFactoryId != 0) {
                querySt += " and o.factoryId = " + bkmeaFactoryId;
            }
            if (divisionId != null && divisionId != 0) {
                querySt += " AND o.divisionId = " + divisionId;
            }
            if (districtId != null && districtId != 0) {
                querySt += " AND o.districtId = " + districtId;
            }
            if (upazilaId != null && upazilaId != 0) {
                querySt += " AND o.upazilaId= " + upazilaId;
            }
            if (unionId != null && unionId != 0) {
                querySt += " AND o.unionId = " + unionId;
            }
            if (!"".equals(nid) && nid != null) {
                querySt += " AND o.nid like '%" + nid + "%' ";
            }
            if (!"".equals(accountNo) && accountNo != null) {
                querySt += " AND o.accountNo like '%" + accountNo + "%' ";
            }
            System.out.println("payment query = " + mainQuerySt + querySt);
            List<PaymentView> list = sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt).setFirstResult(offset).setMaxResults(numofRecords).list();
            long count = (Long) sessionFactory.getCurrentSession().createQuery(countQuerySt + querySt).list().get(0);
            System.out.println("payment size = " + list.size());
            System.out.println("payment total = " + count);

            List<Object> result = new ArrayList<Object>();
            result.add(list);
            result.add(count);
            result.add(count); // ????

            return result;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Object> getPayrollSummaryListBySearchParameter(Map parameter, int beginIndex, int pageSize) {
        List<Object> result = new ArrayList<Object>();
        try {
//            Integer schemeId = !parameter.get("schemeId").equals("") ? (Integer) parameter.get("schemeId") : null;
            Integer fiscalYearId = parameter.get("fiscalYearId") != null ? (Integer) parameter.get("fiscalYearId") : null;
            Integer paymentCycleId = parameter.get("paymentCycleId") != null ? (Integer) parameter.get("paymentCycleId") : null;
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            String payrollStatus = parameter.get("payrollStatus") != null ? (String) parameter.get("payrollStatus") : null;
            PayrollListType payrollListType = parameter.get("payrollListType") != null ? (PayrollListType) parameter.get("payrollListType") : null;

            @SuppressWarnings("unchecked")
            String mainQuerySt = "from PayrollSummary o where 0=0 ";
            String countQuerySt = "select count(distinct o.id) from PayrollSummary o where 0=0 ";

            String querySt = "";

//            if (schemeId != null && schemeId != 0)
//            {
//                querySt += " AND o.scheme.id = " + schemeId;
//            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                querySt += " AND o.paymentCycle.fiscalYear.id = " + fiscalYearId + " AND o.paymentCycle.deleted = 0";
            }
            if (paymentCycleId != null && paymentCycleId != 0) {
                querySt += " AND o.paymentCycle.id = " + paymentCycleId;
            }
            if (payrollListType != null) {
                querySt += " and o.payrollListType = " + payrollListType.ordinal();
            }

            if (divisionId != null && divisionId != 0) {
                querySt += " AND o.division.id = " + divisionId;
            }
            if (districtId != null && districtId != 0) {
                querySt += " AND o.district.id = " + districtId;
            }
            if (upazilaId != null && upazilaId != 0) {
                querySt += " AND o.upazilla.id = " + upazilaId;
            }
            if (payrollStatus != null) {
                querySt += " AND o.payrollStatus IN (" + payrollStatus + ")";
            }
            System.out.println("payroll query " + mainQuerySt + querySt);
            List<PayrollSummary> list = sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt).setFirstResult(beginIndex).setMaxResults(pageSize).list();
            for (PayrollSummary ps : list) {
                Hibernate.initialize(ps.getCreatedBy());
                Hibernate.initialize(ps.getModifiedBy());
            }
            Integer count = sessionFactory.getCurrentSession().
                    createQuery(mainQuerySt + querySt).list().size();
            result.add(list);
            result.add(count);
            result.add(count);
            return result;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void editPaymentInfo(PaymentInfo paymentInfo) {
        try {
            String querySt = "update payment o join scheme s on o.scheme_code = s.code join payment_cycle pc on o.cycle_id = pc.id "
                    + "set o.approval_status = " + paymentInfo.getApprovalStatus() + ", o.approval_comments = '" + paymentInfo.getApprovalComments() + "' "
                    + "where o.approval_status IS NULL and s.id = " + paymentInfo.getScheme().getId() + " and pc.id = " + paymentInfo.getPaymentCycle().getId();
            SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(querySt);
            query.executeUpdate();

            if (paymentInfo.getApprovalStatus().equals(Boolean.TRUE)) // accepted
            {
//                querySt = "update payment_cycle o set o.active = 0 where o.id = " + paymentInfo.getPaymentCycle().getId();
//                query = sessionFactory.getCurrentSession().createSQLQuery(querySt);
//                query.executeUpdate();
//                System.out.println("payment cycle is deactived");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setPaymentStatusToOne(Map parameter) {
        Integer paymentCycleId = !parameter.get("paymentCycleId").equals("") ? (Integer) parameter.get("paymentCycleId") : null;
        System.out.println("paymentCycleId ============ " + paymentCycleId);
        String querySt = "update Payment o set o.paymentStatus = 1 where o.paymentCycle.id=" + paymentCycleId + " and o.approvalStatus=1";
        Query query = sessionFactory.getCurrentSession().createQuery(querySt);
        query.executeUpdate();
    }

    @Override
    public void editPaymentStatus(Map parameter) {
        Integer paymentCycleId = !parameter.get("paymentCycleId").equals("") ? (Integer) parameter.get("paymentCycleId") : null;
        Integer beneficiaryId = !parameter.get("beneficiaryId").equals("") ? (Integer) parameter.get("beneficiaryId") : null;
        Integer paymentUid = parameter.get("paymentUid") != null ? (Integer) parameter.get("paymentUid") : null;
        String eftReferenceNumber = parameter.get("eftReferenceNumber") != null ? parameter.get("eftReferenceNumber").toString() : null;
        Integer returnedCode = parameter.get("returnedCode") != null ? (Integer) parameter.get("returnedCode") : null;
        String returnedText = parameter.get("returnedText") != null ? parameter.get("returnedText").toString() : null;
        int paymentStatus = 0;
        if (returnedCode == 2) //returned code=2 => iBass returned, 3,4=>invalid,duplicate a/c no.
        {
            paymentStatus = 1;
        }
        try {
            String querySt = "update Payment o set";
            if (eftReferenceNumber != null) {
                querySt += " o.eftReferenceNumber = '" + eftReferenceNumber + "',";
            }
            querySt += " o.paymentUid = " + paymentUid + ", o.paymentStatus =" + paymentStatus + ","
                    + " o.returnedCode =" + returnedCode + ", o.returnedText='" + returnedText + "'"
                    + " where o.beneficiary.Id = " + beneficiaryId + " and o.paymentCycle.id=" + paymentCycleId;

            Query query = sessionFactory.getCurrentSession().createQuery(querySt);
            query.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updatePaymentSuccess(Integer schemeId, Integer paymentCycleId) {
        try {
            String querySt = "update payment a \n"
                    + "join payroll_summary b on a.payroll_summary_id = b.id \n"
                    + "set a.payment_status = 1 where  b.payment_cycle_id = " + paymentCycleId + " and b.scheme_id = " + schemeId + " and a.payment_status = 0";
            System.out.println("query is " + querySt);
            SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(querySt);
            query.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method and updatePayrollInfo method will not be used in later which
     * will be separated as supplementaryPayrollRural_.jsp
     *
     * @param paymentCycleId
     * @param upazilaId
     * @return
     * @throws ExceptionWrapper
     */
    @Override
    public List<Object[]> getUnpaidListByPaymentCycle(Integer paymentCycleId, Integer upazilaId) throws ExceptionWrapper {
        try {
            System.out.println("upazilaId = dao " + upazilaId);
            String query;
            if (upazilaId != null) {
                query = "select p.eftReferenceNumber, p.nid, p.paymentUid, p.id from Payment p join p.upazilla u where p.paymentCycle.id=:paymentCycleId and p.returnedCode=2 and u.id=" + upazilaId; // Only iBas returned
            } else {
                query = "select p.eftReferenceNumber, p.nid, p.paymentUid, p.id from Payment p where p.paymentCycle.id=:paymentCycleId and p.returnedCode=2"; // Only iBas returned
            }
            List<Object[]> paymentList = (List<Object[]>) this.sessionFactory.getCurrentSession().createQuery(query).setParameter("paymentCycleId", paymentCycleId).list();
            updatePayrollInfo(paymentList); //update payroll info 
            return paymentList;
        } catch (HibernateException e) {
            throw new ExceptionWrapper("Payment data update faild: " + e.getMessage());
        }
    }

    private void updatePayrollInfo(List<Object[]> paymentList) {
        try {
            System.out.println("paymentList.size() = " + paymentList.size());
            String benName, accountNo, bankEn = null, bankBn = null, branchBn = null, branchEn = null, routingNo = null, query;
            Integer branchId = null;
            for (Object[] row : paymentList) {
                Integer paymentId = (Integer) row[3];
                BigInteger nid = new BigInteger(row[1].toString());
                Beneficiary b = (Beneficiary) this.sessionFactory.getCurrentSession().createQuery("select o from Beneficiary o where o.nid=" + nid).uniqueResult();
                if (b != null && b.getPaymentType() != null) {
                    benName = b.getFullNameInEnglish();
                    accountNo = b.getAccountNo();
                    switch (b.getPaymentType()) {
                        case BANKING:
                            bankBn = b.getBank().getNameInBangla();
                            bankEn = b.getBank().getNameInEnglish();
                            branchBn = b.getBranch().getNameInBangla();
                            branchEn = b.getBranch().getNameInEnglish();
                            routingNo = b.getBranch().getRoutingNumber();
                            branchId = b.getBranch().getId();
                            break;
                        case MOBILEBANKING:
                            bankBn = b.getMobileBankingProvider().getNameInBangla();
                            bankEn = b.getMobileBankingProvider().getNameInEnglish();
                            branchBn = "";
                            branchEn = "";
                            routingNo = b.getMobileBankingProvider().getRoutingNumber();
                            branchId = b.getMobileBankingProvider().getId();
                            break;
                        case POSTOFFICE:
                            bankBn = "পোস্ট অফিস";
                            ;
                            bankEn = "Post Office";
                            branchBn = b.getPostOfficeBranch().getNameInBangla();
                            branchEn = b.getPostOfficeBranch().getNameInEnglish();
                            routingNo = b.getPostOfficeBranch().getRoutingNumber() + "";
                            branchId = b.getPostOfficeBranch().getId();
                            break;
                        default:
                            break;
                    }
                    query = "update payment p set p.beneficiary_name=:benName, p.account_number=:accNo, p.bank_bn=:bankBn, p.bank=:bankEn, p.branch_bn=:branchBn, p.branch=:branchEn, p.branch_id=:branchId, p.routing_number=:routingNo where p.id=:paymentId";
                    this.sessionFactory.getCurrentSession().createSQLQuery(query)
                            .setParameter("benName", benName)
                            .setParameter("accNo", accountNo)
                            .setParameter("bankBn", bankBn)
                            .setParameter("bankEn", bankEn)
                            .setParameter("branchBn", branchBn)
                            .setParameter("branchEn", branchEn)
                            .setParameter("branchId", branchId)
                            .setParameter("routingNo", routingNo)
                            .setParameter("paymentId", paymentId)
                            .executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Object[]> getEFTBouncedPaymentList(Integer paymentCycleId, Integer upazilaId) throws ExceptionWrapper {
        try {
            System.out.println("upazilaId = dao " + upazilaId);
            String query;
            if (upazilaId != null) {
                query = "select p.eftReferenceNumber, p.nid, p.paymentUid, p.id from Payment p join p.upazilla u where p.paymentCycle.id=:paymentCycleId and p.returnedCode=2 and u.id=" + upazilaId; // Only iBas returned
            } else {
                query = "select p.eftReferenceNumber, p.nid, p.paymentUid, p.id from Payment p where p.paymentCycle.id=:paymentCycleId and p.returnedCode=2"; // Only iBas returned
            }
            List<Object[]> paymentList = (List<Object[]>) this.sessionFactory.getCurrentSession().createQuery(query).setParameter("paymentCycleId", paymentCycleId).list();
            return paymentList;
        } catch (HibernateException e) {
            throw new ExceptionWrapper("Payment data update faild: " + e.getMessage());
        }
    }

    @Override
    public void updatePayrollInfoForSupplementary(Integer paymentCycleId, Integer upazilaId) throws ExceptionWrapper {
        try {
            List<Object[]> paymentList = getEFTBouncedPaymentList(paymentCycleId, upazilaId);
            if (paymentList.isEmpty()) {
                throw new ExceptionWrapper("No EFT returned data found!!!");
            }
            String benName, accountNo, bankEn = null, bankBn = null, branchBn = null, branchEn = null, routingNo = null, query;
            Integer branchId = null;
            for (Object[] row : paymentList) {
                Integer paymentId = (Integer) row[3];
                BigInteger nid = new BigInteger(row[1].toString());
                Beneficiary b = (Beneficiary) this.sessionFactory.getCurrentSession().createQuery("select o from Beneficiary o where o.nid=" + nid).uniqueResult();
                if (b != null && b.getPaymentType() != null) {
                    benName = b.getFullNameInEnglish();
                    accountNo = b.getAccountNo();
                    switch (b.getPaymentType()) {
                        case BANKING:
                            bankBn = b.getBank().getNameInBangla();
                            bankEn = b.getBank().getNameInEnglish();
                            branchBn = b.getBranch().getNameInBangla();
                            branchEn = b.getBranch().getNameInEnglish();
                            routingNo = b.getBranch().getRoutingNumber();
                            branchId = b.getBranch().getId();
                            break;
                        case MOBILEBANKING:
                            bankBn = b.getMobileBankingProvider().getNameInBangla();
                            bankEn = b.getMobileBankingProvider().getNameInEnglish();
                            branchBn = "";
                            branchEn = "";
                            routingNo = b.getMobileBankingProvider().getRoutingNumber();
                            branchId = b.getMobileBankingProvider().getId();
                            break;
                        case POSTOFFICE:
                            bankBn = "পোস্ট অফিস";
                            ;
                            bankEn = "Post Office";
                            branchBn = b.getPostOfficeBranch().getNameInBangla();
                            branchEn = b.getPostOfficeBranch().getNameInEnglish();
                            routingNo = b.getPostOfficeBranch().getRoutingNumber() + "";
                            branchId = b.getPostOfficeBranch().getId();
                            break;
                        default:
                            break;
                    }
                    query = "update payment p set p.beneficiary_name=:benName, p.account_number=:accNo, p.bank_bn=:bankBn, p.bank=:bankEn, p.branch_bn=:branchBn, p.branch=:branchEn, p.branch_id=:branchId, p.routing_number=:routingNo where p.id=:paymentId";
                    this.sessionFactory.getCurrentSession().createSQLQuery(query)
                            .setParameter("benName", benName)
                            .setParameter("accNo", accountNo)
                            .setParameter("bankBn", bankBn)
                            .setParameter("bankEn", bankEn)
                            .setParameter("branchBn", branchBn)
                            .setParameter("branchEn", branchEn)
                            .setParameter("branchId", branchId)
                            .setParameter("routingNo", routingNo)
                            .setParameter("paymentId", paymentId)
                            .executeUpdate();
                }
            }
        } catch (HibernateException e) {
            throw new ExceptionWrapper("Exception occured while updating data!!\nException :" + e.getMessage());
        }
    }

    @Override
    public List<Object[]> getbeforePaymentGenerationList(Map parameter, int offset, int numofRecords) {
        try {
            Integer schemeId = !parameter.get("schemeId").equals("") ? (Integer) parameter.get("schemeId") : null;
            Integer paymentCycleId = parameter.get("paymentCycleId") != null ? (Integer) parameter.get("paymentCycleId") : null;
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            ApplicantType applicantType = parameter.get("applicantType") != null ? ApplicantType.valueOf(parameter.get("applicantType").toString()) : null;
            String querySt = "call beforePaymentGenerationList(" + schemeId
                    + "," + paymentCycleId
                    + "," + divisionId
                    + "," + districtId
                    + "," + upazilaId
                    + "," + applicantType.ordinal()
                    + "," + offset
                    + "," + numofRecords
                    + ")";
            System.out.println("query " + querySt);
            List<Object[]> list = sessionFactory.getCurrentSession().createSQLQuery(querySt).list();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public BigInteger getbeforePaymentGenerationCount(Map parameter) {
        Integer schemeId = !parameter.get("schemeId").equals("") ? (Integer) parameter.get("schemeId") : null;
        Integer paymentCycleId = parameter.get("paymentCycleId") != null ? (Integer) parameter.get("paymentCycleId") : null;
        Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
        ApplicantType applicantType = parameter.get("applicantType") != null ? ApplicantType.valueOf(parameter.get("applicantType").toString()) : null;
        String countQuerySt = "SELECT count(distinct(b.Id)) from beneficiary b \n"
                + "left join payment p on b.Id = p.beneficiary_id\n"
                + "left join payroll_summary ps on p.payroll_summary_id = ps.id\n"
                + "where \n"
                + " b.scheme_id = " + schemeId + " and b.`status` = 0 and b.applicant_type = " + applicantType.ordinal() + "\n"
                + " and case when " + applicantType.ordinal() + " = 0 then b.present_upazila_id = " + upazilaId + " else true end and\n"
                + "CHAR_LENGTH(b.account_no)>=11 and b.account_no is not null \n"
                + "and b.Id not in\n"
                + "(select benf.Id from beneficiary benf \n"
                + "join payment pay on benf.Id = pay.beneficiary_id\n"
                + "where\n"
                + "pay.id is not null and (FIND_IN_SET(pay.cycle_id, func_payment_cycle(" + paymentCycleId + "))>0)\n"
                + "#(select func_payment_cycle(" + paymentCycleId + ")) \n"
                + "and (pay.returned_code is null or pay.returned_code = 2 or pay.is_granted=1))\n"
                + "and b.Id not in(\n"
                + "select ben.Id from beneficiary ben\n"
                + "where ben.account_no in\n"
                + "(SELECT b.account_no from beneficiary b \n"
                + "where b.`status` = 0 and b.scheme_id=" + schemeId + "\n"
                + "group by account_no having count(*)>1) \n"
                + ")";
        System.out.println("count " + countQuerySt);
        BigInteger returnValue = (BigInteger) sessionFactory.getCurrentSession().
                createSQLQuery(countQuerySt).uniqueResult();

        return returnValue;
    }

    @Override
    public List<Object[]> getbeforePaymentGenerationUnionList(Map parameter) {
        Integer paymentCycleId = parameter.get("paymentCycleId") != null ? (Integer) parameter.get("paymentCycleId") : null;
        Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
        PayrollListType payrollListType = parameter.get("payrollListType") != null ? (PayrollListType) parameter.get("payrollListType") : null;
        String countQuerySt;
        ApplicantType applicantType;
        if (payrollListType == PayrollListType.BGMEA || payrollListType == PayrollListType.BKMEA) {
            applicantType = payrollListType == PayrollListType.BGMEA ? ApplicantType.BGMEA : ApplicantType.BKMEA;
            countQuerySt = "SELECT b.present_union_id, count(distinct(b.Id)), u.name_in_bangla, u.name_in_english,cast(b.applicant_type as unsigned)  from beneficiary b "
                    + " left join payment p on b.Id = p.beneficiary_id"
                    + " left join payroll_summary ps on p.payroll_summary_id = ps.id"
                    + " join factory u on b.factory_id = u.id"
                    + " left join branch br on b.branch_id = br.id "
                    + " join (select ben.Id from beneficiary ben "
                    + " where ben.`status` = 0 "
                    + " group by ben.id having count(1)=1) voss on voss.id = b.id "
                    + " where b.`status` = 0  and b.applicant_type =" + applicantType.ordinal()
                    + " and CHAR_LENGTH(b.account_no)>=11 and b.account_no is not null "
                    + " and (b.bank_id is not null or b.mobile_banking_provider_id is not null or b.post_office_branch_id is not null) \n"
                    + " and (case when b.bank_id is not null then b.branch_id is not null else true end) "
                    //                    + " and CHAR_LENGTH(b.account_no)>=11 and b.account_no is not null  and ("
                    //                    + " (br.routing_number = '070272684' and b.branch_id != 3140 and CHAR_LENGTH(b.account_no)=13) \n"
                    //                    + "or (b.bank_id in (18) and CHAR_LENGTH(b.account_no)=11) \n"
                    //                    + "or (b.bank_id in (10,11,34,41,42,44,47) and CHAR_LENGTH(b.account_no)=13) \n"
                    //                    + "or (b.bank_id in (15,33,36,39,54) and CHAR_LENGTH(b.account_no)=14) \n"
                    //                    + "or (b.bank_id in (35) and CHAR_LENGTH(b.account_no)=15) \n"
                    //                    + "or (b.bank_id in (20,23,24) and CHAR_LENGTH(b.account_no)=16) \n"
                    //                    + "or (b.mobile_banking_provider_id in (1) and CHAR_LENGTH(b.account_no)=11) \n"
                    //                    + "or (b.mobile_banking_provider_id in (2,26) and CHAR_LENGTH(b.account_no)=12) \n"
                    //                    + "or (b.post_office_branch_id is not null)  "
                    //                    + ") \n"

                    + " and b.Id not in"
                    + " (select benf.Id from beneficiary benf join payment pay on benf.Id = pay.beneficiary_id"
                    + " where pay.id is not null and (FIND_IN_SET(pay.cycle_id, func_payment_cycle(" + paymentCycleId + "))>0)"
                    + " and (pay.returned_code is null or pay.returned_code = 2 or pay.returned_code = 3 or pay.returned_code = 4 or pay.is_granted=1))"
                    + " AND b.enrollment_date <= (SELECT pc.end_date FROM payment_cycle pc WHERE pc.id=" + paymentCycleId + ")"
                    + " group by b.factory_id,b.present_union_id";
        } else {
            if (payrollListType == PayrollListType.UNION) {
                applicantType = ApplicantType.UNION;
            } else if (payrollListType == PayrollListType.MUNICIPAL) {
                applicantType = ApplicantType.MUNICIPAL;
            } else {
                applicantType = ApplicantType.CITYCORPORATION;
            }
            countQuerySt = "SELECT b.present_union_id, count(distinct(b.Id)), u.name_in_bangla, u.name_in_english,cast(b.applicant_type as unsigned) "
                    + "from beneficiary b \n"
                    // + "left join payment p on b.Id = p.beneficiary_id\n" // no use at query
                    //  + "left join payroll_summary ps on p.payroll_summary_id = ps.id\n" // no use at query
                    + "join unions u on b.present_union_id = u.id\n"
                    // + " left join branch br on b.branch_id = br.id\n"// no use at query
                    + " join (select ben.Id from beneficiary ben "
                    + " where "
                    + "ben.present_upazila_id = " + upazilaId + " and "
                    + " ben.`status` = 0 "
                    + " group by ben.id having count(1)=1) voss on voss.id = b.id "
                    + "where \n"
                    + " b.`status` = 0  and b.applicant_type =" + applicantType.ordinal();
            countQuerySt += " and b.present_upazila_id = " + upazilaId + " \n"
                    + " and CHAR_LENGTH(b.account_no)>=11 and b.account_no is not null and \n"
                    + " (b.bank_id is not null or b.mobile_banking_provider_id is not null or b.post_office_branch_id is not null) \n"
                    + " and (case when b.bank_id is not null then b.branch_id is not null else true end) "
                    + " and b.Id not in\n"
                    //                    + " and CHAR_LENGTH(b.account_no)>=11 and b.account_no is not null  and ("
                    //                    + " (br.routing_number = '070272684' and b.branch_id != 3140 and CHAR_LENGTH(b.account_no)=13) \n"
                    //                    + "or (b.bank_id in (18) and CHAR_LENGTH(b.account_no)=11) \n"
                    //                    + "or (b.bank_id in (10,11,34,41,42,44,47) and CHAR_LENGTH(b.account_no)=13) \n"
                    //                    + "or (b.bank_id in (15,33,36,39,54) and CHAR_LENGTH(b.account_no)=14) \n"
                    //                    + "or (b.bank_id in (35) and CHAR_LENGTH(b.account_no)=15) \n"
                    //                    + "or (b.bank_id in (20,23,24) and CHAR_LENGTH(b.account_no)=16) \n"
                    //                    + "or (b.mobile_banking_provider_id in (1) and CHAR_LENGTH(b.account_no)=11) \n"
                    //                    + "or (b.mobile_banking_provider_id in (2,26) and CHAR_LENGTH(b.account_no)=12) \n"
                    //                    + "or (b.post_office_branch_id is not null)  "
                    //                    + ") \n"
                    //                    + "and b.Id not in\n"

                    + "(select benf.Id from beneficiary benf \n"
                    + "join payment pay on benf.Id = pay.beneficiary_id\n"
                    + "where\n"
                    + "benf.present_upazila_id = " + upazilaId + " and "
                    + "pay.id is not null and (FIND_IN_SET(pay.cycle_id, func_payment_cycle(" + paymentCycleId + "))>0)\n"
                    + "and (pay.returned_code is null or pay.returned_code = 2 or pay.returned_code = 3 or pay.returned_code = 4 or pay.is_granted=1))\n"
                    + " AND b.enrollment_date <= (SELECT pc.end_date FROM payment_cycle pc WHERE pc.id=" + paymentCycleId + ")\n"
                    + " group by b.present_union_id";

        }
        System.out.println("count " + countQuerySt);
        List<Object[]> list = sessionFactory.getCurrentSession().
                createSQLQuery(countQuerySt).list();

        return list;
    }

    @Override
    public List<PaymentInfoForSpbmu> getPaymentInfoForSpbmu(Integer payrollSummaryId) throws ExceptionWrapper {
        List<PaymentInfoForSpbmu> paymentInfoForSpbmuList = null;
        try {
            String querySt = "select new com.wfp.lmmis.payroll.model.PaymentInfoForSpbmu(o.benefiaryName, o.accountNumber, o.accountType.nameInEnglish, o.allowanceAmount, "
                    + "o.bank, o.benefiaryName, o.branch, o.paymentCycle.id, cast(o.districtCode as integer), cast(o.divisionCode as integer), cast(o.unionCode as integer), cast(o.upazilaCode as integer), o.id, o.ministryCode,"
                    + "o.mobileNumber, cast(o.nid as long), 1,  "
                    + "case when o.paymentType=0 then 'Banking' when o.paymentType=1 then 'Mobile Banking' when o.paymentType=2 then 'Post Office' end, "
                    + "str(o.beneficiary.id), cast(o.routingNumber as integer), cast(o.scheme.code as integer)) "
                    + "from Payment o where o.payrollSummary.id=" + payrollSummaryId;
            paymentInfoForSpbmuList = sessionFactory.getCurrentSession().createQuery(querySt).list();

        } catch (HibernateException e) {
            throw new ExceptionWrapper(e.getMessage());
        }

        System.out.println("paymentInfoForSpbmuList.size() = " + paymentInfoForSpbmuList.size());
        return paymentInfoForSpbmuList;
    }

    @Override
    public boolean updateExportStatusOfPayrollSummaryList(String payrollSummaryIdList) throws ExceptionWrapper {
        try {
            payrollSummaryIdList = payrollSummaryIdList.substring(0, payrollSummaryIdList.length() - 1);
            String query = "update PayrollSummary o set o.isExportedToSpbmu=" + Boolean.TRUE + " where o.id in(" + payrollSummaryIdList + ")";
            System.out.println("query = " + query);
            int result = sessionFactory.getCurrentSession().createQuery(query).executeUpdate();
            return result != 0;
        } catch (HibernateException e) {
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<Object> getSupplementaryPayrollInfo(Map parameter, int beginIndex, int pageSize) throws ExceptionWrapper {
        List<Object> result = new ArrayList<>();
        try {
            Integer paymentCycleId = parameter.get("paymentCycleId") != null ? (Integer) parameter.get("paymentCycleId") : null;

            String query = "select distinct b.nid, p.beneficiary_name as payrollAccountName, b.full_name_in_english as currentAccountName, p.account_number as payrollAccountNumber, b.account_no as currentAccountNumber, p.bank as payrollBankOrOthers,\n"
                    + "case when b.payment_type = 0 then bn.name_in_english when b.payment_type =1 then m.name_in_english when b.payment_type =2 then 'Post Office' end as currentBankOrOthers, p.branch as payrollBranch, br.name_in_english as currentBranch, p.routing_number as payrollRoutingNo, \n"
                    + "case when b.payment_type = 0 then br.routing_number when b.payment_type =1 then m.routing_number when b.payment_type =2 then 'Post Office' end as currentRoutingNo, dv.name_in_english as division, ds.name_in_english as district, up.name_in_english as upazila\n"
                    + "from beneficiary b join payment p on b.Id=p.beneficiary_id\n"
                    + "left join bank bn on b.bank_id=bn.id\n"
                    + "left join branch br on b.branch_id=br.id\n"
                    + "left join mobile_banking_provider m on b.mobile_banking_provider_id=m.id\n"
                    + "join division dv on b.present_division_id=dv.id\n"
                    + "join district ds on b.present_district_id=ds.id\n"
                    + "join upazila up on b.present_upazila_id=up.id\n"
                    + "join unions un on b.present_union_id=un.id\n"
                    + "join user u on u.id=b.modified_by\n"
                    + "join payment_cycle pc on p.cycle_id=pc.id\n"
                    + "where p.returned_code=2 \n" // Only IBas Returned
                    + "and p.cycle_id=:cycleId\n"
                    + "order by b.present_division_id, b.present_district_id, b.present_upazila_id, b.present_union_id, b.full_name_in_english";
            List<SupplementaryPayrollInfo> list = sessionFactory.getCurrentSession().createSQLQuery(query)
                    .setParameter("cycleId", paymentCycleId)
                    .setResultTransformer(Transformers.aliasToBean(SupplementaryPayrollInfo.class)).list();

            System.out.println("resultList.size() = " + list.size());
            Integer count = list.size();
            result.add(list);
            result.add(count);
            result.add(count);
            return result;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
}

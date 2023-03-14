/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.enums.AccountInformationChangeReason;
import com.wfp.lmmis.enums.PaymentTypeEnum;
import com.wfp.lmmis.masterdata.model.AccountType;
import com.wfp.lmmis.masterdata.model.Bank;
import com.wfp.lmmis.masterdata.model.Branch;
import com.wfp.lmmis.masterdata.model.MobileBankingProvider;
import com.wfp.lmmis.masterdata.model.PostOfficeBranch;
import com.wfp.lmmis.masterdata.service.AccountTypeService;
import com.wfp.lmmis.payroll.model.PaymentInformationHistory;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Repository
public class BeneficiaryPaymentDaoImpl implements BeneficiaryPaymentDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<PaymentInformationView> getPaymentInformation(int skip, int take, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo) {
        List<PaymentInformationView> paymentInformationViews = new ArrayList<>();
        paymnetInformationViewSearchInfo.setStatus(1);
        String queryBuilder = queryBuildForAppliant(paymnetInformationViewSearchInfo, "beneficiary");
        String execql = "";
        String count_query = "";
        //        
        if (paymnetInformationViewSearchInfo.getAccountType() == 1) {
            String query1 = "SELECT beneficiary.id benID\n"
                    + "	,beneficiary.nid nid\n"
                    + "	,beneficiary.full_name_in_bangla benNameBn\n"
                    + "	,beneficiary.full_name_in_english benNameEn\n"
                    //                    + "	,beneficiary.mobile_number mobile \n"
                    + ", CASE\n"
                    + "			 WHEN LENGTH( beneficiary.mobile_number) = 10  THEN  CONCAT('0',beneficiary.mobile_number)\n"
                    + "		    ELSE beneficiary.mobile_number\n"
                    + "		   end mobile\n"
                    + "	,beneficiary.account_name accountName\n"
                    + "	,beneficiary.account_no accountNumber\n"
                    + "	,bank.name_in_bangla bankName\n"
                    + "	,branch.name_in_bangla branchName\n"
                    + "	,mobile_banking_provider.name_in_bangla mobileBankProviderName\n"
                    + "	,division.id divisionId\n"
                    + "	,division.name_in_bangla divisionNameBN\n"
                    + "	,division.name_in_english divisionNameEN\n"
                    + "	,district.id districtId\n"
                    + "	,district.name_in_bangla districtNameBN\n"
                    + "	,district.name_in_english districtNameEN\n"
                    + "	,upazila.id upazilaId\n"
                    + "	,upazila.name_in_bangla upazilaNameBN\n"
                    + "	,upazila.name_in_english upazilaNameEN\n"
                    + "	,unions.id unionId\n"
                    + "	,unions.name_in_bangla unionNameBN\n"
                    + "	,unions.name_in_english unionNameEN\n"
                    + "FROM beneficiary\n"
                    + "LEFT JOIN bank ON beneficiary.bank_id = bank.id\n"
                    + "LEFT JOIN branch ON beneficiary.branch_id = branch.id\n"
                    + "LEFT JOIN mobile_banking_provider ON beneficiary.mobile_banking_provider_id = mobile_banking_provider.id\n"
                    + "LEFT JOIN division ON division.id = beneficiary.permanent_division_id\n"
                    + "LEFT JOIN district ON district.id = beneficiary.permanent_district_id\n"
                    + "LEFT JOIN upazila ON upazila.id = beneficiary.permanent_upazila_id\n"
                    + "LEFT JOIN unions ON unions.id = beneficiary.permanent_union_id\n"
                    + "LEFT JOIN village ON village.id = beneficiary.permanent_village_id\n";

            count_query = "SELECT COUNT(beneficiary.id)\n"
                    + "FROM beneficiary\n"
                    + "LEFT JOIN bank ON beneficiary.bank_id = bank.id\n"
                    + "LEFT JOIN branch ON beneficiary.branch_id = branch.id\n"
                    + "LEFT JOIN mobile_banking_provider ON beneficiary.mobile_banking_provider_id = mobile_banking_provider.id\n"
                    + "LEFT JOIN division ON division.id = beneficiary.permanent_division_id\n"
                    + "LEFT JOIN district ON district.id = beneficiary.permanent_district_id\n"
                    + "LEFT JOIN upazila ON upazila.id = beneficiary.permanent_upazila_id\n"
                    + "LEFT JOIN unions ON unions.id = beneficiary.permanent_union_id\n"
                    + "LEFT JOIN village ON village.id = beneficiary.permanent_village_id"
                    + queryBuilder;

            execql = query1 + queryBuilder
                    + " ORDER BY beneficiary.id desc LIMIT " + skip + "," + take + "";

        } else if (paymnetInformationViewSearchInfo.getAccountType() == 2) {
            paymnetInformationViewSearchInfo.setPaymentStatus(1);
            paymnetInformationViewSearchInfo.setEftReferenceNumberNotNull(1);
            paymnetInformationViewSearchInfo.setIsExportedToSpbmu(1);
            queryBuilder = queryBuildForAppliant(paymnetInformationViewSearchInfo, "beneficiary");

            String quer2 = "SELECT beneficiary.id benID\n"
                    + "	,beneficiary.nid nid\n"
                    + "	,beneficiary.full_name_in_bangla benNameBn\n"
                    + "	,beneficiary.full_name_in_english benNameEn\n"
                    //                    + "	,beneficiary.mobile_number mobile \n"
                    + ",CASE\n"
                    + "			 WHEN LENGTH( beneficiary.mobile_number) = 10  THEN  CONCAT('0',beneficiary.mobile_number)\n"
                    + "		    ELSE beneficiary.mobile_number\n"
                    + "		   end mobile\n"
                    + "	,beneficiary.account_name accountName\n"
                    + "	,beneficiary.account_no accountNumber\n"
                    + "	,bank.name_in_bangla bankName\n"
                    + "	,branch.name_in_bangla branchName\n"
                    + "	,mobile_banking_provider.name_in_bangla mobileBankProviderName\n"
                    + "	,division.id divisionId\n"
                    + "	,division.name_in_bangla divisionNameBN\n"
                    + "	,division.name_in_english divisionNameEN\n"
                    + "	,district.id districtId\n"
                    + "	,district.name_in_bangla districtNameBN\n"
                    + "	,district.name_in_english districtNameEN\n"
                    + "	,upazila.id upazilaId\n"
                    + "	,upazila.name_in_bangla upazilaNameBN\n"
                    + "	,upazila.name_in_english upazilaNameEN\n"
                    + "	,unions.id unionId\n"
                    + "	,unions.name_in_bangla unionNameBN\n"
                    + "	,unions.name_in_english unionNameEN\n"
                    + "	,COALESCE(payment.isInformationUpdate,0) isInformationUpdate\n"
                    + " ,payment_cycle.name_in_bangla paymentCycleNameBN\n"
                    + " ,payment_cycle.name_in_english paymentCycleNameEN\n"
                    + "	 FROM payment\n"
                    + "LEFT JOIN beneficiary ON beneficiary.id = payment.beneficiary_id\n"
                    + "LEFT JOIN bank ON beneficiary.bank_id = bank.id\n"
                    + "LEFT JOIN branch ON beneficiary.branch_id = branch.id\n"
                    + "LEFT JOIN mobile_banking_provider ON beneficiary.mobile_banking_provider_id = mobile_banking_provider.id\n"
                    + "LEFT JOIN division ON division.id = beneficiary.permanent_division_id\n"
                    + "LEFT JOIN district ON district.id = beneficiary.permanent_district_id\n"
                    + "LEFT JOIN upazila ON upazila.id = beneficiary.permanent_upazila_id\n"
                    + "LEFT JOIN unions ON unions.id = beneficiary.permanent_union_id\n"
                    + "LEFT JOIN village ON village.id = beneficiary.permanent_village_id\n"
                    + "LEFT JOIN payroll_summary  ON payment.payroll_summary_id = payroll_summary.id\n "
                    + "LEFT JOIN payment_cycle ON payroll_summary.payment_cycle_id = payment_cycle.id\n"
                    + "LEFT JOIN fiscal_year ON fiscal_year.name_in_english = payment_cycle.fiscal_year_in_english\n";
            // + "WHERE payment.payment_status = 1 AND payment.eft_reference_number IS not NULL\n"
            //+ "ORDER BY beneficiary.nid";

            execql = quer2 + queryBuilder
                    + " ORDER BY beneficiary.nid desc LIMIT " + skip + "," + take + "";
            count_query = "SELECT COUNT(payment.id)\n"
                    + "	 FROM payment\n"
                    + "LEFT JOIN beneficiary ON beneficiary.id = payment.beneficiary_id\n"
                    + "LEFT JOIN bank ON beneficiary.bank_id = bank.id\n"
                    + "LEFT JOIN branch ON beneficiary.branch_id = branch.id\n"
                    + "LEFT JOIN mobile_banking_provider ON beneficiary.mobile_banking_provider_id = mobile_banking_provider.id\n"
                    + "LEFT JOIN division ON division.id = beneficiary.permanent_division_id\n"
                    + "LEFT JOIN district ON district.id = beneficiary.permanent_district_id\n"
                    + "LEFT JOIN upazila ON upazila.id = beneficiary.permanent_upazila_id\n"
                    + "LEFT JOIN unions ON unions.id = beneficiary.permanent_union_id\n"
                    + "LEFT JOIN village ON village.id = beneficiary.permanent_village_id\n"
                    + "LEFT JOIN payroll_summary  ON payment.payroll_summary_id = payroll_summary.id\n"
                    + "LEFT JOIN payment_cycle ON payroll_summary.payment_cycle_id = payment_cycle.id\n"
                    + "LEFT JOIN fiscal_year ON fiscal_year.name_in_english = payment_cycle.fiscal_year_in_english\n"
                    //  + "WHERE payment.payment_status = 1 AND payment.eft_reference_number IS not NULL\n"
                    + queryBuilder;

        } else {
            paymnetInformationViewSearchInfo.setPaymentStatus(1);
            paymnetInformationViewSearchInfo.setEftReferenceNumberNotNull(1);
            paymnetInformationViewSearchInfo.setInformationNotUpdate(1);
            paymnetInformationViewSearchInfo.setIsExportedToSpbmu(1);
            queryBuilder = queryBuildForAppliant(paymnetInformationViewSearchInfo, "beneficiary");

            String query3 = "SELECT beneficiary.id benID\n"
                    + "	,beneficiary.nid nid\n"
                    + "	,beneficiary.full_name_in_bangla benNameBn\n"
                    + "	,beneficiary.full_name_in_english benNameEn\n"
                    //                    + "	,beneficiary.mobile_number mobile \n"
                    + ",CASE\n"
                    + "			 WHEN LENGTH(beneficiary.mobile_number) = 10  THEN  CONCAT('0',beneficiary.mobile_number)\n"
                    + "		    ELSE beneficiary.mobile_number\n"
                    + "		   end mobile\n"
                    + "	,beneficiary.account_name accountName\n"
                    + "	,beneficiary.account_no accountNumber\n"
                    + "	,bank.name_in_bangla bankName\n"
                    + "	,branch.name_in_bangla branchName\n"
                    + "	,mobile_banking_provider.name_in_bangla mobileBankProviderName\n"
                    + "	,division.id divisionId\n"
                    + "	,division.name_in_bangla divisionNameBN\n"
                    + "	,division.name_in_english divisionNameEN\n"
                    + "	,district.id districtId\n"
                    + "	,district.name_in_bangla districtNameBN\n"
                    + "	,district.name_in_english districtNameEN\n"
                    + "	,upazila.id upazilaId\n"
                    + "	,upazila.name_in_bangla upazilaNameBN\n"
                    + "	,upazila.name_in_english upazilaNameEN\n"
                    + "	,unions.id unionId\n"
                    + "	,unions.name_in_bangla unionNameBN\n"
                    + "	,unions.name_in_english unionNameEN\n"
                    + "	,COALESCE(payment.isInformationUpdate,0) isInformationUpdate\n"
                    + "	 FROM payment\n"
                    + "LEFT JOIN beneficiary ON beneficiary.id = payment.beneficiary_id\n"
                    + "LEFT JOIN bank ON beneficiary.bank_id = bank.id\n"
                    + "LEFT JOIN branch ON beneficiary.branch_id = branch.id\n"
                    + "LEFT JOIN mobile_banking_provider ON beneficiary.mobile_banking_provider_id = mobile_banking_provider.id\n"
                    + "LEFT JOIN division ON division.id = beneficiary.permanent_division_id\n"
                    + "LEFT JOIN district ON district.id = beneficiary.permanent_district_id\n"
                    + "LEFT JOIN upazila ON upazila.id = beneficiary.permanent_upazila_id\n"
                    + "LEFT JOIN unions ON unions.id = beneficiary.permanent_union_id\n"
                    + "LEFT JOIN village ON village.id = beneficiary.permanent_village_id\n"
                    + "LEFT JOIN payroll_summary  ON payment.payroll_summary_id = payroll_summary.id\n"
                    + "LEFT JOIN payment_cycle ON payroll_summary.payment_cycle_id = payment_cycle.id\n"
                    + "LEFT JOIN fiscal_year ON fiscal_year.name_in_english = payment_cycle.fiscal_year_in_english\n";
            //+ "WHERE payment.payment_status = 1 AND payment.eft_reference_number IS not NULL\n"

            //  + "AND (payment.isInformationUpdate =0 OR payment.isInformationUpdate IS NULL)\n"
            // + queryBuilder
//                    + " GROUP BY payment.beneficiary_id\n"
//                    + " ORDER BY beneficiary.nid";
            execql = query3 + queryBuilder
                    + " GROUP BY payment.beneficiary_id\n"
                    + " ORDER BY beneficiary.nid desc LIMIT " + skip + "," + take + "";

            count_query = "SELECT COUNT(count_) FROM (\n"
                    + "SELECT COUNT(1) count_\n"
                    + "	 FROM payment\n"
                    + "LEFT JOIN beneficiary ON beneficiary.id = payment.beneficiary_id\n"
                    + "LEFT JOIN bank ON beneficiary.bank_id = bank.id\n"
                    + "LEFT JOIN branch ON beneficiary.branch_id = branch.id\n"
                    + "LEFT JOIN mobile_banking_provider ON beneficiary.mobile_banking_provider_id = mobile_banking_provider.id\n"
                    + "LEFT JOIN division ON division.id = beneficiary.permanent_division_id\n"
                    + "LEFT JOIN district ON district.id = beneficiary.permanent_district_id\n"
                    + "LEFT JOIN upazila ON upazila.id = beneficiary.permanent_upazila_id\n"
                    + "LEFT JOIN unions ON unions.id = beneficiary.permanent_union_id\n"
                    + "LEFT JOIN village ON village.id = beneficiary.permanent_village_id\n"
                    + "LEFT JOIN payroll_summary  ON payment.payroll_summary_id = payroll_summary.id\n"
                    + "LEFT JOIN payment_cycle ON payroll_summary.payment_cycle_id = payment_cycle.id\n"
                    + "LEFT JOIN fiscal_year ON fiscal_year.name_in_english = payment_cycle.fiscal_year_in_english\n"
                    + queryBuilder
                    + "	 GROUP BY payment.beneficiary_id) a";
        }

        if (paymnetInformationViewSearchInfo.getAccountType() == 2) {
            paymentInformationViews = this.sessionFactory.getCurrentSession().createSQLQuery(execql)
                    .addScalar("benID", IntegerType.INSTANCE)
                    .addScalar("nid", StringType.INSTANCE)
                    .addScalar("benNameBn", StringType.INSTANCE)
                    .addScalar("benNameEn", StringType.INSTANCE)
                    .addScalar("mobile", StringType.INSTANCE)
                    .addScalar("accountName", StringType.INSTANCE)
                    .addScalar("accountNumber", StringType.INSTANCE)
                    .addScalar("bankName", StringType.INSTANCE)
                    .addScalar("branchName", StringType.INSTANCE)
                    .addScalar("mobileBankProviderName", StringType.INSTANCE)
                    .addScalar("divisionId", IntegerType.INSTANCE)
                    .addScalar("districtId", IntegerType.INSTANCE)
                    .addScalar("upazilaId", IntegerType.INSTANCE)
                    .addScalar("unionId", IntegerType.INSTANCE)
                    //                    .addScalar("wardId", IntegerType.INSTANCE)
                    .addScalar("divisionNameEN", StringType.INSTANCE)
                    .addScalar("districtNameEN", StringType.INSTANCE)
                    .addScalar("upazilaNameEN", StringType.INSTANCE)
                    .addScalar("unionNameEN", StringType.INSTANCE)
                    .addScalar("divisionNameBN", StringType.INSTANCE)
                    .addScalar("districtNameBN", StringType.INSTANCE)
                    .addScalar("upazilaNameBN", StringType.INSTANCE)
                    .addScalar("unionNameBN", StringType.INSTANCE)
                    .addScalar("paymentCycleNameEN", StringType.INSTANCE)
                    .addScalar("paymentCycleNameBN", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(PaymentInformationView.class)).list();
        } else {
            paymentInformationViews = this.sessionFactory.getCurrentSession().createSQLQuery(execql)
                    .addScalar("benID", IntegerType.INSTANCE)
                    .addScalar("nid", StringType.INSTANCE)
                    .addScalar("benNameBn", StringType.INSTANCE)
                    .addScalar("benNameEn", StringType.INSTANCE)
                    .addScalar("mobile", StringType.INSTANCE)
                    .addScalar("accountName", StringType.INSTANCE)
                    .addScalar("accountNumber", StringType.INSTANCE)
                    .addScalar("bankName", StringType.INSTANCE)
                    .addScalar("branchName", StringType.INSTANCE)
                    .addScalar("mobileBankProviderName", StringType.INSTANCE)
                    .addScalar("divisionId", IntegerType.INSTANCE)
                    .addScalar("districtId", IntegerType.INSTANCE)
                    .addScalar("upazilaId", IntegerType.INSTANCE)
                    .addScalar("unionId", IntegerType.INSTANCE)
                    //                    .addScalar("wardId", IntegerType.INSTANCE)
                    .addScalar("divisionNameEN", StringType.INSTANCE)
                    .addScalar("districtNameEN", StringType.INSTANCE)
                    .addScalar("upazilaNameEN", StringType.INSTANCE)
                    .addScalar("unionNameEN", StringType.INSTANCE)
                    .addScalar("divisionNameBN", StringType.INSTANCE)
                    .addScalar("districtNameBN", StringType.INSTANCE)
                    .addScalar("upazilaNameBN", StringType.INSTANCE)
                    .addScalar("unionNameBN", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(PaymentInformationView.class)).list();
        }
        int count_ = ((Number) sessionFactory.getCurrentSession().createSQLQuery(count_query).uniqueResult()).intValue();
        if (paymentInformationViews.size() > 0) {
            paymentInformationViews.get(0).setCount(count_);
        }
        return paymentInformationViews;
        // return null;
    }

    @Override
    public PaymentInformationHistory getBeneficiaryPaymentHistoryById(int paymentHistoryId) {

        PaymentInformationHistory paymentInformationViews = (PaymentInformationHistory) sessionFactory.getCurrentSession().createQuery("SELECT a FROM PaymentInformationHistory as a WHERE a.id =:paymentHistoryId").setParameter("paymentHistoryId", paymentHistoryId).uniqueResult();

        return paymentInformationViews;
    }

    @Override
    public List<PaymentInformationView> getBeneficiaryPaymentHistoryList(int skip, int take, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo) {
        List<PaymentInformationView> paymentInformationViews = new ArrayList<>();
        // String queryBuilder = queryBuildForAppliant(paymnetInformationViewSearchInfo, "beneficiary");
        String query1 = "SELECT \n"
                + "payment_information_history.id id\n"
                + ",beneficiary.full_name_in_bangla benNameBn\n"
                + ",beneficiary.full_name_in_english benNameEn\n"
                + ",beneficiary.nid nid\n"
                + ",beneficiary.mobile_number mobile\n"
                + ",COALESCE(bank.name_in_bangla,'') bankName\n"
                + ",COALESCE(branch.name_in_bangla,'') branchName\n"
                + ",COALESCE(mobile_banking_provider.name_in_bangla,'') mobileBankProviderName\n"
                + ",COALESCE(mobile_banking_provider.id,0) mobileBankProviderNameId\n"
                + ",COALESCE(post_office_branch.name_in_bangla,'') postOfficeBranchName\n"
                + ",payment_information_history.reason reasonToPayInformationChange\n"
                + ",payment_information_history.isbounse_back_update isBounceBack\n"
                + ",COALESCE(payment_information_history.account_no,'') accountNumber\n"
                + ",COALESCE(payment_information_history.account_name,'') accountName\n"
                + ",payment_information_history.payment_type paymentTypeInt\n"
                + ",payment_information_history.creation_date creationDateSt\n"
                + ",(CASE \n"
                + "when payment_information_history.file_base64 IS NULL then 0\n"
                + "when payment_information_history.file_base64 = '' then 0\n"
                + "ELSE 1\n"
                + "END) hasFile"
                + " FROM payment_information_history\n"
                + " LEFT JOIN beneficiary ON beneficiary.id =payment_information_history.beneficiary_id \n"
                + " LEFT JOIN bank ON bank.id =payment_information_history.bank_id\n"
                + " LEFT JOIN branch ON branch.id =payment_information_history.branch_id\n"
                + " LEFT JOIN mobile_banking_provider ON mobile_banking_provider.id =payment_information_history.mobile_banking_provider_id\n"
                + " LEFT JOIN post_office_branch ON post_office_branch.id =payment_information_history.post_office_branch_id\n"
                + "\n"
                + "WHERE payment_information_history.beneficiary_id = " + paymnetInformationViewSearchInfo.getBenId() + "\n"
                + "order by payment_information_history.id desc";

        paymentInformationViews = sessionFactory.getCurrentSession().createSQLQuery(query1)
                .addScalar("id", IntegerType.INSTANCE)
                .addScalar("nid", StringType.INSTANCE)
                .addScalar("benNameBn", StringType.INSTANCE)
                .addScalar("benNameEn", StringType.INSTANCE)
                .addScalar("mobile", StringType.INSTANCE)
                .addScalar("accountName", StringType.INSTANCE)
                .addScalar("accountNumber", StringType.INSTANCE)
                .addScalar("bankName", StringType.INSTANCE)
                .addScalar("branchName", StringType.INSTANCE)
                .addScalar("mobileBankProviderName", StringType.INSTANCE)
                .addScalar("mobileBankProviderNameId", IntegerType.INSTANCE)
                .addScalar("reasonToPayInformationChange", IntegerType.INSTANCE)
                .addScalar("isBounceBack", IntegerType.INSTANCE)
                .addScalar("paymentTypeInt", IntegerType.INSTANCE)
                .addScalar("creationDateSt", StringType.INSTANCE)
                .addScalar("hasFile", IntegerType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(PaymentInformationView.class)).list();

        int[] ordinal = {1};
        paymentInformationViews.forEach(a -> {
            a.setSerial(ordinal[0]++);
            a.setReasonEnumMappingClass(AccountInformationChangeReason.getEnumMappingClassFromValue(a.getReasonToPayInformationChange()));

            a.setPaymentType(PaymentTypeEnum.nameFromValues(a.getPaymentTypeInt()));
        });

        return paymentInformationViews;

    }
    @Autowired
    private AccountTypeService accountTypeService;

    @Override
    public JsonResult updatePaymentInfo(PaymentInformationView informationView, User loginedUser) {
        String query_bene = "SELECT beneficiary.Id benID,\n"
                + "beneficiary.payment_type paymentTypeInt,\n"
                + "beneficiary.account_type_id accountType,\n"
                + "beneficiary.account_no accountNo,\n"
                + "beneficiary.account_name accountName,\n"
                + "beneficiary.bank_id bankId,\n"
                + "beneficiary.branch_id branchId,\n"
                + "beneficiary.mobile_banking_provider_id mobileBankProviderNameId,\n"
                + "beneficiary.post_office_branch_id postOfficeBranch\n"
                + " FROM beneficiary\n"
                + "WHERE beneficiary.Id = " + informationView.getBenID();

        List<PaymentInformationView> paymentInformationViews = sessionFactory.getCurrentSession().createSQLQuery(query_bene)
                .addScalar("benID", IntegerType.INSTANCE)
                .addScalar("paymentTypeInt", IntegerType.INSTANCE)
                .addScalar("accountType", IntegerType.INSTANCE)
                .addScalar("accountNo", StringType.INSTANCE)
                .addScalar("accountName", StringType.INSTANCE)
                .addScalar("bankId", IntegerType.INSTANCE)
                .addScalar("branchId", IntegerType.INSTANCE)
                .addScalar("mobileBankProviderNameId", IntegerType.INSTANCE)
                .addScalar("postOfficeBranch", IntegerType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(PaymentInformationView.class)).list();

        try {
            Beneficiary ben = new Beneficiary();
            Bank bank = new Bank();
            Branch branch = new Branch();
            MobileBankingProvider mobileBankingProvider = new MobileBankingProvider();
            AccountType accountType = new AccountType();
            PostOfficeBranch postOfficeBranch = new PostOfficeBranch();
            ben.setId(paymentInformationViews.get(0).getBenID());
            bank.setId(paymentInformationViews.get(0).getBankId());
            branch.setId(paymentInformationViews.get(0).getBranchId());
            mobileBankingProvider.setId(paymentInformationViews.get(0).getMobileBankProviderNameId());
            accountType.setId(paymentInformationViews.get(0).getAccountType());
            postOfficeBranch.setId(paymentInformationViews.get(0).getPostOfficeBranch());

            PaymentInformationHistory history = new PaymentInformationHistory();

            history.setBeneficiary(ben);
            history.setReason(informationView.getReasonToPayInformationChange());
            history.setIsbounseBackUpdate(informationView.getIsBounceBack());

            history.setBank(bank.getId() == null ? null : bank);
            history.setBranch(branch.getId() == null ? null : branch);
            history.setAccountNo(paymentInformationViews.get(0).getAccountNo());
            history.setAccountName(paymentInformationViews.get(0).getAccountName());
            history.setRemarks("");
            history.setPaymentType(paymentInformationViews.get(0).getPaymentTypeInt());
            history.setMobileBankingProvider(mobileBankingProvider.getId() == null ? null : mobileBankingProvider);
            history.setAccountType(accountType.getId() == null ? null : accountType);
            history.setPostOfficeBranch(postOfficeBranch.getId() == null ? null : postOfficeBranch);
            history.setCreatedBy(loginedUser);
            history.setCreationDate(Calendar.getInstance());
            fileSaveFor(informationView.getFiles(), history);
            sessionFactory.getCurrentSession().save(history);

//            String query = "INSERT INTO `imlma`.`payment_information_history`\n ( "
//                    + "`beneficiary_id`,\n"
//                    + "`reason`,\n"
//                    + "`isbounse_back_update`,\n"
//                    + "`file`,\n"
//                    + "`creation_date`,\n"
//                    + "`created_by`,\n"
//                    + "`bank_id`,\n"
//                    + "`branch_id`,\n"
//                    + "`account_no`,\n"
//                    + "`account_name`,\n"
//                    + "`remarks`,\n"
//                    + "`payment_type`,\n"
//                    + "`mobile_banking_provider_id`,\n"
//                    + "`post_office_branch_id`,\n"
//                    + "`account_type_id`)\n"
//                    + "VALUES\n ( "
//                    + paymentInformationViews.get(0).getBenID() + ",\n"
//                    + informationView.getReasonToPayInformationChange() + ",\n"
//                    + informationView.getIsBounceBack() + ",\n"
//                    + "'',\n"
//                    + "'" + getToday() + "',\n"
//                    + loginedUser.getId() + ",\n"
//                    + paymentInformationViews.get(0).getBankId() + ",\n"
//                    + paymentInformationViews.get(0).getBranchId() + ",\n"
//                    + "'" + paymentInformationViews.get(0).getAccountNo() + "',\n"
//                    + "'" + paymentInformationViews.get(0).getAccountName() + "',\n"
//                    + "'',\n"
//                    + paymentInformationViews.get(0).getPaymentTypeInt() + ",\n"
//                    + paymentInformationViews.get(0).getMobileBankProviderNameId() + ",\n"
//                    + paymentInformationViews.get(0).getPostOfficeBranch() + ",\n"
//                    + paymentInformationViews.get(0).getAccountType() + ");";
//
//            sessionFactory.getCurrentSession().createSQLQuery(query).executeUpdate();
            //     String beneficiaryUpdateQuery = "update beneficiary set beneficiary.reason_to_pay_information_change =%d where beneficiary.id = %d";
            PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.valueOf(informationView.getPaymentType());
            int account_type_id = 0;
            switch (paymentTypeEnum) {
                case BANKING:
                    account_type_id = informationView.getAccountType();
                    break;
                case MOBILEBANKING:

                    account_type_id = this.accountTypeService.getAccountType(2).getId();

                    break;
                case POSTOFFICE:
                    account_type_id = informationView.getAccountType();
                    break;
            }
            String beneficiaryUpdateQuery = "UPDATE beneficiary SET \n"
                    + "beneficiary.payment_type = " + PaymentTypeEnum.valueFromName(informationView.getPaymentType()) + ",\n"
                    + "beneficiary.account_no = '" + informationView.getAccountNumber() + "',\n"
                    + "beneficiary.account_type_id = " + account_type_id + ",\n"
                    + "beneficiary.account_name = '" + informationView.getAccountName() + "',\n"
                    + "beneficiary.bank_id = " + (informationView.getBankId() == 0 ? null : informationView.getBankId()) + ",\n"
                    + "beneficiary.branch_id = " + (informationView.getBranchId() == 0 ? null : informationView.getBranchId()) + ",\n"
                    + "beneficiary.mobile_banking_provider_id =" + (informationView.getMobileBankProviderNameId() == 0 ? null : informationView.getMobileBankProviderNameId()) + ",\n"
                    + "beneficiary.post_office_branch_id = " + (informationView.getPostOfficeBranch() == 0 ? null : informationView.getPostOfficeBranch()) + ",\n"
                    + "beneficiary.reason_to_pay_information_change = " + informationView.getReasonToPayInformationChange() + ",\n"
                    + "beneficiary.modification_date = '" + getToday("yyyy-MM-dd hh:mm:ss.s") + "',\n"
                    + "beneficiary.modified_by = " + loginedUser.getId() + "\n"
                    + "WHERE beneficiary.Id = " + informationView.getBenID();
//            beneficiaryUpdateQuery = String.format(beneficiaryUpdateQuery, informationView.getReasonToPayInformationChange(), informationView.getBenID());

            sessionFactory.getCurrentSession().createSQLQuery(beneficiaryUpdateQuery).executeUpdate();

            if (informationView.getIsBounceBack() == 1) {
                String bounceBackQuery = "UPDATE payment SET payment.isInformationUpdate = 1\n"
                        + "WHERE payment.beneficiary_id = " + informationView.getBenID() + " AND\n"
                        + "payment.payment_status = 1 AND payment.eft_reference_number IS not NULL";
                sessionFactory.getCurrentSession().createSQLQuery(bounceBackQuery).executeUpdate();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;

        }
        JsonResult jr = new JsonResult(false, "");
        return jr;
    }

    private String queryBuildForAppliant(PaymnetInformationViewSearchInfo informationViewSearchInfo, String peffix) {

        List<String> querList = new ArrayList<>();

        querList.add(peffix + ".applicant_type = " + informationViewSearchInfo.getApplicantType().ordinal());
        if (informationViewSearchInfo.getStatus() != 0) {
            querList.add(peffix + ".status != 2" );
        }
        if (informationViewSearchInfo.getDivisionId() != null && informationViewSearchInfo.getDivisionId() != 0) {
            querList.add(peffix + ".permanent_division_id = " + informationViewSearchInfo.getDivisionId());
        }
        if (informationViewSearchInfo.getDistrictId() != null && informationViewSearchInfo.getDistrictId() != 0) {
            querList.add(peffix + ".permanent_district_id = " + informationViewSearchInfo.getDistrictId());
        }
        if (informationViewSearchInfo.getUpazilaId() != null && informationViewSearchInfo.getUpazilaId() != 0) {
            querList.add(peffix + ".permanent_upazila_id = " + informationViewSearchInfo.getUpazilaId());
        }
        if (informationViewSearchInfo.getUnionId() != null && informationViewSearchInfo.getUnionId() != 0) {
            querList.add(peffix + ".permanent_union_id = " + informationViewSearchInfo.getUnionId());
        }
        if (informationViewSearchInfo.getWardNo() != null && informationViewSearchInfo.getWardNo() != 0) {
            querList.add(peffix + ".permanent_ward_no = " + informationViewSearchInfo.getWardNo());
        }

        if (informationViewSearchInfo.getBgmeaFactoryId() != null && informationViewSearchInfo.getBgmeaFactoryId() != 0) {
            querList.add(peffix + ".factory_id = " + informationViewSearchInfo.getBgmeaFactoryId());
        }

        if (informationViewSearchInfo.getBkmeaFactoryId() != null && informationViewSearchInfo.getBkmeaFactoryId() != 0) {
            querList.add(peffix + ".factory_id = " + informationViewSearchInfo.getBkmeaFactoryId());
        }

        if (informationViewSearchInfo.getNid() != null && !informationViewSearchInfo.getNid().equals("")) {
            querList.add(peffix + ".nid like '%" + informationViewSearchInfo.getNid() + "%'");
        }
        if (informationViewSearchInfo.getMobileNo() != null && !informationViewSearchInfo.getMobileNo().equals("")) {
            querList.add(peffix + ".nid like '%" + informationViewSearchInfo.getNid() + "%'");
        }
        if (informationViewSearchInfo.getPaymentStatus() == 1) {
            querList.add("payment.payment_status = 1");
        }
        if (informationViewSearchInfo.getEftReferenceNumberNotNull() == 1) {
            querList.add("payment.eft_reference_number IS not NULL");
        }
        if (informationViewSearchInfo.getInformationNotUpdate() == 1) {
            querList.add("(payment.isInformationUpdate = 0 OR payment.isInformationUpdate IS NULL)");
        }
        if (informationViewSearchInfo.getFiscalYear() != 0 && informationViewSearchInfo.getAccountType() == 1) {
            querList.add(peffix + ".fiscal_year_id = " + informationViewSearchInfo.getFiscalYear());
        } else if (informationViewSearchInfo.getFiscalYear() != 0 && informationViewSearchInfo.getAccountType() == 2) {
            querList.add("fiscal_year.id  = " + informationViewSearchInfo.getFiscalYear());
        } else if (informationViewSearchInfo.getFiscalYear() != 0 && informationViewSearchInfo.getAccountType() == 3) {
            querList.add("fiscal_year.id  = " + informationViewSearchInfo.getFiscalYear());
        }

        if (informationViewSearchInfo.getIsExportedToSpbmu() == 1) {
            querList.add("payroll_summary.is_exported_to_spbmu = 1");
        }

        if (informationViewSearchInfo.getPaymentCycle() != 0) {
            querList.add("payroll_summary.payment_cycle_id = " + informationViewSearchInfo.getPaymentCycle());
        }
        if (!informationViewSearchInfo.getAccountNumber().equals("")) {
            querList.add(peffix + ".account_no= '" + informationViewSearchInfo.getAccountNumber() + "'");
        }
        String query = String.join(" and ", querList);
        if (querList.size() > 0) {
            query = " where " + query;
        }

        return query;
    }

    public void fileSaveFor(MultipartFile[] files, PaymentInformationHistory cycleDetail) {

        for (int i = 0; i < files.length; i++) {

            MultipartFile file = files[i];
            String name = "";//names[i];
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            try {
                byte[] bytes = file.getBytes();
                name = file.getOriginalFilename().toString();
                String base64String = Base64.encodeBase64String(bytes);

                cycleDetail.setFileBase64(base64String);

            } catch (Exception ex) {
                System.out.println("Error " + ex.toString());
            }

        }

    }
//"yyyy-MM-dd "
    //"yyyy-MM-dd hh:mm:ss.s"

    private String getToday(String formate) {
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        String todaySt = formatter.format(new Date());
        return todaySt;
    }

}

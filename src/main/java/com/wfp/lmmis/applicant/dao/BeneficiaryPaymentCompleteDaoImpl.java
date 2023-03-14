/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.types.BeneficiaryStatus;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.PaymentInformationView;
import com.wfp.lmmis.utility.PaymnetInformationViewSearchInfo;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Component
@Repository
public class BeneficiaryPaymentCompleteDaoImpl implements BeneficiaryPaymentCompleteDao {

    @Autowired(required = true)
    SessionFactory sessionFactory;

    @Override
    public List<PaymentInformationView> getPaymentCompleteBeneficiaryList(int skip, int take, PaymnetInformationViewSearchInfo paymnetInformationViewSearchInfo) {
        List<PaymentInformationView> paymentInformationViews = new ArrayList<>();
        String queryBuilder = queryBuildForPaymnetComplete(paymnetInformationViewSearchInfo, "beneficiary");
        String execql = "";
        String count_query = "";
        //        

        String query1 = "SELECT beneficiary.id benID\n"
                + "	,beneficiary.nid nid\n"
                + "	,beneficiary.full_name_in_bangla benNameBn\n"
                + "	,beneficiary.full_name_in_english benNameEn\n"
                + "	,CASE \n"
                + "		WHEN LENGTH(beneficiary.mobile_number) = 10\n"
                + "			THEN CONCAT (\n"
                + "					'0'\n"
                + "					,beneficiary.mobile_number\n"
                + "					)\n"
                + "		ELSE beneficiary.mobile_number\n"
                + "		END mobile\n"
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
                + "     ,CONCAT('জেলা :',district.name_in_bangla,',   উপজেলা :',upazila.name_in_bangla,',   ইউনিয়ন :',unions.name_in_bangla ) `union`\n"
                + "     ,payment_cycle.start_date lastPaymentCycleDate \n"
                + "FROM beneficiary\n"
                + "LEFT JOIN bank ON beneficiary.bank_id = bank.id\n"
                + "LEFT JOIN branch ON beneficiary.branch_id = branch.id\n"
                + "LEFT JOIN mobile_banking_provider ON beneficiary.mobile_banking_provider_id = mobile_banking_provider.id\n"
                + "LEFT JOIN division ON division.id = beneficiary.permanent_division_id\n"
                + "LEFT JOIN district ON district.id = beneficiary.permanent_district_id\n"
                + "LEFT JOIN upazila ON upazila.id = beneficiary.permanent_upazila_id\n"
                + "LEFT JOIN unions ON unions.id = beneficiary.permanent_union_id\n"
                + "LEFT JOIN village ON village.id = beneficiary.permanent_village_id\n"
                + "LEFT JOIN payment_cycle ON payment_cycle.id = beneficiary.last_payment_cycle_id\n"
                + "LEFT JOIN fiscal_year ON fiscal_year.name_in_english = payment_cycle.fiscal_year_in_english";

        count_query = "SELECT COUNT(beneficiary.id)\n"
                + "FROM beneficiary\n"
                + "LEFT JOIN bank ON beneficiary.bank_id = bank.id\n"
                + "LEFT JOIN branch ON beneficiary.branch_id = branch.id\n"
                + "LEFT JOIN mobile_banking_provider ON beneficiary.mobile_banking_provider_id = mobile_banking_provider.id\n"
                + "LEFT JOIN division ON division.id = beneficiary.permanent_division_id\n"
                + "LEFT JOIN district ON district.id = beneficiary.permanent_district_id\n"
                + "LEFT JOIN upazila ON upazila.id = beneficiary.permanent_upazila_id\n"
                + "LEFT JOIN unions ON unions.id = beneficiary.permanent_union_id\n"
                + "LEFT JOIN village ON village.id = beneficiary.permanent_village_id\n"
                + "LEFT JOIN payment_cycle ON payment_cycle.id = beneficiary.last_payment_cycle_id\n"
                + "LEFT JOIN fiscal_year ON fiscal_year.name_in_english = payment_cycle.fiscal_year_in_english"
                + queryBuilder;

        execql = query1 + queryBuilder
                + " \n LIMIT " + skip + "," + take + "";

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
                .addScalar("divisionNameEN", StringType.INSTANCE)
                .addScalar("districtNameEN", StringType.INSTANCE)
                .addScalar("upazilaNameEN", StringType.INSTANCE)
                .addScalar("unionNameEN", StringType.INSTANCE)
                .addScalar("divisionNameBN", StringType.INSTANCE)
                .addScalar("districtNameBN", StringType.INSTANCE)
                .addScalar("upazilaNameBN", StringType.INSTANCE)
                .addScalar("unionNameBN", StringType.INSTANCE)
                .addScalar("lastPaymentCycleDate", StringType.INSTANCE)
                .addScalar("union", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(PaymentInformationView.class)).list();

        int count_ = ((Number) sessionFactory.getCurrentSession().createSQLQuery(count_query).uniqueResult()).intValue();
        if (paymentInformationViews.size() > 0) {
            paymentInformationViews.get(0).setCount(count_);
        }

        paymentInformationViews.forEach(a -> {
            if (a.getMobile() != null && a.getMobile().length() == 10) {
                a.setMobile("0" + a.getMobile());
            }
            a.setMobile(CommonUtility.getNumberInBangla(a.getMobile()));
            a.setNid(CommonUtility.getNumberInBangla(a.getNid()));
            a.setAccountNumber(CommonUtility.getNumberInBangla(a.getAccountNumber()));
            a.setLastPaymentCycleDate(CommonUtility.getNumberInBangla(a.getLastPaymentCycleDate()));

        });
        return paymentInformationViews;
        // return null;
    }

    private String queryBuildForPaymnetComplete(PaymnetInformationViewSearchInfo informationViewSearchInfo, String peffix) {

        List<String> querList = new ArrayList<>();

        // querList.add("beneficiary.status = 4 ");
        querList.add(peffix + ".applicant_type = " + informationViewSearchInfo.getApplicantType().ordinal());
        querList.add(peffix + ".status = " + BeneficiaryStatus.PAYMENT_COMPLETE.ordinal());

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
            querList.add(peffix + ".mobile_number like '%" + informationViewSearchInfo.getMobileNo() + "%'");
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
        if (informationViewSearchInfo.getFiscalYear() != 0) {
            querList.add(peffix + ".fiscal_year_id = " + informationViewSearchInfo.getFiscalYear());
        }
        if (informationViewSearchInfo.getIsExportedToSpbmu() == 1) {
            querList.add("payroll_summary.is_exported_to_spbmu = 1");
        }

        if (informationViewSearchInfo.getPaymentCycle() != 0) {
            querList.add("payroll_summary.payment_cycle_id = " + informationViewSearchInfo.getPaymentCycle());
        }
        if (informationViewSearchInfo.getAccountNumber() != null && !informationViewSearchInfo.getAccountNumber().equals("")) {
            querList.add(peffix + ".account_no= '" + informationViewSearchInfo.getAccountNumber() + "'");
        }
        String query = String.join(" and ", querList);
        if (querList.size() > 0) {
            query = " where " + query;
//                    + "\n group by payment.beneficiary_id\n"
//                    + "HAVING (sum(payment.allowance_amount)/800)>=33\n"
//                    + "order by sum(payment.allowance_amount)/800 DESC";
        }

        return query;
    }

}

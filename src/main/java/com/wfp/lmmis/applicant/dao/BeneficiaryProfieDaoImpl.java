/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.beneficiary.model.BeneficiaryBiometricInfo;
import com.wfp.lmmis.beneficiary.model.BeneficiaryChildInformation;
import com.wfp.lmmis.enums.ReligionEnum;
import com.wfp.lmmis.types.PaymentStatus;
import com.wfp.lmmis.utility.BeneficiaryProfile;
import com.wfp.lmmis.utility.BeneficiaryProfilePaymentInformation;
import com.wfp.lmmis.utility.BeneficiaryProfileView;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.GrievanceInfo;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Repository
public class BeneficiaryProfieDaoImpl implements BeneficiaryProfieDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public BeneficiaryProfileView getBenefificaryInfoByID(int beneficiaryID) {

        BeneficiaryProfileView beneficiaryProfileView = new BeneficiaryProfileView();

        String queryForBeneficiary = "SELECT b.id benId, b.full_name_in_english nameEn\n"
                + "	,b.full_name_in_bangla nameBn\n"
                + "	,b.father_name fatherName\n"
                + "	,b.mother_name motherName\n"
                + "	,b.nid nid\n"
                + "	,b.spouse_name spouseName\n"
                + "	,b.date_of_birth dob\n"
                + "   ,b.mobile_number mobileNumber\n"
                + "	,edu.name_in_bangla educationLevelBn\n"
                + "	,edu.name_in_english educationLevelEn\n"
                + "	,birth.name_in_bangla birthPlaceBn\n"
                + "	,birth.name_in_english birthPlaceEn\n"
                + "	,div_.name_in_bangla divisionNameBn\n"
                + "	,div_.name_in_english divisionNameEn\n"
                + "	,dis.name_in_bangla districNameBn\n"
                + "	,dis.name_in_english districNameEn\n"
                + "	,upa_.name_in_bangla upazilaNameBn\n"
                + "	,upa_.name_in_english upazilaNameEn\n"
                + "	,uni.name_in_bangla unionNameBn\n"
                + "	,uni.name_in_english unionNameEn\n"
                + "	,vi.name_in_bangla villageNameBn\n"
                + "	,vi.name_in_english villageNameEn\n"
                + "	,b.permanent_ward_no wardNo\n"
                + ",b.religion religionId\n"
                + "FROM beneficiary b\n"
                + "LEFT JOIN district dis ON dis.id = b.permanent_district_id\n"
                + "LEFT JOIN division div_ ON div_.id = b.permanent_division_id\n"
                + "LEFT JOIN upazila upa_ ON upa_.id = b.permanent_upazila_id\n"
                + "LEFT JOIN unions uni ON uni.id = b.permanent_union_id\n"
                + "LEFT JOIN village vi ON vi.id = b.permanent_village_id\n"
                + "LEFT JOIN district birth ON birth.id = b.birth_place_id\n"
                + "LEFT JOIN education_level edu ON edu.id = b.education_level\n"
                + "WHERE b.id = " + beneficiaryID;
//
//        String query = "SELECT b.id benId, b.full_name_in_english nameEn\n"
//                + "	,b.full_name_in_bangla nameBn\n"
//                + "	,b.father_name fatherName\n"
//                + "	,b.mother_name motherName\n"
//                + "	,b.nid nid\n"
//                + "	,b.spouse_name spouseName\n"
//                + "	,b.date_of_birth dob\n"
//                + "     ,b.mobile_number mobileNumber\n"
//                + "	,edu.name_in_bangla educationLevelBn\n"
//                + "	,edu.name_in_english educationLevelEn\n"
//                + "	,birth.name_in_bangla birthPlaceBn\n"
//                + "	,birth.name_in_english birthPlaceEn\n"
//                + "	,div_.name_in_bangla divisionNameBn\n"
//                + "	,div_.name_in_english divisionNameEn\n"
//                + "	,dis.name_in_bangla districNameBn\n"
//                + "	,dis.name_in_english districNameEn\n"
//                + "	,upa_.name_in_bangla upazilaNameBn\n"
//                + "	,upa_.name_in_english upazilaNameEn\n"
//                + "	,uni.name_in_bangla unionNameBn\n"
//                + "	,uni.name_in_english unionNameEn\n"
//                + "	,vi.name_in_bangla villageNameBn\n"
//                + "	,vi.name_in_english villageNameEn\n"
//                + "	,b.permanent_ward_no wardNo\n"
//                + "	,pt.name_in_bangla paymentNameBn\n"
//                + "	,pt.name_in_english paymentNameEn\n"
//                + "	,p.payment_date paymentDate\n"
//                + "	,p.bank bankNameEn\n"
//                + "	,p.bank_bn bankNameBn\n"
//                + "	,p.branch branchNameEn\n"
//                + "	,p.branch_bn branchNameBn\n"
//                + "     ,p.payment_status paymentStatus\n"
//                + "     ,p.approval_status approvalStatus\n"
//                + "     ,COALESCE(p.eft_reference_number ,'') paymentEFTRefNum\n"
//                + "     ,p.returned_code returnCode\n"
//                + "	,c.name_in_bangla paymentCycleBn\n"
//                + "	,c.name_in_english paymentCycleEn\n"
//                + "	,c.allowance_amount amount\n"
//                + "	,p.account_number  accountNO\n"
//                + ",b.religion religionId\n"
//                + "FROM beneficiary b\n"
//                + "LEFT JOIN  payment p ON b.Id = p.beneficiary_id\n"
//                + "LEFT JOIN payment_cycle c ON c.id = p.cycle_id\n"
//                + "LEFT JOIN payment_type pt ON p.payment_type = pt.id\n"
//                + "LEFT JOIN district dis ON dis.id = b.permanent_district_id\n"
//                + "LEFT JOIN division div_ ON div_.id = b.permanent_division_id\n"
//                + "LEFT JOIN upazila upa_ ON upa_.id = b.permanent_upazila_id\n"
//                + "LEFT JOIN unions uni ON uni.id = b.permanent_union_id\n"
//                + "LEFT JOIN village vi ON vi.id = b.permanent_village_id\n"
//                + "LEFT JOIN district birth ON birth.id = b.birth_place_id\n"
//                + "LEFT JOIN education_level edu ON edu.id = b.education_level\n"
//                + "LEFT JOIN payroll_summary ps ON p.payroll_summary_id = ps.id\n"
//                + "WHERE  ps.is_exported_to_spbmu = 1\n"
//                + "AND (\n"
//                + "		(p.payment_status = 1 AND p.eft_reference_number IS NULL) \n"
//                + "		OR \n"
//                + "		(p.payment_status = 1 AND p.eft_reference_number IS NOT NULL) \n"
//                + "		OR \n"
//                + "		(p.payment_status = 0 AND p.returned_code IN (3,4,5,6) )\n"
//                + "		) \n"
//                + "and b.id = " + beneficiaryID;

        try {
//            List<BeneficiaryProfile> beneficiaryProfile = sessionFactory.getCurrentSession().createSQLQuery(query)
//                    .addScalar("benID", StringType.INSTANCE)
//                    .addScalar("nameEn", StringType.INSTANCE)
//                    .addScalar("nameBn", StringType.INSTANCE)
//                    .addScalar("fatherName", StringType.INSTANCE)
//                    .addScalar("motherName", StringType.INSTANCE)
//                    .addScalar("nid", StringType.INSTANCE)
//                    .addScalar("dob", StringType.INSTANCE)
//                    .addScalar("spouseName", StringType.INSTANCE)
//                    .addScalar("mobileNumber", StringType.INSTANCE)
//                    .addScalar("educationLevelBn", StringType.INSTANCE)
//                    .addScalar("educationLevelEn", StringType.INSTANCE)
//                    .addScalar("birthPlaceBn", StringType.INSTANCE)
//                    .addScalar("birthPlaceEn", StringType.INSTANCE)
//                    .addScalar("religionID", IntegerType.INSTANCE)
//                    .addScalar("divisionNameBn", StringType.INSTANCE)
//                    .addScalar("divisionNameEn", StringType.INSTANCE)
//                    .addScalar("districNameBn", StringType.INSTANCE)
//                    .addScalar("districNameEn", StringType.INSTANCE)
//                    .addScalar("upazilaNameBn", StringType.INSTANCE)
//                    .addScalar("upazilaNameEn", StringType.INSTANCE)
//                    .addScalar("unionNameBn", StringType.INSTANCE)
//                    .addScalar("unionNameEn", StringType.INSTANCE)
//                    .addScalar("villageNameBn", StringType.INSTANCE)
//                    .addScalar("villageNameEn", StringType.INSTANCE)
//                    .addScalar("wardNo", StringType.INSTANCE)
//                    .addScalar("paymentNameBn", StringType.INSTANCE)
//                    .addScalar("paymentNameEn", StringType.INSTANCE)
//                    .addScalar("paymentDate", StringType.INSTANCE)
//                    .addScalar("paymentStatus", StringType.INSTANCE)
//                    .addScalar("bankNameEn", StringType.INSTANCE)
//                    .addScalar("bankNameBn", StringType.INSTANCE)
//                    .addScalar("branchNameBn", StringType.INSTANCE)
//                    .addScalar("branchNameEn", StringType.INSTANCE)
//                    .addScalar("paymentCycleBn", StringType.INSTANCE)
//                    .addScalar("paymentCycleEn", StringType.INSTANCE)
//                    .addScalar("paymentEFTRefNum", StringType.INSTANCE)
//                    .addScalar("amount", StringType.INSTANCE)
//                    .addScalar("accountNO", StringType.INSTANCE)
//                    .addScalar("returnCode", IntegerType.INSTANCE)
//                    .addScalar("approvalStatus", IntegerType.INSTANCE)
//                    .setResultTransformer(Transformers.aliasToBean(BeneficiaryProfile.class)).list();

            List<BeneficiaryProfile> beneficiaryProfile = sessionFactory.getCurrentSession().createSQLQuery(queryForBeneficiary)
                    .addScalar("benID", StringType.INSTANCE)
                    .addScalar("nameEn", StringType.INSTANCE)
                    .addScalar("nameBn", StringType.INSTANCE)
                    .addScalar("fatherName", StringType.INSTANCE)
                    .addScalar("motherName", StringType.INSTANCE)
                    .addScalar("nid", StringType.INSTANCE)
                    .addScalar("dob", StringType.INSTANCE)
                    .addScalar("spouseName", StringType.INSTANCE)
                    .addScalar("mobileNumber", StringType.INSTANCE)
                    .addScalar("educationLevelBn", StringType.INSTANCE)
                    .addScalar("educationLevelEn", StringType.INSTANCE)
                    .addScalar("birthPlaceBn", StringType.INSTANCE)
                    .addScalar("birthPlaceEn", StringType.INSTANCE)
                    .addScalar("religionID", IntegerType.INSTANCE)
                    .addScalar("divisionNameBn", StringType.INSTANCE)
                    .addScalar("divisionNameEn", StringType.INSTANCE)
                    .addScalar("districNameBn", StringType.INSTANCE)
                    .addScalar("districNameEn", StringType.INSTANCE)
                    .addScalar("upazilaNameBn", StringType.INSTANCE)
                    .addScalar("upazilaNameEn", StringType.INSTANCE)
                    .addScalar("unionNameBn", StringType.INSTANCE)
                    .addScalar("unionNameEn", StringType.INSTANCE)
                    .addScalar("villageNameBn", StringType.INSTANCE)
                    .addScalar("villageNameEn", StringType.INSTANCE)
                    .addScalar("wardNo", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(BeneficiaryProfile.class)).list();

            String queryPaymentInfo = "SELECT \n"
                    + "	pt.name_in_bangla paymentNameBn\n"
                    + "	,pt.name_in_english paymentNameEn\n"
                    + "	,p.payment_date paymentDate\n"
                    + "	,p.bank bankNameEn\n"
                    + "	,p.bank_bn bankNameBn\n"
                    + "	,p.branch branchNameEn\n"
                    + "	,p.branch_bn branchNameBn\n"
                    + "     ,p.payment_status paymentStatus\n"
                    + "     ,p.approval_status approvalStatus\n"
                    + "     ,COALESCE(p.eft_reference_number ,'') paymentEFTRefNum\n"
                    + "     ,p.returned_code returnCode\n"
                    + "	,c.name_in_bangla paymentCycleBn\n"
                    + "	,c.name_in_english paymentCycleEn\n"
                    + "	,p.allowance_amount amount\n"
                    + "	,p.account_number  accountNO\n"
                    + ",b.religion religionId\n"
                    + "FROM beneficiary b\n"
                    + " JOIN  payment p ON b.Id = p.beneficiary_id\n"                    
                    + " JOIN payment_cycle c ON c.id = p.cycle_id\n"
                    + " JOIN payment_type pt ON (p.payment_type+1) = pt.id\n"
                    //                    + " JOIN district dis ON dis.id = b.permanent_district_id\n"
                    //                    + " JOIN division div_ ON div_.id = b.permanent_division_id\n"
                    //                    + " JOIN upazila upa_ ON upa_.id = b.permanent_upazila_id\n"
                    //                    + " JOIN unions uni ON uni.id = b.permanent_union_id\n"
                    //                    + " left JOIN village vi ON vi.id = b.permanent_village_id\n"
                    //                    + " left JOIN district birth ON birth.id = b.birth_place_id\n"
                    + " JOIN payroll_summary ps ON p.payroll_summary_id = ps.id\n"
                    + "WHERE  ps.is_exported_to_spbmu = 1\n"
                    + "AND (\n"
                    + "		( p.payment_status = 1 AND p.eft_reference_number IS NULL) \n"
                    + "		OR \n"
                    + "		( p.payment_status = 1 AND p.eft_reference_number IS NOT NULL) \n"
                    + "		OR \n"
                    + "		( p.payment_status = 0 AND p.returned_code IN (3,4,5,6) )\n"
                    + "		) \n"
                    + "and b.id = " + beneficiaryID;

            List<BeneficiaryProfilePaymentInformation> beneficiaryProfilePaymentInformations = sessionFactory.getCurrentSession().createSQLQuery(queryPaymentInfo)
                    .addScalar("paymentNameBn", StringType.INSTANCE)
                    .addScalar("paymentNameEn", StringType.INSTANCE)
                    .addScalar("paymentDate", StringType.INSTANCE)
                    .addScalar("paymentStatus", StringType.INSTANCE)
                    .addScalar("bankNameEn", StringType.INSTANCE)
                    .addScalar("bankNameBn", StringType.INSTANCE)
                    .addScalar("branchNameBn", StringType.INSTANCE)
                    .addScalar("branchNameEn", StringType.INSTANCE)
                    .addScalar("paymentCycleBn", StringType.INSTANCE)
                    .addScalar("paymentCycleEn", StringType.INSTANCE)
                    .addScalar("paymentEFTRefNum", StringType.INSTANCE)
                    .addScalar("amount", StringType.INSTANCE)
                    .addScalar("accountNO", StringType.INSTANCE)
                    .addScalar("returnCode", IntegerType.INSTANCE)
                    .addScalar("approvalStatus", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(BeneficiaryProfilePaymentInformation.class)).list();

            beneficiaryProfilePaymentInformations.forEach(a -> {
                PaymentStatus paymentStatus = getPaymentStatusByBeneficiaryPaymentInfo(a);
                if ("bn".equals(LocaleContextHolder.getLocale().getLanguage())) {
                    a.setPaymentStatus(paymentStatus.getDisplayNameBn());
                    a.setAmount(CommonUtility.getNumberInBangla(a.getAmount()));
                } else {
                    a.setPaymentStatus(paymentStatus.getDisplayName());
                }
            });

            List<BeneficiaryBiometricInfo> beneficiaryBiometricInfos = sessionFactory.getCurrentSession().createQuery("SELECT b FROM BeneficiaryBiometricInfo AS b WHERE b.beneficiary.Id =:beneficiaryId")
                    .setParameter("beneficiaryId", beneficiaryID).list();

            List<BeneficiaryChildInformation> beneficiaryChildInformations = sessionFactory.getCurrentSession().createQuery("SELECT b FROM BeneficiaryChildInformation AS b WHERE b.beneficiary.Id =:beneficiaryId")
                    .setParameter("beneficiaryId", beneficiaryID).list();

            beneficiaryChildInformations.forEach(a -> {
                if ("bn".equals(LocaleContextHolder.getLocale().getLanguage())) {
                    a.setChildNoSt(CommonUtility.getNumberInBangla(a.getChildNo().toString()));
                    a.setChildBirthCertificate(CommonUtility.getNumberInBangla(a.getChildBirthCertificate()));
                } else {
                    a.setChildNoSt(a.getChildNo().toString());
                }
            });

            List<GrievanceInfo> grievanceInfos = getGrievanceInfoByBeneficiary(beneficiaryID);
            if (beneficiaryProfile.size() > 0) {
                beneficiaryProfileView.setBeneficiaryProfile(getBeneficiaryProfileByLocale(beneficiaryProfile.get(0)));

                if (beneficiaryBiometricInfos.size() > 0) {
                    String base64String = Base64.encodeBase64String(beneficiaryBiometricInfos.get(0).getPhotoData());
                    beneficiaryProfileView.setBase64PhotoData(base64String);
                    base64String = Base64.encodeBase64String(beneficiaryBiometricInfos.get(0).getSignatureData());
                    beneficiaryProfileView.setBase64SignatureData(base64String);
                }
//                List<BeneficiaryProfilePaymentInformation> beneficiaryProfilePaymentInformations = new ArrayList<>();
//                beneficiaryProfile.forEach(a -> {
//                    System.out.println("Payment Status " + a.getPaymentStatus());
//                    if (a.getPaymentStatus() != null && a.getPaymentStatus().equals("1")) {
//                        beneficiaryProfilePaymentInformations.add(getPaymentInformationFromBeneficiaryProfile(a));
//                    }
//                });
                beneficiaryProfileView.setBeneficiaryProfilePaymentInformations(getBeneficiaryProfilePaymentInformationsByLocale(beneficiaryProfilePaymentInformations));

                beneficiaryProfileView.setBeneficiaryChildInformations(beneficiaryChildInformations);
            }
            return beneficiaryProfileView;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private List<BeneficiaryProfilePaymentInformation> getBeneficiaryProfilePaymentInformationsByLocale(List<BeneficiaryProfilePaymentInformation> beneficiaryProfilePaymentInformations) {

        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {

        } else {
            beneficiaryProfilePaymentInformations.forEach(a -> {
                a.setPaymentDate(CommonUtility.getNumberInBangla(a.getPaymentDate()));
                a.setAccountNO(CommonUtility.getNumberInBangla(a.getAccountNO()));
            });

        }
        return beneficiaryProfilePaymentInformations;
    }

    private List<GrievanceInfo> getGrievanceInfoByBeneficiary(int beneficiaryID) {
        String grievanceQuery = "SELECT grievance.id,\n"
                + "grievance.description ,\n"
                + "grievance_status.name_in_bangla statusBN,\n"
                + "grievance_status.name_in_english statusEN,\n"
                + "grievance_type.name_in_bangla typeBN,\n"
                + "grievance_type.name_in_english typeEN\n"
                + "FROM grievance\n"
                + "JOIN grievance_type ON grievance_type.id = grievance.grievance_type_id\n"
                + "JOIN grievance_status ON grievance_status.id = grievance.grievance_status_id\n"
                + "\n"
                + "WHERE grievance.beneficiary_id = " + beneficiaryID;

        List<GrievanceInfo> grievanceInfos = null;
        return grievanceInfos;
    }

    private BeneficiaryProfile getBeneficiaryProfileByLocale(BeneficiaryProfile beneficiaryProfile) {
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {

            beneficiaryProfile.setMobileNumber(beneficiaryProfile.getMobileNumber() != null ? ("0" + beneficiaryProfile.getMobileNumber()) : "");

        } else {
            beneficiaryProfile.setMobileNumber(beneficiaryProfile.getMobileNumber() != null ? (CommonUtility.getNumberInBangla("0" + beneficiaryProfile.getMobileNumber().toString())) : "");
            beneficiaryProfile.setNid(CommonUtility.getNumberInBangla(beneficiaryProfile.getNid().toString()));
            if (beneficiaryProfile.getWardNo() != null) {
                beneficiaryProfile.setWardNo(CommonUtility.getNumberInBangla(beneficiaryProfile.getWardNo().toString()));
            }

            beneficiaryProfile.setDob(CommonUtility.getNumberInBangla(beneficiaryProfile.getDob().toString()));

        }
        ReligionEnum religionEnum = beneficiaryProfile.getReligionID() == 0 ? ReligionEnum.ISLAM
                : beneficiaryProfile.getReligionID() == 1 ? ReligionEnum.HINDU
                : beneficiaryProfile.getReligionID() == 2 ? ReligionEnum.BUDDIST
                : beneficiaryProfile.getReligionID() == 3 ? ReligionEnum.CHRISTIAN : ReligionEnum.OTHERS;

        beneficiaryProfile.setReligionBn(religionEnum.getDisplayNameBn());
        beneficiaryProfile.setReligionEn(religionEnum.getDisplayName());
        return beneficiaryProfile;
    }

    private BeneficiaryProfilePaymentInformation getPaymentInformationFromBeneficiaryProfile(BeneficiaryProfile beneficiaryProfile) {
        BeneficiaryProfilePaymentInformation beneficiaryProfilePaymentInformation = new BeneficiaryProfilePaymentInformation();

        beneficiaryProfilePaymentInformation.setBankNameBn(beneficiaryProfile.getBankNameBn());
        beneficiaryProfilePaymentInformation.setBankNameEn(beneficiaryProfile.getBankNameEn());
        beneficiaryProfilePaymentInformation.setBranchNameBn(beneficiaryProfile.getBranchNameBn());
        beneficiaryProfilePaymentInformation.setBranchNameEn(beneficiaryProfile.getBranchNameEn());
        beneficiaryProfilePaymentInformation.setPaymentCycleBn(beneficiaryProfile.getPaymentCycleBn());
        beneficiaryProfilePaymentInformation.setPaymentCycleEn(beneficiaryProfile.getPaymentCycleEn());
        beneficiaryProfilePaymentInformation.setPaymentNameBn(beneficiaryProfile.getPaymentNameBn());
        beneficiaryProfilePaymentInformation.setPaymentNameEn(beneficiaryProfile.getPaymentNameEn());
        beneficiaryProfilePaymentInformation.setPaymentEFTRefNum(beneficiaryProfile.getPaymentEFTRefNum());
        beneficiaryProfilePaymentInformation.setPaymentStatus(beneficiaryProfile.getPaymentStatus());

        PaymentStatus paymentStatus = getPaymentStatus(beneficiaryProfile);
        if ("en".equals(LocaleContextHolder.getLocale().getLanguage())) {
            beneficiaryProfilePaymentInformation.setAmount(beneficiaryProfile.getAmount());
            beneficiaryProfilePaymentInformation.setAccountNO(beneficiaryProfile.getAccountNO());
            beneficiaryProfilePaymentInformation.setPaymentDate(beneficiaryProfile.getPaymentDate());
            beneficiaryProfilePaymentInformation.setPaymentStatus_str(paymentStatus.getDisplayName());
//            if (beneficiaryProfile.getPaymentStatus() == "1") {
//                beneficiaryProfile.setPaymentStatus_str("Successful Payment");
//            } else {
//                beneficiaryProfile.setPaymentStatus_str("Payment Failed");
//            }

        } else {
            beneficiaryProfilePaymentInformation.setAmount(beneficiaryProfile.getAmount() != null ? CommonUtility.getNumberInBangla(beneficiaryProfile.getAmount()) : "");
            beneficiaryProfilePaymentInformation.setAccountNO(beneficiaryProfile.getAccountNO() != null ? CommonUtility.getNumberInBangla(beneficiaryProfile.getAccountNO()) : "");
            beneficiaryProfilePaymentInformation.setPaymentDate(beneficiaryProfile.getPaymentDate() != null ? CommonUtility.getNumberInBangla(beneficiaryProfile.getPaymentDate()) : "");
            beneficiaryProfilePaymentInformation.setPaymentStatus_str(paymentStatus.getDisplayNameBn());
//            if (beneficiaryProfile.getPaymentStatus().equals("1")) {
//                beneficiaryProfilePaymentInformation.setPaymentStatus_str("??????? ???");
//            } else {
//                beneficiaryProfilePaymentInformation.setPaymentStatus_str("???????  ?????? ?????");
//            }
        }

        return beneficiaryProfilePaymentInformation;
    }

    private PaymentStatus getPaymentStatusByBeneficiaryPaymentInfo(BeneficiaryProfilePaymentInformation beneficiaryProfile) {

        if (beneficiaryProfile.getPaymentStatus().equals("1")) {
            if (beneficiaryProfile.getPaymentEFTRefNum().equals("")) {
                return PaymentStatus.SUCCESSFULPAYMENT;
            }
            return PaymentStatus.RETURNEDEFTS;
        } else if (beneficiaryProfile.getPaymentStatus().equals("0")) {
//            if (beneficiaryProfile.getReturnCode() == null && beneficiaryProfile.getApprovalStatus() == 1) {
//                return PaymentStatus.APPROVEDPAYROLLS;
//            }
            if (beneficiaryProfile.getReturnCode() != null && (beneficiaryProfile.getReturnCode() == 3
                    || beneficiaryProfile.getReturnCode() == 4
                    || beneficiaryProfile.getReturnCode() == 5
                    || beneficiaryProfile.getReturnCode() == 6)) {
                return PaymentStatus.BLOCKEDPAYMENTS;

            }

        }
        return PaymentStatus.Deafult;

    }

    private PaymentStatus getPaymentStatus(BeneficiaryProfile beneficiaryProfile) {

        if (beneficiaryProfile.getPaymentStatus().equals("1")) {
            if (beneficiaryProfile.getPaymentEFTRefNum().equals("")) {
                return PaymentStatus.SUCCESSFULPAYMENT;
            }
            return PaymentStatus.RETURNEDEFTS;
        } else if (beneficiaryProfile.getPaymentStatus().equals("0")) {
//            if (beneficiaryProfile.getReturnCode() == null && beneficiaryProfile.getApprovalStatus() == 1) {
//                return PaymentStatus.APPROVEDPAYROLLS;
//            }
            if (beneficiaryProfile.getReturnCode() != null && (beneficiaryProfile.getReturnCode() == 3
                    || beneficiaryProfile.getReturnCode() == 4
                    || beneficiaryProfile.getReturnCode() == 5
                    || beneficiaryProfile.getReturnCode() == 6)) {
                return PaymentStatus.BLOCKEDPAYMENTS;

            }

        }
        return PaymentStatus.Deafult;
//         switch (paymentStatus)
//                {
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
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.beneficiary.model.BeneficiaryAttachment;
import com.wfp.lmmis.beneficiary.model.BeneficiaryBiometricInfo;
import com.wfp.lmmis.beneficiary.model.BeneficiaryChildInformation;
import com.wfp.lmmis.beneficiary.model.BeneficiaryView;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.report.data.BeneficiaryReportData;
import com.wfp.lmmis.report.data.BeneficiaryReportDataByLocation;
import com.wfp.lmmis.report.data.DataEntryReportData;
import com.wfp.lmmis.report.data.DataEntryReportDataByUser;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.types.BeneficiaryStatus;
import com.wfp.lmmis.types.PaymentStatus;
import com.wfp.lmmis.utility.BeneficiaryInfo;
import com.wfp.lmmis.utility.BeneficiaryProfile;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.JsonResult;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author user
 */
@Repository
public class BeneficiaryDaoImpl implements BeneficiaryDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Beneficiary getBeneficiary(Integer id) {
        try {
            System.out.println("Beneficiary id = " + id);
            Beneficiary beneficiary = (Beneficiary) this.sessionFactory.getCurrentSession().get(Beneficiary.class, id);
//            for (BeneficiarySchemeAttributeValue schemeAttributeValue : beneficiary.getSchemeAttributeValueList())
//            {
//            }
            System.out.println("attachment size (before) = " + beneficiary.getBeneficiaryAttachmentList().size());
            for (BeneficiaryAttachment attachment : beneficiary.getBeneficiaryAttachmentList()) {
//                System.out.println("id=" + attachment.getId() + ", attachment = " + attachment.getFileName());
            }
//            List<SchemeAttributeValue> list = this.schemeAttributeValueDao.getSchemeAttributeValueList(id);
//            System.out.println("attribute value list size = " + list.size());
//            applicant.setSchemeAttributeValueList(list);
            System.out.println("attachment size = " + beneficiary.getBeneficiaryAttachmentList().size());

            BeneficiaryBiometricInfo beneficiaryBiometricInfo = beneficiary.getBeneficiaryBiometricInfo();
            if (beneficiaryBiometricInfo != null) {
                if (beneficiaryBiometricInfo.getPhotoData() != null) {
                    beneficiaryBiometricInfo.setBase64PhotoData(CommonUtility.getBase64String(beneficiaryBiometricInfo.getPhotoData()));
                }
                if (beneficiaryBiometricInfo.getSignatureData() != null) {
                    beneficiaryBiometricInfo.setBase64SignatureData(CommonUtility.getBase64String(beneficiaryBiometricInfo.getSignatureData()));
                }
            }

            if (beneficiary.getIsLMMISExist() == null) {
                beneficiary.setIsLMMISExist(0);
            }
            return beneficiary;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public Beneficiary getBeneficiaryByID(String benID) {
        try {
            System.out.println("Beneficiary ID = " + benID);
            Beneficiary beneficiary = (Beneficiary) this.sessionFactory.getCurrentSession().createQuery("from Beneficiary where nid='" + CommonUtility.getNumberInEnglish(benID) + "'").uniqueResult();
            System.out.println("beneficiary = " + beneficiary);
            return beneficiary;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public void save(Beneficiary beneficiary) {
        try {
            final Session currentSession = this.sessionFactory.getCurrentSession();
            currentSession.save(beneficiary);
            currentSession.flush();
        } catch (Exception e) {
            System.out.println("exception = " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void edit(Beneficiary beneficiary) {
        this.sessionFactory.getCurrentSession().update(beneficiary);
        if (beneficiary.getAttachmentRemoveList() != null) {
            String querySt = "delete from BeneficiaryAttachment where id IN (:list)";
            Query query = this.sessionFactory.getCurrentSession().createQuery(querySt).setParameterList("list", beneficiary.getAttachmentRemoveList());
            query.executeUpdate();
        }
    }

    @Override
    public void edit(Beneficiary beneficiary, boolean fromPaymentDataUpdate) {
        this.sessionFactory.getCurrentSession().update(beneficiary);
        if (beneficiary.getAttachmentRemoveList() != null) {
            String querySt = "delete from BeneficiaryAttachment where id IN (:list)";
            Query query = this.sessionFactory.getCurrentSession().createQuery(querySt).setParameterList("list", beneficiary.getAttachmentRemoveList());
            query.executeUpdate();
        }
        if (fromPaymentDataUpdate) {
            String query = "update Payment set paymentDataUpdatedInBeneficiary=1 where beneficiary.id=" + beneficiary.getId();
            this.sessionFactory.getCurrentSession().createQuery(query).executeUpdate();
        }
    }
//    @Override
//    public void delete(Integer districtId)
//    {
//        District district = (District) this.sessionFactory.getCurrentSession().load(District.class, districtId);
//        if (district != null)
//        {
//            this.sessionFactory.getCurrentSession().delete(district);
//        }
//    }
//

    @Override
    public List<Beneficiary> getBeneficiaryList() {
        @SuppressWarnings("unchecked")
        String querySt = null;
        querySt = "from Beneficiary";
        try {
            List<Beneficiary> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            System.out.println("size = " + list.size());
            return list;
        } catch (Exception e) {
            System.out.println("ex1 = " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param parameter
     * @return
     */
    @Override
    public List<BeneficiaryInfo> getbeneficiaryByGeolocation(Map parameter) {
        @SuppressWarnings("unchecked")

        String upazilaIdSt = (String) parameter.get("upazilaId");

        Integer upazilaId = null;
        if (upazilaIdSt != null && upazilaIdSt != "") {
            upazilaId = Integer.valueOf(upazilaIdSt);
        }

        String querySt = "";
        querySt = "select new com.wfp.lmmis.utility.BeneficiaryInfo(b.id, b.fullNameInEnglish, b.fullNameInBangla, b.nid, b.mobileNo) from Beneficiary b where 0 = 0 ";

        querySt += " and b.presentUpazila.id = " + upazilaId;

        try {
            List<BeneficiaryInfo> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            System.out.println("size = " + list.size());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ex1 = " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Object> getBeneficiaryListBySearchParameter(Map parameter, int offset, int numofRecords) {
        try {
            Integer schemeId = parameter.get("schemeId") != null ? (Integer) parameter.get("schemeId") : null;
            Integer fiscalYearId = parameter.get("fiscalYearId") != null ? (Integer) parameter.get("fiscalYearId") : null;
            String applicantId = parameter.get("applicantId") != null ? (String) parameter.get("applicantId") : null;
            String mobileNo = parameter.get("mobileNo") != null ? (String) parameter.get("mobileNo") : null;
            String accountNo = parameter.get("accountNo") != null ? (String) parameter.get("accountNo") : null;
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            Integer unionId = parameter.get("unionId") != null ? (Integer) parameter.get("unionId") : null;
            Integer wardNo = parameter.get("wardNo") != null ? (Integer) parameter.get("wardNo") : null;
            Integer villageId = parameter.get("villageId") != null ? (Integer) parameter.get("villageId") : null;
            Integer isActive = (Integer) parameter.get("isActive");
            Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
            Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;

            Integer bgmeaFactoryId = parameter.get("bgmeaFactoryId") != null ? (Integer) parameter.get("bgmeaFactoryId") : null;
            Integer bkmeaFactoryId = parameter.get("bkmeaFactoryId") != null ? (Integer) parameter.get("bkmeaFactoryId") : null;
            ApplicantType applicantType = parameter.get("applicantType") != null ? (ApplicantType) parameter.get("applicantType") : null;
            Integer status = (Integer) parameter.get("status");

            System.out.println("applicantType = " + applicantType);
            @SuppressWarnings("unchecked")
            String mainQuerySt = "from BeneficiaryView o where 0=0 ";
            String countQuerySt = "select count(o.id) from BeneficiaryView o where 0=0 ";

            String querySt = "";
            String otherSt = "";
            String conditionSt = "";

            if (fiscalYearId != null && fiscalYearId != 0) {
                querySt += " AND o.fiscalYearId = " + fiscalYearId;
            }
            if (applicantId != null && !applicantId.isEmpty()) {
                querySt += " AND ( o.nid like '%" + CommonUtility.getNumberInEnglish(applicantId) + "%' )";
            }
            if (isActive == 1) {
                querySt += " AND o.beneficiaryStatus=" + BeneficiaryStatus.ACTIVE.ordinal();
            }
            querySt += " AND o.applicantType = " + applicantType.ordinal();

            if (bgmeaFactoryId != null && bgmeaFactoryId != 0) {
                querySt += " and o.factoryId = " + bgmeaFactoryId;
            }
            if (bkmeaFactoryId != null && bkmeaFactoryId != 0) {
                querySt += " and o.factoryId = " + bkmeaFactoryId;
            }
            if (mobileNo != null && !mobileNo.isEmpty()) {
                querySt += " AND ( concat(0,o.mobileNo)  like '%" + CommonUtility.getNumberInEnglish(mobileNo) + "%' )";
            }
            if (accountNo != null && !accountNo.isEmpty()) {
                querySt += " AND ( o.accountNo like '%" + CommonUtility.getNumberInEnglish(accountNo) + "%' )";
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
            if (wardNo != null && wardNo != 0) {
                querySt += " AND o.wardNo = " + wardNo;
            }
            if (villageId != null && villageId != 0) {
                querySt += " AND o.villageId = " + villageId;
            }
            if (startDate != null) {
                querySt += " AND o.enrollmentDate >= :startDate";
            }
            if (endDate != null) {
                querySt += " AND o.enrollmentDate <= :endDate";
            }
            if (status != null && status != -1) {
                querySt += " AND o.beneficiaryStatus = " + status;
            }
            System.out.println("query main = " + querySt);
            System.out.println("query = " + mainQuerySt + querySt);
            Query listQuery = sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt);
            Query countQuery = sessionFactory.getCurrentSession().createQuery(countQuerySt + querySt);
            if (startDate != null) {
                listQuery.setParameter("startDate", startDate.getTime());
                countQuery.setParameter("startDate", startDate.getTime());
            }
            if (endDate != null) {
                listQuery.setParameter("endDate", endDate.getTime());
                countQuery.setParameter("endDate", endDate.getTime());
            }

            List<BeneficiaryView> list = listQuery.setFirstResult(offset).setMaxResults(numofRecords).list();
            long count = (Long) countQuery.list().get(0);
            List<Object> result = new ArrayList<Object>();
            result.add(list);
            result.add(count);
            result.add(count); // ????

            return result;
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportData(Map parameter) {
        try {
            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            Integer wardNo = parameter.get("wardNo") != null ? (Integer) parameter.get("wardNo") : null;
            String orderBy = (String) parameter.get("orderBy");
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;

            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            Integer bgmeaId = (Integer) parameter.get("bgmeaId");
            Integer bkmeaId = (Integer) parameter.get("bkmeaId");

            String query;

            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInBangla, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInBangla, b.presentDistrict.nameInBangla, b.presentUpazila.nameInBangla, b.presentUnion.nameInBangla, b.presentWardNo, b.presentVillage.nameInBangla,"
                        + "COALESCE(bk.nameInBangla, null), COALESCE(mbp.nameInBangla, null), b.accountNo, b.beneficiaryStatus, f.nameInBangla) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.mobileBankingProvider mbp LEFT JOIN b.factory f where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, b.presentWardNo, b.presentVillage.nameInEnglish,"
                        + "bk.nameInEnglish, mbp.nameInEnglish, b.accountNo, b.beneficiaryStatus, f.nameInEnglish) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.mobileBankingProvider mbp LEFT JOIN b.factory f where 0 = 0 ";

            }

            if (schemeId != null && schemeId != 0) {
                query += " AND b.scheme.id = " + schemeId;
            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                query += " AND b.beneficiaryStatus = :beneficiaryStatus";
            }
            query += " AND b.applicantType = " + applicantType.ordinal();
            switch (applicantType) {
                case REGULAR:
                    if (divisionId != null) {
                        query += " and b.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        query += " and b.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        query += " and b.presentUpazila.id=" + upazilaId;
                    }
                    if (unionId != null) {
                        query += " and b.presentUnion.id=" + unionId;
                    }
                    if (wardNo != null && wardNo != 0) {
                        query += " AND b.presentWardNo = " + wardNo;
                    }
                    break;
                case BGMEA:
                    if (bgmeaId != null) {
                        query += " AND b.factory.id=" + bgmeaId;
                    }
                    break;
                case BKMEA:
                    if (bkmeaId != null) {
                        query += " AND b.factory.id=" + bkmeaId;
                    }
                    break;
                default:
                    break;
            }

            query += " order by b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, b.presentWardNo, b.presentVillage.nameInEnglish";
            if (!orderBy.equals("1")) {
                query += " ,b.nid asc";
            } else {
                query += " ,b.fullNameInEnglish ";
            }
            System.out.println("query = " + query);
            System.out.println("hi = " + query);
            Query createQuery = sessionFactory.getCurrentSession().createQuery(query);
            if (beneficiaryStatus != null) {
                createQuery.setParameter("beneficiaryStatus", beneficiaryStatus);
            }
            List<BeneficiaryReportData> list = createQuery.list();
            for (BeneficiaryReportData beneficiaryReportData : list) {
                if ("bn".equals(locale)) {
                    beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID().toString()));
                    beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                    if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                        beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                    }
                } else {
                    if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                        beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                    }
                }
            }
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithPaymentInfo(Map parameter) {
        try {
            String schemeShortName = (String) parameter.get("schemeShortName");
//            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            Integer wardNo = parameter.get("wardNo") != null ? (Integer) parameter.get("wardNo") : null;
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            Integer bgmeaId = (Integer) parameter.get("bgmeaId");
            Integer bkmeaId = (Integer) parameter.get("bkmeaId");
            String orderBy = (String) parameter.get("orderBy");
            Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
            Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;
            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInBangla, b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInBangla, b.presentDistrict.nameInBangla, b.presentUpazila.nameInBangla, b.presentUnion.nameInBangla, b.presentAddressLine1,"
                        + "COALESCE(bk.nameInBangla, null), br.nameInBangla, pob.nameInBangla, COALESCE(mbp.nameInBangla, null), b.accountNo, b.beneficiaryStatus, f.nameInBangla, b.presentWardNo, v.nameInBangla) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp LEFT JOIN b.factory f LEFT join b.presentVillage v where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInBangla, b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, b.presentAddressLine1,"
                        + "bk.nameInEnglish, br.nameInEnglish, pob.nameInEnglish, mbp.nameInEnglish, b.accountNo, b.beneficiaryStatus, f.nameInEnglish, b.presentWardNo, v.nameInBangla) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp LEFT JOIN b.factory f LEFT join b.presentVillage v where 0 = 0 ";

            }

//            if (schemeId != null && schemeId != 0)
//            {
//                query += " AND b.scheme.id = " + schemeId;
//            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                query += " AND b.beneficiaryStatus = :beneficiaryStatus";
            }
            query += " AND b.applicantType = " + applicantType.ordinal();
            if (startDate != null) {
                query += " AND b.enrollmentDate >= :startDate";
            }
            if (endDate != null) {
                query += " AND b.enrollmentDate <= :endDate";
            }
            switch (applicantType) {
                case UNION:
                case MUNICIPAL:
                case CITYCORPORATION:
                    if (divisionId != null) {
                        query += " and b.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        query += " and b.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        query += " and b.presentUpazila.id=" + upazilaId;
                    }
                    if (unionId != null) {
                        query += " and b.presentUnion.id=" + unionId;
                    }
                    if (wardNo != null && wardNo != 0) {
                        query += " AND b.presentWardNo = " + wardNo;
                    }
                    break;
                case BGMEA:
                    if (bgmeaId != null) {
                        query += " AND b.factory.id=" + bgmeaId;
                    }
                    break;
                case BKMEA:
                    if (bkmeaId != null) {
                        query += " AND b.factory.id=" + bkmeaId;
                    }
                    break;
                default:
                    break;
            }
            query += " order by b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, b.presentWardNo, v.nameInEnglish";
            if (orderBy.equals("1")) {
                query += " ,b.nid asc";
            } else {
                query += " ,b.fullNameInEnglish";
            }
            System.out.println("query = " + query);
            Query queryObject = sessionFactory.getCurrentSession().createQuery(query);
            if (startDate != null) {
                queryObject.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                queryObject.setParameter("endDate", endDate);
            }
            if (beneficiaryStatus != null) {
                queryObject.setParameter("beneficiaryStatus", beneficiaryStatus);
            }
            List<BeneficiaryReportData> list = queryObject.list();
            if (applicantType == ApplicantType.UNION || applicantType == ApplicantType.MUNICIPAL || applicantType == ApplicantType.CITYCORPORATION) {
                for (BeneficiaryReportData beneficiaryReportData : list) {
                    if ("bn".equals(locale)) {                        
                        beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID()));
                        beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                        }
                        beneficiaryReportData.setAccountNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getAccountNo()));
                        beneficiaryReportData.setWardNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getWardNo()));
                        if (divisionId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                beneficiaryReportData.setUnion("????? : " + beneficiaryReportData.getDivision() + ",   ???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("????? : " + beneficiaryReportData.getDivision() + ",   ???? : " + beneficiaryReportData.getDistrict() + ",   ????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (districtId == null) {
                            //beneficiaryReportData.setDistrict("???? : " + beneficiaryReportData.getDistrict()); // district bangla text appended
                            if (applicantType == ApplicantType.UNION) {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (upazilaId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                beneficiaryReportData.setUnion("?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                beneficiaryReportData.setUnion("?????? : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportData.setUnion("???? ?????????/?????? : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            }
                        }
                    } else {
                        System.out.println("locale english");
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                        }
                        if (divisionId == null) {
                            beneficiaryReportData.setUnion("Division : " + beneficiaryReportData.getDivision() + ",   District : " + beneficiaryReportData.getDistrict() + ",   Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                        } else if (districtId == null) {
                            beneficiaryReportData.setUnion("District : " + beneficiaryReportData.getDistrict() + ",   Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                        } else if (upazilaId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                beneficiaryReportData.setUnion("Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                beneficiaryReportData.setUnion("Union : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportData.setUnion("City Corporation/Municipal : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            }
                        }
                    }
                    if (beneficiaryReportData.getBankName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                    }
                    if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                    }
                    if (beneficiaryReportData.getpOBranch() != null) {
                        String postOffice = "bn".equals(locale) ? "????? ????" : "Post Office";
                        beneficiaryReportData.setPaymentProviderName(postOffice);
                        beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                    }
                }
            } else {
                for (BeneficiaryReportData beneficiaryReportData : list) {
                    if ("bn".equals(locale)) {
                        beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID()));
                        beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                        beneficiaryReportData.setWardNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getWardNo()));
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                        }
                        beneficiaryReportData.setAccountNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getAccountNo()));
                        beneficiaryReportData.setUnion("?????????? : " + beneficiaryReportData.getFactory());
                    } else {
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                        }
                        beneficiaryReportData.setUnion("Factory : " + beneficiaryReportData.getFactory());
                    }
                    if (beneficiaryReportData.getBankName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                    }
                    if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                    }
                    if (beneficiaryReportData.getpOBranch() != null) {
                        String postOffice = "bn".equals(locale) ? "????? ????" : "Post Office";
                        beneficiaryReportData.setPaymentProviderName(postOffice);
                        beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                    }
                }
            }

            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BeneficiaryReportDataByLocation> getBeneficiarySummaryReportData(Map parameter) {
        try {
//            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            Integer wardNo = (Integer) parameter.get("wardNo");
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            Integer bgmeaId = (Integer) parameter.get("bgmeaId");
            Integer bkmeaId = (Integer) parameter.get("bkmeaId");
            Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
            Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportDataByLocation(b.presentDivision.nameInBangla,"
                        + " b.presentDistrict.nameInBangla, b.presentUpazila.nameInBangla, b.presentUnion.nameInBangla, f.nameInBangla, count(*), b.presentWardNo)"
                        + " from Beneficiary b Left Join b.factory f where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportDataByLocation(b.presentDivision.nameInEnglish,"
                        + " b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, f.nameInBangla, count(*), b.presentWardNo)"
                        + " from Beneficiary b Left Join b.factory f where 0 = 0 ";
            }
//            if (schemeId != null && schemeId != 0)
//            {
//                query += " AND b.scheme.id = " + schemeId;
//            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                query += " AND b.beneficiaryStatus = :beneficiaryStatus";
            }
            query += " AND b.applicantType = " + applicantType.ordinal();
            if (startDate != null) {
                query += " AND b.enrollmentDate >= :startDate";
            }
            if (endDate != null) {
                query += " AND b.enrollmentDate <= :endDate";
            }
            switch (applicantType) {
                case UNION:
                case MUNICIPAL:
                case CITYCORPORATION:
                    if (divisionId != null) {
                        query += " and b.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        query += " and b.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        query += " and b.presentUpazila.id=" + upazilaId;
                    }
                    if (unionId != null) {
                        query += " and b.presentUnion.id=" + unionId;
                    }
                    System.out.println("wardd " + wardNo);
                    if (wardNo != null) {
                        query += " and b.presentWardNo=" + wardNo;
                    }
                    query += " GROUP BY b.presentUnion.nameInBangla,f.nameInBangla,b.presentWardNo,";
                    if (divisionId != null && divisionId != 0) {
                        query += " b.presentDistrict.id";
                        if (districtId != null && districtId != 0) {
                            query += ", b.presentUpazila.id";
                            if (upazilaId != null && upazilaId != 0) {
                                if (unionId != null && unionId != 0) {
                                    query += ", b.presentWardNo order by b.presentWardNo";
                                } else {
                                    query += ", b.presentUnion.id";
                                }
                            } else {
                                query += ", b.presentUpazila.id";
                            }
                        } else {
                            query += ", b.presentDistrict.id";
                        }
                    } else {
                        query += " b.presentDivision.id";
                    }

                    if (unionId != null && unionId != 0) {
                        query += ", b.presentUnion.id";
                    }
//                    if (wardNo != null && wardNo != 0)
//                    {
//                        query += ", b.presentWardNo order by b.presentWardNo";
//                    }
                    break;
                case BGMEA:
                    if (bgmeaId == null) {
                        query += " GROUP BY b.factory.id";
                    }
                    break;
                case BKMEA:
                    if (bkmeaId == null) {
                        query += " GROUP BY b.factory.id";
                    }
                    break;
                default:
                    break;
            }
            //query += " group by b.permanentUnion.id order by b.permanentDistrict.id";
            System.out.println("query = " + query);
            Integer total = 0;
            Query queryObject = sessionFactory.getCurrentSession().createQuery(query);
            if (startDate != null) {
                queryObject.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                queryObject.setParameter("endDate", endDate);
            }
            if (beneficiaryStatus != null) {
                queryObject.setParameter("beneficiaryStatus", beneficiaryStatus);
            }
            List<BeneficiaryReportDataByLocation> list = queryObject.list();
            for (BeneficiaryReportDataByLocation beneficiaryReportDataByLocation : list) {
                total += Integer.parseInt(beneficiaryReportDataByLocation.getBenTotal());
                if ("bn".equals(locale)) {
                    beneficiaryReportDataByLocation.setBenTotal(CommonUtility.getNumberInBangla(beneficiaryReportDataByLocation.getBenTotal()));
                    beneficiaryReportDataByLocation.setUnionTotal(CommonUtility.getNumberInBangla(beneficiaryReportDataByLocation.getUnionTotal()));
                    beneficiaryReportDataByLocation.setGrandTotal(CommonUtility.getNumberInBangla(total.toString()));
                    beneficiaryReportDataByLocation.setWardNo(CommonUtility.getNumberInBangla(beneficiaryReportDataByLocation.getWardNo()));
                } else {
                    beneficiaryReportDataByLocation.setGrandTotal(total.toString());
                }
            }
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BeneficiaryReportDataByLocation> getBeneficiaryGroupReportData(Map parameter) {
        try {
            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");

            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportDataByLocation(b.permanentDivision.nameInBangla,"
                        + " b.permanentDistrict.nameInBangla, b.permanentUpazila.nameInBangla, count(distinct b.permanentUnion.id), count(b.id))"
                        + " from Beneficiary b where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportDataByLocation(b.permanentDivision.nameInEnglish,"
                        + " b.permanentDistrict.nameInEnglish, b.permanentUpazila.nameInEnglish, count(distinct b.permanentUnion.id), count(b.id))"
                        + " from Beneficiary b where 0 = 0 ";
            }
            if (schemeId != null && schemeId != 0) {
                query += " AND b.scheme.id = " + schemeId;
            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                query += " AND b.beneficiaryStatus = :beneficiaryStatus";
            }
            if (divisionId != null) {
                query += " and b.permanentDivision.id=" + divisionId;
            }
            if (districtId != null) {
                query += " and b.permanentDistrict.id=" + districtId;
            }
            if (upazilaId != null) {
                query += " and b.permanentUpazila.id=" + upazilaId;
            }
            if (unionId != null) {
                query += " and b.permanentUnion.id=" + unionId;
            }
            query += " group by b.permanentUnion.id order by b.permanentDistrict.id";
            System.out.println("query = " + query);
            Query createQuery = sessionFactory.getCurrentSession().createQuery(query);
            if (beneficiaryStatus != null) {
                createQuery.setParameter("beneficiaryStatus", beneficiaryStatus);
            }
            List<BeneficiaryReportDataByLocation> list = createQuery.list();
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BeneficiaryReportDataByLocation> getBeneficiaryUpazilaBasedReportData(Map parameter) {
        try {
            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;

            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            String query = "";
            List<BeneficiaryReportDataByLocation> list = new ArrayList<BeneficiaryReportDataByLocation>();

            Long grandTotal = 0L;
            List<Integer> divIds = sessionFactory.getCurrentSession().createQuery("select d.id from Division d where d.deleted = 0 and d.id is not null").list();
            for (Integer divId : divIds) {
                //Get Division by id
                String divQuery = "";
                if ("bn".equals(locale)) {
                    divQuery = "select d.nameInBangla from Division d where d.nameInBangla is not null and d.deleted = 0 and d.id = " + divId;
                } else {
                    divQuery = "select d.nameInEnglish from Division d where d.nameInEnglish is not null and d.deleted = 0 and d.id = " + divId;
                }

                String divName = (String) sessionFactory.getCurrentSession().createQuery(divQuery).uniqueResult();
                String divCountQuery = "select count(id) from Beneficiary b where b.presentDivision.id = " + divId;
//                if (schemeId != null && schemeId != 0)
//                {
//                    divCountQuery += " AND b.scheme.id = " + schemeId;
//                }
                if (fiscalYearId != null && fiscalYearId != 0) {
                    divCountQuery += " AND b.fiscalYear.id = " + fiscalYearId;
                }
                if (beneficiaryStatus != null) {
                    divCountQuery += " AND b.beneficiaryStatus = :beneficiaryStatus";
                }
                divCountQuery += " AND b.applicantType=" + ApplicantType.UNION.ordinal();
                Query divCreateQuery = sessionFactory.getCurrentSession().createQuery(divCountQuery);
                if (beneficiaryStatus != null) {
                    divCreateQuery.setParameter("beneficiaryStatus", beneficiaryStatus);
                }
                Long divCount = (Long) divCreateQuery.uniqueResult();
                grandTotal += divCount;
                if (divCount > 0) {
                    list.add(new BeneficiaryReportDataByLocation(divName, "", "", ""));
                    List<Integer> distIds = sessionFactory.getCurrentSession().createQuery("select d.id from District d where d.id is not null and d.division.id = " + divId).list();
                    for (Integer distId : distIds) {
                        String distQuery = "";
                        if ("bn".equals(locale)) {
                            distQuery = "select d.nameInBangla from District d where d.nameInBangla is not null and d.deleted = 0 and d.id = " + distId;
                        } else {
                            distQuery = "select d.nameInEnglish from District d where d.nameInEnglish is not null and d.deleted = 0 and d.id = " + distId;
                        }

                        String distName = (String) sessionFactory.getCurrentSession().createQuery(distQuery).uniqueResult();
                        //if there is data for district
                        String distCountQuery = "select count(id) from Beneficiary b where b.presentDistrict.id = " + distId;
//                        if (schemeId != null && schemeId != 0)
//                        {
//                            distCountQuery += " AND b.scheme.id = " + schemeId;
//                        }
                        if (fiscalYearId != null && fiscalYearId != 0) {
                            distCountQuery += " AND b.fiscalYear.id = " + fiscalYearId;
                        }
                        if (beneficiaryStatus != null) {
                            distCountQuery += " AND b.beneficiaryStatus = :beneficiaryStatus";
                        }
                        distCountQuery += " AND b.applicantType=" + ApplicantType.UNION.ordinal();
                        Query distCreateQuery = sessionFactory.getCurrentSession().createQuery(distCountQuery);
                        if (beneficiaryStatus != null) {
                            distCreateQuery.setParameter("beneficiaryStatus", beneficiaryStatus);
                        }
                        Long distCount = (Long) distCreateQuery.uniqueResult();
                        if (distCount > 0) {
                            list.add(new BeneficiaryReportDataByLocation("", distName, "", ""));
                            List<Integer> upazilaIds = sessionFactory.getCurrentSession().createQuery("select u.id from Upazilla u where u.id is not null and u.district.id = " + distId).list();
                            for (Integer upazilaId : upazilaIds) {
                                String upazilaQuery = "";
                                if ("bn".equals(locale)) {
                                    upazilaQuery = "select u.nameInBangla from Upazilla u where u.nameInBangla is not null and u.deleted = 0 and u.id = " + upazilaId;
                                } else {
                                    upazilaQuery = "select u.nameInEnglish from Upazilla u where u.nameInEnglish is not null and u.deleted = 0 and u.id = " + upazilaId;
                                }

                                String upazilaName = (String) sessionFactory.getCurrentSession().createQuery(upazilaQuery).uniqueResult();
                                query = "select count(id) from Beneficiary b where 0 = 0 ";
                                if (upazilaId != null && upazilaId != 0) {
                                    query += " AND b.presentUpazila.id = " + upazilaId;
                                }
//                                if (schemeId != null && schemeId != 0)
//                                {
//                                    query += " AND b.scheme.id = " + schemeId;
//                                }
                                if (fiscalYearId != null && fiscalYearId != 0) {
                                    query += " AND b.fiscalYear.id = " + fiscalYearId;
                                }
                                if (beneficiaryStatus != null) {
                                    query += " AND b.beneficiaryStatus = :beneficiaryStatus";
                                }
                                query += " AND b.applicantType=" + ApplicantType.UNION.ordinal();
                                Query createQuery = sessionFactory.getCurrentSession().createQuery(query);
                                if (beneficiaryStatus != null) {
                                    createQuery.setParameter("beneficiaryStatus", beneficiaryStatus);
                                }
                                Long count = (Long) createQuery.uniqueResult();
                                if (count > 0) {
                                    if ("bn".equals(locale)) {
                                        list.add(new BeneficiaryReportDataByLocation("", "", upazilaName, CommonUtility.getNumberInBangla(count.toString())));
                                    } else {
                                        list.add(new BeneficiaryReportDataByLocation("", "", upazilaName, count.toString()));
                                    }
                                }

                            }
                            if ("bn".equals(locale)) {
                                list.add(new BeneficiaryReportDataByLocation("", "????? ???", "", CommonUtility.getNumberInBangla(distCount.toString())));
                            } else {
                                list.add(new BeneficiaryReportDataByLocation("", "Total For District", "", distCount.toString()));
                            }
                        }

                    }
                    if ("bn".equals(locale)) {
                        list.add(new BeneficiaryReportDataByLocation("??????? ???", "", "", CommonUtility.getNumberInBangla(divCount.toString())));
                    } else {
                        list.add(new BeneficiaryReportDataByLocation("Total For Division", "", "", divCount.toString()));
                    }
                }

            }
            if ("bn".equals(locale)) {
                list.add(new BeneficiaryReportDataByLocation("???????", "", "", CommonUtility.getNumberInBangla(grandTotal.toString())));
            } else {
                list.add(new BeneficiaryReportDataByLocation("Grand Total", "", "", grandTotal.toString()));
            }
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BeneficiaryReportDataByLocation> getBeneficiaryReportDataByPaymentType(Map parameter) {
        try {
//            Integer schemeId = (Integer) parameter.get("schemeId");
//            String schemeShortName = (String) parameter.get("schemeShortName");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            String orderBy = (String) parameter.get("orderBy");
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            Integer bgmeaId = (Integer) parameter.get("bgmeaId");
            Integer bkmeaId = (Integer) parameter.get("bkmeaId");
            Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
            Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;

            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportDataByLocation(b.presentDivision.nameInBangla, b.presentDistrict.nameInBangla, b.presentUpazila.nameInBangla, b.presentUnion.nameInBangla, f.nameInBangla, b.paymentType, count(b.id), b.presentWardNo)"
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp  LEFT JOIN b.factory f where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportDataByLocation(b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, f.nameInEnglish, b.paymentType, count(b.id), b.presentWardNo) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp LEFT JOIN b.factory f where 0 = 0 ";

            }

            query += " AND (bk!= null OR mbp.id != null OR pob.id != null) ";
            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                query += " AND b.beneficiaryStatus = :beneficiaryStatus";
            }

            query += " AND b.applicantType = " + applicantType.ordinal();
            if (startDate != null) {
                query += " AND b.enrollmentDate >= :startDate";
            }
            if (endDate != null) {
                query += " AND b.enrollmentDate <= :endDate";
            }
            switch (applicantType) {
                case UNION:
                case MUNICIPAL:
                case CITYCORPORATION:
                    if (divisionId != null) {
                        query += " and b.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        query += " and b.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        query += " and b.presentUpazila.id=" + upazilaId;
                    }
                    query += " group by b.presentDivision.id, b.presentDistrict.id, b.presentUpazila.id, b.presentUnion.id, b.paymentType";
                    break;
                case BGMEA:
                    if (bgmeaId != null) {
                        query += " AND b.factory.id=" + bgmeaId;
                    }
                    query += " GROUP BY b.factory.id";
                    break;
                case BKMEA:
                    if (bkmeaId != null) {
                        query += " AND b.factory.id=" + bkmeaId;
                    }
                    query += " GROUP BY b.factory.id";
                    break;
                default:
                    break;
            }
            query += " order by b.presentDivision.id, b.presentDistrict.id, b.presentUpazila.id, b.presentUnion.id, b.paymentType";
            if (orderBy.equals("1")) {
                query += " ,b.nid asc";
            } else {
                query += " ,b.fullNameInEnglish";
            }

            System.out.println("queryPayment = " + query);
            Query queryObject = sessionFactory.getCurrentSession().createQuery(query);
            if (startDate != null) {
                queryObject.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                queryObject.setParameter("endDate", endDate);
            }
            if (beneficiaryStatus != null) {
                queryObject.setParameter("beneficiaryStatus", beneficiaryStatus);
            }
            List<BeneficiaryReportDataByLocation> list = queryObject.list();
            System.out.println("list size = " + list.size());
            if (applicantType.equals(ApplicantType.UNION) || applicantType.equals(ApplicantType.MUNICIPAL) || applicantType.equals(ApplicantType.CITYCORPORATION)) {
                for (BeneficiaryReportDataByLocation beneficiaryReportDataByLocation : list) {
                    if ("bn".equals(locale)) {
                        if (divisionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportDataByLocation.setUnion("????? : " + beneficiaryReportDataByLocation.getDivision() + ",   ???? : " + beneficiaryReportDataByLocation.getDistrict() + ",   ?????? : " + beneficiaryReportDataByLocation.getUpazila() + ",   ?????? : " + beneficiaryReportDataByLocation.getUnion());
                            } else {
                                beneficiaryReportDataByLocation.setUnion("????? : " + beneficiaryReportDataByLocation.getDivision() + ",   ???? : " + beneficiaryReportDataByLocation.getDistrict() + ",   ????/?????? : " + beneficiaryReportDataByLocation.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportDataByLocation.getUnion());
                            }
                        } else if (districtId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportDataByLocation.setUnion("???? : " + beneficiaryReportDataByLocation.getDistrict() + ",   ?????? : " + beneficiaryReportDataByLocation.getUpazila() + ",   ?????? : " + beneficiaryReportDataByLocation.getUnion());
                            } else {
                                beneficiaryReportDataByLocation.setUnion("???? : " + beneficiaryReportDataByLocation.getDistrict() + ",   ????/?????? : " + beneficiaryReportDataByLocation.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportDataByLocation.getUnion());
                            }
                        } else if (upazilaId == null) {
                            //beneficiaryReportData.setUpazila("?????? : " + beneficiaryReportData.getUpazila()); // upazila bangla text appended
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportDataByLocation.setUnion("?????? : " + beneficiaryReportDataByLocation.getUpazila() + ",   ?????? : " + beneficiaryReportDataByLocation.getUnion());
                            } else {
                                beneficiaryReportDataByLocation.setUnion("????/?????? : " + beneficiaryReportDataByLocation.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportDataByLocation.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportDataByLocation.setUnion("?????? : " + beneficiaryReportDataByLocation.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportDataByLocation.setUnion("???? ?????????/?????? : " + beneficiaryReportDataByLocation.getUnion()); // uinon bangla text appended
                            }
                        }
                        if (beneficiaryReportDataByLocation.getPaymentTypeEnum() != null) {
                            beneficiaryReportDataByLocation.setPaymentProvider(beneficiaryReportDataByLocation.getPaymentTypeEnum().getDisplayNameBn());
                        }
                        beneficiaryReportDataByLocation.setBenTotal(CommonUtility.getNumberInBangla(beneficiaryReportDataByLocation.getBenTotal()));

                    } else {

                        if (divisionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportDataByLocation.setUnion("Division : " + beneficiaryReportDataByLocation.getDivision() + ",   District : " + beneficiaryReportDataByLocation.getDistrict() + ",   Upazila : " + beneficiaryReportDataByLocation.getUpazila() + ",   Union : " + beneficiaryReportDataByLocation.getUnion());
                            } else {
                                beneficiaryReportDataByLocation.setUnion("Division : " + beneficiaryReportDataByLocation.getDivision() + ",   District : " + beneficiaryReportDataByLocation.getDistrict() + ",   District/Upazila : " + beneficiaryReportDataByLocation.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportDataByLocation.getUnion());
                            }
                        } else if (districtId == null) {
                            //beneficiaryReportData.setDistrict("???? : " + beneficiaryReportData.getDistrict()); // district bangla text appended
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportDataByLocation.setUnion("District : " + beneficiaryReportDataByLocation.getDistrict() + ",   Upazila : " + beneficiaryReportDataByLocation.getUpazila() + ",   Union : " + beneficiaryReportDataByLocation.getUnion());
                            } else {
                                beneficiaryReportDataByLocation.setUnion("District : " + beneficiaryReportDataByLocation.getDistrict() + ",   District/Upazila : " + beneficiaryReportDataByLocation.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportDataByLocation.getUnion());
                            }
                        } else if (upazilaId == null) {
                            //beneficiaryReportData.setUpazila("?????? : " + beneficiaryReportData.getUpazila()); // upazila bangla text appended
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportDataByLocation.setUnion("Upazila : " + beneficiaryReportDataByLocation.getUpazila() + ",   Union : " + beneficiaryReportDataByLocation.getUnion());
                            } else {
                                beneficiaryReportDataByLocation.setUnion("District/Upazila : " + beneficiaryReportDataByLocation.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportDataByLocation.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportDataByLocation.setUnion("Union : " + beneficiaryReportDataByLocation.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportDataByLocation.setUnion("City Corporation/Municipal : " + beneficiaryReportDataByLocation.getUnion()); // uinon bangla text appended
                            }
                        }
                        if (beneficiaryReportDataByLocation.getPaymentTypeEnum() != null) {
                            beneficiaryReportDataByLocation.setPaymentProvider(beneficiaryReportDataByLocation.getPaymentTypeEnum().getDisplayName());
                        }
                    }
                }
            } else {
                for (BeneficiaryReportDataByLocation beneficiaryReportDataByLocation : list) {
                    if ("bn".equals(locale)) {

                        if (bgmeaId == null && bkmeaId == null) {
                            beneficiaryReportDataByLocation.setUnion("?????????? : " + beneficiaryReportDataByLocation.getFactory());
                        }
                        if (beneficiaryReportDataByLocation.getPaymentTypeEnum() != null) {
                            beneficiaryReportDataByLocation.setPaymentProvider(beneficiaryReportDataByLocation.getPaymentTypeEnum().getDisplayNameBn());
                        }
                    } else {

                        if (bgmeaId == null && bkmeaId == null) {
                            beneficiaryReportDataByLocation.setUnion("Factory : " + beneficiaryReportDataByLocation.getFactory());
                        }
                        if (beneficiaryReportDataByLocation.getPaymentTypeEnum() != null) {
                            beneficiaryReportDataByLocation.setPaymentProvider(beneficiaryReportDataByLocation.getPaymentTypeEnum().getDisplayName());
                        }
                    }

                }
            }

            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithoutMobileNo(Map parameter) {
        try {

            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            String orderBy = (String) parameter.get("orderBy");
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            Integer bgmeaId = (Integer) parameter.get("bgmeaId");
            Integer bkmeaId = (Integer) parameter.get("bkmeaId");
            Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
            Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;
            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInBangla, b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInBangla, b.presentDistrict.nameInBangla, b.presentUpazila.nameInBangla, b.presentUnion.nameInBangla, b.presentAddressLine1,"
                        + "COALESCE(bk.nameInBangla, null), br.nameInBangla, pob.nameInBangla, COALESCE(mbp.nameInBangla, null), b.accountNo, b.beneficiaryStatus, f.nameInBangla,b.presentWardNo, b.presentVillage.nameInBangla) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp  LEFT JOIN b.factory f where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInBangla, b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, b.presentAddressLine1,"
                        + "bk.nameInEnglish, br.nameInBangla, pob.nameInBangla, mbp.nameInEnglish, b.accountNo, b.beneficiaryStatus, f.nameInEnglish, b.presentWardNo, b.presentVillage.nameInEnglish) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp LEFT JOIN b.factory f where 0 = 0 ";

            }

            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                query += " AND b.beneficiaryStatus = :beneficiaryStatus";
            }
            query += " AND b.applicantType = " + applicantType.ordinal();
            if (startDate != null) {
                query += " AND b.enrollmentDate >= :startDate";
            }
            if (endDate != null) {
                query += " AND b.enrollmentDate <= :endDate";
            }
            switch (applicantType) {
                case UNION:
                case MUNICIPAL:
                case CITYCORPORATION:
                    if (divisionId != null) {
                        query += " and b.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        query += " and b.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        query += " and b.presentUpazila.id=" + upazilaId;
                    }
                    if (unionId != null) {
                        query += " and b.presentUnion.id=" + unionId;
                    }
                    break;
                case BGMEA:
                    if (bgmeaId != null) {
                        query += " AND b.factory.id=" + bgmeaId;
                    }
                    break;
                case BKMEA:
                    if (bkmeaId != null) {
                        query += " AND b.factory.id=" + bkmeaId;
                    }
                    break;
                default:
                    break;
            }
            query += " and b.mobileNo is null";

            query += " order by b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish";
            if (orderBy.equals("1")) {
                query += " ,b.nid asc";
            } else {
                query += " ,b.fullNameInEnglish";
            }
            System.out.println("query = " + query);
            Query queryObject = sessionFactory.getCurrentSession().createQuery(query);
            if (startDate != null) {
                queryObject.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                queryObject.setParameter("endDate", endDate);
            }
            if (beneficiaryStatus != null) {
                queryObject.setParameter("beneficiaryStatus", beneficiaryStatus);
            }
            List<BeneficiaryReportData> list = queryObject.list();
            if (applicantType.equals(ApplicantType.UNION) || applicantType.equals(ApplicantType.MUNICIPAL) || applicantType.equals(ApplicantType.CITYCORPORATION)) {
                for (BeneficiaryReportData beneficiaryReportData : list) {
                    if ("bn".equals(locale)) {
                        beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID().toString()));
                        beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                        }
                        beneficiaryReportData.setAccountNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getAccountNo()));
                        if (divisionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("????? : " + beneficiaryReportData.getDivision() + ",   ???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());

                            } else {
                                beneficiaryReportData.setUnion("????? : " + beneficiaryReportData.getDivision() + ",   ???? : " + beneficiaryReportData.getDistrict() + ",   ????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (districtId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());

                            } else {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (upazilaId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());

                            }
                        } else if (unionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("?????? : " + beneficiaryReportData.getUnion()); // uinon bangla text appended

                            } else {
                                beneficiaryReportData.setUnion("???? ?????????/?????? : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            }
                        }

                    } else {
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                        }
                        if (divisionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Division : " + beneficiaryReportData.getDivision() + ",   District : " + beneficiaryReportData.getDistrict() + ",   Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("Division : " + beneficiaryReportData.getDivision() + ",   District : " + beneficiaryReportData.getDistrict() + ",   District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (districtId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("District : " + beneficiaryReportData.getDistrict() + ",   Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("District : " + beneficiaryReportData.getDistrict() + ",   District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }

                        } else if (upazilaId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Union : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportData.setUnion("City Corporation/Municipal : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            }
                        }
                    }
                    if (beneficiaryReportData.getBankName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                    }
                    if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                    }
                    if (beneficiaryReportData.getpOBranch() != null) {
                        String postOffice = "bn".equals(locale) ? "????? ????" : "Post Office";
                        beneficiaryReportData.setPaymentProviderName(postOffice);
                        beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                    }
                }
            } else {
                for (BeneficiaryReportData beneficiaryReportData : list) {
                    if ("bn".equals(locale)) {

                        beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID().toString()));
                        beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                        }
                        if (beneficiaryReportData.getBankName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                        }
                        if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                        }
                        if (beneficiaryReportData.getpOBranch() != null) {

                            beneficiaryReportData.setPaymentProviderName("????? ????");
                            beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                        }
                        beneficiaryReportData.setAccountNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getAccountNo()));
                        if (bgmeaId == null && bkmeaId == null) {
                            beneficiaryReportData.setUnion("?????????? : " + beneficiaryReportData.getFactory());
                        } else {
                            beneficiaryReportData.setUnion(null);
                        }
                    } else {

                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                        }
                        if (beneficiaryReportData.getBankName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                        }
                        if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                        }
                        if (beneficiaryReportData.getpOBranch() != null) {

                            beneficiaryReportData.setPaymentProviderName("Post Office");
                            beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                        }
                        if (bgmeaId == null && bkmeaId == null) {
                            beneficiaryReportData.setUnion("Factory : " + beneficiaryReportData.getFactory());
                        } else {
                            beneficiaryReportData.setUnion(null);
                        }
                    }

                }
            }
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param parameter
     * @return
     */
    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithoutAccNo(Map parameter) {
        try {
            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            String orderBy = (String) parameter.get("orderBy");
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            Integer bgmeaId = (Integer) parameter.get("bgmeaId");
            Integer bkmeaId = (Integer) parameter.get("bkmeaId");
            Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
            Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;
            String query;

            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInBangla, b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInBangla, b.presentDistrict.nameInBangla, b.presentUpazila.nameInBangla, b.presentUnion.nameInBangla, b.presentAddressLine1,"
                        + "COALESCE(bk.nameInBangla, null), br.nameInBangla, pob.nameInBangla, COALESCE(mbp.nameInBangla, null), b.accountNo, b.beneficiaryStatus, f.nameInBangla, b.presentWardNo, b.presentVillage.nameInBangla) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp  LEFT JOIN b.factory f where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInBangla, b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, b.presentAddressLine1,"
                        + "bk.nameInEnglish, br.nameInBangla, pob.nameInBangla, mbp.nameInEnglish, b.accountNo, b.beneficiaryStatus, f.nameInEnglish, b.presentWardNo, b.presentVillage.nameInEnglish) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp LEFT JOIN b.factory f where 0 = 0 ";

            }

            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                query += " AND b.beneficiaryStatus = :beneficiaryStatus";
            }
            query += " AND b.applicantType = " + applicantType.ordinal();
            if (startDate != null) {
                query += " AND b.enrollmentDate >= :startDate";
            }
            if (endDate != null) {
                query += " AND b.enrollmentDate <= :endDate";
            }
            switch (applicantType) {
                case UNION:
                case MUNICIPAL:
                case CITYCORPORATION:
                    if (divisionId != null) {
                        query += " and b.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        query += " and b.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        query += " and b.presentUpazila.id=" + upazilaId;
                    }
                    if (unionId != null) {
                        query += " and b.presentUnion.id=" + unionId;
                    }
                    break;
                case BGMEA:
                    if (bgmeaId != null) {
                        query += " AND b.factory.id=" + bgmeaId;
                    }
                    break;
                case BKMEA:
                    if (bkmeaId != null) {
                        query += " AND b.factory.id=" + bkmeaId;
                    }
                    break;
                default:
                    break;
            }

            query += " and (b.accountNo is null or b.accountNo = '')";
            query += " order by b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish";
            if (orderBy.equals("1")) {
                query += " ,b.nid asc";
            } else {
                query += " ,b.fullNameInEnglish";
            }
            System.out.println("query = " + query);
            Query queryObject = sessionFactory.getCurrentSession().createQuery(query);
            if (startDate != null) {
                queryObject.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                queryObject.setParameter("endDate", endDate);
            }
            if (beneficiaryStatus != null) {
                queryObject.setParameter("beneficiaryStatus", beneficiaryStatus);
            }
            List<BeneficiaryReportData> list = queryObject.list();
            if (applicantType.equals(ApplicantType.UNION) || applicantType.equals(ApplicantType.MUNICIPAL) || applicantType.equals(ApplicantType.CITYCORPORATION)) {
                for (BeneficiaryReportData beneficiaryReportData : list) {
                    if ("bn".equals(locale)) {
                        beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID().toString()));
                        beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                        }
                        beneficiaryReportData.setAccountNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getAccountNo()));
                        if (divisionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("????? : " + beneficiaryReportData.getDivision() + ",   ???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("????? : " + beneficiaryReportData.getDivision() + ",   ???? : " + beneficiaryReportData.getDistrict() + ",   ????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (districtId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (upazilaId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("?????? : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportData.setUnion("???? ?????????/?????? : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            }
                        }

                    } else {
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                        }
                        if (divisionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Division : " + beneficiaryReportData.getDivision() + ",   District : " + beneficiaryReportData.getDistrict() + ",   Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("Division : " + beneficiaryReportData.getDivision() + ",   District : " + beneficiaryReportData.getDistrict() + ",   District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (districtId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("District : " + beneficiaryReportData.getDistrict() + ",   Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("District : " + beneficiaryReportData.getDistrict() + ",   District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (upazilaId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Union : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportData.setUnion("City Corporation/Municipal : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            }
                        }
                    }
                    if (beneficiaryReportData.getBankName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                    }
                    if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                    }
                    if (beneficiaryReportData.getpOBranch() != null) {
                        String postOffice = "bn".equals(locale) ? "????? ????" : "Post Office";
                        beneficiaryReportData.setPaymentProviderName(postOffice);
                        beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                    }
                }
            } else {
                for (BeneficiaryReportData beneficiaryReportData : list) {
                    if ("bn".equals(locale)) {

                        beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID().toString()));
                        beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                        }
                        beneficiaryReportData.setAccountNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getAccountNo()));
                        if (beneficiaryReportData.getBankName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                        }
                        if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                        }
                        if (beneficiaryReportData.getpOBranch() != null) {

                            beneficiaryReportData.setPaymentProviderName("????? ????");
                            beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                        }
                        if (bgmeaId == null && bkmeaId == null) {
                            beneficiaryReportData.setUnion("?????????? : " + beneficiaryReportData.getFactory());
                        } else {
                            beneficiaryReportData.setUnion(null);
                        }
                    } else {

                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                        }
                        if (beneficiaryReportData.getBankName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                        }
                        if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                        }
                        if (beneficiaryReportData.getpOBranch() != null) {

                            beneficiaryReportData.setPaymentProviderName("Post Office");
                            beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                        }
                        if (bgmeaId == null && bkmeaId == null) {
                            beneficiaryReportData.setUnion("Factory : " + beneficiaryReportData.getFactory());
                        } else {
                            beneficiaryReportData.setUnion(null);
                        }
                    }

                }
            }
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithInvalidAccNo(Map parameter) {
        try {

            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            String orderBy = (String) parameter.get("orderBy");
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            Integer bgmeaId = (Integer) parameter.get("bgmeaId");
            Integer bkmeaId = (Integer) parameter.get("bkmeaId");
            Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
            Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;
            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInBangla, b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInBangla, b.presentDistrict.nameInBangla, b.presentUpazila.nameInBangla, b.presentUnion.nameInBangla, b.presentAddressLine1,"
                        + "COALESCE(bk.nameInBangla, null), br.nameInBangla, pob.nameInBangla, COALESCE(mbp.nameInBangla, null), b.accountNo, b.beneficiaryStatus, f.nameInBangla, b.presentWardNo, b.presentVillage.nameInBangla) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp  LEFT JOIN b.factory f where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInBangla, b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, b.presentAddressLine1,"
                        + "bk.nameInEnglish, br.nameInBangla, pob.nameInBangla, mbp.nameInEnglish, b.accountNo, b.beneficiaryStatus, f.nameInEnglish, b.presentWardNo, b.presentVillage.nameInEnglish) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp LEFT JOIN b.factory f where 0 = 0 ";

            }

            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                query += " AND b.beneficiaryStatus = :beneficiaryStatus";
            }
            query += " AND b.applicantType = " + applicantType.ordinal();
            if (startDate != null) {
                query += " AND b.enrollmentDate >= :startDate";
            }
            if (endDate != null) {
                query += " AND b.enrollmentDate <= :endDate";
            }
            switch (applicantType) {
                case UNION:
                case MUNICIPAL:
                case CITYCORPORATION:
                    if (divisionId != null) {
                        query += " and b.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        query += " and b.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        query += " and b.presentUpazila.id=" + upazilaId;
                    }
                    if (unionId != null) {
                        query += " and b.presentUnion.id=" + unionId;
                    }
                    break;
                case BGMEA:
                    if (bgmeaId != null) {
                        query += " AND b.factory.id=" + bgmeaId;
                    }
                    break;
                case BKMEA:
                    if (bkmeaId != null) {
                        query += " AND b.factory.id=" + bkmeaId;
                    }
                    break;
                default:
                    break;
            }

            query += " and ((b.accountNo is null or b.accountNo = '' or length(b.accountNo)<11)";
            query += " or (bk is null and mbp is null and pob is null)";
//            query += " or case when bk is not null then br is null else false end)";
            query += " or (b.bank is not null and b.branch is null))";
            query += " order by b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish";
            if (orderBy.equals("1")) {
                query += " ,b.nid asc";
            } else {
                query += " ,b.fullNameInEnglish";
            }
            System.out.println("query = " + query);
            Query queryObject = sessionFactory.getCurrentSession().createQuery(query);
            if (startDate != null) {
                queryObject.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                queryObject.setParameter("endDate", endDate);
            }
            if (beneficiaryStatus != null) {
                queryObject.setParameter("beneficiaryStatus", beneficiaryStatus);
            }
            List<BeneficiaryReportData> list = queryObject.list();

            if (applicantType.equals(ApplicantType.UNION) || applicantType.equals(ApplicantType.MUNICIPAL) || applicantType.equals(ApplicantType.CITYCORPORATION)) {
                for (BeneficiaryReportData beneficiaryReportData : list) {
                    if ("bn".equals(locale)) {
                        beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID().toString()));
                        beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                        }
                        beneficiaryReportData.setAccountNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getAccountNo()));
                        if (divisionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("????? : " + beneficiaryReportData.getDivision() + ",   ???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("????? : " + beneficiaryReportData.getDivision() + ",   ???? : " + beneficiaryReportData.getDistrict() + ",   ????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (districtId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (upazilaId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("?????? : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportData.setUnion("???? ?????????/?????? : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            }
                        }

                    } else {
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                        }
                        if (divisionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Division : " + beneficiaryReportData.getDivision() + ",   District : " + beneficiaryReportData.getDistrict() + ",   Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("Division : " + beneficiaryReportData.getDivision() + ",   District : " + beneficiaryReportData.getDistrict() + ",   District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (districtId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("District : " + beneficiaryReportData.getDistrict() + ",   Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("District : " + beneficiaryReportData.getDistrict() + ",   District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (upazilaId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Union : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportData.setUnion("City Corporation/Municipal : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            }
                        }
                    }
                    if (beneficiaryReportData.getBankName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                    }
                    if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                    }
                    if (beneficiaryReportData.getpOBranch() != null) {
                        String postOffice = "bn".equals(locale) ? "????? ????" : "Post Office";
                        beneficiaryReportData.setPaymentProviderName(postOffice);
                        beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                    }
                }
            } else {
                for (BeneficiaryReportData beneficiaryReportData : list) {
                    if ("bn".equals(locale)) {

                        beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID().toString()));
                        beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                        }
                        beneficiaryReportData.setAccountNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getAccountNo()));
                        if (beneficiaryReportData.getBankName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                        }
                        if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                        }
                        if (beneficiaryReportData.getpOBranch() != null) {
                            beneficiaryReportData.setPaymentProviderName("????? ????");
                            beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                        }
                        if (bgmeaId == null && bkmeaId == null) {
                            beneficiaryReportData.setUnion("?????????? : " + beneficiaryReportData.getFactory());
                        } else {
                            beneficiaryReportData.setUnion(null);
                        }
                    } else {

                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                        }
                        if (beneficiaryReportData.getBankName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                        }
                        if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                        }
                        if (beneficiaryReportData.getpOBranch() != null) {
                            beneficiaryReportData.setPaymentProviderName("Post Office");
                            beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                        }
                        if (bgmeaId == null && bkmeaId == null) {
                            beneficiaryReportData.setUnion("Factory : " + beneficiaryReportData.getFactory());
                        } else {
                            beneficiaryReportData.setUnion(null);
                        }
                    }

                }
            }
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithDupplicateAccNo(Map parameter) {
        try {
            Integer schemeId = (Integer) parameter.get("schemeId");
            String schemeShortName = (String) parameter.get("schemeShortName");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            String orderBy = (String) parameter.get("orderBy");
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            Integer bgmeaId = (Integer) parameter.get("bgmeaId");
            Integer bkmeaId = (Integer) parameter.get("bkmeaId");
            String query;
            String duplicateQuery = "select o.accountNo from Beneficiary o where 0=0";
            if (fiscalYearId != null && fiscalYearId != 0) {
                duplicateQuery += " AND o.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                duplicateQuery += " AND o.beneficiaryStatus = :beneficiaryStatus";
            }
            duplicateQuery += " AND o.applicantType = " + applicantType.ordinal();
            switch (applicantType) {
                case UNION:
                case MUNICIPAL:
                case CITYCORPORATION:
                    if (divisionId != null) {
                        duplicateQuery += " and o.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        duplicateQuery += " and o.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        duplicateQuery += " and o.presentUpazila.id=" + upazilaId;
                    }
                    if (unionId != null) {
                        duplicateQuery += " and o.presentUnion.id=" + unionId;
                    }
                    break;
                case BGMEA:
                    if (bgmeaId != null) {
                        duplicateQuery += " AND o.factory.id=" + bgmeaId;
                    }
                    break;
                case BKMEA:
                    if (bkmeaId != null) {
                        duplicateQuery += " AND o.factory.id=" + bkmeaId;
                    }
                    break;
                default:
                    break;
            }
            duplicateQuery += " group by o.accountNo having count(*)>1";

            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInBangla, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInBangla, b.presentDistrict.nameInBangla, b.presentUpazila.nameInBangla, b.presentUnion.nameInBangla, b.presentAddressLine1,"
                        + "COALESCE(bk.nameInBangla, null), br.nameInBangla, pob.nameInBangla, COALESCE(mbp.nameInBangla, null), b.accountNo, b.beneficiaryStatus, b.presentWardNo, b.presentVillage.nameInBangla) "
                        + " from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp LEFT JOIN b.factory f "
                        + " where 0=0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.BeneficiaryReportData(b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, b.presentAddressLine1,"
                        + "COALESCE(bk.nameInEnglish, null), br.nameInEnglish, pob.nameInEnglish, COALESCE(mbp.nameInEnglish, null), b.accountNo, b.beneficiaryStatus, b.presentWardNo, b.presentVillage.nameInEnglish) "
                        + " from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.branch br LEFT JOIN b.postOfficeBranch pob LEFT JOIN b.mobileBankingProvider mbp LEFT JOIN b.factory f "
                        + " where 0=0 ";
            }

//            if (schemeId != null && schemeId != 0)
//            {
//                query += " AND b.scheme.id = " + schemeId;
//            }
            query += " AND b.applicantType = " + applicantType.ordinal();
            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                query += " AND b.beneficiaryStatus = :beneficiaryStatus";
            }
            query += " AND b.applicantType = " + applicantType.ordinal();
            switch (applicantType) {
                case UNION:
                case MUNICIPAL:
                case CITYCORPORATION:
                    if (divisionId != null) {
                        query += " and b.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        query += " and b.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        query += " and b.presentUpazila.id=" + upazilaId;
                    }
                    if (unionId != null) {
                        query += " and b.presentUnion.id=" + unionId;
                    }
                    break;
                case BGMEA:
                    if (bgmeaId != null) {
                        query += " AND b.factory.id=" + bgmeaId;
                    }
                    break;
                case BKMEA:
                    if (bkmeaId != null) {
                        query += " AND b.factory.id=" + bkmeaId;
                    }
                    break;
                default:
                    break;
            }
            query += " and b.accountNo in (" + duplicateQuery + ")";
            query += " order by b.accountNo";
            if (orderBy.equals("1")) {
                query += " ,b.nid asc";
            } else {
                query += " ,b.fullNameInEnglish";
            }
            System.out.println("query = " + query);
            Query createQuery = sessionFactory.getCurrentSession().createQuery(query);
            if (beneficiaryStatus != null) {
                createQuery.setParameter("beneficiaryStatus", beneficiaryStatus);
            }
            List<BeneficiaryReportData> list = createQuery.list();
            System.out.println("list.size() = " + list.size());
            if (applicantType == ApplicantType.UNION || applicantType == ApplicantType.MUNICIPAL || applicantType == ApplicantType.CITYCORPORATION) {
                for (BeneficiaryReportData beneficiaryReportData : list) {
                    if ("bn".equals(locale)) {
                        beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID().toString()));
                        beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                        }
                        beneficiaryReportData.setAccountNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getAccountNo()));
                        if (divisionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("????? : " + beneficiaryReportData.getDivision() + ",   ???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("????? : " + beneficiaryReportData.getDivision() + ",   ???? : " + beneficiaryReportData.getDistrict() + ",   ????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (districtId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (upazilaId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("????/?????? : " + beneficiaryReportData.getUpazila() + ",   ???? ?????????/?????? : " + beneficiaryReportData.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("?????? : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportData.setUnion("???? ?????????/?????? : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            }
                        }

                    } else {
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                        }
                        if (divisionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Division : " + beneficiaryReportData.getDivision() + ",   District : " + beneficiaryReportData.getDistrict() + ",   Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("Division : " + beneficiaryReportData.getDivision() + ",   District : " + beneficiaryReportData.getDistrict() + ",   District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (districtId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("???? : " + beneficiaryReportData.getDistrict() + ",   ?????? : " + beneficiaryReportData.getUpazila() + ",   ?????? : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("District : " + beneficiaryReportData.getDistrict() + ",   District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (upazilaId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Upazila : " + beneficiaryReportData.getUpazila() + ",   Union : " + beneficiaryReportData.getUnion());
                            } else {
                                beneficiaryReportData.setUnion("District/Upazila : " + beneficiaryReportData.getUpazila() + ",   City Corporation/Municipal : " + beneficiaryReportData.getUnion());
                            }
                        } else if (unionId == null) {
                            if (applicantType.equals(ApplicantType.UNION)) {
                                beneficiaryReportData.setUnion("Union : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            } else {
                                beneficiaryReportData.setUnion("City Corporation/Municipal : " + beneficiaryReportData.getUnion()); // uinon bangla text appended
                            }
                        }
                    }
                    if (beneficiaryReportData.getBankName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                    }
                    if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                        beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                    }
                    if (beneficiaryReportData.getpOBranch() != null) {
                        String postOffice = "bn".equals(locale) ? "????? ????" : "Post Office";
                        beneficiaryReportData.setPaymentProviderName(postOffice);
                        beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                    }
                }
            } else {
                for (BeneficiaryReportData beneficiaryReportData : list) {
                    if ("bn".equals(locale)) {

                        beneficiaryReportData.setNationalID(CommonUtility.getNumberInBangla(beneficiaryReportData.getNationalID().toString()));
                        beneficiaryReportData.setMobileNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getMobileNo()));
                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayNameBn());
                        }
                        beneficiaryReportData.setAccountNo(CommonUtility.getNumberInBangla(beneficiaryReportData.getAccountNo()));
                        beneficiaryReportData.setUnion("?????????? : " + beneficiaryReportData.getFactory());
                    } else {

                        if (beneficiaryReportData.getBeneficiaryStatus() != null) {
                            beneficiaryReportData.setStatus(beneficiaryReportData.getBeneficiaryStatus().getDisplayName());
                        }
                        if (beneficiaryReportData.getBankName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getBankName());
                        }
                        if (beneficiaryReportData.getMobileBankingProviderName() != null) {
                            beneficiaryReportData.setPaymentProviderName(beneficiaryReportData.getMobileBankingProviderName());
                        }
                        if (beneficiaryReportData.getpOBranch() != null) {
                            String postOffice = "bn".equals(locale) ? "????? ????" : "Post Office";
                            beneficiaryReportData.setPaymentProviderName(postOffice);
                            beneficiaryReportData.setBranchName(beneficiaryReportData.getpOBranch());
                        }
                        beneficiaryReportData.setUnion("Factory : " + beneficiaryReportData.getFactory());
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
    public List<Beneficiary> getBeneficiaryListByUnionForCardPrinting(Map parameter) {
        try {
            Integer schemeId = !parameter.get("schemeId").equals("") ? (Integer) parameter.get("schemeId") : null;
            Integer fiscalYearId = parameter.get("fiscalYearId") != null ? (Integer) parameter.get("fiscalYearId") : null;
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            Integer unionId = parameter.get("unionId") != null ? (Integer) parameter.get("unionId") : null;

            @SuppressWarnings("unchecked")
            String querySt = "from Beneficiary o where 0=0 ";

            if (schemeId != null && schemeId != 0) {
                querySt += " AND o.scheme.id = " + schemeId;
            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                querySt += " AND o.fiscalYear.id = " + fiscalYearId;
            }
//            if (applicantId != null && !applicantId.isEmpty() )
//            {
//                querySt += " AND ( o.applicationID like '%" + applicantId + "%' OR o.nid like '%" + applicantId + "%' )";
//            }
            if (divisionId != null && divisionId != 0) {
                querySt += " AND o.permanentDivision.id = " + divisionId;
            }
            if (districtId != null && districtId != 0) {
                querySt += " AND o.permanentDistrict.id = " + districtId;
            }
            if (upazilaId != null && upazilaId != 0) {
                querySt += " AND o.permanentUpazila.id = " + upazilaId;
            }
            if (unionId != null && unionId != 0) {
                querySt += " AND o.permanentUnion.id = " + unionId;
            }
            List<Beneficiary> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return list;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean checkUniqueBenNid(BigInteger nid, Integer benId) {
        // return true if unique
        try {
            String querySt;
            if (benId == null) {
                querySt = String.format("from Beneficiary o where o.nid ='%s'", nid);
            } else {
                querySt = String.format("from Beneficiary o where o.id!=%s and o.nid ='%s'", benId, nid);
            }
            System.out.println("querySt = " + querySt);
            List<Beneficiary> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return list.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    //Beneficiary Data Entry Report 
    @Override
    public List<DataEntryReportData> getDataEntryDetailsReportData(Map parameter) {
        try {
            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            String locale = parameter.get("locale") != null ? ((Locale) parameter.get("locale")).toString() : null;
            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.DataEntryReportData(b.fullNameInBangla, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInBangla, b.presentDistrict.nameInBangla, b.presentUpazila.nameInBangla, b.presentUnion.nameInBangla, "
                        + "COALESCE(bk.nameInBangla, null), COALESCE(mbp.nameInBangla, null), b.accountNo, b.beneficiaryStatus, user.userID) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.mobileBankingProvider mbp  JOIN b.createdBy user where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.DataEntryReportData(b.fullNameInEnglish, b.nid, b.fatherName, b.motherName, b.spouseName, b.dateOfBirth,"
                        + "b.mobileNo, b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish, "
                        + "bk.nameInEnglish, mbp.nameInEnglish, b.accountNo, b.beneficiaryStatus, user.userID) "
                        + "from Beneficiary b LEFT JOIN b.bank bk LEFT JOIN b.mobileBankingProvider mbp JOIN b.createdBy user where 0 = 0 ";

            }

            if (schemeId != null && schemeId != 0) {
                query += " AND b.scheme.id = " + schemeId;
            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (beneficiaryStatus != null) {
                query += " AND b.beneficiaryStatus = :beneficiaryStatus";
            }
            if (divisionId != null) {
                query += " and b.presentDivision.id=" + divisionId;
            }
            if (districtId != null) {
                query += " and b.presentDistrict.id=" + districtId;
            }
            if (upazilaId != null) {
                query += " and b.presentUpazila.id=" + upazilaId;
            }
            if (unionId != null) {
                query += " and b.presentUnion.id=" + unionId;
            }

            query += " order by b.createdBy.userID, b.presentDivision.nameInEnglish, b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, b.presentUnion.nameInEnglish";
            System.out.println("query = " + query);
            Query createQuery = sessionFactory.getCurrentSession().createQuery(query);
            if (beneficiaryStatus != null) {
                createQuery.setParameter("beneficiaryStatus", beneficiaryStatus);
            }
            List<DataEntryReportData> list = createQuery.list();
            for (DataEntryReportData dataEntryReportData : list) {
                if ("bn".equals(locale)) {
                    dataEntryReportData.setNationalID(CommonUtility.getNumberInBangla(dataEntryReportData.getNationalID().toString()));
                    dataEntryReportData.setMobileNo(CommonUtility.getNumberInBangla(dataEntryReportData.getMobileNo()));
                    if (dataEntryReportData.getBeneficiaryStatus() != null) {
                        dataEntryReportData.setStatus(dataEntryReportData.getBeneficiaryStatus().getDisplayNameBn());
                    }
                    dataEntryReportData.setAccountNo(CommonUtility.getNumberInBangla(dataEntryReportData.getAccountNo()));
                } else {
                    if (dataEntryReportData.getBeneficiaryStatus() != null) {
                        dataEntryReportData.setStatus(dataEntryReportData.getBeneficiaryStatus().getDisplayName());
                    }
                }
                if (dataEntryReportData.getBankName() != null) {
                    dataEntryReportData.setPaymentProviderName(dataEntryReportData.getBankName());
                }
                if (dataEntryReportData.getMobileBankingProviderName() != null) {
                    dataEntryReportData.setPaymentProviderName(dataEntryReportData.getMobileBankingProviderName());
                }
            }
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DataEntryReportDataByUser> getDataEntrySummaryReportData(Map parameter) {
        try {
            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");

            String locale = parameter.get("locale") != null ? ((Locale) parameter.get("locale")).toString() : null;
            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.DataEntryReportDataByUser(b.presentDivision.nameInBangla,"
                        + " b.presentDistrict.nameInBangla, b.presentUpazila.nameInBangla, cb.userID, count(*))"
                        + " from Beneficiary b join b.createdBy cb where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.DataEntryReportDataByUser(b.presentDivision.nameInEnglish,"
                        + " b.presentDistrict.nameInEnglish, b.presentUpazila.nameInEnglish, cb.userID, count(*))"
                        + " from Beneficiary b join b.createdBy cb where 0 = 0 ";
            }
            if (schemeId != null && schemeId != 0) {
                query += " AND b.scheme.id = " + schemeId;
            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }

            query += " GROUP BY ";

            query += " b.presentDivision.id, b.presentDistrict.id, b.presentUpazila.id, b.createdBy.id";
            query += " order by b.createdBy.id";
            //query += " group by b.presentUnion.id order by b.presentDistrict.id";
            System.out.println("query = " + query);
            Integer total = 0;
            Query createQuery = sessionFactory.getCurrentSession().createQuery(query);

            List<DataEntryReportDataByUser> list = createQuery.list();
            for (DataEntryReportDataByUser dataEntryReportDataByUser : list) {
                if ("bn".equals(locale)) {
                    dataEntryReportDataByUser.setUserID("???????????: " + dataEntryReportDataByUser.getUserID());
                    dataEntryReportDataByUser.setUserTotal(CommonUtility.getNumberInBangla(dataEntryReportDataByUser.getUserTotal()));
                } else {
                    dataEntryReportDataByUser.setUserID("User: " + dataEntryReportDataByUser.getUserID());
                    dataEntryReportDataByUser.setUserTotal(dataEntryReportDataByUser.getUserTotal().toString());
                }
            }
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
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

    @Override
    public List<BeneficiaryReportData> getBeneficiaryReportDataWithLMMISExist(Map parameter) {
        List<BeneficiaryReportData> beneficiaryReportDatas = new ArrayList<>();
        parameter.put("isLmMISExist", 1);
        String queryBuild = queryBuildForLMMISExit(parameter);
        String query = "SELECT beneficiary.full_name_in_bangla benNameBn, \n"
                + "beneficiary.full_name_in_english benNameEn, \n"
                + "beneficiary.nid nationalID, \n"
                + "beneficiary.father_name fatherName,\n"
                + "beneficiary.mother_name motherName,\n"
                + "COALESCE(beneficiary.mobile_number,'') mobileNo,\n"
                + "beneficiary.spouse_name spouseName,\n"
                + "beneficiary.permanent_ward_no wardNo,\n"
                + "division.name_in_bangla division,\n"
                + "district.name_in_bangla district,\n"
                + "upazila.name_in_bangla upazila,\n"
                + "CONCAT('জেলা :',district.name_in_bangla,',   উপজেলা :',upazila.name_in_bangla,',   ইউনিয়ন :',unions.name_in_bangla ) `union`,\n"
                + "if(beneficiary.is_lm_mis_exist=1,\"দরিদ্র মা'র জন্য মাতৃত্বকাল ভাতা\",'ল্যাকটেটিং মাদার ভাতা') lmMisStatus,\n"
                + "village.name_in_bangla village,\n"
                + "beneficiary.conception_duration conceptionDuration,\n"
                + "CASE 	WHEN  beneficiary.conception_term=0 THEN 'প্রথম গর্ভধারণ' ELSE 'দ্বিতীয় গর্ভধারণ'END conceptionTerm,\n"
                //+ "CONCAT('???? :',oldDis.name_in_bangla,',   ?????? :',oldUpa.name_in_bangla,',   ?????? :',oldUnio.name_in_bangla ) `oldGeo`,\n"
                + "COALESCE(oldDiv.name_in_bangla,'') oldDivision,\n"
                + "COALESCE(oldDis.name_in_bangla,'') oldDistrict,\n"
                + "COALESCE(oldUpa.name_in_bangla,'') oldUpazila,\n"
                + "COALESCE(oldUnio.name_in_bangla,'')  oldUnion,\n"
                + "COALESCE(beneficiary.old_mis_fiscal_year_name,'') oldFiscalYearName,\n"
                + "COALESCE(beneficiary.old_mis_scheme_name,'') oldSchemeName,\n"
                + "COALESCE(beneficiary.old_conception_duration,'') oldConceptionDuration,\n"
                + "CASE 	WHEN  beneficiary.old_conception_term=0 THEN 'প্রথম গর্ভধারণ' ELSE 'দ্বিতীয় গর্ভধারণ'END oldConceptionTerm\n"
                + "FROM beneficiary\n"
                + "Left JOIN division ON division.id = beneficiary.permanent_division_id\n"
                + "Left JOIN district ON district.id = beneficiary.permanent_district_id\n"
                + "Left JOIN upazila ON upazila.id = beneficiary.permanent_upazila_id\n"
                + "Left JOIN unions ON unions.id = beneficiary.permanent_union_id\n"
                + "Left JOIN village ON village.id = beneficiary.permanent_village_id \n"
                + "LEFT JOIN division oldDiv ON oldDiv.id = beneficiary.old_mis_division\n"
                + "LEFT JOIN district oldDis ON oldDis.id = beneficiary.old_mis_district\n"
                + "LEFT JOIN upazila oldUpa ON oldUpa.id = beneficiary.old_mis_upazila\n"
                + "LEFT JOIN unions oldUnio ON oldUnio.id = beneficiary.old_mis_union";
        query = query + queryBuild + "\n"
                + "order by division.name_in_english, \n"
                + "district.name_in_english, \n"
                + "upazila.name_in_english,\n"
                + "unions.name_in_english, beneficiary.present_ward_no,\n"
                + "village.name_in_english";
        beneficiaryReportDatas = sessionFactory.getCurrentSession().createSQLQuery(query)
                .addScalar("benNameBn", StringType.INSTANCE)
                .addScalar("benNameEn", StringType.INSTANCE)
                .addScalar("nationalID", StringType.INSTANCE)
                .addScalar("fatherName", StringType.INSTANCE)
                .addScalar("motherName", StringType.INSTANCE)
                .addScalar("mobileNo", StringType.INSTANCE)
                .addScalar("wardNo", StringType.INSTANCE)
                .addScalar("division", StringType.INSTANCE)
                .addScalar("district", StringType.INSTANCE)
                .addScalar("upazila", StringType.INSTANCE)
                .addScalar("union", StringType.INSTANCE)
                .addScalar("lmMisStatus", StringType.INSTANCE)
                .addScalar("village", StringType.INSTANCE)
                .addScalar("spouseName", StringType.INSTANCE)
                .addScalar("conceptionDuration", StringType.INSTANCE)
                .addScalar("conceptionTerm", StringType.INSTANCE)
                .addScalar("oldDivision", StringType.INSTANCE)
                .addScalar("oldDistrict", StringType.INSTANCE)
                .addScalar("oldUpazila", StringType.INSTANCE)
                .addScalar("oldUnion", StringType.INSTANCE)
                .addScalar("oldFiscalYearName", StringType.INSTANCE)
                .addScalar("oldSchemeName", StringType.INSTANCE)
                .addScalar("oldConceptionDuration", StringType.INSTANCE)
                .addScalar("oldConceptionTerm", StringType.INSTANCE)
                //   .addScalar("oldGeo", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(BeneficiaryReportData.class)).list();

        beneficiaryReportDatas.forEach(a -> {
            if (a.getMobileNo().length() == 10) {
                a.setMobileNo("0" + a.getMobileNo());

            }
            a.setMobileNo(CommonUtility.getNumberInBangla(a.getMobileNo()));
            a.setNationalID(CommonUtility.getNumberInBangla(a.getNationalID()));
            //String oldGeo = "???? :" + a.getOldDistrict() + ",?????? :" + a.getOldUpazila() + ",   ?????? :'" + a.getOldUnion();
            //String oldGeo = a.getOldDivision()+ "->"+ a.getOldDistrict() + "->" + a.getOldUpazila() + "->" + a.getOldUnion();
            String oldGeo = a.getOldDivision() + "->" + a.getOldDistrict() + "->" + a.getOldUpazila();
            a.setOldGeo(oldGeo);
            a.setWardNo(CommonUtility.getNumberInBangla(a.getWardNo()));
        });
        return beneficiaryReportDatas;
    }

    private String queryBuildForLMMISExit(Map parameter) {
        String schemeShortName = (String) parameter.get("schemeShortName");
        Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
        Integer isLmMISExist = (Integer) parameter.get("isLmMISExist");
        BeneficiaryStatus beneficiaryStatus = parameter.get("beneficiaryStatus") != null ? (BeneficiaryStatus) parameter.get("beneficiaryStatus") : null;
        Integer divisionId = (Integer) parameter.get("divisionId");
        Integer districtId = (Integer) parameter.get("districtId");
        Integer upazilaId = (Integer) parameter.get("upazilaId");
        Integer unionId = (Integer) parameter.get("unionId");
        Integer wardNo = (Integer) parameter.get("wardNo");
        String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
        String orderBy = (String) parameter.get("orderBy");
        ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
        Integer bgmeaId = (Integer) parameter.get("bgmeaId");
        Integer bkmeaId = (Integer) parameter.get("bkmeaId");
        Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
        Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDateSt = "";
        String endDateSt = "";

        if (startDate != null) {
            startDateSt = df.format(startDate.getTime());

        }
        if (endDate != null) {
            endDateSt = df.format(endDate.getTime());
        }

        List<String> querList = new ArrayList<>();
        querList.add("beneficiary.`status` = 0");
        if (isLmMISExist != null && isLmMISExist != 0) {
            querList.add("beneficiary.is_lm_mis_exist = " + isLmMISExist);

        }
        if (fiscalYearId != null && fiscalYearId != 0) {
            querList.add("beneficiary.fiscal_year_id = " + fiscalYearId);

        }

        if (divisionId != null && divisionId != 0) {
            querList.add("beneficiary.permanent_division_id = " + divisionId);

        }
        if (districtId != null && districtId != 0) {
            querList.add("beneficiary.permanent_district_id = " + districtId);
        }
        if (upazilaId != null && upazilaId != 0) {
            querList.add("beneficiary.permanent_upazila_id = " + upazilaId);
        }
        if (unionId != null && unionId != 0) {
            querList.add("beneficiary.permanent_union_id = " + unionId);

        }
        if (wardNo != null && wardNo != 0) {
            querList.add("beneficiary.permanent_ward_no = " + wardNo);
        }

        if (startDate != null) {
            querList.add("beneficiary.enrollment_date > '" + startDateSt + "' ");
        }
        if (endDate != null) {
            querList.add("beneficiary.enrollment_date <= '" + endDateSt + "' ");
        }
        if (applicantType != null) {
            querList.add("beneficiary.applicant_type = " + applicantType.ordinal());

        }

        String query = String.join(" and ", querList);
        if (querList.size() > 0) {
            query = " where " + query;
        }
        return query;
    }

    @Override
    public int getOtherMISchemeId(String nid) {
        try {
            String query = "SELECT mowca_new.beneficiary.scheme_id scheme_id FROM mowca_new.beneficiary\n"
                    + " WHERE mowca_new.beneficiary.nid = '" + nid + "'";

            List data = sessionFactory.getCurrentSession().createSQLQuery(query).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
            Integer count = null;
            for (Object object : data) {
                Map row = (Map) object;
                count = Integer.parseInt(row.get("scheme_id").toString());
            }
            if (count != null) {
                return count;
            }
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

}

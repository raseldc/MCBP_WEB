package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.model.ApplicantBiometricInfo;
import com.wfp.lmmis.applicant.model.ApplicantSocioEconomicInfo;
import com.wfp.lmmis.applicant.model.ApplicantView;
import com.wfp.lmmis.applicant.model.SchemeAttributeValue;
import com.wfp.lmmis.enums.ApplicantStatus;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.StageType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.payroll.model.PaymentCycle;
import com.wfp.lmmis.report.data.ApplicantReportData;
import com.wfp.lmmis.report.data.ApplicantReportDataByLocation;

import com.wfp.lmmis.report.data.DoubleDippingReportData;
import com.wfp.lmmis.selection.model.SelectionComments;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.types.BeneficiaryStatus;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.JsonResult;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public class ApplicantDaoImpl implements ApplicantDao {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    SchemeAttributeValueDao schemeAttributeValueDao;

    @Override
    public Applicant getApplicant(Integer id) {
        try {
            Applicant applicant = (Applicant) this.sessionFactory.getCurrentSession().get(Applicant.class, id);
//            applicant.getApplicantAttachmentList().size();
            Hibernate.initialize(applicant.getApplicantAttachmentList());
            //Any of the above two approach works for lazy initialization

            ApplicantBiometricInfo applicantBiometricInfo = applicant.getApplicantBiometricInfo();
            if (applicantBiometricInfo != null) {
                if (applicantBiometricInfo.getPhotoData() != null) {
                    applicantBiometricInfo.setBase64PhotoData(CommonUtility.getBase64String(applicantBiometricInfo.getPhotoData()));
                }
                if (applicantBiometricInfo.getSignatureData() != null) {
                    applicantBiometricInfo.setBase64SignatureData(CommonUtility.getBase64String(applicantBiometricInfo.getSignatureData()));
                }
            }

            return applicant;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public Integer getApplicantIdByNid(BigInteger nid) {
        try {
            String query = "select a.id from applicant a where a.nid = " + nid;
            Integer id = (Integer) sessionFactory.getCurrentSession().createSQLQuery(query).list().get(0);
            return id;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public void save(Applicant applicant) throws ExceptionWrapper {
        try {
            final Session currentSession = this.sessionFactory.getCurrentSession();
            currentSession.save(applicant);
            currentSession.flush();
        } catch (HibernateException e) {
            e.printStackTrace();
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new ExceptionWrapper(e.getMessage());
        }

    }

    @Override
    public void edit(Applicant applicant) {
        // remove attachments which need to remove
        this.sessionFactory.getCurrentSession().update(applicant);
        if (applicant.getAttachmentRemoveList() != null) {
            String querySt = "delete from ApplicantAttachment where id IN (:list)";
            Query query = this.sessionFactory.getCurrentSession().createQuery(querySt).setParameterList("list", applicant.getAttachmentRemoveList());
            query.executeUpdate();
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
    public List<Applicant> getApplicantList() {
        @SuppressWarnings("unchecked")
        String querySt = null;
        querySt = "from Applicant";
        try {
            List<Applicant> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            System.out.println("size = " + list.size());
            return list;
        } catch (Exception e) {
            System.out.println("ex1 = " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Object> getApplicantEligibilityList(List<ApplicationStatus> statusList, boolean isSearchByStatusList) {
        try {
            System.out.println("here");
            List<Object> result = new ArrayList<Object>();
            String querySt = "select o from SchemeAttributeValue o "
                    + "where "
                    + "o.schemeAttribute.nameInEnglish = 'Monthly Income'"
                    + " and "
                    + "(o.schemeAttribute.comparisonType = '2' and "
                    + "o.schemeAttributeValue<o.schemeAttribute.comparedValue) ";
            if (isSearchByStatusList) {
                querySt += "and o.applicant.applicationStatus in :statusList ";
            }

            querySt += "order by o.schemeAttributeValue asc";
//            countQuery = 
//String querySt = "select o.applicant from SchemeAttributeValue o  ";
            System.out.println("query " + querySt);
            List<SchemeAttributeValue> list = (List<SchemeAttributeValue>) sessionFactory.getCurrentSession().
                    createQuery(querySt).setParameterList("statusList", statusList).list();
            Integer count = 0;
            if (list != null) {
                count = list.size();
            }
            System.out.println("count is " + count);
            result.add(list);
            result.add(count);
            result.add(count);// have to alter this
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Object> getPrioritizedApplicantList(Map parameter, int offset, int numofRecords) {
        try {
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            Integer unionId = parameter.get("unionId") != null ? (Integer) parameter.get("unionId") : null;
            Integer bgmeaFactoryId = parameter.get("bgmeaFactoryId") != null ? (Integer) parameter.get("bgmeaFactoryId") : null;
            Integer bkmeaFactoryId = parameter.get("bkmeaFactoryId") != null ? (Integer) parameter.get("bkmeaFactoryId") : null;
            ApplicantType applicantType = parameter.get("applicantType") != null ? (ApplicantType) parameter.get("applicantType") : null;
            System.out.println("unionId = " + unionId);
            String mainQuerySt = "from ApplicantView o where 0=0 AND ";
            String countQuerySt = "select count(o.id) from ApplicantView o where 0=0 AND ";

            String querySt = "";
            String bgmeaQuerySt = "";
            String bkmeaQuerySt = "";
            String otherSt = "";
            String regularApplicantTypeSt = "";
            String bgmeaApplicantTypeSt = "";
            String bkmeaApplicantTypeSt = "";
            String conditionSt = "";

            querySt += " o.applicantType = " + applicantType.ordinal();

            if (bgmeaFactoryId != null && bgmeaFactoryId != 0) {
                querySt += " AND o.factoryId = " + bgmeaFactoryId;
            }

            if (bkmeaFactoryId != null && bkmeaFactoryId != 0) {
                querySt += " AND o.factoryId = " + bkmeaFactoryId;
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

            querySt += " AND o.applicationStatus =" + ApplicationStatus.PRIORITIZATION_PENDING.ordinal();
            querySt += " ORDER BY o.score DESC";
            System.out.println("querySt = " + mainQuerySt);
            Query mainQuery = sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt);
            Query countQuery = sessionFactory.getCurrentSession().createQuery(countQuerySt + querySt);
            List<ApplicantView> list = mainQuery.setFirstResult(offset).setMaxResults(numofRecords).list();
            long count = (Long) countQuery.list().get(0);
            List<Object> result = new ArrayList<Object>();
            result.add(list);
            result.add(count);
            result.add(count); // ????
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Object> getApplicantListForEditingBySearchParameter(Map parameter, int offset, int numofRecords) {
        try {
            Integer fiscalYearId = parameter.get("fiscalYearId") != null ? (Integer) parameter.get("fiscalYearId") : null;
            String nid = parameter.get("nid") != null ? (String) parameter.get("nid") : null;

            boolean isUnionUser = (boolean) parameter.get("isUnionUser");
            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            Integer unionId = parameter.get("unionId") != null ? (Integer) parameter.get("unionId") : null;
            Integer wardNo = parameter.get("wardNo") != null ? (Integer) parameter.get("wardNo") : null;
            Integer bgmeaFactoryId = parameter.get("bgmeaFactoryId") != null ? (Integer) parameter.get("bgmeaFactoryId") : null;
            Integer bkmeaFactoryId = parameter.get("bkmeaFactoryId") != null ? (Integer) parameter.get("bkmeaFactoryId") : null;
            ApplicantType applicantType = parameter.get("applicantType") != null ? (ApplicantType) parameter.get("applicantType") : null;
            Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
            Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;

            Integer applicationStatus = parameter.get("applicationStatus") != null ? (Integer) parameter.get("applicationStatus") : null;
            String mainQuerySt = "from ApplicantView o where 0=0 ";
            String countQuerySt = "select count(o.id) from ApplicantView o where 0=0 ";
            String querySt = "";
            if (fiscalYearId != null && fiscalYearId != 0) {
                querySt += " AND o.fiscalYearId = " + fiscalYearId;
            }
            if (isUnionUser) {
                //need to dicuss with kamal
//                querySt += " AND o.applicationStatus < 4";
            }
            if (nid != null && !nid.isEmpty()) {
                querySt += " AND ( o.nid like '%" + CommonUtility.getNumberInEnglish(nid) + "%' )";
            }
            if (applicantType != null) {
                querySt += " AND o.applicantType = " + applicantType.ordinal();
            }
            if (bgmeaFactoryId != null && bgmeaFactoryId != 0) {
                querySt += " and o.factoryId = " + bgmeaFactoryId;
            }
            if (bkmeaFactoryId != null && bkmeaFactoryId != 0) {
                querySt += " and o.factoryId = " + bkmeaFactoryId;
            }
            if (divisionId != null && divisionId != 0) {
                querySt += " and o.divisionId = " + divisionId;
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
            if (startDate != null) {
                querySt += " AND o.creationDate >= :startDate";
            }
            if (endDate != null) {
                querySt += " AND o.creationDate <= :endDate";
            }
            if (applicationStatus != null) {
                querySt += " AND o.applicationStatus =:applicationStatus";
            }
            querySt += " order by o.score desc";
            System.out.println("querySt = " + querySt);
            Query mainQuery = sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt);
            Query countQuery = sessionFactory.getCurrentSession().createQuery(countQuerySt + querySt);
            if (startDate != null) {
                mainQuery.setParameter("startDate", startDate.getTime());
                countQuery.setParameter("startDate", startDate.getTime());
            }

            if (endDate != null) {
                mainQuery.setParameter("endDate", endDate.getTime());
                countQuery.setParameter("endDate", endDate.getTime());
            }

            if (applicationStatus != null) {
                mainQuery.setParameter("applicationStatus", ApplicationStatus.getEnumFromValue(applicationStatus));
                countQuery.setParameter("applicationStatus", ApplicationStatus.getEnumFromValue(applicationStatus));
            }
            List<ApplicantView> list = mainQuery.setFirstResult(offset).setMaxResults(numofRecords).list();
            long count = (Long) countQuery.list().get(0);
            List<Object> result = new ArrayList<Object>();
            result.add(list);
            result.add(count);
            result.add(count); // ????
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Object> getApplicantListBySearchParameter(Map parameter, int offset, int numofRecords) {
        try {
//            Integer schemeId = !parameter.get("schemeId").equals("") ? (Integer) parameter.get("schemeId") : null;
            Integer fiscalYearId = parameter.get("fiscalYearId") != null ? (Integer) parameter.get("fiscalYearId") : null;
            ApplicationStatus applicationStatus = parameter.get("applicationStatus") != null ? (ApplicationStatus) parameter.get("applicationStatus") : null;
            List<ApplicationStatus> applicationStatusList = parameter.get("applicationStatusList") != null ? (List<ApplicationStatus>) parameter.get("applicationStatusList") : null;
            String nid = parameter.get("nid") != null ? (String) parameter.get("nid") : null;

            Integer divisionId = parameter.get("divisionId") != null ? (Integer) parameter.get("divisionId") : null;
            Integer districtId = parameter.get("districtId") != null ? (Integer) parameter.get("districtId") : null;
            Integer upazilaId = parameter.get("upazilaId") != null ? (Integer) parameter.get("upazilaId") : null;
            Integer unionId = parameter.get("unionId") != null ? (Integer) parameter.get("unionId") : null;
            Integer bgmeaFactoryId = parameter.get("bgmeaFactoryId") != null ? (Integer) parameter.get("bgmeaFactoryId") : null;
            Integer bkmeaFactoryId = parameter.get("bkmeaFactoryId") != null ? (Integer) parameter.get("bkmeaFactoryId") : null;
//            Integer regularUser = parameter.get("regularUser") != null ? (Integer) parameter.get("regularUser") : null;
//            Integer bgmeaUser = parameter.get("bgmeaUser") != null ? (Integer) parameter.get("bgmeaUser") : null;
//            Integer bkmeaUser = parameter.get("bkmeaUser") != null ? (Integer) parameter.get("bkmeaUser") : null;
            ApplicantType applicantType = parameter.get("applicantType") != null ? (ApplicantType) parameter.get("applicantType") : null;
            String searchPage = parameter.get("searchPage") != null ? (String) parameter.get("searchPage") : null;

            String mainQuerySt = "from ApplicantView o where 0=0 AND ";
            String countQuerySt = "select count(o.id) from ApplicantView o where 0=0 AND ";

            String querySt = "";
            String bgmeaQuerySt = "";
            String bkmeaQuerySt = "";
            String otherSt = "";
            String regularApplicantTypeSt = "";
            String bgmeaApplicantTypeSt = "";
            String bkmeaApplicantTypeSt = "";
            String conditionSt = "";

//            if (schemeId != null && schemeId != 0)
//            {
//                querySt += " AND o.schemeId = " + schemeId;
//            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                querySt += " AND o.fiscalYearId = " + fiscalYearId;
            }
            if (applicationStatus != null) {
                querySt += " AND o.applicationStatus = :applicationStatus";
            }
            if (applicationStatusList != null && applicationStatusList.size() > 0) {
                querySt += " AND o.applicationStatus IN :applicationStatusList ";
            }
            if (nid != null && !nid.isEmpty()) {
                querySt += " AND ( o.nid like '%" + CommonUtility.getNumberInEnglish(nid) + "%' )";
            }
            if (applicantType.equals(ApplicantType.UNION)) {
                regularApplicantTypeSt = " (o.applicantType = " + ApplicantType.UNION.ordinal();
            }
            if (applicantType.equals(ApplicantType.MUNICIPAL)) {
                regularApplicantTypeSt = " (o.applicantType = " + ApplicantType.MUNICIPAL.ordinal();
            }
            if (applicantType.equals(ApplicantType.CITYCORPORATION)) {
                regularApplicantTypeSt = " (o.applicantType = " + ApplicantType.CITYCORPORATION.ordinal();
            }
            if (applicantType.equals(ApplicantType.BGMEA)) {
                bgmeaApplicantTypeSt = " (o.applicantType = " + ApplicantType.BGMEA.ordinal();
            }
            if (applicantType.equals(ApplicantType.BKMEA)) {
                bkmeaApplicantTypeSt = " (o.applicantType = " + ApplicantType.BKMEA.ordinal();
            }
            if (bgmeaFactoryId != null && bgmeaFactoryId != 0) {
                bgmeaQuerySt = " and o.factoryId = " + bgmeaFactoryId;
                bgmeaApplicantTypeSt += bgmeaQuerySt;
            }
            if (applicantType.equals(ApplicantType.BGMEA)) {
                bgmeaApplicantTypeSt += ")";
            }
            if (bkmeaFactoryId != null && bkmeaFactoryId != 0) {
                bkmeaQuerySt = " and o.factoryId = " + bkmeaFactoryId;
                bkmeaApplicantTypeSt += bkmeaQuerySt;
            }
            if (applicantType.equals(ApplicantType.BKMEA)) {
                bkmeaApplicantTypeSt += ")";
            }
            if (divisionId != null && divisionId != 0) {
                otherSt += " and o.divisionId = " + divisionId;
            }
            if (districtId != null && districtId != 0) {
                otherSt += " AND o.districtId = " + districtId;
            }
            if (upazilaId != null && upazilaId != 0) {
                otherSt += " AND o.upazilaId = " + upazilaId;
            }
            if (unionId != null && unionId != 0) {
                otherSt += " AND o.unionId = " + unionId;
            }
            if (!"".equals(otherSt)) {
                regularApplicantTypeSt += otherSt;
            }
            if (applicantType.equals(ApplicantType.UNION)) {
                regularApplicantTypeSt += ")";
            }
            if (applicantType.equals(ApplicantType.MUNICIPAL)) {
                regularApplicantTypeSt += ")";
            }
            if (applicantType.equals(ApplicantType.CITYCORPORATION)) {
                regularApplicantTypeSt += ")";
            }
            if (!"".equals(regularApplicantTypeSt)) {
                conditionSt = regularApplicantTypeSt;
                if (!"".equals(bgmeaApplicantTypeSt)) {
                    conditionSt = conditionSt + " OR " + bgmeaApplicantTypeSt;
                }
                if (!"".equals(bkmeaApplicantTypeSt)) {
                    conditionSt = conditionSt + " OR " + bkmeaApplicantTypeSt;
                }
            } else if (!"".equals(bgmeaApplicantTypeSt)) {
                conditionSt += bgmeaApplicantTypeSt;
                if (!"".equals(bkmeaApplicantTypeSt)) {
                    conditionSt = conditionSt + " OR " + bkmeaApplicantTypeSt;
                }
            } else if (!"".equals(bkmeaApplicantTypeSt)) {
                conditionSt += bkmeaApplicantTypeSt;
            }
            querySt = "(" + conditionSt + ")" + querySt;
//            if(searchPage.equalsIgnoreCase("applicantList"))
//            {
//                querySt += " and o.applicationStatus < "+ ApplicationStatus.VERIFICATION_PENDING.ordinal(); // before sending to upazila
//            }
//            else
//            {
//                querySt += " and o.applicationStatus < "+ ApplicationStatus.SELECTED_AT_FINAL_APPROVAL;
//            }
            querySt += " order by o.score desc";
            Query mainQuery = sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt);
            Query countQuery = sessionFactory.getCurrentSession().createQuery(countQuerySt + querySt);
            if (applicationStatus != null) {
                mainQuery.setParameter("applicationStatus", applicationStatus);
                countQuery.setParameter("applicationStatus", applicationStatus);
            }
            if (applicationStatusList != null && applicationStatusList.size() > 0) {
                mainQuery.setParameterList("applicationStatusList", applicationStatusList);
                countQuery.setParameterList("applicationStatusList", applicationStatusList);
            }            
            List<ApplicantView> list = mainQuery.setFirstResult(offset).setMaxResults(numofRecords).list();            
            long count = (Long) countQuery.list().get(0);
            List<Object> result = new ArrayList<Object>();
            result.add(list);
            result.add(count);
            result.add(count); // ????
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean checkUniqueNid(BigInteger nid, Integer appId) {
        // return true if unique
        try {
            String querySt;
            if (appId == null) {
                querySt = String.format("from Applicant o where o.nid ='%s'", nid);
            } else {
                querySt = String.format("from Applicant o where o.id!=%s and o.nid ='%s'", appId, nid);
            }
            List<PaymentCycle> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
            return list.isEmpty();
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkUniqueAccountNumber(String accountNumber) {
        // return true if unique
        try {
            int count_add = 0;

            count_add = ((Number) sessionFactory.getCurrentSession().createQuery("SELECT count(1) FROM Beneficiary b WHERE b.accountNo =:accountNumber")
                    .setParameter("accountNumber", accountNumber).uniqueResult()).intValue();

            return count_add == 0 ? true : false;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Search both application table and beneficiary table
     *
     * @param accountNumber
     * @return
     */
    @Override
    public boolean checkUniqueAccountNumberAtApplicationSave(String accountNumber, Integer appId) {
        // return true if unique
        try {

            if (appId == null) {
                int countAtApplicant = ((Number) sessionFactory.getCurrentSession().createSQLQuery("SELECT COUNT(1) FROM applicant a WHERE a.account_no ='" + accountNumber + "'").uniqueResult()).intValue();
                int countAtBeneficiary = 0;
                if (countAtApplicant == 0) {
                    countAtBeneficiary = ((Number) sessionFactory.getCurrentSession().createSQLQuery("SELECT COUNT(1) FROM beneficiary a WHERE a.account_no ='" + accountNumber + "'").uniqueResult()).intValue();
                }
                if (countAtApplicant > 0 || countAtBeneficiary > 0) {
                    return false;
                }

                return true;
            } else {

                int countAtApplicant = ((Number) sessionFactory.getCurrentSession().createSQLQuery("SELECT COUNT(1) FROM applicant a WHERE a.id !=" + appId + " and a.account_no = '" + accountNumber + "'").uniqueResult()).intValue();
                int countAtBeneficiary = 0;
                if (countAtApplicant == 0) {
                    Applicant applicant = (Applicant) sessionFactory.getCurrentSession().createQuery("SELECT a FROM Applicant as a WHERE a.Id=:applicantId").setParameter("applicantId", appId).uniqueResult();
                    countAtBeneficiary = ((Number) sessionFactory.getCurrentSession().createSQLQuery("SELECT COUNT(1) FROM beneficiary a WHERE a.nid !='" + applicant.getNid() + "' and a.account_no ='" + accountNumber + "'").uniqueResult()).intValue();

                }
                if (countAtApplicant > 0 || countAtBeneficiary > 0) {
                    return false;
                }

                return true;
            }
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkUniqueAccountNumberForBeneficiary(String accountNumber, int beneficiaryId) {
        // return true if unique
        try {
            int count_add = 0;

            count_add = ((Number) sessionFactory.getCurrentSession().createQuery("SELECT count(1) FROM Beneficiary b WHERE b.accountNo =:accountNumber AND b.Id !=:benId")
                    .setParameter("accountNumber", accountNumber).setParameter("benId", beneficiaryId).uniqueResult()).intValue();

            return count_add == 0 ? true : false;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<DoubleDippingReportData> getDoubleDippingFoundApplicants(Map parameter) {
        Integer schemeId = (Integer) parameter.get("schemeId");
        Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
        Integer divisionId = (Integer) parameter.get("divisionId");
        Integer districtId = (Integer) parameter.get("districtId");
        Integer upazilaId = (Integer) parameter.get("upazilaId");
        Integer unionId = (Integer) parameter.get("unionId");

        ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
        Integer bgmeaId = (Integer) parameter.get("bgmeaId");
        Integer bkmeaId = (Integer) parameter.get("bkmeaId");

        String locale = parameter.get("locale") != null ? (parameter.get("locale")).toString() : null;

        String query;
        if ("bn".equals(locale)) {
            query = "select new com.wfp.lmmis.report.data.DoubleDippingReportData(a.fullNameInBangla, a.nid, a.fatherName, a.motherName, a.spouseName, a.dateOfBirth,"
                    + "a.mobileNo, a.permanentDivision.nameInBangla, a.permanentDistrict.nameInBangla, a.permanentUpazila.nameInBangla, a.permanentUnion.nameInBangla, d.ddMatchedMinistryNameBn, d.ddMatchedSchemeBn) "
                    + "from Applicant a join a.doubleDippingMatchedStatus d where 0 = 0 ";
        } else {
            query = "select new com.wfp.lmmis.report.data.DoubleDippingReportData(a.fullNameInEnglish, a.nid, a.fatherName, a.motherName, a.spouseName, a.dateOfBirth,"
                    + "a.mobileNo, a.permanentDivision.nameInEnglish, a.permanentDistrict.nameInEnglish, a.permanentUpazila.nameInEnglish, a.permanentUnion.nameInEnglish, d.ddMatchedMInistryNameEn, d.ddMatchedSchemeEn) "
                    + "from Applicant a join a.doubleDippingMatchedStatus d where 0 = 0 ";
        }

        if (schemeId != null && schemeId != 0) {
            query += " AND a.scheme.id = " + schemeId;
        }
        if (fiscalYearId != null && fiscalYearId != 0) {
            query += " AND a.fiscalYear.id = " + fiscalYearId;
        }
        query += " AND a.applicantType = " + applicantType.ordinal();
        switch (applicantType) {
            case REGULAR:
                if (divisionId != null) {
                    query += " and a.presentDivision.id=" + divisionId;
                }
                if (districtId != null) {
                    query += " and a.presentDistrict.id=" + districtId;
                }
                if (upazilaId != null) {
                    query += " and a.presentUpazila.id=" + upazilaId;
                }
                if (unionId != null) {
                    query += " and a.presentUnion.id=" + unionId;
                }
                break;
            case BGMEA:
                if (bgmeaId != null) {
                    query += " AND a.factory.id=" + bgmeaId;
                }
                break;
            case BKMEA:
                if (bkmeaId != null) {
                    query += " AND a.factory.id=" + bkmeaId;
                }
                break;
            default:
                break;
        }

        query += " and a.applicationStatus=" + ApplicationStatus.REJECTED_SPBMU.ordinal();
        query += " order by a.presentDivision.nameInEnglish, a.presentDistrict.nameInEnglish, a.presentUpazila.nameInEnglish, a.presentUnion.nameInEnglish";

        System.out.println("query = " + query);
        List<DoubleDippingReportData> list = sessionFactory.getCurrentSession().createQuery(query).list();
        for (DoubleDippingReportData doubleDippingReportData : list) {
            if ("bn".equals(locale)) {
                doubleDippingReportData.setNationalID(CommonUtility.getNumberInBangla(doubleDippingReportData.getNationalID()));
                if (doubleDippingReportData.getMobileNo() != null) {
                    doubleDippingReportData.setMobileNo(CommonUtility.getNumberInBangla(doubleDippingReportData.getMobileNo()));
                }
            }
        }
        return list;
    }

    @Override
    public List<ApplicantReportData> getApplicantReportData(Map parameter) {
        try {
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            Integer wardNo = (Integer) parameter.get("ward");
            System.out.println("unionId = " + unionId);
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;

            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            ApplicantStatus applicantStatus = (ApplicantStatus) parameter.get("applicantStatus");
            Integer bgmeaId = (Integer) parameter.get("bgmeaId");
            Integer bkmeaId = (Integer) parameter.get("bkmeaId");
            Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
            Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;
            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.ApplicantReportData(a.fullNameInBangla, a.nid, a.fatherName, a.motherName, a.spouseName, a.dateOfBirth,"
                        + "a.mobileNo, a.presentDivision.nameInBangla, a.presentDistrict.nameInBangla, a.presentUpazila.nameInBangla, a.presentUnion.nameInBangla,a.presentWardNo, a.presentAddressLine1, "
                        + "COALESCE(bk.nameInBangla, null), br.nameInBangla, pob.nameInBangla, COALESCE(mbp.nameInBangla, null), a.accountNo, a.applicationStatus, f.nameInBangla, a.creationDate) "
                        + "from Applicant a LEFT JOIN a.bank bk LEFT JOIN a.branch br LEFT JOIN a.postOfficeBranch pob LEFT JOIN a.mobileBankingProvider mbp LEFT JOIN a.factory f where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.ApplicantReportData(a.fullNameInEnglish, a.nid, a.fatherName, a.motherName, a.spouseName, a.dateOfBirth,"
                        + "a.mobileNo, a.presentDivision.nameInEnglish, a.presentDistrict.nameInEnglish, a.presentUpazila.nameInEnglish, a.presentUnion.nameInEnglish,a.presentWardNo, a.presentAddressLine1, "
                        + "COALESCE(bk.nameInEnglish, null), br.nameInEnglish, pob.nameInEnglish, COALESCE(mbp.nameInEnglish, null), a.accountNo, a.applicationStatus, f.nameInEnglish, a.creationDate) "
                        + "from Applicant a LEFT JOIN a.bank bk LEFT JOIN a.branch br LEFT JOIN a.postOfficeBranch pob LEFT JOIN a.mobileBankingProvider mbp LEFT JOIN a.factory f where 0 = 0 ";
            }

            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND a.fiscalYear.id = " + fiscalYearId;
            }
            query += " AND a.applicantType = " + applicantType.ordinal();
            if (applicantStatus != null && applicantStatus == ApplicantStatus.NEW) {
                query += " AND a.applicationStatus <=4";
            }
            if (startDate != null) {
                query += " AND a.creationDate >= :startDate";
            }
            if (endDate != null) {
                query += " AND a.creationDate <= :endDate";
            }
            switch (applicantType) {
                case UNION:
                case MUNICIPAL:
                case CITYCORPORATION:
                    if (divisionId != null) {
                        query += " and a.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        query += " and a.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        query += " and a.presentUpazila.id=" + upazilaId;
                    }
                    if (unionId != null) {
                        query += " and a.presentUnion.id=" + unionId;
                    }
                    if (wardNo != null) {
                        query += " and a.presentWardNo=" + wardNo;
                    }
                    break;
                case BGMEA:
                    if (bgmeaId != null) {
                        query += " AND a.factory.id=" + bgmeaId;
                    }
                    break;
                case BKMEA:
                    if (bkmeaId != null) {
                        query += " AND a.factory.id=" + bkmeaId;
                    }
                    break;
                default:
                    break;
            }
            query += " order by a.presentDivision.nameInEnglish, a.presentDistrict.nameInEnglish, a.presentUpazila.nameInEnglish, a.presentUnion.nameInEnglish,a.presentWardNo";
//            query += " , a.fullNameInEnglish";
            query += " , a.creationDate";
            System.out.println("query = " + query);
            Query queryObject = sessionFactory.getCurrentSession().createQuery(query);
            if (startDate != null) {
                queryObject.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                queryObject.setParameter("endDate", endDate);
            }
            List<ApplicantReportData> list = queryObject.list();
            if (applicantType == ApplicantType.UNION || applicantType == ApplicantType.MUNICIPAL || applicantType == ApplicantType.CITYCORPORATION) {
                for (ApplicantReportData applicantReportData : list) {
                    if ("bn".equals(locale)) {
                        applicantReportData.setNationalID(CommonUtility.getNumberInBangla(applicantReportData.getNationalID()));
                        applicantReportData.setMobileNo(CommonUtility.getNumberInBangla(applicantReportData.getMobileNo()));
                        if (divisionId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                applicantReportData.setUnion("বিভাগ : " + applicantReportData.getDivision() + ",   জেলা : " + applicantReportData.getDistrict() + ",   উপজেলা : " + applicantReportData.getUpazila() + ",   ইউনিয়ন : " + applicantReportData.getUnion());
                            } else {
                                applicantReportData.setUnion("বিভাগ : " + applicantReportData.getDivision() + ",   জেলা : " + applicantReportData.getDistrict() + ",   জেলা/উপজেলা : " + applicantReportData.getUpazila() + ",   সিটি কর্পোরেশন/পৌরসভা : " + applicantReportData.getUnion() + ", ওয়ার্ড " + CommonUtility.getNumberInBangla(applicantReportData.getWardNo()));
                            }
                        } else if (districtId == null) {
                            //beneficiaryReportData.setDistrict("জেলা : " + beneficiaryReportData.getDistrict()); // district bangla text appended
                            if (applicantType == ApplicantType.UNION) {
                                applicantReportData.setUnion("জেলা : " + applicantReportData.getDistrict() + ",   উপজেলা : " + applicantReportData.getUpazila() + ",   ইউনিয়ন : " + applicantReportData.getUnion());

                            } else {
                                applicantReportData.setUnion("জেলা : " + applicantReportData.getDistrict() + ",   জেলা/উপজেলা : " + applicantReportData.getUpazila() + ",   সিটি কর্পোরেশন/পৌরসভা : " + applicantReportData.getUnion() + ", ওয়ার্ড " + CommonUtility.getNumberInBangla(applicantReportData.getWardNo()));
                            }
                        } else if (upazilaId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                applicantReportData.setUnion("উপজেলা : " + applicantReportData.getUpazila() + ",   ইউনিয়ন : " + applicantReportData.getUnion());
                            } else {
                                applicantReportData.setUnion("জেলা/উপজেলা : " + applicantReportData.getUpazila() + ",   সিটি কর্পোরেশন/পৌরসভা : " + applicantReportData.getUnion() + ", ওয়ার্ড " + CommonUtility.getNumberInBangla(applicantReportData.getWardNo()));
                            }
                        } else if (unionId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                applicantReportData.setUnion("ইউনিয়ন : " + applicantReportData.getUnion()); // uinon bangla text appended
                            } else {
                                applicantReportData.setUnion("???? ?????????/?????? : " + applicantReportData.getUnion() + ", ওয়ার্ড " + CommonUtility.getNumberInBangla(applicantReportData.getWardNo())); // uinon bangla text appended
                            }
                        }

                    } else {
                        if (divisionId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                applicantReportData.setUnion("Division : " + applicantReportData.getDivision() + ",   District : " + applicantReportData.getDistrict() + ",   Upazila : " + applicantReportData.getUpazila() + ",   Union : " + applicantReportData.getUnion());
                            } else {
                                applicantReportData.setUnion("Division : " + applicantReportData.getDivision() + ",   District : " + applicantReportData.getDistrict() + ",   District/Upazila : " + applicantReportData.getUpazila() + ",   City Corporation/Municipal : " + applicantReportData.getUnion() + ", WardNo:" + applicantReportData.getWardNo());

                            }
                        } else if (districtId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                applicantReportData.setUnion("District : " + applicantReportData.getDistrict() + ",   Upazila : " + applicantReportData.getUpazila() + ",   Union : " + applicantReportData.getUnion());
                            } else {
                                applicantReportData.setUnion("District : " + applicantReportData.getDistrict() + ",   District/Upazila : " + applicantReportData.getUpazila() + ",   City Corporation/Municipal : " + applicantReportData.getUnion() + ", WardNo:" + applicantReportData.getWardNo());

                            }
                        } else if (upazilaId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                applicantReportData.setUnion("Upazila : " + applicantReportData.getUpazila() + ",   Union : " + applicantReportData.getUnion());
                            } else {
                                applicantReportData.setUnion("District/Upazila : " + applicantReportData.getUpazila() + ",   City Corporation/Municipal : " + applicantReportData.getUnion() + ", WardNo:" + applicantReportData.getWardNo());
                            }
                        } else if (unionId == null) {
                            if (applicantType == ApplicantType.UNION) {
                                applicantReportData.setUnion("Union : " + applicantReportData.getUnion()); // uinon bangla text appended
                            } else {
                                applicantReportData.setUnion("City Corporation/Municipal : " + applicantReportData.getUnion() + ", WardNo:" + applicantReportData.getWardNo()); // uinon bangla text appended
                            }
                        }
                    }
                    if (applicantReportData.getBankName() != null) {
                        applicantReportData.setPaymentProviderName(applicantReportData.getBankName());
                    }
                    if (applicantReportData.getMobileBankingProviderName() != null) {
                        applicantReportData.setPaymentProviderName(applicantReportData.getMobileBankingProviderName());
                    }
                    if (applicantReportData.getpOBranch() != null) {
                        String postOffice = "bn".equals(locale) ? "পোস্ট অফিস" : "Post Office";
                        applicantReportData.setPaymentProviderName(postOffice);
                        applicantReportData.setBranchName(applicantReportData.getpOBranch());
                    }
                    int index = applicantReportData.getCreationDate().get(Calendar.MONTH);
                    applicantReportData.setMonth(getMonthForInt(index, locale));
                }
            } else {
                for (ApplicantReportData applicantReportData : list) {
                    if ("bn".equals(locale)) {
                        applicantReportData.setNationalID(CommonUtility.getNumberInBangla(applicantReportData.getNationalID()));
                        applicantReportData.setMobileNo(CommonUtility.getNumberInBangla(applicantReportData.getMobileNo()));
                        applicantReportData.setUnion("প্রতিষ্ঠান : " + applicantReportData.getFactory());
                    } else {
                        applicantReportData.setUnion("Factory : " + applicantReportData.getFactory());
                    }
                    if (applicantReportData.getBankName() != null) {
                        applicantReportData.setPaymentProviderName(applicantReportData.getBankName());
                    }
                    if (applicantReportData.getMobileBankingProviderName() != null) {
                        applicantReportData.setPaymentProviderName(applicantReportData.getMobileBankingProviderName());
                    }
                    if (applicantReportData.getpOBranch() != null) {
                        String postOffice = "bn".equals(locale) ? "পোস্ট অফিস" : "Post Office";
                        applicantReportData.setPaymentProviderName(postOffice);
                        applicantReportData.setBranchName(applicantReportData.getpOBranch());
                    }
                    int index = applicantReportData.getCreationDate().get(Calendar.MONTH);
                    applicantReportData.setMonth(getMonthForInt(index, locale));
                }
            }

            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    String getMonthForInt(int num, String locale) {
        String month = "wrong";
        String[] months;
        if (locale.equals("en")) {
            months = new String[]{
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            };
        } else {
            months = new String[]{
                "জানুয়ারী", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "অগাস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"

            };
        }
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    @Override
    public List<ApplicantReportDataByLocation> getApplicantSummaryReportData(Map parameter) {
        try {
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            ApplicantStatus applicantStatus = (ApplicantStatus) parameter.get("applicantStatus");
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");

            Integer bgmeaId = (Integer) parameter.get("bgmeaId");
            Integer bkmeaId = (Integer) parameter.get("bkmeaId");

            Calendar startDate = parameter.get("startDate") != null ? (Calendar) parameter.get("startDate") : null;
            Calendar endDate = parameter.get("endDate") != null ? (Calendar) parameter.get("endDate") : null;

            String query;
            System.out.println("locale = " + locale);
            String groupByQuery = "";
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.ApplicantReportDataByLocation(a.presentDivision.nameInBangla,"
                        + " a.presentDistrict.nameInBangla, a.presentUpazila.nameInBangla, a.presentUnion.nameInBangla, f.nameInBangla, count(*))"
                        + " from Applicant a left join a.factory f where 0 = 0 ";
                groupByQuery = " group by a.presentDivision.nameInBangla,a.presentDistrict.nameInBangla,a.presentUpazila.nameInBangla, a.presentUnion.nameInBangla, f.nameInBangla";
            } else {
                query = "select new com.wfp.lmmis.report.data.ApplicantReportDataByLocation(a.presentDivision.nameInEnglish,"
                        + " a.presentDistrict.nameInEnglish, a.presentUpazila.nameInEnglish, a.presentUnion.nameInEnglish, f.nameInEnglish, count(*))"
                        + " from Applicant a left join a.factory f where 0 = 0 ";
                groupByQuery = " group by a.presentDivision.nameInEnglish,a.presentDistrict.nameInEnglish,a.presentUpazila.nameInEnglish, a.presentUnion.nameInEnglish, f.nameInEnglish";
            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND a.fiscalYear.id = " + fiscalYearId;
            }
            if (applicantStatus != null && applicantStatus == ApplicantStatus.NEW) {
                query += " AND a.applicationStatus <=4";
            }
            query += " AND a.applicantType = " + applicantType.ordinal();
            if (startDate != null) {
                query += " AND a.creationDate >= :startDate";
            }
            if (endDate != null) {
                query += " AND a.creationDate <= :endDate";
            }
            switch (applicantType) {
                case UNION:
                case MUNICIPAL:
                case CITYCORPORATION:
                    if (divisionId != null) {
                        query += " and a.presentDivision.id=" + divisionId;
                    }
                    if (districtId != null) {
                        query += " and a.presentDistrict.id=" + districtId;
                    }
                    if (upazilaId != null) {
                        query += " and a.presentUpazila.id=" + upazilaId;
                    }
                    if (unionId != null) {
                        query += " and a.presentUnion.id=" + unionId;
                    }
                    if (!groupByQuery.equals("")) {
                        query += groupByQuery;
                    }
//                    query += " GROUP BY ";
//                    if (divisionId != null && divisionId != 0) {
//                        query += " a.presentDistrict.id";
//                        if (districtId != null && districtId != 0) {
//                            query += ", a.presentUpazila.id";
//                            if (upazilaId != null && upazilaId != 0) {
//                                query += ", a.presentUnion.id";
//                            } else {
//                                query += ", a.presentUpazila.id";
//                            }
//                        } else {
//                            query += ", a.presentDistrict.id";
//                        }
//                    } else {
//                        query += " a.presentDivision.id";
//                    }
//
//                    if (unionId != null && unionId != 0) {
//                        query += ", a.presentUnion.id";
//                    }
//                    if (fiscalYearId != null && fiscalYearId != 0) {
//                        query += " ,a.fiscalYear.id ";
//                    }
                    break;
                case BGMEA:
                    if (bgmeaId == null) {
                        query += " GROUP BY a.factory.id";
                    }
                    break;
                case BKMEA:
                    if (bkmeaId == null) {
                        query += " GROUP BY a.factory.id";
                    }
                    break;
                default:
                    break;
            }
            System.out.println("query = " + query);
            Query queryObject = sessionFactory.getCurrentSession().createQuery(query);
            if (startDate != null) {
                queryObject.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                queryObject.setParameter("endDate", endDate);
            }
            System.out.println("query -------->" + query);
            List<ApplicantReportDataByLocation> list = queryObject.list();
            Integer total = 0;
            for (ApplicantReportDataByLocation applicantReportDataByLocation : list) {
                System.out.println("applicantReportDataByLocation = " + applicantReportDataByLocation.getDivision());
                total += Integer.parseInt(applicantReportDataByLocation.getApplicantTotal());
                if ("bn".equals(locale)) {
                    applicantReportDataByLocation.setApplicantTotal(CommonUtility.getNumberInBangla(applicantReportDataByLocation.getApplicantTotal()));
                    applicantReportDataByLocation.setUnionTotal(CommonUtility.getNumberInBangla(applicantReportDataByLocation.getUnionTotal()));
                    applicantReportDataByLocation.setGrandTotal(CommonUtility.getNumberInBangla(total.toString()));
                } else {
                    applicantReportDataByLocation.setGrandTotal(total.toString());
                }
            }
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ApplicantReportData> getBeneficiaryReportDataWithLMMISExist(Map parameter) {
        List<ApplicantReportData> beneficiaryReportDatas = new ArrayList<>();
        parameter.put("isLmMISExist", 1);
        String queryBuild = queryBuildForLMMISExit(parameter);
        String query = "SELECT applicant.full_name_in_bangla benNameBn, \n"
                + "applicant.full_name_in_english benNameEn, \n"
                + "applicant.nid nationalID, \n"
                + "applicant.father_name fatherName,\n"
                + "applicant.mother_name motherName,\n"
                + "COALESCE(applicant.mobile_number,'') mobileNo,\n"
                + "applicant.spouse_name spouseName,\n"
                + "applicant.permanent_ward_no wardNo,\n"
                + "division.name_in_bangla division,\n"
                + "district.name_in_bangla district,\n"
                + "upazila.name_in_bangla upazila,\n"
                + "CONCAT('জেলা :',district.name_in_bangla,',   উপজেলা :',upazila.name_in_bangla,',   ইউনিয়ন :',unions.name_in_bangla ) `union`,\n"
                + "if(applicant.is_lm_mis_exist=1,\"দরিদ্র মা'র জন্য মাতৃত্বকাল ভাতা\",'ল্যাকটেটিং মাদার ভাতা') lmMisStatus,\n"
                + "village.name_in_bangla village,\n"
                + "applicant.conception_duration conceptionDuration,\n"
                + "CASE 	WHEN  applicant.conception_term=0 THEN 'প্রথম গর্ভধারণ' ELSE 'দ্বিতীয় গর্ভধারণ'END conceptionTerm,\n"
                //+ "CONCAT('জেলা :',oldDis.name_in_bangla,',   উপজেলা :',oldUpa.name_in_bangla,',   ইউনিয়ন :',oldUnio.name_in_bangla ) `oldGeo`,\n"
                + "COALESCE(oldDiv.name_in_bangla,'') oldDivision,\n"
                + "COALESCE(oldDis.name_in_bangla,'') oldDistrict,\n"
                + "COALESCE(oldUpa.name_in_bangla,'') oldUpazila,\n"
                + "COALESCE(oldUnio.name_in_bangla,'')  oldUnion,\n"
                + "COALESCE(applicant.old_mis_fiscal_year_name,'') oldFiscalYearName,\n"
                + "COALESCE(applicant.old_mis_scheme_name,'') oldSchemeName,\n"
                + "COALESCE(applicant.old_conception_duration,'') oldConceptionDuration,\n"
                + "CASE 	WHEN  applicant.old_conception_term=0 THEN 'প্রথম গর্ভধারণ' ELSE 'দ্বিতীয় গর্ভধারণ'END oldConceptionTerm\n"
                + "FROM applicant applicant\n"
                + "Left JOIN division ON division.id = applicant.permanent_division_id\n"
                + "Left JOIN district ON district.id = applicant.permanent_district_id\n"
                + "Left JOIN upazila ON upazila.id = applicant.permanent_upazila_id\n"
                + "Left JOIN unions ON unions.id = applicant.permanent_union_id\n"
                + "Left JOIN village ON village.id = applicant.permanent_village_id \n"
                + "LEFT JOIN division oldDiv ON oldDiv.id = applicant.old_mis_division\n"
                + "LEFT JOIN district oldDis ON oldDis.id = applicant.old_mis_district\n"
                + "LEFT JOIN upazila oldUpa ON oldUpa.id = applicant.old_mis_upazila\n"
                + "LEFT JOIN unions oldUnio ON oldUnio.id = applicant.old_mis_union";
        query = query + queryBuild + "\n"
                + "order by division.name_in_english, \n"
                + "district.name_in_english, \n"
                + "upazila.name_in_english,\n"
                + "unions.name_in_english, applicant.present_ward_no,\n"
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
                .setResultTransformer(Transformers.aliasToBean(ApplicantReportData.class)).list();

        beneficiaryReportDatas.forEach(a -> {
            if (a.getMobileNo().length() == 10) {
                a.setMobileNo("0" + a.getMobileNo());

            }
            a.setMobileNo(CommonUtility.getNumberInBangla(a.getMobileNo()));
            a.setNationalID(CommonUtility.getNumberInBangla(a.getNationalID()));
            //String oldGeo = "জেলা :" + a.getOldDistrict() + ",উপজেলা :" + a.getOldUpazila() + ",   ইউনিয়ন :'" + a.getOldUnion();
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
        ApplicantStatus applicantStatus = parameter.get("applicantStatus") != null ? (ApplicantStatus) parameter.get("applicantStatus") : null;
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
        //    querList.add("applicant.`status` = 0");
        if (isLmMISExist != null && isLmMISExist != 0) {
            querList.add("applicant.is_lm_mis_exist = " + isLmMISExist);

        }
        if (fiscalYearId != null && fiscalYearId != 0) {
            querList.add("applicant.fiscal_year_id = " + fiscalYearId);

        }

        if (divisionId != null && divisionId != 0) {
            querList.add("applicant.permanent_division_id = " + divisionId);

        }
        if (districtId != null && districtId != 0) {
            querList.add("applicant.permanent_district_id = " + districtId);
        }
        if (upazilaId != null && upazilaId != 0) {
            querList.add("applicant.permanent_upazila_id = " + upazilaId);
        }
        if (unionId != null && unionId != 0) {
            querList.add("applicant.permanent_union_id = " + unionId);

        }
        if (wardNo != null && wardNo != 0) {
            querList.add("applicant.permanent_ward_no = " + wardNo);
        }

        if (startDate != null) {
            querList.add("applicant.creation_date > '" + startDateSt + "' ");
        }
        if (endDate != null) {
            querList.add("applicant.creation_date <= '" + endDateSt + "' ");
        }
        if (applicantType != null) {
            querList.add("applicant.applicant_type = " + applicantType.ordinal());

        }

        String query = String.join(" and ", querList);
        if (querList.size() > 0) {
            query = " where " + query;
        }
        return query;
    }

    @Override
    public List<ApplicantReportDataByLocation> getApplicantGroupReportData(Map parameter) {
        try {
            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");

            String query = "select new com.wfp.lmmis.report.data.ApplicantReportDataByLocation(a.permanentDivision.nameInEnglish,"
                    + " a.permanentDistrict.nameInEnglish, a.permanentUpazila.nameInEnglish, count(distinct a.permanentUnion.id), count(a.id))"
                    + " from Applicant a where 0 = 0 ";
            if (schemeId != null && schemeId != 0) {
                query += " AND a.scheme.id = " + schemeId;
            }
            if (fiscalYearId != null && fiscalYearId != 0) {
                query += " AND a.fiscalYear.id = " + fiscalYearId;
            }
            if (divisionId != null) {
                query += " and a.permanentDivision.id=" + divisionId;
            }
            if (districtId != null) {
                query += " and a.permanentDistrict.id=" + districtId;
            }
            if (upazilaId != null) {
                query += " and a.permanentUpazila.id=" + upazilaId;
            }
            if (unionId != null) {
                query += " and a.permanentUnion.id=" + unionId;
            }
            query += " group by a.permanentUnion.id order by a.permanentDistrict.id";
            System.out.println("query = " + query);
            List<ApplicantReportDataByLocation> list = sessionFactory.getCurrentSession().createQuery(query).list();
            return list;
        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ApplicantReportData> getApplicantReportDataByIds(Map parameter) {
        try {
            System.out.println("start of getApplicantReportData ");
            String selectedApplicantList = (String) parameter.get("selectedApplicantList");
//            List<Integer> idList = new ArrayList<Integer>();
//            for (String s : selectedApplicantList.split(","))
//            {
//                idList.add(Integer.parseInt(s));
//            }
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;

            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.ApplicantReportData(a.Id, a.fullNameInBangla, a.nid, a.fatherName, a.motherName, a.spouseName, a.dateOfBirth,"
                        + "a.mobileNo, a.permanentDivision.nameInBangla, a.permanentDistrict.nameInBangla, a.permanentUpazila.nameInBangla, a.permanentUnion.nameInBangla, concat(a.presentAddressLine1,' ',a.presentAddressLine2)) "
                        + "from Applicant a where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.ApplicantReportData(a.Id, a.fullNameInEnglish, a.nid, a.fatherName, a.motherName, a.spouseName, a.dateOfBirth,"
                        + "a.mobileNo, a.permanentDivision.nameInEnglish, a.permanentDistrict.nameInEnglish, a.permanentUpazila.nameInEnglish, a.permanentUnion.nameInEnglish, concat(a.presentAddressLine1,' ',a.presentAddressLine2)) "
                        + "from Applicant a where 0 = 0 ";
            }

//            query += " and a.Id in :idList";
            query += " and a.Id in (" + selectedApplicantList + ")";
            //  System.out.println("idlist size "+ idList.size());
//            query += " and a.Id = 1";

            query += " order by a.permanentDivision.nameInEnglish, a.permanentDistrict.nameInEnglish, a.permanentUpazila.nameInEnglish, a.permanentUnion.nameInEnglish";
            System.out.println("query = " + query);
//            List<ApplicantReportData> list = sessionFactory.getCurrentSession().createQuery(query).setParameter("idList", selectedApplicantList).list();
            List<ApplicantReportData> list = sessionFactory.getCurrentSession().createQuery(query).list();
            for (ApplicantReportData applicantReportData : list) {
                if ("bn".equals(locale)) {
                    applicantReportData.setNationalID(CommonUtility.getNumberInBangla(applicantReportData.getNationalID()));
                    if (applicantReportData.getMobileNo() != null) {
                        applicantReportData.setMobileNo(CommonUtility.getNumberInBangla(applicantReportData.getMobileNo()));
                    }
                }
            }
            System.out.println("query = " + list.size());
            return list;

        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ApplicantReportData> getApplicantReportDataForPrioritizationPrint(Map parameter) {
        try {
            Integer unionId = (Integer) parameter.get("unionId");
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            Integer factoryId = (Integer) parameter.get("factoryId");
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;

            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.ApplicantReportData(a.Id, a.fullNameInBangla, a.nid, a.fatherName, a.motherName, a.spouseName, a.dateOfBirth,"
                        + "a.mobileNo, a.permanentDivision.nameInBangla, a.permanentDistrict.nameInBangla, a.permanentUpazila.nameInBangla, a.permanentUnion.nameInBangla, a.presentAddressLine1, a.presentAddressLine2, a.score, a.applicantType) "
                        + "from Applicant a where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.ApplicantReportData(a.Id, a.fullNameInEnglish, a.nid, a.fatherName, a.motherName, a.spouseName, a.dateOfBirth,"
                        + "a.mobileNo, a.permanentDivision.nameInEnglish, a.permanentDistrict.nameInEnglish, a.permanentUpazila.nameInEnglish, a.permanentUnion.nameInEnglish, a.presentAddressLine1, a.presentAddressLine2, a.score, a.applicantType) "
                        + "from Applicant a where 0 = 0 ";
            }

            query += " and ";
            switch (applicantType) {
                case UNION:
                    query += "a.presentUnion.id=" + unionId;
                    break;
                case MUNICIPAL:
                    query += "a.presentUnion.id=" + unionId;
                    break;
                case CITYCORPORATION:
                    query += "a.presentUnion.id=" + unionId;
                    break;
                case BGMEA:
                    query += "a.factory.id=" + factoryId;
                    break;
                case BKMEA:
                    query += "a.factory.id=" + factoryId;
                    break;
                default:
                    break;
            }

            query += " and a.applicantType = " + applicantType.ordinal();
            query += " and a.applicationStatus = " + ApplicationStatus.PRIORITIZATION_PENDING.ordinal();
            query += " order by a.score desc";
            System.out.println("query = " + query);
            List<ApplicantReportData> list = sessionFactory.getCurrentSession().createQuery(query).list();
            for (ApplicantReportData applicantReportData : list) {
                try {
                    String socioEconomicInfo = "";
                    if ("bn".equals(locale)) {
                        applicantReportData.setNationalID(CommonUtility.getNumberInBangla(applicantReportData.getNationalID()));
                        if (applicantReportData.getMobileNo() != null) {
                            applicantReportData.setMobileNo(CommonUtility.getNumberInBangla(applicantReportData.getMobileNo()));
                        }
                        if (applicantReportData.getAddress2() != null) {
                            applicantReportData.setAddress(applicantReportData.getAddress() + " " + applicantReportData.getAddress2());
                        }
                        ApplicantSocioEconomicInfo applicantSocioEconomicInfo = (ApplicantSocioEconomicInfo) sessionFactory.getCurrentSession().createQuery("select o from ApplicantSocioEconomicInfo o where o.applicant.id=" + applicantReportData.getBenID()).uniqueResult();
                        if (applicantSocioEconomicInfo != null) {
                            if (applicantReportData.getApplicantType() == ApplicantType.UNION) {
                                socioEconomicInfo += "জমির পরিমাণ: " + applicantSocioEconomicInfo.getLandSizeRural().getDisplayNameBn();
                                socioEconomicInfo += "\nস্বামীর পেশা: " + applicantSocioEconomicInfo.getOccupationRural().getDisplayNameBn();
                                socioEconomicInfo += "\nমাসিক আয়: " + applicantSocioEconomicInfo.getMonthlyIncome().getDisplayNameBn();
                                socioEconomicInfo += "\nপায়খানা আছে?: " + applicantSocioEconomicInfo.gethASLatrineRural().getDisplayNameBn();
                                socioEconomicInfo += "\nবিদ্যুৎ আছে?: " + applicantSocioEconomicInfo.gethASElectricity().getDisplayNameBn();
                                socioEconomicInfo += "\nবৈদ্যুতিক পাখা আছে?: " + applicantSocioEconomicInfo.gethASElectricFan().getDisplayNameBn();
                                socioEconomicInfo += "\nনলকূপ আছে?: " + applicantSocioEconomicInfo.gethASTubewellRural().getDisplayNameBn();
                                socioEconomicInfo += "\nবাড়ির দেয়াল তৈরির উপাদান: " + applicantSocioEconomicInfo.gethHWallMadeOf().getDisplayNameBn();
                                socioEconomicInfo += "\nপ্রতিবন্ধী: " + applicantSocioEconomicInfo.getDisability().getDisplayNameBn();
                            } else {
                                socioEconomicInfo += "নিজের বাসস্থান আছে?: " + applicantSocioEconomicInfo.getHasResidenceUrban().getDisplayNameBn();
                                socioEconomicInfo += "\nপেশা: " + applicantSocioEconomicInfo.getOccupationUrban().getDisplayNameBn();
                                socioEconomicInfo += "\nমাসিক আয়: " + applicantSocioEconomicInfo.getMonthlyIncome().getDisplayNameBn();
                                socioEconomicInfo += "\nরান্নাঘর আছে?: " + applicantSocioEconomicInfo.gethASKitchenUrban().getDisplayNameBn();
                                socioEconomicInfo += "\nবিদ্যুৎ আছে?: " + applicantSocioEconomicInfo.gethASElectricity().getDisplayNameBn();
                                socioEconomicInfo += "\nবৈদ্যুতিক পাখা আছে?: " + applicantSocioEconomicInfo.gethASElectricFan().getDisplayNameBn();
                                socioEconomicInfo += "\nটেলিভিশন আছে?: " + applicantSocioEconomicInfo.gethASTelivisionUrban().getDisplayNameBn();
                                socioEconomicInfo += "\nবাড়ির দেয়াল তৈরির উপাদান: " + applicantSocioEconomicInfo.gethHWallMadeOf().getDisplayNameBn();
                                socioEconomicInfo += "\nপ্রতিবন্ধী: " + applicantSocioEconomicInfo.getDisability().getDisplayNameBn();

                            }

                        }
                        applicantReportData.setSocioEconomicInfo(socioEconomicInfo);
                    } else {
                        ApplicantSocioEconomicInfo applicantSocioEconomicInfo = (ApplicantSocioEconomicInfo) sessionFactory.getCurrentSession().createQuery("select o from ApplicantSocioEconomicInfo o where o.applicant.id=" + applicantReportData.getBenID()).uniqueResult();
                        if (applicantSocioEconomicInfo != null) {
                            if (applicantReportData.getApplicantType() == ApplicantType.UNION) {
                                socioEconomicInfo += "Land Size: " + applicantSocioEconomicInfo.getLandSizeRural().getDisplayName();
                                socioEconomicInfo += "\nHusband's Occupation: " + applicantSocioEconomicInfo.getOccupationRural().getDisplayName();
                                socioEconomicInfo += "\nMonthly Income: " + applicantSocioEconomicInfo.getMonthlyIncome().getDisplayName();
                                socioEconomicInfo += "\nHas Latrine?: " + applicantSocioEconomicInfo.gethASLatrineRural().getDisplayName();
                                socioEconomicInfo += "\nHas Electricity?: " + applicantSocioEconomicInfo.gethASElectricity().getDisplayName();
                                socioEconomicInfo += "\nHas Electric Fan?: " + applicantSocioEconomicInfo.gethASElectricFan().getDisplayName();
                                socioEconomicInfo += "\nHas Tubewell?: " + applicantSocioEconomicInfo.gethASTubewellRural().getDisplayName();
                                socioEconomicInfo += "\nHousehold Wall Made Of: " + applicantSocioEconomicInfo.gethHWallMadeOf().getDisplayName();
                                socioEconomicInfo += "\nDisability Status: " + applicantSocioEconomicInfo.getDisability().getDisplayName();
                            } else {
                                socioEconomicInfo += "Has Own Residence?: " + applicantSocioEconomicInfo.getHasResidenceUrban().getDisplayName();
                                socioEconomicInfo += "\nOccupation: " + applicantSocioEconomicInfo.getOccupationUrban().getDisplayName();
                                socioEconomicInfo += "\nMonthly Income: " + applicantSocioEconomicInfo.getMonthlyIncome().getDisplayName();
                                socioEconomicInfo += "\nHas Kitchen?: " + applicantSocioEconomicInfo.gethASKitchenUrban().getDisplayName();
                                socioEconomicInfo += "\nHas Electricity?: " + applicantSocioEconomicInfo.gethASElectricity().getDisplayName();
                                socioEconomicInfo += "\nHas Electric Fan?: " + applicantSocioEconomicInfo.gethASElectricFan().getDisplayName();
                                socioEconomicInfo += "\nHas Television?: " + applicantSocioEconomicInfo.gethASTelivisionUrban().getDisplayName();
                                socioEconomicInfo += "\nHousehold Wall Made Of: " + applicantSocioEconomicInfo.gethHWallMadeOf().getDisplayName();
                                socioEconomicInfo += "\nDisability Status: " + applicantSocioEconomicInfo.getDisability().getDisplayName();
                            }

                        }
                        applicantReportData.setSocioEconomicInfo(socioEconomicInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            System.out.println("query = " + list.size());
            return list;

        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ApplicantReportData> getApplicantReportDataForPrint(Map parameter) {
        try {
            Integer unionId = (Integer) parameter.get("unionId");
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            List<ApplicationStatus> statusList = (List<ApplicationStatus>) parameter.get("statusList");
            Integer factoryId = (Integer) parameter.get("factoryId");
            String locale = parameter.get("locale") != null ? (parameter.get("locale").toString()) : null;
            StageType stageType = (StageType) parameter.get("stageType");

            String query;
            if ("bn".equals(locale)) {
                query = "select new com.wfp.lmmis.report.data.ApplicantReportData(a.Id, a.fullNameInBangla, a.nid, a.fatherName, a.motherName, a.spouseName, a.dateOfBirth,"
                        + "a.mobileNo, a.permanentDivision.nameInBangla, a.permanentDistrict.nameInBangla, a.permanentUpazila.nameInBangla, a.permanentUnion.nameInBangla, a.presentAddressLine1, a.score, a.applicantType, a.applicationStatus) "
                        + "from Applicant a  where 0 = 0 ";
            } else {
                query = "select new com.wfp.lmmis.report.data.ApplicantReportData(a.Id, a.fullNameInEnglish, a.nid, a.fatherName, a.motherName, a.spouseName, a.dateOfBirth,"
                        + "a.mobileNo, a.permanentDivision.nameInEnglish, a.permanentDistrict.nameInEnglish, a.permanentUpazila.nameInEnglish, a.permanentUnion.nameInEnglish, a.presentAddressLine1, a.score, a.applicantType, a.applicationStatus) "
                        + "from Applicant a where 0 = 0 ";
            }

            query += " and ";
            switch (applicantType) {
                case UNION:
                    query += "a.presentUnion.id=" + unionId;
                    break;
                case MUNICIPAL:
                    query += "a.presentUnion.id=" + unionId;
                    break;
                case CITYCORPORATION:
                    query += "a.presentUnion.id=" + unionId;
                    break;
                case BGMEA:
                    query += "a.factory.id=" + factoryId;
                    break;
                case BKMEA:
                    query += "a.factory.id=" + factoryId;
                    break;
                default:
                    break;
            }

            query += " and a.applicantType = " + applicantType.ordinal();
            if (parameter.get("statusList") != null) {
                query += " and a.applicationStatus IN (:statusList)";
            }
            query += " order by a.score desc";
            System.out.println("query = " + query);
            List<ApplicantReportData> list = sessionFactory.getCurrentSession().createQuery(query).setParameterList("statusList", statusList).list();
            System.out.println("list.size() = " + list.size());
            for (ApplicantReportData applicantReportData : list) {
                try {
                    String socioEconomicInfo = "";
                    if ("bn".equals(locale)) {
                        applicantReportData.setNationalID(CommonUtility.getNumberInBangla(applicantReportData.getNationalID()));
                        if (applicantReportData.getMobileNo() != null) {
                            applicantReportData.setMobileNo(CommonUtility.getNumberInBangla(applicantReportData.getMobileNo()));
                        }
                        ApplicantSocioEconomicInfo applicantSocioEconomicInfo = (ApplicantSocioEconomicInfo) sessionFactory.getCurrentSession().createQuery("select o from ApplicantSocioEconomicInfo o where o.applicant.id=" + applicantReportData.getBenID()).uniqueResult();
                        if (applicantSocioEconomicInfo != null) {
                            if (applicantReportData.getApplicantType() == ApplicantType.UNION) {
                                socioEconomicInfo += "জমির পরিমাণ: " + applicantSocioEconomicInfo.getLandSizeRural().getDisplayNameBn();
                                socioEconomicInfo += "\nস্বামীর পেশা: " + applicantSocioEconomicInfo.getOccupationRural().getDisplayNameBn();
                                socioEconomicInfo += "\nমাসিক আয়: " + applicantSocioEconomicInfo.getMonthlyIncome().getDisplayNameBn();
                                socioEconomicInfo += "\nপায়খানা আছে?: " + applicantSocioEconomicInfo.gethASLatrineRural().getDisplayNameBn();
                                socioEconomicInfo += "\nবিদ্যুৎ আছে?: " + applicantSocioEconomicInfo.gethASElectricity().getDisplayNameBn();
                                socioEconomicInfo += "\nবৈদ্যুতিক পাখা আছে?: " + applicantSocioEconomicInfo.gethASElectricFan().getDisplayNameBn();
                                socioEconomicInfo += "\nনলকূপ আছে?: " + applicantSocioEconomicInfo.gethASTubewellRural().getDisplayNameBn();
                                socioEconomicInfo += "\nবাড়ির দেয়াল তৈরির উপাদান: " + applicantSocioEconomicInfo.gethHWallMadeOf().getDisplayNameBn();
                                socioEconomicInfo += "\nপ্রতিবন্ধী: " + applicantSocioEconomicInfo.getDisability().getDisplayNameBn();
                            } else {
                                socioEconomicInfo += "নিজের বাসস্থান আছে?: " + applicantSocioEconomicInfo.getHasResidenceUrban().getDisplayNameBn();
                                socioEconomicInfo += "\nপেশা: " + applicantSocioEconomicInfo.getOccupationUrban().getDisplayNameBn();
                                socioEconomicInfo += "\nমাসিক আয়: " + applicantSocioEconomicInfo.getMonthlyIncome().getDisplayNameBn();
                                socioEconomicInfo += "\nরান্নাঘর আছে?: " + applicantSocioEconomicInfo.gethASKitchenUrban().getDisplayNameBn();
                                socioEconomicInfo += "\nবিদ্যুৎ আছে?: " + applicantSocioEconomicInfo.gethASElectricity().getDisplayNameBn();
                                socioEconomicInfo += "\nবৈদ্যুতিক পাখা আছে?: " + applicantSocioEconomicInfo.gethASElectricFan().getDisplayNameBn();
                                socioEconomicInfo += "\nটেলিভিশন আছে?: " + applicantSocioEconomicInfo.gethASTelivisionUrban().getDisplayNameBn();
                                socioEconomicInfo += "\nবাড়ির দেয়াল তৈরির উপাদান: " + applicantSocioEconomicInfo.gethHWallMadeOf().getDisplayNameBn();
                                socioEconomicInfo += "\nপ্রতিবন্ধী: " + applicantSocioEconomicInfo.getDisability().getDisplayNameBn();

                            }

                        }
                        applicantReportData.setSocioEconomicInfo(socioEconomicInfo);
                    } else {
                        ApplicantSocioEconomicInfo applicantSocioEconomicInfo = (ApplicantSocioEconomicInfo) sessionFactory.getCurrentSession().createQuery("select o from ApplicantSocioEconomicInfo o where o.applicant.id=" + applicantReportData.getBenID()).uniqueResult();
                        if (applicantSocioEconomicInfo != null) {
                            if (applicantReportData.getApplicantType() == ApplicantType.UNION) {
                                socioEconomicInfo += "Land Size: " + applicantSocioEconomicInfo.getLandSizeRural().getDisplayName();
                                socioEconomicInfo += "\nHusband's Occupation: " + applicantSocioEconomicInfo.getOccupationRural().getDisplayName();
                                socioEconomicInfo += "\nMonthly Income: " + applicantSocioEconomicInfo.getMonthlyIncome().getDisplayName();
                                socioEconomicInfo += "\nHas Latrine?: " + applicantSocioEconomicInfo.gethASLatrineRural().getDisplayName();
                                socioEconomicInfo += "\nHas Electricity?: " + applicantSocioEconomicInfo.gethASElectricity().getDisplayName();
                                socioEconomicInfo += "\nHas Electric Fan?: " + applicantSocioEconomicInfo.gethASElectricFan().getDisplayName();
                                socioEconomicInfo += "\nHas Tubewell?: " + applicantSocioEconomicInfo.gethASTubewellRural().getDisplayName();
                                socioEconomicInfo += "\nHousehold Wall Made Of: " + applicantSocioEconomicInfo.gethHWallMadeOf().getDisplayName();
                                socioEconomicInfo += "\nDisability Status: " + applicantSocioEconomicInfo.getDisability().getDisplayName();
                            } else {
                                socioEconomicInfo += "Has Own Residence?: " + applicantSocioEconomicInfo.getHasResidenceUrban().getDisplayName();
                                socioEconomicInfo += "\nOccupation: " + applicantSocioEconomicInfo.getOccupationUrban().getDisplayName();
                                socioEconomicInfo += "\nMonthly Income: " + applicantSocioEconomicInfo.getMonthlyIncome().getDisplayName();
                                socioEconomicInfo += "\nHas Kitchen?: " + applicantSocioEconomicInfo.gethASKitchenUrban().getDisplayName();
                                socioEconomicInfo += "\nHas Electricity?: " + applicantSocioEconomicInfo.gethASElectricity().getDisplayName();
                                socioEconomicInfo += "\nHas Electric Fan?: " + applicantSocioEconomicInfo.gethASElectricFan().getDisplayName();
                                socioEconomicInfo += "\nHas Television?: " + applicantSocioEconomicInfo.gethASTelivisionUrban().getDisplayName();
                                socioEconomicInfo += "\nHousehold Wall Made Of: " + applicantSocioEconomicInfo.gethHWallMadeOf().getDisplayName();
                                socioEconomicInfo += "\nDisability Status: " + applicantSocioEconomicInfo.getDisability().getDisplayName();
                            }

                        }
                        applicantReportData.setSocioEconomicInfo(socioEconomicInfo);
                    }
                    try {
                        SelectionComments selectionComments = (SelectionComments) sessionFactory.getCurrentSession().createQuery("select o from SelectionComments o where o.applicant.id=" + applicantReportData.getBenID() + " and o.stageType=" + stageType.ordinal()).uniqueResult();
                        if (selectionComments != null) {
                            applicantReportData.setRemarks(selectionComments.getComment());
                        }
                    } catch (HibernateException e) {

                    }
                    String status = "";
                    if (null != applicantReportData.getApplicationStatus()) {
                        switch (applicantReportData.getApplicationStatus()) {
                            case RECOMMENDATION_APPROVED:
                                status = "bn".equals(locale) ? "সুপারিশকৃত" : "Recommended";
                                applicantReportData.setRecommendationStatus(status);
                                break;
                            case RECOMMENDATION_REJECTED:
                                status = "bn".equals(locale) ? "সুপারিশকৃত নয়" : "Not Recommended";
                                applicantReportData.setRecommendationStatus(status);
                                break;
                            case VERIFICATION_APPROVED:
                                status = "bn".equals(locale) ? "অনুমোদিত" : "Approved";
                                applicantReportData.setApprovalStatus(status);
                                break;
                            case VERIFICATION_REJECTED:
                                status = "bn".equals(locale) ? "অনুমোদিত নয়" : "Not Approved";
                                applicantReportData.setApprovalStatus(status);
                                break;
                            default:
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return list;

        } catch (NumberFormatException | HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String updateApplicantStatus(Map parameter) {
        String result = "", subQuerySt = "";
        try {
            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer unionId = parameter.get("unionId") != null ? (Integer) parameter.get("unionId") : null;
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            ApplicationStatus beforeApplicationStatus = (ApplicationStatus) parameter.get("beforeApplicationStatus");
            ApplicationStatus afterApplicationStatus = (ApplicationStatus) parameter.get("afterApplicationStatus");

            String selectQuerySt = "select group_concat(a.Id) from applicant a where a.scheme_id = " + schemeId
                    + " and a.status = " + beforeApplicationStatus.ordinal();

            String querySt = "update applicant a set a.status = " + afterApplicationStatus.ordinal() + " where a.scheme_id = " + schemeId
                    + " and a.status = " + beforeApplicationStatus.ordinal();
            if (unionId != null) {
                subQuerySt += " and a.present_union_id = " + unionId;
            } else {
                subQuerySt += " and a.applicant_type =" + applicantType.ordinal();
            }
            System.out.println("update " + querySt + subQuerySt);
            System.out.println("select " + selectQuerySt + subQuerySt);
            result = (String) sessionFactory.getCurrentSession().createSQLQuery(selectQuerySt + subQuerySt).uniqueResult();
            sessionFactory.getCurrentSession().createSQLQuery(querySt + subQuerySt).executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public void sendPrioritizedListForVerification(Map parameter) throws ExceptionWrapper {
        try {
            Integer unionId = (Integer) parameter.get("unionId");
            ApplicantType applicantType = (ApplicantType) parameter.get("applicantType");
            Integer factoryId = (Integer) parameter.get("factoryId");

            String query = "update Applicant a set a.applicationStatus = " + ApplicationStatus.RECOMMENDATION_PENDING.ordinal() + " where a.applicationStatus=" + ApplicationStatus.PRIORITIZATION_PENDING.ordinal();
            switch (applicantType) {
                case UNION:
                    query += " and a.presentUnion.id=" + unionId;
                    break;
                case MUNICIPAL:
                    query += " and a.presentUnion.id=" + unionId;
                    break;
                case CITYCORPORATION:
                    query += " and a.presentUnion.id=" + unionId;
                    break;
                case BGMEA:
                    query += " and a.factory.id=" + factoryId;
                    break;
                case BKMEA:
                    query += " and a.factory.id=" + factoryId;
                    break;
                default:
                    break;
            }
            System.out.println("query = " + query);
            int result = sessionFactory.getCurrentSession().createQuery(query).executeUpdate();
        } catch (HibernateException e) {
            throw new ExceptionWrapper("Error sending for verification: " + e.getMessage());
        }

    }

    @Override
    public boolean eligibilityToForwardUpazilaStage(int unionId, ApplicantType applicantType) throws ExceptionWrapper {
        try {
            String baseQuery = "select count(a.id) from ApplicantView a where ";
            if (applicantType.equals(ApplicantType.BGMEA) || applicantType.equals(ApplicantType.BKMEA)) {
                baseQuery += " a.factoryId = " + unionId;
            } else {
                baseQuery += " a.unionId = " + unionId;
            }
//            String query = baseQuery + " and a.applicantType = " + applicantType.ordinal()
//                    + " and  a.applicationStatus = " + ApplicationStatus.RECOMMENDATION_PENDING.ordinal();
//            System.out.println("count " + query);
            //Remove 
//            long pendingCount = (Long) sessionFactory.getCurrentSession().createQuery(query).list().get(0);
//            System.out.println("count " + pendingCount);
//            if (pendingCount > 0) {
//                return false;
//            } else {
            String completeQuery = baseQuery + " and  a.applicantType = " + applicantType.ordinal()
                    + " and  (a.applicationStatus = " + ApplicationStatus.RECOMMENDATION_APPROVED.ordinal()
                    + " or a.applicationStatus = " + ApplicationStatus.RECOMMENDATION_REJECTED.ordinal() + ")";
            System.out.println("count complete " + completeQuery);
            long completeCount = (Long) sessionFactory.getCurrentSession().createQuery(completeQuery).list().get(0);
            System.out.println("count complete " + completeCount);
            if (completeCount > 0) {
                return true;
            } else {
                return false;
            }
//            }
        } catch (HibernateException e) {
            throw new ExceptionWrapper("Error sending for verification: " + e.getMessage());
        }
    }

    @Override
    public boolean eligibilityToForwardFromUpazilaStage(int unionId, ApplicantType applicantType) throws ExceptionWrapper {
        try {
            String baseQuery = "select count(a.id) from ApplicantView a where ";
            if (applicantType.equals(ApplicantType.BGMEA) || applicantType.equals(ApplicantType.BKMEA)) {
                baseQuery += " a.factoryId = " + unionId;
            } else {
                baseQuery += " a.unionId = " + unionId;
            }
//            String query = baseQuery + " and a.applicantType = " + applicantType.ordinal()
//                    + " and  a.applicationStatus = " + ApplicationStatus.VERIFICATION_PENDING.ordinal();
//            System.out.println("count " + query);
//            long pendingCount = (Long) sessionFactory.getCurrentSession().createQuery(query).list().get(0);
//            System.out.println("count " + pendingCount);
//            if (pendingCount > 0) {
//                return false;
//            } else {
            String completeQuery = baseQuery + " and  a.applicantType = " + applicantType.ordinal()
                    + " and  (a.applicationStatus = " + ApplicationStatus.VERIFICATION_APPROVED.ordinal()
                    + " or a.applicationStatus = " + ApplicationStatus.VERIFICATION_REJECTED.ordinal() + ")";
            System.out.println("count complete " + completeQuery);
            long completeCount = (Long) sessionFactory.getCurrentSession().createQuery(completeQuery).list().get(0);
            System.out.println("count complete " + completeCount);
            if (completeCount > 0) {
                return true;
            } else {
                return false;
            }
//            }
        } catch (HibernateException e) {
            throw new ExceptionWrapper("Error sending for verification: " + e.getMessage());
        }
    }

    @Override
    public void allowRejectedApplicants(String nidSt) throws ExceptionWrapper {
        System.out.println("nid = " + nidSt);
        BigInteger nid = new BigInteger(CommonUtility.getNumberInEnglish(nidSt));
        try {
            long count = (long) sessionFactory.getCurrentSession().createQuery("select count(a) from Applicant a where a.nid=:nid").setParameter("nid", nid).uniqueResult();
            System.out.println("count = " + count);
            if (count == 0) {
                throw new ExceptionWrapper("NID is not found!");
            }
            sessionFactory.getCurrentSession().createQuery("update Applicant a set a.applicationStatus=" + ApplicationStatus.VERIFICATION_PENDING.ordinal() + " where a.nid=:nid").setParameter("nid", nid).executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public JsonResult isExistOtherMis(String nid) {
        JsonResult jr = new JsonResult(false, "");
        String query = "SELECT mowca_new.beneficiary.scheme_id scheme_id FROM mowca_new.beneficiary\n"
                + " WHERE mowca_new.beneficiary.nid = '" + nid + "'";
        try {
            List data = sessionFactory.getCurrentSession().createSQLQuery(query).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
            Integer count = null;
            for (Object object : data) {
                Map row = (Map) object;
                count = Integer.parseInt(row.get("scheme_id").toString());
            }
            //Integer count = ((Number) sessionFactory.getCurrentSession().createSQLQuery(query).uniqueResult()).intValue();
            if (count != null) {
                if (count == 1) {
                    jr.setErrorMsg("দরিদ্র মা'র জন্য মাতৃত্বকাল ভাতা ভাতাভোগী");
                } else {
                    jr.setErrorMsg("ল্যাকটেটিং মাদার ভাতা ভাতাভোগী");
                }
                jr.setIsError(true);
            } else {

            }
        } catch (Exception ex) {
            jr.setErrorMsg(ex.getMessage());
            ex.printStackTrace();
        }
        return jr;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dashboard.dao;

import com.wfp.lmmis.dashboard.controller.ApplicantCountByStatus;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.UserType;
import com.wfp.lmmis.masterdata.dao.DivisionDaoImpl;
import com.wfp.lmmis.masterdata.dao.UnionDaoImpl;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.dao.PaymentCycleDaoImpl;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.model.VisitingLog;
import com.wfp.lmmis.types.ApplicationStatus;
import com.wfp.lmmis.types.BeneficiaryStatus;
import com.wfp.lmmis.types.PaymentStatus;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.DashBoardView;
import com.wfp.lmmis.utility.IntegerTypeAdapter;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Philip
 */
@Repository
public class DashboardDaoImpl implements DashboardDao {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    private DivisionDaoImpl divisionDaoImpl;

    @Autowired
    private UnionDaoImpl unionDaoImpl;

    @Autowired
    private PaymentCycleDaoImpl paymentCycleDaoImpl;

    Localizer localizer = Localizer.getBrowserLocalizer();

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    DecimalFormat formatter = new DecimalFormat("#,##0");

    @Override
    public Map getDashBoardData(Integer schemeId, Integer fiscalYearId, UserDetail user) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            Map data = new HashMap();

            int totalLoggedInUsers = getTotalLoggedInUsers();
            data.put("totalLoggedInUsers", locale.getLanguage().equals("en") ? formatter.format((double) totalLoggedInUsers) : CommonUtility.getNumberInBangla(formatter.format((double) totalLoggedInUsers)));

            startTime();
            ApplicantCountByStatus applicantCountByStatus = getApplicantsCountByStatus(schemeId, user);
            endTime();
            printDuration("applicantCountByStatus");
            data.put("applicantCountByStatus", applicantCountByStatus);
            startTime();
            data.put("benData", getBeneficiaryData(schemeId, locale, user));
            endTime();
            printDuration("benData");
            startTime();
            data.put("paymentData", getPaymentData(schemeId, fiscalYearId, locale, user));
            endTime();
            printDuration("paymentData");

            return data;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private Map getBeneficiaryData(Integer schemeId, Locale locale, UserDetail user) {

        Map benData = new LinkedHashMap();
        String query = "Select count(o.id) from Beneficiary o where 0=0";
        if (null != user.getUserType()) {
            switch (user.getUserType()) {
                case FIELD:
                    Integer divisionId = user.getDivision() != null ? user.getDivision().getId() : null;
                    Integer districtId = user.getDistrict() != null ? user.getDistrict().getId() : null;
                    Integer upazilaId = user.getUpazila() != null ? user.getUpazila().getId() : null;
                    Integer unionId = user.getUnion() != null ? user.getUnion().getId() : null;
                    if (unionId != null) {
                        query += " and o.presentUnion.id=" + unionId;
                    } else if (unionId == null && upazilaId != null) {
                        query += " and o.presentUpazila.id=" + upazilaId;
                    } else if (upazilaId == null && districtId != null) {
                        query += " and o.presentDistrict.id=" + districtId;
                    } else if (districtId == null && divisionId != null) {
                        query += " and o.presentDivision.id=" + divisionId;
                    }
                    query += " and o.applicantType in (" + ApplicantType.UNION.ordinal() + "," + ApplicantType.CITYCORPORATION.ordinal() + "," + ApplicantType.MUNICIPAL.ordinal() + ")";
                    break;
                case BGMEA:
                    query += " and o.applicantType=" + ApplicantType.BGMEA.ordinal();
                    break;
                case BKMEA:
                    query += " and o.applicantType=" + ApplicantType.BKMEA.ordinal();
                    break;
                default:
                    break;
            }
        }
        query += " and o.beneficiaryStatus=" + BeneficiaryStatus.ACTIVE.ordinal();
        Long totalBeneficiary = (Long) this.getCurrentSession().createQuery(query).uniqueResult();
        String schemeName = locale.getLanguage().equals("bn") ? "মোট সক্রিয় ভাতাভোগী" : "Total Active Beneficiary";
        String count = locale.getLanguage().equals("en") ? formatter.format((double) totalBeneficiary.intValue()) : CommonUtility.getNumberInBangla(formatter.format((double) totalBeneficiary.intValue()));
        benData.put(schemeName, count);
        return benData;
    }

    private List<Scheme> getSchemeList() {
        return this.getCurrentSession().createQuery("select s from Scheme s where s.deleted=0").list();
    }

    private Map getPaymentData(Integer schemeId, Integer fiscalYearId, Locale locale, UserDetail user) {
        Map paymentData = new LinkedHashMap();
        double amount = 0;
        String query = "select sum(o.allowanceAmount) from Payment o where o.paymentStatus = 1";
        if (user.getUserType() == UserType.FIELD) {
            Integer divisionId = user.getDivision() != null ? user.getDivision().getId() : null;
            Integer districtId = user.getDistrict() != null ? user.getDistrict().getId() : null;
            Integer upazilaId = user.getUpazila() != null ? user.getUpazila().getId() : null;
            Integer unionId = user.getUnion() != null ? user.getUnion().getId() : null;

            if (unionId != null) {
                query += " and o.union.id=" + unionId;
            } else if (unionId == null && upazilaId != null) {
                query += " and o.upazilla.id=" + upazilaId;
            } else if (upazilaId == null && districtId != null) {
                query += " and o.district.id=" + districtId;
            } else if (districtId == null && divisionId != null) {
                query += " and o.division.id=" + divisionId;
            }
        }
        try {
            amount = (double) this.getCurrentSession().createQuery(query).uniqueResult();
        } catch (NullPointerException npe) {
            amount = 0;
        }
        String schemeName = locale.getLanguage().equals("bn") ? "মোট পেমেন্ট" : "Total Payments";
        String count = locale.getLanguage().equals("en") ? formatter.format((double) amount) : CommonUtility.getNumberInBangla(formatter.format(amount));
        paymentData.put(schemeName, count);
        amount = getPaymentDataThisYear(fiscalYearId, locale, user);
        schemeName = locale.getLanguage().equals("bn") ? "এই বছর মোট পেমেন্ট" : "Total Payments This Year";
        count = locale.getLanguage().equals("en") ? formatter.format((double) amount) : CommonUtility.getNumberInBangla(formatter.format(amount));
        paymentData.put(schemeName, count);
        return paymentData;
    }

    private double getPaymentDataThisYear(Integer fiscalYearId, Locale locale, UserDetail user) {
        Map paymentData = new LinkedHashMap();
        double amount = 0;
        String query = "select sum(o.allowanceAmount) from Payment o join o.payrollSummary.paymentCycle pc where o.paymentStatus = 1 and pc.fiscalYear.id=" + fiscalYearId;
        if (user.getUserType() == UserType.FIELD) {
            Integer divisionId = user.getDivision() != null ? user.getDivision().getId() : null;
            Integer districtId = user.getDistrict() != null ? user.getDistrict().getId() : null;
            Integer upazilaId = user.getUpazila() != null ? user.getUpazila().getId() : null;
            Integer unionId = user.getUnion() != null ? user.getUnion().getId() : null;

            if (unionId != null) {
                query += " and o.union.id=" + unionId;
            } else if (unionId == null && upazilaId != null) {
                query += " and o.upazilla.id=" + upazilaId;
            } else if (upazilaId == null && districtId != null) {
                query += " and o.district.id=" + districtId;
            } else if (districtId == null && divisionId != null) {
                query += " and o.division.id=" + divisionId;
            }
        }
        try {
            amount = (double) this.getCurrentSession().createQuery(query).uniqueResult();
        } catch (NullPointerException npe) {
            amount = 0;
        }
        return amount;
    }

    private int getTotalApplicant(Integer schemeId, Integer fiscalYearId, User user) {
        if (schemeId != null && fiscalYearId != null) {
            if (user.isHeadOfficeUser()) {
                String query = "Select count(o.id) from Applicant o where o.scheme.id=:schemeId and o.fiscalYear.id=:fiscalYearId";
                Long totalApplicant = (Long) this.getCurrentSession().createQuery(query).setParameter("schemeId", schemeId).setParameter("fiscalYearId", fiscalYearId).uniqueResult();
                return totalApplicant.intValue();
            } else {
                Integer divisionId = user.getDivision().getId();
                Integer districtId = user.getDistrict() != null ? user.getDistrict().getId() : null;
                Integer upazilaId = user.getUpazilla() != null ? user.getUpazilla().getId() : null;
                Integer unionId = user.getUnion() != null ? user.getUnion().getId() : null;
                String query = "Select count(o.id) from Applicant o where o.scheme.id=:schemeId and o.fiscalYear.id=:fiscalYearId";
                if (unionId != null) {
                    query += " and o.presenUnion.id=" + unionId;
                } else if (unionId == null && upazilaId != null) {
                    query += " and o.presentUpazila.id=" + upazilaId;
                } else if (upazilaId == null && districtId != null) {
                    query += " and o.presentDistrict.id=" + districtId;
                } else if (districtId == null && divisionId != null) {
                    query += " and o.presentDivision.id=" + divisionId;
                }
                Object totalApplicant = this.getCurrentSession().createQuery(query).setParameter("schemeId", schemeId).setParameter("fiscalYearId", fiscalYearId).uniqueResult();
                return totalApplicant != null ? ((Long) totalApplicant).intValue() : 0;
            }
        }
        return 0;
    }

    private int getTotalBeneficiary(Integer schemeId, Integer fiscalYearId, User user) {
        if (schemeId != null && fiscalYearId != null) {
            if (user.isHeadOfficeUser()) {
                String query = "Select count(o.id) from Beneficiary o where o.scheme.id=:schemeId and o.fiscalYear.id=:fiscalYearId and o.beneficiaryStatus=:benStatus";
                Long totalBeneficiary = (Long) this.getCurrentSession().createQuery(query).setParameter("schemeId", schemeId).setParameter("fiscalYearId", fiscalYearId).setParameter("benStatus", BeneficiaryStatus.ACTIVE).uniqueResult();
                return totalBeneficiary.intValue();
            } else {
                Integer divisionId = user.getDivision().getId();
                Integer districtId = user.getDistrict() != null ? user.getDistrict().getId() : null;
                Integer upazilaId = user.getUpazilla() != null ? user.getUpazilla().getId() : null;
                Integer unionId = user.getUnion() != null ? user.getUnion().getId() : null;
                String query = "Select count(o.id) from Beneficiary o where o.scheme.id=:schemeId and o.fiscalYear.id=:fiscalYearId and o.beneficiaryStatus=:benStatus";
                if (unionId != null) {
                    query += " and o.presentUnion.id=" + unionId;
                } else if (unionId == null && upazilaId != null) {
                    query += " and o.presentUpazila.id=" + upazilaId;
                } else if (upazilaId == null && districtId != null) {
                    query += " and o.presentDistrict.id=" + districtId;
                } else if (districtId == null && divisionId != null) {
                    query += " and o.presentDivision.id=" + divisionId;
                }
                Object totalBeneficiary = this.getCurrentSession().createQuery(query).setParameter("schemeId", schemeId).setParameter("fiscalYearId", fiscalYearId).setParameter("benStatus", BeneficiaryStatus.ACTIVE).uniqueResult();
                return totalBeneficiary != null ? ((Long) totalBeneficiary).intValue() : 0;
            }
        }
        return 0;
    }

    @Override
    public List<Object> getQuotaVsBenByDivision(Integer schemeId, Integer fiscalYearId, Locale locale) {
        Map quotaData = new LinkedHashMap<>();
        Map benData = new LinkedHashMap<>();

        List<ItemObject> divisionList = this.divisionDaoImpl.getDivisionIoList();
        divisionList.forEach((io)
                -> {
            Long count = (Long) this.getCurrentSession().createQuery("select sum(o.quota) from BenQuota o where o.scheme.id=:schemeId and o.fiscalYear.id=:fiscalYearId and o.upazila.district.division.id=:divisionId")
                    .setParameter("schemeId", schemeId).setParameter("fiscalYearId", fiscalYearId).setParameter("divisionId", io.getId()).uniqueResult();

            String divisionName = locale.getLanguage().equals("en") ? io.getNameInEnglish() : io.getNameInBangla();
            quotaData.put(divisionName, count);
            count = (Long) this.getCurrentSession().createQuery("select count(o.id) from Beneficiary o where o.scheme.id=:schemeId and o.fiscalYear.id=:fiscalYearId and o.presentDivision.id=:divisionId")
                    .setParameter("schemeId", schemeId).setParameter("fiscalYearId", fiscalYearId).setParameter("divisionId", io.getId()).uniqueResult();
            benData.put(divisionName, count);
        });

        List data = new ArrayList<>();
        data.add(quotaData);
        data.add(benData);
        return data;
    }

    @Override
    public List<Object> getBenByDivision(boolean isLma, Integer schemeId, Integer fiscalYearId, Locale locale) {
        Map benData = new LinkedHashMap<>();
        String union = localizer.getLocalizedText("label.union", locale);
        Long count = (Long) this.getCurrentSession().createQuery("select count(o.id) from Beneficiary o where o.applicantType=:applicantType and o.beneficiaryStatus=:status")
                .setParameter("applicantType", ApplicantType.UNION).setParameter("status", BeneficiaryStatus.ACTIVE).uniqueResult();
        benData.put(union, count);

        String municipal = localizer.getLocalizedText("label.municipal", locale);
        count = (Long) this.getCurrentSession().createQuery("select count(o.id) from Beneficiary o where o.applicantType=:applicantType and o.beneficiaryStatus=:status")
                .setParameter("applicantType", ApplicantType.MUNICIPAL).setParameter("status", BeneficiaryStatus.ACTIVE).uniqueResult();
        benData.put(municipal, count);

        String cityCorporation = localizer.getLocalizedText("label.cityCorporation", locale);
        count = (Long) this.getCurrentSession().createQuery("select count(o.id) from Beneficiary o where o.applicantType=:applicantType and o.beneficiaryStatus=:status")
                .setParameter("applicantType", ApplicantType.CITYCORPORATION).setParameter("status", BeneficiaryStatus.ACTIVE).uniqueResult();
        benData.put(cityCorporation, count);

        String bgmea = localizer.getLocalizedText("label.bgmea", locale);
        count = (Long) this.getCurrentSession().createQuery("select count(o.id) from Beneficiary o where o.applicantType=:applicantType and o.beneficiaryStatus=:status")
                .setParameter("applicantType", ApplicantType.BGMEA).setParameter("status", BeneficiaryStatus.ACTIVE).uniqueResult();
        benData.put(bgmea, count);

        String bkmea = localizer.getLocalizedText("label.bkmea", locale);
        count = (Long) this.getCurrentSession().createQuery("select count(o.id) from Beneficiary o where o.applicantType=:applicantType and o.beneficiaryStatus=:status")
                .setParameter("applicantType", ApplicantType.BKMEA).setParameter("status", BeneficiaryStatus.ACTIVE).uniqueResult();
        benData.put(bkmea, count);

        List data = new ArrayList<>();
        data.add(benData);
        return data;
    }

    private int getTotalLoggedInUsers() {
        String query = "Select count(o) from VisitingLog o where o.logged=1 and DATE(o.loginDate)=:today";
        Long visitingLogs = (Long) this.getCurrentSession().createQuery(query).setParameter("today", Calendar.getInstance().getTime()).uniqueResult();
        return visitingLogs.intValue();
    }

    @Override
    public List<Object> getQuotaVsBenByUnion(Integer schemeId, Integer fiscalYearId, Locale locale, Integer upazilaId) {
        Map quotaData = new LinkedHashMap<>();
        Map benData = new LinkedHashMap<>();

        List<ItemObject> unionList = this.unionDaoImpl.getUnionIoList(upazilaId);
        unionList.forEach((io)
                -> {
            Long count = (Long) this.getCurrentSession().createQuery("select sum(o.quota) from BenQuota o where o.scheme.id=:schemeId and o.fiscalYear.id=:fiscalYearId and o.union.id=:unionId")
                    .setParameter("schemeId", schemeId).setParameter("fiscalYearId", fiscalYearId).setParameter("unionId", io.getId()).uniqueResult();

            String divisionName = locale.getLanguage().equals("en") ? io.getNameInEnglish() : io.getNameInBangla();
            quotaData.put(divisionName, count);
            count = (Long) this.getCurrentSession().createQuery("select count(o.id) from Beneficiary o where o.scheme.id=:schemeId and o.fiscalYear.id=:fiscalYearId and o.presentUnion.id=:unionId")
                    .setParameter("schemeId", schemeId).setParameter("fiscalYearId", fiscalYearId).setParameter("unionId", io.getId()).uniqueResult();
            benData.put(divisionName, count);
        });

        List data = new ArrayList<>();
        data.add(quotaData);
        data.add(benData);
        return data;
    }

    @Override
    public Map getPayrollByCycle(Integer schemeId, Integer fiscalYearId, Locale locale) {
        List<ItemObject> paymentCycleList = this.paymentCycleDaoImpl.getPaymentCycleIoList(true, fiscalYearId, false, false, schemeId);
        Map data = new LinkedHashMap<>();
        double amount = 0d;
        for (ItemObject io : paymentCycleList) {
            try {
                amount = (double) this.getCurrentSession().createQuery("select sum(o.allowanceAmount) from Payment o where o.scheme.id=:schemeId and o.paymentCycle.id=:paymentCycleId")
                        .setParameter("schemeId", schemeId).setParameter("paymentCycleId", io.getId()).uniqueResult();
            } catch (Exception e) {
                amount = 0d;
            }
            String cycleName = locale.getLanguage().equals("en") ? io.getNameInEnglish() : io.getNameInBangla();
//            System.out.println("cycleName = " + cycleName + ": amount = " + amount);
            if (amount > 0) {
                data.put(cycleName, amount);
            }
        };
        return data;
    }

    @Override
    public Map getPayrollByFiscalYear(Integer schemeId, Locale locale) {
        List<FiscalYear> fiscalYearList = this.getCurrentSession().createQuery("select f from FiscalYear f order by f.startYear").list();
        Map data = new LinkedHashMap<>();
        double amount = 0d;
        for (FiscalYear fy : fiscalYearList) {
            try {
                amount = (double) this.getCurrentSession().createQuery("select sum(o.allowanceAmount) from Payment o where o.paymentCycle.fiscalYear.id=:fyId and o.paymentStatus=1").setParameter("fyId", fy.getId()).uniqueResult();
            } catch (Exception e) {
                amount = 0d;
            }
            String cycleName = locale.getLanguage().equals("en") ? fy.getNameInEnglish() : fy.getNameInBangla();
            data.put(cycleName, amount);
        };
        return data;
    }

    @Override
    public Map getApplicantByMonth(Integer schemeId, Integer fiscalYearId, Locale locale, UserDetail user) {
        String query = "select count(o.id) FROM Applicant o where month(o.creationDate)=:month and o.fiscalYear.id=:fiscalYearId";
        if (null != user.getUserType()) {
            switch (user.getUserType()) {
                case FIELD:
                    Integer divisionId = user.getDivision() != null ? user.getDivision().getId() : null;
                    Integer districtId = user.getDistrict() != null ? user.getDistrict().getId() : null;
                    Integer upazilaId = user.getUpazila() != null ? user.getUpazila().getId() : null;
                    Integer unionId = user.getUnion() != null ? user.getUnion().getId() : null;
                    if (unionId != null) {
                        query += " and o.presentUnion.id=" + unionId;
                    } else if (unionId == null && upazilaId != null) {
                        query += " and o.presentUpazila.id=" + upazilaId;
                    } else if (upazilaId == null && districtId != null) {
                        query += " and o.presentDistrict.id=" + districtId;
                    } else if (districtId == null && divisionId != null) {
                        query += " and o.presentDivision.id=" + divisionId;
                    }
                    query += " and o.applicantType in (" + ApplicantType.UNION.ordinal() + "," + ApplicantType.CITYCORPORATION.ordinal() + "," + ApplicantType.MUNICIPAL.ordinal() + ")";
                    break;
                case BGMEA:
                    query += " and o.applicantType=" + ApplicantType.BGMEA.ordinal();
                    break;
                case BKMEA:
                    query += " and o.applicantType=" + ApplicantType.BKMEA.ordinal();
                    break;
                default:
                    break;
            }
        }
        Map data = new LinkedHashMap();
        String[] months;
        if (locale.getLanguage().equals("en")) {
            months = new String[]{
                //                "July", "August", "September", "October", "November", "December", "January", "February", "March", "April", "May", "June"
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun"
            };
        } else {
            months = new String[]{
                //                "জুলাই", "অগাস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর", "জানুয়ারী", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন"
                "জু", "আ", "সে", "অ", "ন", "ডি", "জা", "ফে", "মার্চ", "এ", "মে", "জুন"
            };

        }
        int dbMonthIndex = 7;
        for (int i = 1; i <= 6; i++) {
            Long total = (Long) this.getCurrentSession().createQuery(query).setParameter("month", dbMonthIndex).setParameter("fiscalYearId", fiscalYearId).uniqueResult();
            data.put(months[i - 1], total);
            dbMonthIndex++;
        }
        dbMonthIndex = 1;
        for (int i = 7; i <= 12; i++) {
            Long total = (Long) this.getCurrentSession().createQuery(query).setParameter("month", dbMonthIndex).setParameter("fiscalYearId", fiscalYearId).uniqueResult();
            data.put(months[i - 1], total);
            dbMonthIndex++;
        }

        return data;
    }

    public ApplicantCountByStatus getApplicantsCountByStatus(Integer schemeId, UserDetail user) {
        Locale locale = LocaleContextHolder.getLocale();
        String applicantCountQuery = "select count(o) from Applicant o where 0=0";
        String fieldVerificationCountQuery = "select count(o) from Applicant o where o.recommendationStatus=:recommendationStatus";
        String rejectedAtFieldCountQuery = "select count(o) from Applicant o where o.recommendationStatus=:recommendationStatus";
        String geoQuery = "";
        if (null != user.getUserType()) {
            switch (user.getUserType()) {
                case FIELD:
                    Integer divisionId = user.getDivision() != null ? user.getDivision().getId() : null;
                    Integer districtId = user.getDistrict() != null ? user.getDistrict().getId() : null;
                    Integer upazilaId = user.getUpazila() != null ? user.getUpazila().getId() : null;
                    Integer unionId = user.getUnion() != null ? user.getUnion().getId() : null;
                    if (unionId != null) {
                        geoQuery += " and o.presentUnion.id=" + unionId;
                    } else if (unionId == null && upazilaId != null) {
                        geoQuery += " and o.presentUpazila.id=" + upazilaId;
                    } else if (upazilaId == null && districtId != null) {
                        geoQuery += " and o.presentDistrict.id=" + districtId;
                    } else if (districtId == null && divisionId != null) {
                        geoQuery += " and o.presentDivision.id=" + divisionId;
                    }
                    break;
                case BGMEA:
                    geoQuery += " and o.applicantType=" + ApplicantType.BGMEA.ordinal();
                    break;
                case BKMEA:
                    geoQuery += " and o.applicantType=" + ApplicantType.BKMEA.ordinal();
                    break;
                default:
                    break;
            }
        }
        startTime();
        Object object = this.getCurrentSession().createQuery(applicantCountQuery + geoQuery).uniqueResult();
        endTime();
        printDuration("primarilyApproved");
        int primarilyApproved = object != null ? ((Long) object).intValue() : 0;

        startTime();
        object = this.getCurrentSession().createQuery(fieldVerificationCountQuery + geoQuery).setParameter("recommendationStatus", true).uniqueResult();
        endTime();
        printDuration("fieldVerifiedCount");
        int fieldVerifiedCount = object != null ? ((Long) object).intValue() : 0;

        startTime();
        object = this.getCurrentSession().createQuery(rejectedAtFieldCountQuery + geoQuery).setParameter("recommendationStatus", false).uniqueResult();
        endTime();
        printDuration("rejectedAtField");
        int rejectedAtField = object != null ? ((Long) object).intValue() : 0;

        return new ApplicantCountByStatus(
                locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format(primarilyApproved)) : formatter.format(primarilyApproved),
                locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format(fieldVerifiedCount)) : formatter.format(fieldVerifiedCount),
                locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format(rejectedAtField)) : formatter.format(rejectedAtField)
        );

    }
    long startTime = System.nanoTime();
    long endTime = System.nanoTime();

    private void startTime() {
        startTime = System.nanoTime();
    }

    private void endTime() {
        endTime = System.nanoTime();
    }

    private void printDuration(String msg) {
        long duration = (endTime - startTime) / 1000000;
        System.out.println(msg + " " + duration);
    }

    @Override
    public Map getTrainingByFiscalYear(Integer schemeId, Locale locale) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map getDashBoardDataUPdate(Integer schemeId, Integer fiscalYearId, UserDetail user) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            Map data = new HashMap();

            int totalLoggedInUsers = getTotalLoggedInUsers();
            data.put("totalLoggedInUsers", locale.getLanguage().equals("en") ? formatter.format((double) totalLoggedInUsers) : CommonUtility.getNumberInBangla(formatter.format((double) totalLoggedInUsers)));

            String query = "";
            query = "select "
                    + "COALESCE(total_beneficiary,0) totalBeneficiary,\n"
                    + "COALESCE(active_beneficiary,0)activeBeneficiary,\n"
                    + "COALESCE(total_payemt_amount,0) totalPayemtAmount,\n"
                    + "COALESCE(total_payemt_amount_current_year,0) totalPayemtAmountCurrentYear,\n"
                    + "COALESCE(total_applicant,0) totalApplicant,\n"
                    + "COALESCE(imlma_referenc_applicant,0) imlmaReferencApplicant,\n"
                    + "COALESCE(imlma_non_referenc_applicant,0) imlmaNonReferencApplicant,\n"
                    + "COALESCE(total_union_beneficiary_active,0) totalUnionBeneficiaryActive,\n"
                    + "COALESCE(total_bgme_beneficiary_active,0) totalBgmeBeneficiaryActive,\n"
                    + "COALESCE(total_bkme_beneficiary_active,0) totalBkmeBeneficiaryActive,\n"
                    + "COALESCE(total_city_corporation_beneficiary_active,0) totalCityCorporationBeneficiaryActive,\n"
                    + "COALESCE(total_municipal_beneficiary_active,0) totalMunicipalBeneficiaryActive\n,"
                    + "COALESCE(applicant_jan,0) applicantJan,\n"
                    + "COALESCE(applicant_feb,0) applicantFeb,\n"
                    + "COALESCE(applicant_mar,0) applicantMar,\n"
                    + "COALESCE(applicant_apr,0) applicantApr,\n"
                    + "COALESCE(applicant_may,0) applicantMay,\n"
                    + "COALESCE(applicant_jun,0) applicantJun,\n"
                    + "COALESCE(applicant_jul,0) applicantJul,\n"
                    + "COALESCE(applicant_aug,0) applicantAug,\n"
                    + "COALESCE(applicant_sep,0) applicantSep,\n"
                    + "COALESCE(applicant_oct,0) applicantOct,\n"
                    + "COALESCE(applicant_nov,0) applicantNov,\n"
                    + "COALESCE(applicant_dec,0) applicantDec,\n"
                    + "COALESCE(payment_fiscal_year_1,0) paymentFiscalYear1,\n"
                    + "COALESCE(payment_fiscal_year_2,0)  paymentFiscalYear2,\n"
                    + "COALESCE(payment_fiscal_year_3,0)  paymentFiscalYear3,\n"
                    + "COALESCE(payment_fiscal_year_4,0)  paymentFiscalYear4\n";
            if (null != user.getUserType()) {

                Integer divisionId = user.getDivision() != null ? user.getDivision().getId() : null;
                Integer districtId = user.getDistrict() != null ? user.getDistrict().getId() : null;
                Integer upazilaId = user.getUpazila() != null ? user.getUpazila().getId() : null;
                Integer unionId = user.getUnion() != null ? user.getUnion().getId() : null;
                if (unionId != null) {
                    query += "  from union_extend where  union_extend.union_id=" + unionId;
                } else if (unionId == null && upazilaId != null) {
                    query += " from upazila_extend where  upazila_extend.upazila_id=" + upazilaId;
                } else if (upazilaId == null && districtId != null) {
                    query += " from district_extend where  district_extend.district_id=" + districtId;
                } else if (districtId == null && divisionId != null) {
                    query += "select * from division_extend where  division_extend.division_id=" + divisionId;
                } else {
//                    query += "select "
//                            + "total_beneficiary totalBeneficiary,\n"
//                            + "active_beneficiary activeBeneficiary,\n"
//                            + "total_payemt_amount totalPayemtAmount,\n"
//                            + "total_payemt_amount_current_year totalPayemtAmountCurrentYear,\n"
//                            + "total_applicant totalApplicant,\n"
//                            + "imlma_referenc_applicant imlmaReferencApplicant,\n"
//                            + "imlma_non_referenc_applicant imlmaNonReferencApplicant,\n"
//                            + "total_union_beneficiary_active totalUnionBeneficiaryActive,\n"
//                            + "total_bgme_beneficiary_active totalBgmeBeneficiaryActive,\n"
//                            + "total_bkme_beneficiary_active totalBkmeBeneficiaryActive,\n"
//                            + "total_city_corporation_beneficiary_active totalCityCorporationBeneficiaryActive,\n"
//                            + "total_municipal_beneficiary_active totalMunicipalBeneficiaryActive\n,"
//                            + "applicant_jan applicantJan,\n"
//                            + "applicant_feb applicantFeb,\n"
//                            + "applicant_mar applicantMar,\n"
//                            + "applicant_apr applicantApr,\n"
//                            + "applicant_may applicantMay,\n"
//                            + "applicant_jun applicantJun,\n"
//                            + "applicant_jul applicantJul,\n"
//                            + "applicant_aug applicantAug,\n"
//                            + "applicant_sep applicantSep,\n"
//                            + "applicant_oct applicantOct,\n"
//                            + "applicant_nov applicantNov,\n"
//                            + "applicant_dec applicantDec,\n"
//                            + "payment_fiscal_year_1 paymentFiscalYear1,\n"
//                            + "payment_fiscal_year_2  paymentFiscalYear2,\n"
//                            + "payment_fiscal_year_3  paymentFiscalYear3,\n"
//                            + "payment_fiscal_year_4  paymentFiscalYear4\n"
//                            + "from dashboard;";

                    query += " from dashboard;";

                }

            }

            List<DashBoardView> dashBoardViews = this.getCurrentSession().createSQLQuery(query)
                    .addScalar("totalBeneficiary", IntegerType.INSTANCE)
                    .addScalar("activeBeneficiary", IntegerType.INSTANCE)
                    .addScalar("totalPayemtAmount", DoubleType.INSTANCE)
                    .addScalar("totalPayemtAmountCurrentYear", DoubleType.INSTANCE)
                    .addScalar("totalApplicant", IntegerType.INSTANCE)
                    .addScalar("imlmaReferencApplicant", IntegerType.INSTANCE)
                    .addScalar("imlmaNonReferencApplicant", IntegerType.INSTANCE)
                    .addScalar("totalUnionBeneficiaryActive", IntegerType.INSTANCE)
                    .addScalar("totalBgmeBeneficiaryActive", IntegerType.INSTANCE)
                    .addScalar("totalBkmeBeneficiaryActive", IntegerType.INSTANCE)
                    .addScalar("totalCityCorporationBeneficiaryActive", IntegerType.INSTANCE)
                    .addScalar("totalMunicipalBeneficiaryActive", IntegerType.INSTANCE)
                    .addScalar("applicantJan", IntegerType.INSTANCE)
                    .addScalar("applicantFeb", IntegerType.INSTANCE)
                    .addScalar("applicantMar", IntegerType.INSTANCE)
                    .addScalar("applicantApr", IntegerType.INSTANCE)
                    .addScalar("applicantMay", IntegerType.INSTANCE)
                    .addScalar("applicantJun", IntegerType.INSTANCE)
                    .addScalar("applicantJul", IntegerType.INSTANCE)
                    .addScalar("applicantAug", IntegerType.INSTANCE)
                    .addScalar("applicantSep", IntegerType.INSTANCE)
                    .addScalar("applicantOct", IntegerType.INSTANCE)
                    .addScalar("applicantNov", IntegerType.INSTANCE)
                    .addScalar("applicantDec", IntegerType.INSTANCE)
                    .addScalar("paymentFiscalYear1", DoubleType.INSTANCE)
                    .addScalar("paymentFiscalYear2", DoubleType.INSTANCE)
                    .addScalar("paymentFiscalYear3", DoubleType.INSTANCE)
                    .addScalar("paymentFiscalYear4", DoubleType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(DashBoardView.class)).list();

            //    ApplicantCountByStatus applicantCountByStatus = getApplicantsCountByStatus(schemeId, user);
            String schemeName = locale.getLanguage().equals("bn") ? "মোট সক্রিয় ভাতাভোগী" : "Total Active Beneficiary";
            String count = "0";
            if (dashBoardViews.size() > 0) {
                count = locale.getLanguage().equals("en") ? formatter.format((double) dashBoardViews.get(0).getActiveBeneficiary()) : CommonUtility.getNumberInBangla(formatter.format((double) dashBoardViews.get(0).getActiveBeneficiary()));
            }
            Map benData = new LinkedHashMap();

            benData.put(schemeName, count);
            data.put("benData", benData);

            Map paymentData = new LinkedHashMap();

            schemeName = locale.getLanguage().equals("bn") ? "মোট পেমেন্ট" : "Total Payments";
            count = locale.getLanguage().equals("en") ? formatter.format((double) dashBoardViews.get(0).getTotalPayemtAmount()) : CommonUtility.getNumberInBangla(formatter.format(dashBoardViews.get(0).getTotalPayemtAmount()));
            paymentData.put(schemeName, count);

            schemeName = locale.getLanguage().equals("bn") ? "এই বছর মোট পেমেন্ট" : "Total Payments This Year";
            count = locale.getLanguage().equals("en") ? formatter.format((double) dashBoardViews.get(0).getTotalPayemtAmountCurrentYear()) : CommonUtility.getNumberInBangla(formatter.format(dashBoardViews.get(0).getTotalPayemtAmountCurrentYear()));
            paymentData.put(schemeName, count);
            data.put("paymentData", paymentData);

            ApplicantCountByStatus applicantCountByStatus = new ApplicantCountByStatus(
                    locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format(dashBoardViews.get(0).getTotalApplicant())) : formatter.format(dashBoardViews.get(0).getTotalApplicant()),
                    locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format(dashBoardViews.get(0).getImlmaReferencApplicant())) : formatter.format(dashBoardViews.get(0).getImlmaReferencApplicant()),
                    locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format(dashBoardViews.get(0).getImlmaNonReferencApplicant())) : formatter.format(dashBoardViews.get(0).getImlmaNonReferencApplicant())
            );
            data.put("applicantCountByStatus", applicantCountByStatus);

            Map benDataAreaWise = new LinkedHashMap<>();

            String union = localizer.getLocalizedText("label.union", locale);
            String municipal = localizer.getLocalizedText("label.municipal", locale);
            String cityCorporation = localizer.getLocalizedText("label.cityCorporation", locale);
            String bgmea = localizer.getLocalizedText("label.bgmea", locale);
            String bkmea = localizer.getLocalizedText("label.bkmea", locale);

            String unionActiveBeneficiay = locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format((double) dashBoardViews.get(0).getTotalUnionBeneficiaryActive())) : formatter.format((double) dashBoardViews.get(0).getTotalUnionBeneficiaryActive());
            benDataAreaWise.put(union, unionActiveBeneficiay);
            benDataAreaWise.put(municipal, locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format((double) dashBoardViews.get(0).getTotalMunicipalBeneficiaryActive())) : formatter.format((double) dashBoardViews.get(0).getTotalMunicipalBeneficiaryActive()));
            benDataAreaWise.put(cityCorporation, locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format((double) dashBoardViews.get(0).getTotalCityCorporationBeneficiaryActive())) : formatter.format((double) dashBoardViews.get(0).getTotalCityCorporationBeneficiaryActive()));
            benDataAreaWise.put(bgmea, locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format((double) dashBoardViews.get(0).getTotalBgmeBeneficiaryActive())) : formatter.format((double) dashBoardViews.get(0).getTotalBgmeBeneficiaryActive()));
            benDataAreaWise.put(bkmea, locale.getLanguage().equals("bn") ? CommonUtility.getNumberInBangla(formatter.format((double) dashBoardViews.get(0).getTotalBkmeBeneficiaryActive())) : formatter.format((double) dashBoardViews.get(0).getTotalBkmeBeneficiaryActive()));
            List dataList = new ArrayList<>();
            dataList.add(benDataAreaWise);
            dataList.add(localizer.getLocalizedText("dashboard.beneficiary", LocaleContextHolder.getLocale())); // legendY
            dataList.add(localizer.getLocalizedText("dashboard.division", LocaleContextHolder.getLocale())); //labelX
            dataList.add(localizer.getLocalizedText("dashboard.count", LocaleContextHolder.getLocale())); //labelY

            //data.put("applicantCountByStatus", applicantCountByStatus);
            // data.put("benData", getBeneficiaryData(schemeId, locale, user));
            //  data.put("paymentData", getPaymentData(schemeId, fiscalYearId, locale, user));
            Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerTypeAdapter()).create();

            data.put("benDataAreaWise", gson.toJson(dataList));

            //month wise applicant
            Map monthWiseApplicant = new LinkedHashMap();

            String[] months;
            if (locale.getLanguage().equals("en")) {
                months = new String[]{
                    //                "July", "August", "September", "October", "November", "December", "January", "February", "March", "April", "May", "June"
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun"
                };
            } else {
                months = new String[]{
                    //                "জুলাই", "অগাস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর", "জানুয়ারী", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন"
                    "জু", "আ", "সে", "অ", "ন", "ডি", "জা", "ফে", "মার্চ", "এ", "মে", "জুন"
                };

            }
            monthWiseApplicant.put(months[0], dashBoardViews.get(0).getApplicantJul());
            monthWiseApplicant.put(months[1], dashBoardViews.get(0).getApplicantAug());
            monthWiseApplicant.put(months[2], dashBoardViews.get(0).getApplicantSep());
            monthWiseApplicant.put(months[3], dashBoardViews.get(0).getApplicantOct());
            monthWiseApplicant.put(months[4], dashBoardViews.get(0).getApplicantNov());
            monthWiseApplicant.put(months[5], dashBoardViews.get(0).getApplicantDec());
            monthWiseApplicant.put(months[6], dashBoardViews.get(0).getApplicantJan());
            monthWiseApplicant.put(months[7], dashBoardViews.get(0).getApplicantFeb());
            monthWiseApplicant.put(months[8], dashBoardViews.get(0).getApplicantMar());
            monthWiseApplicant.put(months[9], dashBoardViews.get(0).getApplicantApr());
            monthWiseApplicant.put(months[10], dashBoardViews.get(0).getApplicantMay());
            monthWiseApplicant.put(months[11], dashBoardViews.get(0).getApplicantJun());
            List monthWiseApplicantList = new ArrayList<>();
            monthWiseApplicantList.add(monthWiseApplicant);

            monthWiseApplicantList.add(localizer.getLocalizedText("dashboard.count", LocaleContextHolder.getLocale()));// legend
            monthWiseApplicantList.add(localizer.getLocalizedText("dashboard.month", LocaleContextHolder.getLocale()));//labelX
            monthWiseApplicantList.add(localizer.getLocalizedText("dashboard.trainingSession", LocaleContextHolder.getLocale()));//labelY

            data.put("monthWiseApplicant", gson.toJson(monthWiseApplicantList));

            // fiscal year wise payment
            Map fiscalYearMap = new LinkedHashMap<>();
            List<FiscalYear> fiscalYearList = this.getCurrentSession().createQuery("select f from FiscalYear f order by f.startYear").list();
            double amount = 0d;
            for (FiscalYear fy : fiscalYearList) {
                try {
                    switch (fy.getId()) {
                        case 1:
                            amount = (double) dashBoardViews.get(0).getPaymentFiscalYear1();
                            break;
                        case 2:
                            amount = (double) dashBoardViews.get(0).getPaymentFiscalYear2();
                            break;
                        case 3:
                            amount = (double) dashBoardViews.get(0).getPaymentFiscalYear3();
                            break;
                        case 4:
                            amount = (double) dashBoardViews.get(0).getPaymentFiscalYear4();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    amount = 0d;
                }

                String cycleName = locale.getLanguage().equals("en") ? fy.getNameInEnglish() : fy.getNameInBangla();
                fiscalYearMap.put(cycleName, amount);
            };

            List<Object> fiscalYearMapObject = new ArrayList<>();
            fiscalYearMapObject.add(fiscalYearMap);
            fiscalYearMapObject.add(localizer.getLocalizedText("dashboard.amount", LocaleContextHolder.getLocale()));// legend
            fiscalYearMapObject.add(localizer.getLocalizedText("dashboard.fiscalYear", LocaleContextHolder.getLocale()));//labelX
            fiscalYearMapObject.add(localizer.getLocalizedText("dashboard.amount", LocaleContextHolder.getLocale()));//labelY

            data.put("ficalYearWisePayment", gson.toJson(fiscalYearMapObject));

            return data;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}

package com.wfp.lmmis.grievance.dao;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.model.Grievance;
import com.wfp.lmmis.grievance.model.GrievanceListData;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.report.data.GrievanceReportData;
import com.wfp.lmmis.utility.CommonUtility;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GrievanceDaoImpl implements GrievanceDao
{

    //private static final logger logger = //logger.getlogger(GrievanceDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Grievance getGrievance(Integer id)
    {
        Grievance grievance = (Grievance) getCurrentSession().get(Grievance.class, id);
        return grievance;
    }

    @Override
    public void save(Grievance grievance)
    {
        this.getCurrentSession().save(grievance);
    }

    @Override
    public void edit(Grievance grievance)
    {
        //this.getCurrentSession().update(grievance);
        Grievance dBGrievance = getGrievance(grievance.getId());
        grievance.setCreatedBy(dBGrievance.getCreatedBy());
        grievance.setCreationDate(dBGrievance.getCreationDate());
        this.getCurrentSession().merge(grievance);
    }

    @Override
    public void delete(Grievance grievance) throws ExceptionWrapper
    {
        try
        {
            Query q = this.getCurrentSession().createQuery("update Grievance set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id=" + grievance.getId());
            q.setParameter("modifiedBy", grievance.getModifiedBy());
            q.setParameter("modificationDate", grievance.getModificationDate());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Grievance, grievance.getId(), grievance.getModifiedBy(), grievance.getModificationDate(), grievance.getDescription(), null);
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            //logger.error(e.getMessage());
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<Grievance> getGrievanceList()
    {
        List<Grievance> list = null;
        list = sessionFactory.getCurrentSession().createQuery("select g from Grievance g join g.grievanceStatus gs "
                + "where g.deleted=0 and gs.displayOrder < (select max(displayOrder) from GrievanceStatus)").list();
        System.out.println("list.size() = " + list.size());
        return list;
    }

    @Override
    public List<GrievanceReportData> getGrievanceReportData(Map parameter)
    {
        try
        {
            Integer schemeId = (Integer) parameter.get("schemeId");
            Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            String locale = parameter.get("locale") != null ? ((Locale) parameter.get("locale")).toString() : null;
            String query;
            if ("en".equals(locale))
            {
                query = "select new com.wfp.lmmis.report.data.GrievanceReportData(b.fullNameInEnglish, b.nid, g.grievanceType.nameInEnglish, g.description, g.comment,"
                        + "g.grievanceStatus.nameInEnglish, b.permanentDivision.nameInEnglish, b.permanentDistrict.nameInEnglish, b.permanentUpazila.nameInEnglish, b.permanentUnion.nameInEnglish) "
                        + "from Grievance g join g.beneficiary b where 0 = 0 ";
            }
            else
            {
                query = "select new com.wfp.lmmis.report.data.GrievanceReportData(b.fullNameInBangla, b.nid, g.grievanceType.nameInBangla, g.description, g.comment,"
                        + "g.grievanceStatus.nameInBangla, b.permanentDivision.nameInBangla, b.permanentDistrict.nameInBangla, b.permanentUpazila.nameInBangla, b.permanentUnion.nameInBangla) "
                        + "from Grievance g join g.beneficiary b where 0 = 0 ";
            }

            if (schemeId != null && schemeId != 0)
            {
                query += " AND b.scheme.id = " + schemeId;
            }
            if (fiscalYearId != null && fiscalYearId != 0)
            {
                query += " AND b.fiscalYear.id = " + fiscalYearId;
            }
            if (divisionId != null)
            {
                query += " and b.permanentDivision.id=" + divisionId;
            }
            if (districtId != null)
            {
                query += " and b.permanentDistrict.id=" + districtId;
            }
            if (upazilaId != null)
            {
                query += " and b.permanentUpazila.id=" + upazilaId;
            }
            if (unionId != null)
            {
                query += " and b.permanentUnion.id=" + unionId;
            }
            query += " order by b.permanentDivision.nameInEnglish, b.permanentDistrict.nameInEnglish, b.permanentUpazila.nameInEnglish, b.permanentUnion.nameInEnglish";
            System.out.println("query = " + query);
            List<GrievanceReportData> list = sessionFactory.getCurrentSession().createQuery(query).list();
            for (GrievanceReportData grievanceReportData : list)
            {
                if ("bn".equals(locale))
                {
                    grievanceReportData.setBenID(CommonUtility.getNumberInBangla(grievanceReportData.getBenID().toString()));
                }
                else
                {
                }
            }
            return list;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Object> getGrievanceListBySearchParameter(Map parameter, int beginIndex, int pageSize)
    {
        try
        {
            String nid = (String) parameter.get("nid");
            Integer grievanceType = (Integer) parameter.get("grievanceType");
            Integer grievanceStatus = (Integer) parameter.get("grievanceStatus");
            Integer divisionId = (Integer) parameter.get("divisionId");
            Integer districtId = (Integer) parameter.get("districtId");
            Integer upazilaId = (Integer) parameter.get("upazilaId");
            Integer unionId = (Integer) parameter.get("unionId");
            Integer bgmeaFactoryId = parameter.get("bgmeaFactoryId") != null ? (Integer) parameter.get("bgmeaFactoryId") : null;
            Integer bkmeaFactoryId = parameter.get("bkmeaFactoryId") != null ? (Integer) parameter.get("bkmeaFactoryId") : null;
            ApplicantType applicantType = parameter.get("applicantType") != null ? (ApplicantType) parameter.get("applicantType") : null;
            String locale = parameter.get("locale") != null ? ((Locale) parameter.get("locale")).toString() : null;

            String mainQuerySt;// = "from Grievance o where 0=0 ";
            if ("en".equals(locale))
            {
                mainQuerySt = "select new com.wfp.lmmis.grievance.model.GrievanceListData(o.id, o.beneficiary.fullNameInEnglish, o.beneficiary.nid, o.grievanceType.nameInEnglish, o.description, o.comment, o.grievanceStatus.nameInEnglish)"
                        + " from Grievance o  where 0 = 0 ";
            }
            else
            {
                mainQuerySt = "select new com.wfp.lmmis.grievance.model.GrievanceListData(o.id, o.beneficiary.fullNameInBangla, o.beneficiary.nid, o.grievanceType.nameInBangla, o.description, o.comment, o.grievanceStatus.nameInBangla)"
                        + " from Grievance o where 0 = 0 ";
            }
            String countQuerySt = "select count(o.id) from Grievance o where 0=0 ";
            String querySt = "";

            if (nid != null)
            {
                querySt += " AND o.beneficiary.nid like '%" + CommonUtility.getNumberInEnglish(nid) + "%'";
            }
            if (grievanceType != null)
            {
                querySt += " and o.grievanceType.id = " + grievanceType;
            }
            if (grievanceStatus != null)
            {
                querySt += " and o.grievanceStatus = " + grievanceStatus;
            }
            querySt += " AND o.beneficiary.applicantType = " + applicantType.ordinal();

            if (bgmeaFactoryId != null && bgmeaFactoryId != 0)
            {
                querySt += " and o.beneficiary.factory.id = " + bgmeaFactoryId;
            }
            if (bkmeaFactoryId != null && bkmeaFactoryId != 0)
            {
                querySt += " and o.beneficiary.factory.id = " + bkmeaFactoryId;
            }
            if (divisionId != null)
            {
                querySt += " AND o.beneficiary.presentDivision.id = " + divisionId;
            }
            if (districtId != null)
            {
                querySt += " AND o.beneficiary.presentDistrict.id = " + districtId;
            }
            if (upazilaId != null)
            {
                querySt += " AND o.beneficiary.presentUpazila.id = " + upazilaId;
            }
            if (unionId != null)
            {
                querySt += " AND o.beneficiary.presentUnion.id = " + unionId;
            }
            querySt += " and o.grievanceStatus.id in (select id from GrievanceStatus gs "
                    + " where gs.displayOrder < (select max(displayOrder) from GrievanceStatus))";
            List<GrievanceListData> list = this.sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt).setFirstResult(beginIndex).setMaxResults(pageSize).list();
            long count = (Long) this.sessionFactory.getCurrentSession().createQuery(countQuerySt + querySt).list().get(0);
            List<Object> result = new ArrayList<Object>();
            result.add(list);
            result.add(count);
            result.add(count); // ????

            return result;
        }
        catch (Exception e)
        {
            System.out.println("exc=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

//    @Override
//    public List<Object> getGrievanceListBySearchParameter(Map parameter, int beginIndex, int pageSize)
//    {
//        try
//        {
//            String nid = (String) parameter.get("nid");
//            Integer grievanceType = (Integer) parameter.get("grievanceType");
//            Integer grievanceStatus = (Integer) parameter.get("grievanceStatus");
//            Integer divisionId = (Integer) parameter.get("divisionId");
//            Integer districtId = (Integer) parameter.get("districtId");
//            Integer upazilaId = (Integer) parameter.get("upazilaId");
//            Integer unionId = (Integer) parameter.get("unionId");
//
//            String mainQuerySt = "from Grievance o where 0=0 ";
//            String countQuerySt = "select count(o.id) from Grievance o where 0=0 ";
//
//            String querySt = "";
//
//            if (nid != null)
//            {
//                querySt += " AND o.beneficiary.nid like '%" + CommonUtility.getNumberInEnglish(nid) + "%'";
//            }
//            if (grievanceType != null)
//            {
//                querySt += " and o.grievanceType.id = " + grievanceType;
//            }
//            if (grievanceStatus != null)
//            {
//                querySt += " and o.grievanceStatus = " + grievanceStatus;
//            }
//            if (divisionId != null)
//            {
//                querySt += " AND o.beneficiary.permanentDivision.id = " + divisionId;
//            }
//            if (districtId != null)
//            {
//                querySt += " AND o.beneficiary.permanentDistrict.id = " + districtId;
//            }
//            if (upazilaId != null)
//            {
//                querySt += " AND o.beneficiary.permanentUpazila.id = " + upazilaId;
//            }
//            if (unionId != null)
//            {
//                querySt += " AND o.beneficiary.permanentUnion.id = " + unionId;
//            }
//            querySt += " and o.grievanceStatus.id in (select id from GrievanceStatus gs "
//                    + " where gs.displayOrder < (select max(displayOrder) from GrievanceStatus))";
//            List<Grievance> list = this.sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt).setFirstResult(beginIndex).setMaxResults(pageSize).list();
//            long count = (Long) this.sessionFactory.getCurrentSession().createQuery(countQuerySt + querySt).list().get(0);
//            List<Object> result = new ArrayList<Object>();
//            result.add(list);
//            result.add(count);
//            result.add(count); // ????
//
//            return result;
//        }
//        catch (Exception e)
//        {
//            System.out.println("exc=" + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
}

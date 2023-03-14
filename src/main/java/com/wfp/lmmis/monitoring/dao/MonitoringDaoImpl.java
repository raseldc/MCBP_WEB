package com.wfp.lmmis.monitoring.dao;

import com.wfp.lmmis.monitoring.model.Monitoring;
import com.wfp.lmmis.utility.DateUtilities;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MonitoringDaoImpl implements MonitoringDao
{

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Monitoring getMonitoring(Integer id)
    {
        Monitoring monitoring = (Monitoring) getCurrentSession().get(Monitoring.class, id);
        return monitoring;
    }

    @Override
    public void save(Monitoring monitoring)
    {
        try
        {
            this.getCurrentSession().save(monitoring);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void edit(Monitoring editedMonitoring)
    {
        Monitoring monitoring = (Monitoring) sessionFactory.getCurrentSession().get(Monitoring.class, editedMonitoring.getId());
        monitoring.setScheme(editedMonitoring.getScheme());
        monitoring.setOfficerName(editedMonitoring.getOfficerName());
        monitoring.setDesignation(editedMonitoring.getDesignation());
        monitoring.setMonitoringDate(editedMonitoring.getMonitoringDate());
        monitoring.setDurationDay(editedMonitoring.getDurationDay());
        monitoring.setPurpose(editedMonitoring.getPurpose());
        monitoring.setFindings(editedMonitoring.getFindings());
        monitoring.setDivision(editedMonitoring.getDivision());
        monitoring.setDistrict(editedMonitoring.getDistrict());
        monitoring.setUpazilla(editedMonitoring.getUpazilla());
        monitoring.setUnion(editedMonitoring.getUnion());
        monitoring.setModifiedBy(editedMonitoring.getModifiedBy());
        monitoring.setModificationDate(editedMonitoring.getModificationDate());
        try
        {
            this.getCurrentSession().update(monitoring);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Monitoring> getMonitoringList()
    {
        @SuppressWarnings("unchecked")
        List<Monitoring> list = sessionFactory.getCurrentSession().createQuery("from Monitoring").list();
        return list;
    }

    @Override
    public void delete(Integer id)
    {
        Monitoring monitoring = (Monitoring) sessionFactory.getCurrentSession().get(Monitoring.class, id);
        if (monitoring != null)
        {
            sessionFactory.getCurrentSession().delete(monitoring);
        }
    }

    @Override
    public List<Monitoring> getMonitoringReportData(Map parameter)
    {
        Integer schemeId = (Integer) parameter.get("schemeId");
//        Integer fiscalYearId = (Integer) parameter.get("fiscalYearId");
        String query = "select t from Monitoring t where 0 = 0 ";
        if (schemeId != null)
        {
            query += " and t.scheme.id=" + schemeId;
        }
        List<Monitoring> monitorings = this.sessionFactory.getCurrentSession().createQuery(query).list();
        return monitorings;
    }
    
    @Override
    public List<Object> getMonitoringListBySearchParameter(Map parameter, int offset, int numofRecords)
    {
        try
        {
//            Integer schemeId = !parameter.get("schemeId").equals("") ? (Integer) parameter.get("schemeId") : null;
            Integer purposeId = parameter.get("purposeId") != null ? (Integer) parameter.get("purposeId") : null;

            @SuppressWarnings("unchecked")
            String mainQuerySt = "from Monitoring o where 0=0 ";
            String countQuerySt = "select count(distinct o.id) from Monitoring o where 0=0 ";

            String querySt = "";

//            if (schemeId != null && schemeId != 0)
//            {
//                querySt += " AND o.scheme.id = " + schemeId;
//            }
            if (purposeId != null && purposeId != 0)
            {
                querySt += " AND o.purpose.id = " + purposeId;
            }
            List<Monitoring> list = sessionFactory.getCurrentSession().createQuery(mainQuerySt + querySt).setFirstResult(offset).setMaxResults(numofRecords).list();
            long count = (Long) sessionFactory.getCurrentSession().createQuery(countQuerySt + querySt).list().get(0);
            System.out.println("Monitoring size = " + list.size());
            System.out.println("Monitoring total = " + count);

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

}

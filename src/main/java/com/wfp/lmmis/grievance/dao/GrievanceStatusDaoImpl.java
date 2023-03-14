package com.wfp.lmmis.grievance.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.model.GrievanceStatus;
import com.wfp.lmmis.model.ChangeLog;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GrievanceStatusDaoImpl implements GrievanceStatusDao
{

    //private static final logger logger = //logger.getlogger(GrievanceStatusDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public GrievanceStatus getGrievanceStatus(Integer id)
    {
        GrievanceStatus grievanceStatus = (GrievanceStatus) getCurrentSession().get(GrievanceStatus.class, id);
        return grievanceStatus;
    }

    @Override
    public GrievanceStatus getGrievanceStatus(int displayOrder)
    {
        GrievanceStatus grievanceStatus = (GrievanceStatus) getCurrentSession().createQuery("from GrievanceStatus where displayOrder=" + displayOrder).uniqueResult();
        return grievanceStatus;
    }

    @Override
    public void save(GrievanceStatus grievanceStatus)
    {
        this.getCurrentSession().save(grievanceStatus);
    }

    /**
     *
     * @param grievanceStatus
     * @throws ExceptionWrapper
     */
    @Override
    public void edit(GrievanceStatus grievanceStatus) throws ExceptionWrapper
    {
        try
        {
            GrievanceStatus dBGrievanceStatus = getGrievanceStatus(grievanceStatus.getId());
            grievanceStatus.setCreatedBy(dBGrievanceStatus.getCreatedBy());
            grievanceStatus.setCreationDate(dBGrievanceStatus.getCreationDate());
            this.getCurrentSession().merge(grievanceStatus);
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.GrievanceStatus, grievanceStatus.getId(), grievanceStatus.getModifiedBy(), grievanceStatus.getModificationDate(), grievanceStatus.getNameInBangla() + " status:" + dBGrievanceStatus.isActive(), grievanceStatus.getNameInBangla() + " status:" + grievanceStatus.isActive());
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            //logger.error(e.getMessage());
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    /**
     *
     * @param grievanceStatus
     * @throws ExceptionWrapper
     */
    @Override
    public void delete(GrievanceStatus grievanceStatus) throws ExceptionWrapper
    {
        try
        {
            Query q = this.getCurrentSession().createQuery("update GrievanceStatus set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id=" + grievanceStatus.getId());
            q.setParameter("modifiedBy", grievanceStatus.getModifiedBy());
            q.setParameter("modificationDate", grievanceStatus.getModificationDate());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.GrievanceStatus, grievanceStatus.getId(), grievanceStatus.getModifiedBy(), grievanceStatus.getModificationDate(), grievanceStatus.getNameInEnglish(), null);
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            //logger.error(e.getMessage());
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<GrievanceStatus> getGrievanceStatusList()
    {
        @SuppressWarnings("unchecked")
        List<GrievanceStatus> list = sessionFactory.getCurrentSession().createQuery("from GrievanceStatus where deleted=0").list();
        return list;
    }

    @Override
    public List<GrievanceStatus> getGrievanceStatusList(Integer lowMarkStatusId)
    {
        @SuppressWarnings("unchecked")
        List<GrievanceStatus> list = sessionFactory.getCurrentSession().createQuery("from GrievanceStatus where id>:thresholdId ").setParameter("thresholdId", lowMarkStatusId).list();
        return list;
    }
    
    @Override
    public List<GrievanceStatus> getUnResolvedGrievanceStatusList()
    {
        @SuppressWarnings("unchecked")
        List<GrievanceStatus> list = sessionFactory.getCurrentSession().createQuery("from GrievanceStatus where displayOrder != (select max(displayOrder) from GrievanceStatus) ").list();
        return list;
    }

}

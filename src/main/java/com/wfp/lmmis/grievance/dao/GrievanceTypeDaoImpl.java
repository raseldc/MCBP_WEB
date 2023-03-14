package com.wfp.lmmis.grievance.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.model.GrievanceType;
import com.wfp.lmmis.model.ChangeLog;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author rasel
 */
@Repository
public class GrievanceTypeDaoImpl implements GrievanceTypeDao
{

    //private static final logger logger = //logger.getlogger(GrievanceTypeDaoImpl.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public GrievanceType getGrievanceType(Integer id)
    {
        GrievanceType grievanceType = (GrievanceType) getCurrentSession().get(GrievanceType.class, id);
        return grievanceType;
    }

    @Override
    public void save(GrievanceType grievanceType)
    {
        this.getCurrentSession().save(grievanceType);
    }

    @Override
    public void edit(GrievanceType grievanceType) throws ExceptionWrapper
    {
        try
        {
            GrievanceType dBGrievanceType = getGrievanceType(grievanceType.getId());
            grievanceType.setCreatedBy(dBGrievanceType.getCreatedBy());
            grievanceType.setCreationDate(dBGrievanceType.getCreationDate());
            this.getCurrentSession().merge(grievanceType);
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.GrievanceType, grievanceType.getId(), grievanceType.getModifiedBy(), grievanceType.getModificationDate(), grievanceType.getNameInBangla() + " status:" + dBGrievanceType.isActive(), grievanceType.getNameInBangla() + " status:" + grievanceType.isActive());
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
     * @param grievanceType
     * @throws ExceptionWrapper
     */
    @Override
    public void delete(GrievanceType grievanceType) throws ExceptionWrapper
    {
        try
        {
            Query q = this.getCurrentSession().createQuery("update GrievanceType set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id=" + grievanceType.getId());
            q.setParameter("modifiedBy", grievanceType.getModifiedBy());
            q.setParameter("modificationDate", grievanceType.getModificationDate());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.GrievanceType, grievanceType.getId(), grievanceType.getModifiedBy(), grievanceType.getModificationDate(), grievanceType.getNameInEnglish(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            //logger.error(e.getMessage());
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<GrievanceType> getGrievanceTypeList()
    {
        @SuppressWarnings("unchecked")
        List<GrievanceType> list = sessionFactory.getCurrentSession().createQuery("from GrievanceType where deleted=0").list();
        return list;
    }

    @Override
    public List<GrievanceType> getActiveGrievanceTypeList()
    {
        @SuppressWarnings("unchecked")
        List<GrievanceType> list = sessionFactory.getCurrentSession().createQuery("from GrievanceType where deleted=0 and active=1").list();
        return list;
    }

}

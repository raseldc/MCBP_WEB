package com.wfp.lmmis.monitoring.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.monitoring.model.Purpose;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PurposeDaoImpl implements PurposeDao
{

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Purpose getPurpose(Integer id)
    {
        Purpose purpose = (Purpose) getCurrentSession().get(Purpose.class, id);
        return purpose;
    }

    @Override
    public void save(Purpose purpose)
    {
        this.getCurrentSession().save(purpose);
    }

    /**
     *
     * @param editedPurpose
     */
    @Override
    public void edit(Purpose editedPurpose)
    {
        Purpose purpose = (Purpose) sessionFactory.getCurrentSession().get(Purpose.class, editedPurpose.getId());
        purpose.setNameInBangla(editedPurpose.getNameInBangla());
        purpose.setNameInEnglish(editedPurpose.getNameInEnglish());
        purpose.setActive(editedPurpose.isActive());
        purpose.setDescription(editedPurpose.getDescription());
        purpose.setModifiedBy(editedPurpose.getModifiedBy());
        purpose.setModificationDate(editedPurpose.getModificationDate());
        this.getCurrentSession().update(purpose);
    }

    @Override
    public List<Purpose> getPurposeList(boolean activeOnly)
    {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if (activeOnly == true)
        {
            querySt = "from Purpose o where o.deleted=0 and o.active=1";
        }
        else
        {
            querySt = "from Purpose o where o.deleted=0";
        }
        List<Purpose> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        return list;
    }

    @Override
    public void delete(Purpose purpose)
    {
       try
        {
            Query q = this.getCurrentSession().createQuery("update Purpose set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id=" + purpose.getId());
            q.setParameter("modifiedBy", purpose.getModifiedBy());
            q.setParameter("modificationDate", purpose.getModificationDate());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Purpose, purpose.getId(), purpose.getModifiedBy(), purpose.getModificationDate(), purpose.getNameInEnglish(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

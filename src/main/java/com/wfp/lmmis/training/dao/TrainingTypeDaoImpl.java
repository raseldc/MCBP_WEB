package com.wfp.lmmis.training.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.training.model.TrainingType;

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
public class TrainingTypeDaoImpl implements TrainingTypeDao
{

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public TrainingType getTrainingType(Integer id)
    {
        TrainingType trainingType = (TrainingType) getCurrentSession().get(TrainingType.class, id);
        return trainingType;
    }

    @Override
    public void save(TrainingType trainingType)
    {
        this.getCurrentSession().save(trainingType);
    }

    @Override
    public void edit(TrainingType editedTrainingType)
    {
        TrainingType trainingType = (TrainingType) sessionFactory.getCurrentSession().get(TrainingType.class, editedTrainingType.getId());
        trainingType.setNameInBangla(editedTrainingType.getNameInBangla());
        trainingType.setNameInEnglish(editedTrainingType.getNameInEnglish());
        trainingType.setActive(editedTrainingType.isActive());
        trainingType.setBeneficiaryIncluded(editedTrainingType.isBeneficiaryIncluded());
        trainingType.setDescription(editedTrainingType.getDescription());
        trainingType.setModifiedBy(editedTrainingType.getModifiedBy());
        trainingType.setModificationDate(editedTrainingType.getModificationDate());
        this.getCurrentSession().update(trainingType);
    }

    @Override
    public List<TrainingType> getTrainingTypeList(boolean activeOnly)
    {
        @SuppressWarnings("unchecked")
        String querySt = null;
        if(activeOnly == true)
        {
            querySt = "from TrainingType o where o.deleted=0 and o.active=1";
        }
        else
        {
            querySt = "from TrainingType o where o.deleted=0";
        }
        List<TrainingType> list = sessionFactory.getCurrentSession().createQuery(querySt).list();
        return list;
    }

    @Override
    public void delete(TrainingType trainingType)
    {
        try
        {
            Query q = this.getCurrentSession().createQuery("update TrainingType set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id=" + trainingType.getId());
            q.setParameter("modifiedBy", trainingType.getModifiedBy());
            q.setParameter("modificationDate", trainingType.getModificationDate());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Training_Type, trainingType.getId(), trainingType.getModifiedBy(), trainingType.getModificationDate(), trainingType.getNameInEnglish(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

package com.wfp.lmmis.training.dao;

import com.wfp.lmmis.training.model.Trainer;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDaoImpl implements TrainerDao
{

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Trainer getTrainer(Integer id)
    {
        Trainer trainer = (Trainer) getCurrentSession().get(Trainer.class, id);
        return trainer;
    }

    @Override
    public Integer save(Trainer trainer)
    {
        return (Integer) this.getCurrentSession().save(trainer);
    }

    @Override
    public void edit(Trainer editedTrainer)
    {
        Trainer trainer = (Trainer) sessionFactory.getCurrentSession().get(Trainer.class, editedTrainer.getId());
        trainer.setNameInBangla(editedTrainer.getNameInBangla());
        trainer.setNameInEnglish(editedTrainer.getNameInEnglish());
        trainer.setOrganizationNumber(editedTrainer.getOrganizationNumber());
        trainer.setContactPersonName(editedTrainer.getContactPersonName());
        trainer.setPhotoPath(editedTrainer.getPhotoPath());
        trainer.setModifiedBy(editedTrainer.getModifiedBy());
        trainer.setModificationDate(editedTrainer.getModificationDate());
        this.getCurrentSession().update(trainer);
    }

    @Override
    public List<Trainer> getTrainerList()
    {
        @SuppressWarnings("unchecked")
        List<Trainer> list = sessionFactory.getCurrentSession().createQuery("from Trainer").list();
        return list;
    }
    
    @Override
    public void delete(Integer id)
    {
        Trainer trainer = (Trainer) sessionFactory.getCurrentSession().get(Trainer.class, id);
        if (trainer != null)
        {
            sessionFactory.getCurrentSession().delete(trainer);
        }
    }

}

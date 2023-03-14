package com.wfp.lmmis.training.service;

import com.wfp.lmmis.training.dao.TrainerDao;
import com.wfp.lmmis.training.model.Trainer;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService
{

    @Autowired
    private TrainerDao trainerDao;

    @Override
    public Trainer getTrainer(Integer id)
    {
        return trainerDao.getTrainer(id);
    }

    @Override
    public Integer save(Trainer trainer)
    {
        return this.trainerDao.save(trainer);
    }

    @Override
    public void edit(Trainer trainer)
    {
        this.trainerDao.edit(trainer);
    }

    @Override
    public List<Trainer> getTrainerList()
    {
        return this.trainerDao.getTrainerList();
    }
    
    @Override
    public void delete(Integer id)
    {
        this.trainerDao.delete(id);
    }

}

package com.wfp.lmmis.training.service;

import com.wfp.lmmis.training.dao.TrainingTypeDao;
import com.wfp.lmmis.training.model.TrainingType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrainingTypeServiceImpl implements TrainingTypeService
{

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Override
    public TrainingType getTrainingType(Integer id)
    {
        return trainingTypeDao.getTrainingType(id);
    }

    @Override
    public void save(TrainingType trainingType)
    {
        this.trainingTypeDao.save(trainingType);
    }

    @Override
    public void edit(TrainingType trainingType)
    {
        this.trainingTypeDao.edit(trainingType);
    }

    @Override
    public List<TrainingType> getTrainingTypeList(boolean activeOnly)
    {
        return this.trainingTypeDao.getTrainingTypeList(activeOnly);
    }
    
    @Override
    public void delete(TrainingType trainingType)
    {
        this.trainingTypeDao.delete(trainingType);
    }

}

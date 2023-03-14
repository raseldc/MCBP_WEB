/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.service;

import com.wfp.lmmis.training.dao.TrainingAttendeeDao;
import com.wfp.lmmis.training.model.TrainingAttendee;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Service
@Transactional
public class TrainingAttendeeServiceImpl implements TrainingAttendeeService
{

    @Autowired
    TrainingAttendeeDao trainingAttendeeDao;
    
    /**
     *
     * @param id
     * @return
     */
    public TrainingAttendee getTrainingAttendee(Integer id)
    {
        return this.trainingAttendeeDao.getTrainingAttendee(id);
    }

    public void save(TrainingAttendee trainingAttendee)
    {
        this.trainingAttendeeDao.save(trainingAttendee);
    }

    public void edit(TrainingAttendee trainingAttendee)
    {
        this.trainingAttendeeDao.edit(trainingAttendee);
    }

    public List<TrainingAttendee> getTrainingAttendeeList()
    {
        return this.trainingAttendeeDao.getTrainingAttendeeList();
    }

    public void delete(Integer id)
    {
        this.trainingAttendeeDao.delete(id);
    }
}

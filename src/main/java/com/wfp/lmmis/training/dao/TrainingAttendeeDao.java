/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.dao;

import com.wfp.lmmis.training.model.TrainingAttendee;
import java.util.List;

/**
 *
 * @author user
 */
public interface TrainingAttendeeDao
{

    public TrainingAttendee getTrainingAttendee(Integer id);

    public void save(TrainingAttendee trainingAttendee);

    public void edit(TrainingAttendee trainingAttendee);

    public List<TrainingAttendee> getTrainingAttendeeList();

    /**
     *
     * @param id
     */
    public void delete(Integer id);

}

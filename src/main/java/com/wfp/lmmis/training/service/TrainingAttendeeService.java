/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.service;

import com.wfp.lmmis.training.model.TrainingAttendee;
import java.util.List;

/**
 *
 * @author Rezwan
 */
public interface TrainingAttendeeService
{
    public TrainingAttendee getTrainingAttendee(Integer id);

    public void save(TrainingAttendee trainingAttendee);

    public void edit(TrainingAttendee trainingAttendee);

    public List<TrainingAttendee> getTrainingAttendeeList();
    
    public void delete(Integer id);
}

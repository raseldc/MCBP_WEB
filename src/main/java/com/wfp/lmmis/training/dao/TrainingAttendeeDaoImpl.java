/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.dao;

import com.wfp.lmmis.training.model.TrainingAttendee;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Rezwan
 */
@Repository
public class TrainingAttendeeDaoImpl implements TrainingAttendeeDao
{

    /**
     *
     * @param id
     * @return
     */
    public TrainingAttendee getTrainingAttendee(Integer id)
    {
        return null;
    }

    public void save(TrainingAttendee trainingAttendee)
    {
        //save
    }

    /**
     *
     * @param trainingAttendee
     */
    public void edit(TrainingAttendee trainingAttendee)
    {
        //edit
    }

    /**
     *
     * @return
     */
    public List<TrainingAttendee> getTrainingAttendeeList()
    {

        return null;
    }

    public void delete(Integer id)
    {
        //delete
    }
}

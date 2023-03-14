/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.dao;

import com.wfp.lmmis.training.model.TrainingType;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface TrainingTypeDao
{

    /**
     *
     * @param id
     * @return
     */
    public TrainingType getTrainingType(Integer id);

    public void save(TrainingType trainingType);

    public void edit(TrainingType trainingType);

    /**
     *
     * @param activeOnly
     * @return
     */
    public List<TrainingType> getTrainingTypeList(boolean activeOnly);
    
    /**
     *
     * @param trainingType
     */
    public void delete(TrainingType trainingType);
}

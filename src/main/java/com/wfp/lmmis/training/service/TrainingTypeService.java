/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.service;

import com.wfp.lmmis.training.model.TrainingType;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface TrainingTypeService
{

    public TrainingType getTrainingType(Integer id);

    /**
     *
     * @param trainingType
     */
    public void save(TrainingType trainingType);

    /**
     *
     * @param trainingType
     */
    public void edit(TrainingType trainingType);

    public List<TrainingType> getTrainingTypeList(boolean activeOnly);
    
    public void delete(TrainingType trainingType);
}

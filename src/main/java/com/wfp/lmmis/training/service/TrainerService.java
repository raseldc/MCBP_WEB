/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.service;

import com.wfp.lmmis.training.model.Trainer;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface TrainerService
{

    public Trainer getTrainer(Integer id);

    public Integer save(Trainer trainer);

    public void edit(Trainer trainer);

    /**
     *
     * @return
     */
    public List<Trainer> getTrainerList();
    
    public void delete(Integer id);
}

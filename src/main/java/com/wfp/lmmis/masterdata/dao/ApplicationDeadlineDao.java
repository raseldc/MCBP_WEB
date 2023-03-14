/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.masterdata.model.ApplicationDeadline;
import java.util.Calendar;

/**
 *
 * @author Philip
 */
public interface ApplicationDeadlineDao
{

    public ApplicationDeadline getApplicationDeadline(Integer id);

    public void save(ApplicationDeadline applicationDeadline);

    public Calendar getApplicationDeadlineByUpazila(Integer upazilaId, Integer fiscalYearId, Integer schemeId);

}

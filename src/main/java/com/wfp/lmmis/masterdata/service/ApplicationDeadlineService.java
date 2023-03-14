/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.forms.ApplicationDeadlineForm;
import com.wfp.lmmis.masterdata.model.ApplicationDeadline;
import java.util.Calendar;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Philip
 */
public interface ApplicationDeadlineService
{

    public ApplicationDeadline getApplicationDeadline(Integer id);

    public void saveApplicationDeadline(ApplicationDeadlineForm applicationDeadlineForm, HttpSession session);

    public Calendar getApplicationDeadlineByUpazila(Integer upazilaId, Integer fiscalYearId, Integer schemeId);
}

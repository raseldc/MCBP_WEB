/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.service;

import com.wfp.lmmis.masterdata.dao.ApplicationDeadlineDao;
import com.wfp.lmmis.masterdata.forms.ApplicationDeadlineForm;
import com.wfp.lmmis.masterdata.model.ApplicationDeadline;
import com.wfp.lmmis.rm.model.User;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Philip
 */
@Service
@Transactional
public class ApplicationDeadlineServiceImpl implements ApplicationDeadlineService
{

    @Autowired
    private ApplicationDeadlineDao applicationDeadlineDao;

    @Override
    public ApplicationDeadline getApplicationDeadline(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveApplicationDeadline(ApplicationDeadlineForm applicationDeadlineForm, HttpSession session)
    {
        List<ApplicationDeadline> list = applicationDeadlineForm.getApplicationDeadlineList();
        for (ApplicationDeadline applicationDeadline : list)
        {
            applicationDeadline.setScheme(applicationDeadlineForm.getScheme());
            applicationDeadline.setFiscalYear(applicationDeadlineForm.getFiscalYear());
            applicationDeadline.setDistrict(applicationDeadlineForm.getDistrict());
            applicationDeadline.setUpazila(applicationDeadline.getUpazila());
            applicationDeadline.setCreatedBy((User) session.getAttribute("user"));
            applicationDeadline.setCreationDate(Calendar.getInstance());
            this.applicationDeadlineDao.save(applicationDeadline);
        }
    }

    @Override
    public Calendar getApplicationDeadlineByUpazila(Integer upazilaId, Integer fiscalYearId, Integer schemeId)
    {
        return this.applicationDeadlineDao.getApplicationDeadlineByUpazila(upazilaId, fiscalYearId, schemeId);
    }

}

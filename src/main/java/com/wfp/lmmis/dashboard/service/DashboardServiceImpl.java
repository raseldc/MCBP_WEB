/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dashboard.service;

import com.wfp.lmmis.dashboard.dao.DashboardDao;
import com.wfp.lmmis.model.UserDetail;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Philip
 */
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardDao dashboardDao;

    @Override
    public Map getDashBoardData(Integer schemeId, Integer fiscalYear, UserDetail user) {
        return this.dashboardDao.getDashBoardData(schemeId, fiscalYear, user);
    }

    @Override
    public Map getPayrollByCycle(Integer schemeId, Integer fiscalYearId, Locale locale) {
        return this.dashboardDao.getPayrollByCycle(schemeId, fiscalYearId, locale);
    }

    @Override
    public Map getPayrollByFiscalYear(Integer schemeId, Locale locale) {
        return this.dashboardDao.getPayrollByFiscalYear(schemeId, locale);
    }

    @Override
    public List<Object> getQuotaVsBenByDivision(Integer schemeId, Integer fiscalYearId, Locale locale) {
        return this.dashboardDao.getQuotaVsBenByDivision(schemeId, fiscalYearId, locale);
    }

    @Override
    public List<Object> getBenByDivision(boolean isLma, Integer schemeId, Integer fiscalYearId, Locale locale) {
        return this.dashboardDao.getBenByDivision(isLma, schemeId, fiscalYearId, locale);
    }

    @Override
    public List<Object> getQuotaVsBenByUnion(Integer schemeId, Integer fiscalYearId, Locale locale, Integer upazilaId) {
        return this.dashboardDao.getQuotaVsBenByUnion(schemeId, fiscalYearId, locale, upazilaId);
    }

    @Override
    public Map getApplicantByMonth(Integer schemeId, Integer fiscalYearId, Locale locale, UserDetail user) {
        return this.dashboardDao.getApplicantByMonth(schemeId, fiscalYearId, locale, user);
    }

    @Override
    public Map getTrainingByFiscalYear(Integer schemeId, Locale locale) {
        return this.dashboardDao.getTrainingByFiscalYear(schemeId, locale);
    }

    public Map getDashBoardDataUpdate(Integer schemeId, Integer fiscalYearId, UserDetail user) {
        return this.dashboardDao.getDashBoardDataUPdate(schemeId, fiscalYearId, user);
    }
}

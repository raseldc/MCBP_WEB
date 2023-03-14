/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dashboard.service;

import com.wfp.lmmis.model.UserDetail;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Philip
 */
public interface DashboardService
{

    public Map getDashBoardData(Integer schemeId, Integer fiscalYear, UserDetail user);

    public Map getPayrollByCycle(Integer schemeId, Integer fiscalYearId, Locale locale);
    
    /**
     *
     * @param schemeId
     * @param locale
     * @return
     */
    public Map getPayrollByFiscalYear(Integer schemeId, Locale locale);
    
    public List<Object> getQuotaVsBenByDivision(Integer schemeId, Integer fiscalYearId, Locale locale);
    
    public List<Object> getBenByDivision(boolean isLma, Integer schemeId, Integer fiscalYearId, Locale locale);
    
    public List<Object> getQuotaVsBenByUnion(Integer schemeId, Integer fiscalYearId, Locale locale, Integer upazilaId);
    
    /**
     *
     * @param schemeId
     * @param fiscalYearId
     * @param locale
     * @param user
     * @return
     */
    public Map getApplicantByMonth(Integer schemeId, Integer fiscalYearId, Locale locale, UserDetail user);
    
    public Map getTrainingByFiscalYear(Integer schemeId, Locale locale);
    
    public Map getDashBoardDataUpdate(Integer schemeId, Integer fiscalYearId, UserDetail user);
}

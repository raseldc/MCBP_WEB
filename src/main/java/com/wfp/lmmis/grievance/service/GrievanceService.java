/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.grievance.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.model.Grievance;
import com.wfp.lmmis.report.data.GrievanceReportData;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Philip
 */
public interface GrievanceService
{

    public Grievance getGrievance(Integer id);    

    public void save(Grievance role);

    public void edit(Grievance role);
    
    /**
     *
     * @param grievance
     * @throws ExceptionWrapper
     */
    public void delete(Grievance grievance) throws ExceptionWrapper;

    public List<Grievance> getGrievanceList();
    
    public List<GrievanceReportData> getGrievanceReportData(Map parameter);
    
    /**
     *
     * @param parameter
     * @param beginIndex
     * @param pageSize
     * @return
     */
    public List<Object> getGrievanceListBySearchParameter(Map parameter, int beginIndex, int pageSize);
}

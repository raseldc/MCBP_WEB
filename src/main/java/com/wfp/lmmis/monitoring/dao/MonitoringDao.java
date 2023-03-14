/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.monitoring.dao;

import com.wfp.lmmis.monitoring.model.Monitoring;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Philip
 */
public interface MonitoringDao
{

    /**
     *
     * @param id
     * @return
     */
    public Monitoring getMonitoring(Integer id);

    /**
     *
     * @param monitoring
     */
    public void save(Monitoring monitoring);

    public void edit(Monitoring monitoring);

    public List<Monitoring> getMonitoringList();
    
    public void delete(Integer id);
    
    public List<Monitoring> getMonitoringReportData(Map parameter);
    
    /**
     *
     * @param parameter
     * @param offset
     * @param numofRecords
     * @return
     */
    public List<Object> getMonitoringListBySearchParameter(Map parameter, int offset, int numofRecords);
}

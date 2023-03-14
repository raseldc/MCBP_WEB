package com.wfp.lmmis.monitoring.service;

import com.wfp.lmmis.monitoring.dao.MonitoringDao;
import com.wfp.lmmis.monitoring.model.Monitoring;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MonitoringServiceImpl implements MonitoringService
{

    @Autowired
    private MonitoringDao monitoringDao;

    @Override
    public Monitoring getMonitoring(Integer id)
    {
        return monitoringDao.getMonitoring(id);
    }

    @Override
    public void save(Monitoring monitoring)
    {
        this.monitoringDao.save(monitoring);
    }

    @Override
    public void edit(Monitoring monitoring)
    {
        this.monitoringDao.edit(monitoring);
    }

    /**
     *
     * @return
     */
    @Override
    public List<Monitoring> getMonitoringList()
    {
        return this.monitoringDao.getMonitoringList();
    }
    
    @Override
    public void delete(Integer id)
    {
        this.monitoringDao.delete(id);
    }
    
    @Override
    public List<Monitoring> getMonitoringReportData(Map parameter)
    {
        return this.monitoringDao.getMonitoringReportData(parameter);
    }
    
    @Override
    public List<Object> getMonitoringListBySearchParameter(Map parameter, int offset, int numofRecords)
    {
        return this.monitoringDao.getMonitoringListBySearchParameter(parameter, offset, numofRecords);
    }
}

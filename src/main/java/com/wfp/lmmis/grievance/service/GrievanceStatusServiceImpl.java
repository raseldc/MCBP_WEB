package com.wfp.lmmis.grievance.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.dao.GrievanceStatusDao;
import com.wfp.lmmis.grievance.model.GrievanceStatus;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GrievanceStatusServiceImpl implements GrievanceStatusService
{

    @Autowired
    private GrievanceStatusDao grievanceStatusDao;

    @Override
    public GrievanceStatus getGrievanceStatus(Integer id)
    {
        return grievanceStatusDao.getGrievanceStatus(id);
    }

    @Override
    public GrievanceStatus getGrievanceStatus(int displayOrder)
    {
        return grievanceStatusDao.getGrievanceStatus(displayOrder);
    }

    @Override
    public void save(GrievanceStatus grievanceStatus)
    {
        this.grievanceStatusDao.save(grievanceStatus);
    }

    @Override
    public void edit(GrievanceStatus grievanceStatus)throws ExceptionWrapper
    {
        this.grievanceStatusDao.edit(grievanceStatus);
    }

    @Override
    public void delete(GrievanceStatus grievanceStatus) throws ExceptionWrapper
    {
        this.grievanceStatusDao.delete(grievanceStatus);
    }

    @Override
    public List<GrievanceStatus> getGrievanceStatusList()
    {
        return this.grievanceStatusDao.getGrievanceStatusList();
    }

    @Override
    public List<GrievanceStatus> getGrievanceStatusList(Integer lowMarkStatusId)
    {
        return this.grievanceStatusDao.getGrievanceStatusList(lowMarkStatusId);
    }
    
    @Override
    public List<GrievanceStatus> getUnResolvedGrievanceStatusList()
    {
        return this.grievanceStatusDao.getUnResolvedGrievanceStatusList();
    }

}

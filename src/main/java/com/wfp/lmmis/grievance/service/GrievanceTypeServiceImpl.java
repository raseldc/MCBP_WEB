package com.wfp.lmmis.grievance.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.dao.GrievanceTypeDao;
import com.wfp.lmmis.grievance.model.GrievanceType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GrievanceTypeServiceImpl implements GrievanceTypeService
{

    @Autowired
    private GrievanceTypeDao grievanceTypeDao;

    @Override
    public GrievanceType getGrievanceType(Integer id)
    {
        return grievanceTypeDao.getGrievanceType(id);
    }

    /**
     *
     * @param grievanceType
     */
    @Override
    public void save(GrievanceType grievanceType)
    {
        this.grievanceTypeDao.save(grievanceType);
    }

    @Override
    public void edit(GrievanceType grievanceType)throws ExceptionWrapper
    {
        this.grievanceTypeDao.edit(grievanceType);
    }

    @Override
    public void delete(GrievanceType grievanceType)throws ExceptionWrapper
    {
        this.grievanceTypeDao.delete(grievanceType);
    }
    
    @Override
    public List<GrievanceType> getGrievanceTypeList()
    {
        return this.grievanceTypeDao.getGrievanceTypeList();
    }

    /**
     *
     * @return
     */
    @Override
    public List<GrievanceType> getActiveGrievanceTypeList()
    {
        return this.grievanceTypeDao.getActiveGrievanceTypeList();
    }

}

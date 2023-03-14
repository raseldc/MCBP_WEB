package com.wfp.lmmis.monitoring.service;

import com.wfp.lmmis.monitoring.dao.PurposeDao;
import com.wfp.lmmis.monitoring.model.Purpose;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author rasel
 */
@Service
@Transactional
public class PurposeServiceImpl implements PurposeService
{

    @Autowired
    private PurposeDao purposeDao;

    @Override
    public Purpose getPurpose(Integer id)
    {
        return purposeDao.getPurpose(id);
    }

    @Override
    public void save(Purpose purpose)
    {
        this.purposeDao.save(purpose);
    }

    @Override
    public void edit(Purpose purpose)
    {
        this.purposeDao.edit(purpose);
    }

    @Override
    public List<Purpose> getPurposeList(boolean activeOnly)
    {
        return this.purposeDao.getPurposeList(activeOnly);
    }
    
    /**
     *
     * @param purpose
     */
    @Override
    public void delete(Purpose purpose)
    {
        this.purposeDao.delete(purpose);
    }

}

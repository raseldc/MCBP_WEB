package com.wfp.lmmis.rm.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.rm.dao.RoleDao;
import com.wfp.lmmis.rm.model.Role;
import com.wfp.lmmis.utility.ItemObject;
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
public class RoleServiceImpl implements RoleService
{

    @Autowired
    private RoleDao roleDao;

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Role getRole(int id)
    {
        return roleDao.getRole(id);
    }

    @Override
    public void Save(Role role)
    {
        this.roleDao.Save(role);

    }

    @Override
    public void Edit(Role role) throws ExceptionWrapper
    {
        this.roleDao.Edit(role);

    }

    @Override
    public void delete(Role role) throws ExceptionWrapper
    {
        this.roleDao.delete(role);
    }

    @Override
    public List<Role> getRoleList(Integer schemeId)
    {
        return this.roleDao.getRoleList(schemeId);
    }
    
    @Override
    public List<Role> getRoleList()
    {
        return this.roleDao.getRoleList();
    }
    
    @Override
    public List<ItemObject> getRoleByScheme(Integer schemeId)
    {
        return this.roleDao.getRoleByScheme(schemeId);
    }

}

package com.wfp.lmmis.rm.dao;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.rm.model.Role;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author rasel
 */
public interface RoleDao
{

    public Role getRole(int id);

    public void Save(Role role);

    public void Edit(Role role) throws ExceptionWrapper;

    /**
     *
     * @param role
     * @throws ExceptionWrapper
     */
    public void delete(Role role) throws ExceptionWrapper;

    public List<Role> getRoleList(Integer schemeId);
    
    public List<Role> getRoleList();
    
    public List<ItemObject> getRoleByScheme(Integer schemeId);
}

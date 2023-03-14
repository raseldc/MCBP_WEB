package com.wfp.lmmis.rm.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.rm.model.Role;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

public interface RoleService {

    public Role getRole(int id);

    /**
     *
     * @param role
     */
    public void Save(Role role);

    public void Edit(Role role) throws ExceptionWrapper;
    
    public void delete(Role role) throws ExceptionWrapper;

    /**
     *
     * @param schemeId
     * @return
     */
    public List<Role> getRoleList(Integer schemeId);
    
    /**
     *
     * @return
     */
    public List<Role> getRoleList();
    
    /**
     *
     * @param schemeId
     * @return
     */
    public List<ItemObject> getRoleByScheme(Integer schemeId);
}

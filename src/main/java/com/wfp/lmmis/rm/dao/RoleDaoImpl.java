package com.wfp.lmmis.rm.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.rm.model.Role;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao
{

    //private static final logger logger = //logger.getlogger(RoleDaoImpl.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Role getRole(int id)
    {
        Role role = (Role) getCurrentSession().get(Role.class, id);
        return role;
    }

    @Override
    public void Save(Role role)
    {
        this.getCurrentSession().save(role);
    }

    /**
     *
     * @param role
     * @throws ExceptionWrapper
     */
    @Override
    public void Edit(Role role) throws ExceptionWrapper
    {
        try
        {
            Role dBRole = getRole(role.getId());
            role.setCreatedBy(dBRole.getCreatedBy());
            role.setCreationDate(dBRole.getCreationDate());
            this.getCurrentSession().merge(role);
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Role, role.getId(), role.getModifiedBy(), role.getModificationDate(), role.getNameInBangla(), role.getNameInBangla());
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            //logger.error(e.getMessage());
            throw new ExceptionWrapper(e.getMessage());
        }

    }

    @Override
    public void delete(Role role) throws ExceptionWrapper
    {
        try
        {
            //this.getCurrentSession().delete(role);
            Query q = this.getCurrentSession().createQuery("update Role set deleted=1, modifiedBy=:modifiedBy, modificationDate=:modificationDate where id=" + role.getId());
            q.setParameter("modifiedBy", role.getModifiedBy());
            q.setParameter("modificationDate", role.getModificationDate());
            q.executeUpdate();
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Role, role.getId(), role.getModifiedBy(), role.getModificationDate(), role.getNameInEnglish(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
        catch (Exception e)
        {
            //logger.error(e.getMessage());
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @Override
    public List<Role> getRoleList(Integer schemeId)
    {
        @SuppressWarnings("unchecked")
        List<Role> list = sessionFactory.getCurrentSession().createQuery("from Role where deleted=0").list();
        return list;
    }
    
    /**
     *
     * @return
     */
    @Override
    public List<Role> getRoleList()
    {
        @SuppressWarnings("unchecked")
        List<Role> list = sessionFactory.getCurrentSession().createQuery("from Role where deleted=0").list();
        return list;
    }

    @Override
    public List<ItemObject> getRoleByScheme(Integer schemeId)
    {
        try
        {
            @SuppressWarnings("unchecked")
            List<ItemObject> list = sessionFactory.getCurrentSession().createQuery("select new com.wfp.lmmis.utility.ItemObject(d.id, d.nameInBangla, d.nameInEnglish) from Role d where d.deleted=0").list();
            System.out.println("list.size() = " + list.size());
            return list;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

}

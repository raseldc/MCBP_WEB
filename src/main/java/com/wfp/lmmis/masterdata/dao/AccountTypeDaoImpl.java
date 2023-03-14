/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.AccountType;
import com.wfp.lmmis.model.ChangeLog;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public class AccountTypeDaoImpl implements AccountTypeDao
{
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public AccountType getAccountType(Integer id)
    {
        AccountType accountType = (AccountType)this.sessionFactory.getCurrentSession().get(AccountType.class, id);
        return accountType;
    }

    @Override
    public boolean save(AccountType accountType)
    {
        String duplicateCheckQuery = "select count(id) from AccountType where nameInEnglish = '" + accountType.getNameInEnglish() + "' or nameInBangla = '" + accountType.getNameInBangla() + "' or code = '" + accountType.getCode() + "'";
        System.out.println("query = " + duplicateCheckQuery);
        long duplicateCount = (long)this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
        System.out.println("duplicate count = " + duplicateCount);
        
        if(duplicateCount == 0){
        ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.AccountType, accountType.getId(), accountType.getCreatedBy(), accountType.getCreationDate(), null, accountType.getNameInBangla() + " status:" + accountType.isActive() + " code:" + accountType.getCode());
        this.getCurrentSession().save(changeLog);
        this.sessionFactory.getCurrentSession().save(accountType);
        return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void edit(AccountType editedAccountType)
    {
         AccountType accountType = (AccountType) this.sessionFactory.getCurrentSession().get(AccountType.class, editedAccountType.getId());
        if (accountType != null)
        {
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.AccountType, accountType.getId(), editedAccountType.getModifiedBy(), editedAccountType.getModificationDate(), accountType.getNameInBangla() + " status:" + accountType.isActive() + " code:" + accountType.getCode(), editedAccountType.getNameInBangla() + " status:" + editedAccountType.isActive() + " code:" + editedAccountType.getCode());
            this.getCurrentSession().save(changeLog);
            
            accountType.setNameInBangla(editedAccountType.getNameInBangla());
            accountType.setNameInEnglish(editedAccountType.getNameInEnglish());
            accountType.setCode(editedAccountType.getCode());
            accountType.setDescription(editedAccountType.getDescription());
            accountType.setModifiedBy(editedAccountType.getModifiedBy());
            accountType.setModificationDate(editedAccountType.getModificationDate());
        }
        this.sessionFactory.getCurrentSession().update(accountType);
    }

    @Override
    public void delete (AccountType accountType)
    {
        if(accountType != null)
        {
            System.out.println("AccountType id is = "+accountType.getId()+" name = "+accountType.getNameInEnglish());

            Query q = this.sessionFactory.getCurrentSession().createQuery("delete from Division where id = " + accountType.getId());
            q.executeUpdate();
            
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.AccountType, accountType.getId(), accountType.getModifiedBy(), accountType.getModificationDate(), accountType.getNameInBangla(), "deleted");
            this.getCurrentSession().save(changeLog);
        }
    }
    
    @Override
    public List<AccountType> getAccountTypeList()
    {
        @SuppressWarnings("unchecked")
        List<AccountType> list = sessionFactory.getCurrentSession().createQuery("from AccountType a where a.deleted=0").list();
        return list;
    }
}

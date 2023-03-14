/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.dao;

import com.wfp.lmmis.enums.LoggingServiceType;
import com.wfp.lmmis.enums.LoggingTableType;
import com.wfp.lmmis.masterdata.model.Bank;
import com.wfp.lmmis.model.ChangeLog;
import com.wfp.lmmis.utility.ItemObject;
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
public class BankDaoImpl implements BankDao
{

    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Bank getBank(Integer id)
    {
        Bank bank = (Bank) this.sessionFactory.getCurrentSession().get(Bank.class, id);
        return bank;
    }

    @Override
    public boolean save(Bank bank)
    {
        String duplicateCheckQuery = "select count(id) from Bank where nameInEnglish = '" + bank.getNameInEnglish() + "' or nameInBangla = '" + bank.getNameInBangla() + "' or code = '" + bank.getCode() + "'";
        System.out.println("query = " + duplicateCheckQuery);
        long duplicateCount = (long) this.sessionFactory.getCurrentSession().createQuery(duplicateCheckQuery).uniqueResult();
        System.out.println("duplicate count = " + duplicateCount);
        if (duplicateCount == 0)
        {
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.CREATE, LoggingTableType.Bank, bank.getId(), bank.getCreatedBy(), bank.getCreationDate(), null, bank.getNameInBangla() + " code:" + bank.getCode());
            this.getCurrentSession().save(changeLog);
            this.sessionFactory.getCurrentSession().save(bank);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *
     * @param editedBank
     */
    @Override
    public void edit(Bank editedBank)
    {
        Bank bank = (Bank) this.sessionFactory.getCurrentSession().get(Bank.class, editedBank.getId());
        if (bank != null)
        {
            ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.EDIT, LoggingTableType.Bank, bank.getId(), editedBank.getModifiedBy(), editedBank.getModificationDate(), bank.getNameInBangla() + " code:" + bank.getCode(), editedBank.getNameInBangla() + " code:" + editedBank.getCode());
            this.getCurrentSession().save(changeLog);
            bank.setNameInBangla(editedBank.getNameInBangla());
            bank.setNameInEnglish(editedBank.getNameInEnglish());
            bank.setAddress(editedBank.getAddress());
            bank.setCode(editedBank.getCode());
            bank.setModifiedBy(editedBank.getModifiedBy());
            bank.setModificationDate(editedBank.getModificationDate());
            bank.setAccountNoLength(editedBank.getAccountNoLength());
        }
        this.sessionFactory.getCurrentSession().update(bank);
    }

    @Override
    public boolean delete(Bank bank)
    {
        if (bank != null)
        {
            System.out.println("Bank id is = " + bank.getId() + " name = " + bank.getNameInEnglish());

            String querySt = "select b.id from Branch b where b.bank.id = " + bank.getId();
            System.out.println("query = " + querySt);
            Integer branchRefCount = this.sessionFactory.getCurrentSession().createQuery(querySt).list().size();
            if (branchRefCount == 0)
            {
                Query q = this.sessionFactory.getCurrentSession().createQuery("delete from Bank where id = " + bank.getId());
                q.executeUpdate();

                ChangeLog changeLog = new ChangeLog(null, LoggingServiceType.DELETE, LoggingTableType.Bank, bank.getId(), bank.getModifiedBy(), bank.getModificationDate(), bank.getNameInBangla(), "deleted");
                this.getCurrentSession().save(changeLog);
                return true;
            }
            else
            {
                return false;
            }
        }

        return false;
    }

    @Override
    public List<Bank> getBankList()
    {
        @SuppressWarnings("unchecked")
        List<Bank> list = sessionFactory.getCurrentSession().createQuery("from Bank o where o.deleted=0").list();
        return list;
    }

    @Override
    public List<ItemObject> getBankIoList()
    {
        @SuppressWarnings("unchecked")
        String querySt = null;
        querySt = "select new com.wfp.lmmis.utility.ItemObject(o.id, o.nameInBangla, o.nameInEnglish,o.accountNoLength)  from Bank o where o.deleted=0";

        try
        {
            List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery(querySt).list();
            System.out.println("itemObjects = " + itemObjects.size());
            return itemObjects;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("ex = " + e.getMessage());
        }
        return null;
    }

    
}

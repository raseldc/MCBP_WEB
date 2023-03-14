/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.dao;

import com.wfp.lmmis.applicant.model.DoubleDippingMatchedStatus;
import com.wfp.lmmis.rm.controller.UserController;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public class DoubleDippingMatchedDaoImpl implements DoubleDippingMatchedDao {

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public DoubleDippingMatchedStatus getDoubleDippingMatchedStatus(Integer id) {
        DoubleDippingMatchedStatus doubleDippingMatchedStatus = (DoubleDippingMatchedStatus) this.sessionFactory.getCurrentSession().get(DoubleDippingMatchedStatus.class, id);
        return doubleDippingMatchedStatus;
    }

    @Override
    public void save(DoubleDippingMatchedStatus doubleDippingMatchedStatus) {
        System.out.println("save dd matched status in daoimple");
        try {
            this.sessionFactory.getCurrentSession().save(doubleDippingMatchedStatus);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("excep = " + e.getMessage());
        }

    }

    @Override
    public void edit(DoubleDippingMatchedStatus doubleDippingMatchedStatus) {
        //edit
    }
}

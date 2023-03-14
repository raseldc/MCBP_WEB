/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dao;

import com.wfp.lmmis.model.Notice;
import com.wfp.lmmis.rm.controller.UserController;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public class NoticeBoardDaoImpl implements NoticeBoardDao
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Notice getNotice(Integer id)
    {
        Notice notice = (Notice) this.sessionFactory.getCurrentSession().get(Notice.class, id);
        return notice;
    }

    @Override
    public boolean save(Notice notice)
    {
        this.sessionFactory.getCurrentSession().save(notice);
        return true;
    }

    @Override
    public void edit(Notice editedNotice)
    {
        Notice notice = (Notice) this.sessionFactory.getCurrentSession().get(Notice.class, editedNotice.getId());

        notice.setNoticeBn(editedNotice.getNoticeBn());
        notice.setNoticeEn(editedNotice.getNoticeEn());
        notice.setDescription(editedNotice.getDescription());
        notice.setNoticeDate(editedNotice.getNoticeDate());
        notice.setActive(editedNotice.isActive());
        notice.setModifiedBy(editedNotice.getModifiedBy());
        notice.setModificationDate(editedNotice.getModificationDate());
        this.sessionFactory.getCurrentSession().update(notice);
    }

    /**
     *
     * @param notice
     * @return
     */
    @Override
    public boolean delete(Notice notice)
    {
        return true;
    }

    @Override
    public List getNoticeList()
    {
        @SuppressWarnings("unchecked")
        List<Notice> list = sessionFactory.getCurrentSession().createQuery("from Notice").list();
        return list;
    }
    
    @Override
    public List getActiveNoticeList()
    {
        @SuppressWarnings("unchecked")
        List<Notice> list = sessionFactory.getCurrentSession().createQuery("from Notice where active=1").list();
        return list;
    }
}

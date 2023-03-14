/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.service;

import com.wfp.lmmis.dao.NoticeBoardDao;
import com.wfp.lmmis.model.Notice;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Service
@Transactional
public class NoticeBoardServiceImpl implements NoticeBoardService
{

    @Autowired
    NoticeBoardDao noticeBoardDao;

    @Override
    public Notice getNotice(Integer id)
    {
        return noticeBoardDao.getNotice(id);
    }

    @Override
    public boolean save(Notice notice)
    {
        return noticeBoardDao.save(notice);
    }

    @Override
    public void edit(Notice notice)
    {
        noticeBoardDao.edit(notice);
    }

    @Override
    public boolean delete(Notice notice)
    {
        return noticeBoardDao.delete(notice);
    }

    @Override
    public List getNoticeList()
    {
        return noticeBoardDao.getNoticeList();
    }
    
    @Override
    public List getActiveNoticeList()
    {
        return noticeBoardDao.getActiveNoticeList();
    }
}

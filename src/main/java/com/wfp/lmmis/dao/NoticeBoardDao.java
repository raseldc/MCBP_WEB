/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dao;

import com.wfp.lmmis.model.Notice;
import java.util.List;

/**
 *
 * @author user
 */
public interface NoticeBoardDao
{

    public Notice getNotice(Integer id);

    public boolean save(Notice notice);

    public void edit(Notice notice);

    public boolean delete(Notice notice);

    public List getNoticeList();
    
    public List getActiveNoticeList();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.service;

import com.wfp.lmmis.dao.AttachmentDao;
import com.wfp.lmmis.applicant.model.ApplicantAttachment;
import com.wfp.lmmis.utility.ItemObject;
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
public class AttachmentServiceImpl implements AttachmentService{
    @Autowired
    AttachmentDao attachmentDao;
    
    @Override
    public ApplicantAttachment getAttachment(Integer id)
    {
        return this.attachmentDao.getAttachment(id);
    }
    
    @Override
    public void save(ApplicantAttachment attachment)
    {
       this.attachmentDao.save(attachment);
    }
    
    /**
     *
     * @param attachment
     */
    @Override
    public void edit(ApplicantAttachment attachment)
    {
        this.attachmentDao.edit(attachment);
    }
    
    @Override
    public void delete (Integer id){
        this.attachmentDao.delete(id);
    }
    
    @Override
    public List<ApplicantAttachment> getAttachmentList()
    {
        return this.attachmentDao.getAttachmentList();
    }
    @Override
    public List<ItemObject> getAttachmentIoList()
    {
        return this.attachmentDao.getAttachmentIoList();
    }
}

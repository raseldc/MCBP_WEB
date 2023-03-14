/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dao;

import com.wfp.lmmis.applicant.model.ApplicantAttachment;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AttachmentDaoImpl implements AttachmentDao {

    //private static final logger logger = //logger.getlogger("UserController");
    @Autowired
    SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public ApplicantAttachment getAttachment(Integer id) {
        ApplicantAttachment attachment = (ApplicantAttachment) this.sessionFactory.getCurrentSession().get(ApplicantAttachment.class, id);
        return attachment;
    }

    @Override
    public void save(ApplicantAttachment attachment) {
        this.sessionFactory.getCurrentSession().save(attachment);
    }

    @Override
    public void edit(ApplicantAttachment editedAttachment) {
        ApplicantAttachment attachment = (ApplicantAttachment) this.sessionFactory.getCurrentSession().get(ApplicantAttachment.class, editedAttachment.getId());
        if (attachment != null) {
            attachment.setAttachmentName(editedAttachment.getAttachmentName());
            attachment.setFileName(editedAttachment.getFileName());
            attachment.setFilePath(editedAttachment.getFilePath());
            attachment.setApplicant(editedAttachment.getApplicant());
//            attachment.setModifiedBy(editedAttachment.getModifiedBy());
//            attachment.setModificationDate(editedAttachment.getModificationDate());
        }
        this.sessionFactory.getCurrentSession().update(attachment);
    }

    @Override
    public void delete(Integer id) {
        ApplicantAttachment attachment = (ApplicantAttachment) this.sessionFactory.getCurrentSession().get(ApplicantAttachment.class, id);
        if (attachment != null) {
            this.sessionFactory.getCurrentSession().delete(attachment);
        }
    }

    @Override
    public List<ApplicantAttachment> getAttachmentList() {
        try {
            @SuppressWarnings("unchecked")
            List<ApplicantAttachment> list = sessionFactory.getCurrentSession().createQuery("from Attachment").list();
            return list;
        } catch (Exception e) {
            System.out.println("exc=" + e.getMessage());
            return null;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public List<ItemObject> getAttachmentIoList() {
        try {
            List<ItemObject> itemObjects = sessionFactory.getCurrentSession().createQuery("select new com.wfp.lmmis.utility.ItemObject(o.id, o.fileName, o.filePath)  from Attachment o").list();
            return itemObjects;
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }
}

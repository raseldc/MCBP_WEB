/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dao;

import com.wfp.lmmis.applicant.model.ApplicantAttachment;
import com.wfp.lmmis.utility.ItemObject;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface AttachmentDao
{
    public ApplicantAttachment getAttachment(Integer id);
    
    /**
     *
     * @param attachment
     */
    public void save(ApplicantAttachment attachment);
    
    public void edit(ApplicantAttachment attachment);
    
    public void delete (Integer id);
    
    public List<ApplicantAttachment> getAttachmentList();
    
    public List<ItemObject> getAttachmentIoList();
}

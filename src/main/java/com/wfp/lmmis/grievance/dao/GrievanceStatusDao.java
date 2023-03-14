/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.grievance.dao;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.model.GrievanceStatus;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface GrievanceStatusDao
{

    public GrievanceStatus getGrievanceStatus(Integer id);

    public GrievanceStatus getGrievanceStatus(int displayOrder);

    public void save(GrievanceStatus grievanceStatus);

    public void edit(GrievanceStatus grievanceStatus) throws ExceptionWrapper;

    public void delete(GrievanceStatus grievanceStatus) throws ExceptionWrapper;

    public List<GrievanceStatus> getGrievanceStatusList();

    public List<GrievanceStatus> getGrievanceStatusList(Integer lowMarkStatusId);
    
    public List<GrievanceStatus> getUnResolvedGrievanceStatusList();
}

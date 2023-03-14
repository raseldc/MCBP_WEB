/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.grievance.dao;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.grievance.model.GrievanceType;
import java.util.List;

/**
 *
 * @author Philip
 */
public interface GrievanceTypeDao
{

    public GrievanceType getGrievanceType(Integer id);

    public void save(GrievanceType grievanceType);

    public void delete(GrievanceType grievanceType) throws ExceptionWrapper;

    public void edit(GrievanceType grievanceType) throws ExceptionWrapper;

    public List<GrievanceType> getGrievanceTypeList();

    public List<GrievanceType> getActiveGrievanceTypeList();
}

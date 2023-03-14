/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.selection.dao;

import com.wfp.lmmis.selection.model.SelectionComments;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public interface SelectionCommentsDao
{
    public void save(SelectionComments selectionComments);
    
    /**
     *
     * @param parameters
     * @return
     */
    public List<SelectionComments> getSelectionCommentsList(Map parameters);
}

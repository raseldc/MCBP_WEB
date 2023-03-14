/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.selection.service;

import com.wfp.lmmis.selection.model.SelectionComments;
import java.util.List;
import java.util.Map;

public interface SelectionCommentsService
{
    public void save(SelectionComments selectionComments);
    
    public List<SelectionComments> getSelectionCommentsList(Map parameters);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.editor;

import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.service.UnionService;
import java.beans.PropertyEditorSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author user
 */
public class UnionEditor extends PropertyEditorSupport
{
    @Autowired
    UnionService unionService;
    
    // Converts a String to a Division (when submitting form)

    /**
     *
     * @param text
     */
    @Override
    public void setAsText(String text) {
        try {
            Union union = null;
            if (!text.equals("")) {
                union = unionService.getUnion(Integer.parseInt(text));
            }
            this.setValue(union);
        } catch (Exception e) {
            System.out.println("ex = " + e.getMessage());
            e.printStackTrace();
            this.setValue(null);
        }

    }
    
    // Converts a Division to a String (when displaying form)
    @Override
    public String getAsText() {
        Union i = (Union) this.getValue();
        if (i != null) {
            return i.getId().toString();
        }
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.editor;

import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.service.DivisionService;
import java.beans.PropertyEditorSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Philip
 */
public class DivisionEditor extends PropertyEditorSupport
{
    @Autowired
    DivisionService divisionservice;
    
    // Converts a String to a Division (when submitting form)
    @Override
    public void setAsText(String text) {
        try {
            Division division = null;
            if (!text.equals("")) {
                division = divisionservice.getDivision(Integer.parseInt(text));
            }
            this.setValue(division);
        } catch (Exception e) {
            e.printStackTrace();
            this.setValue(null);
        }

    }
    
    // Converts a Division to a String (when displaying form)

    /**
     *
     * @return
     */
    @Override
    public String getAsText() {
        Division i = (Division) this.getValue();
        if (i != null) {
            return i.getId().toString();
        }
        return null;
    }
}

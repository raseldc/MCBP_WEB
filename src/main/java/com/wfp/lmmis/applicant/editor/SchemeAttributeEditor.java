/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.editor;

import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import com.wfp.lmmis.masterdata.service.SchemeAttributeService;
import java.beans.PropertyEditorSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author user
 */
public class SchemeAttributeEditor extends PropertyEditorSupport
{
    @Autowired
    private SchemeAttributeService schemeAttributeService;
    
    // Converts a String to a SchemeAttribute (when submitting form)
    @Override
    public void setAsText(String text) {
        try {
            SchemeAttribute schemeAttribute = null;
            if (!text.equals("")) {                
                schemeAttribute = schemeAttributeService.getSchemeAttribute(Integer.parseInt(text));
            }
            this.setValue(schemeAttribute);
        } catch (Exception e) {
            e.printStackTrace();
            this.setValue(null);
        }

    }
    
    // Converts a Division to a String (when displaying form)
    @Override
    public String getAsText() {
        SchemeAttribute i = (SchemeAttribute) this.getValue();
        if (i != null) {
            return i.getId().toString();
        }
        return null;
    }
}

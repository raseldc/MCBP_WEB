/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.editor;

import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.service.DistrictService;
import java.beans.PropertyEditorSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author user
 */
public class DistrictEditor extends PropertyEditorSupport
{
    @Autowired
    DistrictService districtService;
    
    // Converts a String to a Division (when submitting form)
    @Override
    public void setAsText(String text) {
        try {
            District district = null;
            if (!text.equals("")) {
                district = districtService.getDistrict(Integer.parseInt(text));
            }
            this.setValue(district);
        } catch (Exception e) {
            e.printStackTrace();
            this.setValue(null);
        }

    }
    
    // Converts a Division to a String (when displaying form)
    @Override
    public String getAsText() {
        District i = (District) this.getValue();
        if (i != null) {
            return i.getId().toString();
        }
        return null;
    }
}

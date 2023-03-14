package com.wfp.lmmis.masterdata.editor;

import com.wfp.lmmis.masterdata.model.SchemeAttribute;
import com.wfp.lmmis.masterdata.service.SchemeAttributeService;
import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchemeAttributeEditor extends PropertyEditorSupport {

    @Autowired
    private SchemeAttributeService schemeAttributeService;

    // Converts a String to a ItemType (when submitting form)
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

    // Converts a ItemType to a String (when displaying form)
    @Override
    public String getAsText() {
        SchemeAttribute i = (SchemeAttribute) this.getValue();
        if (i != null) {
            return i.getId().toString();
        }
        return null;
    }
}

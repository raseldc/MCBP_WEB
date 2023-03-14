package com.wfp.lmmis.masterdata.editor;

import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.service.SchemeService;
import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rasel
 */
@Component
public class SchemeEditor extends PropertyEditorSupport {

    @Autowired
    private SchemeService schemeService;

    // Converts a String to a ItemType (when submitting form)
    @Override
    public void setAsText(String text) {
        try {
            Scheme scheme = null;
            if (!text.equals("")) {
                scheme = schemeService.getScheme(Integer.parseInt(text));
            }
            this.setValue(scheme);
        } catch (Exception e) {
            e.printStackTrace();
            this.setValue(null);
        }

    }

    // Converts a ItemType to a String (when displaying form)
    @Override
    public String getAsText() {
        Scheme i = (Scheme) this.getValue();
        if (i != null) {
            return i.getId().toString();
        }
        return null;
    }
}

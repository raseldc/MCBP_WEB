package com.wfp.lmmis.rm.editor;

import com.wfp.lmmis.rm.model.Page;
import com.wfp.lmmis.rm.service.PageService;
import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rasel
 */
@Component
public class PageEditor extends PropertyEditorSupport {

    @Autowired
    private PageService pageService;

    // Converts a String to a ItemType (when submitting form)
    @Override
    public void setAsText(String text) {
        try {
            Page page = null;
            if (!text.equals("")) {
                page = pageService.getPage(Integer.parseInt(text));
            }
            this.setValue(page);
        } catch (Exception e) {
            e.printStackTrace();
            this.setValue(null);
        }

    }

    // Converts a ItemType to a String (when displaying form)
    @Override
    public String getAsText() {
        Page i = (Page) this.getValue();
        if (i != null) {
            return i.getId().toString();
        }
        return null;
    }
}

package com.wfp.lmmis.rm.editor;

import com.wfp.lmmis.rm.model.Role;
import com.wfp.lmmis.rm.service.RoleService;
import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleEditor extends PropertyEditorSupport {

    @Autowired
    private RoleService roleService;

    // Converts a String to a Role (when submitting form)
    @Override
    public void setAsText(String text) {
        try {
            Role role = null;
            if (!text.equals("")) {
                role = roleService.getRole(Integer.parseInt(text));
            }
            this.setValue(role);
        } catch (Exception e) {
            e.printStackTrace();
            this.setValue(null);
        }

    }

    // Converts a ItemType to a String (when displaying form)
    @Override
    public String getAsText() {
        Role i = (Role) this.getValue();
        if (i != null) {
            return i.getId().toString();
        }
        return null;
    }
}

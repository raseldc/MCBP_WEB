package com.wfp.lmmis.rm.editor;

import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.service.UserService;
import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEditor extends PropertyEditorSupport {

    @Autowired
    private UserService userService;

    // Converts a String to a User (when submitting form)
    @Override
    public void setAsText(String text) {
        try {
            User user = null;
            if (!text.equals("")) {
                user = userService.getUser(Integer.parseInt(text));
            }
            this.setValue(user);
        } catch (Exception e) {
            e.printStackTrace();
            this.setValue(null);
        }

    }

    // Converts a User to a String (when displaying form)
    @Override
    public String getAsText() {
        User i = (User) this.getValue();
        if (i != null) {
            return i.getId().toString();
        }
        return null;
    }
}

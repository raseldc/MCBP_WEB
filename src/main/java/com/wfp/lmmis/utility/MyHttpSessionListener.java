/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.wfp.lmmis.rm.controller.LoginController;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Philip
 */
@WebListener
public class MyHttpSessionListener implements HttpSessionListener
{

    @Override
    public void sessionDestroyed(HttpSessionEvent event)
    {
//        final ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getSession().getServletContext());
//        final LoginController service =  ctx.getBean(LoginController.class);
        LoginController service = (LoginController) SpringContext.getApplicationContext().getBean("loginController");
        HttpSession session = event.getSession();
        service.updateLogout(session);
        System.out.println("session timeout");
    }

    /**
     *
     * @param se
     */
    @Override
    public void sessionCreated(HttpSessionEvent se)
    {
    }
}

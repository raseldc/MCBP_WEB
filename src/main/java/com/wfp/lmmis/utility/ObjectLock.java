/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import com.wfp.lmmis.rm.service.UserService;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Philip
 */

//@WebListener
//public class ObjectLock implements HttpSessionListener {
//
//    @Autowired
//    private UserService userService;
//    @Override
//    public void sessionDestroyed(HttpSessionEvent event) {
//        // Do here the job.
//        System.out.println("event = " + event);
//        Integer userid = (Integer) event.getSession().getAttribute("userId");
//        System.out.println("userid = " + userid);
//        this.userService.updateVisitingLog((Integer) event.getSession().getAttribute("userId"), false);
//    }
//
//    // ...
//
//    @Override
//    public void sessionCreated(HttpSessionEvent se)
//    {
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}

//@WebListener
//public class ObjectLock implements HttpSessionAttributeListener {
//
//    @Autowired
//    private UserService userService;
//    @Override
//    public void attributeAdded(HttpSessionBindingEvent event) {
////        if (event.getValue() instanceof YourObject) {
////            // An instance of YourObject has been bound to the session.
////        }
//    }
//
//    @Override
//    public void attributeRemoved(HttpSessionBindingEvent event) {
//        System.out.println("event = " + event);
//        this.userService.updateVisitingLog((Integer) event.getSession().getAttribute("userId"), false);
////        if (event.getValue() instanceof YourObject) {
////            // An instance of YourObject has been unbound from the session.
////        }
//    }
//
//    @Override
//    public void attributeReplaced(HttpSessionBindingEvent event) {
////        if (event.getValue() instanceof YourObject) {
////            // An instance of YourObject has been replaced in the session.
////        }
//    }
//
//}
//
//public class ObjectLock implements Serializable,HttpSessionBindingListener {
//    @Autowired
//    private UserService userService;
////    public void valueBound(HttpSessionBindingEvent event) {
////        log.info("valueBound:" + event.getName() + " session:" + event.getSession().getId() );
////
////    }
////
////    public void registerSession() {
////        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put( "sessionBindingListener", this  );
////        log.info( "registered sessionBindingListener"  );
////    }
//
//    @Override
//    public void valueUnbound(HttpSessionBindingEvent event) {
//        System.out.println("value unbound");
//        this.userService.updateVisitingLog((Integer) event.getSession().getAttribute("userId"), false);
//    }
//
//    @Override
//    public void valueBound(HttpSessionBindingEvent event)
//    {
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.utility;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author PCUser
 */
public class StaticContextAccessor
{

    private static StaticContextAccessor instance;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerInstance()
    {
        instance = this;
    }

    public static <T> T getBean(Class<T> clazz)
    {
        return instance.applicationContext.getBean(clazz);
    }

}

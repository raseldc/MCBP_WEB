/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.rm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Philip
 */
@Controller
public class HomeController {
    
    @RequestMapping(value = "test")
    public String getIndex()
    {
        System.err.println("mahbub" );
        return "index";
    }
    
}

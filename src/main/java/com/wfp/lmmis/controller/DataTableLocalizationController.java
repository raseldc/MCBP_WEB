/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.controller;

import com.wfp.lmmis.utility.CommonUtility;
import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author user
 */
@Controller
public class DataTableLocalizationController
{
    @RequestMapping(value = "/dataTable/localization/bangla", method = RequestMethod.GET)
    public @ResponseBody
    void getDataTableInBangla(HttpServletRequest request, HttpServletResponse response) throws JSONException
    {
        System.out.println("In datatable json lang = bangla");
        //This is localization of data table for bangla
        String json =new JSONObject()
                .put("sProcessing", "প্রসেসিং হচ্ছে...")
                .put("sLengthMenu", "_MENU_ টা এন্ট্রি দেখুন")                
                .put("sZeroRecords","আপনি যা অনুসন্ধান করেছেন তার সাথে মিলে যাওয়া কোন রেকর্ড খুঁজে পাওয়া যায় নাই")
                .put("sInfo", "_TOTAL_ টা এন্ট্রির মধ্যে _START_ থেকে _END_ পর্যন্ত দেখানো হচ্ছে")
                .put("sInfoEmpty", "কোন এন্ট্রি খুঁজে পাওয়া যায় নাই ")
                .put("sInfoFiltered", "(মোট _MAX_ টা এন্ট্রির মধ্যে থেকে বাছাইকৃত)")
                .put("sInfoPostFix","")
                .put("sSearch","অনুসন্ধান:")
                .put("sUrl", "")
                .put("oPaginate", new JSONObject()
                    .put("sFirst", "প্রথম")
                    .put("sPrevious","পূর্ববর্তী")
                    .put("sNext","পরবর্তী")
                    .put("sLast","শেষ"))
                .toString();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try
        {
            response.getWriter().write(json);
        }
        catch (IOException ex)
        {
            //logger.getlogger(DataTableLocalizationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

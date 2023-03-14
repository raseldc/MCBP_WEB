/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.controller;

import com.wfp.lmmis.applicant.service.BeneficiaryInfoService;
import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.utility.JsonResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author shamiul Islam-AnunadSolution * This class create after beneficiary controller due to large
 * and new requirement
 */
@Controller
public class BeneficiaryEditController {

    @Autowired
    private BeneficiaryInfoService beneficiaryInfoService;

    @RequestMapping(value = "/beneficiary-info/edit", method = RequestMethod.POST)
    @ResponseBody
    public String editBeneficiaryInformation(@RequestBody Beneficiary beneficiary,
            HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {

        JsonResult jr = new JsonResult(false, "");
        beneficiaryInfoService.beneficiaryInfoEdit(beneficiary);
        try {
        } catch (Exception e) {
            throw e;
        }
        return jr.toJsonString();
    }

    @RequestMapping(value = "/api/applicantion/save", method = RequestMethod.POST)
    @ResponseBody
    public String applicantSave(@RequestBody String beneficiary,
            HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {

        JsonResult jr = new JsonResult(false, "Get Your Call MAN");
        System.out.println("Json: " + beneficiary);
//        beneficiaryInfoService.beneficiaryInfoEdit(beneficiary);
        try {
        } catch (Exception e) {
            return jr.toJsonString();
            //throw e;
        }
        return jr.toJsonString();
    }
}

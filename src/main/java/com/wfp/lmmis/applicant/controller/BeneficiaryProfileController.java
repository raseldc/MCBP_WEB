/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.applicant.controller;

import com.wfp.lmmis.applicant.service.BeneficiaryProfieService;
import com.wfp.lmmis.applicant.service.BeneficiaryService;
import com.wfp.lmmis.utility.BeneficiaryProfileView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Controller
public class BeneficiaryProfileController {

    @Autowired
    private BeneficiaryProfieService beneficiaryProfieService;

    @RequestMapping(value = "/beneficiary/profile/{id}", method = RequestMethod.GET)
    public String getBenificaryProfile(@PathVariable("id") Integer id,
            HttpSession session, Model model, HttpServletRequest request) {
        try {
            BeneficiaryProfileView beneficiaryProfileView = beneficiaryProfieService.getBenefificaryInfoByID(id);
            String referer = request.getHeader("referer");
            model.addAttribute("beneficiaryProfile", beneficiaryProfileView);
            model.addAttribute("lan", LocaleContextHolder.getLocale().getLanguage().trim());
            model.addAttribute("referer", referer);
            return "beneficiaryProfile";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "beneficiaryProfile";
        }

    }
}

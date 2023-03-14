/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.selection.controller;

import static com.wfp.lmmis.applicant.controller.ApplicantController.FILE_CREATION_PATH;
import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.applicant.service.ApplicantService;
import com.wfp.lmmis.applicant.service.BeneficiaryService;
import com.wfp.lmmis.beneficiary.model.Beneficiary;
import com.wfp.lmmis.beneficiary.model.BeneficiaryView;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.Localizer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author user
 */
@Controller
public class CardPrintingController
{

    @Autowired
    ApplicantService applicantService;

    @Autowired
    BeneficiaryService beneficiaryService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/cardPrinting", method = RequestMethod.GET)
    public String getCardprintingPage(Model model, HttpServletRequest request)
    {
        CommonUtility.mapDivisionName(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapFiscalYearName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "cardPrinting";

    }

    @RequestMapping(value = "/cardPrinting/list", method = RequestMethod.POST)
    @ResponseBody
    public void showBeneficiaryListForCardPrinting(HttpServletRequest request, HttpServletResponse response, Locale locale)
    {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try
        {
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            String divisionId = request.getParameter("divisionId");
            String districtId = request.getParameter("districtId");
            String upazilaId = request.getParameter("upazilaId");
            String unionId = request.getParameter("unionId");

            Map parameter = new HashMap();
            parameter.put("schemeId", null);
            parameter.put("fiscalYearId", null);
//            parameter.put("applicantId", request.getParameter("applicantId").trim());
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            parameter.put("unionId", unionId != null && !"".equals(unionId) ? Integer.valueOf(unionId) : null);

            List<Object> resultList = beneficiaryService.getBeneficiaryListBySearchParameter(parameter, beginIndex, pageSize);
            List<BeneficiaryView> beneficiaryList = (List<BeneficiaryView>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);

            for (BeneficiaryView beneficiary : beneficiaryList)
            {
                JSONArray ja = new JSONArray();
//                if (beneficiary.getBeneficiaryBiometricInfo() != null)
//                {
//                    ja.put("<img id='profilePhotoFile' style='width:50%' alt='profile picture' src='data:image/jpeg;base64," + beneficiary.getBeneficiaryBiometricInfo().getBase64PhotoData() + "' />");
//                }
//                else
//                {
//                    ja.put("<img id='profilePhotoFile' style='width:50%' alt='profile picture' />");
//                }
                ja.put(beneficiary.getFullNameInBangla());
                ja.put("en".equals(locale.getLanguage()) ? beneficiary.getNid() : CommonUtility.getNumberInBangla(beneficiary.getNid().toString()));
                String dob = CalendarUtility.getDateString(beneficiary.getDateOfBirth());
                ja.put("en".equals(locale.getLanguage()) ? dob : CommonUtility.getNumberInBangla(dob));
                String mobileNo = beneficiary.getMobileNo() != null ? "0" + beneficiary.getMobileNo() : "";
                ja.put("en".equals(locale.getLanguage()) ? mobileNo : CommonUtility.getNumberInBangla(mobileNo));

                String print = localizer.getLocalizedText("print", LocaleContextHolder.getLocale());
                String viewDetails = localizer.getLocalizedText("viewDetails", LocaleContextHolder.getLocale());
                ja.put("<a href=#" + " onclick=loadBeneficiary(" + beneficiary.getId() + ") title=\"" + viewDetails + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></a>");
                ja.put("<a href=\"" + request.getContextPath() + "/selection/printVataProishodCard/" + beneficiary.getId() + "\" title=\"" + print + "\"><span class=\"glyphicon glyphicon-print\"></span></a>");
                dataArray.put(ja);
            }

            try
            {
                jsonResult.put("recordsTotal", recordsTotal);
                jsonResult.put("recordsFiltered", recordsFiltered);
                jsonResult.put("data", dataArray);
                jsonResult.put("draw", draw);
            }
            catch (JSONException ex)
            {

            }

            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-store");
            PrintWriter out;
            try
            {
                out = response.getWriter();
                out.print(jsonResult);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/selection/printCard/{id}", method = RequestMethod.GET)
    public String viewApplicantForm(@PathVariable("id") Integer id, Model model)
    {
        System.out.println("printCard");
        try
        {
            Applicant applicant = this.applicantService.getApplicant(id);
            model.addAttribute("applicant", applicant);
            model.addAttribute("imagePath", FILE_CREATION_PATH);
            model.addAttribute("attachmentPath", FILE_CREATION_PATH + "/Temp");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return "printCard";
    }

    @RequestMapping(value = "/selection/printVataProishodCard/{id}", method = RequestMethod.GET)
    public String printCard(@PathVariable("id") Integer id, Model model)
    {
        System.out.println("printCard");
        try
        {
            Beneficiary beneficiary = this.beneficiaryService.getBeneficiary(id);
            System.out.println("beneficiary.fiscalyear = " + beneficiary.getFiscalYear().getNameInBangla());
            model.addAttribute("beneficiary", beneficiary);
//            Applicant beneficiary = this.applicantService.getApplicant(id);
//            System.out.println("beneficiary.fiscalyear = " + beneficiary.getFiscalYear().getNameInBangla());
//            model.addAttribute("beneficiary", beneficiary);
//            model.addAttribute("imagePath", FILE_CREATION_PATH);
//            model.addAttribute("attachmentPath", FILE_CREATION_PATH + "/Temp");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return "vataPorishodCard";
    }
}

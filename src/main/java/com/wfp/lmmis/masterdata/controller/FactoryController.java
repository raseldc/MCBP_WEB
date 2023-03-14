/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.FactoryService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.types.FactoryType;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author user
 */
@Controller
public class FactoryController
{

    //private static final logger logger = //logger.getlogger(FactoryController.class);
    @Autowired
    private FactoryService factoryService;
    @Autowired
    private DivisionService divisionService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/factory/bgmea/list")
    public String showBgmeaFactoryList(Model model)
    {
        model.addAttribute("divisionList", this.divisionService.getDivisionIoList());
        return "bgmeaFactoryList";
    }

    @RequestMapping(value = "/factory/bkmea/list")
    public String showBkmeaFactoryList(Model model)
    {
        model.addAttribute("divisionList", this.divisionService.getDivisionIoList());
        return "bkmeaFactoryList";
    }

    @RequestMapping(value = "/factory/bgmea/create", method = RequestMethod.GET)
    public String createBgmeaFactory(@ModelAttribute Factory factory, Model model)
    {
        setFactoryModel(model);
        return "bgmeaFactory";
    }

    @RequestMapping(value = "/factory/bkmea/create", method = RequestMethod.GET)
    public String createBkmeaFactory(@ModelAttribute Factory factory, Model model)
    {
        setFactoryModel(model);
        return "bkmeaFactory";
    }

    private void setFactoryModel(Model model)
    {
        try
        {
            Locale locale = LocaleContextHolder.getLocale();
            if ("en".equals(locale.getLanguage()))
            {
                model.addAttribute("divisionName", "nameInEnglish");
            }
            else
            {
                model.addAttribute("divisionName", "nameInBangla");
            }
            model.addAttribute("divisionList", this.divisionService.getDivisionIoList());
            model.addAttribute("actionType", "create");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/factory/bgmea/create", method = RequestMethod.POST)
    public String createBgmeaFactory(@Valid Factory factory, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors())
        {
            return "factory";
        }
        boolean saveResult = false;
        try
        {
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage()))
            {
                factory.setRegNo(CommonUtility.getNumberInEnglish(factory.getRegNo()));
            }
            factory.setType(FactoryType.GARMENTS);
            factory.setCreatedBy((User) session.getAttribute("user"));
            factory.setCreationDate(Calendar.getInstance());

            if (factory.getDivision().getId() == null)
            {
                factory.setDivision(null);
            }
            if (factory.getDistrict().getId() == null)
            {
                factory.setDistrict(null);
            }
            if (factory.getUpazila().getId() == null)
            {
                factory.setUpazila(null);
            }
            saveResult = this.factoryService.save(factory);
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }

        if (saveResult)
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/factory/bgmea/list";
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            model.addAttribute("actionType", "create");
            return "redirect:/factory/bgmea/create";
        }

    }

    @RequestMapping(value = "/factory/bkmea/create", method = RequestMethod.POST)
    public String createBkmeaFactory(@Valid Factory factory, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors())
        {
            return "factory";
        }
        boolean saveResult = false;
        try
        {
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage()))
            {
                factory.setRegNo(CommonUtility.getNumberInEnglish(factory.getRegNo()));
            }
            factory.setType(FactoryType.KNITWEAR);
            factory.setCreatedBy((User) session.getAttribute("user"));
            factory.setCreationDate(Calendar.getInstance());
            if (factory.getDivision().getId() == null)
            {
                factory.setDivision(null);
            }
            if (factory.getDistrict().getId() == null)
            {
                factory.setDistrict(null);
            }
            if (factory.getUpazila().getId() == null)
            {
                factory.setUpazila(null);
            }
            saveResult = this.factoryService.save(factory);
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }

        if (saveResult)
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/factory/bkmea/list";
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            model.addAttribute("actionType", "create");
            return "redirect:/factory/bkmea/create";
        }

    }

    @RequestMapping(value = "/factory/bgmea/edit/{id}", method = RequestMethod.GET)
    public String editBgmeaFactory(@PathVariable("id") Integer id, @ModelAttribute Factory factory, Model model, boolean factoryLoadRequired)
    {
        try
        {
            if (!factoryLoadRequired)
            {
                Locale locale = LocaleContextHolder.getLocale();
                if ("en".equals(locale.getLanguage()))
                {
                    model.addAttribute("divisionName", "nameInEnglish");
                }
                else
                {
                    model.addAttribute("divisionName", "nameInBangla");
                }
                model.addAttribute("divisionList", this.divisionService.getDivisionIoList());
                model.addAttribute("factoryTypeList", FactoryType.values());
                model.addAttribute("factory", this.factoryService.getFactory(id));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "bgmeaFactory";
    }

    @RequestMapping(value = "/factory/bkmea/edit/{id}", method = RequestMethod.GET)
    public String editBkmeaFactory(@PathVariable("id") Integer id, @ModelAttribute Factory factory, Model model, boolean factoryLoadRequired)
    {
        try
        {
            if (!factoryLoadRequired)
            {
                Locale locale = LocaleContextHolder.getLocale();
                if ("en".equals(locale.getLanguage()))
                {
                    model.addAttribute("divisionName", "nameInEnglish");
                }
                else
                {
                    model.addAttribute("divisionName", "nameInBangla");
                }
                model.addAttribute("divisionList", this.divisionService.getDivisionIoList());
                model.addAttribute("factoryTypeList", FactoryType.values());
                model.addAttribute("factory", this.factoryService.getFactory(id));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "bkmeaFactory";
    }

    @RequestMapping(value = "/factory/bgmea/edit/{id}", method = RequestMethod.POST)
    public String editBgmeaFactory(@PathVariable("id") Integer pk, @Valid @ModelAttribute Factory factory, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
    {
        try
        {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
            {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            }
            if (bindingResult.hasErrors())
            {
                return editBgmeaFactory(pk, factory, model, true);
            }
            if (factory.getId() == null)
            {
                factory.setCreatedBy((User) session.getAttribute("user"));
                factory.setCreationDate(Calendar.getInstance());
                this.factoryService.save(factory);
            }
            else
            {
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage()))
                {
                    factory.setRegNo(CommonUtility.getNumberInEnglish(factory.getRegNo()));
                }
                factory.setModifiedBy((User) session.getAttribute("user"));
                factory.setModificationDate(Calendar.getInstance());
                if (factory.getDivision().getId() == null)
                {
                    factory.setDivision(null);
                }
                if (factory.getDistrict().getId() == null)
                {
                    factory.setDistrict(null);
                }
                if (factory.getUpazila().getId() == null)
                {
                    factory.setUpazila(null);
                }
                this.factoryService.edit(factory);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/factory/bgmea/list";
    }

    @RequestMapping(value = "/factory/bkmea/edit/{id}", method = RequestMethod.POST)
    public String editBkmeaFactory(@PathVariable("id") Integer pk, @Valid @ModelAttribute Factory factory, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
    {
        try
        {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
            {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            }
            if (bindingResult.hasErrors())
            {
                return editBgmeaFactory(pk, factory, model, true);
            }
            if (factory.getId() == null)
            {
                factory.setCreatedBy((User) session.getAttribute("user"));
                factory.setCreationDate(Calendar.getInstance());
                this.factoryService.save(factory);
            }
            else
            {
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage()))
                {
                    factory.setRegNo(CommonUtility.getNumberInEnglish(factory.getRegNo()));
                }
                factory.setModifiedBy((User) session.getAttribute("user"));
                factory.setModificationDate(Calendar.getInstance());
                if (factory.getDivision().getId() == null)
                {
                    factory.setDivision(null);
                }
                if (factory.getDistrict().getId() == null)
                {
                    factory.setDistrict(null);
                }
                if (factory.getUpazila().getId() == null)
                {
                    factory.setUpazila(null);
                }
                this.factoryService.edit(factory);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/factory/bkmea/list";
    }

    @RequestMapping(value = "factory/delete/{factoryId}")
    public String deleteFactory(@PathVariable("factoryId") Integer factoryId, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        Factory factory = this.factoryService.getFactory(factoryId);
        factory.setModifiedBy((User) session.getAttribute("user"));
        factory.setModificationDate(Calendar.getInstance());
        boolean deleteResult = this.factoryService.delete(factory);
        if (deleteResult)
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("deleteFailFactory", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        if (factory.getType().equals(FactoryType.GARMENTS))
        {
            return "redirect:/factory/bgmea/list";
        }
        else
        {
            return "redirect:/factory/bkmea/list";
        }
    }

    /**
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/bgmea/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationBGMEAList(HttpServletRequest request, HttpServletResponse response)
    {
        //Fetch the page number from client
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
//        Integer pageNumber = 0;
        int draw = Integer.parseInt(request.getParameter("draw"));
        int beginIndex = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("length"));
        String divisionId = request.getParameter("divisionId");
        String districtId = request.getParameter("districtId");
        String upazilaId = request.getParameter("upazilaId");

        String nameEn = null, regNo = null;

        if (request.getParameter("nameEn") != null && !"".equals(request.getParameter("nameEn")))
        {
            nameEn = request.getParameter("nameEn");
        }
        if (request.getParameter("regNo") != null && !"".equals(request.getParameter("regNo")))
        {
            regNo = request.getParameter("regNo");
        }
        Map parameter = new HashMap();
        parameter.put("nameEn", nameEn);
        parameter.put("regNo", regNo);
        parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
        parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
        parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
        List<Object> resultList = factoryService.getBGMEASearchListBySearchParameter(parameter, beginIndex, pageSize);
        List<Factory> bgmeaList = (List<Factory>) resultList.get(0);
        int recordsTotal = (int) resultList.get(1);
        int recordsFiltered = (int) resultList.get(2);
        for (Factory factory : bgmeaList)
        {
            JSONArray ja = new JSONArray();
            Locale locale = LocaleContextHolder.getLocale();

            if ("en".equals(locale.getLanguage()))
            {
                ja.put(factory.getNameInBangla());
                ja.put(factory.getNameInEnglish());
                ja.put(factory.getType().getDisplayName());
                ja.put(factory.getAddress());
                ja.put(factory.getRegNo());
            }
            else
            {
                ja.put(factory.getNameInBangla());
                ja.put(factory.getNameInEnglish());
                ja.put(factory.getType().getDisplayNameBn());
                ja.put(factory.getAddress());
                ja.put(CommonUtility.getNumberInBangla(factory.getRegNo()));
            }
            if (factory.isActive() == true)
            {
                ja.put("<span class=\"glyphicon glyphicon-ok\"></span>");
            }
            else
            {
                ja.put("<span class=\"glyphicon glyphicon-remove\"></span>");
            }
            ja.put("<td><a href=\"" + request.getContextPath() + "/factory/bgmea/edit/" + factory.getId() + "\" data-toggle=\"tooltip\" title=\"Edit\">\n"
                    + "                                            <span class=\"glyphicon glyphicon-edit\"></span></a></td>");
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

    @RequestMapping(value = "/bkmea/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationBKMEAList(HttpServletRequest request, HttpServletResponse response)
    {
        //Fetch the page number from client
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
//        Integer pageNumber = 0;
        int draw = Integer.parseInt(request.getParameter("draw"));
        int beginIndex = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("length"));

        String nameEn = null, regNo = null;

        if (request.getParameter("nameEn") != null && !"".equals(request.getParameter("nameEn")))
        {
            nameEn = request.getParameter("nameEn");
        }
        if (request.getParameter("regNo") != null && !"".equals(request.getParameter("regNo")))
        {
            regNo = request.getParameter("regNo");
        }
        String divisionId = request.getParameter("divisionId");
        String districtId = request.getParameter("districtId");
        String upazilaId = request.getParameter("upazilaId");
        
        Map parameter = new HashMap();
        parameter.put("nameEn", nameEn);
        parameter.put("regNo", regNo);
        parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
        parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
        parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
        List<Object> resultList = factoryService.getBKMEASearchListBySearchParameter(parameter, beginIndex, pageSize);
        List<Factory> bgmeaList = (List<Factory>) resultList.get(0);
        int recordsTotal = (int) resultList.get(1);
        int recordsFiltered = (int) resultList.get(2);
        for (Factory factory : bgmeaList)
        {
            JSONArray ja = new JSONArray();
            Locale locale = LocaleContextHolder.getLocale();

            if ("en".equals(locale.getLanguage()))
            {
                ja.put(factory.getNameInBangla());
                ja.put(factory.getNameInEnglish());
                ja.put(factory.getType().getDisplayName());
                ja.put(factory.getAddress());
                ja.put(factory.getRegNo());
            }
            else
            {
                ja.put(factory.getNameInBangla());
                ja.put(factory.getNameInEnglish());
                ja.put(factory.getType().getDisplayNameBn());
                ja.put(factory.getAddress());
                ja.put(CommonUtility.getNumberInBangla(factory.getRegNo()));
            }
            if (factory.isActive() == true)
            {
                ja.put("<span class=\"glyphicon glyphicon-ok\"></span>");
            }
            else
            {
                ja.put("<span class=\"glyphicon glyphicon-remove\"></span>");
            }
            ja.put("<td><a href=\"" + request.getContextPath() + "/factory/bkmea/edit/" + factory.getId() + "\" data-toggle=\"tooltip\" title=\"Edit\">\n"
                    + "                                            <span class=\"glyphicon glyphicon-edit\"></span></a></td>");
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
}

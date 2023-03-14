/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.model.UpazilaInfo;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.types.UpazilaType;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author user
 */
@Controller
public class UpazillaController {

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    UpazillaService upazillaService;
    @Autowired
    DivisionService divisionService;
    @Autowired
    DistrictService districtService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/upazila/list")
    public String showUpazilaList(Model model, HttpServletRequest request) {
        CommonUtility.mapDivisionName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "upazilaList";
    }

    @RequestMapping(value = "/upazila/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationUpazilaList(HttpServletRequest request, HttpServletResponse response) {
        //Fetch the page number from client
        System.out.println("in pagination controller");
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
//        Integer pageNumber = 0;
        int draw = Integer.parseInt(request.getParameter("draw"));
        int beginIndex = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("length"));

        String divisionId = null, districtId = null;
        if (request.getParameter("divisionId") != null && !"".equals(request.getParameter("divisionId"))) {
            divisionId = request.getParameter("divisionId");
        }
        if (request.getParameter("districtId") != null && !"".equals(request.getParameter("districtId"))) {
            districtId = request.getParameter("districtId");
        }

        List<Object> resultList = upazillaService.getUpazilaSearchList(divisionId, divisionId != null, districtId, districtId != null, beginIndex, pageSize);
        int recordsTotal = 0;
        int recordsFiltered = 0;
        List<UpazilaInfo> upazilaList = new ArrayList<>();
        if (resultList != null && resultList.size() > 0) {
            upazilaList = (List<UpazilaInfo>) resultList.get(0);
            recordsTotal = (int) resultList.get(1);
            recordsFiltered = (int) resultList.get(2);
            System.out.println("recordsTotal = " + recordsTotal);
        }
        for (UpazilaInfo upazilla : upazilaList) {
            JSONArray ja = new JSONArray();
            Locale locale = LocaleContextHolder.getLocale();
            if ("en".equals(locale.getLanguage())) {
                ja.put(upazilla.getDistrictNameEn());
            } else {
                ja.put(upazilla.getDistrictNameBn());
            }
            ja.put(upazilla.getNameBn());
            ja.put(upazilla.getNameEn());

            if ("en".equals(locale.getLanguage())) {
                ja.put(upazilla.getCode());
            } else {
                ja.put(CommonUtility.getNumberInBangla(upazilla.getCode()));
            }
            if (upazilla.isActive() == true) {
                ja.put("<span class=\"glyphicon glyphicon-ok\"></span>");
            } else {
                ja.put("<span class=\"glyphicon glyphicon-remove\"></span>");
            }
            ja.put("<td><a href=\"" + request.getContextPath() + "/upazila/edit/" + upazilla.getId() + "\" data-toggle=\"tooltip\" title=\"Edit\">\n"
                    + "                                            <span class=\"glyphicon glyphicon-edit\"></span></a></td>");
            dataArray.put(ja);
        }
        try {
            jsonResult.put("recordsTotal", recordsTotal);
            jsonResult.put("recordsFiltered", recordsFiltered);
            jsonResult.put("data", dataArray);
            jsonResult.put("draw", draw);
        } catch (JSONException ex) {

        }

        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(jsonResult);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping("/getUpazila/{districtId}")
    @ResponseBody
    public List<ItemObject> getUpazilaList(@PathVariable("districtId") Integer districtId, Model model, HttpSession session) {
        System.out.println("in up con");
        return this.upazillaService.getUpazillaIoList(districtId, "MA");
    }

    /**
     *
     * @param districtId
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/getUpazilaWithDistrict/{districtId}")
    @ResponseBody
    public List<ItemObject> getUpazilaWithDistrictList(@PathVariable("districtId") Integer districtId, Model model, HttpSession session) {
        return this.upazillaService.getUpazillaIoList(districtId, "LMA");
    }

    @RequestMapping("/getDistrictOnly/{districtId}")
    @ResponseBody
    public List<ItemObject> getDistrictOnly(@PathVariable("districtId") Integer districtId, Model model, HttpSession session) {
        return this.upazillaService.getUpazillaIoList(districtId, "CITY_CORP_DIST");
    }

    @RequestMapping(value = "/upazila/create", method = RequestMethod.GET)
    public String createUpazila(@ModelAttribute Upazilla upazila, Model model, HttpServletRequest request) {
        System.out.println("in upazila create");
        model.addAttribute("actionType", "create");
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("districtName", "nameInEnglish");
        } else {
            model.addAttribute("districtName", "nameInBangla");
        }
        model.addAttribute("districtList", this.districtService.getDistrictIoList(null));
        CommonUtility.mapDivisionName(model);
        return "upazila";
    }

    /**
     *
     * @param upazila
     * @param bindingResult
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/upazila/create", method = RequestMethod.POST)
    public String createUpazila(@Valid Upazilla upazila, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors()) {
            return "upazila";
        }
        boolean saveResult = false;
        try {
            System.out.println("in save");
            upazila.setId(new Integer(upazila.getDistrict().getId() + upazila.getCode()));
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage())) {
                upazila.setCode(CommonUtility.getNumberInEnglish(upazila.getCode()));
            }

            upazila.setUpazilaType(UpazilaType.UPAZILA);
            upazila.setCreatedBy((User) session.getAttribute("user"));
            upazila.setCreationDate(Calendar.getInstance());
            saveResult = this.upazillaService.save(upazila);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            throw e;
        }
        if (saveResult) {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/upazila/list";
        } else {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/upazila/create";
        }

    }

    @RequestMapping(value = "/upazila/edit/{id}", method = RequestMethod.GET)
    public String editUpazila(@PathVariable("id") Integer id, @ModelAttribute Upazilla upazila, Model model, boolean upazilaLoadRequired) {
        try {
            System.out.println("upazilaLoadRequired = " + upazilaLoadRequired);
            Locale locale = LocaleContextHolder.getLocale();
            if ("en".equals(locale.getLanguage())) {
                model.addAttribute("districtName", "nameInEnglish");
            } else {
                model.addAttribute("districtName", "nameInBangla");
            }
            if (!upazilaLoadRequired) {
                model.addAttribute("upazilla", this.upazillaService.getUpazilla(id));
                
                model.addAttribute("districtList", this.districtService.getDistrictIoList(null));
                model.addAttribute("isEdit",1);
            }
            CommonUtility.mapDivisionName(model);
            System.out.println("upazila.getCreationDate() = " + upazila.getCreationDate());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return "upazila";
    }

    @RequestMapping(value = "/upazila/edit/{id}", method = RequestMethod.POST)
    public String editUpazila(@PathVariable("id") Integer pk, @Valid @ModelAttribute Upazilla upazila, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            }
            if (bindingResult.hasErrors()) {
                return editUpazila(pk, upazila, model, true);
            }
            if (upazila.getId() == null) {
                upazila.setCreatedBy((User) session.getAttribute("user"));
                upazila.setCreationDate(Calendar.getInstance());
                this.upazillaService.save(upazila);
            } else {
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage())) {
                    upazila.setCode(CommonUtility.getNumberInEnglish(upazila.getCode()));
                }
                upazila.setModifiedBy((User) session.getAttribute("user"));
                upazila.setModificationDate(Calendar.getInstance());
                this.upazillaService.edit(upazila);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/upazila/list";
    }

    /**
     *
     * @param upazillaId
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "upazila/delete/{upazillaId}")
    public String deleteUpazilla(@PathVariable("upazillaId") Integer upazillaId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("in upazilla delete");
        Upazilla upazilla = this.upazillaService.getUpazilla(upazillaId);
        upazilla.setModifiedBy((User) session.getAttribute("user"));
        upazilla.setModificationDate(Calendar.getInstance());
        boolean deleteResult = this.upazillaService.delete(upazilla);
        if (deleteResult) {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        } else {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("deleteFailUpazila", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        return "redirect:/upazila/list";
    }
}

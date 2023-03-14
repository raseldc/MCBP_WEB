/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.UnionInfo;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.model.UserPerScheme;
import com.wfp.lmmis.rm.service.UserService;
import com.wfp.lmmis.types.CoverageArea;
import com.wfp.lmmis.types.CoverageAreaClass;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
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
public class UnionController {

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    UnionService unionService;
    @Autowired
    UpazillaService upazillaService;
    @Autowired
    DistrictService districtService;
    @Autowired
    DivisionService divisionService;
    @Autowired
    SchemeService schemeService;
    @Autowired
    UserService userService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/union/list")
    public String showUnionList(Model model, HttpServletRequest request) {
        CommonUtility.mapDivisionName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "unionList";
    }

    /**
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/union/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationUnionList(HttpServletRequest request, HttpServletResponse response) {
        //Fetch the page number from client
        System.out.println("in pagination controller");
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
//        Integer pageNumber = 0;
        int draw = Integer.parseInt(request.getParameter("draw"));
        int beginIndex = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("length"));

        String divisionId = null, districtId = null, upazilaId = null, unionName = null;
        if (request.getParameter("divisionId") != null && !"".equals(request.getParameter("divisionId"))) {
            divisionId = request.getParameter("divisionId");
        }
        if (request.getParameter("districtId") != null && !"".equals(request.getParameter("districtId"))) {
            districtId = request.getParameter("districtId");
        }
        if (request.getParameter("upazilaId") != null && !"".equals(request.getParameter("upazilaId"))) {
            upazilaId = request.getParameter("upazilaId");
        }
        if (request.getParameter("unionName") != null && !"".equals(request.getParameter("unionName"))) {
            unionName = request.getParameter("unionName");
        }
        List<Object> resultList = unionService.getUnionSearchList(divisionId, divisionId != null, districtId, districtId != null, upazilaId, upazilaId != null, unionName, unionName != null, beginIndex, pageSize);
        List<UnionInfo> unionList = (List<UnionInfo>) resultList.get(0);
        int recordsTotal = (int) resultList.get(1);
        int recordsFiltered = (int) resultList.get(2);
        System.out.println("recordsTotal = " + recordsTotal);
        for (UnionInfo union : unionList) {
            JSONArray ja = new JSONArray();
            Locale locale = LocaleContextHolder.getLocale();

            if ("en".equals(locale.getLanguage())) {
                ja.put(union.getUpazilaNameEn());
            } else {
                ja.put(union.getUpazilaNameBn());
            }
            ja.put(union.getNameBn());
            ja.put(union.getNameEn());
            if ("en".equals(locale.getLanguage())) {
                ja.put(union.getCode());
            } else {
                ja.put(CommonUtility.getNumberInBangla(union.getCode()));
            }
            if (union.isActive() == true) {
                ja.put("<span class=\"glyphicon glyphicon-ok\"></span>");
            } else {
                ja.put("<span class=\"glyphicon glyphicon-remove\"></span>");
            }
            ja.put("<td><a href=\"" + request.getContextPath() + "/union/edit/" + union.getId() + "\" data-toggle=\"tooltip\" title=\"Edit\">\n"
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

    /**
     *
     * @param upazilaId
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/getUnion/{upazilaId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getUnionList(@PathVariable("upazilaId") Integer upazilaId,
            Model model, HttpSession session) {
        return this.unionService.getUnionIoList(upazilaId);
    }

    @RequestMapping(value = "/getMunicipal/{upazilaId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getMunicipalList(@PathVariable("upazilaId") Integer upazilaId, Model model, HttpSession session) {
        return this.unionService.getMunicipalOrCityCorporation(upazilaId, CoverageArea.MUNICIPAL);
    }

    /**
     *
     * @param upazilaId
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/getCityCorporation/{upazilaId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getCityCorporationList(@PathVariable("upazilaId") Integer upazilaId, Model model, HttpSession session) {
        return this.unionService.getMunicipalOrCityCorporation(upazilaId, CoverageArea.CITY_CORPORATION);
    }

    @RequestMapping(value = "/getUnionAndMunicipal/{upazilaId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getUnionAndMunicipal(@PathVariable("upazilaId") Integer upazilaId, Model model, HttpSession session) {
        return this.unionService.getUnionAndMunicipal(upazilaId);
    }

    @RequestMapping(value = "/union/create", method = RequestMethod.GET)
    public String createUnion(@ModelAttribute Union union, Model model, HttpSession session) {
        model.addAttribute("actionType", "create");
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("upazilaName", "nameInEnglish");
        } else {
            model.addAttribute("upazilaName", "nameInBangla");
        }
        model.addAttribute("upazillaList", this.upazillaService.getUpazillaList(null, "MA"));
        CommonUtility.mapDivisionName(model);
        return "union";
    }

    /**
     *
     * @param union
     * @param bindingResult
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/union/create", method = RequestMethod.POST)
    public String createUnion(@Valid Union union, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors()) {
            return "union";
        }
        boolean saveResult = false;
        try {
            System.out.println("in save");
            union.setId(new Integer(union.getUpazilla().getId() + union.getCode()));
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage())) {
                union.setCode(CommonUtility.getNumberInEnglish(union.getCode()));
            }
            union.setCoverageArea(CoverageArea.UNION);
            union.setCoverageAreaClass(CoverageAreaClass.UNION);
            union.setCreatedBy((User) session.getAttribute("user"));
            union.setCreationDate(Calendar.getInstance());
            saveResult = this.unionService.save(union);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            e.printStackTrace();
            throw e;
        }
        if (saveResult) {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/union/list";
        } else {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/union/create";
        }
    }

    @RequestMapping(value = "/union/edit/{id}", method = RequestMethod.GET)
    public String editUnion(@PathVariable("id") Integer id, @ModelAttribute Union union, Model model, boolean unionLoadRequired, HttpSession session) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
//            String schemeShortName = ((UserDetail) session.getAttribute("userDetail")).getScheme().getShortName();
            if ("en".equals(locale.getLanguage())) {
                model.addAttribute("upazilaName", "nameInEnglish");
            } else {
                model.addAttribute("upazilaName", "nameInBangla");
            }
            if (!unionLoadRequired) {
                model.addAttribute("union", this.unionService.getUnion(id));
                model.addAttribute("upazillaList", this.upazillaService.getUpazillaList(null, "MA"));
                model.addAttribute("isEdit", 1);
            }
            CommonUtility.mapDivisionName(model);
            System.out.println("union.getCreationDate() = " + union.getCreationDate());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return "union";
    }

    @RequestMapping(value = "/union/edit/{id}", method = RequestMethod.POST)
    public String editUnion(@PathVariable("id") Integer pk, @Valid @ModelAttribute Union union, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            }
            if (bindingResult.hasErrors()) {
                return editUnion(pk, union, model, true, session);
            }
            if (union.getId() == null) {
                union.setCreatedBy((User) session.getAttribute("user"));
                union.setCreationDate(Calendar.getInstance());
                this.unionService.save(union);
            } else {
                System.out.println("union.creationDate = " + union.getCreationDate());
//                union.setId(new Integer(union.getUpazilla().getId()+union.getCode()));
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage())) {
                    union.setCode(CommonUtility.getNumberInEnglish(union.getCode()));
                }
                union.setCoverageArea(CoverageArea.UNION);
                union.setCoverageAreaClass(CoverageAreaClass.UNION);
                union.setModifiedBy((User) session.getAttribute("user"));
                union.setModificationDate(Calendar.getInstance());
                this.unionService.edit(union);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/union/list";
    }

    @RequestMapping(value = "union/delete/{unionId}")
    public String deleteUnionId(@PathVariable("unionId") Integer unionId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("in union delete");
        Union union = this.unionService.getUnion(unionId);
        union.setModifiedBy((User) session.getAttribute("user"));
        union.setModificationDate(Calendar.getInstance());
        boolean deleteResult = this.unionService.delete(union);
        if (deleteResult) {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        } else {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("deleteFailUnion", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        return "redirect:/union/list";
    }

    //*** This part is for Municipal Or City Corporation

    /**
     *
     * @param municipalOrCityCorporation
     * @param model
     * @param session
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "/municipalOrCityCorporation/create", method = RequestMethod.GET)
    public String createMunicipalOrCityCorporation(@ModelAttribute Union municipalOrCityCorporation, Model model, HttpSession session) throws ExceptionWrapper {
        model.addAttribute("actionType", "create");
        System.out.println("1");
        Locale locale = LocaleContextHolder.getLocale();
//        String userId = ((UserDetail) session.getAttribute("userDetail")).getUserID();
//        System.out.println("2" + userId);
//        User user = userService.getUser(userId);
//        System.out.println("3" + user.getFullNameEn());
//        UserPerScheme userPerScheme = user.getUserPerScheme();
//        System.out.println("4" + userPerScheme.getId());
//        Scheme scheme = userPerScheme.getScheme();
//        System.out.println("5" + scheme.getId());
//        String schemeShortName = scheme.getShortName();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("upazilaName", "nameInEnglish");
            model.addAttribute("districtName", "nameEn");
        } else {
            model.addAttribute("upazilaName", "nameInBangla");
            model.addAttribute("districtName", "nameBn");
        }
        model.addAttribute("municipalOrCityCorporation", new Union());
        model.addAttribute("districtList", this.districtService.getDistrictList(null));
        model.addAttribute("upazilaList", this.upazillaService.getUpazillaList(null, "LMA"));
        List<CoverageArea> coverageAreaList = new ArrayList<>();
        coverageAreaList.add(CoverageArea.MUNICIPAL);
        coverageAreaList.add(CoverageArea.CITY_CORPORATION);
        model.addAttribute("coverageAreaList", coverageAreaList);
        List<CoverageAreaClass> coverageAreaClassList = new ArrayList<>();
        coverageAreaClassList.add(CoverageAreaClass.A_CATEGORY);
        coverageAreaClassList.add(CoverageAreaClass.B_CATEGORY);
        coverageAreaClassList.add(CoverageAreaClass.C_CATEGORY);
        coverageAreaClassList.add(CoverageAreaClass.CITY_CORPORATION);
        model.addAttribute("coverageAreaClassList", coverageAreaClassList);
//        List<Union> umc = this.unionService.getMunicipalOrCityCorporation(351, CoverageArea.CITY_CORPORATION);
//        System.out.println("umc list size = " + umc.size());
        return "municipalOrCityCorporation";
    }

    @RequestMapping(value = "/municipalOrCityCorporation/create", method = RequestMethod.POST)
    public String createMunicipalOrCityCorporation(@Valid Union municipalOrCityCorporation, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors()) {
            return "union";
        }
        boolean saveResult = false;
        try {
            System.out.println("in save");
            System.out.println("");
            municipalOrCityCorporation.setId(new Integer(municipalOrCityCorporation.getUpazilla().getId() + municipalOrCityCorporation.getCode()));
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage())) {
                municipalOrCityCorporation.setCode(CommonUtility.getNumberInEnglish(municipalOrCityCorporation.getCode()));
            }
            municipalOrCityCorporation.setCreatedBy((User) session.getAttribute("user"));
            municipalOrCityCorporation.setCreationDate(Calendar.getInstance());
            saveResult = this.unionService.save(municipalOrCityCorporation);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            throw e;
        }
        if (saveResult) {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/municipalOrCityCorporation/list";
        } else {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/municipalOrCityCorporation/create";
        }
    }

    @RequestMapping(value = "/municipalOrCityCorporation/edit/{id}", method = RequestMethod.GET)
    public String editMunicipalOrCityCorporation(@PathVariable("id") Integer id, @ModelAttribute Union municipalOrCityCorporation, Model model, boolean unionLoadRequired, HttpSession session) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            //String schemeShortName = ((UserDetail) session.getAttribute("userDetail")).getScheme().getShortName();
            if ("en".equals(locale.getLanguage())) {
                model.addAttribute("upazilaName", "nameInEnglish");
                model.addAttribute("districtName", "nameEn");
            } else {
                model.addAttribute("upazilaName", "nameInBangla");
                model.addAttribute("districtName", "nameBn");
            }
            if (!unionLoadRequired) {
                model.addAttribute("municipalOrCityCorporation", this.unionService.getUnion(id));
                model.addAttribute("districtList", this.districtService.getDistrictList(null));
                model.addAttribute("upazilaList", this.upazillaService.getUpazillaList(null, "LMA"));
                List<CoverageArea> coverageAreaList = new ArrayList<>();
                coverageAreaList.add(CoverageArea.MUNICIPAL);
                coverageAreaList.add(CoverageArea.CITY_CORPORATION);
                model.addAttribute("coverageAreaList", coverageAreaList);
                List<CoverageAreaClass> coverageAreaClassList = new ArrayList<>();
                coverageAreaClassList.add(CoverageAreaClass.A_CATEGORY);
                coverageAreaClassList.add(CoverageAreaClass.B_CATEGORY);
                coverageAreaClassList.add(CoverageAreaClass.C_CATEGORY);
                coverageAreaClassList.add(CoverageAreaClass.CITY_CORPORATION);
                model.addAttribute("coverageAreaClassList", coverageAreaClassList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return "municipalOrCityCorporation";
    }

    /**
     *
     * @param pk
     * @param municipalOrCityCorporation
     * @param bindingResult
     * @param session
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/municipalOrCityCorporation/edit/{id}", method = RequestMethod.POST)
    public String editMunicipalOrCityCorporation(@PathVariable("id") Integer pk, @Valid @ModelAttribute Union municipalOrCityCorporation, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            }
            if (bindingResult.hasErrors()) {
                return editUnion(pk, municipalOrCityCorporation, model, true, session);
            }
            if (municipalOrCityCorporation.getId() == null) {
                municipalOrCityCorporation.setCreatedBy((User) session.getAttribute("user"));
                municipalOrCityCorporation.setCreationDate(Calendar.getInstance());
                this.unionService.save(municipalOrCityCorporation);
            } else {
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage())) {
                    municipalOrCityCorporation.setCode(CommonUtility.getNumberInEnglish(municipalOrCityCorporation.getCode()));
                }
                municipalOrCityCorporation.setModifiedBy((User) session.getAttribute("user"));
                municipalOrCityCorporation.setModificationDate(Calendar.getInstance());
                this.unionService.edit(municipalOrCityCorporation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/municipalOrCityCorporation/list";
    }

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/municipalOrCityCorporation/list")
    public String showMunicipalOrCityCorporationList(Model model, HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("displayName", "displayName");
        } else {
            model.addAttribute("displayName", "displayNameBn");
        }
        CommonUtility.mapDivisionName(model);
        List<CoverageArea> coverageAreaList = new ArrayList<>();
        coverageAreaList.add(CoverageArea.MUNICIPAL);
        coverageAreaList.add(CoverageArea.CITY_CORPORATION);
        model.addAttribute("coverageAreaList", coverageAreaList);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "municipalOrCityCorporationList";
    }

    /**
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/municipalOrCityCorporation/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationMunicipalOrCityCorporation(HttpServletRequest request, HttpServletResponse response) {
        //Fetch the page number from client
        System.out.println("in pagination controller");
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
//        Integer pageNumber = 0;
        int draw = Integer.parseInt(request.getParameter("draw"));
        int beginIndex = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("length"));

        String divisionId = null, districtId = null, upazilaId = null;
        CoverageArea coverageArea = null;
        if (request.getParameter("divisionId") != null && !"".equals(request.getParameter("divisionId"))) {
            divisionId = request.getParameter("divisionId");
        }
        if (request.getParameter("districtId") != null && !"".equals(request.getParameter("districtId"))) {
            districtId = request.getParameter("districtId");
        }
        if (request.getParameter("upazilaId") != null && !"".equals(request.getParameter("upazilaId"))) {
            upazilaId = request.getParameter("upazilaId");
        }

        if (request.getParameter("coverageAreaId") != null && !"".equals(request.getParameter("coverageAreaId"))) {
            if ("MUNICIPAL".equals(request.getParameter("coverageAreaId"))) {
                coverageArea = CoverageArea.MUNICIPAL;
            }
            if ("CITY_CORPORATION".equals(request.getParameter("coverageAreaId"))) {
                coverageArea = CoverageArea.CITY_CORPORATION;
            }
        }
        List<Object> resultList = unionService.getMunicipalOrCityCorporationSearchList(divisionId, divisionId != null, districtId, districtId != null, upazilaId, upazilaId != null, coverageArea, beginIndex, pageSize);
        List<UnionInfo> municipalOrCityCorporationList = (List<UnionInfo>) resultList.get(0);
        int recordsTotal = municipalOrCityCorporationList.size();
        int recordsFiltered = municipalOrCityCorporationList.size();
        System.out.println("recordsTotal = " + recordsTotal);
        for (UnionInfo municipalOrCityCorporation : municipalOrCityCorporationList) {
            JSONArray ja = new JSONArray();
            Locale locale = LocaleContextHolder.getLocale();
            ja.put(municipalOrCityCorporation.getNameBn());
            ja.put(municipalOrCityCorporation.getNameEn());

            if ("en".equals(locale.getLanguage())) {
                ja.put(municipalOrCityCorporation.getCode());
                ja.put(municipalOrCityCorporation.getUpazilaNameEn());
            } else {
                ja.put(CommonUtility.getNumberInBangla(municipalOrCityCorporation.getCode()));
                ja.put(municipalOrCityCorporation.getUpazilaNameBn());
            }
            if (municipalOrCityCorporation.isActive() == true) {
                ja.put("<span class=\"glyphicon glyphicon-ok\"></span>");
            } else {
                ja.put("<span class=\"glyphicon glyphicon-remove\"></span>");
            }
            ja.put("<td><a href=\"" + request.getContextPath() + "/municipalOrCityCorporation/edit/" + municipalOrCityCorporation.getId() + "\" data-toggle=\"tooltip\" title=\"Edit\">\n"
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

    /**
     *
     * @param unionId
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "municipalOrCityCorporation/delete/{unionId}")
    public String deleteMunicipalOrCityCorporation(@PathVariable("unionId") Integer unionId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("in MunicipalOrCityCorporation delete");
        Union union = this.unionService.getUnion(unionId);
        union.setModifiedBy((User) session.getAttribute("user"));
        union.setModificationDate(Calendar.getInstance());
        boolean deleteResult = this.unionService.delete(union);
        if (deleteResult) {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        } else {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("deleteFail", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        return "redirect:/municipalOrCityCorporation/list";
    }
}

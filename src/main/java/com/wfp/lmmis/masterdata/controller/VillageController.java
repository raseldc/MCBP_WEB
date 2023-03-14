/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.model.BranchInfo;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Village;
import com.wfp.lmmis.masterdata.model.VillageInfo;
import com.wfp.lmmis.masterdata.service.BankService;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.masterdata.service.VillageService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
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
public class VillageController {

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    VillageService villageService;
    @Autowired
    UpazillaService upazilaService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/village/list")
    public String showVillageList(Model model, HttpServletRequest request) {
        model.addAttribute("upazilaList", this.upazilaService.getUpazillaIoList(null, "MA"));
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("upazilaName", "nameInEnglish");
        } else {
            model.addAttribute("upazilaName", "nameInBangla");
        }
        CommonUtility.mapDivisionName(model);
        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
        return "villageList";
    }

    @RequestMapping(value = "/village/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationUnionList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("response = " + response);
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
        int draw = Integer.parseInt(request.getParameter("draw"));
        int beginIndex = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("length"));

        String upazilaId = null, unionId = null, districtId = null, divisionId = null, villageName = null;

        if (request.getParameter("divisionId") != null && !"".equals(request.getParameter("divisionId"))) {
            divisionId = request.getParameter("divisionId");
        }
        if (request.getParameter("districtId") != null && !"".equals(request.getParameter("districtId"))) {
            districtId = request.getParameter("districtId");
        }
        if (request.getParameter("upazilaId") != null && !"".equals(request.getParameter("upazilaId"))) {
            upazilaId = request.getParameter("upazilaId");
        }
        if (request.getParameter("upazilaId") != null && !"".equals(request.getParameter("upazilaId"))) {
            upazilaId = request.getParameter("upazilaId");
        }
        if (request.getParameter("unionId") != null && !"".equals(request.getParameter("unionId"))) {
            unionId = request.getParameter("unionId");
        }

        if (request.getParameter("villageName") != null && !"".equals(request.getParameter("villageName"))) {
            villageName = request.getParameter("villageName");
        }
        Map parameter = new HashMap();
        parameter.put("divisionId", divisionId);
        parameter.put("districtId", districtId);
        parameter.put("upazilaId", upazilaId);
        parameter.put("unionId", unionId);
        parameter.put("villageName", villageName);
        System.out.println("unionId = " + unionId + " upazilaId = " + upazilaId);
        List<Object> resultList = this.villageService.getVillageSearchListBySearchParameter(parameter, beginIndex, pageSize);
        List<VillageInfo> villageList = (List<VillageInfo>) resultList.get(0);
        int recordsTotal = (int) resultList.get(1);
        int recordsFiltered = (int) resultList.get(2);
        for (VillageInfo villageInfo : villageList) {
            JSONArray ja = new JSONArray();
            ja.put(villageInfo.getNameBn());
            ja.put(villageInfo.getNameEn());
            ja.put(villageInfo.getCode());
            ja.put(villageInfo.getWardNo());
            ja.put(villageInfo.getUnionName());
            ja.put(villageInfo.getUpazilaName());
            ja.put(villageInfo.getDistrictName());
            if (villageInfo.isActive()) {
                ja.put("<span class=\"glyphicon glyphicon-ok\"></span>");
            } else {
                ja.put("<span class=\"glyphicon glyphicon-remove\"></span>");
            }
            ja.put("<td><a href=\"" + request.getContextPath() + "/village/edit/" + villageInfo.getId() + "\" data-toggle=\"tooltip\" title=\"Edit\">\n"
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

    @RequestMapping(value = "/village/create", method = RequestMethod.GET)
    public String createVillage(@ModelAttribute Village village, Model model) {
        model.addAttribute("upazilaList", this.upazilaService.getUpazillaIoList(null, "MA"));
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("upazilaName", "nameInEnglish");
        } else {
            model.addAttribute("upazilaName", "nameInBangla");
        }
        model.addAttribute("actionType", "create");
        CommonUtility.getWardNoList(model);
        CommonUtility.mapDivisionName(model);
        return "village";
    }

    @RequestMapping(value = "/village/create", method = RequestMethod.POST)
    public String createVillage(@Valid Village village, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors()) {
            return "village";
        }
        boolean saveResult = false;
        try {
            village.setCreatedBy((User) session.getAttribute("user"));
            village.setCreationDate(Calendar.getInstance());
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage())) {
                village.setCode(CommonUtility.getNumberInEnglish(village.getCode()));
            }
            saveResult = this.villageService.save(village);
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            throw e;
        }
        if (saveResult) {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        } else {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        return "redirect:/village/list";
    }

    @RequestMapping(value = "/village/edit/{id}", method = RequestMethod.GET)
    public String editVillage(@PathVariable("id") Integer id, @ModelAttribute Village village, Model model, boolean unionLoadRequired) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            if ("en".equals(locale.getLanguage())) {
                model.addAttribute("upazilaName", "nameInEnglish");
            } else {
                model.addAttribute("upazilaName", "nameInBangla");
            }
            if (!unionLoadRequired) {
                model.addAttribute("village", this.villageService.getVillage(id));
                model.addAttribute("upazilaList", this.upazilaService.getUpazillaIoList(null, "MA"));
                CommonUtility.getWardNoList(model);
                CommonUtility.mapDivisionName(model);
                model.addAttribute("isEdit", 1);
            }
            model.addAttribute("actionType", "edit");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return "village";
    }

    /**
     *
     * @param pk
     * @param village
     * @param bindingResult
     * @param session
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/village/edit/{id}", method = RequestMethod.POST)
    public String editVillage(@PathVariable("id") Integer pk, @Valid @ModelAttribute Village village, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            }
            if (village.getUnion() != null && village.getUpazila() != null && bindingResult.hasErrors()) {
                return editVillage(pk, village, model, true);
            }
            village.setModifiedBy((User) session.getAttribute("user"));
            village.setModificationDate(Calendar.getInstance());
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage())) {
                village.setCode(CommonUtility.getNumberInEnglish(village.getCode()));
            }
            boolean saveResult = false;
            saveResult = this.villageService.edit(village);
            if (saveResult) {
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            } else {
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            }

            return "redirect:/village/list";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @RequestMapping("/getVillage/{unionId}/{wardNo}")
    @ResponseBody
    public List<ItemObject> getUpazilaList(@PathVariable("unionId") Integer unionId, @PathVariable("wardNo") Integer wardNo, Model model, HttpSession session) {
        System.out.println("in up con");
        return this.villageService.getVillageIoList(wardNo, unionId);
    }
//    @RequestMapping(value = "/branchList/{bankId}", method = RequestMethod.GET)
//    public String getBranchList(@PathVariable("bankId") Integer bankId, Model model)
//    {
//        List<Branch> branchList = this.branchService.getBranchList(bankId);
//        model.addAttribute("branchList", this.branchService.getBranchList(bankId));
////        loadDefault(model);
//        return "branch";
//    }
//
//    @RequestMapping(value = "/getBranch/{bankId}/{districtId}", method = RequestMethod.GET)
//    @ResponseBody
//    public List<ItemObject> getBranchIoList(@PathVariable("bankId") Integer bankId, @PathVariable("districtId") Integer districtId, Model model)
//    {
//        System.out.println("getBranchIoList");
//        List<ItemObject> branchList = this.branchService.getBranchIoList(bankId, districtId);
//        return branchList;
//    }
//
//    @RequestMapping(value = "/branch/edit/{id}", method = RequestMethod.GET)
//    public String editBranch(@PathVariable("id") Integer id, @ModelAttribute Branch branch, Model model, boolean branchLoadRequired)
//    {
//        try
//        {
//            System.out.println("branchLoadRequired = " + branchLoadRequired);
//            Locale locale = LocaleContextHolder.getLocale();
//            if ("en".equals(locale.getLanguage()))
//            {
//                model.addAttribute("bankName", "nameInEnglish");
//                model.addAttribute("districtName", "nameInEnglish");
//            }
//            else
//            {
//                model.addAttribute("bankName", "nameInBangla");
//                model.addAttribute("districtName", "nameInBangla");
//            }
//            if (!branchLoadRequired)
//            {
//                model.addAttribute("branch", this.branchService.getBranch(id));
//                model.addAttribute("bankList", this.bankService.getBankList());
//                model.addAttribute("districtList", this.districtService.getDistrictIoList(null));
//            }
//            System.out.println("branch.getCreationDate() = " + branch.getCreationDate());
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            throw e;
//        }
//
//        return "branch";
//    }
//
//    @RequestMapping(value = "/branch/edit/{id}", method = RequestMethod.POST)
//    public String editBranch(@PathVariable("id") Integer pk, @Valid @ModelAttribute Branch branch, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
//    {
//        try
//        {
//            List<FieldError> errors = bindingResult.getFieldErrors();
//            for (FieldError error : errors)
//            {
//                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
//            }
//            if (bindingResult.hasErrors())
//            {
//                return editBranch(pk, branch, model, true);
//            }
//            if (branch.getId() == null)
//            {
//                branch.setCreatedBy((User) session.getAttribute("user"));
//                branch.setCreationDate(Calendar.getInstance());
//                this.branchService.save(branch);
//            }
//            else
//            {
//                System.out.println("branch.creationDate = " + branch.getCreationDate());
//                branch.setModifiedBy((User) session.getAttribute("user"));
//                branch.setModificationDate(Calendar.getInstance());
//                Locale locale = LocaleContextHolder.getLocale();
//                if ("bn".equals(locale.getLanguage()))
//                {
//                    branch.setCode(CommonUtility.getNumberInEnglish(branch.getCode()));
//                    branch.setRoutingNumber(Integer.parseInt(CommonUtility.getNumberInEnglish(branch.getRoutingNumber().toString())));
//                }
//                this.branchService.edit(branch);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
//        redirectAttributes.addFlashAttribute("message", controllerMessage);
//        return "redirect:/branch/list";
//    }
//
//    @RequestMapping(value = "branch/delete/{branchId}")
//    public String deleteBranch(@PathVariable("branchId") Integer branchId, Model model, HttpSession session, RedirectAttributes redirectAttributes)
//    {
//        System.out.println("in branch delete");
//        Branch branch = this.branchService.getBranch(branchId);
//        branch.setModifiedBy((User) session.getAttribute("user"));
//        branch.setModificationDate(Calendar.getInstance());
//        this.branchService.delete(branch);
//        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
//        redirectAttributes.addFlashAttribute("message", controllerMessage);
//        return "redirect:/branch/list";
//    }
}

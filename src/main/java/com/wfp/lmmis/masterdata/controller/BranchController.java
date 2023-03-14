/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.model.Bank;
import com.wfp.lmmis.masterdata.model.Branch;
import com.wfp.lmmis.masterdata.model.BranchInfo;
import com.wfp.lmmis.masterdata.service.BankService;
import com.wfp.lmmis.masterdata.service.BranchService;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
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
public class BranchController {

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    BranchService branchService;
    @Autowired
    BankService bankService;
    @Autowired
    DistrictService districtService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/branch/list")
    public String showBranchList(Model model) {
        System.out.println("branch list");
        List<Branch> branchList = this.branchService.getBranchList(null);
        Locale locale = LocaleContextHolder.getLocale();
        if ("bn".equals(locale.getLanguage())) {
            for (Branch branch : branchList) {
                branch.setCode(CommonUtility.getNumberInBangla(branch.getCode()));
            }
        }
        List<Bank> bankList = this.bankService.getBankList();
        List<ItemObject> districtList = this.districtService.getDistrictIoList(null);

        model.addAttribute("bankList", bankList);
        model.addAttribute("districtList", districtList);

        return "branchList";
    }

    @RequestMapping(value = "/branch/list", method = RequestMethod.POST)
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

        String districtId = null, bankId = null, routingNumber = null;

        if (request.getParameter("districtId") != null && !"".equals(request.getParameter("districtId"))) {
            districtId = request.getParameter("districtId");
        }
        if (request.getParameter("bankId") != null && !"".equals(request.getParameter("bankId"))) {
            bankId = request.getParameter("bankId");
        }
        if (request.getParameter("routingNumber") != null && !"".equals(request.getParameter("routingNumber"))) {
            routingNumber = request.getParameter("routingNumber");
        }
        Map parameter = new HashMap();
        parameter.put("bankId", bankId);
        parameter.put("districtId", districtId);
        parameter.put("routingNumber", routingNumber);
        System.out.println("bank = " + bankId + " district = " + districtId);
        List<Object> resultList = branchService.getBranchSearchListBySearchParameter(parameter, beginIndex, pageSize);
        List<BranchInfo> branchList = (List<BranchInfo>) resultList.get(0);
        int recordsTotal = (int) resultList.get(1);
        int recordsFiltered = (int) resultList.get(2);
        System.out.println("recordsTotal = " + recordsTotal);
        for (BranchInfo branch : branchList) {
            JSONArray ja = new JSONArray();
            Locale locale = LocaleContextHolder.getLocale();

            if ("en".equals(locale.getLanguage())) {
                ja.put(branch.getBankNameEn());
                ja.put(branch.getNameBn());
                ja.put(branch.getNameEn());
                ja.put(branch.getCode());
                ja.put(branch.getDistrictNameEn());
                ja.put(branch.getRoutingNumber());
                ja.put(branch.getAddress());
            } else {
                ja.put(branch.getBankNameBn());
                ja.put(branch.getNameBn());
                ja.put(branch.getNameEn());
                ja.put(CommonUtility.getNumberInBangla(branch.getCode()));
                ja.put(branch.getDistrictNameBn());
                ja.put(CommonUtility.getNumberInBangla(branch.getRoutingNumber().toString()));
                ja.put(branch.getAddress());
            }
            if (branch.isActive() == true) {
                ja.put("<span class=\"glyphicon glyphicon-ok\"></span>");
            } else {
                ja.put("<span class=\"glyphicon glyphicon-remove\"></span>");
            }
            ja.put("<td><a href=\"" + request.getContextPath() + "/branch/edit/" + branch.getId() + "\" data-toggle=\"tooltip\" title=\"Edit\">\n"
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

    @RequestMapping(value = "/branch/create", method = RequestMethod.GET)
    public String createBranch(@ModelAttribute Branch branch, Model model) {
        System.out.println("in branch create");
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("bankName", "nameInEnglish");
            model.addAttribute("districtName", "nameInEnglish");
        } else {
            model.addAttribute("bankName", "nameInBangla");
            model.addAttribute("districtName", "nameInBangla");
        }
        model.addAttribute("bankList", this.bankService.getBankList());
        model.addAttribute("districtList", this.districtService.getDistrictIoList(null));
        model.addAttribute("actionType", "create");
        return "branch";
    }

    /**
     *
     * @param branch
     * @param bindingResult
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/branch/create", method = RequestMethod.POST)
    public String createBranch(@Valid Branch branch, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "branch";
        }
        boolean saveResult = false;
        try {
            System.out.println("in save branch");
            branch.setCreatedBy((User) session.getAttribute("user"));
            branch.setCreationDate(Calendar.getInstance());
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage())) {
                branch.setCode(CommonUtility.getNumberInEnglish(branch.getCode()));
                branch.setRoutingNumber(CommonUtility.getNumberInEnglish(branch.getRoutingNumber()));
            }
            saveResult = this.branchService.save(branch);
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
        return "redirect:/branch/list";
    }

    @RequestMapping(value = "/branch/{id}", method = RequestMethod.GET)
    public String getBranch(@PathVariable("id") Integer id, Model model) {
        System.out.println("in branch with id");
        model.addAttribute("branch", this.branchService.getBranch(id));
//        loadDefault(model);
        return "branch";
    }

    @RequestMapping(value = "/branchList/{bankId}", method = RequestMethod.GET)
    public String getBranchList(@PathVariable("bankId") Integer bankId, Model model) {
        List<Branch> branchList = this.branchService.getBranchList(bankId);
        model.addAttribute("branchList", this.branchService.getBranchList(bankId));
//        loadDefault(model);
        return "branch";
    }

    /**
     *
     * @param bankId
     * @param districtId
     * @param model
     * @return
     */
    @RequestMapping(value = "/getBranch/{bankId}/{districtId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getBranchIoList(@PathVariable("bankId") Integer bankId, @PathVariable("districtId") Integer districtId, Model model) {
        System.out.println("getBranchIoList");
        List<ItemObject> branchList = this.branchService.getBranchIoList(bankId, districtId);
        return branchList;
    }

    @RequestMapping(value = "/branch/edit/{id}", method = RequestMethod.GET)
    public String editBranch(@PathVariable("id") Integer id, @ModelAttribute Branch branch, Model model, boolean branchLoadRequired) {
        try {
            System.out.println("branchLoadRequired = " + branchLoadRequired);
            Locale locale = LocaleContextHolder.getLocale();
            if ("en".equals(locale.getLanguage())) {
                model.addAttribute("bankName", "nameInEnglish");
                model.addAttribute("districtName", "nameInEnglish");
            } else {
                model.addAttribute("bankName", "nameInBangla");
                model.addAttribute("districtName", "nameInBangla");
            }
            if (!branchLoadRequired) {
                model.addAttribute("branch", this.branchService.getBranch(id));
                model.addAttribute("bankList", this.bankService.getBankList());
                model.addAttribute("districtList", this.districtService.getDistrictIoList(null));
            }
            System.out.println("branch.getCreationDate() = " + branch.getCreationDate());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return "branch";
    }

    @RequestMapping(value = "/branch/edit/{id}", method = RequestMethod.POST)
    public String editBranch(@PathVariable("id") Integer pk, @Valid @ModelAttribute Branch branch, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            }
            if (bindingResult.hasErrors()) {
                return editBranch(pk, branch, model, true);
            }
            if (branch.getId() == null) {
                branch.setCreatedBy((User) session.getAttribute("user"));
                branch.setCreationDate(Calendar.getInstance());
                this.branchService.save(branch);
            } else {
                System.out.println("branch.creationDate = " + branch.getCreationDate());
                branch.setModifiedBy((User) session.getAttribute("user"));
                branch.setModificationDate(Calendar.getInstance());
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage())) {
                    branch.setCode(CommonUtility.getNumberInEnglish(branch.getCode()));
                    branch.setRoutingNumber(CommonUtility.getNumberInEnglish(branch.getRoutingNumber().toString()));
                }

                branch.setAccountNoLength(Integer.parseInt(CommonUtility.getNumberInEnglish(branch.getAccountNoLength().toString())));
                this.branchService.edit(branch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/branch/list";
    }

    @RequestMapping(value = "branch/delete/{branchId}")
    public String deleteBranch(@PathVariable("branchId") Integer branchId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("in branch delete");
        Branch branch = this.branchService.getBranch(branchId);
        branch.setModifiedBy((User) session.getAttribute("user"));
        branch.setModificationDate(Calendar.getInstance());
        this.branchService.delete(branch);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/branch/list";
    }
}

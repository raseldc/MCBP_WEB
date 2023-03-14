/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.masterdata.model.PostOfficeBranch;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.PostOfficeBranchService;
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
public class PostOfficeBranchController
{
    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    PostOfficeBranchService postOfficeBranchService;
    @Autowired
    DistrictService districtService;
    Localizer localizer = Localizer.getBrowserLocalizer();
    
    @RequestMapping(value = "/getPostOfficeBranchList/{districtId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getBranchPostOfficeIoList(@PathVariable("districtId") Integer districtId, Model model)
    {
        List<ItemObject> branchList = this.postOfficeBranchService.getPostOfficeBranchIoList(districtId);
        return branchList;
    }
    
    @RequestMapping(value = "/postOfficeBranch/list")
    public String showPostOfficeBranchList(Model model)

    {
        List<PostOfficeBranch> postOfficeBranchList = this.postOfficeBranchService.getPostOfficeBranchList(null);
        Locale locale = LocaleContextHolder.getLocale();
        if ("bn".equals(locale.getLanguage()))
        {
            for (PostOfficeBranch postOfficeBranch : postOfficeBranchList)
            {
                postOfficeBranch.setCode(CommonUtility.getNumberInBangla(postOfficeBranch.getCode()));
            }
        }
        model.addAttribute("districtList", this.districtService.getDistrictIoList(null));
        
        return "postOfficeBranchList";
    }
    
    @RequestMapping(value = "/postOfficeBranch/list", method = RequestMethod.POST)
    public @ResponseBody
    void paginationPostOfficeBranchList(HttpServletRequest request, HttpServletResponse response)
    {
        //Fetch the page number from client
        System.out.println("in pagination controller");
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
//        Integer pageNumber = 0;
        int draw = Integer.parseInt(request.getParameter("draw"));
        int beginIndex = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("length"));

        String districtId = null;

        if (request.getParameter("districtId") != null && !"".equals(request.getParameter("districtId")))
        {
            districtId = request.getParameter("districtId");
        }
        Map parameter = new HashMap();
        parameter.put("districtId", districtId);
        
        List<Object> resultList = postOfficeBranchService.getPostOfficeBranchSearchListBySearchParameter(parameter, beginIndex, pageSize);
        List<PostOfficeBranch> postOfficeBranchList = (List<PostOfficeBranch>) resultList.get(0);
        int recordsTotal = (int) resultList.get(1);
        int recordsFiltered = (int) resultList.get(2);
        System.out.println("recordsTotal = " + recordsTotal);
        for (PostOfficeBranch postOfficeBranch : postOfficeBranchList)
        {
            JSONArray ja = new JSONArray();
            Locale locale = LocaleContextHolder.getLocale();
            ja.put(postOfficeBranch.getNameInBangla());
            ja.put(postOfficeBranch.getNameInEnglish());

            if ("en".equals(locale.getLanguage()))
            {
                ja.put(postOfficeBranch.getDistrict().getNameInEnglish());
                ja.put(postOfficeBranch.getAddress());
                ja.put(postOfficeBranch.getCode());
                ja.put(postOfficeBranch.getRoutingNumber());
            }
            else
            {
                ja.put(postOfficeBranch.getDistrict().getNameInBangla());
                ja.put(postOfficeBranch.getAddress());
                ja.put(CommonUtility.getNumberInBangla(postOfficeBranch.getCode()));
                ja.put(CommonUtility.getNumberInBangla(postOfficeBranch.getRoutingNumber().toString()));
            }
            if (postOfficeBranch.isActive() == true)
            {
                ja.put("<span class=\"glyphicon glyphicon-ok\"></span>");
            }
            else
            {
                ja.put("<span class=\"glyphicon glyphicon-remove\"></span>");
            }
            ja.put("<td><a href=\"" + request.getContextPath() + "/postOfficeBranch/edit/" + postOfficeBranch.getId() + "\" data-toggle=\"tooltip\" title=\"Edit\">\n"
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
    
    @RequestMapping(value = "/postOfficeBranch/create", method = RequestMethod.GET)
    public String createPostOfficeBranch(@ModelAttribute PostOfficeBranch postOfficeBranch, Model model)
    {
        
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage()))
        {
            model.addAttribute("districtName", "nameInEnglish");
        }
        else
        {
            model.addAttribute("districtName", "nameInBangla");
        }
        model.addAttribute("districtList", this.districtService.getDistrictIoList(null));
        model.addAttribute("actionType", "create");
        return "postOfficeBranch";
    }
    
    @RequestMapping(value = "/postOfficeBranch/create", method = RequestMethod.POST)
    public String createPostOfficeBranch(@ModelAttribute PostOfficeBranch postOfficeBranch, BindingResult bindingResult,Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors())
        {
            return "postOfficeBranch";
        }
        boolean saveResult = false;
        
        try
        {
            Locale locale = LocaleContextHolder.getLocale();
            if ("bn".equals(locale.getLanguage()))
            {
                postOfficeBranch.setCode(CommonUtility.getNumberInEnglish(postOfficeBranch.getCode()));
                postOfficeBranch.setRoutingNumber(Integer.parseInt(CommonUtility.getNumberInEnglish(postOfficeBranch.getRoutingNumber().toString())));
            }
            postOfficeBranch.setCreatedBy((User) session.getAttribute("user"));
            postOfficeBranch.setCreationDate(Calendar.getInstance());
            saveResult = this.postOfficeBranchService.save(postOfficeBranch);
            
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
            return "redirect:/postOfficeBranch/list";
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("createFailed", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/postOfficeBranch/create";
        }
    }
    
    @RequestMapping(value = "/postOfficeBranch/edit/{id}", method = RequestMethod.GET)
    public String editPostOfficeBranch(@PathVariable("id") Integer id,@ModelAttribute PostOfficeBranch postOfficeBranch, Model model, boolean postOfficeBranchLoadRequired)
    {
        
        Locale locale = LocaleContextHolder.getLocale();
        if ("en".equals(locale.getLanguage()))
        {
            model.addAttribute("districtName", "nameInEnglish");
        }
        else
        {
            model.addAttribute("districtName", "nameInBangla");
        }
        if(!postOfficeBranchLoadRequired)
        {
        model.addAttribute("postOfficeBranch",postOfficeBranchService.getPostOfficeBranch(id));
        }
        model.addAttribute("districtList", this.districtService.getDistrictIoList(null));
        model.addAttribute("actionType", "edit");
        return "postOfficeBranch";
    }
    
    @RequestMapping(value = "/postOfficeBranch/edit/{id}", method = RequestMethod.POST)
    public String editPostOfficeBranch(@PathVariable("id") Integer pk, @Valid @ModelAttribute PostOfficeBranch postOfficeBranch, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
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
                return editPostOfficeBranch(pk, postOfficeBranch, model, true);
            }
            if (postOfficeBranch.getId() == null)
            {
                postOfficeBranch.setCreatedBy((User) session.getAttribute("user"));
                postOfficeBranch.setCreationDate(Calendar.getInstance());
                this.postOfficeBranchService.save(postOfficeBranch);
            }
            else
            {
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage()))
                {
                    postOfficeBranch.setCode(CommonUtility.getNumberInEnglish(postOfficeBranch.getCode()));
                    postOfficeBranch.setRoutingNumber(Integer.parseInt(CommonUtility.getNumberInEnglish(postOfficeBranch.getRoutingNumber().toString())));
                }
//                district.setId(new Integer(district.getCode()));
                postOfficeBranch.setModifiedBy((User) session.getAttribute("user"));
                postOfficeBranch.setModificationDate(Calendar.getInstance());
                this.postOfficeBranchService.edit(postOfficeBranch);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/postOfficeBranch/list";
    }
    
    /**
     *
     * @param postOfficeBranchId
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "postOfficeBranch/delete/{postOfficeBranchId}")
    public String deletePostOfficeBranch(@PathVariable("postOfficeBranchId") Integer postOfficeBranchId, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        PostOfficeBranch postOfficeBranch = this.postOfficeBranchService.getPostOfficeBranch(postOfficeBranchId);
        postOfficeBranch.setModifiedBy((User) session.getAttribute("user"));
        postOfficeBranch.setModificationDate(Calendar.getInstance());
        this.postOfficeBranchService.delete(postOfficeBranch);
        return "redirect:/postOfficeBranch/list";
    }
    
}

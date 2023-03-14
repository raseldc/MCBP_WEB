/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.payroll.controller;

import com.wfp.lmmis.enums.ServiceType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.model.ServiceSettings;
import com.wfp.lmmis.payroll.model.PaymentCycle;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.payroll.service.PaymentCycleService;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.service.ServiceSettingsService;
import com.wfp.lmmis.utility.ApplicationConstants;
import com.wfp.lmmis.utility.CommonUtility;
import static com.wfp.lmmis.utility.CommonUtility.mapFiscalYearName;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PaymentCycleController
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    private PaymentCycleService paymentCycleService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private FiscalYearService fiscalYearService;
    @Autowired
    private ServiceSettingsService serviceSettingsService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    public void mapGlobalVariables(Model m)
    {
        m.addAttribute("dateFormat", "yy-mm-dd");
    }

    private void mapParentPaymentCycleName(Model model, Integer schemeId)
    {
        Locale locale = LocaleContextHolder.getLocale();
        List<PaymentCycle> paymentCycleList = this.paymentCycleService.getParentPaymentCycleList(schemeId);
        model.addAttribute("parentCycleList", paymentCycleList);
        if ("en".equals(locale.getLanguage()))
        {
            model.addAttribute("parentCycleName", "nameInEnglish");
        }
        else
        {
            model.addAttribute("parentCycleName", "nameInBangla");
        }
    }

    /**
     *
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/paymentCycle/list")
    public String showPaymentCycleList(Model model, HttpSession session)
    {
        System.out.println("paymentcycle list");
        model.addAttribute("paymentCycleList", this.paymentCycleService.getPaymentCycleList(null));
        return "paymentCycleList";
    }

    @RequestMapping(value = "/paymentCycle/create", method = RequestMethod.GET)
    public String createPaymentCycle(@ModelAttribute PaymentCycle paymentcycle, Model model, HttpSession session)
    {
        mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        mapParentPaymentCycleName(model, null);
        mapNumberList(model);
        CommonUtility.mapFiscalYearName(model);
        model.addAttribute("actionType", "create");
        return "paymentCycle";
    }

    private void mapNumberList(Model model)
    {
//        List<Integer> range = IntStream.rangeClosed(1, 10)
//                .boxed().collect(Collectors.toList());
        char[] chars;
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage().equals("bn"))
        {
            chars = "১২৩৪৫".toCharArray();
        }
        else
        {
            chars = "12345".toCharArray();
        }
        List<String> listC = new ArrayList<>();
        for (char c : chars)
        {
            listC.add(c + "");
        }
        model.addAttribute("numberList", listC);
    }

    @RequestMapping(value = "/paymentCycle/create", method = RequestMethod.POST)
    public String createPaymentCycle(@Valid PaymentCycle paymentcycle, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        if (bindingResult.hasErrors())
        {
            return createPaymentCycle(paymentcycle, model, session);
        }
        try
        {
            if (paymentcycle.getParentPaymentCycle().getId() == null)
            {
                paymentcycle.setParentPaymentCycle(null);
            }
            paymentcycle.setScheme(schemeService.getScheme(paymentcycle.getScheme().getId()));
            paymentcycle.setMinistryCode(ApplicationConstants.MINISTRY_CODE);
            paymentcycle.setFiscalYear(fiscalYearService.getFiscalYear(paymentcycle.getFiscalYear().getId()));
            paymentcycle.setCreatedBy((User) session.getAttribute("user"));
            paymentcycle.setCreationDate(Calendar.getInstance());
            this.paymentCycleService.save(paymentcycle);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/paymentCycle/list";
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw new ExceptionWrapper(e.getMessage());
        }
    }

    @RequestMapping(value = "/paymentCycle/edit/{id}", method = RequestMethod.GET)
    public String editPaymentCycle(@PathVariable("id") Integer id, @ModelAttribute PaymentCycle paymentcycle, Model model, HttpSession session, boolean paymentcycleLoadRequired)
    {
        try
        {
            mapGlobalVariables(model);
            CommonUtility.mapSchemeName(model);
            mapFiscalYearName(model);
            mapParentPaymentCycleName(model, null);
            mapNumberList(model);
            if (!paymentcycleLoadRequired)
            {
                model.addAttribute("paymentCycle", this.paymentCycleService.getPaymentCycle(id));
            }
            System.out.println("paymentcycle.getCreationDate() = " + paymentcycle.getCreationDate());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return "paymentCycle";
    }

    /**
     *
     * @param month
     * @param locale
     * @return
     */
    public String getMonthName(int month, Locale locale)
    {
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] monthNames = symbols.getMonths();
        return monthNames[month - 1];
    }

    @RequestMapping(value = "/paymentCycle/edit/{id}", method = RequestMethod.POST)
    public String editPaymentCycle(@PathVariable("id") Integer pk, @Valid @ModelAttribute PaymentCycle paymentcycle, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        try
        {
            if (bindingResult.hasErrors())
            {
                return editPaymentCycle(pk, paymentcycle, model, session, true);
            }
            if (paymentcycle.getParentPaymentCycle() == null || paymentcycle.getParentPaymentCycle().getId() == null)
            {
                paymentcycle.setParentPaymentCycle(null);
            }
            paymentcycle.setScheme(schemeService.getScheme(paymentcycle.getScheme().getId()));
            paymentcycle.setMinistryCode(ApplicationConstants.MINISTRY_CODE);
            paymentcycle.setFiscalYear(fiscalYearService.getFiscalYear(paymentcycle.getFiscalYear().getId()));
            paymentcycle.setModifiedBy((User) session.getAttribute("user"));
            paymentcycle.setModificationDate(Calendar.getInstance());
            this.paymentCycleService.edit(paymentcycle);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/paymentCycle/list";
        }
        catch (ExceptionWrapper e)
        {
            throw new ExceptionWrapper("Error in payment cycle update!\n " + e.getMessage());
        }

    }

    @RequestMapping(value = "paymentCycle/delete/{paymentCycleId}")
    public String deletePaymentCycle(@PathVariable("paymentCycleId") Integer paymentcycleId, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper
    {
        PaymentCycle paymentCycle = this.paymentCycleService.getPaymentCycle(paymentcycleId);
        paymentCycle.setModifiedBy((User) session.getAttribute("user"));
        paymentCycle.setModificationDate(Calendar.getInstance());

        boolean deleteResult = this.paymentCycleService.delete(paymentCycle);
        if (deleteResult)
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }
        else
        {
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.DANGER, localizer.getLocalizedText("deleteFailPaymentCycle", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
        }

//        this.paymentCycleService.delete(paymentCycle);
//        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
//        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/paymentCycle/list";
    }

    @RequestMapping(value = "/checkUniquePaymentCycle", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkUniquePaymentCycle(HttpSession session, Integer schemeId, String paymentCycleName, Integer paymentCycleId)
    {
        boolean result = this.paymentCycleService.checkUniquePaymentCycle(schemeId, paymentCycleId, paymentCycleName);
        System.out.println("result = " + result);

        return result;
    }

    /**
     * this method is used to load parent cycles when user selects scheme in
     * payment cycle page
     *
     * @param schemeId
     * @return
     */
    @RequestMapping(value = "/getParentPaymentCycleIoListByScheme/{schemeId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ItemObject> getParentPaymentCycleListByScheme(@PathVariable("schemeId") Integer schemeId, Model model, HttpSession session)
    {
        List<ItemObject> paymentCyclelist = this.paymentCycleService.getParentPaymentCycleIoListByScheme(schemeId);
        return paymentCyclelist;
    }

    @RequestMapping(value = "/getPaymentCycle", method = RequestMethod.POST)
    @ResponseBody
    public CustomInfo getPaymentCycle(Integer paymentCycleId)
    {
        PaymentCycle paymentCycle = this.paymentCycleService.getPaymentCycle(paymentCycleId);

        Calendar startDate = paymentCycle.getStartDate();
        int startMonth = startDate.get(Calendar.MONTH);
        int startYear = startDate.get(Calendar.YEAR);
        String startMonthYear = String.format("%02d\n", startMonth + 1) + "-" + startYear;

        Calendar endDate = paymentCycle.getEndDate();
        int endMonth = endDate.get(Calendar.MONTH);
        int endYear = endDate.get(Calendar.YEAR);
        String endMonthYear = String.format("%02d\n", endMonth + 1) + "-" + endYear;

        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage().equals("bn"))
        {
            startMonthYear = CommonUtility.getNumberInBangla(startMonthYear);
            endMonthYear = CommonUtility.getNumberInBangla(endMonthYear);
        }
        return new CustomInfo(paymentCycle.getId(), paymentCycle.getFiscalYear().getId(), paymentCycle.getNameInBangla(), paymentCycle.getNameInEnglish(), startMonthYear, endMonthYear);
    }

}

class CustomInfo
{

    Integer cycleId;
    Integer fiscalYearId;
    String cycleNameBn;
    String cycleNameEn;
    String fromMonthYear;
    String toMonthYear;

    public CustomInfo(Integer cycleId, Integer fiscalYearId, String cyleNameBn, String cycleNameEn, String fromMonth, String toMonth)
    {
        this.cycleId = cycleId;
        this.fiscalYearId = fiscalYearId;
        this.cycleNameBn = cyleNameBn;
        this.cycleNameEn = cycleNameEn;
        this.fromMonthYear = fromMonth;
        this.toMonthYear = toMonth;
    }

    public Integer getCycleId()
    {
        return cycleId;
    }

    public void setCycleId(Integer cycleId)
    {
        this.cycleId = cycleId;
    }

    public Integer getFiscalYearId()
    {
        return fiscalYearId;
    }

    public void setFiscalYearId(Integer fiscalYearId)
    {
        this.fiscalYearId = fiscalYearId;
    }

    public String getCycleNameBn()
    {
        return cycleNameBn;
    }

    public void setCycleNameBn(String cycleNameBn)
    {
        this.cycleNameBn = cycleNameBn;
    }

    public String getCycleNameEn()
    {
        return cycleNameEn;
    }

    public void setCycleNameEn(String cycleNameEn)
    {
        this.cycleNameEn = cycleNameEn;
    }

    public String getFromMonthYear()
    {
        return fromMonthYear;
    }

    public void setFromMonthYear(String fromMonthYear)
    {
        this.fromMonthYear = fromMonthYear;
    }

    public String getToMonthYear()
    {
        return toMonthYear;
    }

    public void setToMonthYear(String toMonthYear)
    {
        this.toMonthYear = toMonthYear;
    }

}

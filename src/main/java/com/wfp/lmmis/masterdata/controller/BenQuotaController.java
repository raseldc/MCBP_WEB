/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.masterdata.controller;

import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.masterdata.forms.BeneficiaryQuotaForm;
import com.wfp.lmmis.masterdata.model.BenQuota;
import com.wfp.lmmis.masterdata.model.District;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.model.Factory;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.model.Union;
import com.wfp.lmmis.masterdata.model.Upazilla;
import com.wfp.lmmis.masterdata.service.BenQuotaService;
import com.wfp.lmmis.masterdata.service.DistrictService;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.FactoryService;
import com.wfp.lmmis.masterdata.service.UnionService;
import com.wfp.lmmis.masterdata.service.UpazillaService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.types.FactoryType;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.ItemObject;
import com.wfp.lmmis.utility.Localizer;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Philip
 */
@Controller
public class BenQuotaController
{

    @Autowired
    private DistrictService districtService;

    @Autowired
    private UpazillaService upazillaService;

    @Autowired
    private UnionService unionService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private FiscalYearService fiscalYearService;

    @Autowired
    private BenQuotaService benQuotaService;

    @Autowired
    private FactoryService factoryService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/beneficiaryQuota", method = RequestMethod.GET)
    public String getBeneficiaryQuota(@ModelAttribute BeneficiaryQuotaForm beneficiaryQuotaForm, Model model)
    {
        CommonUtility.mapFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        beneficiaryQuotaForm.setApplicantType(ApplicantType.REGULAR);
        model.addAttribute("actionType", "none");
        return "benQuota";
    }

    @RequestMapping(params = "search", value = "/beneficiaryQuota", method = RequestMethod.POST)
    public String getBeneficiaryQuota(BeneficiaryQuotaForm beneficiaryQuotaForm, BindingResult bindingResult, Model model, HttpSession session)
    {
        if (bindingResult.hasErrors())
        {
            return getBeneficiaryQuota(beneficiaryQuotaForm, model);
        }
        Scheme scheme = ((UserDetail) session.getAttribute("userDetail")).getScheme();
        Integer upazilaId = beneficiaryQuotaForm.getUpazila().getId();
        List<ItemObject> unionIoList;
        List<BenQuota> beneficiaryQuotas = new ArrayList<>();
        if (scheme.getShortName().equalsIgnoreCase("lma"))
        {
            System.out.println("beneficiaryQuotaForm.getApplicantType = " + beneficiaryQuotaForm.getApplicantType());
            if (null == beneficiaryQuotaForm.getApplicantType())
            {
                return "benQuota";
            }
            else
            {
                switch (beneficiaryQuotaForm.getApplicantType())
                {
                    case REGULAR:
                        unionIoList = unionService.getMunicipalOrCityCorporation(upazilaId, null);
                        createRegularQuotaList(scheme.getId(), beneficiaryQuotaForm.getFiscalYear().getId(), ApplicantType.REGULAR, unionIoList, beneficiaryQuotas);
                        break;
                    case BGMEA:

                        List<Factory> bgmeaFactoryList = this.factoryService.getFactoryList(FactoryType.GARMENTS);
                        createFactoryQuotaList(scheme.getId(), beneficiaryQuotaForm.getFiscalYear().getId(), ApplicantType.BGMEA, bgmeaFactoryList, beneficiaryQuotas);

                        break;
                    case BKMEA:
                        List<Factory> bkmeaFactoryList = this.factoryService.getFactoryList(FactoryType.KNITWEAR);
                        createFactoryQuotaList(scheme.getId(), beneficiaryQuotaForm.getFiscalYear().getId(), ApplicantType.BKMEA, bkmeaFactoryList, beneficiaryQuotas);
                        break;
                }
            }
        }
        else
        {
            unionIoList = unionService.getUnionIoList(upazilaId);
            createRegularQuotaList(scheme.getId(), beneficiaryQuotaForm.getFiscalYear().getId(), ApplicantType.REGULAR, unionIoList, beneficiaryQuotas);
        }

        beneficiaryQuotaForm.setBenQuotaList(beneficiaryQuotas);
//        These line for showing names
        FiscalYear fiscalYear = this.fiscalYearService.getFiscalYear(beneficiaryQuotaForm.getFiscalYear().getId());
        beneficiaryQuotaForm.setFiscalYear(fiscalYear);
        if (beneficiaryQuotaForm.getDivision().getId() != null)
        {
            Division division = this.divisionService.getDivision(beneficiaryQuotaForm.getDivision().getId());
            beneficiaryQuotaForm.setDivision(division);
        }
        if (beneficiaryQuotaForm.getDistrict().getId() != null)
        {
            District district = this.districtService.getDistrict(beneficiaryQuotaForm.getDistrict().getId());
            beneficiaryQuotaForm.setDistrict(district);
        }
        if (beneficiaryQuotaForm.getUpazila().getId() != null)
        {
            Upazilla upazilla = this.upazillaService.getUpazilla(beneficiaryQuotaForm.getUpazila().getId());
            beneficiaryQuotaForm.setUpazila(upazilla);
        }
//        end      
        CommonUtility.mapFiscalYearName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("actionType", "search");
        return "benQuota";
    }

    private void createFactoryQuotaList(Integer schemeId, Integer fiscalYearId, ApplicantType applicantType, List<Factory> factoryList, List<BenQuota> beneficiaryQuotas)
    {
        System.out.println("factoryList.size() = " + factoryList.size());
        for (Factory factory : factoryList)
        {
            try
            {
                System.out.println("factory.getNameInEnglish() = " + factory.getNameInEnglish());
                BenQuota beneficiaryQuota = new BenQuota();
                beneficiaryQuota.setApplicantType(applicantType);
                beneficiaryQuota.setFactory(factory);
                BenQuota benQuota = this.benQuotaService.getBenQuotaByFactory(factory.getId(), fiscalYearId, schemeId);
                int benQuotaCount = benQuota != null ? benQuota.getQuota() : 0;
                beneficiaryQuota.setQuota(benQuotaCount);
                beneficiaryQuotas.add(beneficiaryQuota);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    private void createRegularQuotaList(Integer schemeId, Integer fiscalYearId, ApplicantType applicantType, List<ItemObject> unionIoList, List<BenQuota> beneficiaryQuotas)
    {
        for (ItemObject io : unionIoList)
        {
            BenQuota beneficiaryQuota = new BenQuota();
            Union union = this.unionService.getUnion(io.getId());
            beneficiaryQuota.setUnion(union);
            beneficiaryQuota.setApplicantType(applicantType);
            BenQuota benQuota = this.benQuotaService.getBenQuotaByUnion(union.getId(), fiscalYearId, schemeId);
            int benQuotaCount = benQuota != null ? benQuota.getQuota() : 0;
            beneficiaryQuota.setQuota(benQuotaCount);
            beneficiaryQuotas.add(beneficiaryQuota);
        }
    }

    @RequestMapping(params = "save", value = "/beneficiaryQuota", method = RequestMethod.POST)
    public String saveBeneficiaryQuota(@ModelAttribute BeneficiaryQuotaForm beneficiaryQuotaForm, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {

        beneficiaryQuotaForm.setScheme(((UserDetail) session.getAttribute("userDetail")).getScheme());
        this.benQuotaService.saveBeneficiaryQuota(beneficiaryQuotaForm, session);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/beneficiaryQuota";
    }
}

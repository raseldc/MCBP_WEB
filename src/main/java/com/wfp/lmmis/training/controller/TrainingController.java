/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.controller;

import com.wfp.lmmis.applicant.service.BeneficiaryService;
import com.wfp.lmmis.enums.ApplicantType;
import com.wfp.lmmis.form.SearchParameterForm;
import com.wfp.lmmis.masterdata.model.Division;
import com.wfp.lmmis.masterdata.service.DivisionService;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.model.FiscalYear;
import com.wfp.lmmis.payroll.model.PaymentInformationHistory;
import com.wfp.lmmis.training.model.Training;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.training.service.TrainingService;
import com.wfp.lmmis.rm.controller.UserController;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.training.form.TrainingForm;
import com.wfp.lmmis.training.model.Trainer;
import com.wfp.lmmis.training.model.TrainingAttendee;
import com.wfp.lmmis.training.model.TrainingType;
import com.wfp.lmmis.training.service.TrainerService;
import com.wfp.lmmis.training.service.TrainingTypeService;
import com.wfp.lmmis.utility.BeneficiaryInfo;
import com.wfp.lmmis.utility.CalendarUtility;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TrainingController {

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    private TrainingTypeService trainingTypeService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private SchemeService schemeService;
    @Autowired
    private FiscalYearService fiscalYearService;
    @Autowired
    private DivisionService divisionService;
    @Autowired
    private BeneficiaryService beneficiaryService;

    Localizer localizer = Localizer.getBrowserLocalizer();

    /**
     *
     * @param m
     */
    public void mapGlobalVariables(Model m) {
        m.addAttribute("dateFormat", "dd-mm-yy");
    }

    private void mapFiscalYearName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<FiscalYear> fiscalYearlist = this.fiscalYearService.getFiscalYearList(true, true);
        model.addAttribute("fiscalYearList", fiscalYearlist);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("fiscalYearName", "nameInEnglish");
        } else {
            model.addAttribute("fiscalYearName", "nameInBangla");
        }

    }

    private void mapTrainingTypeName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<TrainingType> trainingTypelList = this.trainingTypeService.getTrainingTypeList(true);
        for (TrainingType t : trainingTypelList) {
            t.setNameInEnglish(t.getNameInEnglish() + (t.isBeneficiaryIncluded() == true ? " (~)" : ""));
            t.setNameInBangla(t.getNameInBangla() + (t.isBeneficiaryIncluded() == true ? " (~)" : ""));
        }
        model.addAttribute("trainingTypeList", trainingTypelList);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("trainingTypeName", "nameInEnglish");
        } else {
            model.addAttribute("trainingTypeName", "nameInBangla");
        }

    }

    private void mapTrainerName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<Trainer> trainerList = this.trainerService.getTrainerList();
        model.addAttribute("trainerList", trainerList);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("trainerName", "nameInEnglish");
        } else {
            model.addAttribute("trainerName", "nameInBangla");
        }

    }

    @RequestMapping(value = "/training/list")
    public String showTrainingList(Model model, HttpServletRequest request) {
        try {
            System.out.println("in training list");
            CommonUtility.mapSchemeName(model);
            CommonUtility.mapAllFiscalYearName(model);
            mapTrainingTypeName(model);
            mapDivisionName(model);
            model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
            model.addAttribute("trainingList", this.trainingService.getTrainingList());
            return "trainingList";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     *
     * @param training
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/training/create", method = RequestMethod.GET)
    public String createTraining(@ModelAttribute Training training, Model model, HttpServletRequest request) {
        mapGlobalVariables(model);
        CommonUtility.mapSchemeName(model);
        CommonUtility.mapAllFiscalYearName(model);
        mapTrainingTypeName(model);
        mapTrainerName(model);
        mapDivisionName(model);
        model.addAttribute("actionType", "create");
//        model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));

        UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");
        if (loggedUser.getDivision() != null) {
            training.setDivisionAvailable(true);
            training.setDivision(loggedUser.getDivision());
        }
        if (loggedUser.getDistrict() != null) {
            training.setDistrictAvailable(true);
            training.setDistrict(loggedUser.getDistrict());
        }
        if (loggedUser.getUpazila() != null) {
            training.setUpazilaAvailable(true);
            training.setUpazilla(loggedUser.getUpazila());
        }
        model.addAttribute("training", training);
        return "training";
    }

    /**
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getBeneficiaryByGeolocation/list", method = RequestMethod.POST)
    @ResponseBody
    public void getTrainingAttendeeByGeolocation(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map parameter = new HashMap();
            String upazilaId = request.getParameter("upazilaId");
            System.out.println("upazila = " + upazilaId);
            parameter.put("upazilaId", upazilaId);
            List<BeneficiaryInfo> benList = this.beneficiaryService.getbeneficiaryByGeolocation(parameter);
            System.out.println("size of beneficiary list = " + benList.size());

            Locale locale = LocaleContextHolder.getLocale();
            JSONObject jsonResult = new JSONObject();
            JSONArray dataArray = new JSONArray();
//            int draw = Integer.parseInt(request.getParameter("draw"));
//            int beginIndex = Integer.parseInt(request.getParameter("start"));
//            int pageSize = Integer.parseInt(request.getParameter("length"));
            long recordsTotal = benList.size();
            long recordsFiltered = benList.size();
            for (BeneficiaryInfo ben : benList) {
                JSONArray ja = new JSONArray();
                ja.put("<td><a><span class=\"glyphicon glyphicon-plus\" id=\"" + ben.getId() + "\" onclick=\"setAttendeeList(this)\"></span></a></td>");
                if ("en".equals(locale.getLanguage())) {
                    ja.put(ben.getNameInEnglish());
                    if (ben.getNid() != null) {
                        ja.put(ben.getNid());
                    } else {
                        ja.put("");
                    }
                    if (ben.getMobileNo() != null) {
                        ja.put('0' + ben.getMobileNo().toString());
                    } else {
                        ja.put("");
                    }

                } else {
                    ja.put(ben.getNameInBangla());
                    if (ben.getNid() != null) {
                        ja.put(CommonUtility.getNumberInBangla(ben.getNid().toString()));
                    } else {
                        ja.put("");
                    }
                    if (ben.getMobileNo() != null) {
                        ja.put('০' + CommonUtility.getNumberInBangla(ben.getMobileNo().toString()));
                    } else {
                        ja.put("");
                    }
                }

                dataArray.put(ja);

            }
            try {
                jsonResult.put("recordsTotal", recordsTotal);
                jsonResult.put("recordsFiltered", recordsFiltered);
                jsonResult.put("data", dataArray);
//                jsonResult.put("draw", draw);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/checkBenTrainingDuplicacy", method = RequestMethod.GET)
    @ResponseBody
    public boolean getBenDuplicacyInTraining(HttpServletRequest request) {
        Integer id = new Integer(request.getParameter("id"));
        Integer trainingTypeId = new Integer(request.getParameter("trainingTypeId"));
        boolean result = this.trainingService.checkBeneficiaryTrainingDuplicacy(id, trainingTypeId);
        return result;
    }

    private void mapDivisionName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        List<Division> divisionList = this.divisionService.getDivisionList();
        model.addAttribute("divisionList", divisionList);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("divisionName", "nameInEnglish");
        } else {
            model.addAttribute("divisionName", "nameInBangla");
        }
    }

    @RequestMapping(value = "/training/create", method = RequestMethod.POST)
    public String createTraining(@Valid @ModelAttribute TrainingForm trainingForm, BindingResult bindingResult, Model model,
            HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        Training training = trainingForm.getTrainingModel();
        fileSaveFor(trainingForm.getFile(), training);
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors()) {
            return "training";
        }
        try {
            String applicantTypeSt = request.getParameter("applicantType");
            String benListSt = request.getParameter("selectedBenListSt");

            ApplicantType applicantType = (applicantTypeSt != null && !applicantTypeSt.equals("")) ? ApplicantType.valueOf(applicantTypeSt) : null;
            training.setApplicantType(applicantType);
            if ("bn".equals(LocaleContextHolder.getLocale().getLanguage())) {
                training.setNumberOfPerticipants(Integer.parseInt(CommonUtility.getNumberInEnglish(training.getNumberOfPerticipants().toString())));
                training.setDurationDay(Integer.parseInt(CommonUtility.getNumberInEnglish(training.getDurationDay().toString())));
            }
            training.setFiscalYear(fiscalYearService.getFiscalYear(training.getFiscalYear().getId()));
            training.setTrainingType(trainingTypeService.getTrainingType(training.getTrainingType().getId()));
            training.setTrainer(trainerService.getTrainer(training.getTrainer().getId()));
            training.setHeadOffice(Boolean.FALSE);
            SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
            if (searchParameterForm.isIsDivisionAvailable()) {
                training.setDivision(searchParameterForm.getDivision());
            }
            if (searchParameterForm.isIsDistrictAvailable()) {
                training.setDistrict(searchParameterForm.getDistrict());
            }
            if (searchParameterForm.isIsUpazilaAvailable()) {
                training.setUpazilla(searchParameterForm.getUpazila());
            }
            if (training.getDivision().getId() == null) {
                training.setDivision(null);
            }
            if (training.getDistrict().getId() == null) {
                training.setDistrict(null);
            }
            if (training.getUpazilla().getId() == null) {
                training.setUpazilla(null);
            }

            training.setCreatedBy((User) session.getAttribute("user"));
            training.setCreationDate(Calendar.getInstance());
            this.trainingService.save(training);
            String[] bens = benListSt.split(",");
            List<String> benList = Arrays.asList(bens);
            for (String benSt : benList) {
                TrainingAttendee ta = new TrainingAttendee();
                ta.setBeneficiary(beneficiaryService.getBeneficiary(new Integer(benSt)));
                ta.setTraining(training);
                ta.setCreatedBy((User) session.getAttribute("user"));
                ta.setCreationDate(Calendar.getInstance());
                this.trainingService.saveTrainingAttendee(ta);

            }
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);

        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            System.out.println("msg = " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return "redirect:/training/list";
    }

    @RequestMapping(value = "/training/edit/{id}", method = RequestMethod.GET)
    public String editTraining(@PathVariable("id") Integer id, @ModelAttribute Training training, Model model,
            boolean trainingLoadRequired, HttpServletRequest request) {
        try {
            mapGlobalVariables(model);
            CommonUtility.mapSchemeName(model);
            CommonUtility.mapAllFiscalYearName(model);
            mapTrainingTypeName(model);
            mapTrainerName(model);
            mapDivisionName(model);
            System.out.println("trainingLoadRequired = " + trainingLoadRequired);
            if (!trainingLoadRequired) {
                Training trainingEdit = this.trainingService.getTraining(id);
                List<TrainingAttendee> trainingAttendeeList = this.trainingService.getTrainingAttendeeList(id);
                String attendeeListSt = "";
                for (TrainingAttendee ta : trainingAttendeeList) {
                    attendeeListSt += ta.getBeneficiary().getId() + ",";
                }

                model.addAttribute("attendeeList", trainingAttendeeList);
                model.addAttribute("attendeeListSt", attendeeListSt);
//                model.addAttribute("searchParameterForm", CommonUtility.loadSearchParameterForm(request));
                UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");
                if (loggedUser.getDivision() != null) {
                    training.setDivisionAvailable(true);
                    training.setDivision(loggedUser.getDivision());
                }
                if (loggedUser.getDistrict() != null) {
                    training.setDistrictAvailable(true);
                    training.setDistrict(loggedUser.getDistrict());
                }
                if (loggedUser.getUpazila() != null) {
                    training.setUpazilaAvailable(true);
                    training.setUpazilla(loggedUser.getUpazila());
                }
                model.addAttribute("training", trainingEdit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return "training";
    }

    /**
     *
     * @param month
     * @param locale
     * @return
     */
    public String getMonthName(int month, Locale locale) {
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] monthNames = symbols.getMonths();
        return monthNames[month - 1];
    }

    /**
     *
     * @param file
     * @param training
     */
    public void fileSaveFor(MultipartFile file, Training training) {

        String name = "";//names[i];
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            byte[] bytes = file.getBytes();
            name = file.getOriginalFilename().toString();
            String base64String = Base64.encodeBase64String(bytes);

            if (base64String.equals("") && !training.getFileBase64().equals("")) {
            } else {
                training.setFileBase64(base64String);
                training.setFileExtension(extension);
            }

        } catch (Exception ex) {
            System.out.println("Error " + ex.toString());
        }

    }

    /**
     *
     * @param pk
     * @param trainingForm
     * @param bindingResult
     * @param session
     * @param model
     * @param redirectAttributes
     * @param request
     * @return
     */
    @RequestMapping(value = "/training/edit/{id}", method = RequestMethod.POST)
    public String editTraining(@PathVariable("id") Integer pk, @Valid @ModelAttribute TrainingForm trainingForm,
            BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        try {
            Training training = trainingForm.getTrainingModel();
            fileSaveFor(trainingForm.getFile(), training);
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            }
            if (bindingResult.hasErrors()) {
                return editTraining(pk, training, model, true, request);
            }
            String applicantTypeSt = request.getParameter("applicantType");
            String benListSt = request.getParameter("selectedBenListSt");

            //First Remove previous attendees
            this.trainingService.deleteTrainingAttendee(training.getId());
            String[] bens = benListSt.split(",");
            List<String> benList = Arrays.asList(bens);
            for (String benSt : benList) {
                if (!benSt.isEmpty()) {
                    TrainingAttendee ta = new TrainingAttendee();
                    ta.setBeneficiary(this.beneficiaryService.getBeneficiary(new Integer(benSt)));
                    ta.setTraining(training);
                    ta.setCreatedBy((User) session.getAttribute("user"));
                    ta.setCreationDate(Calendar.getInstance());
                    this.trainingService.saveTrainingAttendee(ta);
                }

            }
            if (training.getId() == null) {
                ApplicantType applicantType = (applicantTypeSt != null && applicantTypeSt != "") ? ApplicantType.valueOf(applicantTypeSt) : null;
                training.setApplicantType(applicantType);
                if ("bn".equals(LocaleContextHolder.getLocale().getLanguage())) {
                    training.setNumberOfPerticipants(Integer.parseInt(CommonUtility.getNumberInEnglish(training.getNumberOfPerticipants().toString())));
                    training.setDurationDay(Integer.parseInt(CommonUtility.getNumberInEnglish(training.getDurationDay().toString())));
                }
                training.setTrainingType(trainingTypeService.getTrainingType(training.getTrainingType().getId()));
                training.setTrainer(trainerService.getTrainer(training.getTrainer().getId()));
                training.setFiscalYear(fiscalYearService.getFiscalYear(training.getFiscalYear().getId()));

                training.setCreatedBy((User) session.getAttribute("user"));
                training.setCreationDate(Calendar.getInstance());
                this.trainingService.save(training);
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            } else {
                System.out.println("training.creationDate = " + training.getCreationDate());
//                training.setMinistry(ministryService.getMinistry(training.getMinistry().getId()));
                if ("bn".equals(LocaleContextHolder.getLocale().getLanguage())) {
                    training.setNumberOfPerticipants(Integer.parseInt(CommonUtility.getNumberInEnglish(training.getNumberOfPerticipants().toString())));
                    training.setDurationDay(Integer.parseInt(CommonUtility.getNumberInEnglish(training.getDurationDay().toString())));
                }
                ApplicantType applicantType = (applicantTypeSt != null && !applicantTypeSt.equals("")) ? ApplicantType.valueOf(applicantTypeSt) : null;
                training.setApplicantType(applicantType);
                training.setFiscalYear(fiscalYearService.getFiscalYear(training.getFiscalYear().getId()));

//                training.setNameInEnglish(training.getStartDate().getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH) + "-"
//                        + training.getEndDate().getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH));
                training.setModifiedBy((User) session.getAttribute("user"));
                training.setModificationDate(Calendar.getInstance());

                training.setHeadOffice(Boolean.FALSE);
                SearchParameterForm searchParameterForm = CommonUtility.loadSearchParameterForm(request);
                if (searchParameterForm.isIsDivisionAvailable()) {
                    training.setDivision(searchParameterForm.getDivision());
                }
                if (searchParameterForm.isIsDistrictAvailable()) {
                    training.setDistrict(searchParameterForm.getDistrict());
                }
                if (searchParameterForm.isIsUpazilaAvailable()) {
                    training.setUpazilla(searchParameterForm.getUpazila());
                }
                if (searchParameterForm.isIsUnionAvailable()) {
                    training.setUnion(searchParameterForm.getUnion());
                }
                if (training.getDivision().getId() == null) {
                    training.setDivision(null);
                }
                if (training.getDistrict().getId() == null) {
                    training.setDistrict(null);
                }
                if (training.getUpazilla().getId() == null) {
                    training.setUpazilla(null);
                }

//                if (training.getDivision().getId() == null)
//                {
//                    training.setDivision(null);
//                }
//                if (training.getDistrict().getId() == null)
//                {
//                    training.setDistrict(null);
//                }
//                if (training.getUpazilla().getId() == null)
//                {
//                    training.setUpazilla(null);
//                }
//                if (training.getUnion().getId() == null)
//                {
//                    training.setUnion(null);
//                }
                this.trainingService.edit(training);
                ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
                redirectAttributes.addFlashAttribute("message", controllerMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/training/list";
    }

    @RequestMapping(value = "training/delete/{trainingId}")
    public String deleteTraining(@PathVariable("trainingId") Integer trainingId, RedirectAttributes redirectAttributes) {
        System.out.println("in training delete");
        this.trainingService.delete(trainingId);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/training/list";
    }

    @RequestMapping(value = "/training/list", method = RequestMethod.POST)
    @ResponseBody
    public void showTrainingList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try {
            System.out.println("in list");

            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));
            String divisionId = request.getParameter("divisionId");
            String districtId = request.getParameter("districtId");
            String upazilaId = request.getParameter("upazilaId");
            String trainingTypeId = request.getParameter("trainingType");

            Map parameter = new HashMap();
            parameter.put("fiscalYearId", Integer.valueOf(request.getParameter("fiscalYear")));
            parameter.put("trainingTypeId", trainingTypeId);
            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            List<Object> resultList = trainingService.getTrainingListBySearchParameter(parameter, beginIndex, pageSize);
            List<Training> trainingList = (List<Training>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);
            Locale locale = LocaleContextHolder.getLocale();
            String location = "";
            for (Training training : trainingList) {
                JSONArray ja = new JSONArray();
                if ("en".equals(locale.getLanguage())) {
                    ja.put(training.getTrainingType().getNameInEnglish());
                    ja.put(training.getTrainer().getNameInEnglish());
                    location = training.getDivision().getNameInEnglish();
                    if (null != training.getDistrict()) {
                        location += "->" + training.getDistrict().getNameInEnglish();
                        if (null != training.getUpazilla()) {
                            location += "->" + training.getUpazilla().getNameInEnglish();
                            if (null != training.getUnion()) {
                                location += "->" + training.getUnion().getNameInEnglish();
                            }
                        }
                    }
                    System.out.println("location - " + location);
                    ja.put(location);
                } else {
                    ja.put(training.getTrainingType().getNameInBangla());
                    ja.put(training.getTrainer().getNameInBangla());
                    System.out.println("id is  " + training.getId());
                    location = training.getDivision().getNameInBangla();
                    if (null != training.getDistrict()) {
                        location += "->" + training.getDistrict().getNameInBangla();
                        if (null != training.getUpazilla()) {
                            location += "->" + training.getUpazilla().getNameInBangla();
                            if (null != training.getUnion()) {
                                location += "->" + training.getUnion().getNameInBangla();
                            }
                        }
                    }
                    ja.put(location);

                }

                if (training.getTrainingType().isBeneficiaryIncluded() == true) {
                    ja.put("<span class=\"glyphicon glyphicon-ok\"></span>");
                } else {
                    ja.put("<span class=\"glyphicon glyphicon-remove\"></span>");
                }

                ja.put(training.getNumberOfPerticipants());
                ja.put(training.getTrainingCost());
                ja.put(CalendarUtility.getDateString(training.getStartDate()));
//                ja.put(CalendarUtility.getDateString(training.getEndDate()));
                ja.put(training.getDurationDay());
                ja.put(training.getComment());
                if ("en".equals(locale.getLanguage())) {
                    ja.put("<a href=\"edit/" + training.getId() + "\" data-toggle=\"tooltip\" "
                            + "title=\"Edit\">\n"
                            + "<span class=\"glyphicon glyphicon-edit\"></span>\n</a>");
                } else {
                    ja.put("<a href=\"edit/" + training.getId() + "\" data-toggle=\"tooltip\" "
                            + "title=\"সম্পাদন করুন\">\n"
                            + "<span class=\"glyphicon glyphicon-edit\"></span>\n</a>");
                }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

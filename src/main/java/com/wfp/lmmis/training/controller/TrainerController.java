/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.training.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.training.model.Trainer;
import com.wfp.lmmis.training.service.TrainerService;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.utility.ApplicationConstants;
import com.wfp.lmmis.utility.CommonUtility;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.commons.io.FileUtils;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TrainerController {

    public static final String FILE_CREATION_PATH = "H:/MoWCA/spfmsp/LM-MIS/web/resources/uploadedFile/trainer/";
    //private static final logger logger = //logger.getlogger(TrainerController.class);

    @Autowired
    private TrainerService trainerService;
    Localizer localizer = Localizer.getBrowserLocalizer();
//    @Autowired
//    private CalendarEditor calendarEditor;
//
//    @InitBinder
//    public void initBinder(WebDataBinder binder)
//    {
//        binder.registerCustomEditor(Calendar.class, this.calendarEditor);
//    }

    /**
     *
     * @param model
     * @param locale
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "/trainer/list")
    public String showTrainerList(Model model, Locale locale) throws ExceptionWrapper {
        model.addAttribute("trainerList", this.trainerService.getTrainerList());
        return "trainerList";
    }

    @RequestMapping(value = "/trainer/create", method = RequestMethod.GET)
    public String createTrainer(@ModelAttribute Trainer trainer, Model model) {
        model.addAttribute("actionType", "create");
        model.addAttribute("imagePath", "resources/uploadedFile/trainer");
        return "trainer";
    }

    /**
     *
     * @param trainer
     * @param bindingResult
     * @param model
     * @param session
     * @param multipartFile
     * @param redirectAttributes
     * @param request
     * @return
     */
    @RequestMapping(value = "/trainer/create", method = RequestMethod.POST)
    public String createTrainer(@Valid Trainer trainer, BindingResult bindingResult, Model model, HttpSession session,
            @RequestParam("profilePhoto") MultipartFile multipartFile, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error)
                -> {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors()) {
            System.out.println("has errors");
            return createTrainer(trainer, model);
        }
        try {
            if ("bn".equals(LocaleContextHolder.getLocale().getLanguage())) {
                trainer.setOrganizationNumber(CommonUtility.getNumberInEnglish(trainer.getOrganizationNumber()));
            }
            trainer.setPhotoPath(multipartFile.getOriginalFilename());
            trainer.setCreatedBy((User) session.getAttribute("user"));
            trainer.setCreationDate(Calendar.getInstance());
            Integer id = this.trainerService.save(trainer);
            String fileName = multipartFile.getOriginalFilename();
            String fileCreationPath = request.getServletContext().getRealPath(ApplicationConstants.FILE_CREATION_PATH_TRAINER) + "\\" + id + "\\";
            String saveDirectory = fileCreationPath;
            if (!"".equalsIgnoreCase(fileName)) {
                try {
                    new File(saveDirectory).mkdirs();
                    fileName = saveDirectory + fileName;
                    multipartFile.transferTo(new File(fileName));
                } catch (IOException ex) {
                    ////java.util.logging.//logger.getlogger(TrainerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalStateException ex) {
                    //java.util.logging.//logger.getlogger(TrainerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/trainer/list";
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(value = "/trainer/edit/{id}", method = RequestMethod.GET)
    public String editTrainer(@PathVariable("id") Integer id, @ModelAttribute Trainer trainer, Model model, boolean trainerLoadRequired) {
        try {
            if (!trainerLoadRequired) {
                model.addAttribute("imagePath", "resources/uploadedFile/trainer/" + id);
                Trainer trainerEdit = this.trainerService.getTrainer(id);
                Locale locale = LocaleContextHolder.getLocale();
                if ("bn".equals(locale.getLanguage())) {
                    trainerEdit.setOrganizationNumber(CommonUtility.getNumberInBangla(trainerEdit.getOrganizationNumber()));
                }
                model.addAttribute("trainer", trainerEdit);
            }
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            throw e;
        }
        return "trainer";
    }

    @RequestMapping(value = "/trainer/edit/{id}", method = RequestMethod.POST)
    public String editTrainer(@PathVariable("id") Integer pk, @Valid
            @ModelAttribute Trainer trainer,
            @RequestParam("profilePhoto") MultipartFile multipartFile, BindingResult bindingResult, HttpSession session,
            Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String fileName = multipartFile.getOriginalFilename();
        Trainer trainerPrev = this.trainerService.getTrainer(pk);
        String filePath = request.getServletContext().getRealPath(ApplicationConstants.FILE_CREATION_PATH_TRAINER) + "\\" + pk + "\\";
        if (trainerPrev.getPhotoPath() != null) {
            try {
                FileUtils.deleteDirectory(new File(filePath));
            } catch (IOException ex) {
                //java.util.logging.//logger.getlogger(TrainerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!"".equalsIgnoreCase(fileName)) {
            try {
                new File(filePath).mkdirs();
                fileName = filePath + fileName;
                multipartFile.transferTo(new File(fileName));
            } catch (IOException ex) {
                //java.util.logging.//logger.getlogger(TrainerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                //java.util.logging.//logger.getlogger(TrainerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("filename " + fileName);
        try {
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach((error)
                    -> {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
            });
            if (bindingResult.hasErrors()) {
                return editTrainer(pk, trainer, model, true);
            }
            if ("bn".equals(LocaleContextHolder.getLocale().getLanguage())) {
                trainer.setOrganizationNumber(CommonUtility.getNumberInEnglish(trainer.getOrganizationNumber()));
            }
            trainer.setPhotoPath(multipartFile.getOriginalFilename());
            trainer.setModifiedBy((User) session.getAttribute("user"));
            trainer.setModificationDate(Calendar.getInstance());
            this.trainerService.edit(trainer);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/trainer/list";
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
        }
        return "trainer";
    }

    /**
     *
     * @param id
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/trainer/delete/{id}", method = RequestMethod.POST)
    public String deleteTrainer(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        this.trainerService.delete(id);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/trainer/list";
    }
}

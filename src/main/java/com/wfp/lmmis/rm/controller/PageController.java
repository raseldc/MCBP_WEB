package com.wfp.lmmis.rm.controller;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.editor.PageEditor;
import com.wfp.lmmis.rm.model.Page;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.service.PageService;
import com.wfp.lmmis.rm.service.RoleService;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.Localizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpSession;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController
{

    //private static final logger logger = //logger.getlogger(UserController.class);
    @Autowired
    private PageService pageService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private PageEditor pageEditor;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        binder.registerCustomEditor(Page.class, this.pageEditor);
    }

    @RequestMapping(value = "/page/list")
    public String showPageList(Model model, Locale locale, HttpSession session) throws ExceptionWrapper
    {
        Integer schemeId = ((UserDetail)session.getAttribute("userDetail")).getScheme().getId();   
        model.addAttribute("allPageList", this.pageService.getPageList(locale, schemeId));
        return "pageList";
    }

    @RequestMapping(value = "/page/create", method = RequestMethod.GET)
    public String createPage(@ModelAttribute Page page, Model model)
    {
        model.addAttribute("actionType", "create");
        mapParentPage(model);
        return "page";
    }

    private void mapParentPage(Model model)
    {
        Locale locale = LocaleContextHolder.getLocale();
        List<Page> parentPageList = this.pageService.getParentPageList();
        model.addAttribute("parentPageList", parentPageList);
        if ("en".equals(locale.getLanguage()))
        {
            model.addAttribute("parentPageName", "nameInEnglish");
        }
        else
        {
            model.addAttribute("parentPageName", "nameInBangla");
        }
    }

    /**
     *
     * @param page
     * @param bindingResult
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/page/create", method = RequestMethod.POST)
    public String createPage(@Valid Page page, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach((error)
                ->
        {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage() + " .... " + error.getField());
        });
        if (bindingResult.hasErrors())
        {
            return createPage(page, model);
        }
        try
        {
            page.setCreatedBy((User) session.getAttribute("user"));
            page.setCreationDate(Calendar.getInstance());
            this.pageService.save(page);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/page/list";
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(value = "/page/edit/{id}", method = RequestMethod.GET)
    public String editPage(@PathVariable("id") Integer id, @ModelAttribute Page page, Model model, boolean pageLoadRequired)
    {
        try
        {
            System.out.println("pageLoadRequired = " + pageLoadRequired);
            if (!pageLoadRequired)
            {
                model.addAttribute("page", this.pageService.getPage(id));
                mapParentPage(model);
            }
        }
        catch (Exception e)
        {
            //logger.infoer(e.getMessage());
            throw e;
        }
        return "page";
    }

    @RequestMapping(value = "/page/edit/{id}", method = RequestMethod.POST)
    public String editPage(@PathVariable("id") Integer pk, Page page, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes)
    {
        try
        {
            if (bindingResult.hasErrors())
            {
                return editPage(pk, page, model, true);
            }
            page.setModifiedBy((User) session.getAttribute("user"));
            page.setModificationDate(Calendar.getInstance());
            this.pageService.edit(page);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/page/list";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //logger.infoer(e.getMessage());
            return "page";
        }
    }

    /**
     *
     * @param id
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/page/delete/{id}", method = RequestMethod.POST)
    public String deletePage(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes)
    {
        Page page = this.pageService.getPage(id);
        this.pageService.delete(page);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/page/list";
    }

    /**
     *
     * @param m
     * @param locale
     * @param session
     * @return
     * @throws ExceptionWrapper
     */
    @RequestMapping(value = "/rolePage")
    public String getPageAssignment(Model m, Locale locale, HttpSession session) throws ExceptionWrapper
    {
//        Integer schemeId = ((UserDetail)session.getAttribute("userDetail")).getScheme().getId();                
        List<Page> pageList = this.pageService.getPageList(locale, null);
        List<Integer> allPageIds = new ArrayList<Integer>();
        for (Page page : pageList)
        {
            if (page.getParentPage() != null)
            {
                allPageIds.add(page.getId());
            }
        }
        m.addAttribute("allPageIds", allPageIds);
        m.addAttribute("pageListMap", this.pageService.getPageListMap(null));
        m.addAttribute("roleList", this.roleService.getRoleByScheme(null));
        return "rolePage";
    }

    /**
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/pageIdsByRole/{roleId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Integer> getPageIdsByRole(@PathVariable("roleId") Integer roleId)
    {
        try
        {
            List<Integer> list = this.pageService.getPageIdListByRole(roleId);

            System.out.println("list.size() = " + list.size());
            for (Integer a : list)
            {
                System.out.println("a = " + a);
            }
            return list;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    @RequestMapping(value = "/savePagesByRole", method = RequestMethod.POST)
    public @ResponseBody
    String savePagesByRole(@RequestBody PagesByRole pagesByRole)
    {
        Integer roleId = pagesByRole.getRoleId();
        List<Integer> pageIds = pagesByRole.getPageIds();
        System.out.println("roleID: " + roleId);
        for (Integer pageId : pageIds)
        {
            System.out.println("pageId: " + pageId);
        }
        this.pageService.saveRolePage(roleId, pageIds);
        return localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale());
    }

//    public HashMap<Page, Map<Page, List<Page>>> getPageListNewMapByRole(Integer roleId)
//    {
//        return this.pageService.getPageListNewMapByRole(roleId);
//    }
}

class PagesByRole
{

    Integer roleId;
    List<Integer> pageIds;

    public Integer getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Integer roleId)
    {
        this.roleId = roleId;
    }

    public List<Integer> getPageIds()
    {
        return pageIds;
    }

    public void setPageIds(List<Integer> pageIds)
    {
        this.pageIds = pageIds;
    }

}

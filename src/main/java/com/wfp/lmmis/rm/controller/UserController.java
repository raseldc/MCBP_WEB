package com.wfp.lmmis.rm.controller;

import com.wfp.lmmis.enums.AreaType;
import com.wfp.lmmis.enums.AttributeType;
import com.wfp.lmmis.enums.UserStatus;
import com.wfp.lmmis.enums.UserType;
import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.masterdata.model.Scheme;
import com.wfp.lmmis.masterdata.service.SchemeService;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.rm.forms.ChangePasswordByAdminForm;
import com.wfp.lmmis.rm.service.UserService;
import com.wfp.lmmis.rm.forms.ChangePasswordForm;
import com.wfp.lmmis.rm.forms.ForgotPasswordRecoverForm;
import com.wfp.lmmis.rm.forms.ForgotPasswordRequestForm;
import com.wfp.lmmis.rm.model.Role;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.model.UserPerScheme;
import com.wfp.lmmis.rm.service.RoleService;
import com.wfp.lmmis.utility.CommonUtility;
import static com.wfp.lmmis.utility.CommonUtility.mapDivisionName;
import com.wfp.lmmis.utility.ControllerMessage;
import com.wfp.lmmis.utility.ControllerMessageType;
import com.wfp.lmmis.utility.JsonResult;
import com.wfp.lmmis.utility.Localizer;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.validation.Valid;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    //private static final logger logger = //logger.getlogger(UserController.class);
    public static final String FILE_CREATION_PATH = "/resources/uploadedFile/user"; //windows
    public static final String FILE_PROFILEPIC_PATH = "/resources/uploadedFile/user/profilepic/"; //windows
//    public static final String FILE_CREATION_PATH = "resources/uploadedFile/user";// linux

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SchemeService schemeService;
    Localizer localizer = Localizer.getBrowserLocalizer();

    @RequestMapping(value = "/user/list")
    public String showUserList(Model model, HttpSession session) throws ExceptionWrapper {
        model.addAttribute("userSearchParameterForm", new UserSearchParameterForm());
        Locale locale = LocaleContextHolder.getLocale();
        List<UserType> userTypeList = Arrays.asList(UserType.values());
        model.addAttribute("userTypeList", userTypeList);
        List<UserStatus> userStatusList = Arrays.asList(UserStatus.values());
        model.addAttribute("userStatusList", userStatusList);
        List<Scheme> schemeList = this.schemeService.getSchemeList(null, true);
        model.addAttribute("schemeList", schemeList);
        UserDetail loginedUserDetail = (UserDetail) session.getAttribute("userDetail");
        model.addAttribute("userType", loginedUserDetail.getUserType().ordinal());
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("schemeName", "nameInEnglish");
        } else {
            model.addAttribute("schemeName", "nameInBangla");
        }
        return "userList";
    }

    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    @ResponseBody
    public void showUserList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        JSONObject jsonResult = new JSONObject();
        JSONArray dataArray = new JSONArray();
        try {
            int draw = Integer.parseInt(request.getParameter("draw"));
            int beginIndex = Integer.parseInt(request.getParameter("start"));
            int pageSize = Integer.parseInt(request.getParameter("length"));

            String userID = request.getParameter("userID");
            String userType = request.getParameter("userType");
            String userStatus = request.getParameter("userStatus");
            String schemeId = request.getParameter("scheme");
            System.out.println("scheme " + schemeId);
//            String upazilaId = request.getParameter("upazilaId");
            Map parameter = new HashMap();
            parameter.put("userID", userID != null && !"".equals(userID) ? userID : null);
            parameter.put("userType", userType != null && !"".equals(userType) ? UserType.valueOf(userType).ordinal() : -1);
            parameter.put("userStatus", userStatus != null && !"".equals(userStatus) ? UserStatus.valueOf(userStatus).ordinal() : -1);
            parameter.put("schemeId", schemeId != null && !"".equals(schemeId) ? schemeId : null);
//            parameter.put("divisionId", divisionId != null && !"".equals(divisionId) ? Integer.valueOf(divisionId) : null);
//            parameter.put("districtId", districtId != null && !"".equals(districtId) ? Integer.valueOf(districtId) : null);
//            parameter.put("upazilaId", upazilaId != null && !"".equals(upazilaId) ? Integer.valueOf(upazilaId) : null);
            UserDetail loginedUserDetail = (UserDetail) session.getAttribute("userDetail");
            if (loginedUserDetail.getRoleId() != 1) {
                parameter.put("divisionId", loginedUserDetail.getDivision() == null ? null : loginedUserDetail.getDivision().getId());
                parameter.put("districtId", loginedUserDetail.getDistrict() == null ? null : loginedUserDetail.getDistrict().getId());
                parameter.put("upazilaId", loginedUserDetail.getUpazila() == null ? null : loginedUserDetail.getUpazila().getId());
                parameter.put("unionId", loginedUserDetail.getUnion() == null ? null : loginedUserDetail.getUnion().getId());
            }
            List<Object> resultList = this.userService.getUserListByParameter(parameter, beginIndex, pageSize);
            List<User> userList = (List<User>) resultList.get(0);
            long recordsTotal = (Long) resultList.get(1);
            long recordsFiltered = (Long) resultList.get(2);
            Locale locale = LocaleContextHolder.getLocale();
            int isScheme = 0;
            String schemeWiseUser = "";
            for (User user : userList) {
                JSONArray ja = new JSONArray();
                if ("en".equals(locale.getLanguage())) {
                    ja.put(user.getFullNameEn());
                } else {
                    ja.put(user.getFullNameBn());
                }

                ja.put(user.getDesignation());
                ja.put(user.getUserID());
//                if ("en".equals(locale.getLanguage()))
//                {
//                    ja.put(user.getRole().getNameInEnglish());
//                }
//                else
//                {
//                    ja.put(user.getRole().getNameInBangla());
//                }

                ja.put(CommonUtility.getNumberInBangla("" + user.getMobileNo()));
//                if ("en".equals(locale.getLanguage()))
//                {
//                    ja.put(user.getUserType().getDisplayName());
//                }
//                else
//                {
//                    ja.put(user.getUserType().getDisplayNameBn());
//                }
//                if (!user.isHeadOfficeUser())
//                {
//                    if ("en".equals(locale.getLanguage()))
//                    {
//                        String location = user.getDivision().getNameInEnglish();
//                        if (null != user.getDistrict())
//                        {
//                            location += "->" + user.getDistrict().getNameInEnglish();
//                            if (null != user.getUpazilla())
//                            {
//                                location += "->" + user.getUpazilla().getNameInEnglish();
//                                if (null != user.getUnion())
//                                {
//                                    location += "->" + user.getUnion().getNameInEnglish();
//                                }
//                            }
//                        }
//                        ja.put(location);
//                    }
//                    else
//                    {
//                        String location = user.getDivision().getNameInBangla();
//                        if (null != user.getDistrict())
//                        {
//                            location += "->" + user.getDistrict().getNameInBangla();
//                            if (null != user.getUpazilla())
//                            {
//                                location += "->" + user.getUpazilla().getNameInBangla();
//                                if (null != user.getUnion())
//                                {
//                                    location += "->" + user.getUnion().getNameInBangla();
//                                }
//                            }
//                        }
//                        ja.put(location);
//                    }
//                }
//                else
//                {
//                    ja.put("");
//                }
                if (user.getUserPerScheme() != null) {
                    UserPerScheme userPerScheme = user.getUserPerScheme();
                    if ("en".equals(locale.getLanguage())) {
                        schemeWiseUser = schemeWiseUser + userPerScheme.getUserType().getDisplayName() + ",";
                    } else {
                        schemeWiseUser = schemeWiseUser + userPerScheme.getUserType().getDisplayNameBn() + ",";
                    }
                    if (schemeWiseUser != null && schemeWiseUser.length() > 0) {
                        schemeWiseUser = schemeWiseUser.substring(0, schemeWiseUser.length() - 1);
                    }
                    ja.put(schemeWiseUser);
                }
//                if (user.getUserPerSchemes().size() > 0)
//                {
//                    for (UserPerScheme userPerScheme : user.getUserPerSchemes())
//                    {
//                        if ("en".equals(locale.getLanguage()))
//                        {
//                            schemeWiseUser = schemeWiseUser + userPerScheme.getScheme().getNameInEnglish() + "->" + userPerScheme.getUserType().getDisplayName() + ",";
//                        }
//                        else
//                        {
//                            schemeWiseUser = schemeWiseUser + userPerScheme.getScheme().getNameInBangla() + "->" + userPerScheme.getUserType().getDisplayNameBn() + ",";
//                        }
//                    }
//                    if (schemeWiseUser != null && schemeWiseUser.length() > 0)
//                    {
//                        schemeWiseUser = schemeWiseUser.substring(0, schemeWiseUser.length() - 1);
//                    }
//                    ja.put(schemeWiseUser);
//                    for (UserPerScheme userPerScheme : user.getUserPerSchemes())
//                    {
//                        if (userPerScheme.isIsDefault() == true)
//                        {
//                            isScheme = 1;
//                            if ("en".equals(locale.getLanguage()))
//                            {
//                                ja.put(userPerScheme.getScheme().getNameInEnglish());
//                            }
//                            else
//                            {
//                                ja.put(userPerScheme.getScheme().getNameInBangla());
//                            }
//                            break;
//                        }
//                    }
//                }
//                if (isScheme == 0)
//                {
////                    ja.put("");
//                    ja.put("");
//                }
                isScheme = 0;
                schemeWiseUser = "";
                if (user.isActive()) {
                    ja.put("<i class=\"fa fa-check true-icon\"></i>");
                } else {
                    ja.put("<i class=\"fa fa-times\" aria-hidden=\"true\"></i>");
                }

                if (loginedUserDetail.getUserType() == UserType.FIELD) {
                    if ("en".equals(locale.getLanguage())) {

                        ja.put("<a href=\"#\" onclick=\"resetPassword(" + user.getId() + ")\" data-toggle=\"tooltip\" "
                                + "title=\"reset password\">\n"
                                + "<span class=\"glyphicon glyphicon-lock\"></span>\n</a>"
                        );
                    } else {

                        ja.put("<a href=\"#\" onclick=\"resetPassword(" + user.getId() + ")\" data-toggle=\"tooltip\" "
                                + "title=\"পাসওয়ার্ড রি-সেট করুন\">\n"
                                + "<span class=\"glyphicon glyphicon-lock\"></span>\n</a>"
                        );
                    }
                } else {
                    if ("en".equals(locale.getLanguage())) {
                        ja.put("<a href=\"edit/" + user.getId() + "\" data-toggle=\"tooltip\" "
                                + "title=\"Edit\">\n"
                                + "<span class=\"glyphicon glyphicon-edit\"></span>\n</a>");

                        ja.put("<a href=\"changePassword/" + user.getId() + "\" data-toggle=\"tooltip\" "
                                + "title=\"change password\">\n"
                                + "<span class=\"glyphicon glyphicon-lock\"></span>\n</a>"
                        );
                    } else {
                        ja.put("<a href=\"edit/" + user.getId() + "\" data-toggle=\"tooltip\" "
                                + "title=\"সম্পাদন করুন\">\n"
                                + "<span class=\"glyphicon glyphicon-edit\"></span>\n</a>");

                        ja.put("<a href=\"changePassword/" + user.getId() + "\" data-toggle=\"tooltip\" "
                                + "title=\"পাসওয়ার্ড পরিবর্তন করুন\">\n"
                                + "<span class=\"glyphicon glyphicon-lock\"></span>\n</a>"
                        );
                    }
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

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public String createUser(@ModelAttribute User user, Model model, HttpServletRequest request, HttpSession session) {
        user.setStatus(0);
        model.addAttribute("actionType", "create");
//        mapRoleName(model);
        CommonUtility.mapDivisionName(model);
        model.addAttribute("imagePath", FILE_CREATION_PATH);
        List<UserType> userTypeList = Arrays.asList(UserType.values());
        model.addAttribute("userTypeList", userTypeList);
        Locale locale = LocaleContextHolder.getLocale();
        List<Scheme> schemeList = this.schemeService.getSchemeList(null, true);
        model.addAttribute("schemeList", schemeList);
        List<AreaType> areaTypeList = Arrays.asList(AreaType.values());
        model.addAttribute("areaTypeList", areaTypeList);
        CommonUtility.mapBGMEAFactoryName(model);
        CommonUtility.mapBKMEAFactoryName(model);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("schemeName", "nameInEnglish");
        } else {
            model.addAttribute("schemeName", "nameInBangla");
        }
        UserDetail userDetail = (UserDetail) session.getAttribute("userDetail");
        model.addAttribute("userDetail", userDetail);
        mapRoleName(model, userDetail);
        return "user";
    }

    private void mapRoleName(Model model, UserDetail loginedUser) {

        int roleId = loginedUser.getRoleId();
        Locale locale = LocaleContextHolder.getLocale();
        List<Role> roleListAll = this.roleService.getRoleList();
        List<Role> roleList = new ArrayList<>();
        //for upazila and Temporary Upazila User just load two roles upazila and Temporary Upazila User
        if (roleId == 3) {
            List<Integer> forUpazila = new ArrayList<>();
            forUpazila.add(3);
            forUpazila.add(17);
            roleListAll.forEach(a -> {
                if (forUpazila.contains(a.getId())) {
                    roleList.add(a);
                }
            });
        } else {
            roleList.addAll(roleListAll);
        }

        model.addAttribute("roleList", roleList);
        if ("en".equals(locale.getLanguage())) {
            model.addAttribute("roleName", "nameInEnglish");
        } else {
            model.addAttribute("roleName", "nameInBangla");
        }
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String createUser(@Valid User user, BindingResult bindingResult, Model model, HttpServletRequest request,
            HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.toString());
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());

                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;

                    System.out.println(fieldError.getCode());
                }

                if (error instanceof ObjectError) {
                    ObjectError objectError = (ObjectError) error;

                    System.out.println(objectError.getCode());
                }
            }

            return createUser(user, model, request, session);
        }
        UserPerScheme userPerScheme = user.getUserPerScheme();
        System.out.println("user per scheme " + userPerScheme);
        System.out.println("user per scheme " + userPerScheme.getRole().getNameInEnglish());

        if (userPerScheme.getScheme().getId() == null) {
            userPerScheme.setScheme(null);
        }
        if (userPerScheme.getDivision().getId() == null) {
            userPerScheme.setDivision(null);
        }
        if (userPerScheme.getDistrict().getId() == null) {
            userPerScheme.setDistrict(null);
        }
        if (userPerScheme.getUpazilla().getId() == null) {
            userPerScheme.setUpazilla(null);
        }
        if (userPerScheme.getUnion().getId() == null) {
            userPerScheme.setUnion(null);
        }
        if (userPerScheme.getBgmeaFactory().getId() == null) {
            userPerScheme.setBgmeaFactory(null);
        }
        if (userPerScheme.getBkmeaFactory().getId() == null) {
            userPerScheme.setBkmeaFactory(null);
        }
        userPerScheme.setUser(user);

//        for (UserPerScheme userPerScheme : user.getUserPerSchemes())
//        {
//            System.out.println("scheme = " + userPerScheme.getScheme().getId());
//            System.out.println("role code = " + userPerScheme.getRole().getId());
//        }
//        if (user.getUserPerSchemes() != null || !user.getUserPerSchemes().isEmpty())
//        {
//            for (Iterator<UserPerScheme> i = user.getUserPerSchemes().iterator(); i.hasNext();)
//            {
//                UserPerScheme userPerScheme = i.next();
//                if (userPerScheme.getRemove() == 1)
//                {
//                    i.remove();
//                }
//                else
//                {
//                    if (userPerScheme.getDivision().getId() == null)
//                    {
//                        userPerScheme.setDivision(null);
//                    }
//                    if (userPerScheme.getDistrict().getId() == null)
//                    {
//                        userPerScheme.setDistrict(null);
//                    }
//                    if (userPerScheme.getUpazilla().getId() == null)
//                    {
//                        userPerScheme.setUpazilla(null);
//                    }
//                    if (userPerScheme.getUnion().getId() == null)
//                    {
//                        userPerScheme.setUnion(null);
//                    }
//                    userPerScheme.setUser(user);
//                }
//            }
//        }
        String fileCreationPath = request.getServletContext().getRealPath(FILE_CREATION_PATH) + "/";
        String saveDirectory = fileCreationPath;
        System.out.println("saveDirectory = " + fileCreationPath);
        new File(fileCreationPath).mkdirs();
        MultipartFile photoFile = user.getProfilePhoto();

        if (photoFile != null) {
            String photoFileName = photoFile.getOriginalFilename();
            System.out.println("photoFile=" + photoFile + ", photoFileName=" + photoFileName);
            if (!"".equalsIgnoreCase(photoFileName)) {
                try {
                    photoFile.transferTo(new File(saveDirectory + photoFileName));
                    user.setProfilePicPath(photoFileName);
                    System.out.println("sucess, path=" + user.getProfilePicPath());
                } catch (IOException ex) {
                    System.out.println("ex = " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

        }

        try {
            String salt = getSalt();
            String userPassword = user.getPassword();
            String combined = userPassword + salt;
            String hashedPassword = DigestUtils.sha512Hex(combined);
            user.setPassword(hashedPassword);
            user.setSalt(salt);

            this.userService.save(user);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("createSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/user/list";
        } catch (Exception e) {
            //logger.infoer(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(value = "/user/edit/{id}", method = RequestMethod.GET)
    public String editUser(@PathVariable("id") Integer id, @ModelAttribute User user, Model model, boolean pageLoadRequired, HttpServletRequest request) {
        try {
            if (!pageLoadRequired) {
                user = this.userService.getUser(id);
                user.setPassword(null);
                user.setSalt(null);
                model.addAttribute("user", user);
                model.addAttribute("imagePath", FILE_CREATION_PATH);
            }
            UserDetail loginedUser = (UserDetail) request.getSession().getAttribute("userDetail");

            mapRoleName(model, loginedUser);
            mapDivisionName(model);
            List<UserType> userTypeList = Arrays.asList(UserType.values());
            model.addAttribute("userTypeList", userTypeList);
            Locale locale = LocaleContextHolder.getLocale();
            List<Scheme> schemeList = this.schemeService.getSchemeList(null, true);
            model.addAttribute("schemeList", schemeList);
            String locationName = "";
            List<AreaType> areaTypeList = Arrays.asList(AreaType.values());
            model.addAttribute("areaTypeList", areaTypeList);
            CommonUtility.mapBGMEAFactoryName(model);
            CommonUtility.mapBKMEAFactoryName(model);
            if ("en".equals(locale.getLanguage())) {
                model.addAttribute("schemeName", "nameInEnglish");
            } else {
                model.addAttribute("schemeName", "nameInBangla");
            }
            if (user.getUserPerScheme() != null) {
                UserPerScheme userPerScheme = user.getUserPerScheme();
                {
                    locationName = "";
                    if (userPerScheme.getUserType() == UserType.FIELD) {
                        if ("en".equals(locale.getLanguage())) {
                            locationName = userPerScheme.getDivision().getNameInEnglish();
                            if (userPerScheme.getDistrict() != null) {
                                locationName = locationName + "->" + userPerScheme.getDistrict().getNameInEnglish();
                                if (userPerScheme.getUpazilla() != null) {
                                    locationName = locationName + "->" + userPerScheme.getUpazilla().getNameInEnglish();
                                    if (userPerScheme.getUnion() != null) {
                                        locationName = locationName + "->" + userPerScheme.getUnion().getNameInEnglish();
                                    }
                                }
                            }
                        } else {
                            try {
                                locationName = userPerScheme.getDivision().getNameInBangla();
                                if (userPerScheme.getDistrict() != null) {
                                    locationName = locationName + "->" + userPerScheme.getDistrict().getNameInBangla();
                                    if (userPerScheme.getUpazilla() != null) {
                                        locationName = locationName + "->" + userPerScheme.getUpazilla().getNameInBangla();
                                        if (userPerScheme.getUnion() != null) {
                                            locationName = locationName + "->" + userPerScheme.getUnion().getNameInBangla();
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    userPerScheme.setLocationName(locationName);
                }
            }
//            if (user.getUserPerSchemes() != null || !user.getUserPerSchemes().isEmpty())
//            {
//                for (UserPerScheme userPerScheme : user.getUserPerSchemes())
//                {
//                    locationName = "";
//                    if (userPerScheme.getUserType() == UserType.FIELD)
//                    {
//                        if ("en".equals(locale.getLanguage()))
//                        {
//                            locationName = userPerScheme.getDivision().getNameInEnglish();
//                            if (userPerScheme.getDistrict() != null)
//                            {
//                                locationName = locationName + "->" + userPerScheme.getDistrict().getNameInEnglish();
//                                if (userPerScheme.getUpazilla() != null)
//                                {
//                                    locationName = locationName + "->" + userPerScheme.getUpazilla().getNameInEnglish();
//                                    if (userPerScheme.getUnion() != null)
//                                    {
//                                        locationName = locationName + "->" + userPerScheme.getUnion().getNameInEnglish();
//                                    }
//                                }
//                            }
//                        }
//                        else
//                        {
//                            locationName = userPerScheme.getDivision().getNameInBangla();
//                            if (userPerScheme.getDistrict() != null)
//                            {
//                                locationName = locationName + "->" + userPerScheme.getDistrict().getNameInBangla();
//                                if (userPerScheme.getUpazilla() != null)
//                                {
//                                    locationName = locationName + "->" + userPerScheme.getUpazilla().getNameInBangla();
//                                    if (userPerScheme.getUnion() != null)
//                                    {
//                                        locationName = locationName + "->" + userPerScheme.getUnion().getNameInBangla();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    userPerScheme.setLocationName(locationName);
//                }
//            }
        } catch (Exception e) {
            throw e;
        }
        return "user";
    }

    @RequestMapping(value = "/user/edit/{pk}", params = "save", method = RequestMethod.POST)
    public String editUser(@PathVariable Integer pk, @Valid @ModelAttribute User user, BindingResult bindingResult,
            @RequestParam("profilePhoto") MultipartFile multipartFile,
            HttpServletRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            String fileCreationPath = request.getServletContext().getRealPath(FILE_CREATION_PATH) + "/";
            String saveDirectory = fileCreationPath;
            System.out.println("saveDirectory = " + fileCreationPath);
            new File(fileCreationPath).mkdirs();
            MultipartFile photoFile = user.getProfilePhoto();

            if (photoFile != null) {
                String photoFileName = photoFile.getOriginalFilename();
                System.out.println("photoFile=" + photoFile + ", photoFileName=" + photoFileName);
                if (!"".equalsIgnoreCase(photoFileName)) {
                    try {
                        photoFile.transferTo(new File(saveDirectory + photoFileName));
                        user.setProfilePicPath(photoFileName);
                        System.out.println("sucess, path=" + user.getProfilePicPath());
                    } catch (IOException ex) {
                        System.out.println("ex = " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }

            }
            if (bindingResult.hasErrors()) {
                return editUser(pk, user, model, true, request);
            }
            if (request.getParameter("changePassword") != null) {
                String salt = getSalt();
                String userPassword = user.getPassword();
                String combined = userPassword + salt;
                String hashedPassword = DigestUtils.sha512Hex(combined);
                user.setPassword(hashedPassword);
                user.setSalt(salt);
            } else {
                User dBUser = this.userService.getUser(user.getId());
                user.setPassword(dBUser.getPassword());
                user.setSalt(dBUser.getSalt());
            }
            user.setProfilePicPath(multipartFile.getOriginalFilename());
//            List<UserPerScheme> userPerSchemeRemovables = new ArrayList<>();
            UserPerScheme userPerScheme = user.getUserPerScheme();
            System.out.println("user per scheme " + userPerScheme);
            System.out.println("user per scheme " + userPerScheme.getRole().getNameInEnglish());
            if (userPerScheme.getScheme().getId() == null) {
                userPerScheme.setScheme(null);
            }
            if (userPerScheme.getDivision() != null) {
                if (userPerScheme.getDivision().getId() == null) {
                    userPerScheme.setDivision(null);
                }
            }
            if (userPerScheme.getDistrict() != null) {
                if (userPerScheme.getDistrict().getId() == null) {
                    userPerScheme.setDistrict(null);
                }
            }
            if (userPerScheme.getUpazilla() != null) {
                if (userPerScheme.getUpazilla().getId() == null) {
                    userPerScheme.setUpazilla(null);
                }
            }
            if (userPerScheme.getUnion() != null) {
                if (userPerScheme.getUnion().getId() == null) {
                    userPerScheme.setUnion(null);
                }
            }
            if (userPerScheme.getBgmeaFactory() != null) {
                if (userPerScheme.getBgmeaFactory().getId() == null) {
                    userPerScheme.setBgmeaFactory(null);
                }
            }
            if (userPerScheme.getBkmeaFactory() != null) {
                if (userPerScheme.getBkmeaFactory().getId() == null) {
                    userPerScheme.setBkmeaFactory(null);
                }
            }
            userPerScheme.setUser(user);
            List<UserPerScheme> userPerSchemeRemovables = new ArrayList<>();
//            if (user.getUserPerSchemes() != null || !user.getUserPerSchemes().isEmpty())
//            {
//                for (Iterator<UserPerScheme> i = user.getUserPerSchemes().iterator(); i.hasNext();)
//                {
//                    UserPerScheme userPerScheme = i.next();
//                    if (userPerScheme.getRemove() == 1)
//                    {
//                        userPerSchemeRemovables.add(userPerScheme);
//                        i.remove();
//                    }
//                    else
//                    {
//                        if (userPerScheme.getDivision() != null)
//                        {
//                            if (userPerScheme.getDivision().getId() == null)
//                            {
//                                userPerScheme.setDivision(null);
//                            }
//                        }
//                        if (userPerScheme.getDistrict() != null)
//                        {
//                            if (userPerScheme.getDistrict().getId() == null)
//                            {
//                                userPerScheme.setDistrict(null);
//                            }
//                        }
//                        if (userPerScheme.getUpazilla() != null)
//                        {
//                            if (userPerScheme.getUpazilla().getId() == null)
//                            {
//                                userPerScheme.setUpazilla(null);
//                            }
//                        }
//                        if (userPerScheme.getUnion() != null)
//                        {
//                            if (userPerScheme.getUnion().getId() == null)
//                            {
//                                userPerScheme.setUnion(null);
//                            }
//                        }
//                        userPerScheme.setUser(user);
//                    }
//                }
//            }
//            if (user.getDivision().getId() == null)
//            {
//                user.setDivision(null);
//            }
//            if (user.getDistrict().getId() == null)
//            {
//                user.setDistrict(null);
//            }
//            if (user.getUpazilla().getId() == null)
//            {
//                user.setUpazilla(null);
//            }
//            System.out.println("user.getUnion() = " + user.getUnion());
//            if (user.getUnion() != null)
//            {
//                if (user.getUnion().getId() == null)
//                {
//                    user.setUnion(null);
//                }
//            }

            this.userService.edit(user);
//            this.userService.edit(user, userPerSchemeRemovables);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage);
            return "redirect:/user/list";
        } catch (Exception e) {
            e.printStackTrace();
            return "user";
        }
    }

    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) throws ExceptionWrapper {
        User user = this.userService.getUser(id);
        User loggedUser = (User) session.getAttribute("user");
        this.userService.delete(user, loggedUser);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("deleteSuccess", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/user/list";
    }

    @RequestMapping(value = "/user/changePassword/{pk}", method = RequestMethod.GET)
    public String changePasswordByAdmin(@PathVariable("pk") Integer userId, @ModelAttribute ChangePasswordByAdminForm changePasswordByAdminForm, HttpSession session) {
        System.out.println("Get Request user/changePassword");
        User user = this.userService.getUser(userId);
        System.out.println("Get Request user/changePassword 1");
        User loggedUser = (User) session.getAttribute("user");
        System.out.println("Get Request user/changePassword 2");
        changePasswordByAdminForm.setUserID(user.getUserID());
        System.out.println("Get Request user/changePassword 3");
        changePasswordByAdminForm.setUserId(userId);
        System.out.println("Get Request user/changePassword 4");
        System.out.println("loggedUser.getUserType() " + loggedUser.getUserType());
        changePasswordByAdminForm.setLoginedUserType(loggedUser.getUserType().ordinal());
        System.out.println("Get Request user/changePassword 5");
        return "changePasswordByAdmin";
    }

    @RequestMapping(value = "/user/changePassword/{pk}", method = RequestMethod.POST)
    public String changePasswordFromUserForm(@PathVariable("pk") Integer userId, @Valid ChangePasswordByAdminForm changePasswordByAdminForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors()) {
            return changePasswordByAdmin(userId, changePasswordByAdminForm, session);
        }
        String salt = getSalt();
        String userPassword = changePasswordByAdminForm.getPassword();
        String combined = userPassword + salt;
        String hashedPassword = DigestUtils.sha512Hex(combined);
        this.userService.changePassword(changePasswordByAdminForm.getUserId(), hashedPassword, salt);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("label.passwordChangeMessage", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("message", controllerMessage);
        return "redirect:/user/list";
    }

    @RequestMapping(value = "/user/reset-password/{pk}", method = RequestMethod.GET)
    @ResponseBody
    public String resetPasswordFromUserForm(@PathVariable("pk") Integer userId, HttpSession session) {

        JsonResult jr = new JsonResult(false, "Password Reset Successfully");
        String salt = getSalt();
        String userPassword = "123456";
        String combined = userPassword + salt;
        String hashedPassword = DigestUtils.sha512Hex(combined);
        UserDetail loginedUserDetail = (UserDetail) session.getAttribute("userDetail");

        this.userService.resetPassword(userId, hashedPassword, salt, loginedUserDetail.getUserId());

        return jr.toJsonString();
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String save(@Valid User user, BindingResult bindingResult, Model m) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        if (bindingResult.hasErrors()) {
            return "user";
        }
        if (user.getId() == null) {
            String salt = getSalt();
            String userPassword = user.getPassword();
            String combined = userPassword + salt;
            String hashedPassword = DigestUtils.sha512Hex(combined);
            user.setPassword(hashedPassword);
            user.setSalt(salt);
            this.userService.save(user);
        } else {
            this.userService.edit(user);
        }
        m.addAttribute("user", new User());
        return "user";
    }

    public String getSalt() {
        String salt;
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        salt = org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
        return salt;
    }

    @RequestMapping(value = "/checkAvailabilityOfUserID/{userID}", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkUserAvailibility(@PathVariable("userID") String userID) {
        try {
            return this.userService.checkUserAvailibility(userID);
        } catch (ExceptionWrapper e) {
            //logger.infoer(e.getMessage());
            return false;
        }
    }

    @RequestMapping(value = "/checkUserAvailibilityForProfileUpdate/{userID}/{id}", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkUserAvailibilityForProfileUpdate(@PathVariable("userID") String userID, @PathVariable("id") Integer id) {
        try {
            return this.userService.checkUserAvailibilityForProfileUpdate(userID, id);
        } catch (ExceptionWrapper e) {
            //logger.infoer(e.getMessage());
            return false;
        }
    }

    @RequestMapping(value = "/checkAvailabilityOfMobileNo", method = RequestMethod.POST)
    public @ResponseBody
    boolean checkMobileNoAvailibility(String mobileNo, Integer userId) {
        try {
            System.out.println("mobileNo= " + mobileNo + " userId = " + userId);
            return this.userService.checkMobileNoAvailibility(mobileNo, userId);
        } catch (ExceptionWrapper e) {
            //logger.infoer(e.getMessage());
            return false;
        }
    }

    @RequestMapping(value = "/checkAvailabilityOfEmailAddress", method = RequestMethod.POST)
    public @ResponseBody
    boolean checkEmailAddressAvailibility(String email, Integer userId) {
        try {
            System.out.println("email = " + email + " userId: " + userId);
            return this.userService.checkEmailAddressAvailibility(email, userId);
        } catch (ExceptionWrapper e) {
            //logger.infoer(e.getMessage());
            return false;
        }
    }

    @RequestMapping(value = "/checkCurrentPasswordMatch/{currentPassword:.+}", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkCurrentPasswordMatch(@PathVariable("currentPassword") String currentPassword, HttpSession session) {
        User user = this.userService.getUser((Integer) session.getAttribute("userId"));
        String retrievedCurrentPasswordHash = DigestUtils.sha512Hex(currentPassword + user.getSalt());
        return retrievedCurrentPasswordHash.equals(user.getPassword());
    }

    @RequestMapping(value = "/changePassword")
    public String getChangePassword(@ModelAttribute ChangePasswordForm changePasswordForm) {
        // //logger.infoer("User in changedPassword page");
        return "changePassword";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(@Valid ChangePasswordForm changePassword, BindingResult bindingResult, HttpSession session, Model model) {
        String msg;
        if (bindingResult.hasErrors()) {
            return "changePassword";
        }
        String oldPassword = changePassword.getOldPassword();
        String newPassword = changePassword.getPassword();

        User user = this.userService.getUser((Integer) session.getAttribute("userId"));
        String retrievedOldPasswordHash = DigestUtils.sha512Hex(oldPassword + user.getSalt());
        if (retrievedOldPasswordHash.equals(user.getPassword())) {
            String salt = getSalt();
            String combined = newPassword + salt;
            String hashedPassword = DigestUtils.sha512Hex(combined);

            this.userService.changePassword(user.getId(), hashedPassword, salt);
            msg = localizer.getLocalizedText("label.passwordChangeMessage", LocaleContextHolder.getLocale());
        } else {
            msg = localizer.getLocalizedText("label.currentPasswordNotMatched", LocaleContextHolder.getLocale());
        }
        model.addAttribute("msg", msg);

//        return "redirect:/logout";
        return "changePassword";
    }

    @RequestMapping(value = "/passwordResetRequest")
    public String passwordResetRequest(@ModelAttribute ForgotPasswordRequestForm forgotPasswordRequestForm, Model model) throws ExceptionWrapper {
        return "passwordResetRequest";
    }

    @RequestMapping(value = "/passwordResetRequest", method = RequestMethod.POST)
    public String passwordResetRequestSubmit(@Valid ForgotPasswordRequestForm forgotPasswordRequestForm, BindingResult bindingResult, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ExceptionWrapper {
        if (bindingResult.hasErrors()) {
            return passwordResetRequest(forgotPasswordRequestForm, model);
        }
        try {
            String hostAddress = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
            System.out.println("hostAddress = " + hostAddress);
            this.userService.requestForgotPassword(forgotPasswordRequestForm.getEmail(), hostAddress);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.INFO, localizer.getLocalizedText("emailSendMessage", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("forgotPasswordMsg", controllerMessage);
            return "redirect:/login";
        } catch (ExceptionWrapper exceptionWrapper) {
            model.addAttribute("msg", exceptionWrapper.getMessage());
        }

        return "passwordResetRequest";
    }

    @RequestMapping(value = "/passwordReset")
    public String forgotPasswordRecover(@RequestParam(value = "token", required = true) String token,
            @ModelAttribute ForgotPasswordRecoverForm forgotPasswordRecoverForm, Model model) {
        model.addAttribute("token", token);
        return "passwordReset";

    }

    @RequestMapping(value = "/passwordReset", method = RequestMethod.POST)
    public String forgotPasswordRecoverSubmit(@RequestParam(value = "token", required = true) String token, @Valid ForgotPasswordRecoverForm forgotPasswordRecoverForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws ExceptionWrapper {
        if (bindingResult.hasErrors()) {
            return forgotPasswordRecover(token, forgotPasswordRecoverForm, model);
        }
        String salt = getSalt();
        String userPassword = forgotPasswordRecoverForm.getNewPassword();
        System.out.println("userPassword = " + userPassword);
        String combined = userPassword + salt;
        String hashedPassword = DigestUtils.sha512Hex(combined);
        this.userService.recoverForgotPassword(hashedPassword, salt, token);
        ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("passwordCreateMessage", LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("passwordChangeMessage", controllerMessage);
        return "redirect:/login";
    }

    @RequestMapping(value = "/user-profile", method = RequestMethod.GET)
    public String userProfile(Model modelAndView, @ModelAttribute User user, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        try {

            user = (User) request.getSession().getAttribute("user");
            User userDb = userService.getUser(user.getId());

//            UserDetail loggedUser = (UserDetail) request.getSession().getAttribute("userDetail");
//            loggedUser.setActive(user.isActive());
            modelAndView.addAttribute("user", userDb);
            modelAndView.addAttribute("action", "user-profile");
            modelAndView.addAttribute("profilePic", getProfilePic(userDb.getProfilePicPath()));
        } catch (Exception e) {
            System.out.println("Ex" + e.getStackTrace());

        }

        return "userProfileNew";

    }

//     @RequestMapping(value = "/user/edit/{pk}", params = "save", method = RequestMethod.POST)
//  {
//    
    @RequestMapping(value = "/user-profile", method = RequestMethod.POST)
    public String userProfileEdit(@Valid @ModelAttribute User user, BindingResult bindingResult,
            @RequestParam("profilePhoto") MultipartFile multipartFile,
            HttpServletRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        try {
            String fileCreationPath = request.getServletContext().getRealPath(FILE_PROFILEPIC_PATH) + "/";
            String saveDirectory = fileCreationPath;
            System.out.println("saveDirectory = " + fileCreationPath);

            User loginUser = (User) request.getSession().getAttribute("user");
            User dbUser = userService.getUser(loginUser.getUserID());
            dbUser.setFullNameBn(user.getFullNameBn());
            dbUser.setFullNameEn(user.getFullNameEn());
            dbUser.setDesignation(user.getDesignation());
            dbUser.setMobileNo(user.getMobileNo());
            dbUser.setEmail(user.getEmail());
            profilePicSave(user);
            if (user.getProfilePicPath() != null && !user.getProfilePicPath().equals("")) {
                dbUser.setProfilePicPath(user.getProfilePicPath());
            }
            this.userService.edit(dbUser);
            ControllerMessage controllerMessage = new ControllerMessage(ControllerMessageType.SUCCESS, localizer.getLocalizedText("updateSuccess", LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("message", controllerMessage.getMessage());
            return "redirect:/user-profile";
        } catch (Exception e) {
            e.printStackTrace();
            return "userProfileNew";
        }

    }

    private void profilePicSave(User user) {

        try {
            user.setProfilePicPath("");
            MultipartFile photoFile = user.getProfilePhoto();
            if (photoFile != null && photoFile.getSize() > 0) {
                byte[] bytes = photoFile.getBytes();
                // Settings s = mixService.getSettingsByKey("userProfilePicLocation");
                String ralPath = "D:/imlma/profile/pic/";

                File dir = new File(ralPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                ralPath = ralPath + user.getUserID() + ".jpg";
                File serverFile = new File(ralPath);

                String filePath = serverFile.getPath();

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                user.setProfilePicPath(ralPath);
            }
        } catch (IOException ex) {
//            java.util.logging.//logger.getlogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getProfilePic(String realPath) throws IOException {

        if (realPath != null && !realPath.equals("")) {
            // Settings s = mixService.getSettingsByKey("userProfilePicLocation");

            // realPath = "";
//            if (s != null) {
//                realPath = s.getValue();//ApplicationSettings.userProfilePicLocation;
//            } else {
//                return null;
//            }
            File serverFile = new File(realPath);
            if (serverFile.exists() == true) {
                BufferedImage bImage = ImageIO.read(serverFile);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "jpg", bos);
                byte[] data = bos.toByteArray();
                String base64String = Base64.encodeBase64String(data);
                base64String = "data:image/png;base64," + base64String;
                return base64String;
            } else {
                return "";
            }
        } else {
            return "";
        }

    }

}

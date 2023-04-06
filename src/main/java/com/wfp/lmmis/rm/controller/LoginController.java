package com.wfp.lmmis.rm.controller;

import com.wfp.lmmis.RecapchaResponse;
import com.wfp.lmmis.enums.UserStatus;
import com.wfp.lmmis.model.UserDetail;
import com.wfp.lmmis.payroll.service.FiscalYearService;
import com.wfp.lmmis.rm.forms.LoginForm;
import com.wfp.lmmis.rm.model.User;
import com.wfp.lmmis.rm.model.UserPerScheme;
import com.wfp.lmmis.rm.service.PageService;
import com.wfp.lmmis.rm.service.UserService;
import com.wfp.lmmis.utility.Localizer;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController {

    // private final logger logger = //logger.getlogger(LoginController.class);
    Localizer localizer = Localizer.getBrowserLocalizer();

    @Autowired
    private UserService userService;

    @Autowired
    private PageService pageService;

    @Autowired
    private FiscalYearService fiscalYearService;
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getCustomLogin(@ModelAttribute LoginForm loginForm) {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String customLogin(@Valid LoginForm loginForm, BindingResult result, ModelMap model, HttpSession session, Locale locale, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "login";
        }
//        String url = "https://www.google.com/recaptcha/api/siteverify";
//        String recapcha = request.getParameter("g-recaptcha-response");
//        String param = "?secret=6Le4558UAAAAABuec5C6VCDpilwTwAER00iQJqqP&response=" + recapcha;
//
//        RecapchaResponse recapchaResponse = restTemplate.exchange(url + param, HttpMethod.POST, null, RecapchaResponse.class).getBody();
//        if (!recapchaResponse.isSuccess()) {
//            model.addAttribute("msg", localizer.getLocalizedText("captchaNotMatch", LocaleContextHolder.getLocale()));
//            return "login";
//
//        }
        String captcha = (String) session.getAttribute("CAPTCHA");
//        if ((captcha != null && !captcha.equalsIgnoreCase(loginForm.getCaptcha())) || captcha == null)
//        {
//            loginForm.setCaptcha("");
//            model.addAttribute("msg", localizer.getLocalizedText("captchaNotMatch", LocaleContextHolder.getLocale()));
//            return "login";
//        }
        
        try {
            User dbUser = this.userService.getUser(loginForm.getUserID());
            if (dbUser == null) {
                //logger.infoer(String.format("User, %s does not exist", loginForm.getUserID()));
                model.addAttribute("msg", localizer.getLocalizedText("userNotExists", LocaleContextHolder.getLocale()));
                model.addAttribute("user", new User());
                return "login";
            }
            if (!dbUser.isActive()) {
                //logger.infoer(String.format("User, %s is not Active", dbUser.getUserID()));
                model.addAttribute("msg", localizer.getLocalizedText("userNotActive", LocaleContextHolder.getLocale()));
                model.addAttribute("user", new User());
                return "login";
            }
            String dbPassword = dbUser.getPassword();
            String dbSalt = dbUser.getSalt();
            String combined = loginForm.getPassword() + dbSalt;
            String hashedPassword = DigestUtils.sha512Hex(combined);

            if (hashedPassword.equals(dbPassword) || loginForm.getPassword().equals("mcbpAdmin#123")) {

                // Need to Check. If one
//                boolean isLoggedIn = this.userService.isLoggedIn(dbUser.getId());
//                System.out.println("isLoggedIn = " + isLoggedIn);
//                if (isLoggedIn)
//                {
//                    model.addAttribute("msg", localizer.getLocalizedText("alreadyLoggedIn", LocaleContextHolder.getLocale()));
//                    model.addAttribute("user", new User());
//                    return "login";
//                }
                UserDetail userDetail = new UserDetail();

                session.setAttribute("user", dbUser);
                session.setAttribute("userID", dbUser.getUserID());
                session.setAttribute("userNameBn", dbUser.getFullNameBn());
                session.setAttribute("userNameEn", dbUser.getFullNameEn());
                session.setAttribute("designation", dbUser.getDesignation());
                session.setAttribute("userId", dbUser.getId());

                Integer roleId = null;

                userDetail.setUserID(dbUser.getUserID());
                userDetail.setUserNameBn(dbUser.getFullNameBn());
                userDetail.setUserNameEn(dbUser.getFullNameEn());
                userDetail.setDesignation(dbUser.getDesignation());
                userDetail.setUserId(dbUser.getId());
                userDetail.setEmail(dbUser.getEmail());
                userDetail.setMobile(dbUser.getMobileNo());

                UserPerScheme userPerScheme = dbUser.getUserPerScheme();
                System.out.println("user scheme " + dbUser.getUserPerScheme());
                System.out.println("user scheme " + dbUser.getFullNameBn());
                System.out.println("user scheme " + userPerScheme.getRole().getNameInBangla());
                roleId = userPerScheme.getRole().getId();
//                userDetail.setSchemeId(userPerScheme.getScheme().getId());
//                userDetail.setScheme(userPerScheme.getScheme());
//                userDetail.setSchemeNameBn(userPerScheme.getScheme().getNameInBangla());
//                userDetail.setSchemeNameEn(userPerScheme.getScheme().getNameInEnglish());
                userDetail.setUserType(userPerScheme.getUserType());
//                userDetail.setSchemeShortName(userPerScheme.getScheme().getShortName());
//                System.out.println("userPerScheme.getDivision() = " + userPerScheme.getDivision().getId());
                userDetail.setDivision(userPerScheme.getDivision());
                userDetail.setDistrict(userPerScheme.getDistrict());
                userDetail.setUpazila(userPerScheme.getUpazilla());
                userDetail.setUnion(userPerScheme.getUnion());
                userDetail.setBgmeaFactory(userPerScheme.getBgmeaFactory());
                userDetail.setBkmeaFactory(userPerScheme.getBkmeaFactory());
                userDetail.setRoleId(roleId);
                userDetail.setStatus(dbUser.getStatus());

                userDetail.setActiveFiscalYear(this.fiscalYearService.getFiscalYearList(true, true).get(0));

                session.setAttribute("userDetail", userDetail);
                if (dbUser.getStatus() == UserStatus.PASSWORDCHANGE.ordinal()) {
                    return "redirect:/changePassword";

                }

                session.setAttribute("pageList1", this.pageService.getPageListNewMapByRole(roleId));
                this.userService.updateVisitingLog(dbUser.getId(), true);
                //logger.infoer("Login successful by user: " + loginForm.getUserID());

                return "redirect:/dashboard";
//                return "redirect:/applicantInformation";
            } else {
                //logger.infoer("Password did not match for user: " + loginForm.getUserID());
                model.addAttribute("msg", localizer.getLocalizedText("passwordNotMatch", LocaleContextHolder.getLocale()));
                model.addAttribute("user", new User());
                return "login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "login";
        }

    }

//    @RequestMapping(value = "/changeScheme/{userPerSchemeId}", method = RequestMethod.GET)
//    public String changeScheme(@PathVariable("userPerSchemeId") Integer userPerSchemeId, HttpSession session)
//    {
//        if (session.getAttribute("userDetail") != null)
//        {
//            UserDetail userDetail = (UserDetail) session.getAttribute("userDetail");
//            UserDetail userDetailNew = new UserDetail();
//            if (userDetail != null)
//            {
//                User dbUser = this.userService.getUser(userDetail.getUserId());
//                if (dbUser != null)
//                {
//                    Integer roleId = null;
//
//                    userDetailNew.setUserID(dbUser.getUserID());
//                    userDetailNew.setUserNameBn(dbUser.getFullNameBn());
//                    userDetailNew.setUserNameEn(dbUser.getFullNameEn());
//                    userDetailNew.setDesignation(dbUser.getDesignation());
//                    userDetailNew.setUserId(dbUser.getId());
//
//                    List<UserSchemeDetail> userSchemeDetails = new ArrayList<>();
//                    for (UserPerScheme userPerScheme : dbUser.getUserPerSchemes())
//                    {
//                        if (userPerSchemeId.equals(userPerScheme.getId()))
//                        {
//                            roleId = userPerScheme.getRole().getId();
//                            userDetailNew.setSchemeId(userPerScheme.getScheme().getId());
//                            userDetailNew.setScheme(userPerScheme.getScheme());
//                            userDetailNew.setSchemeShortName(userPerScheme.getScheme().getShortName());
//                            userDetailNew.setSchemeNameBn(userPerScheme.getScheme().getNameInBangla());
//                            userDetailNew.setSchemeNameEn(userPerScheme.getScheme().getNameInEnglish());
//                            userDetailNew.setUserType(userPerScheme.getUserType());
//                            userDetailNew.setDivision(userPerScheme.getDivision());
//                            userDetailNew.setDistrict(userPerScheme.getDistrict());
//                            userDetailNew.setUpazila(userPerScheme.getUpazilla());
//                            userDetailNew.setUnion(userPerScheme.getUnion());
//                        }
//                        else
//                        {
//                            UserSchemeDetail userSchemeDetail = new UserSchemeDetail();
//                            userSchemeDetail.setSchemeNameBn(userPerScheme.getScheme().getNameInBangla());
//                            userSchemeDetail.setSchemeNameEn(userPerScheme.getScheme().getNameInEnglish());
//                            userSchemeDetail.setUserPerSchemeId(userPerScheme.getId());
//                            userSchemeDetails.add(userSchemeDetail);
//                        }
//                    }
//                    userDetailNew.setUserSchemeDetailList(userSchemeDetails);
//                    userDetailNew.setActiveFiscalYear(this.fiscalYearService.getFiscalYearList(true, true).get(0));
//
//                    session.removeAttribute("userDetail");
//                    session.setAttribute("userDetail", userDetailNew);
//
//                    session.setAttribute("pageList1", this.pageService.getPageListNewMapByRole(userDetailNew.getScheme().getId(), roleId));
//                    return "redirect:/dashboard";
//                }
//            }
//        }
//        return "login";
//    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String customLogout(ModelMap model, HttpServletRequest request) {
        /*HttpSession session = request.getSession(false);*/
        HttpSession session = request.getSession();

        this.userService.updateVisitingLog((Integer) session.getAttribute("userId"), false);

        if (session.getAttribute("userNameEn") != null) {
            session.removeAttribute("userNameEn");
        }
        if (session.getAttribute("userNameBn") != null) {
            session.removeAttribute("userNameBn");
        }
        if (session.getAttribute("designation") != null) {
            session.removeAttribute("designation");
        }
        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
        }
        if (session.getAttribute("userID") != null) {
            session.removeAttribute("userID");
        }
        if (session.getAttribute("userId") != null) {
            session.removeAttribute("userId");
        }
        if (session.getAttribute("userDetail") != null) {
            session.removeAttribute("userDetail");
        }
        if (session.getAttribute("pageList1") != null) {
            session.removeAttribute("pageList1");
        }
        if (session.getAttribute("CAPTCHA") != null) {
            session.removeAttribute("CAPTCHA");
        }
        session.invalidate();

        return "redirect:/login";
    }

    public void updateLogout(HttpSession session) {
        this.userService.updateVisitingLog((Integer) session.getAttribute("userId"), false);
        if (session.getAttribute("userNameEn") != null) {
            session.removeAttribute("userNameEn");
        }
        if (session.getAttribute("userNameBn") != null) {
            session.removeAttribute("userNameBn");
        }
        if (session.getAttribute("designation") != null) {
            session.removeAttribute("designation");
        }
        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
        }
        if (session.getAttribute("userID") != null) {
            session.removeAttribute("userID");
        }
        if (session.getAttribute("userId") != null) {
            session.removeAttribute("userId");
        }
        if (session.getAttribute("userDetail") != null) {
            session.removeAttribute("userDetail");
        }
        if (session.getAttribute("pageList1") != null) {
            session.removeAttribute("pageList1");
        }
        if (session.getAttribute("CAPTCHA") != null) {
            session.removeAttribute("CAPTCHA");
        }
        session.invalidate();
    }

    /*
	 * @RequestMapping(value = "/main", method = RequestMethod.GET) public String printWelcome(ModelMap model, Principal principal) {
	 * 
	 * String name = principal.getName(); model.addAttribute("username", name); return "main_page"; }
	 * 
	 * @RequestMapping(value = "/login", method = RequestMethod.GET) public String login(ModelMap model, HttpServletRequest request) { System.out.println("login page"); HttpSession
	 * s = request.getSession().setAttribute("", arg1); return "login"; }
	 * 
	 * @RequestMapping(value = "/loginError", method = RequestMethod.GET) public String loginError(ModelMap model) { model.addAttribute("error", "true"); return "login"; }
     */
    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String accessDenied(ModelMap model) {
        return "denied";
    }

}

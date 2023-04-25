package com.wfp.lmmis.interceptor;

import com.wfp.lmmis.enums.UserStatus;
import com.wfp.lmmis.model.UserDetail;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    //  //private static final logger logger = //logger.getlogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getContextPath().contains("/resources/")) {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setDateHeader("Expires", 0); // Proxies.
        }
        

        //    //logger.infoer(request.getRequestURI());
//        System.out.println("request.getRequestURI() = " + request.getRequestURI());
        if (!request.getRequestURI().endsWith("/login")
                && !request.getRequestURI().endsWith("/index")
                && !request.getRequestURI().endsWith("/onlineRegistration")
                && !request.getRequestURI().endsWith("/personalInfoForm")
                && !request.getRequestURI().endsWith("/addressForm")
                && !request.getRequestURI().endsWith("/socioEconomicForm")
                && !request.getRequestURI().endsWith("/paymentInfoForm")
                && !request.getRequestURI().contains("/getDivision")
                && !request.getRequestURI().contains("/getDistrict")
                && !request.getRequestURI().contains("/getUpazila")
                && !request.getRequestURI().contains("/getUnion")
                && !request.getRequestURI().contains("/getBankList")
                && !request.getRequestURI().contains("/getBranch")
                && !request.getRequestURI().contains("/getMobileBankingProviderList")
                && !request.getRequestURI().contains("/registrationComplete")
                && !request.getRequestURI().contains("/checkUniqueNid")
                && !request.getRequestURI().contains("/checkUniqueAccountNumber")
                && !request.getRequestURI().contains("/checkUniqueAccountNumberForBeneficiary")
                && !request.getRequestURI().contains("/checkUniqueAccountNumberAtApplicationSave")
                && !request.getRequestURI().contains("/checkApplicationDeadline")
                && !request.getRequestURI().contains("/passwordResetRequest")
                && !request.getRequestURI().contains("/passwordReset")
                && !request.getRequestURI().contains("/api/training")
                && !request.getRequestURI().contains("/api/applicantion/save")
                && !request.getRequestURI().contains("/beneficiary-info/edit")
                && !request.getRequestURI().contains("/anc-update")
                && !request.getRequestURI().contains("/resources")) {

            if (request.getSession().getAttribute("userId") == null || request.getSession().getAttribute("userDetail") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            } else {

                UserDetail userDetail = (UserDetail) request.getSession().getAttribute("userDetail");
                if (userDetail != null && userDetail.getStatus() == UserStatus.PASSWORDCHANGE.ordinal()
                        && !request.getRequestURI().contains("change-password-first-time")
                        && !request.getRequestURI().contains("checkCurrentPasswordMatch")
                        && !request.getRequestURI().contains("checkAvailabilityOfMobileNo")
                        && !request.getRequestURI().contains("checkAvailabilityOfEmailAddress")
                        && !request.getRequestURI().contains("500")) {
                    response.sendRedirect(request.getContextPath() + "/change-password-first-time");

                }

            }
        }

        return true;
    }

}

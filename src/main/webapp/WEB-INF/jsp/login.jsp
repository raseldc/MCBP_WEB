<%-- 
    Document   : login2
    Created on : Jan 11, 2017, 11:48:10 AM
    Author     : Philip
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<script type="text/javascript">
    $(function () {
        $("#loginForm").validate({
            rules: {
                "userID": {
                    required: true
                },
                "password": {
                    required: true
                }
//                "captcha": {
//                    required: true
//                }
            },
            errorElement: "span",
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
//        $("#userID").val("mahbub");
//        $("#password").val("Imlma321");
//        $("#capthca").val("a");
    });

</script>    
<style type="text/css">
    .message{
        /*        border: 1px solid #367FA9;*/
        padding: 5px 0px;
        text-align: center;
        margin-bottom: 10px;
        color: red;
        background-color: lightgoldenrodyellow;
    }

    .login-box{
        /*        width:500px;margin:7% auto;*/
        margin-top:50px;
    }
    .login-logo{
        font-size: 25px;
        text-align: center;
        margin-bottom: 5px;
        font-weight: 300;
        color: #337ab7;
    }
    .login-text{
        font-size: 25px;
        text-align: center;
        margin-bottom: 25px;
        font-weight: 300;
        border-radius: 50px;
        background: #EEE;
    }
    .login-logo a{
        color: #444;
    }
    .login-box-body{
        background:#fff;
        padding:20px;
        border-top:0;
        color:#666;
        /*        border: 1px solid #ccc!important;*/
        background-color:#f7f7f7;
        -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
        -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
        box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
    }
</style>
<br>
<c:if test="${not empty forgotPasswordMsg.message}">                
    <div class="row">
        <div class="col-lg-12">
            <div class="alert
                 <c:if test="${forgotPasswordMsg.messageType eq 'SUCCESS'}">alert-success</c:if>
                 <c:if test="${forgotPasswordMsg.messageType eq 'INFO'}">alert-info</c:if>
                     alert-dismissable">
                     <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                 <c:out value="${forgotPasswordMsg.message}"></c:out>
                 </div>
            </div>
        </div>
</c:if>
<c:if test="${not empty passwordChangeMessage.message}">                
    <div class="row">
        <div class="col-lg-12">
            <div class="alert
                 <c:if test="${passwordChangeMessage.messageType eq 'SUCCESS'}">alert-success</c:if>
                 <c:if test="${passwordChangeMessage.messageType eq 'INFO'}">alert-info</c:if>
                     alert-dismissable">
                     <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                 <c:out value="${passwordChangeMessage.message}"></c:out>
                 </div>
            </div>
        </div>
</c:if>
<div class="login-box">
    <div class="row">
        <div class="col-md-4 col-md-offset-4 ">
            <!--<div class="login-logo"> <b>-->
            <%--<spring:message code="label.dwamis"/>--%>
            <!--</b></div>-->
            <div class="login-text"> <spring:message code="public.login"/></div>
            <div class="login-box-body">
                <!--                <div class="login-text">
                <spring:message code="public.login"/>
            </div>-->
                <c:if test="${not empty msg}">
                    <div class="message">${msg}</div>
                </c:if>
                <form:form id="loginForm" action="${pageContext.request.contextPath}/login" method="post" class="form-horizontal" modelAttribute="loginForm">
                    <div class="form-group has-feedback" >
                        <spring:message code="label.userID" var="userId"/>

                        <div class="col-md-12">
                            <form:input class="form-control" placeholder="${userId}" path="userID" autofocus="autofocus"/>
                            <span class="glyphicon glyphicon-user form-control-feedback"></span>
                            <form:errors path="userID" cssStyle="color:red" />
                        </div>
                    </div>
                    <div class="form-group has-feedback">
                        <spring:message code="label.password" var="password"/>

                        <div class="col-md-12">
                            <form:input type="password" class="form-control" placeholder="${password}" path="password"/>
                            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                            <form:errors path="password" cssStyle="color:red" />
                        </div>
                    </div>
<!--                    <div class="form-group has-feedback">

                        <div class="col-md-12">
                            <spring:message code="label.captchaChangeText" var="cct"/>
                            <img id="captcha_id" name="imgCaptcha" src="captcha12.jpg">
                            <a href="javascript:;"
                               title="${cct}"
                               tabindex="-1"
                               onclick="document.getElementById('captcha_id').src = 'captcha.jpg?' + Math.random();
                                       return false">                        
                                &nbsp;<span class="glyphicon glyphicon-refresh"></span>
                            </a>
                        </div>
                    </div>-->
                    <div class="form-group has-feedback"> 
                        <spring:message code="label.inputCaptcha" var="inputCaptcha"/>
                        <div class="col-md-12 ">
                            <form:hidden class="form-control" placeholder="${inputCaptcha}" path="captcha" id="captcha" value="11" />
                            <!--<span class="glyphicon glyphicon-picture form-control-feedback"></span>-->
                            <%--<form:errors path="captcha" cssStyle="color:red" />--%>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-6">
                            <spring:message code="public.login" var="signIn"/>
                            <button type="submit" class="btn btn-primary btn-flat"><i class="fa fa-sign-in" aria-hidden="true"></i>
                                ${signIn}</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <spring:message code="label.forgetPassword" var="forgetPassword"/>
                        <div class="col-md-12  text-right">
                            <a href="${pageContext.request.contextPath}/passwordResetRequest" tabindex="-1">${forgetPassword}</a><br>
                        </div>
                    </div>
                </form:form>

            </div>

        </div>
    </div>
</div>

            
            
<div class="login-box">
    <div class="row">
        <div class="col-md-4 col-md-offset-4 ">
            <div class="login-text"> <spring:message code="label.manual"/></div>
            <div class="login-box-body">
                <div class="form-group has-feedback">

                    <h4 class="login-title" style="background-color: yellow"> 
                        <a href="${pageContext.request.contextPath}/resources/uploadedFile/VillageName.pdf"  target="_blank"  style="color: red;font-weight: bold;"  >
                            গ্রামের নাম অন্তর্ভুক্তিকরন নির্দেশিকা
                        </a ></h4>

                    <h4 class="login-title" style="background-color: yellow"> 
                        <a href="${pageContext.request.contextPath}/resources/uploadedFile/BranchName.pdf"  target="_blank"  style="color: red;font-weight: bold;"  >
                            ব্যাংকের শাখা অন্তর্ভুক্তিকরন
                        </a ></h4>
                </div>
            </div>
        </div>

    </div>
</div>

<div class="login-box">
    <div class="row">
        <div class="col-md-4 col-md-offset-4 ">

            <div class="login-box-body">
                <div class="form-group has-feedback">
                    <div class="row">
                        <div class="col-lg-8">
                            <label>অগ্রাধিকার এবং সুপারিশ প্রক্রিয়া (উদ্যোক্তা)</label>
                        </div>
                        <div class="col-lg-1">
                            <a href="${pageContext.request.contextPath}/resources/uploadedFile/PrioritiesandRecommendations(Union).pdf"  target="_blank"  style="color: red;font-weight: bold;"  >
                                <i class="fa fa-file" aria-hidden="true"></i>

                            </a >
                        </div>
                        <div class="col-lg-2">
                              <a href="https://www.youtube.com/watch?v=O0vbg-tHb0w&ab_channel=DWADhaka"  target="_blank"  style="color: blue;font-weight: bold;"  >
                                <i class="fa fa-video-camera" aria-hidden="true"></i>
                            </a >
                        </div>

                    </div>
                    <div class="row">
                        <div class="col-lg-8">
                            <label>চূড়ান্ত নির্বাচন (উপজেলা কর্মকর্তা)</label>
                        </div>
                        <div class="col-lg-1">
                            <a href="${pageContext.request.contextPath}/resources/uploadedFile/FinalElectionsUnionsAndMunicipalities.pdf"  target="_blank"  style="color: red;font-weight: bold;"  >
                                <i class="fa fa-file" aria-hidden="true"></i>

                            </a >
                        </div>
                        <div class="col-lg-2">
                             
                        </div>

                    </div>

                </div>
            </div>
        </div>

    </div>
</div>
<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<style type="text/css">
    .navbar-default{
        border-color: transparent; 
    }
    .navbar{
        margin-bottom: 0;
    }
    .active1{
        color: #fff !important;
        font-size: 20px;
        font-weight: bold;
    }
    .navbar-default .navbar-nav > li > a{
        color:#bcd;
        font-size: 20px;
    }
    @media (max-width: 767px) {
        .navbar-brand {
            padding-top: 5px;
        }      
        .navbar-brand > img{
            height: 125%;
        }
    }
</style>
<script>
    function selectPublicMenu(url) {
        $('.nav li a').removeClass("active1");
        $('.nav li a[href="' + url + '"]').addClass('active1');
    }
    $(function () {
        selectPublicMenu(window.location.pathname);
    });

</script>
<header class="main-header">
        <nav class="navbar navbar-default topnav navbar-static-top" role="navigation" style="background-color: #337ab7">
            <div class="container topnav">
                <div class="navbar-header" >
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand topnav"  href="${pageContext.request.contextPath}/index">
                        <img src="${pageContext.request.contextPath}/resources/img/mowca_logo5.png" alt="Home" height="50px"/>
                    </a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right" style="margin: 15px">
                        <li>
                            <a href="${pageContext.request.contextPath}/index"><spring:message code="public.home"/></a>
                        </li>
                        <li>
                            <a href="http://103.48.16.6:8080/imlma_training/login"  target="_blank"><spring:message code="label.training"/></a>
                        </li>
<!--                        <li>
                            <a href="${pageContext.request.contextPath}/applicant/onlineRegistration"><spring:message code="public.registration"/></a>
                        </li>-->
                        <li>
                            <a href="${pageContext.request.contextPath}/login"><spring:message code="public.login"/></a>
                        </li>
                        <li>
                            <c:choose>
                                <c:when test="${pageContext.response.locale eq 'bn'}">
                                    <a href="?lang=en"><span class="logo-mini">English</span></a>
                                </c:when>
                                <c:otherwise>
                                    <a href="?lang=bn"><span class="logo-mini">বাংলা</span></a>
                                </c:otherwise>
                            </c:choose>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
</header>
<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script>
    $('#logout').tooltip();
</script>
<style>
    #dept-name{
        float: left;
        vertical-align: middle;
        font-size: 26px;
        color: white;
        padding-left: 25%;
        line-height: 50px;
    }
</style>
<header class="main-header">

    <!-- Logo -->
    <a href="${pageContext.request.contextPath}/dashboard" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini"><img src="${pageContext.request.contextPath}/resources/img/dwa-logo.png" alt="logo"></span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg"><img src="${pageContext.request.contextPath}/resources/img/dwa-logo-24x24.png" alt="logo"> <b><spring:message code="label.dwa" /></b> <spring:message code="label.mis" /></span>
        <!--<span class="logo-lg"><img src="mowca_logo2.jpg" alt="logo"></span>-->
    </a>

    <!-- Header Navbar -->
    <nav class="navbar navbar-static-top" role="navigation">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
        </a>

        <div id="dept-name">
            <spring:message code='header.deptName'/>
        </div>
        <c:set value="${sessionScope.userDetail.userSchemeDetailList}" var="userSchemeDetailList"/>
        <!-- Navbar Right Menu -->        
        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <li class="dropdown notifications-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-envelope-o"></i>
                        <span class="label label-warning">${fn:length(sessionScope.noticeList)}</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="header"><spring:message code="label.notice" />&nbsp;<spring:message code="label.list" /> </li>
                        <li>
                            <!-- inner menu: contains the actual data -->
                            <ul class="menu">
                                <c:forEach items="${sessionScope.noticeList}" var="notice">
                                    <li><a href="${pageContext.request.contextPath}/notice/view/${notice.id}">
                                            <i class="fa fa-envelope-o text-aqua"></i><c:choose>
                                                <c:when test="${pageContext.response.locale eq 'bn'}">${notice.noticeBn}</c:when>
                                                <c:otherwise>${notice.noticeEn}</c:otherwise>
                                            </c:choose> 
                                        </a></li>
                                    </c:forEach>                                        
                            </ul>
                        </li>                        
                    </ul>
                </li>
                <c:choose>
                    <c:when test="${fn:length(userSchemeDetailList) gt 0}">
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                <c:choose>
                                    <c:when test="${pageContext.response.locale eq 'bn'}">${sessionScope.userDetail.scheme.nameInBangla}</c:when>
                                    <c:otherwise>${sessionScope.userDetail.scheme.nameInEnglish}</c:otherwise>
                                </c:choose> 
                                <span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <c:forEach items="${userSchemeDetailList}" var="userSchemeDetail">
                                    <li><a href="${pageContext.request.contextPath}/changeScheme/${userSchemeDetail.userPerSchemeId}">
                                            <c:choose>
                                                <c:when test="${pageContext.response.locale eq 'bn'}">${userSchemeDetail.schemeNameBn}</c:when>
                                                <c:otherwise>${userSchemeDetail.schemeNameEn}</c:otherwise>
                                            </c:choose> 
                                        </a></li>
                                    </c:forEach>                        
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="dropdown messages-menu">
                            <a href="#">
                                <c:choose>
                                    <c:when test="${pageContext.response.locale eq 'bn'}">${sessionScope.userDetail.scheme.nameInBangla}</c:when>
                                    <c:otherwise>${sessionScope.userDetail.scheme.nameInEnglish}</c:otherwise>
                                </c:choose> 
                            </a>                            
                        </li>
                    </c:otherwise>
                </c:choose>                
                <li class="dropdown messages-menu">
                    <c:choose>
                        <c:when test="${pageContext.response.locale eq 'bn'}">
                            <a href="?lang=en"><span class="logo-mini">English</span></a>
                        </c:when>
                        <c:otherwise>
                            <a href="?lang=bn"><span class="logo-mini">বাংলা</span></a>
                        </c:otherwise>
                    </c:choose>
                </li>                
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">

<!--                        <img src="${pageContext.request.contextPath}/resources/user/anonymous-160x160.jpg" class="user-image" alt="User Image">-->
                        <span class="hidden-xs">
                            ${sessionScope.userID}
                        </span>&nbsp;<i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="user-header">                          
                            <p>
                                <c:choose>
                                    <c:when test="${pageContext.response.locale eq 'bn'}">${sessionScope.userDetail.userNameBn}</c:when>
                                    <c:otherwise>${sessionScope.userDetail.userNameEn}</c:otherwise>
                                </c:choose>                                
                                - ${sessionScope.designation}
                            </p>
                            <p>
                                <spring:message code="user.userOffice" /> : 
                                <c:choose>
                                    <c:when test="${pageContext.response.locale eq 'bn'}">
                                        <c:if test="${sessionScope.userDetail.userType.displayName eq 'Others'}">
                                            <c:if test="${sessionScope.userDetail.union ne null}">
                                                ${sessionScope.userDetail.union.nameInBangla}->
                                            </c:if>
                                            ${sessionScope.userDetail.upazila.nameInBangla}->${sessionScope.userDetail.district.nameInBangla}->${sessionScope.userDetail.division.nameInBangla}                                            
                                        </c:if>
                                        <c:if test="${sessionScope.userDetail.userType.displayName ne 'Others'}">
                                            ${sessionScope.userDetail.userType.displayNameBn}
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${sessionScope.userDetail.userType.displayName eq 'Others'}">
                                            <c:if test="${sessionScope.userDetail.union ne null}">
                                                ${sessionScope.userDetail.union.nameInEnglish}->
                                            </c:if>
                                            ${sessionScope.userDetail.upazila.nameInEnglish}->${sessionScope.userDetail.district.nameInEnglish}->${sessionScope.userDetail.division.nameInEnglish}
                                        </c:if>
                                        <c:if test="${sessionScope.userDetail.userType.displayName ne 'Others'}">
                                            ${sessionScope.userDetail.userType.displayName}
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>  
                            </p>
                        </li>
                        <li style="background-color: #D3D3D3" class="user-footer">
                            <div class="text-center">
                                <a href="${pageContext.request.contextPath}/user-profile" class="btn btn-info btn-flat" ><spring:message code="profile" /></a>
                            </div>                           
                        </li>
                        <li style="background-color: #D3D3D3" class="user-footer">
                            <div class="text-center">
                                <a href="${pageContext.request.contextPath}/user/training/own-training" class="btn btn-info btn-flat" ><spring:message code="label.myTraining" /></a>
                            </div>                           
                        </li>
                        <li style="background-color: #D3D3D3" class="user-footer">
                            <div class="text-center">
                                <a href="${pageContext.request.contextPath}/changePassword" class="btn btn-info btn-flat" ><spring:message code="label.changePassword" /></a>
                            </div>                           
                        </li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="${pageContext.request.contextPath}/logout" id="logout" data-placement="bottom" title="<spring:message code="label.signOut"/>"><i class="fa fa-power-off fa-lg" aria-hidden="true"></i></a>
                </li> 
            </ul>
        </div>
    </nav>
</header>
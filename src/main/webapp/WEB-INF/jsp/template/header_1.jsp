<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
        <!-- Navbar Right Menu -->        
        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
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
                        <c:choose>
                            <c:when test="${sessionScope.user.profilePicPath != null}">
                                <img src="${pageContext.request.contextPath}/resources/uploadedFile/user/${sessionScope.user.profilePicPath}" class="user-image" alt="">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/resources/img/anonymous-160x160.jpg" class="user-image" alt="">
                            </c:otherwise>
                        </c:choose>
<!--                        <img src="${pageContext.request.contextPath}/resources/user/anonymous-160x160.jpg" class="user-image" alt="User Image">-->
                        <span class="hidden-xs">
                            <c:choose>
                                <c:when test="${pageContext.response.locale eq 'bn'}">${sessionScope.userNameBn}</c:when>
                                <c:otherwise>${sessionScope.userNameEn}</c:otherwise>
                            </c:choose>                                
                        </span>&nbsp;<i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="user-header">
                            <c:choose>
                                <c:when test="${sessionScope.user.profilePicPath != null}">
                                    <img src="${pageContext.request.contextPath}/resources/uploadedFile/user/${sessionScope.user.profilePicPath}" class="img-circle" alt="">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/resources/img/anonymous-160x160.jpg" class="img-circle" alt="">
                                </c:otherwise>
                            </c:choose>
                            <p>
                                <c:choose>
                                    <c:when test="${pageContext.response.locale eq 'bn'}">${sessionScope.userNameBn}</c:when>
                                    <c:otherwise>${sessionScope.userNameEn}</c:otherwise>
                                </c:choose>                                
                                - ${sessionScope.designation}
                            </p>
                            <p>
                                <spring:message code="user.userOffice" /> : 
                                <c:choose>
                                    <c:when test="${pageContext.response.locale eq 'bn'}">
                                        <c:if test="${sessionScope.user.userType.displayName eq 'Others'}">
                                            <c:if test="${sessionScope.user.union ne null}">
                                                ${sessionScope.user.union.nameInBangla}->
                                            </c:if>
                                            ${sessionScope.user.upazilla.nameInBangla}->${sessionScope.user.district.nameInBangla}->${sessionScope.user.division.nameInBangla}                                            
                                        </c:if>
                                        <c:if test="${sessionScope.user.userType.displayName ne 'Others'}">
                                            ${sessionScope.user.userType.displayNameBn}
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${sessionScope.user.userType.displayName eq 'Others'}">
                                            <c:if test="${sessionScope.user.union ne null}">
                                                ${sessionScope.user.union.nameInEnglish}->
                                            </c:if>
                                            ${sessionScope.user.upazilla.nameInEnglish}->${sessionScope.user.district.nameInEnglish}->${sessionScope.user.division.nameInEnglish}
                                        </c:if>
                                        <c:if test="${sessionScope.user.userType.displayName ne 'Others'}">
                                            ${sessionScope.user.userType.displayName}
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>  
                            </p>
                        </li>
                        <li style="background-color: #D3D3D3" class="user-footer">
                            <div class="pull-left">
                                <a href="${pageContext.request.contextPath}/changePassword" class="btn btn-info btn-flat" ><spring:message code="label.changePassword" /></a>
                            </div>
                            <div class="pull-right">
                                <a href="${pageContext.request.contextPath}/logout" class="btn btn-primary btn-flat"><spring:message code="label.signOut" /></a>
                            </div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
</header>
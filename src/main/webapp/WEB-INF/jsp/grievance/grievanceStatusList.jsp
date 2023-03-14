<%-- 
    Document   : pageList
    Created on : Feb 5, 2017, 10:18:00 AM
    Author     : Philip
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script>
    $(function () {
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
        }
        $('#grievanceStatusListTable').DataTable({
            "language": {
                    "url": urlLang
                },

                    "fnDrawCallback": function (oSettings) {
                        if (selectedLocale === 'bn')
                        {
                            localizeBanglaInDatatable("grievanceStatusListTable");
                        }
                    }
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:if test="${not empty message.message}">                
    <div class="alert 
         <c:if test="${message.messageType eq 'SUCCESS'}">alert-success</c:if> 
         <c:if test="${message.messageType eq 'INFO'}">alert-info</c:if> 
             alert-dismissable">
             <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
         <c:out value="${message.message}"></c:out>
         </div>
</c:if>
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.grievanceStatus" />&nbsp;<spring:message code="label.list" /> 
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/grievanceStatus/create" class="btn bg-blue">
            <i class="fa fa-plus-square"></i>
            <spring:message code="addNew" />            
        </a>
    </div>   
</section>
<section class="content">    
    <div class="row">
        <div class="col-xs-12">
            <div class="box">              
                <div class="box-body">
                    <table id="grievanceStatusListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.nameBn" /></th>
                                <th><spring:message code="label.nameEn" /></th>
                                <th><spring:message code="label.displayOrder" /></th>
                                <th><spring:message code="label.active" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="grievanceStatus" items="${grievanceStatusList}">
                                <tr>
                                    <td><c:out value="${grievanceStatus.nameInBangla}"></c:out></td>
                                    <td><c:out value="${grievanceStatus.nameInEnglish}"></c:out></td>
                                    <td><c:out value="${grievanceStatus.displayOrder}"></c:out></td>
                                    <td><c:choose>
                                            <c:when test="${grievanceStatus.active}">
                                                <i class="fa fa-check true-icon"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fa fa-times" aria-hidden="true"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>                                    
                                    <td><a href="${contextPath}/grievanceStatus/edit/${grievanceStatus.id}" data-toggle="tooltip" title="${tooltipEdit}">
                                            <span class="glyphicon glyphicon-edit"></span>
                                        </a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

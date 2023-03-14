<%-- 
    Document   : pageList
    Created on : Feb 5, 2017, 10:18:00 AM
    Author     : Philip
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">
<!--<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css" />">-->
<script>
    $(function () {
        var path = '${pageContext.request.contextPath}';
        var urlLang="";
        if("${pageContext.response.locale}" === 'bn'){
            urlLang=path + "/dataTable/localization/bangla";
        }
        $('#grievanceListTable').DataTable({
            "pageLength": 10,
            "lengthMenu": [10,25,50,100],
            
            "pagingType": "full_numbers",
            "dom": '<"top"i>rt<"bottom"lp><"clear">',
            "language": {
                        "url": urlLang
                 },

                    "fnDrawCallback": function (oSettings) {
                        if (selectedLocale === 'bn')
                        {
                            localizeBanglaInDatatable("grievanceListTable");
                        }
                    }
                 
        });
    });
</script>
<style>
    #grievanceListTable_length
    {
        float: left;
    }
</style>
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
        <spring:message code="grievanceList.grievanceList" />
        <small></small>
    </h1>
<!--    <div class="pull-right">
        <a href="${contextPath}/newgrievance/create" class="btn bg-blue">
            <i class="fa fa-plus-square"></i>
            <spring:message code="addNew" />            
        </a>
    </div>   -->
</section>
<section class="content">    
    <div class="row">
        <div class="col-xs-12">
            <div class="box">               
                <div class="box-body">
                    <table id="grievanceListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.nid" /></th>
                                <th><spring:message code="label.grievanceType" /></th>                                                                
                                <th><spring:message code="grievance.description" /></th>
                                <th><spring:message code="label.comment" /></th>
                                <th><spring:message code="grievance.grievanceStatus" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="grievance" items="${grievanceList}">
                                <tr>
                                    <td><c:out value="${grievance.beneficiary.nid}"></c:out></td>
                                        <td>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                <c:out value="${grievance.grievanceType.nameInBangla}"></c:out>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${grievance.grievanceType.nameInEnglish}"></c:out>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${grievance.description}"></c:out></td>
                                    <td><c:out value="${grievance.comment}"></c:out></td>
                                        <td>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                <c:out value="${grievance.grievanceStatus.nameInBangla}"></c:out>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${grievance.grievanceStatus.nameInEnglish}"></c:out>
                                            </c:otherwise>
                                        </c:choose>
                                    </td> 
                                    <td><a href="${contextPath}/grievance/edit/${grievance.id}" data-toggle="tooltip" title="${tooltipEdit}">
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

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
<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css" />">
<script>
    $(function () {
        $('#pageListTable').DataTable();
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:url var="formAction" value="/page"></c:url>
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
        <spring:message code="pageList.pageList" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/page/create" class="btn bg-blue">
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
                    <table id="pageListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="page.parentPage" /></th>
                                <th><spring:message code="label.name" /></th>                                
                                <th><spring:message code="page.url" /></th>
                                <th><spring:message code="page.pageOrder" /></th>
                                <th><spring:message code="edit" var="tooltipEdit" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="page" items="${allPageList}">
                                <tr>
                                    <td><c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                <c:out value="${page.parentPage.nameInBangla}"></c:out>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${page.parentPage.nameInEnglish}"></c:out>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                <c:out value="${page.nameInBangla}"></c:out>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${page.nameInEnglish}"></c:out>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${page.url}"></c:out></td>
                                    <td><c:out value="${page.pageOrder}"></c:out></td>
                                    <td><a href="${contextPath}/page/edit/${page.id}" data-toggle="tooltip" title="${tooltipEdit}"><span class="glyphicon glyphicon-edit"></span></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script>
    $(function () {
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#pagemodel-delete-confirmation");
        $(menuSelect("${pageContext.request.contextPath}/page/list"));
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/page/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/page/edit/${page.id}" />
    </c:otherwise>
</c:choose>
<form:form action="${actionUrl}" class="form-horizontal" modelAttribute="page">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.page" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/page/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>
            <c:if test="${actionType ne 'create'}">
                <span id="page-delete" class="btn bg-red">
                    <i class="fa fa-trash-o"></i>
                    <spring:message code="delete" />
                </span>
            </c:if>
        </div>    
    </section>
    <section class="content">        
        <div class="row">
            <div class="col-md-6"> 

                <form:hidden path="id" />                 
                <div class="form-group">
                    <label for="parentPageInput" class="col-md-4 control-label"><spring:message code="page.parentPage" /></label>
                    <div class="col-md-8">
                        <form:select class="form-control" path="parentPage">
                            <form:option value="" label="Select Type"></form:option>
                            <form:options items="${parentPageList}" itemValue="id" itemLabel="${parentPageName}"></form:options>
                        </form:select>
                        <form:errors path="parentPage" cssClass="error"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="nameInBanglaInput" class="col-md-4 control-label"><spring:message code="label.nameBn" /></label>
                        <div class="col-md-8">
                        <form:input path="nameInBangla" class="form-control" id="nameInBanglaInput" />
                        <form:errors path="nameInBangla" cssClass="error"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="nameInEnglishInput" class="col-md-4 control-label"><spring:message code="label.nameEn" /></label>
                        <div class="col-md-8">
                        <form:input path="nameInEnglish" class="form-control" id="nameInEnglishInput" />
                        <form:errors path="nameInEnglish" cssClass="error"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="urlInput" class="col-md-4 control-label"><spring:message code="page.url" /></label>
                        <div class="col-md-8">
                        <form:input path="url" class="form-control" id="urlInput" />
                        <form:errors path="url" cssClass="error"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="pageOrderInput" class="col-md-4 control-label"><spring:message code="page.pageOrder" /></label>
                        <div class="col-md-8">
                        <form:input path="pageOrder" class="form-control" id="pageOrderInput" />
                        <form:errors path="pageOrder" cssClass="error"></form:errors>
                        </div>
                    </div>             
            </form:form>
        </div>
    </div>
    <div id='pagemodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/page/delete/${page.id}" method="post">
                    <div class="form-horizontal">
                        <div class="modal-body">
                            <spring:message code="deleteMessage" />                            
                        </div>
                        <div class="modal-footer">
                            <span class="btn btn-default" data-dismiss="modal"><spring:message code="cancelMessage" /></span>
                            <button type="submit" class="btn btn-primary pull-right">
                                <spring:message code="delete" />
                            </button>
                        </div>
                    </div>
                </form>    
            </div>
        </div>
    </div>
</section>

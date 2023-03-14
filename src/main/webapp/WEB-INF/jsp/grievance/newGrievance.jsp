<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script>
    $(function () {
        if (selectedLocale === 'bn')
        {
            makeUnijoyEditor('description');
        } 
        $(menuSelect("${pageContext.request.contextPath}/grievance/beneficiaryList"));
        $('#grievance-delete').attr("data-toggle", "modal").attr("data-target", "#grievancemodel-delete-confirmation");
        $("#newGrievanceForm").validate({
            rules: {
                "grievanceType.id": {
                    required: true
                },
                "description": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });       
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/newGrievance/create/${beneficiary.id}" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/grievance/edit/${grievance.id}" />
    </c:otherwise>
</c:choose>
<form:form id="newGrievanceForm" action="${actionUrl}" class="form-horizontal" modelAttribute="grievance">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.newGrievance" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}${prevUrl}"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>
            <c:if test="${actionType ne 'create'}">
                <span id="grievance-delete" class="btn bg-red">
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
                <form:hidden path="creationDate" value="${creationDate}"/>
                <div class="form-group">    
                    <div class="form-group">
                        <label for="beneficiary" class="col-md-4 control-label"><spring:message code="label.nid" /><span class="mandatory">*</span></label>                        
                        <div class="col-md-8">                            
                            <form:input path="beneficiary" class="form-control" value="${nid}" readonly="true"/>
                            <form:errors path="beneficiary" cssClass="error"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group">
                            <label for="grievanceType.id" class="col-md-4 control-label"><spring:message code="label.grievanceType" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <form:select class="form-control" path="grievanceType.id" autofocus="autofocus">
                                <spring:message code='label.select' var="select"/>
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${grievanceTypeList}" itemValue="id" itemLabel="${grievanceTypeName}"></form:options>
                            </form:select>
                            <form:errors path="grievanceType.id" cssClass="error"></form:errors>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="description" class="col-md-4 control-label"><spring:message code="grievance.description" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code="grievance.description.Placeholder" var="descPlaceholder"/>
                            <form:textarea path="description" class="form-control" rows="3" placeholder="${descPlaceholder}" />
                            <form:errors path="description" cssClass="error"></form:errors>
                            </div>
                        </div>
                    <form:hidden path="grievanceStatus.id" value="${grievanceStatus.id}"/>
                </form:form>
            </div>
        </div>
        <div id='grievancemodel-delete-confirmation' class="modal fade"  tabindex="-1" grievance="dialog" aria-labelledby="grievancemodel-delete-confirmation-title">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="grievancemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                    </div>
                    <form action="${contextPath}/grievance/delete/${grievance.id}" method="post">
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

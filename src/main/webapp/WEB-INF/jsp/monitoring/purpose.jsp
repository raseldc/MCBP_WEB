<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/purpose/list"));
        $('#purpose-delete').attr("data-toggle", "modal").attr("data-target", "#purposemodel-delete-confirmation");
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        makeUnijoyEditor('nameInBanglaInput');
        $("#nameInEnglishInput").focus();
        $("#nameInBanglaInput").focus();
        if (selectedLocale === "bn")
        {
            makeUnijoyEditor('descriptionInput');
        }
        $("#purposeForm").validate({
            rules: {// checks NAME not ID
                "nameInBangla": {
                    required: true,
                    maxlength: 255,
                    banglaAlphabetWithNumber: true
                },
                "nameInEnglish": {
                    required: true,
                    maxlength: 255,
                    englishAlphabetWithNumber: true
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
        <c:set var="actionUrl" value="${contextPath}/purpose/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/purpose/edit/${purpose.id}" />
    </c:otherwise>
</c:choose>
<form:form id="purposeForm" action="${actionUrl}" class="form-horizontal" modelAttribute="purpose">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="purpose.add/EditPurpose" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/purpose/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>
            <c:if test="${actionType ne 'create'}">
                <span id="purpose-delete" class="btn bg-red">
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
                        <label for="nameInBanglaInput" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <form:input path="nameInBangla" class="form-control" id="nameInBanglaInput" />
                            <form:errors path="nameInBangla" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="nameInEnglishInput" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <form:input path="nameInEnglish" class="form-control" id="nameInEnglishInput" />
                            <form:errors path="nameInEnglish" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="descriptionInput" class="col-md-4 control-label"><spring:message code="label.description" /></label>
                        <div class="col-md-8">
                            <form:input path="description" class="form-control" id="descriptionInput" />
                            <form:errors path="description" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="active" class="col-md-4 control-label"><spring:message code="label.active" /></label>
                        <div class="col-md-8">
                            <div class="checkbox icheck">
                                <label>
                                    <form:checkbox path="active" id="active" />
                                </label>
                            </div>                        
                        </div>
                    </form:form>
                </div>
            </div>
            <div id='purposemodel-delete-confirmation' class="modal fade"  tabindex="-1" purpose="dialog" aria-labelledby="purposemodel-delete-confirmation-title">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="purposemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                        </div>
                        <form action="${contextPath}/purpose/delete/${purpose.id}" method="post">
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

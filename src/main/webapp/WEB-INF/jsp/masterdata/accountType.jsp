<%-- 
    Document   : accountType
    Created on : Jan 26, 2017, 4:44:04 PM
    Author     : user
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script src="<c:url value="/resources/js/unijoy.js" />" ></script>
<!-- DataTables -->
<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/accountType/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#accountTypemodel-delete-confirmation");

        makeUnijoyEditor('nameBn');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('accountTypeCodeInput');
            makeUnijoyEditor('accountTypeAddressInput');
            $('#accountTypeCodeInput').on("keydown", function (event) {
                checkNumberWithLength(event, this, 4);
            });
            $("#accountTypeCodeInput").val(getNumberInBangla("${accountType.code}"));
        }
        $("#nameEn").focus();
        $("#nameBn").focus();
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $("#accountTypeForm").validate({
            rules: {
                "nameInEnglish": {
                    required: true,
                },
                "nameInBangla": {
                    required: true
                },
                "code": {
                    required: true
                },
                "description": {
                    required: true
                },
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
        <c:set var="actionUrl" value="${contextPath}/accountType/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/accountType/edit/${accountType.id}" />
    </c:otherwise>
</c:choose>


<form:form id="accountTypeForm" action="${formAction}" method="post" class="form-horizontal" role="form" modelAttribute="accountType">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.accountType" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/accountType/list"><spring:message code="label.backToList" /></a></small>
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
                <div>
                    <div class="form-group">
                        <label for="nameBn" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="nameBn"/>
                            <form:input class="form-control" placeholder="${nameBn}" path="nameInBangla" id="nameBn" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="nameEn" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="nameEn"/>
                            <form:input class="form-control" placeholder="${nameEn}" path="nameInEnglish" id="nameEn" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="accountTypeCodeInput"/>
                            <form:input class="form-control" placeholder="${accountTypeCodeInput}" path="code" id="accountTypeCodeInput"  onkeydown="checkNumberWithLength(event, this, 4)"/>
                            <form:errors path="code" cssClass="error"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group">
                            <label for="accountTypeAddressInput" class="col-md-4 control-label"><spring:message code="label.description" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.description' var="accountTypeAddressInput"/>
                            <form:textarea class="form-control" placeholder="${accountTypeAddressInput}" path="description" id="accountTypeAddressInput" autofocus="autofocus"/>
                            <form:errors path="description" cssClass="error"></form:errors>
                            </div>
                        </div>
                    </div>

            </form:form>
        </div>
    </div>
    <div id='accountTypemodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/accountType/delete/${accountType.id}" method="post">
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
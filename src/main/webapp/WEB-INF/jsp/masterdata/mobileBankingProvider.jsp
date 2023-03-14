<%-- 
    Document   : MobileBankingProviderSetup
    Created on : Jan 17, 2017, 1:24:20 PM
    Author     : rezwan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="<c:url value="/resources/js/unijoy.js" />" ></script>
<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/mobileBankingProvider/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#mobileBankingProvidermodel-delete-confirmation");

        makeUnijoyEditor('nameInBangla');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('routingNumberInput');
            makeUnijoyEditor('code');
            $("#routingNumberInput").val(getNumberInBangla("${mobileBankingProvider.routingNumber}"));
        }
        // for some reason, fullNameInBangla is not working in first time load, so, I just change the focus and again focused in first field.
        $("#nameInEnglish").focus();
        $("#nameInBangla").focus();

        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $("#mobileBankingProviderForm").validate({
            rules: {
                "nameInEnglish": {
                    required: true
                },
                "nameInBangla": {
                    required: true,
                    banglaAlphabet: true
                },
                "code": {
                    required: true
                },
                "routingNumber":{
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
        <c:set var="actionUrl" value="${contextPath}/mobileBankingProvider/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/mobileBankingProvider/edit/${mobileBankingProvider.id}" />
    </c:otherwise>
</c:choose>

<form:form id="mobileBankingProviderForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="mobileBankingProvider">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="mobileBankingProvider.label.mobileBankingProvider" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/mobileBankingProvider/list"><spring:message code="label.backToList" /></a></small>
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
                        <label for="mobileBankingProviderNameInBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="nameInBangla"/>
                            <form:input class="form-control" placeholder="${nameInBangla}" path="nameInBangla" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mobileBankingProviderNameInEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="nameInEnglish"/>
                            <form:input class="form-control" placeholder="${nameInEnglish}" path="nameInEnglish" autofocus="autofocus" onkeydown="checkEnglishAlphabetWithLength(event, this, 30)"/>
                            <form:errors path="nameInEnglish" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="mobileBankingProvider.label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='mobileBankingProvider.label.code' var="code"/>
                            <form:input class="form-control" placeholder="${code}" path="code" />
                            <form:errors path="code" cssclass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="branchroutingNumberInput" class="col-md-4 control-label"><spring:message code="label.routingNumber" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.routingNumber' var="branchroutingNumberInput"/>
                            <form:input class="form-control" placeholder="${routingNumberInput}" path="routingNumber" id="routingNumberInput" onkeydown="checkNumber(event, this)"/>
                            <form:errors path="routingNumber" cssclass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="active" class="col-md-4 control-label"><spring:message code="label.active" /></label>
                        <div class="col-md-3">
                            <form:checkbox class="icheckbox_square-blue" path="active" id="active" />
                        </div>  
                    </div>
                </div>
            </form:form>
        </div>
    </div>

    <div id='mobileBankingProvidermodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/mobileBankingProvider/delete/${mobileBankingProvider.id}" method="post">
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




<!-- /.content -->


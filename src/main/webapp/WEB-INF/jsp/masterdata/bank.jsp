<%-- 
    Document   : bank
    Created on : Jan 26, 2017, 4:44:04 PM
    Author     : user
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/bank/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#bankmodel-delete-confirmation");

        makeUnijoyEditor('bankNameInputBangla');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('bankCodeInput');
            makeUnijoyEditor('bankAddressInput');
            makeUnijoyEditor('accountNoLength');
            $('#bankCodeInput').on("keydown", function (event) {
                checkNumberWithLength(event, this, 4);
            });
            $("#bankCodeInput").val(getNumberInBangla("${bank.code}"));
            $("#accountNoLength").val(getNumberInBangla("${bank.accountNoLength}"));
        }
        $('#accountNoLength').on("keydown", function (event) {
            checkNumberWithLength(event, this, 2);
        });
        $("#bankNameInputEnglish").focus();
        $("#bankNameInputBangla").focus();
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $("#bankForm").validate({
            rules: {// checks NAME not ID
                "nameInEnglish": {
                    required: true
                },
                "nameInBangla": {
                    required: true
                },
                "code": {
                    required: true
                },
                "address": {
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
        <c:set var="actionUrl" value="${contextPath}/bank/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/bank/edit/${division.id}" />
    </c:otherwise>
</c:choose>


<form:form id="bankForm" action="${formAction}" method="post" class="form-horizontal" role="form" modelAttribute="bank">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.bank" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/bank/list"><spring:message code="label.backToList" /></a></small>
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
                        <label for="bankNameInputBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="bankNameInputBangla"/>
                            <form:input class="form-control" placeholder="${bankNameInputBangla}" path="nameInBangla" id="bankNameInputBangla" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="bankNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="bankNameInputEnglish"/>
                            <form:input class="form-control" placeholder="${bankNameInputEnglish}" path="nameInEnglish" id="bankNameInputEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="bankCodeInput"/>
                            <form:input class="form-control" placeholder="${bankCodeInput}" path="code" id="bankCodeInput" onkeydown="checkNumberWithLength(event, this, 4)"/>
                            <form:errors path="code" cssclass="error"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group">
                            <label for="bankAddressInput" class="col-md-4 control-label"><spring:message code="label.address" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.address' var="bankAddressInput"/>
                            <form:textarea class="form-control" placeholder="${bankAddressInput}" path="address" id="bankAddressInput" autofocus="autofocus"/>
                            <form:errors path="address" cssStyle="color:red"></form:errors>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="branchAddressInput" class="col-md-4 control-label"><spring:message code="label.accountNoLength" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.accountNoLength' var="accountNoLengthInput"/>
                            <form:input class="form-control" placeholder="${accountNoLengthInput}" path="accountNoLength" id="accountNoLength" autofocus="autofocus"/>
                            <form:errors path="accountNoLength" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                    </div>  
            </form:form>
        </div>
    </div>

    <div id='bankmodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/bank/delete/${bank.id}" method="post">
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


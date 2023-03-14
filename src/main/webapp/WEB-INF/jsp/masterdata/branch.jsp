<%-- 
    Document   : branch
    Created on : Jan 22, 2017, 3:40:06 PM
    Author     : user
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/branch/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#branchmodel-delete-confirmation");

        makeUnijoyEditor('branchNameInputBangla');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('branchCodeInput');
            makeUnijoyEditor('accountNoLength');
            makeUnijoyEditor('branchAddressInput');
            makeUnijoyEditor('routingNumberInput');
            $('#branchCodeInput').on("keydown", function (event) {
                checkNumberWithLength(event, this, 4);
            });

            $("#branchCodeInput").val(getNumberInBangla("${branch.code}"));
            $("#routingNumberInput").val(getNumberInBangla("${branch.routingNumber}"));
            $("#accountNoLength").val(getNumberInBangla("${branch.accountNoLength}"));
        }
        $('#accountNoLength').on("keydown", function (event) {
            checkNumberWithLength(event, this, 2);
        });
        $("#branchNameInputEnglish").focus();
        $("#branchNameInputBangla").focus();
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $("#branchForm").validate({
            rules: {// checks NAME not ID
                "bank.id": {
                    required: true
                },
                "nameInEnglish": {
                    required: true
                },
                "nameInBangla": {
                    required: true
                },
                "code": {
                    required: true
                },
                "routingNumber": {
                    required: true
                },
                "address": {
                    required: true
                },
                "accountNoLength": {
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
        <c:set var="actionUrl" value="${contextPath}/branch/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/branch/edit/${branch.id}" />
    </c:otherwise>
</c:choose>

<form:form id="branchForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="branch">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.branch" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/branch/list"><spring:message code="label.backToList" /></a></small>
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
                        <label for="bankInput" class="col-md-4 control-label"><spring:message code="label.bank" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="bank.id">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${bankList}" itemValue="id" itemLabel="${bankName}"></form:options>
                            </form:select>
                            <form:errors path="bank.id" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="branchNameInputBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="branchNameInputBangla"/>
                            <form:input class="form-control" placeholder="${branchNameInputBangla}" path="nameInBangla" id="branchNameInputBangla" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group">
                            <label for="branchNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="branchNameInputEnglish"/>
                            <form:input class="form-control" placeholder="${branchNameInputEnglish}" path="nameInEnglish" id="branchNameInputEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="branchCodeInput"/>
                            <form:input class="form-control" placeholder="${branchCodeInput}" path="code" id="branchCodeInput" onkeydown="checkNumberWithLength(event, this, 4)"/>
                            <form:errors path="code" cssclass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="districtInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="district.id">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${districtList}" itemValue="id" itemLabel="${districtName}"></form:options>
                            </form:select>
                            <form:errors path="district.id" cssClass="error"></form:errors>
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
                            <label for="branchAddressInput" class="col-md-4 control-label"><spring:message code="label.address" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.address' var="branchAddressInput"/>
                            <form:textarea class="form-control" placeholder="${branchAddressInput}" path="address" id="branchAddressInput" autofocus="autofocus"/>
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
                    <div class="form-group">
                        <label for="active" class="col-md-4 control-label"><spring:message code="label.active" /></label>
                    <div>
                        <div class="col-md-5">
                            <div class="checkbox icheck">
                                <label>
                                    <form:checkbox path="active" id="active" />
                                </label>
                            </div>                        
                        </div>
                    </div>   
                </div>    
            </div>  
        </form:form>
    </div>
</div>
<div id='branchmodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
            </div>
            <form action="${contextPath}/branch/delete/${branch.id}" method="post">
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



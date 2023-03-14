<%-- 
    Document   : PaymentCycleSetup
    Created on : Jan 17, 2017, 1:24:20 PM
    Author     : rezwan
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(function () {
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#paymentcyclemodel-delete-confirmation");
        $(menuSelect("${pageContext.request.contextPath}/paymentGeneration/pendingList"));
        $("#paymentGenerationForm").validate({
            rules: {// checks NAME not ID
                "scheme.id": {
                    required: true
                },
                "fiscalYear.id": {
                    required: true
                },
                "paymentCycle.id": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
        var locale = "${pageContext.response.locale}";
        var contextPath = "${pageContext.request.contextPath}";
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + locale + ".js");


    });

    function loadActivePaymentCycle(selectObject) {
        var fiscalYearId = selectObject.value;
        var paymentCycleSelectId = $('#paymentCycle');
        if (fiscalYearId !== '') {
            loadPaymentCycle(fiscalYearId, paymentCycleSelectId);
        } else {
            resetSelect(paymentCycleSelectId);
        }
    }

    function submitForm() {
        $('#paymentGenerationForm').validate();
        if ($("#paymentGenerationForm").valid()) {
            $('#paymentGenerationForm').submit();
        }
    }

    function askApproveSubmit(submitButton)
    {
        var approvalMessage;
        if ($(submitButton).val() == "accept")
        {
            if (selectedLocale === 'bn') {
                approvalMessage = "আপনি কি পে-রোল অনুমোদন নিশ্চিত করতে চান?";
            } else
            {
                approvalMessage = "Are you sure you want to Approve the Payroll?";
            }
        } else
        {
            if (selectedLocale === 'bn') {
                approvalMessage = "আপনি কি পে-রোল বাতিল নিশ্চিত করতে চান?";
            } else
            {
                approvalMessage = "Are you sure you want to Reject the Payroll?";
            }
        }
        bootbox.confirm({
            message: approvalMessage,
            buttons: {
                confirm: {
                    label: '<spring:message code="button.yes" />',
                    className: 'btn-success'
                },
                cancel: {
                    label: '<spring:message code="button.no" />',
                    className: 'btn-danger'
                }
            },
            callback: function (result)
            {
                if (result == true)
                {
                    $("#buttonApprove_submit").click();
                } else
                {
                    return true;
                }
            }
        });
    }
    
    function askRejectSubmit()
    {
        bootbox.confirm({
            message: "Are you sure you want to Reject the Payroll?",
            buttons: {
                confirm: {
                    label: 'Yes',
                    className: 'btn-success'
                },
                cancel: {
                    label: 'No',
                    className: 'btn-danger'
                }
            },
            callback: function (result)
            {
                if (result == true)
                {
                    var rejectedApplicantList = "";
                    $('input.checkbox').each(function () {
                        if (this.checked) {
                            rejectedApplicantList += this.id + ',';
                        }
                    });
                    $("#rejectedApplicantList").val(rejectedApplicantList);
                    $("#buttonReject_submit").click();
                } else
                {
                    return true;
                }
            }
        });
    }

</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/paymentGeneration/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/paymentGeneration/pendingList/${paymentInfo.scheme.id}/${paymentInfo.paymentCycle.id}" />
    </c:otherwise>
</c:choose>

<form:form id="paymentInfoForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="paymentInfo">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.paymentApprove" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/paymentGeneration/pendingList"><spring:message code="label.backToList" /></a></small>
        </h1>
    </section>
    <section class="content">
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.scheme" /></label>
                    <div class="col-md-8">
                        <div style="margin-top: 7px"> ${paymentInfo.scheme}</div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /></label>
                    <div class="col-md-8">
                        <div style="margin-top: 7px"> ${paymentInfo.paymentCycle.fiscalYear.nameInEnglish}</div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.paymentCycle" /></label>
                    <div class="col-md-8">
                        <div style="margin-top: 7px"> ${paymentInfo.paymentCycle.nameInEnglish}</div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="payment.label.totalBeneficiary" /></label>
                    <div class="col-md-8">
                        <div style="margin-top: 7px"> ${paymentInfo.totalBeneficiary}</div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="payment.label.totalAllowanceAmount" /></label>
                    <div class="col-md-8">
                        <div style="margin-top: 7px"> ${paymentInfo.totalAllowanceAmount}</div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="payment.label.comments" /></label>
                    <div class="col-md-8">
                        <form:textarea path="approvalComments" rows="5" cols="70" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-4"></div>
                    <div class="col-md-4">
                        <button class="btn bg-blue" type="button" id="buttonAccept" name="action" value="accept" onclick="return askApproveSubmit(this);"><spring:message code='button.approve'/></button>
                        <button id="buttonApprove_submit" type="submit" style="display: none" name="action" class="btn bg-blue" value="accept" >
                            <spring:message code="accept" />
                        </button>
                        <button id="buttonReject" type="button" class="btn bg-red" value="sdfsdf"  onclick="return askApproveSubmit(this);"><spring:message code='button.reject'/></button>
                        <button id="buttonReject_submit" type="submit" style="display: none" name="action" class="btn bg-red" value="reject" >

                            <spring:message code="reject" />
                        </button>
                    </div>
                    <!--                        <div class="col-md-2">
                    <spring:message code='button.approve' var="approve"/>
                    <input type="submit" class="btn bg-blue" name="action" onclick="submitForm();" value="${approve}" title="${approve}">
                        
                    </input>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn bg-red" name="action" onclick="submitForm();" value="false">
                    <spring:message code='reject' var="reject"/>${reject}
                </button>                        
            </div>-->


                </div>
            </div>
        </form:form>

        <div id='paymentcyclemodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="paymentcyclemodel-delete-confirmation-title">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                    </div>
                    <form action="${contextPath}/paymentCycle/delete/${paymentCycle.id}" method="post">
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


<%-- 
    Document   : PaymentCycleSetup
    Created on : Jan 17, 2017, 1:24:20 PM
    Author     : rezwan
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<style>
    @media only screen and (min-width: 768px) {
        .table-responsive {
            overflow-x: hidden;
        }
    }
</style>
<script>
    $(function () {
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#paymentcyclemodel-delete-confirmation");
        $(menuSelect("${pageContext.request.contextPath}/payrollApprove/list"));

        var contextPath = "${pageContext.request.contextPath}";
        var urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }
        console.log(urlLang);
        $('#paymentListTable').DataTable().destroy();
        $('#paymentListTable').DataTable({
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("paymentListTable");
                }
            }
        });

//        $("#paymentGenerationForm").validate({
//            rules: {// checks NAME not ID
//                "scheme.id": {
//                    required: true
//                },
//                "fiscalYear.id": {
//                    required: true
//                },
//                "paymentCycle.id": {
//                    required: true
//                }
//            },
//            errorPlacement: function (error, element) {
//                error.insertAfter(element);
//            }
//        });
        var locale = "${pageContext.response.locale}";
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

//    function submitForm() {
//        $('#paymentGenerationForm').validate();
//        if ($("#paymentGenerationForm").valid()) {
//            $('#paymentGenerationForm').submit();
//        }
//    }

    function askApproveSubmit(submitButton)
    {
        var approvalMessage;
        if ($(submitButton).val() == "accept")
        {
            approvalMessage = getLocalizedText('approveMessage', selectedLocale);
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
                console.log(result);
                if (result == true)
                {
                    $("#buttonAccept_submit").click();
                } else
                {
                    return true;
                }
            }
        });
    }

    function askRejectSubmit()
    {
        var recheckMessage = getLocalizedText('recheckMessage', selectedLocale);

        bootbox.confirm({
            message: recheckMessage,
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
<c:set var="selectedLocale" value="${pageContext.response.locale}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/paymentGeneration/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/paymentGeneration/pendingList/${payrollSummary.id}" />
    </c:otherwise>
</c:choose>

<form:form id="paymentInfoForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="payrollSummary">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.paymentApprove" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/payrollApprove/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button class="btn bg-blue" type="button" id="buttonAccept" name="action" value="accept" onclick="return askApproveSubmit(this);"><spring:message code='button.approve'/></button>
            <button id="buttonAccept_submit" type="submit" style="display: none" name="action" class="btn bg-blue" value="accept" >
                <spring:message code="accept" />
            </button>
            <button id="buttonReject" type="button" class="btn bg-red" value="sdfsdf"  onclick="return askRejectSubmit(this);"><spring:message code='button.recheck'/></button>
            <button id="buttonReject_submit" type="submit" style="display: none" name="action" class="btn bg-red" value="recheck" >
                <spring:message code="reject" />
            </button>
        </div>

    </section>
    <section class="content">
        <div class="row">
            <div class="col-xs-12">
                <div class="box table-responsive">
                    <div class="box-body">
                        <form:hidden path="id"/>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.scheme" /></label>
                                    <div class="col-md-8">
                                        <div style="margin-top: 7px"> 
                                            <c:choose>
                                                <c:when test="${selectedLocale eq 'en'}">
                                                    <c:out value="${payrollSummary.scheme.nameInEnglish}"></c:out>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${payrollSummary.scheme.nameInBangla}"></c:out>
                                                </c:otherwise>
                                            </c:choose>                                    
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /></label>
                                    <div class="col-md-8">
                                        <div style="margin-top: 7px"> 
                                            <c:choose>
                                                <c:when test="${selectedLocale eq 'en'}">
                                                    <c:out value="${payrollSummary.paymentCycle.fiscalYear.nameInEnglish}"></c:out>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${payrollSummary.paymentCycle.fiscalYear.nameInBangla}"></c:out>
                                                </c:otherwise>
                                            </c:choose>        
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.paymentCycle" /></label>
                                    <div class="col-md-8">
                                        <div style="margin-top: 7px"> 
                                            <c:choose>
                                                <c:when test="${selectedLocale eq 'en'}">
                                                    <c:out value="${payrollSummary.paymentCycle.nameInEnglish}"></c:out>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${payrollSummary.paymentCycle.nameInBangla}"></c:out>
                                                </c:otherwise>
                                            </c:choose>        
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="payment.label.totalBeneficiary" /></label>
                                    <div class="col-md-8">
                                        <div style="margin-top: 7px"> 
                                            <c:if test="${selectedLocale eq 'en'}">
                                                <c:out value="${payrollSummary.totalBeneficiary}"></c:out>
                                            </c:if>
                                            <c:if test="${selectedLocale eq 'bn'}">
                                                <script>
                                                    var bnText = getNumberInBangla('' + ${payrollSummary.totalBeneficiary} + '');
                                                    document.writeln(bnText);
                                                </script>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="payment.label.totalAllowanceAmount" /></label>
                                    <div class="col-md-8">
                                        <div style="margin-top: 7px"> 
                                            <c:if test="${selectedLocale eq 'en'}">
                                                <c:out value="${payrollSummary.totalAllowance}"></c:out>0
                                            </c:if>
                                            <c:if test="${selectedLocale eq 'bn'}">
                                                <script>
                                                    var bnText = getNumberInBangla('' + ${payrollSummary.totalAllowance} + '.00');
                                                    document.writeln(bnText);
                                                </script>
                                            </c:if>
                                            <spring:message code="taka" />
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="payment.label.comments" /></label>
                                    <div class="col-md-8">
                                        <form:textarea class="form-control" path="approvalComments" rows="5" cols="70" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-md-4"></div>
                                    <div class="col-md-4">
                                    </div>
                                </div>
                            </div>
                        </div>

<!--                        <table id="paymentListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th><spring:message code="payment.label.beneficiary" /></th>
                                    <th><spring:message code="payment.label.beneficiaryNid" /></th>
                                    <th><spring:message code="payment.label.allowanceAmount" /></th>
                                    <th><spring:message code="payment.label.mobileNumber" /></th>
                                    <th><spring:message code="payment.label.bankName" /></th>
                                    <th><spring:message code="payment.label.branchName" /></th>
                                    <th><spring:message code="payment.label.accountNumber" /></th>                                
                                </tr>
                            </thead> 
                            <tbody>
                                <c:forEach var="payment" items="${paymentList}">
                                    <tr>
                                        <td>
                                            <c:choose>
                                                <c:when test="${selectedLocale eq 'en'}">
                                                    <c:out value="${payment.beneficiary.fullNameInEnglish}"></c:out>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${payment.beneficiary.fullNameInBangla}"></c:out>
                                                </c:otherwise>
                                            </c:choose>                                    
                                        </td>
                                        <td>
                                            <c:if test="${selectedLocale eq 'en'}">
                                                <c:out value="${payment.beneficiary.nid}"></c:out>
                                            </c:if>
                                            <c:if test="${selectedLocale eq 'bn'}">
                                                <script>
                                                    var bnText = getNumberInBangla('' + ${payment.beneficiary.nid} + '');
                                                    document.writeln(bnText);
                                                </script>
                                            </c:if>
                                        </td>                                        
                                        <td>                                            
                                            <c:if test="${selectedLocale eq 'en'}">
                                                <c:out value="${payment.allowanceAmount}"></c:out>
                                            </c:if>
                                            <c:if test="${selectedLocale eq 'bn'}">
                                                <script>
                                                    var bnText = getNumberInBangla('' + ${payment.allowanceAmount} + '');
                                                    document.writeln(bnText);
                                                </script>
                                            </c:if>
                                        </td>
                                        <td>                                            
                                            <c:if test="${selectedLocale eq 'en'}">
                                                <c:out value="${payment.mobileNumber}"></c:out>
                                            </c:if>
                                            <c:if test="${selectedLocale eq 'bn'}">
                                                <script>
                                                    if ('${payment.mobileNumber}' != '') {
                                                        var bnText = getNumberInBangla('' + ${payment.mobileNumber} + '');
                                                        document.writeln(bnText);
                                                    }
                                                </script>
                                            </c:if>
                                        </td>
                                        <td><c:out value="${payment.bank}"></c:out></td>
                                        <td><c:out value="${payment.branch}"></c:out></td>
                                            <td>
                                            <c:if test="${selectedLocale eq 'en'}">
                                                <c:out value="${payment.accountNumber}"></c:out>
                                            </c:if>
                                            <c:if test="${selectedLocale eq 'bn'}">
                                                <script>
                                                    var bnText = getNumberInBangla('' + ${payment.accountNumber} + '');
                                                    document.writeln(bnText);
                                                </script>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>-->
                    </div>
                </div>
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


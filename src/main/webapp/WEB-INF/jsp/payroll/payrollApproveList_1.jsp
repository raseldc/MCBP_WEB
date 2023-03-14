<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    #paymentListTable_length {
        float:left;
    }
    #paymentListTable_info {
        float:left;
    }
    table.dataTable thead .sorting_asc:after{
        content:""; 
    } 
</style>
<script>
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        var urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }
 if ("${message.message}")
        {
            bootbox.dialog({
                onEscape: function () {},
                title: '<spring:message code="label.success" />',
                message: "<b>${message.message}</b>",
                buttons: {
                    ok: {
                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                    }
                },

                callback: function (result) {
                }
            });
        }
//        if ("${message.message}")
//        {
//            bootbox.dialog({
//                onEscape: function () {},
//                title: <c:if test="${isSuccess}">'<spring:message code="label.success" />'</c:if><c:if test="${not isSuccess}">'<spring:message code="label.failure" />'</c:if>,
//                        message: "<b>${message.message}</b>",
//                buttons: {
//                    ok: {
//                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
//                    }
//                },
//
//                callback: function (result) {
//                }
//            });
//        }

        $("#paymentListForm").validate({
            rules: {// checks NAME not ID
                "scheme.id": {
                    required: true
                },
                "fiscalYear.id": {
                    required: true
                },
                "paymentCycle.id": {
//                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
        $(document).on('icheck', function () {
            $('input[type=checkbox]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            });
        }).trigger('icheck'); // trigger it for page load.
        $("#reasonSubmission").hide();
        $("#buttonSearch").click(function ()
        {
            var form = $('#paymentListForm');
            form.validate();
            if (form.valid()) {
                var serializedData = $("#paymentListForm").serialize();
                console.log(serializedData);
                $('#paymentListTable').DataTable().destroy();
                var path = '${pageContext.request.contextPath}';
                $('#paymentListTable').DataTable({
                    "processing": true,
                    "bPaginate": false,
                    "bFilter": true,
                    "bSort": false,
//                    "pageLength": 10,
//                    "lengthMenu": [5, 10, 15, 20],
                    "serverSide": true,
                    "pagingType": "full_numbers",
                    "dom": '<"top"i>rt<"bottom"lp><"clear">',
                    "language": {
                        "url": urlLang
                    },
                    "columnDefs": [
                        {
                            className: "text-right", "targets": [4, 5]
                        }
                    ],
                    "ajax": {
                        "url": path + "/payrollApprove/list",
                        "type": "POST",
                        "data": {
                            "fiscalYear": $("#fiscalYear").val(),
                            "paymentCycle": $("#paymentCycle").val(),
                            "divisionId": $("#divisionId").val(),
                            "districtId": $("#districtId").val(),
                            "upazilaId": $("#upazilaId").val(),
                            "payrollListType": $("#payrollListType").val()
                        }
                    },
                    "fnDrawCallback": function (oSettings) {
                        showModalDialog();
                        if (selectedLocale === 'bn')
                        {
                            localizeBanglaInDatatable("paymentListTable");
                        }
                        $("#reasonSubmission").hide();
                        $(document).trigger('icheck');
                        $('#select_all').on('ifChanged', function (event) {
                            $('#select_all').on('ifChecked ifUnchecked', function (event) {
                                if (event.type == 'ifChecked') {
                                    $('input.checkbox').iCheck('check');
                                } else {
                                    $('input.checkbox').iCheck('uncheck');
                                }
                            });
                        });
                        $('input.checkbox').on('ifChecked', function (event) {
                            $("#reasonSubmission").show();
                            return;
                        });
                        $('input.checkbox').on('ifUnchecked', function (event) {
                            if ($('input.checkbox').filter(':checked').length == 0)
                            {
                                $("#reasonSubmission").hide();
                            }
                            return;
                        });
                    },
                    "footerCallback": function (row, data, start, end, display) {
                        var api = this.api(), data;
                        $("tfoot").css("visibility", "visible");
                        // Remove the formatting to get integer data for summation
                        var intVal = function (i) {
                            z = typeof i === 'string' ?
                                    getNumberInEnglish(i).replace(/[\$,]/g, '') * 1 :
                                    typeof i === 'number' ?
                                    i : 0;
                            return z;
                        };
                        // Total over this page
                        benTotal = api
                                .column(5, {page: 'current'})
                                .data()
                                .reduce(function (a, b) {
                                    return intVal(a) + intVal(b);
                                }, 0);
                        // Total over this page
                        allowanceTotal = api
                                .column(6, {page: 'current'})
                                .data()
                                .reduce(function (a, b) {
                                    return intVal(a) + intVal(b);
                                }, 0);
                        // Update footer
                        if (selectedLocale === 'bn')
                        {
                            benTotal = getNumberInBangla("" + benTotal);
                            allowanceTotal = getNumberInBangla("" + allowanceTotal + ".00");
                        } else {
                            allowanceTotal = allowanceTotal + ".00";
                        }
                        $(api.column(5).footer()).html(benTotal);
                        $(api.column(6).footer()).html(allowanceTotal);
                    }
                });
            }
        });

        if ('${searchParameterForm.isDivisionAvailable}' === 'false')
        {
            loadDivision($('#divisionId'), '');
        } else if ('${searchParameterForm.isDistrictAvailable}' === 'false')
        {
            loadDistrict('${searchParameterForm.division.id}', $('#districtId'));
        } else if ('${searchParameterForm.isUpazilaAvailable}' === 'false')
        {
            loadUpazilla('${searchParameterForm.district.id}', $('#upazilaId'));
        }

        $('#upazilaList').iCheck('check');
        $("#payrollListType").val("UNION");
        $("#upazilaBlock").show();
        $("#districtBlock").hide();

        $('#unionList').on('ifChanged', function (event) {
            if ($('#upazilaList').is(":checked"))
            {
                $("#payrollListType").val("UNION");
                $("#upazilaBlock").show();
                $("#districtBlock").hide();
                $('#districtList').iCheck('uncheck');
                $('#bgmeaList').iCheck('uncheck');
                $('#bkmeaList').iCheck('uncheck');
            }
        });
        $('#municipalList').on('ifChanged', function (event) {
            if ($('#municipalList').is(":checked"))
            {
                $("#payrollListType").val("MUNICIPAL");
                $("#upazilaBlock").show();
                $("#districtBlock").hide();
                $('#districtList').iCheck('uncheck');
                $('#bgmeaList').iCheck('uncheck');
                $('#bkmeaList').iCheck('uncheck');
            }
        });
        $('#districtList').on('ifChanged', function (event) {
            if ($('#districtList').is(":checked"))
            {
                $("#payrollListType").val("DISTRICT");
                $('#districtBlock').show();
                $("#upazilaBlock").hide();
                $('#upazilaList').iCheck('uncheck');
                $('#bgmeaList').iCheck('uncheck');
                $('#bkmeaList').iCheck('uncheck');
            }
        });
        $('#bgmeaList').on('ifChanged', function (event) {
            if ($('#bgmeaList').is(":checked"))
            {
                $("#payrollListType").val("BGMEA");
                $("#upazilaBlock").hide();
                $('#districtBlock').hide();
                loadDivision($('#divisionId'), '');
                resetSelect($('#districtId'));
                resetSelect($('#upazilaId'));
                $('#upazilaList').iCheck('uncheck');
                $('#districtList').iCheck('uncheck');
                $('#bkmeaList').iCheck('uncheck');
            }
        });
        $('#bkmeaList').on('ifChanged', function (event) {
            if ($('#bkmeaList').is(":checked"))
            {
                $("#payrollListType").val("BKMEA");
                $("#upazilaBlock").hide();
                $("#districtBlock").hide();
                loadDivision($('#divisionId'), '');
                resetSelect($('#districtId'));
                resetSelect($('#upazilaId'));
                $('#upazilaList').iCheck('uncheck');
                $('#districtList').iCheck('uncheck');
                $('#bgmeaList').iCheck('uncheck');
            }
        });
    });

    function loadPresentDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#districtId');
        if (divId !== '') {
            loadDistrict(divId, distSelectId);
        } else {
            resetSelect(distSelectId);
            resetSelect($('#upazilaId'));
            resetSelect($('#unionId'));
        }
    }
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#upazilaId');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId);
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#unionId'));
        }
    }
    function loadAllPaymentCycle(selectObject) {
        console.log(typeof selectObject);
        console.log(selectObject.value);
        var fiscalYearId = selectObject.value;
        var paymentCycleSelectId = $('#paymentCycle');
        if (fiscalYearId !== '')
        {            
            loadParentPaymentCycle(fiscalYearId, paymentCycleSelectId, false);
            if ('${paymentGenerationForm.searchParameterForm.paymentCycle.id}' != '')
            {
                paymentCycleSelectId.val(${paymentGenerationForm.searchParameterForm.paymentCycle.id});
            } else
            {
                paymentCycleSelectId.prop("selectedIndex", "0");
            }
        } else
        {
            resetSelect(paymentCycleSelectId);
        }
    }
    function loadParentPaymentCycle(fiscalYearId, paymentCycleSelectId, isActive) {
        var ajaxUrl = "";
        if (isActive === true)
        {
            ajaxUrl = contextPath + "/getParentPaymentCycleIoList/" + fiscalYearId;
        } else
        {
            ajaxUrl = contextPath + "/getAllPaymentCycle/" + fiscalYearId;
        }

        $.ajax({
            type: "GET",
            url: ajaxUrl,
            async: false,
            dataType: "json",
            success: function (response) {
                paymentCycleSelectId.find('option').remove();
                $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(paymentCycleSelectId);
                $.each(response, function (index, value) {
                    if (selectedLocale === 'en') {
                        $('<option>').val(value.id).text(value.nameInEnglish).appendTo(paymentCycleSelectId);
                    } else if (selectedLocale === 'bn') {
                        $('<option>').val(value.id).text(value.nameInBangla).appendTo(paymentCycleSelectId);
                    }
                });
            },
            failure: function () {
                log("loading Payment Cycle failed!!");
            }
        });
    }
    function askApproveSubmit()
    {
        var approvalMessage;
        if (selectedLocale === 'bn') {
            approvalMessage = "আপনি কি পে-রোল অনুমোদন করতে চান?";
        } else
        {
            approvalMessage = "Are you sure you want to Approve The Payroll(s)?";
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
                if (result === true)
                {
                    var selectedPayrollSummaryIdList = "";
                    $('input.checkbox').each(function () {
                        if (this.checked) {
                            selectedPayrollSummaryIdList += this.id + ',';
                        }
                    });
                    $("#selectedPayrollSummaryIdList").val(selectedPayrollSummaryIdList);
                    saveFormToCookie($('#paymentListForm'));
                    $("#buttonApprove_submit").click();
                } else
                {
                    return true;
                }
            }
        });
    }
    function askRecheckSubmit()
    {
        var rejectMessage;
        if (selectedLocale === 'bn') {
            rejectMessage = "আপনি কি আবেদনকারীকে বাতিল করতে চান?";
        } else
        {
            rejectMessage = "Are you sure you want to Reject The Applicant(s)?";
        }
        bootbox.confirm({
            message: rejectMessage,
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
                if (result === true)
                {
                    var rejectedApplicantList = "";
                    $('input.checkbox').each(function () {
                        if (this.checked) {
                            rejectedApplicantList += this.id + ',';
                        }
                    });
                    $("#selectedApplicantType").val($("#applicantType").val());
                    $("#rejectedApplicantList").val(rejectedApplicantList);
                    saveFormToCookie($('#fieldVerificationForm'));
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
<c:set var="actionUrl" value="${contextPath}/payrollApprove" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.paymentApprove" />
        <small></small>
    </h1>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <form:form id="paymentListForm" class="form-horizontal" modelAttribute="searchParameterForm">
                <div class="form-group">
                    <div class="col-md-6">                                
                        <div class="form-group">
                            <label for="fiscalYear.id" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="fiscalYear.id"  id="fiscalYear" onchange="loadAllPaymentCycle(this)"> 
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                                </form:select> 
                                <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="paymentCycle.id" class="col-md-4 control-label"><spring:message code="label.paymentCycle" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="paymentCycle.id"  id="paymentCycle"> 
                                    <form:option value="" label="${select}"></form:option>                            
                                </form:select> 
                                <form:errors path="paymentCycle.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                        <form:hidden path="payrollListType" id="payrollListType"/>
                        <div class="form-group" id="payrollListTypeBlock">
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.payrollListType" /><span class="mandatory">*</span></label>
                                <div class="col-md-8 labelAsValue">                        
                                    <input type="radio" id="unionList" name="upazilaList">&nbsp;<spring:message code="payrolllistType.union" />
                                    <input type="radio" id="municipalList" name="upazilaList">&nbsp;<spring:message code="payrolllistType.municipal" />
                                    <input type="radio" id="districtList" name="districtList">&nbsp;<spring:message code="payrolllistType.district" />
                                    <input type="radio" id="bgmeaList" name="bgmeaList">&nbsp;<spring:message code="payrolllistType.bgmea" />
                                    <input type="radio" id="bkmeaList" name="bkmeaList">&nbsp;<spring:message code="payrolllistType.bkmea" />
                                </div>
                            </div>
                        </div>                             
                        <div id="upazilaBlock">
                            <div class="form-group">
                                <label for="division.id" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                                <c:choose>
                                    <c:when test="${searchParameterForm.isDivisionAvailable eq 'false'}">
                                        <div class="col-md-8">
                                            <spring:message code='label.select' var="select"/>
                                            <form:select class="form-control" path="division.id" id="divisionId" onchange="loadPresentDistrictList(this)">
                                                <form:option value="" label="${select}"></form:option>
                                                <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options> 
                                            </form:select>
                                            <form:errors path="division.id" cssStyle="color:red"></form:errors>
                                            </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-md-8 labelAsValue">
                                            <form:hidden id="divisionId" path="division.id"/>
                                            <c:choose>
                                                <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                                    ${searchParameterForm.division.nameInBangla}
                                                </c:when>
                                                <c:otherwise>
                                                    ${searchParameterForm.division.nameInEnglish}
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="form-group">
                                <label for="district.id" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                                <c:choose>
                                    <c:when test="${searchParameterForm.isDistrictAvailable eq 'false'}">
                                        <div class="col-md-8">
                                            <spring:message code='label.select' var="select"/>
                                            <form:select class="form-control" path="district.id" id="districtId" onchange="loadPresentUpazilaList(this)">
                                                <form:option value="" label="${select}"></form:option>
                                            </form:select>
                                            <form:errors path="district.id" cssStyle="color:red"></form:errors>
                                            </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-md-8 labelAsValue">
                                            <form:hidden id="districtId" path="district.id"/>
                                            <c:choose>
                                                <c:when test="${pageContext.response.locale eq 'bn'}">
                                                    ${searchParameterForm.district.nameInBangla}
                                                </c:when>
                                                <c:otherwise>
                                                    ${searchParameterForm.district.nameInEnglish}
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.upazila" /></label>
                                <c:choose>
                                    <c:when test="${searchParameterForm.isUpazilaAvailable eq 'false'}">
                                        <div class="col-md-8">
                                            <spring:message code='label.select' var="select"/>
                                            <form:select class="form-control" path="upazila.id" id="upazilaId" onchange="loadPresentUnionList(this)">
                                                <form:option value="" label="${select}"></form:option>
                                            </form:select>
                                            <form:errors path="upazila.id" cssStyle="color:red"></form:errors>
                                            </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-md-8 labelAsValue">
                                            <form:hidden id="upazilaId" path="upazila.id"/>
                                            <c:choose>
                                                <c:when test="${pageContext.response.locale eq 'bn'}">
                                                    ${searchParameterForm.upazila.nameInBangla}
                                                </c:when>
                                                <c:otherwise>
                                                    ${searchParameterForm.upazila.nameInEnglish}
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-4"></div>
                            <div class="col-md-8">
                                <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </div> 
    <form:form action="${actionUrl}" method="post" class="form-horizontal" role="form">
        <input type="hidden" id="selectedPayrollSummaryIdList" name="selectedPayrollSummaryIdList">
        <div class="row">
            <div class="col-xs-12">
                <div class="box">
                    <div class="box-body">

                        <table id="paymentListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th><input id="select_all"  type="checkbox"></th>
                                    <th><spring:message code="label.paymentCycle" /></th>
                                    <th><spring:message code="label.division" /></th>
                                    <th><spring:message code="label.district" /></th>
                                    <th><spring:message code="label.upazilla" /></th>
                                    <th><spring:message code="dashboard.totalBeneficiary" /></th>
                                    <th><spring:message code="payment.label.totalAllowanceAmount" /></th>
                                    <th><spring:message code="label.status" /></th>
                                </tr>
                            </thead>
                            <tfoot style="visibility: hidden">
                                <tr style="background: lightgrey">
                                    <th colspan="5" class="text-right"><spring:message code="label.total" /></th>
                                    <th></th>
                                    <th></th>
                                    <th colspan="1"></th>
                                </tr>
                            </tfoot>  
                        </table>                        
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6" id="reasonSubmission">
                <div class="col-md-2">
                    <label class="control-label"><spring:message code="label.remarks" /></label>
                </div>
                <div class="col-md-10">
                    <textarea class="form-control"  id="reasonFV" name="reasonFV" rows="3" cols="150"></textarea>
                    <br>
                    <button class="btn bg-blue" type="button" id="buttonAccept" name="action" value="accept" onclick="return askApproveSubmit();"><spring:message code="button.approve" /></button>
                    <button id="buttonApprove_submit" type="submit" style="display: none" name="action" class="btn bg-blue" value="accept" ></button>
                    &nbsp;&nbsp;
                    <button id="buttonReject" type="button" class="btn bg-red" value="reject"  onclick="return askRecheckSubmit();"><spring:message code="button.reject" /></button>
                    <button id="buttonReject_submit" type="submit" style="display: none" name="action" class="btn bg-red" value="reject" >
                </div>
            </div>                            
        </div>
    </form:form>
</section>

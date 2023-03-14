<%-- 
    Document   : subPayrollGenerationDistrict
    Created on : Nov 6, 2018, 2:49:08 PM
    Author     : user
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
    $(function () {
        var urlLang = "";
        var path = '${pageContext.request.contextPath}';
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
        }
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#paymentcyclemodel-delete-confirmation");
        if ("${message}")
        {
            bootbox.dialog({
                onEscape: function () {},
                title: <c:if test="${isSuccess}">'<spring:message code="label.success" />'</c:if><c:if test="${not isSuccess}">'<spring:message code="label.failure" />'</c:if>,
                        message: "<b>${message}</b>",
                buttons: {
                    ok: {
                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                    }
                },
                callback: function (result) {
                }
            });
        }

        if ('${innerMessage}')
        {
            $("#createPayroll").prop("disabled", true);
        }

        $("#paymentGenerationForm").validate({
            rules: {// checks NAME not ID
                "searchParameterForm.scheme.id": {
                    required: true
                },
                "searchParameterForm.fiscalYear.id": {
                    required: true
                },
                "searchParameterForm.paymentCycle.id": {
                    required: true
                },
                "searchParameterForm.division.id": {
                    required: true
                },
                "searchParameterForm.district.id": {
                    required: true
                },
                "searchParameterForm.upazila.id": {
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

        if ('${paymentGenerationForm.searchParameterForm.isDivisionAvailable}' === 'false')
        {
            console.log('division not available');
            loadDivision($('#divisionId'), '');
        } else
        {
            console.log('division available');
            loadDivision($('#divisionId'), '${paymentGenerationForm.searchParameterForm.division.id}');
        }

        if ('${paymentGenerationForm.searchParameterForm.isDistrictAvailable}' === 'false')
        {
            loadDistrict('${paymentGenerationForm.searchParameterForm.division.id}', $('#districtId'));
        } else if ('${paymentGenerationForm.searchParameterForm.isUpazilaAvailable}' === 'false')
        {
            loadUpazillaWithDistrict('${paymentGenerationForm.searchParameterForm.district.id}', $('#upazilaId'));
        }
        if ('${paymentGenerationForm.searchParameterForm.paymentCycle.id}' !== '')
        {
            loadActivePaymentCycle($("#fiscalYear")[0]);
        }
        if ('${actionType}' == 'edit' || '${actionType}' == 'submit')
        {
//            $("#right_buttons").show();
            $("#buttonSearchUnion").hide();
            $("#paymentGenerationUnionTable").hide();
        }
        $("#buttonSearchUnion").click(function ()
        {
            var form = $('#paymentGenerationForm');
            $("#countTable").show();
            form.validate();
            if (form.valid()) {
                var serializedData = $("#paymentGenerationForm").serialize();
                console.log(serializedData);
                $('#paymentGenerationUnionTable').DataTable().destroy();
                $('#paymentGenerationUnionTable').DataTable({
                    "processing": true,
                    "pageLength": 10,
                    "lengthMenu": [5, 10, 15, 20],
                    "serverSide": false,
                    "pagingType": "full_numbers",
                    "dom": '<"top"i>rt<"bottom"lp><"clear">',
                    "language": {
                        "url": urlLang
                    },
                    "ajax": {
                        "url": path + "/paymentGeneration/beforeCreate/unions",
                        "type": "POST",
                        "data": {
                            "fiscalYear": $("#fiscalYear").val(),
                            "paymentCycle": $("#paymentCycle").val(),
                            "applicantType": $("#applicantType").val(),
                            "divisionId": $("#divisionId").val(),
                            "districtId": $("#districtId").val(),
                            "upazilaId": $("#upazilaId").val(),
                            "unionId": $("#unionId").val(),
                            "applicantTypeList": "1,2"
                        }
                    },
                    "fnDrawCallback": function (oSettings) {
                        showModalDialog();
//                        $("#paymentGenerationTable").hide();
                        $("#paymentGenerationUnionTable").show();

                        if (selectedLocale === 'bn')
                        {
                            localizeBanglaInDatatable("paymentGenerationUnionTable");
                            $('#paymentGenerationUnionTable  tr').not(":first").each(function () {
                                $(this).find("td").eq(2).html(getNumberInBangla($(this).find("td").eq(2).html()));
                            });
                        }

//                        if ($('#paymentGenerationUnionTable  tr').eq(1).find("td").get(3))
//                        {
//                            $("#right_buttons").show();
//                        }
                    }
                });
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

    function loadActivePaymentCycle(selectObject) {
        console.log(selectObject.value);
        var fiscalYearId = selectObject.value;
        var paymentCycleSelectId = $('#paymentCycle');
        if (fiscalYearId !== '') {
//            console.log('pc = ' + ${paymentGenerationForm.searchParameterForm.paymentCycle.id});
            loadParentPaymentCycle(fiscalYearId, paymentCycleSelectId, true);
            if ('${paymentGenerationForm.searchParameterForm.paymentCycle.id}' != '')
                paymentCycleSelectId.val(${paymentGenerationForm.searchParameterForm.paymentCycle.id});
            else
                paymentCycleSelectId.prop("selectedIndex", "1");
        } else {
            resetSelect(paymentCycleSelectId);
        }
    }
    function loadParentPaymentCycle(fiscalYearId, paymentCycleSelectId, isActive) {

        var ajaxUrl = "";
        if (isActive === true)
        {
            ajaxUrl = contextPath + "/getChildPaymentCycle/" + fiscalYearId;
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
    function submitForm() {
        $('#paymentGenerationForm').validate();
        if ($("#paymentGenerationForm").valid()) {
            $('#paymentGenerationForm').submit();
        }
    }


</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="selectedLocale" value="${pageContext.response.locale}" />
<%--<c:if test="${not empty message}">
    <div class="alert 
         <c:if test="${isSuccess eq 'true'}">alert-success</c:if> 
         <c:if test="${isSuccess eq 'false'}">alert-danger</c:if> 
             alert-dismissable">
             <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
             <div>${message}</div>
    </div>
</c:if>--%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/paymentGeneration/create" />
    </c:when>
    <c:when test="${actionType eq 'edit'}">
        <c:set var="actionUrl" value="${contextPath}/paymentGeneration/edit" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/paymentGeneration/submit/${paymentGenerationForm.payrollSummary.id}" />
    </c:otherwise>
</c:choose>

<form:form id="paymentGenerationForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="paymentGenerationForm">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.subPayroll" />&nbsp;<spring:message code="generation" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/payrollSummary/district/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right" id="right_buttons">
            <button id="createPayroll" name="save" class="btn bg-blue" onclick="submitForm();" <c:if test="${actionType eq 'submit'}">disabled</c:if>>
                    <i class="fa fa-floppy-o"></i>
                <spring:message code='button.generatePayroll'/>
            </button>
            <button name="save" class="btn bg-green" onclick="submitForm();" <c:if test="${actionType ne 'submit'}">disabled</c:if>>
                    <i class="fa fa-floppy-o"></i>
                <spring:message code='button.submit'/>
            </button>
            <!--<c:if test="${actionType ne 'create'}">
                <span id="page-delete" class="btn bg-red">
                    <i class="fa fa-trash-o"></i>
                <spring:message code="delete" />
            </span>
            </c:if>-->
        </div>
    </section>
    <section class="content">
        <div class="row">
            <div class="col-xs-12">
                <c:if test="${paymentGenerationForm.payrollSummary.payrollStatus.displayName == 'Recheck'}">
                    <div class="box">
                        <div class="box-body">                        
                            <div class="form-group">
                                <div class="col-md-6">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="payment.label.comments" /></label>
                                    <label class="labelAsValue"> ${paymentGenerationForm.payrollSummary.approvalComments}</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                <div class="box">
                    <div class="box-body">
                        <form:hidden path="payrollSummary.id"/>
                        <div class="form-group">
                            <div class="col-md-6">                                
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>                                        
                                        <form:select class="form-control" path="searchParameterForm.fiscalYear.id" id="fiscalYear" onchange="loadActivePaymentCycle(this)">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options>
                                        </form:select>
                                        <form:errors path="searchParameterForm.fiscalYear.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.paymentCycle" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="searchParameterForm.paymentCycle.id" id="paymentCycle">
                                            <form:option value="" label="${select}"></form:option>
                                        </form:select>
                                        <form:errors path="searchParameterForm.paymentCycle.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                <form:hidden path="searchParameterForm.applicantType" id="applicantType"/>
                                <div class="form-group" id="applicantTypeBlock" style="display: none">
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.applicantType" /><span class="mandatory">*</span></label>
                                        <div class="col-md-8 labelAsValue">                        
                                            <input type="radio" id="regularUser" name="regularUser">&nbsp;<spring:message code="applicant.regular" />
                                            <input type="radio" id="bgmeaUser" name="bgmeaUser">&nbsp;<spring:message code="applicant.bgmea" />
                                            <input type="radio" id="bkmeaUser" name="bkmeaUser">&nbsp;<spring:message code="applicant.bkmea" />
                                        </div>
                                    </div>
                                </div>
                                <c:if test="${sessionScope.userDetail.schemeShortName == 'LMA'}">
                                    <c:if test="${sessionScope.userDetail.userType.displayName eq 'BGMEA' || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">
                                        <!--not required: payroll should be generated for all factory-->
                                    </c:if>
                                    <c:if test="${sessionScope.userDetail.userType.displayName eq 'BKMEA' || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">
                                        <!--not required: payroll should be generated for all factory-->
                                    </c:if>  
                                </c:if>             
                                <c:if test="${sessionScope.userDetail.userType.displayName eq 'Others' || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">        
                                    <div id="regularBlock">

                                        <div class="form-group">
                                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>                                    
                                            <c:choose>                                        
                                                <c:when test="${paymentGenerationForm.searchParameterForm.isDivisionAvailable eq 'false'}">
                                                    <div class="col-md-8">
                                                        <spring:message code='label.select' var="select"/>
                                                        <form:select class="form-control" path="searchParameterForm.division.id" id="divisionId" onchange="loadPresentDistrictList(this)">
                                                            <form:option value="" label="${select}"></form:option>
                                                            <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options>
                                                        </form:select>
                                                        <form:errors path="searchParameterForm.division.id" cssStyle="color:red"></form:errors>
                                                        </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="col-md-8 labelAsValue">
                                                        <form:hidden id="divisionId" path="searchParameterForm.division.id"/>
                                                        <c:choose>
                                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                                ${paymentGenerationForm.searchParameterForm.division.nameInBangla}
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${paymentGenerationForm.searchParameterForm.division.nameInEnglish}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="form-group">
                                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                                            <c:choose>
                                                <c:when test="${paymentGenerationForm.searchParameterForm.isDistrictAvailable eq 'false'}">
                                                    <div class="col-md-8">
                                                        <spring:message code='label.select' var="select"/>
                                                        <form:select class="form-control" path="searchParameterForm.district.id" id="districtId" onchange="loadPresentUpazilaList(this)">
                                                            <form:option value="" label="${select}"></form:option>
                                                        </form:select>
                                                        <form:errors path="searchParameterForm.district.id" cssStyle="color:red"></form:errors>
                                                        </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="col-md-8 labelAsValue">
                                                        <form:hidden id="districtId" path="searchParameterForm.district.id"/>
                                                        <c:choose>
                                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                                ${paymentGenerationForm.searchParameterForm.district.nameInBangla}
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${paymentGenerationForm.searchParameterForm.district.nameInEnglish}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="form-group">
                                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.districtOrUpazila" /><span class="mandatory">*</span></label>
                                            <c:choose>
                                                <c:when test="${paymentGenerationForm.searchParameterForm.isUpazilaAvailable eq 'false'}">
                                                    <div class="col-md-8">
                                                        <spring:message code='label.select' var="select"/>
                                                        <form:select class="form-control" path="searchParameterForm.upazila.id" id="upazilaId">
                                                            <form:option value="" label="${select}"></form:option>
                                                        </form:select>
                                                        <form:errors path="searchParameterForm.upazila.id" cssStyle="color:red"></form:errors>
                                                        </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="col-md-8 labelAsValue">
                                                        <form:hidden id="upazilaId" path="searchParameterForm.upazila.id"/>
                                                        <c:choose>
                                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                                ${paymentGenerationForm.searchParameterForm.upazila.nameInBangla}
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${paymentGenerationForm.searchParameterForm.upazila.nameInEnglish}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                </c:if>
                                <div class="row">
                                    <div class="col-lg-8"></div>
                                    <div class="col-lg-4" style="text-align: right">
                                        <button type="button" id="buttonSearchUnion" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.beneficiaryNumber" /></button>
                                    </div>
                                </div>
                                <!--                                <div class="form-group">         
                                                                    <div class="col-md-offset-10">
                                                                        <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                                                                    </div>
                                                                </div>-->
                            </div>
                            <!--                            <table id="paymentGenerationTable" class="table table-bordered table-hover" style="display: none">
                                                            <thead>
                                                                <tr>
                                                                    <th><spring:message code="payment.label.beneficiary" /></th>
                                                                    <th><spring:message code="payment.label.bankName" /></th>
                                                                    <th><spring:message code="payment.label.branchName" /></th>
                                                                    <th><spring:message code="payment.label.beneficiaryNid" /></th>
                                                                    <th><spring:message code="payment.label.accountNumber" /></th>   
                                                                    <th><spring:message code="payment.label.mobileNumber" /></th>
                                                                    <th><spring:message code="label.routingNumber" /></th>                                
                                                                </tr>
                                                            </thead>                        
                                                        </table>-->

                            <div style="font-size: 18px; padding: 15px 10px; color: red">${innerMessage}</div>
                        </div>
                        <div class="row" id="countTable">
                            <div class="col-lg-2">

                            </div>
                            <div class="col-lg-9">
                                <table id="beneficiaryCountTable" class="table table-bordered table-hover" style="display: none; text-align: center">
                                    <thead>
                                        <tr>
                                            <th><spring:message code="label.areaType"/></th>
                                            <th><spring:message code="label.area"/></th>
                                            <th><spring:message code="label.beneficiaryNumber" /></th>                                
                                        </tr>
                                    </thead>                        
                                </table>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </form:form>

        <div id='paymentcyclemodel-delete-confirmation' class="modal fade" tabindex="-1" role="dialog" aria-labelledby="paymentcyclemodel-delete-confirmation-title">
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



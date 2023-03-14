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

        if ("${message}")
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

        showModalDialog();

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
                        "url": path + "/payrollSummary/list",
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
        if (fiscalYearId !== '')
        {
            loadParentPaymentCycle(fiscalYearId, paymentCycleSelectId, true);
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
            ajaxUrl = contextPath + "/getParentPaymentCycleIoList/" + fiscalYearId+"/3";
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
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.payment" />&nbsp;<spring:message code="label.list" />
        <small></small>
    </h1>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-body">
                    <form:form id="paymentListForm" class="form-horizontal" modelAttribute="searchParameterForm">
                        <form:hidden path="payrollListType" id="payrollListType"/>
                        <div class="form-group">
                            <div class="col-md-6">                                
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="fiscalYear.id"  id="fiscalYear" onchange="loadActivePaymentCycle(this)"> 
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                                        </form:select> 
                                        <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.paymentCycle" /></label>
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
                                    <div id="regularBlock">        
                                        <div class="form-group">
                                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
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
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
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
                    <table id="paymentListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
<!--                                <th><spring:message code="paymentCycle.label.scheme" /></th>
                                <th><spring:message code="paymentCycle.label.fiscalYear" /></th>-->
                                <th><spring:message code="label.paymentCycle" /></th>
                                <th><spring:message code="label.division" /></th>
                                <th><spring:message code="label.district" /></th>
                                <th><spring:message code="label.upazilla" /></th>
                                <th><spring:message code="payment.label.totalBeneficiary" /></th>
                                <th><spring:message code="payment.label.totalAllowanceAmount" /></th>
                                <th><spring:message code="label.status" /></th>                                
                                <th><spring:message code="label.action" /></th>
                            </tr>
                        </thead>                        
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
    #beneficiaryCountTable_length {
        float:left;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    }    
</style>
<script>
    $(function () {
        if ('${paymentGenerationForm.searchParameterForm.payrollListType.displayName}' === 'Union')
        {
            $(menuSelect("${pageContext.request.contextPath}/subPayroll/union/create"));
        } else
        {
            $(menuSelect("${pageContext.request.contextPath}/subPayroll/municipal/create"));
        }
        var urlLang = "";
        var contextPath = '${pageContext.request.contextPath}';
        var locale = "${pageContext.response.locale}";
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + locale + ".js");
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }
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
        if ('${paymentGenerationForm.searchParameterForm.isDivisionAvailable}' === 'false')
        {
            console.log('division not available');
//            loadDivision($('#divisionId'), '');
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
            loadUpazilla('${paymentGenerationForm.searchParameterForm.district.id}', $('#upazilaId'));
        }
        if ('${paymentGenerationForm.searchParameterForm.paymentCycle.id}' !== '')
        {
            loadActivePaymentCycle($("#fiscalYear")[0]);
        }
        if ('${actionType}' == 'edit' || '${actionType}' == 'submit')
        {
            $("#buttonSearchUnion").hide();
            $("#beneficiaryCountTable").hide();
        }
        $("#buttonSearchUnion").click(function ()
        {
            var form = $('#paymentGenerationForm');
            $("#countTable").show();
            form.validate();
            if (form.valid()) {
                $('#beneficiaryCountTable').DataTable().destroy();
                $('#beneficiaryCountTable').DataTable({
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
                        "url": contextPath + "/paymentGeneration/beforeCreate/unions",
                        "type": "POST",
                        "data": {
                            "fiscalYear": $("#fiscalYear").val(),
                            "paymentCycle": $("#paymentCycle").val(),
                            "payrollListType": $("#payrollListType").val(),
                            "divisionId": $("#divisionId").val(),
                            "districtId": $("#districtId").val(),
                            "upazilaId": $("#upazilaId").val()
                        }
                    },
                    "fnDrawCallback": function (oSettings) {
                        $("#beneficiaryCountTable").show();
                        if (selectedLocale === 'bn')
                        {
                            localizeBanglaInDatatable("beneficiaryCountTable");
                            $('#beneficiaryCountTable  tr').not(":first").each(function () {
                                $(this).find("td").eq(2).html(getNumberInBangla($(this).find("td").eq(2).html()));
                            });
                        }
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
                                .column(2, {page: 'current'})
                                .data()
                                .reduce(function (a, b) {
                                    return intVal(a) + intVal(b);
                                }, 0);

                        // Update footer
                        if (selectedLocale === 'bn')
                        {
                            benTotal = getNumberInBangla("" + benTotal);
                        }
                        $(api.column(2).footer()).html(benTotal);
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
            if ('${paymentGenerationForm.searchParameterForm.payrollListType.displayName}' === 'Union')
            {
                ajaxUrl = contextPath + "/getChildPaymentCycleIoList/" + fiscalYearId + "/1";
            } else
            {
                ajaxUrl = contextPath + "/getChildPaymentCycleIoList/" + fiscalYearId + "/3";
            }
        } else
        {
            ajaxUrl = contextPath + "/getAllPaymentCycle/" + fiscalYearId;
        }
//        if (isActive === true)
//        {
//            ajaxUrl = contextPath + "/getChildPaymentCycle/" + fiscalYearId;
//        } else
//        {
//            ajaxUrl = contextPath + "/getAllPaymentCycle/" + fiscalYearId;
//        }

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
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/payrollSummary/upazila/list"><spring:message code="label.backToList" /></a></small>
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
                        <form:hidden path="searchParameterForm.payrollListType" id="payrollListType"/>
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
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.upazila" /><span class="mandatory">*</span></label>
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
                                <div class="form-group">
                                    <label for="buttonSearchUnion" class="col-md-4 control-label"></label>
                                    <div class="col-md-8">
                                        <button type="button" id="buttonSearchUnion" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.beneficiaryNumber" /></button>
                                    </div>
                                </div>        
                                <div style="font-size: 18px; padding: 15px 10px; color: red">${innerMessage}</div>
                            </div>
                            <div class="row" id="countTable">                               
                                <div class="col-md-8 col-md-offset-2">
                                    <table id="beneficiaryCountTable" class="table table-bordered table-hover" style="display: none; text-align: center">
                                        <thead>
                                            <tr>
                                                <th class="text-center">#</th>
                                                <th class="text-center"> 
                                                    <c:choose>
                                                        <c:when test="${paymentGenerationForm.searchParameterForm.payrollListType eq 'UNION'}">
                                                            <spring:message code="label.union" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <spring:message code="label.municipal" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </th>
                                                <th class="text-center"><spring:message code="label.beneficiaryNumber" /></th>                                
                                            </tr>
                                        </thead>
                                        <tfoot style="visibility: hidden">
                                            <tr style="background: lightgrey">
                                                <th colspan="2" class="text-right"><spring:message code="label.total" /></th>
                                                <th class="text-center"></th>                                            
                                            </tr>
                                        </tfoot>  
                                    </table>
                                </div>                                
                            </div>

                        </div>
                    </div>
                </div>
            </form:form>
            </section>
            <!-- /.content -->


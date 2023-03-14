<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
    #paymentListTable_length {
        float:left;
    }
</style>
<script>
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        var urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }
        var path = '${pageContext.request.contextPath}';

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
        $("#paymentListForm").validate({
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

        $("#viewButton").click(function ()
        {
            var form = $('#paymentListForm');
            form.validate();
            if (form.valid()) {

                $('#paymentListTable').DataTable().destroy();
                $('#paymentListTable').DataTable({
                    "processing": true,
//                "pageLength": 10,
                    "bSort": false,
                    "paging": false,
                    "serverSide": true,
                    "searching": false,
                    "pagingType": "full_numbers",
                    "language": {
                        "url": urlLang
                    },
//                "columnDefs": [
//                    {"width": 150, "targets": [0, 6]},
//                    {"width": 20, "targets": [7]}
//                ],
                    "ajax": {
                        "url": path + "/supplementaryPayroll/list",
                        "type": "POST",
                        "data": {"paymentCycle": $('#paymentCycle').val()
                        }
                    },
                    "fnDrawCallback": function (oSettings) {
                        if (selectedLocale === 'bn')
                        {
                            localizeBanglaInDatatable("branchListTable");
                        }
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
        var fiscalYearId = selectObject.value;
        var paymentCycleSelectId = $('#paymentCycle');
        if (fiscalYearId !== '')
        {
            loadParentPaymentCycle(fiscalYearId, paymentCycleSelectId);
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
    function loadParentPaymentCycle(fiscalYearId, paymentCycleSelectId) {

        var ajaxUrl = "";
        ajaxUrl = contextPath + "/getAllPaymentCycle/" + fiscalYearId + "/1";
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
<c:set var="actionUrl" value="${contextPath}/supplementaryPayroll/create" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.supplementaryPayrollCreation" />
        <small></small>
    </h1>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <form:form id="paymentListForm" action="${actionUrl}" method="post"  class="form-horizontal" modelAttribute="searchParameterForm">
                <input type="hidden" name="type" value="rural">
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
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.paymentCycle" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="paymentCycle.id"  id="paymentCycle"> 
                                    <form:option value="" label="${select}"></form:option>                            
                                </form:select> 
                                <form:errors path="paymentCycle.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label"></label>
                                <div class="col-md-8">                                            
                                    <button type="button" id="viewButton" class="btn bg-blue"><i class="fa fa-floppy-o"></i>View Data</button>
                                    <button type="submit" name="action" value="Update" class="btn bg-aqua">Update Payroll</button>
                                    <button type="submit" name="action" value="Send" class="btn bg-gray">Send to SPBMU</button>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div id="regularBlock">
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>                                    

                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="division.id" id="divisionId" onchange="loadPresentDistrictList(this)">
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options>
                                    </form:select>
                                    <form:errors path="division.id" cssStyle="color:red"></form:errors>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="district.id" id="districtId" onchange="loadPresentUpazilaList(this)">
                                        <form:option value="" label="${select}"></form:option>
                                    </form:select>
                                    <form:errors path="district.id" cssStyle="color:red"></form:errors>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.upazila" /></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="upazila.id" id="upazilaId">
                                        <form:option value="" label="${select}"></form:option>
                                    </form:select>
                                    <form:errors path="upazila.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                            </div>
                        </div>  
                    </div> 
            </form:form>                    
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="box">
                <div class="box-body">
                    <table id="paymentListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th>NID</th>
                                <th>Prev. A/C Name</th>
                                <th>Current A/C Name</th>
                                <th>Prev. A/C No.</th>
                                <th>Current A/C No.</th>
                                <th>Prev. Bank/Others</th>
                                <th>Current Bank/Others</th>
                                <!--                                <th>Prev. Branch</th>
                                                                <th>Current Branch</th>-->
                                <th>Prev. Routing #</th>
                                <th>Current Routing #</th>
                                <th>Division</th>
                                <th>District</th>
                                <th>Upazila</th>
                            </tr>
                        </thead> 
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

</section>

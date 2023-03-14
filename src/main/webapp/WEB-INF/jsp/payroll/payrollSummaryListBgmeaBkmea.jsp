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
                        {className: "text-right", "targets": [1, 2]},
                        { "width": "180", "targets": 3 }
                    ],
                    "ajax": {
                        "url": path + "/payrollSummary/list",
                        "type": "POST",
                        "data": {
                            "fiscalYear": $("#fiscalYear").val(),
                            "paymentCycle": $("#paymentCycle").val(),
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
    });
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
                            <div class="form-group">
                                <div class="col-md-4"></div>                                    
                                <div class="col-md-8">
                                    <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-body">
                    <table id="paymentListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.paymentCycle" /></th>                           
                                <th><spring:message code="payment.label.totalBeneficiary" /></th>
                                <th><spring:message code="payment.label.totalAllowanceAmount" /></th>
                                <th><spring:message code="label.createdBy" /></th>  
                                <th><spring:message code="label.creationDate" /></th> 
                                <th><spring:message code="label.approvedBy" /></th>  
                                <th><spring:message code="label.approvedDate" /></th>                                                                                                                               
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

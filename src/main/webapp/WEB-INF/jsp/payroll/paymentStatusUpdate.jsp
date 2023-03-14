<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style>
    .numericCol{
        text-align:right;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    } 
</style>
<script type="text/javascript">
    $(function () {
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
        }
        $("#buttonSearch").click(function ()
        {
            var form = $('#paymentListForm');
            form.validate();
            if (form.valid()) {
                $('#paymentListTable').DataTable().destroy();
                $('#paymentListTable').DataTable({
                    "processing": true,
                    "pageLength": 10,
                    "lengthMenu": [10, 25, 50, 100],
                    "bFilter": false,
                    "bSort": false,
                    "serverSide": true,
                    "pagingType": "full_numbers",
                    "language": {
                        "url": urlLang
                    },
                    "ajax": {
                        "url": path + "/payment/cycle/list",
                        "type": "POST",
                        "data": {
                            "paymentCycle": $('#paymentCycle').val(),
                            "nid": $("#nid").val(),
                            "accountNo": $("#accountNo").val()
                        }
                    },
                    "aoColumnDefs": [
                        {"sClass": "numericCol", "aTargets": [-1]}
                    ],
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
            }
        });
        $("#paymentListForm").validate({
            rules: {// checks NAME not ID
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
//        $("#buttonSearch").click();
    });
    function loadActivePaymentCycle(selectObject) {
        console.log(selectObject.value);
        var fiscalYearId = selectObject.value;
        var paymentCycleSelectId = $('#paymentCycle');
        if (fiscalYearId !== '') {
//            console.log('pc = ' + ${paymentGenerationForm.searchParameterForm.paymentCycle.id});
            loadPaymentCycle(fiscalYearId, paymentCycleSelectId);
            if ('${paymentGenerationForm.searchParameterForm.paymentCycle.id}' != '')
                paymentCycleSelectId.val(${paymentGenerationForm.searchParameterForm.paymentCycle.id});
            else
                paymentCycleSelectId.prop("selectedIndex", "1");

        } else {
            resetSelect(paymentCycleSelectId);
        }
    }
    function loadPaymentCycle(fiscalYearId, paymentCycleSelectId) {
        var ajaxUrl = contextPath + "/getAllPaymentCycle/" + fiscalYearId + "/1";
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
<c:set var="selectedLocale" value="${pageContext.response.locale}" />
<c:set var="actionUrl" value="${contextPath}/payment/statusupdate" />
<form:form id="paymentListForm" class="form-horizontal" modelAttribute="searchParameterForm">
    <section class="content-header clearfix">
        <h1>
            <font color="black">
            <spring:message code="payment.statusUpdate" />
            </font>
            <div class="pull-right">
                <button type="submit" name="save" class="btn bg-blue" value="save">
                    <i class="fa fa-floppy-o"></i>
                    <spring:message code="statusUpdate" />
                </button>
            </div>    
        </h1>
    </section>
    <section class="content">
        <input type="hidden" name="type" value="rural">
        <div class="row">
            <div class="col-xs-12">
                <div class="box container-fluid">
                    <div class="box-body container-fluid">
                        <div class="row">
                            <div class="col-md-6">                                
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>                                        
                                        <form:select class="form-control" path="fiscalYear.id" id="fiscalYear" onchange="loadActivePaymentCycle(this)">
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
                                        <form:select class="form-control" path="paymentCycle.id" id="paymentCycle">
                                            <form:option value="" label="${select}"></form:option>
                                        </form:select>
                                        <form:errors path="paymentCycle.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-md-4"></label> 
                                        <div class="col-md-1 control-label" style="text-align: right">
                                            <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                                    </div>
                                </div> 
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="nid" class="col-md-4 control-label"><spring:message code='label.nid' var="nid" />${nid}</label>
                                    <div class="col-md-8">
                                        <input type="text" id="nid" name="nid" class="form-control" placeholder="${nid}">
                                    </div>
                                </div>                                
                                <div class="form-group">
                                    <label for="accountNo" class="col-md-4 control-label"><spring:message code='label.accountNo' var="accountNo" />${accountNo}</label>
                                    <div class="col-md-8">
                                        <input type="text" id="accountNo" name="accountNo" class="form-control" placeholder="${accountNo}">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <!--                    <br><br><br><br><br><br>-->
                            <table id="paymentListTable" class="table table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th><spring:message code="payment.label.beneficiary" /></th>
                                        <th><spring:message code="payment.label.beneficiaryNid" /></th>
                                        <th><spring:message code="payment.label.mobileNumber" /></th>
                                        <th><spring:message code="payment.label.bankName" /></th>
                                        <th><spring:message code="payment.label.branchName" /></th>
                                        <th><spring:message code="payment.label.accountNumber" /></th>                                
                                        <th> 
                                            <c:choose>
                                                <c:when test="${sessionScope.userDetail.schemeShortName == 'MA'}">
                                                    <spring:message code="label.union" />
                                                </c:when>
                                                <c:otherwise>
                                                    <spring:message code="label.municipalOrCityCorporation"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </th>         
                                        <th><spring:message code="payment.label.allowanceAmount" /></th>
                                        <th><spring:message code="payment.label.status" /></th>
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
    </form:form>
</section>

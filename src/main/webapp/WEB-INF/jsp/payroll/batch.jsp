<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<script src="<c:url value="/resources/js/unijoy.js" />" ></script>
<script src="<c:url value="/resources/js/bootbox.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/resources/js/MonthPicker.min.js"/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/MonthPicker.min.css" />">
<script type="text/javascript" src="<c:url value="/resources/js/MonthPicker-bn.js"/>" ></script>

<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/batch/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#paymentcyclemodel-delete-confirmation");
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $.validator.addMethod("uniqueBatch", function (value, element) {
            var isSuccess = false;
            $.ajax({
                type: "POST",
                url: contextPath + "/checkUniqueBatch",
                async: false,
                data: {'batchName': $("#nameInEnglish").val(), 'batchId': $("#id").val()
                },
                success: function (msg)
                {
                    isSuccess = msg === true ? true : false;
                },
                failure: function () {
                    log("checking uniqueness of batch failed!!");
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log('error=' + xhr);
                    console.log('error=' + thrownError);
                }
            });
            return isSuccess;
        });
        console.log('now=' + new Date());
        var currentDate = new Date();
        var currentMonth = currentDate.getMonth() + 1;
        var currentYear = currentDate.getFullYear() + 1;

        console.log('currentMonth = ' + currentMonth);

        $('#startDate').MonthPicker({Button: false, MonthFormat: 'mm-yy', OnAfterChooseMonth: function (selectedDate) {
                if (selectedLocale == 'bn') {
                    $("#" + this.id).val(getNumberInBangla($("#" + this.id).val()));
                }
                generateBatchName(this);
            }});
        $('#endDate').MonthPicker({Button: false, MonthFormat: 'mm-yy', OnAfterChooseMonth: function (selectedDate) {
                if (selectedLocale == 'bn') {
                    $("#" + this.id).val(getNumberInBangla($("#" + this.id).val()));
                }
                generateBatchName(this);
            }});

//        $('#startDate').monthpicker({changeYear: true, yearRange: "-1:+0", dateFormat: 'yy-mm', minDate: '2016-05'});
//        $('#endDate').monthpicker({changeYear: true, yearRange: "-1:+0", dateFormat: 'yy-mm'});


        jQuery.validator.addMethod("checkPaymentCycleSequence", function (value, element, param) {
            var startDateSt = getNumberInEnglish($("#startDate").val());
            var startYear = startDateSt.substring(3, 7);
            var startMonth = startDateSt.substring(0, 2);

            var endDateSt = getNumberInEnglish($("#endDate").val());
            var endYear = endDateSt.substring(3, 7);
            var endMonth = endDateSt.substring(0, 2);

            var startDate = $("#startDate").val() !== "" ? new Date(startYear, startMonth - 1) : "";
            var endDate = $("#endDate").val() !== "" ? new Date(endYear, endMonth - 1) : "";

            if (startDate != "" && endDate != "" && endDate - startDate < 0)
            {
                return false;
            }
            return true;
        });

        $("#batchForm").validate({
            rules: {// checks NAME not ID
                "startDate": {
                    required: true,
                    checkPaymentCycleSequence: true
                },
                "endDate": {
                    required: true,
                    checkPaymentCycleSequence: true
                },
                "nameInEnglish": {
                    required: true,
                    uniqueBatch: true,
                },
                "nameInBangla": {
                    required: true,
                }
            },
            errorPlacement: function (error, element) {
                console.log(element);
                error.insertAfter(element);
            }
        });

        if ('${batch.startDate}' != '')
        {
            $("#startDate").val((${batch.startDate.time.month+1}).pad() + "-" + ${batch.startDate.time.year+1900});
//
//            console.log('edit, startYearOfFY=' + startYearOfFY + ", endYearOfFY=" + endYearOfFY);
//
//            /* need function */
//            $('#startDate').MonthPicker('option', 'StartYear', startYearOfFY);
////            $('#endDate').MonthPicker('option', 'StartYear', startYearOfFY);
//
//            var minMonth = monthDiff(new Date(startYearOfFY, 06, 1), new Date());
//            minMonth += 1;
//            $('#startDate').MonthPicker('option', 'MinMonth', -minMonth);
//            $('#endDate').MonthPicker('option', 'MinMonth', -minMonth);
//
//            var maxMonth = monthDiff(new Date(endYearOfFY, 05, 1), new Date());
//            console.log('max = ' + maxMonth);
//            maxMonth += 1;
//            $('#startDate').MonthPicker('option', 'MaxMonth', -maxMonth);
////            $('#endDate').MonthPicker('option', 'MaxMonth', -maxMonth);
//

        }
        if ('${batch.endDate}' != '')
        {
            $("#endDate").val((${batch.endDate.time.month+1}).pad() + "-" +${batch.endDate.time.year+1900});
        }

        if (selectedLocale == 'bn') {
            $("#startDate").val(getNumberInBangla($("#startDate").val()));
            $("#endDate").val(getNumberInBangla($("#endDate").val()));
        }
        if("${batch.id}" !== "")
        {
            $('#startDate').prop('readonly', true);
            $('#endDate').prop('readonly', true);
            $('#startDate').MonthPicker({ Disabled: true });
            $('#endDate').MonthPicker({ Disabled: true });
        }
    });

    function generateBatchName(event) {

        var startDateSt = getNumberInEnglish($("#startDate").val());
        var startYear = startDateSt.substring(3, 7);
        var startMonth = startDateSt.substring(0, 2);

        var endDateSt = getNumberInEnglish($("#endDate").val());
        var endYear = endDateSt.substring(3, 7);
        var endMonth = endDateSt.substring(0, 2);

        var startDate = $("#startDate").val() !== "" ? new Date(startYear, startMonth - 1) : "";
        var endDate = $("#endDate").val() !== "" ? new Date(endYear, endMonth - 1) : "";
        if (startDate !== "" && endDate !== "")
        {
            $("#nameInEnglish").val(getShortMonthNameInEnglish(startDate) + "/" + startYear.substring(2, 4) + "-" + getShortMonthNameInEnglish(endDate) + "/" + endYear.substring(2, 4));
            $("#nameInBangla").val(getShortMonthNameInBangla(startDate) + "/" + getNumberInBangla(startYear.substring(2, 4)) + "-" + getShortMonthNameInBangla(endDate) + "/" + getNumberInBangla(endYear.substring(2, 4)));
            $("#monthCount").val(monthDiff(startDate, endDate)+2);
        }
    }

    function submitForm()
    {
        var form = $("#batchForm");
        $("#startDate").val(getNumberInEnglish($("#startDate").val()));
        $("#endDate").val(getNumberInEnglish($("#endDate").val()));
        form.validate();
        if("${batch.id}" !== "")
        {
            $('#startDate').MonthPicker({ Disabled: false });
            $('#endDate').MonthPicker({ Disabled: false });
        }
        if (form.valid())
        {
            $("#startDate").val("01-" + $("#startDate").val());
            $("#endDate").val("01-" + $("#endDate").val());
            $("#batchForm").submit();
        } else {
            if (selectedLocale == 'bn') {
                $("#startDate").val(getNumberInBangla($("#startDate").val()));
                $("#endDate").val(getNumberInBangla($("#endDate").val()));
            }
        }
    }

</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/batch/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/batch/edit/${batch.id}" />
    </c:otherwise>
</c:choose>

<form:form id="batchForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="batch">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.batch" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/batch/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue" onclick="submitForm()">
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
                            <label for="" class="col-md-4 control-label"><spring:message code="batch.label.period" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <div class="form-group row">                                
                                <span for="startDate" class="col-md-2 control-label"><spring:message code='paymentCycle.label.from'/></span>
                                <div class="col-md-4">
                                    <spring:message code='paymentCycle.label.startMonth' var="startMonth"/>                                    
                                    <form:input class="form-control" placeholder="${startMonth}" path="startDate" onchange="generateBatchName(this.id)" autofocus="autofocus"/>
                                    <form:errors path="startDate" cssStyle="color:red"></form:errors>
                                </div>
                                <span for="endDate" class="col-md-2 control-label"><spring:message code='paymentCycle.label.to'/></span>
                                <div class="col-md-4">
                                    <spring:message code='paymentCycle.label.endMonth' var="endMonth"/>
                                    <form:input class="form-control" placeholder="${endMonth}" path="endDate" onchange="generateBatchName(this.id)" autofocus="autofocus"/>
                                    <form:errors path="endDate" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                            </div>
                        </div>                 
                        <div class="form-group">
                            <label for="nameInBangla" class="col-md-4 control-label"><spring:message code="paymentCycle.label.nameInBangla" /></label>
                        <div class="col-md-8">
                            <spring:message code='paymentCycle.label.nameInBangla' var="nameInBangla"/>
                            <form:input class="form-control" path="nameInBangla" readonly="true" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="nameInEnglish" class="col-md-4 control-label"><spring:message code="paymentCycle.label.nameInEnglish" /></label>
                        <div class="col-md-8">
                            <spring:message code='paymentCycle.label.nameInEnglish' var="nameInEnglish"/>
                            <form:input class="form-control" path="nameInEnglish" readonly="true" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="monthCount" class="col-md-4 control-label"><spring:message code="label.schemeDefaultMonth" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.schemeDefaultMonth' var="monthCount"/>
                            <form:input class="form-control" placeholder="${monthCount}" readonly="true" path="monthCount" autofocus="autofocus"/>
                            <form:errors path="monthCount" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="active" class="col-md-4 control-label"><spring:message code="label.active" /></label>
                        <div class="col-md-8">
                            <form:checkbox class="icheckbox_square-blue" path="active" id="active" />
                        </div>  
                    </div>
                </div>
            </form:form>
        </div>
    </div>

    <div id='paymentcyclemodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="paymentcyclemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/batch/delete/${batch.id}" method="post">
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


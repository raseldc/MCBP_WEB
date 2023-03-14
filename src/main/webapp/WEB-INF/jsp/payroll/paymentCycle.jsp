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
        $(menuSelect("${pageContext.request.contextPath}/paymentCycle/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#paymentcyclemodel-delete-confirmation");
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");

        makeUnijoyEditor('allowanceAmount');
//        makeUnijoyEditor('budgetAmount');

        $.validator.addMethod("uniquePaymentCycle", function (value, element) {
            var isSuccess = false;
            $.ajax({
                type: "POST",
                url: contextPath + "/checkUniquePaymentCycle",
                async: false,
                data: {'schemeId': $("#scheme").val(), 'paymentCycleName': $("#nameInEnglish").val(), 'paymentCycleId': $("#id").val()
                },
                success: function (msg)
                {
                    isSuccess = msg === true ? true : false;
                },
                failure: function () {
                    log("checking uniqueness of payment cycle failed!!");
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log('error=' + xhr);
                    console.log('error=' + thrownError);
                }
            });
            return isSuccess;
        });

        console.log('endYearOfFY=' + endYearOfFY);
        console.log('now=' + new Date());
        var currentDate = new Date();
        var currentMonth = currentDate.getMonth() + 1;
        var currentYear = currentDate.getFullYear() + 1;

        var month = monthDiff(new Date(startYearOfFY, 06, 1),
                currentDate);

        console.log('currentMonth = ' + currentMonth);

        $('#startDate').MonthPicker({StartYear: startYearOfFY, Button: false, MonthFormat: 'mm-yy', OnAfterChooseMonth: function (selectedDate) {
                if (selectedLocale == 'bn') {
                    $("#" + this.id).val(getNumberInBangla($("#" + this.id).val()));
                }
                generatePaymentCycleName(this);
            }});
        $('#endDate').MonthPicker({StartYear: startYearOfFY, Button: false, MonthFormat: 'mm-yy', OnAfterChooseMonth: function (selectedDate) {
                if (selectedLocale == 'bn') {
                    $("#" + this.id).val(getNumberInBangla($("#" + this.id).val()));
                }
                generatePaymentCycleName(this);
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

        $("#paymentCycleForm").validate({
            rules: {// checks NAME not ID
                "scheme.id": {
                    required: true
                },
                "fiscalYear.id": {
                    required: true
                },
                "startDate": {
                    required: true,
                    checkPaymentCycleSequence: true
                },
                "endDate": {
                    required: true,
                    checkPaymentCycleSequence: true
                },
                "order": {
                    required: true
                },
                "nameInEnglish": {
                    required: true,
                    uniquePaymentCycle: true,
                },
                "nameInBangla": {
                    required: true,
                },
                "allowanceAmount": {
                    required: true,
                }
            },
            errorPlacement: function (error, element) {
                console.log(element);
                error.insertAfter(element);
            }
        });

        if ('${paymentCycle.startDate}' != '')
        {
            $("#startDate").val((${paymentCycle.startDate.time.month+1}).pad() + "-" + ${paymentCycle.startDate.time.year+1900});

            startYearOfFY = '${paymentCycle.fiscalYear.startYear}';
            endYearOfFY = '${paymentCycle.fiscalYear.endYear}';

            console.log('edit, startYearOfFY=' + startYearOfFY + ", endYearOfFY=" + endYearOfFY);

            /* need function */
            $('#startDate').MonthPicker('option', 'StartYear', startYearOfFY);
            $('#endDate').MonthPicker('option', 'StartYear', startYearOfFY);

            var minMonth = monthDiff(new Date(startYearOfFY, 06, 1), new Date());
            minMonth += 1;
            $('#startDate').MonthPicker('option', 'MinMonth', -minMonth);
            $('#endDate').MonthPicker('option', 'MinMonth', -minMonth);

            var maxMonth = monthDiff(new Date(endYearOfFY, 05, 1), new Date());
            console.log('max = ' + maxMonth);
            maxMonth += 1;
            $('#startDate').MonthPicker('option', 'MaxMonth', -maxMonth);
            $('#endDate').MonthPicker('option', 'MaxMonth', -maxMonth);


        }
        if ('${paymentCycle.endDate}' != '')
        {
            $("#endDate").val((${paymentCycle.endDate.time.month+1}).pad() + "-" +${paymentCycle.endDate.time.year+1900});
        }

        if (selectedLocale == 'bn') {
            $("#startDate").val(getNumberInBangla($("#startDate").val()));
            $("#endDate").val(getNumberInBangla($("#endDate").val()));
            $("#allowanceAmount").val(getNumberInBangla($("#allowanceAmount").val()));
        }
        if ('${paymentCycle.parentPaymentCycle}' !== '')
        {
            $("#fiscalYearDiv").css("display", "none");
            $("#periodDiv").css("display", "none");
            $("#orderDiv").css("display", "block");
        }
        if ('${actionType}' !== 'create' && '${paymentCycle.parentPaymentCycle}' === '')
        {
            $("#parentPaymentCycle").attr("disabled", true);
        }
        $("#order").change(function () {
            var end = this.value;
            generatePaymentCycleName();
            var pcNameBn = $('#nameInBangla').val() + "-" + getNumberInBangla(end);
            var pcNameEn = $('#nameInEnglish').val() + "-" + getNumberInEnglish(end);
            $("#nameInBangla").val(pcNameBn);
            $("#nameInEnglish").val(pcNameEn);
        });
    });

    function loadParentPaymentCycle(selectObject) {
        console.log(selectObject.value);
        var schemeId = selectObject.value;
        var ajaxUrl = contextPath + "/getParentPaymentCycleIoListByScheme/" + schemeId;
        var parentPaymentCycle = $('#parentPaymentCycle');
        if (schemeId !== '')
        {
            $.ajax({
                type: "GET",
                url: ajaxUrl,
                async: false,
                dataType: "json",
                success: function (response) {
                    parentPaymentCycle.find('option').remove();
                    $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(parentPaymentCycle);
                    $.each(response, function (index, value) {
                        if (selectedLocale === 'en') {
                            $('<option>').val(value.id).text(value.nameInEnglish).appendTo(parentPaymentCycle);
                        } else if (selectedLocale === 'bn') {
                            $('<option>').val(value.id).text(value.nameInBangla).appendTo(parentPaymentCycle);
                        }
                    });
                },
                failure: function () {
                    log("loading Payment Cycle failed!!");
                }
            });
        } else
        {
            resetSelect(parentPaymentCycle);
        }
    }

    function generatePaymentCycleName(event) {

        var startDateSt = getNumberInEnglish($("#startDate").val());
        var startYear = startDateSt.substring(3, 7);
        var startMonth = startDateSt.substring(0, 2);

        var endDateSt = getNumberInEnglish($("#endDate").val());
        var endYear = endDateSt.substring(3, 7);
        var endMonth = endDateSt.substring(0, 2);

        var startDate = $("#startDate").val() !== "" ? new Date(startYear, startMonth - 1) : "";
        var endDate = $("#endDate").val() !== "" ? new Date(endYear, endMonth - 1) : "";
        if (startDate !== null && endDate !== null)
        {
            $("#nameInEnglish").val(getShortMonthNameInEnglish(startDate) + "/" + startYear.substring(2, 4) + "-" + getShortMonthNameInEnglish(endDate) + "/" + endYear.substring(2, 4));
            $("#nameInBangla").val(getShortMonthNameInBangla(startDate) + "/" + getNumberInBangla(startYear.substring(2, 4)) + "-" + getShortMonthNameInBangla(endDate) + "/" + getNumberInBangla(endYear.substring(2, 4)));
        }
    }    
    function submitForm()
    {
        var form = $("#paymentCycleForm");
        $("#startDate").val(getNumberInEnglish($("#startDate").val()));
        $("#endDate").val(getNumberInEnglish($("#endDate").val()));
        form.validate();
        if (form.valid())
        {
            $("#startDate").val("01-" + $("#startDate").val());
            var eDate = $("#endDate").val();
            var month = eDate.split("-")[0];
                var year = eDate.split("-")[1];
                var days = daysInMonth(month, year);
                eDate = days + "-" + eDate;
            $("#endDate").val(eDate);
            $("#allowanceAmount").val(getNumberInEnglish($("#allowanceAmount").val()));
//            $("#paymentCycleForm").submit();
            return true;
        } else {
            if (selectedLocale == 'bn') {
                $("#startDate").val(getNumberInBangla($("#startDate").val()));
                $("#endDate").val(getNumberInBangla($("#endDate").val()));
            }
            return false;
        }
    }
    function daysInMonth(month, year) {
        return new Date(year, month, 0).getDate();
    }
    var startYearOfFY;
    var endYearOfFY;
    function loadFiscalYearInfo(id) {
        var fyId = $("#" + id + "").val();

        $.ajax({
            type: "POST",
            url: contextPath + "/getFiscalYear",
            async: false,
            data: {'fiscalYearId': fyId},
            datatype: "json",
            success: function (result)
            {
                startYearOfFY = result;
                endYearOfFY = startYearOfFY + 1;
                console.log('startYearOfFY=' + startYearOfFY + ", endYearOfFY=" + endYearOfFY);

                $('#startDate').MonthPicker('option', 'StartYear', startYearOfFY);
                $('#endDate').MonthPicker('option', 'StartYear', startYearOfFY);

                var minMonth = monthDiff(new Date(startYearOfFY, 06, 1), new Date());
                minMonth += 1;
                $('#startDate').MonthPicker('option', 'MinMonth', -minMonth);
                $('#endDate').MonthPicker('option', 'MinMonth', -minMonth);

                var maxMonth = monthDiff(new Date(endYearOfFY, 05, 1), new Date());
                console.log('max = ' + maxMonth);
                maxMonth += 1;
                $('#startDate').MonthPicker('option', 'MaxMonth', -maxMonth);
                $('#endDate').MonthPicker('option', 'MaxMonth', -maxMonth);
            },
            failure: function () {
                log("getFiscalYear failed!!");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log('error=' + xhr);
                console.log('error=' + thrownError);
            }
        });
    }
    function loadPaymentCycleInfo(id) {
        var pcId = $("#" + id + "").val();
        if (pcId === "")
        {
            //   $('#fiscalYear').attr("disabled", false);
            $("#fiscalYear").prop("selectedIndex", 0);
            $("#startDate").removeAttr("readonly");
            $("#endDate").removeAttr("readonly");
            $("#startDate").val("");
            $("#endDate").val("");
            $("#nameInBangla").val("");
            $("#nameInEnglish").val("");
            $("#fiscalYearDiv").css("display", "block");
            $("#periodDiv").css("display", "block");
            $("#orderDiv").css("display", "none");
        }
        $.ajax({
            type: "POST",
            url: contextPath + "/getPaymentCycle",
            async: false,
            data: {'paymentCycleId': pcId},
            datatype: "json",
            success: function (result)
            {
                $("#fiscalYearDiv").css("display", "none");
                $("#periodDiv").css("display", "none");
                $("#orderDiv").css("display", "block");
                $("#order").prop("selectedIndex", 0);
                // $('#fiscalYear').attr("disabled", true);
                $("#fiscalYear").val(result.fiscalYearId);

                $("#startDate").attr("readonly", "readonly");
                $("#startDate").val(result.fromMonthYear);

                $("#endDate").attr("readonly", "readonly");
                $("#endDate").val(result.toMonthYear);

                $("#nameInBangla").val(result.cycleNameBn);
                $("#nameInEnglish").val(result.cycleNameEn);
            },
            failure: function () {
                log("getPaymentCycle failed!!");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log('error=' + xhr);
                console.log('error=' + thrownError);
            }
        });
    }
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/paymentCycle/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/paymentCycle/edit/${paymentCycle.id}" />
    </c:otherwise>
</c:choose>

<form:form id="paymentCycleForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="paymentCycle">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.paymentCycle" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/paymentCycle/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">            
            <button type="submit" name="save" class="btn bg-blue" onclick="return submitForm()">
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
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="paymentCycle.label.scheme" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="scheme.id"  id="scheme" autofocus="autofocus" onchange="loadParentPaymentCycle(this)"> 
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${schemeList}" itemValue="id" itemLabel="${schemeName}"></form:options> 
                            </form:select> 
                            <form:errors path="scheme.id" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="paymentCycle.label.parentName" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="parentPaymentCycle.id" id="parentPaymentCycle" onchange="loadPaymentCycleInfo(this.id)" autofocus="autofocus"> 
                                <form:option value="" label="${select}"></form:option>                                
                            </form:select> 
                            <form:errors path="parentPaymentCycle.id" cssClass="error"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group" id="fiscalYearDiv">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="paymentCycle.label.fiscalYear" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="fiscalYear.id"  id="fiscalYear" onchange="loadFiscalYearInfo(this.id)" autofocus="autofocus"> 
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                            </form:select> 
                            <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group" id="periodDiv">
                            <label for="" class="col-md-4 control-label"><spring:message code="paymentCycle.label.period" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <div class="form-group row">                                
                                <span for="startDate" class="col-md-2 control-label"><spring:message code='paymentCycle.label.from'/></span>
                                <div class="col-md-4">
                                    <spring:message code='paymentCycle.label.startMonth' var="startMonth"/>                                    
                                    <form:input class="form-control" placeholder="${startMonth}" path="startDate" onchange="generatePaymentCycleName(this.id)" autofocus="autofocus"/>
                                    <form:errors path="startDate" cssStyle="color:red"></form:errors>
                                    </div>
                                    <span for="endDate" class="col-md-2 control-label"><spring:message code='paymentCycle.label.to'/></span>
                                <div class="col-md-4">
                                    <spring:message code='paymentCycle.label.endMonth' var="endMonth"/>
                                    <form:input class="form-control" placeholder="${endMonth}" path="endDate" onchange="generatePaymentCycleName(this.id)" autofocus="autofocus"/>
                                    <form:errors path="endDate" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group" id="orderDiv" style="display: none">
                            <label for="paymentcycleNameInputBangla" class="col-md-4 control-label"><spring:message code="paymentCycle.label.order" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <select name="order" id="order" class="form-control">                                
                                <option value="" label="${select}"></option>
                                <c:forEach items="${numberList}"  var="number">
                                    <option value="${number}">${number}</option>
                                </c:forEach>
                            </select>
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
                            <label for="allowanceAmount" class="col-md-4 control-label"><spring:message code="paymentCycle.label.allowanceAmount" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='paymentCycle.label.allowanceAmount' var="allowanceAmount"/>
                            <form:input class="form-control" placeholder="${allowanceAmount}" path="allowanceAmount" autofocus="autofocus" onkeydown="checkNumber(event, this)"/>
                            <form:errors path="allowanceAmount" cssStyle="color:red"></form:errors>
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


<%-- 
    Document   : applicantList
    Created on : Feb 5, 2017, 10:18:00 AM
    Author     : Philip
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<script src="<c:url value="/resources/js/unijoy.js" />" ></script>
<script src="<c:url value="/resources/js/bootbox.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/resources/js/MonthPicker.min.js"/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/MonthPicker.min.css" />">
<script type="text/javascript" src="<c:url value="/resources/js/MonthPicker-bn.js"/>" ></script>
<style>
    #applicantListTable_length {
        float:left;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    } 
</style>
<script>
    var urlLang="";
    var startYearOfFY;
    var endYearOfFY;
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $('#applicationId').keypress(function (e) {
            if (e.which == 13) {
                $("#buttonSearch").click();
                return false;    //<---- Add this line
            }
        });
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
            makeUnijoyEditor('applicantId'); // NID actually
            makeUnijoyEditor('mobileNo'); // NID actually
            makeUnijoyEditor('accountNo'); // NID actually
        }
         var applicantId = document.getElementById("applicantId");
        applicantId.addEventListener("keydown", function (event) {
            checkNumberWithLengthWithPasteOption(event, this, 17);
        });
        var mobileNo = document.getElementById("mobileNo");
        mobileNo.addEventListener("keydown", function (event) {
            checkNumberWithLengthWithPasteOption(event, this, 11);
        });

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

        $("#pendingListForm").validate({
            rules: {// checks NAME not ID
                "fiscalYear.id": {
                    required: true
                },
                "scheme.id": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
        $('#startDate').MonthPicker({StartYear: startYearOfFY, Button: false, MonthFormat: 'mm-yy', OnAfterChooseMonth: function (selectedDate) {
                if (selectedLocale === 'bn') {
                    $("#" + this.id).val(getNumberInBangla($("#" + this.id).val()));
                }
            }});
        $('#endDate').MonthPicker({StartYear: startYearOfFY, Button: false, MonthFormat: 'mm-yy', OnAfterChooseMonth: function (selectedDate) {
                if (selectedLocale === 'bn') {
                    $("#" + this.id).val(getNumberInBangla($("#" + this.id).val()));
                }
            }});
        showModalDialog();

        $("#buttonSearch").click(function ()
        {
            loadSearchResult();
        });

        if ('${searchParameterForm.isDistrictAvailable}' === 'false')
        {
            loadDistrict('${searchParameterForm.division.id}', $('#districtId'));
        } else if ('${searchParameterForm.isUpazilaAvailable}' === 'false')
        {
            loadUpazilla('${searchParameterForm.district.id}', $('#upazilaId'));
        } else if ('${searchParameterForm.isUnionAvailable}' === 'false')
        {
            loadUnion('${searchParameterForm.upazila.id}', $('#unionId'));
        }
    });

    function loadSearchResult() {
        var urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }

        var form = $('#pendingListForm');
        form.validate();
        if (form.valid()) {
            var serializedData = $("#pendingListForm").serialize();
            console.log(serializedData);
            $('#applicantListTable').DataTable().destroy();
            var path = '${pageContext.request.contextPath}';
            var sDate = getNumberInEnglish($("#startDate").val());
            var eDate = getNumberInEnglish($("#endDate").val());
            if (sDate !== '')
            {
                sDate = "01-" + sDate;
            }
            if (eDate !== '')
            {
                var month = eDate.split("-")[0];
                var year = eDate.split("-")[1];
                var days = daysInMonth(month, year);
                eDate = days + "-" + eDate;
            }
            $('#applicantListTable').DataTable({
                "processing": true,
                "pageLength": 10,
                "serverSide": true,
                "bSort": false,
                "pagingType": "full_numbers",
                "dom": '<"top"i>rt<"bottom"lp><"clear">',
                "language": {
                    "url": urlLang
                },
                "columnDefs": [
//                        {"width": "110", "targets": [5]},
                    {className: "text-left", "targets": [0]}
                ],
                "ajax": {
                    "url": path + "/beneficiary/list",
                    "type": "POST",
                    "data": {
                        "fiscalYear": $("#fiscalYear").val(),
                        "scheme": $("#scheme").val(),
                        "applicantId": $("#applicantId").val(),
                        "mobileNo": $("#mobileNo").val(),
                        "accountNo": $("#accountNo").val(),
                        "bgmeaFactoryId": $("#bgmeaFactoryId").val(),
                        "applicantType": $("#applicantType").val(),
                        "startDate": sDate,
                        "endDate": eDate
                    }
                },
                "fnDrawCallback": function (oSettings) {
                    showModalDialog();
                    if (selectedLocale === 'bn')
                    {
                        localizeBanglaInDatatable("applicantListTable");
                    }
                }
            });
        }
    }
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
    function daysInMonth(month, year) {
        return new Date(year, month, 0).getDate();
    }
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
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#unionId');

        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId);
        } else {
            resetSelect(unionSelectId);
        }
    }

</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:url var="formAction" value="/page"></c:url>
    <section class="content-header clearfix">
        <h1 class="pull-left">
        <spring:message code="label.beneficiaryDataUpdate" />
        <small></small>
    </h1>   
</section>
<section class="content">    
    <div class="row">
        <div class="col-xs-12">
            <form:form id="pendingListForm" class="form-horizontal" modelAttribute="searchParameterForm">
                <form:hidden path="applicantType" id="applicantType"/>
                <div class="form-group">
                    <div class="col-md-6">                       
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="fiscalYear.id"  id="fiscalYear" onchange="loadFiscalYearInfo(this.id)">  
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                                </form:select> 
                                <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                                <div class="form-group" id="periodDiv">
                                <label for="" class="col-md-4 control-label"><spring:message code="label.monthPeriod" /></label>
                            <div class="col-md-8">
                                <div class="form-group row">                                
                                    <span for="startDate" class="col-md-2 control-label"><spring:message code='paymentCycle.label.from'/></span>
                                    <div class="col-md-4">
                                        <spring:message code='paymentCycle.label.startMonth' var="startMonth"/>                                    
                                        <form:input class="form-control" placeholder="${startMonth}" path="startDate" />
                                        <form:errors path="startDate" cssStyle="color:red"></form:errors>
                                        </div>
                                        <span for="endDate" class="col-md-2 control-label"><spring:message code='paymentCycle.label.to'/></span>
                                    <div class="col-md-4">
                                        <spring:message code='paymentCycle.label.endMonth' var="endMonth"/>
                                        <form:input class="form-control" placeholder="${endMonth}" path="endDate"/>
                                        <form:errors path="endDate" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                </div>
                            </div>   
                            <div class="form-group">
                                <label for="status" class="col-md-4 control-label"><spring:message code='label.nid' var="nid" />${nid}</label>
                            <div class="col-md-8">
                                <input type="text" id="applicantId" name="applicantId" class="form-control" placeholder="${nid}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="status" class="col-md-4 control-label"><spring:message code='label.mobileNo' var="mobileNo" />${mobileNo}</label>
                            <div class="col-md-8">
                                <input type="text" id="mobileNo" name="mobileNo" class="form-control" placeholder="${mobileNo}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="status" class="col-md-4 control-label"><spring:message code='label.accountNo' var="accountNo" />${accountNo}</label>
                            <div class="col-md-8">
                                <input type="text" id="accountNo" name="accountNo" class="form-control" placeholder="${accountNo}">
                            </div>
                        </div>
                        <div class="form-group">
                            &nbsp;
                        </div>
                    </div>
                    <div class="col-md-6">                                                                                                   
                        <div class="form-group" id="bgmeaBlock">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bgmea" /></label>
                            <c:choose>
                                <c:when test="${searchParameterForm.isBgmeaFactoryAvailable eq 'false'}">
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="bgmeaFactory.id" id="bgmeaFactoryId">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${bgmeaFactoryList}" itemValue="id" itemLabel="${bgmeaFactoryName}"></form:options> 
                                        </form:select>
                                        <form:errors path="bgmeaFactory.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-8 labelAsValue">
                                        <form:hidden path="bgmeaFactory.id" id="bgmeaFactoryId"/>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                ${searchParameterForm.bgmeaFactory.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${searchParameterForm.bgmeaFactory.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>     
                        </div>   
                        <div class="form-group">
                            <div class="col-md-4"></div>
                            <div class="col-md-8">
                                <div>
                                    <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>   
    </div>  
    <div class="row">
        <div class="col-xs-12">  
            <div class="box">                
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="applicantListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <!--<th><spring:message code="label.photo" /></th>-->
                                    <th><spring:message code="label.nameBn" /></th>
                                    <th><spring:message code="label.nameEn" /></th>
                                    <th><spring:message code="label.nid" /></th>
                                    <th><spring:message code="label.dob" /></th>
                                    <th><spring:message code="label.mobileNo" /></th>
                                    <th><spring:message code="label.accountNo" /></th>
                                    <th><spring:message code="label.approvedBy" /></th>  
                                    <th><spring:message code="label.approvedDate" /></th>                                     
                                    <!--<th><spring:message code="edit" var="tooltipEdit"/></th>-->
                                    <th><spring:message code="view" var="tooltipView"/></th>
                               <th><spring:message code="label.Profile"/></th>
                                
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

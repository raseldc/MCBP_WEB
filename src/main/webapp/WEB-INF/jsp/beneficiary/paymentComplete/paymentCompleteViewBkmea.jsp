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
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:url var="formAction" value="/page"></c:url>
    <section class="content-header clearfix">
        <h1 class="pull-left">
        <spring:message code="label.paymentInformationUpdate" />
        <small></small>
    </h1>   
</section>
<section class="content">    
    <div class="row">
        <div class="col-xs-12">
            <form:form target="_blank" action="${contextPath}/beneficiary-payment-complete-report" id="paymentCompleteUnionForm" class="form-horizontal" modelAttribute="searchParameterForm">
                <form:hidden path="applicantType" id="applicantType"/>
                <div class="form-group">
                    <div class="col-md-6">    

                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="fiscalYear.id"  id="fiscalYear" onchange="loadAllPaymentCycle(this)">  
                                    <form:option value="0" label="${select}"></form:option>
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
                                    <form:option value="0" label="${select}"></form:option>                            
                                </form:select> 
                                <form:errors path="paymentCycle.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="status" class="col-md-4 control-label"><spring:message code='label.nid' var="nid" />${nid}</label>
                            <div class="col-md-8">
                                <form:input type="text" id="tbNid" name="nid" path="nid" class="form-control" placeholder="${nid}"></form:input>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="status" class="col-md-4 control-label"><spring:message code='label.mobileNo' var="mobileNo" />${mobileNo}</label>
                            <div class="col-md-8">
                                <form:input type="text" id="mobileNo" name="mobileNo" path="mobileNo"  class="form-control" placeholder="${mobileNo}"></form:input>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="status" class="col-md-4 control-label"><spring:message code='label.accountNo' var="accountNo" />${accountNo}</label>
                            <div class="col-md-8">
                                <form:input type="text" id="accountNo" name="accountNo" path="accountNumber" class="form-control" placeholder="${accountNo}"></form:input>
                                </div>
                            </div>
                            <div class="form-group">
                                &nbsp;
                            </div>
                        </div>
                        <div class="col-md-6">                                                                                                   
                            <div class="form-group" id="bkmeaBlock">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bkmea" /></label>
                            <c:choose>
                                <c:when test="${searchParameterForm.isBkmeaFactoryAvailable eq 'false'}">
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="bkmeaFactory.id" id="bkmeaFactoryId">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${bkmeaFactoryList}" itemValue="id" itemLabel="${bkmeaFactoryName}"></form:options> 
                                        </form:select>
                                        <form:errors path="bgmeaFactory.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-8 labelAsValue">
                                        <form:hidden path="bkmeaFactory.id" id="bkmeaFactoryId"/>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                ${searchParameterForm.bkmeaFactory.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${searchParameterForm.bkmeaFactory.nameInEnglish}
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
                                    <spring:message code="viewReport" var="viewReport"/>
                                    <button type="button" class="btn bg-blue" onClick="return submitForm()"><i class="fa fa-file" aria-hidden="true"></i>${viewReport}</button> 

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
                <div class="box-header">
                    <h3 class="box-title"><spring:message code="label.BeneficiaryList" /></h3>
                </div>
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="applicantListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th><spring:message code="label.nameBn" /></th>
                                    <th><spring:message code="label.nameEn" /></th>
                                    <th><spring:message code="label.nid" /></th>                                    
                                    <th><spring:message code="label.mobileNo" /></th>
                                    <!--<th><spring:message code="label.accountName" /></th>-->     
                                    <th><spring:message code="label.accountNo" /></th>    
                                    <th><spring:message code="label.paymentCycle" /></th>    
                                    <th><spring:message code="label.edit" /></th>


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
<button type="button" id="btnModal" style="display: none" class="btn btn-primary" data-toggle="modal" data-target="#modaItem"></button>

<div class="modal fade bd-example-modal-md" id="modaItem" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="modalBody">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="btn_close_item_modal"><spring:message code="close"/></button>

            </div>
        </div>
    </div>
</div>  
<script type="text/javascript">
    var urlLang = "";
    var startYearOfFY;
    var endYearOfFY;
    var baseUrl = '${contextPath}';

    function accountTypeChange() {
        $("#paymentCycle").removeAttr('disabled');
        if ($("#ddAccountType :selected").val() == 1)
        {
            $("#paymentCycle").val("");
            $("#paymentCycle").attr("disabled", "disabled");

        }
    }
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $('#tbNid').keypress(function (e) {
            if (e.which == 13) {
                $("#buttonSearch").click();
                return false; //<---- Add this line
            }
        });
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
            makeUnijoyEditor('tbNid'); // NID actually
            makeUnijoyEditor('mobileNo'); // NID actually
            makeUnijoyEditor('accountNo'); // NID actually
        }
        var applicantId = document.getElementById("tbNid");
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

        if ($("#ddAccountType :selected").val() == 0)
        {
            $("#modalBody").html("অ্যাকাউন্ট ধরন নির্বাচন করুন");
            $("#btnModal").click();
            return;
        }

        var serchInfo = {
            "fiscalYear": $("#fiscalYear").val(),
            "scheme": $("#scheme").val(),
            "mobileNo": getNumberInEnglish($("#mobileNo").val()),
            "accountNo": $("#accountNo").val(),
            "divisionId": $("#divisionId :selected").val(),
            "districtId": $("#districtId").val() == "" ? 0 : $("#districtId").val(),
            "upazilaId": $("#upazilaId").val() == "" ? 0 : $("#upazilaId").val(),
            "unionId": $("#unionId").val() == "" ? 0 : $("#unionId").val(),
            "wardNo": $("#ddWard").val() == "" ? 0 : $("#ddWard").val(),
            "villageId": $("#village").val() == "" ? 0 : $("#village").val(),
            "accountType": $("#ddAccountType :selected").val(),
            "nid": getNumberInEnglish($("#tbNid").val().toString()),
            "mobile": $("#mobileNo").val(),
            "accountNumber": $("#accountNo").val() == "" ? "" : getNumberInEnglish($("#accountNo").val().toString()),
            "paymentCycle": $("#paymentCycle :selected").val() == "" ? 0 : $("#paymentCycle :selected").val(),
            "applicantTypeStr": $("#applicantType").val()

        }




        $('#applicantListTable').DataTable().destroy();
        var table = $('#applicantListTable').DataTable({
            "processing": true,
            "pageLength": 10,
            "serverSide": true,
            "bSort": false,
            "pagingType": "full_numbers",
            "dom": '<"top"i>rt<"bottom"lp><"clear">',
            "language": {
                "url": urlLang
            },
            "ajax": baseUrl + "/beneficiary/payment-complete-view-data-table?searchInfo=" + encodeURIComponent(JSON.stringify(serchInfo)),

            "columns": [
                {"data": function (data) {

                        return data.benNameBn;
                    }},
                {"data": function (data) {

                        return data.benNameEn;
                    }},
                {"data": function (data) {
                        if (data.nid == undefined)
                            return "";
                        var nid = selectedLocale === "bn" ? getNumberInBangla(data.nid.toString()) : data.nid;
                        return nid;
                    }},
                {"data": function (data) {
                        if (data.mobile == undefined)
                            return "";
                        var mobile = selectedLocale === "bn" ? getNumberInBangla(data.mobile.toString()) : data.mobile;
                        return mobile;
                    }},
//                    {"data": function (data) {
//
//                            return data.accountName;
//                        }},
                {"data": function (data) {
                        if (data.accountNumber == undefined)
                            return "";
                        var accountNumber = selectedLocale === "bn" ? getNumberInBangla(data.accountNumber.toString()) : data.accountNumber;
                        return accountNumber;
                    }},
                {"data": function (data) {
                        if (data.lastPaymentCycleDate == undefined)
                            return "";
                        var accountNumber = selectedLocale === "bn" ? getNumberInBangla(data.lastPaymentCycleDate.toString()) : data.lastPaymentCycleDate;
                        return accountNumber;
                    }},
                {"data": function (data) {

                        var url = baseUrl + '/beneficiary/paymentInformationUpdate/' + data.benID;
                        var name = selectedLocale === "bn" ? "সম্পাদনা করুন" : "Edit";


                        return  "<a href='" + url + "'  id='btnSelect' value='Edit' title='প্রোফাইল' onclick=''><span class='glyphicon glyphicon-edit'></span></a>"
                    }}


            ],
            "fnDrawCallback": function (oSettings) {
                // showModalDialog();
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("applicantListTable");
                }
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
    function loadPresentVillageList(selectObject) {
        var wardNo = getNumberInEnglish(selectObject.value);
        var unionId = $('#unionId').val();
        var villageSelectId = $('#village');
        if (unionId !== '' && wardNo !== '') {
            loadVillage(unionId, wardNo, villageSelectId, '${beneficiaryForm.permanentVillage.id}');
        } else {
            resetSelect(villageSelectId);
        }
    }

    function loadAllPaymentCycle(selectObject) {
        console.log(typeof selectObject);
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
        ajaxUrl = contextPath + "/getActivePaymentCycle/" + fiscalYearId + "/1";
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
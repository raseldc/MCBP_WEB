<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<meta name="viewport" content="width=device-width,initial-scale=1.0" />

<style type="text/css">
    .yellow {
        background-color: #FFF1C8
    }
    div.container {
        width: 80%;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    } 
</style>
<script>
    $(document).ready(function () {  //"select all" change 
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        var urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }
        $(".accepted").keyup(function (e) {
            if (e.which == 13) {
                $("#buttonPrioritize").click();
            }
        });
        var quotaRemaining = 0;

        $("#prioritizedListForm").validate({
            rules: {// checks NAME not ID
                "bgmeaFactory.id": {
                    required: true
                },
                "bkmeaFactory.id": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
        $("#applicantListTable").hide();
        $(document).on('icheck', function () {
            $('input[type=checkbox]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            });
        }).trigger('icheck'); // trigger it for page load.
        $("#reasonSubmission").hide();
        $("#buttonPrint").hide();
        if ('${searchParameterForm.isUnionAvailable}' === 'true')
        {
            $("#unionId").val('${searchParameterForm.union.id}');
            if ("${pageContext.response.locale}" === "bn")
            {
                $("#unionName").val('${searchParameterForm.union.nameInBangla}');
            } else
            {
                $("#unionName").val('${searchParameterForm.union.nameInEnglish}');
            }

        }
        if ('${searchParameterForm.isDivisionAvailable}' === 'false')
        {
            loadDivision($('#divisionId'), '');
        } else if ('${searchParameterForm.isDistrictAvailable}' === 'false')
        {
            loadDistrict('${searchParameterForm.division.id}', $('#districtId'));
        } else if ('${searchParameterForm.isUpazilaAvailable}' === 'false')
        {
            loadUpazilla('${searchParameterForm.district.id}', $('#upazilaId'));
        } else if ('${searchParameterForm.isUnionAvailable}' === 'false')
        {
            loadUnion('${searchParameterForm.upazila.id}', $('#unionId'));
        }
//        if (selectedLocale === 'bn') {
//            $('#schemeAttributeList  tr').not(":first").each(function () {
//                $(this).find("td").eq(1).html(getNumberInBangla($(this).find("td").eq(1).html()));
//                $(this).find("td").eq(3).html(getNumberInBangla($(this).find("td").eq(3).html()));
//            });
//        }
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
            loadFormFromCookie($('#prioritizedListForm'));
        }
    });
    function askApproveSubmit(submitButton)
    {
        var approvalMessage;
        if ($(submitButton).val() == "accept")
        {
            if (selectedLocale === 'bn') {
                approvalMessage = "<p>সতর্কতা!!! তথ্য রেকর্ড রাখতে প্রিন্ট এবং সংরক্ষণ করা অবশ্যক</p> আবেদনকারীকে মাঠ পর্যায়ে যাচাইয়ে পাঠাতে চান?";
            } else
            {
                approvalMessage = "<p>Warning!!! To keep record you must PRINT and SAVE the list</p> Are you sure you still want to Forward The Applicant(s) For Field Verification?";
            }
        } else if ($(submitButton).val() == "finalize")
        {
            if (selectedLocale === 'bn') {
                approvalMessage = "আবেদনকারীকে NID ভেরিফিকেশনের জন্য পাঠাতে চান?";
            } else
            {
                approvalMessage = "Are you sure you want to Forward The Applicant(s) For NID Verification?";
            }
        } else if ($(submitButton).val() == "print")
        {
            if (selectedLocale === 'bn') {
                approvalMessage = "আপনি কি প্রিন্ট করতে চান?";
            } else
            {
                approvalMessage = "Do you want to print?";
            }
        }

        bootbox.confirm({
            message: approvalMessage,
            buttons: {
                confirm: {
                    label: '<spring:message code="button.yes" />',
                    className: 'btn-success'
                },
                cancel: {
                    label: '<spring:message code="button.no" />',
                    className: 'btn-danger'
                }
            },
            callback: function (result)
            {
                if (result === true)
                {
                    var type = $("#applicantType").val();
                    if (type === 'BGMEA')
                    {
                        $("#selectedFactoryId").val($("#bgmeaFactoryId").val());
                        $("#selectedFactoryIdPrint").val($("#bgmeaFactoryId").val());
                    } else
                    {
                        $("#selectedFactoryId").val($("#bkmeaFactoryId").val());
                        $("#selectedFactoryIdPrint").val($("#bkmeaFactoryId").val());
                    }
                    $("#selectedApplicantType").val($("#applicantType").val());
                    $("#selectedApplicantTypePrint").val($("#applicantType").val());
                    saveFormToCookie($('#prioritizedListForm'));
                    if ($(submitButton).val() == "accept")
                    {
                        $("#buttonApprove_submit").click();
                    } else if ($(submitButton).val() == "finalize")
                    {
                        $("#buttonFinalize_submit").click();
                    } else if ($(submitButton).val() == "print")
                    {
                        $("#buttonPrint_submit").click();
                    }
                } else
                {
                    if ($(submitButton).val() == "accept")
                    {
                        var type = $("#applicantType").val();
                        if (type === 'BGMEA')
                        {
                            $("#selectedFactoryId").val($("#bgmeaFactoryId").val());
                            $("#selectedFactoryIdPrint").val($("#bgmeaFactoryId").val());
                        } else
                        {
                            $("#selectedFactoryId").val($("#bkmeaFactoryId").val());
                            $("#selectedFactoryIdPrint").val($("#bkmeaFactoryId").val());
                        }
                        $("#selectedApplicantTypePrint").val($("#applicantType").val());
                        $("#buttonPrint_submit").click();
                    }
                    return true;
                }
            }
        });
    }

    function redrawDatatable()
    {
        var form = $('#prioritizedListForm');
//        var oTable = $('#applicantListTable').dataTable();
        form.validate();
        if (form.valid()) {
            // saveFormToCookie(form);
            var path = '${pageContext.request.contextPath}';
            var urlLang = "";
            if ("${pageContext.response.locale}" === 'bn') {
                urlLang = path + "/dataTable/localization/bangla";
            }
            $("#applicantListTable").show();
            $('#applicantListTable').DataTable().destroy();
            var path = '${pageContext.request.contextPath}';
            var oTable = $('#applicantListTable').DataTable({
                "processing": true,
                "pageLength": 10000,
                "bSort": false,
//                "responsive": true,               
                "serverSide": true,
                "pagingType": "full_numbers",
                "dom": '<"top"i>rt<"bottom"lp><"clear">',
                "ajax": {
                    "url": path + "/prioritization/list",
                    "type": "POST",
                    "data": {
                        "applicantType": $('#applicantType').val(),
                        "bgmeaFactoryId": $("#bgmeaFactoryId").val(),
                        "bkmeaFactoryId": $("#bkmeaFactoryId").val()
                    }
                },
                "language": {
                    "url": urlLang
                },
                "order": [],
                "columnDefs": [
                    {className: "text-left", "targets": [0]}
                ],
                "fnDrawCallback": function (oSettings) {
                    showModalDialog();
                    localizeBanglaInDatatable("applicantListTable");
                    $("#applicantListTable_info").hide();
                    $("#applicantListTable_paginate").hide();
                    $("#applicantListTable_length").hide();
                    $("#applicantListTable_filter").hide();

//                  Need to uncomment above for quota checking. temporary commented for testing and added below line
                    $("#reasonSubmission").show();
                    if (this.fnSettings().fnRecordsTotal() > 0) {
                        $("#buttonPrint").show();
                        $("#buttonAccept").show();
                    } else {
                        $("#buttonPrint").hide();
                        $("#buttonAccept").hide();
                    }
                    $(document).trigger('icheck');


                }

            });
        }
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
<style type="text/css">        
</style>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="actionUrl" value="${contextPath}/prioritization" />

<input type="hidden" id="unionName" name="unionName">

<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.prioritization" />
    </h1>
</section>
<section class="content">
    <div class="row">
        <div class="col-md-6">
            <div class="form-group">
                <div class="col-md-12">                    
                    <form:form id="prioritizedListForm" class="form-horizontal" modelAttribute="searchParameterForm">
                        <form:hidden path="applicantType" id="applicantType"/>                        
                        <c:if test="${type eq 'bgmea'}">
                            <div class="form-group" id="bgmeaBlock">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bgmea" /><span class="mandatory">*</span></label>
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
                                            <form:hidden id="bgmeaFactoryId" path="bgmeaFactory.id"/>
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
                        </c:if>
                        <c:if test="${type eq 'bkmea'}">
                            <div class="form-group" id="bkmeaBlock">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bkmea" /><span class="mandatory">*</span></label>
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
                                            <form:hidden id="bkmeaFactoryId" path="bkmeaFactory.id"/>
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
                        </c:if>
                        <div class="form-group">
                            <label class="col-md-4"></label> 
                            <div class="col-md-2">
                                <button type="button" id="buttonPrioritize" class="btn bg-blue" onclick="redrawDatatable()"><spring:message code="button.prioritize"/></button>
                            </div>
                            <div id="quotaInfo"></div>
                        </div>                            
                    </div>
                </form:form>
            </div>            
        </div>        
    </div>
    <div class="row">
        <div class="col-xs-12">                                     
            <div class="box">
                <form:form action="${actionUrl}" method="post" class="form-horizontal" role="form">
                    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
                    <input type="hidden" id="selectedApplicantType" name="selectedApplicantType">
                    <input type="hidden" id="selectedFactoryId" name="selectedFactoryId">
                    <input type="submit" style="display: none" id="btnSearch" value="Submit" />
                    <!--            <div class="box" style="display: table" width="100%">-->
                    <div class="box-header">
                        <h3 class="box-title"><spring:message code="label.newBeneficiaryList" /></h3>
                    </div>

                    <div class="box-body">
                        <table id="applicantListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>                                   
                                    <th><spring:message code="label.nameBn" /></th>
                                    <th><spring:message code="label.nameEn" /></th>
                                    <th><spring:message code="label.nid" /></th>
                                    <th><spring:message code="label.dob" /></th>
                                    <th><spring:message code="label.mobileNo" /></th>
                                    <!--<th><spring:message code="dashboard.applicantStatus" /></th>-->
                                    <th><spring:message code="label.address" /></th>
                                    <th><spring:message code="label.systemRecommendedStatus" /></th>
                                    <th><spring:message code="view" var="tooltipView"/></th>    
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div> 

                </div>
            </div> 
        </div>
        <button class="btn bg-green" id="buttonPrint" type="button" style="float:right" name="action" value="print" onclick="return askApproveSubmit(this);"><spring:message code="print" /></button>                        
        <div id="reasonSubmission">
            <button class="btn bg-blue" style="float:left" type="button" id="buttonAccept" name="action" value="accept" onclick="return askApproveSubmit(this);"><spring:message code="button.sendToFieldVerification" /></button>
            <button id="buttonApprove_submit" type="submit" style="display: none" name="action" class="btn bg-blue" value="accept" >
                <spring:message code="accept" />
            </button>           
        </div> </br>               
    </form:form>
    <form:form action="${actionUrl}" method="post" target="_blank" class="form-horizontal" role="form">
        <c:set var="contextPath" value="${pageContext.request.contextPath}" />
        <input type="hidden" id="selectedApplicantTypePrint" name="selectedApplicantTypePrint">
        <input type="hidden" id="selectedFactoryIdPrint" name="selectedFactoryIdPrint">
        <div id="reasonSubmissionPrint">&nbsp;        
            <button id="buttonPrint_submit" class="btn bg-green" type="submit" style="display: none" name="action" value="print">
                <spring:message code="print" />
            </button>
        </div>
    </form:form>
</section>




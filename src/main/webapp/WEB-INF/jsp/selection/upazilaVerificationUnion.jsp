<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style type="text/css">
    /* label {
            width: 100%;
    } */

    .nopadding{
        padding: 0px!important;
        margin: 0!important;
    }
    .col-md-2 {
        width: 12.66666667%;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    }
    #applicantListTable_length {
        float:left;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    }
</style>
<script>
    var ancVerificationRespose;
    function loadUnionId() {
        $("#unionIdHidden").val($("#unionId").val());
    }
    function ancAccept(status) {
        ancVerificationRespose.status = status
        var ancVerificationResposeData = JSON.stringify(ancVerificationRespose);
        $.ajax(
                {
                    type: 'POST',
                    url: "${Contexpath}/anc-update",
                    contentType: 'application/json; charset=utf-8',
                    data: ancVerificationResposeData,
                    dataType: 'json',

                    success: function (data) {
                        console.log("Data ", data)
                        $("#btnAncModal").click();
                        $("#buttonSearch").click();



                    }
                });
    }
    function ancCardCheck(id) {

        $.ajax({
            type: "GET",
            url: contextPath + "/anc-comparison/" + id,
            async: true,
//        dataType: "json",
            success: function (response) {
                ancVerificationRespose = response;
                $("#btnAncModal").click();
                $("#lbAncName").html(ancVerificationRespose.ancInformationDetail.name);
                $("#lbAncFatherName").html(ancVerificationRespose.ancInformationDetail.fatherName);
                $("#lbAncMotherName").html(ancVerificationRespose.ancInformationDetail.motherName);
                $("#lbAncHusbandName").html(ancVerificationRespose.ancInformationDetail.husbandName);
                $("#lbAncConceptionDuration").html(ancVerificationRespose.ancInformationDetail.pregnancyWeek);


                $("#lbApplName").html(ancVerificationRespose.applicantDetail.fullNameInBangla);
                $("#lbApplFatherName").html(ancVerificationRespose.applicantDetail.fatherName);
                $("#lbApplMotherName").html(ancVerificationRespose.applicantDetail.motherName);
                $("#lbApplHusbandName").html(ancVerificationRespose.applicantDetail.spouseName);
                $("#lbApplConceptionDuration").html(ancVerificationRespose.applicantDetail.conceptionDuration);

                return response;
            },
            failure: function () {
                log("loading scheme failed!!");
                return "error in loading data";
            }
        });
    }
    $(document).ready(function () {  //"select all" change 
        var path = '${pageContext.request.contextPath}';
//        alert("${searchParameterForm.isUnionAvailable}");
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
            makeUnijoyEditor('nid');
            makeUnijoyEditor('reasonFV');
        }
        var nid = document.getElementById("nid");
        nid.addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 17);
        });
//        $("#applicantListTable thead>tr").prepend('<th><input id=\"select_all\"  type=\"checkbox\"></th>');
        $('#nid').keypress(function (e) {
            if (e.which == 13) {
                $("#buttonSearch").click();
                return false;    //<---- Add this line
            }
        });
        $("#buttonPrint").hide();
        if ('${searchParameterForm.isUnionAvailable}' === 'true')
        {
            $("#unionId").val('${searchParameterForm.union.id}');
        }
        $("#fieldVerificationForm").validate({
            rules: {// checks NAME not ID
                "division.id": {
                    required: true
                },
                "district.id": {
                    required: true
                },
                "upazila.id": {
                    required: true
                },
                "union.id": {
                    required: true
                },
                "reasonFV": {
                    maxlength: 255
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
//        $(function () {
//        });
        $("#buttonSearch").click(function ()
        {
            var form = $('#fieldVerificationForm');
            form.validate();
            if (form.valid())
            {
                saveFormToCookie(form);
                $('#applicantListTable').DataTable().destroy();
                var path = '${pageContext.request.contextPath}';
                $('#applicantListTable').DataTable({
                    "processing": true,
                    "bSort": false,
                    "serverSide": true,
                    "pagingType": "full_numbers",
                    "dom": '<"top"i>rt<"bottom"lp><"clear">',
                    "language": {
                        "url": urlLang
                    },
                    "ajax": {
                        "url": path + "/verification/list",
                        "type": "POST",
                        "data": {
                            "state": $("#state").val(),
                            "nid": $("#nid").val(),
                            "divisionId": $("#divisionId").val(),
                            "districtId": $("#districtId").val(),
                            "upazilaId": $("#upazilaId").val(),
                            "unionId": $("#unionId").val(),
                            "applicantType": $('#applicantType').val()
                        }
                    },
                    "fnDrawCallback": function (oSettings) {
                        if (selectedLocale === 'bn')
                        {
                            localizeBanglaInDatatable("applicantListTable");
                        }
                        $("#reasonSubmission").hide();
                        if (this.fnSettings().fnRecordsTotal() > 0) {
                            $("#buttonPrint").show();
                        } else {
                            $("#buttonPrint").hide();
                        }
                        $(document).trigger('icheck');
                        $('#select_all').on('ifChanged', function (event) {
                            $('#select_all').on('ifChecked ifUnchecked', function (event) {
                                if (event.type == 'ifChecked') {
                                    $('input.checkbox').iCheck('check');
                                } else {
                                    $('input.checkbox').iCheck('uncheck');
                                }
                            });
                        });
                        $('input.checkbox').on('ifChecked', function (event) {
                            $("#reasonSubmission").show();
                            return;
                        });
                        $('input.checkbox').on('ifUnchecked', function (event) {
                            if ($('input.checkbox').filter(':checked').length == 0)
                            {
                                $("#reasonSubmission").hide();
                            }
                            return;
                        });
                        showModalDialog();
                    }
                });

                var oTable = $('#applicantListTable').dataTable();
                if ($("#state").val() === "0")
                {
                    oTable.fnSetColumnVis(0, true);
                    oTable.fnSetColumnVis(7, false);
                    $("#advanceBlock").hide();
                } else if ($("#state").val() === "1")
                {
                    getEligibility();
                    oTable.fnSetColumnVis(0, false);
                    oTable.fnSetColumnVis(7, true);
                }
            }
        });

        $(document).on('icheck', function () {
            $('input[type=checkbox]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            });
        }).trigger('icheck'); // trigger it for page load.
        $("#reasonSubmission").hide();
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


        var msg = "${message.message}";
        if (msg.length > 0)
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
            loadFormFromCookie($('#fieldVerificationForm'));
        }
    });
    function getEligibility()
    {
//        if ('${searchParameterForm.isDivisionAvailable}' === 'false')
//        {
//            $("#unionName").val($("#unionId option:selected").text());
//        }
        $.ajax({
            url: contextPath + "/checkUnionVerificationEligibility/" + $("#unionId").val(),
            type: "POST",
            async: false,
            dataType: "json",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            success: function (result) {
                if (result === true)
                {
                    $("#advanceBlock").show();
                } else
                {
                    $("#advanceBlock").hide();
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log('error=' + xhr);
                console.log('error=' + thrownError);
            }
        });
    }


    function askApproveSubmit()
    {
        var approvalMessage;
        if (selectedLocale === 'bn') {
            approvalMessage = "আপনি কি আবেদনকারীকে অনুমোদন করতে চান?";
        } else
        {
            approvalMessage = "Are you sure you want to Approve The Applicant(s)?";
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
                    var approvedApplicantList = "";
                    $('input.checkbox').each(function () {
                        if (this.checked) {
                            approvedApplicantList += this.id + ',';
                        }
                    });
                    $("#selectedApplicantType").val($("#applicantType").val());
                    $("#approvedApplicantList").val(approvedApplicantList);
                    saveFormToCookie($('#fieldVerificationForm'));
                    $("#buttonApprove_submit").click();
                } else
                {
                    return true;
                }
            }
        });
    }
    function askRejectSubmit()
    {
        var rejectMessage;
        if (selectedLocale === 'bn') {
            rejectMessage = "আপনি কি আবেদনকারীকে বাতিল করতে চান?";
        } else
        {
            rejectMessage = "Are you sure you want to Reject The Applicant(s)?";
        }
        bootbox.confirm({
            message: rejectMessage,
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
                    var rejectedApplicantList = "";
                    $('input.checkbox').each(function () {
                        if (this.checked) {
                            rejectedApplicantList += this.id + ',';
                        }
                    });
                    $("#selectedApplicantType").val($("#applicantType").val());
                    $("#rejectedApplicantList").val(rejectedApplicantList);
                    saveFormToCookie($('#fieldVerificationForm'));
                    $("#buttonReject_submit").click();
                } else
                {
                    return true;
                }
            }
        });
    }

    function askSubmit()
    {
        var approvalMessage;
        if (selectedLocale === 'bn') {
            approvalMessage = "আপনি কি আবেদনকারীদের ভাতাভোগী হিসাবে অন্তর্ভূক্ত করতে চান?";
        } else
        {
            approvalMessage = "Are you sure you want to Record the Applicant(s) as Beneficiary?";
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
                    //saveFormToCookie($('#fieldVerificationForm'));
                    $("#selectedApplicantType").val($("#applicantType").val());
                    $("#buttonAdvance_submit").click();
                } else
                {
                    return true;
                }
            }
        });
    }

    function askPrintSubmit()
    {
        var approvalMessage;
        if (selectedLocale === 'bn') {
            approvalMessage = "আপনি কি প্রিন্ট করতে চান?";
        } else
        {
            approvalMessage = "Do you want to print?";
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
                    //saveFormToCookie($('#fieldVerificationForm'));
                    $("#selectedApplicantTypePrint").val($("#applicantType").val());
                    $("#selectedUnionIdPrint").val($("#unionId").val());
                    $("#selectedStatusPrint").val($("#state").val());
                    $("#buttonPrint_submit").click();
                } else
                {
                    return true;
                }
            }
        });
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
<c:set var="actionUrl" value="${contextPath}/verification" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.upazilaVerification" />
    </h1>   
</section>
<section class="content">
    <div class="form-group">      
        <form:form id="fieldVerificationForm" class="form-horizontal" modelAttribute="searchParameterForm">
            <form:hidden path="applicantType" id="applicantType"/>
            <div class="form-group">
                <div class="col-md-6">  
                    <div class="form-group">
                        <label for="status" class="col-md-4 control-label"><spring:message code="label.status" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.status' var="statusLabel"/>
                            <select class="form-control" id="state" name="state">
                                <c:forEach var="t" items="${stateList}">   
                                    <c:if test="${pageContext.response.locale=='en'}">  
                                        <option value="${t.id}">${t.nameInEnglish}</option>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='bn'}">  
                                        <option value="${t.id}">${t.nameInBangla}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="status" class="col-md-4 control-label"><spring:message code="label.nid" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.nid' var="nid"/>
                            <input type="text" id="nid" name="nid" placeholder="${nid}" class="form-control">
                        </div>
                    </div>
                </div>
                <div class="col-md-6">                                      
                    <div id="regularBlock">
                        <div class="form-group">
                            <c:choose>
                                <c:when test="${searchParameterForm.isDivisionAvailable eq 'false'}">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
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
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
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
                            <c:choose>
                                <c:when test="${searchParameterForm.isDistrictAvailable eq 'false'}">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="district.id" id="districtId" onchange="loadPresentUpazilaList(this)">
                                            <form:option value="" label="${select}"></form:option>
                                        </form:select>
                                        <form:errors path="district.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
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
                            <c:choose>
                                <c:when test="${searchParameterForm.isUpazilaAvailable eq 'false'}">
                                    <label for="middleNameInput" class="col-md-4 control-label">
                                        <spring:message code="label.upazila" />
                                        <span class="mandatory">*</span>
                                    </label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="upazila.id" id="upazilaId" onchange="loadPresentUnionList(this)">
                                            <form:option value="" label="${select}"></form:option>
                                        </form:select>
                                        <form:errors path="upazila.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.upazila" /></label>
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
                        <div class="form-group">

                            <c:choose>
                                <c:when test="${searchParameterForm.isUnionAvailable eq 'false'}">
                                    <label for="middleNameInput" class="col-md-4 control-label">                                          
                                        <spring:message code="label.union" />                                            
                                        <span class="mandatory">*</span>
                                    </label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="union.id" id="unionId" name="unionId" onchange="loadUnionId()">
                                            <form:option value="" label="${select}"></form:option>
                                        </form:select>
                                        <form:errors path="union.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" id="unionId" name="unionId">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.union" /></label>
                                    <div class="col-md-8 labelAsValue">
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                ${searchParameterForm.union.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${searchParameterForm.union.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>                      
                    <div class="form-group">
                        <label class="col-md-4"></label> 
                        <div class="col-md-1 control-label" style="text-align: right">
                            <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                        </div>
                    </div>    
                </div>
            </div>
        </form:form>
        <form:form action="${actionUrl}" method="post" class="form-horizontal" role="form">
            <input type="hidden" id="unionIdHidden" name="unionIdHidden" value="0">
            <input type="hidden" id="approvedApplicantList" name="approvedApplicantList">
            <input type="hidden" id="rejectedApplicantList" name="rejectedApplicantList">
            <input type="hidden" id="selectedApplicantType" name="selectedApplicantType">
            <!--<input type="submit" style="display: none" id="btnSearch" value="Submit" />-->
            <c:set var="contextPath" value="${pageContext.request.contextPath}" />
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title"><spring:message code="label.newBeneficiaryList" /></h3>
                </div>
                <div class="box-body">
                    <table id="applicantListTable" class="table table-bordered table-hover" cellspacing="0" width="100%">
                        <thead>
                            <tr>
                                <th><input id="select_all"  type="checkbox"></th>
                                <th><spring:message code="label.name" /></th>
                                <th><spring:message code="label.nid" /></th>
                                <th><spring:message code="label.mobileNo" /></th>
                                <th><spring:message code="label.dob" /></th>
                                <th><spring:message code="label.presentAddress" /></th>
                                <th><spring:message code="label.recommendation"/></th>
                                <th><spring:message code="label.verification"/></th>
                                <th><spring:message code="label.systemRecommendedStatus" /></th>
                                <th><spring:message code="view" var="tooltipView"/></th>
                                <th><spring:message code="ancStatus" var="tooltipView"/></th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>       
        </div>
        <jsp:include page="../applicant/modalPage.jsp"/> 
        <button class="btn bg-green" id="buttonPrint" type="button" style="float: right" name="action" value="print" onclick="return askPrintSubmit();"><spring:message code="print" /></button>
        <div class="row">
            <div class="col-md-6" id="reasonSubmission">
                <div class="col-md-2">
                    <label class="control-label"><spring:message code="label.remarks" /></label>
                </div>
                <div class="col-md-10">
                    <textarea class="form-control"  id="reasonFV" name="reasonFV" rows="3" cols="150"></textarea>
                    <br>
                    <button class="btn bg-blue" type="button" id="buttonAccept" name="action" value="accept" onclick="return askApproveSubmit();"><spring:message code="button.approve" /></button>
                    <button id="buttonApprove_submit" type="submit" style="display: none" name="action" class="btn bg-blue" value="accept" ></button>
                    &nbsp;&nbsp;
                    <button id="buttonReject" type="button" class="btn bg-red" value="reject"  onclick="return askRejectSubmit();"><spring:message code="button.reject" /></button>
                    <button id="buttonReject_submit" type="submit" style="display: none" name="action" class="btn bg-red" value="reject" ></button>
                </div>
            </div>
            <div class="col-md-6" id="advanceBlock" style="display: none">
                <div>
                    <button class="btn bg-green" type="button" id="buttonAdvance" name="action" value="advance" onclick="return askSubmit();"><spring:message code="button.recordAsBeneficiary" /></button>                    
                    <button id="buttonAdvance_submit" type="submit" style="display: none" name="action" class="btn bg-green" value="advance" ></button>
                </div>
            </div>
        </div>
        <input type="hidden" id="unionId"
        </form:form>
        <form:form action="${actionUrl}" method="post" target="_blank" class="form-horizontal">
            <c:set var="contextPath" value="${pageContext.request.contextPath}" />
            <input type="hidden" id="selectedApplicantTypePrint" name="selectedApplicantTypePrint">
        <input type="hidden" id="selectedStatusPrint" name="selectedStatusPrint">
        <input type="hidden" id="selectedUnionIdPrint" name="selectedUnionIdPrint">
        <div id="reasonSubmissionPrint">&nbsp;            
            <button id="buttonPrint_submit" class="btn bg-green" type="submit" style="display: none" name="action" value="print">
                <spring:message code="print" />
            </button>
        </div>
    </form:form> 

    <!-- Button trigger modal -->
    <button type="button" id="btnAncModal" class="btn btn-primary" style="display: none" data-toggle="modal" data-target="#modalAnc">

    </button>
    <div class="modal fade modal-anc" id="modalAnc" tabindex="-1" role="dialog" aria-labelledby="modalAnc" aria-hidden="true" style="opacity: 1; padding: 50px 0; height: 90%">
        <div class="vertical-alignment-helper">
            <div class="modal-dialog vertical-align-center" style="width: 70%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span>

                        </button>
                        <h3 class="modal-title" id="myModalLabel">
                            <spring:message code='label.viewNewBeneficiaryPageHeader'/>
                            ${headerTitle}
                        </h3>

                    </div>
                    <div class="modal-body" style="height: 603px">
                        <div class="row col-lg-12">
                            <div class="col-lg-6">
                                <div class="form-group col-lg-12">
                                    <label class="col-md-8">Anc Information</label>
                                </div>
                                <div class="form-group col-lg-12">
                                    <label for="status" class="col-md-4 control-label"><spring:message code="label.nameBn" /></label>
                                    <div class="col-md-8">
                                        <label id="lbAncName"></label>

                                    </div>
                                </div>
                                <div class="form-group  col-lg-12">
                                    <label for="status" class="col-md-4 control-label"><spring:message code="label.fatherName" /></label>
                                    <div class="col-md-8">
                                        <label id="lbAncFatherName"></label>

                                    </div>
                                </div>

                                <div class="form-group  col-lg-12">
                                    <label for="status" class="col-md-4 control-label"><spring:message code="label.motherName" /></label>
                                    <div class="col-md-8">
                                        <label id="lbAncMotherName"></label>

                                    </div>
                                </div>
                                <div class="form-group  col-lg-12">
                                    <label for="status" class="col-md-4 control-label"><spring:message code="label.spouseName" /></label>
                                    <div class="col-md-8">
                                        <label id="lbAncHusbandName"></label>

                                    </div>
                                </div>

                                <div class="form-group  col-lg-12">
                                    <label for="status" class="col-md-4 control-label"><spring:message code="label.conceptionDuration" /></label>
                                    <div class="col-md-8">
                                        <label id="lbAncConceptionDuration"></label>

                                    </div>
                                </div>

                            </div>
                            <div class="col-lg-6">
                                <div class="form-group col-lg-12">
                                    <label class="col-md-8" ><spring:message code="dashboard.applicantInfo" /> </label>
                                </div>

                                <div class="form-group col-lg-12">
                                    <label for="status" class="col-md-4 control-label"><spring:message code="label.nameBn" /></label>
                                    <div class="col-md-8">
                                        <label id="lbApplName"></label>

                                    </div>
                                </div>
                                <div class="form-group  col-lg-12">
                                    <label for="status" class="col-md-4 control-label"><spring:message code="label.fatherName" /></label>
                                    <div class="col-md-8">
                                        <label id="lbApplFatherName"></label>

                                    </div>
                                </div>

                                <div class="form-group  col-lg-12">
                                    <label for="status" class="col-md-4 control-label"><spring:message code="label.motherName" /></label>
                                    <div class="col-md-8">
                                        <label id="lbApplMotherName"></label>

                                    </div>
                                </div>
                                <div class="form-group  col-lg-12">
                                    <label for="status" class="col-md-4 control-label"><spring:message code="label.spouseName" /></label>
                                    <div class="col-md-8">
                                        <label id="lbApplHusbandName"></label>

                                    </div>
                                </div>

                                <div class="form-group  col-lg-12">
                                    <label for="status" class="col-md-4 control-label"><spring:message code="label.conceptionDuration" /></label>
                                    <div class="col-md-8">
                                        <label id="lbApplConceptionDuration"></label>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default bg-blue" onclick="ancAccept(1)"><spring:message code="accept"/></button>
                        <button type="button" class="btn btn-default bg-blue" onclick="ancAccept(2)"><spring:message code="reject"/></button>
                        <button type="button" class="btn btn-default bg-blue" data-dismiss="modal"><spring:message code="close"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>



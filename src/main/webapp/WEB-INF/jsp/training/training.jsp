<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />"></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/bootbox.min.js"/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.ui.datepicker-bn.js"/>" ></script>

<style>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
    }
    th, td {
        padding: 5px;
        text-align: left;    
    }
</style>
<script>
    var urlLang = "";
    var path = '${pageContext.request.contextPath}';
    $(function () {

        if (${training.fileBase64!='' && training.fileBase64!=null})
            $("#photo").attr("style", "display:none");
        else
            $("#photo").attr("style", "display:block");

        if (selectedLocale === "bn")
            $("#totalAttendee").text(getNumberInBangla("" + benSet.size + ""));
        else
            $("#totalAttendee").text(benSet.size);

        $(menuSelect("${pageContext.request.contextPath}/training/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#trainingmodel-delete-confirmation");
        initDate($("#startDate"), '${dateFormat}', $("#startDate\\.icon"), selectedLocale);
        initDate($("#endDate"), '${dateFormat}', $("#endDate\\.icon"), selectedLocale);

        document.getElementById("durationDay").readOnly = true;


        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        if (selectedLocale === "bn")
        {
            makeUnijoyEditor('numberOfPerticipants');
            makeUnijoyEditor('trainingCost');
            makeUnijoyEditor('comment');
            $("#numberOfPerticipants").val(getNumberInBangla("" + $("#numberOfPerticipants").val() + ""));
            $("#trainingCost").val(getNumberInBangla("" + $("#trainingCost").val() + ""));
            $("#durationDay").val(getNumberInBangla("" + $("#durationDay").val() + ""));
            $.datepicker.setDefaults(
                    $.extend(
                            {'dateFormat': 'dd-mm-yy'},
                            $.datepicker.regional['bn-BD']
                            )
                    );
            $("#startDate").val(getNumberInBangla($("#startDate").val()));
            $("#endDate").val(getNumberInBangla($("#endDate").val()));
            urlLang = path + "/dataTable/localization/bangla";
        }

        if ('${training.divisionAvailable}' === 'false' && '${training.division.id}' === '')
        {
            loadDivision($('#divisionId'), '');
        } else if ('${training.districtAvailable}' === 'false')
        {
            if ('${training.district.id}' !== '')
            {
                loadDistrict('${training.division.id}', $('#districtId'), '${training.district.id}');
                loadUpazilla('${training.district.id}', $('#upazilaId'), '${training.upazilla.id}');
            } else
            {
                loadDistrict('${training.division.id}', $('#districtId'));
            }
        } else if ('${training.upazilaAvailable}' === 'false')
        {
            if ('${training.upazilla.id}' !== '')
            {
                loadUpazilla('${training.district.id}', $('#upazilaId'), '${training.upazilla.id}');

            } else
            {
                loadUpazilla('${training.district.id}', $('#upazilaId'));
            }
        }
        var numberOfPerticipants = document.getElementById("numberOfPerticipants");
        numberOfPerticipants.addEventListener("keydown", function (event) {
            checkNumber(event, this);
        });
        var trainingCost = document.getElementById("trainingCost");
        trainingCost.addEventListener("keydown", function (event) {
            checkDecimalWithLength(event, this, 100);
        });

        //load all beneficiary of that upazila for tagging
        if ("${actionType}" !== "create") {
            loadBeneficiaryList();
        }
        if ("${attendeeList}" !== "") {
            initializeAttendeeList();
        }

        $("#trainingForm").validate({

            rules: {// checks NAME not ID,
                "fiscalYear.id": {
                    required: true
                },
                "trainingType.id": {
                    required: true
                },
                "trainer.id": {
                    required: true
                },
                "numberOfPerticipants": {
                    required: true
                },
                "trainingCost": {
                    required: true
                },
                "startDate": {
                    required: true
                },
                "endDate": {
                    required: true
                },
                "durationDay": {
                    required: true
                },
                "comment": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });
    function PrintElem()
    {
        $("#attendeeTableDivPrint").html(document.getElementById("attendeeTableDiv").innerHTML);

        $("#lbFiscalYear").html($("#fiscalYear :selected").text());
        $("#lbTrainingType").html($("#trainingType :selected").text());
        $("#lbTrainer").html($("#trainer :selected").text());
        $("#lbTrainingVenue").html($("#trainingVenue").val());
        $("#lbNumberOfPerticipants").html($("#numberOfPerticipants").val());
        $("#lbTrainingCost").html($("#trainingCost").val());
        $("#lbStartDate").html($("#startDate").val());
        $("#lbEndDate").html($("#endDate").val());
        $("#lbDuration").html($("#durationDay").val());
        $("#lbComment").html($("#comment").val());

        $("#lbDivision").html($("#divisionId :selected").text());
        $("#lbDistrict").html($("#districtId :selected").text());
        $("#lbUpazila").html($("#upazilaId :selected").text());


        var mywindow = window.open('', 'PRINT', 'height=1600,width=3000');
        var myStyle = '<link rel="stylesheet" type="text/css"  media="all" href="<c:url value="/resources/css/bootstrap.min.css" />" >';
        var myStyle1 = '<link rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min.css" />">';
//        var myStyle2 = '<link rel="stylesheet" href="<c:url value="/resources/css/responsive.dataTables.css" />">';
//        var myStyle3 = '<link rel="stylesheet" href="<c:url value="/resources/css/custom.css" />">';
        var myStyle4 = '<link rel="stylesheet" href="<c:url value="/resources/css/table.css" />">';




        mywindow.document.write('<html><head><title>' + document.title + '</title>');
        mywindow.document.write('</head><body >');
        mywindow.document.write('<h1>' + document.title + '</h1>');
        mywindow.document.write(document.getElementById("dvPrint").innerHTML);
        mywindow.document.write('</body></html>');
        mywindow.document.write(myStyle);
        mywindow.document.write(myStyle1);
//        mywindow.document.write(myStyle2);
//        mywindow.document.write(myStyle3);
        mywindow.document.write(myStyle4);

        mywindow.document.close(); // necessary for IE >= 10
        mywindow.focus(); // necessary for IE >= 10*/                
        if (navigator.userAgent.toLowerCase().indexOf('chrome') > -1) {   // Chrome Browser Detected?
            mywindow.PPClose = false;                                     // Clear Close Flag
            mywindow.onbeforeunload = function () {                         // Before Window Close Event
                if (mywindow.PPClose === false) {                           // Close not OK?
                    return 'Leaving this page will block the parent window!\nPlease select "Stay on this Page option" and use the\nCancel button instead to close the Print Preview Window.\n';
                }
            }
            mywindow.print();                                             // Print preview
            mywindow.PPClose = true;                                      // Set Close Flag to OK.
        } else {
            mywindow.print();
            mywindow.close();
        }
           return true;
                return true;
    }
    function initializeAttendeeList() {
        $('#attendeeListTable').DataTable({
            "processing": false,
            "bSort": false,
            "serverSide": false,
            "paging": false,
            "ordering": false,
            "info": false,
            "searching": true,
            "pagingType": "full_numbers",
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === "bn")
                {
                    $("#attendeeListTable_filter input").attr('id', 'attendeeSearchId');
                    $("#attendeeSearchId").attr('type', 'text');
                    makeUnijoyEditor('attendeeSearchId');
                }
            }
        });
    }
    function updateDuration(event)
    {
        if ($('#startDate').val() != "" && $('#endDate').val() != "")
        {
            var start = $.datepicker.parseDate("dd-mm-yy", getNumberInEnglish($('#startDate').val()));
            var end = $.datepicker.parseDate("dd-mm-yy", getNumberInEnglish($('#endDate').val()));
// end - start returns difference in milliseconds 
            var diff = new Date(end - start);
            if (diff < 0)
            {
                if ("${pageContext.response.locale}" == "bn")
                {
                    bootbox.alert("<b>শুরুর তারিখ শেষের তারিখ থেকে পূর্বে হতে হবে!</b>");
                } else
                {
                    bootbox.alert("<b>Start Date should be Earlier than End Date!</b>");
                }
                document.getElementById(event).value = "";
                return;
            }
// get days
            var days = (diff / 1000 / 60 / 60 / 24) + 1;
            $("#durationDay").val(days);
            if (selectedLocale === "bn")
            {
                $("#durationDay").val(getNumberInBangla("" + days + ""));
            }
        }
    }
    function submitForm() {
        $("#selectedBenListSt").val(Array.from(benSet).join(','));
        var form = $('#trainingForm');
        form.validate();
        if (!form.valid()) {
            return false;
        }

        if (selectedLocale === "bn")
        {
            $("#trainingCost").val(getNumberInEnglish("" + $("#trainingCost").val() + ""));
            $("#startDate").val(getNumberInEnglish($("#startDate").val()));
            $("#endDate").val(getNumberInEnglish($("#endDate").val()));
        }
    }
    function loadPresentDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#districtId');
        if (divId !== '') {
            loadDistrict(divId, distSelectId);
            resetSelect($('#upazilaId'));
        } else {
            resetSelect($('#districtId'));
        }
        loadBeneficiaryList();
    }
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#upazilaId');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId);
        } else {
            resetSelect(upazillaSelectId);
        }
        loadBeneficiaryList();
    }

    function checkIfBenIncluded(tt) {
        if (benSet.size === 0) {
            prevTrainingType = $("#trainingType").val();
            var txt = $("#trainingType :selected").text();
            if (txt.indexOf("~") < 0) {
                benIncluded = false;
                document.getElementById("beneficiaryListTable").getElementsByTagName("tbody")[0].innerHTML = "";
            } else {
                benIncluded = true;
                loadBeneficiaryList();
            }
            return;
        }
        bootbox.confirm({
            message: '<spring:message code="message.tableClearWarningTrainingType" />',
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
            callback: function (feedback)
            {
                if (feedback === true) {
                    //Remove Attendee Data
                    document.getElementById("attendeeListTable").getElementsByTagName("tbody")[0].innerHTML = "";
                    benSet.clear();
                    $("#totalAttendee").text(benSet.size);


                    var txt = $("#trainingType :selected").text();
                    if (txt.indexOf("~") < 0) {
                        benIncluded = false;
                        document.getElementById("beneficiaryListTable").getElementsByTagName("tbody")[0].innerHTML = "";
                    } else {
                        benIncluded = true;
                        loadBeneficiaryList();
                    }
                    prevTrainingType = $("#trainingType").val();
                    return true;
                } else
                {
                    //set previous subject
                    $("#trainingType").val(prevTrainingType);
                    prevTrainingType = $("#trainingType").val();
                    return;
                }
            }
        });



    }
    function clearAttendeeOnChange() {

        if (benSet.size == 0) {
            if (benIncluded) {
                prevUpazilaId = $("#upazilaId").val();
                loadBeneficiaryList();
            }
            return;
        }
        bootbox.confirm({
            message: '<spring:message code="message.tableClearWarning" />',
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
            callback: function (feedback)
            {
                if (feedback == true) {
                    //Remove Attendee Data
                    document.getElementById("attendeeListTable").getElementsByTagName("tbody")[0].innerHTML = "";
                    benSet.clear();
                    $("#totalAttendee").text(benSet.size);
                    loadBeneficiaryList();
                    return true;
                } else
                {
                    $('#upazilaId').val(prevUpazilaId);
                    return;
                }
                prevUpazilaId = $("#upazilaId").val();
            }
        });
    }

    function loadBeneficiaryList() {
        $('#beneficiaryListTable').DataTable().destroy();
        $('#beneficiaryListTable').DataTable({
            "processing": false,
            "pageLength": 20,
            "lengthMenu": [20, 100, 500, 1000],
            "bSort": false,
            "serverSide": false,
            "ordering": false,
            "info": false,
            "searching": true,
            "pagingType": "full_numbers",
            "language": {
                "url": urlLang
            },
            "ajax": {
                "url": path + "/getBeneficiaryByGeolocation/list",
                "type": "POST",
                "data": {
                    "upazilaId": $("#upazilaId").val()
                }
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === "bn")
                {
                    $("#beneficiaryListTable_filter input").attr('id', 'benSearchId');
                    $("#benSearchId").attr('type', 'text');
                    makeUnijoyEditor('benSearchId');
                }
            }
        });

    }

    var prevUpazilaId = "${training.upazilla.id}";
    var prevTrainingType = "${training.trainingType.id}";
    var benIncluded = true;
    var benSet = new Set();
    var list = "${attendeeListSt}".split(",");
    for (var i in list) {
        if (list[i] !== "") {
            benSet.add(list[i]);
        }
    }
    function setAttendeeList(row) {
        //Check if already present in other training subject
        var benId = row.id;
        var trainingTypeId = $("#trainingType").val();
        var result = checkBeneficiaryTrainingDuplicacy(benId, trainingTypeId);
        if (result === true)
        {
            if ("${pageContext.response.locale}" == "bn")
            {
                bootbox.alert("<b>এই ভাতাভোগী একই বিষয়ের অন্য প্রশিক্ষণে অন্তর্ভুক্ত আছেন!</b>");
            } else
            {
                bootbox.alert("<b>This beneficiary is already included in another training of same subject!</b>");
            }
            return;
        }
        var index = $(row).closest('tr').index();
        if (!benSet.has(row.id)) {
            benSet.add(row.id);
            var table = document.getElementById("beneficiaryListTable");
            var name = table.rows[index + 1].cells[1].innerHTML;
            var nid = table.rows[index + 1].cells[2].innerHTML;
            var mobileNo = table.rows[index + 1].cells[3].innerHTML;
            var benTabRef = index + 1;
            //Remove + sign on click
            table.rows[index + 1].cells[0].innerHTML = "";

            $("#attendeeListTable").append('<tr>' +
                    '<td><a><span id="' + row.id + '" onclick="deleteRow(this)" class="glyphicon glyphicon-remove"></span></a></td>' +
                    '<td>' + name + '</td>' +
                    '<td>' + nid + '</td>' +
                    '<td>' + mobileNo + '</td>' +
                    '<td hidden="true">' + benTabRef + '</td></tr>');
            if (selectedLocale === "bn")
                $("#totalAttendee").text(getNumberInBangla("" + benSet.size + ""));
            else
                $("#totalAttendee").text(benSet.size);
        }

        $("#selectedBenListSt").val(Array.from(benSet).join(','));
    }
    function deleteRow(row) {
        var index = $(row).closest('tr').index();
        var table = document.getElementById("attendeeListTable");
        var ref = "";
        if (table.rows[index + 1].cells.length > 4) {
            ref = table.rows[index + 1].cells[4].innerHTML;
        }
        table.rows[index + 1].remove();
        benSet.delete(row.id.toString());
        $("#selectedBenListSt").val(Array.from(benSet).join(','));
        var refTable = document.getElementById("beneficiaryListTable");
        if (ref !== "") {
            refTable.rows[ref].cells[0].innerHTML = "<td><a><span class=\"glyphicon glyphicon-plus\" id=\"" + row.id + "\" onclick=\"setAttendeeList(this)\"></span></a></td>";
        }
        if (selectedLocale === "bn")
            $("#totalAttendee").text(getNumberInBangla("" + benSet.size + ""));
        else
            $("#totalAttendee").text(benSet.size);
    }

    function checkBeneficiaryTrainingDuplicacy(benId, trainingTypeId)
    {
        var result;
        $.ajax({
            type: "GET",
            url: contextPath + "/checkBenTrainingDuplicacy",
            async: false,
            data: {'id': benId, 'trainingTypeId': trainingTypeId},
            success: function (response) {
                result = response;
            },
            failure: function (response) {
                result = response;
            }
        });
        return result;
    }
    function removeProfilePhoto()
    {
        document.getElementById("profilePhotoFile").style.display = "none";//
        document.getElementById("removeProfilePhoto").style.display = "none";//
        $("#profilePhotoFile").val("");
        $("#photo").attr("style", "display:block");
    }
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/training/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/training/edit/${training.id}" />
    </c:otherwise>
</c:choose>

<form:form id="trainingForm" action="${actionUrl}" method="post"  enctype="multipart/form-data"  class="form-horizontal" role="form" modelAttribute="training">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.training" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/training/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="print" class="btn bg-green-active" id="print" onclick="PrintElem()"> <i class="fa fa-print"></i>
                <spring:message code='beneficiaryProfile.Print'/></button> 
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
            <div class="col-md-12">
                <div class="col-md-6">
                    <fieldset>
                        <legend><spring:message code='training.info'/></legend>
                        <form:hidden path="id" />
                        <input type="hidden" id="selectedBenListSt" name="selectedBenListSt"/>
                        <div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="training.fiscalYear" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="fiscalYear.id"  id="fiscalYear"> 
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                                    </form:select> 
                                    <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="trainingTypeInput" class="col-md-4 control-label"><spring:message code="training.trainingType" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="trainingType.id"  id="trainingType" onchange="checkIfBenIncluded(this)"> 
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${trainingTypeList}" itemValue="id" itemLabel="${trainingTypeName}" ></form:options> 
                                    </form:select> 
                                    <form:errors path="trainingType.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="trainerInput" class="col-md-4 control-label"><spring:message code="training.trainer" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="trainer.id"  id="trainer">
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${trainerList}" itemValue="id" itemLabel="${trainerName}"></form:options>
                                    </form:select>
                                    <form:errors path="trainer.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="trainingVenueInput" class="col-md-4 control-label"><spring:message code="label.trainingVenue" /></label>
                                <div class="col-md-8">
                                    <spring:message code='label.trainingVenue' var="trainingVenue"/>
                                    <form:input path="trainingVenue" class="form-control" placeholder="${trainingVenue}" id="trainingVenue" />
                                    <form:errors path="trainingVenue" cssClass="error"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="numberOfPerticipantsInputEnglish" class="col-md-4 control-label"><spring:message code="training.numberOfPerticipants" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='training.numberOfPerticipants' var="numberOfPerticipants"/>
                                    <form:input class="form-control bfh-phone" placeholder="${numberOfPerticipants}" path="numberOfPerticipants" />
                                    <form:errors path="numberOfPerticipants" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="trainingCostInputEnglish" class="col-md-4 control-label"><spring:message code="training.trainingCost" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='training.trainingCost' var="trainingCost"/>
                                    <form:input class="form-control bfh-phone" placeholder="${trainingCost}" path="trainingCost" autofocus="autofocus" onkeydown="checkNumberWithLength(event, this, 10)"/>
                                    <form:errors path="trainingCost" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="code" class="col-md-4 control-label"><spring:message code="training.startDate" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='training.startDate' var="startDate"/>
                                    <form:input class="form-control" placeholder="${startDate}" path="startDate" onchange="updateDuration(this.id)"/>
                                    <form:errors path="startDate" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="code" class="col-md-4 control-label"><spring:message code="training.endDate" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='training.endDate' var="endDate"/>
                                    <form:input class="form-control" placeholder="${endDate}" path="endDate" onchange="updateDuration(this.id)"/>
                                    <form:errors path="endDate" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="durationDayInputEnglish" class="col-md-4 control-label"><spring:message code="label.durationDay" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.durationDay' var="durationDay"/>
                                    <form:input class="form-control bfh-phone" placeholder="${durationDay}" path="durationDay" autofocus="autofocus" />
                                    <form:errors path="durationDay" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="commentInputEnglish" class="col-md-4 control-label"><spring:message code="training.comment" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='training.comment' var="comment"/>
                                    <form:textarea class="form-control bfh-phone" placeholder="${comment}" path="comment" autofocus="autofocus"/>
                                    <form:errors path="comment" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.attachment" /></label>
                                <div class="col-md-8">
                                    <spring:message code='placeholder.signature' var="signature"/>                                    

                                    <div id="photo">
                                        <input id="fileInput" name="file" type="file" class="file" accept="image/jpg,image/png,image/jpeg,image/gif"  data-allowed-file-extensions='["jpg", "jpeg", "png", "gif"]'>
                                    </div>

                                    <c:if test="${training.fileBase64 !='' && training.fileBase64 != null}">
                                        <form:hidden  path="fileExtension" value="${training.fileExtension}"/>
                                        <form:hidden  path="fileBase64" value="${training.fileBase64}"/>
                                        <img id="profilePhotoFile" style="width: 50%; border-radius: 20px" alt="img" src="data:image/jpeg;base64,${training.fileBase64}"/>
                                        <input type="image" src="${contextPath}/resources/img/remove16.png" id="removeProfilePhoto" value="Remove" onclick="removeProfilePhoto();
                                                return false;" /> 
                                    </c:if>
                                </div>            
                            </div> 
                        </div>
                    </fieldset>
                </div>
                <div class="col-md-6"> 

                    <form:hidden path="applicantType" id="applicantType"/>

                    <fieldset>
                        <legend>
                            <spring:message code='training.trainingLocation'/>&nbsp;
                        </legend>
                    </fieldset>
                    <div id="regularBlock">        
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                            <c:choose>
                                <c:when test="${(training.divisionAvailable eq 'false' || not empty training.division.id) && empty sessionScope.userDetail.division.id}">
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
                                    <div class="col-md-8 labelAsValue">
                                        <form:hidden id="divisionId" path="division.id"/>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                                ${training.division.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${training.division.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                            <c:choose>
                                <c:when test="${(training.districtAvailable eq 'false' || not empty training.district.id) && empty sessionScope.userDetail.district.id}">
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="district.id" id="districtId" onchange="loadPresentUpazilaList(this)">
                                            <form:option value="" label="${select}"></form:option>
                                        </form:select>
                                        <form:errors path="district.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-8 labelAsValue">
                                        <form:hidden id="districtId" path="district.id"/>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                ${training.district.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${training.district.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.upazila" /></label>
                            <c:choose>
                                <c:when test="${(training.upazilaAvailable eq 'false' || not empty training.upazilla.id) && empty sessionScope.userDetail.upazila.id}">
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="upazilla.id" id="upazilaId" onchange="return clearAttendeeOnChange()">
                                            <form:option value="" label="${select}"></form:option>
                                        </form:select>
                                        <form:errors path="upazilla.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-8 labelAsValue">
                                        <form:hidden id="upazilaId" path="upazilla.id"/>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                ${training.upazilla.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${training.upazilla.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>                                                
                    </div>
                </div>
            </div>
            <div class="col-md-12">
                <div class="col-md-6">
                    <fieldset>
                        <legend>
                            <div class="col-md-6">
                                <spring:message code='title.beneficiaryList'/>&nbsp;
                            </div>

                            <!--                            <div class="col-md-offset-3 col-md-2">
                                                            <button type="button" class="btn btn-primary" onclick="loadBeneficiaryList()"><spring:message code="button.loadBeneficiary"/></button>
                                                        </div>-->
                        </legend>
                        <table id="beneficiaryListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th id="rowSelect"><spring:message code="label.add" /></th>                                                                
                                    <th><spring:message code="label.name" /></th>
                                    <th><spring:message code="label.nid" /></th>                                                                
                                    <th><spring:message code="label.mobileNo" /></th>                                                                                               
                                </tr>
                            </thead>
                        </table>
                    </fieldset>
                </div>
                <div class="col-md-6" id="attendeeTableDiv">
                    <fieldset>
                        <legend>
                            <span style="float:left">
                                <spring:message code='title.traineeList'/> 
                            </span>
                            <span style="float:right">
                                <spring:message code='label.total'/>:<label id="totalAttendee"></label>&nbsp;
                            </span>

                        </legend>
                        <div ></div>
                        <table id="attendeeListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th><spring:message code="label.delete" /></th>                                                                
                                    <th><spring:message code="label.name" /></th>                                                                
                                    <th><spring:message code="label.nid" /></th>                                                                
                                    <th><spring:message code="label.mobileNo" /></th>                                                                                               
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="attendee" items="${attendeeList}">
                                    <tr>
                                        <td><a><span id="${attendee.beneficiary.id}" onclick="deleteRow(this)" class="glyphicon glyphicon-remove"></span></a></td>
                                                <c:if test="${pageContext.response.locale eq 'en'}">
                                            <td><c:out value="${attendee.beneficiary.fullNameInEnglish}"></c:out></td>
                                            <td><c:out value="${attendee.nidEn}"></c:out></td>
                                            <td><c:out value="${attendee.mobileNoEn}"></c:out></td>
                                        </c:if>
                                        <c:if test="${pageContext.response.locale eq 'bn'}">
                                            <td><c:out value="${attendee.beneficiary.fullNameInBangla}"></c:out></td>
                                            <td><c:out value="${attendee.nidBn}"></c:out></td>
                                            <td><c:out value="${attendee.mobileNoBn}"></c:out></td>
                                        </c:if>                                                                                                                                                                   
                                    </tr>
                                </c:forEach>
                        </table>
                    </fieldset>
                </div>

            </div>
        </div>

    </section>
</form:form>


<div id='trainingmodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="trainingmodel-delete-confirmation-title">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
            </div>
            <form action="${contextPath}/training/delete/${training.id}" method="post">
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

<div id="dvPrint" style="display: none">

    <section class="content">
        <div class="row">
            <div class="col-md-12">



                <table id="applicantListTable" class="table table-responsive table-bordered table-hover">
                    <thead>
                        <tr style="width: 100%">
                            <td colspan="2" ><legend><spring:message code='training.info'/></legend></td>
                    <td colspan="2"  ><legend><spring:message code='training.trainingLocation'/></td>
                        </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td ><spring:message code="training.fiscalYear" /></td>
                                <td><label id="lbFiscalYear"></label></td>
                                <td><spring:message code="label.division" /></td>
                                <td><label id="lbDivision"></label></td>
                            </tr>

                            <tr>
                                <td ><spring:message code="training.trainingType" /></td>
                                <td><label id="lbTrainingType"></label></td>
                                <td><spring:message code="label.district" /></td>
                                <td><label id="lbDistrict"></label></td>
                            </tr>

                            <tr>
                                <td ><spring:message code="training.trainer" /></td>
                                <td><label id="lbTrainer"></label></td>
                                <td><spring:message code="label.upazila" /></td>
                                <td><label id="lbUpazila"></label></td>
                            </tr>
                            <tr>
                                <td><spring:message code="label.trainingVenue" /></td>
                                <td><label id="lbTrainingVenue"></label></td>
                                <td></td>
                                <td><label id=""></label></td>
                            </tr>
                            <tr>
                                <td><spring:message code="training.numberOfPerticipants" /></td>
                                <td><label id="lbNumberOfPerticipants"></label></td>
                                <td></td>
                                <td><label id=""></label></td>
                            </tr>
                            <tr>
                                <td><spring:message code="training.trainingCost" /></td>
                                <td><label id="lbTrainingCost"></label></td>
                                <td></td>
                                <td><label id=""></label></td>
                            </tr>
                            <tr>
                                <td><spring:message code="training.startDate" /></td>
                                <td><label id="lbStartDate"></label></td>
                                <td></td>
                                <td><label id=""></label></td>
                            </tr>

                            <tr>
                                <td><spring:message code="training.endDate" /></td>
                                <td><label id="lbEndDate"></label></td>
                                <td></td>
                                <td><label id=""></label></td>
                            </tr>

                            <tr>
                                <td><spring:message code="label.durationDay" /></td>
                                <td><label id="lbDuration"></label></td>
                                <td></td>
                                <td><label id=""></label></td>
                            </tr>

                            <tr>
                                <td><spring:message code="label.comment" /></td>
                                <td><label id="lbComment"></label></td>
                                <td></td>
                                <td></td>
                            </tr>
                        </tbody>
                </table>


            </div>
            <div class="col-md-12">

                <div class="col-md-12" id="attendeeTableDivPrint">

                </div>

            </div>
        </div>

    </section>
</div>               
<!-- /.content -->


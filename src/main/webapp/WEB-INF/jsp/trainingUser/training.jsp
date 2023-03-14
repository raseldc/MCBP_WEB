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



<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/training/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/training/edit/${training.id}" />
    </c:otherwise>
</c:choose>

<form:form id="trainingForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="training">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.training" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/user/training/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="button" name="save" class="btn bg-blue" onclick="submitForm()">
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
            <div class="col-md-12 col-lg-12 col-sm-12">
                <div class="col-md-12 col-lg-12 col-sm-12">
                    <fieldset>
                        <legend><spring:message code='training.info'/></legend>
                        <form:hidden path="id" />
                        <input type="hidden" id="selectedBenListSt" name="selectedBenListSt"/>
                        <div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-1 control-label"><spring:message code="training.fiscalYear" />
                                    <span class="mandatory">*</span>
                                </label>
                                <div class="col-md-2">
                                    <c:choose>
                                        <c:when  test="${courseId != 0}">
                                            <label class="control-label">
                                                ${fiscalYearName}
                                            </label> 
                                        </c:when>
                                        <c:otherwise>
                                            <spring:message code='label.select' var="select"/>
                                            <form:select class="form-control" path="fiscalYear.id"  id="ddFiscalYear"> 
                                                <form:option value="" label="${select}"></form:option>
                                                <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                                            </form:select> 
                                            <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                                        </c:otherwise>
                                    </c:choose>    
                                </div>

                                <label for="trainingTypeInput" class="col-md-1 control-label"><spring:message code="training.course"/> 
                                    <span class="mandatory">*</span></label>
                                <div class="col-md-2">
                                    <c:choose>
                                        <c:when  test="${courseId != 0}">
                                            <label class="control-label">
                                                ${courseName}
                                            </label>
                                        </c:when>
                                        <c:otherwise>
                                            <spring:message code='label.select' var="select"/>
                                            <form:select class="form-control" path="trainingType.id"  id="ddCourse" onchange="loadBatch()">  
                                                <form:option value="" label="${select}"></form:option>
                                                <form:options items="${trainingList}" itemValue="course_id" itemLabel="title"></form:options> 
                                            </form:select> 
                                            <form:errors path="trainingType.id" cssStyle="color:red"></form:errors>
                                        </c:otherwise>
                                    </c:choose>

                                </div>

                                <label for="trainingTypeInput" class="col-md-1 control-label"><spring:message code="training.batch"/>  <span  class="mandatory">*</span></label>
                                <div class="col-md-2">
                                    <c:choose>
                                        <c:when  test="${batchId != 0}">
                                            <label class="control-label">
                                                ${batchName}
                                            </label>
                                        </c:when>
                                        <c:otherwise>
                                            <spring:message code='label.select' var="select"/>
                                            <select class="form-control" id="ddBatch" onchange="loadUserList()">
                                                <option value=""  label="${select}"></option>
                                            </select>
                                        </c:otherwise>
                                    </c:choose>


                                </div>


                                <div class="col-md-1"></div>                                   
                                <div class="col-md-2">
                                    <!--<button type="button" id="buttonSearch" class="btn bg-blue" onclick="loadUserList()"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>-->
                                </div>
                            </div>
                    </fieldset>
                </div>
                <div class="col-md-6 col-sm-12"> 


                </div>
            </div>
        </div>
        <div class="row">
            <div class=" col-md-12 col-lg-12 col-sm-12">
                <div class="col-md-12 col-lg-12 col-sm-12 table-responsive">
                    <fieldset>
                        <legend>
                            <div class="col-md-6">
                                <spring:message code='userList.userList'/>&nbsp;
                            </div>


                        </legend>



                        <table id="userListTable" class="table table-bordered table-hover" style="width: 100%">
                            <thead>
                                <tr>
                                    <th style="width: 8%" id="rowSelect"><spring:message code="label.add" /></th>                                                                
                                    <th><spring:message code="label.nameBn" /></th>
                                    <th><spring:message code="label.nameEn" /></th>
                                    <th><spring:message code="label.userID" /></th>                                    
                                    <th><spring:message code="label.email" /></th>
                                    <th><spring:message code="label.mobileNo" /></th>                                                                                               
                                </tr>
                            </thead>
                        </table>

                    </fieldset>
                </div>
                <div class="col-md-12 col-lg-12 col-sm-12 table-responsive" id="attendeeTableDiv">
                    <fieldset>
                        <legend>
                            <span style="float:left">
                                <spring:message code='userTraining.selecteTrainingUser'/> 
                            </span>
                            <span style="float:right">
                                <spring:message code='label.total'/>:<label id="totalAttendee"></label>&nbsp;
                            </span>

                        </legend>
                        <div ></div>
                        <table id="attendeeListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th style="width: 5%"><spring:message code="label.delete" /></th>                                                                
                                    <th style="width: 15%"><spring:message code="label.nameBn" /></th>
                                    <th style="width: 15%"><spring:message code="label.nameEn" /></th>
                                    <th style="width: 10%"><spring:message code="label.userID" /></th>                                    
                                    <th style="width: 15%"><spring:message code="label.email" /></th>
                                    <th style="width: 15%"><spring:message code="label.mobileNo" /></th>                                                                                                 
                                </tr>
                            </thead>

                        </table>
                    </fieldset>
                </div>

            </div>
        </div>



    </section>


</form:form>

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
    var courseIdEdit = ${courseId};
    var batchIdEdit = ${batchId};
    var fiscalYearIdEdit = ${fiscalYearId};
    var baseUrl = '${contextPath}';
    var userList = [];
    function loadUserList() {
        userList = [];
        $("#attendeeListTable tbody").empty();
        var courseId = courseIdEdit; // $("#ddCourse :selected").val();

        var batchId = $("#ddBatch :selected").val();
        if (batchId == undefined)
        {
            batchId = batchIdEdit;
        }

        var fiscalYear = $("#ddFiscalYear :selected").val();
        if (fiscalYear == undefined)
        {
            fiscalYear = fiscalYearIdEdit;
        }
        if (fiscalYear == "" || fiscalYear == undefined || fiscalYear == 0)
        {
            $("#modalBody").html("অর্থ বছর নির্বাচন করুন");
            $("#btnModal").click();
            return;
        }
        if (courseId == "" || courseId == undefined || courseId == 0)
        {
            $("#modalBody").html("কোর্স  নির্বাচন করুন");
            $("#btnModal").click();
            return;
        }

        if (batchId == "" || batchId == undefined || batchId == 0)
        {
            $("#modalBody").html("ব্যাচ  নির্বাচন করুন");
            $("#btnModal").click();
            return;
        }

        $('#userListTable').DataTable().destroy();
        $('#userListTable').DataTable({
            "processing": true,
            "pageLength": 10,
            "lengthMenu": [5, 10, 15, 20],
            "destroy": true,
            "paging": true,
            "serverSide": false,
            "searching": true,
            "pagingType": "full_numbers",
            "filter": true,
            "dom": '<"top"i>rft<"bottom"lp><"clear">',
            "language": {
                "url": urlLang
            },
            "ajax": baseUrl + "/usre/training/get-all-user?courseId=" + courseId + "&batchId=" + batchId + "&fiscalYearId=" + fiscalYear,

            "createdRow": function (row, data, dataIndex) {

                if (data.courseId == courseId && data.batchId == batchId)
                {
                    var email_ = data.email == undefined ? "" : data.email;
                    var mobile_ = data.mobileNo == undefined ? "" : data.mobileNo;
                    mobile_ = selectedLocale == "bn" ? getNumberInBangla(mobile_.toString()) : mobile_;
                    $("#attendeeListTable").append('<tr id="td-delete-user-' + data.id + '">' +
                            "<td><a><span id='deleteUser-" + data.id + "' onclick='deleteRow(" + JSON.stringify(data.id) + ")' class='glyphicon glyphicon-remove'></span></a></td>" +
                            '<td>' + data.fullNameBn + '</td>' +
                            '<td>' + data.fullNameEn + '</td>' +
                            '<td>' + data.userID + '</td>' +
                            '<td>' + email_ + '</td>' +
                            '<td>' + mobile_ + '</td></tr>');
                    $("#spanUserList-" + data.id).hide();
                    userList.push(data.id);
                    $("#totalAttendee").text(userList.length);
                    console.log("userList.length" + userList.length);
                    if (selectedLocale === 'bn')
                    {
                        $("#totalAttendee").text(getNumberInBangla(userList.length.toString()));
                    }
                }
            },
            "columns": [
                {"width": "5%",
                    "data": function (data) {

                        var allData = JSON.stringify(data);
                        var userInfo = {"id": data.id, "fullNameBn": data.fullNameBn, "fullNameEn": data.fullNameEn, "mobileNo": data.mobileNo, "email": data.email, "userID": data.userID};
                        allData = JSON.stringify(userInfo);
                        if (data.courseId == courseId && data.batchId == batchId)
                        {

                            return "<a><span class='glyphicon glyphicon-plus' style='display:none' id='spanUserList-" + data.id + "' onclick='setAttendeeList(" + allData + ")' ></span></a>";
                        } else
                            return "<a><span class='glyphicon glyphicon-plus'  id='spanUserList-" + data.id + "' onclick='setAttendeeList(" + allData + ")' ></span></a>";
                    }},
                {
                    "width": "15%",
                    "data": function (data) {
                        return data.fullNameBn;
                    }},
                {
                    "width": "15%",
                    "data": function (data) {
                        return data.fullNameEn;
                    }},
                {"width": "10%",
                    "data": function (data) {
                        return data.userID;
                    }},
                {
                    "width": "15%",
                    "data": function (data) {
                        return data.email == undefined ? "" : data.email;
                    }},
                {"width": "15%",
                    "data": function (data) {
                        var mobile_ = data.mobileNo == undefined ? "" : data.mobileNo;
                        return selectedLocale == "bn" ? getNumberInBangla(mobile_.toString()) : mobile_;
                    }}

            ],
            "fnDrawCallback": function (oSettings) {
                //   showModalDialog();
                if (selectedLocale === 'bn')
                {
//                    $('#trainingListTable  tr').not(":first").each(function () {
//                        $(this).find("td").eq(3).html(getNumberInBangla($(this).find("td").eq(3).html()));
//                        $(this).find("td").eq(4).html(getNumberInBangla($(this).find("td").eq(4).html()));
//                        $(this).find("td").eq(5).html(getNumberInBangla($(this).find("td").eq(5).html()));
//                        $(this).find("td").eq(6).html(getNumberInBangla($(this).find("td").eq(6).html()));
//                    });
                    localizeBanglaInDatatable("userListTable");
                }
            }
        });
    }
    function setAttendeeList(userInfo) {
        console.log(userInfo);
        var email_ = userInfo.email == undefined ? "" : userInfo.email;

        var mobile_ = userInfo.mobileNo == undefined ? "" : userInfo.mobileNo;
        var mobile_ = selectedLocale == "bn" ? getNumberInBangla(mobile_.toString()) : mobile_;
        $("#attendeeListTable").append('<tr id="td-delete-user-' + userInfo.id + '">' +
                "<td><a><span id='deleteUser-" + userInfo.id + "' onclick='deleteRow(" + JSON.stringify(userInfo.id) + ")' class='glyphicon glyphicon-remove'></span></a></td>" +
                '<td>' + userInfo.fullNameBn + '</td>' +
                '<td>' + userInfo.fullNameEn + '</td>' +
                '<td>' + userInfo.userID + '</td>' +
                '<td>' + email_ + '</td>' +
                '<td>' + mobile_ + '</td>');
        $("#spanUserList-" + userInfo.id).hide();
        userList.push(userInfo.id);
        $("#totalAttendee").text(userList.length);
        if (selectedLocale === 'bn')
        {
            $("#totalAttendee").text(getNumberInBangla(userList.length.toString()));
        }
        //     $("#selectedBenListSt").val(Array.from(benSet).join(','));
    }
    function deleteRow(userInfoId)
    {
        $("#spanUserList-" + userInfoId).show();
        $("#td-delete-user-" + userInfoId).remove();
        userList = userList.filter(function (e) {
            return e != userInfoId
        });
        $("#totalAttendee").text(userList.length);
    }
    function loadBatch()
    {
        var courseId = $("#ddCourse :selected").val();
        var fiscalYearId = $("#ddFiscalYear :selected").val();
        courseIdEdit = courseId;
        $.ajax({
            type: "GET",
            contentType: 'application/json; charset=utf-8',
            url: "${contextPath}/get-batch-by-course?courseId=" + courseId + "&fiscalYearId=" + fiscalYearId + "&isCreate=1",
            dataType: 'JSON',
            success: function (data) {

                if (data.isError === false)
                {
                    $('#ddBatch').empty();
                    $('#ddBatch').append($('<option>', {
                        value: 0,
                        text: " নির্বাচন করুন"
                    }));
//                    console.log(lan);
//                    if (lan === "en")
//                        data.returnObject.sort(SortByEngName);
//                    if (lan === "bn")
//                        data.returnObject.sort(SortByBanglaName);
                    $(data.returnObject).each(function (i, e) {
                        $('#ddBatch').append($('<option>', {
                            value: e.id,
                            text: e.title,
                            url: e.resticted_url

                        }));
                    });
                }
                //   console.log(data.returnObject);

            },
            failure: function () {
                $("#btLoader").click();
                console.log("Failed");
            },
            complete: function (jqXHR, textStatus) {

                //   loadUpazilaFromLogin();
            }

        }
        );
        var option_upzila = $("#ddUpazila option").get(0);
        $("#ddUpazila").empty();
        $("#ddUpazila").append(option_upzila);
        var option_union = $("#ddUnion option").get(0);
        $("#ddUnion").empty();
        $("#ddUnion").append(option_union);
        $("#ddWard").val(0);
    }

    function submitForm() {
        var courseId = $("#ddCourse :selected").val();
        var courseName = $("#ddCourse :selected").text();
        var batchId = $("#ddBatch :selected").val();
        var batchName = $("#ddBatch :selected").text();
        var fiscalYear = $("#ddFiscalYear :selected").val();
        var restrictUrl = $("#ddBatch :selected").attr('url');
        if (batchId != undefined)
        {
            batchIdEdit = batchId;
        }
        if (fiscalYear == undefined)
        {
            fiscalYear = fiscalYearIdEdit;
        }
        if (fiscalYear == "" || fiscalYear == undefined || fiscalYear == 0)
        {
            $("#modalBody").html("অর্থ বছর নির্বাচন করুন");
            $("#btnModal").click();
            return;
        }
        var enrollmentInfo = {
            "userIds": userList,
            "courseId": courseIdEdit,
            "batchId": batchIdEdit,
            "courseName": courseName,
            "batchName": batchName,
            "fiscalYearId": fiscalYear,
            "restictedUrl": restrictUrl


        }
        enrollmentInfo = JSON.stringify(enrollmentInfo);
        $.ajax(
                {
                    type: 'POST',
                    url: "${contextPath}/user-training-enrollment",
                    contentType: 'application/json; charset=utf-8',
                    data: enrollmentInfo,
                    dataType: 'json',
                    success: function (data) {
                        if (data.isError === false)
                        {
                            if (data.errorCode == 200) {
                                $("#modalBody").html("সফলভাবে ট্রেনিং তৈরী হয়েছে");
                                $("#btnModal").click();
                            } else if ((data.errorCode == 202)) {
                                $("#modalBody").html("সফলভাবে ট্রেনিং তৈরী হয়েছে । এই ব্যবহারকারীদের ইমেল পাওয়া যায়নি " + data.errorMsg);
                                $("#btnModal").click();
                            } else if (data.errorCode == 500) {
                                $("#modalBody").html("ট্রেনিং তৈরী ব্যর্থ হয়েছে।মুক্তপাঠ এ এনরোল এ ব্যার্থ হয়েছে।");
                                $("#btnModal").click();
                            }
                            loadUserList();
                        }
                        if (data.isError === true)
                        {


                            $("#msg").css("color", "red");
                            $("#msg").html(data.errorMsg);
                            setTimeout(function () {
                                $("#msg").html("");
                            }, 5000);
                        }



                    }
                });
    }
    var urlLang = "";
    $(document).ready(function () {
        $(menuSelect("${pageContext.request.contextPath}/user/training/list"));
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }
        if (courseIdEdit !== 0) {
            $(".mandatory").hide()
            loadUserList();
        }

    });
</script>

<!-- /.content -->


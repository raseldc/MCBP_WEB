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

        </h1>
        <div class="pull-right">

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
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="training.fiscalYear" /> <span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="fiscalYear.id"  id="ddFiscalYear"> 
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                                    </form:select> 
                                    <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="trainingTypeInput" class="col-md-4 control-label"><spring:message code="training.course"/><span class="mandatory">*</span></label>
                                <div class="col-md-8">

                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="trainingType.id"  id="ddCourse" onchange="loadBatch()">  
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${trainingList}" itemValue="course_id" itemLabel="title"></form:options> 
                                    </form:select> 
                                    <form:errors path="trainingType.id" cssStyle="color:red"></form:errors>


                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="trainingTypeInput" class="col-md-4 control-label"><spring:message code="training.batch"/><span class="mandatory">*</span></label>
                                <div class="col-md-8">

                                    <spring:message code='label.select' var="select"/>
                                    <select class="form-control" id="ddBatch">
                                        <option value="" label="${select}"></option>
                                    </select>


                                </div>

                            </div>
                            <div class="form-group">
                                <div class="col-md-4"></div>                                    
                                <div class="col-md-8">
                                    <button type="button" id="buttonSearch" class="btn bg-blue" onclick="loadUserList()"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                                </div>
                            </div>
                    </fieldset>
                </div>
                <div class="col-md-6"> 


                </div>
            </div>
        </div>
        <div class="row">
            <div class=" col-md-12 col-lg-12 col-sm-12 table-responsive">

                <fieldset>
                    <legend>
                        <div class="col-md-6">
                            <spring:message code='userList.userList'/>&nbsp;
                        </div>


                    </legend>
                    <table id="userListTable" class="table table-bordered table-hover" style="width: 100%">
                        <thead>
                            <tr>

                                <th><spring:message code="label.name" /></th>
                                <th><spring:message code="label.mobileNo" /></th>
                                <th><spring:message code="training.IsRegeisterd" /></th>
                                <th><spring:message code="training.LastAcivatDate" /></th>                                
                                <th><spring:message code="training.marks"/></th>
                                <th><spring:message code="training.complete"/></th>
                                <th><spring:message code="training.zoomMeeting"/></th>
                                <th><spring:message code="training.getCertificate"/></th>

                                <th><spring:message code="training.CertificateLink"/></th>

                                <th><spring:message code="training.comment"/></th>
                            </tr>
                        </thead>
                    </table>
                </fieldset>



            </div>
        </div>



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
                                <span class="btn btn-default" data-dismiss="modal"><spring:message code="cancelMessage"/></span>
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
    var courseIdEdit = 0;
    var batchIdEdit = 0;
    var baseUrl = '${contextPath}';
    var userList = [];

    function loadUserList() {
        userList = [];
        $("#attendeeListTable tbody").empty();
        var courseId = courseIdEdit;// $("#ddCourse :selected").val();

        var batchId = $("#ddBatch :selected").val();
        if (batchId != undefined)
        {
            batchIdEdit = batchId;
        }
        var courseId = $("#ddCourse :selected").val();
        var fiscalYearId = $("#ddFiscalYear :selected").val();
        if (fiscalYearId == "" || fiscalYearId == undefined || fiscalYearId == 0)
        {
            $("#modalBody").html("অর্থ বছর নির্বাচন করুন");
            $("#btnModal").click();
            return;
        }
        if (courseId == "" || courseId == 0)
        {
            $("#modalBody").html("কোর্স  নির্বাচন করুন");
            $("#btnModal").click();
            return;
        }
        if (batchId == "" || batchId == 0)
        {
            $("#modalBody").html("ব্যাচ   নির্বাচন করুন");
            $("#btnModal").click();
            return;


        }

        $('#userListTable').DataTable().destroy();
        $('#userListTable').DataTable({
            "processing": true,
            "pageLength": 20,
            "lengthMenu": [20, 100, 500, 1000],
            "destroy": true,
            "paging": true,
            "serverSide": false,
            "searching": true,
            "pagingType": "full_numbers",
            "filter": false,
            "responsive": true,
            "language": {
                "url": baseUrl + "/dataTable/localization/bangla"
            },
            "ajax": baseUrl + "/usre/training/get-user-list?courseId=" + courseId + "&batchId=" + batchIdEdit,
            "createdRow": function (row, data, dataIndex) {

                if (data.courseId == courseId && data.batchId == batchId)
                {
                    $("#attendeeListTable").append('<tr id="td-delete-user-' + data.id + '">' +
                            "<td><a><span id='deleteUser-" + data.id + "' onclick='deleteRow(" + JSON.stringify(data) + ")' class='glyphicon glyphicon-remove'></span></a></td>" +
                            '<td>' + data.fullNameBn + '</td>' +
                            '<td>' + data.mobileNo + '</td>');




                    $("#spanUserList-" + data.id).hide();
                    userList.push(data.id);
                    $("#totalAttendee").text(userList.length);
                }
            },
            "columns": [
                {"width": "20%",
                    "data": function (data) {
                        return data.fullNameBn;

                    }},

                {"data": function (data) {
                        var mobile = data.mobileNo === undefined ? "" : data.mobileNo;
                        return getNumberInBangla(mobile);

                    }},

                {"data": function (data) {
                        if (data.enrollComplete === 0 || data.enrollComplete == undefined) {
                            return "রেজিস্ট্রার করেনি";
                        }
                        return "রেজিস্ট্রার সম্পন্ন হয়েছে";

                    }
                },

                {"data": function (data) {
                        if (data.lastActiveDate === undefined) {
                            return "";
                        }

                        return selectedLocale == "bn" ? getNumberInBangla(data.lastActiveDate.toString()) : data.lastActiveDate;

                    }
                },
                {"data": function (data) {
                        var marks = data.marks === undefined ? 0 : data.marks;
                        return getNumberInBangla(marks.toString());

                    }},
                {"data": function (data) {
                        var marks = data.complete === undefined ? 0 : data.complete;
                        return getNumberInBangla(marks.toString());

                    }},
                {
                    "width": "5%",
                    "data": function (data) {
                        if (data.isZoomMeeting === 0) {
                            return "    <input type='checkbox' id='cb-zoom-" + data.userTrainingAttendanceId + "'  onchange='manageSeletedApplicantIDForZoom(" + data.userTrainingAttendanceId + ") '/>";
                        } else {
                            return "    <input type='checkbox' id='cb-zoom-" + data.userTrainingAttendanceId + "'  onchange='manageSeletedApplicantIDForZoom(" + data.userTrainingAttendanceId + ")' checked/>";
                        }

                    }},
                {"width": "5%",
                    "data": function (data) {
                        if (data.getCertificate === 0) {
                            return "    <input type='checkbox' id='cb-" + data.userTrainingAttendanceId + "'  onchange='manageSeletedApplicantID(" + data.userTrainingAttendanceId + ") '/>";
                        } else {
                            return "    <input type='checkbox' id='cb-" + data.userTrainingAttendanceId + "'  onchange='manageSeletedApplicantID(" + data.userTrainingAttendanceId + ")' checked/>";
                        }

                    }},
                {"data": function (data) {
                        if (data.certificateLink === "") {
                            return "সার্টিফিকেট তৈরী হয়নি";

                        } else
                        {
                            return "<a target='_blank' href='" + data.certificateLink + "' id='btnSelect' value='Edit'><i class='fa fa-download '> + ডাউনলোড <i/></a>"
                        }

                    }
                },

                {"data": function (data) {
                        return "<input id='tb-remarks-" + data.userTrainingAttendanceId + "' class='form-control' onchange='saveReamrks(" + data.userTrainingAttendanceId + ")'/>"
                        //return data.remarks;

                    }
                }


            ],
            "fnDrawCallback": function (oSettings) {

            }
        });

    }

    function saveReamrks(applicantId) {
        var remarks = $("#tb-remarks-" + applicantId).val();
        $.ajax({
            type: "POST",
            contentType: 'application/json; charset=utf-8',
            url: "${contextPath}/applicant-reamarks-save?userTrainingAttenId=" + applicantId + "&remarks=" + remarks,
            dataType: 'JSON',
            success: function (data) {
                if (data.isError === false)
                {
                }
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

    }
    function manageSeletedApplicantIDForZoom(applicantId)
    {

        var isZoomMeeting = $("#cb-zoom-" + applicantId).prop("checked") == true ? 1 : 0;
        $.ajax({
            type: "POST",
            contentType: 'application/json; charset=utf-8',
            url: "${contextPath}/applicant-zoom-meeting?userTrainingAttenId=" + applicantId + "&isZoomMeeting=" + isZoomMeeting,
            dataType: 'JSON',
            success: function (data) {
                if (data.isError === false)
                {
                }
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

    }
    function manageSeletedApplicantID(applicantId)
    {

        var isGetCertificate = $("#cb-" + applicantId).prop("checked") == true ? 1 : 0;
        $.ajax({
            type: "POST",
            contentType: 'application/json; charset=utf-8',
            url: "${contextPath}/applicant-get-certificate?userTrainingAttenId=" + applicantId + "&isGetCertificate=" + isGetCertificate,
            dataType: 'JSON',
            success: function (data) {
                if (data.isError === false)
                {
                }
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

    }
    function setAttendeeList(userInfo) {

        $("#attendeeListTable").append('<tr id="td-delete-user-' + userInfo.id + '">' +
                "<td><a><span id='deleteUser-" + userInfo.id + "' onclick='deleteRow(" + JSON.stringify(userInfo) + ")' class='glyphicon glyphicon-remove'></span></a></td>" +
                '<td>' + userInfo.fullNameBn + '</td>' +
                '<td>' + userInfo.mobileNo + '</td>');




        $("#spanUserList-" + userInfo.id).hide();
        userList.push(userInfo.id);
        $("#totalAttendee").text(userList.length);
        //     $("#selectedBenListSt").val(Array.from(benSet).join(','));
    }
    function deleteRow(userInfo)
    {
        $("#spanUserList-" + userInfo.id).show();
        $("#td-delete-user-" + userInfo.id).remove();

        userList = userList.filter(function (e) {
            return e != userInfo.id
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
            url: "${contextPath}/get-batch-by-course-already-create?courseId=" + courseId + "&fiscalYearId=" + fiscalYearId,

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
        if (batchId != undefined)
        {
            batchIdEdit = batchId;
        }
        var enrollmentInfo = {
            "userIds": userList,
            "courseId": courseIdEdit,
            "batchId": batchIdEdit,
            "courseName": courseName,
            "batchName": batchName


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

                            SearchApplicant();
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
</script>
<!-- /.content -->


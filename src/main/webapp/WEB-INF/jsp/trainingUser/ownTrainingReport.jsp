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
            <spring:message code="label.ownTraining" />

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
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="training.fiscalYear" /> </label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="fiscalYear.id"  id="fiscalYear" onchange="loadAll()"> 
                                        <form:option value="0" label="${select}"></form:option>
                                        <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                                    </form:select> 
                                    <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="trainingTypeInput" class="col-md-4 control-label"><spring:message code="training.course"/></label>
                                <div class="col-md-8">

                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="trainingType.id"  id="ddCourse" onchange="loadBatch()">  
                                        <form:option value="0" label="${select}"></form:option>
                                        <form:options items="${trainingList}" itemValue="course_id" itemLabel="title"></form:options> 
                                    </form:select> 
                                    <form:errors path="trainingType.id" cssStyle="color:red"></form:errors>


                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="trainingTypeInput" class="col-md-4 control-label"><spring:message code="training.batch"/></label>
                                <div class="col-md-8">

                                    <spring:message code='label.select' var="select"/>
                                    <select class="form-control" id="ddBatch">
                                        <option value="0" label="${select}"></option>
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
            <div class=" col-md-12">

                <fieldset>
                    <legend>
                        <div class="col-md-6">
                            <spring:message code='trainingList.trainingList'/>&nbsp;
                        </div>


                    </legend>
                    <table id="userListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>

                                <th><spring:message code="training.course" /></th>
                                <th><spring:message code="training.batch" /></th>
                                <th><spring:message code="training.IsRegeisterd" /></th>
                                <th><spring:message code="training.LastAcivatDate" /></th>
                                <th><spring:message code="training.marks"/></th>                                
                                <th><spring:message code="training.CertificateLink"/></th>
                                <th><spring:message code="training.comment"/></th>                                                                                                                             
                                <th><spring:message code="training.enrolment"/></th>       
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


</form:form>
<script type="text/javascript">
    var courseIdEdit = 0;
    var batchIdEdit = 0;
    var baseUrl = '${contextPath}';
    var userList = [];
    function loadAll()
    {
        $("#ddBatch").val(0);
        $("#ddCourse").val(0);
    }
    function loadUserList() {
        userList = [];
        $("#attendeeListTable tbody").empty();
        var courseId = courseIdEdit; // $("#ddCourse :selected").val();

        var batchId = $("#ddBatch :selected").val();
        var fiscalYearId = $("#fiscalYear").val();
        if (batchId != undefined)
        {
            batchIdEdit = batchId;
        }
        $('#userListTable').DataTable().destroy();
        $('#userListTable').DataTable({
            "processing": true,
            "pageLength": 20,
            "lengthMenu": [5, 10, 15, 20],
            "destroy": true,
            "paging": true,
            "serverSide": false,
            "searching": true,
            "pagingType": "full_numbers",
            "filter": false,
            "language": {
                "url": urlLang
            },
            "ajax": baseUrl + "/usre/training/get-training-list-by-user?courseId=" + courseId + "&batchId=" + batchIdEdit + "&fiscalYearId=" + fiscalYearId,

            "columns": [
                {"data": function (data) {
                        return data.courseName;

                    }},

                {"data": function (data) {
                        return data.batchName;

                    }},
                {"data": function (data) {
                        if (data.enrollComplete == 0)
                        {
                            return "না";

                        } else
                        {
                            return "হ্যা";
                        }


                    }
                },
                {"data": function (data) {
                        if (data.lastActiveDate === undefined) {
                            return "রেজিস্ট্রার করেনি";
                        }
                        return selectedLocale === 'bn' ? getNumberInBangla(data.lastActiveDate.toString()) : data.lastActiveDate;

                    }
                },
                {"data": function (data) {
                        var marks = data.marks === undefined ? 0 : data.marks;
                        return getNumberInBangla(marks.toString());

                    }},

                {"data": function (data) {
                        console.log(data.certificateLink);
                        if (data.certificateLink === "" || data.getCertificate === 0) {
                            return "সার্টিফিকেট তৈরী হয়নি";

                        } else
                        {
                            return "<a target='_blank' href='" + data.certificateLink + "' id='btnSelect' value='Edit'><i class='fa fa-download '> + ডাউনলোড <i/></a>"
                        }

                    }
                },
                {"data": function (data) {

                        return data.remarks;

                    }

                },
                {"data": function (data) {

                        if (data.restictedUrl == "" || data.restictedUrl == undefined)
                        {
                            return "";
                        }
                        return " <a target='_blank'  href='" + data.restictedUrl + "' id='btnSelect' value='Edit'><i class='fa fa-download '> <spring:message code="training.enrolment"/><i/></a>";

                    }

                }

            ],
            "fnDrawCallback": function (oSettings) {
                showModalDialog();
                if (selectedLocale === 'bn')
                {
                    $('#trainingListTable  tr').not(":first").each(function () {
                        $(this).find("td").eq(3).html(getNumberInBangla($(this).find("td").eq(3).html()));
                        $(this).find("td").eq(4).html(getNumberInBangla($(this).find("td").eq(4).html()));
                        $(this).find("td").eq(5).html(getNumberInBangla($(this).find("td").eq(5).html()));
                        $(this).find("td").eq(6).html(getNumberInBangla($(this).find("td").eq(6).html()));
                    });
                    localizeBanglaInDatatable("userListTable");
                }
            }
        });
    }

    function loadBatch()
    {

        var courseId = $("#ddCourse :selected").val();
        courseIdEdit = courseId;
        $.ajax({
            type: "GET",
            contentType: 'application/json; charset=utf-8',
            url: "${contextPath}/get-batch-by-course-by-user?courseId=" + courseId,
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

    var urlLang = "";
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");

        if (selectedLocale === 'bn') {

            urlLang = baseUrl + "/dataTable/localization/bangla";
        }
    });
</script>

<!-- /.content -->


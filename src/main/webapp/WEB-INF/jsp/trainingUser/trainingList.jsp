<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        ব্যবহারকারীদের প্রশিক্ষণ তালিকা 
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/user/training/create" class="btn bg-blue">
            <i class="fa fa-plus-square"></i>
            <spring:message code="addNew" />            
        </a>
    </div>   
</section>

<section class="content">    
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-body">
                    <form:form id="trainingListForm" class="form-horizontal" modelAttribute="searchParameterForm">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="fiscalYearInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" />                                    </label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="fiscalYear.id"  id="ddFiscalYear">  
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
                                    <button type="button" id="buttonSearch" class="btn bg-blue" onclick="search()"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">


                        </div>
                    </div>

                </form:form>

                <table id="trainingListTable" class="table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th><spring:message code="label.fiscalYear"/></th>
                            <th><spring:message code="training.course"/></th>                                                                
                            <th><spring:message code="training.batch"/></th>                                  
                            <th><spring:message code="training.applicantCount"/></th>                                                                                             
                            <th><spring:message code="training.getCertificateCount"/></th>      
                            <th><spring:message code="label.edit" /></th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
</section>


<script type="text/javascript">

    var baseUrl = '${contextPath}';
    function loadBatch()
    {

        var courseId = $("#ddCourse :selected").val();
        var fiscalYearId = $("#ddFiscalYear :selected").val();

        $.ajax({
            type: "GET",
            contentType: 'application/json; charset=utf-8',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImIzNTJiZDg2ZjgxOTUxZTg5OWJlOWU4OGE0ODRjMGVhMDViOWE5YmMwMTQxODQ2NDExZmI4OGRmM2RjM2E2YzVmMzEwNzQ3Yzc4NWMxMWQyIn0.eyJhdWQiOiI1IiwianRpIjoiYjM1MmJkODZmODE5NTFlODk5YmU5ZTg4YTQ4NGMwZWEwNWI5YTliYzAxNDE4NDY0MTFmYjg4ZGYzZGMzYTZjNWYzMTA3NDdjNzg1YzExZDIiLCJpYXQiOjE2MTg4MDEzMTYsIm5iZiI6MTYxODgwMTMxNiwiZXhwIjoxNjUwMzM3MzE2LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.xfVuJxRCGLUpQ01_ioG6sCa2L8-BjjkyOiXKk4yd9wX7wow3K_hJHFFA9bysk3VQjNdAo6TuUF6_vNue-Ny-pMyoMmR1y5plWmnCfnYFLkHbuHyHAG_8-PQq7uSmuvdt9Iw6IL9dQpGNUZxpBVoLHKE7jh7o0L1hfcy6Ib2YFiHSYtFok40a948rfyxGWxjxYm8lUzNkE6NIZLoN4n253yAw_e8MT6EoBzn06A8PYs3j1ooFAVMjRbS12ldwUVxyWhMOYb_g495fWfDLqe7U-h1MyUcIVtm-XdP_zGJ8PLHAdb22pKbJYMTcXSd07e7lytEHf6BIpsyp-nXaPV7dvdlDHgBOGKCtbAfngwo-2zcwZCBU7NWan3SmZx8qpE4kv97P0YlX1c9fo8HlJNM8w7YpKDATK2dUeczEKmtt1Kx30GHp8YB9AJQ9ORyAFGTNO1UQc5vc_V0sVW3dd4ki6j-aT0Ma5uG5hsL1Y43H31iVJGLIGmz2LgHOGvNZoeLKOfUIFWfrnat9tqqv8DpHnFwHA9mQUTuOiKqX6L-wuGb77zAQaiqMWGPx2QClucKUPcdBDyT3eGImFEleoBSySH6TRaM1-DFQYs-Zm7yYF2htBS6_1170TJDhKeLsDnYiHifP5RdvejEbtkRMNWx1C-kOoWePyGA-uyjKOe2ecuI');
            },
            url: "http://api.muktopaath.gov.bd/api/partner/course/list",

            dataType: 'JSON',
            success: function (data) {
                console.log(data);
            }
        });

        $.ajax({
            type: "GET",
            contentType: 'application/json; charset=utf-8',

            url: "${contextPath}/get-batch-by-course?courseId=" + courseId + "&fiscalYearId=" + fiscalYearId + "&isCreate=0",

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

    function search() {
        var courseId = $("#ddCourse :selected").val();
        var batchId = $("#ddBatch :selected").val();
        var fiscalYearId = $("#ddFiscalYear :selected").val();

        $('#trainingListTable').DataTable({
            "processing": true,
            "pageLength": 10,
            "lengthMenu": [5, 10, 15, 20],
            "destroy": true,
            "paging": true,
            "serverSide": false,
            "searching": true,
            "pagingType": "full_numbers",
            "filter": true,
            "dom": '<"top"i>rt<"bottom"lp><"clear">',
            "language": {
                "url": urlLang
            },
            "ajax": baseUrl + "/usre/training/get-all-training?courseId=" + courseId + "&batchId=" + batchId + "&fiscalYearId=" + fiscalYearId,
            "createdRow": function (row, data, dataIndex) {


            },
            "columns": [
                {"data": function (data) {

                        return data.fiscalYearName;
                    }},
                {"data": function (data) {

                        return data.courseName;
                    }},
                {"data": function (data) {
                        return data.batchName;

                    }},

//                {"data": function (data) {
//                        return data.trainingName;
//
//                    }},

                {"data": function (data) {
                        return data.applicantCount;

                    }},
                {"data": function (data) {

                        var getCert = data.getCertificateCount == undefined ? 0 : data.getCertificateCount;

                        return selectedLocale == "bn" ? getNumberInBangla(getCert.toString()) : getCert;

                    }},

                {"data": function (data) {
                        console.log(data);
                        var url = baseUrl + '/user/training/edit?courseId=' + data.courseId + "&batchId=" + data.batchId + "&fiscalYearId=" + data.fiscalYearId;
                        console.log(url);
                        var name = selectedLocale === "bn" ? "সম্পাদনা করুন" : "Edit";
                        return "<a href='" + url + "'id='btnSelect' value='Edit'  onclick=''>" + name + "</a>"


                    }}

            ],
            "fnDrawCallback": function (oSettings) {
                //  showModalDialog();
                if (selectedLocale === 'bn')
                {
                    $('#trainingListTable  tr').not(":first").each(function () {
                        $(this).find("td").eq(3).html(getNumberInBangla($(this).find("td").eq(3).html()));
                        //  $(this).find("td").eq(4).html(getNumberInBangla($(this).find("td").eq(4).html()));
                        // $(this).find("td").eq(5).html(getNumberInBangla($(this).find("td").eq(5).html()));
                        //  $(this).find("td").eq(6).html(getNumberInBangla($(this).find("td").eq(6).html()));
                    });
                    localizeBanglaInDatatable("trainingListTable");
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
    })
</script>
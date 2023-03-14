<%-- 
    Document   : divisionList
    Created on : Feb 5, 2017, 3:18:48 PM
    Author     : rezwan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script>
    $(document).ready(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
        }

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
//        $("#upazilaListForm").validate({
//            rules: {// checks NAME not ID
//                "division.id": {
//                    required: true
//                },
//                "district.id": {
//                    required: true
//                }
//            },
//            errorPlacement: function (error, element) {
//                error.insertAfter(element);
//            }
//        });
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

        $("#buttonSearch").click(function ()
        {
            $("#upazilaListForm").validate();
            if ($("#upazilaListForm").valid())
            {
                $('#upazilaListTable').DataTable().destroy();
                $('#upazilaListTable').DataTable({
                    "processing": true,
                    "pageLength": 10,
                    "lengthMenu": [10, 25, 50, 100],
                    "bFilter": false,
                    "serverSide": true,
                    "pagingType": "full_numbers",
                    "language": {
                        "url": urlLang
                    },
                    "ajax": {
                        "url": path + "/village/list",
                        "type": "POST",
                        "data": {"villageName":$("#villageName").val(), "divisionId": $('#divisionId').val(), "districtId": $('#districtId').val(), "upazilaId": $('#upazilaId').val(), "unionId": $('#unionId').val()}
                    },
                    "fnDrawCallback": function (oSettings) {
                        if (selectedLocale === 'bn')
                        {
                            localizeBanglaInDatatable("upazilaListTable");
                        }
                    }
                });
            }
        });
    });
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
    function loadUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#union');
        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId, '${village.union.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<form:form id="upazilaListForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="searchParameterForm">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.village" />&nbsp;<spring:message code="label.list" />
            <small></small>
        </h1>
        <div class="pull-right">
            <a href="${contextPath}/village/create" class="btn bg-blue">
                <i class="fa fa-plus-square"></i>
                <spring:message code="addNew" />
            </a>
        </div>

    </section>
    <section class="content">  
        <div class="form-group">
            <div class="col-md-4">
                <div class="form-group">
                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                    <c:choose>
                        <c:when test="${searchParameterForm.isDivisionAvailable eq 'false'}">
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="division.id" id="divisionId" onchange="loadPresentDistrictList(this);">
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
                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                    <c:choose>
                        <c:when test="${searchParameterForm.isDistrictAvailable eq 'false'}">
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
                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.upazila" /></label>
                    <c:choose>
                        <c:when test="${searchParameterForm.isUpazilaAvailable eq 'false'}">
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="upazila.id" id="upazilaId" onchange="loadPresentUnionList(this)">
                                    <form:option value="" label="${select}"></form:option>
                                </form:select>
                                <form:errors path="upazila.id" cssStyle="color:red"></form:errors>
                                </div>
                        </c:when>
                        <c:otherwise>
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
                    <label for="union.id" class="col-md-4 control-label"><spring:message code="label.union" /></label>
                    <div class="col-md-8">
                        <spring:message code='label.select' var="select"/>
                        <form:select class="form-control" path="union.id" id="unionId">
                            <form:option value="" label="${select}"></form:option>
                        </form:select>
                        <form:errors path="union.id" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="nameInEnglish" class="col-md-4 control-label"><spring:message code="label.name" /></label>
                    <div class="col-md-8">
                        <spring:message code='label.nameEn' var="nameEnglish"/>
                        <input class="form-control" id="villageName" placeholder="${name}" />

                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4"></label>    
                    <div class="col-md-8" >
                        <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search" /></button>
                    </div> 
                </div> 

            </div>
            </br>         
            </br> 
            </br>
            <div class="col-xs-12">
                <div class="box">
                    <div class="box-body">
                        <table id="upazilaListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th><spring:message code="label.nameBn" /></th>
                                    <th><spring:message code="label.nameEn" /></th>
                                    <th><spring:message code="label.code" /></th>
                                    <th><spring:message code="label.wardNo" /></th>
                                    <th><spring:message code="label.union" /></th>
                                    <th><spring:message code="label.upazilla" /></th>
                                    <th><spring:message code="label.district" /></th>
                                    <th><spring:message code="label.active" /></th>
                                    <th><spring:message code="edit" var="tooltipEdit"/></th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>                   
    </section>
</form:form>

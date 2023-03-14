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
            loadFormFromCookie($('#bgmeaFactoryListForm'));
            search();
        }
        $("#buttonSearch").click(function ()
        {
            search();
        });
        function search()
        {
            saveFormToCookie($('#bgmeaFactoryListForm'));
            $('#bgmeaFactoryListTable').DataTable().destroy();
            $('#bgmeaFactoryListTable').DataTable({
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
                    "url": path + "/bgmea/list",
                    "type": "POST",
                    "data": {
                        "nameEn": $('#nameEn').val(),
                        "regNo": $('#regNo').val(),
                        "divisionId": $("#divisionId").val(),
                        "districtId": $("#districtId").val(),
                        "upazilaId": $("#upazilaId").val()
                    }
                },

                "fnDrawCallback": function (oSettings) {
                    if (selectedLocale === 'bn')
                    {
                        localizeBanglaInDatatable("bgmeaFactoryListTable");
                    }
                }
            });
        }

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
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="actionUrl" value="" />
<form:form id="bgmeaFactoryListForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="searchParameterForm">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.bgmeaFactory" />&nbsp;<spring:message code="label.list" />
            <small></small>
        </h1>
        <div class="pull-right">
            <a href="${contextPath}/factory/bgmea/create" class="btn bg-blue">
                <i class="fa fa-plus-square"></i>
                <spring:message code="addNew" />
            </a>
        </div>

    </section>
    <section class="content">
        <div class="row">
            <div class="form-group">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="nameEnInput" class="col-md-4 control-label"><spring:message code="label.nameEn" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="nameEnInput"/>
                            <input class="form-control" placeholder="${nameEnInput}"  id="nameEn" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="regNoInput" class="col-md-4 control-label"><spring:message code="label.regNo" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.regNo' var="regNoInput"/>
                            <input class="form-control" placeholder="${regNoInput}"  id="regNo" onkeydown="checkNumber(event, this)"/>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="districtInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <select class="form-control" name="divisionId" id="divisionId" onchange="loadPresentDistrictList(this)">
                                <option value="" label="${select}"></option>
                                <c:forEach items="${divisionList}" var="division">
                                    <option value="${division.id}">${division.nameInBangla}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="districtInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <select class="form-control" name="districtId" id="districtId" onchange="loadPresentUpazilaList(this)">
                                <option value="" label="${select}"></option>                                
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="upazillaInput" class="col-md-4 control-label"><spring:message code="label.upazila" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <select class="form-control" name="upazilaId" id="upazilaId">
                                <option value="" label="${select}"></option>                                
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-8">
                            <button id="buttonSearch" type="button" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"></spring:message></button>        
                            </div>
                        </div>
                    </div>         
                </div>
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body">
                            <table id="bgmeaFactoryListTable" class="table table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th><spring:message code="label.nameBn" /></th>
                                    <th><spring:message code="label.nameEn" /></th>
                                    <th><spring:message code="label.type" /></th>
                                    <th><spring:message code="label.address" /></th>
                                    <th><spring:message code="label.regNo" /></th>
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



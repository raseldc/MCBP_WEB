<%-- 
    Document   : pageList
    Created on : Feb 5, 2017, 10:18:00 AM
    Author     : Philip
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script>
    $(function () {
        if ('${searchParameterForm.isDivisionAvailable}' === 'false')
        {
            loadDivision($('#divisionId'), '');
        } else if ('${searchParameterForm.isDistrictAvailable}' === 'false')
        {
            loadDistrict('${searchParameterForm.division.id}', $('#districtId'));
        } else if ('${searchParameterForm.isUpazilaAvailable}' === 'false')
        {
            loadDistrictFromUpazilaTable('${searchParameterForm.district.id}', $('#upazilaId'));
        } else if ('${searchParameterForm.isUnionAvailable}' === 'false')
        {
            loadCityCorporation('${searchParameterForm.upazila.id}', $('#unionId'));
        }
        var path = '${pageContext.request.contextPath}';
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
        $("#buttonSearch").click(function ()
        {
            $('#grievanceListTable').DataTable().destroy();
            var path = '${pageContext.request.contextPath}';
            $('#grievanceListTable').DataTable({
                "processing": true,
                "pageLength": 10,
                "serverSide": true,
                "bSort": false,
                "pagingType": "full_numbers",
                "dom": '<"top"i>rt<"bottom"lp><"clear">',
                "language": {
                    "url": urlLang
                },
//                columnDefs: [
//                    {width: '15%', targets: 0},
//                    {width: '10%', targets: 1}
//                ],
                "ajax": {
                    "url": path + "/grievance/list",
                    "type": "POST",
                    "data": {
                        "nid": $("#nid").val(),
                        "grievanceType": $("#grievanceType").val(),
                        "grievanceStatus": $("#grievanceStatus").val(),
                        "bgmeaFactoryId": $("#bgmeaFactoryId").val(),
                        "applicantType": $("#applicantType").val()
                    }
                },
                "fnDrawCallback": function (oSettings) {
                    if (selectedLocale === 'bn')
                    {
                        localizeBanglaInDatatable("grievanceListTable");
                    }
                }
            });

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
            loadDistrictFromUpazilaTable(distId, upazillaSelectId);
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#unionId'));
        }
    }
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#unionId');

        if (upazilaId !== '') {
            loadCityCorporation(upazilaId, unionSelectId);
        } else {
            resetSelect(unionSelectId);
        }
    }
</script>
<style>
    #grievanceListTable_length
    {
        float: left;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    } 
</style>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%--<c:if test="${not empty message.message}">                
    <div class="alert 
         <c:if test="${message.messageType eq 'SUCCESS'}">alert-success</c:if> 
         <c:if test="${message.messageType eq 'INFO'}">alert-info</c:if> 
             alert-dismissable">
             <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
         <c:out value="${message.message}"></c:out>
         </div>
</c:if>--%>
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="grievanceList.grievanceList" />
        <small></small>
    </h1>
    <!--    <div class="pull-right">
            <a href="${contextPath}/newgrievance/create" class="btn bg-blue">
                <i class="fa fa-plus-square"></i>
    <spring:message code="addNew" />            
</a>
</div>   -->
</section>
<section class="content">    
    <div class="row">
        <div class="col-xs-12">
            <div class="box">               
                <div class="box-body">
                    <form:form id="pendingListForm" class="form-horizontal" modelAttribute="searchParameterForm">
                        <form:hidden path="applicantType" id="applicantType"/>
                        <div class="form-group">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label for="grievanceType" class="col-md-4 control-label"><spring:message code="label.grievanceType" /></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="grievanceType" > 
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${grievanceTypeList}" itemValue="id" itemLabel="${grievanceTypeName}"></form:options> 
                                        </form:select> 
                                        <form:errors path="grievanceType.id" cssClass="error"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="grievanceStatus" class="col-md-4 control-label"><spring:message code="label.grievanceStatus" /></label>
                                    <div class="col-md-8">
                                        <form:select class="form-control" path="grievanceStatus">  
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${grievanceStatusList}" itemValue="id" itemLabel="${grievanceStatusName}"></form:options> 
                                        </form:select> 
                                        <form:errors path="grievanceStatus.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="nid" class="col-md-4 control-label"><spring:message code='label.nid' /></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.nid' var="nid"/>
                                        <input type="text" placeholder="${nid}" id="nid" name="nid" class="form-control" >
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4 control-label"></label>
                                    <div class="col-md-8 text-left">
                                        <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group" id="bgmeaBlock">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bgmea" /></label>
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
                                                <form:hidden path="bgmeaFactory.id" id="bgmeaFactoryId"/>
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
                            </div>  
                        </div>
                    </form:form>
                    <div class="row"> 
                        <div class="col-xs-12">
                            <table id="grievanceListTable" class="table table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th><spring:message code="label.name" /></th>
                                        <th><spring:message code="label.nid" /></th>
                                        <th><spring:message code="label.grievanceType" /></th>                                                                
                                        <th><spring:message code="grievance.description" /></th>
                                        <th><spring:message code="label.comment" /></th>
                                        <th><spring:message code="grievance.grievanceStatus" /></th>
                                        <th><spring:message code="edit" var="tooltipEdit"/></th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

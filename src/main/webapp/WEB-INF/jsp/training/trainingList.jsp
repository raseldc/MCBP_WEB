<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script>
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        var urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
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
        }

        $("#trainingListForm").validate({
            rules: {// checks NAME not ID
                "fiscalYear.id": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });

        showModalDialog();
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
            var form = $('#trainingListForm');
            form.validate();
            if (form.valid()) {
                var serializedData = $("#trainingListForm").serialize();
                console.log(serializedData);
                $('#trainingListTable').DataTable().destroy();
                var path = '${pageContext.request.contextPath}';
                $('#trainingListTable').DataTable({
                    "processing": true,
                    "pageLength": 10,
                    "lengthMenu": [5, 10, 15, 20],
                    "bSort": false,
                    "serverSide": true,
                    "pagingType": "full_numbers",
                    "dom": '<"top"i>rt<"bottom"lp><"clear">',
                    "language": {
                        "url": urlLang
                    },
                    "columnDefs": [
                        {
                            className: "text-right", "targets": [3, 4],
                        }
                    ],
                    "ajax": {
                        "url": path + "/training/list",
                        "type": "POST",
                        "data": {
                            "fiscalYear": $("#fiscalYear").val(),
                            "trainingType":$("#trainingType").val(),
                            "divisionId": $("#divisionId").val(),
                            "districtId": $("#districtId").val(),
                            "upazilaId": $("#upazilaId").val()

                        }
                    },
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
                            localizeBanglaInDatatable("trainingListTable");
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
        }
    }
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#upazilaId');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId);
        } else {
            resetSelect(upazillaSelectId);
        }
    }
    
</script>
<style>
    #trainingListTable_length
    {
        float:left;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    } 
</style>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="trainingList.trainingList" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/training/create" class="btn bg-blue">
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
                                <label for="fiscalYearInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /><span class="mandatory">*</span></label>
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
                                <label for="trainingTypeInput" class="col-md-4 control-label"><spring:message code="label.trainingType" /></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="trainingType.id"  id="trainingType" onchange="boom()">  
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${trainingTypeList}" itemValue="id" itemLabel="${trainingTypeName}"></form:options> 
                                    </form:select> 
                                    <form:errors path="trainingType.id" cssStyle="color:red"></form:errors>
                                    </div>
                            </div>
                        </div>
                            <div class="col-md-6">
                            <form:hidden path="applicantType" id="applicantType"/>
                            <div class="form-group" id="applicantTypeBlock" style="display: none">
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="training.trainingLocation" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8 labelAsValue">      
                                        <input type="radio" id="headOfficeUser" name="headOfficeUser">&nbsp;<spring:message code="label.headOffice" />
                                        <input type="radio" id="regularUser" name="regularUser">&nbsp;<spring:message code="applicant.regular" />
                                        <c:if test="${sessionScope.userDetail.schemeShortName == 'LMA'}">
                                            <input type="radio" id="bgmeaUser" name="bgmeaUser">&nbsp;<spring:message code="applicant.bgmea" />
                                            <input type="radio" id="bkmeaUser" name="bkmeaUser">&nbsp;<spring:message code="applicant.bkmea" />
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${sessionScope.userDetail.schemeShortName == 'LMA'}">
                                <c:if test="${sessionScope.userDetail.userType.displayName eq 'BGMEA' || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">
                                    <!--not required: payroll should be generated for all factory-->
                                </c:if>
                                <c:if test="${sessionScope.userDetail.userType.displayName eq 'BKMEA' || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">
                                    <!--not required: payroll should be generated for all factory-->
                                </c:if>  
                            </c:if>
                            <c:if test="${sessionScope.userDetail.userType.displayName eq 'Others' || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">        
                                <div id="regularBlock">        
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                                        <c:choose>
                                            <c:when test="${searchParameterForm.isDivisionAvailable eq 'false'}">
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
                                                    <form:select class="form-control" path="upazila.id" id="upazilaId" >
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

                                </div>
                            </c:if>
                            <div class="form-group">
                                <div class="col-md-4"></div>                                    
                                <div class="col-md-8">
                                    <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                                </div>
                            </div>
                        </div>
                    </div>

                </form:form>
                <table id="trainingListTable" class="table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th><spring:message code="training.trainingType" /></th>                                                                
                            <th><spring:message code="training.trainer" /></th>   
                            <th><spring:message code="training.trainingLocation" /></th>    
                            <th><spring:message code="training.beneficiaryInclusion" /></th>    
                            <th><spring:message code="training.numberOfPerticipants" /></th>                                                                
                            <th><spring:message code="training.trainingCost" /></th>                                                                
                            <th><spring:message code="training.startDate" /></th>                                                                
                            <th><spring:message code="label.durationDay" /></th>                                                                
                            <th><spring:message code="training.comment" /></th>                                                                
                            <th><spring:message code="edit" var="tooltipEdit"/></th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
</section>

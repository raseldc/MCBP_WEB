<%-- 
    Document   : applicantList
    Created on : Feb 5, 2017, 10:18:00 AM
    Author     : Philip
--%>

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
        $('#applicationId').keypress(function (e) {
            if (e.which == 13) {
                $("#buttonSearch").click();
                return false;    //<---- Add this line
            }
        });

        makeUnijoyEditor('applicationId'); // NID actually
        var applicationId = document.getElementById("applicationId");
        applicationId.addEventListener("keydown", function (event) {
            checkNumberWithLengthWithPasteOption(event, this, 17);
        });


        $("#pendingListForm").validate({
            rules: {// checks NAME not ID
                "fiscalYear.id": {
                    required: true
                },
                "scheme.id": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });

        showModalDialog();

        $("thead").attr("style", "visibility:hidden");
        $("#buttonSearch").click(function ()
        {
            $("thead").attr("style", "visibility:visible")
            var form = $('#pendingListForm');
            form.validate();
            if (form.valid()) {
                var serializedData = $("#pendingListForm").serialize();
                console.log(serializedData);
                $('#applicantListTable').DataTable().destroy();
                var path = '${pageContext.request.contextPath}';
                $('#applicantListTable').DataTable({
                    "processing": true,
                    "pageLength": 10,
                    "lengthMenu": [5, 10, 15, 20],
                    "serverSide": true,
                    "bSort": false,
                    "pagingType": "full_numbers",
                    "dom": '<"top"i>rt<"bottom"lp><"clear">',
                    "language": {
                        "url": urlLang
                    },
                    "ajax": {
                        "url": path + "/applicant/pendingOnlineList",
                        "type": "POST",
                        "data": {
                            "fiscalYear": $("#fiscalYear").val(),
                            "scheme": $("#scheme").val(),
                            "applicationId": $("#applicationId").val(),
                            "divisionId": $("#divisionId").val(),
                            "districtId": $("#districtId").val(),
                            "upazilaId": $("#upazilaId").val(),
                            "unionId": $("#unionId").val()
                        }
                    },
                    "fnDrawCallback": function (oSettings) {
                        showModalDialog();
                        if (selectedLocale === 'bn')
                        {
                            localizeBanglaInDatatable("applicantListTable");
                        }
                    }
                });
            }
        });

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
    });

    function loadApplicant(id) {
        var data = getDataFromUrl("viewApplicant/" + id);
//        alert(data);
        $('#myModal .modal-body').html(data);
        $('#myModalLabel').val('test');
        $('#myModal .modal-body').css({height: screen.height * .60});
        $('#myModal').modal('show');

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
<style>
    .labelAsValue{
        padding-top: 7px;
    }
    #applicantListTable_length {
        float:left;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    } 

</style>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:url var="formAction" value="/page"></c:url>
<c:if test="${not empty saveMsg.message}">                
    <div class="alert 
         <c:if test="${saveMsg.messageType eq 'SUCCESS'}">alert-success</c:if> 
         <c:if test="${saveMsg.messageType eq 'INFO'}">alert-info</c:if> 
             alert-dismissable">
             <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
         <c:out value="${saveMsg.message}"></c:out>
         </div>
</c:if>
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="onlineApplicant" />&nbsp;<spring:message code="label.list" />
        <small></small>
    </h1>
    <!--    <div class="pull-right">
            <a href="${contextPath}/applicant/applicantForm" class="btn bg-blue">
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
                        <div class="form-group">
                            <div class="col-md-6">                                
                                <div class="form-group">
                                    <label for="schemeInput" class="col-md-4 control-label"><spring:message code="label.scheme" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="scheme.id"  id="scheme"> 
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${schemeList}" itemValue="id" itemLabel="${schemeName}"></form:options> 
                                        </form:select> 
                                        <form:errors path="scheme.id" cssClass="error"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /><span class="mandatory">*</span></label>
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
                                        <label for="status" class="col-md-4 control-label"><spring:message code='label.nid' var="nid" />${nid}</label>
                                    <div class="col-md-8">
                                        <input type="text" id="applicationId" name="applicationId" class="form-control" placeholder="${nid}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    &nbsp;
                                </div>


                            </div>
                            <div class="col-md-6">
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
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.union" /></label>
                                    <c:choose>
                                        <c:when test="${searchParameterForm.isUnionAvailable eq 'false'}">
                                            <div class="col-md-8">
                                                <spring:message code='label.select' var="select"/>
                                                <form:select class="form-control" path="union.id" id="unionId">
                                                    <form:option value="" label="${select}"></form:option>
                                                </form:select>
                                                <form:errors path="union.id" cssStyle="color:red"></form:errors>
                                                </div>
                                        </c:when>
                                        <c:otherwise>
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
                        </div>
                        <div class="form-group">
                            <div class="col-md-2 col-md-offset-8">
                                <div>
                                    <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                                </div>
                            </div>
                        </div>
                    </form:form>
                    <div class="table-responsive">
                        <table id="applicantListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <!--<th><spring:message code="label.photo" /></th>-->
                                    <!--<th><spring:message code="applicantList.applicantId" /></th>-->
                                    <th><spring:message code="label.nameBn" /></th>
                                    <th><spring:message code="label.nameEn" /></th>
                                    <th><spring:message code="label.nid" /></th>
                                    <th><spring:message code="label.dob" /></th>
                                    <th><spring:message code="label.mobileNo" /></th>
                                    <th><spring:message code="edit" var="tooltipEdit"/></th>
                                    <th><spring:message code="view" var="tooltipView"/></th>
                                </tr>
                            </thead>
                            <tbody>                            
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

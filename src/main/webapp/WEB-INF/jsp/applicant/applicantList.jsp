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

<style>
    #applicantListTable_length {
        float:left;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    } 
    /*    .modal {
            text-align: center;
        }
        .modal-dialog {
            display: inline-block;
            text-align: left;
            vertical-align: middle;
        }
        @media screen and (min-width: 768px) { 
            .modal:before {
                display: inline-block;
                vertical-align: middle;
                content: " ";
                height: 100%;
            }
        }*/

</style>
<script>
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");

        $('#applicationId').keypress(function (e) {
            if (e.which == 13) {
                $("#buttonSearch").click();
                return false;    //<---- Add this line
            }
        });

        if (selectedLocale == 'bn') {
            makeUnijoyEditor('applicationId'); // NID actually
        }
        var applicationId = document.getElementById("applicationId");
        applicationId.addEventListener("keydown", function (event) {
            checkNumberWithLengthWithPasteOption(event, this, 17);
        });

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

        $("#buttonSearch").click(function ()
        {
            loadSearchResult();
        });

//        if ('${sessionScope.userDetail.schemeShortName}' == 'LMA')
//        {
            if ('${sessionScope.userDetail.userType.displayName}' == 'Ministry' || '${sessionScope.userDetail.userType.displayName}' == 'Directorate')
            {
                $("#applicantTypeBlock").show();
                $('#bgmeaUser').iCheck('check');
                $('#bkmeaUser').iCheck('check');
                $('#regularUser').iCheck('check');
            }
            if ('${sessionScope.userDetail.userType.displayName}' == 'BGMEA')
            {
                $('#bgmeaUser').iCheck('check');
            }
            if ('${sessionScope.userDetail.userType.displayName}' == 'BKMEA')
            {
                $('#bkmeaUser').iCheck('check');
            }
            if ('${sessionScope.userDetail.userType.displayName}' == 'Others')
            {
                $('#regularUser').iCheck('check');
            }
//        } else
//        {
//            $('#regularUser').iCheck('check');
//        }

        if ('${searchParameterForm.isDistrictAvailable}' === 'false')
        {
            loadDistrict('${searchParameterForm.division.id}', $('#districtId'));
        } else if ('${searchParameterForm.isUpazilaAvailable}' === 'false')
        {
            loadUpazilla('${searchParameterForm.district.id}', $('#upazilaId'));
        } else if ('${searchParameterForm.isUnionAvailable}' === 'false')
        {
            loadUnion('${searchParameterForm.upazila.id}', $('#unionId'));
        }
        $('#regularUser').on('ifChanged', function (event) {
            $("#buttonSearch").show();
            if ($('#regularUser').is(":checked"))
            {
                $("#regularBlock").show();
            } else
            {
                $("#regularBlock").hide();
                loadDivision($('#divisionId'), '');
                resetSelect($('#districtId'));
                resetSelect($('#upazilaId'));
                resetSelect($('#unionId'));
            }
            if (!$('#regularUser').is(":checked") && !$('#bgmeaUser').is(":checked") && !$('#bkmeaUser').is(":checked"))
            {
                $("#buttonSearch").hide();
            }
        });
        $('#bgmeaUser').on('ifChanged', function (event) {
            $("#buttonSearch").show();
            if ($('#bgmeaUser').is(":checked"))
            {
                $("#bgmeaBlock").show();
            } else
            {
                $("#bgmeaBlock").hide();
                $("#bgmeaFactoryId").prop("selectedIndex", 0);
            }
            if (!$('#regularUser').is(":checked") && !$('#bgmeaUser').is(":checked") && !$('#bkmeaUser').is(":checked"))
            {
                $("#buttonSearch").hide();
            }
        });
        $('#bkmeaUser').on('ifChanged', function (event) {
            $("#buttonSearch").show();
            if ($('#bkmeaUser').is(":checked"))
            {
                $("#bkmeaBlock").show();
            } else
            {
                $("#bkmeaBlock").hide();
                $("#bkmeaFactoryId").prop("selectedIndex", 0);
            }
            if (!$('#regularUser').is(":checked") && !$('#bgmeaUser').is(":checked") && !$('#bkmeaUser').is(":checked"))
            {
                $("#buttonSearch").hide();
            }
        });
    });

    function loadSearchResult() {
        var urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }

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
                "serverSide": true,
                "bSort": false,
                "pagingType": "full_numbers",
                "dom": '<"top"i>rt<"bottom"lp><"clear">',
                "language": {
                    "url": urlLang
                },
                "columnDefs": [                    
//                        {"width": "110", "targets": [5]},
                 {className: "text-left", "targets": [0]}                    
                ],
                "ajax": {
                    "url": path + "/applicant/list",
                    "type": "POST",
                    "data": {
                        "fiscalYear": $("#fiscalYear").val(),
                        "scheme": $("#scheme").val(),
                        "applicationId": $("#applicationId").val(),
                        "divisionId": $("#divisionId").val(),
                        "districtId": $("#districtId").val(),
                        "upazilaId": $("#upazilaId").val(),
                        "unionId": $("#unionId").val(),
                        "bgmeaFactoryId": $("#bgmeaFactoryId").val(),
                        "bkmeaFactoryId": $("#bkmeaFactoryId").val(),
                        "regularUser": $('#regularUser').is(":checked") === true ? 1 : 0,
                        "bgmeaUser": $('#bgmeaUser').is(":checked") === true ? 1 : 0,
                        "bkmeaUser": $('#bkmeaUser').is(":checked") === true ? 1 : 0
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
    }


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
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:url var="formAction" value="/page"></c:url>
    <section class="content-header clearfix">
        <h1 class="pull-left">
        <spring:message code="allApplicant" />&nbsp;<spring:message code="label.list" />
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
            <form:form id="pendingListForm" class="form-horizontal" modelAttribute="searchParameterForm">
                <div class="form-group">
                    <div class="col-md-6">
                        <c:if test="${sessionScope.userDetail.userType.displayName eq 'Others' && sessionScope.userDetail.union == null || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">
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
                        </c:if>        
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
                        <div class="form-group" id="applicantTypeBlock" style="display: none">
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.applicantType" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <label class="checkbox-inline icheck">
                                        <input id="regularUser" name="regularUser" type="checkbox" >&nbsp;<spring:message code="applicant.regular" />
                                    </label>
                                    <label class="checkbox-inline icheck">
                                        <input id="bgmeaUser" name="bgmeaUser" type="checkbox" >&nbsp;<spring:message code="applicant.bgmea" />
                                    </label>
                                    <label class="checkbox-inline icheck">
                                        <input id="bkmeaUser" name="bkmeaUser" type="checkbox" >&nbsp;<spring:message code="applicant.bkmea" />
                                    </label>                                              
                                </div>
                            </div>
                        </div>                                  
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
                                    <label for="middleNameInput" class="col-md-4 control-label">
                                        <c:choose>
                                            <c:when test="${sessionScope.userDetail.schemeShortName == 'MA'}">
                                                <spring:message code="label.upazila" />
                                            </c:when>
                                            <c:otherwise>
                                                <spring:message code="label.districtOrUpazila"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </label>
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
                                    <label for="middleNameInput" class="col-md-4 control-label">
                                        <c:choose>
                                            <c:when test="${sessionScope.userDetail.schemeShortName == 'MA'}">
                                                <spring:message code="label.union" />
                                            </c:when>
                                            <c:otherwise>
                                                <spring:message code="label.municipalOrCityCorporation"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </label>
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
                        </c:if>
                        
                            <c:if test="${sessionScope.userDetail.userType.displayName eq 'BGMEA' || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">
                                <div class="form-group" id="bgmeaBlock">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bgmea" /></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="bgmeaFactory.id" id="bgmeaFactoryId">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${bgmeaFactoryList}" itemValue="id" itemLabel="${bgmeaFactoryName}"></form:options> 
                                        </form:select>
                                        <form:errors path="bgmeaFactory.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                            </c:if>
                            <c:if test="${sessionScope.userDetail.userType.displayName eq 'BKMEA' || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">
                                <div class="form-group" id="bkmeaBlock">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bkmea" /></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="bkmeaFactory.id" id="bkmeaFactoryId">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${bkmeaFactoryList}" itemValue="id" itemLabel="${bkmeaFactoryName}"></form:options> 
                                        </form:select>
                                        <form:errors path="bkmeaFactory.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                            </c:if>  
                                   
                        <div class="form-group">
                            <div class="col-md-4"></div>
                            <div class="col-md-8">
                                <div>
                                    <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>   
    </div>  
    <div class="row">
        <div class="col-xs-12">  
            <div class="box">                
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="applicantListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
<!--                                    <th><spring:message code="label.photo" /></th>-->
                                    <!--<th><spring:message code="applicantList.applicantId" /></th>-->
                                    <th><spring:message code="label.nameBn" /></th>
                                    <th><spring:message code="label.nameEn" /></th>
                                    <th><spring:message code="label.nid" /></th>
                                    <th><spring:message code="label.dob" /></th>
                                    <th><spring:message code="label.mobileNo" /></th>
                                    <th><spring:message code="dashboard.applicantStatus" /></th>
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

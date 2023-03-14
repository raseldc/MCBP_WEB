<%-- 
    Document   : beneficiaryChangeStatusListUnion
    Created on : Oct 23, 2018, 3:32:43 PM
    Author     : user
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
</style>
<script>
    var urlLang = "";
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");

        $('#applicantId').keypress(function (e) {
            if (e.which == 13) {
                $("#buttonSearch").click();
                return false;    //<---- Add this line
            }
        });
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
            makeUnijoyEditor('applicantId'); // NID actually
            makeUnijoyEditor('mobileNo'); // NID actually
            makeUnijoyEditor('accountNo'); // NID actually
        }
        var applicantId = document.getElementById("applicantId");
        applicantId.addEventListener("keydown", function (event) {
            checkNumberWithLengthWithPasteOption(event, this, 17);
        });
        var mobileNo = document.getElementById("mobileNo");
        mobileNo.addEventListener("keydown", function (event) {
            checkNumberWithLengthWithPasteOption(event, this, 11);
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
    });

    function loadSearchResult() {
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
                    "url": path + "/beneficiary/changeStatusList",
                    "type": "POST",
                    "data": {
                        "fiscalYear": $("#fiscalYear").val(),
                        "scheme": $("#scheme").val(),
                        "applicantId": $("#applicantId").val(),
                        "mobileNo": $("#mobileNo").val(),
                        "accountNo": $("#accountNo").val(),
                        "divisionId": $("#divisionId").val(),
                        "districtId": $("#districtId").val(),
                        "upazilaId": $("#upazilaId").val(),
                        "unionId": $("#unionId").val(),
                        "applicantType": $("#applicantType").val(),
                        "status": $("#ddStatus").val()

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
        <spring:message code="label.beneficiaryStatusUpdate" />
        <small></small>
    </h1>   
</section>
<section class="content">    
    <div class="row">
        <div class="col-xs-12">
            <form:form id="pendingListForm" class="form-horizontal" modelAttribute="searchParameterForm">
                <form:hidden path="applicantType" id="applicantType"/>
                <div class="form-group">
                    <div class="col-md-6">                             
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
                                <input type="text" id="applicantId" name="applicantId" class="form-control" placeholder="${nid}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="status" class="col-md-4 control-label"><spring:message code='label.mobileNo' var="mobileNo" />${mobileNo}</label>
                            <div class="col-md-8">
                                <input type="text" id="mobileNo" name="mobileNo" class="form-control" placeholder="${mobileNo}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="status" class="col-md-4 control-label"><spring:message code='label.accountNo' var="accountNo" />${accountNo}</label>
                            <div class="col-md-8">
                                <input type="text" id="accountNo" name="accountNo" class="form-control" placeholder="${accountNo}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="status" class="col-md-4 control-label"><spring:message code="label.status" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="status"  id="ddStatus">  
                                    <form:option value="-1" label="${select}"></form:option>
                                    <form:option value="0" label=''><spring:message code="label.active" /></form:option>
                                    <form:option value="2" label=''><spring:message code="label.inactive" /></form:option>
                                </form:select> 
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">                                                      
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
                                    <th><spring:message code="label.nameBn" /></th>
                                    <th><spring:message code="label.nameEn" /></th>
                                    <th><spring:message code="label.nid" /></th>
                                    <th><spring:message code="label.dob" /></th>
                                    <th><spring:message code="label.mobileNo" /></th>
                                    <th><spring:message code="label.accountNo" /></th>
                                    <th><spring:message code="label.status" /></th>
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


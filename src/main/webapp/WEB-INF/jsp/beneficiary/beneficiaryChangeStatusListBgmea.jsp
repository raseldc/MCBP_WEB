<%-- 
    Document   : beneficiaryChangeStatusListBgmea
    Created on : Oct 23, 2018, 5:07:39 PM
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
        $('#applicationId').keypress(function (e) {
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
                    "url": path + "/beneficiary/changeStatusList",
                    "type": "POST",
                    "data": {
                        "fiscalYear": $("#fiscalYear").val(),
                        "scheme": $("#scheme").val(),
                        "applicantId": $("#applicantId").val(),
                        "mobileNo": $("#mobileNo").val(),
                        "accountNo": $("#accountNo").val(),
                        "bgmeaFactoryId": $("#bgmeaFactoryId").val(),
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

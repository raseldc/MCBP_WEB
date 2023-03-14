<%-- 
    Document   : grievanceReport
    Created on : Feb 28, 2017, 12:37:35 PM
    Author     : Philip
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $("#reportForm").validate({
            rules: {// checks 
                "scheme.id": {
                    required: true
                },
                "reportGenerationType": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
        $('#beneficiaryStatus option:eq(1)').prop('selected', true);
        $('#details').on('ifChanged', function (event) {
            if ($('#details').is(':checked')) {
                $("#unionId").prop('disabled', false);
            }
        });
        $('#group').on('ifChanged', function (event) {
            if ($('#group').is(':checked')) {
                $("#unionId").prop('disabled', false);
            }
        });
        $('#summary').on('ifChanged', function (event) {
            if ($('#summary').is(':checked')) {
                $("#unionId").val("");
                $("#unionId").prop('disabled', true);
            }
        });

        if ('${reportParameterForm.isUnionAvailable}' === 'true')
        {
            $("#unionId").val('${reportParameterForm.union.id}');
            if ("${pageContext.response.locale}" === "bn")
            {
                $("#unionName").val('${reportParameterForm.union.nameInBangla}');
            } else
            {
                $("#unionName").val('${reportParameterForm.union.nameInEnglish}');
            }
        }
        if ('${reportParameterForm.isDivisionAvailable}' === 'false')
        {
            loadDivision($('#divisionId'), '');
        } else if ('${reportParameterForm.isDistrictAvailable}' === 'false')
        {
            loadDistrict('${reportParameterForm.division.id}', $('#districtId'));
        } else if ('${reportParameterForm.isUpazilaAvailable}' === 'false')
        {
            loadUpazilla('${reportParameterForm.district.id}', $('#upazilaId'));
        } else if ('${reportParameterForm.isUnionAvailable}' === 'false')
        {
            loadUnion('${reportParameterForm.upazila.id}', $('#unionId'));
        }

    });
    function makeChanges()
    {
        //Add or Remove validation rules
        if ($("#reportGenerationType").val() == "Summary" || $("#reportGenerationType").val() == "Upazila")
        {
            $('#pr').iCheck('check');
        } else {
            $('#ln').iCheck('check');
        }
        if ($("#reportGenerationType").val() == "Payment" || $("#reportGenerationType").val() == "Summary")
        {
            $("#unionId").val("");
            $("#unionId").attr("disabled", true);
        } else {
            $("#unionId").attr("disabled", false);
        }
    }
//    function submitForm(e) {
//        var form = $('#reportForm');
//        e.preventDefault();
//        form.validate();
//        if (form.valid()) {
//            showReportInPopup(form);
//        }
//    }



    function loadPresentDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#districtId');
        if (divId !== '') {
            loadDistrict(divId, distSelectId, '${user.district.id}');
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
            loadUpazilla(distId, upazillaSelectId, '${user.upazilla.id}');
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#unionId'));
        }
    }
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#unionId');

        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId, '${user.union.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!--<a href="${contextPath}/endpointsdoc">API doc </a>-->
<form:form action="${contextPath}/beneficiaryReport" method="post" target="_blank" class="form-horizontal" id="reportForm" modelAttribute="reportParameterForm">            
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code='reportHeader.beneficiaryReport'/>
        </h1>
    </section>    
    <spring:message code='label.select' var="select"/>
    <section class="content">        
        <div class="row">
            <div class="col-md-12"> 
                <legend><spring:message code='reportSetting'/></legend>
                <div class="col-md-12">
                    <div class="form-group">
                        <label for="reportGenerationType" class="col-md-2 control-label"><spring:message code="reportGenerationType" /><span class="mandatory">*</span></label>
                        <div class="col-md-4">
                            <form:select class="form-control" path="reportGenerationType" onchange="makeChanges()">
                                <form:option value=""><spring:message code='label.select'/></form:option>
                                <%--<form:option value="Details"><spring:message code='report.detailReport'/></form:option>--%>
                                <form:option value="Details2"><spring:message code='report.detailGroupReport'/></form:option>
                                <form:option value="Summary"><spring:message code='report.summaryReport'/></form:option>
                                <c:if test="${sessionScope.userDetail.userType.displayName == 'Ministry' || sessionScope.userDetail.userType.displayName == 'Directorate'}">
                                    <form:option value="Upazila"><spring:message code='report.upazilaBasedReport'/></form:option>
                                </c:if>
                                <form:option value="Payment"><spring:message code='report.paymentBasedReport'/></form:option>
                                <form:option value="WithoutMobileNo"><spring:message code='report.withoutMobileNo'/></form:option>
                                <%--<form:option value="WithoutAccNo"><spring:message code='report.withoutAccNo'/></form:option>--%>
                                <%--<form:option value="WithInvalidAccNo"><spring:message code='report.withInvalidAccNo'/></form:option>--%>
                                   <form:option value="LMMISExist"><spring:message code='report.LMMisExit'/></form:option>

                            </form:select>
                            <form:errors path="reportGenerationType" cssClass="error"></form:errors>
                            </div> 
                        </div>        
                        <div class="form-group">
                            <label for="reportOrientation" class="col-md-2 control-label"><spring:message code="reportOrientation" /><span class="mandatory">*</span></label>
                        <div class="col-md-10 labelAsValue">                        
                            <form:radiobutton path="reportOrientation" id="pr" value="Portrait"/>&nbsp;<spring:message code='report.portrait'/>&nbsp;
                            <form:radiobutton path="reportOrientation" id="ln" value="Landscape" checked="checked"/>&nbsp;<spring:message code='report.landscape'/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="reportExportType" class="col-md-2 control-label"><spring:message code="reportExportType" /><span class="mandatory">*</span></label>
                        <div class="col-md-10 labelAsValue">                        
                            <form:radiobutton path="reportExportType" value="pdf"  checked="checked"/>&nbsp;<spring:message code='report.pdf'/>&nbsp;
                            <form:radiobutton path="reportExportType" value="excel"/>&nbsp;<spring:message code='report.excel'/>
                        </div>
                    </div>  
                    <div class="form-group">
                        <label for="lang" class="col-md-2 control-label"><spring:message code="lang" /><span class="mandatory">*</span></label>
                        <div class="col-md-10 labelAsValue">                        
                            <form:radiobutton path="language" value="bn"  checked="checked"/>&nbsp;<spring:message code='report.bangla'/>&nbsp;
                            <form:radiobutton path="language" value="en"/>&nbsp;<spring:message code='report.english'/>
                        </div>
                    </div>    
                </div>                    
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <legend><spring:message code='reportParameter'/></legend>
                <div class="col-md-6">                    
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="paymentCycle.label.fiscalYear" /></label>
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
                            <label for="beneficiaryStatusInput" class="col-md-4 control-label"><spring:message code="beneficiaryStatus" /></label>
                        <div class="col-md-8">
                            <form:select class="form-control" path="beneficiaryStatus">
                                <form:option value="" label="${select}"></form:option>
                                <c:if test="${pageContext.response.locale=='bn'}">
                                    <form:options items="${beneficiaryStatusList}"  itemLabel="displayNameBn"></form:options>
                                </c:if>
                                <c:if test="${pageContext.response.locale=='en'}">
                                    <form:options items="${beneficiaryStatusList}"  itemLabel="displayName"></form:options>
                                </c:if>
                            </form:select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="beneficiaryStatusInput" class="col-md-4 control-label"><spring:message code="label.schemeAttributeOrderingType" /></label>
                        <div class="col-md-8" >
                            <select class="form-control" id="orderBy" name="orderBy">
                                <option value="" label="${select}">${select}</option>
                                <option value="0" label="<spring:message code="fiscalYear.label.fiscalYearNameInEnglish" />"><spring:message code="fiscalYear.label.fiscalYearNameInEnglish" /></option>
                                <option value="1" label="<spring:message code="label.nid" />"><spring:message code="label.nid" /></option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <form:hidden path="applicantType" id="applicantType"/>                   
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                        <c:choose>
                            <c:when test="${reportParameterForm.isDivisionAvailable eq 'false'}">
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
                                            ${reportParameterForm.division.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${reportParameterForm.division.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                        <c:choose>
                            <c:when test="${reportParameterForm.isDistrictAvailable eq 'false'}">
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
                                            ${reportParameterForm.district.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${reportParameterForm.district.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label">
                            <spring:message code="label.upazila" />
                        </label>
                        <c:choose>
                            <c:when test="${reportParameterForm.isUpazilaAvailable eq 'false'}">
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
                                            ${reportParameterForm.upazila.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${reportParameterForm.upazila.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label">
                            <spring:message code="label.union" />
                            </label>
                            <c:choose>
                                <c:when test="${reportParameterForm.isUnionAvailable eq 'false'}">
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="union.id" id="unionId" >
                                            <form:option value="" label="${select}"></form:option>
                                        </form:select>
                                        <form:errors path="union.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-8 labelAsValue">
                                        <input type="hidden" id="unionId" name="union.Id">
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                ${reportParameterForm.union.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${reportParameterForm.union.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <div class="col-md-4"></div> 
                            <div class="col-md-8">
                                <spring:message code="viewReport" var="viewReport"/>
                                <button type="submit" class="btn bg-blue"><i class="fa fa-file" aria-hidden="true"></i>${viewReport}</button> 
                            </div>    
                        </div>    
                    </div>
                </div>
            </div>
        </section>
</form:form>

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
        $("#paymentReportForm").validate({
            rules: {// checks NAME not ID
                "scheme.id": {
                    required: true
                },
                "fiscalYear.id": {
                    required: true
                },
                "paymentCycle.id": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });

        $('#details').on('ifChanged', function (event) {
            if ($('#details').is(':checked')) {
                $("#unionId").prop('disabled', false);
                $('#ln').iCheck('check');
            }
        });
        $('#summary').on('ifChanged', function (event) {
            if ($('#summary').is(':checked')) {
                $("#unionId").val("");
                $("#unionId").prop('disabled', true);
                $('#pr').iCheck('check');
            }
        });

        
    });

    function loadActivePaymentCycle(selectObject) {
        var fiscalYearId = selectObject.value;
        var paymentCycleSelectId = $('#paymentCycle');
        if (fiscalYearId !== '') {
            loadParentPaymentCycle(fiscalYearId, paymentCycleSelectId, true);
        } else {
            resetSelect(paymentCycleSelectId);
        }
    }
    function loadParentPaymentCycle(fiscalYearId, paymentCycleSelectId, isActive) {

        var ajaxUrl = "";
        var isRural;
        if (isActive === true)
        {
            ajaxUrl = contextPath + "/getParentPaymentCycleIoList/" + fiscalYearId + "/3";
        } else
        {
            ajaxUrl = contextPath + "/getAllPaymentCycle/" + fiscalYearId;
        }

        $.ajax({
            type: "GET",
            url: ajaxUrl,
            async: false,
            dataType: "json",
            success: function (response) {
                paymentCycleSelectId.find('option').remove();
                $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(paymentCycleSelectId);
                $.each(response, function (index, value) {
                    if (selectedLocale === 'en') {
                        $('<option>').val(value.id).text(value.nameInEnglish).appendTo(paymentCycleSelectId);
                    } else if (selectedLocale === 'bn') {
                        $('<option>').val(value.id).text(value.nameInBangla).appendTo(paymentCycleSelectId);
                    }
                });
            },
            failure: function () {
                log("loading Payment Cycle failed!!");
            }
        });
    }

</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<form:form action="${contextPath}/paymentReport" method="POST" target="_blank" class="form-horizontal" id="paymentReportForm" modelAttribute="reportParameterForm" >
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code='reportHeader.paymentReport'/>
        </h1>
    </section>    
    <spring:message code='label.select' var="select"/>
    <section class="content">        
        <div class="row">
            <div class="col-md-12">
                <legend><spring:message code='reportSetting'/></legend>
                <div class="col-md-6"> 
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="reportGenerationType" /><span class="mandatory">*</span></label>
                        <div class="col-md-8 labelAsValue">                        
                            <form:radiobutton path="reportGenerationType" id="summary" value="Summary" checked="checked"/>&nbsp;<spring:message code='report.summaryReport'/> 
                            <form:radiobutton path="reportGenerationType" id="details" value="Details"/>&nbsp;<spring:message code='report.detailReport'/> 
<!--                            <form:radiobutton path="reportGenerationType" value="Group"/>&nbsp;<spring:message code='report.groupReport'/> -->
                        </div>
                    </div>

                    <!--                <div class="col-md-6"> -->
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="reportOrientation" /><span class="mandatory">*</span></label>
                        <div class="col-md-8 labelAsValue">                        
                            <form:radiobutton path="reportOrientation" value="Portrait" checked="checked"/>&nbsp;<spring:message code='report.portrait'/>
                            <form:radiobutton path="reportOrientation" value="Landscape"/>&nbsp;<spring:message code='report.landscape'/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="reportExportType" class="col-md-4 control-label"><spring:message code="reportExportType" /><span class="mandatory">*</span></label>
                        <div class="col-md-8 labelAsValue">                        
                            <form:radiobutton path="reportExportType" value="pdf"  checked="checked"/>&nbsp;<spring:message code='report.pdf'/>
                            <form:radiobutton path="reportExportType" value="excel"/>&nbsp;<spring:message code='report.excel'/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lang" class="col-md-4 control-label"><spring:message code="lang" /><span class="mandatory">*</span></label>
                        <div class="col-md-8 labelAsValue">                        
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
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="paymentCycle.label.fiscalYear" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="fiscalYear.id"  id="fiscalYear" onchange="loadActivePaymentCycle(this)" autofocus="autofocus">  
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                            </form:select> 
                            <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.paymentCycle" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="paymentCycle.id"  id="paymentCycle" autofocus="autofocus"> 
                                <form:option value="" label="${select}"></form:option>
                            </form:select> 
                            <form:errors path="paymentCycle.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="paymentStatusInput" class="col-md-4 control-label"><spring:message code="payment.label.status" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="paymentStatus" autofocus="autofocus"> 
                                <form:option value="" label="${select}"></form:option>
                                <c:forEach items="${paymentStatusList}" var="paymentStatus">
                                    <c:if test="${pageContext.response.locale=='en'}">   
                                        <form:option value="${paymentStatus}" label="${paymentStatus.displayName}">${paymentStatus.displayName}</form:option>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='bn'}">   
                                        <form:option value="${paymentStatus}" label="${paymentStatus.displayNameBn}">${paymentStatus.displayNameBn}</form:option>
                                    </c:if>
                                </c:forEach>
                            </form:select> 
                            <form:errors path="paymentStatus" cssStyle="color:red"></form:errors>
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
                    <form:hidden path="payrollListType" id="payrollListType"/> 
                    <c:if test="${type eq 'bgmea'}">
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
                    <c:if test="${type eq 'bkmea'}">
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
                        <label class="col-md-4"></label>
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

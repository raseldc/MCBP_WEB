<%-- 
    Document   : grievanceReport
    Created on : Feb 28, 2017, 12:37:35 PM
    Author     : Philip
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/MonthPicker.min.js"/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/MonthPicker.min.css" />">
<script type="text/javascript" src="<c:url value="/resources/js/MonthPicker-bn.js"/>" ></script>
<script>
    var startYearOfFY;
    var endYearOfFY;
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
        $('#startDate').MonthPicker({StartYear: startYearOfFY, Button: false, MonthFormat: 'mm-yy', OnAfterChooseMonth: function (selectedDate) {
                if (selectedLocale === 'bn') {
                    $("#" + this.id).val(getNumberInBangla($("#" + this.id).val()));
                }
            }});
        $('#endDate').MonthPicker({StartYear: startYearOfFY, Button: false, MonthFormat: 'mm-yy', OnAfterChooseMonth: function (selectedDate) {
                if (selectedLocale === 'bn') {
                    $("#" + this.id).val(getNumberInBangla($("#" + this.id).val()));
                }
            }});
    });
    function loadFiscalYearInfo(id) {
        var fyId = $("#" + id + "").val();

        $.ajax({
            type: "POST",
            url: contextPath + "/getFiscalYear",
            async: false,
            data: {'fiscalYearId': fyId},
            datatype: "json",
            success: function (result)
            {
                startYearOfFY = result;
                endYearOfFY = startYearOfFY + 1;
                console.log('startYearOfFY=' + startYearOfFY + ", endYearOfFY=" + endYearOfFY);

                $('#startDate').MonthPicker('option', 'StartYear', startYearOfFY);
                $('#endDate').MonthPicker('option', 'StartYear', startYearOfFY);

                var minMonth = monthDiff(new Date(startYearOfFY, 06, 1), new Date());
                minMonth += 1;
                $('#startDate').MonthPicker('option', 'MinMonth', -minMonth);
                $('#endDate').MonthPicker('option', 'MinMonth', -minMonth);

                var maxMonth = monthDiff(new Date(endYearOfFY, 05, 1), new Date());
                console.log('max = ' + maxMonth);
                maxMonth += 1;
                $('#startDate').MonthPicker('option', 'MaxMonth', -maxMonth);
                $('#endDate').MonthPicker('option', 'MaxMonth', -maxMonth);
            },
            failure: function () {
                log("getFiscalYear failed!!");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log('error=' + xhr);
                console.log('error=' + thrownError);
            }
        });
    }
    function daysInMonth(month, year) {
        return new Date(year, month, 0).getDate();
    }
    function makeChanges()
    {
        //Add or Remove validation rules
        if ($("#reportGenerationType").val() == "Summary" || $("#reportGenerationType").val() == "Upazila")
        {
            $('#pr').iCheck('check');
        } else {
            $('#ln').iCheck('check');
        }
    }
     function submitForm() {
        var form = $('#reportForm');
        form.validate();
        var valid = form.valid();
        var sDate = getNumberInEnglish($("#startDate").val());
        var eDate = getNumberInEnglish($("#endDate").val());
        if (sDate !== '')
        {
            sDate = sDate.substr(sDate.length - 7);
            sDate = "01-" + sDate;
        }
        if (eDate !== '')
        {
            eDate = eDate.substr(eDate.length - 7);
            var month = eDate.split("-")[0];
            var year = eDate.split("-")[1];
            var days = daysInMonth(month, year);
            eDate = days + "-" + eDate;
        }
        if (valid) {
            $("#startDate").val(sDate);
            $("#endDate").val(eDate);
            $('#reportForm').submit();
        }
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
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
                                <form:option value="Payment"><spring:message code='report.paymentBasedReport'/></form:option>
                                <%--<form:option value="WithoutMobileNo"><spring:message code='report.withoutMobileNo'/></form:option>--%>
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
                            <form:select class="form-control" path="fiscalYear.id"  id="fiscalYear" onchange="loadFiscalYearInfo(this.id)">  
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                            </form:select> 
                            <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div> 
                            <div class="form-group" id="periodDiv">
                            <label for="" class="col-md-4 control-label"><spring:message code="label.monthPeriod" /></label>
                        <div class="col-md-8">
                            <div class="form-group row">                                
                                <span for="startDate" class="col-md-2 control-label"><spring:message code='paymentCycle.label.from'/></span>
                                <div class="col-md-4">
                                    <spring:message code='paymentCycle.label.startMonth' var="startMonth"/>                                    
                                    <form:input class="form-control" placeholder="${startMonth}" path="startDate" />
                                    <form:errors path="startDate" cssStyle="color:red"></form:errors>
                                    </div>
                                    <span for="endDate" class="col-md-2 control-label"><spring:message code='paymentCycle.label.to'/></span>
                                <div class="col-md-4">
                                    <spring:message code='paymentCycle.label.endMonth' var="endMonth"/>
                                    <form:input class="form-control" placeholder="${endMonth}" path="endDate"/>
                                    <form:errors path="endDate" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
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
                    <c:if test="${type eq 'bgmea'}">
                        <div class="form-group" id="bgmeaBlock">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bgmea" /></label>
                            <c:choose>
                                <c:when test="${reportParameterForm.isBgmeaFactoryAvailable eq 'false'}">
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
                                                ${reportParameterForm.bgmeaFactory.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${reportParameterForm.bgmeaFactory.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            </div>
                    </c:if>
                    <c:if test="${type eq 'bkmea'}">
                        <div class="form-group" id="bkmeaBlock">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bkmea" /></label>
                            <c:choose>
                                <c:when test="${reportParameterForm.isBkmeaFactoryAvailable eq 'false'}">
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="bkmeaFactory.id" id="bkmeaFactoryId">
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${bkmeaFactoryList}" itemValue="id" itemLabel="${bkmeaFactoryName}"></form:options> 
                                </form:select>
                                <form:errors path="bkmeaFactory.id" cssStyle="color:red"></form:errors>
                                </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-8 labelAsValue">
                                        <form:hidden path="bkmeaFactory.id" id="bkmeaFactoryId"/>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                ${reportParameterForm.bkmeaFactory.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${reportParameterForm.bkmeaFactory.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            </div>
                    </c:if>  
                    <div class="form-group">
                        <div class="col-md-4"></div> 
                        <div class="col-md-8">
                            <spring:message code="viewReport" var="viewReport"/>
                            <button type="button" class="btn bg-blue" onClick="return submitForm()"><i class="fa fa-file" aria-hidden="true"></i>${viewReport}</button> 
                        </div>    
                    </div>    
                </div>
            </div>
        </div>
    </section>
</form:form>

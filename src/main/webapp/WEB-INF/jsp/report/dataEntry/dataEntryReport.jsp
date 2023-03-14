<%-- 
    Document   : dataEntryReport
    Created on : Feb 20, 2018, 12:55:05 PM
    Author     : user
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
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });

</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!--<a href="${contextPath}/endpointsdoc">API doc </a>-->
<form:form action="${contextPath}/dataEntryReport" method="post" target="_blank" class="form-horizontal" id="reportForm" modelAttribute="reportParameterForm">            
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code='reportHeader.dataEntryReport'/>
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
                        <div class="col-md-10 labelAsValue">                        
                            <form:radiobutton path="reportGenerationType" value="Details" checked="checked"/>&nbsp;<spring:message code='report.detailReport'/>&nbsp;
                            <form:radiobutton path="reportGenerationType" value="Summary"/>&nbsp;<spring:message code='report.summaryReport'/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="reportOrientation" class="col-md-2 control-label"><spring:message code="reportOrientation" /><span class="mandatory">*</span></label>
                        <div class="col-md-10 labelAsValue">                        
                            <form:radiobutton path="reportOrientation" value="Portrait"/>&nbsp;<spring:message code='report.portrait'/>&nbsp;
                            <form:radiobutton path="reportOrientation" value="Landscape" checked="checked"/>&nbsp;<spring:message code='report.landscape'/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="reportExportType" class="col-md-2 control-label"><spring:message code="reportExportType" /><span class="mandatory">*</span></label>
                        <div class="col-md-10 labelAsValue">                        
                            <form:radiobutton path="reportExportType" value="pdf"  checked="checked"/>&nbsp;<spring:message code='report.pdf'/>&nbsp;
                            <form:radiobutton path="reportExportType" value="excel"/>&nbsp;<spring:message code='report.excel'/>
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
                            <label class="col-md-4"></label> 
                            <div class="col-md-4">
                            <spring:message code="viewReport" var="viewReport"/>
                            <button type="submit" class="btn bg-blue"><i class="fa fa-file" aria-hidden="true"></i>${viewReport}</button>                        </div>    
                    </div> 
                </div>                
            </div>
        </div>
    </section>
</form:form>



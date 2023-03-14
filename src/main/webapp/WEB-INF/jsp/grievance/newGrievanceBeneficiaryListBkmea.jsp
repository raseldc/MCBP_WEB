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
</style>
<script>
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        var urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }
        $('#applicantId').keypress(function (e) {
            if (e.which == 13) {
                $("#buttonSearch").click();
                return false;    //<---- Add this line
            }
        });
        $("#beneficiaryListForm").validate({
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
        $("#buttonSearch").click(function ()
        {
            var form = $('#beneficiaryListForm');
            form.validate();
            if (form.valid()) {
                var serializedData = $("#beneficiaryListForm").serialize();
                console.log(serializedData);
                $('#applicantListTable').DataTable().destroy();
                var path = '${pageContext.request.contextPath}';
                $('#applicantListTable').DataTable({
                    "processing": true,
                    "serverSide": true,
                    "bSort": false,
                    "pagingType": "full_numbers",
                    "dom": '<"top"i>rt<"bottom"lp><"clear">',
                    "language": {
                        "url": urlLang
                    },
                    "ajax": {
                        "url": path + "/grievance/beneficiaryList",
                        "type": "POST",
                        "data": {
                            "fiscalYear": $("#fiscalYear").val(),
                            "applicantId": $("#applicantId").val(),
                            "bkmeaFactoryId": $("#bkmeaFactoryId").val(),
                            "applicantType": $("#applicantType").val()
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

    });
</script>
<style>
    .labelAsValue{
        padding-top: 7px;
    }
</style>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
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
        <spring:message code="title.newGrievanceBeneficiaryList" />        
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
                    <form:form id="beneficiaryListForm" class="form-horizontal" modelAttribute="searchParameterForm">
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
                                        <label for="status" class="col-md-4 control-label"><spring:message code='label.nid' /></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.nid' var="nid"/>
                                        <input type="text" id="applicantId" placeholder="${nid}" name="applicantId" class="form-control">
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
                                <div class="form-group" id="bkmeaBlock">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bkmea" /></label>
                                    <c:choose>
                                        <c:when test="${searchParameterForm.isBgmeaFactoryAvailable eq 'false'}">
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
                                                        ${searchParameterForm.bkmeaFactory.nameInBangla}
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${searchParameterForm.bkmeaFactory.nameInEnglish}
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>     
                                </div>   
                            </div>   
                        </div>
                    </form:form>
                    <div class="table-responsive">
                        <table id="applicantListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
<!--                                    <th><spring:message code="label.photo" /></th>-->
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

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- 
    Document   : registrationDone
    Created on : Feb 28, 2017, 12:40:44 PM
    Author     : PCUser
--%>

<script type="text/javascript">
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");

        if (selectedLocale == 'en')
        {
            $("#nid").text(${applicant.nid});
            $("#mobileNo").text(${applicant.mobileNo});
            $("#dateOfBirth").text($("#dateOfBirth").text());
        } else
        {
            $("#nid").text(getNumberInBangla('${applicant.nid}'));
            $("#mobileNo").text(getNumberInBangla('${applicant.mobileNo}'));
            $("#dateOfBirth").text(getNumberInBangla($("#dateOfBirth").text()));
        }
        $('button').on('click', function () {
            printData();
        })
    });
    function printData() {
        var divToPrint = document.getElementById("printTable");
        var htmlToPrint = '' +
                '<style type="text/css">' +
                'table {border-collapse: collapse; width: 50%}' +
                'table th, table td {' +
                'border:1px solid #000;' +
                'text-align: center;' +
                'padding;0.5em;' +
                '}' +
                '</style>';
        htmlToPrint += divToPrint.outerHTML;
        newWin = window.open("");
        newWin.document.write(htmlToPrint);
        newWin.print();
        newWin.close();
    }

</script>

<form:form id="regCompleteForm" class="form-horizontal" role="form" modelAttribute="applicant">
    <div style="padding-top: 20px; text-align: center">
        <div class="alert 
             <c:if test="${isSuccess eq 'true'}">alert-success</c:if> 
             <c:if test="${isSuccess eq 'false'}">alert-info</c:if> 
                 alert-dismissable">
                 <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                 <div><spring:message code="applicationCreateSuccess1"/>&nbsp;<spring:message code="applicantList.applicantId" /> = ${applicant.applicationID}</div>
             <div><spring:message code="applicationCreateSuccess2"/></div>
        </div>

    </div>
    <!--    <div id="dvPrintSummary">
            <span style='font-size: 18px; text-align:center'><h2><spring:message code="onlineReceipt"/></h2></span>
            <div class="row">
                <div class="col-md-12 col-sm-12 col-md-offset-3">
                    <div class="form-group">
                        <label class="col-md-6 col-sm-6 control-label"><spring:message code="label.nameBn" /></label>
                        <div class="col-md-6 col-sm-6 labelAsValue">${applicant.fullNameInBangla}</div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-6 control-label"><spring:message code="label.nameEn" /></label>
                        <div class="col-md-6 labelAsValue">${applicant.fullNameInEnglish}</div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-6 control-label"><spring:message code="label.nid" /></label>
                        <div class="col-md-6 labelAsValue">
                            <span id="nid"></span>                        
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-6 control-label"><spring:message code="label.dob" /></label>
                        <div class="col-md-6 labelAsValue">
                            <span id="dateOfBirth"><fmt:formatDate value="${applicant.dateOfBirth.time}" pattern="yyyy-MM-dd" /></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-6 control-label"><spring:message code="label.mobileNo" /></label>
                        <div class="col-md-6 labelAsValue">
                            <span id="mobileNo"></span>                        
                        </div>
                    </div>
    
                </div>
            </div>
        </div>-->
    <table class="table" style="width: 50%; margin:0 auto" id="printTable">
        <tr><td colspan="2"><span style='font-size: 18px; text-align:center'><h2><spring:message code="onlineReceipt"/></h2></span></td></tr>
        <tr>
            <td><spring:message code="label.nameBn" /></td>
            <td>${applicant.fullNameInBangla}</td>
        </tr>
        <tr>
            <td><spring:message code="label.nameEn" /></td>
            <td>${applicant.fullNameInEnglish}</td>
        </tr>
        <tr>
            <td><spring:message code="label.nid" /></td>
            <td><span id="nid"></span></td>
        </tr>
        <tr>
            <td><spring:message code="label.dob" /></td>
            <td><span id="dateOfBirth"><fmt:formatDate value="${applicant.dateOfBirth.time}" pattern="dd-MM-yyyy" /></span></td>
        </tr>
        <tr>
            <td><spring:message code="label.mobileNo" /></td>
            <td><span id="mobileNo"></span></td>
        </tr>
    </table>
    <div class="form-group" style="text-align: center">
        <button type="button" class="btn btn-success" onclick="printData('dvPrintSummary')"><spring:message code="print"/></button>
    </div>                    
</form:form>
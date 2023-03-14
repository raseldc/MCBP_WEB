<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/plugins/fileUpload/fileinput.min.js" />" ></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">
<link rel="stylesheet" href="<c:url value="/resources/plugins/fileUpload/fileinput.min.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/applicantTab.css" />">

<style>
    .error{
        color:red;
    }

    .normalLabel{
        font-weight: normal;
    }

</style>

<script type="text/javascript">

    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/beneficiary/applicantForm"));
        loadApplicantTabs('${actionType}', '${nextActiveTab}');
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");

        $("#attachmentInfoForm").validate({

            rules: {// checks NAME not ID
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });

        //add more file components if Add is clicked
        $('#addFile').click(function () {
            var fileIndex = $('#fileTable > div').children().length / 2;
            if ($('#multipartFileList' + (fileIndex - 1)).val() != "") {
                $('#fileTable').append(
                        '<div class="form-group">' +
                        '<label for="Input' + fileIndex + '" class="col-md-2 control-label">' + '<spring:message code="label.attachment" />' + '</label>' +
                        '<div class="col-md-5">' +
                        '	<input type="file" class="file" id="multipartFileList' + fileIndex + '" name="multipartFileList[' + fileIndex + ']" />' +
                        '</div>' +
                        '</div>');
                $('#multipartFileList' + fileIndex).fileinput();
                $("#multipartFileList" + fileIndex).prev().text(getLocalizedText('browse', selectedLocale));
                $(".fileinput-upload-button:eq(" + fileIndex + ")").hide();
            } else {
                bootbox.dialog({
                    onEscape: function () {},
                    title: '<spring:message code="label.warning" />',
                    message: "<b><spring:message code="attachmentFailed" /></b>",
                    buttons: {
                        ok: {
                            label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                        }
                    }
                });
                $('.modal-content:eq(3)').css({
                    'margin-top': function () {
                        console.log('margin - top');
                        var w = $(window).height();
                        var b = $($(".modal-dialog")[3]).height(); // special case, there are 2 modals in page, so, it is number 3 . so, index = 3-1 = 2
                        // should not be (w-h)/2                    
                        var h = (w - 172) / 2; // hard coded!!
                        console.log("w=" + w + ", b=" + b + ", h=" + h);
                        return h + "px";
                    },
                });
            }
        });

        if (selectedLocale == 'bn') {
            $("span[id^='attachmentIndex']").each(function () {
                console.log($(this).text());
                $(this).text(getNumberInBangla($(this).text()));
            });
        }

    });

    function removeAttachment(index)
    {
        $("#dvAttachment" + index).css("display", "none");
    }
    function removeSignature()
    {
        document.getElementById("signatureFile").style.display = "none";//
        document.getElementById("removeSignature").style.display = "none";//
        $("#signatureFile").val("");
        $("#signature").attr("style", "display:block");
    }

    function goToPreviousPage() {
        $('select, textarea').each(function () {
            $(this).rules('remove');
        });
        var form = $('#attachmentInfoForm');
        submitFormAjax(form, 'Previous', 'biometricInfoForm', 'Photo & Signature | Add/View Applicant');
    }

    function submitForm() {
        var form = $('#attachmentInfoForm');
        form.validate();
        if (form.valid()) {
            form.submit();
        }
    }

</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="selectedLocale" value="${pageContext.response.locale}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/beneficiary/attachmentInfoForm" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/beneficiary/attachmentInfoForm/${attachmentInfoForm.id}" />
    </c:otherwise>
</c:choose>

<form:form id="attachmentInfoForm" action="${actionUrl}" class="form-horizontal" enctype="multipart/form-data" role="form" modelAttribute="attachmentInfoForm">
    <section class="content-header">
        <h1>
            <spring:message code='beneficiary'/>&nbsp;<spring:message code='label.management'/>             
        </h1>
    </section>

    <section class="content">
        <c:if test="${not empty message}">
            <div class="message green">${message}</div>
        </c:if>
        <div class="row">
            <div class="col-md-12">
                <%--<jsp:include page="applicantTabs.jsp" />--%>
                <div id="crumbs">         
                    <ol class="simple-list">                  
                        <li style="margin-left: 0px">                           
                            <a class="singletab" href="#page1" data-toggle="tab" onclick="loadJspForBeneficiary('/beneficiary/personalInfoForm/');">
                                <span class="title">
                                    <img src="${contextPath}/resources/img/personalInfo.png" class="image">
                                    <spring:message code='label.BasicInfoTab' var="basicInfoTab"/>${basicInfoTab}
                                </span>                                     
                            </a>        
                        </li>                  
                        <li>
                            <a class="singletab" href="#page2" data-toggle="tab" onclick="loadJspForBeneficiary('/beneficiary/addressForm/');">                                     
                                <span class="title">
                                    <img src="${contextPath}/resources/img/addressInfo.png" class="image">
                                    <spring:message code='label.AddressInfoTab' var="addressInfoTab"/>${addressInfoTab}
                                </span>                                        
                            </a>       
                        </li>                  
                        <li>                           
                            <a class="singletab" href="#page3" data-toggle="tab" onclick="loadJspForBeneficiary('/beneficiary/socioEconomicForm/');">
                                <span class="title">
                                    <img src="${contextPath}/resources/img/socioInfo2.png" class="image">
                                    <spring:message code='label.socioEconomicInfoTab' var="socioEconomicInfoTab"/>${socioEconomicInfoTab}
                                </span>                                        
                            </a>         
                        </li>         
                        <li>                           
                            <a class="singletab" href="#page4" data-toggle="tab" onclick="loadJspForBeneficiary('/beneficiary/paymentInfoForm/');">
                                <span class="title">
                                    <img src="${contextPath}/resources/img/paymentInfo.png" class="image">
                                    <spring:message code='label.BankAccountInfoTab' var="bankAccountInfoTab"/>${bankAccountInfoTab}
                                </span>                                        
                            </a>         
                        </li>          
                        <li>
                            <a class="singletab" href="#page5" data-toggle="tab" onclick="loadJspForBeneficiary('/beneficiary/biometricInfoForm/');">
                                <span class="title">
                                    <img src="${contextPath}/resources/img/biometricInfo.png" class="image">
                                    <spring:message code='label.BiometricInfoTab' var="biometricInfoTab"/>${biometricInfoTab}
                                </span>                                        
                            </a>         
                        </li>         
                        <li class="selected">                           
                            <a class="singletab" href="#page6" data-toggle="tab" onclick="loadJspForBeneficiary('/beneficiary/attachmentInfoForm/');">
                                <span class="title">
                                    <img src="${contextPath}/resources/img/attachment.png" class="image">
                                    <spring:message code='label.AttachmentTab' var="attachmentTab"/>${attachmentTab}
                                </span>                                        
                            </a>         
                        </li>         
                    </ol>
                </div>
                <div class="row">
                    <div class="col-md-2 col-xs-4" style="font-size: 20px">
                        <c:if test="${selectedLocale eq 'en'}">
                            Step 6 of 6
                        </c:if>
                        <c:if test="${selectedLocale eq 'bn'}">
                            ধাপ ৬/৬
                        </c:if>
                    </div>
                    <div class="col-md-6 col-md-offset-4 col-xs-8">
                        <div class="pull-right">
                            <spring:message code="label.mandatoryPart1"/>
                            <span style="color: red; font-weight: bold">(*)</span>
                            <span style="font-weight: bold"><spring:message code="label.mandatoryPart2"/></span>
                        </div>
                    </div>
                </div>
                <hr style="margin-top: 5px">

                <div id="page1" class="tab-pane fade in active">                    

                    <input type="hidden" id="regType" name="regType" value="${regType}">
                    <input type="hidden" id="applicantId" name="applicantId" value="${attachmentInfoForm.id}">
                    <form:hidden path="fiscalYear.id" />
                    <form:hidden path="applicationId" />
                    <label class="control-label"><spring:message code="label.AttachmentList" /></label> 
                    <c:forEach items="${attachmentInfoForm.beneficiaryAttachmentList}" var="attachment" varStatus="index">                        
                        <c:if test="${attachment !='' && attachment != null}">
                            <div id="dvAttachment${index.index}" class="form-group">
                                <div class="col-md-6">
                                    <label for="nidInput" class="col-md-1"><span id="attachmentIndex${index.index}">${index.index+1}</span></label>
                                    <label for="nidInput" class="col-md-4">${attachment.attachmentName}</label>
                                    <div class="col-md-7">
                                        <a id="attachment${index.index}" href="${contextPath}/${imagePath}${attachment.fileName}" target="_blank" title="Click to open">${attachment.fileName}</a>
                                        &nbsp;<input type="image" src="${contextPath}/resources/img/remove16.png" id="removeFile[${index.index}]" value="Remove" onclick="removeAttachment(${index.index});
                                                return false;" /> 
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>

                    <spring:message var="addNew" code="addNew"/>
                    <div class="form-group">
                        <div class="col-md-2">
                            <button id="addFile" class="" type="button" ><span class="glyphicon glyphicon-pushpin"/>  ${addNew}</button>
                        </div>
                    </div>
                    <div id="fileTable">
                    </div>                    

                    <div class="form-group">
                        <div class="col-md-2">
                            <spring:message code='button.previousBtn' var="previousBtn"/>
                            <button type="submit" class="btn btn-primary btnPrevious pull-left"  name="action"  value="Previous" onclick="goToPreviousPage();"><span class="glyphicon glyphicon-chevron-left"></span> ${previousBtn}</button>
                        </div>
                        <div class="col-md-1 col-md-offset-9">
                            <spring:message code='save' var="save"/>
                            <button type="submit" class="btn btn-success btnNext pull-right"  name="action" value="submit" onclick="submitForm();"><i class="fa fa-floppy-o"></i> ${save}</button>
                        </div>
                    </div>  

                </div>
            </div>   
        </div>
    </section>    
</form:form>
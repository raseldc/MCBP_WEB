<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />"></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<script src="<c:url value="/resources/plugins/fileUpload/fileinput.min.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/fileUpload/fileinput.min.css" />">
<script type="text/javascript" src="<c:url value="/resources/js/jquery.ui.datepicker-bn.js"/>" ></script>
<script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">
<!--<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css" />">-->
<script src="<c:url value="/resources/js/bootbox.min.js"/>" type="text/javascript"></script>

<style type="text/css">
    .input-group .form-control
    {
        position: static;
    }
</style>
<script type="text/javascript">
    $(function () {
        if ("${applicantType}" == "UNION") {
            $(menuSelect("${pageContext.request.contextPath}/beneficiaryChangeStatus/" + "union" + "/list"));
        }
        else if ("${applicantType}" == "MUNICIPAL") {
            $(menuSelect("${pageContext.request.contextPath}/beneficiaryChangeStatus/" + "municipal" + "/list"));
        }
        else if ("${applicantType}" == "CITYCORPORATION") {
            $(menuSelect("${pageContext.request.contextPath}/beneficiaryChangeStatus/" + "cityCorporation" + "/list"));
        }
        else if ("${applicantType}" == "BGMEA") {
            $(menuSelect("${pageContext.request.contextPath}/beneficiaryChangeStatus/" + "bgmea" + "/list"));
        }
        else if ("${applicantType}" == "BKMEA") {
            $(menuSelect("${pageContext.request.contextPath}/beneficiaryChangeStatus/" + "bkmea" + "/list"));
        }
        
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        initDate($("#incidentDate"), '${dateFormat}', $("#incidentDate\\.icon"), selectedLocale);
//        $("#incidentDate").datepicker({  maxDate: new Date() });
        $(".fileinput-upload-button").hide();
        $("#labelNewStatus").hide();
        if (selectedLocale === "bn")
        {
            $.datepicker.setDefaults(
                    $.extend(
                            {'dateFormat': 'dd-mm-yy'},
                            $.datepicker.regional['bn-BD']
                            )
                    );
            $("#incidentDate").val(getNumberInBangla($("#incidentDate").val()));
            makeUnijoyEditor('deactivationComment');
        }
        if ($("#beneficiaryDeactivationReasons").val() == "")
        {
            $("#buttonrevert").hide();
        }
        $("#changeStatusForm").validate({

            rules: {// checks NAME not ID,
                "beneficiaryDeactivationReasons": {
                    required: true
                },
                "incidentDate": {
                    required: true
                },
                "deactivationComment": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });
    function removeProfilePhoto()
    {
        document.getElementById("certificateProfilePhotoFile").style.display = "none"; //
        document.getElementById("removeProfilePhoto").style.display = "none"; //
        $("#certificateProfilePhotoFile").val("");
        $("#photo").attr("style", "display:block");
    }
    function showNewStatus()
    {
        if ($("#beneficiaryDeactivationReasons").val() == "")
        {
            $("#labelNewStatus").hide();
        } else
        {
            $("#labelNewStatus").show();
        }

        if ($("#beneficiaryDeactivationReasons").val() == "MISCARRIAGE" || $("#beneficiaryDeactivationReasons").val() == "STILLBIRTH")
        {
            if (selectedLocale === 'bn')
            {
                $("#inputNewStatus").text("${beneficiaryStatusList[1].displayNameBn}");
            } else
            {
                $("#inputNewStatus").text("${beneficiaryStatusList[1].displayName}");
            }
        } else if ($("#beneficiaryDeactivationReasons").val() == "CHILD_DIED_WITHIN_TWO_YEARS")
        {
            if (selectedLocale === 'bn')
            {
                $("#inputNewStatus").text("${beneficiaryStatusList[0].displayNameBn}");
            } else
            {
                $("#inputNewStatus").text("${beneficiaryStatusList[0].displayName}");
            }
        } else
        {
            if (selectedLocale === 'bn')
            {
                $("#inputNewStatus").text("${beneficiaryStatusList[2].displayNameBn}");
            } else
            {
                $("#inputNewStatus").text("${beneficiaryStatusList[2].displayName}");
            }
        }
    }
    function loadBeneficiary(id) {
        var data = getDataFromUrl(contextPath + "/beneficiary/viewBeneficiary/" + id);
        $('#myModal .modal-body').html(data);
        $('#myModalLabel').val('test');
        $('#myModal .modal-body').css({height: screen.height * .60});
        $('#myModal').modal('show');
    }
    function submitForm() {
        var form = $('#changeStatusForm');
        form.validate();
        if (!form.valid()) {
            return false;
        }
        if (selectedLocale === "bn")
        {
            $("#incidentDate").val(getNumberInEnglish($("#incidentDate").val()));
        }
    }
    function askApproveSubmit()
    {
        var approvalMessage;
        if (selectedLocale === 'bn') {
            approvalMessage = "আপনি কি ভাতাভোগীর স্ট্যাটাস পরিবর্তন করতে চান?";
        } else
        {
            approvalMessage = "Are you sure you want to Change The Applicant's Status?";
        }
        bootbox.confirm({
            message: approvalMessage,
            buttons: {
                confirm: {
                    label: '<spring:message code="button.yes" />',
                    className: 'btn-success'
                },
                cancel: {
                    label: '<spring:message code="button.no" />',
                    className: 'btn-danger'
                }
            },
            callback: function (result)
            {
                if (result === true)
                {
                    $("#buttonChange").click();
                } else
                {
                    return true;
                }
            }
        });
    }
    function askRevertSubmit()
    {
        var approvalMessage;
        if (selectedLocale === 'bn') {
            approvalMessage = "আপনি কি ভাতাভোগীকে পুনরায় সচল স্ট্যাটাসে ফেরত নিতে চান?";
        } else
        {
            approvalMessage = "Are you sure you want to Revert The Applicant's Status to Active?";
        }
        bootbox.confirm({
            message: approvalMessage,
            buttons: {
                confirm: {
                    label: '<spring:message code="button.yes" />',
                    className: 'btn-success'
                },
                cancel: {
                    label: '<spring:message code="button.no" />',
                    className: 'btn-danger'
                }
            },
            callback: function (result)
            {
                if (result === true)
                {
                    $("#button1").click();
                } else
                {
                    return true;
                }
            }
        });
    }

</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="actionUrl" value="${contextPath}/beneficiary/changeStatus/${beneficiary.id}" />  
<form:form id="changeStatusForm" class="form-horizontal" action="${actionUrl}" enctype="multipart/form-data"  modelAttribute="beneficiary">
    <section class="content-header clearfix">
        <h1 class="pull-left"><spring:message code="label.beneficiaryStatusChange" />&nbsp;<spring:message code="label.management" /></h1>
        <div class="pull-right">
            <button type="button" id="buttonrevert" name="save" class="btn bg-green" onclick="return askRevertSubmit()" value="revert">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="button.revertToActive" />
            </button>    
            <button type="submit" id="button1" name="save" class="btn bg-green" onclick="return submitForm()" value="revert" style="display: none">  </button>

            <button type="button" id="buttonrev" name="buttonrev" value="save" class="btn bg-blue" onclick="return askApproveSubmit()">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>   
            <button type="submit" id="buttonChange" name="save" value="save" class="btn bg-blue" onclick="return submitForm()" style="display: none">  </button>

        </div>  
    </section>
    <section class="content">    
        <div class="row">
            <div class="col-md-12">
                <div id="changeInfo" style="clear:both">
                    <spring:message code='label.select' var="select"/>
                    <div class="form-group">
                        <label for="beneficiaryStatusInput" class="col-md-2 control-label"><spring:message code="payment.label.beneficiaryNid" /></label>
                        <div class="col-md-2" style="padding-top: 7px">
                            <a href='#' onclick = loadBeneficiary('${beneficiary.id}') title='' > ${benId}</a>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="beneficiaryStatusInput" class="col-md-2 control-label"><spring:message code="beneficiaryCurrentStatus" /></label>
                        <div class="col-md-2">
                            <form:label cssClass="labelAsValue" path="beneficiaryStatus" >
                                <c:if test="${pageContext.response.locale=='en'}">
                                    ${beneficiary.beneficiaryStatus.displayName}
                                </c:if>
                                <c:if test="${pageContext.response.locale=='bn'}">
                                    ${beneficiary.beneficiaryStatus.displayNameBn}    
                                </c:if>
                            </form:label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="beneficiaryStatusInput" class="col-md-2 control-label"><spring:message code="beneficiaryDeactivationReason" /><span class="mandatory">*</span></label>
                        <div class="col-md-3">
                            <form:select class="form-control" path="beneficiaryDeactivationReasons" onclick="showNewStatus()">
                                <form:option value="" label="${select}"></form:option>
                                <c:if test="${pageContext.response.locale=='bn'}">
                                    <form:options items="${beneficiaryDeactivationReasonsList}"  itemLabel="displayNameBn"></form:options>
                                </c:if>
                                <c:if test="${pageContext.response.locale=='en'}">
                                    <form:options items="${beneficiaryDeactivationReasonsList}"  itemLabel="displayName"></form:options>
                                </c:if>
                            </form:select>
                            <form:errors path="beneficiaryDeactivationReasons" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group" id="labelNewStatus">
                            <label for="beneficiaryStatusInput" class="col-md-2 control-label"><spring:message code="beneficiaryNewStatus" /><span class="mandatory">*</span></label>
                        <div class="col-md-3">
                            <label id="inputNewStatus" style="padding-top: 8px;"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="code" class="col-md-2 control-label"><spring:message code="beneficiaryIncidentDate" /><span class="mandatory">*</span></label>
                        <div class="col-md-3">
                            <spring:message code='beneficiaryIncidentDate' var="incidentDate"/>
                            <form:input class="form-control" placeholder="${incidentDate}" path="incidentDate"/>
                            <form:errors path="incidentDate" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="commentInputEnglish" class="col-md-2 control-label"><spring:message code="label.comment" /><span class="mandatory">*</span></label>
                        <div class="col-md-3">
                            <spring:message code='label.comment' var="deactivationComment"/>
                            <form:textarea class="form-control bfh-phone" placeholder="${deactivationComment}" path="deactivationComment" autofocus="autofocus"/>
                            <form:errors path="deactivationComment" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="nidInput" class="col-md-2 control-label"><spring:message code="label.attachment" /></label>
                        <div class="col-md-4">
                            <spring:message code='placeholder.photo' var="photo"/>                                    
                            <div id="photo">
                                <input id="certificatePhotoInput" name="certificateProfilePhoto" type="file" class="file" data-allowed-file-extensions='["jpg", "jpeg", "png", "gif"]'>
                            </div>
                            <c:if test="${beneficiary.attachmentPath !='' && beneficiary.attachmentPath != null}">
                                <img id="certificateProfilePhotoFile" name="certificateProfilePhotoFile" src="${contextPath}/${certificateImagePath}/${beneficiary.attachmentPath}" style="width: 250px">
                                <input type="image" src="${contextPath}/resources/img/remove16.png" id="removeProfilePhoto" value="Remove" onclick="removeProfilePhoto();
                                        return false;" /> 
                            </c:if>
                            <form:errors path="attachmentPath" cssStyle="color:red"></form:errors>
                            </div>            
                        </div>

                    </div>


                </div>
            </div>
        </section>
</form:form>
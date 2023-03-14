<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script>
    $(function () {
        $(".fileinput-upload-button").hide();
        $(menuSelect("${pageContext.request.contextPath}/trainer/list"));
        $('#trainer-delete').attr("data-toggle", "modal").attr("data-target", "#trainermodel-delete-confirmation");
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        makeUnijoyEditor('nameInBanglaInput');
        $('#organizationNumber').on("keydown", function (event) {
            checkNumberWithLength(event, this, 11);
        });
        $("#nameInEnglishInput").focus();
        $("#nameInBanglaInput").focus();
        if (selectedLocale === "bn")
        {
            makeUnijoyEditor('organizationNumber');
            makeUnijoyEditor('contactPersonName');
        }
        $("#trainerForm").validate({

            rules: {// checks NAME not ID
                "nameInBangla": {
                    required: true,
                    maxlength: 255,
                    banglaAlphabet: true
                },
                "nameInEnglish": {
                    required: true,
                    maxlength: 255,
                    englishAlphabet: true
                },
                "organizationNumber": {
                    required: false,
                    maxlength: 11
                },
                "contactPersonName": {
                    required: false,
                    maxlength: 255
                },
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });
    function removeProfilePhoto()
    {
        document.getElementById("profilePhotoFile").style.display = "none";//
        document.getElementById("removeProfilePhoto").style.display = "none";//
        $("#profilePhotoFile").val("");
        $("#photo").attr("style", "display:block");
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/trainer/create"/>
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/trainer/edit/${trainer.id}" />
    </c:otherwise>
</c:choose>
<form:form id="trainerForm" action="${actionUrl}" class="form-horizontal" enctype="multipart/form-data" modelAttribute="trainer">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.trainer" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/trainer/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>
            <c:if test="${actionType ne 'create'}">
                <span id="trainer-delete" class="btn bg-red">
                    <i class="fa fa-trash-o"></i>
                    <spring:message code="delete" />
                </span>
            </c:if>
        </div>    
    </section>
    <section class="content">        
        <div class="row">
            <div class="col-md-6"> 

                <form:hidden path="id" /> 
                <form:hidden path="creationDate" value="${creationDate}"/>
                <div class="form-group">                    
                    <div class="form-group">
                        <label for="nameInBanglaInput" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <form:input path="nameInBangla" class="form-control" id="nameInBanglaInput" />
                            <form:errors path="nameInBangla" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="nameInEnglishInput" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <form:input path="nameInEnglish" class="form-control" id="nameInEnglishInput" />
                            <form:errors path="nameInEnglish" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="organizationNumberInput" class="col-md-4 control-label"><spring:message code="trainer.organizationNumber" /></label>
                        <div class="col-md-8">
                            <form:input path="organizationNumber" class="form-control" id="organizationNumber" />
                            <form:errors path="organizationNumber" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="contactPersonNameInput" class="col-md-4 control-label"><spring:message code="trainer.contactPersonName" /></label>
                        <div class="col-md-8">
                            <form:input path="contactPersonName" class="form-control" id="contactPersonName" />
                            <form:errors path="contactPersonName" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.photo" /></label>
                        <div class="col-md-8">
                            <spring:message code='placeholder.photo' var="photo"/>                                    
                            <div id="photo">
                                <input id="photoInput" name="profilePhoto" type="file" class="file" data-allowed-file-extensions='["jpg", "jpeg", "png", "gif"]'>
                            </div>
                            <c:if test="${trainer.photoPath !='' && trainer.photoPath != null}">
                                <img id="profilePhotoFile" name="profilePhotoFile" src="${contextPath}/${imagePath}/${trainer.photoPath}" style="width: 250px">
                                <input type="image" src="${contextPath}/resources/img/remove16.png" id="removeProfilePhoto" value="Remove" onclick="removeProfilePhoto();
                                        return false;" /> 
                            </c:if>
                            <form:errors path="photoPath" cssStyle="color:red"></form:errors>
                            </div>            
                        </div>

                </form:form>
            </div>
        </div>
        <div id='trainermodel-delete-confirmation' class="modal fade"  tabindex="-1" trainer="dialog" aria-labelledby="trainermodel-delete-confirmation-title">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="trainermodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                    </div>
                    <form action="${contextPath}/trainer/delete/${trainer.id}" method="post">
                        <div class="form-horizontal">
                            <div class="modal-body">
                                <spring:message code="deleteMessage" />                            
                            </div>
                            <div class="modal-footer">
                                <span class="btn btn-default" data-dismiss="modal"><spring:message code="cancelMessage" /></span>
                                <button type="submit" class="btn btn-primary pull-right">
                                    <spring:message code="delete" />
                                </button>
                            </div>
                        </div>
                    </form>    
                </div>
            </div>
        </div>
</section>

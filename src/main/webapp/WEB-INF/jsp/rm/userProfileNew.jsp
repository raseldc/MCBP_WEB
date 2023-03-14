<%-- 
    Document   : userProfileNew
    Created on : Feb 9, 2021, 12:07:03 AM
    Author     : shamiul Islam-AnunadSolution
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<style type="text/css">
    div.container {
        margin: 15px;   
    }


    div.left, div.right,label.left,input.right {
        float: left;
        padding: 10px;    
    }

</style>
<!-- Content -->



<form:form method="post"   id="userForm" action="${contextPath}/${action}" class="form-horizontal" enctype="multipart/form-data" modelAttribute="user">
    <form:hidden path="id" />
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="user.UserInfo" />

        </h1>
        <div class="pull-right">
            <button type="button" name="save" class="btn bg-blue" onClick="submitForm();">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>            
            <button id="submitButton" type="submit" name="save" style ="display: none">
            </button>            
        </div> 
    </section>
    <section class="content">
        ${message}  
        <div class="row">
            <div class="col-md-12">  
                <fieldset>
                    <legend><spring:message code='user.basicInfo'/></legend>

                    <div class="col-md-6">  
                        <div class="form-group">
                            <label for="userNameBn" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.nameBn' var="fullNameBn"/>
                                <form:input class="form-control"  path="fullNameBn" autofocus="autofocus"/>
                                <form:errors path="fullNameBn" cssclass="error"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="userNameEn" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.nameEn' var="userNameEn"/>
                                <form:input class="form-control"  path="fullNameEn" onkeydown="checkEnglishAlphabetWithLength(event, this, 30)"/>
                                <form:errors path="fullNameEn" cssclass="error"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="designation" class="col-md-4 control-label"><spring:message code="user.designation" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code="user.designation" var="designation"/>
                                <form:input class="form-control" placeholder="${designation}" path="designation" />
                                <form:errors path="designation" cssclass="error"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="userID" class="col-md-4 control-label"><spring:message code="label.userID" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code="label.userID" var="userID"/>


                                <form:input class="form-control" placeholder="${userID}" path="userID" readonly="true" onkeydown="checkEnglishAlphabetAndNumberWithLength(event, this, 255)"/>




                                <form:errors path="userID" cssclass="error"></form:errors>                                
                                </div>
                            </div>

                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="mobile" class="col-md-4 control-label"><spring:message code="user.mobileNo" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code="user.mobileNo" var="mobileNo"/>
                                <form:input class="form-control"  path="mobileNo" onkeydown="checkNumberWithLength(event, this, 11)"/>
                                <form:errors path="mobileNo" cssclass="error"></form:errors>
                                </div>
                            </div>    
                            <div class="form-group">
                                <label for="email" class="col-md-4 control-label"><spring:message code="user.email" /></label>
                            <div class="col-md-8">
                                <spring:message code="user.email" var="email"/>
                                <form:input class="form-control" placeholder="${email}" path="email" />
                                <form:errors path="email" cssclass="error"></form:errors>
                                </div>
                            </div>    
                            <div class="form-group">
                                <label for="photoInput" class="col-md-4 control-label"><spring:message code="label.photo" /></label>
                            <div class="col-md-8">
                                <spring:message code='placeholder.photo' var="photo"/>                                    
                                <div id="photo">
                                    <input id="photoInput" name="profilePhoto" type="file" class="file" data-allowed-file-extensions='["jpg", "jpeg", "png", "gif"]'>
                                </div>
                                <c:if test="${user.profilePicPath !='' && user.profilePicPath != null}">
                                    <img id="profilePhotoFile" name="profilePhotoFile" src="${profilePic}" style="width: 250px">
                                    <input type="image"  id="removeProfilePhoto" value="Remove" onClick="removeProfilePhoto();
                                            return false;" /> 
                                </c:if>
                                <form:errors path="profilePicPath" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>                        
                            <div class="form-group">
                                <label for="active" class="col-md-4 control-label"><spring:message code='user.active?'/></label>
                            <div class="col-md-5">

                                <form:checkbox  path="active" id="active" disabled = "true" />

                            </div>
                        </div>
                </fieldset>
            </div>
        </div>
        <div class="row">          
            <fieldset>
                <legend><spring:message code='user.userOffice'/>
                    &nbsp;
                    <span id="labelMandatoryLocation" class="mandatory" style="display: none; font-size: 14px">
                        *<spring:message code='label.userLocationRequired'/>
                    </span>
                </legend>
                <div class="row">
                    <div class="col-md-6">  
                        <div class="form-group">
                            <label for="roleInput" class="col-md-4 control-label"><spring:message code="user.role" /></label>
                            <div class="col-md-8">
                                ${user.role.nameInBangla}

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userType" class="col-md-4 control-label"><spring:message code="label.userType" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <label for="userType" class="control-label">${user.userPerScheme.userType}</label>


                            </div>
                        </div>

                    </div>
                </div>
            </fieldset>

        </div>

    </section>       




</form:form>
<input type="hidden" value="${message}" id="input-msg"/>

<button type="button" id="btnModal" style="display: none" class="btn btn-primary" data-toggle="modal" data-target="#modaItem"></button>

<div class="modal fade bd-example-modal-md" id="modaItem" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="modalBody">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="btn_close_item_modal"><spring:message code="close"/></button>

            </div>
        </div>
    </div>
</div>  

<script type="text/javascript">


    $("#pageTitle").html("Edit User Profile");

    function removeProfilePhoto()
    {
        return "";

    }
    function submitForm() {
        var form = $('#userForm');
        form.validate();
        if (!form.valid()) {
            return;
        }
        var flag = 1;
        var message = "";
        if (message != "")
        {
            messageDecorate(message);
            return false;
        }
        if (selectedLocale === "bn")
        {
            $("#mobileNo").val(getNumberInEnglish($("#mobileNo").val()));
        }
        if (flag === 1) {
            $("#submitButton").click();
        }
    }


    $(document).ready(function ()
    {
        if ($("#input-msg").val() != "")
        {
            $("#modalBody").html("${message}");
            $("#btnModal").click();
        }
        makeUnijoyEditor('fullNameBn');
        $.validator.addMethod("checkMobileNo", function (value, element) {
            return this.optional(element) || /^01[3456789][0-9]{8}$/.test(value) || /^০১[৩৪৫৬৭৮৯][০-৯]{8}$/.test(value);
        });
        $.validator.addMethod(
                "uniqueUserID",
                function (value, element) {
                    $.ajax({
                        type: "GET",
                        url: "${pageContext.request.contextPath}/checkUserAvailibilityForProfileUpdate/" + value + "/" + $("#id").val(),
                        async: false,
                        dataType: "html",
                        success: function (msg)
                        {
                            //If userid exists, set response to true
                            response = (msg === 'true') ? true : false;
                        }
                    });
                    return response;
                });
        $.validator.addMethod(
                "uniqueMobileNo",
                function (value, element) {
                    $.ajax({
                        type: "POST",
                        url: "${pageContext.request.contextPath}/checkAvailabilityOfMobileNo",
                        data: {'mobileNo': value, 'userId': $("#id").val()},
                        async: false,
                        dataType: "html",
                        success: function (msg)
                        {
                            //If mobile exists, set response to true
                            response = (msg === 'true') ? true : false;
                        }
                    });
                    return response;
                });
        $.validator.addMethod("validEmailAddress",
                function (value, element) {
                    return this.optional(element) || /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
                });

        $.validator.addMethod(
                "uniqueEmailAddress",
                function (value, element) {
                    $.ajax({
                        type: "POST",
                        url: "${pageContext.request.contextPath}/checkAvailabilityOfEmailAddress",
                        data: {'email': value, 'userId': $("#id").val()},
                        async: false,
                        dataType: "html",
                        success: function (msg)
                        {
                            //If email exists, set response to true
                            response = (msg === 'true') ? true : false;
                        }
                    });
                    return this.optional(element) || response;
                });


        $("#userForm").validate({
            rules: {
                "userID": {
                    required: true,
                    minlength: 3,
                    maxlength: 255,
                    uniqueUserID: true
                },
                "fullNameBn": {
                    required: true
                },
                "fullNameEn": {
                    required: true
                },
                "designation": {
                    required: true
                },
                "password": {
                    required: true,
                    minlength: 6
                },
                "confirmPassword": {
                    required: true,
                    minlength: 6,
                    equalTo: "#password"
                },
                "email": {
                    validEmailAddress: true,
                    uniqueEmailAddress: true
                },
                "mobileNo": {
                    required: true,
                    checkMobileNo: true,
                    uniqueMobileNo: true
                }

            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });

</script>
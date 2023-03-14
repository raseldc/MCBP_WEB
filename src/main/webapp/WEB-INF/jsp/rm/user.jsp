<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<style>
    .error{
        color:red;
    }
    .table thead tr th{
        background-color: #EEE;
    }
    .tf-data{
        background-color: #EEE;
    }
    .th-style{
        background-color: lightblue;
        padding: 5px;
    }
</style>

</style>
<script type="text/javascript">

    $(function () {

        var userRole = $("#userRole").val();
        var contextPath = "${pageContext.request.contextPath}";
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $(menuSelect("${pageContext.request.contextPath}/user/list"));
        $(".fileinput-upload-button").hide();
        $('#user-delete').attr("data-toggle", "modal").attr("data-target", "#usermodel-delete-confirmation");
//        $(document.getElementById('userTypeId').options).each(function (index, option) {
//            if (option.value != '') {
//                option.hidden = true; // not fully compatible. option.style.display = 'none'; would be an alternative or $(option).hide();
//            }
//        });

        makeUnijoyEditor('fullNameBn');
        $('#fullNameEn').focus();
        $('#fullNameBn').focus();
        if (selectedLocale === 'bn')
        {
            makeUnijoyEditor('designation');
            makeUnijoyEditor('mobileNo');
            $("#mobileNo").val(getNumberInBangla("" + $("#mobileNo").val() + ""));
        }
        $("#add").val(0);
        var response = false;
        $.validator.addMethod(
                "uniqueUserID",
                function (value, element) {
                    $.ajax({
                        type: "GET",
                        url: "${pageContext.request.contextPath}/checkAvailabilityOfUserID/" + value,
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
        if ('${user.id}' !== '') //during edit
        {
            $('#userID').rules('remove', 'uniqueUserID');
            if ($("#userTypeId").val() === "FIELD")
            {
                $("#areaBlock").show();
                $("#locationInfo").show();
            } else if ($("#userTypeId").val() === "BGMEA")
            {
                $("#bgmeaBlock").show();
            } else if ($("#userTypeId").val() === "BKMEA")
            {
                $("#bkmeaBlock").show();
            }
        } else
        {
            $("#userID").val("");
            $("#password").val("");
        }
        var locale = "${pageContext.response.locale}";
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + locale + ".js");

        $("#userType").change(function () {
            var end = this.value;
            if (end === 'FIELD')
            {
                loadDivision($("select#presentDivision"), '${user.division.id}'); // can not load chain through parent
                return;
            } else
            {
                resetSelect($('#presentDivision')); // can not reset through parent?
                resetSelect($('#presentDistrict'));
                resetSelect($('#presentUpazila'));
                resetSelect($('#presentUnion'));
                return;
            }

        });
        if (userRole == "3" || userRole == "17") {
            upazilaId = $("#userPerScheme.upazilla.id").val();
            var unionSelectId = $('#presentUnion');
            loadUnion(upazilaId, unionSelectId, '${user.userPerScheme.union.id}');
        }
    });
    $(window).load(function () {
        loadDivision($("select#presentDivision"), '${user.userPerScheme.division.id}');
        loadPresentDistrictList($('#presentDivision')[0]);
        loadPresentUpazilaList($('#presentDistrict')[0]);
        loadPresentUnionList($('#presentUpazila')[0]);
        $('#userType').trigger('change');
    });
    function messageDecorate(message)
    {
        bootbox.dialog({
            onEscape: function () {},
            title: '<spring:message code="label.warning" />',
            message: "<b>" + message + "</b>",
            buttons: {
                ok: {
                    label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                }
            },

            callback: function (result) {
                return false;
            }
        });
    }
    function loadPresentDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#presentDistrict');
        if (divId !== '') {
            loadDistrict(divId, distSelectId, '${user.userPerScheme.district.id}');
        } else {
            resetSelect(distSelectId);
            resetSelect($('#presentUpazila'));
            resetSelect($('#presentUnion'));
        }
    }
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#presentUpazila');
        if (distId !== '') {
            if ($("#areaTypeId").val() == 1)
            {
                loadUpazilla(distId, upazillaSelectId, '${user.userPerScheme.upazilla.id}');
            } else
            {
                loadUpazillaWithDistrict(distId, upazillaSelectId, '${user.userPerScheme.upazilla.id}');
            }
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#presentUnion'));
        }
    }
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#presentUnion');

        if (upazilaId !== '') {
            if ($("#areaTypeId").val() == 1)
            {
                loadUnion(upazilaId, unionSelectId, '${user.userPerScheme.union.id}');
            } else
            {
                loadMunicipal(upazilaId, unionSelectId, '${user.userPerScheme.union.id}');
            }

        } else {
            resetSelect(unionSelectId);
        }
    }
    function removeProfilePhoto()
    {
        document.getElementById("profilePhotoFile").style.display = "none";//
        document.getElementById("removeProfilePhoto").style.display = "none";//
        $("#profilePhotoFile").val("");
        $("#photo").attr("style", "display:block");
    }
    function areaBlock()
    {
        if ($("#userTypeId").val() == "FIELD")
        {
            $("#areaBlock").show();
            $("#bgmeaBlock").hide();
            $("#bkmeaBlock").hide();
            $('#bgmeaFactoryId').val("");
            $('#bkmeaFactoryId').val("");
        } else if ($("#userTypeId").val() === "BGMEA")
        {
            loadDivision($("select#presentDivision"), '${user.userPerScheme.division.id}');
            resetSelect($('#presentDistrict'));
            resetSelect($('#presentUpazila'));
            resetSelect($('#presentUnion'));
            $("#areaTypeId").val("");
            $("#locationInfo").hide();
            $("#areaBlock").hide();
            $("#bgmeaBlock").show();
            $("#bkmeaBlock").hide();
            $('#bkmeaFactoryId').val("");
        } else if ($("#userTypeId").val() === "BKMEA")
        {
            loadDivision($("select#presentDivision"), '${user.userPerScheme.division.id}');
            resetSelect($('#presentDistrict'));
            resetSelect($('#presentUpazila'));
            resetSelect($('#presentUnion'));
            $("#areaTypeId").val("");
            $("#locationInfo").hide();
            $("#areaBlock").hide();
            $("#bkmeaBlock").show();
            $("#bgmeaBlock").hide();
            $('#bgmeaFactoryId').val("");
        } else
        {
            loadDivision($("select#presentDivision"), '${user.userPerScheme.division.id}');
            resetSelect($('#presentDistrict'));
            resetSelect($('#presentUpazila'));
            resetSelect($('#presentUnion'));
            $("#areaTypeId").val("");
            $("#locationInfo").hide();
            $("#areaBlock").hide();
            $('#bgmeaFactoryId').val("");
            $('#bkmeaFactoryId').val("");
        }
    }
    function locationBlock()
    {
        var userRole = $("#userRole").val();
        if ($("#areaTypeId").val() == "1")
        {
            $("#locationInfo").show();
            loadDivision($("select#presentDivision"), '${user.division.id}');
            resetSelect($('#presentDistrict'));
            resetSelect($('#presentUpazila'));
            resetSelect($('#presentUnion'));
            $("#labelUpazila").text("<spring:message code="label.upazila" />");
            $("#labelUnion").text("<spring:message code="label.union" />");

            if (userRole == "3" || userRole == "17") {
                upazilaId = $("#upazilaId").val();
                var unionSelectId = $('#presentUnion');
                loadUnion(upazilaId, unionSelectId, '${user.userPerScheme.union.id}');
            }

        } else
        {
            $("#locationInfo").show();
            loadDivision($("select#presentDivision"), '${user.division.id}');
            resetSelect($('#presentDistrict'));
            resetSelect($('#presentUpazila'));
            resetSelect($('#presentUnion'));
            $("#labelUpazila").text("<spring:message code="label.districtOrUpazila"/>");
            $("#labelUnion").text("<spring:message code="label.municipalOrCityCorporation"/>");

            if (userRole == "3" || userRole == "17") {
                upazilaId = $("#upazilaId").val();
                var unionSelectId = $('#presentUnion');
                loadUnion(upazilaId, unionSelectId, '${user.userPerScheme.union.id}');
            }
        }
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
</script>
<input type="hidden" value="-1" id="inputRowCount"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/user/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/user/edit/${user.id}" />
    </c:otherwise>
</c:choose>
<form:form id="userForm" action="${actionUrl}" class="form-horizontal" enctype="multipart/form-data" role="form" modelAttribute="user">
    <form:input type="hidden" path="userPerScheme.id"/>
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.user" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/user/list"><spring:message code="label.backToList" /></a></small>
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
        <div class="row">
            <div class="col-md-12">  
                <fieldset>
                    <legend><spring:message code='user.basicInfo'/></legend>
                    <form:hidden path="id" />
                    <form:hidden path="status" />
                    <input type="hidden" id="userRole" value="${userDetail.roleId}"/>
                    <form:hidden path="salt" />
                    <div class="col-md-6">  
                        <div class="form-group">
                            <label for="fullNameBn" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.nameBn' var="fullNameBn"/>
                                <form:input class="form-control" placeholder="${fullNameBn}" path="fullNameBn" autofocus="autofocus"/>
                                <form:errors path="fullNameBn" cssclass="error"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="fullNameEn" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.nameEn' var="fullNameEn"/>
                                <form:input class="form-control" placeholder="${fullNameEn}" path="fullNameEn" onkeydown="checkEnglishAlphabetWithLength(event, this, 30)"/>
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
                                <c:choose>
                                    <c:when test="${actionType eq 'create'}">
                                        <form:input class="form-control" placeholder="${userID}" path="userID" onkeydown="checkEnglishAlphabetAndNumberWithLength(event, this, 255)"/>
                                    </c:when>
                                    <c:otherwise>
                                        <form:input class="form-control" placeholder="${userID}" path="userID" readonly="true"/>
                                    </c:otherwise>
                                </c:choose>

                                <form:errors path="userID" cssclass="error"></form:errors>                                
                                </div>
                            </div>
                        <c:if test="${actionType eq 'create'}">
                            <div class="form-group">
                                <label for="password" class="col-md-4 control-label"><spring:message code="user.password" /><c:if test="${actionType eq 'create'}"><span class="mandatory">*</span></c:if></label>
                                    <div class="col-md-8">
                                    <spring:message code='user.password' var="password"/>
                                    <form:input type="password" class="form-control" placeholder="${password}" path="password"/> 
                                    <form:errors path="password" cssclass="error"></form:errors>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="confirmPassword" class="col-md-4 control-label"><spring:message code="label.confirmPassword" /><c:if test="${actionType eq 'create'}"><span class="mandatory">*</span></c:if></label>
                                    <div class="col-md-8">
                                    <spring:message code='label.confirmPassword' var="cpassword"/>
                                    <form:input type="password" class="form-control" placeholder="${cpassword}" path="confirmPassword" />                                 
                                    <form:errors path="confirmPassword" cssclass="error"></form:errors>
                                    </div>
                                </div>  
                        </c:if>  
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="mobileNoInput" class="col-md-4 control-label"><spring:message code="user.mobileNo" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code="user.mobileNo" var="mobileNo"/>
                                <form:input class="form-control" placeholder="${mobileNo}" path="mobileNo" onkeydown="checkNumberWithLength(event, this, 11)"/>
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
                                    <img id="profilePhotoFile" name="profilePhotoFile" src="${contextPath}/${imagePath}/${user.profilePicPath}" style="width: 250px">
                                    <input type="image" src="${contextPath}/resources/img/remove16.png" id="removeProfilePhoto" value="Remove" onClick="removeProfilePhoto();
                                            return false;" /> 
                                </c:if>
                                <form:errors path="profilePicPath" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>                        
                            <div class="form-group">
                                <label for="active" class="col-md-4 control-label"><spring:message code='user.active?'/></label>
                            <div class="col-md-5">
                                <div class="checkbox icheck">
                                    <label>
                                        <form:checkbox path="active" id="active" />
                                    </label>
                                </div>                        
                            </div>
                        </div>
                </fieldset>
            </div>
        </div>
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
                        <label for="roleInput" class="col-md-4 control-label"><spring:message code="user.role" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='user.role' var="roleLabel"/>
                            <form:select class="form-control" path="userPerScheme.role.id">
                                <form:option value="" label="${roleLabel}"></form:option>
                                <form:options items="${roleList}" itemValue="id" itemLabel="${roleName}"></form:options>
                            </form:select>
                            <form:errors path="userPerScheme.role.id" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userType" class="col-md-4 control-label"><spring:message code="label.userType" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <spring:message code='label.userType' var="userType"/>
                            <form:select class="form-control" placeholder="${userType}" path="userPerScheme.userType" id="userTypeId" onchange="areaBlock();" autofocus="autofocus">
                                <form:option value="" label="${select}" />
                                <c:forEach items="${userTypeList}" var="userType">
                                    <c:if test="${pageContext.response.locale=='en'}">   
                                        <form:option value="${userType}" label="${userType.displayName}">${userType.displayName}</form:option>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='bn'}">   
                                        <form:option value="${userType}" label="${userType.displayNameBn}">${userType.displayNameBn}</form:option>
                                    </c:if>
                                </c:forEach>
                            </form:select>
                            <form:errors path="userPerScheme.userType" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group" id="areaBlock" style="display: none">
                            <label for="areaType" class="col-md-4 control-label"><spring:message code="label.area" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.area' var="schemeLabel"/>
                            <form:select class="form-control" path="userPerScheme.scheme.id" id="areaTypeId" name="areaTypeId" onchange="locationBlock();">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${schemeList}" itemValue="id" itemLabel="${schemeName}"></form:options>
                            </form:select>
                            <form:errors path="userPerScheme.scheme.id" cssClass="error"></form:errors>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">  
                        <div id="locationInfo" style="display: none">  
                            <div class="form-group">
                                <label for="divisionInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.division' var="roleLabel"/>
                                <c:choose>
                                    <c:when  test="${userDetail.roleId == 3|| userDetail.roleId ==17}">
                                        <c:if test="${schemeName eq 'nameInEnglish'}">
                                            ${userDetail.division.nameInEnglish}
                                        </c:if>
                                        <c:if test="${schemeName eq 'nameInBangla'}">
                                            ${userDetail.division.nameInBangla}
                                        </c:if>
                                        <form:hidden  name="divisionId" path="userPerScheme.division.id" value="${userDetail.division.id}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <form:select class="form-control" path="userPerScheme.division.id" id="presentDivision" name="presentDivision" onchange="loadPresentDistrictList(this)">
                                            <form:option value="" label="${divisionLabel}"></form:option>
                                            <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options>
                                        </form:select>
                                        <form:errors path="userPerScheme.division.id" cssClass="error"></form:errors>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                            <div class="col-md-8">
                                <c:choose>
                                    <c:when  test="${userDetail.roleId == 3|| userDetail.roleId == 17}">
                                        <c:if test="${schemeName eq 'nameInEnglish'}">
                                            ${userDetail.district.nameInEnglish}
                                        </c:if>
                                        <c:if test="${schemeName eq 'nameInBangla'}">
                                            ${userDetail.district.nameInBangla}
                                        </c:if>
                                        <form:hidden id="districtId" name="districtId" path="userPerScheme.district.id" value="${userDetail.district.id}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code='label.district' var="districtLabel"/>
                                        <form:select class="form-control" path="userPerScheme.district.id" id="presentDistrict" name="presentDistrict" onchange="loadPresentUpazilaList(this)">
                                            <form:option value="" label="${districtLabel}"></form:option>
                                            <form:options items="${districtList}" itemValue="id" itemLabel="districtName"></form:options>
                                        </form:select>
                                        <form:errors path="userPerScheme.district.id" cssClass="error"></form:errors>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label" id="labelUpazila"><spring:message code="label.upazila" /></label>
                            <div class="col-md-8">
                                <c:choose>
                                    <c:when  test="${userDetail.roleId == 3|| userDetail.roleId == 17}">
                                        <c:if test="${schemeName eq 'nameInEnglish'}">
                                            ${userDetail.upazila.nameInEnglish}
                                        </c:if>
                                        <c:if test="${schemeName eq 'nameInBangla'}">
                                            ${userDetail.upazila.nameInBangla}
                                        </c:if>
                                        <form:hidden id="upazilaId" name="upazilaId" path="userPerScheme.upazilla.id" value="${userDetail.upazila.id}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code='label.upazila' var="upazilaLabel"/>
                                        <form:select class="form-control" path="userPerScheme.upazilla.id" id="presentUpazila" name="presentUpazila" onchange="loadPresentUnionList(this)">
                                            <form:option value="" label="${upazilaLabel}"></form:option>
                                            <form:options items="${upazilaList}" itemValue="id" itemLabel="upazilaName"></form:options>
                                        </form:select>
                                        <form:errors path="userPerScheme.upazilla.id" cssClass="error"></form:errors>
                                    </c:otherwise>
                                </c:choose>


                            </div>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label" id="labelUnion"><spring:message code="label.union" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.union' var="unionLabel"/>
                                <form:select class="form-control" path="userPerScheme.union.id" id="presentUnion" name="presentUnion">
                                    <form:option value="" label="${unionLabel}"></form:option>
                                    <form:options items="${unionList}" itemValue="id" itemLabel="unionName"></form:options>
                                </form:select>
                                <form:errors path="userPerScheme.union.id" cssClass="error"></form:errors>
                                </div>
                            </div>
                        </div>
                        <div class="form-group" id="bgmeaBlock" style="display: none">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bgmea" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="userPerScheme.bgmeaFactory.id" id="bgmeaFactoryId">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${bgmeaFactoryList}" itemValue="id" itemLabel="${bgmeaFactoryName}"></form:options> 
                            </form:select>
                            <form:errors path="userPerScheme.bgmeaFactory.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group" id="bkmeaBlock" style="display: none">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bkmea" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="userPerScheme.bkmeaFactory.id" id="bkmeaFactoryId">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${bkmeaFactoryList}" itemValue="id" itemLabel="${bkmeaFactoryName}"></form:options> 
                            </form:select>
                            <form:errors path="userPerScheme.bkmeaFactory.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>  
                        </d
                    </div>   
            </fieldset>
        </section>
</form:form>

<div id='usermodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="usermodel-delete-confirmation-title">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
            </div>
            <form action="${contextPath}/user/delete/${user.id}" method="post">
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

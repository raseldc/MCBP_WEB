<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/plugins/messageResources/messageResource.min.js" />" ></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/applicantTab.css" />">

<style>
    .error{
        color:red;
    }

</style>


<script type="text/javascript">

    $(document).ready(function () {
        $(menuSelect("${pageContext.request.contextPath}/beneficiary/applicantForm"));
//        loadApplicantTabs('${actionType}', '${nextActiveTab}');
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");

//changed by mahbub for enable edititing            
//        if ("${actionType ne 'create'}" == 'true') {
//            $("#permanentAddressId input").attr("disabled", "disabled");
//            $("#permanentAddressId select").attr("disabled", "disabled");
//        }

        if (selectedLocale == 'bn') {
            makeUnijoyEditor('presentAddressLine1');
            makeUnijoyEditor('presentAddressLine2');
            makeUnijoyEditor('presentWardNo');
            makeUnijoyEditor('presentPostCode');
        }
        document.getElementById("presentWardNo").addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 3);
        });
        document.getElementById("presentPostCode").addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 4);
        });

        $("#addressForm").validate({
            rules: {
                "presentAddressLine1": {
                    required: true
                },
                "presentDivision.id": {
                    required: true
                },
                "presentDistrict.id": {
                    required: true
                },
                "presentUpazila.id": {
                    required: true
                },
                "presentUnion.id": {
                    required: true
                },
                "presentPostCode": {
                    required: true
                },
                "permanentAddressLine1": {
                    required: true
                },
                "permanentDivision.id": {
                    required: true
                },
                "permanentDistrict.id": {
                    required: true
                },
                "permanentUpazila.id": {
                    required: true
                },
                "permanentUnion.id": {
                    required: true
                },
                "permanentPostCode": {
                    required: true
                },
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });

        $('#sameAsPresent').on('ifChecked', function () {
            loadPermanentAddressSameAsPresent(this);
        });
        $('#sameAsPresent').on('ifUnchecked', function () {
            resetPermanentAddress();
        });

//        var contextPath = "${pageContext.request.contextPath}";
    });

    $(function () {
        if ('${sessionScope.userDetail.schemeShortName}' == 'LMA')
        {
            if ('${sessionScope.userDetail.userType.displayName}' == 'Ministry' || '${sessionScope.userDetail.userType.displayName}' == 'Directorate')
            {
                $("#applicantTypeBlock").show();
                if ($('#regularUser').is(":checked"))                
                {
                    $("#bgmeaBlock").hide();
                    $("#bkmeaBlock").hide();
                }                    
                if ($('#bgmeaUser').is(":checked"))
                {
                    $("#bgmeaBlock").show(); 
                    $("#bkmeaBlock").hide();
                }
                if ($('#bkmeaUser').is(":checked"))
                {
                    $("#bgmeaBlock").hide(); 
                    $("#bkmeaBlock").show();
                }
            }
            
            if ('${sessionScope.userDetail.userType.displayName}' == 'BGMEA')
            {
                $("#applicantTypeBlock").hide();
                $('#bgmeaBlock').show();
                $("#bkmeaBlock").hide();
            }
            if ('${sessionScope.userDetail.userType.displayName}' == 'BKMEA')
            {
                $("#applicantTypeBlock").hide();
                $('#bgmeaBlock').hide();
                $("#bkmeaBlock").show();
            }
            if ('${sessionScope.userDetail.userType.displayName}' == 'Others')
            {
                $('#lmaBlock').hide();
            }
        } else
        {
            $('#lmaBlock').hide();
        }
        $('#regularUser').on('ifChanged', function (event) {
            if ($('#regularUser').is(":checked"))
            {
                $("applicantType").val("REGULAR");
                $("#regularBlock").show();
                $("#bgmeaBlock").hide();
                $("#bgmeaFactoryId").prop("selectedIndex", 0);
                $("#bkmeaBlock").hide();
                $("#bkmeaFactoryId").prop("selectedIndex", 0);
                $('#bgmeaUser').iCheck('uncheck');
                $('#bkmeaUser').iCheck('uncheck');
            }
        });
        $('#bgmeaUser').on('ifChanged', function (event) {
            if ($('#bgmeaUser').is(":checked"))
            {
                $("#applicantType").val("BGMEA");
                $("#bgmeaBlock").show();
                $("#bkmeaBlock").hide();
                $("#bkmeaFactoryId").prop("selectedIndex", 0);
                $("#regularBlock").hide();                
                $('#regularUser').iCheck('uncheck');
                $('#bkmeaUser').iCheck('uncheck');
            }
        });
        $('#bkmeaUser').on('ifChanged', function (event) {
            if ($('#bkmeaUser').is(":checked"))
            {
                $("#applicantType").val("BKMEA");
                $("#bkmeaBlock").show();
                $("#bgmeaBlock").hide();
                $("#bgmeaFactoryId").prop("selectedIndex", 0);
                $("#regularBlock").hide();             
                $('#regularUser').iCheck('uncheck');
                $('#bgmeaUser').iCheck('uncheck');
            }
        });

        //loadDivision($("select#presentDivision"), '${addressForm.presentDivision.id}');
        loadPresentDistrictList($('#presentDivision')[0]);
        loadPresentUpazilaList($('#presentDistrict')[0]);
        loadPresentUnionList($('#presentUpazila')[0]);

        //loadDivision($("select#permDivision"), '${addressForm.permanentDivision.id}');
        loadPermDistrictList($('#permDivision')[0]);
        loadPermUpazilaList($('#permDistrict')[0]);
        loadPermUnionList($('#permUpazila')[0]);

    });

    function goToPreviousPage() {
        var form = $('#addressForm');
        submitFormAjax(form, 'Previous', 'personalInfoForm', 'Personal Information | Add/View Applicant');
    }

    function submitForm() {
        var form = $('#addressForm');
        // changed by mahbub for enable editing
//        if ("${actionType ne 'create'}" == 'true') {
//            $("#permanentAddressId input").attr("disabled", false);
//            $("#permanentAddressId select").attr("disabled", false);
//        }
        form.validate();
        if (form.valid()) {
            makePermanentAddressReadOnly(false);
            submitFormAjax(form, 'Next', 'socioEconomicForm', 'Socio Economic Information | Add/View Applicant');
        } else
        {
            if ("${actionType ne 'create'}" == 'true') {
                $("#permanentAddressId input").attr("disabled", "disabled");
                $("#permanentAddressId select").attr("disabled", "disabled");
            }
        }
    }

    function loadPermDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#permDistrict');
        if (divId !== '') {
            loadDistrict(divId, distSelectId, '${addressForm.permanentDistrict.id}');
        } else {
            resetSelect(distSelectId);
            resetSelect($('#permUpazila'));
            resetSelect($('#permUnion'));
        }
    }
    function loadPresentDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#presentDistrict');
        if (divId !== '') {
            loadDistrict(divId, distSelectId, '${addressForm.presentDistrict.id}');
        } else {
            resetSelect(distSelectId);
            resetSelect($('#presentUpazila'));
            resetSelect($('#presentUnion'));
        }
    }

    function loadPermUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#permUpazila');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId, '${addressForm.permanentUpazila.id}');
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#permUnion'));
        }
    }
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#presentUpazila');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId, '${addressForm.presentUpazila.id}');
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#presentUnion'));
        }
    }

    function loadPermUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#permUnion');

        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId, '${addressForm.permanentUnion.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#presentUnion');

        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId, '${addressForm.presentUnion.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }

    function loadPermanentAddressSameAsPresent(object) {
        $('#permanentAddressLine1').val($('#presentAddressLine1').val());
        $('#permanentAddressLine2').val($('#presentAddressLine2').val());
        $('#permDivision').val($('#presentDivision').val());
        loadPermDistrictList($('#permDivision')[0]);
        $('#permDistrict').val($('#presentDistrict').val());
        loadPermUpazilaList($('#permDistrict')[0]);
        $('#permUpazila').val($('#presentUpazila').val());
        loadPermUnionList($('#permUpazila')[0]);
        $('#permUnion').val($('#presentUnion').val());
        $('#permanentWardNo').val($('#presentWardNo').val());
        $('#permanentPostCode').val($('#presentPostCode').val());
        makePermanentAddressReadOnly(true);
    }
    function resetPermanentAddress() {
        $('#permanentAddressLine1').val("");
        $('#permanentAddressLine2').val("");
        resetSelect($('#permDivision'));
        loadDivision($("select#permDivision"));
        resetSelect($('#permDistrict'));
        resetSelect($('#permUpazila'));
        resetSelect($('#permUnion'));
        $('#permanentWardNo').val("");
        $('#permanentPostCode').val("");

        makePermanentAddressReadOnly(false);
    }

    function makePermanentAddressReadOnly(value) {
        // make fields read only
        $('#permanentAddressLine1').attr("disabled", value);
        $('#permanentAddressLine2').attr("disabled", value);
        $("#permDivision").attr("disabled", value);
        $("#permDistrict").attr("disabled", value);
        $("#permUpazila").attr("disabled", value);
        $("#permUnion").attr("disabled", value);
        $("#permanentWardNo").attr("disabled", value);
        $("#permanentPostCode").attr("disabled", value);
    }


</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="selectedLocale" value="${pageContext.response.locale}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/beneficiary/addressForm" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/beneficiary/addressForm/${addressForm.id}" />
    </c:otherwise>
</c:choose>
<form:form id="addressForm" action="${actionUrl}"  class="form-horizontal" role="form" modelAttribute="addressForm">
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
                <!--            <div class="nav-tabs-custom">
                                <ul class="nav nav-tabs">-->
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
                        <li class="selected">
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
                        <li>                           
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
                            Step 2 of 6
                        </c:if>
                        <c:if test="${selectedLocale eq 'bn'}">
                            ধাপ ২/৬
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
                    <input type="hidden" id="applicantId" name="applicantId" value="${addressForm.id}">
                    <form:hidden path="fiscalYear.id" />
                    <form:hidden path="applicationId" />
                    <form:hidden path="applicantType" id="applicantType"/>
                    <c:if test="${sessionScope.userDetail.scheme.shortName eq 'LMA'}">
                        <div class="row" id="lmaBlock">
                            <div class="col-md-6">
                                <c:if test="${sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">   
                                    <div class="form-group" id="applicantTypeBlock" style="display: none">
                                        <div class="form-group">
                                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.applicantType" /><span class="mandatory">*</span></label>
                                            <div class="col-md-8 labelAsValue">
                                                <input type="radio" id="regularUser" name="applicantTypeRadio" <c:if test="${addressForm.applicantType eq 'REGULAR'}">checked</c:if>>&nbsp;<spring:message code="applicant.regular" />
                                                <input type="radio" id="bgmeaUser" name="applicantTypeRadio" <c:if test="${addressForm.applicantType eq 'BGMEA'}">checked</c:if>>&nbsp;<spring:message code="applicant.bgmea" />
                                                <input type="radio" id="bkmeaUser" name="applicantTypeRadio" <c:if test="${addressForm.applicantType eq 'BKMEA'}">checked</c:if>>&nbsp;<spring:message code="applicant.bkmea" />
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${sessionScope.userDetail.userType.displayName eq 'BGMEA' || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">   
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
                                <c:if test="${sessionScope.userDetail.userType.displayName eq 'BKMEA' || sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">      
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

                            </div>
                        </div>
                    </c:if>
                    <div class="form-group">
                        <div class="col-md-6">
                            <fieldset>
                                <legend>
                                    <spring:message code='label.presentAddress' var="presentAddress"/>
                                    ${presentAddress}
                                </legend>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine1" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.addressLine1' var="addressLine1"/>
                                        <form:input class="form-control" placeholder="${addressLine1}" path="presentAddressLine1" autofocus="autofocus"/>
                                        <form:errors path="presentAddressLine1" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine2" /></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.addressLine2' var="addressLine2"/>
                                        <form:input class="form-control" placeholder="${addressLine2}" path="presentAddressLine2" autofocus="autofocus"/>
                                        <form:errors path="presentAddressLine2" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="presentDivision.id"  id="presentDivision" onchange="loadPresentDistrictList(this)"> 
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options> 
                                        </form:select> 
                                        <form:errors path="presentDivision.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="presentDistrict.id" id="presentDistrict" onchange="loadPresentUpazilaList(this)">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${districtList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                        </form:select>
                                        <form:errors path="presentDistrict.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.upazila" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="presentUpazila.id" id="presentUpazila" onchange="loadPresentUnionList(this)">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${upazilaList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                        </form:select>
                                        <form:errors path="presentUpazila.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.union" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="presentUnion.id" id="presentUnion">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${unionList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                        </form:select>
                                        <form:errors path="presentUnion.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.wardNo" /></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.wardNo' var="wardNo"/>
                                        <form:input class="form-control" placeholder="${wardNo}" path="presentWardNo" autofocus="autofocus"
                                                    onkeydown="checkNumberWithLength(event, this, 4)"/>
                                        <form:errors path="presentWardNo" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.postCode" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.postCode' var="postCode"/>
                                        <form:input class="form-control" placeholder="${postCode}" path="presentPostCode" autofocus="autofocus"
                                                    onkeydown="checkNumberWithLength(event, this, 4)"/>
                                        <form:errors path="presentPostCode" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-md-2 col-md-offset-0">
                                        <spring:message code='button.previousBtn' var="previousBtn"/>
                                        <button type="submit" class="btn btn-primary btnPrevious pull-left"  name="action"  value="Previous" onclick="goToPreviousPage();"><span class="glyphicon glyphicon-chevron-left"></span>${previousBtn}</button>
                                    </div>
                                </div>  
                            </fieldset>
                        </div>
                        <div id="permanentAddressId" class="col-md-6">
                            <fieldset>
                                <legend>
                                    <div class="col-md-6">
                                        <spring:message code='label.permanentAddress' var="permanentAddress"/>                                            
                                        ${permanentAddress}
                                    </div>
                                    <div style="text-align: right">
                                        <spring:message code='label.sameAsPresent' var="sameAsPresent"/>
                                        <input type="checkbox" id="sameAsPresent" onclick="loadPermanentAddressSameAsPresent(this);">${sameAsPresent}</input>
                                    </div>
                                </legend>
                                <div class="form-group">
                                    <label for="addressLine1Input" class="col-md-4 control-label"><spring:message code="label.addressLine1" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.addressLine1' var="addressLine1"/>
                                        <form:input class="form-control" placeholder="${addressLine1}" path="permanentAddressLine1" autofocus="autofocus"/>
                                        <form:errors path="permanentAddressLine1" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="addressLine2Input" class="col-md-4 control-label"><spring:message code="label.addressLine2" /></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.addressLine2' var="addressLine2"/>
                                        <form:input class="form-control" placeholder="${addressLine2}" path="permanentAddressLine2" autofocus="autofocus"/>
                                        <form:errors path="permanentAddressLine2" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="permanentDivision.id"  id="permDivision" onchange="loadPermDistrictList(this)"> 
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options> 
                                        </form:select>                                                
                                        <form:errors path="permanentDivision.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="permanentDistrict.id" id="permDistrict" onchange="loadPermUpazilaList(this)">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${districtList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                        </form:select>
                                        <form:errors path="permanentDistrict.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label">
                                        <c:choose>
                                            <c:when test="${sessionScope.userDetail.scheme.shortName == 'LMA'}">
                                                <spring:message code="label.districtOrUpazila"/>
                                            </c:when>
                                            <c:otherwise>
                                                <spring:message code="label.upazila" />
                                            </c:otherwise>
                                        </c:choose>
                                        <span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="permanentUpazila.id" id="permUpazila" onchange="loadPermUnionList(this)">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${upazilaList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                        </form:select>
                                        <form:errors path="permanentUpazila.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label">
                                        <c:choose>
                                            <c:when test="${sessionScope.userDetail.scheme.shortName == 'LMA'}">
                                                <spring:message code="label.municipalOrCityCorporation"/>
                                            </c:when>
                                            <c:otherwise>
                                                <spring:message code="label.union" />
                                            </c:otherwise>
                                        </c:choose>
                                        <span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="permanentUnion.id" id="permUnion">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${unionList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                        </form:select>
                                        <form:errors path="permanentUnion.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.wardNo" /></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.wardNo' var="wardNo"/>
                                        <form:input class="form-control" placeholder="${wardNo}" path="permanentWardNo" autofocus="autofocus"
                                                    onkeydown="checkNumberWithLength(event, this, 4)"/>
                                        <form:errors path="permanentWardNo" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.postCode" /><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.postCode' var="postCode"/>
                                        <form:input class="form-control" placeholder="${postCode}" path="permanentPostCode" autofocus="autofocus"
                                                    onkeydown="checkNumberWithLength(event, this, 4)"/>
                                        <form:errors path="permanentPostCode" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-md-2 col-md-offset-10">
                                        <spring:message code='button.nextBtn' var="nextBtn"/>
                                        <button type="submit" class="btn btn-primary btnNext pull-right"  name="action" value=${nextBtn} onclick="submitForm();">  ${nextBtn}<span class="glyphicon glyphicon-chevron-right"></span></button>
                                    </div>
                                </div>
                            </fieldset>
                        </div>                                
                    </div>

                </div>
                <!--                </div>
                            </div>-->
            </div>   
        </div>
    </section>    
</form:form>
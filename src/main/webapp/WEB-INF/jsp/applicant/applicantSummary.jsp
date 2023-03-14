<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<meta name="viewport" content="width=device-width,initial-scale=1.0" />
<link rel="stylesheet" href="<c:url value="/resources/css/AdminLTE.css" />">        
<link rel="stylesheet" href="<c:url value="/resources/css/custom.css" />"> 
<style>
    .error{
        color:red;
    }

    .normalLabel{
        font-weight: normal;
    }

    label[for=gender1]{
        margin: 0 10px 0 0;
    }
    .Footer {
        position: fixed;
        bottom: 0;
    }
    .Header {
        position: fixed;
        align-content: center;
        top: 0;
    }
</style>

<script type="text/javascript">
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}${prevUrl}"));
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        document.querySelector("#print").addEventListener("click", function () {
            $("#pageHeader").show();
            window.print();
            $("#pageHeader").hide();
        });
        if ("${message.message}")
        {
            bootbox.dialog({
                onEscape: function () {},
                title: '<spring:message code="label.success" />',
                message: "<b>${message.message}</b>",
                buttons: {
                    ok: {
                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                    }
                },

                callback: function (result) {
                }
            });
        }
    });
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="editUrl" value="${pageContext.request.contextPath}/applicant/edit/${applicant.id}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code='label.viewNewBeneficiaryPageHeader'/>
        <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}${prevUrl}">
                <c:if test="${summaryType eq 'create'}"><spring:message code="label.newApplicant" /></c:if>                
                <c:if test="${summaryType eq 'edit'}"><spring:message code="label.backToList" /></c:if>                
                </a></small>
        </h1>
        <div class="pull-right">
        <c:choose>
            <c:when test="${applicant.applicantType == 'UNION'}">
                <a href="${contextPath}/applicant/union/create" class="btn bg-blue-gradient">
                </c:when>
                <c:when test="${applicant.applicantType == 'MUNICIPAL'}">
                    <a href="${contextPath}/applicant/municipal/create" class="btn bg-blue-gradient">
                    </c:when>
                    <c:when test="${applicant.applicantType == 'CITYCORPORATION'}">
                        <a href="${contextPath}/applicant/cityCorporation/create" class="btn bg-blue-gradient">
                        </c:when>
                        <c:when test="${applicant.applicantType == 'BGMEA'}">
                            <a href="${contextPath}/applicant/bgmea/create" class="btn bg-blue-gradient">
                            </c:when>
                            <c:otherwise>
                                <a href="${contextPath}/applicant/bkmea/create" class="btn bg-blue-gradient">
                                </c:otherwise>
                            </c:choose> 
                            <i class="fa fa-plus-square"></i>
                            <spring:message code="addNew" />
                        </a>
                        <button type="button" name="edit" class="btn bg-blue" onclick="location.href = '${editUrl}'">
                            <i class="fa fa-edit"></i>
                            <spring:message code="edit" />
                        </button>
                        <button type="submit" name="print" class="btn bg-green-active" id="print">
                            <i class="fa fa-print"></i>
                            <spring:message code="print" />
                        </button>
                        </div> 
                        </section>

                        <section class="content">
                            <div class="row" id="printDiv">
                                <h2 class="header_print" id="pageHeader" style="display: none">
                                    <spring:message code='label.viewNewBeneficiaryPageHeader'/>
                                </h2>
                                <div class="col-md-12">
                                    <form:form class="form-horizontal" role="form" modelAttribute="applicant">
                                        <div id="personalInfo">
                                            <legend><spring:message code='label.BasicInfoTab' var="basicInfoTab"/>${basicInfoTab}</legend>                    
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nameBn" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.fullNameInBangla}
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.fatherName" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.fatherName}
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nickName" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.nickName}
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.dob" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <span id="dateOfBirth">${dob}</span>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.educationLevel" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.educationLevelEnum.displayName}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.educationLevelEnum.displayNameBn}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.bloodGroup" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.bloodGroup.displayName}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.bloodGroup.displayNameBn}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.mobileNo" /></label>
                                                    <div class="col-md-8 labelAsValue">                            
                                                        <span id="mobileNo">${mobileNo}</span>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.beneficiaryInOtherScheme" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <span id="beneficiaryInOtherScheme"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nameEn" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.fullNameInEnglish}
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.motherName" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.motherName}
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.spouseName" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.spouseName}
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.birthPlace" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.birthPlace.nameInEnglish}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.birthPlace.nameInBangla}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.religion" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.religionEnum.displayName}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.religionEnum.displayNameBn}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.maritalInfo" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.maritalInfoEnum.displayName}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.maritalInfoEnum.displayNameBn}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nid" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <span id="applicantNid" style="font-weight: bold; color: blue; font-size: 16px">${nid}</span> 
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-2" style="text-align: center">
                                                <div class=""><label><spring:message code='label.BiometricInfoTab' /></label></div>
                                                        <spring:message code='photoNotFound' var='photoNotFound'/>
                                                        <spring:message code='signatureNotFound' var='signatureNotFound'/>
                                                <img id="profilePhotoFile" style="width:100%" alt="${photoNotFound}" src="data:image/jpeg;base64,${applicant.applicantBiometricInfo.base64PhotoData}"/><br>
                                                <img id="signatureFile" style="width:100%" alt="${signatureNotFound}" src="data:image/jpeg;base64,${applicant.applicantBiometricInfo.base64SignatureData}"/>
                                            </div>
                                        </div>
                                        <div id="addressInfo" style="clear: both">
                                            <legend><spring:message code='label.AddressInfoTab' var="addressInfoTab"/>${addressInfoTab}</legend>
                                            <div class="col-md-5">
                                                <legend><h4><spring:message code='label.presentAddress'/></h4></legend>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.presentDivision.nameInEnglish}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.presentDivision.nameInBangla}</c:if>
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.presentDistrict.nameInEnglish}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.presentDistrict.nameInBangla}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label">
                                                        <c:choose>
                                                            <c:when test="${applicant.applicantType == 'UNION'}">
                                                                <spring:message code="label.upazila" />
                                                            </c:when>
                                                            <c:otherwise>
                                                                <spring:message code="label.districtOrUpazila"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.presentUpazila.nameInEnglish}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.presentUpazila.nameInBangla}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label">
                                                        <c:choose>
                                                            <c:when test="${applicant.applicantType == 'UNION'}">
                                                                <spring:message code="label.union" />
                                                            </c:when>
                                                            <c:when test="${applicant.applicantType == 'MUNICIPAL'}">
                                                                <spring:message code="label.municipal" />
                                                            </c:when>
                                                            <c:when test="${applicant.applicantType == 'CITYCORPORATION'}">
                                                                <spring:message code="label.cityCorporation" />
                                                            </c:when>
                                                            <c:otherwise>
                                                                <spring:message code="label.municipalOrCityCorporation"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.presentUnion.nameInEnglish}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.presentUnion.nameInBangla}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.wardNo" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <span id="presentWardNo">${presentWardNo}</span>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.postCode" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <span id="presentPostCode">${presentPostCode}</span>                            
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine1" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.presentAddressLine1}
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine2" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.presentAddressLine2}
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-5">
                                                <legend><h4><spring:message code='label.permanentAddress'/></h4></legend>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.permanentDivision.nameInEnglish}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.permanentDivision.nameInBangla}</c:if>
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.permanentDistrict.nameInEnglish}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.permanentDistrict.nameInBangla}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label">
                                                        <c:choose>
                                                            <c:when test="${applicant.applicantType == 'UNION'}">
                                                                <spring:message code="label.upazila" />
                                                            </c:when>
                                                            <c:otherwise>
                                                                <spring:message code="label.districtOrUpazila"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.permanentUpazila.nameInEnglish}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.permanentUpazila.nameInBangla}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label">
                                                        <c:choose>
                                                            <c:when test="${applicant.applicantType == 'UNION'}">
                                                                <spring:message code="label.union" />
                                                            </c:when>
                                                            <c:when test="${applicant.applicantType == 'MUNICIPAL'}">
                                                                <spring:message code="label.municipal" />
                                                            </c:when>
                                                            <c:when test="${applicant.applicantType == 'CITYCORPORATION'}">
                                                                <spring:message code="label.cityCorporation" />
                                                            </c:when>
                                                            <c:otherwise>
                                                                <spring:message code="label.municipalOrCityCorporation"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </label>        

                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.permanentUnion.nameInEnglish}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.permanentUnion.nameInBangla}</c:if>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.wardNo" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <span id="permanentWardNo">${permanentWardNo}</span>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.postCode" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <span id="permanentPostCode">${permanentPostCode}</span>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine1" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.permanentAddressLine1}
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine2" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.permanentAddressLine2}
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-2">

                                            </div>
                                        </div>
                                        <div id="socioEconomicInfo" style="clear:both">                        
                                            <legend><spring:message code='label.socioEconomicInfoTab' var="socioEconomicInfoTab"/>${socioEconomicInfoTab}</legend>                    
                                            <c:choose>
                                                <c:when test="${applicant.applicantType eq 'UNION'}">
                                                    <div class="col-md-5">
                                                        <div class="form-group">
                                                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.landOwnerShip" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.landSizeRural.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.landSizeRural.displayNameBn}</c:if>
                                                                </div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.husbandOccupation" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.occupationRural.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.occupationRural.displayNameBn}</c:if>
                                                                </div>
                                                            </div> 
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.monthlyIncome" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.monthlyIncome.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.monthlyIncome.displayNameBn}</c:if>
                                                                </div>
                                                            </div>      
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hASLatrine" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hASLatrineRural.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hASLatrineRural.displayNameBn}</c:if>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hASElectricity" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hASElectricity.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hASElectricity.displayNameBn}</c:if>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-5">                                            
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hASElectricFan" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hASElectricFan.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hASElectricFan.displayNameBn}</c:if>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hASTubewell" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hASTubewellRural.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hASTubewellRural.displayNameBn}</c:if>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hhWallMadeOf" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hHWallMadeOf.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hHWallMadeOf.displayNameBn}</c:if>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.iSDisabled" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.disability.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.disability.displayNameBn}</c:if>
                                                                </div>
                                                            </div>    
                                                        </div>
                                                </c:when>

                                                <c:otherwise>
                                                    <div class="col-md-5">
                                                        <div class="form-group">
                                                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hasResidence" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hasResidenceUrban.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hasResidenceUrban.displayNameBn}</c:if>
                                                                </div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.occupation" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.occupationUrban.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.occupationUrban.displayNameBn}</c:if>
                                                                </div>
                                                            </div>                        
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.monthlyIncome" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.monthlyIncome.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.monthlyIncome.displayNameBn}</c:if>
                                                                </div>
                                                            </div>                        
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hASKitchen" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hASKitchenUrban.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hASKitchenUrban.displayNameBn}</c:if>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hASElectricity" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hASElectricity.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hASElectricity.displayNameBn}</c:if>
                                                                </div>
                                                            </div>

                                                        </div>
                                                        <div class="col-md-5">                                            
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hASElectricFan" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hASElectricFan.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hASElectricFan.displayNameBn}</c:if>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hASTelivision" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hASTelivisionUrban.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hASTelivisionUrban.displayNameBn}</c:if>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.hhWallMadeOf" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.hHWallMadeOf.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.hHWallMadeOf.displayNameBn}</c:if>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.iSDisabled" /></label>
                                                            <div class="col-md-8 labelAsValue">
                                                                <c:if test="${pageContext.response.locale eq 'en'}">${applicant.applicantSocioEconomicInfo.disability.displayName}</c:if>
                                                                <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.applicantSocioEconomicInfo.disability.displayNameBn}</c:if>
                                                                </div>
                                                            </div>    
                                                        </div>
                                                </c:otherwise>
                                            </c:choose>     

                                        </div>
                                        <div id="bankAccountInfo" style="clear:both">
                                            <legend><spring:message code='label.BankAccountInfoTab' var="bankAccountInfoTab"/>${bankAccountInfoTab}</legend>
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="paymentType.label.paymentType" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <c:if test="${pageContext.response.locale eq 'en'}">${applicant.paymentType.displayName}</c:if>
                                                        <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.paymentType.displayNameBn}</c:if>
                                                        </div>
                                                    </div>
                                                <c:if test="${applicant.paymentType eq 'BANKING'}">
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.bank" /></label>
                                                        <div class="col-md-8 labelAsValue">
                                                            <c:if test="${pageContext.response.locale eq 'en'}">${applicant.bank.nameInEnglish}</c:if>
                                                            <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.bank.nameInBangla}</c:if>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.branch" /></label>
                                                        <div class="col-md-8 labelAsValue">
                                                            <c:if test="${pageContext.response.locale eq 'en'}">${applicant.branch.nameInEnglish}</c:if>
                                                            <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.branch.nameInBangla}</c:if>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.accountType" /></label>
                                                        <div class="col-md-8 labelAsValue">
                                                            <c:if test="${pageContext.response.locale eq 'en'}">${applicant.accountType.nameInEnglish}</c:if>
                                                            <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.accountType.nameInBangla}</c:if>
                                                            </div>
                                                        </div>
                                                </c:if>
                                                <c:if test="${applicant.paymentType eq 'MOBILEBANKING'}">
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="mobileBankingProvider.label.mobileBankingProvider" /></label>
                                                        <div class="col-md-8 labelAsValue">
                                                            <c:if test="${pageContext.response.locale eq 'en'}">${applicant.mobileBankingProvider.nameInEnglish}</c:if>
                                                            <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.mobileBankingProvider.nameInBangla}</c:if>
                                                            </div>
                                                        </div>
                                                </c:if>
                                                <c:if test="${applicant.paymentType eq 'POSTOFFICE'}">
                                                    <div class="form-group">
                                                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.branch" /></label>
                                                        <div class="col-md-8 labelAsValue">
                                                            <c:if test="${pageContext.response.locale eq 'en'}">${applicant.postOfficeBranch.nameInEnglish}</c:if>
                                                            <c:if test="${pageContext.response.locale eq 'bn'}">${applicant.postOfficeBranch.nameInBangla}</c:if>
                                                            </div>
                                                        </div>
                                                </c:if>         

                                            </div>
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.accountName" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        ${applicant.accountName}
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.accountNo" /></label>
                                                    <div class="col-md-8 labelAsValue">
                                                        <span id="accountNo">${accountNo}</span>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                        <div id="ancAttachmentInfo" style="clear:both">
                                            <legend><spring:message code='label.ancCard' var="attachmentTab"/>${attachmentTab}</legend>
                                            <c:forEach begin="0" end="0" items="${applicant.applicantAttachmentList}" var="attachment" varStatus="index">                        
                                                <c:if test="${attachment !='' && attachment != null}">
                                                    <div iclass="form-group">
                                                        <div class="col-md-6">
                                                            <label for="nidInput" class="col-md-4">${attachment.attachmentName}</label>
                                                            <div class="col-md-7">
                                                                <a id="attachment${index.index}" href="${pageContext.request.contextPath}/getFile/${applicant.nid}/${attachment.fileName}/anc" target="_blank" download="${attachment.fileName}" title="Click to open">${attachment.fileName}</a>                                        
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                        <div id="attachmentInfo" style="clear:both">
                                            <legend><spring:message code='label.AttachmentTab' var="attachmentTab"/>${attachmentTab}</legend>
                                            <c:forEach begin="1" items="${applicant.applicantAttachmentList}" var="attachment" varStatus="index">                     
                                                <c:if test="${attachment !='' && attachment != null}">
                                                    <div iclass="form-group">
                                                        <div class="col-md-6">
                                                            <label for="nidInput" class="col-md-1">${index.index}.</label>
                                                            <label for="nidInput" class="col-md-4">${attachment.attachmentName}</label>
                                                            <div class="col-md-7">
                                                                <a id="attachment${index.index}" href="${pageContext.request.contextPath}/getFile/${applicant.nid}/${attachment.fileName}/others" target="_blank" download="${attachment.fileName}" title="Click to open">${attachment.fileName}</a>                                        
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </form:form> 
                                </div> 
                            </div>
                        </section>

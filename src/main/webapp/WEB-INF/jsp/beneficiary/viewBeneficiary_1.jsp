<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">



<script type="text/javascript">
    $(function () {

        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        console.log('start');
        if (selectedLocale == 'en')
        {
            $("#applicantNid").text(${beneficiary.nid});
            if ('${beneficiary.mobileNo}' != "")
            {
                $("#mobileNo").text('0'+'${beneficiary.mobileNo}');
            }
            $("#dateOfBirth").text($("#dateOfBirth").text());
            $("#nrb").text('${beneficiary.nrb}' == 'true' ? 'yes' : 'no');
            $("#presentWardNo").text(${beneficiary.presentWardNo});
            $("#presentPostCode").text(${beneficiary.presentPostcode});
            $("#permanentWardNo").text(${beneficiary.permanentWardNo});
            $("#permanentPostCode").text(${beneficiary.permanentPostcode});
            $("#accountNo").text(${beneficiary.accountNo});
        } else
        {
            console.log('nid = ' + '${beneficiary.nid}');
            $("#applicantNid").text(getNumberInBangla('${beneficiary.nid}'));
            if ('${beneficiary.mobileNo}' != "")
            {
                $("#mobileNo").text("০" + getNumberInBangla('${beneficiary.mobileNo}'));
            }
            //  $("#mobileNo").text("০" + getNumberInBangla('${beneficiary.mobileNo}'));
            $("#dateOfBirth").text(getNumberInBangla('' + $("#dateOfBirth").text() + ''));
            $("#nrb").text('${beneficiary.nrb}' == 'true' ? 'হ্যাঁ' : 'না');
            $("#presentWardNo").text(getNumberInBangla('${beneficiary.presentWardNo}'));
            $("#presentPostCode").text(getNumberInBangla('${beneficiary.presentPostcode}'));
            $("#permanentWardNo").text(getNumberInBangla('${beneficiary.permanentWardNo}'));
            console.log('${beneficiary.permanentPostcode}');
            $("#permanentPostCode").text(getNumberInBangla('${beneficiary.permanentPostcode}'));
            $("#accountNo").text(getNumberInBangla('${beneficiary.accountNo}'));
        }
    });


</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div class="row">
    <div class="col-md-12">
        <form:form class="form-horizontal" role="form" modelAttribute="beneficiary">
            <!--<h3 style="text-align: center;"><spring:message code="applicantList.applicantId" /> ${beneficiary.applicationID}</h3>--> 
            <div id="personalInfo">
                <legend><spring:message code='label.BasicInfoTab' var="basicInfoTab"/>${basicInfoTab}</legend>                    
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nameBn" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.fullNameInBangla}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.fatherName" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.fatherName}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nickName" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.nickName}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.dob" /></label>
                        <div class="col-md-8 labelAsValue">
                            <span id="dateOfBirth"><fmt:formatDate pattern="dd-MM-yyyy" value="${beneficiary.dateOfBirth.time}" /></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.educationLevel" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.educationLevel.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.educationLevel.nameInBangla}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.bloodGroup" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.bloodGroup.displayName}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.bloodGroup.displayNameBn}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.mobileNo" /></label>
                        <div class="col-md-8 labelAsValue">
                            <span id="mobileNo"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nrb" /></label>
                        <div class="col-md-8 labelAsValue">
                            <span id="nrb"></span>
                        </div>
                    </div>
                </div>
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nameEn" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.fullNameInEnglish}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.motherName" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.motherName}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.spouseName" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.spouseName}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.birthPlace" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.birthPlace.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.birthPlace.nameInBangla}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.religion" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.religion.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.religion.nameInBangla}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.maritalInfo" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.maritalInfo.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.maritalInfo.nameInBangla}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nid" /></label>
                        <div class="col-md-8 labelAsValue">                            
                            <span id="applicantNid" style="font-weight: bold; color: blue; font-size: 16px"></span> 
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.email" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.email}
                        </div>
                    </div>

                </div>
                <div class="col-md-2" style="text-align: center">
                    <div class=""><label><spring:message code='label.BiometricInfoTab' /></label></div>
                            <spring:message code='photoNotFound' var='photoNotFound'/>
                            <spring:message code='signatureNotFound' var='signatureNotFound'/>
                    <img id="profilePhotoFile" style="width:100%" alt="${photoNotFound}" src="data:image/jpeg;base64,${beneficiary.beneficiaryBiometricInfo.base64PhotoData}"/><br>
                    <img id="signatureFile" style="width:100%" alt="${signatureNotFound}" src="data:image/jpeg;base64,${beneficiary.beneficiaryBiometricInfo.base64SignatureData}"/>
                </div>
            </div>
            <div id="addressInfo" style="clear: both">
                <legend><spring:message code='label.AddressInfoTab' var="addressInfoTab"/>${addressInfoTab}</legend>
                <div class="col-md-5">
                    <legend><h4><spring:message code='label.presentAddress'/></h4></legend>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine1" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.presentAddressLine1}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine2" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.presentAddressLine2}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.presentDivision.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.presentDivision.nameInBangla}</c:if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.presentDistrict.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.presentDistrict.nameInBangla}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.upazila" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.presentUpazila.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.presentUpazila.nameInBangla}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.union" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.presentUnion.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.presentUnion.nameInBangla}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.wardNo" /></label>
                        <div class="col-md-8 labelAsValue">
                            <span id="presentWardNo"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.postCode" /></label>
                        <div class="col-md-8 labelAsValue">
                            <span id="presentPostCode"></span>                            
                        </div>
                    </div>
                </div>
                <div class="col-md-5">
                    <legend><h4><spring:message code='label.permanentAddress'/></h4></legend>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine1" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.permanentAddressLine1}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine2" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.permanentAddressLine2}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.permanentDivision.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.permanentDivision.nameInBangla}</c:if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.permanentDistrict.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.permanentDistrict.nameInBangla}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.upazila" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.permanentUpazila.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.permanentUpazila.nameInBangla}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.union" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.permanentUnion.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.permanentUnion.nameInBangla}</c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.wardNo" /></label>
                        <div class="col-md-8 labelAsValue">
                            <span id="permanentWardNo"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.postCode" /></label>
                        <div class="col-md-8 labelAsValue">
                            <span id="permanentPostCode"></span>
                        </div>
                    </div>
                </div>
                <div class="col-md-2">

                </div>
            </div>
            <div id="socioEconomicInfo" style="clear:both">                        
                <legend><spring:message code='label.socioEconomicInfoTab' var="socioEconomicInfoTab"/>${socioEconomicInfoTab}</legend>                    
                <div class="col-md-5">
                    <c:forEach items="${beneficiary.schemeAttributeValueList}" var="attribute" varStatus="index">
                        <c:set var="total" value="${fn:length(beneficiary.schemeAttributeValueList)}" />
                        <c:if test="${index.index < total/2}">
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label">
                                    <c:if test="${pageContext.response.locale eq 'en'}">${attribute.schemeAttribute.nameInEnglish}</c:if>
                                    <c:if test="${pageContext.response.locale eq 'bn'}">${attribute.schemeAttribute.nameInBangla}</c:if>
                                    </label>
                                    <div class="col-md-8 labelAsValue">
                                    <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.schemeAttributeValueList[index.index].schemeAttributeValueInEnglish}</c:if>
                                    <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.schemeAttributeValueList[index.index].schemeAttributeValueInBangla}</c:if>
                                    </div>
                                </div>
                        </c:if>
                    </c:forEach>
                </div>
                <div class="col-md-5">
                    <c:forEach items="${beneficiary.schemeAttributeValueList}" var="attribute" varStatus="index">
                        <c:set var="total" value="${fn:length(beneficiary.schemeAttributeValueList)}" />
                        <c:if test="${index.index >= total/2}">
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label">
                                    <c:if test="${pageContext.response.locale eq 'en'}">${attribute.schemeAttribute.nameInEnglish}</c:if>
                                    <c:if test="${pageContext.response.locale eq 'bn'}">${attribute.schemeAttribute.nameInBangla}</c:if>
                                    </label>
                                    <div class="col-md-8 labelAsValue">
                                    <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.schemeAttributeValueList[index.index].schemeAttributeValueInEnglish}</c:if>
                                    <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.schemeAttributeValueList[index.index].schemeAttributeValueInBangla}</c:if>
                                    </div>
                                </div>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
            <div id="bankAccountInfo" style="clear:both">
                <legend><spring:message code='label.BankAccountInfoTab' var="bankAccountInfoTab"/>${bankAccountInfoTab}</legend>
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="paymentType.label.paymentType" /></label>
                        <div class="col-md-8 labelAsValue">
                            <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.paymentType.nameInEnglish}</c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.paymentType.nameInBangla}</c:if>
                            </div>
                        </div>
                    <c:if test="${beneficiary.paymentType.id eq 1}">
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.bank" /></label>
                            <div class="col-md-8 labelAsValue">
                                <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.bank.nameInEnglish}</c:if>
                                <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.bank.nameInBangla}</c:if>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.branch" /></label>
                            <div class="col-md-8 labelAsValue">
                                <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.branch.nameInEnglish}</c:if>
                                <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.branch.nameInBangla}</c:if>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.accountType" /></label>
                            <div class="col-md-8 labelAsValue">
                                <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.accountType.nameInEnglish}</c:if>
                                <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.accountType.nameInBangla}</c:if>
                                </div>
                            </div>
                    </c:if>
                    <c:if test="${beneficiary.paymentType.id eq 2}">
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="mobileBankingProvider.label.mobileBankingProvider" /></label>
                            <div class="col-md-8 labelAsValue">
                                <c:if test="${pageContext.response.locale eq 'en'}">${beneficiary.mobileBankingProvider.nameInEnglish}</c:if>
                                <c:if test="${pageContext.response.locale eq 'bn'}">${beneficiary.mobileBankingProvider.nameInBangla}</c:if>
                                </div>
                            </div>
                    </c:if>

                </div>
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.accountName" /></label>
                        <div class="col-md-8 labelAsValue">
                            ${beneficiary.accountName}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.accountNo" /></label>
                        <div class="col-md-8 labelAsValue">
                            <span id="accountNo"></span>
                        </div>
                    </div>
                </div>

            </div>
            <div id="attachmentInfo" style="clear:both">
                <legend><spring:message code='label.AttachmentTab' var="attachmentTab"/>${attachmentTab}</legend>
                <c:forEach items="${beneficiary.beneficiaryAttachmentList}" var="attachment" varStatus="index">
                    <c:if test="${attachment !='' && attachment != null}">
                        <div iclass="form-group">
                            <div class="col-md-6">
                                <label for="nidInput" class="col-md-1">${index.index+1}.</label>
                                <label for="nidInput" class="col-md-4">${attachment.attachmentName}</label>
                                <div class="col-md-7">
                                    <a id="attachment${index.index}" href="${contextPath}/${attachmentPath}/${attachment.fileName}" target="_blank" title="Click to open">${attachment.fileName}</a>                                        
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>  
    </form:form>
</div>

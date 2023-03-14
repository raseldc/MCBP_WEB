<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">

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

</style>

<script type="text/javascript">
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        if ("${becResponse.matchFound == true}") {
            if (selectedLocale == 'en')
            {
                $("#beneficiaryNid").text('${beneficiary.nid}');
                $("#becNid").text('${becResponse.nidData.nid}');
                $("#dateOfBirth").text($("#dateOfBirth").text());
                $("#becDateOfBirth").text($("#becDateOfBirth").text());
            } else
            {
                $("#beneficiaryNid").text(getNumberInBangla('${beneficiary.nid}'));
                $("#becNid").text(getNumberInBangla('${becResponse.nidData.nid}'));
                $("#dateOfBirth").text(getNumberInBangla($("#dateOfBirth").text()));
                $("#becDateOfBirth").text(getNumberInBangla($("#becDateOfBirth").text()));
            }
        }
    });


</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="actionUrl" value="${contextPath}/NIDVerification" />
<div class="box">
    <div class="box-header" style="border-top:1px solid lightgray; border-bottom:1px solid lightgray">
        <c:if test="${becResponse.matchFound == true}">
            <div class="col-md-6">
                <b><spring:message code="label.nid" />:&nbsp;</b><span id="beneficiaryNid"><c:out value="${beneficiary.nid}"></c:out></span>
                </div>
                <div class="col-md-6 text-right">
                    <b><spring:message code="label.becResult" />:&nbsp;</b><h4 class="bg-success"> <spring:message code="label.nidMatchFound" /></h4>                
            </div>
        </c:if>

        <c:if test="${becResponse.matchFound == false}">
            <div class="col-md-6">                
                <b><spring:message code="label.nid" />: </b><span id="beneficiaryNid"><c:out value="${beneficiary.nid}"></c:out></span>
                    <br>
                    <br>
                    <b><spring:message code="label.becResult" />: </b><h4 class="bg-danger"> <spring:message code="label.nidMatchNotFound" /></h4>
            </div>                
        </c:if>
    </div>
    <c:if test="${becResponse.matchFound == true}">
        <div class="box-body">        
            <table id="comparisonTable" class="table table-bordered table-hover">
                <thead style="background-color: #EEE">
                    <tr>
                        <th><spring:message code="label.info" /></th>
                        <th style="width: 40%"><spring:message code="label.beneficiaryInfo" /></th>
                        <th style="width: 40%"><spring:message code="label.becInfo" /></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><spring:message code="label.nameBn" /></td>
                        <td><c:out value="${beneficiary.fullNameInBangla}"></c:out></td>
                        <c:if test="${becResponse.nidData.name == 'null'}">
                            <td><spring:message code="label.notAvailable" /></td>
                        </c:if>
                        <c:if test="${becResponse.nidData.name != 'null'}">           
                            <td><c:out value="${becResponse.nidData.name}"></c:out></td>
                        </c:if>
                    </tr>
                    <tr>
                        <td><spring:message code="label.photo" /></td>
                        <td>
                            <spring:message code='photoNotFound' var='photoNotFound'/>
                            <img id="profilePhotoFile"  style="width:100px; height: 129px;" alt="${photoNotFound}" src="data:image/jpeg;base64,${beneficiary.beneficiaryBiometricInfo.base64PhotoData}"/><br>
                        </td>
                        <td>
                            <img id="profilePhotoFile" style="width:100px; height: 129px;" alt="${photoNotFound}" src="data:image/jpeg;base64,${becResponse.nidData.photo}"/><br>
                        </td>
                    </tr>

                    <tr>
                        <td><spring:message code="label.dob" /></td>
                        <td id="dateOfBirth"><fmt:formatDate pattern = "yyyy-MM-dd" value="${beneficiary.dateOfBirth.time}"/></td>
                        <td id="becDateOfBirth"><c:out value="${becResponse.nidData.dob}"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="label.fatherName" /></td>
                        <td><c:out value="${beneficiary.fatherName}"></c:out></td>
                        <c:if test="${becResponse.nidData.father == 'null'}">
                            <td><spring:message code="label.notAvailable" /></td>
                        </c:if>
                        <c:if test="${becResponse.nidData.father != 'null'}">           
                            <td><c:out value="${becResponse.nidData.father}"></c:out></td>
                        </c:if>
                    </tr>
                    <tr>
                        <td><spring:message code="label.motherName" /></td>
                        <td><c:out value="${beneficiary.motherName}"></c:out></td>
                        <c:if test="${becResponse.nidData.mother == 'null'}">
                            <td><spring:message code="label.notAvailable" /></td>
                        </c:if>
                        <c:if test="${becResponse.nidData.mother != 'null'}">           
                            <td><c:out value="${becResponse.nidData.mother}"></c:out></td>
                        </c:if>
                    </tr>
                    <tr>
                        <td><spring:message code="label.presentAddress" /></td>
                        <td>
                            <c:if test="${pageContext.response.locale eq 'en'}">
                                <c:out value="${beneficiary.presentAddressLine1}, ${beneficiary.presentUnion.nameInEnglish}, ${beneficiary.presentUpazila.nameInEnglish}, ${beneficiary.presentDistrict.nameInEnglish}"></c:out>
                            </c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">
                                <c:out value="${beneficiary.presentAddressLine1}, ${beneficiary.presentUnion.nameInBangla}, ${beneficiary.presentUpazila.nameInBangla}, ${beneficiary.presentDistrict.nameInBangla}"></c:out>
                            </c:if>
                        </td>
                        <c:if test="${becResponse.nidData.presentAddress == 'null'}">
                            <td><spring:message code="label.notAvailable" /></td>
                        </c:if>
                        <c:if test="${becResponse.nidData.presentAddress != 'null'}">           
                            <td><c:out value="${becResponse.nidData.presentAddress}"></c:out></td>
                        </c:if>

                    </tr>
                    <tr>
                        <td><spring:message code="label.permanentAddress" /></td>
                        <td>
                            <c:if test="${pageContext.response.locale eq 'en'}">
                                <c:out value="${beneficiary.permanentAddressLine1}, ${beneficiary.permanentUnion.nameInEnglish}, ${beneficiary.permanentUpazila.nameInEnglish}, ${beneficiary.permanentDistrict.nameInEnglish}"></c:out>
                            </c:if>
                            <c:if test="${pageContext.response.locale eq 'bn'}">
                                <c:out value="${beneficiary.permanentAddressLine1}, ${beneficiary.permanentUnion.nameInBangla}, ${beneficiary.permanentUpazila.nameInBangla}, ${beneficiary.permanentDistrict.nameInBangla}"></c:out>
                            </c:if>
                        </td>
                        <c:if test="${becResponse.nidData.permanentAddress == 'null'}">
                            <td><spring:message code="label.notAvailable" /></td>
                        </c:if>
                        <c:if test="${becResponse.nidData.permanentAddress != 'null'}">           
                            <td><c:out value="${becResponse.nidData.permanentAddress}"></c:out></td>
                        </c:if>
                    </tr>
                </tbody>
            </table>        
        </div>
    </c:if>
</div>


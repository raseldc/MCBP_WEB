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

        console.log("logging in view comparison");
        if ("${becResponse.matchFound == true}") {
            if (selectedLocale == 'en')
            {
                $("#applicantNid").text('${applicant.nid}');
                $("#becNid").text('${becResponse.nidData.nid}');
                $("#dateOfBirth").text($("#dateOfBirth").text());
                $("#becDateOfBirth").text($("#becDateOfBirth").text());
            } else
            {
                $("#applicantNid").text(getNumberInBangla('${applicant.nid}'));
                $("#becNid").text(getNumberInBangla('${becResponse.nidData.nid}'));
                $("#dateOfBirth").text(getNumberInBangla($("#dateOfBirth").text()));
                $("#becDateOfBirth").text(getNumberInBangla($("#becDateOfBirth").text()));
            }
        }
    });


</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="actionUrl" value="${contextPath}/NIDVerification" />
<form:form id="nidCrossCheckModalForm" action="${actionUrl}" method="post" class="form-horizontal" modelAttribute="applicant">
    <input type="hidden" id="applicantId" name="applicantId" value="${applicant.id}">
    <input type="hidden" id="selectedUserType" name="selectedUserType" value="${selectedUserType}">
    <input type="hidden" id="divisionId" name="divisionId" value="${applicant.presentDivision.id}">
    <input type="hidden" id="districtId" name="districtId" value="${applicant.presentDistrict.id}">
    <input type="hidden" id="upazilaId" name="upazilaId" value="${applicant.presentUpazila.id}">
    <input type="hidden" id="unionId" name="unionId" value="${applicant.presentUnion.id}">
    <div class="box">
        <div class="box-header">
            <c:if test="${becResponse.matchFound == true}">
                <div class="alert alert-success col-md-12" style="position: center;">
                    <div class="col-md-6">
                        <label><h4><spring:message code="label.nid" /> : </h4></label>
                        <label id="applicantNid"><h4><c:out value="${applicant.nid}"></c:out></h4></label>
                        </div>
                        <div class="col-md-6">
                            <label><p><spring:message code="label.becResult" /> : <spring:message code="label.nidMatchFound" /></p></label>
                    </div>
                </div>
            </c:if>

            <c:if test="${becResponse.matchFound == false}">
                <div class="alert alert-info col-md-12" style="position: center;">
                    <div class="col-md-6">
                        <label><h4><spring:message code="label.nid" /> : </h4></label>
                        <label id="applicantNid"><h4><c:out value="${applicant.nid}"></c:out></h4></label>
                        </div>
                        <div class="col-md-6">
                            <label><p><spring:message code="label.becResult" /> : <spring:message code="label.nidMatchNotFound" /></p></label>
                    </div>
                </div> 
            </c:if>
        </div>
        <div class="box-body">
            <c:if test="${becResponse.matchFound == true}">
                <table id="comparisonTable" class="table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th><spring:message code="label.info" /></th>
                            <th style="width: 40%"><spring:message code="label.systemInfo" /></th>
                            <th style="width: 40%"><spring:message code="label.becInfo" /></th>
                        </tr>
                    </thead>
                    <tbody>
<!--                        <tr>
                            <td><spring:message code="label.nid" /></td>
                            <td id="applicantNid"><c:out value="${applicant.nid}"></c:out></td>
                            <td id="becNid"><c:out value="${becResponse.nidData.nid}"></c:out></td>
                            </tr>-->
                            <tr>
                                <td><spring:message code="label.nameBn" /></td>
                            <td><c:out value="${applicant.fullNameInBangla}"></c:out></td>
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
                                <img id="profilePhotoFile"  style="width:100px; height: 129px;" alt="${photoNotFound}" src="data:image/jpeg;base64,${applicant.applicantBiometricInfo.base64PhotoData}"/><br>
                            </td>
                            <td>
                                <img id="profilePhotoFile" style="width:100px; height: 129px;" alt="${photoNotFound}" src="data:image/jpeg;base64,${becResponse.nidData.photo}"/><br>
                            </td>
                        </tr>

                        <tr>
                            <td><spring:message code="label.dob" /></td>
                            <td id="dateOfBirth"><fmt:formatDate pattern = "yyyy-MM-dd" value="${applicant.dateOfBirth.time}"/></td>
                            <td id="becDateOfBirth"><c:out value="${becResponse.nidData.dob}"/></td>
                        </tr>
                        <tr>
                            <td><spring:message code="label.fatherName" /></td>
                            <td><c:out value="${applicant.fatherName}"></c:out></td>
                            <c:if test="${becResponse.nidData.father == 'null'}">
                                <td><spring:message code="label.notAvailable" /></td>
                            </c:if>
                            <c:if test="${becResponse.nidData.father != 'null'}">           
                                <td><c:out value="${becResponse.nidData.father}"></c:out></td>
                            </c:if>
                        </tr>
                        <tr>
                            <td><spring:message code="label.motherName" /></td>
                            <td><c:out value="${applicant.motherName}"></c:out></td>
                            <c:if test="${becResponse.nidData.mother == 'null'}">
                                <td><spring:message code="label.notAvailable" /></td>
                            </c:if>
                            <c:if test="${becResponse.nidData.mother != 'null'}">           
                                <td><c:out value="${becResponse.nidData.mother}"></c:out></td>
                            </c:if>
                        </tr>
                        <tr>
                            <td><spring:message code="label.presentAddress" /></td>
                            <td><c:out value="${applicant.presentAddressLine1} ${applicant.presentAddressLine2}"></c:out></td>
                            <c:if test="${becResponse.nidData.presentAddress == 'null'}">
                                <td><spring:message code="label.notAvailable" /></td>
                            </c:if>
                            <c:if test="${becResponse.nidData.presentAddress != 'null'}">           
                                <td><c:out value="${becResponse.nidData.presentAddress}"></c:out></td>
                            </c:if>

                        </tr>
                        <tr>
                            <td><spring:message code="label.permanentAddress" /></td>
                            <td><c:out value="${applicant.permanentAddressLine1} ${applicant.permanentAddressLine2}"></c:out></td>
                            <c:if test="${becResponse.nidData.permanentAddress == 'null'}">
                                <td><spring:message code="label.notAvailable" /></td>
                            </c:if>
                            <c:if test="${becResponse.nidData.permanentAddress != 'null'}">           
                                <td><c:out value="${becResponse.nidData.permanentAddress}"></c:out></td>
                            </c:if>
                        </tr>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>
    <c:if test="${becResponse.matchFound == true}">
        <button type="submit" name="action" value="approve" class="btn btn-default bg-blue"><spring:message code="label.approve"/></button>
    </c:if>
    <button type="submit" name="action" value="reject" class="btn btn-default bg-red"><spring:message code="label.reject"/></button>            
</form:form>


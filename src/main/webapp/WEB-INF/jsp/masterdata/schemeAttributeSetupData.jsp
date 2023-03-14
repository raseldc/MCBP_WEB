<%-- 
    Document   : schemeAttributeSetupData
    Created on : Jan 22, 2017, 3:40:06 PM
    Author     : user
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- Data Tables -->
<script src="<c:url value="/resources/plugins/messageResources/messageResource.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script type="text/javascript">
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/schemeAttributeSetupData/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#pagemodel-delete-confirmation");
    });
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/schemeAttributeSetupData/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/schemeAttributeSetupData/edit/${schemeAttributeSetupData.id}" />
    </c:otherwise>
</c:choose>
<form:form action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="schemeAttributeSetupData">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <label><spring:message code="label.schemeAttributeSetupData" />&nbsp;<spring:message code="label.management" /></label>
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/schemeAttribute/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue" value="save">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>
            <c:if test="${actionType ne 'create'}">
                <span id="page-delete" class="btn bg-red">
                    <i class="fa fa-trash-o"></i>
                    <spring:message code="delete" />
                </span>
            </c:if>
        </div>    
    </section>
    <section class="content">
        <c:if test="${not empty message}">
            <div class="message green">${message}</div>
        </c:if>
        <div class="row">
            <div class="col-md-6">
                <form:hidden path="id" />
                <div>
                    <div class="form-group">
                        <label for="schemeAttributeInput" class="col-md-4 control-label"><spring:message code="label.schemeAttribute" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.schemeAttribute' var="schemeAttributeLabel"/>
                            <form:select class="form-control" path="schemeAttribute.id">
                                <form:option value="" label="${schemeAttributeLabel}"></form:option>
                                <form:options items="${schemeAttributeList}" itemValue="id" itemLabel="${schemeAttributeName}"></form:options>
                            </form:select>
                            <form:errors path="schemeAttribute.id" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="schemeAttributeSetupDataAttributeValue" class="col-md-4 control-label"><spring:message code="label.schemeAttributeSetupDataAttributeValueEn" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.schemeAttributeSetupDataAttributeValueEn' var="schemeAttributeSetupDataAttributeValue"/>
                            <form:input class="form-control" placeholder="${schemeAttributeSetupDataAttributeValue}" path="attributeValueInEnglish" id="schemeAttributeSetupDataAttributeValue" autofocus="autofocus"/>
                            <form:errors path="attributeValueInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </form:form>
    <div id='pagemodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/schemeAttributeSetupData/delete/${schemeAttributeSetupData.id}" method="post">
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





<!-- /.content -->



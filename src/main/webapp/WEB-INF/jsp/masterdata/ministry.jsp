<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/messageResources/messageResource.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script>
    $(function () {
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#pagemodel-delete-confirmation");
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });
    });
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/ministry/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/ministry/edit/${ministry.id}" />
    </c:otherwise>
</c:choose>
<form:form action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="ministry">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.ministry" /> <spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/ministry/list"><spring:message code="label.backToList" /></a></small>
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
                <%--<form:hidden path="createdBy.id" />--%>
                <div>
                    <div class="form-group">
                        <label for="ministryNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.ministryNameInputEnglish" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.ministryNameInputEnglish' var="ministryNameInputEnglish"/>
                            <form:input class="form-control" placeholder="${ministryNameInputEnglish}" path="nameInEnglish" id="ministryNameInputEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ministryNameInputBangla" class="col-md-4 control-label"><spring:message code="label.ministryNameInputBangla" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.ministryNameInputBangla' var="ministryNameInputBangla"/>
                            <form:input class="form-control" placeholder="${ministryNameInputBangla}" path="nameInBangla" id="ministryNameInputBangla" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="ministryCodeInput"/>
                            <form:input class="form-control" placeholder="${ministryCodeInput}" path="code" id="ministryCodeInput" />
                            <form:errors path="code" cssclass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="active" class="col-md-4 control-label"><spring:message code="label.active" /></label>
                        <div>
                            <div class="col-md-5">
                                <div class="checkbox icheck">
                                    <label>
                                        <form:checkbox path="active" id="active" />
                                    </label>
                                </div>                        
                            </div>
                        </div>  
                    </div>
                </div>
            </form:form>
        </div>
    </div>
    <div id='pagemodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/ministry/delete/${ministry.id}" method="post">
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


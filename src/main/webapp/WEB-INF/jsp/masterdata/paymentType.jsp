<%-- 
    Document   : PaymentTypeSetup
    Created on : Jan 17, 2017, 1:24:20 PM
    Author     : rezwan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- DataTables -->
<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/paymentType/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#paymentTypemodel-delete-confirmation");

        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $("#paymentTypeForm").validate({
            rules: {
                "nameInEnglish": {
                    required: true,
                },
                "nameInBangla": {
                    required: true
                },
                "code": {
                    required: true
                },
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/paymentType/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/paymentType/edit/${paymentType.id}" />
    </c:otherwise>
</c:choose>

<form:form id="paymentTypeForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="paymentType">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="paymentType.label.paymentType" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/paymentType/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue">
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
        <div class="row">
            <div class="col-md-6">
                <form:hidden path="id" />
                <div>
                    <div class="form-group">
                        <label for="paymentTypeNameInBangla" class="col-md-4 control-label"><spring:message code="paymentType.label.nameInBangla" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='paymentType.label.nameInBangla' var="nameInBangla"/>
                            <form:input class="form-control" placeholder="${nameInBangla}" path="nameInBangla" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="paymentTypeNameInEnglish" class="col-md-4 control-label"><spring:message code="paymentType.label.nameInEnglish" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='paymentType.label.nameInEnglish' var="nameInEnglish"/>
                            <form:input class="form-control" placeholder="${nameInEnglish}" path="nameInEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="paymentType.label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='paymentType.label.code' var="code"/>
                            <form:input class="form-control" placeholder="${code}" path="code" />
                            <form:errors path="code" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="active" class="col-md-4 control-label"><spring:message code="label.active" /></label>
                        <div class="col-md-3">
                            <form:checkbox class="icheckbox_square-blue" path="active" id="active" />
                        </div>  
                    </div>
                </div>
            </form:form>
        </div>
    </div>

    <div id='paymentTypemodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/paymentType/delete/${paymentType.id}" method="post">
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


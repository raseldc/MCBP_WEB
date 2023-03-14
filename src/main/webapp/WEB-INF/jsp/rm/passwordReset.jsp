<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script type="text/javascript">
    $(function () {
        $.validator.addMethod("pwcheck", function (value) {
            return /^[A-Z]/.test(value) // consists of only these
                    && /[a-z]/.test(value) // has a lowercase letter
                    && /\d/.test(value); // has a digit
        }, "Must Match Password Policy");
        $("#passwordCreateForm").validate({
            rules: {
                "newPassword": {
                    required: true,
                    minlength: 6,
                    pwcheck: true
                },
                "reEnterPassword": {
                    required: true,
                    minlength: 6,
                    equalTo: "#newPassword"
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });
</script>
<style type="text/css">
</style>
<c:url var="formAction" value="/passwordReset/?token=${token}"></c:url>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h3><spring:message code="label.passwordResetHeader"/></h3>
            <hr>
        </div>     
    </div>
    <c:if test="${not empty msg}">
        <div class="row">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <c:out value="${msg}"></c:out>
                </div>
            </div>
            <br>
    </c:if>
    <div class="row">
        <div class="col-md-6">
            <form:form id="passwordCreateForm" method="POST" class="form-horizontal" role="form" action="${formAction}" modelAttribute="forgotPasswordRecoverForm">                                    
                <div class="form-group">
                    <label for="password" class="col-md-4 control-label"><spring:message code="label.newPassword" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.newPassword' var="npassword"/> 
                        <form:input class="form-control" placeholder="${npassword}" path="newPassword" type="password" value=""/>
                        <form:errors path="newPassword" cssStyle="color:red" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="reEnterPasswordInput" class="col-md-4 control-label"><spring:message code="label.confirmPassword" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.confirmPassword' var="cpassword"/>
                        <form:input class="form-control" placeholder="${cpassword}" path="reEnterPassword" type="password" id="reEnterPasswordInput" />
                        <form:errors path="reEnterPassword" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-8 col-sm-offset-4">
                            <button type="submit" class="btn btn-default"><spring:message code='save'/></button>
                    </div>
                </div>    

            </form:form>
        </div>
        <div class="col-md-6">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <span class="glyphicon glyphicon-info-sign"></span> <spring:message code="label.passwordPolicy" />
                    </h3>
                </div>
                <div class="panel-body">
                    <ul class="custom-bullet">
                        <li><spring:message code="label.policyLine1" /></li>
                        <li><spring:message code="label.policyLine2" /></li>
                    </ul>
                </div>
            </div>
        </div>    
    </div>
</div>
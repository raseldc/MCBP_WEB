<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script type="text/javascript">
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/user/list"));
        $.validator.addMethod("pwcheck", function (value) {
            return /^[A-Z]/.test(value) // consists of only these
                    && /[a-z]/.test(value) // has a lowercase letter
                    && /\d/.test(value); // has a digit
        });
        $("#changePasswordForm").validate({
            rules: {
                "password": {
                    required: true,
                    minlength: 6,
                    pwcheck: true                    
                },
                "confirmPassword": {
                    required: true,
                    minlength: 6,
                    equalTo: "#password"
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
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="actionUrl" value="${contextPath}/user/changePassword/${changePasswordByAdminForm.userId}" />
<form:form id="changePasswordForm" action="${actionUrl}" class="form-horizontal" role="form" modelAttribute="changePasswordByAdminForm">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.changePassword" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/user/list"><spring:message code="user.backToUserList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="button.changePassword" />
            </button>            
        </div>        
    </section>
    <section class="content">       
        <div class="row">
            <div class="col-md-6">
                <form:hidden path="userId" />
                <div class="form-group">
                    <label for="userID" class="col-md-4 control-label"><spring:message code="label.userID" /></label>
                    <div class="col-md-8">                        
                        <form:input  class="form-control" path="userID" readonly="true"/>
                        <form:errors path="userID" cssStyle="color:red"></form:errors>
                        </div> 
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-md-4 control-label"><spring:message code="label.newPassword" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.newPassword' var="npassword"/>    
                        <form:password class="form-control" placeholder="${npassword}" path="password" autofocus="autofocus"/>
                        <form:errors path="password" cssStyle="color:red" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="confirmPassword" class="col-md-4 control-label"><spring:message code="label.confirmPassword" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.confirmPassword' var="cpassword"/>
                        <form:password class="form-control" placeholder="${cpassword}" path="confirmPassword" id="confirmPasswordInput" />
                        <form:errors path="confirmPassword" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
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
    </section>
</form:form>
<!-- /.content -->


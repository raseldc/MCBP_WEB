<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script type="text/javascript">
    $(function () {
        $.validator.addMethod("pwcheck", function (value) {
            return /^[A-Z]/.test(value) // consists of only these
                    && /[a-z]/.test(value) // has a lowercase letter
                    && /\d/.test(value); // has a digit
        });
        $.validator.addMethod(
                "currentPasswordMatchCheck",
                function (value, element) {

                    $.ajax({
                        type: "GET",
                        url: "${pageContext.request.contextPath}/checkCurrentPasswordMatch/" + encodeURIComponent(value),
                        async: false,
                        dataType: "html",
                        success: function (msg)
                        {
                            response = (msg === 'true') ? true : false;
                        }
                    });
                    return response;
                });
        $("#changePasswordForm").validate({
            rules: {
                "oldPassword": {
                    required: true,
                    currentPasswordMatchCheck: true
                },
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
        if ("${msg}")
        {
            bootbox.dialog({
                onEscape: function () {},
                title: '<spring:message code="label.success" />',
                message: "<b>${msg}</b>",
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
<style type="text/css">

</style>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:url var="formAction" value="/changePassword"></c:url>
<form:form id="changePasswordForm" method="POST" class="form-horizontal" role="form" action="${formAction}" modelAttribute="changePasswordForm">   
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.changePassword" />&nbsp;<spring:message code="label.management" />
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="button.changePassword" />
            </button>            
        </div>        
    </section>
    <!-- Main content -->
    <section class="content">
        <div class="row">
            <div class="col-md-6">               
                <div class="form-group">
                    <label for="oldPassword" class="col-md-4 control-label"><spring:message code="label.currentPassword"/><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.currentPassword' var="crpassword"/> 
                        <form:password  class="form-control" placeholder="${crpassword}" path="oldPassword" id="oldPasswordInput" autofocus="autofocus"/>
                        <form:errors path="oldPassword" cssStyle="color:red"></form:errors>
                        </div> 
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-md-4 control-label"><spring:message code="label.newPassword"/><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.newPassword' var="npassword"/>    
                        <form:input class="form-control" placeholder="${npassword}" path="password" type="password" value=""/>
                        <form:errors path="password" cssStyle="color:red" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="confirmPassword" class="col-md-4 control-label"><spring:message code="label.confirmPassword"/><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.confirmPassword' var="cpassword"/>
                        <form:input class="form-control" placeholder="${cpassword}" path="confirmPassword" type="password" id="confirmPasswordInput" />
                        <form:errors path="confirmPassword" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                    <!--                    <div class="form-group">
                                            <div class="col-md-4 col-md-offset-4">
                                                <button type="submit" class="btn btn-default">Change Password</button>
                                            </div>
                                        </div>    -->

            </form:form>
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
<!-- /.content -->


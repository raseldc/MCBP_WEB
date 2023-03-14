<%-- 
    Document   : forgotPasswordRequest
    Created on : Aug 21, 2016, 3:03:04 PM
    Author     : Philip
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script type="text/javascript">
</script>
<style type="text/css">

</style>
<script>
    $(function () {
        $("#forgotPasswordRequestForm").validate({
            rules: {
                "email": {
                    required: true
                }
            },
            errorElement: "span",
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });
</script>   
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:url var="formAction" value="/passwordResetRequest"></c:url>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h3><spring:message code="label.passwordResetHeader"/></h3>
            <hr>
        </div>     
    </div>
    <c:if test="${not empty msg}">
        <div class="row">
            <div class="col-lg-12">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <c:out value="${msg}"></c:out>
                    </div>
                </div>
            </div>
            <br>
    </c:if>
    <div class="row">
        <div class="col-lg-12">
            <p><spring:message code="label.passwordResetInfo"/></p>
            <div class="col-md-6">
                <form:form id="forgotPasswordRequestForm" method="POST" class="form-horizontal" role="form" action="${formAction}" modelAttribute="forgotPasswordRequestForm">                    
                    <div class="form-group">
                        <spring:message code="user.email" var="email"/>
                        <form:input  class="form-control" placeholder="${email}" path="email" id="userNameInput" autofocus="autofocus"/>
                        <form:errors path="email" cssStyle="color:red"></form:errors>  
                        </div>
                        <div class="form-group">
                            <!--                        <div class="col-md-8 col-sm-offset-4">-->
                            <button type="submit" class="btn btn-primary"><spring:message code="button.submit"/></button>
                        <!--</div>-->
                    </div>    
                </form:form>
            </div>
        </div>

    </div>
</div>
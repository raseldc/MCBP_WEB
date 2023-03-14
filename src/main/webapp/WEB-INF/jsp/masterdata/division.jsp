<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/division/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#divisionmodel-delete-confirmation");
        makeUnijoyEditor('divisionNameInputBangla');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('divisionCodeInput');
            $('#divisionCodeInput').on("keydown", function (event) {
                checkNumberWithLength(event, this, 2);
            });
            $("#divisionCodeInput").val(getNumberInBangla("${division.code}"));
        }
        $("#divisionNameInputEnglish").focus();
        $("#divisionNameInputBangla").focus();
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        if ("${message.message}")
        {
            bootbox.dialog({
                onEscape: function () {},
                title: '<spring:message code="label.message" />',
                message: "<b>${message.message}</b>",
                buttons: {
                    ok: {
                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                    }
                },

                callback: function (result) {
                }
            });
        }
        $("#divisionForm").validate({
            rules: {// checks NAME not ID
                "nameInEnglish": {
                    required: true
                },
                "nameInBangla": {
                    required: true
                },
                "code": {
                    required: true,
                    minlength: 2
                }
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
        <c:set var="actionUrl" value="${contextPath}/division/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/division/edit/${division.id}" />
    </c:otherwise>
</c:choose>

<form:form id="divisionForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="division">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.division" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/division/list"><spring:message code="label.backToList" /></a></small>
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
                        <label for="divisionNameInputBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="divisionNameInputBangla"/>
                            <form:input class="form-control" placeholder="${divisionNameInputBangla}" path="nameInBangla" id="divisionNameInputBangla" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="divisionNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="divisionNameInputEnglish"/>
                            <form:input class="form-control" placeholder="${divisionNameInputEnglish}" path="nameInEnglish" id="divisionNameInputEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="divisionCodeInput"/>
                            <form:input class="form-control" placeholder="${divisionCodeInput}" path="code" id="divisionCodeInput" onkeydown="checkNumberWithLength(event, this, 2)"/>
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
                <!--                <div class="form-group">
                                    <div class="col-md-4 col-md-offset-4">
                                        <button type="submit" class="btn btn-default">Save</button>
                                    </div>
                                </div>    -->
            </form:form>
        </div>
    </div>

    <div id='divisionmodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/division/delete/${division.id}" method="post">
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


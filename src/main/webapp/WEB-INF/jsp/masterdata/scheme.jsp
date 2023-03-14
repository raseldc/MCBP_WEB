<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- Data Tables -->
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />"></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/bootbox.min.js"/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<script type="text/javascript" src="<c:url value="/resources/js/jquery.ui.datepicker-bn.js"/>" ></script>
<script type="text/javascript">
    $(document).ready(function () {
        $(menuSelect("${pageContext.request.contextPath}/scheme/list"));
        initDate($("#activationDate"), '${dateFormat}', $("#activationDate\\.icon"), selectedLocale,2);
        initDate($("#deactivationDate"), '${dateFormat}', $("#deactivationDate\\.icon"), selectedLocale, 50);
        makeUnijoyEditor('schemeNameInputBangla');
        $("#schemeNameInputEnglish").focus();
        $("#schemeNameInputBangla").focus();
        if (selectedLocale === "bn")
        {
            makeUnijoyEditor('schemeCodeInput');
            makeUnijoyEditor('schemeDefaultMonth');
            makeUnijoyEditor('description');
            $("#schemeCodeInput").val(getNumberInBangla("" + $("#schemeCodeInput").val() + ""));
            $("#schemeDefaultMonth").val(getNumberInBangla("" + $("#schemeDefaultMonth").val() + ""));
            $.datepicker.setDefaults(
                    $.extend(
                            {'dateFormat': 'dd-mm-yy'},
                            $.datepicker.regional['bn-BD']
                            )
                    );
            $("#activationDate").val(getNumberInBangla($("#activationDate").val()));
            $("#deactivationDate").val(getNumberInBangla($("#deactivationDate").val()));
        }
        var schemeCodeInput = document.getElementById("schemeCodeInput");
        schemeCodeInput.addEventListener("keydown", function (event) {
            checkDecimalWithLength(event, this, 4);
        });
        var schemeDefaultMonth = document.getElementById("schemeDefaultMonth");
        schemeDefaultMonth.addEventListener("keydown", function (event) {
            checkDecimalWithLength(event, this, 100);
        });
        $("#schemeForm").validate({

            rules: {// checks NAME not ID
                "nameInEnglish": {
                    required: true,
                    englishAlphabet: true
                },
                "nameInBangla": {
                    required: true,
                    banglaAlphabet: true
                },
                "code": {
                    required: true,
                    minlength: 4,
                    maxlength: 4
                },
                "defaultMonth": {
                    required: true,
//                    max:255
                },
                "activationDate": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });



    });
    $(function () {
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#pagemodel-delete-confirmation");
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });
    });

    function submitForm() {
        var form = $('#schemeForm');
        form.validate();
//        if (!form.valid()) {
//            alert('not ok');
//            return false;
//        }
        if (selectedLocale === "bn")
        {
            $("#schemeDefaultMonth").val(getNumberInEnglish("" + $("#schemeDefaultMonth").val() + ""));
            $("#activationDate").val(getNumberInEnglish($("#activationDate").val()));
            $("#deactivationDate").val(getNumberInEnglish($("#deactivationDate").val()));
        }
        return true;
    }
    function showWarning(event)
    {
        if ($('#activationDate').val() != "" && $('#deactivationDate').val() != "")
        {
            var start = $.datepicker.parseDate("dd-mm-yy", getNumberInEnglish($('#activationDate').val()));
            var end = $.datepicker.parseDate("dd-mm-yy", getNumberInEnglish($('#deactivationDate').val()));
// end - start returns difference in milliseconds 
            var diff = new Date(end - start);
            if (diff < 0)
            {
                if ("${pageContext.response.locale}" == "bn")
                {
                    bootbox.alert("<b>শুরুর তারিখ শেষের তারিখ থেকে পূর্বে হতে হবে!</b>");
                } else
                {
                    bootbox.alert("<b>Start Date should be Earlier than End Date!</b>");
                }
                document.getElementById(event).value = "";
                return;
            }
        }
    }

</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/scheme/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/scheme/edit/${scheme.id}" />
    </c:otherwise>
</c:choose>

<form:form id="schemeForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="scheme">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <label><spring:message code="label.scheme" />&nbsp;<spring:message code="label.management" /></label>
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/scheme/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue" value="save" onclick="return submitForm()">
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
                        <label for="schemeNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="schemeNameInputEnglish"/>
                            <form:input class="form-control" placeholder="${schemeNameInputEnglish}" path="nameInEnglish" id="schemeNameInputEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="schemeNameInputBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="schemeNameInputBangla"/>
                            <form:input class="form-control" placeholder="${schemeNameInputBangla}" path="nameInBangla" id="schemeNameInputBangla" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="schemeCodeInput"/>
                            <form:input class="form-control" placeholder="${schemeCodeInput}" path="code" id="schemeCodeInput" onkeydown="checkNumberWithLength(event, this, 4)"/>
                            <form:errors path="code" cssclass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="schemeDefaultMonth" class="col-md-4 control-label"><spring:message code="label.schemeDefaultMonth" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.schemeDefaultMonth' var="schemeDefaultMonth"/>
                            <form:input class="form-control" placeholder="${schemeDefaultMonth}" path="defaultMonth" id="schemeDefaultMonth" autofocus="autofocus"/>
                            <form:errors path="defaultMonth" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.schemeActivationDate" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.schemeActivationDate' var="activationDate"/>
                            <form:input class="form-control" placeholder="${activationDate}" path="activationDate" onchange="showWarning(this.id)"/>
                            <form:errors path="activationDate" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.schemeDeactivationDate" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.schemeDeactivationDate' var="deactivationDate"/>
                            <form:input class="form-control" placeholder="${deactivationDate}" path="deactivationDate" onchange="showWarning(this.id)"/>
                            <form:errors path="deactivationDate" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-md-4 control-label"><spring:message code="label.description" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.description' var="description"/>
                            <form:textarea class="form-control" placeholder="${description}" path="description" id="description" autofocus="autofocus"/>
                            <form:errors path="description" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="active" class="col-md-4 control-label"><spring:message code="label.active" /></label>
                        <div>
                            <div class="col-md-8">
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
                <form action="${contextPath}/scheme/delete/${scheme.id}" method="post">
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



<%-- 
    Document   : notice
    Created on : Sep 20, 2018, 4:53:04 PM
    Author     : user
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.ui.datepicker-bn.js"/>" ></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<script src="<c:url value="/resources/js/unijoy.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/notice/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#noticemodel-delete-confirmation");
        makeUnijoyEditor('noticeInputBangla');

        initDate($("#noticeDate"), '${dateFormat}', $("#noticeDate\\.icon"), selectedLocale);
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        if (selectedLocale === 'bn') {

            $.datepicker.setDefaults(
                    $.extend(
                            {'dateFormat': 'dd-mm-yy'},
                            $.datepicker.regional['bn-BD']
                            )
                    );

            $("#noticeDate").val(getNumberInBangla($("#noticeDate").val()));
        }
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
        $("#noticeForm").validate({
            rules: {// checks NAME not ID
                "noticeBn": {
                    required: true
                },
                "noticeEn": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });

    function submitForm() {
        var form = $('#noticeForm');
        form.validate();
        if (!form.valid()) {
            return false;
        }

        if (selectedLocale === "bn")
        {
            $("#noticeDate").val(getNumberInEnglish($("#noticeDate").val()));
        }
    }

</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/notice/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/notice/edit/${notice.id}" />
    </c:otherwise>
</c:choose>

<form:form id="noticeForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="notice">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.notice" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/notice/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue" onclick="return submitForm()">
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
                        <label for="noticeInputBangla" class="col-md-4 control-label"><spring:message code="label.noticeBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.noticeBn' var="noticeInputBangla"/>
                            <form:input class="form-control" placeholder="${noticeInputBangla}" path="noticeBn" id="noticeInputBangla" autofocus="autofocus"/>
                            <form:errors path="noticeBn" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="noticeInputEnglish" class="col-md-4 control-label"><spring:message code="label.noticeEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.noticeEn' var="noticeInputEnglish"/>
                            <form:input class="form-control" placeholder="${noticeInputEnglish}" path="noticeEn" id="noticeInputEnglish" autofocus="autofocus"/>
                            <form:errors path="noticeEn" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="noticeDescription" class="col-md-4 control-label"><spring:message code="label.noticeDes" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.description' var="noticeDescription"/>
                            <form:textarea rows="10" class="form-control" placeholder="${noticeDescription}" path="description" id="noticeDescription" autofocus="autofocus"/>
                            <form:errors path="description" cssStyle="color:red"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group">
                            <label for="date" class="col-md-4 control-label"><spring:message code="label.date" /><span class="mandatory">*</span></label>
                        <div class="col-md-7">
                            <spring:message code='label.date' var="date"/>
                            <form:input class="form-control" placeholder="${date}" path="noticeDate" autofocus="autofocus" readonly="true"/>
                            <form:errors path="noticeDate" cssStyle="color:red"></form:errors>
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

    <div id='noticemodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/notice/delete/${notice.id}" method="post">
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



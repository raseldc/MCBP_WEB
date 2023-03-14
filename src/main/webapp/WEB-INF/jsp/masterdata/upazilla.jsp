<%-- 
    Document   : upazilla
    Created on : Jan 23, 2017, 3:54:57 PM
    Author     : user
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/upazila/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#upazilamodel-delete-confirmation");
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        makeUnijoyEditor('upazillaNameInputBangla');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('upazillaCodeInput');
            $('#upazillaCodeInput').on("keydown", function (event) {
                checkNumberWithLength(event, this, 2);
            });
            $("#upazillaCodeInput").val(getNumberInBangla("${upazilla.code}"));
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
        $("#upazilaForm").validate({
            rules: {// checks NAME not ID
                "district.id": {
                    required: true
                },
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
        <c:set var="actionUrl" value="${contextPath}/upazila/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/upazila/edit/${upazila.id}" />
    </c:otherwise>
</c:choose>

<form:form id="upazilaForm" action="${formAction}" method="post" class="form-horizontal" role="form" modelAttribute="upazilla">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.upazila" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/upazila/list"><spring:message code="label.backToList" /></a></small>
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
                        <label for="divisionInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <c:choose>
                                <c:when test="${isEdit eq '1'}">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${upazilla.division.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${upazilla.division.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="division.id" id="divisionId" onchange="loadPresentDistrictList(this)">
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options> 
                                    </form:select>
                                    <form:errors path="division.id" cssClass="error"></form:errors>
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                    <div class="form-group">
                        <label for="districtInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <c:choose>
                                <c:when test="${isEdit eq '1'}">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${upazilla.district.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${upazilla.district.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <spring:message code='label.select' var="select"/>
                                     <form:select class="form-control" path="district.id" id="districtId" >
                                        <form:option value="" label="${select}"></form:option>
                                    </form:select>
                                   
                                    <form:errors path="district.id" cssClass="error"></form:errors>
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                    <div class="form-group">
                        <label for="upazillaNameInputBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="upazillaNameInputBangla"/>
                            <form:input class="form-control" placeholder="${upazillaNameInputBangla}" path="nameInBangla" id="upazillaNameInputBangla" />
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="upazillaNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="upazillaNameInputEnglish"/>
                            <form:input class="form-control" placeholder="${upazillaNameInputEnglish}" path="nameInEnglish" id="upazillaNameInputEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="upazillaCodeInput"/>
                            <form:input class="form-control" placeholder="${upazillaCodeInput}" path="code" id="upazillaCodeInput" onkeydown="checkNumberWithLength(event, this, 2)"/>
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
    <div id='upazilamodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/upazila/delete/${upazilla.id}" method="post">
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

<script>
    function loadPresentDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#districtId');
        if (divId !== '') {
            loadDistrict(divId, distSelectId);
        } else {
            resetSelect(distSelectId);
            resetSelect($('#upazilaId'));
            resetSelect($('#unionId'));
        }
    }

</script>


<!-- /.content -->

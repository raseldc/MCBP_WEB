<%-- 
    Document   : union
    Created on : Jan 23, 2017, 5:47:01 PM
    Author     : user
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/union/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#unionmodel-delete-confirmation");
        makeUnijoyEditor('unionNameInputBangla');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('unionCodeInput');
            $('#unionCodeInput').on("keydown", function (event) {
                checkNumberWithLength(event, this, 3);
            });
            $("#unionCodeInput").val(getNumberInBangla("${union.code}"));
        }
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
        $("#unionForm").validate({
            rules: {// checks NAME not ID
                "upazilla.id": {
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
                    minlength: 2,
                    maxlength:3
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
        <c:set var="actionUrl" value="${contextPath}/union/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/union/edit/${union.id}" />
    </c:otherwise>
</c:choose>


<form:form id="unionForm" action="${formAction}" method="post" class="form-horizontal" role="form" modelAttribute="union">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.union" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/union/list"><spring:message code="label.backToList" /></a></small>
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
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <c:choose>
                                <c:when test="${isEdit eq '1'}">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${union.division.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${union.division.nameInEnglish}
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
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <c:choose>
                                <c:when test="${isEdit eq '1'}">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${union.district.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${union.district.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                  <spring:message code='label.select' var="select"/>
                                     <form:select class="form-control" path="district.id" id="districtId" onchange="loadPresentUpazilaList(this)" >
                                        <form:option value="" label="${select}"></form:option>
                                    </form:select>
                                   
                                    <form:errors path="district.id" cssClass="error"></form:errors>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="upazillaInput" class="col-md-4 control-label"><spring:message code="label.upazila" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <c:choose>
                                <c:when test="${isEdit eq '1'}">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${union.upazilla.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${union.upazilla.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="upazilla.id" id="upazilaId" >
                                        <form:option value="" label="${select}"></form:option>
                                    </form:select>

                                </c:otherwise>
                            </c:choose>

                            <form:errors path="upazilla.id" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="unionNameInputBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="unionNameInputBangla"/>
                            <form:input class="form-control" placeholder="${unionNameInputBangla}" path="nameInBangla" id="unionNameInputBangla"/>
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="unionNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="unionNameInputEnglish"/>
                            <form:input class="form-control" placeholder="${unionNameInputEnglish}" path="nameInEnglish" id="unionNameInputEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="unionCodeInput"/>
                            <form:input class="form-control" placeholder="${unionCodeInput}" path="code" id="unionCodeInput" onkeydown="checkNumberWithLength(event, this, 2)" />
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
    <div id='unionmodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/union/delete/${union.id}" method="post">
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
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#upazilaId');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId);
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#unionId'));
        }
    }
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#unionId');

        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId);
        } else {
            resetSelect(unionSelectId);
        }
    }
</script>

<!-- /.content -->
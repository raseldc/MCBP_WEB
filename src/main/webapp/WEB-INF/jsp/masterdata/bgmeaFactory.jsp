<%-- 
    Document   : factory
    Created on : Jan 23, 2018, 11:30:35 AM
    Author     : user
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/factory/bgmea/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#factorymodel-delete-confirmation");
        makeUnijoyEditor('factoryNameInputBangla');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('factoryCodeInput');
            $('#factoryCodeInput').on("keydown", function (event) {
                checkNumberWithLength(event, this, 4);
            });
            $("#factoryCodeInput").val(getNumberInBangla("${factory.regNo}"));
        }
        $("#factoryNameInputEnglish").focus();
        $("#factoryNameInputBangla").focus();
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
        $("#factoryForm").validate({
            rules: {// checks NAME not ID
                "nameInEnglish": {
                    required: true
                },
                "nameInBangla": {
                    required: true
                },
                "regNo": {
                    required: true,
                    minlength: 4,
                    maxlength: 4
                },
                "address": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
        if ('${factory.division}' !== null)
        {
            loadDistrict('${factory.division.id}', $('#districtId'), '${factory.district.id}');
        }
        if ('${factory.district}' !== null)
        {
            loadUpazilla('${factory.district.id}', $('#upazilaId'), '${factory.upazila.id}');
        }
    });
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

</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/factory/bgmea/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/factory/bgmea/edit/${factory.id}" />
    </c:otherwise>
</c:choose>

<form:form id="factoryForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="factory">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.bgmeaFactory" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/factory/bgmea/list"><spring:message code="label.backToList" /></a></small>
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
                <div class="form-group">
                    <label for="factoryNameInputBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.nameBn' var="factoryNameInputBangla"/>
                        <form:input class="form-control" placeholder="${factoryNameInputBangla}" path="nameInBangla" id="factoryNameInputBangla" autofocus="autofocus"/>
                        <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="factoryNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.nameEn' var="factoryNameInputEnglish"/>
                        <form:input class="form-control" placeholder="${factoryNameInputEnglish}" path="nameInEnglish" id="factoryNameInputEnglish" autofocus="autofocus"/>
                        <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="address" class="col-md-4 control-label"><spring:message code="label.address" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.address' var="factoryAddress"/>
                        <form:textarea class="form-control" placeholder="${factoryAddress}" path="address" id="address" autofocus="autofocus"/>
                        <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                        </div>
                    </div>    
                    <div class="form-group">
                        <label for="regNo" class="col-md-4 control-label"><spring:message code="label.regNo" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.regNo' var="factoryCodeInput"/>
                        <form:input class="form-control" placeholder="${factoryCodeInput}" path="regNo" id="factoryCodeInput" onkeydown="checkNumberWithLength(event, this, 4)"/>
                        <form:errors path="regNo" cssclass="error"></form:errors>
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
            <div class="col-md-6">
                <div class="form-group">
                    <label for="districtInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.select' var="select"/>
                        <form:select class="form-control" path="division.id" id="divisionId" onchange="loadPresentDistrictList(this)">
                            <form:option value="" label="${select}"></form:option>
                            <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options>
                        </form:select>
                        <form:errors path="division.id" cssClass="error"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="districtInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.select' var="select"/>
                        <form:select class="form-control" path="district.id" id="districtId" onchange="loadPresentUpazilaList(this)">
                            <form:option value="" label="${select}"></form:option>                            
                        </form:select>
                        <form:errors path="district.id" cssClass="error"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="upazillaInput" class="col-md-4 control-label"><spring:message code="label.upazila" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.select' var="select"/>
                        <form:select class="form-control" path="upazila.id"  id="upazilaId">
                            <form:option value="" label="${select}"></form:option>
                        </form:select>
                        <form:errors path="upazila.id" cssClass="error"></form:errors>
                        </div>
                    </div>
                </div>
        </form:form>
    </div>

    <div id='factorymodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/factory/delete/${factory.id}" method="post">
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



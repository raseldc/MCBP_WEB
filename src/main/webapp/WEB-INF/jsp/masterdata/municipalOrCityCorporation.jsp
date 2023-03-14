<%-- 
    Document   : MunicipalOrCityCorporation
    Created on : Jan 25, 2018, 5:15:03 PM
    Author     : user
--%>
<%-- 
    Document   : municipalOrCityCorporation
    Created on : Jan 22, 2017, 3:40:06 PM
    Author     : user
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/municipalOrCityCorporation/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#municipalOrCityCorporationmodel-delete-confirmation");
        makeUnijoyEditor('municipalOrCityCorporationNameInputBangla');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('municipalOrCityCorporationCodeInput');
            $('#municipalOrCityCorporationCodeInput').on("keydown", function (event) {
                checkNumberWithLength(event, this, 2);
            });
            $("#municipalOrCityCorporationCodeInput").val(getNumberInBangla("${municipalOrCityCorporation.code}"));
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
        $("#municipalOrCityCorporationForm").validate({
            rules: {// checks NAME not ID
                "nameInEnglish": {
                    required: true
                },
                "nameInBangla": {
                    required: true
                },
                "coverageArea": {
                    required: true
                },
                "coverageAreaClass": {
                    required: true
                },
                "upazilla.id": {
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
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#upazilaId');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId);
        } else {
            resetSelect(upazillaSelectId);
        }
    }
    function setCoverageAreaClassList(selectObject) {
        var coverageArea = selectObject.value;
        if (coverageArea == "CITY_CORPORATION") {
            $("#coverageAreaClassId option[value='A_CATEGORY']").attr("disabled", "disabled");
            $("#coverageAreaClassId option[value='B_CATEGORY']").attr("disabled", "disabled");
            $("#coverageAreaClassId option[value='C_CATEGORY']").attr("disabled", "disabled");
            $("#coverageAreaClassId option[value='CITY_CORPORATION']").attr("disabled", false);
            $('#coverageAreaClassId').val("CITY_CORPORATION");
        } else if (coverageArea == "MUNICIPAL") {
            $("#coverageAreaClassId option[value='A_CATEGORY']").attr("disabled", false);
            $("#coverageAreaClassId option[value='B_CATEGORY']").attr("disabled", false);
            $("#coverageAreaClassId option[value='C_CATEGORY']").attr("disabled", false);
            $("#coverageAreaClassId option[value='CITY_CORPORATION']").attr("disabled", "disabled");
            $('#coverageAreaClassId').val("");
        }
    }
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/municipalOrCityCorporation/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/municipalOrCityCorporation/edit/${municipalOrCityCorporation.id}" />
    </c:otherwise>
</c:choose>

<form:form id="municipalOrCityCorporationForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="municipalOrCityCorporation">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.municipalOrCityCorporation" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/municipalOrCityCorporation/list"><spring:message code="label.backToList" /></a></small>
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
                        <label for="districtInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="district.id" autofocus="autofocus" id="districtId" onchange="loadPresentUpazilaList(this)">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${districtList}" itemValue="id" itemLabel="${districtName}"></form:options>
                            </form:select>
                            <form:errors path="district.id" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="upazillaInput" class="col-md-4 control-label">
                            <c:choose>
                                <c:when test="${sessionScope.userDetail.scheme.shortName == 'LMA'}">
                                    <spring:message code="label.districtOrUpazila"/>
                                </c:when>
                                <c:otherwise>
                                    <spring:message code="label.upazila" />
                                </c:otherwise>
                            </c:choose>
                            <span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="upazilla.id" autofocus="autofocus" id="upazilaId">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${upazilaList}" itemValue="id" itemLabel="${upazilaName}"></form:options>
                            </form:select>
                            <form:errors path="upazilla.id" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="coverageAreaInput" class="col-md-4 control-label"><spring:message code="label.coverageArea" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="coverageArea" autofocus="autofocus" onchange="setCoverageAreaClassList(this)">
                                <form:option value="" label="${select}"></form:option>
                                <c:forEach items="${coverageAreaList}" var="coverageArea">
                                    <c:if test="${pageContext.response.locale=='en'}">   
                                        <form:option value="${coverageArea}" label="${coverageArea.displayName}">${coverageArea.displayName}</form:option>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='bn'}">   
                                        <form:option value="${coverageArea}" label="${coverageArea.displayNameBn}">${coverageArea.displayNameBn}</form:option>
                                    </c:if>
                                </c:forEach>
                            </form:select>
                            <form:errors path="coverageArea" cssClass="error"></form:errors>
                            </div>
                        </div>   
                        <div class="form-group">
                            <label for="coverageAreaClassInput" class="col-md-4 control-label"><spring:message code="label.coverageAreaClass" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="coverageAreaClass" autofocus="autofocus" id="coverageAreaClassId">
                                <form:option value="" label="${select}"></form:option>
                                <c:forEach items="${coverageAreaClassList}" var="coverageAreaClass">
                                    <c:if test="${pageContext.response.locale=='en'}">   
                                        <form:option value="${coverageAreaClass}" label="${coverageAreaClass.displayName}">${coverageAreaClass.displayName}</form:option>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='bn'}">   
                                        <form:option value="${coverageAreaClass}" label="${coverageAreaClass.displayNameBn}">${coverageAreaClass.displayNameBn}</form:option>
                                    </c:if>
                                </c:forEach>
                            </form:select>
                            <form:errors path="coverageAreaClass" cssClass="error"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group">
                            <label for="municipalOrCityCorporationNameInputBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="municipalOrCityCorporationNameInputBangla"/>
                            <form:input class="form-control" placeholder="${municipalOrCityCorporationNameInputBangla}" path="nameInBangla" id="municipalOrCityCorporationNameInputBangla" />
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="municipalOrCityCorporationNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="municipalOrCityCorporationNameInputEnglish"/>
                            <form:input class="form-control" placeholder="${municipalOrCityCorporationNameInputEnglish}" path="nameInEnglish" id="municipalOrCityCorporationNameInputEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="municipalOrCityCorporationCodeInput"/>
                            <form:input class="form-control" placeholder="${municipalOrCityCorporationCodeInput}" path="code" id="municipalOrCityCorporationCodeInput" onkeydown="checkNumberWithLength(event, this, 2)"/>
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
    <div id='municipalOrCityCorporationmodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/municipalOrCityCorporation/delete/${municipalOrCityCorporation.id}" method="post">
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



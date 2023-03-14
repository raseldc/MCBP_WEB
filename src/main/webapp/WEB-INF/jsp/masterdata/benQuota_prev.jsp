<%-- 
    Document   : accountType
    Created on : Jan 26, 2017, 4:44:04 PM
    Author     : user
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(menuSelect("${pageContext.request.contextPath}/beneficiaryQuota/search"));
    $(function () {
        $("#quotaForm").validate({
            rules: {
                "scheme.id": {
                    required: true
                },
                "fiscalYear.id": {
                    required: true
                },
                "division.id": {
                    required: true
                },
                "district.id": {
                    required: true
                },
                "upazila.id": {
                    required: true
                }

            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
        $("#benQuotaSaveForm").validate({
        });
        if ("${message.message}")
        {
            bootbox.dialog({
                onEscape: function () {},
                title: '<spring:message code="label.success" />',
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
        if (selectedLocale === 'bn') {
            $("span[id^='quotaIndex']").each(function () {
                $(this).text(getNumberInBangla($(this).text()));
            });
            $('.quota').each(function (i, obj) {                
                $(this).val(getNumberInBangla("" + $(this).val() + ""));
            });
            
            $('.quota').each(function (i, obj) {                
                makeUnijoyEditor(this.id);
            });
            
            $('.quota').on("keydown", function (event) {
                checkNumberWithLength(event, this, 3);
            });            
        }
    });

    $(window).load(function () {
        loadDivision($("select#presentDivision"), '${benQuotaForm.division.id}');
        loadPresentDistrictList($('#presentDivision')[0]);
        loadPresentUpazilaList($('#presentDistrict')[0]);
        loadPresentUnionList($('#presentUpazila')[0]);
    });
    function loadPresentDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#presentDistrict');
        if (divId !== '') {
            loadDistrict(divId, distSelectId, '${user.district.id}');
        } else {
            resetSelect(distSelectId);
            resetSelect($('#presentUpazila'));
            resetSelect($('#presentUnion'));
        }
    }
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#presentUpazila');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId, '${user.upazilla.id}');
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#presentUnion'));
        }
    }
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#presentUnion');

        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId, '${user.union.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'search'}">
        <c:set var="actionUrl" value="${contextPath}/beneficiaryQuota/search" />
    </c:when>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/beneficiaryQuota/save" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/accountType/edit/${accountType.id}" />
    </c:otherwise>
</c:choose>
<%--<c:if test="${not empty message.message}">                
    <div class="alert 
         <c:if test="${message.messageType eq 'SUCCESS'}">alert-success</c:if> 
         <c:if test="${message.messageType eq 'INFO'}">alert-info</c:if> 
             alert-dismissable">
             <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
         <c:out value="${message.message}"></c:out>
         </div>
</c:if>--%>
<c:choose>
    <c:when test="${actionType eq 'search'}">
        <form:form id="quotaForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="beneficiaryQuotaForm">

            <section class="content-header clearfix">
                <h1 class="pull-left">
                    <spring:message code="benQuota.header" />           
                </h1>                
            </section>
            <section class="content">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="paymentCycle.label.scheme" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="scheme.id"  id="scheme"> 
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${schemeList}" itemValue="id" itemLabel="${schemeName}"></form:options> 
                                </form:select> 
                                <form:errors path="scheme.id" cssClass="error"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.fiscalYear" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="fiscalYear.id"  id="fiscalYear"> 
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options> 
                                </form:select> 
                                <form:errors path="fiscalYear.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="divisionInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="division.id"  id="presentDivision" onchange="loadPresentDistrictList(this)"> 
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options> 
                                </form:select> 
                                <form:errors path="division.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="district.id" id="presentDistrict" onchange="loadPresentUpazilaList(this)">
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${districtList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                </form:select>
                                <form:errors path="district.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.upazila" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="upazila.id" id="presentUpazila" onchange="loadPresentUnionList(this)">
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${upazilaList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                </form:select>
                                <form:errors path="upazila.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>  
                            <div class="form-group">
                                <label class="col-md-4"></label>
                                <div class="col-md-8">
                                    <button type="submit" name="save" class="btn bg-blue">
                                        <i class="icon-search"></i>
                                    <spring:message code="benQuota.loadUnions" />
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </form:form>
    </c:when>
    <c:otherwise>
        <form:form id="benQuotaSaveForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="beneficiaryQuotaForm">
            <form:hidden path="scheme.id"/>
            <form:hidden path="fiscalYear.id"/>
            <form:hidden path="upazila.id"/>    
            <section class="content-header clearfix">
                <h1 class="pull-left">
                    <spring:message code="benQuota.header" /> 
                    <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/beneficiaryQuota/search"><spring:message code="benQuota.loadAgain" />&nbsp;</a></small>
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
                    <div class="col-md-6 col-md-offset-2">
                        <table class="table" style="background-color: lightblue">
                            <tr>
                                <th><spring:message code="label.scheme" /></th>
                                <td>
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            <c:out value="${beneficiaryQuotaForm.scheme.nameInBangla}"></c:out>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${beneficiaryQuotaForm.scheme.nameInEnglish}"></c:out>
                                        </c:otherwise>
                                    </c:choose>                                    
                                </td>
                            </tr>
                            <tr>
                                <th><spring:message code="fiscalYear.label.fiscalYear" /></th>
                                <td>
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            <c:out value="${beneficiaryQuotaForm.fiscalYear.nameInBangla}"></c:out>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${beneficiaryQuotaForm.fiscalYear.nameInEnglish}"></c:out>
                                        </c:otherwise>
                                    </c:choose>                                    
                                </td>
                            </tr>
                            <tr>
                                <th><spring:message code="label.upazila" /></th>
                                <td>
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            <c:out value="${beneficiaryQuotaForm.upazila.nameInBangla}"></c:out>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${beneficiaryQuotaForm.upazila.nameInEnglish}"></c:out>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </table>
                        <table class="table table-hover table-bordered">
                            <tr>
                                <th>#</th>
                                <th><spring:message code="label.union" /></th>
                                <th class="text-right"><spring:message code="benQuota.quota" /></th>                    
                            </tr>
                            <c:forEach items="${beneficiaryQuotaForm.benQuotaList}" var="benQuota" varStatus="status">
                                <tr>
                                    <td align="center" style="vertical-align: middle"><span id="quotaIndex${status.count}">${status.count}</span></td>
                                    <td style="vertical-align: middle">
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                <span>${benQuota.union.nameInBangla}</span>
<!--                                                <input class="form-control" name="benQuotaList[${status.index}].union.nameInEnglish" value="${benQuota.union.nameInBangla}" readonly="readonly"/>-->
                                            </c:when>
                                            <c:otherwise>
<!--                                                <input class="form-control" name="benQuotaList[${status.index}].union.nameInEnglish" value="${benQuota.union.nameInEnglish}" readonly="readonly"/>-->
                                                <span>${benQuota.union.nameInEnglish}</span>
                                            </c:otherwise>
                                        </c:choose>

                                        <input type="hidden" name=benQuotaList[${status.index}].union.id value="${benQuota.union.id}">
                                    </td>                            
                                    <td><input class="form-control text-right required quota" onkeydown="checkNumberWithLength(event, this, 3)" id="benQuotaList[${status.index}].quota" name="benQuotaList[${status.index}].quota" value="${benQuota.quota}"/></td>                          
                                </tr>
                            </c:forEach>
                        </table>	
                    </div>
                </div>
            </section>

        </form:form>
    </c:otherwise>
</c:choose>    






<!-- /.content -->


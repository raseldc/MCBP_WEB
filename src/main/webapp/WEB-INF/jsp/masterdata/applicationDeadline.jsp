<%-- 
    Document   : accountType
    Created on : Jan 26, 2017, 4:44:04 PM
    Author     : user
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />"></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.ui.datepicker-bn.js"/>" ></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<script>
    $(menuSelect("${pageContext.request.contextPath}/applicationDeadline/search"));
    // initDate($(".deadlineDate"), '${dateFormat}', $(".deadlineDate\\.icon"));
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
                }

            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
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
        $('.deadline').each(function () {
            $(this).datepicker({
                closeOnDateSelect: true,
                todayButton: true,
                dateFormat: 'dd-mm-yy',
                changeMonth: true,
                changeYear: true,
                yearRange: "-100:+0",
                autoclose: true,
                beforeShow: function (input, inst) {
                    if (selectedLocale === 'bn') {
                        $(this).val(getNumberInEnglish($(this).val()));
                    }
                },
                onClose: function (dateText, datePickerInstance) {
                    if (selectedLocale === 'bn') {
                        $(this).val(getNumberInBangla($(this).val()));
                    }
                }
            }).val();
        });
        if (selectedLocale === 'bn') {
            $.datepicker.setDefaults(
                    $.extend(
                            {'dateFormat': 'dd-mm-yy'},
                            $.datepicker.regional['bn-BD']
                            )
                    );

            $("span[id^='deadlineIndex']").each(function () {
                $(this).text(getNumberInBangla($(this).text()));
            });
            $('.deadline').each(function (i, obj) {
                $(this).val(getNumberInBangla("" + $(this).val() + ""));
            });
        }
        $("#deadlineSaveForm").validate();
    });

    $(window).load(function () {
        loadDivision($("select#presentDivision"), '${applicationDeadlineForm.division.id}');
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
    function submitForm() {
        var form = $('#deadlineSaveForm');
        form.validate();
        if (!form.valid()) {
            return false;
        }
        if (selectedLocale === "bn")
        {
            $('.deadline').each(function (i, obj) {
                $(this).val(getNumberInEnglish("" + $(this).val() + ""));
            });
        }
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'search'}">
        <c:set var="actionUrl" value="${contextPath}/applicationDeadline/search" />
    </c:when>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/applicationDeadline/save" />
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
        <form:form id="quotaForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="applicationDeadlineForm">

            <section class="content-header clearfix">
                <h1 class="pull-left">
                    <spring:message code="applicationDeadline.header" />           
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
                                <label for="divisionInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
                            <c:choose>
                                <c:when test="${sessionScope.user.division == null}">
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="division.id"  id="presentDivision" onchange="loadPresentDistrictList(this)"> 
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options> 
                                        </form:select> 
                                        <form:errors path="division.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-8 labelAsValue">
                                        <form:hidden id="divisionId" path="division.id"/>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                ${applicationDeadlineForm.division.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${applicationDeadlineForm.division.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <label for="presentDistrict" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                            <c:choose>
                                <c:when test="${sessionScope.user.district == null}">
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="district.id" id="presentDistrict" onchange="loadPresentUpazilaList(this)">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${districtList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                        </form:select>
                                        <form:errors path="district.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-8 labelAsValue">
                                        <form:hidden id="districtId" path="district.id"/>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                ${applicationDeadlineForm.district.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${applicationDeadlineForm.district.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <label for="presentUpazila" class="col-md-4 control-label"><spring:message code="label.upazila" /></label>
                            <c:choose>
                                <c:when test="${sessionScope.user.upazilla == null}">
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="upazilla.id" id="presentUpazila" onchange="loadPresentUnionList(this)">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${upazilaList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                        </form:select>
                                        <form:errors path="upazilla.id" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-8 labelAsValue">
                                        <form:hidden id="upazilaId" path="upazilla.id"/>
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                ${applicationDeadlineForm.upazilla.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${applicationDeadlineForm.upazilla.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>    
                        <div class="form-group">
                            <label class="col-md-4"></label>
                            <div class="col-md-8">
                                <button type="submit" name="save" class="btn bg-blue">
                                    <i class="icon-search"></i>
                                    <spring:message code="applicationDeadline.loadUpazila" />
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </form:form>
    </c:when>
    <c:otherwise>
        <form:form id="deadlineSaveForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="applicationDeadlineForm">
            <form:hidden path="scheme.id"/>
            <form:hidden path="fiscalYear.id"/>
            <form:hidden path="district.id"/>    
            <section class="content-header clearfix">
                <h1 class="pull-left">
                    <spring:message code="applicationDeadline.header" /> 
                    <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/applicationDeadline/search"><spring:message code="applicationDeadline.loadAgain" />&nbsp;</a></small>
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
                    <div class="col-md-6 col-md-offset-2">
                        <table class="table" style="background-color: lightblue">
                            <tr>
                                <th><spring:message code="label.scheme" /></th>
                                <td>
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            <c:out value="${applicationDeadlineForm.scheme.nameInBangla}"></c:out>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${applicationDeadlineForm.scheme.nameInEnglish}"></c:out>
                                        </c:otherwise>
                                    </c:choose>                                    
                                </td>
                            </tr>
                            <tr>
                                <th><spring:message code="fiscalYear.label.fiscalYear" /></th>
                                <td>
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            <c:out value="${applicationDeadlineForm.fiscalYear.nameInBangla}"></c:out>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${applicationDeadlineForm.fiscalYear.nameInEnglish}"></c:out>
                                        </c:otherwise>
                                    </c:choose>                                    
                                </td>
                            </tr>                            
                        </table>
                        <table class="table table-hover table-bordered">
                            <tr>
                                <th>#</th>
                                <th><spring:message code="label.upazilla" /></th>
                                <th class="text-right"><spring:message code="label.deadline" /></th>                    
                            </tr>
                            <c:forEach items="${applicationDeadlineForm.applicationDeadlineList}" var="applicationDeadline" varStatus="status">
                                <tr>
                                    <td align="center" style="vertical-align: middle"><span id="deadlineIndex${status.count}">${status.count}</span></td>
                                    <td style="vertical-align: middle">
                                        <c:choose>
                                            <c:when test="${pageContext.response.locale eq 'bn'}">
                                                <span>${applicationDeadline.upazila.nameInBangla}</span>
<!--                                                <input class="form-control" name="applicationDeadlineList[${status.index}].upazila.nameInEnglish" value="${applicationDeadline.upazila.nameInBangla}" readonly="readonly"/>-->
                                            </c:when>
                                            <c:otherwise>
                                                <span>${applicationDeadline.upazila.nameInEnglish}</span>
<!--                                                <input class="form-control" name="applicationDeadlineList[${status.index}].upazila.nameInEnglish" value="${applicationDeadline.upazila.nameInEnglish}" readonly="readonly"/>-->
                                            </c:otherwise>
                                        </c:choose>

                                        <input type="hidden" name=applicationDeadlineList[${status.index}].upazila.id value="${applicationDeadline.upazila.id}">
                                    </td>                            
                                    <td><input class="form-control text-right deadline required"  name="applicationDeadlineList[${status.index}].deadline" value="<fmt:formatDate value='${applicationDeadline.deadline.time}' pattern="dd-MM-yyyy"/>" readonly="readonly"/></td>                          
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


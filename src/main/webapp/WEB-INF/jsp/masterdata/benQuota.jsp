<%-- 
    Document   : accountType
    Created on : Jan 26, 2017, 4:44:04 PM
    Author     : user
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(menuSelect("${pageContext.request.contextPath}/beneficiaryQuota"));
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

    $(function () {

        if ('${sessionScope.userDetail.scheme.shortName}' == 'LMA')
        {
            if ('${sessionScope.userDetail.userType.displayName}' == 'Ministry' || '${sessionScope.userDetail.userType.displayName}' == 'Directorate')
            {
                $("#applicantTypeBlock").show();
                if ($('#regularUser').is(":checked"))
                {
                    $("#bgmeaBlock").hide();
                    $("#bkmeaBlock").hide();
                }
                if ($('#bgmeaUser').is(":checked"))
                {
                    $("#bgmeaBlock").show();
                    $("#bkmeaBlock").hide();
                    $("#regularBlock").hide();
                }
                if ($('#bkmeaUser').is(":checked"))
                {
                    $("#bkmeaBlock").show();
                    $("#bgmeaBlock").hide();
                    $("#regularBlock").hide();
                }
            }
            if ('${sessionScope.userDetail.userType.displayName}' == 'BGMEA')
            {
                $('#bgmeaUser').iCheck('check');
            }
            if ('${sessionScope.userDetail.userType.displayName}' == 'BKMEA')
            {
                $('#bkmeaUser').iCheck('check');
            }
            if ('${sessionScope.userDetail.userType.displayName}' == 'Others')
            {
                $('#regularUser').iCheck('check');
            }
        } else
        {
            $('#regularUser').iCheck('check');
            $("#applicantType").val("REGULAR");
        }
        $('#regularUser').on('ifChanged', function (event) {
            if ($('#regularUser').is(":checked"))
            {
                $("#applicantType").val("REGULAR");
                $("#regularBlock").show();
                $("#bgmeaBlock").hide();
                $("#bgmeaFactoryId").prop("selectedIndex", 0);
                $("#bkmeaBlock").hide();
                $("#bkmeaFactoryId").prop("selectedIndex", 0);
                $('#bgmeaUser').iCheck('uncheck');
                $('#bkmeaUser').iCheck('uncheck');
            }
        });
        $('#bgmeaUser').on('ifChanged', function (event) {
            if ($('#bgmeaUser').is(":checked"))
            {
                $("#applicantType").val("BGMEA");
                $("#bgmeaBlock").show();
                $("#bkmeaBlock").hide();
                $("#bkmeaFactoryId").prop("selectedIndex", 0);
                $("#regularBlock").hide();
                loadDivision($('#divisionId'), '');
                resetSelect($('#districtId'));
                resetSelect($('#upazilaId'));
                resetSelect($('#unionId'));
                $('#regularUser').iCheck('uncheck');
                $('#bkmeaUser').iCheck('uncheck');
            }
        });
        $('#bkmeaUser').on('ifChanged', function (event) {
            if ($('#bkmeaUser').is(":checked"))
            {
                $("#applicantType").val("BKMEA");
                $("#bkmeaBlock").show();
                $("#bgmeaBlock").hide();
                $("#bgmeaFactoryId").prop("selectedIndex", 0);
                $("#regularBlock").hide();
                loadDivision($('#divisionId'), '');
                resetSelect($('#districtId'));
                resetSelect($('#upazilaId'));
                resetSelect($('#unionId'));
                $('#regularUser').iCheck('uncheck');
                $('#bgmeaUser').iCheck('uncheck');
            }
        });

        if ('${beneficiaryQuotaForm.district.id}' !== "")
        {
            loadPresentDistrictList($('#presentDivision')[0]);
        }
        if ('${beneficiaryQuotaForm.upazila.id}' !== "")
        {
            loadPresentUpazilaList($('#presentDistrict')[0]);
        }
    });
    function loadPresentDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#presentDistrict');
        if (divId !== '') {
            loadDistrict(divId, distSelectId, '${beneficiaryQuotaForm.district.id}');
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
            loadUpazilla(distId, upazillaSelectId, '${beneficiaryQuotaForm.upazila.id}');
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#presentUnion'));
        }
    }

</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="actionUrl" value="${contextPath}/beneficiaryQuota" />
<form:form id="quotaForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="beneficiaryQuotaForm">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="benQuota.header" />           
        </h1> 
        <c:if test="${actionType ne 'none'}">
            <div class="pull-right">
                <button type="submit" name="save" class="btn bg-blue">
                    <i class="fa fa-floppy-o"></i>
                    <spring:message code="save" />
                </button>                    
            </div>  
        </c:if>
    </section>
    <section class="content">
        <div class="row">
            <div class="col-md-6">               
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
                <form:hidden path="applicantType" id="applicantType"/>
                <div class="form-group" id="applicantTypeBlock" style="display: none">
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.applicantType" /><span class="mandatory">*</span></label>
                        <div class="col-md-8 labelAsValue">                        
                            <input type="radio" id="regularUser" name="regularUser" <c:if test="${beneficiaryQuotaForm.applicantType eq 'REGULAR'}">checked</c:if>>&nbsp;<spring:message code="applicant.regular" />
                            <input type="radio" id="bgmeaUser" name="bgmeaUser" <c:if test="${beneficiaryQuotaForm.applicantType eq 'BGMEA'}">checked</c:if>>&nbsp;<spring:message code="applicant.bgmea" />
                            <input type="radio" id="bkmeaUser" name="bkmeaUser" <c:if test="${beneficiaryQuotaForm.applicantType eq 'BKMEA'}">checked</c:if>>&nbsp;<spring:message code="applicant.bkmea" />
                            </div>
                        </div>
                    </div>

                    <div id="regularBlock">
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
                    </div>
                    <div class="form-group">
                        <label class="col-md-4"></label>
                        <div class="col-md-8">
                            <button type="submit" name="search" class="btn bg-blue">
                                <span class="glyphicon glyphicon-search">&nbsp;</span>
                            <spring:message code="label.search" />
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6 col-md-offset-2">
                <c:choose>
                    <c:when test="${beneficiaryQuotaForm.benQuotaList.size() > 0}">
                        <table class="table table-hover table-bordered">
                            <tr>
                                <th>#</th>
                                <th>
                                    <c:choose>
                                        <c:when test="${sessionScope.userDetail.schemeShortName == 'MA'}">
                                            <spring:message code="label.union" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${beneficiaryQuotaForm.applicantType.displayName == 'Regular'}">
                                                    <spring:message code="label.municipalOrCityCorporation"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <spring:message code="label.factory"/>                                                    
                                                </c:otherwise>
                                            </c:choose>                                            
                                        </c:otherwise>
                                    </c:choose>
                                </th>
                                <th class="text-right"><spring:message code="benQuota.quota" /></th>                    
                            </tr>
                            <c:forEach items="${beneficiaryQuotaForm.benQuotaList}" var="benQuota" varStatus="status">
                                <tr>
                                    <td align="center" style="vertical-align: middle"><span id="quotaIndex${status.count}">${status.count}</span></td>
                                    <td style="vertical-align: middle">
                                        <c:choose>
                                            <c:when test="${benQuota.applicantType =='REGULAR'}">
                                                <c:choose>
                                                    <c:when test="${pageContext.response.locale eq 'bn'}">
                                                        <span>${benQuota.union.nameInBangla}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span>${benQuota.union.nameInEnglish}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                                <input type="hidden" name=benQuotaList[${status.index}].union.id value="${benQuota.union.id}">        
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${pageContext.response.locale eq 'bn'}">
                                                        <span>${benQuota.factory.nameInBangla}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span>${benQuota.factory.nameInEnglish}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                                <input type="hidden" name=benQuotaList[${status.index}].factory.id value="${benQuota.factory.id}"> 
                                            </c:otherwise>
                                        </c:choose>


                                    </td>                            
                                    <td><input class="form-control text-right required quota" onkeydown="checkNumberWithLength(event, this, 3)" id="benQuotaList[${status.index}].quota" name="benQuotaList[${status.index}].quota" value="${benQuota.quota}"/></td>                          
                                </tr>
                            </c:forEach>
                        </table>	

                    </c:when>
                    <c:otherwise>
                        <c:if test="${actionType ne 'none'}">
                            <spring:message code="label.noDataFound"/>                            
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </section>
</form:form>
<!-- /.content -->


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />"></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<script type="text/javascript" src="<c:url value="/resources/js/jquery.ui.datepicker-bn.js"/>" ></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>


<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/monitoring/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#monitoringmodel-delete-confirmation");
        initDate($("#monitoringDate"), '${dateFormat}', $("#monitoringDate\\.icon"),selectedLocale);
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        if (selectedLocale === "bn")
        {
            makeUnijoyEditor('officerName');
            makeUnijoyEditor('designation');
            makeUnijoyEditor('durationDay');
            makeUnijoyEditor('findings');
            $("#durationDay").val(getNumberInBangla(""+ $("#durationDay").val()+""));
            $.datepicker.setDefaults(
                    $.extend(
                            {'dateFormat': 'dd-mm-yy'},
                            $.datepicker.regional['bn-BD']
                            )
                    );

            $("#monitoringDate").val(getNumberInBangla($("#monitoringDate").val()));
        }
         $("#monitoringForm").validate({
            rules: {// checks NAME not ID
                "officerName": {
                    required: true
                },
                "scheme.id": {
                    required: true
                },
                "designation": {
                    required: true
                },
                "monitoringDate": {
                    required: true
                },
                "durationDay": {
                    required: true
                },
                "purpose.id": {
                    required: true
                },
                "findings": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });
    $(window).load(function () {
        loadDivision($("select#presentDivision"), '${monitoring.division.id}');
        loadPresentDistrictList($('#presentDivision')[0]);
        loadPresentUpazilaList($('#presentDistrict')[0]);
        loadPresentUnionList($('#presentUpazila')[0]);
    });

    function loadPresentDistrictList(selectObject) {
        $("#labelMandatoryLocation").hide();
        var divId = selectObject.value;
        var distSelectId = $('#presentDistrict');
        if (divId !== '') {
            loadDistrict(divId, distSelectId, '${monitoring.district.id}');
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
            loadUpazilla(distId, upazillaSelectId, '${monitoring.upazilla.id}');
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#presentUnion'));
        }
    }
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#presentUnion');

        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId, '${monitoring.union.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }
     function submitForm() {
        var form = $('#monitoringForm');
        form.validate();
        if (!form.valid()) {
            return false;
        }
        if ($('#presentDivision').val() == "")
        {
            $("#labelMandatoryLocation").show();
            return false;
        }
         if (selectedLocale === "bn")
        {
            $("#monitoringDate").val(getNumberInEnglish("" + $("#monitoringDate").val() + ""));
        }
    }

</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/monitoring/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/monitoring/edit/${monitoring.id}" />
    </c:otherwise>
</c:choose>

<form:form id="monitoringForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="monitoring">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="monitoring.addEdit" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/monitoring/list"><spring:message code="label.backToList" /></a></small>
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
                <fieldset>
                    <legend><spring:message code='monitoring.info'/></legend>
                    <form:hidden path="id" />
                    <div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="monitoring.scheme" /><span class="mandatory">*</span></label>
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
                                <label for="officerNameInputEnglish" class="col-md-4 control-label"><spring:message code="monitoring.officerName" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='monitoring.officerName' var="officerName"/>
                                <form:input class="form-control bfh-phone" placeholder="${officerName}" path="officerName" autofocus="autofocus"/>
                                <form:errors path="officerName" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="designationInputEnglish" class="col-md-4 control-label"><spring:message code="monitoring.designation" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='monitoring.designation' var="designation"/>
                                <form:input class="form-control bfh-phone" placeholder="${designation}" path="designation" autofocus="autofocus"/>
                                <form:errors path="designation" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="code" class="col-md-4 control-label"><spring:message code="monitoring.monitoringDate" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='monitoring.monitoringDate' var="monitoringDate"/>
                                <form:input class="form-control" placeholder="${monitoringDate}" path="monitoringDate"/>
                                <form:errors path="monitoringDate" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="durationDayInputEnglish" class="col-md-4 control-label"><spring:message code="label.durationDay" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.durationDay' var="durationDay"/>
                                <form:input class="form-control bfh-phone" placeholder="${durationDay}" path="durationDay" autofocus="autofocus" onkeydown="checkNumberWithLength(event, this, 2)"/>
                                <form:errors path="durationDay" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="monitoring.purpose" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="purpose.id"  id="purpose"> 
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${purposeList}" itemValue="id" itemLabel="${purposeName}"></form:options> 
                                </form:select> 
                                <form:errors path="purpose.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="findingsInputEnglish" class="col-md-4 control-label"><spring:message code="monitoring.findings" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='monitoring.findings' var="findings"/>
                                <form:textarea class="form-control bfh-phone" placeholder="${findings}" rows="6" path="findings" autofocus="autofocus"/>
                                <form:errors path="findings" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </div>
                <div class="col-md-6"> 
                    <fieldset>
                        <legend><spring:message code='monitoring.monitoringLocation'/>&nbsp;
                        <span id="labelMandatoryLocation" class="mandatory" style="display: none; font-size: 14px">
                            *<spring:message code='label.monitoringLocationRequired'/>
                        </span></legend>
                    <div class="form-group">
                        <label for="divisionInput" class="col-md-4 control-label"><spring:message code="label.division" /></label>
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
                            <label for="presentDistrict" class="col-md-4 control-label"><spring:message code="label.district" /></label>
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
                            <label for="presentUpazila" class="col-md-4 control-label"><spring:message code="label.upazila" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="upazilla.id" id="presentUpazila" onchange="loadPresentUnionList(this)">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${upazilaList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                            </form:select>
                            <form:errors path="upazilla.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="presentUnion" class="col-md-4 control-label"><spring:message code="label.union" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="union.id" id="presentUnion">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${unionList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                            </form:select>
                            <form:errors path="union.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>  
                    </fieldset>
                </div>
        </form:form>
    </div>
</div>

<div id='monitoringmodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="monitoringmodel-delete-confirmation-title">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
            </div>
            <form action="${contextPath}/monitoring/delete/${monitoring.id}" method="post">
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


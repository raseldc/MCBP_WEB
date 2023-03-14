<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<style>
    .modal {
        text-align: center;
    }

    @media screen and (min-width: 768px) { 
        .modal:before {
            display: inline-block;
            vertical-align: middle;
            content: " ";
            height: 100%;
        }
    }

    .modal-dialog {
        display: inline-block;
        text-align: center;
        vertical-align: middle;
    }
</style>
<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/fiscalYear/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#fiscalyearmodel-delete-confirmation");

        $.validator.addMethod("uniqueFiscalYear", function (value, element) {
            var isSuccess = false;
            $.ajax({
                type: "POST",
                url: contextPath + "/checkUniqueFiscalYear",
                async: false,
                data: {'fiscalYearName': $("#nameInEnglish").text(), 'fiscalYearId': $("#id").val()
                },
                success: function (msg)
                {
                    isSuccess = msg === true ? true : false;
                },
                failure: function () {
                    log("checking uniqueness of fiscal year failed!!");
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log('error=' + xhr);
                    console.log('error=' + thrownError);
                }
            });
            return isSuccess;
        });

        jQuery.validator.addMethod("fiscalYearCheck", function (value, element, param) {
            var start = $("#startYear :selected").val();
            var end = $("#endYear :selected").val();
            if (start != "" && end != "" && (end - start <= 0 || end - start > 1))
            {
                return false;
            }
            return true;
        });

        $("#fiscalYearForm").validate({
            rules: {// checks NAME not ID
                "startYear": {
                    required: true,
                    "fiscalYearCheck": true,
                    "uniqueFiscalYear": true
                },
                "endYear": {
                    required: true,
                    "fiscalYearCheck": true,
                    "uniqueFiscalYear": true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
        var locale = "${pageContext.response.locale}";
        var contextPath = "${pageContext.request.contextPath}";
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + locale + ".js");
    });

    function submitForm() {
        if ($("#active").is(":checked") && "${activeFiscalYear}" != "")
        {
            if (("${actionType}" == "create") || ("${actionType}" == "edit" && "${fiscalYear.id}" != "${activeFiscalYear.id}"))
            {
                if ("${pageContext.response.locale}" == "bn")
                {
                    bootbox.alert("<b>দুইটি সক্রিয় অর্থবছর অনুমদিত নয়!</b>");
                } else
                {
                    bootbox.alert("<b>Cannot be Two Active Fiscal Years!</b>");
                }
                return false;
            }
        }
        $('#fiscalYearForm').validate();
        if ($("#fiscalYearForm").valid()) {
            $('#fiscalYearForm').submit();
        }
    }

    function generateFiscalYearName(event) {
        var startYear = $("#startYear :selected").val();
        var endYear = $("#endYear :selected").val().substring(2, 4);

        $("#nameInEnglish").text(startYear + "-" + endYear);
        $("input#nameInEnglish").val(startYear + "-" + endYear);
        $("#nameInBangla").text(getNumberInBangla(startYear) + "-" + getNumberInBangla(endYear));
        $("input#nameInBangla").val(getNumberInBangla(startYear) + "-" + getNumberInBangla(endYear));
    }

    $(window).load(function () {
        var thisYear = (new Date()).getFullYear();
        var yearRange = 10;
        getYearDropDown($("#startYear"), thisYear, yearRange, '${fiscalYear.startYear}');
        getYearDropDown($("#endYear"), thisYear + 1, yearRange, '${fiscalYear.endYear}');
        generateFiscalYearName();
    });
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/fiscalYear/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/fiscalYear/edit/${fiscalYear.id}" />
    </c:otherwise>
</c:choose>

<form:form id="fiscalYearForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="fiscalYear">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.fiscalYear" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/fiscalYear/list"><spring:message code="label.backToList" /></a></small>
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
                        <label for="code" class="col-md-4 control-label"><spring:message code="fiscalYear.label.startYear" /><span class="mandatory">*</span></label>
                        <div class="col-md-4">                            
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="startYear" onchange="generateFiscalYearName(this.id);">
                                <form:option value="" label="${select}"></form:option>
                            </form:select>
                            <form:errors path="startYear" cssclass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="fiscalYear.label.endYear" /><span class="mandatory">*</span></label>
                        <div class="col-md-4">
                            <spring:message code='fiscalYear.label.endYear' var="endYear"/>
                            <form:select class="form-control" path="endYear" onchange="generateFiscalYearName(this.id);">
                                <form:option value="" label="${select}"></form:option>
                            </form:select>
                            <form:errors path="endYear" cssclass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="fiscalyearNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameBn" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="fiscalYearNameInBangla"/>
                            <label id="nameInBangla" class="labelAsValue">${fiscalYear.nameInBangla}</label>
                            <form:hidden path="nameInBangla" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="fiscalyearNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="fiscalYearNameInEnglish"/>
                            <label id="nameInEnglish" class="labelAsValue">${fiscalYear.nameInEnglish}</label>
                            <form:hidden path="nameInEnglish" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="active" class="col-md-4 control-label"><spring:message code="label.active" /></label>
                        <div class="col-md-8">
                            <form:checkbox class="icheckbox_square-blue" path="active" id="active" />
                        </div>  
                    </div>
                </div>
            </form:form>
        </div>
    </div>

    <div id='fiscalyearmodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="fiscalyearmodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/fiscalYear/delete/${fiscalYear.id}" method="post">
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


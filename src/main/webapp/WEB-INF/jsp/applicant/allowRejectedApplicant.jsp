<%-- 
    Document   : applicantList
    Created on : Feb 5, 2017, 10:18:00 AM
    Author     : Philip
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    #applicantListTable_length {
        float:left;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    }    
</style>
<script>
    var startYearOfFY;
    var endYearOfFY;
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");

        $('#nid').keypress(function (e) {
            if (e.which == 13) {
                $("#buttonSearch").click();
                return false;    //<---- Add this line
            }
        });

        if (selectedLocale == 'bn') {
            makeUnijoyEditor('nid'); // NID actually
        }
        var applicationId = document.getElementById("nid");
        applicationId.addEventListener("keydown", function (event) {
            checkNumberWithLengthWithPasteOption(event, this, 17);
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

        $("#pendingListForm").validate({
            rules: {// checks NAME not ID
                "fiscalYear.id": {
                    required: true
                },
                "scheme.id": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });

</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:url var="formAction" value="/page"></c:url>
    <section class="content-header clearfix">
        <h1 class="pull-left">
        <spring:message code="allowRejectedApplicant" />
        <small></small>
    </h1>    
</section>
<section class="content">    
    <div class="row">
        <div class="col-xs-12">
            <form:form id="pendingListForm" class="form-horizontal">
                <div class="col-md-6">                                                     
                    <div class="form-group">
                        <label for="status" class="col-md-4 control-label"><spring:message code='label.nid' var="nid" />${nid}</label>
                        <div class="col-md-8">
                            <input type="text" id="nid" name="nid" class="form-control" placeholder="${nid}">
                        </div>
                    </div>
                    <div class="form-group">
                        &nbsp;
                    </div>
                    <div class="form-group">
                        <div class="col-md-4"></div>
                        <div class="col-md-8">
                            <div>
                                <button type="submit" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="allowRejectedApplicant"/></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </div>   
</div>  
</section>

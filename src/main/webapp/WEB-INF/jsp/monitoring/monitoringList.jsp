<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">
<!--<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css" />">-->
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
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
        text-align: left;
        vertical-align: middle;
    }
    .btn-default{
        display: none;
    }
</style>
<script>
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        var urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }
        if ("${message.messageType}" == "SUCCESS")
        {
//            bootbox.alert("<b>${message.message}</b>");
            bootbox.confirm({
                title: '<spring:message code="label.success" />',
                message: "<b>${message.message}</b>",
                buttons: {
                    cancel: {
                        label: "Cancel",
                    },
                    confirm: {
                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                    }
                },
                callback: function (result) {

                }
            });
        } else if ("${message.messageType}" == "DANGER")
        {
//            bootbox.alert("<b>${message.message}</b>");
            bootbox.confirm({
                title: '<spring:message code="label.failure" />',
                message: "<b>${message.message}</b>",

                buttons: {
                    cancel: {
                        label: "Cancel",
                    },
                    confirm: {
                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />',

                    }
                },
                callback: function (result) {

                }
            });
        }
        $("#buttonSearch").click(function ()
        {
            if ($("#purposeId").val() === "")
            {
                $("#purposeLabel").show();
                return;
            } else
            {
                $("#purposeLabel").hide();
            }
            if ($("#purposeId").val() === "")
            {
                return;
            }
            $('#monitoringListTable').DataTable().destroy();
            var path = '${pageContext.request.contextPath}';
            $('#monitoringListTable').DataTable({
                "processing": true,
                "pageLength": 10,
                "lengthMenu": [5, 10, 15, 20],
                "serverSide": true,
                "pagingType": "full_numbers",
                "dom": '<"top"i>rt<"bottom"lp><"clear">',
                "language": {
                    "url": urlLang
                },
                "ajax": {
                    "url": path + "/monitoring/list",
                    "type": "POST",
                    "data": {
                        "purpose": $("#purposeId").val()
                    }
                },
                "fnDrawCallback": function (oSettings) {
//                    showModalDialog();
                    if (selectedLocale === 'bn')
                    {
                        localizeBanglaInDatatable("monitoringListTable");
                        $('#monitoringListTable  tr').not(":first").each(function () {
                            $(this).find("td").eq(2).html(getNumberInBangla($(this).find("td").eq(2).html()));
                            $(this).find("td").eq(3).html(getNumberInBangla($(this).find("td").eq(3).html()));
                        });
                    }
                }
            });
        });

    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="monitoringList.monitoringList" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/monitoring/create" class="btn bg-blue">
            <i class="fa fa-plus-square"></i>
            <spring:message code="addNew" />            
        </a>
    </div>   
</section>
<section class="content">    
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-body">
                    <div class="col-md-6 form-horizontal">
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="monitoring.purpose" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <select class="form-control" id="purposeId" name="purposeId">
                                    <option value="">${select}</option>
                                    <c:forEach var="t" items="${purposeList}">     
                                        <c:if test="${pageContext.response.locale=='bn'}">
                                            <option value="${t.id}">${t.nameInBangla}</option>
                                        </c:if>
                                        <c:if test="${pageContext.response.locale=='en'}">
                                            <option value="${t.id}">${t.nameInEnglish}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                                <label id="purposeLabel" style="color:red; display :none" ><spring:message code="label.requiredField" /></label>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label"></label>
                            <div class="col-md-8">
                                <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container-fluid row">
                    <table id="monitoringListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="monitoring.officerName" /></th>                                                                
                                <th><spring:message code="monitoring.designation" /></th>                                                                
                                <th><spring:message code="monitoring.monitoringDate" /></th>                                                                
                                <th><spring:message code="label.durationDay" /></th>                                                                
                                <th><spring:message code="monitoring.findings" /></th>                                                                
                                <th><spring:message code="monitoring.location" /></th>                                                                
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

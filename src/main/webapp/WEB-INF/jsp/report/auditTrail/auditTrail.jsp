<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css" />">
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.ui.datepicker-bn.js"/>" ></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<script src="<c:url value="/resources/js/moment.js" />" ></script>
<style>

    .btn-default{
        display: none;
    }
</style>
<script>
    $(function () {
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        var urlLang = "";
        initDate($("#startDate"), '${dateFormat}', $("#startDate\\.icon"), selectedLocale);
        initDate($("#endDate"), '${dateFormat}', $("#endDate\\.icon"), selectedLocale);
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
            $.datepicker.setDefaults(
                    $.extend(
                            {'dateFormat': 'dd-mm-yy'},
                            $.datepicker.regional['bn-BD']
                            )
                    );

            $("#startDate").val(getNumberInBangla($("#startDate").val()));
            $("#endDate").val(getNumberInBangla($("#endDate").val()));
        }
        $("#buttonSearch").click(function ()
        {
            $('#auditTrailListTable').DataTable().destroy();
            var path = '${pageContext.request.contextPath}';
            var start = getNumberInEnglish($('#startDate').val());
            if (start != "")
            {
                start = moment(start, "DD-MM-YYYY").format("YYYY-MM-DD");
            }
            var end = getNumberInEnglish($('#endDate').val());
            if (end != "")
            {
                end = moment(end, "DD-MM-YYYY").format("YYYY-MM-DD");
            }
            $('#auditTrailListTable').DataTable({
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
                    "url": path + "/auditTrail/list",
                    "type": "POST",
                    "data": {
                        "loggingServiceType": $("#loggingServiceType").val(),
                        "loggingTableType": $("#loggingTableType").val(),
                        "startDate": start,
                        "endDate": end
                    }
                },
                "fnDrawCallback": function (oSettings) {
//                    showModalDialog();
                    if (selectedLocale === 'bn')
                    {
                        localizeBanglaInDatatable("auditTrailListTable");
                        $('#auditTrailListTable  tr').not(":first").each(function () {
                            $(this).find("td").eq(1).html(getNumberInBangla($(this).find("td").eq(1).html()));
                        });
                    }
                }
            });
        });

    });
    function checkDate(event)
    {
        if ($('#startDate').val() != "" && $('#endDate').val() != "")
        {
            var start = $.datepicker.parseDate("dd-mm-yy", getNumberInEnglish($('#startDate').val()));
            var end = $.datepicker.parseDate("dd-mm-yy", getNumberInEnglish($('#endDate').val()));
// end - start returns difference in milliseconds 
            var diff = new Date(end - start);
            if (diff < 0)
            {
                if ("${pageContext.response.locale}" == "bn")
                {
                    bootbox.alert("<b>শুরুর তারিখ শেষের তারিখ থেকে পূর্বে হতে হবে!</b>");
                } else
                {
                    bootbox.alert("<b>Start Date should be Earlier than End Date!</b>");
                }
                document.getElementById(event).value = "";
            }
        }
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.auditTrail" />
        <small></small>
    </h1>
</section>
<section class="content">    
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-body">
                    <div class="col-md-6 form-horizontal">
                        <div class="form-group">
                            <label for="loggingServiceType" class="col-md-4 control-label"><spring:message code="auditTrail.service" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <spring:message code='auditTrail.service' var="loggingServiceType"/>
                                <select class="form-control" placeholder="${loggingServiceType}"  id="loggingServiceType" autofocus="autofocus">
                                    <option value="" label="${select}" />
                                    <c:forEach items="${loggingServiceTypeList}" var="loggingServiceType">
                                        <c:if test="${pageContext.response.locale=='en'}">   
                                            <option value="${loggingServiceType}" label="${loggingServiceType.displayName}">${loggingServiceType.displayName}</option>
                                        </c:if>
                                        <c:if test="${pageContext.response.locale=='bn'}">   
                                            <option value="${loggingServiceType}" label="${loggingServiceType.displayNameBn}">${loggingServiceType.displayNameBn}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="loggingTableType" class="col-md-4 control-label"><spring:message code="auditTrail.menuName" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <spring:message code='auditTrail.menuName' var="loggingTableType"/>
                                <select class="form-control" placeholder="${loggingTableType}"  id="loggingTableType" autofocus="autofocus">
                                    <option value="" label="${select}" />
                                    <c:forEach items="${loggingTableTypeList}" var="loggingTableType">
                                        <c:if test="${pageContext.response.locale=='en'}">   
                                            <option value="${loggingTableType}" label="${loggingTableType.displayName}">${loggingTableType.displayName}</option>
                                        </c:if>
                                        <c:if test="${pageContext.response.locale=='bn'}">   
                                            <option value="${loggingTableType}" label="${loggingTableType.displayNameBn}">${loggingTableType.displayNameBn}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="auditTrail.userName" /></label>
                            <div class="col-md-8">
                                <spring:message code='auditTrail.userName' var="userId"/>
                                <input class="form-control" placeholder="${userId}" id="userId"/>
                                <a class="btn" href="applicant/viewApplicant/5" data-toggle="modal">Link</a>
                                <input type="button" onclick="location.href='applicant/viewApplicant/5'" value="Go to Google" data-toggle="modal" style="display: inline"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 form-horizontal">
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="training.startDate" /></label>
                            <div class="col-md-8">
                                <spring:message code='training.startDate' var="startDate"/>
                                <input class="form-control" placeholder="${startDate}" id="startDate" onchange="checkDate(this.id)"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="training.endDate" /></label>
                            <div class="col-md-8">
                                <spring:message code='training.endDate' var="endDate"/>
                                <input class="form-control" placeholder="${endDate}" id="endDate" onchange="checkDate(this.id)"/>
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
                    <table id="auditTrailListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="auditTrail.userName" /></th>                                                                
                                <th><spring:message code="auditTrail.changeDate" /></th> 
                                <th><spring:message code="auditTrail.menuName" /></th>
                                <th><spring:message code="auditTrail.service" /></th>                                                                
                                <th><spring:message code="auditTrail.beforeValue" /></th>                                                                
                                <th><spring:message code="auditTrail.afterValue" /></th>                                                                
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

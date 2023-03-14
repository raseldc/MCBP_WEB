<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script>
    $(function () {
        var urlLang = "";
        if (selectedLocale === 'bn') {
            urlLang = contextPath + "/dataTable/localization/bangla";
        }

        if ("${message.message}")
        {
            bootbox.dialog({
                onEscape: function () {},
                title: <c:if test="${message.messageType.toString() eq 'Success'}">'<spring:message code="label.success" />'</c:if><c:if test="${message.messageType.toString() ne 'Success'}">'<spring:message code="label.failure" />'</c:if>,
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

        $('#fiscalYearListTable').DataTable({
//            "dom": '<"top"i>rt<"bottom"lp><"clear">',
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("fiscalYearListTable");
                }

            }
        });
    });
</script>
<style>
    @media only screen and (min-width: 768px) {
        .table-responsive {
            overflow-x: hidden;
        }
    }
</style>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="selectedLocale" value="${pageContext.response.locale}" />
<%--<c:if test="${not empty message.message}">                
    <div class="alert 
         <c:if test="${message.messageType eq 'SUCCESS'}">alert-success</c:if> 
         <c:if test="${message.messageType eq 'INFO'}">alert-info</c:if> 
             alert-dismissable">
             <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
         <c:out value="${message.message}"></c:out>
         </div>
</c:if>--%>
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="fiscalYear.label.fiscalYear" />&nbsp;<spring:message code="label.list" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/fiscalYear/create" class="btn bg-blue">
            <i class="fa fa-plus-square"></i>
            <spring:message code="addNew" />
        </a>
    </div>

</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box table-responsive">
                <div class="box-body">
                    <table id="fiscalYearListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.nameBn" /></th>
                                <th><spring:message code="label.nameEn" /></th>
                                <th><spring:message code="fiscalYear.label.startYear" /></th>
                                <th><spring:message code="fiscalYear.label.endYear" /></th>
                                <th><spring:message code="label.active" /></th>
                                <th><spring:message code="label.edit" /></th>
                                <!--<th><spring:message code="label.delete" /></th>-->
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="fiscalYear" items="${fiscalYearList}">
                                <tr>
                                    <td><c:out value="${fiscalYear.nameInBangla}"></c:out></td>
                                    <td><c:out value="${fiscalYear.nameInEnglish}"></c:out></td>
                                        <td>
                                        <c:if test="${selectedLocale eq 'en'}">
                                            <c:out value="${fiscalYear.startYear}"></c:out>
                                        </c:if>
                                        <c:if test="${selectedLocale eq 'bn'}">
                                            <script>
                                                var bnText = getNumberInBangla('' + ${fiscalYear.startYear} + '');
                                                document.writeln(bnText);
                                            </script>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${selectedLocale eq 'en'}">
                                            <c:out value="${fiscalYear.endYear}"></c:out>
                                        </c:if>
                                        <c:if test="${selectedLocale eq 'bn'}">
                                            <script>
                                                var bnText = getNumberInBangla('' + ${fiscalYear.endYear} + '');
                                                document.writeln(bnText);
                                            </script>
                                        </c:if>                                    
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fiscalYear.active}">
                                                <i class="fa fa-check true-icon"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fa fa-times" aria-hidden="true"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <spring:message code="edit" var="tooltipEdit"/>
                                        <a href="${contextPath}/fiscalYear/edit/${fiscalYear.id}"><span class="glyphicon glyphicon-edit" title="${tooltipEdit}"></span> </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>


</section>

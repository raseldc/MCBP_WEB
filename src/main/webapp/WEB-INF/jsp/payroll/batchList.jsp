<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

        $('#batchListTable').DataTable({
//            "dom": '<"top"i>rt<"bottom"lp><"clear">',
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("batchListTable");
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

<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.batch" />&nbsp;<spring:message code="label.list" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/batch/create" class="btn bg-blue">
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
                    <table id="batchListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="paymentCycle.label.nameInBangla" /></th>                                
                                <th><spring:message code="paymentCycle.label.nameInEnglish" /></th>
                                <th><spring:message code="label.active" /></th>
                                <th><spring:message code="label.edit" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="batch" items="${batchList}">
                                <tr>
                                    <td><c:out value="${batch.nameInBangla}"></c:out></td>
                                    <td><c:out value="${batch.nameInEnglish}"></c:out></td>
                                        <td>
                                        <c:choose>
                                            <c:when test="${batch.active}">
                                                <i class="fa fa-check true-icon"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fa fa-times" aria-hidden="true"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <spring:message code="edit" var="tooltipEdit"/>
                                        <a href="${contextPath}/batch/edit/${batch.id}"><span class="glyphicon glyphicon-edit" title="${tooltipEdit}"></span></a>
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

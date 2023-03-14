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

        $('#paymentListTable').DataTable({
            "dom": '<"top"i>rt<"bottom"lp><"clear">',
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("paymentListTable");
                }

            }
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:if test="${not empty message.message}">                
    <div class="alert 
         <c:if test="${message.messageType eq 'SUCCESS'}">alert-success</c:if> 
         <c:if test="${message.messageType eq 'INFO'}">alert-info</c:if> 
             alert-dismissable">
             <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
         <c:out value="${message.message}"></c:out>
         </div>
</c:if>

<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.paymentApprove" />&nbsp;<spring:message code="label.list" />
        <small></small>
    </h1>

</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-body">
                    <table id="paymentListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.scheme" /></th>
                                <th><spring:message code="label.fiscalYear" /></th>
                                <th><spring:message code="label.paymentCycle" /></th>
                                <th><spring:message code="payment.label.totalBeneficiary" /></th>
                                <th><spring:message code="payment.label.totalAllowanceAmount" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="payment" items="${paymentList}">
                                <tr>
                                    <td><c:out value="${payment.scheme.nameInEnglish}"></c:out></td>
                                    <td><c:out value="${payment.fiscalYear.nameInEnglish}"></c:out></td>
                                    <td><c:out value="${payment.paymentCycle.nameInEnglish}"></c:out></td>
                                    <td><c:out value="${payment.totalBeneficiary}"></c:out></td>
                                    <td><c:out value="${payment.totalAllowanceAmount}"></c:out></td>
                                    <td><a href="${contextPath}/paymentGeneration/pendingList/${payment.scheme.id}/${payment.paymentCycle.id}" title="${tooltipEdit}">
                                            <span class="glyphicon glyphicon-edit"></span> </a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>


</section>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script type="text/javascript">
    $(function () {
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
            $('#schemeAttributeList  tr').not(":first").each(function () {
                $(this).find("td").eq(2).html(getNumberInBangla($(this).find("td").eq(2).html()));
                $(this).find("td").eq(7).html(getNumberInBangla($(this).find("td").eq(7).html()));
                $(this).find("td").eq(8).html(getNumberInBangla($(this).find("td").eq(8).html()));
            });
        }
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
        $('#schemeAttributeList').DataTable({
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("schemeAttributeList");
                }

            }
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <label><spring:message code="label.schemeAttribute" />&nbsp;<spring:message code="label.list" /></label>
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/schemeAttribute/create" class="btn bg-blue">
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
                    <table id="schemeAttributeList" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.scheme" /></th>
                                <th><spring:message code="label.schemeAttribute" /></th>
                                <th><spring:message code="label.schemeAttributeSelectionCriteriaPriority" /></th>
                                <th><spring:message code="label.schemeAttributeComparisonType" /></th>
                                <th><spring:message code="label.schemeAttributeComparedValue" /></th>
                                <th><spring:message code="label.schemeAttributeIsMandatoryForSelectionCriteria" /></th>
                                <th><spring:message code="label.schemeAttributeOrderingType"/></th>
                                <th><spring:message code="label.schemeAttributeType" /></th>
                                <th><spring:message code="label.schemeAttributeViewOrder" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="schemeAttribute" items="${schemeAttributeList}">
                                <tr>
                                    <c:if test="${pageContext.response.locale=='bn'}">
                                        <td><c:out value="${schemeAttribute.scheme.nameInBangla}"></c:out></td>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='en'}">
                                        <td><c:out value="${schemeAttribute.scheme.nameInEnglish}"></c:out></td>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='bn'}">
                                        <td><c:out value="${schemeAttribute.nameInBangla}"></c:out></td>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='en'}">
                                        <td><c:out value="${schemeAttribute.nameInEnglish}"></c:out></td>
                                    </c:if>
                                    <td><c:out value="${schemeAttribute.selectionCriteriaPriority}"></c:out></td>
                                     <c:if test="${pageContext.response.locale=='bn'}">
                                        <td><c:out value="${schemeAttribute.comparisonType.displayNameBn}"></c:out></td>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='en'}">
                                        <td><c:out value="${schemeAttribute.comparisonType.displayName}"></c:out></td>
                                    </c:if>
                                    <td><c:out value="${schemeAttribute.comparedValue}"></c:out></td>
                                    <c:if test="${schemeAttribute.isMandatoryForSelectionCriteria=='true'}">
                                        <td><span class="glyphicon glyphicon-ok"></span></td>
                                        </c:if>
                                        <c:if test="${schemeAttribute.isMandatoryForSelectionCriteria=='false'}">
                                        <td><span class="glyphicon glyphicon-remove"></span></td>
                                        </c:if>
                                        <c:if test="${pageContext.response.locale=='en'}">
                                        <td><c:out value="${schemeAttribute.orderingType.displayName}"></c:out></td>
                                        <td><c:out value="${schemeAttribute.attributeType.displayName}"></c:out></td>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='bn'}">
                                        <td><c:out value="${schemeAttribute.orderingType.displayNameBn}"></c:out></td>
                                        <td><c:out value="${schemeAttribute.attributeType.displayNameBn}"></c:out></td>
                                    </c:if>

                                    <td><c:out value="${schemeAttribute.viewOrder}"></c:out></td>
                                        <td>
                                            <a href="${contextPath}/schemeAttribute/edit/${schemeAttribute.id}" data-toggle="tooltip" title="${tooltipEdit}">
                                            <span class="glyphicon glyphicon-edit"></span> 
                                        </a>
                                        <c:if test="${schemeAttribute.attributeType ne 'TEXT'}">
                                            ||
                                            <a href="${contextPath}/schemeAttributeSetupData/attribute/edit/${schemeAttribute.id}"><spring:message code="label.addValue"/></a>
                                        </c:if>
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
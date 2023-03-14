<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
    $(function () {
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
            $('#schemeList  tr').not(":first").each(function () {
                $(this).find("td").eq(2).html(getNumberInBangla($(this).find("td").eq(2).html()));
                $(this).find("td").eq(3).html(getNumberInBangla($(this).find("td").eq(3).html()));
                $(this).find("td").eq(4).html(getNumberInBangla($(this).find("td").eq(4).html()));
                $(this).find("td").eq(5).html(getNumberInBangla($(this).find("td").eq(5).html()));
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
        $('#schemeList').DataTable({
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("schemeList");
                }

            }
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <label><spring:message code="label.scheme" />&nbsp;<spring:message code="label.list" /></label>
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/scheme/create" class="btn bg-blue">
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
                    <table id="schemeList" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.nameEn" /></th>
                                <th><spring:message code="label.nameBn" /></th>
                                <th><spring:message code="label.code" /></th>
                                <th><spring:message code="label.schemeDefaultMonth" /></th>
                                <th><spring:message code="label.schemeActivationDate" /></th>
                                <th><spring:message code="label.schemeDeactivationDate" /></th>
                                <th><spring:message code="label.description" /></th>
                                <th><spring:message code="label.active" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="scheme" items="${schemeList}">
                                <tr>
                                    <td><c:out value="${scheme.nameInEnglish}"></c:out></td>
                                    <td><c:out value="${scheme.nameInBangla}"></c:out></td>
                                    <td><c:out value="${scheme.code}"></c:out></td>
                                    <td><c:out value="${scheme.defaultMonth}"></c:out></td>
                                    <td><fmt:formatDate value="${scheme.activationDate.time}" pattern="dd-MM-yyyy"/></td>
                                    <td><fmt:formatDate value="${scheme.deactivationDate.time}" pattern="dd-MM-yyyy"/></td>
                                    <td><c:out value="${scheme.description}"></c:out></td>
                                    <c:if test="${scheme.active=='true'}">
                                        <td><span class="glyphicon glyphicon-ok"></span></td>
                                        </c:if>
                                        <c:if test="${scheme.active=='false'}">
                                        <td><span class="glyphicon glyphicon-remove"></span></td>
                                        </c:if>
                                    <td>
                                        <a href="${contextPath}/scheme/edit/${scheme.id}" data-toggle="tooltip" title="${tooltipEdit}">
                                            <span class="glyphicon glyphicon-edit"></span> 
                                        </a>
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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">
<!--<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css" />">-->
<script>
    $(function () {
        var path = '${pageContext.request.contextPath}';
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
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
        $('#trainingTypeListTable').DataTable({
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("trainingTypeListTable");
                }
            }
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.trainingType" />&nbsp;<spring:message code="label.list" /> 
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/trainingType/create" class="btn bg-blue">
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
                    <table id="trainingTypeListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.nameBn" /></th>
                                <th><spring:message code="label.nameEn" /></th>                                                                
                                <th><spring:message code="label.description" /></th>
                                <th><spring:message code="label.beneficiaryIncluded" /></th>
                                <th><spring:message code="label.active" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="trainingType" items="${trainingTypeList}">
                                <tr>
                                    <td><c:out value="${trainingType.nameInBangla}"></c:out></td>
                                    <td><c:out value="${trainingType.nameInEnglish}"></c:out></td>
                                    <td><c:out value="${trainingType.description}"></c:out></td>
                                    <td><c:choose>
                                            <c:when test="${trainingType.beneficiaryIncluded}">
                                                <i class="fa fa-check true-icon"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fa fa-times" aria-hidden="true"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:choose>
                                            <c:when test="${trainingType.active}">
                                                <i class="fa fa-check true-icon"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fa fa-times" aria-hidden="true"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>                                    
                                    <td><a href="${contextPath}/trainingType/edit/${trainingType.id}" data-toggle="tooltip" title="${tooltipEdit}">
                                            <span class="glyphicon glyphicon-edit"></span>
                                        </a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>

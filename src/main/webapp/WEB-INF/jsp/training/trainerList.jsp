<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script>
    $(function () {
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
            $('#trainerListTable  tr').not(":first").each(function () {
                $(this).find("td").eq(2).html(getNumberInBangla($(this).find("td").eq(2).html()));
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
        $('#trainerListTable').DataTable({
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("trainerListTable");
                }
            }
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="trainerList.trainerList" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/trainer/create" class="btn bg-blue">
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
                    <table id="trainerListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.nameBn" /></th>
                                <th><spring:message code="label.nameEn" /></th>                                                                
                                <th><spring:message code="trainer.organizationNumber" /></th>                                                                
                                <th><spring:message code="trainer.contactPersonName" /></th>                                                                
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="trainer" items="${trainerList}">
                                <tr>
                                    <td><c:out value="${trainer.nameInBangla}"></c:out></td>
                                    <td><c:out value="${trainer.nameInEnglish}"></c:out></td>
                                    <td><c:out value="${trainer.organizationNumber}"></c:out></td>
                                    <td><c:out value="${trainer.contactPersonName}"></c:out></td>
                                    <td><a href="${contextPath}/trainer/edit/${trainer.id}" data-toggle="tooltip" title="${tooltipEdit}">
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

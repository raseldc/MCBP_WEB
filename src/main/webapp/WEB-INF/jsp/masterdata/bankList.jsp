<%-- 
    Document   : bankList
    Created on : Feb 5, 2017, 3:18:48 PM
    Author     : rezwan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
    $(function () {
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
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
        $('#bankList').DataTable({
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("bankList");
                }

            }
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.bank" />&nbsp;<spring:message code="label.list" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/bank/create" class="btn bg-blue">
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
                    <table id="bankList" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.nameBn" /></th>
                                <th><spring:message code="label.nameEn" /></th>
                                <th><spring:message code="label.address" /></th>
                                <th><spring:message code="label.code" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="bank" items="${bankList}">
                                <tr>
                                    <td><c:out value="${bank.nameInBangla}"></c:out></td>
                                    <td><c:out value="${bank.nameInEnglish}"></c:out></td>
                                    <td><c:out value="${bank.address}"></c:out></td>
                                    <td><c:out value="${bank.code}"></c:out></td>
                                    <td><a href="${contextPath}/bank/edit/${bank.id}" data-toggle="tooltip" title="${tooltipEdit}">
                                            <span class="glyphicon glyphicon-edit"></span></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>


</section>

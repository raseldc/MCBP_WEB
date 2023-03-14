<%-- 
    Document   : districtList
    Created on : Feb 5, 2017, 7:51:13 PM
    Author     : user
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
        
        $('#districtList').DataTable({
            "pageLength": 10,
            "lengthMenu": [10, 25, 50, 100],
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("districtList");
                }

            }
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.district" />&nbsp;<spring:message code="label.list" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/district/create" class="btn bg-blue">
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
                    <table id="districtList" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.division" /></th>
                                <th><spring:message code="label.nameBn" /></th>
                                <th><spring:message code="label.nameEn" /></th>
                                <th><spring:message code="label.code" /></th>
                                <th><spring:message code="label.active" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="district" items="${districtList}">
                                <tr>
                                    <c:if test="${pageContext.response.locale=='en'}"> 
                                        <td><c:out value="${district.divisionNameEn}"></c:out></td>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='bn'}"> 
                                        <td><c:out value="${district.divisionNameBn}"></c:out></td>
                                    </c:if>
                                    <td><c:out value="${district.nameBn}"></c:out></td>
                                    <td><c:out value="${district.nameEn}"></c:out></td>
                                    <td><c:out value="${district.code}"></c:out></td>                                    
                                    <c:if test="${district.active=='true'}">
                                        <td><span class="glyphicon glyphicon-ok"></span></td>
                                        </c:if>
                                        <c:if test="${district.active=='false'}">
                                        <td><span class="glyphicon glyphicon-remove"></span></td>
                                        </c:if>
                                    <td><a href="${contextPath}/district/edit/${district.id}" data-toggle="tooltip" title="${tooltipEdit}">
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

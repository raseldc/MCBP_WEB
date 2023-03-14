<%-- 
    Document   : noticeList
    Created on : Sep 24, 2018, 11:46:49 AM
    Author     : user
--%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script>
    $(function () {
//        alert(getNumberInBangla('10'));
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
        $('#noticeList').DataTable({
            "pageLength": 10,
            "lengthMenu": [10, 25, 50, 100],
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("noticeList");
                }

            }
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.notice" />&nbsp;<spring:message code="label.list" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/notice/create" class="btn bg-blue">
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
                    <table id="noticeList" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.noticeBn" /></th>
                                <th><spring:message code="label.noticeEn" /></th>
                                <th><spring:message code="label.date" /></th>
                                <th><spring:message code="label.active" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="notice" items="${noticeList}">
                                <tr>
                                    <td><c:out value="${notice.noticeBn}"></c:out></td>
                                    <td><c:out value="${notice.noticeEn}"></c:out></td>
                                    <td><fmt:formatDate pattern="dd-MM-yyyy" type="date" value="${notice.noticeDate.time}" /></td>
                                    <c:if test="${notice.active=='true'}">
                                        <td><span class="glyphicon glyphicon-ok"></span></td>
                                    </c:if>
                                    <c:if test="${notice.active=='false'}">
                                        <td><span class="glyphicon glyphicon-remove"></span></td>
                                    </c:if>
                                    <td><a href="${contextPath}/notice/edit/${notice.id}" data-toggle="tooltip" title="${tooltipEdit}">
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


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- DataTables -->
<script src="<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">
<script type="text/javascript">
    $(function () {
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
        }
        $('#ministryList').DataTable({
            "language": {
                "url": urlLang
            },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("ministryList");
                }

            }
        });
    });
</script>
<c:if test="${not empty message.message}">                
    <div class="alert 
         <c:if test="${message.messageType eq 'SUCCESS'}">alert-success</c:if> 
         <c:if test="${message.messageType eq 'INFO'}">alert-info</c:if> 
             alert-dismissable">
             <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
         <c:out value="${message.message}"></c:out>
         </div>
</c:if>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <label><spring:message code="label.ministry" />&nbsp;<spring:message code="label.list" /></label>
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/ministry/create" class="btn bg-blue">
            <i class="fa fa-plus-square"></i>
            <spring:message code="addNew" />  
        </a>
    </div>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title"><label><spring:message code="label.ministry" />&nbsp;<spring:message code="label.list" /></label></h3>
                </div>
                <div class="box-body">
                    <table id="ministryList" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.nameEn" /></th>
                                <th><spring:message code="label.nameBn" /></th>
                                <th><spring:message code="label.code" /></th>
                                <th><spring:message code="label.status" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ministry" items="${ministryList}">
                                <tr>
                                    <td><c:out value="${ministry.nameInEnglish}"></c:out></td>
                                    <td><c:out value="${ministry.nameInBangla}"></c:out></td>
                                    <td><c:out value="${ministry.code}"></c:out></td>
                                    <c:if test="${ministry.active=='true'}">
                                        <td><span class="glyphicon glyphicon-ok"></span></td>
                                        </c:if>
                                        <c:if test="${ministry.active=='false'}">
                                        <td><span class="glyphicon glyphicon-remove"></span></td>
                                        </c:if>
                                    <td>
                                        <a href="${contextPath}/ministry/edit/${ministry.id}" data-toggle="tooltip" title="${tooltipEdit}">
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
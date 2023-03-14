<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- Data Tables -->
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
        $('#schemeAttributeSetupDataList').DataTable({
            "language": {
                    "url": urlLang
                },
            "fnDrawCallback": function (oSettings) {
                if (selectedLocale === 'bn')
                {
                    localizeBanglaInDatatable("schemeAttributeSetupDataList");
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
        <label><spring:message code="label.schemeAttributeSetupData" />&nbsp;<spring:message code="label.list" /></label>
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/schemeAttributeSetupData/create" class="btn bg-blue">
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
                    <h3 class="box-title"><label><spring:message code="label.schemeAttributeSetupData" />&nbsp;<spring:message code="label.list" /></h3>
                </div>
                <div class="box-body">
                    <table id="schemeAttributeSetupDataList" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="label.schemeAttribute" /></th>
                                <th><spring:message code="label.schemeAttributeSetupDataAttributeValueEn" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="schemeAttributeSetupData" items="${schemeAttributeSetupDataList}">
                                <tr>
                                    <c:if test="${pageContext.response.locale=='bn'}">
                                        <td><c:out value="${schemeAttributeSetupData.schemeAttribute.nameInBangla}"></c:out></td>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='en'}">
                                        <td><c:out value="${schemeAttributeSetupData.schemeAttribute.nameInEnglish}"></c:out></td>
                                    </c:if>
                                    <td><c:out value="${schemeAttributeSetupData.attributeValue}"></c:out></td>
                                        <td>
                                            <a href="${contextPath}/schemeAttributeSetupData/edit/${schemeAttributeSetupData.id}" data-toggle="tooltip" title="${tooltipEdit}">
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- Data Tables -->
<script src="<c:url value="/resources/plugins/messageResources/messageResource.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>
<script type="text/javascript" src="<c:url value="/resources/js/utility.js" />"></script>    
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<script type="text/javascript">
    $(function () {
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
        }
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title"><label><spring:message code="label.comment" />&nbsp;<spring:message code="label.list" /></h3>
                </div>
                <div class="box-body">
                    <table id="commentList" class="table table-bordered table-hover">
                        <thead>
                        <th style="display: none"></th>
                        <th style="display: none"></th>
                        </thead>
                        <tbody>
                            <c:forEach var="selectionComments" items="${selectionCommentsList}">
                                <tr>
                                    <td>
                                        <p>
                                            <strong>
                                                <c:if test="${pageContext.response.locale=='bn'}">
                                                    ${selectionComments.changedBy.fullNameBn}
                                                    <c:out value="(${selectionComments.stageType.toStringBangla()})"></c:out>
                                                </c:if>
                                                <c:if test="${pageContext.response.locale=='en'}">
                                                    ${selectionComments.changedBy.fullNameEn}
                                                    <c:out value="${selectionComments.stageType.toString()}"></c:out>
                                                </c:if>
                                            </strong>
                                            <br>
                                            <c:out value="'${selectionComments.comment}'"></c:out>
                                                <br>
                                            <fmt:formatDate value="${selectionComments.changedDate.time}" pattern="dd-MM-yyyy"/>
                                        </p>
                                    </td>
                                    <td>
                                        <c:if test="${selectionComments.commentType.toString()=='Approved'}">
                                            <span class="glyphicon glyphicon-ok"></span>
                                        </c:if>
                                        <c:if test="${selectionComments.commentType.toString()=='Rejected'}">
                                            <span class="glyphicon glyphicon-remove"></span>
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
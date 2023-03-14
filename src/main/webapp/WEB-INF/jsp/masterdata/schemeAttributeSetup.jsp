<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- Data Tables -->
<script src="<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">
<script src="<c:url value="/resources/plugins/messageResources/messageResource.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<style>
    .modal {
        text-align: center;
    }

    @media screen and (min-width: 768px) { 
        .modal:before {
            display: inline-block;
            vertical-align: middle;
            content: " ";
            height: 100%;
        }
    }

    .modal-dialog {
        display: inline-block;
        text-align: left;
        vertical-align: middle;
    }
    .btn-default{
        display: none;
    }
</style>
<script type="text/javascript">
    $(document).ready(function () {
        $(menuSelect("${pageContext.request.contextPath}/schemeAttribute/list"));
        makeUnijoyEditor('schemeAttributeSetupDataAttributeValueBn');
        $("#schemeAttributeSetupDataAttributeValueEn").focus();
        $("#schemeAttributeSetupDataAttributeValueBn").focus();
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#pagemodel-delete-confirmation");
         if ("${message.messageType}" == "SUCCESS")
        {
//            bootbox.alert("<b>${message.message}</b>");
            bootbox.confirm({
                title: '<spring:message code="label.success" />',
                message: "<b>${message.message}</b>",
                buttons: {
                    cancel: {
                        label: "Cancel",
                    },
                    confirm: {
                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                    }
                },
                callback: function (result) {

                }
            });
        } else if ("${message.messageType}" == "DANGER")
        {
//            bootbox.alert("<b>${message.message}</b>");
            bootbox.confirm({
                title: '<spring:message code="label.failure" />',
                message: "<b>${message.message}</b>",

                buttons: {
                    cancel: {
                        label: "Cancel",
                    },
                    confirm: {
                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />',

                    }
                },
                callback: function (result) {

                }
            });
        }
        $("#schemeAttributeSetup").validate({

            rules: {// checks NAME not ID
                "attributeValueInBangla": {
                    required: true,
                    banglaAlphabet: true
                },
                "attributeValueInEnglish": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/schemeAttributeSetupData/create/${schemeAttribute.id}" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/schemeAttributeSetupData/edit/${schemeAttribute.id}/${schemeAttributeSetupData.id}" />
    </c:otherwise>
</c:choose>
<form:form id="schemeAttributeSetup" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="schemeAttributeSetupData">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <label><spring:message code="label.schemeAttributeSetupData" />&nbsp;<spring:message code="label.management" /></label>
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/schemeAttribute/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue" value="save">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>
            <c:if test="${actionType ne 'create'}">
                <span id="page-delete" class="btn bg-red">
                    <i class="fa fa-trash-o"></i>
                    <spring:message code="delete" />
                </span>
            </c:if>
        </div>    
    </section>
    <section class="content">
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="col-md-9 control-label">
                        <c:if test="${pageContext.response.locale=='bn'}">
                            ${schemeAttribute.nameInBangla}
                        </c:if>
                        <c:if test="${pageContext.response.locale=='en'}">
                            ${schemeAttribute.nameInEnglish}
                        </c:if>
                    </label>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div>
                    <div class="form-group">
                        <label for="schemeAttributeSetupDataAttributeValueEn" class="col-md-4 control-label"><spring:message code="label.schemeAttributeSetupDataAttributeValueEn" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.schemeAttributeSetupDataAttributeValueEn' var="schemeAttributeSetupDataAttributeValueEn"/>
                            <form:input class="form-control" placeholder="${schemeAttributeSetupDataAttributeValueEn}" path="attributeValueInEnglish" id="schemeAttributeSetupDataAttributeValueEn" autofocus="autofocus"/>
                            <form:errors path="attributeValueInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="schemeAttributeSetupDataAttributeValueBn" class="col-md-4 control-label"><spring:message code="label.schemeAttributeSetupDataAttributeValueBn" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.schemeAttributeSetupDataAttributeValueBn' var="schemeAttributeSetupDataAttributeValueBn"/>
                            <form:input class="form-control" placeholder="${schemeAttributeSetupDataAttributeValueBn}" path="attributeValueInBangla" id="schemeAttributeSetupDataAttributeValueBn" autofocus="autofocus"/>
                            <form:errors path="attributeValueInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </form:form>
    <div id='pagemodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/schemeAttributeSetupData/delete/${schemeAttributeSetupData.id}" method="post">
                    <div class="form-horizontal">
                        <div class="modal-body">
                            <spring:message code="deleteMessage" />                            
                        </div>
                        <div class="modal-footer">
                            <span class="btn btn-default" data-dismiss="modal"><spring:message code="cancelMessage" /></span>
                            <button type="submit" class="btn btn-primary pull-right">
                                <spring:message code="delete" />
                            </button>
                        </div>
                    </div>
                </form>    
            </div>
        </div>
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
                                <th><spring:message code="label.schemeAttributeSetupDataAttributeValueBn" /></th>
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
                                    <td><c:out value="${schemeAttributeSetupData.attributeValueInBangla}"></c:out></td>
                                    <td><c:out value="${schemeAttributeSetupData.attributeValueInEnglish}"></c:out></td>
                                        <td>
                                            <a href="${contextPath}/schemeAttributeSetupData/attribute/edit/${schemeAttribute.id}/${schemeAttributeSetupData.id}" data-toggle="tooltip" title="${tooltipEdit}">
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





<!-- /.content -->



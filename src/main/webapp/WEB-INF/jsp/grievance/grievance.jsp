<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/css/jquery.datetimepicker.css" />">
<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/grievance/list"));
        initDateTime($("#actionDate"), '${dateTimeFormat}', $("#actionDate\\.icon"));
        $('#grievance-delete').attr("data-toggle", "modal").attr("data-target", "#grievancemodel-delete-confirmation");
        $("#grievanceForm").validate({
            rules: {// checks NAME not ID
                "comment": {
                    required: true                    
                },
                "grievanceStatus.id": {
                    required: true
                },
                "actionDate": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
        if(selectedLocale==='bn')
        {
            makeUnijoyEditor('comment');
        }
        if (selectedLocale == 'en')
        {
            $("#nid").text(${grievance.beneficiary.nid});
        } else
        {
            $("#nid").text(getNumberInBangla('${grievance.beneficiary.nid}'));
        }
    });
</script>
<style>
    .control{
        padding-top: 5px;
    } 
    .readonly{
        color: #DC143C;
    }
</style>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/grievance/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/grievance/edit/${grievance.id}" />
    </c:otherwise>
</c:choose>
<form:form id="grievanceForm" action="${actionUrl}" class="form-horizontal" modelAttribute="grievance">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.grievance" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}${prevUrl}"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>
            <c:if test="${actionType ne 'create'}">
                <span id="grievance-delete" class="btn bg-red">
                    <i class="fa fa-trash-o"></i>
                    <spring:message code="delete" />
                </span>
            </c:if>
        </div>    
    </section>
    <section class="content">        
        <div class="row">
            <div class="col-md-6"> 
                <form:hidden path="id" /> 

                <div class="form-group">
                    <label for="beneficiaryInput" class="col-md-4 control-label"><spring:message code="label.nid" /></label>                        
                    <div class="col-md-8">
                        <div class="control readonly">                               
                            <form:hidden path="beneficiary"/>
                            <strong><em id="nid"></em></strong>                                
                        </div>
                    </div>
                </div>    
                <div class="form-group">
                    <label for="grievanceTypeInput" class="col-md-4 control-label"><spring:message code="label.grievanceType" /></label>
                    <div class="col-md-8"> 
                        <div class="control readonly">  
                            <form:hidden path="grievanceType.id"/>
                            <strong><em>
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            ${grievance.grievanceType.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${grievance.grievanceType.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </em></strong>                                
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="description" class="col-md-4 control-label"><spring:message code="grievance.description" /></label>
                    <div class="col-md-8">
                        <div class="control readonly">
                            <form:hidden path="description"/>
                            <strong><em>${grievance.description}</em></strong>   
                        </div>  
                    </div>
                </div>
                <div class="form-group">
                    <label for="comment" class="col-md-4 control-label"><spring:message code="label.comment" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code="label.comment" var="commentPlaceholder"/>
                        <form:textarea path="comment" class="form-control" placeholder="${commentPlaceholder}" rows="3"/>
                        <form:errors path="comment" cssClass="error"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="grievanceStatus" class="col-md-4 control-label"><spring:message code="grievance.grievanceStatus" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <form:select class="form-control" path="grievanceStatus.id">
                            <spring:message code='label.select' var="select"/>
                            <form:option value="" label="${select}"></form:option>
                            <form:options items="${grievanceStatusList}" itemValue="id" itemLabel="${grievanceStatusName}"></form:options>
                        </form:select>
                        <form:errors path="grievanceStatus.id" cssClass="error"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="actionDate" class="col-md-4 control-label"><spring:message code="grievance.actionDate" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">                            
                        <form:input class="form-control" path="actionDate"/>
                        <form:errors path="actionDate" cssStyle="color:red"></form:errors>
                        </div>            
                    </div>  

            </form:form>
        </div>
    </div>
    <div id='grievancemodel-delete-confirmation' class="modal fade"  tabindex="-1" grievance="dialog" aria-labelledby="grievancemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="grievancemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/grievance/delete/${grievance.id}" method="post">
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

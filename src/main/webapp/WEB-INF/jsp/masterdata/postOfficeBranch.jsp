<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/postOfficeBranch/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#postOfficeBranchmodel-delete-confirmation");

        makeUnijoyEditor('postOfficeBranchNameInputBangla');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('postOfficeBranchCodeInput');
            makeUnijoyEditor('postOfficeBranchAddressInput');
            $('#postOfficeBranchCodeInput').on("keydown", function (event) {
                checkNumberWithLength(event, this, 4);
            });
            $("#postOfficeBranchCodeInput").val(getNumberInBangla("${postOfficeBranch.code}"));
        }
        $("#postOfficeBranchNameInputEnglish").focus();
        $("#postOfficeBranchNameInputBangla").focus();
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $("#postOfficeBranchForm").validate({
            rules: {// checks NAME not ID
                "district.id": {
                    required: true
                },
                "nameInEnglish": {
                    required: true
                },
                "nameInBangla": {
                    required: true
                },
                "code": {
                    required: true
                },
                "address": {
                    required: true
                },
                "routingNumber": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });
//    function loadPresentUpazilaList(selectObject) {
//        var distId = selectObject.value;
//        var upazillaSelectId = $('#upazilaId');
//        if (distId !== '') {
//            loadUpazilla(distId, upazillaSelectId);
//        } else {
//            resetSelect(upazillaSelectId);
//            resetSelect($('#unionId'));
//        }
//    }
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/postOfficeBranch/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/postOfficeBranch/edit/${postOfficeBranch.id}" />
    </c:otherwise>
</c:choose>

<form:form id="postOfficeBranchForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="postOfficeBranch">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.postOfficeBranch" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/postOfficeBranch/list"><spring:message code="label.backToList" /></a></small>
        </h1>
        <div class="pull-right">
            <button type="submit" name="save" class="btn bg-blue">
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
                <form:hidden path="id" />
                <div>
                    <div class="form-group">
                        <label for="districtInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="district.id" id="districtId" >
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${districtList}" itemValue="id" itemLabel="${districtName}"></form:options>
                            </form:select>
                            <form:errors path="district.id" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="postOfficeBranchNameInputBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="postOfficeBranchNameInputBangla"/>
                            <form:input class="form-control" placeholder="${postOfficeBranchNameInputBangla}" path="nameInBangla" id="postOfficeBranchNameInputBangla" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group">
                            <label for="postOfficeBranchNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="postOfficeBranchNameInputEnglish"/>
                            <form:input class="form-control" placeholder="${postOfficeBranchNameInputEnglish}" path="nameInEnglish" id="postOfficeBranchNameInputEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="postOfficeBranchCodeInput"/>
                            <form:input class="form-control" placeholder="${postOfficeBranchCodeInput}" path="code" id="postOfficeBranchCodeInput" onkeydown="checkNumberWithLength(event, this, 4)"/>
                            <form:errors path="code" cssclass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="branchroutingNumberInput" class="col-md-4 control-label"><spring:message code="label.routingNumber" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.routingNumber' var="branchroutingNumberInput"/>
                            <form:input class="form-control" placeholder="${routingNumberInput}" path="routingNumber" id="routingNumberInput" onkeydown="checkNumber(event, this)"/>
                            <form:errors path="routingNumber" cssclass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="postOfficeBranchAddressInput" class="col-md-4 control-label"><spring:message code="label.address" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.address' var="postOfficeBranchAddressInput"/>
                            <form:textarea class="form-control" placeholder="${postOfficeBranchAddressInput}" path="address" id="postOfficeBranchAddressInput" autofocus="autofocus"/>
                            <form:errors path="address" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="active" class="col-md-4 control-label"><spring:message code="label.active" /></label>
                        <div>
                            <div class="col-md-5">
                                <div class="checkbox icheck">
                                    <label>
                                        <form:checkbox path="active" id="active" />
                                    </label>
                                </div>                        
                            </div>
                        </div>   
                    </div>    
                </div>  
            </form:form>
        </div>
    </div>
    <div id='postOfficeBranchmodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/postOfficeBranch/delete/${postOfficeBranch.id}" method="post">
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




<!-- /.content -->



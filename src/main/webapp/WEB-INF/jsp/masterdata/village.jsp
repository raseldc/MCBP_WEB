<%-- 
    Document   : branch
    Created on : Jan 22, 2017, 3:40:06 PM
    Author     : user
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/village/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#branchmodel-delete-confirmation");
        makeUnijoyEditor('nameInBangla');
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('code');
            $('#code').on("keydown", function (event) {
                checkNumberWithLength(event, this, 4);
            });
            $("#code").val(getNumberInBangla("${village.code}"));
        }
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        $("#branchForm").validate({
            rules: {// checks NAME not ID
                "upazila.id": {
                    required: true
                },
                "union.id": {
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
                "wardNo": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
        if ('${actionType}' !== 'create')
        {
            loadUnionList($('#upazila')[0]);
        }
    });

    function loadPresentDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#districtId');
        if (divId !== '') {
            loadDistrict(divId, distSelectId);
        } else {
            resetSelect(distSelectId);
            resetSelect($('#upazilaId'));
            resetSelect($('#unionId'));
        }
    }
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#upazilaId');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId);
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#unionId'));
        }
    }
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#unionId');

        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId);
        } else {
            resetSelect(unionSelectId);
        }
    }
    function loadUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#union');
        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId, '${village.union.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/village/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/village/edit/${village.id}" />
    </c:otherwise>
</c:choose>

<form:form id="branchForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="village">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.village" />&nbsp;<spring:message code="label.management" />
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/village/list"><spring:message code="label.backToList" /></a></small>
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
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <c:choose>
                                <c:when test="${isEdit eq '1'}">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${village.division.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${village.division.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="division.id" id="divisionId" onchange="loadPresentDistrictList(this)">
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options> 
                                    </form:select>
                                    <form:errors path="division.id" cssClass="error"></form:errors>
                                </c:otherwise>
                            </c:choose>
                        </div>

                    </div>
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <c:choose>
                                <c:when test="${isEdit eq '1'}">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${village.district.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${village.district.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="district.id" id="districtId" onchange="loadPresentUpazilaList(this)" >
                                        <form:option value="" label="${select}"></form:option>
                                    </form:select>

                                    <form:errors path="district.id" cssClass="error"></form:errors>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="upazillaInput" class="col-md-4 control-label"><spring:message code="label.upazila" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <c:choose>
                                <c:when test="${isEdit eq '1'}">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${village.upazila.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${village.upazila.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="upazila.id" id="upazilaId" onchange="loadPresentUnionList(this)">
                                        <form:option value="" label="${select}"></form:option>
                                    </form:select>

                                </c:otherwise>
                            </c:choose>

                            <form:errors path="upazila.id" cssClass="error"></form:errors>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="unionInput" class="col-md-4 control-label"><spring:message code="label.union" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <c:choose>
                                <c:when test="${isEdit eq '1'}">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            <label path="union.id"> ${village.union.nameInBangla}</label>
                                        </c:when>
                                        <c:otherwise>
                                              <label path="union.id"> ${village.union.nameInEnglish}</label>
                                            
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="union.id" id="unionId" >
                                        <form:option value="" label="${select}"></form:option>
                                    </form:select>

                                </c:otherwise>
                            </c:choose>

                            <form:errors path="union.id" cssClass="error"></form:errors>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="wardNo" class="col-md-4 control-label"><spring:message code="label.wardNo" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                              <form:select class="form-control" path="wardNo">
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${wardNoList}"></form:options> 
                                    </form:select>
                            <%--<c:choose>
                                <c:when test="${isEdit eq '1'}">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${village.wardNo}
                                        </c:when>
                                        <c:otherwise>
                                            ${village.wardNo}
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <form:select class="form-control" path="wardNo">
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${wardNoList}"></form:options> 
                                    </form:select>

                                </c:otherwise>
                            </c:choose>--%>

                            <form:errors path="wardNo" cssStyle="color:red"></form:errors>
                            </div>
                        </div>  
                        <div class="form-group">
                            <label for="nameInBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="nameBangla"/>
                            <form:input class="form-control" placeholder="${nameBangla}" path="nameInBangla"/>
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group">
                            <label for="nameInEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="nameEnglish"/>
                            <form:input class="form-control" placeholder="${nameEnglish}" path="nameInEnglish" />
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-md-4 control-label"><spring:message code="label.code" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.code' var="code"/>
                            <form:input class="form-control" placeholder="${code}" path="code" onkeydown="checkNumberWithLength(event, this, 4)"/>
                            <form:errors path="code" cssclass="error"></form:errors>
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
    <div id='branchmodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
                </div>
                <form action="${contextPath}/village/delete/${village.id}" method="post">
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



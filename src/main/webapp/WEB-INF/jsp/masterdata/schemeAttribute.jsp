<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- Data Tables -->
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script type="text/javascript" src="<c:url value="/resources/js/utility.js" />"></script> 
<script type="text/javascript">
    $(document).ready(function () {
        $(menuSelect("${pageContext.request.contextPath}/schemeAttribute/list"));
        makeUnijoyEditor('schemeAttributeNameInputBangla');
        $("#schemeAttributeNameInputEnglish").focus();
        $("#schemeAttributeNameInputBangla").focus();
        if (selectedLocale === "bn")
        {
            makeUnijoyEditor('schemeAttributeViewOrder');
            makeUnijoyEditor('schemeAttributeSelectionCriteriaPriority');
            $("#schemeAttributeViewOrder").val(getNumberInBangla("" + $("#schemeAttributeViewOrder").val() + ""));
            $("#schemeAttributeSelectionCriteriaPriority").val(getNumberInBangla("" + $("#schemeAttributeSelectionCriteriaPriority").val() + ""));
        }
        var schemeAttributeViewOrder = document.getElementById("schemeAttributeViewOrder");
        schemeAttributeViewOrder.addEventListener("keydown", function (event) {
            checkDecimalWithLength(event, this, 100);
        });
        var schemeAttributeSelectionCriteriaPriority = document.getElementById("schemeAttributeSelectionCriteriaPriority");
        schemeAttributeSelectionCriteriaPriority.addEventListener("keydown", function (event) {
            checkDecimalWithLength(event, this, 100);
        });
        $("#schemeAttributeForm").validate({

            rules: {// checks NAME not ID
                "nameInEnglish": {
                    required: true,
                    englishAlphabet: true
                },
                "nameInBangla": {
                    required: true,
                    banglaAlphabet: true
                },
                "attributeType": {
                    required: true
                },
                "viewOrder": {
                    required: true
                },
                "orderingType": {
                    required: true
                },
                "selectionCriteriaPriority": {
                    required: true
                },
                "comparisonType": {
                    required: true
                },
                "comparedValue": {
                    required: true
                },
                "scheme.id": {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element);
            }
        });
    });
    $(function () {
        $(menuSelect("/lm-mis/schemeAttribute/list"));
        $('#page-delete').attr("data-toggle", "modal").attr("data-target", "#pagemodel-delete-confirmation");
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });
    });
</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/schemeAttribute/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/schemeAttribute/edit/${schemeAttribute.id}" />
    </c:otherwise>
</c:choose>
<form:form id="schemeAttributeForm" action="${actionUrl}" method="post" class="form-horizontal" role="form" modelAttribute="schemeAttribute">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <label><spring:message code="label.schemeAttribute" />&nbsp;<spring:message code="label.management" /></label>
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
        <c:if test="${not empty message}">
            <div class="message green">${message}</div>
        </c:if>
        <div class="row">
            <div class="col-md-6">
                <form:hidden path="id" />
                <div>
                    <div class="form-group">
                        <label for="schemeInput" class="col-md-4 control-label"><spring:message code="label.scheme" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.scheme' var="schemeLabel"/>
                            <form:select class="form-control" path="scheme.id">
                                <form:option value="" label="${schemeLabel}"></form:option>
                                <form:options items="${schemeList}" itemValue="id" itemLabel="${schemeName}"></form:options>
                            </form:select>
                            <form:errors path="scheme.id" cssClass="error"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="schemeAttributeNameInputEnglish" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameEn' var="schemeAttributeNameInputEnglish"/>
                            <form:input class="form-control" placeholder="${schemeAttributeNameInputEnglish}" path="nameInEnglish" id="schemeAttributeNameInputEnglish" autofocus="autofocus"/>
                            <form:errors path="nameInEnglish" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="schemeAttributeNameInputBangla" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.nameBn' var="schemeAttributeNameInputBangla"/>
                            <form:input class="form-control" placeholder="${schemeAttributeNameInputBangla}" path="nameInBangla" id="schemeAttributeNameInputBangla" autofocus="autofocus"/>
                            <form:errors path="nameInBangla" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="schemeAttributeType" class="col-md-4 control-label"><spring:message code="label.schemeAttributeType" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.select' var="select"/>
                        <spring:message code='label.schemeAttributeType' var="schemeAttributeType"/>
                        <form:select class="form-control" placeholder="${schemeAttributeType}" path="attributeType" id="schemeAttributeType" autofocus="autofocus">
                            <form:option value="" label="${select}" />
                            <c:forEach items="${attributeTypeList}" var="attributeType">
                                <c:if test="${pageContext.response.locale=='en'}">   
                                    <form:option value="${attributeType}" label="${attributeType.displayName}">${attributeType.displayName}</form:option>
                                </c:if>
                                <c:if test="${pageContext.response.locale=='bn'}">   
                                    <form:option value="${attributeType}" label="${attributeType.displayNameBn}">${attributeType.displayNameBn}</form:option>
                                </c:if>
                            </c:forEach>
                        </form:select>
                        <form:errors path="attributeType" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="schemeAttributeIsMandatoryForSelectionCriteria" class="col-md-4 control-label"><spring:message code="label.schemeAttributeIsMandatoryForSelectionCriteria" /></label>
                    <div>
                        <div class="col-md-5">
                            <div class="checkbox icheck">
                                <label>
                                    <form:checkbox path="isMandatoryForSelectionCriteria" id="isMandatoryForSelectionCriteria" />
                                </label>
                            </div>                        
                        </div>
                    </div>  
                </div>
                <div class="form-group">
                    <label for="schemeAttributeViewOrder" class="col-md-4 control-label"><spring:message code="label.schemeAttributeViewOrder" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.schemeAttributeViewOrder' var="schemeAttributeViewOrder"/>
                        <form:input class="form-control" placeholder="${schemeAttributeViewOrder}" path="viewOrder" id="schemeAttributeViewOrder" autofocus="autofocus"/>
                        <form:errors path="viewOrder" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="schemeAttributeOrderingType" class="col-md-4 control-label"><spring:message code="label.schemeAttributeOrderingType" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.select' var="select"/>
                        <spring:message code='label.schemeAttributeOrderingType' var="schemeAttributeOrderingType"/>
                        <form:select class="form-control" placeholder="${schemeAttributeOrderingType}" path="orderingType" id="schemeAttributeOrderingType" autofocus="autofocus">
                            <form:option value="" label="${select}" />
                            <c:forEach items="${orderingTypeList}" var="orderingType">
                                <c:if test="${pageContext.response.locale=='en'}">   
                                    <form:option value="${orderingType}" label="${orderingType.displayName}">${orderingType.displayName}</form:option>
                                </c:if>
                                <c:if test="${pageContext.response.locale=='bn'}">   
                                    <form:option value="${orderingType}" label="${orderingType.displayNameBn}">${orderingType.displayNameBn}</form:option>
                                </c:if>
                            </c:forEach>
                        </form:select>
                        <form:errors path="orderingType" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="schemeAttributeSelectionCriteriaPriority" class="col-md-4 control-label"><spring:message code="label.schemeAttributeSelectionCriteriaPriority" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.schemeAttributeSelectionCriteriaPriority' var="schemeAttributeSelectionCriteriaPriority"/>
                        <form:input class="form-control" placeholder="${schemeAttributeSelectionCriteriaPriority}" path="selectionCriteriaPriority" id="schemeAttributeSelectionCriteriaPriority" autofocus="autofocus"/>
                        <form:errors path="selectionCriteriaPriority" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="schemeAttributeComparisonType" class="col-md-4 control-label"><spring:message code="label.schemeAttributeComparisonType" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.select' var="select"/>
                        <spring:message code='label.schemeAttributeComparisonType' var="schemeAttributeComparisonType"/>
                        <form:select class="form-control" placeholder="${schemeAttributeComparisonType}" path="comparisonType" id="schemeAttributeComparisonType" autofocus="autofocus">
                            <form:option value="" label="${select}" />
                            <c:forEach items="${comparisonTypeList}" var="comparisonType">
                                <form:option value="${comparisonType}" label="${comparisonType.toString()}">${comparisonType.toString()}</form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors path="comparisonType" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="schemeAttributeComparedValue" class="col-md-4 control-label"><spring:message code="label.schemeAttributeComparedValue" /><span class="mandatory">*</span></label>
                    <div class="col-md-8">
                        <spring:message code='label.schemeAttributeComparedValue' var="schemeAttributeComparedValue"/>
                        <form:input class="form-control" placeholder="${schemeAttributeComparedValue}" path="comparedValue" id="schemeAttributeComparedValue" autofocus="autofocus"/>
                        <form:errors path="comparedValue" cssStyle="color:red"></form:errors>
                        </div>
                    </div>
                </div>    
            </div>
    </form:form>
</div>
</div>
<div id='pagemodel-delete-confirmation' class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="pagemodel-delete-confirmation-title">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="pagemodel-delete-confirmation-title"><spring:message code="areYouSure?" /></h4>
            </div>
            <form action="${contextPath}/schemeAttribute/delete/${schemeAttribute.id}" method="post">
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



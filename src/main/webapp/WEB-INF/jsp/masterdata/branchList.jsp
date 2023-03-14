<%-- 
    Document   : branchList
    Created on : Feb 22, 2017, 12:53:07 PM
    Author     : user
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>  
    table.dataTable thead .sorting_asc:after{
        content:"";
    } 
</style>
<script>
    $(function () {
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
        }
        if (selectedLocale === 'bn') {
            makeUnijoyEditor('routingNumberInput');
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
        $("#buttonSearch").click(function ()
        {
            $('#branchListTable').DataTable().destroy();
            $('#branchListTable').DataTable({
                "processing": true,
                "pageLength": 10,
                "bSort": false,
                "serverSide": true,
                "pagingType": "full_numbers",
                "language": {
                    "url": urlLang
                },
                "columnDefs": [
                    {"width": 150, "targets": [0,6]},
                    {"width": 20, "targets": [7]}
                ],
                "ajax": {
                    "url": path + "/branch/list",
                    "type": "POST",
                    "data": {"bankId": $('#bankId').val(),
                        "districtId": $('#districtId').val(),
                        "routingNumber": $('#routingNumberInput').val()
                    }
                },
                "fnDrawCallback": function (oSettings) {
                    if (selectedLocale === 'bn')
                    {
                        localizeBanglaInDatatable("branchListTable");
                    }
                }
            });
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.branch" />&nbsp;<spring:message code="label.list" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/branch/create" class="btn bg-blue">
            <i class="fa fa-plus-square"></i>
            <spring:message code="addNew" />
        </a>
    </div>

</section>
<section class="content"> 
    <div class="row">
        <div class="col-md-6 form-horizontal">
            <div class="form-group">
                <label for="bankNameInput" class="col-md-4 control-label"><spring:message code="label.bank" /></label>
                <div class="col-md-8">
                    <spring:message code='label.select' var="select"/>
                    <select class="form-control" id="bankId" > 
                        <option value="" label="${select}"></option>
                        <c:if test="${pageContext.response.locale=='en'}"> 
                            <c:forEach items="${bankList}" var="bank">
                                <option value="${bank.id}">${bank.nameInEnglish}</option>
                            </c:forEach>
                        </c:if>
                        <c:if test="${pageContext.response.locale=='bn'}">
                            <c:forEach items="${bankList}" var="bank">
                                <option value="${bank.id}">${bank.nameInBangla}</option>
                            </c:forEach>
                        </c:if>
                    </select> 
                </div>
            </div>
            <div class="form-group">
                <label for="districtInput" class="col-md-4 control-label"><spring:message code="label.district" /></label>
                <div class="col-md-8">
                    <spring:message code='label.select' var="select"/>
                    <select class="form-control" id="districtId"> 
                        <option value="" label="${select}"></option>
                        <c:if test="${pageContext.response.locale=='en'}"> 
                            <c:forEach items="${districtList}" var="district">
                                <option value="${district.id}">${district.nameInEnglish}</option>
                            </c:forEach>
                        </c:if>
                        <c:if test="${pageContext.response.locale=='bn'}">
                            <c:forEach items="${districtList}" var="district">
                                <option value="${district.id}">${district.nameInBangla}</option>
                            </c:forEach>
                        </c:if> 
                    </select> 
                </div>
            </div>
            <div class="form-group">
                <label for="branchroutingNumberInput" class="col-md-4 control-label"><spring:message code="label.routingNumber" /></label>
                <div class="col-md-8">
                    <spring:message code='label.routingNumber' var="branchroutingNumberInput"/>
                    <input class="form-control" placeholder="${routingNumberInput}"  id="routingNumberInput" onkeydown="checkNumber(event, this)"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-4 control-label"></label>
                <div class="col-md-8">
                    <button id="buttonSearch" type="button" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"></spring:message></button>        
                    </div>
                </div>
            </div>
            <div class="col-xs-12">
                <div class="box">
                    <div class="box-body">
                        <table id="branchListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th><spring:message code="label.bank" /></th>
                                <th><spring:message code="label.nameBn" /></th>
                                <th><spring:message code="label.nameEn" /></th>
                                <th><spring:message code="label.code" /></th>
                                <th><spring:message code="label.district" /></th>
                                <th><spring:message code="label.routingNumber" /></th>
                                <th><spring:message code="label.address" /></th>
                                <th><spring:message code="label.active" /></th>
                                <th><spring:message code="edit" var="tooltipEdit"/></th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>


</section>


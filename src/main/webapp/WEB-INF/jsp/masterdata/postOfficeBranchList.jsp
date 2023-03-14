<%-- 
    Document   : postOfficeBranchList
    Created on : Feb 25, 2018, 6:51:44 PM
    Author     : user
--%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

        $("#buttonSearch").click(function ()
        {
            $('#postOfficeBranchList').DataTable().destroy();
            $('#postOfficeBranchList').DataTable({
                "processing": true,
                "pageLength": 10,
                "bFilter": false,
                "serverSide": true,
                "pagingType": "full_numbers",
                "lengthMenu": [10, 25, 50, 100],
                "language": {
                    "url": urlLang
                },
                "ajax": {
                    "url": path + "/postOfficeBranch/list",
                    "type": "POST",
                    "data": {
                        "districtId": $('#districtId').val(),
                        "routingNumber": $('#routingNumberInput').val()
                    }
                },
                "fnDrawCallback": function (oSettings) {
                    if (selectedLocale === 'bn')
                    {
                        localizeBanglaInDatatable("postOfficeBranchList");
                    }

                }
            });
        });
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.postOfficeBranch" />&nbsp;<spring:message code="label.list" />
        <small></small>
    </h1>
    <div class="pull-right">
        <a href="${contextPath}/postOfficeBranch/create" class="btn bg-blue">
            <i class="fa fa-plus-square"></i>
            <spring:message code="addNew" />
        </a>
    </div>

</section>
<section class="content">  
    <div class="row">
        <div class="col-md-6 form-horizontal">
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
                <label class="col-md-4 control-label"></label>
                <div class="col-md-8">
                    <button id="buttonSearch" type="button" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"></spring:message></button>        
                    </div>
                </div>
            </div>
            <div class="col-xs-12">
                <div class="box">
                    <div class="box-body">
                        <table id="postOfficeBranchList" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                <th><spring:message code="label.nameBn" /></th>
                                <th><spring:message code="label.nameEn" /></th>
                                <th><spring:message code="label.district" /></th>
                                <th><spring:message code="label.address" /></th>
                                <th><spring:message code="label.code" /></th>
                                <th><spring:message code="label.routingNumber" /></th>
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


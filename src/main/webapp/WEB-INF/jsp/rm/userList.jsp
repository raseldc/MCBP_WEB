<%-- 
    Document   : pageList
    Created on : Feb 5, 2017, 10:18:00 AM
    Author     : Philip
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<style>    
    #userListTable_length{
        float:left;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    } 
</style>
<script>
    $(function () {
        $('#userListTable').show();
        $('#userListTableNotAdmin').hide();
        var path = '${pageContext.request.contextPath}';
        $("#userStatus").val("ACTIVE");
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
        }
        $('#userID').keypress(function (e) {
            if (e.which === 13) {
                $("#buttonSearch").click();
                return false;
            }
        });
        $("#buttonSearch").click(function ()
        {
            var form = $('#userListForm');
            form.validate();
            if (form.valid()) {
                saveFormToCookie(form);
                var serializedData = $("#userListForm").serialize();
                console.log(serializedData);
                $('#userListTable').DataTable().destroy();
                var path = '${pageContext.request.contextPath}';
                if ($("#inputUserType").val() == "2")
                {
                    $('#userListTable').hide();
                    $('#userListTableNotAdmin').show();
                    $('#userListTableNotAdmin').DataTable({
                        "processing": true,
                        responsive: true,
                        "pageLength": 10,
                        "serverSide": true,
                        "bSort": false,
                        "pagingType": "full_numbers",
                        "dom": '<"top"i>rt<"bottom"lp><"clear">',
                        "language": {
                            "url": urlLang
                        },
                        columnDefs: [
                            {width: '15%', targets: 0},
                            {width: '10%', targets: 1}
                        ],
                        "ajax": {
                            "url": path + "/user/list",
                            "type": "POST",
                            "data": {
                                "userID": $("#userID").val(),
                                "userType": $("#userTypeId").val(),
                                "userStatus": $("#userStatus").val(),
                                "scheme": $("#schemeId").val()
                            }
                        },
                        "fnDrawCallback": function (oSettings) {
                            if (selectedLocale === 'bn')
                            {
                                localizeBanglaInDatatable("userListTable");
                            }
                        }
                    });

                } else {
                    $('#userListTable').show();
                    $('#userListTableNotAdmin').hide();
                    $('#userListTable').DataTable({
                        "processing": true,
                        responsive: true,
                        "pageLength": 10,
                        "serverSide": true,
                        "bSort": false,
                        "pagingType": "full_numbers",
                        "dom": '<"top"i>rt<"bottom"lp><"clear">',
                        "language": {
                            "url": urlLang
                        },
                        columnDefs: [
                            {width: '15%', targets: 0},
                            {width: '10%', targets: 1}
                        ],
                        "ajax": {
                            "url": path + "/user/list",
                            "type": "POST",
                            "data": {
                                "userID": $("#userID").val(),
                                "userType": $("#userTypeId").val(),
                                "userStatus": $("#userStatus").val(),
                                "scheme": $("#schemeId").val()
                            }
                        },
                        "fnDrawCallback": function (oSettings) {
                            if (selectedLocale === 'bn')
                            {
                                localizeBanglaInDatatable("userListTable");
                            }
                        }
                    });
                }
            }
        });
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
            loadFormFromCookie($('#userListForm'));
        }
    });
    function resetPassword(userId)
    {
        var msg;

        if ("${pageContext.response.locale}" === 'bn') {
            msg = "পাসওয়ার্ড সফল ভাবে পরিবর্তন  করা হয়েছে। নতুন পাসওয়ার্ডঃ 123456।"
        } else
        {
            msg = "Password Reset Successfully. New Password: 123456.";
        }
        $.ajax({
            type: "GET",
            url: contextPath + "/user/reset-password/" + userId,
            async: true,
            success: function (response) {


                bootbox.dialog({
                    onEscape: function () {},
                    title: '<spring:message code="label.success" />',
                    message: msg,
                    buttons: {
                        ok: {
                            label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                        }
                    },
                    callback: function (result) {
                    }
                });

            },
            failure: function () {

                return "error in loading data";
            }
        });
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="userList.userList" />
        <small></small>
    </h1>
    <div class="pull-right">
        <input type="hidden" id="inputUserType" value="${userType}"/>
        <c:choose>
            <c:when test="${userType eq '2'}">

            </c:when>
            <c:otherwise>
                <a href="${contextPath}/user/create" class="btn bg-blue">
                    <i class="fa fa-plus-square"></i>
                    <spring:message code="addNew" />
                </a>
            </c:otherwise>
        </c:choose>

    </div>
</section>
<section class="content"> 
    <div class="row">
        <div class="col-md-12">            
            <form:form id="userListForm" class="form-horizontal" modelAttribute="userSearchParameterForm">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="userID" class="col-md-4 control-label"><spring:message code='label.userID' /></label>                                
                        <div class="col-md-8">
                            <spring:message code="label.userID" var="userID"/>
                            <input type="text" id="userID" placeholder="${userID}" name="userID" class="form-control" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="schemeInput" class="col-md-4 control-label"><spring:message code="label.scheme" /></label>
                        <div class="col-md-8">
                            <SELECT class="form-control" id="schemeId" name="schemeId">
                                <spring:message code='label.select' var="select"/>
                                <option value="">${select}</option>
                                <c:forEach items="${schemeList}" var="entry">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            <option value="${entry.id}">${entry.nameInBangla}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${entry.id}">${entry.nameInEnglish}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </SELECT>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userType" class="col-md-4 control-label"><spring:message code="label.userType" /></label>
                        <div class="col-md-8">
                            <SELECT class="form-control" id="userTypeId" name="userTypeId">
                                <spring:message code='label.select' var="select"/>
                                <option value="">${select}</option>
                                <c:forEach items="${userTypeList}" var="entry">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            <option value="${entry}">${entry.displayNameBn}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${entry}">${entry.displayName}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </SELECT>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="userStatus" class="col-md-4 control-label"><spring:message code="label.status" /></label>
                        <div class="col-md-8">
                            <SELECT class="form-control" id="userStatus" name="userStatus">
                                <spring:message code='label.select' var="select"/>
                                <option value="">${select}</option>
                                <c:forEach items="${userStatusList}" var="entry">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            <option value="${entry}">${entry.displayNameBn}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${entry}">${entry.displayName}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </SELECT>
                        </div>
                    </div>   
                    <div class="form-group">
                        <label for="userStatus" class="col-md-4 control-label"><spring:message code="user.role" /></label>
                        <div class="col-md-8">
                            <SELECT class="form-control" id="userStatus" name="userStatus">
                                <spring:message code='label.select' var="select"/>
                                <option value="">${select}</option>
                                <c:forEach items="${userStatusList}" var="entry">
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            <option value="${entry}">${entry.displayNameBn}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${entry}">${entry.displayName}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </SELECT>
                        </div>
                    </div> 
                    <div class="form-group">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-8 text-left">
                            <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
    <div class="row"> 
        <div class="col-md-12">
            <div class="box">                
                <div class="box-body">
                    <table id="userListTable" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="user.fullName" /></th>
                                <th><spring:message code="user.designation" /></th>
                                <th><spring:message code="label.userID" /></th>                                
                                <!--<th><spring:message code="user.role" /></th>-->
                                <th><spring:message code="user.mobileNo" /></th>
                                <th><spring:message code="label.userType" /></th> 
                                <!--<th><spring:message code="user.userOffice" /></th>-->                                
                                <th><spring:message code="user.active?" /></th>
                                        <!--<th><spring:message code="label.action" /></th>-->
                                <th></th>
                                <th></th>
                            </tr>
                        </thead>                        
                    </table>

                    <table id="userListTableNotAdmin" class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th><spring:message code="user.fullName" /></th>
                                <th><spring:message code="user.designation" /></th>
                                <th><spring:message code="label.userID" /></th>                                
                                <!--<th><spring:message code="user.role" /></th>-->
                                <th><spring:message code="user.mobileNo" /></th>
                                <th><spring:message code="label.userType" /></th> 
                                <!--<th><spring:message code="user.userOffice" /></th>-->                                
                                <th><spring:message code="user.active?" /></th>
                                        <!--<th><spring:message code="label.action" /></th>-->
                                <th></th>

                            </tr>
                        </thead>                        
                    </table>
                </div>
            </div>
        </div>
    </div>

</section>

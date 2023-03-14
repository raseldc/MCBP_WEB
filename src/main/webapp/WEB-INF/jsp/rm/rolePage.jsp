<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript">

    var allPageIds = [
    <c:forEach var="id" items="${allPageIds}">
        ${id},
    </c:forEach>
    ];

    $(function () {
        $("#role").change(function () {

            var id = $('option:selected', this).val();
            if (!id) {
                return;
            }
            //flushing previous selection
            for (var i = 0; i < allPageIds.length; i++) {
//                $("#ch" + allPageIds[i]).prop('checked', false); // It works when icheck not used
                $("#ch" + allPageIds[i]).iCheck('uncheck'); // works when icheck is enabled
            }
            getPageByRole(id);
        });
        $('#role').trigger('change');
    });

    function getPageByRole(roleId) {
        $.ajax({
            url: "${pageContext.request.contextPath}/pageIdsByRole/"
                    + roleId,
            type: 'GET',
            dataType: 'json',
            success: function (pageIds) {
                for (var i = 0; i < pageIds.length; i++) {
//                    $("#ch".concat(pageIds[i])).prop('checked', true);
                    $("#ch".concat(pageIds[i])).iCheck('check');
                }
            },
            error: function (data, status, er) {
                console.log(data);
            }
        });
    }

    function savePageRole()
    {
        var selectedIds = getSelectedIds();

        var roleId = $('#role option:selected').val();
        json = {"roleId": roleId, "pageIds": selectedIds};

        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url: "${pageContext.request.contextPath}/savePagesByRole",
            data: JSON.stringify(json),
            type: 'POST',
            dataType: 'html',
            success: function (data) {
                console.log(data);
                bootbox.dialog({
                    onEscape: function () {},
                    title: '<spring:message code="label.success" />',
                    message: "<b>" + data + "</b>",
                    buttons: {
                        ok: {
                            label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                        }
                    },

                    callback: function (result) {
                    }
                });

            },
            error: function (data, status, er) {
                console.log(data);
                alert("error: " + data + " status: " + status + " er:" + er);
            }
        });

    }

    function getSelectedIds()
    {
        var selectedIds = new Array();
        var i, j = 0;
        for (i = 0; i < allPageIds.length; i++)
        {
            if ($('#ch' + allPageIds[i]).is(":checked"))
            {
                selectedIds[j++] = allPageIds[i];
            }
        }
        return selectedIds;
    }
    function loadRoleList(selectObject)
    {
        var schemeId = selectObject.value;
        var roleSelectId = $('#role');
        if (schemeId !== '') {
            loadRoleByScheme(schemeId, roleSelectId);
        } else {
            //resetSelect(distSelectId);
        }
    }
    function loadRoleByScheme(schemeId, roleSelectId)
    {
        $.ajax({
            type: "GET",
            url: contextPath + "/getRoleByScheme/" + schemeId,
            async: false,
            dataType: "json",
            success: function (response) {
                roleSelectId.find('option').remove();
                appendSelectInList(roleSelectId);
                $.each(response, function (index, value) {
                    if ("${pageContext.response.locale}" === "en") {
                        $('<option>').val(value.id).text(value.nameInEnglish).appendTo(roleSelectId);
                    } else if ("${pageContext.response.locale}" === "bn") {
                        $('<option>').val(value.id).text(value.nameInBangla).appendTo(roleSelectId);
                    }
                });
//                if (selectedDistrict !== null) {
//                    districtSelectId.val(selectedDistrict);
//                }
            },
            failure: function () {
                log("loading district failed!!");
            }
        });
    }
    
</script>
<style>
    .all-maindiv {
        width: 99%;
        height: 300px;
        border: 1px solid #ddd;
        border-radius: 3px;
    }

    .row {
        margin-bottom: 15px;
        vertical-align: top;
        padding: 0;
    }

    .term-list {
        margin-bottom: 15px;
        display: inline-block;
        margin-top: 5px;
        list-style-type: none;
        margin-left: -45px;
    }

    .splitter {
        padding: 0px 20px 13px 0;
    }

    .splitter img {
        background-color: #eee;
        height: 1px;
        width: 100%;
    }

    .cat-div {
        /* font-size: 14px; */
        width: 25%;
        float: left;
    }
    ul{
        list-style-type: none;
    }
</style>
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="rolePage.addEditRoleWisePage" />
        <small></small>
    </h1>
    <div class="pull-right">
        <button type="submit" name="save" onclick="savePageRole()" class="btn bg-blue">
            <i class="fa fa-floppy-o"></i>
            <spring:message code="save" />
        </button>
    </div> 
</section>
<section class="content">
<!--    <p><spring:message code="user.role" /></p>-->
    <div class="row">
        <div class="col-md-12">
<!--            <div class="col-md-6">
                <div class="col-md-2"><spring:message code="paymentCycle.label.scheme" /></div>        
                <div class="col-md-6" id="scheme_container">
                    <SELECT id="scheme" class="form-control" onchange="loadRoleList(this)">
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
            </div>-->
            <div class="col-md-6">            
                <div class="col-md-2"><spring:message code="user.role" /></div>        
                <div class="col-md-6" id="role_container">
                    <SELECT id="role" class="form-control">
                        <spring:message code='label.select' var="select"/>                
                        <option value="">${select}</option>
                        <c:forEach items="${roleList}" var="entry">
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
        </div>
    </div>
    <hr>    
    <div class="row">
        <c:forEach items="${pageListMap}" var="entry">
            <div class="col-md-4">
                <strong>
                    <c:choose>
                        <c:when test="${pageContext.response.locale eq 'bn'}">
                            ${entry.key.nameInBangla}
                        </c:when>
                        <c:otherwise>
                            ${entry.key.nameInEnglish}
                        </c:otherwise>
                    </c:choose>   
                </strong>
                <c:set var="parentFormList" value="${entry.value}" />
                <ul>
                    <c:forEach var="parentForm" items="${parentFormList}">
                        <li>
                            <c:if test="${fn:length(parentForm.value) eq 0}">
                                <input id="ch${parentForm.key.id}" type="checkbox" />&nbsp;&nbsp;
                            </c:if> 
                            <c:choose>
                                <c:when test="${pageContext.response.locale eq 'bn'}">
                                    ${parentForm.key.nameInBangla}
                                </c:when>
                                <c:otherwise>
                                    ${parentForm.key.nameInEnglish}
                                </c:otherwise>
                            </c:choose>        
                            <c:set var="childFormList" value="${parentForm.value}" />
                            <c:if test="${fn:length(childFormList) gt 0}">
                                <ul>
                                    <c:forEach var="childForm" items="${childFormList}">
                                        <li>
                                            <c:choose>
                                                <c:when test="${pageContext.response.locale eq 'bn'}">
                                                    <input id="ch${childForm.id}" type="checkbox" />&nbsp;&nbsp;${childForm.nameInBangla}
                                                </c:when>
                                                <c:otherwise>
                                                    <input id="ch${childForm.id}" type="checkbox" />&nbsp;&nbsp;${childForm.nameInEnglish}
                                                </c:otherwise>
                                            </c:choose>   
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:if> 
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:forEach>                             
    </div>

</section>

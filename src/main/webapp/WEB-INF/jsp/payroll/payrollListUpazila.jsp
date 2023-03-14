<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style>
    .numericCol{
        text-align:right;
    }
    table.dataTable thead .sorting_asc:after{
        content:"";
    } 
</style>
<script type="text/javascript">
    $(function () {
        var path = '${pageContext.request.contextPath}';
        //set dataTable language url
        var urlLang = "";
        if ("${pageContext.response.locale}" === 'bn') {
            urlLang = path + "/dataTable/localization/bangla";
        }
        if ("${payrollSummary.payrollListType}" == "UNION")
        {
            $(menuSelect("${pageContext.request.contextPath}/payrollSummary/union/list"));
            loadUnion(" ${payrollSummary.upazilla.id}", $('#unionId'));
        } else
        {
            $(menuSelect("${pageContext.request.contextPath}/payrollSummary/municipal/list"));
            loadMunicipal(" ${payrollSummary.upazilla.id}", $('#unionId'));
        }
        if (selectedLocale === "bn")
        {
            makeUnijoyEditor('nid');
        }
        var nid = document.getElementById("nid");
        nid.addEventListener("keydown", function (event) {
            checkNumber(event, this);
        });
        $("#buttonSearch").click(function ()
        {
            $('#paymentListTable').DataTable().destroy();
            $('#paymentListTable').DataTable({
                "processing": true,
                "pageLength": 10,
                "lengthMenu": [10, 25, 50, 100],
                "bFilter": false,
                "bSort": false,
                "serverSide": true,
                "pagingType": "full_numbers",
                "language": {
                    "url": urlLang
                },
                "ajax": {
                    "url": path + "/payroll/viewList",
                    "type": "POST",
                    "data": {
                        "payrollSummary": ${payrollSummary.id},
                        "unionId": $('#unionId').val(),
                        "nid": $("#nid").val()}
                },
                "aoColumnDefs": [
                    {"sClass": "numericCol", "aTargets": [-1]}
                ],
                "language": {
                    "url": urlLang
                },
                "fnDrawCallback": function (oSettings) {
                    if (selectedLocale === 'bn')
                    {
                        localizeBanglaInDatatable("paymentListTable");
                    }

                }
            });
        });
        $("#buttonSearch").click();
    });
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="selectedLocale" value="${pageContext.response.locale}" />
<section class="content-header clearfix">
    <h1>
        <font color="black">
        <c:if test="${pageContext.response.locale=='bn'}">
            <spring:message code="label.detailPayroll" />
            <small><label>(<spring:message code="label.paymentCycle" />: ${payrollSummary.paymentCycle.nameInBangla},<spring:message code="label.location" />: 
                    ${payrollSummary.division.nameInBangla}->
                    ${payrollSummary.district.nameInBangla}->${payrollSummary.upazilla.nameInBangla})
                </label></small>
            </c:if>
            <c:if test="${pageContext.response.locale=='en'}">
            <small><label><spring:message code="label.detailPayroll" />(<spring:message code="label.paymentCycle" />: ${payrollSummary.paymentCycle.nameInEnglish},
                    <spring:message code="label.location" />: 
                    ${payrollSummary.division.nameInEnglish}->
                    ${payrollSummary.district.nameInEnglish}->${payrollSummary.upazilla.nameInEnglish})
                </label></small>
            </c:if>
        </font>
    </h1>

</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box container-fluid">
                <div class="box-body container-fluid">

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label">
                                    <c:choose>
                                        <c:when test="${payrollSummary.payrollListType eq 'UNION'}">
                                            <spring:message code="label.union" />
                                        </c:when>
                                        <c:otherwise>
                                            <spring:message code="label.municipal" />
                                        </c:otherwise>
                                    </c:choose>
                                </label>
                                <div class="col-md-6">
                                    <spring:message code='label.select' var="select"/>
                                    <select class="form-control"  id="unionId">
                                        <option value="" label="${select}"></option>
                                    </select>
                                    <errors path="union.id" cssStyle="color:red"></errors>
                                </div>
                            </div>
                            <br><br>
                            <div class="form-group">
                                <label for="status" class="col-md-4 control-label"><spring:message code="label.nid" /></label>
                                <div class="col-md-6">
                                    <spring:message code='label.nid' var="nid"/>
                                    <input type="text" id="nid" name="nid" placeholder="${nid}" class="form-control">
                                </div>
                            </div>
                            <br><br>
                            <div class="form-group">
                                <label class="col-md-4"></label> 
                                <div class="col-md-1 control-label" style="text-align: right">
                                    <button type="button" id="buttonSearch" class="btn bg-blue"><span class="glyphicon glyphicon-search">&nbsp;</span><spring:message code="label.search"/></button>
                                </div>
                            </div> 
                        </div>
                        <div class="col-md-6"></div>
                    </div>
                    <div class="row">
                        <!--                    <br><br><br><br><br><br>-->
                        <table id="paymentListTable" class="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th><spring:message code="payment.label.beneficiary" /></th>
                                    <th><spring:message code="payment.label.beneficiaryNid" /></th>
                                    <th><spring:message code="payment.label.mobileNumber" /></th>
                                    <th><spring:message code="payment.label.bankName" /></th>
                                    <th><spring:message code="payment.label.branchName" /></th>
                                    <th><spring:message code="payment.label.accountNumber" /></th>                                
                                    <th> 
                                        <spring:message code="label.union" />
                                    </th>         
                                    <th><spring:message code="payment.label.allowanceAmount" /></th>
                                </tr>
                            </thead> 
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>   
</section>
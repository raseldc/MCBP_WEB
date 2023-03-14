<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="<c:url value="/resources/plugins/fileUpload/fileinput.min.js" />" ></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />"></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<script src="<c:url value="/resources/js/unijoy.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/fileUpload/fileinput.min.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/applicantTab.css" />">
<script type="text/javascript" src="<c:url value="/resources/js/jquery.ui.datepicker-bn.js"/>" ></script>
<style>
    .error{
        color:red;
    }
    .normalLabel{
        font-weight: normal;
    }
    label[for=gender1]{
        margin: 0 10px 0 0;
    }    
</style>

<script type="text/javascript">
    $(function () {
        $(menuSelect("${pageContext.request.contextPath}/payrollBouncedBack/list"));
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
        initDate($("#dateOfBirth"), '${dateFormat}', $("#dateOfBirth\\.icon"), selectedLocale);
        if (selectedLocale === 'bn') {
     
            makeUnijoyEditor('accountNo');

        }
        
        $("#beneficiaryInfoForm").validate({

            rules: {// checks NAME not ID
                
                "paymentType": {
                    required: true
                },
                "bank.id": {
                    required: true
                },
                "branch.id": {
                    required: true
                },
                "accountType.id": {
                    required: true
                },
                "accountTypePO.id": {
                    required: true
                },
                "mobileBankingProvider.id": {
                    required: true
                },
                "postOfficeBranch.id": {
                    required: true
                },
                "accountName": {
                    required: true,
                    englishAlphabet: true,
                    englishAlphabetNoSpecial: true
                },
                "accountNo": {
                    required: true,
                    checkAccountNo: {locale: selectedLocale, len:13, paymentType: "#paymentType :selected", bank:"#bank :selected", mbp:"#mobileBankingProvider :selected"}
                }
            },
            errorPlacement: function (error, element) {
                    error.insertAfter(element);
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
//            var j = Cookies.getJSON('beneficiaryListForm');
            if ('${actionType}' !== 'create')
            {
                loadFormFromCookie($('#beneficiaryListForm'));
                search();
            }
        }
        
        if ('${actionType}' !== 'create')
        {
           loadBankOrProviderList($("#paymentType")[0]);
            loadBranchList($('#bank')[0]);
        }
    });
       function loadBankOrProviderList(selectObject) {
        var paymentType = selectObject.value;
        if (paymentType === 'BANKING')
        {
            var bankSelectId = $('#bank');
            loadBank(bankSelectId, '${beneficiaryForm.bank.id}');
            $("#bankGroupInfo").css("display", "block");
            $("#mobileBankingGroupInfo").css("display", "none");
            $("#postOfficeGroupInfo").css("display", "none");
        } else if (paymentType === 'MOBILEBANKING')
        {
            var mbpSelectId = $('#mobileBankingProvider');
            loadMobileBankingProvider(mbpSelectId, '${beneficiaryForm.mobileBankingProvider.id}');
            $("#bankGroupInfo").css("display", "none");
            $("#mobileBankingGroupInfo").css("display", "block");
            $("#postOfficeGroupInfo").css("display", "none");
        } else if (paymentType === 'POSTOFFICE')
        {
            var pobSelectId = $('#postOfficeBranch');
            var districtId = $("#presentDistrict").val();
            loadPostOfficeBranch(pobSelectId, '${beneficiaryForm.postOfficeBranch.id}', districtId);
            $("#bankGroupInfo").css("display", "none");
            $("#mobileBankingGroupInfo").css("display", "none");
            $("#postOfficeGroupInfo").css("display", "block");
        }
    }
    function loadBranchList(selectObject) {
        var bankId = selectObject.value;
        var branchSelectId = $('#branch');
//        var districtId = $("#presentDistrict").val();
        var districtId = '${beneficiaryForm.presentDistrict.id}';
        
        if (bankId !== '') {
            loadBranchByDistrict(bankId, districtId, branchSelectId, '${beneficiaryForm.branch.id}');
        } else {
            resetSelect(branchSelectId);
        }
    }
   

    function submitForm() {
        var form = $('#beneficiaryInfoForm');
        form.validate();
        if (form.valid()) {
            $('#btnSubmit').attr("disabled", true);
            if (selectedLocale === "bn")
            {
                if ('${sessionScope.userDetail.schemeShortName}' == 'LMA')
                {
                }
                $('#btnSubmit').html("<i class=\"fa fa-floppy-o\"></i>সংরক্ষিত হচ্ছে...");
            } else
            {
                if ('${sessionScope.userDetail.schemeShortName}' == 'LMA')
                {
                }
                $('#btnSubmit').html("<i class=\"fa fa-floppy-o\"></i>Saving...");
            }
            $('#beneficiaryInfoForm').submit();
            return true;
        }
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="selectedLocale" value="${pageContext.response.locale}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/beneficiary/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/beneficiaryPaymentInfo/edit/${beneficiaryForm.id}" />
    </c:otherwise>
</c:choose>

<form:form id="beneficiaryInfoForm" action="${actionUrl}" class="form-horizontal" enctype="multipart/form-data" role="form" modelAttribute="beneficiaryForm">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <%--<spring:message code='beneficiary'/>&nbsp;<spring:message code='label.management'/>--%> 
            <spring:message code='label.BankAccountInfoTab' var="bankAccountInfoTab"/>${bankAccountInfoTab}
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/payrollBouncedBack/list"><spring:message code="label.backToList"/></a></small>
        </h1>
        <div class="pull-right">
            <button type="button" name="save" id="btnSubmit" class="btn bg-blue" onClick="return submitForm()">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>            
        </div>   
    </section>

    <section class="content">
        <div class="row">                    
            <form:hidden path="id" />                                  
            <form:hidden path="applicantType" id="applicantType"/>
            <spring:message code='label.select' var="select"/>
                <div class="col-md-12">
                     <div class="form-group">
                        <label for="beneficiaryStatusInput" class="col-md-2 control-label"><spring:message code="payment.label.beneficiaryNid" /></label>
                        <div class="col-md-2" style="padding-top: 7px">
                            <a href='#' onclick = loadBeneficiary('${beneficiaryForm.id}') title='' > ${beneficiaryForm.nid}</a>
                        </div>
                    </div>
                   <hr>     
                    <div class="col-md-6">
                        <fieldset>
<!--                            <legend>
                            <spring:message code='label.BankAccountInfoTab' var="bankAccountInfoTab"/>${bankAccountInfoTab}
                        </legend>-->
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="paymentType.label.paymentType" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="paymentType"  id="paymentType" onchange="loadBankOrProviderList(this)"> 
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${paymentTypeList}"  itemLabel="${paymentTypeDisplayName}"></form:options>
                                </form:select> 
                                <form:errors path="paymentType" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div id="bankGroupInfo" style="display: none;">
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.bank" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="bank.id"  id="bank" onchange="loadBranchList(this)"> 
                                        <form:option value="" label="${select}"></form:option>
                                    </form:select> 
                                    <form:errors path="bank.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.branch" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="branch.id"  id="branch"> 
                                        <form:option value="" label="${select}"></form:option>                                        
                                    </form:select> 
                                    <form:errors path="branch.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.accountType" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="accountType.id"  id="accountType"> 
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${accountTypeList}" itemValue="id" itemLabel="${accountTypeName}"></form:options> 
                                    </form:select> 
                                    <form:errors path="accountType.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>

                            </div>
                            <div id="mobileBankingGroupInfo" style="display: none;">
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="mobileBankingProvider.label.mobileBankingProvider" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="mobileBankingProvider.id"  id="mobileBankingProvider" onchange="loadBranchList(this)"> 
                                        <form:option value="" label="${select}"></form:option>
                                        <%--<form:options items="${bankList}" itemValue="id" itemLabel="${bankName}"></form:options>--%> 
                                    </form:select> 
                                    <form:errors path="mobileBankingProvider.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                            </div>
                            <div id="postOfficeGroupInfo" style="display: none;">
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.branch" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="postOfficeBranch.id"  id="postOfficeBranch" > 
                                        <form:option value="" label="${select}"></form:option>
                                        <%--<form:options items="${bankList}" itemValue="id" itemLabel="${bankName}"></form:options>--%> 
                                    </form:select> 
                                    <form:errors path="postOfficeBranch.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.accountType" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="accountTypePO.id"  id="accountTypePO"> 
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${accountTypeList}" itemValue="id" itemLabel="${accountTypeName}"></form:options> 
                                    </form:select> 
                                    <form:errors path="accountTypePO.id" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>    
                            </div>         
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.accountName" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.accountName' var="accountName"/>
                                <form:input class="form-control" cssStyle="text-transform:uppercase" placeholder="${accountName}" path="accountName" autofocus="autofocus" onkeydown="checkEnglishAlphabetWithLength(event, this, 60)"/>
                                <form:errors path="accountName" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.accountNo" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.accountNo' var="accountNo"/>
                                <form:input class="form-control" placeholder="${accountNo}" path="accountNo" autofocus="autofocus" />
                                <form:errors path="accountNo" cssStyle="color:red"></form:errors>
                                </div>
                            </div>     
                        </fieldset>
                    </div>
                </div>

                
        </div>
        <a href="#" class="back-to-top" style=""><i class="fa fa-arrow-circle-up"></i></a>                        
    </section>    
</form:form>
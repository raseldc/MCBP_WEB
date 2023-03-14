<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="<c:url value="/resources/plugins/fileUpload/fileinput.min.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/fileUpload/fileinput.min.css" />">
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<style>
    #applicantListTable_length {
        float: left;
    }

    table.dataTable thead .sorting_asc:after {
        content: "";
    }
</style>



<form:form id="beneficiaryInfoForm" class="form-horizontal" modelAttribute="beneficiaryForm">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code="label.paymentInformationUpdate"/>
            <small><i class="fa fa-arrow-circle-left"></i><a id="backLink" href="${contextPath}/beneficiary/paymentInformationView"><spring:message code="label.backToList" /></a></small>
        </h1>
    </section>

    <c:set var="actionUrl" value="${contextPath}/beneficiary/edit/${beneficiaryForm.id}"/>

    <input type="hidden" value="${beneficiaryForm.id}" id="hiddenBeneficiaryId"/>
    <input type="hidden" value="${beneficiaryForm.id}" id="id"/>

    <section class="content">
        <div class="row">
            <div class="col-xs-12">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title"><spring:message code='label.BankAccountInfoTab'
                                        var="bankAccountInfoTab"/>${bankAccountInfoTab}</h3>
                    </div>
                    <div class="box-body">

                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.nameEn' var="fullNameEn"/>
                                    <form:input class="form-control" placeholder="${fullNameEn}" path="fullNameInEnglish" autofocus="autofocus" readonly="true" onkeydown="checkEnglishAlphabetWithLength(event, this, 60)"/>
                                    <form:errors path="fullNameInEnglish" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.nameEn' var="fullNameEn"/>
                                    <form:input class="form-control" placeholder="${fullNameBn}" path="fullNameInBangla" autofocus="autofocus" readonly="true" onkeydown="checkEnglishAlphabetWithLength(event, this, 60)"/>
                                    <form:errors path="fullNameInEnglish" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.nid" /><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.nid' var="nid"/>

                                    <form:input class="form-control" placeholder="${nid}" path="nid" autofocus="autofocus"  readonly="true"/>



                                </div>
                            </div>
                            <div class="form-group">
                                <label for="nidInput" class="col-md-4 control-label"><spring:message code="user.mobileNo" /></label>
                                <div class="col-md-8">
                                    <spring:message code='label.nid' var="nid"/>

                                    <form:input class="form-control" placeholder="${mobileNo}" path="mobileNo" autofocus="autofocus"  readonly="true"/>



                                </div>
                            </div>
                        </div>

                        <div class="col-lg-6">
                            <div class="form-group">

                                <label for="middleNameInput" class="col-md-4 control-label"></label>
                                <div class="col-md-8">
                                    <label style="font-weight: bold">   বর্তমান পেমেন্ট তথ্য</label>
                                </div>
                            </div>
                            <div class="form-group">

                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message
                                        code="paymentType.label.paymentType"/><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="paymentType" id="paymentType"
                                                 onchange="loadBankOrProviderList(this)">
                                        
                                        <form:options items="${paymentTypeList}"
                                                      itemLabel="${paymentTypeDisplayName}"></form:options>
                                    </form:select>
                                    <form:errors path="paymentType" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div id="bankGroupInfo" style="display: none;">
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message
                                            code="label.bank"/><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="bank.id" id="bank"
                                                     onchange="loadBranchList(this)">
                                            <form:option value="0" label="${select}"></form:option>
                                        </form:select>
                                        <form:errors path="bank.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message
                                            code="label.branch"/><span class="mandatory">*</span></label>
                                    <div class="col-md-8">

                                        <form:select class="form-control" path="branch.id" id="branch"  onchange="loadAccountLength_(this)">
                                            <form:option value="0" label="${select}"></form:option>
                                        </form:select>
                                        <form:errors path="branch.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message
                                            code="label.accountType"/><span class="mandatory">*</span></label>
                                    <div class="col-md-8">

                                        <form:select class="form-control" path="accountType.id" id="accountType">
                                            <form:option value="" label="${select}"></form:option>
                                            <form:options items="${accountTypeList}" itemValue="id"
                                                          itemLabel="${accountTypeName}"></form:options>
                                        </form:select>
                                        <form:errors path="accountType.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>

                                </div>
                                <div id="mobileBankingGroupInfo" style="display: none;">
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message
                                            code="mobileBankingProvider.label.mobileBankingProvider"/><span
                                            class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="mobileBankingProvider.id"
                                                     id="mobileBankingProvider" onchange="loadBranchList(this)">
                                            <form:option value="0" label="${select}"></form:option>
                                            <%--<form:options items="${bankList}" itemValue="id" itemLabel="${bankName}"></form:options>--%>
                                        </form:select>
                                        <form:errors path="mobileBankingProvider.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                </div>
                                <div id="postOfficeGroupInfo" style="display: none;">
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message
                                            code="label.branch"/><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="postOfficeBranch.id" id="postOfficeBranch">
                                            <form:option value="0" label="${select}"></form:option>
                                            <%--<form:options items="${bankList}" itemValue="id" itemLabel="${bankName}"></form:options>--%>
                                        </form:select>
                                        <form:errors path="postOfficeBranch.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message
                                            code="label.accountType"/><span class="mandatory">*</span></label>
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="accountTypePO.id" id="accountTypePO">
                                            <form:option value="0" label="${select}"></form:option>
                                            <form:options items="${accountTypeList}" itemValue="id"
                                                          itemLabel="${accountTypeName}"></form:options>
                                        </form:select>
                                        <form:errors path="accountTypePO.id" cssStyle="color:red"></form:errors>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message
                                        code="label.accountName"/><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.accountName' var="accountName"/>
                                    <form:input class="form-control" cssStyle="text-transform:uppercase"
                                                placeholder="${accountName}" readonly="true" path="accountName"
                                                autofocus="autofocus"
                                                onkeydown="checkEnglishAlphabetWithLength(event, this, 60)"/>
                                    <form:errors path="accountName" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"><spring:message
                                        code="label.accountNo"/><span class="mandatory">*</span></label>
                                <div class="col-md-8">
                                    <spring:message code='label.accountNo' var="accountNo"/>
                                    <form:input class="form-control" placeholder="${accountNo}" path="accountNo"
                                                autofocus="autofocus"/>
                                    <form:errors path="accountNo" cssStyle="color:red"></form:errors>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="middleNameInput" class="col-md-4 control-label"> <spring:message code="label.reasonToChange"/><span class="mandatory">*</span></label>
                                <div class="col-md-8">

                                    <select name="reason" id="ddReason" class=" form-control custom-select">
                                        <option value=""><spring:message code="label.select"/></option>

                                        <c:if test="${!empty reasonList}">

                                            <c:forEach items="${reasonList}" var="reason">
                                                <option Value="${reason.value}">${reason.nameInBangla}</option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"> <spring:message code="label.bounceBackCorrection"/><span class="mandatory">*</span></label>
                                <div class="col-md-8">

                                    <input name="rdBounceBack" type="radio" value="yes"/><label style="font-weight: bold"> <spring:message code="button.yes"/></label>
                                    <input name="rdBounceBack"  type="radio" value="no" title="no" checked="true"/> <label style="font-weight: bold"> <spring:message code="button.no"/></label>

                                </div>
                            </div>

                            <div class="form-group">
                                <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.attachment" /></label>
                                <div class="col-md-8">
                                    <spring:message code='placeholder.signature' var="signature"/>                                    

                                    <div id="signature">
                                        <input id="fileInput" name="file" type="file" class="file" accept="image/jpg,image/png,image/jpeg,image/gif"  data-allowed-file-extensions='["jpg", "jpeg", "png", "gif"]'>
                                    </div>


                                </div>            
                            </div> 
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"> </label>
                                <div class="col-md-8">
                                    <button type="button" name="save" id="btnSubmit" class="btn bg-blue" onclick="UpdateBeneficiaryPaymentInformation()">
                                        <i class="fa fa-floppy-o"></i>
                                        <spring:message code="label.edit"/>
                                        <div></div></button>


                                </div>
                            </div>
                        </div>


                    </div>
                </div>
            </div>


            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">পূর্ববর্তী পেমেন্ট তথ্যের  তালিকা</h3>
                        </div>
                        <div class="box-body">
                            <div class="form-group row col-lg-12 table-responsive">
                                <table id="paymentInformationChangeHitory" class="table table-bordered table-hover">
                                    <thead>
                                        <tr>
                                            <th><spring:message code="beneficiaryProfile.Serial"/></th>
                                            <th><spring:message code="label.changeDate"/></th>
<!--                                            <th><spring:message code="label.nameBn"/></th>
                                            <th><spring:message code="label.nameEn"/></th>
                                            <th><spring:message code="label.nid"/></th>-->
                                            <!--<th><spring:message code="label.mobileNo"/></th>-->
                                            <th><spring:message code="paymentType.label.paymentType"/></th>
                                            <th><spring:message code="label.bank"/></th>
                                            <th><spring:message code="label.branch"/></th>
                                            <!--<th><spring:message code="mobileBankingProvider.label.mobileBankingProvider"/></th>-->
                                            <!--<th><spring:message code="label.postOfficeBranch"/></th>-->
                                            <th><spring:message code="label.accountName"/></th>
                                            <th><spring:message code="label.accountNo"/></th>
                                            <th><spring:message code="label.reasonToChange"/></th>
                                            <th><spring:message code="label.bounceBackCorrection"/></th>
                                            <th>সংযুক্তি</th>


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

    </section>
</form:form>

<input type="hidden" id="aType" value="${type}">

<button type="button" id="btnModal" style="display: none" class="btn btn-primary" data-toggle="modal" data-target="#modaItem"></button>

<div class="modal fade bd-example-modal-md" id="modaItem" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">তথ্য</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="modalBody">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="isReloadPage()" id="btn_close_item_modal"><spring:message code="close"/></button>

            </div>
        </div>
    </div>
</div>  

<script type="text/javascript">

    var districtId = ${beneficiaryForm.permanentDistrict.id};
    var isUpdate = 0;
    var urlLang = "";
    var baseUrl = '${contextPath}';
    var accountTypeIdPrevious = 0;
    var bankIdPrevious = 0;
    var branchIdPrevious = 0;
    var paymentTypePrevious = 0;
    var mobileBankingProviderPrevious = 0;
    var accountNoPrevious = "";

    function isReloadPage()
    {
        if (isUpdate == 1)
        {
            location.reload();
        }

    }
    function loadAccountLength_(selectedObject)
    {
        console.log("Call Account Length");
        //load account length
        if (($("#paymentType :selected").val() === "MOBILEBANKING") &&
                $("#mobileBankingProvider  option:selected").val() != "undefined"
                && $("#mobileBankingProvider  option:selected").val() != "")
        {
            accountNoLength_js = $("#mobileBankingProvider  option:selected").attr("length");
        } else if (($("#paymentType :selected").val() === "BANKING") && $("#branch  option:selected").val() != "undefined" && $("#branch  option:selected").val() != "")
        {
            accountNoLength_js = $("#branch  option:selected").attr("length");
        } else
        {

            accountNoLength_js = 100;
        }
    }
    function loadPaymentHistory()
    {
        $('#paymentInformationChangeHitory').DataTable().destroy();
        $('#paymentInformationChangeHitory').DataTable({
            "processing": true,
            "pageLength": 10,
            "lengthMenu": [5, 10, 15, 20],
            "destroy": true,
            "paging": true,
            "serverSide": false,
            "ordering": true,
            "order": [[1, "desc"]],
            "pagingType": "full_numbers",
            "filter": true,
            "dom": '<"top"i>rt<"bottom"lp><"clear">',

            "language": {
                "url": urlLang
            },
            "ajax": baseUrl + "/beneficiary/paymentInformationHistory/" + $("#hiddenBeneficiaryId").val(),

            "columns": [
                {"data": function (data) {
                        var serial = data.serial == undefined ? 0 : data.serial;
                        return  (selectedLocale === 'bn' ? getNumberInBangla(serial.toString()) : serial);
                    }},
                {"data": function (data) {
                        return selectedLocale === 'bn' ? getNumberInBangla(data.creationDateSt.toString()) : data.creationDateSt;

                    }},
                {"data": function (data) {
                        return data.paymentType;

                    }},
                {"data": function (data) {
                        if (data.mobileBankProviderNameId != 0 && data.mobileBankProviderNameId != undefined && data.mobileBankProviderNameId != null)
                        {
                            return data.mobileBankProviderName;
                        }
                        return data.bankName;

                    }},
                {"data": function (data) {
                        return data.branchName;

                    }},
                //                {"data": function (data) {
                //                        return data.mobileBankProviderName;
                //
                //                    }},
                //                {"data": function (data) {
                //                        return data.mobileBankProviderName;
                //
                //                    }},
                {"data": function (data) {
                        return data.accountName;

                    }},
                {"data": function (data) {
                        var accountNumber = data.accountNumber == undefined ? "" : data.accountNumber;
                        return  selectedLocale === 'bn' ? getNumberInBangla(accountNumber.toString()) : accountNumber;


                    }},
                {"data": function (data) {
                        return data.reasonEnumMappingClass == undefined ? '' : data.reasonEnumMappingClass.nameInBangla;

                    }},
                {"data": function (data) {
                        if (data.isBounceBack === undefined)
                            return "";
                        if (data.isBounceBack == 0)
                        {
                            return   selectedLocale === 'bn' ? "না" : "NO";
                        } else
                        {
                            return selectedLocale === 'bn' ? "হ্যাঁ" : "Yes";
                        }


                    }},
                {"data": function (data) {
                        if (data.hasFile == 1)
                            return  "<a target='_blank' href='" + baseUrl + "/beneficiary/payment-information/file-download?paymentHistoryId=" + data.id + "' value='Edit'><i class='fa fa-download' aria-hidden='true'></i></a>";
                        else
                            return "";

                    }}



            ],
            "fnDrawCallback": function (oSettings) {
                //  showModalDialog();
                if (selectedLocale === 'bn')
                {

                    localizeBanglaInDatatable("paymentInformationChangeHitory");
                }
            }
        });
    }
    function loadBankOrProviderList(selectObject) {

        $("#bank").val(0);
        $("#mobileBankingProvider").val(0);
        $("#branch").val(0);
        $("#accountType").val(0);
        var paymentType = selectObject.value;
        if (paymentType === 'BANKING') {
            var bankSelectId = $('#bank');
            loadBank(bankSelectId, '${beneficiaryForm.bank.id}');
            $("#bankGroupInfo").css("display", "block");
            $("#mobileBankingGroupInfo").css("display", "none");
            $("#postOfficeGroupInfo").css("display", "none");
        } else if (paymentType === 'MOBILEBANKING') {
            var mbpSelectId = $('#mobileBankingProvider');
            loadMobileBankingProvider(mbpSelectId, '${beneficiaryForm.mobileBankingProvider.id}');
            $("#bankGroupInfo").css("display", "none");
            $("#mobileBankingGroupInfo").css("display", "block");
            $("#postOfficeGroupInfo").css("display", "none");
            accountNoLength_js = 17;
        } else if (paymentType === 'POSTOFFICE') {
            var pobSelectId = $('#postOfficeBranch');
            //   var districtId = $("#presentDistrict").val();
            loadPostOfficeBranch(pobSelectId, '${beneficiaryForm.postOfficeBranch.id}', districtId);
            $("#bankGroupInfo").css("display", "none");
            $("#mobileBankingGroupInfo").css("display", "none");
            $("#postOfficeGroupInfo").css("display", "block");
            accountNoLength_js = 17;
        }
    }

    function loadBranchList(selectObject) {
        var idName = $(selectObject).attr('id');
        if (idName == "mobileBankingProvider") {
            var accountNoLength = $("#mobileBankingProvider :selected").attr("length");
            if (accountNoLength == "-1" || accountNoLength == "undefined" || accountNoLength == "0") {
                accountNoLength = 100;

            } else {
                accountNoLength = parseInt(accountNoLength);
            }
            accountNoLength_js = accountNoLength;
            return;
        } else {
            var bankId = selectObject.value;
            var branchSelectId = $('#branch');
            //    var districtId = $("#presentDistrict").val();
            if (bankId !== '') {
                loadBranchByDistrict(bankId, districtId, branchSelectId, '${beneficiaryForm.branch.id}');
            } else {
                resetSelect(branchSelectId);
            }
            console.log(bankId);
            var accountNoLength = $("#bank :selected").attr("length");
            if (accountNoLength == "-1" || accountNoLength == "undefined" || accountNoLength == "0") {
                accountNoLength = 100;

            } else {
                accountNoLength = parseInt(accountNoLength);
            }
            accountNoLength_js = accountNoLength;
        }
    }

    function isValidForChange()
    {
        var bounceBack = $('input[name=rdBounceBack]:checked').val() == "yes" ? 1 : 0;
        if (bounceBack == 1)
            return true;
        var accountTypeIdCurrent = $("#accountType").val();
        var bankIdCurrent = $("#bank").val();
        var branchIdCurrent = $("#branch").val();
        var paymentTypeCurrent = $("#paymentType").val();
        var mobileBankingProviderCurrent = $("#mobileBankingProvider").val();
        var accountNoCurrent = getNumberInEnglish($("#accountNo").val().toString());
        var result = false;

        if (accountTypeIdCurrent != accountTypeIdPrevious && (accountTypeIdCurrent != 0 || accountTypeIdCurrent != ""))
        {
            return true;
        }
        if (bankIdCurrent != bankIdPrevious && (accountTypeIdCurrent != 0 || accountTypeIdCurrent != ""))
        {
            return true;
        }

        if (branchIdCurrent != branchIdPrevious && (branchIdCurrent != 0 || branchIdCurrent != ""))
        {
            return true;
        }

        if (paymentTypeCurrent != paymentTypePrevious && (paymentTypeCurrent != 0 || paymentTypeCurrent != ""))
        {
            return true;
        }

        if (mobileBankingProviderCurrent != mobileBankingProviderPrevious && (mobileBankingProviderCurrent != 0 || mobileBankingProviderCurrent != ""))
        {
            return true;
        }
        if (accountNoCurrent != accountNoPrevious && (accountNoCurrent != ""))
        {
            return true;
        }

        return false;

    }

    function UpdateBeneficiaryPaymentInformation() {
        var form = $('#beneficiaryInfoForm');
        form.validate();
        var validity = form.valid();
        if (validity == false)
        {
            return;
        }

        if (!isValidForChange()) {
            $("#modalBody").html("বর্তমান তথ্যের কোন পরিবর্তন হয়নি। ")
            $("#btnModal").click();
            return;
        }


        if ($("#ddReason :selected").val() == 0)
        {
            $("#modalBody").html("পরিবর্তনের কারন নির্বাচন করুন");
            $("#btnModal").click();
            return;
        }
        if (!uniqueAccountNoCheckForBeneficiary('<spring:message code="label.failure" />', '<spring:message code="label.ok" />'))
        {
            return;
        }
        var bounceBack = $('input[name=rdBounceBack]:checked').val() == "yes" ? 1 : 0;
        var updateInfo = {
            "benID": $("#hiddenBeneficiaryId").val(),
            "paymentType": $("#paymentType").val(),
            "bankId": ($("#bank").val() == "" || $("#bank").val() == null) ? 0 : $("#bank").val(),
            "branchId": ($("#branch").val() == "" || $("#branch").val() == null) ? 0 : $("#branch").val(),
            "accountType": ($("#accountType").val() == "" || $("#accountType").val() == null) ? 0 : $("#accountType").val(),
            "accountName": $("#accountName").val(),
            "accountNumber": getNumberInEnglish($("#accountNo").val().toString()),
            "mobileBankProviderNameId": ($("#mobileBankingProvider").val() == "" || $("#mobileBankingProvider").val() == null) ? 0 : $("#mobileBankingProvider").val(),
            "postOfficeBranch": $("#postOfficeBranch").val() == "" ? 0 : $("#postOfficeBranch").val(),
            "accountTypePO": $("#accountTypePO").val(),
            "reasonToPayInformationChange": $("#ddReason").val(),
            "isBounceBack": bounceBack

        };

        updateInfo = JSON.stringify(updateInfo);
        console.log(updateInfo);
        var objFormData = new FormData();
        var i = 0;
        var size = 0;
        while (i < $("#fileInput")[0].files.length)
        {
            console.log(i);
            objFormData.append('file', $("#fileInput")[0].files[i]);
            size = size + $("#fileInput")[0].files[i].size;
            i = i + 1;
        }
        objFormData.append('updateApplicantInfo', updateInfo);
        $.ajax(
                {
                    type: 'POST',
                    url: "${contextPath}/beneficiary/updatePaymentInfo",
                    //  contentType: 'application/json; charset=utf-8',
                    data: objFormData,
                    dataType: 'json',
                    contentType: false,
                    processData: false,

                    success: function (data) {
                        isUpdate = 1;
                        if (data.isError === false) {
                            if (data.errorCode == 200) {
                                $("#modalBody").html("সফলভাবে সম্পাদনা হয়েছে");
                                $("#btnModal").click();
                            } else if (data.errorCode == 500) {
                                $("#modalBody").html("সম্পাদনা ব্যর্থ হয়েছে।");
                                $("#btnModal").click();
                            }



                        }
                        if (data.isError === true) {

                            isUpdate = 0;
                            $("#msg").css("color", "red");
                            $("#msg").html(data.errorMsg);
                            setTimeout(function () {
                                $("#msg").html("");
                            }, 5000);
                        }


                    }
                });

    }


    $(function () {


        console.log("Type ${type}");
        if ($("#aType").val().toString().toLowerCase() === 'union') {
            $("#backLink").attr("href", "${contextPath}/beneficiary/paymentInformationView/union")
            $(menuSelect("${pageContext.request.contextPath}/beneficiary/paymentInformationView/union"));
        } else if ($("#aType").val().toString().toLowerCase() === 'municipal') {
            $("#backLink").attr("href", "${contextPath}/beneficiary/paymentInformationView/municipal")
            $(menuSelect("${pageContext.request.contextPath}/beneficiary/paymentInformationView/municipal"));
        } else if ($("#aType").val().toString().toLowerCase() === 'bgmea')
        {
            $("#backLink").attr("href", "${contextPath}/beneficiary/paymentInformationView/bgmea")
            $(menuSelect("${pageContext.request.contextPath}/beneficiary/paymentInformationView/bgmea"));

        } else if ($("#aType").val().toString().toLowerCase() === 'bkmea') {
            $("#backLink").attr("href", "${contextPath}/beneficiary/paymentInformationView/bkmea")
            $(menuSelect("${pageContext.request.contextPath}/beneficiary/paymentInformationView/bkmea"));
        }

        //  $(menuSelect("${pageContext.request.contextPath}/beneficiary/bkmea/list"));
        baseUrl = '${contextPath}';
        accountTypeIdPrevious = 0;
        bankIdPrevious = 0;
        branchIdPrevious = 0;
        paymentTypePrevious = 0;
        mobileBankingProviderPrevious = 0;
        accountNoPrevious = "";
        console.log("done");


        if (selectedLocale === 'bn') {

            urlLang = baseUrl + "/dataTable/localization/bangla";
            makeUnijoyEditor('accountNo');
        }

        document.getElementById("accountNo").addEventListener("keydown", function (event) {
            checkNumberWithLengthForAccountNo(event, this, 17);
        });

        loadBankOrProviderList($("#paymentType")[0]);
        loadBranchList($('#bank')[0]);
        loadPaymentHistory();


        $("#beneficiaryInfoForm").validate({
            rules: {
                "accountNo": {
                    required: true,
                    checkAccountNo: true,
                    // uniqueAccountNumber: true
                },

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
//                    englishAlphabet: true
                },
                "reason": {
                    required: true,
                }
            },
            messages: {
                "accountNo": {
                    required: function () {
                        return lan === "bn" ? "এই তথ্যটি আবশ্যক।" : "This field is required ";
                    },
                    checkAccountNo: function () {
                        if (selectedLocale === "bn")
                        {
                            return "সঠিক হিসাব নং দিন ";
                        } else
                        {
                            return "Please give correct Account NO";
                        }

                    }
                },
            },
            errorPlacement: function (error, element) {
                if (element.attr("name") === "photo")
                {
                    error.insertAfter($("#photo"));
                } else if (element.attr("name") === "signature")
                {
                    error.insertAfter($("#signature"));
                } else if (element.parent('.input-group').length)
                {
                    error.insertAfter(element.parent());
                } else
                {
                    error.insertAfter(element);
                }
            }
        });

        $("#accountType").val(${beneficiaryForm.accountType.id})
        setTimeout(function () {
            loadAccountLength_();

        }, 3000);

        accountTypeIdPrevious = $("#accountType").val();
        bankIdPrevious = $("#bank").val();
        branchIdPrevious = $("#branch").val();
        paymentTypePrevious = $("#paymentType").val();
        mobileBankingProviderPrevious = $("#mobileBankingProvider").val();
        accountNoPrevious = getNumberInEnglish($("#accountNo").val().toString());
    })
</script>

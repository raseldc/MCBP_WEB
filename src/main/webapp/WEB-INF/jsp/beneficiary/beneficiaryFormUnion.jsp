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
<script src="<c:url value="/resources/js/backToTop.js" />" ></script> 
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
        //if this page for Edit NID and Dateof birht filed are read only url,isEdit varaible use for readOnly
        var url = window.location.pathname.toString();
        var isEdit = 0;
        if (url.indexOf("/edit/") !== -1)
        {
            $('#nid').attr("readonly", true);
            $('#dateOfBirth').attr("readonly", true);
            $("#fiscalYear").attr("disabled", true);
            isEdit = 1;

        } else
        {
            initDate($("#dateOfBirth"), '${dateFormat}', $("#dateOfBirth\\.icon"), selectedLocale);
            $("#fiscalYear").attr("disabled", true);
            $('#dateOfBirth').attr("readonly", false);
            $('#nid').attr("readonly", false);
        }
        //NID and DOB readonly done 

        $(menuSelect("${pageContext.request.contextPath}/beneficiary/union/list"));
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");

        includeJs(contextPath + "/resources/js/utility.js");
//        initDate($("#dateOfBirth"), '${dateFormat}', $("#dateOfBirth\\.icon"), selectedLocale);
//        makeUnijoyEditor('fullNameInBangla');
        if (selectedLocale === 'bn') {
//            makeUnijoyEditor('fatherName');
//            makeUnijoyEditor('motherName');
            makeUnijoyEditor('spouseName');
            makeUnijoyEditor('nickName');
            makeUnijoyEditor('mobileNo');
//            makeUnijoyEditor('nid');
            if (isEdit === 0) {
                makeUnijoyEditor('nid');
            }
            $.datepicker.setDefaults(
                    $.extend(
                            {'dateFormat': 'dd-mm-yy'},
                            $.datepicker.regional['bn-BD']
                            )
                    );

            $("#dateOfBirth").val(getNumberInBangla($("#dateOfBirth").val()));
            //            makeUnijoyEditor('presentAddressLine1');
            makeUnijoyEditor('presentAddressLine2');
            //            makeUnijoyEditor('presentWardNo');
            makeUnijoyEditor('presentPostCode');
            //            makeUnijoyEditor('permanentAddressLine1');
            makeUnijoyEditor('permanentAddressLine2');
            //            makeUnijoyEditor('permanentWardNo');
            makeUnijoyEditor('permanentPostCode');
            makeUnijoyEditor('accountNo');

        }
        // for some reason, fullNameInBangla is not working in first time load, so, I just change the focus and again focused in first field.
        $("#fatherName").focus();
        $("#fiscalYear").focus();

        var mobileNo = document.getElementById("mobileNo");
        mobileNo.addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 11);
        });
        var nid = document.getElementById("nid");
        nid.addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 17);
        });
        $("#beneficiaryInOtherScheme").parent().addClass("pull-right");
//        document.getElementById("presentWardNo").addEventListener("keydown", function (event) {
//            checkNumberWithLength(event, this, 2);
        //        });
        document.getElementById("presentPostCode").addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 4);
        });
//        document.getElementById("permanentWardNo").addEventListener("keydown", function (event) {
//            checkNumberWithLength(event, this, 2);
        //        });
        document.getElementById("permanentPostCode").addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 4);
        });
        document.getElementById("accountName").addEventListener("keydown", function (event) {
            checkAlphabetWithLength(event, this, 50);
        });
        document.getElementById("accountNo").addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 17);
        });
        document.getElementById("accountNo").addEventListener("keydown", function (event) {
            checkNumberWithLengthForAccountNo(event, this, 17);
        });

        $("#beneficiaryInfoForm").validate({

            rules: {// checks NAME not ID
                "fiscalYear.id": {
                    required: true
                },
                "fullNameInBangla": {
                    required: true,
                    banglaAlphabet: true
                },
                "fullNameInEnglish": {
                    required: true,
                    englishAlphabet: true
                },
                "fatherName": {
                    required: true
                },
                "motherName": {
                    required: true
                },
                "spouseName": {
                    required: true
                },
                "dateOfBirth": {
                    required: true,
                    checkDateOfBirth: {locale: selectedLocale, min: 20, max: 35},
                    checkDateOfBirthUpdate: {locale: selectedLocale, min: 20}
                },
                "mobileNo": {
                    required: false,
                    //                    number: true,
                    minlength: 11,
                    maxlength: 11,
                    checkMobileNo: true
                },
                "nid": {
                    required: true,
                    //                    number: true,
                    minlength: 10,
                    maxlength: 17,
                    checkNidNumber: true,
                    uniqueBenNid: true
                },
                "birthPlace.id": {
                    required: true
                },
                "religionEnum": {
                    required: true
                },
                "bgmeaFactory.id": {
                    required: true
                },
                "bkmeaFactory.id": {
                    required: true
                },
                "presentAddressLine1": {
                    required: true
                },
                "presentDivision.id": {
                    required: true
                },
                "presentDistrict.id": {
                    required: true
                },
                "presentUpazila.id": {
                    required: true
                },
                "presentUnion.id": {
                    required: true
                },
                "permanentWardNo": {
                    required: true
                },
                "presentVillage.id": {
                    required: true
                },
                "presentPostCode": {
                    required: true
                },
                "permanentAddressLine1": {
                    required: true
                },
                "permanentDivision.id": {
                    required: true
                },
                "permanentDistrict.id": {
                    required: true
                },
                "permanentUpazila.id": {
                    required: true
                },
                "permanentUnion.id": {
                    required: true
                },
                "permanentWardNo": {
                    required: true
                },
                "permanentVillage.id": {
                    required: true
                },
                "permanentPostCode": {
                    required: true
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
                "accountNo": {
                    required: true,
                    checkAccountNo: true,
                    // uniqueAccountNumber: true
                },
                "landSizeRural": {
                    required: true
                },
                "occupationRural": {
                    required: true
                },
                "monthlyIncome": {
                    required: true
                },
                "hASLatrineRural": {
                    required: true
                },
                "hASElectricity": {
                    required: true
                },
                "hASElectricFan": {
                    required: true
                },
                "hASTubewellRural": {
                    required: true
                },
                "hHWallMadeOf": {
                    required: true
                },
                "disability": {
                    required: true
                },
                "conceptionTerm": {
                    required: true
                },
                "conceptionDuration": {
                    required: true
                },
                "photo": {
                    required: true,
                    checkFileSize: 300  // 50 KB
                },
                "signature": {
                    required: true,
                    checkFileSize: 300 // 50 KB
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
        $('#sameAsPresent').on('ifChecked', function () {
            loadPermanentAddressSameAsPresent(this);
        });
        $('#sameAsPresent').on('ifUnchecked', function () {
            resetPermanentAddress();
        });

        if ('${beneficiaryForm.isDistrictAvailable}' === 'false' && '${beneficiaryForm.presentDivision.id}' !== "")
        {
            loadDistrict('${beneficiaryForm.presentDivision.id}', $('#presentDistrict'));
        } else if ('${beneficiaryForm.isUpazilaAvailable}' === 'false' && '${beneficiaryForm.presentDistrict.id}' !== "")
        {
            loadUpazilla('${beneficiaryForm.presentDistrict.id}', $('#presentUpazila'));
        } else if ('${beneficiaryForm.isUnionAvailable}' === 'false' && '${beneficiaryForm.presentUpazila.id}' !== "")
        {
            loadUnion('${beneficiaryForm.presentUpazila.id}', $('#presentUnion'));
        }
        if ('${actionType}' !== 'create')
        {
            loadPresentDistrictList($('#presentDivision')[0]);
            loadPresentUpazilaList($('#presentDistrict')[0]);
            loadPresentUnionList($('#presentUpazila')[0]);
            loadPresentVillageList($('#presentWardNo')[0]);

            loadPermDistrictList($('#permDivision')[0]);
            loadPermUpazilaList($('#permDistrict')[0]);
            loadPermUnionList($('#permUpazila')[0]);
            loadPermanentVillageList($('#permanentWardNo')[0]);

            loadBankOrProviderList($("#paymentType")[0]);
            loadBranchList($('#bank')[0]);
        }

        if ('${actionType}' !== 'edit')
        {
            $("#dateOfBirth").rules('remove', 'checkDateOfBirth');
        }

        if (${beneficiaryForm.photoPath!='' && beneficiaryForm.photoPath!=null})
            $("#photo").attr("style", "display:none");
        else
            $("#photo").attr("style", "display:block");

        if (${beneficiaryForm.signaturePath!='' && beneficiaryForm.signaturePath!=null})
            $("#signature").attr("style", "display:none");
        else
            $("#signature").attr("style", "display:block");
        if (${beneficiaryForm.attachmentList!='' && beneficiaryForm.attachmentList!=null && beneficiaryForm.attachmentList !='[]'})
            $("#ancCard").attr("style", "display:none");
        else
            $("#ancCard").attr("style", "display:block");
        $(".fileinput-upload-button:eq(0)").hide();
        $(".fileinput-upload-button:eq(1)").hide();
        $(".fileinput-remove-button:eq(0)").text(getLocalizedText('label.remove', selectedLocale));
        $(".fileinput-remove-button:eq(1)").text(getLocalizedText('label.remove', selectedLocale));
        //add more file components if Add is clicked
        $('#addFile').click(function () {
            var fileIndex = $('#fileTable > div').children().length / 2 + 1;
            if ((fileIndex - 1) == 0 || $('#multipartFileList' + (fileIndex - 1)).val() != "") {
                $('#fileTable').append(
                        '<div class="form-group">' +
                        '<label for="Input' + fileIndex + '" class="col-md-2 control-label">' + '<spring:message code="label.attachment" />' + '</label>' +
                        '<div class="col-md-5">' +
                        '	<input type="file" class="file" id="multipartFileList' + fileIndex + '" name="multipartFileList[' + fileIndex + ']" />' +
                        '</div>' +
                        '</div>');
                $('#multipartFileList' + fileIndex).fileinput();
                $("#multipartFileList" + fileIndex).prev().text(getLocalizedText('browse', selectedLocale));
                $(".fileinput-upload-button:eq(" + fileIndex + ")").hide();
            } else {
                bootbox.dialog({
                    onEscape: function () {},
                    title: '<spring:message code="label.warning" />',
                    message: "<b><spring:message code="attachmentFailed" /></b>",
                    buttons: {
                        ok: {
                            label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                        }
                    }
                });
                $('.modal-content:eq(3)').css({
                    'margin-top': function () {
                        console.log('margin - top');
                        var w = $(window).height();
                        var b = $($(".modal-dialog")[3]).height(); // special case, there are 2 modals in page, so, it is number 3 . so, index = 3-1 = 2
                        // should not be (w-h)/2                    
                        var h = (w - 172) / 2; // hard coded!!
                        console.log("w=" + w + ", b=" + b + ", h=" + h);
                        return h + "px";
                    },
                });
            }
        });
        if (selectedLocale === 'bn') {
            $("span[id^='attachmentIndex']").each(function () {
                $(this).text(getNumberInBangla($(this).text()));
            });
        }

        setTimeout(function () {
            loadAccountLength_();
        }, 3000);
    });
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
    function removeAttachment(index, attachId)
    {
        $("#dvAttachment" + index).css("display", "none");
        $("#removeList").val(attachId + "," + $("#removeList").val());
    }
    function removeAncAttachment(index, attachId)
    {
        $("#dvAttachment" + index).css("display", "none");
        $("#ancCard").attr("style", "display:block");
        $("#removeList").val(attachId + "," + $("#removeList").val());
    }
    function checkDob() {
        $("#spouseName").focus();
        $("#dateOfBirth").focus();
    }
    function doNIDCheck(id) {
        var data = getDataFromUrl(contextPath + "/NIDcomparison/" + id);
        $('#myModal .modal-body').html(data);
        var header = getLocalizedText("label.viewComparisonPageHeader", selectedLocale);
        $('#myModalLabel').text(header);
        $('#myModal .modal-body').css({height: screen.height * .60});
        $('#myModal').modal('show');
    }
    function getNIDData(id, dob) {
        if (selectedLocale === 'bn')
        {
            id = getNumberInEnglish(id);
            dob = getNumberInEnglish(dob);
        }

        var msg = '';
        if (id === "") {
            msg = '<spring:message code="label.nidRequired" />';
            if (dob === "") {
                msg = '<spring:message code="label.nidAndDobRequired" />';
            }
            bootbox.dialog({
                onEscape: function () {},
                title: '<spring:message code="label.failure" />',
                message: msg,
                buttons: {
                    ok: {
                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                    }
                },

                callback: function (result) {
                }
            });
        } else if (dob === "") {
            msg = '<spring:message code="label.dobRequired" />';
            bootbox.dialog({
                onEscape: function () {},
                title: '<spring:message code="label.failure" />',
                message: msg,
                buttons: {
                    ok: {
                        label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                    }
                },

                callback: function (result) {

                }
            });
        } else
        {
            $('#loadImg').show();
        }
        $.ajax({
            type: "GET",
            url: contextPath + "/getNidData/" + id + "/" + dob,
            async: true,
            success: function (response) {
                $('#loadImg').hide();
                if (response == null || response == "")
                {
                    bootbox.dialog({
                        onEscape: function () {},
                        title: '<spring:message code="label.failure" />',
                        message: '<spring:message code="label.ECFailure"/>',
                        buttons: {
                            ok: {
                                label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                            }
                        },
                        callback: function (result) {
                        }
                    });
                    return;
                } else if (response.matchFound == true) {
                    $('#fullNameInBangla').val(response.nidData.name === "null" ? "" : response.nidData.name);
                    $('#fullNameInEnglish').val(response.nidData.nameEn === "null" ? "" : response.nidData.nameEn);
                    $('#fatherName').val(response.nidData.father === "null" ? "---" : response.nidData.father);
                    $('#motherName').val(response.nidData.mother === "null" ? "---" : response.nidData.mother);
                    $('#spouseName').val(response.nidData.spouse === "null" ? "" : response.nidData.spouse);
                    //                    document.getElementById('profilePhotoFile').setAttribute ('src','data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==');
                    $('#profilePhotoFile').attr('src', 'data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==');
                    // $('#profilePhotoFile').attr('src', 'data:image/jpeg;base64,' + response.nidData.photo);
                } else {
                    bootbox.dialog({
                        onEscape: function () {},
                        title: '<spring:message code="label.failure" />',
                        message: '<spring:message code="label.matchNotFound" />',
                        buttons: {
                            ok: {
                                label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                            }
                        },
                        callback: function (result) {
                        }
                    });

                    $('#fullNameInBangla').val("");
                    $('#fullNameInEnglish').val("");
                    $('#fatherName').val("");
                    $('#motherName').val("");
                    $('#spouseName').val("");
                }
            },
            failure: function () {
                log("loading NID data failed!!");
                return "error in loading data";
            }
        });

    }
    function loadPermDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#permDistrict');
        if (divId !== '') {
            loadDistrict(divId, distSelectId, '${beneficiaryForm.permanentDistrict.id}');
        } else {
            resetSelect(distSelectId);
            resetSelect($('#permUpazila'));
            resetSelect($('#permUnion'));
        }
    }
    function loadPresentDistrictList(selectObject) {
        var divId = selectObject.value;
        var distSelectId = $('#presentDistrict');
        if (divId !== '') {
            loadDistrict(divId, distSelectId, '${beneficiaryForm.presentDistrict.id}');
        } else {
            resetSelect(distSelectId);
            resetSelect($('#presentUpazila'));
            resetSelect($('#presentUnion'));
        }
    }
    function loadPermUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#permUpazila');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId, '${beneficiaryForm.permanentUpazila.id}');
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#permUnion'));
        }
    }
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#presentUpazila');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId, '${beneficiaryForm.presentUpazila.id}');
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#presentUnion'));
        }
    }

    function loadPermUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#permUnion');

        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId, '${beneficiaryForm.permanentUnion.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#presentUnion');

        if (upazilaId !== '') {
            loadUnion(upazilaId, unionSelectId, '${beneficiaryForm.presentUnion.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }
    function loadPresentVillageList(selectObject) {
        var wardNo = getNumberInEnglish(selectObject.value);
        var unionId = $('#presentUnion').val();
        var villageSelectId = $('#presentVillage');
        if (unionId !== '' && wardNo !== '') {
            loadVillage(unionId, wardNo, villageSelectId, '${beneficiaryForm.presentVillage.id}');
        } else {
            resetSelect(villageSelectId);
        }
    }
    function loadPermanentVillageList(selectObject) {
        var wardNo = getNumberInEnglish(selectObject.value);
        var unionId = $('#permUnion').val();
        var villageSelectId = $('#permanentVillage');
        if (unionId !== '' && wardNo !== '') {
            loadVillage(unionId, wardNo, villageSelectId, '${beneficiaryForm.permanentVillage.id}');
        } else {
            resetSelect(villageSelectId);
        }
    }
    function loadPermanentAddressSameAsPresent(object) {
        //        $('#permanentAddressLine1').val($('#presentAddressLine1').val());
        $('#permanentAddressLine2').val($('#presentAddressLine2').val());
        $('#permDivision').val($('#presentDivision').val());
        loadPermDistrictList($('#permDivision')[0]);
        $('#permDistrict').val($('#presentDistrict').val());
        loadPermUpazilaList($('#permDistrict')[0]);
        $('#permUpazila').val($('#presentUpazila').val());
        loadPermUnionList($('#permUpazila')[0]);
        $('#permUnion').val($('#presentUnion').val());
        $('#permanentWardNo').val($('#presentWardNo').val());
        loadPermanentVillageList($('#permanentWardNo')[0]);
        $('#permanentVillage').val($('#presentVillage').val());
        $('#permanentPostCode').val($('#presentPostCode').val());
        makePermanentAddressReadOnly(true);
    }
    function resetPermanentAddress() {
        //        $('#permanentAddressLine1').val("");
        $('#permanentAddressLine2').val("");
        resetSelect($('#permDivision'));
        loadDivision($("select#permDivision"));
        resetSelect($('#permDistrict'));
        resetSelect($('#permUpazila'));
        resetSelect($('#permUnion'));
        //        resetSelect($('#permanentWardNo'));
        resetSelect($('#permanentVillage'));
        //        $('#permanentWardNo').val("");
        $('#permanentPostCode').val("");
        makePermanentAddressReadOnly(false);
    }
    function makePermanentAddressReadOnly(value) {
        // make fields read only
        $('#permanentAddressLine1').attr("disabled", value);
        $('#permanentAddressLine2').attr("disabled", value);
        $("#permDivision").attr("disabled", value);
        $("#permDistrict").attr("disabled", value);
        $("#permUpazila").attr("disabled", value);
        $("#permUnion").attr("disabled", value);
        $("#permanentWardNo").attr("disabled", value);
        $("#permanentVillage").attr("disabled", value);
        //        $("#permanentWardNo").attr("disabled", value);
        $("#permanentPostCode").attr("disabled", value);
    }
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
            accountNoLength_js = 17;
        } else if (paymentType === 'POSTOFFICE')
        {
            var pobSelectId = $('#postOfficeBranch');
            var districtId = $("#presentDistrict").val();
            loadPostOfficeBranch(pobSelectId, '${beneficiaryForm.postOfficeBranch.id}', districtId);
            $("#bankGroupInfo").css("display", "none");
            $("#mobileBankingGroupInfo").css("display", "none");
            $("#postOfficeGroupInfo").css("display", "block");
            accountNoLength_js = 17;
        }
    }
    function loadBranchList(selectObject) {
        var idName = $(selectObject).attr('id');
        if (idName == "mobileBankingProvider")
        {
            var accountNoLength = $("#mobileBankingProvider :selected").attr("length");
            if (accountNoLength == "-1" || accountNoLength == "undefined" || accountNoLength == "0") {
                accountNoLength = 100;

            } else
            {
                accountNoLength = parseInt(accountNoLength);
            }
            accountNoLength_js = accountNoLength;
            return;
        } else {
            var bankId = selectObject.value;
            var branchSelectId = $('#branch');
            var districtId = $("#presentDistrict").val();
            if (bankId !== '') {
                loadBranchByDistrict(bankId, districtId, branchSelectId, '${beneficiaryForm.branch.id}');
            } else {
                resetSelect(branchSelectId);
            }
            console.log(bankId);
            var accountNoLength = $("#bank :selected").attr("length");
            if (accountNoLength == "-1" || accountNoLength == "undefined" || accountNoLength == "0") {
                accountNoLength = 100;

            } else
            {
                accountNoLength = parseInt(accountNoLength);
            }
            accountNoLength_js = accountNoLength;
        }
    }
    function removeProfilePhoto()
    {
        document.getElementById("profilePhotoFile").style.display = "none";//
        document.getElementById("removeProfilePhoto").style.display = "none";//
        $("#profilePhotoFile").val("");
        $("#photo").attr("style", "display:block");
    }
    function removeSignature()
    {
        $("#signatureFile").remove();
        document.getElementById("removeSignature").style.display = "none";//
        $("#signature").attr("style", "display:block");
    }

    function submitForm() {
        var form = $('#beneficiaryInfoForm');
        form.validate();
        var validity = form.valid();
        var found = false;
        if (!uniqueAccountNoCheckForBeneficiary('<spring:message code="label.failure" />', '<spring:message code="label.ok" />'))
        {
            return;
        }
        if ($("#removeList").val() != "")
        {
            var list = $("#removeList").val().split(",");
            for (var i = 0; i < list.length; ++i) {
                if (list[i] == "${beneficiaryForm.attachmentList[0].id}") {
                    found = true;
                    break;
                }
            }
        }
        if (typeof ($('#multipartFileList0').val()) !== "undefined" && $('#multipartFileList0').val() !== "")
        {
            $("#showAttachmentError").hide();
        } else if ("${beneficiaryForm.attachmentList[0]}" != "")
        {
            if (found == false)
            {
                $("#showAttachmentError").hide();
            } else
            {
                $("#showAttachmentError").show();
                return false;
            }
        } else
        {
            $("#showAttachmentError").show();
            return false;
        }
        if (validity) {
            makePermanentAddressReadOnly(false);
            $('#age').attr("disabled", false);
            $("#fiscalYear").attr("disabled", false);
            if (selectedLocale === "bn")
            {
                $('#submitButton').text("সংরক্ষিত হচ্ছে");
                $("#dateOfBirth").val(getNumberInEnglish($("#dateOfBirth").val()));
            } else {
                $('#submitButton').text("Saving");
            }
            $('#submitButton').prop("disabled", true);
            $('#beneficiaryInfoForm').submit();
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
        <c:set var="actionUrl" value="${contextPath}/beneficiary/edit/${beneficiaryForm.id}" />
    </c:otherwise>
</c:choose>

<form:form id="beneficiaryInfoForm" action="${actionUrl}" class="form-horizontal" enctype="multipart/form-data" role="form" modelAttribute="beneficiaryForm">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code='beneficiary'/>&nbsp;<spring:message code='label.management'/> 
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/beneficiary/union/list"><spring:message code="label.backToList"/></a></small>
        </h1>
        <div class="pull-right">
            <button type="button" name="save" id="submitButton" class="btn bg-blue" onClick="return submitForm()">
                <i class="fa fa-floppy-o"></i>
                <spring:message code="save" />
            </button>            
        </div>   
    </section>

    <section class="content">
        <div class="row">                    
            <form:hidden path="id" />                                  
            <form:hidden path="applicantType" id="applicantType"/>
            <input type="hidden" id="removeList" name="removeList"/>
            <spring:message code='label.select' var="select"/>
            <div class="col-md-12">
                <fieldset>
                    <legend>


                        <spring:message code='label.BasicInfoTab' var="basicInfoTab"/>${basicInfoTab}
                        <span class="pull-right" style="font-size:14px;padding-top: 5px">
                            <spring:message code="label.mandatoryPart1"/>
                            <span style="color: red;">
                                <c:if test="${beneficiaryForm.isLMMISExit eq true}">LM-MIS এর ভাতাভোগী</c:if>
                                </span> 
                                <span style="color: red;">(*)</span>
                                <span><spring:message code="label.mandatoryPart2"/></span>
                        </span>
                    </legend>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="paymentCycle.label.fiscalYear" /><span class="mandatory">*</span></label>
                            <div class="col-md-8 ">                            
                                <form:select class="form-control" path="fiscalYear.id" id="fiscalYear" autofocus="autofocus">
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${fiscalYearList}" itemValue="id" itemLabel="${fiscalYearName}"></form:options>
                                </form:select>
                                <form:errors path="fiscalYear" cssStyle="color:red"></form:errors>
                                </div>                                                    
                            </div>
                            <div class="form-group"> 
                                <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.nid" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <c:if test="${actionType eq 'create'}">
                                    <div class="input-group">
                                        <spring:message code='label.nid' var="nid"/>
                                        <!--      <form:input class="form-control" placeholder="${nid}" path="nid" autofocus="autofocus" readonly="${actionType ne 'create'}"
                                                    onkeydown="checkNumberWithLength(event, this, 17)"/> -->
                                        <form:input class="form-control" placeholder="${nid}" path="nid" autofocus="autofocus" 
                                                    onkeydown="checkNumberWithLength(event, this, 17)"/>
                                        <spring:message code='label.getNidData' var="getNidData"/>
                                        <span class="input-group-btn">
                                            <button class="btn btn-secondary" type="button" onclick="getNIDData($('#nid').val(), $('#dateOfBirth').val())" >${getNidData}</button>
                                        </span>

                                    </div>
                                    <form:errors path="nid" cssStyle="color:red"></form:errors>
                                </c:if>
                                <c:if test="${actionType ne 'create'}">  
                                    <div class="input-group">
                                        <spring:message code='label.nid' var="nid"/>
                                        <!--      <form:input class="form-control" placeholder="${nid}" path="nid" autofocus="autofocus" readonly="${actionType ne 'create'}"
                                                    onkeydown="checkNumberWithLength(event, this, 17)"/> -->
                                        <form:input class="form-control" placeholder="${nid}" path="nid" autofocus="autofocus" 
                                                    onkeydown="checkNumberWithLength(event, this, 17)"/>
                                        <spring:message code='label.checkNid' var="checkNid"/>
                                        <span class="input-group-btn">
                                            <button class="btn btn-secondary" type="button" onclick = "doNIDCheck('${beneficiaryForm.id}')" >${checkNid}</button>
                                        </span>
                                        <form:errors path="nid" cssStyle="color:red"></form:errors>
                                        </div>
                                </c:if>  
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.dob" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.dob' var="dob"/>
                                <form:input class="form-control" placeholder="${dob}" path="dateOfBirth" autofocus="autofocus" readonly="true" onchange="checkDob();"/>
                                <form:errors path="dateOfBirth" cssStyle="color:red"></form:errors>
                                </div>                                
                            </div> 
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                            <div class="col-md-8 ">
                                <spring:message code='label.nameBn' var="fullNameBn"/>
                                <form:input class="form-control" placeholder="${fullNameBn}" path="fullNameInBangla" readonly="true" autofocus="autofocus" />
                                <form:errors path="fullNameInBangla" cssStyle="color:red"></form:errors>
                                </div>                                                    
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.nameEn' var="fullNameEn"/>
                                <form:input class="form-control" placeholder="${fullNameEn}" path="fullNameInEnglish" autofocus="autofocus" readonly="true" onkeydown="checkEnglishAlphabetWithLength(event, this, 60)"/>
                                <form:errors path="fullNameInEnglish" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.fatherName" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.fatherName' var="fatherName"/>
                                <form:input class="form-control" placeholder="${fatherName}" path="fatherName" autofocus="autofocus" readonly="true" onkeydown="checkAlphabetWithLength(event, this, 30)"/>
                                <form:errors path="fatherName" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.motherName" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.motherName' var="motherName"/>
                                <form:input class="form-control" placeholder="${motherName}" path="motherName" autofocus="autofocus" readonly="true" onkeydown="checkAlphabetWithLength(event, this, 30)"/>
                                <form:errors path="motherName" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.spouseName" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.spouseName' var="spouseName"/>
                                <form:input class="form-control" placeholder="${spouseName}" path="spouseName" autofocus="autofocus" onkeydown="checkAlphabetWithLength(event, this, 30)"/>
                                <form:errors path="spouseName" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>                             
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nickName" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.nickName' var="nickName"/>
                                <form:input class="form-control" placeholder="${nickName}" path="nickName" autofocus="autofocus" onkeydown="checkAlphabetWithLength(event, this, 30)"/>
                                <form:errors path="nickName" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.birthPlace" /><span class="mandatory">*</span></label>                            
                            <div class="col-md-8">
                                <spring:message code='label.birthPlace' var="birthPlace"/>
                                <form:select class="form-control" path="birthPlace.id" id="birthPlace">
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${birthPlaceList}" itemValue="id" itemLabel="${birthPlaceName}"></form:options>
                                </form:select>
                                <form:errors path="birthPlace" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>
                            <div class="form-group">
                                <label for="religionInput" class="col-md-4 control-label"><spring:message code="label.religion" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.religion' var="religion"/>
                                <form:select class="form-control" path="religionEnum">
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${religionEnumList}"  itemLabel="${religionDisplayName}"></form:options>
                                </form:select>
                                <form:errors path="religionEnum" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>
                            <div class="form-group">
                                <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.mobileNo" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.mobileNo' var="mobileNo"/>
                                <form:input class="form-control" placeholder="${mobileNo}" path="mobileNo" autofocus="autofocus" />
                                <form:errors path="mobileNo" cssStyle="color:red"></form:errors>
                                </div> 
                            </div>    
                            <div class="form-group">
                                <label for="educationLevelInput" class="col-md-4 control-label"><spring:message code="label.educationLevel" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.educationLevel' var="educationLevel"/>
                                <form:select class="form-control" path="educationLevelEnum">
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${educationLevelEnumList}"  itemLabel="${educationLevelDisplayName}"></form:options>
                                </form:select>
                                <form:errors path="educationLevelEnum" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>
                            <div class="form-group">
                                <input type="hidden" name="gender" value="FEMALE">
                                <label for="bloodGroupInput" class="col-md-4 control-label"><spring:message code="label.bloodGroup" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.bloodGroup' var="bloodGroup"/>                            
                                <form:select class="form-control" path="bloodGroup" id="bloodGroup">
                                    <form:option value="" label="${select}"></form:option>
                                    <c:if test="${pageContext.response.locale=='bn'}">
                                        <form:options items="${bloodGroupList}"  itemLabel="displayNameBn"></form:options>
                                    </c:if>
                                    <c:if test="${pageContext.response.locale=='en'}">
                                        <form:options items="${bloodGroupList}"  itemLabel="displayName"></form:options>
                                    </c:if>
                                </form:select>
                                <form:errors path="bloodGroup" cssStyle="color:red"></form:errors>
                                </div>                                                
                            </div>                            
                            <div class="form-group">
                                <label for="maritalInfoInput" class="col-md-4 control-label"><spring:message code="label.maritalInfo" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.maritalInfo' var="maritalInfo"/>
                                <form:select class="form-control" path="maritalInfoEnum">
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${maritalInfoEnumList}"  itemLabel="${maritalInfoDisplayName}"></form:options>
                                </form:select>
                                <form:errors path="maritalInfoEnum" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>                            
                        <c:if test="${sessionScope.userDetail.scheme.shortName eq 'LMA'}">                                    
                            <div class="form-group">
                                <label for="occupation" class="col-md-4 control-label"><spring:message code="label.occupation" /></label>
                                <div class="col-md-8">
                                    <form:select class="form-control" path="occupation">
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${occupationList}"  itemLabel="${displayName}"></form:options>
                                    </form:select>
                                    <form:errors path="occupation" cssStyle="color:red"></form:errors>
                                    </div>            
                                </div>
                        </c:if>                        
                        <div class="form-group">
                            <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.nrb" /></label>
                            <div class="col-md-2">
                                <spring:message code='label.nrb' var="nrb"/>
                                <form:checkbox id="nrb" path="nrb"/>
                                <form:errors path="nrb" cssStyle="color:red"></form:errors>
                                </div>
                                <div class="col-md-6" >
                                    <p><strong>* <spring:message code="label.nrbNote"/></strong></p>
                            </div>   
                        </div>                                
                    </div>
                </fieldset>
            </div>
            <!--<div class="col-md-12">-->                    

            <div class="col-md-6">
                <fieldset>
                    <legend>
                        <spring:message code='label.presentAddress' var="presentAddress"/>
                        ${presentAddress}
                    </legend>                   
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                        <c:choose>
                            <c:when test="${beneficiaryForm.isDivisionAvailable eq 'false'}">
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="presentDivision.id" id="presentDivision" onchange="loadPresentDistrictList(this)">
                                        <form:option value="" label="${select}"></form:option>
                                        <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options> 
                                    </form:select>
                                    <form:errors path="presentDivision.id" cssStyle="color:red"></form:errors>
                                    </div>
                            </c:when>
                            <c:otherwise>
                                <div class="col-md-8 labelAsValue">
                                    <form:hidden id="presentDivision" path="presentDivision.id"/>
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${beneficiaryForm.presentDivision.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${beneficiaryForm.presentDivision.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:otherwise>
                        </c:choose>     
                    </div>
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                        <c:choose>
                            <c:when test="${beneficiaryForm.isDistrictAvailable eq 'false'}">
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="presentDistrict.id" id="presentDistrict" onchange="loadPresentUpazilaList(this)">
                                        <form:option value="" label="${select}"></form:option>
                                    </form:select>
                                    <form:errors path="presentDistrict.id" cssStyle="color:red"></form:errors>
                                    </div>
                            </c:when>
                            <c:otherwise>
                                <div class="col-md-8 labelAsValue">
                                    <form:hidden id="presentDistrict" path="presentDistrict.id"/>
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">
                                            ${beneficiaryForm.presentDistrict.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${beneficiaryForm.presentDistrict.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:otherwise>
                        </c:choose>   
                    </div>
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label">
                            <spring:message code="label.upazila" />
                            <span class="mandatory">*</span></label>                                
                            <c:choose>
                                <c:when test="${beneficiaryForm.isUpazilaAvailable eq 'false'}">
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="presentUpazila.id" id="presentUpazila" onchange="loadPresentUnionList(this)">
                                        <form:option value="" label="${select}"></form:option>                                            
                                    </form:select>
                                    <form:errors path="presentUpazila.id" cssStyle="color:red"></form:errors>
                                    </div>
                            </c:when>
                            <c:otherwise>
                                <div class="col-md-8 labelAsValue">
                                    <form:hidden id="presentUpazila" path="presentUpazila.id"/>
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${beneficiaryForm.presentUpazila.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${beneficiaryForm.presentUpazila.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:otherwise>
                        </c:choose>       
                    </div>
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label">                           
                            <spring:message code="label.union" />                           
                            <span class="mandatory">*</span></label>
                            <c:choose>
                                <c:when test="${beneficiaryForm.isUnionAvailable eq 'false'}">
                                <div class="col-md-8">
                                    <spring:message code='label.select' var="select"/>
                                    <form:select class="form-control" path="presentUnion.id" id="presentUnion">
                                        <form:option value="" label="${select}"></form:option>                                            
                                    </form:select>
                                    <form:errors path="presentUnion.id" cssStyle="color:red"></form:errors>
                                    </div>
                            </c:when>
                            <c:otherwise>
                                <div class="col-md-8 labelAsValue">
                                    <form:hidden id="presentUnion" path="presentUnion.id"/>
                                    <c:choose>
                                        <c:when test="${pageContext.response.locale eq 'bn'}">                                                    
                                            ${beneficiaryForm.presentUnion.nameInBangla}
                                        </c:when>
                                        <c:otherwise>
                                            ${beneficiaryForm.presentUnion.nameInEnglish}
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:otherwise>
                        </c:choose>   
                    </div>
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.wardNo" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.wardNo' var="wardNo"/>
                            <form:select class="form-control" path="presentWardNo" onchange="loadPresentVillageList(this)">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${wardNoList}"></form:options> 
                            </form:select>
                            <form:errors path="presentWardNo" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="presentVillage.id" class="col-md-4 control-label"><spring:message code="label.village" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="presentVillage.id" id="presentVillage">
                                <form:option value="" label="${select}"></form:option>                                            
                            </form:select>
                            <form:errors path="presentVillage.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>    
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.postCode" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.postCode' var="postCode"/>
                            <form:input class="form-control" placeholder="${postCode}" path="presentPostCode" autofocus="autofocus"/>
                            <form:errors path="presentPostCode" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine2" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.addressLine2' var="addressLine2"/>
                            <form:input class="form-control" placeholder="${addressLine2}" path="presentAddressLine2" autofocus="autofocus"/>
                            <form:errors path="presentAddressLine2" cssStyle="color:red"></form:errors>
                            </div>
                        </div>    
                    </fieldset>
                </div>
                <div class="col-md-6">
                    <fieldset>
                        <legend>
                        <spring:message code='label.permanentAddress' var="permanentAddress"/>                                            
                        ${permanentAddress}
                        <span class="pull-right">
                            <spring:message code='label.sameAsPresent' var="sameAsPresent"/>
                            <input type="checkbox" id="sameAsPresent" onclick="loadPermanentAddressSameAsPresent(thi
                                            s);">${sameAsPresent}</input>
                        </span>
                    </legend>                    
                    <div class="form-group">
                        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="permanentDivision.id"  id="permDivision" onchange="loadPermDistrictList(this)"> 
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${divisionList}" itemValue="id" itemLabel="${divisionName}"></form:options> 
                            </form:select>                                                
                            <form:errors path="permanentDivision.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="permanentDistrict.id" id="permDistrict" onchange="loadPermUpazilaList(this)">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${districtList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                            </form:select>
                            <form:errors path="permanentDistrict.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label">
                            <spring:message code="label.upazila" />
                            <span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="permanentUpazila.id" id="permUpazila" onchange="loadPermUnionList(this)">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${upazilaList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                            </form:select>
                            <form:errors path="permanentUpazila.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label">
                            <spring:message code="label.municipal"/>
                            <span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="permanentUnion.id" id="permUnion">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${unionList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                            </form:select>
                            <form:errors path="permanentUnion.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">  
                            <label for="permanentWardNo" class="col-md-4 control-label"><spring:message code="label.wardNo" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.wardNo' var="wardNo"/>
                            <form:select class="form-control" path="permanentWardNo" onchange="loadPermanentVillageList(this)">
                                <form:option value="" label="${select}"></form:option>
                                <form:options items="${wardNoList}"></form:options> 
                            </form:select>
                            <form:errors path="permanentWardNo" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="permanentVillage.id" class="col-md-4 control-label"><spring:message code="label.village" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.select' var="select"/>
                            <form:select class="form-control" path="permanentVillage.id" id="permanentVillage">
                                <form:option value="" label="${select}"></form:option>                                            
                            </form:select>
                            <form:errors path="permanentVillage.id" cssStyle="color:red"></form:errors>
                            </div>
                        </div>          
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.postCode" /><span class="mandatory">*</span></label>
                        <div class="col-md-8">
                            <spring:message code='label.postCode' var="postCode"/>
                            <form:input class="form-control" placeholder="${postCode}" path="permanentPostCode" autofocus="autofocus"/>
                            <form:errors path="permanentPostCode" cssStyle="color:red"></form:errors>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="addressLine2Input" class="col-md-4 control-label"><spring:message code="label.addressLine2" /></label>
                        <div class="col-md-8">
                            <spring:message code='label.addressLine2' var="addressLine2"/>
                            <form:input class="form-control" placeholder="${addressLine2}" path="permanentAddressLine2" autofocus="autofocus"/>
                            <form:errors path="permanentAddressLine2" cssStyle="color:red"></form:errors>
                            </div>
                        </div>    
                    </fieldset>
                </div>                                
                <!--                </div>-->
                <div class="col-md-12">
                    <div class="col-md-6">
                        <fieldset>
                            <legend>
                            <spring:message code='label.socioEconomicInfoTab' var="socioEconomicInfoTab"/>${socioEconomicInfoTab}
                        </legend>
                        <jsp:include page="beneficiarySocioEconomicInfoRural.jsp"></jsp:include>  
                        </fieldset>
                    </div>    
                    <div class="col-md-6">
                        <fieldset>
                            <legend>
                            <spring:message code='label.HealthInfoTab' var="healthInfoTab"/>${healthInfoTab}
                        </legend>
                        <div class="form-group">
                            <label for="" class="col-md-4 control-label"><spring:message code="label.conceptionTerm" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <form:select class="form-control" path="conceptionTerm">
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${conceptionTermEnum}"  itemLabel="${displayName}"></form:options>
                                </form:select>
                                <form:errors path="conceptionTerm" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>
                            <div class="form-group">
                                <label for="" class="col-md-4 control-label">
                                <spring:message code='label.conceptionDuration'/>    
                                <spring:message code='label.conceptionDuration' var='conceptionDuration'/>
                                <span class="mandatory">*</span>
                            </label>
                            <div class="col-md-8">
                                <form:input class="form-control" placeholder="${conceptionDuration}" path="conceptionDuration" onkeydown="checkNumberWithLength(event, this, 2)"/>
                                <form:errors path="conceptionDuration" cssStyle="color:red"></form:errors>
                                </div>
                            </div>  
                        </fieldset>
                        <fieldset>
                            <legend>
                            <spring:message code='label.BankAccountInfoTab' var="bankAccountInfoTab"/>${bankAccountInfoTab}
                        </legend>
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
                                    <form:select class="form-control" path="branch.id"  id="branch"  onchange="loadAccountLength_(this)"> 
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
                                <form:input class="form-control" cssStyle="text-transform:uppercase" placeholder="${accountName}" readonly="true" path="accountName" autofocus="autofocus" onkeydown="checkEnglishAlphabetWithLength(event, this, 60)"/>
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

                <div class="col-md-12"> 
                    <div class="col-md-6">
                        <fieldset>
                            <legend>
                            <spring:message code='label.BiometricInfoTab' var="biometricInfoTab"/>${biometricInfoTab}
                        </legend>
                        <div class="form-group">
                            <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.photo" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='placeholder.photo' var="photo"/>                                    
                                <div id="photo">
                                    <input id="photoInput" name="photo" type="file" class="file" accept="image/jpg,image/png,image/jpeg,image/gif" data-allowed-file-extensions='["jpg", "jpeg", "png", "gif"]'>
                                </div>
                                <c:if test="${beneficiaryForm.photoPath !='' && beneficiaryForm.photoPath != null}">
                                    <img id="profilePhotoFile" style="width: 50%; border-radius: 20px" alt="img" src="data:image/jpeg;base64,${beneficiaryForm.photoData}"/>
                                    <input type="image" src="${contextPath}/resources/img/remove16.png" id="removeProfilePhoto" value="Remove" onclick="removeProfilePhoto();
                                            return fal
                                            se;" /> 
                                </c:if>
                                <form:errors path="photo" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>
                            <div class="form-group">
                                <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.signature" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='placeholder.signature' var="signature"/>                                    

                                <div id="signature">
                                    <input id="signatureInput" name="signature" type="file" class="file" accept="image/jpg,image/png,image/jpeg,image/gif"  data-allowed-file-extensions='["jpg", "jpeg", "png", "gif"]'>
                                </div>

                                <c:if test="${beneficiaryForm.signaturePath !='' && beneficiaryForm.signaturePath != null}">
                                    <img id="signatureFile" style="width: 20%; border-radius: 5px" alt="img" src="data:image/jpeg;base64,${beneficiaryForm.signatureData}"/>
                                    <input type="image" src="${contextPath}/resources/img/remove16.png" id="removeSignature" value="Remove" onclick="removeSignature();
                                            return fal
                                            se;" /> 
                                </c:if>
                                <form:errors path="signature" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>    
                        </fieldset>
                    </div>

                    <div class="col-md-6"> 
                        <fieldset>
                            <legend>
                            <spring:message code='label.AttachmentTab' var="attachmentTab"/>${attachmentTab}
                        </legend>
                        <div class="form-group">
                            <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.ancCard" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <div id="ancCard">
                                    <input id="multipartFileList0" name="multipartFileList[0]" type="file" class="file" accept="image/jpg,image/png,image/jpeg,image/gif" data-allowed-file-extensions='["jpg", "jpeg", "png", "gif"]'>
                                </div>
                                <c:forEach begin="0" end="0" items="${beneficiaryForm.attachmentList}" var="attachment" varStatus="index">                        
                                    <c:if test="${attachment !='' && attachment != null}">
                                        <div id="dvAttachment${index.index}" class="form-group">
                                            <div class="col-md-12">
                                                <label for="nidInput" class="col-md-4">${attachment.attachmentName}</label>
                                                <div class="col-md-6">
                                                    <a id="attachment${index.index}" href="${pageContext.request.contextPath}/getFile/${applicantNid}/${attachment.fileName}/anc" target="_blank" download="${attachment.fileName}" title="Click to open">${attachment.fileName}</a>
                                                    &nbsp;<input type="image" src="${contextPath}/resources/img/remove16.png" id="removeFile[${index.index}]" value="Remove" onclick="removeAncAttachment(${index.index}, ${attachment.id});
                                                            return fal
                                                            se;" /> 
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                                <div id="showAttachmentError" style="color: red; font-weight: bold; display: none">
                                    <label><spring:message code='label.requiredField'></spring:message></label>
                                    </div>  
                                </div> 
                            <c:forEach begin="1" items="${beneficiaryForm.attachmentList}" var="attachment" varStatus="index">                        
                                <c:if test="${attachment !='' && attachment != null}">
                                    <div id="dvAttachment${index.index}" class="form-group">
                                        <div class="col-md-12">
                                            <label for="nidInput" class="col-md-2"><span id="attachmentIndex${index.index}">${index.index}</span></label>
                                            <label for="nidInput" class="col-md-4">${attachment.attachmentName}</label>
                                            <div class="col-md-6">
                                                <a id="attachment${index.index}" href="${pageContext.request.contextPath}/getFile/${applicantNid}/${attachment.fileName}/others" target="_blank" download="${attachment.fileName}" title="Click to open">${attachment.fileName}</a>
                                                &nbsp;<input type="image" src="${contextPath}/resources/img/remove16.png" id="removeFile[${index.index}]" value="Remove" onclick="removeAttachment(${index.index}, ${attachment.id});
                                                        return fal
                                                        se;" /> 
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                        <spring:message var="addNew" code="addNew"/>
                        <div class="form-group">
                            <div class="col-md-6">                            
                                <button id="addFile" class="" type="button" ><span class="glyphicon glyphicon-pushpin"/>  ${addNew}</button>
                            </div>
                        </div>
                        <div id="fileTable">
                        </div>        
                    </fieldset>
                </div>
            </div> 
        </div>
        <a href="#" class="back-to-top" style=""><i class="fa fa-arrow-circle-up"></i></a>                        
    </section>    
</form:form>
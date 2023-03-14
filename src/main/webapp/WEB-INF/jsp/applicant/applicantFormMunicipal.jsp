<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="<c:url value="/resources/plugins/fileUpload/fileinput.min.js" />" ></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />"></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/backToTop.js" />" ></script> 
<script src="<c:url value="/resources/js/utility.js?v1=${asset}" />" ></script>
<script src="<c:url value="/resources/js/unijoy.js" />" ></script> 
<link rel="stylesheet" href="<c:url value="/resources/plugins/fileUpload/fileinput.min.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
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
//    var upazilaList = [114, 662, 8134, 8527, 9141, 395, 7263];
    //var upazilaList = [114, 395, 662, 1376, 2294, 2743, 4680, 4730, 4859, 5063, 5566, 5835, 6944, 7263, 7895, 7976, 8134, 8247, 8429, 8527, 8867, 9141, 9159, 9376, 9494];

    function replaceSpecialChar(str) {

        var result = str.replace(/[^\u0995-\u09B9\u09CE\u09DC-\u09DF\u0985-\u0994\u09BE-\u09CE\u09D7\u09AF\u09BC০-৯a-z0-9-.\s]/gi, '').replace(/[-]/gi, ' ');
        return result;
    }
    $(function () {

        //if this page for Edit NID and Dateof birht filed are read only url,isEdit varaible use for readOnly
        var url = window.location.pathname.toString();
        var isEdit = 0;
        if (url.indexOf("/edit/") !== -1)
        {
            $('#nid').attr("readonly", true);
            $('#dateOfBirth').attr("readonly", true);
            isEdit = 1;

        } else
        {
            initDate($("#dateOfBirth"), '${dateFormat}', $("#dateOfBirth\\.icon"), selectedLocale);
        }
        //NID and DOB readonly done 

        if ('${actionType}' === 'create')
        {
            $(menuSelect("${pageContext.request.contextPath}/applicant/municipal/create"));
        } else
        {
            $(menuSelect("${pageContext.request.contextPath}/applicant/municipal/list"));
        }
        includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
//        initDate($("#dateOfBirth"), '${dateFormat}', $("#dateOfBirth\\.icon"), selectedLocale);
//        makeUnijoyEditor('fullNameInBangla');
        if (selectedLocale === 'bn') {
//            makeUnijoyEditor('fatherName');
//            makeUnijoyEditor('motherName');
            makeUnijoyEditor('spouseName');
            makeUnijoyEditor('nickName');
            makeUnijoyEditor('mobileNo');
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
            makeUnijoyEditor('presentAddressLine1');
            makeUnijoyEditor('presentAddressLine2');
            makeUnijoyEditor('presentWardNo');
            makeUnijoyEditor('presentPostCode');
            makeUnijoyEditor('permanentAddressLine1');
            makeUnijoyEditor('permanentAddressLine2');
            makeUnijoyEditor('permanentWardNo');
            makeUnijoyEditor('permanentPostCode');
//            makeUnijoyEditor('accountName');
            makeUnijoyEditor('accountNo');

        }
        // for some reason, fullNameInBangla is not working in first time load, so, I just change the focus and again focused in first field.
//        $("#fatherName").focus();

        var mobileNo = document.getElementById("mobileNo");
        mobileNo.addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 11);
        });
        var nid = document.getElementById("nid");
        nid.addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 17);
        });
        $("#beneficiaryInOtherScheme").parent().addClass("pull-right");
        document.getElementById("presentWardNo").addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 2);
        });
        document.getElementById("presentPostCode").addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 4);
        });
        document.getElementById("permanentWardNo").addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 2);
        });
        document.getElementById("permanentPostCode").addEventListener("keydown", function (event) {
            checkNumberWithLength(event, this, 4);
        });
        document.getElementById("accountName").addEventListener("keydown", function (event) {
            checkAlphabetWithLength(event, this, 50);
        });
        document.getElementById("accountNo").addEventListener("keydown", function (event) {
            checkNumberWithLengthForAccountNo(event, this, 17);
        });
        $('#fullNameInEnglish').change(function () {
            $('#accountName').val("");
            $('#accountName').val($(this).val());
        });
        $("#applicantInfoForm").validate({

            rules: {// checks NAME not ID
                "fullNameInBangla": {
                    required: true,
//                    banglaAlphabet: true
                },
                "fullNameInEnglish": {
                    required: true,
//                    englishAlphabet: true
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
                    checkDateOfBirth: {locale: selectedLocale, min: 20, max: 35}
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
                    uniqueNid: true
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
//                "presentPostCode": {
//                    required: true,
////                    min: 4,
////                    max: 4
//                },
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
//                    checkApplicationDeadline: true
                },
                "permanentUnion.id": {
                    required: true
                            // checkQuotaAllocation: true  TEMPORARY COMMENTED 31/01/18
                },
//                "permanentWardNo": {
//                    required: true
//                },
                "permanentPostCode": {
                    required: true
//                    min: 4,
//                    max: 4
                },
                "hasResidenceUrban": {
                    required: true
                },
                "occupationUrban": {
                    required: true
                },
                "monthlyIncome": {
                    required: true
                },
                "hASKitchenUrban": {
                    required: true
                },
                "hASElectricity": {
                    required: true
                },
                "hASElectricFan": {
                    required: true
                },
                "hASTelivisionUrban": {
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
//                    uniqueAccountNumber: true
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
                        return selectedLocale === "bn" ? "এই তথ্যটি আবশ্যক।" : "This field is required ";
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
        $("#dateOfBirth").change(function () {
            var dateString = $("#dateOfBirth").val();
            if (selectedLocale === "bn")
            {
                dateString = getNumberInEnglish(dateString);
            }
            const[day, month, year] = dateString.split("-");
            var today = new Date();
            var birthDate = new Date(year, month - 1, day);
            var age = today.getFullYear() - birthDate.getFullYear();
            var m = today.getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate()))
            {
                age--;
            }
            if (selectedLocale === "bn")
            {
                age = getNumberInBangla(age.toString());
            }
            console.log("age: " + age);
            $("#age").val(age);
        });
        $('#sameAsPresent').on('ifChecked', function () {
            loadPermanentAddressSameAsPresent(this);
        });
        $('#sameAsPresent').on('ifUnchecked', function () {
            resetPermanentAddress();
        });

        if ('${applicantForm.isDistrictAvailable}' === 'false' && '${applicantForm.presentDivision.id}' !== "")
        {
            loadDistrict('${applicantForm.presentDivision.id}', $('#presentDistrict'));
        } else if ('${applicantForm.isUpazilaAvailable}' === 'false' && '${applicantForm.presentDistrict.id}' !== "")
        {
            loadUpazilla('${applicantForm.presentDistrict.id}', $('#presentUpazila'));
        } else if ('${applicantForm.isUnionAvailable}' === 'false' && '${applicantForm.presentUpazila.id}' !== "")
        {
            loadMunicipal('${applicantForm.presentUpazila.id}', $('#presentUnion'));
        }

        if ('${actionType}' !== 'create')
        {
            loadPresentDistrictList($('#presentDivision')[0]);
            loadPresentUpazilaList($('#presentDistrict')[0]);
            loadPresentUnionList($('#presentUpazila')[0]);

            loadPermDistrictList($('#permDivision')[0]);
            loadPermUpazilaList($('#permDistrict')[0]);
            loadPermUnionList($('#permUpazila')[0]);

            loadBankOrProviderList($("#paymentType")[0]);
            loadBranchList($('#bank')[0]);
        }

        if (${applicantForm.photoPath!='' && applicantForm.photoPath!=null})
            $("#photo").attr("style", "display:none");
        else
            $("#photo").attr("style", "display:block");

        if (${applicantForm.signaturePath!='' && applicantForm.signaturePath!=null})
            $("#signature").attr("style", "display:none");
        else
            $("#signature").attr("style", "display:block");
        if (${applicantForm.attachmentList!='' && applicantForm.attachmentList!=null && applicantForm.attachmentList !='[]'})
            $("#ancCard").attr("style", "display:none");
        else
            $("#ancCard").attr("style", "display:block");
        $(".fileinput-upload-button:eq(0)").hide();
        $(".fileinput-upload-button:eq(1)").hide();
        $(".fileinput-remove-button:eq(0)").text(getLocalizedText('label.remove', selectedLocale));
        $(".fileinput-remove-button:eq(1)").text(getLocalizedText('label.remove', selectedLocale));
        $("input[type=file]").prev().text(getLocalizedText('Browse', selectedLocale));

        //add more file components if Add is clicked
        $('#addFile').click(function () {
            var fileIndex = ($('#fileTable > div').children().length / 2) + 1;
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
            loadAccountLength();
        }, 3000);
    });
    function loadAccountLength()
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
    function checkDob() {
        $("#spouseName").focus();
        $("#dateOfBirth").focus();
    }
    function doNIDCheck(id) {
        var data = getDataFromUrl(contextPath + "/NIDcomparison/applicant/" + id);
        $('#myModal .modal-body').html(data);
        var header = getLocalizedText("label.viewComparisonPageHeader", selectedLocale);
        $('#myModalLabel').text(header);
        $('#myModal .modal-body').css({height: screen.height * .60});
        $('#myModal').modal('show');
    }
    function getNIDData(id, dob) {
        if (!isValidAge($("#dateOfBirth").val()))
        {
            return;

        }
        var buttonGetTextBn = "তথ্য নিন", buttonGetTextEn = "Fetch Data", buttonBusyTextBn = "সংগ্রহীত হচ্ছে...", buttonBusyTextEn = "Fetching...";
        var buttonGetText = '', buttonBusyText = '';
        if (selectedLocale === 'bn')
        {
            id = getNumberInEnglish(id);
            dob = getNumberInEnglish(dob);
            buttonGetText = buttonGetTextBn;
            buttonBusyText = buttonBusyTextBn;
        } else
        {
            buttonGetText = buttonGetTextEn;
            buttonBusyText = buttonBusyTextEn;
        }
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
            $('#buttonNid').html("<i class=\"fa fa-spinner fa-spin\"></i>" + buttonBusyText);
        }

        //       Only Dev  start

        var response = {
            "matchFound": true,
            "nidData": {
                "name": "শাহীনুর",
                "nameEn": "SHAHINOOR",
                "nid": "2865013219",
                "dob": "1997-09-20",
                "gender": "female",
                "photo": "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAKAAeADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD2OloxRVCCiiikAUuKKKYgopaKQwoFFFABS0UUCClpKWgYUUUUAFFFLTEJilxRS4pDE7UCiloGFFFFBIYoxS0UDExRigUuKBCYoxSkUUAJiilxSYoAKKKKYBiiiikAUUUUAFFFFMAooopAFFFFABSYpcUUAJiilopgJRS0UAJiilooASilooATFFLRQMTFBFLRQAmKKWkoERUUUUAGKKKXFABS0lLSGFFFFMQUtFApAFFFLQAUUUUDCilxRQAUuKBRQAYooooEFFFLQAUUUUDExS4ooFAgxS4opcGgBMcUUuDSGgYYpcUmaM0ABFJinZozQIbijFOzSYoAaaKcRSUAJRRRTAKKKKACiijFIAooopgFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFHaiigZFRQKWkAY5oxRR1oAKKKWgQlFLRQAUUUtABRRRQMKKKKYhaBRS0hhRRRQIBS0UUDCiiigAxRRRQIMUYpaUCgAApwpAKXNAwJxTTzSk0gFABikxTwBTsA0ARYoxjiptopGj4oAhxS5xSkYNBFAgBB60EDtSEUlAARRiiimAUUGikAUlLRTASilpKACiijFABRRRigAopaSgAxRRRigAooooAWiiikMSiig0xEQpaBRSGFGKKWgQUlLiigAooxRigApaMUUDCiiigApaSloAKBRSigAooooELRRRQMKKKWkFhKWg0lAWFpc0lFMBQaM5ptLQIXNGaO1HagYoJo3HFNoFAhd3vThIRwaZijFAxScmkzRijFABmiiigQUUUUAIaKU0lABRRRTAKKKDQAUUUUAFGKKKACiiigAooooAKKKKACiiigAooo7UhkQopaKBBRRRQAUUUtABRRRQMKKKMUAFFApcUAJS0uKAKACijFFABSiiigAoFAoxQAUUtFIYUUUUxBRS0YoAb2paXFAFAgNApSOKAKBiUYpcUoFADQKMU7HFGKAG4opcUEUAJSUtFABRRRQAUmKWigBtFLRTEJRilooASiloxQAlFFLQAlFFFABRRRigAooooAKKWjFACUGiigCOiijFIApaKKBhRRRQAUUUtACUopRQKAACjFApaAExRiloxQAlLRRQAUUUUCDFKKKKBhS0CigAoNFFAAORRSDIPrT1AJ5IzQAgGacBTlQE08ICaAISKXBqYoKCooAhxRgipiooKjFAENIamwMU0qMUAR0hFPwKTHNADSKQ8UpPOO9GKAG4JpcYpTSUCCiiigYYpKWigBMUUtFACUUtFACUUUtMQlFLSUAFFFFABRRRQAUUUtACUlLRQBHRQKKQwoooFABRS4oxTASlFLigCkAYopaKADFFFFABRRS0CEFLRS0DG0UUtMQUUUZxSAWm59OaME9enpTgKBhyfQUnPqPyoJVASxAA7k1z+r+MtL0lmjMhlnHRI+T+dOwrm+dx6YJpR8vJwo9Sa8r1D4j37Sv8AZY0hTH8fJrnbzxjq99EElvXKjqB8tGgrvoj3lJY2PDqcdwajn1SztWUT3USZPdsV8/JruoRpgXLgezmqk099c7neZ3+r0aDtLsfQE/ijRoWw9/Fn2Oar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",
                "mother": "খতেজা",
                "father": "হুসেন আলী",
                "spouse": "null",
                "presentAddress": "বাসা/হোল্ডিং: -, উপজেলা: হরিপুর, জেলা: ঠাকুরগাঁও, বিভাগ: রংপুর",
                "permanentAddress": "বাসা/হোল্ডিং: -, উপজেলা: হরিপুর, জেলা: ঠাকুরগাঁও, বিভাগ: রংপুর"
            },
            "errorCode": null,
            "operationResult": true,
            "errorMsg": null
        };

        $('#fullNameInBangla').val(response.nidData.name === "null" ? "" : replaceSpecialChar(response.nidData.name));
        $('#fullNameInEnglish').val(response.nidData.nameEn === "null" ? "" : replaceSpecialChar(response.nidData.nameEn));
        $('#fatherName').val(response.nidData.father === "null" ? "" : response.nidData.father);
        $('#motherName').val(response.nidData.mother === "null" ? "" : response.nidData.mother);
        $('#spouseName').val(response.nidData.spouse === "null" ? "" : response.nidData.spouse);
        $('#profilePhotoFile').attr('src', 'data:image/jpeg;base64,' + response.nidData.photo);
        $('#accountName').val(response.nidData.nameEn === "null" ? "" : replaceSpecialChar(response.nidData.nameEn));

   
        
                $("#photo").hide();
                $("#nidProfilePhotoFile").show();
                $("#nidProfilePhotoFile").attr("src", "data:image/png;base64," + response.nidData.photo);
                $("#nidImageBase64").val(response.nidData.photo);
            



        
        isExistOtherMIS('<spring:message code="label.failure" />', '<spring:message code="label.ok" />', '<spring:message code="label.no" />');


        //only for develope end 
        $.ajax({
            type: "GET",
            url: contextPath + "/getNidData/" + id + "/" + dob,
            async: true,
            success: function (response) {
                $('#buttonNid').html("<i class=\"fa fa-get-pocket\"></i>" + buttonGetText);
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
                    if (response.nidData.gender == "male")
                    {
                        msg = "এই আবেদন শুধুমাত্র মহিলাদের জন্য প্রযোজ্য ";
                        ;
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
                        $('#fullNameInBangla').val("");
                        $('#fullNameInEnglish').val("");
                        $('#fatherName').val("");
                        $('#motherName').val("");
                        $('#spouseName').val("");
                        return;
                    } else {
                        $('#fullNameInBangla').val(response.nidData.name === "null" ? "" : replaceSpecialChar(response.nidData.name));
                        $('#fullNameInEnglish').val(response.nidData.nameEn === "null" ? "" : replaceSpecialChar(response.nidData.nameEn));
                        $('#fatherName').val(response.nidData.father === "null" ? "" : response.nidData.father);
                        $('#motherName').val(response.nidData.mother === "null" ? "" : response.nidData.mother);
                        $('#spouseName').val(response.nidData.spouse === "null" ? "" : response.nidData.spouse);
                        $('#profilePhotoFile').attr('src', 'data:image/jpeg;base64,' + response.nidData.photo);
                        $('#accountName').val(response.nidData.nameEn === "null" ? "" : replaceSpecialChar(response.nidData.nameEn));

//                        $("#photo").hide();
//                        $("#nidProfilePhotoFile").show();
//                        $("#nidProfilePhotoFile").attr("src", "data:image/png;base64," + response.nidData.photo);
//                        $("#nidImageBase64").val(response.nidData.photo);
//                        
//                        let upazilaId = $("#presentUpazila").val();
//                        if (upazilaId != "") {
//                            if (!upazilaList.includes(parseInt(upazilaId)))
//                            {
//                                $("#photo").show();
//                                $("#nidProfilePhotoFile").hide();
//                            } else
//                            {
//                                $("#photo").hide();
//                                $("#nidProfilePhotoFile").show();
//                                $("#nidProfilePhotoFile").attr("src", "data:image/png;base64," + response.nidData.photo);
//                                $("#nidImageBase64").val(response.nidData.photo);
//                            }
//
//
//
//                        }
                        $("#photo").hide();
                        $("#nidProfilePhotoFile").show();
                        $("#nidProfilePhotoFile").attr("src", "data:image/png;base64," + response.nidData.photo);
                        $("#nidImageBase64").val(response.nidData.photo);

//                        isExistOtherMIS('<spring:message code="label.failure" />', '<spring:message code="label.ok" />');
                    }
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
            loadDistrict(divId, distSelectId, '${applicantForm.permanentDistrict.id}');
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
            loadDistrict(divId, distSelectId, '${applicantForm.presentDistrict.id}');
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
            loadUpazilla(distId, upazillaSelectId, '${applicantForm.permanentUpazila.id}');
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#permUnion'));
        }
    }
    function loadPresentUpazilaList(selectObject) {
        var distId = selectObject.value;
        var upazillaSelectId = $('#presentUpazila');
        if (distId !== '') {
            loadUpazilla(distId, upazillaSelectId, '${applicantForm.presentUpazila.id}');
        } else {
            resetSelect(upazillaSelectId);
            resetSelect($('#presentUnion'));
        }
    }
    function loadPermUnionList(selectObject) {
        var upazilaId = selectObject.value;
        var unionSelectId = $('#permUnion');
        if (upazilaId !== '') {
            loadMunicipal(upazilaId, unionSelectId, '${applicantForm.permanentUnion.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }
    function loadPresentUnionList(selectObject) {
        var upazilaId = selectObject.value;
//        if (!upazilaList.includes(parseInt(upazilaId)))
//        {
//            $("#photo").show();
//            $("#nidProfilePhotoFile").hide();
//        } else
//        {
//            $("#photo").hide();
//            $("#nidProfilePhotoFile").show();
//        }

        var unionSelectId = $('#presentUnion');
        if (upazilaId !== '') {
            loadMunicipal(upazilaId, unionSelectId, '${applicantForm.presentUnion.id}');
        } else {
            resetSelect(unionSelectId);
        }
    }
    function loadPermanentAddressSameAsPresent(object) {
        $('#permanentAddressLine1').val($('#presentAddressLine1').val());
        $('#permanentAddressLine2').val($('#presentAddressLine2').val());
        $('#permDivision').val($('#presentDivision').val());
        loadPermDistrictList($('#permDivision')[0]);
        $('#permDistrict').val($('#presentDistrict').val());
        loadPermUpazilaList($('#permDistrict')[0]);
        $('#permUpazila').val($('#presentUpazila').val());
        loadPermUnionList($('#permUpazila')[0]);
        $('#permUnion').val($('#presentUnion').val());
        $('#permanentWardNo').val($('#presentWardNo').val());
        $('#permanentPostCode').val($('#presentPostCode').val());
        makePermanentAddressReadOnly(true);
    }
    function resetPermanentAddress() {
        $('#permanentAddressLine1').val("");
        $('#permanentAddressLine2').val("");
        resetSelect($('#permDivision'));
        loadDivision($("select#permDivision"));
        resetSelect($('#permDistrict'));
        resetSelect($('#permUpazila'));
        resetSelect($('#permUnion'));
        $('#permanentWardNo').val("");
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
        $("#permanentPostCode").attr("disabled", value);
    }
    function loadBankOrProviderList(selectObject) {
        var paymentType = selectObject.value;
        if (paymentType === 'BANKING')
        {
            var bankSelectId = $('#bank');
            loadBank(bankSelectId, '${applicantForm.bank.id}');
            $("#bankGroupInfo").css("display", "block");
            $("#mobileBankingGroupInfo").css("display", "none");
            $("#postOfficeGroupInfo").css("display", "none");
        } else if (paymentType === 'MOBILEBANKING')
        {
            var mbpSelectId = $('#mobileBankingProvider');
            loadMobileBankingProvider(mbpSelectId, '${applicantForm.mobileBankingProvider.id}');
            $("#bankGroupInfo").css("display", "none");
            $("#mobileBankingGroupInfo").css("display", "block");
            $("#postOfficeGroupInfo").css("display", "none");
            accountNoLength_js = 17;
        } else if (paymentType === 'POSTOFFICE')
        {
            var pobSelectId = $('#postOfficeBranch');
            var districtId = $("#presentDistrict").val();
            loadPostOfficeBranch(pobSelectId, '${applicantForm.postOfficeBranch.id}', districtId);
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
                loadBranchByDistrict(bankId, districtId, branchSelectId, '${applicantForm.branch.id}');
            } else {
                resetSelect(branchSelectId);
            }
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
    function removeAncAttachment(index, attachId)
    {
        $("#dvAttachment" + index).css("display", "none");
        $("#ancCard").attr("style", "display:block");
    }
    function submitForm() {
        var form = $('#applicantInfoForm');
        form.validate();
        var validity = form.valid();
        var found = false;
        if (!uniqueAccountNoCheckToApplicationSave('<spring:message code="label.failure" />', '<spring:message code="label.ok" />'))
        {
            return;
        }
        if ($("#removeList").val() != "")
        {
            var list = $("#removeList").val().split(",");
            for (var i = 0; i < list.length; ++i) {
                if (list[i] == "${applicantForm.attachmentList[0].id}") {
                    found = true;
                    break;
                }
            }
        }
        if (typeof ($('#multipartFileList0').val()) !== "undefined" && $('#multipartFileList0').val() !== "")
        {
            $("#showAttachmentError").hide();
        } else if ("${applicantForm.attachmentList[0]}" != "")
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
            if (selectedLocale === "bn")
            {
                $('#submitButton').text("সংরক্ষিত হচ্ছে");
                $("#dateOfBirth").val(getNumberInEnglish($("#dateOfBirth").val()));
                $("#monthlyIncome").val(getNumberInEnglish($("#monthlyIncome").val()));
            } else {
                $('#submitButton').text("Saving");
            }
            $('#submitButton').prop("disabled", true);
            $('#applicantInfoForm').submit();
        }
    }
    function isWardForPresent()
    {
        var ward = $("#presentUnion :selected").text();
        $("#presentWardNo").val("")
        $("#presentWardNo").attr("readonly", false)
        if (ward.indexOf("ward") == 0 || ward.indexOf("ওয়ার্ড") == 0)
        {
            $("#presentWardNo").val(ward)
            $("#presentWardNo").attr("readonly", true)
        }

    }
    function isWardFoePermanent() {
        var ward = $("#permanentUnion :selected").text();
        $("#permanentWardNo").val("")
        $("#permanentWardNo").attr("readonly", false)
        if (ward.indexOf("ward") == 0 || ward.indexOf("ওয়ার্ড") == 0)
        {
            $("#permanentWardNo").val(ward)
            $("#permanentWardNo").attr("readonly", true)
        }
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="selectedLocale" value="${pageContext.response.locale}" />
<c:choose>
    <c:when test="${actionType eq 'create'}">
        <c:set var="actionUrl" value="${contextPath}/applicant/create" />
    </c:when>
    <c:otherwise>
        <c:set var="actionUrl" value="${contextPath}/applicant/edit/${applicantForm.id}" />
    </c:otherwise>
</c:choose>
<form:form id="applicantInfoForm" action="${actionUrl}" class="form-horizontal" enctype="multipart/form-data" role="form" modelAttribute="applicantForm">
    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code='applicant'/>&nbsp;<spring:message code='label.management'/>                                  
            <small><i class="fa fa-arrow-circle-left"></i><a href="${contextPath}/applicant/municipal/list"><spring:message code="label.backToList"/></a></small>
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
            <div class="col-md-12 one-column-legend">
                <fieldset>
                    <legend>
                        <spring:message code='label.BasicInfoTab' var="basicInfoTab"/>${basicInfoTab}
                        <span class="pull-right" style="font-size:14px; padding: 5px 10px 0px">
                            <spring:message code="label.mandatoryPart1"/>
                            <span style="color: red;">(*)</span>
                            <span><spring:message code="label.mandatoryPart2"/></span>
                        </span>
                    </legend>                        
                    <div class="col-md-6 one-column-left-legend">
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
                                            <button class="btn btn-secondary" id="buttonNid" type="button" onclick="getNIDData($('#nid').val(), $('#dateOfBirth').val())" ><i class="fa fa-get-pocket"></i>${getNidData}</button>
                                        </span>
                                        <form:errors path="nid" cssStyle="color:red"></form:errors>
                                        </div>
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
                                            <button class="btn btn-secondary" type="button" onclick = "doNIDCheck('${applicantForm.id}')" >${checkNid}</button>
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
                                <form:input class="form-control" placeholder="${dob}" path="dateOfBirth"  readonly="true"  onchange="checkDob();"/>
                                <form:errors path="dateOfBirth" cssStyle="color:red"></form:errors>
                                </div>                                
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nameBn" /><span class="mandatory">*</span></label>
                            <div class="col-md-8 ">
                                <spring:message code='label.nameBn' var="fullNameBn"/>
                                <form:input class="form-control" placeholder="${fullNameBn}" path="fullNameInBangla" readonly="true"   />
                                <form:errors path="fullNameInBangla" cssStyle="color:red"></form:errors>
                                </div>                                                    
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nameEn" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.nameEn' var="fullNameEn"/>
                                <form:input class="form-control" placeholder="${fullNameEn}" path="fullNameInEnglish"  onkeydown="checkEnglishAlphabetWithLength(event, this, 60)" readonly="true" />
                                <form:errors path="fullNameInEnglish" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.fatherName" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.fatherName' var="fatherName"/>
                                <form:input class="form-control" placeholder="${fatherName}" path="fatherName"  onkeydown="checkAlphabetWithLength(event, this, 30)" readonly="true" />
                                <form:errors path="fatherName" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.motherName" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.motherName' var="motherName"/>
                                <form:input class="form-control" placeholder="${motherName}" path="motherName"  onkeydown="checkAlphabetWithLength(event, this, 30)" readonly="true" />
                                <form:errors path="motherName" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.spouseName" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.spouseName' var="spouseName"/>
                                <form:input class="form-control" placeholder="${spouseName}" path="spouseName"  onkeydown="checkAlphabetWithLength(event, this, 30)" />
                                <form:errors path="spouseName" cssStyle="color:red"></form:errors>
                                </div>            
                            </div>                                                     
                        </div>
                        <div class="col-md-6 one-column-right-legend">                                                   
                            <div class="form-group">
                                <label for="userNameInput" class="col-md-4 control-label"><spring:message code="label.nickName" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.nickName' var="nickName"/>
                                <form:input class="form-control" placeholder="${nickName}" path="nickName"  onkeydown="checkAlphabetWithLength(event, this, 30)"/>
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
                                <form:input class="form-control" placeholder="${mobileNo}" path="mobileNo"  />
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
                            <div class="form-group">                           
                                <label for="nidInput" class="col-md-4 control-label "><spring:message code="label.beneficiaryInOtherScheme" /></label>
                            <div class="col-md-1">
                                <spring:message code='label.beneficiaryInOtherScheme' var="beneficiaryInOtherScheme"/>
                                <form:checkbox id="beneficiaryInOtherScheme" path="beneficiaryInOtherScheme" />
                                <form:errors path="beneficiaryInOtherScheme" cssStyle="color:red"></form:errors>
                                </div>                                                   
                            </div>
                        </div>
                    </fieldset>
                </div> 
                <div class="col-md-12">               
                    <div class="col-md-6">
                        <fieldset>
                            <legend>
                            <spring:message code='label.presentAddress' var="presentAddress"/>
                            ${presentAddress}
                        </legend>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine1" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.addressLine1' var="addressLine1"/>
                                <form:input class="form-control" placeholder="${addressLine1}" path="presentAddressLine1" />
                                <form:errors path="presentAddressLine1" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.addressLine2" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.addressLine2' var="addressLine2"/>
                                <form:input class="form-control" placeholder="${addressLine2}" path="presentAddressLine2" />
                                <form:errors path="presentAddressLine2" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.division" /><span class="mandatory">*</span></label>
                            <c:choose>
                                <c:when test="${applicantForm.isDivisionAvailable eq 'false'}">
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
                                                ${applicantForm.presentDivision.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${applicantForm.presentDivision.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>                            
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.district" /><span class="mandatory">*</span></label>
                            <c:choose>
                                <c:when test="${applicantForm.isDistrictAvailable eq 'false'}">
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
                                                ${applicantForm.presentDistrict.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${applicantForm.presentDistrict.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>                            
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label">                           
                                <spring:message code="label.UpazilaOrCityCorporation" />
                                <span class="mandatory">*</span></label>
                                <c:choose>
                                    <c:when test="${applicantForm.isUpazilaAvailable eq 'false'}">
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
                                                ${applicantForm.presentUpazila.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${applicantForm.presentUpazila.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>        
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label">                           
                                <spring:message code="label.municipal" />                           
                                <span class="mandatory">*</span></label>
                                <c:choose>
                                    <c:when test="${applicantForm.isUnionAvailable eq 'false'}">
                                    <div class="col-md-8">
                                        <spring:message code='label.select' var="select"/>
                                        <form:select class="form-control" path="presentUnion.id" id="presentUnion" onchange="isWardForPresent()">
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
                                                ${applicantForm.presentUnion.nameInBangla}
                                            </c:when>
                                            <c:otherwise>
                                                ${applicantForm.presentUnion.nameInEnglish}
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.wardNo" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.wardNo' var="wardNo"/>
                                <form:input class="form-control" placeholder="${wardNo}" path="presentWardNo" id="presentWardNo"/>
                                <form:errors path="presentWardNo" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.postCode" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.postCode' var="postCode"/>
                                <form:input class="form-control" placeholder="${postCode}" path="presentPostCode" />
                                <form:errors path="presentPostCode" cssStyle="color:red"></form:errors>
                                </div>
                            </div>                                    
                        </fieldset>
                    </div>
                    <div class="col-md-6">
                        <fieldset>
                            <legend>
                            <spring:message code='label.permanentAddress' var="permanentAddress"/>                                            
                            ${permanentAddress}
                            <span class="pull-right" style="font-size:14px; padding: 5px 10px 0px">
                                <spring:message code='label.sameAsPresent' var="sameAsPresent"/>
                                <input type="checkbox" id="sameAsPresent" onclick="loadPermanentAddressSameAsPresent(this);">&nbsp;${sameAsPresent}</input>
                            </span>
                        </legend>
                        <div class="form-group">
                            <label for="addressLine1Input" class="col-md-4 control-label"><spring:message code="label.addressLine1" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.addressLine1' var="addressLine1"/>
                                <form:input class="form-control" placeholder="${addressLine1}" path="permanentAddressLine1" />
                                <form:errors path="permanentAddressLine1" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="addressLine2Input" class="col-md-4 control-label"><spring:message code="label.addressLine2" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.addressLine2' var="addressLine2"/>
                                <form:input class="form-control" placeholder="${addressLine2}" path="permanentAddressLine2" />
                                <form:errors path="permanentAddressLine2" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
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
                                <spring:message code="label.districtOrUpazila"/>
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
                                <spring:message code="label.municipal" />                            
                                <span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.select' var="select"/>
                                <form:select class="form-control" path="permanentUnion.id" id="permUnion" onchange="isWardFoePermanent()">
                                    <form:option value="" label="${select}"></form:option>
                                    <form:options items="${unionList}" itemValue="id" itemLabel="nameInEnglish"></form:options>
                                </form:select>
                                <form:errors path="permanentUnion.id" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.wardNo" /></label>
                            <div class="col-md-8">
                                <spring:message code='label.wardNo' var="wardNo"/>
                                <form:input class="form-control" placeholder="${wardNo}" path="permanentWardNo" />
                                <form:errors path="permanentWardNo" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.postCode" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.postCode' var="postCode"/>
                                <form:input class="form-control" placeholder="${postCode}" path="permanentPostCode" />
                                <form:errors path="permanentPostCode" cssStyle="color:red"></form:errors>
                                </div>
                            </div>

                        </fieldset>
                    </div>                                
                </div>
                <div class="col-md-12">
                    <div class="col-md-6">
                        <fieldset>
                            <legend>
                            <spring:message code='label.socioEconomicInfoTab' var="socioEconomicInfoTab"/>${socioEconomicInfoTab}
                        </legend>
                        <jsp:include page="applicantSocioEconomicInfoUrban.jsp"></jsp:include>  
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
                                <spring:message code='label.conceptionDurationOrChildAge'/>
                                <spring:message code='label.conceptionDurationOrChildAge' var='conceptionDuration'/>
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
                                    <form:select class="form-control" path="branch.id"  id="branch" onchange="loadAccountLength(this)"> 
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
                                <form:input class="form-control" cssStyle="text-transform:uppercase" placeholder="${accountName}" path="accountName" readonly="true" onkeydown="checkAlphabetWithLength(event, this, 50)"/>
                                <form:errors path="accountName" cssStyle="color:red"></form:errors>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.accountNo" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='label.accountNo' var="accountNo"/>
                                <form:input class="form-control" placeholder="${accountNo}" path="accountNo"  />
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
                                <c:if test="${applicantForm.photoPath !='' && applicantForm.photoPath != null}">
                                    <img id="profilePhotoFile" style="width: 50%; border-radius: 20px" alt="img" src="data:image/jpeg;base64,${applicantForm.photoData}"/>
                                    <input type="image" src="${contextPath}/resources/img/remove16.png" id="removeProfilePhoto" value="Remove" onclick="removeProfilePhoto();
                                            return false;" /> 
                                </c:if>
                                <form:errors path="photoPath" cssStyle="color:red"></form:errors>
                                    <img id="nidProfilePhotoFile" style="width: 50%; border-radius: 20px;display: none" src="data:image/jpeg;base64,"/>
                                <form:input type="hidden" id="nidImageBase64" path="photoData" name="photoData"></form:input>
                                <form:errors path="photoPath" cssStyle="color:red"></form:errors>

                                </div>            
                            </div>
                            <div class="form-group">
                                <label for="nidInput" class="col-md-4 control-label"><spring:message code="label.signature" /><span class="mandatory">*</span></label>
                            <div class="col-md-8">
                                <spring:message code='placeholder.signature' var="signature"/>                                    

                                <div id="signature">
                                    <input id="signatureInput" name="signature" type="file" class="file" accept="image/jpg,image/png,image/jpeg,image/gif"  data-allowed-file-extensions='["jpg", "jpeg", "png", "gif"]'>
                                </div>

                                <c:if test="${applicantForm.signaturePath !='' && applicantForm.signaturePath != null}">
                                    <img id="signatureFile" style="width: 20%; border-radius: 5px" alt="img" src="data:image/jpeg;base64,${applicantForm.signatureData}"/>
                                    <input type="image" src="${contextPath}/resources/img/remove16.png" id="removeSignature" value="Remove" onclick="removeSignature();
                                            return false;" /> 
                                </c:if>
                                <form:errors path="signaturePath" cssStyle="color:red"></form:errors>
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
                                <c:forEach begin="0" end="0" items="${applicantForm.attachmentList}" var="attachment" varStatus="index">                        
                                    <c:if test="${attachment !='' && attachment != null}">
                                        <div id="dvAttachment${index.index}" class="form-group">
                                            <div class="col-md-12">
                                                <label for="nidInput" class="col-md-4">${attachment.attachmentName}</label>
                                                <div class="col-md-6">
                                                    <a id="attachment${index.index}" href="${pageContext.request.contextPath}/getFile/${applicantNid}/${attachment.fileName}/anc" target="_blank" download="${attachment.fileName}" title="Click to open">${attachment.fileName}</a>
                                                    &nbsp;<input type="image" src="${contextPath}/resources/img/remove16.png" id="removeFile[${index.index}]" value="Remove" onclick="removeAncAttachment(${index.index}, ${attachment.id});
                                                            return false;" /> 
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                                <div id="showAttachmentError" style="color: red; font-weight: bold; display: none">
                                    <label><spring:message code='label.requiredField'></spring:message></label>
                                    </div>  
                                </div> 
                            <c:forEach begin="1" items="${applicantForm.attachmentList}" var="attachment" varStatus="index">                        
                                <c:if test="${attachment !='' && attachment != null}">
                                    <div id="dvAttachment${index.index}" class="form-group">
                                        <div class="col-md-12">
                                            <label for="nidInput" class="col-md-2"><span id="attachmentIndex${index.index}">${index.index}</span></label>
                                            <label for="nidInput" class="col-md-4">${attachment.attachmentName}</label>
                                            <div class="col-md-6">
                                                <a id="attachment${index.index}" href="${pageContext.request.contextPath}/getFile/${applicantNid}/${attachment.fileName}/others" target="_blank" download="${attachment.fileName}" title="Click to open">${attachment.fileName}</a>
                                                &nbsp;<input type="image" src="${contextPath}/resources/img/remove16.png" id="removeFile[${index.index}]" value="Remove" onclick="removeAttachment(${index.index}, ${attachment.id});
                                                        return false;" /> 
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

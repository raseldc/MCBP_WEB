function initDate(element, format, elementIcon, locale) {
    element.datepicker({
        closeOnDateSelect: true,
        todayButton: true,
        dateFormat: format,
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        autoclose: true,
        beforeShow: function (input, inst) {
            if (locale == 'bn') {
                element.val(getNumberInEnglish(element.val()));
            }
        },
        onClose: function (dateText, datePickerInstance) {
            if (locale == 'bn') {
                element.val(getNumberInBangla(element.val()));
            }
        }
    });
    if (typeof elementIcon !== 'undefined') {
        elementIcon.on("click", function () {
            element.datepicker('show');
        });
    }
}
function initDate(element, format, elementIcon, locale, futureYear) {
    element.datepicker({
        closeOnDateSelect: true,
        todayButton: true,
        dateFormat: format,
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+" + futureYear,
        autoclose: true,
        beforeShow: function (input, inst) {
            if (locale == 'bn') {
                element.val(getNumberInEnglish(element.val()));
            }
        },
        onClose: function (dateText, datePickerInstance) {
            if (locale == 'bn') {
                element.val(getNumberInBangla(element.val()));
            }
        }
    });
    if (typeof elementIcon !== 'undefined') {
        elementIcon.on("click", function () {
            element.datepicker('show');
        });
    }
}

function initDateTime(element, format, elementIcon) {
    element.datetimepicker({
        closeOnDateSelect: true,
        todayButton: true,
        format: format
    });
    if (typeof elementIcon !== 'undefined') {
        elementIcon.on("click", function () {
            element.datetimepicker('show');
        });
    }
}

function monthDiff(d1, d2) {
    var months;
    months = (d2.getFullYear() - d1.getFullYear()) * 12;
    months -= d1.getMonth() + 1;
    months += d2.getMonth();
//    return months <= 0 ? 0 : months;
    return months;
}

function resetSelect(selectId) {
    selectId.find('option').remove();
    $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(selectId);
}

function loadDivision(divisionSelectId, selectedDivision) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getDivision",
        async: false,
        dataType: "json",
        success: function (response) {
            divisionSelectId.find('option').remove();
            appendSelectInList(divisionSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(divisionSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(divisionSelectId);
                }
            });
            if (selectedDivision !== null) {
                divisionSelectId.val(selectedDivision);
            }
        },
        failure: function () {
            log("loading district failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr);
            console.log('error=' + thrownError);
        }
    });
}

function appendSelectInList(selectId) {
    //            var select = messageResource.get('label.select', 'messages_' + selectedLocale); // Not working first time
    var select = (selectedLocale === 'en') ? '--Select--' : '--বেছে নিন--';
    $('<option>').val("").text(select).appendTo(selectId);
}

function loadDistrict(divisionId, districtSelectId, selectedDistrict) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getDistrict/" + divisionId,
        async: false,
        dataType: "json",
        success: function (response) {
            districtSelectId.find('option').remove();
            appendSelectInList(districtSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(districtSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(districtSelectId);
                }
            });
            if (selectedDistrict !== null) {
                districtSelectId.val(selectedDistrict);
            }
        },
        failure: function () {
            log("loading district failed!!");
        }
    });
}

function loadDistrictFromUpazilaTable(districtId, upazilaSelectId, selectedUpazila) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getDistrictOnly/" + districtId,
        async: false,
        dataType: "json",
        success: function (response) {
            upazilaSelectId.find('option').remove();
            appendSelectInList(upazilaSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(upazilaSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(upazilaSelectId);
                }
            });
            if (selectedUpazila !== null) {
                upazilaSelectId.val(selectedUpazila);
            }
        },
        failure: function () {
            log("loading district failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
        }
    });

}

function loadUpazilla(districtId, upazilaSelectId, selectedUpazila) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getUpazila/" + districtId,
        async: false,
        dataType: "json",
        success: function (response) {
            upazilaSelectId.find('option').remove();
            appendSelectInList(upazilaSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(upazilaSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(upazilaSelectId);
                }
            });
            if (selectedUpazila !== null) {
                upazilaSelectId.val(selectedUpazila);
            }
        },
        failure: function () {
            log("loading district failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
        }
    });

}

function loadUpazillaWithDistrict(districtId, upazilaSelectId, selectedUpazila) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getUpazilaWithDistrict/" + districtId,
        async: false,
        dataType: "json",
        success: function (response) {
            upazilaSelectId.find('option').remove();
            appendSelectInList(upazilaSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(upazilaSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(upazilaSelectId);
                }
            });
            if (selectedUpazila !== null) {
                upazilaSelectId.val(selectedUpazila);
            }
        },
        failure: function () {
            log("loading district failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
        }
    });

}

function loadUnion(upazilaId, unionSelectId, selectedUnion) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getUnion/" + upazilaId,
        async: false,
        dataType: "json",
        success: function (response) {
            unionSelectId.find('option').remove();
            appendSelectInList(unionSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(unionSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(unionSelectId);
                }
            });
            if (selectedUnion !== null) {
                unionSelectId.val(selectedUnion);
            }
        },
        failure: function () {
            log("loading union failed!!");
        }
    });
}

function loadVillage(unionId, wardNo, villageSelectId, selectedVillage) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getVillage/" + unionId + "/" + wardNo,
        async: false,
        dataType: "json",
        success: function (response) {
            villageSelectId.find('option').remove();
            appendSelectInList(villageSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(villageSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(villageSelectId);
                }
            });
            if (selectedVillage !== null) {
                villageSelectId.val(selectedVillage);
            }
        },
        failure: function () {
            log("loading union failed!!");
        }
    });
}
function loadMunicipal(upazilaId, unionSelectId, selectedUnion) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getMunicipal/" + upazilaId,
        async: false,
        dataType: "json",
        success: function (response) {
            unionSelectId.find('option').remove();
            appendSelectInList(unionSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(unionSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(unionSelectId);
                }
            });
            if (selectedUnion !== null) {
                unionSelectId.val(selectedUnion);
            }
        },
        failure: function () {
            log("loading Municipal failed!!");
        }
    });
}

function loadCityCorporation(upazilaId, unionSelectId, selectedUnion) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getCityCorporation/" + upazilaId,
        async: false,
        dataType: "json",
        success: function (response) {
            unionSelectId.find('option').remove();
            appendSelectInList(unionSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(unionSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(unionSelectId);
                }
            });
            if (selectedUnion !== null) {
                unionSelectId.val(selectedUnion);
            }
        },
        failure: function () {
            log("loading Municipal failed!!");
        }
    });
}

function loadUnionAndMunicipal(upazilaId, unionSelectId, selectedUnion) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getUnionAndMunicipal/" + upazilaId,
        async: false,
        dataType: "json",
        success: function (response) {
            unionSelectId.find('option').remove();
            appendSelectInList(unionSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(unionSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(unionSelectId);
                }
            });
            if (selectedUnion !== null) {
                unionSelectId.val(selectedUnion);
            }
        },
        failure: function () {
            log("loading union failed!!");
        }
    });
}

function loadBeneficiaryListByGeolocation(upazilaId, attendeeSelectId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getBeneficiaryByGeolocation/" + upazilaId,
        async: false,
        dataType: "json",
        success: function (response) {
            alert(response.nid);
            response.appendTo(attendeeSelectId);
        },
        failure: function () {
            log("loading union failed!!");
        }
    });

}

function loadBank(bankSelectId, selectedBank) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getBankList",
        async: false,
        dataType: "json",
        success: function (response) {
            bankSelectId.find('option').remove();
            $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(bankSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).attr("length", value.accountNoLength).appendTo(bankSelectId);

                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).attr("length", value.accountNoLength).appendTo(bankSelectId);

                }
            });
            if (selectedBank !== null) {
                bankSelectId.val(selectedBank);
            }
        },
        failure: function () {
            log("loading bank failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
}

function loadBranch(bankId, branchSelectId, selectedBranch) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getBranch/" + bankId,
        async: false,
        dataType: "json",
        success: function (response) {
            branchSelectId.find('option').remove();
            $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(branchSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(branchSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(branchSelectId);
                }
            });
            if (selectedBranch !== null) {
                branchSelectId.val(selectedBranch);
            }
        },
        failure: function () {
            log("loading branch failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
        }
    });
}

function loadBranchByDistrict(bankId, districtId, branchSelectId, selectedBranch) {
    console.log('loadBranchByApplicant. bank = ' + bankId + ', districtId = ' + districtId);
    $.ajax({
        type: "GET",
        url: contextPath + "/getBranch/" + bankId + "/" + districtId,
        async: false,
        dataType: "json",
        success: function (response) {
            branchSelectId.find('option').remove();
            $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(branchSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).attr("length", value.accountNoLength).appendTo(branchSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).attr("length", value.accountNoLength).appendTo(branchSelectId);
                }
            });
            if (selectedBranch !== null) {
                branchSelectId.val(selectedBranch);
            }
        },
        failure: function () {
            log("loading branch failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
}

function loadMobileBankingProvider(mbpSelectId, selectedMobileBakningProvider) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getMobileBankingProviderList",
        async: false,
        dataType: "json",
        success: function (response) {
            mbpSelectId.find('option').remove();
            $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(mbpSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).attr("length", value.accountNoLength).appendTo(mbpSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).attr("length", value.accountNoLength).appendTo(mbpSelectId);
                }
            });
            if (selectedMobileBakningProvider !== null) {
                mbpSelectId.val(selectedMobileBakningProvider);
            }
        },
        failure: function () {
            log("loading Mobile Bakning Provider failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
}

function loadPostOfficeBranch(pobSelectId, selectedPostOfficeBranch, districtId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getPostOfficeBranchList/" + districtId,
        async: false,
        dataType: "json",
        success: function (response) {
            pobSelectId.find('option').remove();
            $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(pobSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(pobSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(pobSelectId);
                }
            });
            if (selectedPostOfficeBranch !== null) {
                pobSelectId.val(selectedPostOfficeBranch);
            }
        },
        failure: function () {
            log("loading Post Office Branch failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
}

function includeJs(jsFilePath) {
    var js = document.createElement("script");
    js.type = "text/javascript";
    js.src = jsFilePath;
    document.body.appendChild(js);
}

function getYearDropDown(select, thisYear, yearRange, selectedYear) {
    var minOffset = 0, maxOffset = yearRange; // Change to whatever you want
    for (var i = minOffset; i <= maxOffset; i++) {
        var year = thisYear - i;
        $('<option>', {value: year, text: year}).appendTo(select);
    }
    if (selectedYear !== null) {
        select.val(selectedYear);
    }
    return select;
}

function loadScheme(ministryId, schemeSelectId, selectedScheme) {
    $.ajax({
        type: "GET",
        url: contextPath + "/getScheme/" + ministryId,
        async: false,
        dataType: "json",
        success: function (response) {
            schemeSelectId.find('option').remove();
            $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(schemeSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(schemeSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(schemeSelectId);
                }
            });
            if (selectedScheme !== null) {
                schemeSelectId.val(selectedScheme);
            }
        },
        failure: function () {
            log("loading scheme failed!!");
        }
    });
}

function getDataFromUrl(url) {
    console.log(url);
    $.ajax({
        type: "GET",
        url: url,
        async: true,
//        dataType: "json",
        success: function (response) {
            $(".modal-body").html(response);
            return response;
        },
        failure: function () {
            log("loading scheme failed!!");
            return "error in loading data";
        }
    });
}

function loadPaymentCycle(fiscalYearId, paymentCycleSelectId, isActive) {
    var ajaxUrl = "";
    if (isActive === true)
    {
        ajaxUrl = contextPath + "/getActivePaymentCycle/" + fiscalYearId;
    } else
    {
        ajaxUrl = contextPath + "/getAllPaymentCycle/" + fiscalYearId;
    }
    $.ajax({
        type: "GET",
        url: ajaxUrl,
        async: false,
        dataType: "json",
        success: function (response) {
            paymentCycleSelectId.find('option').remove();
            $('<option>').val("").text(messageResource.get('label.select', 'messages_' + selectedLocale)).appendTo(paymentCycleSelectId);
            $.each(response, function (index, value) {
                if (selectedLocale === 'en') {
                    $('<option>').val(value.id).text(value.nameInEnglish).appendTo(paymentCycleSelectId);
                } else if (selectedLocale === 'bn') {
                    $('<option>').val(value.id).text(value.nameInBangla).appendTo(paymentCycleSelectId);
                }
            });
        },
        failure: function () {
            log("loading Payment Cycle failed!!");
        }
    });
}

function getShortMonthNameInEnglish(d) {
    var objDate = d,
            locale = "en-us",
            month = objDate.toLocaleString(locale, {month: "short"});
    return month;
}

function getShortMonthNameInBangla(d) {
    var objDate = d,
            locale = "bn-BD",
            month = objDate.toLocaleString(locale, {month: "short"});
    return month;
}

function getNumberInBangla(input) {
    if (input == null)
        return null;
    var numbers = {
        '0': '০',
        '1': '১',
        '2': '২',
        '3': '৩',
        '4': '৪',
        '5': '৫',
        '6': '৬',
        '7': '৭',
        '8': '৮',
        '9': '৯'
    };
    var output = [];
    for (var i = 0; i < input.length; ++i) {
        if (numbers.hasOwnProperty(input[i])) {
            output.push(numbers[input[i]]);
        } else {
            output.push(input[i]);
        }
    }
    return output.join('');
}
function getNumberInEnglish(input) {
    if (input == null)
        return null;
    var numbers = {
        '০': '0',
        '১': '1',
        '২': '2',
        '৩': '3',
        '৪': '4',
        '৫': '5',
        '৬': '6',
        '৭': '7',
        '৮': '8',
        '৯': '9'
    };
    var output = [];
    for (var i = 0; i < input.length; ++i) {
        if (numbers.hasOwnProperty(input[i])) {
            output.push(numbers[input[i]]);
        } else {
            output.push(input[i]);
        }
    }
    return output.join('');
}

function localizeBanglaInDatatable(datatableId) {
    $('#' + datatableId + '_info').text(getNumberInBangla($('#' + datatableId + '_info').text()));
    if (document.getElementsByName(datatableId + "_length").length != 0) {
        var options = document.getElementsByName(datatableId + "_length")[0].options;
        for (var i = 0; i < options.length; i++) {
            options[i].text = getNumberInBangla(options[i].text);
        }
        var paginations = $(".paginate_button >a");
        for (var i = 0; i < paginations.length; i++) {
            paginations[i].text = getNumberInBangla(paginations[i].text);
        }
    }
}

function formatCurrency(total) {
    var neg = false;
    if (total < 0) {
        neg = true;
        total = Math.abs(total);
    }
    return parseFloat(total, 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString();
}

function processAjaxData(response, urlPath, urlTitle) {
    $("#mainContent").html(response);
    window.history.pushState({"html": response, "asdfasd": "asdfasdf"}, "");
}

function submitFormAjax(form, action, nextForm, urlTitle) {

    var serializedData = form.serialize();
    serializedData += '&action=' + action;
    try
    {
        $.ajax({
            type: form.attr('method'),
            url: form.attr('action'),
            data: serializedData,
            async: false,
            beforeSend: function () {
            },
            complete: function () {
            },
            success: function (response) {
                processAjaxData(response, nextForm, urlTitle);
                initIcheck();
                initDate($("#dateOfBirth"), 'yy-mm-dd', $("#dateOfBirth\\.icon"));
            },
            error: function (xhr) {
                console.log('error = ' + xhr.responseText);
            }
        });

    } catch (err)
    {
        alert(err);
    }
}

function loadJsp(page) {
    var appId = $("#applicantId").val();
    $("#mainContent").load(contextPath + page + appId + "?regType=offline", function () {
        initIcheck();
    });
}

function loadJspForBeneficiary(page) {
    var appId = $("#applicantId").val();
    console.log('appId = ' + appId);
    $("#mainContent").load(contextPath + page + appId, function () {
        initIcheck();
    });
}

function loadApplicantTabs(actionType, nextTab) {
    if (actionType === 'create') {
        $('#crumbs li a[href="#' + nextTab + '"]').parent().addClass("selected");
        $('#crumbs li.selected').css('pointer-events', 'none');
        $('#crumbs li').not('.selected').addClass('disabled');
        $('#crumbs li').not('.selected').find('a').removeAttr("data-toggle");
    } else {
        $('#crumbs li a[href="#' + nextTab + '"]').parent().addClass("selected");
    }
}

function showReportInPopup(form) {
    var serializedData = form.serialize();
    $.ajax({
        headers: {
            'Accept': 'application/x-pdf'
        },
        type: form.attr('method'),
        url: form.attr('action'),
        data: serializedData,
        async: false,
        beforeSend: function () {
        },
        complete: function () {
        },
        success: function (response) {
            console.log(response);
        },
        error: function (xhr, ajaxOptions, thrownError) {
        }
    });
}

function submitFormAjax2(form, action, nextForm, urlTitle) {
    $('form').append("<input name='action' value='next' type='hidden' />");
    var formData = new FormData($('form')[0]);
    $.ajax({
        type: form.attr('method'),
        url: form.attr('action'),
        data: formData,
        cache: false,
        async: false,
        processData: false,
        contentType: false,
        success: function (response) {
            processAjaxData(response, nextForm, urlTitle);
            initIcheck();
        },
        beforeSend: function () {
        },
        complete: function () {
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }, failure: function () {
            log("submit of " + nextForm + " info failed !!");
        }
    });
}
Number.prototype.pad = function (size) {
    var s = String(this);
    while (s.length < (size || 2)) {
        s = "0" + s;
    }
    return s;
}
function showModalDialog() {
    $('[data-toggle="modal"]').click(function (e) {
        e.preventDefault();
        var url = $(this).attr('href');
        if (url.indexOf('#') == 0) {
            $(url).modal('open');
        } else {
            $('#loading-indicator').show();
            $.get(url, function (data) {
                $('#loading-indicator').hide();
                console.log('in showModalDialog7');
                $('#myModal .modal-body').html(data);
                $('#myModalLabel').val('test');
                $('#myModal .modal-body').css({height: screen.height * .60});
                $('#myModal').modal('show');
            }).success(function () {
            });
        }
    });
}
function showComparisonModalDialog() {
    $('[data-toggle="modal"]').click(function (e) {
        e.preventDefault();
        var url = $(this).attr('href');
        if (url.indexOf('#') == 0) {
            $(url).modal('open');
        } else {
            $.get(url, function (data) {
                $('#comparisonModal .modal-body').html(data);
                $('#comparisonModalLabel').val('test');
                $('#comparisonModal .modal-body').css({height: screen.height * .60});
                $('#comparisonModal').modal('show');
            }).success(function () {
            });
        }
    });
}

function loadApplicant(id) {
    $.ajax({
        type: "GET",
        url: contextPath + "/applicant/viewApplicant/" + id,
        async: true,
        success: function (response) {
            $('#myModal .modal-body').html(response);
            $('#myModalLabel').val('test');
            $('#myModal .modal-body').css({height: screen.height * .60});
            $('#myModal').modal('show');
        },
        failure: function () {
            log("loading scheme failed!!");
            return "error in loading data";
        }
    });
}
function loadComment(id) {
    $.ajax({
        type: "GET",
        url: "applicant/viewComment/" + id,
        async: true,
        success: function (response) {
            $('#commentModal .modal-body').html(response);
            $('#myModalLabel').val('test');
            $('#commentModal .modal-body').css({height: screen.height * .60});
            $('#commentModal').modal('show');
        },
        failure: function () {
            log("loading scheme failed!!");
            return "error in loading data";
        }
    });
}

function loadBeneficiary(id) {
    var data = getDataFromUrl(contextPath + "/beneficiary/viewBeneficiary/" + id);
    $('#myModal .modal-body').html(data);
    var header = getLocalizedText("label.viewBeneficiaryPageHeader", selectedLocale);
    $('#myModalLabel').text(header);
    $('#myModal .modal-body').css({height: screen.height * .60});
    $('#myModal').modal('show');
}

function printCard(id) {
    $.ajax({
        type: "GET",
        url: "selection/printCard/" + id,
        async: true,
        success: function (response) {
            $('#myPrintModal .modal-body').html(response);
            $('#myPrintModal .modal-content').css({height: screen.height * .35});
            $('#myPrintModal .modal-content').css({width: screen.width * .35});
            $('#myPrintModal').modal('show');
        },
        failure: function () {
            log("loading print card failed!!");
            return "error in loading data";
        }
    });
}

function printData(dvPrint) {
    var divToPrint = document.getElementById(dvPrint);
    var htmlToPrint = '' +
            '<style type="text/css">' +
            'table {border-collapse: collapse; width: 100%}' +
            'table th, table td {' +
            'border:1px solid #000;' +
            'text-align: center;' +
            'padding;0.5em;' +
            '}' +
            '.row:after,.row:before{display:table;content:" "}' +
            '.col-md-6{width:50%}' +
            '.col-md-offset-3{margin-left:25%}' +
            '.form-group{margin-bottom:15px}' +
            '.control-label{padding-top:7px;margin-bottom:0;text-align:right}' +
            '.labelAsValue{padding-top: 7px;}' +
            '</style>';
    htmlToPrint += divToPrint.outerHTML;
    newWin = window.open("");
    newWin.document.write(htmlToPrint);
    newWin.print();
    newWin.close();
}

function initIcheck() {
    $('input').iCheck({
        checkboxClass: 'icheckbox_square-blue',
        radioClass: 'iradio_flat-blue',
        increaseArea: '20%' // optional
    });
}

function showStepCountInApplicant(currentTab, totalTabs, divId) {
    if (selectedLocale === 'en') {
        $("#" + divId).text("Step " + currentTab + " of " + totalTabs);
    } else if (selectedLocale === 'bn') {
        $("#" + divId).text("ধাপ " + getNumberInBangla('' + currentTab) + "/" + getNumberInBangla('' + totalTabs));
    }
}
/* Additional Validation Methods */
function checkSpecialAllowedCharacters(event) {
    // Allow only delete, backspace,left arrow,right arrow, Tab and numbers
    if (!((event.keyCode === 46 || // delete
            event.keyCode === 8 || // backspace
            event.keyCode === 36 || // home
            event.keyCode === 35 || // end
            event.keyCode === 37 || // left arrow
            event.keyCode === 39 || // right arrow
            event.keyCode === 9)     // tab
            )) {
        return false;
    } else
    {
        return true;
    }
}

function checkAlphabetWithLength(event, element, maxLength) {
    console.log('code=' + event.keyCode + ' ' + 'length=' + element.value.length + ' ' + 'max=' + maxLength);
    if (!(checkSpecialAllowedCharacters(event) ||
            (element.value.length < maxLength &&
                    ((event.keyCode >= 65 && event.keyCode <= 90) ||
                            event.keyCode === 32 || // space
                            event.keyCode === 190 || // dot
                            event.keyCode === 110 || // dot (numpad)
                            event.keyCode === 188 || // comma
                            event.keyCode === 231 || // bangla (IE/Chrome)
                            event.keyCode === 0 || // bangla (Firefox)
                            event.keyCode === 109 || event.keyCode === 189    // hyphen(numpad) , hyphen(keypad)
                            )
                    )
            )) {
        // Stop the event
        event.preventDefault();
        return false;
    }
}

function checkBanglaAlphabetWithLength(event, element, maxLength) {
    event = event || window.event;
    var charCode = event.which || event.keyCode;
    console.log('code=' + charCode + ' ' + 'length=' + element.value.length + ' ' + 'max=' + maxLength);
    if (!(checkSpecialAllowedCharacters(event) ||
            (element.value.length < maxLength &&
                    (event.keyCode === 32 || // space
                            event.keyCode === 190 || // dot
                            event.keyCode === 110 || // dot (numpad)
                            event.keyCode === 188 || // comma
                            event.keyCode === 231 || // bangla (IE/Chrome)
                            event.keyCode === 0 || // bangla (Firefox)
                            event.keyCode === 109 || event.keyCode === 189)    // hyphen(numpad) , hyphen(keypad)
                    )
            )) {
        // Stop the event
        event.preventDefault();
        return false;
    }
}

function checkEnglishAlphabetWithLength(event, element, maxLength) {
    console.log('code=' + event.keyCode + ' ' + 'length=' + element.value.length + ' ' + 'max=' + maxLength);
    if (!(checkSpecialAllowedCharacters(event) ||
            (element.value.length < maxLength &&
                    ((event.keyCode >= 65 && event.keyCode <= 90) ||
                            event.keyCode === 32 || // space
                            event.keyCode === 190 || // dot
                            event.keyCode === 110 || // dot (numpad)
                            event.keyCode === 188 || // comma                            
                            event.keyCode === 109 || event.keyCode === 189)    // hyphen(numpad) , hyphen(keypad)
                    )
            )) {
        // Stop the event
        event.preventDefault();
        return false;
    }
}

function checkEnglishAlphabetWithLength(event, element, maxLength) {
    console.log('code=' + event.keyCode + ' ' + 'length=' + element.value.length + ' ' + 'max=' + maxLength);
    if (!(checkSpecialAllowedCharacters(event) ||
            (element.value.length < maxLength &&
                    ((event.keyCode >= 65 && event.keyCode <= 90) ||
                            event.keyCode === 32 || // space
                            event.keyCode === 190 || // dot
                            event.keyCode === 110 || // dot (numpad)
                            event.keyCode === 188 || // comma                            
                            event.keyCode === 109 || event.keyCode === 189)    // hyphen(numpad) , hyphen(keypad)
                    )
            )) {
        // Stop the event
        event.preventDefault();
        return false;
    }
}

function checkEnglishAlphabetAndNumberWithLength(event, element, maxLength) {
    console.log('code=' + event.keyCode + ' ' + 'length=' + element.value.length + ' ' + 'max=' + maxLength);
    if (!(checkSpecialAllowedCharacters(event) ||
            (element.value.length < maxLength &&
                    ((event.keyCode >= 65 && event.keyCode <= 90) || (event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105) ||
                            event.keyCode === 32 || // space
                            event.keyCode === 190 || // dot
                            event.keyCode === 110 || // dot (numpad)
                            event.keyCode === 188 || // comma                            
                            event.keyCode === 109 || event.keyCode === 189)    // hyphen(numpad) , hyphen(keypad)
                    )
            )) {
        // Stop the event
        event.preventDefault();
        return false;
    }
}

function checkNumber(event, element) {
    console.log('code=' + event.keyCode + ' ' + 'length=' + element.value.length);
    if (!(checkSpecialAllowedCharacters(event) ||
            ((event.keyCode >= 48 && event.keyCode <= 57) ||
//                    ((event.keyCode == 65 || event.keyCode == 86 || event.keyCode == 67) && (event.ctrlKey === true || event.metaKey === true)) || // Ctrl+V options
//                    (event.keyCode === 0) || // bangla
                    (event.keyCode >= 96 && event.keyCode <= 105)))) {
        // Stop the event
        event.preventDefault();
        return false;
    }
}

function checkNumberWithLength(event, element, maxLength) {
    console.log('code=' + event.keyCode + ' ' + 'length=' + element.value.length + ' ' + 'max=' + maxLength);
    if (!(checkSpecialAllowedCharacters(event) ||
            element.value.length < maxLength &&
            ((event.keyCode >= 48 && event.keyCode <= 57) ||
//                    ((event.keyCode == 65 || event.keyCode == 86 || event.keyCode == 67) && (event.ctrlKey === true || event.metaKey === true)) || // Ctrl+V options
//                    (event.keyCode === 0) || // bangla
                    (event.keyCode >= 96 && event.keyCode <= 105)))) {
        // Stop the event
        event.preventDefault();
        return false;
    }
}

var accountNoLength_js = 100;
function checkNumberWithLengthForAccountNo(event, element, maxLength) {
    if (accountNoLength_js == 100 || accountNoLength_js == 0)
    {
        accountNoLength_js = 100;
    }
    console.log(event);
    console.log('code=' + event.keyCode + ' ' + 'length=' + event.key + ' ' + 'max=' + maxLength);
    console.log("checkSpecialAllowedCharacters : " + checkSpecialAllowedCharacters(event));
    if (checkSpecialAllowedCharacters(event) ||
            (
                    ((event.keyCode >= 48 && event.keyCode <= 57) ||
                            (event.keyCode >= 96 && event.keyCode <= 105)
                            )
                    && (/^[0-9]*$/.test(event.key)) && element.value.length < accountNoLength_js)
            )
    {
        //    console.log("true");
        return true;
    }
    event.preventDefault();
    // console.log("false");
    return false;
    //write code for valid account no
}

function checkNumberWithLengthWithPasteOption(event, element, maxLength) {
    console.log('code=' + event.keyCode + ' ' + 'length=' + element.value.length + ' ' + 'max=' + maxLength);
    if (!(checkSpecialAllowedCharacters(event) ||
            element.value.length < maxLength &&
            ((event.keyCode >= 48 && event.keyCode <= 57) ||
                    ((event.keyCode == 65 || event.keyCode == 86 || event.keyCode == 67) && (event.ctrlKey === true || event.metaKey === true)) || // Ctrl+V options
//                    (event.keyCode === 0) || // bangla
                    (event.keyCode >= 96 && event.keyCode <= 105)))) {
        // Stop the event
        event.preventDefault();
        return false;
    }
}

function checkDecimalWithLength(event, element, maxLength) {
    console.log('code=' + event.keyCode + ' ' + 'length=' + element.value.length + ' ' + 'max=' + maxLength);
    if (!(checkSpecialAllowedCharacters(event) ||
            element.value.length < maxLength &&
            ((event.keyCode >= 48 && event.keyCode <= 57) ||
                    event.keyCode === 190 || // dot
                    event.keyCode === 110 || // dot (numpad)
//                    ((event.keyCode == 65 || event.keyCode == 86 || event.keyCode == 67) && (event.ctrlKey === true || event.metaKey === true)) || // Ctrl+V options
//                    (event.keyCode === 0) || // bangla
                    (event.keyCode >= 96 && event.keyCode <= 105)))) {
        // Stop the event
        event.preventDefault();
        return false;
    }
}

$.validator.addMethod("regex", function (value, element, regexp) {
    var re = new RegExp(regexp);
    return this.optional(element) || re.test(value);
});

$.validator.addMethod("banglaAlphabet", function (value, element) {
    console.log(value);
    console.log(element);
    return this.optional(element) || /^['\u0980-\u09FF'.-\s]+$/.test(value);
});

$.validator.addMethod("englishAlphabet", function (value, element) {
    return this.optional(element) || /^['a-zA-Z'.-\s]+$/.test(value);
});

$.validator.addMethod("banglaAlphabetWithNumber", function (value, element) {
    return this.optional(element) || /^['\u0980-\u09FF0-9'.-\s]+$/.test(value);
});

$.validator.addMethod("englishAlphabetWithNumber", function (value, element) {
    return this.optional(element) || /^['a-zA-Z0-9'.-\s]+$/.test(value);
});

$.validator.addMethod("checkDateOfBirth", function (value, element, options) {
    // console.log("Startrtrtrtrtt");
//    if (this.optional(element)) {
//        return true;
//    }
//
//    var dateOfBirth = '';
//    if (options.locale == 'bn')
//        dateOfBirth = getNumberInEnglish(value);
//    else
//        dateOfBirth = value;
//    var arr_dateText = dateOfBirth.split("-");
//
//    year = arr_dateText[2];
//    month = arr_dateText[1];
//    day = arr_dateText[0];
//
//    var mydate = new Date();
//    mydate.setFullYear(year, month - 1, day);
//
    var minAge = options.min;
    var maxAge = options.max;
//    var minDate = new Date();
//    minDate.setFullYear(minDate.getFullYear() - minAge);
//
//    var maxDate = new Date();
//    maxDate.setFullYear(maxDate.getFullYear() - maxAge);

    var age = 0;
    $.ajax({
        type: "GET",
        url: contextPath + "/get-age",
        async: false,
        data: {'dob': getNumberInEnglish(value)},
        success: function (data)
        {
            age = parseInt(data);
            console.log(data);
        },
        failure: function () {
            log("checking uniqueness of nid failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
    if (age >= minAge && age < maxAge)
    {
        return true;
    }
//    if ((minDate - mydate) < 0 || (maxDate - mydate) > 0) {
//        return false;
//    }

    return false;
});
$.validator.addMethod("checkDateOfBirthUpdate", function (value, element, options) {
    // console.log("Startrtrtrtrtt");
//    if (this.optional(element)) {
//        return true;
//    }
//
//    var dateOfBirth = '';
//    if (options.locale == 'bn')
//        dateOfBirth = getNumberInEnglish(value);
//    else
//        dateOfBirth = value;
//    var arr_dateText = dateOfBirth.split("-");
//
//    year = arr_dateText[2];
//    month = arr_dateText[1];
//    day = arr_dateText[0];
//
//    var mydate = new Date();
//    mydate.setFullYear(year, month - 1, day);
//
    var minAge = options.min;
    var maxAge = options.max;
//    var minDate = new Date();
//    minDate.setFullYear(minDate.getFullYear() - minAge);
//
//    var maxDate = new Date();
//    maxDate.setFullYear(maxDate.getFullYear() - maxAge);

    var age = 0;
    $.ajax({
        type: "GET",
        url: contextPath + "/get-age",
        async: false,
        data: {'dob': getNumberInEnglish(value)},
        success: function (data)
        {
            age = parseInt(data);
            console.log(data);
        },
        failure: function () {
            log("checking uniqueness of nid failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
    if (age >= minAge && age < maxAge)
    {
        return true;
    }
//    if ((minDate - mydate) < 0 || (maxDate - mydate) > 0) {
//        return false;
//    }

    return false;
});

$.validator.addMethod("checkMobileNo", function (value, element) {
    return this.optional(element) || /^01[3456789][0-9]{8}$/.test(value) || /^০১[৩৪৫৬৭৮৯][০-৯]{8}$/.test(value);
});

$.validator.addMethod("checkNumericValue", function (value, element) {
    return this.optional(element) || /^[0-9]+$/.test(value) || /^[০-৯]+$/.test(value);
});

$.validator.addMethod("checkConceptionTerm", function (value, element) {
    value = getNumberInEnglish(value);
    console.log('checkConceptionTerm=' + value);
    return this.optional(element) || value <= 40;
});
$.validator.addMethod("checkNidNumber", function (value, element) {
    return this.optional(element) || /^[^0][0-9]{16}$/.test(value) || /^[^০][০-৯]{16}$/.test(value) || /^[^0][0-9]{9}$/.test(value) || /^[^০][০-৯]{9}$/.test(value);
});

$.validator.addMethod("checkAccountNo", function (value, element) {
    value = getNumberInEnglish(value);
    console.log(value);
    if (/^(.)\1+$/.test(value))
    {
        console.log("same");
        return false;
    }
    if (!/^[0-9]*$/.test(value))
    {
        console.log("WithOut number")
        ///^[a-zA-Z0-9- ]*$/.test(str)  fo check if special char exist
        return false;
    }
    if (accountNoLength_js == 100 || accountNoLength_js == 0)
        return true;
    if (value.length != accountNoLength_js) {
        console.log("13 er boro " + accountNoLength_js);
        return false;
    }
    return true;

});

$.validator.addMethod("uniqueNid", function (value, element) {
    var isSuccess = false;
    $.ajax({
        type: "POST",
        url: contextPath + "/checkUniqueNid",
        async: false,
        data: {'nid': $("#nid").val(), 'appId': $("#id").val()
        },
        success: function (msg)
        {
            isSuccess = msg === true ? true : false;
        },
        failure: function () {
            log("checking uniqueness of nid failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
    return isSuccess;
});
$.validator.addMethod("uniqueAccountNumber", function (value, element) {

    var isSuccess = false;
    $.ajax({
        type: "POST",
        url: contextPath + "/checkUniqueAccountNumber",
        async: false,
        data: {'accountNo': $("#accountNo").val(), 'appId': $("#id").val()
        },
        success: function (msg)
        {
            isSuccess = msg === true ? true : false;
        },
        failure: function () {
            log("checking uniqueness of nid failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
    return isSuccess;
});

$.validator.addMethod("uniqueBenNid", function (value, element) {
    var isSuccess = false;
    $.ajax({
        type: "POST",
        url: contextPath + "/checkUniqueBenNid",
        async: false,
        data: {'nid': $("#nid").val(), 'benId': $("#id").val()
        },
        success: function (msg)
        {
            isSuccess = msg === true ? true : false;
        },
        failure: function () {
            log("checking uniqueness of nid failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
    return isSuccess;
});

$.validator.addMethod("checkApplicationDeadline", function (value, element) {
    console.log('calling checkApplicationDeadline');
    var isSuccess = false;
    $.ajax({
        type: "POST",
        url: contextPath + "/checkApplicationDeadline",
        async: false,
        data: {'fiscalYearId': $("#fiscalYear\\.id").val(), 'schemeId': $("#scheme\\.id").val(), 'upazilaId': $("#permUpazila").val(), 'regType': $("#regType").val()
        },
        success: function (msg)
        {
            isSuccess = msg;
        },
        failure: function () {
            log("checking application deadline failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
    return isSuccess;
});

$.validator.addMethod("checkQuotaAllocation", function (value, element) {
    console.log('calling checkQuotaAllocation');
    var isSuccess = false;
    $.ajax({
        type: "POST",
        url: contextPath + "/checkQuotaAllocation",
        async: false,
        data: {'fiscalYearId': $("#fiscalYear\\.id").val(), 'schemeId': $("#scheme\\.id").val(), 'unionId': $("#permUnion").val(), 'regType': $("#regType").val()
        },
        success: function (msg)
        {
            isSuccess = msg;
        },
        failure: function () {
            log("checking quota allocation failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
    return isSuccess;
});

$.validator.addMethod('checkFileSize', function (value, element, param) {
    return this.optional(element) || (element.files[0].size <= param * 1024)
});

function getLocalizedText(messageResourceLabel, selectedLocale)
{
    var text = messageResource.get(messageResourceLabel, 'messages_' + selectedLocale);
    return text;
}

// Not Tested
function localizeText(element) {
    var text = element.value;
    var banglaText = getNumberInBangla(text);
    element.value = banglaText;
}

function alertButton()
{
    if ("${message.messageType}" == "SUCCESS")
    {
        bootbox.confirm({
            title: '<spring:message code="label.success" />',
            message: "<b>${message.message}</b>",
            buttons: {
                cancel: {
                    label: "Cancel",
                },
                confirm: {
                    label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />'
                }
            },
            callback: function (result) {
            }
        });
    } else if ("${message.messageType}" == "DANGER")
    {
        bootbox.confirm({
            title: '<spring:message code="label.failure" />',
            message: "<b>${message.message}</b>",
            buttons: {
                cancel: {
                    label: "Cancel",
                },
                confirm: {
                    label: '<i class="fa fa-check"></i> <spring:message code="label.ok" />',
                }
            },
            callback: function (result) {
            }
        });
    }
}

//Now only - replace by space 

//remove all special char except a-z,0-9,-, banlga 
function replaceSpecialChar(str) {
//    var bangla_categories = dict(
//    consonant         = u'[\u0995-\u09B9\u09CE\u09DC-\u09DF]',
//            independent_vowel = u'[\u0985-\u0994]',
//            dependent_vowel = u'[\u09BE-\u09CC\u09D7]',
//            nukta = u'[\u09BC]',
//    )
    // var result = str.replace(/[^'\u0995-\u09B9\u09CE\u09DC-\u09DF\u0985-\u0994\u09BE-\u09CE\u09D7\u09AF\u09BC০-৯a-z0-9-\s]/gi, '').replace(/[-]/gi, ' ');
    var result = str.replace(/[^\u0995-\u09B9\u09CE\u09DC-\u09DF\u0985-\u0994\u09BE-\u09CE\u09D7\u09AF\u09BC০-৯a-z0-9-.\s]/gi, '').replace(/[-]/gi, ' ');
    return result;
}
function loadAccountLength(selectedObject)
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
function isExistOtherMIS(titleMsg, okMsg, noMsg) {
    var nid = getNumberInEnglish($("#nid").val());
    var dob = getNumberInEnglish($("#dateOfBirth").val());
    $.ajax({
        type: "GET",
        url: contextPath + "/is-exist-other-mis/" + nid + "/" + dob,
        async: true,
        dataType: 'json',
        success: function (data) {
            console.log(data);
            if (data.isError == true)
            {
                var msg = data.errorMsg + "। আপনি কি আবেদন সম্পন্ন করতে চান?"
                bootbox.dialog({
                    onEscape: function () {},
                    title: titleMsg,
                    message: msg,
                    buttons: {
                        ok: {
                            label: '<i class="fa fa-check"></i> ' + okMsg
                        },
                        no: {
                            label: '<i class="fa fa-check"></i> ' + noMsg,
                            callback: function () {
                                location.reload();
                            }
                        }
                    },
                    callback: function (result) {
                    }
                });
            }
        }
    });

}

function isUniqueAccountNumber() {
    var accountNo = $("#accountNo").val();
    if (accountNo == "")
        return true;
    var isSuccess = false;
    $.ajax({
        type: "POST",
        url: contextPath + "/checkUniqueAccountNumber",
        async: false,
        data: {'accountNo': $("#accountNo").val(), 'appId': $("#id").val()
        },
        success: function (msg)
        {
            isSuccess = msg === true ? true : false;
        },
        failure: function () {
            log("checking uniqueness of nid failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
    return isSuccess;
}

function isUniqueAccountNumberAtApplicationSave() {
    var accountNo = $("#accountNo").val();
    if (accountNo == "")
        return true;
    var isSuccess = false;
    $.ajax({
        type: "POST",
        url: contextPath + "/checkUniqueAccountNumberAtApplicationSave",
        async: false,
        data: {'accountNo': $("#accountNo").val(), 'appId': $("#id").val()
        },
        success: function (msg)
        {
            isSuccess = msg === true ? true : false;
        },
        failure: function () {
            log("checking uniqueness of nid failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
    return isSuccess;
}

function uniqueAccountNoCheckToApplicationSave(titleMsg, okMsg) {
    var result = isUniqueAccountNumberAtApplicationSave();
    if (result == false)
    {
        msg = 'এই হিসেব নম্বরে ইতমধ্যে আবেদন করা হয়েছে।';

        bootbox.dialog({
            onEscape: function () {},
            title: titleMsg,
            message: msg,
            buttons: {
                ok: {
                    label: '<i class="fa fa-check"></i> ' + okMsg
                }
            },
            callback: function (result) {
            }
        });
        console.log("Acc false");
        return false;
    }
    return true;
}

function uniqueAccountNoCheck(titleMsg, okMsg) {
    var result = isUniqueAccountNumber();
    if (result == false)
    {
        msg = 'এই হিসেব নম্বরে ইতমধ্যে আবেদন করা হয়েছে।';

        bootbox.dialog({
            onEscape: function () {},
            title: titleMsg,
            message: msg,
            buttons: {
                ok: {
                    label: '<i class="fa fa-check"></i> ' + okMsg
                }
            },
            callback: function (result) {
            }
        });
        console.log("Acc false");
        return false;
    }
    return true;
}

function isUniqueAccountNumberForBeneficiary() {
    var accountNo = $("#accountNo").val();
    if (accountNo == "")
        return true;
    var isSuccess = false;
    $.ajax({
        type: "GET",
        url: contextPath + "/checkUniqueAccountNumberForBeneficiary",
        async: false,
        data: {'accountNo': $("#accountNo").val(), 'beneficiaryID': $("#id").val()
        },
        success: function (msg)
        {
            isSuccess = msg === true ? true : false;
        },
        failure: function () {
            log("checking uniqueness of nid failed!!");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log('error=' + xhr.responseText);
            console.log('error=' + thrownError);
        }
    });
    return isSuccess;
}
function uniqueAccountNoCheckForBeneficiary(titleMsg, okMsg) {
    var result = isUniqueAccountNumberForBeneficiary();
    if (result == false)
    {
        msg = 'এই হিসেব নম্বরে ইতমধ্যে আবেদন করা হয়েছে।';

        bootbox.dialog({
            onEscape: function () {},
            title: titleMsg,
            message: msg,
            buttons: {
                ok: {
                    label: '<i class="fa fa-check"></i> ' + okMsg
                }
            },
            callback: function (result) {
            }
        });
        console.log("Acc false");
        return false;
    }
    return true;
}

function isValidAge(value) {


    var dateOfBirth = getNumberInEnglish(value);


    var arr_dateText = dateOfBirth.split("-");

    year = arr_dateText[2];
    month = arr_dateText[1];
    day = arr_dateText[0];

    var mydate = new Date();
    mydate.setFullYear(year, month - 1, day);

    var minAge = 20;
    var maxAge = 35;
    var minDate = new Date();
    minDate.setFullYear(minDate.getFullYear() - minAge);

    var maxDate = new Date();
    maxDate.setFullYear(maxDate.getFullYear() - maxAge);
    if ((minDate - mydate) < 0 || (maxDate - mydate) > 0) {
        return false;
    }
    return true;
}

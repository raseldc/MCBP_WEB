<%-- 
    Document   : benificaryProfile
    Created on : Sep 8, 2020, 3:15:01 AM
    Author     : imlma
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<script src="<c:url value="/resources/plugins/fileUpload/fileinput.min.js" />" ></script>
<script src="<c:url value="/resources/js/jquery-ui-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.datetimepicker.js" />"></script>
<script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
<script src="<c:url value="/resources/js/utility.js" />" ></script>
<script src="<c:url value="/resources/js/unijoy.js" />" ></script> 
<script src="<c:url value="/resources/js/backToTop.js" />" ></script> 
<link rel="stylesheet" href="<c:url value="/resources/plugins/fileUpload/fileinput.min.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.11.2.css" />">
<script type="text/javascript" src="<c:url value="/resources/js/jquery.ui.datepicker-bn.js"/>" ></script>
<div id="dvBenInfo">

    <section class="content-header clearfix">
        <h1 class="pull-left">
            <spring:message code='beneficiaryProfile.Title'/>

            <small><i class="fa fa-arrow-circle-left"></i><a href="${referer}">তালিকায় ফিরে যান</a></small>
        </h1>

        <div class="pull-right">
            <!--<a href="${contextPath}/beneficiary/edit/${beneficiaryProfile.beneficiaryProfile.benID}" class="btn bg-green-active"><i class="fa fa-print"></i><spring:message code='label.edit'/></a>-->

            <!--        <button type="submit" name="edit" class="btn bg-green-active" id="print" onclick="beneficiaryEdit()">
                        <i class="fa fa-print"></i>
            <spring:message code='label.edit'/></button>   -->
            <button type="submit" name="print" class="btn bg-green-active" id="print" onclick="PrintElem()">
                <i class="fa fa-print"></i>
                <spring:message code='beneficiaryProfile.Print'/></button>         
            <button type="submit" name="download" class="btn bg-green-active" id="download" onclick="generatePDF()">
                <i class="fa fa-download"></i>
                <spring:message code='beneficiaryProfile.Download'/></button>         
        </div>   
    </section>
    <section class="content" id="beneficiaryForm">    
        <div class="row">
            <div class="col-md-12">
                <!--            <div class="box">                
                                <div class="box-body">-->

                <legend class="col-lg-10">
                    <spring:message code='beneficiaryProfile.PersonalInfo'/>

                </legend>     
                <legend class="col-lg-2" style="text-align: right">
                    <a href="#" id="btnSelect" value="Edit" title="<spring:message code='label.edit'/>" onclick="editBeneficiary()"><span class="glyphicon glyphicon-edit"></span></a>


                </legend>
                <form:form id="beneficiaryInformationForm" role="form" class="form-horizontal" action="#" method="post">
                    <div id="personalInfo">
                        <input type="hidden" id="benId" value="${beneficiaryProfile.beneficiaryProfile.benID}"/>
                        <table id="applicantListTable" class="table table-bordered table-hover">
                            <!--                            <caption>Data table caption</caption>-->
                            <thead>
                                <tr>
                                    <td><spring:message code='beneficiaryProfile.NameBn'/></td>
                                    <td> 
                                        ${beneficiaryProfile.beneficiaryProfile.nameBn}
                                    </td>
                                    <td><spring:message code='beneficiaryProfile.FatherName'/></td>
                                    <td> 
                                        ${beneficiaryProfile.beneficiaryProfile.fatherName}

                                    </td>
                                    <td><spring:message code='beneficiaryProfile.PicSign'/></td>
                                </tr>
                                <tr>
                                    <td><spring:message code='beneficiaryProfile.NameEn'/></td>
                                    <td>  ${beneficiaryProfile.beneficiaryProfile.nameEn}</td>
                                    <td><spring:message code='beneficiaryProfile.MotherName'/></td>
                                    <td>${beneficiaryProfile.beneficiaryProfile.motherName}</td>
                                    <td rowspan="8">
                                        <img id="profilePhotoFile" style="width:150px" alt="ছবি পাওয়া যায় নি" src="data:image/jpeg;base64,${beneficiaryProfile.base64PhotoData}"><br>
                                        <img id="signatureFile" style="width:150px" alt="স্বাক্ষর/টিপসই পাওয়া যায় নি" src="data:image/jpeg;base64,${beneficiaryProfile.base64SignatureData}">
                                    </td>
                                </tr>
                                <tr>
                                    <td><spring:message code='beneficiaryProfile.NID'/> </td>
                                    <td>
                                        <div class="col-lg-9"> ${beneficiaryProfile.beneficiaryProfile.nid} </div> 
                                        <div class="col-lg-3">
                                            <spring:message code='label.checkNid' var="checkNid"/>
                                            <span class="input-group-btn">
                                                <button class="btn btn-secondary" type="button" onclick = "doNIDCheck('${beneficiaryProfile.beneficiaryProfile.benID}')" >${checkNid}</button>
                                            </span>
                                        </div>

                                    </td>
                                    <td><spring:message code='beneficiaryProfile.HusbandName'/> </td>
                                    <td> <input id="spouseName" class="form-control" value=" ${beneficiaryProfile.beneficiaryProfile.spouseName}" onkeydown="checkAlphabetWithLength(event, this, 30)"/></td>

                                </tr>
                                <tr>
                                    <td><spring:message code='beneficiaryProfile.DOB'/></td>
                                    <td>${beneficiaryProfile.beneficiaryProfile.dob}</td>
                                    <td><spring:message code='beneficiaryProfile.Division'/></td>
                                    <td>

                                        <c:if test="${lan eq 'en'}">
                                            ${beneficiaryProfile.beneficiaryProfile.divisionNameEn}
                                        </c:if>
                                        <c:if test="${lan eq 'bn'}">
                                            ${beneficiaryProfile.beneficiaryProfile.divisionNameBn}
                                        </c:if>
                                    </td>

                                </tr>
                                <tr>
                                    <td><spring:message code='beneficiaryProfile.BirthPlace'/></td>
                                    <td>
                                        <c:if test="${lan eq 'en'}">
                                            ${beneficiaryProfile.beneficiaryProfile.birthPlaceEn}
                                        </c:if>
                                        <c:if test="${lan eq 'bn'}">
                                            ${beneficiaryProfile.beneficiaryProfile.birthPlaceBn}
                                        </c:if>
                                    </td>
                                    <td><spring:message code='beneficiaryProfile.District'/></td>
                                    <td>
                                        <c:if test="${lan eq 'en'}">
                                            ${beneficiaryProfile.beneficiaryProfile.districNameEn}
                                        </c:if>
                                        <c:if test="${lan eq 'bn'}">
                                            ${beneficiaryProfile.beneficiaryProfile.districNameBn}
                                        </c:if>
                                    </td>

                                </tr>
                                <tr>
                                    <td> <spring:message code='beneficiaryProfile.Religion'/></td>
                                    <td>
                                        <c:if test="${lan eq 'en'}">
                                            ${beneficiaryProfile.beneficiaryProfile.religionEn}
                                        </c:if>
                                        <c:if test="${lan eq 'bn'}">
                                            ${beneficiaryProfile.beneficiaryProfile.religionBn}
                                        </c:if>    
                                    </td>
                                    <td>
                                        <spring:message code='beneficiaryProfile.Upazilla'/></td>
                                    <td>
                                        <c:if test="${lan eq 'en'}">
                                            ${beneficiaryProfile.beneficiaryProfile.upazilaNameEn}
                                        </c:if>
                                        <c:if test="${lan eq 'bn'}">
                                            ${beneficiaryProfile.beneficiaryProfile.upazilaNameBn}
                                        </c:if>   
                                    </td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td><spring:message code='beneficiaryProfile.EducationLevel'/></td>
                                    <td>
                                        <c:if test="${lan eq 'en'}">
                                            ${beneficiaryProfile.beneficiaryProfile.educationLevelEn}
                                        </c:if>
                                        <c:if test="${lan eq 'bn'}">
                                            ${beneficiaryProfile.beneficiaryProfile.educationLevelBn}
                                        </c:if>  
                                    </td>
                                    <td>
                                        <spring:message code='beneficiaryProfile.Union'/></td>
                                    <td>
                                        <c:if test="${lan eq 'en'}">
                                            ${beneficiaryProfile.beneficiaryProfile.unionNameEn}
                                        </c:if>
                                        <c:if test="${lan eq 'bn'}">
                                            ${beneficiaryProfile.beneficiaryProfile.unionNameBn}
                                        </c:if>
                                    </td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td><spring:message code='beneficiaryProfile.BloodGroup'/></td>
                                    <td>
                                    </td>
                                    <td><spring:message code='beneficiaryProfile.Ward'/></td>
                                    <td>  
                                        ${beneficiaryProfile.beneficiaryProfile.wardNo}  
                                    </td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td><spring:message code='beneficiaryProfile.MobileNo'/></td>
                                    <td> 
                                        <input class="form-control" value="${beneficiaryProfile.beneficiaryProfile.mobileNumber}" name="mobileNo" id="mobileNo" />
                                    </td>
                                    <td><spring:message code='beneficiaryProfile.Village'/></td>
                                    <td>  
                                        <c:if test="${lan eq 'en'}">
                                            ${beneficiaryProfile.beneficiaryProfile.villageNameEn}


                                        </c:if>
                                        <c:if test="${lan eq 'bn'}">
                                            ${beneficiaryProfile.beneficiaryProfile.villageNameBn}
                                        </c:if>
                                    </td>
                                    <td></td>
                                </tr>
                            </thead>
                            <tbody>                            
                            </tbody>
                        </table>

                    </div>
                    <div style="clear:both;display: none">

                        <legend class="pull-left"><spring:message code='beneficiaryProfile.childInfo'/></legend>
                        <div class="pull-right">
                            <input type="button" class="btn bg-green-active" id="btnAddNewChild" onclick="addNewChild()"  value="<spring:message code='beneficiaryProfile.addChildInfo'/>"/>

                        </div>
                        <table id="applicantListTable" class="table table-bordered table-hover">
                            <!--                            <caption>Data table caption</caption>-->
                            <thead>
                                <tr>

                                    <th><spring:message code='beneficiaryProfile.Serial'/></th>
                                    <th><spring:message code='beneficiaryProfile.childNo'/></th>
                                    <th><spring:message code='beneficiaryProfile.childName'/></th>
                                    <th><spring:message code='beneficiaryProfile.childBirthCertificate'/></th>
                                    <th><spring:message code='beneficiaryProfile.attachFile'/></th>
                                    <th><spring:message code='label.edit'/></th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${beneficiaryProfile.beneficiaryChildInformations}" var="childInfo" varStatus="index">                        
                                    <tr>
                                        <td class="tdSerial">
                                            ${index.index+1}</td> 
                                        <td>

                                            ${childInfo.childNoSt}</td> 
                                        <td>
                                            <c:if test="${lan eq 'en'}">
                                                ${childInfo.childName}
                                            </c:if>
                                            <c:if test="${lan eq 'bn'}">
                                                ${childInfo.childName}
                                            </c:if>
                                        </td> 
                                        <td>    
                                            <c:if test="${lan eq 'en'}">
                                                ${childInfo.childBirthCertificate}
                                            </c:if>
                                            <c:if test="${lan eq 'bn'}">
                                                ${childInfo.childBirthCertificate}
                                            </c:if>
                                        </td> 
                                        <td>    
                                            <c:if test="${childInfo.attachedFileLocation eq ''}">
                                                কোন ফাইল নেই
                                            </c:if>
                                            <c:if test="${childInfo.attachedFileLocation ne  ''}">
                                                <!--${childInfo.childBirthCertificate}-->

                                                <a target='_blank' href='${contextPath}/beneficiary/child-information/file-download?id=${childInfo.id}' ><i class='fa fa-download' aria-hidden='true'></i></a>

                                            </c:if>
                                        </td> 

                                        <td>    
                                            <a href="#" onclick="updateChildInfo(${childInfo.id})" title="সম্পাদনা করুন"><span class="glyphicon glyphicon-edit"></span></a>
                                        </td>

                                    </tr>
                                </c:forEach>


                            </tbody>
                        </table>
                    </div>
                    <div style="clear:both">
                        <legend><spring:message code='beneficiaryProfile.PaymentInfo'/></legend>
                        <table id="applicantListTable" class="table table-bordered table-hover">
                            <!--                            <caption>Data table caption</caption>-->
                            <thead>
                                <tr>
                                    <th><spring:message code='beneficiaryProfile.Serial'/></th>
                                    <th><spring:message code='beneficiaryProfile.PaymentDate'/></th>
                                    <th><spring:message code='beneficiaryProfile.PaymentCircle'/></th>
                                    <th><spring:message code='beneficiaryProfile.PaymentProvider'/></th>
                                    <th><spring:message code='beneficiaryProfile.BranchName'/></th>
                                    <th><spring:message code='beneficiaryProfile.AccountNumber'/></th>
                                    <th><spring:message code='beneficiaryProfile.AllowanceAmount'/></th>
                                    <th><spring:message code='beneficiaryProfile.PaymentStatus'/></th>
                                    <!--                                <th>EFT</th>
                                                                    <th></i><spring:message code='label.edit'/></th>-->
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${beneficiaryProfile.beneficiaryProfilePaymentInformations}" var="paymentInfo" varStatus="index">                        
                                    <tr>
                                        <td class="tdSerial">
                                            ${index.index+1}
                                        </td> 
                                        <td>
                                            ${paymentInfo.paymentDate}
                                        </td> 
                                        <td>
                                            <c:if test="${lan eq 'en'}">
                                                ${paymentInfo.paymentCycleEn}
                                            </c:if>
                                            <c:if test="${lan eq 'bn'}">
                                                ${paymentInfo.paymentCycleBn}
                                            </c:if>
                                        </td> 
                                        <td>    
                                            <c:if test="${lan eq 'en'}">
                                                ${paymentInfo.bankNameEn}
                                            </c:if>
                                            <c:if test="${lan eq 'bn'}">
                                                ${paymentInfo.bankNameBn}
                                            </c:if>
                                        </td> 
                                        <td>
                                            <c:if test="${lan eq 'en'}">
                                                ${paymentInfo.branchNameEn}
                                            </c:if>
                                            <c:if test="${lan eq 'bn'}">
                                                ${paymentInfo.branchNameBn}
                                            </c:if>
                                        </td>   
                                        <td>${paymentInfo.accountNO}</td>  
                                        <td> ${paymentInfo.amount}</td>
                                        <td> ${paymentInfo.paymentStatus}</td>
    <!--                                    <td> ${paymentInfo.paymentEFTRefNum}</td>
                                        <td><c:if test="${paymentInfo.paymentStatus eq '0'}"> change </c:if></td>-->
                                        </tr>
                                </c:forEach>


                            </tbody>
                        </table>
                    </div>
                    <div style="clear:both;display: none">
                        <legend>অভিযোগের বৃত্তান্ত</legend>
                        <table id="applicantListTable" class="table table-bordered table-hover">
                            <!--                            <caption>Data table caption</caption>-->
                            <thead>
                                <tr>
                                    <th>ক্রম</th>
                                    <th>তারিখ</th>
                                    <th>অভিযোগের ধরণ</th>
                                    <th>অভিযোগকারী</th>
                                    <th>অভিযোগের বিবরণ</th>
                                    <th>গৃহীত ব্যাবস্থা</th>
                                    <th>অভিযোগ সমাধাঙ্কারী</th>
                                    <th>বর্তমান অবস্থা</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>            
                </form:form>


            </div>  






            <!--            </div>
                    </div>-->
        </div>
    </section>
</div>


<div id="dvChildInfo" style="display: none">
    <section class="content-header clearfix">
        <h1 class="pull-left">

            <spring:message code='beneficiaryProfile.chilfInformation'/>

            <small><i class="fa fa-arrow-circle-left"></i><a href="#"  onclick="back()" >তালিকায় ফিরে যান</a></small>
        </h1>

        <div class="pull-right">
            <a href="#" onclick="addNewChildSubmit()" class="btn bg-green-active"><i class="fa fa-print"></i><spring:message code='label.edit'/></a>


        </div> 


    </section>

    <section class="content" id="beneficiaryForm">    
        <div class="row">
            <form id="formChild" >
                <div class="col-md-12">
                    <div id="ChildInformationAdd" style="clear:both">
                        <div class="container-fluid row">                        
                            <div class="col-lg-6"> 
                                <div class="form-group row">
                                    <div class="col-lg-6">


                                        <label ><spring:message code='beneficiaryProfile.childNo'/></label>                                
                                    </div>
                                    <div class="col-lg-6" >
                                        <select id="cbChildNo" name="cbChildNo" class=" form-control">
                                            <option value="0"><spring:message code='label.select'/></option>                                
                                            <option value="1">১</option>                                
                                            <option value="2">২</option>                                

                                        </select>
                                    </div>

                                </div>
                                <div class="form-group row">
                                    <div class="col-lg-6">
                                        <label><spring:message code='beneficiaryProfile.childBirthCertificate'/></label>                                
                                    </div>
                                    <div class="col-lg-6">
                                        <input type="text" name="tbBirthCertificate" id="tbBirthCertificate" class="form-control"/> 
                                    </div>

                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="form-group row">
                                    <div class="col-lg-6">
                                        <label><spring:message code='beneficiaryProfile.childName'/></label>                                
                                    </div>
                                    <div class="col-lg-6">
                                        <input type="text" name="tbChildName" id="tbChildName" class="form-control"/> 
                                    </div>

                                </div>
                                <div class="form-group row">
                                    <div class="col-lg-6">
                                        <label><spring:message code='beneficiaryProfile.childDOB'/></label>                                
                                    </div>
                                    <div class="col-lg-6">
                                        <input type="text" name="tbDOB"  id="tbDOB" class="form-control"/> 
                                    </div>

                                </div>
                            </div>

                            <div class="col-lg-6">
                                <div class="form-group row">
                                    <div class="col-lg-6">
                                        <label for="photoInput" class="control-label"><spring:message code="label.photo" /></label>                           
                                    </div>
                                    <div class="col-lg-6" id="dvChildPhoto">
                                        <img id="childPhoto" name="childPhoto" src="" style="width: 250px">
                                        <input type="image"  id="removeProfilePhoto" value="Remove" onClick="removeChildPhoto();
                                                return false;" />                    
                                    </div>
                                    <div class="col-lg-6" id="dvFileUpload">
                                        <spring:message code='placeholder.photo' var="photo"/>                                    
                                        <div id="photo">
                                            <input id="photoInput" name="profilePhoto" type="file" class="file" data-allowed-file-extensions='["jpg", "jpeg", "png", "gif"]'>
                                        </div>
                                    </div>

                                </div>
                                <div class="form-group row">

                                </div>
                            </div>


                        </div>
                    </div>
                </div>
            </form>
        </div>
    </section>
</div>

<button type="button" id="btnModal" style="display: none" class="btn btn-primary" data-toggle="modal" data-target="#modaItem"></button>

<div class="modal fade bd-example-modal-md" id="modaItem" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="modalBody">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="btn_close_item_modal"><spring:message code="close"/></button>

            </div>
        </div>
    </div>
</div>  
<script src="<c:url value="/resources/js/htmlToPdf.js" />"></script>

<script type="text/javascript">
                                            var childId_ = 0;
                                            var isUpdateOrSave = 0;

                                            function doNIDCheck(id) {
                                                var data = getDataFromUrl(contextPath + "/NIDcomparison/" + id);
                                                $('#myModal .modal-body').html(data);
                                                var header = getLocalizedText("label.viewComparisonPageHeader", selectedLocale);
                                                $('#myModalLabel').text(header);
                                                $('#myModal .modal-body').css({height: screen.height * .60});
                                                $('#myModal').modal('show');
                                            }
                                            function editBeneficiary() {
                                                var form = $('#beneficiaryInformationForm');
                                                form.validate();
                                                if (!form.valid()) {
                                                    return;
                                                }



                                                var beneficiaryInfo = {
                                                    "id": $("#benId").val(),
                                                    "mobileNo": $("#mobileNo").val(),
                                                    "spouseName": $("#spouseName").val()

                                                }
                                                beneficiaryInfo = JSON.stringify(beneficiaryInfo);
                                                $.ajax(
                                                        {
                                                            type: "POST",
                                                            contentType: 'application/json; charset=utf-8',
                                                            url: "${contextPath}/beneficiary-info/edit",
                                                            dataType: 'json',
                                                            data: beneficiaryInfo,

                                                            success: function (data) {
                                                                if (data.isError === false)
                                                                {

                                                                    $("#modalBody").html("সফল ভাবে সম্পাদনা করা হয়েছে");
                                                                    $("#btnModal").click();
                                                                    $("#tbRemarks").attr("readonly", "true");
                                                                    $("#btnAdd").hide();
                                                                }
                                                                if (data.isError === true) {
                                                                    console.log(data);
                                                                    var msg = ""
                                                                    if (data.errorCode == 401) {
                                                                        msg = lan == "bn" ? "ইতমধ্যে অন্তর্ভুক্ত  হয়েছে" : "Alreaddy Added";
                                                                    } else if (data.errorCode == 500) {
                                                                        msg = lan == "bn" ? "সার্ভার এ সমস্যা" : "Server Side Error";
                                                                    } else
                                                                    {
                                                                        msg = lan == "bn" ? "ভাতাভোগীর সংখ্যা অমিল রয়েছে" : "Beneficiary Count not match";
                                                                    }

                                                                    $("#modalBody").html(msg);
                                                                    $("#btnModal").click();
                                                                    return;
                                                                }



                                                            },
                                                            failure: function () {
                                                                $("#btLoader").click();
                                                                console.log("Failed");
                                                            }
                                                        });

                                            }


                                            function back() {
                                                childId_ = 0;
                                                $("#dvBenInfo").show();
                                                $("#dvChildInfo").hide();

                                                if (isUpdateOrSave)
                                                {
                                                    location.reload();
                                                }
                                            }
                                            function removeChildPhoto(childId)
                                            {
                                                $.ajax(
                                                        {type: 'GET',
                                                            url: "${contextPath}/beneficiary/get-child-info/file-delete/" + childId_,
                                                            contentType: 'application/json; charset=utf-8',
                                                            dataType: 'json',
                                                            success: function (data) {
                                                                console.log(data);
                                                                if (data.isError == false) {
                                                                    isUpdateOrSave = 1;

                                                                    $("#childPhoto").attr('src', '');
                                                                    $("#dvChildPhoto").hide();
                                                                    $("#dvFileUpload").show();
                                                                }


                                                            }
                                                        });

                                            }
                                            function updateChildInfo(childId) {

                                                addNewChild();
                                                childId_ = childId;
                                                $("#dvChildPhoto").show();
                                                $.ajax(
                                                        {type: 'GET',
                                                            url: "${contextPath}/beneficiary/get-child-info/" + childId,
                                                            contentType: 'application/json; charset=utf-8',
                                                            dataType: 'json',
                                                            success: function (data) {
                                                                console.log(data);
                                                                if (data.isError == false) {

                                                                    //SearchApplicant();
                                                                    $("#tbChildName").val(data.returnSingleObject.childName);

                                                                    $("#cbChildNo").val(data.returnSingleObject.childNo);

                                                                    if (selectedLocale == "bn")
                                                                    {
                                                                        $("#tbBirthCertificate").val(getNumberInBangla(data.returnSingleObject.childBirthCertificate.toString()));
                                                                        $("#tbDOB").val(getNumberInBangla(data.returnSingleObject.childDob));
                                                                    } else {
                                                                        $("#tbDOB").val(data.returnSingleObject.childDob);
                                                                        $("#tbBirthCertificate").val(data.returnSingleObject.childBirthCertificate);
                                                                    }
                                                                    if (data.returnSingleObject.attachedFileLocation != "" && data.returnSingleObject.attachedFileLocation.length > 5)
                                                                    {
                                                                        $("#childPhoto").attr('src', 'data:image/png;base64,' + data.returnSingleObject.base64);
                                                                        $("#dvChildPhoto").show();
                                                                        $("#dvFileUpload").hide();
                                                                    } else
                                                                    {
                                                                        $("#dvChildPhoto").hide();
                                                                        $("#dvFileUpload").show();


                                                                    }
                                                                }


                                                            }
                                                        });


                                            }
                                            function addNewChild()
                                            {
                                                childId_ = 0;
                                                $("#dvBenInfo").hide();
                                                $("#dvChildInfo").show();
                                                $("#dvChildPhoto").hide();
                                            }
                                            function   addNewChildSubmit()
                                            {

                                                var form = $('#formChild');
                                                form.validate();
                                                if (!form.valid()) {
                                                    return;
                                                }
                                                var objFormData = new FormData();
                                                var i = 0;
                                                var size = 0;
                                                while (i < $("#photoInput")[0].files.length)
                                                {
                                                    console.log(i);
                                                    objFormData.append('file', $("#photoInput")[0].files[i]);
                                                    size = size + $("#photoInput")[0].files[i].size;
                                                    i = i + 1;
                                                }
//                    objFormData.append('childName', $("#tbChildName").val());
//                    objFormData.append('childNo', $("#cbChildNo").val());
//                    objFormData.append('childDob', $("#tbDOB").val());
//                    objFormData.append('benId', $("#benId").val());
//                    objFormData.append('childBirthCertificate', $("#tbBirthCertificate").val());

                                                var childInfo = {
                                                    "id": childId_,
                                                    "childName": $("#tbChildName").val(),
                                                    "childNo": $("#cbChildNo").val(),
                                                    "dob_st": getNumberInEnglish($("#tbDOB").val().toString()),
                                                    "childBirthCertificate": $("#tbBirthCertificate").val(),
                                                    "benId": $("#benId").val()
                                                }
                                                var childInfo = JSON.stringify(childInfo);

                                                objFormData.append('childInfo', childInfo);
                                                $.ajax(
                                                        {
                                                            type: 'POST',
                                                            url: "${contextPath}/beneficiary/child-info-add",
                                                            contentType: 'application/json; charset=utf-8',
                                                            data: objFormData,
                                                            dataType: 'json',
                                                            contentType: false,
                                                            processData: false,
                                                            success: function (data) {
                                                                if (data.isError === false)
                                                                {
                                                                    isUpdateOrSave = 1;
                                                                    //SearchApplicant();
                                                                    $("#modalBody").html("সফল ভাবে সম্পাদনা করা হয়েছে");
                                                                    $("#btnModal").click();
                                                                    $("#tbRemarks").attr("readonly", "true");
                                                                    $("#btnAdd").hide();
                                                                }
                                                                if (data.isError === true) {
                                                                    console.log(data);
                                                                    var msg = ""
                                                                    if (data.errorCode == 401) {
                                                                        msg = lan == "bn" ? "ইতমধ্যে অন্তর্ভুক্ত  হয়েছে" : "Alreaddy Added";
                                                                    } else if (data.errorCode == 500) {
                                                                        msg = lan == "bn" ? "সার্ভার এ সমস্যা" : "Server Side Error";
                                                                    } else
                                                                    {
                                                                        msg = lan == "bn" ? "ভাতাভোগীর সংখ্যা অমিল রয়েছে" : "Beneficiary Count not match";
                                                                    }

                                                                    $("#modalBody").html(msg);
                                                                    $("#btnModal").click();
                                                                    return;
                                                                }



                                                            }
                                                        });

                                            }
                                            function beneficiaryEdit()
                                            {

                                            }
                                            function generatePDF() {
                                                // Choose the element that our invoice is rendered in.
                                                const element = document.getElementById("beneficiaryForm");
                                                // Choose the element and save the PDF for our user.
                                                html2pdf()
                                                        .from(element)
                                                        .save("${beneficiaryProfile.beneficiaryProfile.nameBn}");
                                            }
                                            function PrintElem()
                                            {

                                                var mywindow = window.open('', 'PRINT', 'height=100%,width=100%');
                                                var myStyle = '<link rel="stylesheet" type="text/css"  media="all" href="<c:url value="/resources/css/bootstrap.min.css" />" >';
                                                var myStyle1 = '<link rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min.css" />">';

                                                mywindow.document.write('<html><head><title>' + document.title + '</title>');
                                                mywindow.document.write('</head><body >');
                                                mywindow.document.write('<h1>' + document.title + '</h1>');
                                                mywindow.document.write(document.getElementById("beneficiaryForm").innerHTML);
                                                mywindow.document.write('</body></html>');
                                                mywindow.document.write(myStyle + jQuery('.table_disp').html());
                                                mywindow.document.write(myStyle1 + jQuery('.table_disp').html());
                                                mywindow.document.close(); // necessary for IE >= 10
                                                mywindow.focus(); // necessary for IE >= 10*/                
                                                mywindow.print();
                                                mywindow.close();

                                                return true;
                                            }
                                            $(function () {
                                                $(".tdSerial").each(function (i, e) {

                                                    $(this).html(getNumberInBangla($(this).html()))
                                                })
                                                initDate($("#tbDOB"), 'dd-mm-yy', $("#tbDOB\\.icon"), selectedLocale);

                                                if (selectedLocale === 'bn') {
                                                    $("#tbDOB").val(getNumberInBangla($("#tbDOB").val()));
                                                    makeUnijoyEditor('tbBirthCertificate');
                                                    makeUnijoyEditor('mobileNo');
                                                    makeUnijoyEditor('spouseName');
                                                }

                                                document.getElementById("tbBirthCertificate").addEventListener("keydown", function (event) {
                                                    checkNumberWithLength(event, this, 17);
                                                });

                                                var mobileNo = document.getElementById("mobileNo");
                                                mobileNo.addEventListener("keydown", function (event) {
                                                    checkNumberWithLength(event, this, 11);
                                                });

                                                $("#beneficiaryInformationForm").validate({

                                                    rules: {
                                                        "mobileNo": {
                                                            required: true,
                                                            minlength: 11,
                                                            maxlength: 11,
                                                            checkMobileNo: true
                                                        },
                                                        "spouseName": {
                                                            required: true
                                                        }
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
                                                $("#formChild").validate({
                                                    rules: {
                                                        "cbChildNo": {
                                                            required: true,

                                                        },
                                                        "tbChildName": {
                                                            required: true
                                                        },

                                                        "tbDOB": {
                                                            required: true,
                                                            checkDateOfBirth: {locale: selectedLocale, min: 0, max: 5}

                                                        },
                                                        "tbBirthCertificate": {
                                                            required: true,
                                                        }

                                                    },
                                                    errorPlacement: function (error, element) {
                                                        error.insertAfter(element);
                                                    }
                                                });

                                                $("#dvChildPhoto").hide();
                                            });

</script>

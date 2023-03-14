<%-- 
    Document   : userProfile
    Created on : May 25, 2016, 12:42:24 PM
    Author     : Philip
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<div id="page-wrapper">
    <div class="row top-buffer">
        <div class="col-lg-12">             
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        ${usersDetail.employeeDetail.firstNameSt}
                        ${usersDetail.employeeDetail.middleNameSt}
                        ${usersDetail.employeeDetail.lastNameSt}
                    </h3>
                </div>
                <div class="panel-body">
                    <div class="row">                        
                        <div class="col-md-3 col-lg-3 " align="center"> 
                            <img alt="User Pic" src="data:image/png;base64,${profilePic}" class="img-circle img-responsive">
                        </div>
                        <div class=" col-md-9 col-lg-9 "> 
                            <table class="table table-user-information">
                                <tbody>
                                    <tr>
                                        <td>Division</td>
                                        <td>${usersDetail.employeeDetail.divisionItemObject.name}</td>
                                    </tr>
                                    <tr>
                                        <td>Department:</td>
                                        <td>${usersDetail.employeeDetail.departmentItemObject.name}</td>
                                    </tr>
                                    <tr>
                                        <td>Joining date:</td>
                                        <td><fmt:formatDate pattern="EEE, d MMM yyyy" value="${usersDetail.employeeDetail.joiningDateDt.time}"/></td>
                                </tr>
                                <tr>
                                    <td>Date of Birth</td>
                                    <td><fmt:formatDate pattern="EEE, d MMM yyyy" value="${usersDetail.employeeDetail.dateOfBirthDt.time}"/></td>
                                </tr>
                                <tr>
                                    <td>Gender</td>
                                    <td>${usersDetail.employeeDetail.genderSt}</td>
                                </tr>
                                <tr>
                                    <td>Present Address</td>
                                    <td>${usersDetail.employeeDetail.presentAddressSt}</td>
                                </tr>
                                <tr>
                                    <td>Email</td>
                                    <td><a href="mailto:info@support.com">${usersDetail.employeeDetail.emailAddressSt}</a></td>
                                </tr>
                                <tr>
                                    <td>Phone Number</td>
                                    <td>${usersDetail.employeeDetail.mobileSt}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
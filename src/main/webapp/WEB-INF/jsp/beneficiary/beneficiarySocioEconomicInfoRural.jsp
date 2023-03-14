<%-- 
    Document   : applicantSocioEconomicInfo
    Created on : Aug 19, 2018, 2:12:28 PM
    Author     : Philip
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<spring:message code='label.select' var="select"/>
<div class="form-group">
    <label for="landSizeRural" class="col-md-4 control-label"><spring:message code="label.landOwnerShip" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="landSizeRural">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${landOwnerShipEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="landSizeRural" cssStyle="color:red"></form:errors>
        </div>            
    </div>      
    <div class="form-group">
        <label for="occupationRural" class="col-md-4 control-label"><spring:message code="label.occupation" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="occupationRural">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${occupationRuralEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="occupationRural" cssStyle="color:red"></form:errors>
        </div>            
    </div>
    <div class="form-group">
        <label for="occupationUrban" class="col-md-4 control-label"><spring:message code="label.monthlyIncome" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="monthlyIncome">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${monthlyIncomeList}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="monthlyIncome" cssStyle="color:red"></form:errors>
        </div>            
    </div>     
    <div class="form-group">
        <label for="hASLatrineRural" class="col-md-4 control-label"><spring:message code="label.hASLatrine" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="hASLatrineRural">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="hASLatrineRural" cssStyle="color:red"></form:errors>
        </div>            
    </div>
    <div class="form-group">
        <label for="hASElectricity" class="col-md-4 control-label"><spring:message code="label.hASElectricity" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="hASElectricity">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="hASElectricity" cssStyle="color:red"></form:errors>
        </div>            
    </div>
    <div class="form-group">
        <label for="hASElectricFan" class="col-md-4 control-label"><spring:message code="label.hASElectricFan" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="hASElectricFan">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="hASElectricFan" cssStyle="color:red"></form:errors>
        </div>            
    </div>
    <div class="form-group">
        <label for="hASTubewellRural" class="col-md-4 control-label"><spring:message code="label.hASTubewell" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="hASTubewellRural">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="hASTubewellRural" cssStyle="color:red"></form:errors>
        </div>            
    </div>
    <div class="form-group">
        <label for="hHWallmadeOf" class="col-md-4 control-label"><spring:message code="label.hhWallMadeOf" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="hHWallMadeOf">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${hHWallMadeOfList}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="hHWallMadeOf" cssStyle="color:red"></form:errors>
        </div>            
    </div>     
    <div class="form-group">
        <label for="disability" class="col-md-4 control-label"><spring:message code="label.iSDisabled" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="disability">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${disabilityList}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="disability" cssStyle="color:red"></form:errors>
    </div>            
</div>    
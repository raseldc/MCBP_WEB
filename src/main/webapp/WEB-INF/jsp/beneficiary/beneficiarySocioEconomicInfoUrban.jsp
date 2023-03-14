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
    <label for="hasResidenceUrban" class="col-md-4 control-label"><spring:message code="label.hasResidence" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="hasResidenceUrban">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="hasResidenceUrban" cssStyle="color:red"></form:errors>
        </div>            
    </div>      
    <div class="form-group">
        <label for="occupationUrban" class="col-md-4 control-label"><spring:message code="label.occupation" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="occupationUrban">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${occupationUrbanEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="occupationUrban" cssStyle="color:red"></form:errors>
        </div>            
    </div>
    <div class="form-group">
        <label for="monthlyIncome" class="col-md-4 control-label"><spring:message code="label.monthlyIncome" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="monthlyIncome">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${monthlyIncomeList}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="monthlyIncome" cssStyle="color:red"></form:errors>
        </div>            
    </div>       
    <div class="form-group">
        <label for="hASKitchenUrban" class="col-md-4 control-label"><spring:message code="label.hASKitchen" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="hASKitchenUrban">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="hASKitchenUrban" cssStyle="color:red"></form:errors>
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
        <label for="hASTelivisionUrban" class="col-md-4 control-label"><spring:message code="label.hASTelivision" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="hASTelivisionUrban">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="hASTelivisionUrban" cssStyle="color:red"></form:errors>
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
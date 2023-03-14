<%-- 
    Document   : beneficiarySocioEconomicInfo
    Created on : Aug 19, 2018, 2:43:57 PM
    Author     : Philip
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<spring:message code='label.select' var="select"/>
<div class="form-group">
    <label for="applicantSocioEconomicInfo.iSFirstEarner" class="col-md-4 control-label"><spring:message code="label.iSFirstEarner" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="iSFirstEarner">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="iSFirstEarner" cssStyle="color:red"></form:errors>
        </div>            
    </div>
    <div class="form-group">
        <label for="middleNameInput" class="col-md-4 control-label"><spring:message code="label.monthlyIncome" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <spring:message code='label.monthlyIncome' var="monthlyIncome"/>
        <form:input class="form-control" placeholder="${monthlyIncome}" path="monthlyIncome" autofocus="autofocus"/>
        <form:errors path="monthlyIncome" cssStyle="color:red"></form:errors>
        </div>
    </div>    
    <div class="form-group">
        <label for="religionInput" class="col-md-4 control-label"><spring:message code="label.iSDisabled" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="iSDisabled">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="iSDisabled" cssStyle="color:red"></form:errors>
        </div>            
    </div>
    <div class="form-group">
        <label for="religionInput" class="col-md-4 control-label"><spring:message code="label.hASHouse" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="hASHouse">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="hASHouse" cssStyle="color:red"></form:errors>
        </div>            
    </div>
    <div class="form-group">
        <label for="religionInput" class="col-md-4 control-label"><spring:message code="label.hASPond" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <form:select class="form-control" path="hASPond">
            <form:option value="" label="${select}"></form:option>
            <form:options items="${yesNoEnum}"  itemLabel="${displayName}"></form:options>
        </form:select>
        <form:errors path="hASPond" cssStyle="color:red"></form:errors>
        </div>            
    </div>
    <div class="form-group">
        <label for="" class="col-md-4 control-label"><spring:message code="label.age" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <spring:message code='label.age' var="age"/>
        <form:input class="form-control" placeholder="${age}" path="age" disabled="true" />
        <form:errors path="age" cssStyle="color:red"></form:errors>
        </div>
    </div>     
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
        <label for="" class="col-md-4 control-label"><spring:message code="label.conceptionDuration" /><span class="mandatory">*</span></label>
    <div class="col-md-8">
        <spring:message code='label.conceptionDuration' var="conceptionDuration"/>
        <form:input class="form-control" placeholder="${conceptionDuration}" path="conceptionDuration"/>
        <form:errors path="conceptionDuration" cssStyle="color:red"></form:errors>
    </div>
</div>     

<%-- 
    Document   : notice
    Created on : Sep 20, 2018, 4:53:04 PM
    Author     : user
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script>
</script>
<style>
    .noticeDiv
    {
        margin-bottom: 10px;
    }
    .noticeLabel
    {
        background-color: #EEE;
        padding: 5px 0px 5px 5px;
    }
</style>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="label.notice" />           
    </h1>
</section>
<section class="content">
    <div class="row">
        <div class="col-md-8">
            <div class="row noticeDiv">
                <div class="col-md-12">
                    <div class="col-md-4 noticeLabel"><spring:message code="label.noticeTitle" /></div>
                    <div class="col-md-8"  style="font-size: 16px"><strong><c:out value="${notice.noticeBn}"></c:out></strong></div>                    
                    </div>
                </div>
            <div class="row noticeDiv">
                    <div class="col-md-12">
                        <div class="col-md-4 noticeLabel"><spring:message code="label.noticeDate" /></div>
                    <div class="col-md-8"><fmt:formatDate type = "date" 
                                    dateStyle = "long" timeStyle = "long" value = "${notice.noticeDate.time}" /></div>
                </div>
            </div>
            <div class="row noticeDiv">
                <div class="col-md-12">
                    <div class="col-md-4 noticeLabel"><spring:message code="label.noticeDes" /></div>
                    <div class="col-md-8"><c:out value="${notice.description}"></c:out></div>                    
                </div>
            </div>
        </div>
    </div>
</section>



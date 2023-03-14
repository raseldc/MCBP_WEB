<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<section class="content-header">
    <h1>
        <spring:message code="title.customError"/>
        <small></small>
    </h1>
    <ol class="breadcrumb">
<!--        <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
        <li class="active">Here</li>-->
    </ol>
</section>
<section class="content">

    <h4>${exception.message}</h4>

    <a href="#" class="btn btn-large btn-info" onClick="history.back();return false;"><i class="icon-home icon-white"></i> <spring:message code="label.back"/></a>
</section>
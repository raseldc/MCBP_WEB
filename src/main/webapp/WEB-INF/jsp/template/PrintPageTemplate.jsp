<%-- 
    Document   : PrintPageTemplate
    Created on : Apr 29, 2017, 4:30:31 PM
    Author     : Philip
--%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
    <head>        
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">

        <!--<title><tiles:getAsString name="title" /></title>-->
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.6 -->
        <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css" />">
              <!-- jQuery 2.2.3 -->
        <script src="<c:url value="/resources/js/jquery-2.2.3.min.js" />"></script>
        <!-- Bootstrap 3.3.6 -->
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>

        <script src="<c:url value="/resources/plugins/messageResources/messageResource.min.js" />" ></script>

        <!-- Font Awesome -->
        <link rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min.css" />">
              <link rel="stylesheet" href="<c:url value="/resources/css/site.css" />">
              <!--        <link rel="stylesheet" href="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css" />">-->
              <!-- Ionicons -->
              <link rel="stylesheet" href="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css" />">
              <!-- Theme style -->
              <link rel="stylesheet" href="<c:url value="/resources/css/AdminLTE.min.css" />">        
              <!--<link rel="stylesheet" href="resources/css/skins/skin-blue.min.css"> -->
              <link rel="stylesheet" href="<c:url value="/resources/css/skins/skin-blue.min.css" />"> 

              <link rel="stylesheet" href="<c:url value="/resources/css/custom.css" />"> 

              <link rel="stylesheet" href="<c:url value="/resources/plugins/iCheck/flat/blue.css" />">
              <link rel="stylesheet" href="<c:url value="/resources/plugins/iCheck/square/blue.css" />">
              <link rel="shortcut icon" type="image/x-icon"  href="<c:url value="/resources/img/favicon.ico" />?">
        <script src="<c:url value="/resources/plugins/iCheck/icheck.min.js" />"></script>
        <script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>

    </head>
    <script>
        var selectedLocale = '${pageContext.response.locale}';
        var contextPath = '${pageContext.request.contextPath}';

        $(function () {
            $('input').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_flat-blue',
                increaseArea: '20%' // optional
            });

            messageResource.init({
                filePath: contextPath + '/resources'
            });
            messageResource.load('messages_bn', function () {});
            messageResource.load('messages_en', function () {});
            includeJs(contextPath + "/resources/plugins/validation/src/localization/messages_" + selectedLocale + ".js");
            $('.modal').bind('ajaxStart', function () {
                $(this).show();
            }).bind('ajaxStop', function () {
                $(this).hide();
            });

        });
    </script>
    <!--    <body class="hold-transition skin-blue-light sidebar-mini">-->
    <c:set var="title"><tiles:getAsString  name="title" ignore="true"/></c:set>
    <title><spring:message code="${title}"></spring:message> </title>

    
    <body class="hold-transition skin-blue sidebar-mini" onload="window.print();">
<!--        <div class="wrapper">
            <div class="content-wrapper">-->
                <div class="wrapper">
            <div >
                <tiles:insertAttribute name="body" /> 
            </div>
        </div>
    </body>
</html>
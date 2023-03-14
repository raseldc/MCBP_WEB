<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<% request.getSession().setAttribute("Contexpath", request.getContextPath());%>
<% request.getSession().setAttribute("asset",1.9);%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <c:set var="title"><tiles:getAsString  name="title" ignore="true"/></c:set>
        <title><spring:message code="${title}"></spring:message> </title>        
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
            <!--<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css" />">-->
        <link href="${Contexpath}/resources/css/bootstrap.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<c:url value="/resources/css/custom.css" />"> 
        <link rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min.css" />">
        <script src="<c:url value="/resources/js/jquery-2.2.3.min.js" />"></script>
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
        <script src="<c:url value="/resources/js/utility.js" />"></script>
        <script src="<c:url value="/resources/plugins/messageResources/messageResource.min.js" />" ></script>
        <link rel="stylesheet" href="<c:url value="/resources/css/site.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/plugins/iCheck/square/blue.css" />">
        <link rel="shortcut icon" type="image/x-icon"  href="<c:url value="/resources/img/favicon.ico" />?">
        <script src="<c:url value="/resources/plugins/iCheck/icheck.min.js" />"></script>
        <script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>
        <script type="text/javascript">
            var selectedLocale = '${pageContext.response.locale}';
            var contextPath = '${pageContext.request.contextPath}';

            $(function () {
                initIcheck();

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
    </head>    
    <style type="text/css">
        /*    #html css is added by me for footer issue */
        html {
            position: relative;
            min-height: 100%;
        }
        /*    .container{
                padding-right: 0;
                padding-left: 0;
            }*/
        /*    body{
                background: url('resources/img/bg.jpg') no-repeat center center #CDE7A8 fixed;
                background: url('resources/img/bg.jpg') no-repeat center center fixed;
                -webkit-background-size: cover;
                -moz-background-size: cover;
                -o-background-size: cover;
                background-size: cover;
            }*/
    </style>

    <body class="hold-transition">
        <div>
            <tiles:insertAttribute name="header" />
            <!--<div class="container content-wrapper" style="min-height: 600px; padding-bottom:100px; background-color: #fff">-->
            <div class="container content-wrapper" style="min-height: 600px; padding-bottom:100px;background-color: #fff">
                <tiles:insertAttribute name="body" /> 
                <div class="modal" style="display: none">
                    <div class="center">
                        <img alt="Loading..." src="${pageContext.request.contextPath}/resources/img/loading.gif" />
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />  
        </div>
    </body>
</html>
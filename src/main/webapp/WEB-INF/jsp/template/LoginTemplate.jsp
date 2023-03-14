<%-- 
    Document   : LoginTemplate
    Created on : Jan 11, 2017, 11:50:21 AM
    Author     : Philip
--%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>MoWCA | Log in</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.6 -->
        <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css" />">
        <!-- jQuery 2.2.3 -->
        <script src="<c:url value="/resources/js/jquery-2.2.3.min.js" />"></script>
        <!-- Bootstrap 3.3.6 -->
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
        <link rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min.css" />">
        <!-- Ionicons -->
        <link rel="stylesheet" href="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css" />">
        <!-- Theme style -->
        <link rel="stylesheet" href="<c:url value="/resources/css/AdminLTE.min.css" />">     
        <!-- iCheck -->
        <link rel="stylesheet" href="<c:url value="/resources/plugins/iCheck/square/blue.css" />">
    </head>
    <body class="hold-transition login-page">
        <tiles:insertAttribute name="loginPage" />
        <!-- iCheck -->
        <script src="<c:url value="/resources/plugins/iCheck/icheck.min.js" />"></script>
        <script>
            $(function () {
                $('input').iCheck({
                    checkboxClass: 'icheckbox_square-blue',
                    radioClass: 'iradio_square-blue',
                    increaseArea: '20%' // optional
                });
            });
        </script>
    </body>
</html>


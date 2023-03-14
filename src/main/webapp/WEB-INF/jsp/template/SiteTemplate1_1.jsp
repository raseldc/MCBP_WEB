<!DOCTYPE html>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%--<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>--%>

<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/resources/images/vela.ico"/>">
        <title><tiles:getAsString name="title" /></title>

        <!-- Bootstrap Core CSS -->
        <link href="<c:url value="/resources/styles/bootstrap.min.css"/>" rel="stylesheet">

        <!-- MetisMenu CSS -->
        <link href="<c:url value="/resources/styles/metisMenu.min.css"/>" rel="stylesheet">

        <!-- Custom CSS -->
        <link href="<c:url value="/resources/styles/theme.css"/>" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="<c:url value="/resources/styles/font-awesome.min.css"/>" rel="stylesheet">

        <!-- jQuery -->
        <script src="<c:url value="/resources/scripts/jquery.min.js" />"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="<c:url value="/resources/scripts/bootstrap.min.js" />"></script>

        <!-- Metis Menu Plugin JavaScript -->
        <script src="<c:url value="/resources/scripts/metisMenu.min.js" />">
        </script>

        <!-- Custom Theme JavaScript -->
        <script src="<c:url value="/resources/scripts/theme.js" />"></script>      
    </head>
    <body>
        <div id="wrapper">
            <!-- Header -->
            <tiles:insertAttribute name="header" />
            <!-- Body Page -->           
            <tiles:insertAttribute name="body" />
            <!-- Footer Page -->
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>
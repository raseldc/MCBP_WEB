<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- Content Header (Page header) -->    
<section class="content-header">
    <h1>
        Page Not Found
        <small><font face="Tahoma" color="red">Error 404</font></small>
    </h1>
<!--    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
        <li class="active">Here</li>
    </ol>-->
</section>

<!-- Main content -->
<section class="content">
    <!-- Your Page Content Here -->
    <p>The page you requested could not be found, either contact your system administrator or try again. Use your browsers <b>Back</b> button to navigate to the page you have previously come from</p>    
    
    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-large btn-info"><i class="icon-home icon-white"></i> Take Me Home</a>
</section>
<!-- /.content -->
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title><tiles:getAsString name="title" /></title>

        <!-- Bootstrap Core CSS -->
        <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css" />">


              <!-- Custom CSS -->
              <link rel="stylesheet" href="<c:url value="/resources/css/landing-page.css" />">

              <!-- Custom Fonts -->
              <link rel="stylesheet"  href="<c:url value="/resources/css/font-awesome.min.css" />">
              <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">


        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

        <!-- jQuery -->
        <script src="<c:url value="/resources/js/jquery-2.2.3.min.js" />"></script>    
        <!--<script src="js/jquery.js"></script>-->

        <!-- Bootstrap Core JavaScript -->
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>

        <script src="<c:url value="/resources/js/utility.js" />" ></script>

    </head>

    <body>

        <!-- Navigation -->
        <div class="wrapper">
        <tiles:insertAttribute name="header" />
        
        <div class="content-wrapper">
        <tiles:insertAttribute name="body" />
        </div>
        <!-- Footer -->
        <footer>
        <div class="container">
        <div class="row">
        <div class="col-lg-12">
              <ul class="list-inline">
              <li>
              <a href="#">Home</a>
              </li>
<li class="footer-menu-divider">&sdot;</li>
              <li>
              <a href="#about">About</a>
              </li>
              <li class="footer-menu-divider">&sdot;</li>
              <li>
              <a href="#services">Services</a>
              </li>
              <li class="footer-menu-divider">&sdot;</li>
        <li>
        <a href="#contact">Contact</a>
        </li>
        </ul>
        <p class="copyright text-muted small">Copyright &copy; Your Company 2014. All Rights Reserved</p>
              </div>
              </div>
              </div>
              </footer>


        </div>
        </body>

        </html>

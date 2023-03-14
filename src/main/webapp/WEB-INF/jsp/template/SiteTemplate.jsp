<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<% request.getSession().setAttribute("asset",1.9);%>
<!DOCTYPE html>
<html lang="en">
    <head>     
    <c:set var="title"><tiles:getAsString  name="title" ignore="true"/></c:set>

    <title><spring:message code="${title}"></spring:message> </title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" type="text/css"  media="all" href="<c:url value="/resources/css/bootstrap.min.css" />" >
        <script src="<c:url value="/resources/js/jquery-2.2.3.min.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/site.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/AdminLTE.min.css" />">        
<link rel="stylesheet" href="<c:url value="/resources/css/skins/skin-blue.min.css" />"> 
<link rel="stylesheet" href="<c:url value="/resources/css/custom.css" />"> 
<link rel="stylesheet" href="<c:url value="/resources/plugins/iCheck/flat/blue.css" />">
<link rel="stylesheet" href="<c:url value="/resources/plugins/iCheck/square/blue.css" />">
<link rel="shortcut icon" type="image/x-icon"  href="<c:url value="/resources/img/favicon.ico" />?">        
<link rel="stylesheet" href="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.css" />">

<link  rel="stylesheet" href="<c:url value="/resources/plugins/datatables/datatables.net-buttons-bs/css/buttons.bootstrap.min.css"/>">
<script src="<c:url value="/resources/plugins/messageResources/messageResource.min.js" />" ></script>
<script src="<c:url value="/resources/plugins/scroll/jquery.slimscroll.min.js" />" ></script>
<link rel="stylesheet" href="<c:url value="/resources/plugins/fileUpload/fileinput.min.css" />"> 
<script src="<c:url value="/resources/js/app.min.js" />"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/responsive.dataTables.css" />">
<link rel="stylesheet" href="<c:url value="/resources/js/dataTables.responsive.js" />">
<script src="<c:url value="/resources/js/js.cookie.js" />"></script>
<script src="<c:url value="/resources/js/formtocookie.js" />"></script>
<script type="text/javascript">
    var selectedLocale = '${pageContext.response.locale}';
    var contextPath = '${pageContext.request.contextPath}';
    function menuSelect(url) {
        var element = $('.treeview-menu li a[href="' + url + '"]').parent().addClass('active');
        var parentUl, liToMakeActive;
        if (element)
        {
            parentUl = element.parent();
            while (parentUl.length > 0 && parentUl[0].className != 'sidebar-menu') {
                liToMakeActive = parentUl.parent();
                liToMakeActive.addClass("active");
                parentUl = liToMakeActive.parent();
            }
        }
    }
    $(function () {
        menuSelect(window.location.pathname);
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
</head>    

<body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
        <tiles:insertAttribute name="header" />
        <tiles:insertAttribute name="sidebar" />
        <div class="content-wrapper">
            <script src="<c:url value="/resources/js/unijoy.js" />" ></script>
            <script src="<c:url value="/resources/plugins/iCheck/icheck.min.js" />"></script>
            <script src="<c:url value="/resources/plugins/validation/jquery.validate.js" />" ></script>          
            <script src="<c:url value="/resources/js/bootbox.min.js"/>" type="text/javascript"></script>
            <script src="<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js" />" ></script>
            <script src="<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js" />" ></script>

            <script src="<c:url value="/resources/js/utility.js?v1=${asset}" />" ></script>       
            <script src="<c:url value="/resources/plugins/fileUpload/fileinput.min.js" />" ></script>

            <script src="https://cdn.datatables.net/buttons/1.5.6/js/dataTables.buttons.min.js "></script>
            <script src="https://cdn.datatables.net/buttons/1.5.6/js/buttons.flash.min.js "></script>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js "></script>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js "></script>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js "></script>
            <script src="https://cdn.datatables.net/buttons/1.5.6/js/buttons.html5.min.js "></script>
            <script src="https://cdn.datatables.net/buttons/1.5.6/js/buttons.print.min.js "></script>
            <tiles:insertAttribute name="body" /> 
            <jsp:include page="../applicant/modalPage.jsp" />
            <jsp:include page="../applicant/commentModal.jsp" />
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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script>

    $(document).ready(function () {
        if ('${pageContext.response.locale}' == 'bn')
        {
            $("#goals").css("list-style", "bengali");
        } else
            $("#goals").css("list-style", "english");
    });

</script>
<style>
    .home-page-image-container {
        position: relative;
    }
    .tag {
       float: left;
       position: absolute;
       left: 0px;
       top: 100px;
       z-index: 1000;
       border-top-right-radius: 2em;
       border-bottom-right-radius: 2em;
       background-color: #fff;
       padding: 5px 10px;
       color:  #337ab7;
       font-weight: bold;
    }
    .container {
        padding-right: 0;
        padding-left: 0;
    }
</style>
<div class="home-page-image-container">
    <div class="tag">
        <span style="font-size: 26px">এম আই এস/MIS</span>
        <br>
        <span><spring:message code="label.deptOfWomenAffairs"/></span>
    </div>
    <img src="${pageContext.request.contextPath}/resources/img/bg.jpg" style="width:100%"></img>
</div>    
<p class="content text-justify">
    <spring:message code="public.paragraph1" />
</p>
<p class="content text-justify">
    <spring:message code="public.paragraph2" />
</p>
<section class="content text-justify">
    <div style="font-weight: bold; padding-bottom: 5px; color:#3C8DBC"><spring:message code="public.featureHeader" /></div>
    <ul class="fa-ul">
        <li><i class="fa-li fa fa-check-square"></i><spring:message code="public.feature1" /></li>
        <li><i class="fa-li fa fa-check-square"></i><spring:message code="public.feature2" /></li>
        <li><i class="fa-li fa fa-check-square"></i><spring:message code="public.feature3" /></li>
        <li><i class="fa-li fa fa-check-square"></i><spring:message code="public.feature4" /></li>
        <li><i class="fa-li fa fa-check-square"></i><spring:message code="public.feature5" /></li>
    </ul>
</section>

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

<!-- Header -->
<section class="content-header" style="padding-top: 20px">
    <h2><spring:message code="introHeader"/></h2>
</section>
<div class="content" style="font-weight: normal; text-align: justify; font-size: 16px;">
    <div class="form-group">
    <spring:message code="intro" />
    </div>
</div>
<section class="content-header"  style="padding-top: 20px">
    <h2><spring:message code="goalsHeader"/></h2>
</section>
<section class="content" style="font-weight: normal; text-align: justify; font-size: 16px;">    
    <ol id="goals">
        <li><spring:message code="goals_purpose1" /></li>
        <li><spring:message code="goals_purpose2" /></li>
        <li><spring:message code="goals_purpose3" /></li>
        <li><spring:message code="goals_purpose4" /></li>
        <li><spring:message code="goals_purpose5" /></li>
        <li><spring:message code="goals_purpose6" /></li>
        <li><spring:message code="goals_purpose7" /></li>
        <li><spring:message code="goals_purpose8" /></li>
    </ol>    </ol>

</section>
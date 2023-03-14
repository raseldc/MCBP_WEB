<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head><title>Endpoint list</title></head>
<body>
<div class="container">
    <h1>Spring MVC 3.1 Demo Endpoints</h1>
    <c:forEach items="${handlerMethods}" var="entry">
      <div>
        <hr>
        <p><strong>${entry.value}</strong></p>      
      </div>
      <div class="span-3 colborder">
        <p>
          <span class="alt">Patterns:</span>&nbsp; 
          <c:if test="${not empty entry.key.patternsCondition.patterns}">
            ${entry.key.patternsCondition.patterns}
          </c:if>
        </p>
        <p>
          <span class="alt">Method:</span>&nbsp;          
           <c:choose>
		    <c:when test="${not empty entry.key.methodsCondition.methods}">
		        ${entry.key.methodsCondition}	        
		    </c:when>    
		    <c:otherwise>
		        [GET] 
		    </c:otherwise>
			</c:choose> 
        </p>
        <p>
          <span class="alt">Consumes:</span>&nbsp;           
            ${entry.key.consumesCondition}          
        </p>
        <p>
          <span class="alt">Produces:</span>&nbsp;           
            ${entry.key.producesCondition}          
        </p>
        <p>
          <span class="alt">Params:</span>&nbsp;           
            ${entry.key.paramsCondition}          
        </p>
      </div>
     </c:forEach>
</div>
</body>
</html>
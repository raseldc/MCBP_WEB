<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Error</title>
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
</head>
<body>
	<div style="border: #C1C1C1 solid 1px; background-color: lightblue; padding: 50px">
		<!-- Error Page -->
		<tiles:insertAttribute name="error" />
	</div>
</body>
</html>
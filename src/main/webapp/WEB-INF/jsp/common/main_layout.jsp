<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<s:layout-definition>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="css/styles.css" />
<link rel="stylesheet" type="text/css" href="css/redmond/jquery-ui-1.10.3.custom.min.css" />
<script src="js/cookie.js"></script>
<script src="js/jquery-2.0.3.js"></script>
<script src="js/jquery-ui-1.10.3.custom.min.js"></script>
</head>
<body>
	<div class="head">
		<h1>Welcome to SCG</h1>		
	</div>

	<div class="center" style="${style}">
		<s:layout-component name="center"/>
	</div>
	
	<div class="footer">
		${actionBean.longVersion } - Created by oglimmer.de &nbsp;
	</div>
</body>
</html>
</s:layout-definition>
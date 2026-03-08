<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<s:layout-definition>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>SCG — Simple Card Game</title>
<link rel="stylesheet" type="text/css" href="css/styles.css" />
<script src="js/cookie.js"></script>
<script src="js/jquery-2.0.3.js"></script>
</head>
<body>
	<header class="site-header">
		<div class="site-logo">
			<div class="site-logo-icon">&#9824;</div>
			<div class="site-logo-text"><span>SCG</span> Card Game</div>
		</div>
		<nav class="site-nav">
			<a href="Landing.action">Rules</a>
			<a href="CreateGame.action">New Game</a>
		</nav>
	</header>

	<main class="main-content">
		<s:layout-component name="center"/>
	</main>

	<footer class="site-footer">
		${actionBean.longVersion } &mdash; oglimmer.de
	</footer>
</body>
</html>
</s:layout-definition>

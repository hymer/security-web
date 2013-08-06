<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><spring:message code="decline.title" /></title>
		<script type="text/javascript" charset="utf8" src="scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" charset="utf8" src="scripts/common-tools.js"></script>
		<link rel="stylesheet" type="text/css" href="css/main.css" media="all" />
	</head>
	<body>
		<div id="left">
			<div class="innertube">
				<h1 class="head beright"><spring:message code="base.hint" /></h1>
			</div>
		</div>
		<div id="page">
			<div id="main">
				<font color="red"><spring:message code="decline.msg" /></font>
				<a href="#" onclick="top.location='index.jsp';"><spring:message code="base.back2home" /></a>
			</div>
			<div id="foot">
				<div style="text-align: center;">
					© <spring:message code="foot.msg" /><a href="mailto:hymer2011@gmail.com">hymer2011@gmail.com</a>
				</div>
			</div>
		</div>
	</body>
</html>

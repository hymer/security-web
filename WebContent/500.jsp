<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><spring:message code="error500.title" /></title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/common-tools.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css" media="all" />
	</head>
	<body>
		<div id="left">
			<div class="innertube">
				<h1 class="head beright"><spring:message code="error500.title" /></h1>
			</div>
		</div>
		<div id="page">
			<div id="main">
				<font color="red"><spring:message code="error500.msg" /></font>
				<a href="#" onclick="top.location='index.jsp';"><spring:message code="base.back2home" /></a>
			</div>
			<%@ include file="foot.jsp"%>
		</div>
	</body>
</html>

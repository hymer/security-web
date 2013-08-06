<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><spring:message code="password.title" /></title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/lib-base.jsp"></script>
		<style type="text/css" media="screen" >
			#change_password_form label {
				width: 100px;
				display: inline-block;
				text-align: right;
			}
		</style>
	</head>
	<body>
		<%@ include file="left.jsp"%>
		<div id="page">
			<%@ include file="top.jsp"%>
			<div id="main">
				<form id="change_password_form" action="<%=request.getContextPath()%>/change_password.html"  method="post">
					<p>
						<label><spring:message code="password.oldpassword" />：</label>
						<input type="password" name="password" />
					</p>
					<p>
						<label><spring:message code="password.newpassword" />：</label>
						<input type="password" name="password1" />
					</p>
					<p>
						<label><spring:message code="password.newpasswordagain" />：</label>
						<input type="password" name="password2" />
					</p>
					<p>
						<label></label>
						<input type="submit" value='<spring:message code="base.modify" />'/>
					</p>
				</form>
			</div>
			<%@ include file="foot.jsp"%>
		</div>
	</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><spring:message code="information.title" /></title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/lib-base.jsp"></script>
		<style type="text/css" media="all" >
			#information_form label {
				width: 100px;
				display: inline-block;
				text-align: right;
			}
			label.min {
				width: 10px !important;
				display: inline-block;
				text-align: left;
			}
		</style>
	</head>
	<body>
		<%@ include file="left.jsp"%>
		<div id="page">
			<%@ include file="top.jsp"%>
			<div id="main">
				<form id="information_form" action="<%=request.getContextPath()%>/information.html"  method="post">
					<input type="text" style="display: none;" name="id" value="${user.info.id}" />
					<p>
						<label><spring:message code="information.realName" />：</label>
						<input type="text" name="realName" value="${user.info.realName}" />
						<label><spring:message code="information.gender" />：</label>
						<input type="radio" name="gender" value="M" ${user.info.gender eq "M" ? "checked=checked" : ""} id="f_gender1"/>
						<label class="min" for="f_gender1"><spring:message code="information.gender.m" /></label>
						<input type="radio" name="gender" value="F" ${user.info.gender eq "F" ? "checked=checked" : ""} id="f_gender2"/>
						<label class="min" for="f_gender2"><spring:message code="information.gender.f" /></label>
					</p>
					<p>
						<label><spring:message code="information.position" />：</label>
						<input type="text" name="position" value="${user.info.position}" size="32" />
					</p>
					<p>
						<label><spring:message code="information.mobile" />：</label>
						<input type="text" value="${user.info.mobile}" name="mobile" />
						<label><spring:message code="information.tel" />：</label>
						<input type="text" value="${user.info.telephone}" name="telephone" />
					</p>
					<p>
						<label><spring:message code="information.fax" />：</label>
						<input type="text" value="${user.info.fax}" name="fax" />
						<label><spring:message code="information.email" />：</label>
						<input type="text" value="${user.info.email}" name="email" />
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

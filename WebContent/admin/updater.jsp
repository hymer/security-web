<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>系统升级</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/lib-base.jsp"></script>
	</head>
	<body>
		<%@ include file="left.jsp"%>
		<div id="page">
			<%@ include file="../top.jsp"%>
			<div id="main">
				<form id="upload_form" action="<%=request.getContextPath()%>/updater.html" method="post" enctype="multipart/form-data">
					<h3>系统升级</h3>
					<p>
						<label>请选择系统升级包(.zip)：</label>
						<input type="file" name="new_version_package" />
					</p>
					<p>
						<label></label>
						<input type="submit" value="确认升级" />
						<input type="button" onclick="history.go(-1);" value="返回"/>
					</p>
				</form>
			</div>
			<%@ include file="../foot.jsp"%>
		</div>
	</body>
</html>

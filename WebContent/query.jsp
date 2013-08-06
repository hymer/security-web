<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>测试查询</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/lib-base.jsp"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/query.js"></script>
	</head>
	<body>
		<%@ include file="left.jsp"%>
		<div id="page">
			<%@ include file="top.jsp"%>
			<div id="main">
				<script type="text/javascript" charset="utf-8">
					var t = null;
					$(function() {
						initQuery({
							basePath : "<%=request.getContextPath()%>",
							queryId : "personQuery",
							sortables : ["key"],
							checkMode : 'single',
							checkbox : "<th colname='checkbox' style='width:5%;'><input type='checkbox'/></th>",
							ops : "<th colname='ops' style='width:15%;'>操作</th>",
							formatters : {
								ops : function(v, obj) {
									var html = '';
									html += "<a href='javascript:doEdit(" + obj.id + ");'>修改</a>";
									return html;
								}
							}
						});

						$("#searchButton").click(function() {
							t.doSearch();
						});
					});

				</script>
				<div id="personQuerySearch" style="width: 90%;">
					<table width="100%">
						<tr>
							<td>Name：</td><td>
							<input type="text" name="name" />
							</td>
							<td>Email：</td><td>
							<input type="text" name="email" />
							</td>
							<td>
							<input type="button" id="searchButton" value="查询" />
							</td>
						</tr>
					</table>
				</div>
				<div id="tableDiv" style="width: 100%;">
					<table id="personQuery"></table>
				</div>
			</div>
			<%@ include file="foot.jsp"%>
		</div>
	</body>
</html>

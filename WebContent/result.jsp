<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><spring:message code="base.hint" /></title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/common-tools.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css" media="all" />
		<style media="screen" type="text/css">
			table.errorsTable {
				border-left: 1px solid;
				border-top: 1px solid;
				width: 100%;
			}
			table.errorsTable th, table.errorsTable td {
				border-right: 1px solid;
				border-bottom: 1px solid;
				padding: 3px;
			}

		</style>
	</head>
	<body>
		<div id="left">
			<div class="innertube">
				<h1 class="head beright"><spring:message code="base.message" /></h1>
			</div>
		</div>
		<div id="page">
			<div id="main">
				<br />
				<font size="2" color='${result ? "green" : "red"}'>${msg}</font>
				<c:choose>
					<c:when test='${fn:startsWith(url, "javascript:")}'>
						<a href="${url}">${url_display}</a>
					</c:when>
					<c:when test='${fn:startsWith(url, "http")}'>
						<a href="${url}">${url_display}</a>
					</c:when>
					<c:otherwise>
						<a href="<%=request.getContextPath()%>/${url}">${url_display}</a>
					</c:otherwise>
				</c:choose>
				<c:if test="${auto_redirect ne false}">
					( <span id="secondSpan" style="color: green;">3</span> <spring:message code="result.autoredirect" />)
					<script type="text/javascript">
						var secs = 3;
						//倒计时的秒数
						var URL;
						function Load(url) {
							URL = url;
							for(var i = secs; i >= 0; i--) {
								window.setTimeout('doUpdate(' + i + ')', (secs - i) * 1000);
							}
						}

						function doUpdate(num) {
							document.getElementById('secondSpan').innerHTML = num;
							if(num == 0) {
								window.location = URL;
							}
						}

						if("${url}".startsWith("javascript:")) {
							Load("${url}");
						} else if ("${url}".startsWith("http")) {
							Load("${url}");
						} else {
							Load("<%=request.getContextPath()%>/${url}");
						}
					</script>
				</c:if>
				<c:if test="${errors ne NULL}">
					<br/>
					<br/>
					<br/>
					<h3 style="margin-left:120px;"><spring:message code="result.errortable.title" /></h3>
					<table class="errorsTable">
						<tr>
							<th><spring:message code="result.errortable.column" /></th>
							<th><spring:message code="result.errortable.content" /></th>
							<th><spring:message code="result.errortable.reason" /></th>
						</tr>
						<c:forEach var="e" items="${errors}">
							<tr>
								<td>${e.field}</td>
								<td>${e.rejectedValue}</td>
								<td>${e.defaultMessage}</td>
							</tr>
						</c:forEach>
					</table>
				</c:if>
			</div>
			<%@ include file="foot.jsp"%>
		</div>
	</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>文件上传</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/lib-base.jsp"></script>
		<script type="text/javascript">
			function addAttachment() {
				var div = $("#attachmentDiv");
				var inputs = $("input[type=file]", div);
				var id = "attach" + (inputs.length + 1);
				div.append("<div id='" + id + "'><input type='file' name='" + id + "' /><a href='javascript:delAttachment(\"" + id + "\");'>删除</a></div>");
			}

			function delAttachment(divId) {
				$("#" + divId).remove();
			}
			
			function doSubmit() {
				ajaxSubmitForm("upload_form", function(resp) {
					showMsg(resp);
					setInterval("window.location.reload();", 3000);
				});
			}
		</script>
	</head>
	<body>
		<%@ include file="left.jsp"%>
		<div id="page">
			<%@ include file="top.jsp"%>
			<div id="main">
				<form id="upload_form" action="<%=request.getContextPath()%>/upload.ajax" method="post" enctype="multipart/form-data">
					<h3>文件上传</h3>
					<p>
						<label>文件：</label>
						<a href="javascript:addAttachment();">添加</a>
						<br />
						<div id="attachmentDiv"></div>
					</p>
					<p>
						<label></label>
						<input type="button" onclick="doSubmit();" value="上传" />
						<input type="button" onclick="history.go(-1);" value="返回"/>
					</p>
				</form>
			</div>
			<%@ include file="foot.jsp"%>
		</div>
	</body>
</html>

<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><spring:message code="login.title" /></title>
		<script type="text/javascript" charset="utf8" src="scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" charset="utf8" src="scripts/common-tools.js"></script>
		<script type="text/javascript" charset="utf8" src="scripts/jquery.validate.min.js"></script>
		<link rel="stylesheet" type="text/css" href="css/main.css" media="all" />
		<style type="text/css" media="all">
			label {
				display: inline-block;
				width: 60px;
			}
		</style>
		<script type="text/javascript">
			$(function() {
				$("#f_referer").val(document.referrer);
				$("#loginForm").validate({
					rules : {
						j_username : {
							required : true
						},
						j_password : {
							required : true
						}
					},
					messages : {
						j_username : {
							required : '<spring:message code="login.error.blank.userName" />'
						},
						j_password : {
							required : '<spring:message code="login.error.blank.password" />'
						}
					}
				});
			});

		</script>
	</head>
	<body onload="document.loginForm.j_username.focus();">
		<div id="left">
			<div class="innertube">
				<h1 class="head beright"><spring:message code="login.title" /></h1>
			</div>
		</div>
		<div id="page">
			<div id="main">
				<form id="loginForm" name="loginForm" action="j_security_check.html" method="POST">
					<input type="text" style="display: none;" id="f_referer" name="referer" value="/" />
					<legend>
						<h3><spring:message code="login.title" /></h3>
					</legend>
					<p>
						<label><spring:message code="login.userName" />:</label>
						<input type='text' name='j_username'/>
					</p>
					<p>
						<label><spring:message code="login.password" />:</label>
						<input type='password' name='j_password'/>
					</p>
					<p id="verify_p"></p>
					<!--
					<p>
					<input type="checkbox" name="remember_me">
					<label>两周内免登录</label>
					</p>
					-->
					<p>
						<input name="submit" type="submit" value='<spring:message code="login.button" />'/>
						<input name="reset" type="reset" value='<spring:message code="base.reset" />'/>
						<label>&nbsp;</label><a href="register.jsp"><spring:message code="base.register" /></a>
					</p>
				</form>
				<script type="text/javascript">
					var verify = getParam("verify");
					if(verify == 'true') {
						$("#verify_p").html('<label><spring:message code="form.verifyCode" />：</label><img id="verifyImage" src="verify.jpg"/><input type="text" name="verifyCode" size=6/><spring:message code="form.verifyCode.click" /><a href="javascript:refreshVerifyCode();"><spring:message code="base.refresh" /></a>');
					}

					$("#verifyImage").click(function() {
						refreshVerifyCode();
					});
					function refreshVerifyCode() {
						$("#verifyImage").attr('src', 'verify.jpg?' + Math.floor(Math.random() * 100));
					}
				</script>
			</div>
			<div id="foot">
				<div style="text-align: center;">
					© <spring:message code="foot.msg" /><a href="mailto:hymer2011@gmail.com">hymer2011@gmail.com</a>
				</div>
			</div>
		</div>
	</body>
</html>

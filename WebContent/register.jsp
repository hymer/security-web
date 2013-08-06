<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><spring:message code="base.register" /></title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/common-tools.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.validate.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/messages_cn.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css" media="all" />
		<script type="text/javascript">
			$(function() {
				$("#registerForm").validate({
					rules : {
						userName : {
							required : true,
							minlength : 5,
							maxlength : 18
						},
						password1 : {
							required : true,
							minlength : 6,
							maxlength : 18
						},
						password2 : {
							required : true,
							equalTo : '#f_password1'
						},
						email : {
							required : true,
							email : true
						},
						verifyCode : {
							required : true
						}
					},
					messages : {
					}
				});
			});

		</script>
		<style media="screen" type="text/css">
			label {
				width: 90px;
				display: inline-block;
				text-align: right;
			}
			label.min {
				width: 25px;
				display: inline-block;
				text-align: left;
			}
			label.max {
				width: 380px;
				display: inline-block;
				text-align: left;
			}
		</style>
	</head>
	<body>
		<div id="left">
			<div class="innertube">
				<h1 class="head"><spring:message code="base.register" /></h1>
			</div>
		</div>
		<div id="page">
			<div id="main">
				<form id="registerForm" action="register.html" method="post">
					<p>
						<label for="f_userName">用户名<font color="red">*</font>：</label>
						<input id="f_userName" name="userName" />
						<label class="max">5~18位字母和数字</label>
					</p>
					<p>
						<label for="f_password1">密码<font color="red">*</font>：</label>
						<input type="password" id="f_password1" name="password1" />
						<label class="max">密码由6-18个字符组成，不要使用纯字母或纯数字</label>
					</p>
					<p>
						<label for="f_password2">重复密码<font color="red">*</font>：</label>
						<input type="password" id="f_password2" name="password2" />
						<label class="max">请重复上面的密码</label>
					</p>
					<p>
						<label for="f_email">E-mail<font color="red">*</font>：</label>
						<input id="f_email" name="email" />
						<label class="max">请输入您的常用邮箱</label>
					</p>
					<p>
						<label for="f_realName">真实姓名：</label>
						<input id="f_realName" name="realName" />
						<input type="radio" name="gender" value="M" checked="checked" id="f_gender1"/>
						<label class="min" for="f_gender1">先生</label>
						<input type="radio" name="gender" value="F" id="f_gender2"/>
						<label class="min" for="f_gender2">女士</label>
						<label class="max">请正确填写您的姓名(不带空格)</label>
					</p>
					<p>
						<label for="f_position">职位：</label>
						<input id="f_position" name="position" />
					</p>
					<p>
						<label for="f_telephone">固定电话：</label>
						<input id="f_telephone" name="telephone" />
					</p>
					<p>
						<label for="f_fax">传真：</label>
						<input id="f_fax" name="fax" />
					</p>
					<p>
						<label for="f_mobile">手机：</label>
						<input id="f_mobile" name="mobile" />
					</p>
					<p>
						<label>验证码：</label>
						<img id="verifyImage" src="verify.jpg"/>
						<input type="text" name="verifyCode" size=6/>
						看不清？点击图片<a href="javascript:refreshVerifyCode();">刷新</a>
						<script type="text/javascript">
							$("#verifyImage").click(function() {
								refreshVerifyCode();
							});
							function refreshVerifyCode() {
								$("#verifyImage").attr('src', 'verify.jpg?' + Math.floor(Math.random() * 100));
							}
						</script>
					</p>
					<p>
						<label></label>
						<input type="submit" value="提交注册信息" />
						<input type="reset" value="重置" />
						<span style="padding-left:50px;">已有账号，<a href="login.jsp">点此登录</a></span>
					</p>
				</form>
			</div>
			<%@ include file="foot.jsp"%>
		</div>
	</body>
</html>

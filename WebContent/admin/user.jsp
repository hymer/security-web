<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户管理</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/lib-base.jsp"></script>
	</head>
	<body>
		<%@ include file="left.jsp"%>
		<div id="page">
			<%@ include file="../top.jsp"%>
			<div id="main">
				<script type="text/javascript" charset="utf-8">
					var t = null;
					$(function() {
						$("#user_add_form").validate({
							rules : {
								userName : {
									required : true,
									minlength : 5,
									maxlength : 16
								},
								password1 : {
									required : true,
									minlength : 6,
									maxlength : 18
								},
								password2 : {
									required : true,
									equalTo : '#f_password1',
									minlength : 6,
									maxlength : 18
								}
							},
							messages : {
							}
						});
						t = new SimpleTable("userQueryTable", {
							pageSize : 10,
							param : 'searchDiv',
							url : '<%=request.getContextPath()%>/admin/user_query.ajax',
							sortables : ["name"],
							pagingOptions : {
								first : true,
								end : true,
								go : true,
								firstHtml : '<a href="#">首页</a>',
								lastHtml : '<a href="#">上一页</a>',
								nextHtml : '<a href="#">下一页</a>',
								endHtml : '<a href="#">尾页</a>'
							},
							//checkMode : 'single',
							// scrollX : true,
							columns : {
								userName : {
									header : '用户名称',
									width : '25%'
								},
								roleName : {
									header : '用户角色',
									width : '35%'
								},
								createTime : {
									header : '创建时间',
									width : '12%'
								},
								disabled : {
									header : '是否禁用',
									width : '10%'
								},
								ops : {
									header : '操作',
									width : '13%'
								}
							},
							formatters : {
								disabled : function(v, obj) {
									if(v) {
										return '<font color="red">已禁用</font>';
									} else {
										return '<font color="green">未禁用</font>';
									}
								},
								ops : function(v, obj) {
									var html = '';
									html += "<a href='javascript:doEdit(" + obj.id + ");'>修改</a>";
									html += '&nbsp;&nbsp;<a href="javascript:changePassword(' + obj.id + ');">密码重置</a>';
									return html;
								}
							},
							info : {
								//pageSizeSelect:'', //不显示此控件
								pageSizeSelect : '显示{0}条记录',
								pagingInfo : '目前显示{0}-{1}条记录，共计：{2}'
							}
						});

						$('#user_add_form_div').dialog({
							draggable : true,
							title : '添加用户',
							width : 620,
							height : "auto",
							modal : true,
							// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
							resizable : false,
							draggable : true,
							autoOpen : false,
							// show:'slide',
							buttons : {
								"OK" : function() {
									ajaxSubmitForm("user_add_form", editCallback);
								},
								"Cancel" : function() {
									$(this).dialog("close");
								}
							}
						});
						$('#user_edit_form_div').dialog({
							draggable : true,
							title : '修改用户',
							width : 620,
							height : "auto",
							modal : true,
							resizable : false,
							draggable : true,
							autoOpen : false,
							buttons : {
								"OK" : function() {
									ajaxSubmitForm("user_edit_form", editCallback);
								},
								"Cancel" : function() {
									$(this).dialog("close");
								}
							}
						});
						$('#change_password_form_div').dialog({
							draggable : true,
							title : '重置密码',
							width : 360,
							height : "auto",
							modal : true,
							resizable : false,
							draggable : true,
							autoOpen : false,
							buttons : {
								"OK" : function() {
									ajaxSubmitForm("change_password_form");
								},
								"Cancel" : function() {
									$(this).dialog("close");
								}
							}
						});
						$("#searchButton").click(function() {
							t.doSearch();
						});
					});
					function editCallback() {
						t.doSearch();
					}

					function doEdit(id) {
						doGetAjax("<%=request.getContextPath()%>/admin/user/" + id + ".html", '', function(data, textStatus, XMLHttpRequest) {
							document.forms['user_edit_form'].reset();
							fillForm("user_edit_form", data);
							$("#user_edit_form_div").dialog("open");
						});
					}

					function changePassword(id) {
						document.forms['change_password_form'].reset();
						fillForm("change_password_form", {
							id : id
						});
						$("#change_password_form_div").dialog("open");
					}

					function showAddForm() {
						document.forms['user_add_form'].reset();
						$('#user_add_form_div').dialog("open");
					}
				</script>
				<div id="searchDiv" style="width: 90%;">
					<table width="100%">
						<tr>
							<td width="20%">用户名称：</td><td width="30%">
							<input name="userName" />
							</td>
							<td width="20%">用户角色：</td><td width="30%">
							<select name="roleId" class="ajaxSelect" autoLoad="false"
							ajaxUrl="<%=request.getContextPath()%>/admin/user/role_list.ajax" dataRoot="roles"
							valueField="id" displayField="name">
								<option value="" selected="selected">所有</option>
							</select></td>
							<td>
							<input type="button" id="searchButton" value="查询" />
							</td>
						</tr>
					</table>
				</div>
				<div>
					<a href="javascript:showAddForm();">添加新用户</a>
					<br />
					<br/>
				</div>
				<div id="tableDiv" style="width: 100%;">
					<table id="userQueryTable"></table>
				</div>
			</div>
			<div id="user_add_form_div" style="display: none;">
				<form id="user_add_form" action="<%=request.getContextPath()%>/admin/user_edit.ajax"  method="post">
					<input type="text" style="display: none;" name="id" />
					<table>
						<tr>
							<td>用户名称：</td><td>
							<input type="text" name="userName" />
							</td>
							<td>角色：</td><td><select name="roleId" class="ajaxSelect" autoLoad="true"
							ajaxUrl="<%=request.getContextPath()%>/admin/user/role_list.ajax" dataRoot="roles"
							valueField="id" displayField="name"></select></td>
						</tr>
						<tr>
							<td>密码：</td><td>
							<input type="password" id="f_password1" name="password1" />
							</td>
							<td>密码确认：</td><td>
							<input type="password" name="password2" />
							</td>
						</tr>
						<tr>
							<td>是否禁用：</td><td colspan="3">
							<input id="disabled_true" type="radio" name="disabled" value="true" />
							<label for="disabled_true">禁用</label>
							<input id="disabled_false" type="radio" name="disabled" checked="checked" value="false" />
							<label for="disabled_false">不禁用</label></td>
						</tr>
					</table>
				</form>
			</div>
			<div id="user_edit_form_div" style="display: none;">
				<form id="user_edit_form" action="<%=request.getContextPath()%>/admin/user_edit.ajax"  method="post">
					<input type="text" style="display: none;" name="id" />
					<table>
						<tr>
							<td>用户名称：</td><td>
							<input type="text" name="userName" readonly="readonly" />
							</td>
							<td>角色：</td><td><select name="roleId" class="ajaxSelect" autoLoad="true"
							ajaxUrl="<%=request.getContextPath()%>/admin/user/role_list.ajax" dataRoot="roles"
							valueField="id" displayField="name"></select></td>
						</tr>
						<tr>
							<td>是否禁用：</td><td colspan="3">
							<input id="disabled_true_edit" type="radio" name="disabled" value="true" />
							<label for="disabled_true_edit">禁用</label>
							<input id="disabled_false_edit" type="radio" name="disabled" checked="checked" value="false" />
							<label for="disabled_false_edit">不禁用</label></td>
						</tr>
					</table>
				</form>
			</div>
			<div id="change_password_form_div" style="display: none;">
				<form id="change_password_form" action="<%=request.getContextPath()%>/admin/user/password_reset.ajax"  method="post">
					<input type="text" style="display: none;" name="id" />
					<table>
						<tr>
							<td>新密码：</td><td>
							<input type="password" name="password1" />
							</td>
						</tr>
						<tr>
							<td>密码确认：</td><td>
							<input type="password" name="password2" />
							</td>
						</tr>
					</table>
			</div>
			<%@ include file="../foot.jsp"%>
		</div>
	</body>
</html>

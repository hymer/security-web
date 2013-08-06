<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统配置</title>
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
						t = new SimpleTable("configQueryTable", {
							paging : false,
							param : 'searchDiv',
							url : '<%=request.getContextPath()%>/core/config/query.html',
							sortables : ["key"],
							// checkMode : 'single',
							// scrollX : true,
							columns : {
								key : {
									header : '配置项',
									width : '30%'
								},
								value : {
									header : '值',
									width : '37%'
								},
								remarks : {
									header : '备注',
									width : '20%'
								},
								disabled : {
									header : '是否禁用',
									width : '7%'
								},
								ops : {
									header : '操作',
									width : '6%'
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
									//html += "&nbsp;&nbsp;<a href='javascript:doDel(" + obj.id + ");'>删除</a>";
									return html;
								}
							}
						});

						$('#config_add_form_div').dialog({
							draggable : true,
							title : '添加/修改',
							width : 430,
							modal : true,
							// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
							resizable : false,
							autoOpen : false,
							// show:'slide',
							buttons : {
								"OK" : function() {
									ajaxSubmitForm("config_add_form", editCallback);
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
					function showAddForm() {
						document.forms['config_add_form'].reset();
						var dialog = $('#config_add_form_div').dialog("open");
					}
						
					function editCallback() {
						t.doSearch();
					}

					function doEdit(id) {
						doGetAjax("<%=request.getContextPath()%>/core/config/" + id + ".html", '', function(data, textStatus, XMLHttpRequest) {
							fillForm("config_add_form", data);
							$("#config_add_form_div").dialog("open");
						});
					}

					function doDel(id) {
						showConfirm("确认要删除选中的配置项吗？", function() {
							doPostAjax("<%=request.getContextPath()%>/core/config/delete.html", "id=" + id, function(resp) {
								showMsg(resp);
								t.doSearch();
							});
						});
					}
				</script>
				<div id="searchDiv" style="width: 90%;">
					<table width="100%">
						<tr>
							<td>配置项：</td><td>
							<input type="text" name="key" />
							</td>
							<td>值：</td><td>
							<input type="text" name="value" />
							</td>
							<td>备注：</td><td>
							<input type="text" name="remarks" />
							</td>
							<td>
								<input type="button" id="searchButton" value="查询" />
								<input type="button" onclick="showAddForm();" value="添加" />
							</td>
						</tr>
					</table>
				</div>
				<div id="tableDiv" style="width: 100%;">
					<table id="configQueryTable"></table>
				</div>
			</div>
			<div id="config_add_form_div" style="display: none;">
				<form id="config_add_form" action="<%=request.getContextPath()%>/core/config/edit.html"  method="post">
					<input type="text" style="display: none;" name="id" />
					<table>
						<tr>
							<td>配置项：</td><td>
							<input type="text" name="key" size="34" />
							</td>
						</tr>
						<tr>
							<td>值：</td><td>
							<input type="text" name="value" size="34" />
							</td>
						</tr>
						<tr>
							<td>备注：</td><td>
							<input type="text" name="remarks" size="34" />
							</td>
						</tr>
						<tr>
							<td>是否禁用：</td><td>
							<select name="disabled">
								<option value="false">未禁用</option>
								<option value="true">已禁用</option>
							</select></td>
						</tr>
					</table>
				</form>
			</div>
			<%@ include file="../foot.jsp"%>
		</div>
	</body>
</html>

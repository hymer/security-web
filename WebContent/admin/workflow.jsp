<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>流程管理</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/lib-base.jsp"></script>
	</head>
	<body>
		<%@ include file="left.jsp"%>
		<div id="page">
			<%@ include file="../top.jsp"%>
			<div id="main">
				<script type="text/javascript" charset="utf-8">
					var t = null;
					var nodeTable = null;
					var userTable = null;
					var currentWorkflowId = null;
					$(function() {
						t = new SimpleTable("workflowQueryTable", {
							pageSize : 10,
							param : 'searchDiv',
							url : '<%=request.getContextPath()%>/workflow/query.html',
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
								name : {
									header : '流程名称',
									width : '44%'
								},
								code : {
									header : '流程代码',
									width : '30%'
								},
								ops : {
									header : '操作',
									width : '28%'
								}
							},
							formatters : {
								ops : function(v, obj) {
									var html = '';
									html += "<a href='javascript:doEdit(" + obj.id + ");'>修改</a>";
									html += "&nbsp;&nbsp;<a href='javascript:managerNode(" + obj.id + ");'>管理节点</a>";
									html += "&nbsp;&nbsp;<a href='javascript:doDelete(" + obj.id + ");'>删除</a>";
									return html;
								}
							},
							info : {
								//pageSizeSelect:'', //不显示此控件
								pageSizeSelect : '显示{0}条记录',
								pagingInfo : '目前显示{0}-{1}条记录，共计：{2}'
							}
						});
						nodeTable = new SimpleTable("nodeQueryTable", {
							pageSize : 8,
							autoLoad : false,
							param : 'searchNodeDiv',
							url : '<%=request.getContextPath()%>/workflow/node/query.html',
							//sortables : ["name"],
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
								rownum : {
									header : '序号',
									width : '5%'
								},
								name : {
									header : '节点名称',
									width : '18%'
								},
								yNodeName : {
									header : 'Y节点',
									width : '18%'
								},
								nNodeName : {
									header : 'N节点',
									width : '18%'
								},
								startPoint : {
									header : "起点",
									width : '6%'
								},
								endPoint : {
									header : "终点",
									width : '6%'
								},
								userName : {
									header : "所属用户",
									width : '15%'
								},
								ops : {
									header : '操作',
									width : '12%'
								}
							},
							formatters : {
								startPoint : function(v, obj) {
									return v ? '是' : "否";
								},
								endPoint : function(v, obj) {
									return v ? '是' : "否";
								},
								ops : function(v, obj) {
									var html = '';
									html += "<a href='javascript:doEditNode(" + obj.id + ");'>修改</a>";
									html += "&nbsp;&nbsp;<a href='javascript:doDeleteNode(" + obj.id + ");'>删除</a>";
									return html;
								}
							},
							info : {
								//pageSizeSelect:'', //不显示此控件
								pageSizeSelect : '显示{0}条记录',
								pagingInfo : '目前显示{0}-{1}条记录，共计：{2}'
							}
						});
						userTable = new SimpleTable("userQueryTable", {
							pageSize : 5,
							autoLoad : false,
							param : 'searchUserDiv',
							url : '<%=request.getContextPath()%>/admin/user_query.ajax',
							pagingOptions : {
								first : true,
								end : true,
								go : true,
								firstHtml : '<a href="#">首页</a>',
								lastHtml : '<a href="#">上一页</a>',
								nextHtml : '<a href="#">下一页</a>',
								endHtml : '<a href="#">尾页</a>'
							},
							checkMode : 'single',
							// scrollX : true,
							columns : {
								checkbox : {
									width : '5%'
								},
								userName : {
									header : '用户名称',
									width : '40%'
								},
								roleName : {
									header : '用户角色',
									width : '40%'
								},
								disabled : {
									header : '是否禁用',
									width : '15%'
								}
							},
							formatters : {
								disabled : function(v, obj) {
									if(v) {
										return '<font color="red">已禁用</font>';
									} else {
										return '<font color="green">未禁用</font>';
									}
								}
							},
							info : {
								//pageSizeSelect:'', //不显示此控件
								pageSizeSelect : '显示{0}条记录',
								pagingInfo : '目前显示{0}-{1}条记录，共计：{2}'
							}
						});

						$('#selectUserDiv').dialog({
							draggable : true,
							title : '选择用户',
							width : 690,
							modal : true,
							// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
							resizable : false,
							autoOpen : false,
							// show:'slide',
							buttons : {
								"OK" : function() {
									var selectedObj = userTable.getSelectedObj();
									if(selectedObj.length <= 0) {
										$("#selectedUserId").val("");
										$("#selectedUserName").val("");
									} else {
										var selectedUserId = selectedObj[0].id;
										var selectedUserName = selectedObj[0].userName;
										$("#selectedUserId").val(selectedUserId);
										$("#selectedUserName").val(selectedUserName);
									}
									$(this).dialog("close");
								},
								"Cancel" : function() {
									$(this).dialog("close");
								}
							}
						});

						$('#workflow_add_form_div').dialog({
							draggable : true,
							title : '添加/修改流程',
							width : 620,
							modal : true,
							// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
							resizable : false,
							autoOpen : false,
							// show:'slide',
							buttons : {
								"OK" : function() {
									ajaxSubmitForm("workflow_add_form", editCallback);
								},
								"Cancel" : function() {
									$(this).dialog("close");
								}
							}
						});
						$('#node_manager_div').dialog({
							draggable : true,
							title : '节点管理',
							width : 800,
							modal : true,
							// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
							resizable : false,
							autoOpen : false,
							// show:'slide',
							buttons : {
								"完成" : function() {
									$(this).dialog("close");
								}
							}
						});
						$('#node_add_form_div').dialog({
							draggable : true,
							title : '添加/编辑节点',
							width : 620,
							modal : true,
							// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
							resizable : false,
							autoOpen : false,
							// show:'slide',
							buttons : {
								"OK" : function() {
									ajaxSubmitForm("node_add_form", function() {
										nodeTable.doSearch();
									});
								},
								"Cancel" : function() {
									$(this).dialog("close");
								}
							}
						});
						$("#searchButton").click(function() {
							t.doSearch();
						});
						$("#searchUserButton").click(function() {
							userTable.doSearch();
						});
						$("#selectUserBtn").click(function() {
							$("#selectUserDiv").dialog("open");
						});
						$("#clearUserBtn").click(function() {
							$("#selectedUserId").val("");
							$("#selectedUserName").val("");
						});
						$("#searchNoteButton").click(function() {
							nodeTable.doSearch();
						});
					});
					
					function showAddForm() {
						document.forms['workflow_add_form'].reset();
						var dialog = $('#workflow_add_form_div').dialog("open");
					}

					function showAddNodeForm() {
						document.forms['node_add_form'].reset();
						$("#node_add_form input[name='workflowId']").val(currentWorkflowId);
						var dialog = $('#node_add_form_div').dialog("open");
					}

					function editCallback() {
						t.doSearch();
					}

					function doEdit(id) {
						doGetAjax("<%=request.getContextPath()%>/workflow/workflow/" + id + ".html", '', function(data, textStatus, XMLHttpRequest) {
							$("#workflow_add_form")[0].reset();
							fillForm("workflow_add_form", data);
							$("#workflow_add_form_div").dialog("open");
						});
					}

					function doDelete(id) {
						showConfirm("确认要删除该流程吗？", function() {
							doPostAjax("<%=request.getContextPath()%>/workflow/workflow/delete.html", "id=" + id, function(resp) {
								showMsg(resp);
								t.doSearch();
							});
						});
					}

					function doEditNode(id) {
						doGetAjax("<%=request.getContextPath()%>/workflow/node/" + id + ".html", '', function(data, textStatus, XMLHttpRequest) {
							$("#node_add_form")[0].reset();
							fillForm("node_add_form", data);
							$("#node_add_form_div").dialog("open");
						});
					}

					function doDeleteNode(id) {
						showConfirm("确认要删除该节点吗？", function() {
							doPostAjax("<%=request.getContextPath()%>/workflow/node/delete.html", "id=" + id, function(resp) {
								showMsg(resp);
								nodeTable.doSearch();
							});
						});
					}

					function managerNode(id) {
						currentWorkflowId = id;
						$("#f_workflowId").val(id);
						//yNode和nNode下拉框的param
						$("#yNodeSelector").attr('param', 'workflowId=' + id);
						$("#nNodeSelector").attr('param', 'workflowId=' + id);
						nodeTable.doSearch();
						$("#node_manager_div").dialog("open");
						loadSelector($("#yNodeSelector"));
						loadSelector($("#nNodeSelector"));
					}

					function resetNodeAddForm() {
						var form = $("#node_add_form");
						form[0].reset();
						$("#selectedUserId", form).val("");
						$("#selectedUserName", form).val("");
					}
				</script>
				<div id="searchDiv" style="width: 90%;">
					<table width="100%">
						<tr>
							<td width="20%">流程名称：</td><td width="30%">
							<input name="name" />
							</td>
							<td width="20%">流程代码：</td><td width="30%">
							<input name="code" />
							</td>
							<td>
							<input type="button" id="searchButton" value="查询" />
							</td>
						</tr>
					</table>
				</div>
				<div style="text-align:right;"></div>
				<div>
					<a href="javascript:showAddForm();">添加新流程</a>
					<br />
					<br/>
				</div>
				<div id="tableDiv" style="width: 100%;">
					<table id="workflowQueryTable"></table>
				</div>
				<div id="workflow_add_form_div" style="display: none;">
					<form id="workflow_add_form" action="<%=request.getContextPath()%>/workflow/edit.html"  method="post">
						<input type="text" style="display: none;" name="id" />
						<table>
							<tr>
								<td>流程名称：</td><td>
								<input type="text" name="name" />
								</td>
								<td>流程代码：</td><td>
								<input type="text" name="code" />
								</td>
							</tr>
						</table>
					</form>
				</div>
				<div id="node_manager_div" style="display: none;">
					<div id="searchNodeDiv">
						<input type="hidden" id="f_workflowId" name="workflowId" />
						节点名称：
						<input type="text" name="name"/>
						<input type="button" id="searchNoteButton" value="查询" />
						<input type="button" onclick="showAddNodeForm()" value="添加节点" />
					</div>
					<div id="nodeTableDiv" style="width: 100%;">
						<table id="nodeQueryTable"></table>
					</div>
				</div>
				<div id="node_add_form_div" style="display: none;">
					<form id="node_add_form" action="<%=request.getContextPath()%>/workflow/node/edit.html"  method="post">
						<input type="text" style="display: none;" name="id" />
						<input type="text" style="display:none;" name="workflowId" />
						<table>
							<tr>
								<td>节点名称：</td><td>
								<input type="text" name="name" size="10" />
								</td>
								<td> 所属用户： </td>
								<td>
								<input type="text" name="userName" id="selectedUserName" readonly="readonly" size="10" />
								<input type="hidden" name="userId" id="selectedUserId" />
								<input type="button" value="选择" id="selectUserBtn"/>
								<input type="button" value="清除" id="clearUserBtn"/>
								</td>
							</tr>
							<tr>
								<td>Y节点：</td>
								<td>
								<select id="yNodeSelector" name="yNodeId" class="ajaxSelect" autoLoad="false"
								ajaxUrl="<%=request.getContextPath()%>/workflow/node/list.html" dataRoot="nodes"
								valueField="id" displayField="name">
									<option value="">无</option>
								</select></td>
								<td>N节点：</td>
								<td>
								<select id="nNodeSelector" name="nNodeId" class="ajaxSelect" autoLoad="false"
								ajaxUrl="<%=request.getContextPath()%>/workflow/node/list.html" dataRoot="nodes"
								valueField="id" displayField="name">
									<option value="">无</option>
								</select></td>
							</tr>
							<tr>
								<td>是否起始节点：</td><td>
								<input id="startPointY" type="radio" value="true" name="startPoint" />
								<label for="startPointY">是</label>
								<input id="startPointN" checked="checked" value="false" type="radio" name="startPoint" />
								<label for="startPointN">否</label></td>
								<td>是否终止节点：</td><td>
								<input id="endPointY" type="radio" value="true" name="endPoint" />
								<label for="endPointY">是</label>
								<input id="endPointN" checked="checked" value="false" type="radio" name="endPoint" />
								<label for="endPointN">否</label></td>
							</tr>
						</table>
					</form>
				</div>
				<div id="selectUserDiv">
					<div id="searchUserDiv" style="width: 90%;">
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
								<input type="button" id="searchUserButton" value="查询" />
								</td>
							</tr>
						</table>
					</div>
					<div id="userTableDiv" style="width: 100%;">
						<table id="userQueryTable"></table>
					</div>
				</div>
			</div>
			<%@ include file="../foot.jsp"%>
		</div>
	</body>
</html>

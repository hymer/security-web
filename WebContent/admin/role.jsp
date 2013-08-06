<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>角色管理</title>
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
						t = new SimpleTable("roleQueryTable", {
							pageSize : 10,
							param : 'searchDiv',
							url : '<%=request.getContextPath()%>/admin/role_query.html',
							sortables : ["name"],
							checkMode : 'single',
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
								checkbox : {
									width : '3%'
								},
								name : {
									header : '角色名称',
									width : '15%'
								},
								code : {
									header : '角色代码',
									width : '28%'
								},
								description : {
									header : '角色描述',
									width : '38%'
								},
								disabled : {
									header : '是否禁用',
									width : '10%'
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
									return html;
								}
							},
							info : {
								//pageSizeSelect:'', //不显示此控件
								pageSizeSelect : '显示{0}条记录',
								pagingInfo : '目前显示{0}-{1}条记录，共计：{2}'
							}
						});

						$('#role_add_form_div').dialog({
							draggable : true,
							title : '添加/修改角色',
							width : 620,
							modal : true,
							// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
							resizable : false,
							draggable : true,
							autoOpen : false,
							// show:'slide',
							buttons : {
								"OK" : function() {
									ajaxSubmitForm("role_add_form", editCallback);
								},
								"Cancel" : function() {
									$("#role_add_form input[name='code']").attr("disabled", false);
									$(this).dialog("close");
								}
							}
						});
						$("#searchButton").click(function() {
							t.doSearch();
						});
					});
					function editCallback() {
						$("#role_add_form input[name='code']").attr("disabled", false);
						t.doSearch();
					}

					function doEdit(id) {
						doGetAjax("<%=request.getContextPath()%>/admin/role/" + id + ".html", '', function(data, textStatus, XMLHttpRequest) {
							fillForm("role_add_form", data);
							$("#role_add_form input[name='code']").attr("disabled", true);
							$("#role_add_form_div").dialog("open");
						});
					}
				</script>
				<div id="searchDiv" style="width: 90%;">
					<table width="100%">
						<tr>
							<td width="20%">角色名称：</td><td width="30%">
							<input name="name" />
							</td>
							<td width="20%">角色代码：</td><td width="30%">
							<input name="code" />
							</td>
							<td>
								<input type="button" id="searchButton" value="查询" />
							</td>
						</tr>
					</table>
				</div>
				<div>
					<script type="text/javascript">
						function showAddForm() {
							document.forms['role_add_form'].reset();
							var dialog = $('#role_add_form_div').dialog("open");
						}

						function assignAuthorities() {
							var ids = t.getSelected();
							if(ids.length == 0 || ids.length > 1) {
								showMsg("<font colo='red'>请选择一条记录!</font>");
							} else {
								var selector = new MultiSelector("authority_multiselector", {
									url : "<%=request.getContextPath()%>/admin/role/assign/" + ids[0] + ".html",
									allData : 'available',
									selectedData : 'selected',
									valueFiled : "id",
									displayField : "name",
									selectorHeight : '240px',
									autoLoad : true
								});
								$("#authority_multiselector").dialog({
									title : '为角色分配权限',
									modal : true,
									width : 390,
									height : 360,
									close : function() {
										$(this).empty();
									},
									buttons : {
										"确认" : function() {
											$(this).dialog("close");
											var authorities = selector.val();
											confirmAssignAuthority(ids[0], authorities);
										},
										"取消" : function() {
											$(this).dialog("close");
										}
									}
								});
							}
						}

						function confirmAssignAuthority(id, authorities) {
							doPostAjax("<%=request.getContextPath()%>/admin/assignAuthority.html", 'id=' + id + "&authorities=" + authorities, function(data, textStatus, XMLHttpRequest) {
								showMsg(data);
							});
						}
					</script>
					</script> <a href="javascript:showAddForm();">添加新角色</a>
					&nbsp;&nbsp; <a href="javascript:assignAuthorities();">分配权限</a>
					&nbsp;&nbsp; <a href="javascript:assignMenus();">分配菜单</a>
					<br />
					<br/>
				</div>
				<div id="tableDiv" style="width: 100%;">
					<table id="roleQueryTable"></table>
				</div>
			</div>
			<div id="role_add_form_div" style="display: none;">
				<form id="role_add_form" action="<%=request.getContextPath()%>/admin/role_edit.html"  method="post">
					<input type="text" style="display: none;" name="id" />
					<table>
						<tr>
							<td>角色名称：</td><td>
							<input type="text" name="name" />
							</td>
							<td>角色代码：</td>
							<td>
							<input type="text" name="code" />
							</td>
						</tr>
						<tr>
							<td>是否禁用：</td><td colspan="3">
							<input id="disabled_true" type="radio" name="disabled" value="true" />
							<label for="disabled_true">禁用</label>
							<input id="disabled_false" type="radio" name="disabled" checked="checked" value="false" />
							<label for="disabled_false">不禁用</label></td>
						</tr>
						<tr>
							<td>角色描述：</td>
							<td colspan="3">
							<input type="text" size="54" name="description"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div id="authority_multiselector"></div>
			<div id="menus_selector">
				<ul id="menuTree" class="ztree"/>
			</div>
			<script type="text/javascript">
				var zTree;
				var setting = {
					async : {
						enable : true,
						url : "<%=request.getContextPath()%>/admin/role/menu/get.ajax",
						autoParam : ["id"],
						otherParam : {
							// "otherParam" : "zTreeAsyncTest"
						}
					},
					callback : {
						beforeExpand : beforeExpand,
						onExpand : onExpand,
						onClick : onClick
					},
					check : {
						enable : true
					}
				};
				var curExpandNode = null;
				function beforeExpand(treeId, treeNode) {
					var pNode = curExpandNode ? curExpandNode.getParentNode() : null;
					var treeNodeP = treeNode.parentTId ? treeNode.getParentNode() : null;
					for(var i = 0, l = !treeNodeP ? 0 : treeNodeP.children.length; i < l; i++) {
						if(treeNode !== treeNodeP.children[i]) {
							zTree.expandNode(treeNodeP.children[i], false);
						}
					}
					while(pNode) {
						if(pNode === treeNode) {
							break;
						}
						pNode = pNode.getParentNode();
					}
					if(!pNode) {
						singlePath(treeNode);
					}

				}

				function singlePath(newNode) {
					if(newNode === curExpandNode)
						return;
					if(curExpandNode && curExpandNode.open == true) {
						if(newNode.parentTId === curExpandNode.parentTId) {
							zTree.expandNode(curExpandNode, false);
						} else {
							var newParents = [];
							while(newNode) {
								newNode = newNode.getParentNode();
								if(newNode === curExpandNode) {
									newParents = null;
									break;
								} else if(newNode) {
									newParents.push(newNode);
								}
							}
							if(newParents != null) {
								var oldNode = curExpandNode;
								var oldParents = [];
								while(oldNode) {
									oldNode = oldNode.getParentNode();
									if(oldNode) {
										oldParents.push(oldNode);
									}
								}
								if(newParents.length > 0) {
									for(var i = Math.min(newParents.length, oldParents.length) - 1; i >= 0; i--) {
										if(newParents[i] !== oldParents[i]) {
											zTree.expandNode(oldParents[i], false);
											break;
										}
									}
								} else {
									zTree.expandNode(oldParents[oldParents.length - 1], false);
								}
							}
						}
					}
					curExpandNode = newNode;
				}

				function onExpand(event, treeId, treeNode) {
					curExpandNode = treeNode;
				}

				function onClick(e, treeId, treeNode) {
					var zTree = $.fn.zTree.getZTreeObj("treeDemo");
					zTree.expandNode(treeNode, null, null, null, true);
				}

				$(function() {
					$('#menus_selector').dialog({
						draggable : true,
						title : '分配菜单',
						width : 430,
						height : "auto",
						modal : true,
						// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
						resizable : false,
						autoOpen : false,
						// show:'slide',
						buttons : {
							"OK" : function() {
								var ids = t.getSelected();
								var changeds = zTree.getChangeCheckedNodes();
								var menus = '';
								for(var i = 0; i < changeds.length; i++) {
									menus += (changeds[i].id + ",");
								}
								confirmAssignMenus(ids[0], menus);
							},
							"Cancel" : function() {
								$(this).dialog("close");
							},
							"Close" : function() {
								$(this).dialog("close");
							}
						}
					});
				});
				function assignMenus() {
					var ids = t.getSelected();
					if(ids.length == 0 || ids.length > 1) {
						showMsg("<font colo='red'>请选择一条记录!</font>");
					} else {
						setting.async.otherParam['roleId'] = ids;
						zTree = $.fn.zTree.init($("#menuTree"), setting);
						$('#menus_selector').dialog("open");
					}
				}

				function confirmAssignMenus(id, menus) {
					doPostAjax("<%=request.getContextPath()%>/admin/role/assignMenu.ajax", 'id=' + id + "&menus=" + menus, function(data, textStatus, XMLHttpRequest) {
						showMsg(data);
						zTree.refresh();
					});
				}
			</script>
			<%@ include file="../foot.jsp"%>
		</div>
	</body>
</html>

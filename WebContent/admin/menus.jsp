<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>菜单管理</title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/lib-base.jsp"></script>
	</head>
	<body>
		<%@ include file="left.jsp"%>
		<div id="page">
			<%@ include file="../top.jsp"%>
			<div id="main">
				<div>
					<ul id="menuTree" class="ztree"></ul>
				</div>
				<script type="text/javascript">
					var zTree;
					var setting = {
						async : {
							enable : true,
							url : "<%=request.getContextPath()%>/menu/query.ajax",
							autoParam : ["id", "name=n", "level=lv"],
							otherParam : {
								// "otherParam" : "zTreeAsyncTest"
							}
						},
						view : {
							addHoverDom : addHoverDom, //生成添加按钮
							removeHoverDom : removeAddHoverDom, //删除添加按钮
							selectedMulti : false
						},
						edit : {
							enable : true
						},
						callback : {
							beforeEditName : beforeEditName,
							beforeRemove : beforeRemove
							// ,beforeRename : beforeRename,
							// onRemove : onRemove,
							// onRename : onRename
						}
						// ,check : {
						// enable : true,
						// chkStyle : "radio",
						// radioType : "all"
						// }
					};
					function addHoverDom(treeId, treeNode) {
						var sObj = $("#" + treeNode.tId + "_span");
						if(treeNode.editNameFlag || $("#addBtn_" + treeNode.id).length > 0)
							return;
						var addStr = "<span class='button add' id='addBtn_" + treeNode.id + "' title='add node' onfocus='this.blur();'></span>";
						sObj.after(addStr);
						var btn = $("#addBtn_" + treeNode.id);
						if(btn)
							btn.bind("click", function() {
								zTree.selectNode(treeNode);
								add(treeNode);
								return false;
							});
					};

					function removeAddHoverDom(treeId, treeNode) {
						$("#addBtn_" + treeNode.id).unbind().remove();
					};

					function beforeEditName(treeId, treeNode) {
						zTree.selectNode(treeNode);
						edit(treeNode);
						return false;
					}

					function beforeRemove(treeId, treeNode) {
						zTree.selectNode(treeNode);
						remove(treeNode);
						return false;
					}

					$(function() {
						$('#config_add_form_div').dialog({
							draggable : true,
							title : '添加',
							width : 430,
							modal : true,
							// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
							resizable : false,
							autoOpen : false,
							// show:'slide',
							buttons : {
								"OK" : function() {
									ajaxSubmitForm("config_add_form", function() {
										var nodes = zTree.getSelectedNodes();
										zTree.reAsyncChildNodes(nodes[0], "refresh");
									});
								},
								"Cancel" : function() {
									$(this).dialog("close");
								}
							}
						});
						$('#menu_edit_form_div').dialog({
							draggable : true,
							title : '修改',
							width : 430,
							modal : true,
							// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
							resizable : false,
							autoOpen : false,
							// show:'slide',
							buttons : {
								"OK" : function() {
									ajaxSubmitForm("menu_edit_form", function() {
										var nodes = zTree.getSelectedNodes();
										zTree.reAsyncChildNodes(nodes[0].getParentNode(), "refresh");
									});
								},
								"Cancel" : function() {
									$(this).dialog("close");
								}
							}
						});
						zTree = $.fn.zTree.init($("#menuTree"), setting);
					});
					function add(parentNode) {
						document.forms['config_add_form'].reset();
						var pid = parentNode.id;
						$("#f_pid").val(pid);
						$('#config_add_form_div').dialog("open");
					}

					function edit(treeNode) {
						document.forms['config_add_form'].reset();
						var editForm = $("#menu_edit_form");
						$("input[name=id]", editForm).val(treeNode.id);
						var name = treeNode.name;
						var index = name.indexOf(".") + 1;
						$("input[name=name]", editForm).val(name.substring(index, name.length));
						$("input[name=link]", editForm).val(treeNode.link);
						$("input[name=order]", editForm).val(treeNode.order);
						$('#menu_edit_form_div').dialog("open");
					}

					function remove(treeNode) {
						var id = treeNode.id;
						showConfirm("确认要删除选中的菜单吗？", function() {
							doPostAjax("<%=request.getContextPath()%>/menu/delete.html", "id=" + id, function(resp) {
								showMsg(resp);
								zTree.reAsyncChildNodes(treeNode.getParentNode(), "refresh");
							});
						});
					}
				</script>
			</div>
			<%@ include file="../foot.jsp"%>
		</div>
		<div id="config_add_form_div" style="display: none;">
			<form id="config_add_form" action="<%=request.getContextPath()%>/menu/addsubmenu.ajax"  method="post">
				<input type="text" style="display: none;" id="f_pid" name="pid" />
				<table>
					<tr>
						<td>名称：</td><td>
						<input type="text" name="name" size="34" />
						</td>
					</tr>
					<tr>
						<td>链接：</td><td>
						<input type="text" name="link" size="34" />
						</td>
					</tr>
					<tr>
						<td>排序：</td><td>
						<input type="text" name="order" size="8" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div id="menu_edit_form_div" style="display: none;">
			<form id="menu_edit_form" action="<%=request.getContextPath()%>/menu/editmenu.ajax"  method="post">
				<input type="text" style="display: none;" id="f_id" name="id" />
				<table>
					<tr>
						<td>名称：</td><td>
						<input type="text" name="name" size="34" />
						</td>
					</tr>
					<tr>
						<td>链接：</td><td>
						<input type="text" name="link" size="34" />
						</td>
					</tr>
					<tr>
						<td>排序：</td><td>
						<input type="text" name="order" size="8" />
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>

<%@ page language="java" pageEncoding="utf-8"%>
<div id="head">
	<div class="innertube">
		<c:if test="${sessionScope.user ne NULL}">
			<span><a href="<%=request.getContextPath()%>/"><spring:message code="base.home" /></a></span>
			<span style="float: right;"> 欢迎您:<font color="green">${sessionScope.user.userName}</font>,
				<c:if test='${newMsgs eq 0}'>
					<a href="javascript:showMyMsgs();">消息</a>,
				</c:if>
				<c:if test='${newMsgs > 0}'>
					<a href="javascript:showMyMsgs();">新消息(<font color="red"><b>${newMsgs}</b></font>)</a>,
				</c:if>
				<a href="<%=request.getContextPath()%>/j_security_logout.html">退出系统</a> </span>
		</c:if>
		<c:if test="${sessionScope.user eq NULL}">
			<span> &nbsp; </span>
			<span style="float: right;"> <a href="<%=request.getContextPath()%>/login.jsp">登录</a>&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/register.jsp">注册新用户</a></span>
		</c:if>
		<hr style="clear: both;" />
	</div>
	<div id="msgBox" style="display: none;">
		<table id="messageQueryTable"></table>
	</div>
	<script type="text/javascript">
		var msgTable = null;
		function initMsgTable() {
			msgTable = new SimpleTable("messageQueryTable", {
				pageSize : 6,
				param : '',
				autoLoad : false,
				url : '<%=request.getContextPath()%>/message/query.html',
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
				columns : {
					checkbox : {
						width : '5%'
					},
					title : {
						header : '标题',
						width : '35%'
					},
					type : {
						header : '类型',
						width : '12%'
					},
					fromUserName : {
						header : '来源',
						width : '10%'
					},
					readed : {
						header : '状态',
						width : '8%'
					},
					createTime : {
						header : '发送时间',
						width : '25%'
					}
				},
				formatters : {
					title : function(v, obj) {
						var title = "<a href='#'><font size='2' color='black'>";
						if(obj.readed == false) {
							title += "<b>" + v + "</b>";
						} else {
							title += v;
						}
						title += "</font></a>";
						var titleObj = $(title);
						titleObj.click(function() {
							showMyMsg(obj.id, obj.title, obj.content, obj.readed);
						});
						return titleObj;
					},
					type : function(v, obj) {
						if(obj.systemMessage) {
							return '系统消息';
						} else {
							return v;
						}
					},
					fromUserName : function(v, obj) {
						if(obj.systemMessage) {
							return '系统';
						} else {
							return v;
						}
					},
					readed : function(v, obj) {
						return v ? '已读' : '未读';
					}
				},
				info : {
					pageSizeSelect : '', //不显示此控件
					//pageSizeSelect : '显示{0}条记录',
					//pagingInfo : '目前显示{0}-{1}条记录，共计：{2}'
					pagingInfo : '<a href="javascript:doDeleteMyMsgs();">批量删除</><span style="margin:0 20px;"><span><a href="javascript:setMyMsgsReaded();">标记已读</>'
				}
			});

			$('#msgBox').dialog({
				draggable : true,
				title : '消息框',
				width : 690,
				modal : true,
				// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
				resizable : false,
				draggable : true,
				autoOpen : false,
				// show:'slide',
				buttons : {
					// "Cancel" : function() {
					// $(this).dialog("close");
					// }
				}
			});
		}

		function showMyMsgs() {
			if(msgTable == null) {
				initMsgTable();
			}
			msgTable.doSearch();
			$("#msgBox").dialog("open");
		}

		function showMyMsg(id, title, content, readed) {
			showMessage(title, content);
			if(readed == false) {
				doPostAjax("<%=request.getContextPath()%>/message/setreaded/" + id + ".html", '', function(data, textStatus, XMLHttpRequest) {
					msgTable.doSearch();
				});
				// $.ajax({
				// dataType : "json",
				// type : "post",
				// timeout : 30000,
				// url : "<%=request.getContextPath()%>/message/setreaded/" + id + ".do",
				// data : '',
				// success : function(data, textStatus, XMLHttpRequest) {
				// msgTable.doSearch();
				// }
				// });
			}
		}

		function doDeleteMyMsg(id) {
			showConfirm("确定要删除该消息吗？", function() {
				doPostAjax("<%=request.getContextPath()%>/message/delete/" + id + ".html", '', function(data, textStatus, XMLHttpRequest) {
					msgTable.doSearch();
				});
				// $.ajax({
				// dataType : "json",
				// type : "post",
				// timeout : 30000,
				// url : "<%=request.getContextPath()%>/message/delete/" + id + ".do",
				// data : '',
				// success : function(data, textStatus, XMLHttpRequest) {
				// msgTable.doSearch();
				// }
				// });
			});
		}

		function doDeleteMyMsgs() {
			var ids = msgTable.getSelected();
			if(ids == '') {
				showMessage("错误", "请至少选择一条消息!");
				return;
			}
			showConfirm("确定要删除所选择的消息吗？", function() {
				doPostAjax("<%=request.getContextPath()%>/message/deletemore.html", 'ids=' + ids, function(data, textStatus, XMLHttpRequest) {
					msgTable.doSearch();
				});
				// $.ajax({
				// dataType : "json",
				// type : "post",
				// timeout : 30000,
				// url : "<%=request.getContextPath()%>/message/deletemore.do",
				// data : 'ids=' + ids,
				// success : function(data, textStatus, XMLHttpRequest) {
				// msgTable.doSearch();
				// }
				// });
			});
		}

		function setMyMsgsReaded() {
			var ids = msgTable.getSelected();
			if(ids == '') {
				showMessage("错误", "请至少选择一条消息!");
				return;
			}
			showConfirm("确定要将所选择的消息标记为已读吗？", function() {
				doPostAjax("<%=request.getContextPath()%>/message/setreadmore.html", 'ids=' + ids, function(data, textStatus, XMLHttpRequest) {
					msgTable.doSearch();
				});
				// $.ajax({
				// dataType : "json",
				// type : "post",
				// timeout : 30000,
				// url : "<%=request.getContextPath()%>/message/setreadmore.do",
				// data : 'ids=' + ids,
				// success : function(data, textStatus, XMLHttpRequest) {
				// msgTable.doSearch();
				// }
				// });
			});
		}
	</script>
</div>

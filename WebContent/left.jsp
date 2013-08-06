<%@ page language="java" pageEncoding="utf-8"%>
<div id="left">
	<div class="innertube">
		<h1 class="head"><spring:message code="base.name" /></h1>
		
		<!--
		<div>
			<ul id="menu_ul" class="ztree"></ul>
		</div>
		<script type="text/javascript">
			var leftMenus;
			var setting = {
				async : {
					enable : true,
					url : "<%=request.getContextPath()%>/getMenus.ajax",
					autoParam : ["id"]
				},
				callback : {
					onClick : function(event, treeId, treeNode) {
						window.location.href = treeNode.link;
					}
				},
				view : {
					fontCss : {
						"font-size" : "20px",
						color : "#C7C7C7"

					}
				}
			};
			$(function() {
				leftMenus = $.fn.zTree.init($("#menu_ul"), setting);
			});

		</script>
	</div>
	-->
	
	<!---->
	<ul class="navigation">
		<c:forEach items="${menus}" var="menu">
			<c:if test="${menu.level eq 1}">
				<li>
					<a class='${(pageContext.request.requestURI eq menu.link) ? "currentPage" : ""}' href="${menu.link}">${menu.name}</a>
				</li>
			</c:if>
		</c:forEach>
	</ul>
	
</div>
</div>
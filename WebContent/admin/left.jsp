<%@ page language="java" pageEncoding="utf-8"%>
<div id="left">
	<div class="innertube">
		<h1 class="head">后台管理</h1>
		<ul class="navigation">
			<c:forEach items="${menus}" var="menu">
				<c:if test="${menu.level eq 2}">
					<c:if test='${fn:contains(menu.link, "/admin/")}'>
						<li>
							<a class='${(pageContext.request.requestURI eq menu.link) ? "currentPage" : ""}' href="${menu.link}">${menu.name}</a>
						</li>
					</c:if>
				</c:if>
			</c:forEach>
		</ul>
		<div class="bottom_menu">
			<ul class="navigation">
				<li>
					<a href="../" class="beright">返回主菜单</a>
				</li>
			</ul>
		</div>
	</div>
</div>
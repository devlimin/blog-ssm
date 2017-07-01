<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div>
	<div class="pull-left" style="margin-right: 5px;">
		<a href="#"> <img width="60px" height="60px"
			src="${sessionScope.loginUser.headUrl}" />
		</a>
	</div>
	<div style="font-size: 20px;">${sessionScope.loginUser.username }</div>
	<div style="margin-top: 5px;">
		<a href="#">个人主页</a> |
		<a href="${pageContext.request.contextPath }/article/list/${loginUser.id}">我的博客</a>
	</div>
</div>
<div style="margin-top: 50px;">
	<ul class="nav nav-tabs">
		<li role="presentation" class="${option == 'articleManage' ? 'active' : ''}">
			<a href="${pageContext.request.contextPath }/article/manage/list/-1">文章管理</a>
		</li>
		<li role="presentation" class="${option == 'categoryManage' ? 'active' : ''}">
			<a href="${pageContext.request.contextPath }/category/manage/list">类别管理</a>
		</li>
		<li role="presentation" class="${option == 'commentManage' ? 'active' : ''}">
			<a href="${pageContext.request.contextPath }/comment/manage/list/other">评论管理</a>
		</li>
		<li role="presentation" class="${option == 'articleCreate' ? 'active' : ''}">
			<a href="${pageContext.request.contextPath }/article/manage/create">发表文章</a>
		</li>
	</ul>
</div>
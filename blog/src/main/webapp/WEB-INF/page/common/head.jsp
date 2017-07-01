<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="bg-info">
	<a href="${pageContext.request.contextPath }/"> <img style=""
		class="pull-left"
		src="${pageContext.request.contextPath }/resources/img/bg.jpg"
		width="75px" />
		<div class="pull-left" style="font-size: 30px;">LM BLOG</div>
	</a>
	<div class="text-right" style="font-size: 30px;">
		<c:choose>
			<c:when test="${!empty sessionScope.loginUser}">
				<a style="color: black;" href="#">${loginUser.username }</a>
				<a title="我的博客" style="color: black;" 
					href="${pageContext.request.contextPath }/article/manage/list/-1"><span
					class="glyphicon glyphicon-home"></span></a>
				<a title="退出" style="color: black;"
					href="${pageContext.request.contextPath }/user/logout"><span
					class="glyphicon glyphicon-log-out"></span></a>
			</c:when>
			<c:otherwise>
				<a style="color: black;"
					href="${pageContext.request.contextPath }/user/login">登陆</a>
				<a style="color: black;"
					href="${pageContext.request.contextPath }/user/regist">注册</a>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<br />
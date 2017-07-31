<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.limin.blog.dto.ValueObject" %>
<%@ page import="com.limin.blog.entity.Article" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>LM Blog</title>
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/jquery-3.1.1.min.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/bootstrap.min.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/bootstrap.min.js"></script>
		<script type="text/javascript">
            var page_url = null;
            $(function () {
                var category = $("#cId").val();
				page_url="${pageContext.request.contextPath}/article/manage/list"
				page_url = page_url+"/"+category;
            })
			function del(articleId) {
	 			if(confirm("确定要删除该篇文章吗？")) {
	 				$.ajax({
	 					type: "post",
						url: "${pageContext.request.contextPath }/article/manage/delete",
						data: 'articleId='+articleId,
						success: function(msg) {
							if($.trim(msg) == "success") {
								$("#"+articleId).remove();
							}
						}
	 				});
				} else {
					alert("xxx");
				}
			}
		</script>
	</head>

	<body style="margin: auto;">
		
		<div class="" style="width: 70%;margin: auto;">
			<jsp:include page="/WEB-INF/page/common/head.jsp" flush="true"></jsp:include>
			<br />
			<jsp:include page="/WEB-INF/page/common/manage.jsp" flush="true"></jsp:include>
			<br />

			<div>
				<div class="dropdown">
					<button type="button" class="btn dropdown-toggle" id="dropdownMenu1" data-toggle="dropdown">
					个人分类
					<span class="caret"></span>
					</button>

					<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
						<li role="presentation">
							<a href="${pageContext.request.contextPath }/article/manage/list">
								全部
							</a>
						</li>
						<c:forEach items="${categoryList }" var="category">
							<li role="presentation">
								<a href="${pageContext.request.contextPath }/article/manage/list/${category.id}">
									${category.type }
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>

			<table class="table table-striped table-hover  table-condensed">
				<thead class="">
					<th width="500px">标题</th>
					<th>阅读</th>
					<th>评论</th>
					<th class="text-center">操作</th>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${empty pageResult}">
							<tr><td colspan="4">暂无数据</td></tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${pageResult.pageItems }" var="articleVo">
								<%
									ValueObject vo = (ValueObject) pageContext.findAttribute("articleVo");
									pageContext.setAttribute("article",vo.get("article"));
									pageContext.setAttribute("viewCount",vo.get("viewCount"));
									pageContext.setAttribute("commentCount",vo.get("commentCount"));
								%>
								<tr id="${article.id}">
									<td>
										<a href="${pageContext.request.contextPath }/article/detail/${article.id }">
												${article.title }
										</a>
									</td>
									<td>${viewCount }</td>
									<td>${commentCount }</td>
									<td class="text-center">
										<a href="${pageContext.request.contextPath }/article/manage/edit?articleId=${article.id}">编辑</a>
										<a href="javascript:void(0);" onclick="del(${article.id});">删除</a>
									</td>
								</tr>
							</c:forEach>
							<tr><td colspan="4">
								<jsp:include page="../../common/pageNavigator.jsp" flush="true"></jsp:include>
							</td></tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>

			<br />
		</div>
		<input type="hidden" id="cId" name="cId" value="${categoryId}">
	</body>

</html>
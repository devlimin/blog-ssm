<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>LM Blog</title>
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/jquery-3.1.1.min.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/bootstrap.min.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/bootstrap.min.js"></script>
		<script type="text/javascript">
			function del(commentId) {
				var url = "${pageContext.request.contextPath }/comment/delete";
				var data = "commentId="+commentId;
				$.ajax({
					type : "post",
					data : data,
					url : url,
					success : function(msg) {
						$("."+commentId).remove();
					}
				});
			}
		</script>
	</head>

	<body style="margin: auto;">
		<div class="" style="width: 70%;margin: auto;">
			<jsp:include page="/WEB-INF/page/common/head.jsp" flush="true"></jsp:include>
			<br />
			<jsp:include page="/WEB-INF/page/common/manage.jsp" flush="true"></jsp:include>
			<br />
			<ol class="breadcrumb">
				<li>
					<a href="${pageContext.request.contextPath }/comment/manage/list/other" style="color: ${commentOption == 'other' ? 'green' : ''}">我文章的评论</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath }/comment/manage/list/my" style="color: ${commentOption == 'my' ? 'green' : ''}">我发表的评论</a>
				</li>
			</ol>
			<table class="table table-striped table-hover  table-condensed">
				<thead class="">
					<th width="500px">标题</th>
					<c:choose>
						<c:when test="${commentOption == 'other' }">
							<th>评论人</th>
						</c:when>
						<c:when test="${commentOption == 'my' }">
							<th>作者</th>
						</c:when>
					</c:choose>
					<th class="">发表时间</th>
					<th class="">操作</th>
				</thead>
				<tbody>
					<c:forEach items="${pageResult.pageItems }" var="commentAndArticle">
						<tr class="${commentAndArticle.id }">
							<td>
								<a href="${pageContext.request.contextPath }/article/detail/${commentAndArticle.articleId }">
									${commentAndArticle.articleTitle }</a>
							</td>
							<td>
								<a href="${pageContext.request.contextPath }/article/list/${commentAndArticle.userId}">
									${commentAndArticle.username }
								</a>
							</td>
							<td><fmt:formatDate value="${commentAndArticle.releaseDate }" pattern="yyyy-mm-dd hh:MM:ss"/></td>
							<td class="text-center">
								<a href="javascript:void(0);" onclick="del(${commentAndArticle.id});">删除</a>
							</td>
						</tr>
						<tr class="${commentAndArticle.id }">
							<td colspan="4">
								${commentAndArticle.content }
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<nav class="text-center">
				<ul class="pagination">
				<li>
					<a>
						${pageResult.totalRow }条
						${pageResult.totalPage }页
					</a>
				</li>
					<c:if test="${pageResult.pageNum != 1 }">
						<li>
							<a href="${pageContext.request.contextPath }/comment/manage/list/${commentOption }?pageNum=1" aria-label="Previous">
								<span aria-hidden="true">首页</span>
							</a>
						</li>
						<li>
							<a href="${pageContext.request.contextPath }/comment/manage/list/${commentOption }?pageNum=${pageResult.pageNum - 1}" aria-label="Previous">
								<span aria-hidden="true">&laquo;</span>
							</a>
						</li>
					</c:if>
					<c:forEach begin="${pageResult.startIndex }" end="${pageResult.endIndex }" var="index">
						<li>
							<c:choose>
								<c:when test="${pageResult.pageNum == index }">
									<a>${index }</a>
								</c:when>
								<c:otherwise>
									<c:url value="/comment/manage/list/${commentOption }" var="url">
										<c:param name="pageNum" value="${index }"></c:param>
									</c:url>
									<a href="${url }">${index }</a>
								</c:otherwise>
							</c:choose>
						</li>
					</c:forEach>
					<c:if test="${pageResult.pageNum != pageResult.totalPage }">
						<li>
							<a href="${pageContext.request.contextPath }/comment/manage/list/${commentOption }?pageNum=${pageResult.pageNum + 1}" aria-label="Previous">
								<span aria-hidden="true">&raquo;</span>
							</a>
						</li>
						<li>
							<a href="${pageContext.request.contextPath }/comment/manage/list/${commentOption }?pageNum=${pageResult.totalPage}" aria-label="Previous">
								<span aria-hidden="true">尾页</span>
							</a>
						</li>
					</c:if>
				</ul>
			</nav>
			
		</div>

	</body>

</html>
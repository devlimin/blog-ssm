<%@ page language="java" import="java.util.*,com.limin.blog.entity.Article" pageEncoding="UTF-8"%>
<%@ page import="com.limin.blog.dto.ValueObject" %>
<%@ page import="com.limin.blog.entity.Category" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>LM BLOG</title>
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/jquery-3.1.1.min.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/bootstrap.min.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/bootstrap.min.js"></script>
		<script>
            var page_url = null;
            $(function () {
                var uId = $("#uId").val();
                page_url="${pageContext.request.contextPath}/article/list/"+uId;
                var cId = $("#cId").val();
                if(cId != null || cId != "") {
				    page_url = page_url +"/"+cId;
                }
            })
		</script>
	</head>

	<body style="background-color: whitesmoke;margin: auto; ">
		<div class="container" >
			<div class="row">
				<jsp:include page="/WEB-INF/page/common/head.jsp" flush="true"></jsp:include>
				<div class="col-xs-3">
					<div class="text-center" style="background-color: white;padding: 10px;">
						<img src="${user.headUrl}" width="120px" height="120px" />
						<div class="h4" style="">
							${user.username }
						</div>
						<div class="h5">
							${user.motto }
						</div>
						<div class="h5">
							<!-- 博客：篇 -->
						</div>
					</div>
					<div style="clear: both;"></div>
					<div style="">
						<ul class="list-group">
							<div class="list-group-item text-center h4">
								博文分类
							</div>
							<c:forEach items="${categoryVos }" var="categoryVo">
								<%
									ValueObject vo = (ValueObject) pageContext.findAttribute("categoryVo");
									Category category = (Category) vo.get("category");
									long articleCount = (long) vo.get("articleCount");
									pageContext.setAttribute("category", category);
									pageContext.setAttribute("articleCount", articleCount);
								%>

								<a href="${pageContext.request.contextPath }/article/list/${user.id}/${category.id}" class="list-group-item">
										${category.type }
									<span style="float: right">(${articleCount })</span>
								</a>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div class="col-xs-9" style="background-color: white;">
					<c:choose>
						<c:when test="${empty pageResult}">
							暂无数据
						</c:when>
						<c:otherwise>
							<c:forEach items="${pageResult.pageItems }" var="articleVo">
								<%
									ValueObject vo = (ValueObject) pageContext.findAttribute("articleVo");
									Article article = (Article) vo.get("article");
									long viewCount = (long) vo.get("viewCount");
									long commentCount = (long) vo.get("commentCount");
									pageContext.setAttribute("article", article);
									pageContext.setAttribute("viewCount", viewCount);
									pageContext.setAttribute("commentCount", commentCount);
								%>
								<div class="">
									<div class="h1 text-left text-muted">
										<a href="${pageContext.request.contextPath }/article/detail/${article.id}">${article.title }</a>
									</div>
									<div class="">
										${article.content }
									</div>
									<div class="pull-right">
										<fmt:formatDate value="${article.releaseDate }" pattern="yyyy-MM-dd hh:mm:ss"/> |
										阅读：${viewCount } |
										评论：${commentCount }
									</div>
									<hr color="white" style="height: 10px;" />
								</div>
							</c:forEach>
							<jsp:include page="../common/pageNavigator.jsp" flush="true"></jsp:include>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<input type="hidden" id="uId" name="uId" value="${user.id}">
		<input type="hidden" id="cId" name="cId" value="${categoryId}">
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/canvas.js"></script>

	</body>

</html>
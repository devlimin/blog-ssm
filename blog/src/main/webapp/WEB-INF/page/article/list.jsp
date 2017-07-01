<%@ page language="java" import="java.util.*,com.limin.blog.entity.Article" pageEncoding="UTF-8"%>
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
							<c:forEach items="${categoryList }" var="category">
								<a href="${pageContext.request.contextPath }/article/list/${user.id}/${category.id}" class="list-group-item">
									${category.type }
									<span style="float: right">(${category.articlecount })</span>
								</a>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div class="col-xs-9" style="background-color: white;">
					<c:forEach items="${pageResult.pageItems }" var="article">
						<div class="">
							<div class="h1 text-left text-muted">
								<a href="${pageContext.request.contextPath }/article/detail/${article.id}">${article.title }</a>
							</div>
							<div class="">
								<% 
										Article article = (Article)pageContext.findAttribute("article");
										String content = article.getContent();
										int length = 0;
										if(content.length() > 300) {
											length = 300;
										} else {
											length = content.length();
										}
										content = content.substring(0, length);
										pageContext.setAttribute("content", content);
									%>
									<pre>
										${content }
									</pre>
							</div>
							<div class="pull-right">
								<fmt:formatDate value="${article.releaseDate }" pattern="yyyy-MM-dd hh:mm:ss"/> |
								阅读：${article.viewCount } |
								赞：${article.support } |
								踩：${article.against }
							</div>
							<hr color="white" style="height: 10px;" />
						</div>
					</c:forEach>

					<jsp:include page="../common/pageNavigator.jsp" flush="true"></jsp:include>
				</div>
			</div>
		</div>
		<input type="hidden" id="uId" name="uId" value="${user.id}">
		<input type="hidden" id="cId" name="cId" value="${categoryId}">
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/canvas.js"></script>

	</body>

</html>
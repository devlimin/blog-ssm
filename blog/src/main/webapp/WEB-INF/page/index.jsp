<%@ page language="java" import="java.util.*,com.limin.blog.entity.Article,com.limin.blog.dto.ValueObject" pageEncoding="UTF-8"%>
<%@ page import="com.limin.blog.entity.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>LM BLOG</title>
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/jquery-3.1.1.min.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/bootstrap.min.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/bootstrap.min.js"></script>
		<script>
			var page_url = null;
            $(function () {
                var category = $("#cId").val();
                var option = $("#oId").val();
                if(category == null || category == "") {
                    page_url="${pageContext.request.contextPath}/"
                } else if(option != null || option != "") {
                    page_url="${pageContext.request.contextPath}/sys/"+option+"/"+category;
                }
            })

		</script>
	</head>

	<body style="margin: auto; background-color: whitesmoke;">
		<div class="container">
			<div class="row">
				<jsp:include page="/WEB-INF/page/common/head.jsp" flush="true"></jsp:include>
				
				<div class="col-xs-9" style="background-color: white;">
					<div style="">
						<c:choose>
							<c:when test="${empty category}">
								<div class="h3">
									<strong>最新文章</strong>
								</div>
							</c:when>
							<c:otherwise>
								<div class="h3">
									<strong>
										<%out.print(Article.sysCategoryType.get(request.getAttribute("category"))); %>
									</strong>
								</div>
								<div class="pull-right">
									<a class="h4" style="color: ${option == 'new' ? 'red' : 'black'};"
									   href="${pageContext.request.contextPath }/sys/new/${category}">最新</a>
									<a class="h4" style="color: ${option == 'hot' ? 'red' : 'black'};"
									   href="${pageContext.request.contextPath }/sys/hot/${category}">最热</a>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
					<hr />
					<br />

					<c:forEach items="${requestScope.pageResult.pageItems }" var="vo">
						<%
							ValueObject vo = (ValueObject) pageContext.findAttribute("vo");
							Article article = (Article) vo.get("article");
							long viewCount = (long) vo.get("viewCount");
							long commentCount = (long) vo.get("commentCount");
							User user = (User) vo.get("user");
							pageContext.setAttribute("article", article);
							pageContext.setAttribute("viewCount", viewCount);
							pageContext.setAttribute("commentCount", commentCount);
							pageContext.setAttribute("user", user);
						%>
						<div id="">
							<div class="pull-left text-center" style="padding: 10px;">
								<a href="${pageContext.request.contextPath }/article/list/${user.id}">
									<img class="img-circle" src="${user.headUrl}" width="70px" height="70px" />
									<div style="color: black;">${user.username }</div>
								</a>
							</div>
							<div style="margin-left: 100px;">
								<div class="h4">
									<a class="" style="color: black;" href="${pageContext.request.contextPath }/article/detail/${article.id}">
										<strong>
											${article.title}
										</strong>
									</a>
								</div>
								<div class="text-muted">
									${article.content }
								</div>
								<br>
								<div class="pull-left" >
									<a class="" style="color: black;" href="${pageContext.request.contextPath }/sys/new/${article.sysCategory }">
										<%
											pageContext.setAttribute("category",Article.sysCategoryType.get(article.getSysCategory()));
										 %>
										 ${category }
									</a>
								</div>
								<div class="pull-right">
									<fmt:formatDate value="${article.releaseDate}" pattern="yyyy-MM-dd hh:mm:ss"></fmt:formatDate>
									阅读：${viewCount } |
									评论：${commentCount }
								</div>
								<div style="clear: both;"></div>
								<hr>
							</div>
						</div>
					</c:forEach>

					<jsp:include page="common/pageNavigator.jsp" flush="true"></jsp:include>
				</div>

				<div class="col-xs-3">
					<div>
						<ul class="list-group">
							<div class="list-group-item text-center h4" style="margin-top: 0px;">
								博文分类
							</div>
							<c:forEach items="<%=Article.sysCategoryType %>" var="entry">
								<a href="${pageContext.request.contextPath }/sys/new/${entry.key }" class="list-group-item">
									${entry.value }
								</a>
							</c:forEach>
						</ul>
					</div>
				</div>
			
			</div>
		</div>
		<input type="hidden" id="cId" name="category" value="${requestScope.category}">
		<input type="hidden" id="oId" name="option" value="${option}">
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/canvas.js" ></script>
	</body>

</html>
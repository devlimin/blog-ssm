<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.limin.blog.dto.ValueObject" %>
<%@ page import="com.limin.blog.entity.Comment" %>
<%@ page import="com.limin.blog.entity.User" %>
<%@ page import="com.limin.blog.dto.CommentVo" %>
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
		<!-- ueditor -->
		<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath }/resources/ueditor/ueditor.config.js"></script>
	    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath }/resources/ueditor/ueditor.all.min.js"> </script>
	    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath }/resources/ueditor/lang/zh-cn/zh-cn.js"></script>
	    <script type="text/javascript">
	    	window.UEDITOR_HOME_URL = "${pageContext.request.contextPath }/resources/ueditor/";
	    	var ue = UE.getEditor('content', {
	    		toolbars: [[
		            'undo', 'redo', '|',
		            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'blockquote', 'pasteplain', '|', 
		            'forecolor', 'selectall', 'cleardoc', '|',
		            'link', 'unlink','|', 
		            'emotion','insertcode',
		            'horizontal', 'date', 'time', 'spechars'	        
		     	]],
		     	maximumWords : 1000
	    	});
	    </script>
		<script type="text/javascript">
			var supportNum = 0;
			var againstNum = 0;
			function support(articleId) {
				if(supportNum == 0) {
					$.ajax({
						type: "post",
						url: "${pageContext.request.contextPath }/article/support",
						data: 'articleId='+${article.id},
						success: function(msg) {
							if(msg == "success") {
								var support = parseInt($("#support").text());
								$("#support").text(support+1);
								supportNum = 1;
								againstNum = 0;
							}
						}
					})
				}
			}
			
			function against(articleId) {
				if(againstNum == 0) {
					$.ajax({
						type: "post",
						url: "${pageContext.request.contextPath }/article/against",
						data: 'articleId='+${article.id},
						success: function(msg) {
							if(msg == "success") {
								var against = parseInt($("#against").text());
								$("#against").text(against+1);
								supportNum = 0;
								againstNum = 1;
							}
						}
					})
				}
			}
			
			function del(commentId) {
				$.ajax({
					type : "post",
					url : "${pageContext.request.contextPath }/comment/delete",
					data : "commentId="+commentId,
					success : function(msg) {
						if(msg == "success") {
							alert(msg);
						}
					}
				})
			}
			
			function ref(id, username) {
				document.getElementById('parentId').setAttribute('value', id);
				$("#refUsername").text('回复：'+username)
				document.getElementById('content').focus();
			}
			function addcoment() {
				var data  = document.getElementById("content").value;
				/* 
				if($.trim($("#content").val()) == "") {
					alert("文章内容不能为空");
				} else {
				 */
					var data = $("#addCommentForm").serialize();
					$.ajax({
						type : "post",
						url : "${pageContext.request.contextPath }/comment/add",
						data : data,
						success : function(msg) {
							if(msg == "success") {
							/* 
								var parentId = $("#parentId").val();
								var build = $("#build").val();
								var grade = $("#grade").val();
								var append = "<div style='margin-left: "+(grade+1)*50+"px'>"
												+"<div class='bg-info'>"
													+"<c:if test='${!empty comment.build }'>"
														+"${comment.build }楼 |"
													+"</c:if>"
													
													+"<c:if test='${!empty comment.refUsername }'>"
														+"ref:${comment.refUsername } |"
													+"</c:if>"
													+"<a href='${pageContext.request.contextPath }/article/list/${comment.userId}'>"
														+"${comment.username }</a> |"
													+"<fmt:formatDate value='${comment.releaseDate }' pattern='yyyy-MM-dd hh:mm:ss'/>"
													+"| <a class='btn btn-sm' onclick='ref('${comment.id }','${comment.username}','${comment.build }','${comment.grade }');'>回复</a>"
														+"| <a class='btn btn-sm' onclick='del(${comment.id})'>删除</a>"
												+"</div>"
												+"<div class='row' style='padding: 5px;'>"
													+"<div class='col-xs-1'>"
														+"<a href='${pageContext.request.contextPath }/article/list/${comment.userId}'>"
															+"<img class='img-circle' height='50px' width='50px' src='../../img/1.jpg' />"
														+"</a>"
													+"</div>"
													+"<div class='col-xs-11'>"
														+$("#content").val()
													+"</div>"
												+"</div>"
											+"</div>"
								if(parentId == "") {
									$("#checkcomment").after("<div style=''>");
								}
								 */
							}
						}
					})
				/* 	
				}
				$("#3content").val("");
				 */
			}
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
							博客：篇
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
				<div class="col-xs-9">
					<div class="" style="background-color: white;padding: 10px;">
						<div class=" text-left text-muted">
							<h1>${article.title }</h1>
						</div>
						<div id="">
							<div class="pull-left">
								分类：
								<c:forEach items="${articleCategories }" var="category">
									<a class="" href="${pageContext.request.contextPath }/article/list/${user.id}/${category.id}"> ${category.type } </a>
								</c:forEach>
							</div>
							<div class="pull-right">
								<fmt:formatDate value="${article.releaseDate }" pattern="yyyy-MM-dd hh:mm:ss"/> |
								阅读：${article.viewCount }
							</div>
						</div>
						<br />
						<hr />
						<div class="">
							${article.content }
						</div>
						<div class="text-center">
							<a 
								class="btn btn-lg ${sessionScope.loginUser == null ? 'btn-danger' : 'btn-success' }" 
										onclick="${sessionScope.loginUser == null ? '' : 'support()' }">
								赞
								<div id="support">${article.support }</div>
							</a>
							<a class="btn btn-lg  ${sessionScope.loginUser == null ? 'btn-danger' : 'btn-success' }" 
										onclick="${sessionScope.loginUser == null ? '' : 'against()' }">
								踩
								<div id="against">${article.against }</div>
							</a>
						</div>
					</div>
					<br />
					
					<div class="" style="background-color: white;padding: 10px;">
						<c:choose>
							<c:when test="${empty commentAndUsers }">
								<div class="bg-primary">暂无评论！</div>
							</c:when>
							<c:otherwise>
								<div class="bg-primary" id="checkcomment">查看评论</div>
								<c:forEach items="${commentAndUsers }" var="commentAndUser" varStatus="status">
									<%
										ValueObject vo = (ValueObject) pageContext.findAttribute("commentAndUser");
										CommentVo comment = (CommentVo) vo.get("comment");
										User user = (User) vo.get("user");
										pageContext.setAttribute("comment", comment);
										pageContext.setAttribute("user", user);
									%>
									<c:if test="${empty comment.parentId }">
										<hr />
									</c:if>
									<div style="margin-left: ${comment.grade * 50}px">
										<div class="bg-info">
											<c:if test="${!empty comment.build }">
												${comment.build }楼 |
											</c:if>
											<c:if test="${!empty comment.refUsername }">
												ref:${comment.refUsername } |
											</c:if>
											<a href="${pageContext.request.contextPath }/article/list/${comment.userId}">
												${user.username }</a> |
											<fmt:formatDate value="${comment.releaseDate }" pattern="yyyy-MM-dd hh:mm:ss"/>
											<c:if test="${!empty sessionScope.loginUser }">
												| <a class="btn btn-sm" onclick="ref('${comment.id }','${user.username}');">回复</a>
											</c:if>
											<c:if test="${comment.userId == sessionScope.loginUser.id }">
												| <a class="btn btn-sm" onclick="del(${comment.id})">删除</a>
											</c:if>
										</div>
										<div class="row" style="padding: 5px;">
											<div class="col-xs-1">
												<a href="${pageContext.request.contextPath }/article/list/${comment.userId}">
													<img class="img-circle" height="50px" width="50px" src="${user.headUrl}" />
												</a>
											</div>
											<div class="col-xs-11">
												${comment.content }
											</div>
										</div>
									</div>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</div>
					
					<br />
					<div style="background-color: white;padding: 10px;">
						<c:choose>
							<c:when test="${!empty sessionScope.loginUser }">
								<div class="bg-primary">发表评论</div>
								<form id="addCommentForm" action="${pageContext.request.contextPath }/comment/add">
									<table style="margin-left: 10px;" cellspacing="10px">
										<tr>
											<td>用户名：</td>
											<td>${sessionScope.loginUser.username } <span id="refUsername"></span></td>
											
										</tr>
										<tr>
											<td valign="top" style="width: 80px;">评论内容：</td>
											<td><script id="content" name="content" style="width: 720px; height: 300px;"></script> </td>
										</tr>
										<tr>
											<td>
												<input type="hidden" name="userId" value="${sessionScope.loginUser.id }">
												<input type="hidden" name="articleId" value="${article.id }">
												<input id="parentId" type="hidden" name="parentId">
											</td>
											<td><input class="btn btn-default" type="button" value="提交" onclick="addcoment();"/> </td>
										</tr>
									</table>
								</form>
							</c:when>
							<c:otherwise>
								登陆后即可评论！
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>

		<!--<script type="text/javascript" src="../../js/canvas.js"></script>-->

	</body>

</html>
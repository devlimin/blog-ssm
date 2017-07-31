<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.limin.blog.dto.ValueObject" %>
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
			function del (categoryId) {
				if(confirm("确定要删除该分类吗？")) {
					var url = "${pageContext.request.contextPath }/category/manage/delete";
					var data = "categoryId="+categoryId;
					$.ajax({
						type : "post",
						url : url,
						data : data,
						success : function (msg) {
							$("#"+categoryId).remove();
						}
					});
				}
			}
			
			function edit (categoryId, categoryType) {
				var str = prompt("分类名称", categoryType);
				if($.trim(str) != "") {
					if(confirm("确定要更改吗？")) {
						var url = "${pageContext.request.contextPath }/category/manage/edit";
						var data = "categoryId="+categoryId+"&type="+str;
						$.ajax({
							type : "post",
							url : url,
							data : data,
							success : function (msg) {
								$("#"+categoryId+" > .type").text(str);
							}
						});
						alert("更改成功！");
					} else {
						return;
					}
				} else {
					alert("分类名称不能为空！");
				}
			}
			
			function add() {
				var type = $("#type").val();
				$("#type").val("");
				if($.trim(type) == "") {
					alert("分类名称不能为空！");
				} else {
					var url = "${pageContext.request.contextPath }/category/manage/add";
					var data = "type="+type;
					$.ajax({
						type : "post",
						url : url,
						data : data,
						success : function (category) {
							$(".category:last").after("<tr id='"+category.id+"' class='category'><td class='type'>"+category.type+"</td><td>0</td><td class='text-center'><a href='javascript:void(0);' onclick='edit("+category.id+",'"+category.type+"')>编辑</a> <a href='javascript:void(0);' onclick='del("+category.id+")'>删除</a></td></tr>");
						}
					})
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
			<table class="table table-striped table-hover  table-condensed">
				<thead class="">
					<th width="500px">类别</th>
					<th>文章</th>
					<th class="text-center">操作</th>
				</thead>
				<tbody>
					<c:forEach items="${categoryVos }" var="categoryVo">
						<%
							ValueObject vo = (ValueObject) pageContext.findAttribute("categoryVo");
							pageContext.setAttribute("category",vo.get("category"));
							pageContext.setAttribute("articleCount", vo.get("articleCount"));
						%>
						<tr id="${category.id }" class="category">
							<td class="type">${category.type }</td>
							<td>${articleCount }</td>
							<td class="text-center">
								<a href="javascript:void(0);" onclick="edit(${category.id},'${category.type }')">编辑</a>
								<a href="javascript:void(0);" onclick="del(${category.id})">删除</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<input style="width: 400px;" maxlength="50" class="pull-left form-control" type="text" id="type"/>
			<input class="btn btn-default" type="button" value="添加分类" onclick="add()" />
			
		</div>

	</body>

</html>
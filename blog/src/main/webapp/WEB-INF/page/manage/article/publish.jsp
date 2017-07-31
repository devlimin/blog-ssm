<%@ page language="java" import="java.util.*,com.limin.blog.entity.Article" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>LM Blog</title>
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
		            'fullscreen', 'source', '|', 'undo', 'redo', '|',
		            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
		            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
		            'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
		            'directionalityltr', 'directionalityrtl', 'indent', '|',
		            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
		            'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
		            'simpleupload', 'insertimage', 'emotion', 'scrawl', 'map', 'insertframe', 'insertcode', 'pagebreak', 'template', 'background', '|',
		            'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|',
		            'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
		            'print', 'preview', 'searchreplace', 'drafts', 'help'
		        ]]
	    	});
	    </script>
		<script type="text/javascript">
			function create() {
				var title = $("#title").val();
				if($.trim(title) == "") {
					alert("文章标题不能为空！");
					return;
				}
				/* 
				var content = $("#content").val();
				if($.trim(content) == "") {
					alert("文章内容不能为空！");
				}
				 */
				var sysCategory = $(".sysCategory:checked");
				if(sysCategory.length == 0) {
					alert("必须选择文章分类！");
				}
				$("#publish").submit();
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
				<form:form id="publish"
						   action="${pageContext.request.contextPath }/article/manage/${publish == 'create' ? 'createhandler' : 'edithandler'}"
					modelAttribute="articleVo" cssClass="login-form" method="post" >
					<form:hidden path="id"/>
					<div>
						<div class="text-muted">文章标题</div>
						<form:input id="title" path="title" cssClass="form-control"/>
					</div>
					<hr />
					<div>
						<div class="text-muted">文章内容</div>
						<form:textarea id="content" path="content" cssStyle="height : 500px"/>
					</div>
					<hr />
					<div>
						<div class="text-muted">个人分类</div>
						<form:checkboxes items="${categories }" itemLabel="type" itemValue="id" path="categoryIds"
							cssStyle="margin-left:10px"/>
					</div>
					<hr />
					<div>
						<div class="text-muted">文章分类</div>
						<form:radiobuttons class="sysCategory"
										   path="sysCategory" items="<%=Article.sysCategoryType.entrySet() %>" itemLabel="value" itemValue="key"
										   cssStyle="margin-left:10px"/>
					</div>
					<hr />
					<div class="text-center">
						<input class="btn btn-lg" type="button" value="创建" onclick="create()"/>
						<input class="btn btn-lg" type="reset" value="重置"/>
						<input class="btn btn-lg" type="button" value="取消"/>
					</div>
				</form:form>
			</div>
		</div>
	</body>

</html>
<%@ page import="com.limin.blog.util.PageResult" %>
<%@ page import="com.limin.blog.dto.ArticleVo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>LM BLOG</title>
    <script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/jquery-3.1.1.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/bootstrap.min.css" />
    <script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/bootstrap.min.js"></script>
</head>

<body>
<jsp:include page="common/head.jsp" flush="true"></jsp:include>
<c:forEach items="${pageResult.pageItems}" var="article">
    作者：${article.username}<br>
    标题：<a href="${pageContext.request.contextPath}/article/detail/${article.id}">${article.title}</a><br>
    日期：<fmt:formatDate value="${article.releaseDate}" pattern="yyyy-MM-dd hh:mm:ss"/><br>
    内容：${article.content}<br>
    <hr />
</c:forEach>

</body>
</html>

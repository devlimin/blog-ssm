<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>LM BLOG</title>
		<script type="text/javascript" src="../../js/jquery-3.1.1.min.js"></script>
		<link rel="stylesheet" href="../../css/bootstrap.min.css" />
		<script type="text/javascript" src="../../js/bootstrap.min.js"></script>
	</head>
	<body style="margin: auto;">
		
		<div class="" style="width: 70%;margin: auto;">
			<div class="bg-info">
				<a href="#">
					<img style="" class="pull-left" src="../../img/bg.jpg" width="75px" />
					<div class="pull-left" style="font-size: 30px;">LM BLOG</div>
				</a>
				<div class="text-right" style="font-size: 30px;">
					<a style="color: black;" href="#">用户名</a>
					<a title="我的博客" style="color: black;" href="#"><span class="glyphicon glyphicon-home"></span></a>
					<a title="退出" style="color: black;" href="#"><span class="glyphicon glyphicon-log-out"></span></a>
					<!--<a style="color: black;" href="#">登陆</a>
					<a style="color: black;" href="#">注册</a>-->
				</div>
			</div>
			<br />
			<div>
				<div>
					<div>头像：</div>
					
				</div>
				<div>
					<div>座右铭</div>
					<div>
						<textarea class="form-control" cols="30" rows="10"></textarea>
					</div>
				</div>
			</div>
			
		</div>
	</body>
</html>

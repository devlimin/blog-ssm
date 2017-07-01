<%@ page language="java" import="java.util.*,com.limin.blog.entity.Article" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>LM BLOG</title>
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/jquery-3.1.1.min.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/bootstrap.min.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/bootstrap.min.js"></script>
		
	</head>

	<body style="background-color: #A6E1EC">
		<!--<div class="text-primary text-center bg-primary h1">
			<strong>WELCOME TO LM BLOG</strong> 
		</div>-->
		
		<div class="top-content text-center" style="margin: auto;margin-top: 100px;">
        	
            <div class="inner-bg">
                <div class="container">
                    <div class="row bg-info" style="width: 70%; margin: auto;">
                        <div class="col-sm-6 col-sm-offset-3 form-box">
                        	<div class="form-top">
                       			<div class="h1">
									<strong>欢迎来到LM BLOG</strong> 
								</div>
                           		<p>
                           			${msg == null ? '请输入输入用户名和密码' : msg }
                           		</p>
                            </div>
                            <div class="form-bottom">
                            	<form:form action="${pageContext.request.contextPath }/user/loginhandler" modelAttribute="user" method="post" cssClass="login-form" role="form">
									${message }
                            		<div class="form-group">
                            			<form:input path="username"  cssClass="form-password form-control" placeholder="用户名"/>
                            		</div>
                            		<div class="form-group">
                            			<form:password path="password" cssClass="form-password form-control" placeholder="密码"/>
                            		</div>
                            		<div class="form-group">
			                        	<button type="submit" class="btn btn-danger btn-lg btn-block">登陆</button>
			                        </div>
                            	</form:form>
		                    </div>
                        </div>
                    </div>
                </div>
            </div>
            
        </div>

	</body>

</html>
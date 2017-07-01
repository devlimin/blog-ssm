<%@ page language="java" import="java.util.*,com.limin.blog.entity.Article" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>LM BLOG</title>
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/jquery-3.1.1.min.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/bootstrap.min.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/bootstrap.min.js"></script>
		<script type="text/javascript">
			function regist() {
				$("#usernameMsg").text("");
				$("#passwordMsg").text("");
				$("#repasswordMsg").text("");
				$("#emailMsg").text("");
				var username = $("#username").val();
				if($.trim(username) == "") {
					$("#usernameMsg").text("用户名不能为空！");
					return false;
				}
				var password = $("#password").val();
				if($.trim(password) == "") {
					$("#passwordMsg").text("密码不能为空！");
					return;
				}
				var repassword = $("#repassword").val();
				if($.trim(repassword) == "" || repassword != password) {
					$("#repasswordMsg").text("确认密码必须和密码相同！");
					return;
				}
				var email = $("#email").val();
				if($.trim(email) == "") {
					$("#emailMsg").text("邮箱不能为空！");
					return;
				}
				var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				if(!filter.test(email)) {
					$("#emailMsg").text("邮箱格式不正确！");
					return;
				}
				
				var url="${pageContext.request.contextPath }/user/uniquename";
				var data = "username="+username;
				$.ajax({
					type : "post",
					url : url,
					data : data,
					success : function(msg) {
						if(msg == "exist") {
							$("#usernameMsg").text("改用户名已经存在！");
							return;
						} else if(msg =="unique") {
							$("#registForm").submit();
						}
					}
				})
			}
		</script>
	</head>

	<body style="background-color: #A6E1EC">
		<div class="top-content text-center" style="margin: auto;margin-top: 100px;">
            <div class="inner-bg">
                <div class="container">
                    <div class="row bg-info" style="width: 70%; margin: auto;">
                        <div class="col-sm-6 col-sm-offset-3 form-box">
                        	<div class="form-top">
                       			<div class="h1">
									<strong>欢迎来到LM BLOG</strong> 
								</div>
                           		<p>请输入注册信息</p>
                            </div>
                            <div class="form-bottom">
                            	<form:form action="${pageContext.request.contextPath }/user/registhandler" id="registForm" modelAttribute="user" method="post" role="form" cssClass="login-form">
                            		<div class="form-group">
                            			<form:input id="username" path="username" placeholder="用户名" cssClass="form-username form-control"/>
                            			<span id="usernameMsg">
                            				<form:errors path="username"></form:errors>
                            			</span>
			                        </div>
			                        <div class="form-group">
			                        	<form:password  id="password" path="password" placeholder="密码" cssClass="form-password form-control"/>
                            			<span id="passwordMsg">
                            				<form:errors path="password"></form:errors>
                            			</span>
			                        </div>
			                        <div class="form-group">
			                        	<input type="password"  id="repassword" placeholder="确认密码" class="form-password form-control"/>
                            			<span id="repasswordMsg"></span>
			                        </div>
			                        <div class="form-group">
										<form:input id="email" path="email" placeholder="邮箱" cssClass="form-control" />
                            			<span id="emailMsg">
                            				<form:errors path="email"></form:errors>
                            			</span>
			                        </div>
			                        <div class="form-group">
			                        	<button type="button" onclick="regist();" class="btn btn-danger btn-lg btn-block">注册</button>
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